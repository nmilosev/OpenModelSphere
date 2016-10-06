/*************************************************************************

Copyright (C) 2009 Grandite

This file is part of Open ModelSphere.

Open ModelSphere is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.jack.srtool.reverse.file;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JDialog;

import org.modelsphere.jack.awt.JackOptionPane;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.GuiController;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class ReverseWorker extends Worker {
    private static final String DONT_KNOW_HOW_TO_REVERSE_MSG = LocaleMgr.message
            .getString("DoNotKnowHowToReverse0UnknownFileExtension");
    private static final String REVERSE_ENGINEERING = LocaleMgr.screen
            .getString("ReverseEngineering");
    private static final String FILES_REVERSED = LocaleMgr.message
            .getString("{0}FileOn{1}ReversedIn{2}");
    private static final String PROCEED_MESSAGE = LocaleMgr.message
            .getString("ProceedReverseEngineeringMsg");
    private static final String PROCEED_TITLE = LocaleMgr.screen
            .getString("ProceedReverseEngineering");
    private static final String PROCEED = LocaleMgr.screen.getString("proceed");
    private static final String CANCEL = LocaleMgr.screen.getString("cancel");

    private static final Object[] options = { PROCEED, CANCEL };

    // Reverse enginnering in a single transactions until files exceed 2 megs
    // (to put in options?)
    private static final int MAX_SIZE_FOR_A_SINGLE_TRANSACTION = 2 * 1024 * 1024;

    ReverseOptions m_options;

    public ReverseWorker(ReverseOptions options) {
        m_options = options;
    }

    protected void runJob() throws Exception {
        Vector fileVector = m_options.m_fileVector;
        UniqueFileVector uniqueFileVector = new UniqueFileVector(fileVector);
        int totalReverseCount = uniqueFileVector.size();
        Enumeration enumeration = uniqueFileVector.elements();
        ArrayList reverseList = getInstantiatedReverseList();
        Hashtable modelTable = new Hashtable();
        Vector obsoleteObjectVector = new Vector();
        HashMap diagMap = new HashMap(); // set to null if we do not want
        // graphics
        StringWriter logBuffer = new StringWriter();
        int successfulReverseCount = 0;
        boolean atLeastOneError = false;
        GuiController controller = (GuiController) getController();
        DbProject project = m_options.m_proj;
        AbstractReverser reverser = m_options.m_reverser;
        long cumulativeSize = 0;
        JDialog dialog = controller.getDialog();
        long totalSize = uniqueFileVector.getTotalSize();
        sortAccordingPriority(uniqueFileVector, reverseList);

        int singleTransaction = shouldBeSingleTransaction(dialog, totalSize);
        Db db = project.getDb();
        if (singleTransaction == ABORT_TRANSACTION) {
            return;
        } else if (singleTransaction == SINGLE_TRANSACTION) {
            String transactionName = REVERSE_ENGINEERING;
            db.beginTrans(Db.WRITE_TRANS, transactionName);
        } // end if

        DbObject pack = null;
        DbObject newlyCreatedPackage = null;

        while (enumeration.hasMoreElements()) {
            File file = (File) enumeration.nextElement();
            JackReverseEngineeringPlugin reverse = getReverseForFile(reverseList, file);

            // if we know how to reverse the file
            if (reverse != null) {
                Class modelClaz = reverse.getKindOfModel();

                // Call reverse.reverseCurrentFile() with a
                // dedicatedLogBuffer; if dedicatedLogBuffer not empty after
                // the reverse, then write filename if logBuffer, followed
                // by the contents of dedicatedLogBuffer. If successful, display
                // the warnings.
                String filename = file.getAbsolutePath();
                String reverseName = reverse.getSignature().getName();
                controller.println(filename + " ( " + reverseName + ") ..");
                reverser.setPartialProgression(controller, cumulativeSize, totalSize);

                // if class already in hash table, then get its package
                // otherwise, create an instance of the package
                // and put the package and its instance in hash table

                if ((modelClaz != null) && modelTable.containsKey(modelClaz)) {
                    pack = (DbObject) modelTable.get(modelClaz);
                } else {
                    newlyCreatedPackage = reverse.createNewPackage(modelClaz, project);
                    pack = newlyCreatedPackage;
                    modelTable.put(modelClaz, pack);
                } // end if

                String errMsg = reverser.reverseCurrentFile(pack, reverse, // modelClaz,
                        obsoleteObjectVector, diagMap, file, project, logBuffer);

                if (errMsg == null) {
                    successfulReverseCount++;
                    cumulativeSize += file.length();
                    int progression = computeProgression(cumulativeSize, totalSize); // returns a number between 1 and 100
                    controller.checkPoint(progression);
                } else {
                    atLeastOneError = true;
                    String pattern = "{0}:\n{1}\n"; // NOT LOCALIZABLE
                    String message = MessageFormat.format(pattern, new Object[] {
                            file.getAbsolutePath(), errMsg });
                    logBuffer.write(message);
                    controller.incrementWarningsCounter();
                } // end if
            } else { // we don't know how to reverse the file
                atLeastOneError = false;
                String message = MessageFormat.format(DONT_KNOW_HOW_TO_REVERSE_MSG,
                        new Object[] { file.getName() });
                logBuffer.write(message);
            } // end if

            if (!controller.checkPoint()) { // if user has CANCELled
                break;
            }
        } // end while

        if (pack != null)
            reverser.preparePackageForDisplay(pack);

        reverser.clearObjects(obsoleteObjectVector, project);
        db.beginTrans(Db.READ_TRANS);
        String name = project.getName();
        db.commitTrans();
        terminate(instantiatedReverseList, successfulReverseCount, totalReverseCount, logBuffer,
                name);
        int state = controller.getState();
        boolean failed = ((state == Controller.STATE_ABORTING)
                || (state == Controller.STATE_ABORTED) || (state == Controller.STATE_ERROR)) ? true
                : false;

        if (singleTransaction == SINGLE_TRANSACTION) {
            if (failed) {
                db.abortTrans();
            } else {
                db.commitTrans();
            }
        } else {
            // remove any new package if processing was not successful
            if (failed) {
                if (newlyCreatedPackage != null) {
                    db.beginTrans(Db.WRITE_TRANS);
                    newlyCreatedPackage.remove();
                    db.commitTrans();
                } // end if
            } // end if
            db.resetHistory();
        } // end if
    } // end runJob()

    // Return this job's title
    protected String getJobTitle() {
        return REVERSE_ENGINEERING;
    } // getJobTitle()

    //
    // PRIVATE METHODS
    //
    private static final int SINGLE_TRANSACTION = 0;
    private static final int MULTIPLE_TRANSACTIONS = 1;
    private static final int ABORT_TRANSACTION = 2;

    private int shouldBeSingleTransaction(Component owner, long totalSize) {
        int singleTransaction = MULTIPLE_TRANSACTIONS;

        if (totalSize < MAX_SIZE_FOR_A_SINGLE_TRANSACTION) {
            singleTransaction = SINGLE_TRANSACTION;
        } else {
            // ask user
            boolean cancel = askIfUserWantsToProceed(owner);
            if (cancel) {
                singleTransaction = ABORT_TRANSACTION;
            } // end if
        } // end if

        return singleTransaction;
    } // end of shouldBeSingleTransaction()

    private boolean askIfUserWantsToProceed(Component owner) {
        boolean cancel;

        int option = JackOptionPane.showOptionDialog(owner, PROCEED_MESSAGE, PROCEED_TITLE,
                JackOptionPane.DEFAULT_OPTION, JackOptionPane.WARNING_MESSAGE, null, options,
                options[0]);

        cancel = (option == 0) ? false : true;
        return cancel;
    }

    // returns a number between 1 and 100
    private int computeProgression(long cumulativeSize, long totalSize) {
        int progression = (int) (((double) cumulativeSize / (double) totalSize) * 99.0 + 1.0);
        return progression;
    } // end computeProgression()

    // Instantiate reverse only once ArrayList getReverseList()
    private ArrayList instantiatedReverseList = null;

    protected ArrayList getInstantiatedReverseList() {
        if (instantiatedReverseList == null) {
            instantiatedReverseList = new ArrayList();
            Iterator iter = m_options.m_reverseList.iterator();
            while (iter.hasNext()) {
                Class claz = (Class) iter.next();
                try {
                    JackReverseEngineeringPlugin reverse = (JackReverseEngineeringPlugin) claz
                            .newInstance();
                    instantiatedReverseList.add(reverse);
                } catch (IllegalAccessException ex) {
                    // try next reverse
                } catch (InstantiationException ex) {
                    // try next reverse
                }
            } // end while
        } // end if

        return instantiatedReverseList;
    } // end getInstantiatedReverseList()

    private JackReverseEngineeringPlugin getReverseForFile(ArrayList reverseList, File file) {
        JackReverseEngineeringPlugin reverse = null;

        Iterator iter = reverseList.iterator();
        try {
            while (iter.hasNext()) {
                reverse = (JackReverseEngineeringPlugin) iter.next();

                if (reverse.canReverse(file)) {
                    Class modelClaz = reverse.getKindOfModel();
                    if (modelClaz != null) {
                        break;
                    }
                }
            } // end while
        } catch (IOException ex) {
            reverse = null;
        }

        return reverse;
    } // end getReverseForFile()

    // Sort files according the priority of their reverse module (for example,
    // if ClassReverse has a higher priority than JavaReverse, .class files
    // will be reversed before .java files)
    protected void sortAccordingPriority(Vector uniqueFileVector, final ArrayList reverseList) {
        // use a hashtable to associate a file with a reverse module
        final Hashtable table = new Hashtable();

        class PriorityComparator implements Comparator {
            public int compare(Object file1, Object file2) {
                int comparation = 0;
                try {
                    // already in the table?
                    JackReverseEngineeringPlugin reverse1 = (JackReverseEngineeringPlugin) table
                            .get(file1);

                    // if not found
                    if (reverse1 == null) {
                        reverse1 = getRelevantReverse((File) file1, reverseList);
                        table.put(file1, reverse1);
                    }

                    // already in the table?
                    JackReverseEngineeringPlugin reverse2 = (JackReverseEngineeringPlugin) table
                            .get(file2);

                    // if not found
                    if (reverse2 == null) {
                        reverse2 = getRelevantReverse((File) file2, reverseList);
                        table.put(file2, reverse2);
                    }

                    if ((reverse1 != null) && (reverse2 != null)) {
                        comparation = reverse1.getPriority() - reverse2.getPriority();
                    }
                } catch (IOException ex) {
                    // not able to find out which reverse has the higher
                    // priority, return 0
                }

                return comparation;
            }
        }
    } // end sortAccordingPriority()

    private JackReverseEngineeringPlugin getRelevantReverse(File file, ArrayList reverseList)
            throws IOException {
        JackReverseEngineeringPlugin reverse = null;

        Iterator iter = reverseList.iterator();
        while (iter.hasNext()) {
            Class claz = (Class) iter.next();
            try {
                JackReverseEngineeringPlugin r = (JackReverseEngineeringPlugin) claz.newInstance();

                if (r.canReverse(file)) {
                    reverse = r;
                    break;
                }
            } catch (IllegalAccessException ex) {
                // ignore and continue with the next reverse
            } catch (InstantiationException ex) {
                // ignore and continue with the next reverse
            }
        } // end while

        return reverse;
    } // getRelevantReverse()

    // Normaly terminated
    final private void terminate(ArrayList reverseList, int successfulReverseCount,
            int totalReverseCount, StringWriter logBuffer, String name) {
        Controller controller = getController();
        String log = logBuffer.toString();

        // if warning messages in log buffer
        if (log.length() != 0) {
            controller.println(log);
        }

        if (successfulReverseCount > 0) {
            String reverseCountMessage = java.text.MessageFormat.format(FILES_REVERSED,
                    new Object[] { new Integer(successfulReverseCount),
                            new Integer(totalReverseCount), name });
            controller.println(reverseCountMessage);
        } // end if
    } // end terminate()

    //
    // INNER CLASSES
    //
    // Inner class UniqueFileVector
    public static class UniqueFileVector extends Vector {
        long m_totalSize = 0;

        UniqueFileVector(Vector fileVector) {
            Enumeration enumeration = fileVector.elements();
            while (enumeration.hasMoreElements()) {
                File file = (File) enumeration.nextElement();
                this.addUniqueElement(file);
            } // end UniqueFileVector()
        } // end UniqueFileVector

        // returns true if element added
        boolean addUniqueElement(File aFile) {
            boolean alreadyThere = false;

            // don't add in file vector if it's not really a file..
            if (aFile.isDirectory()) {
                return false;
            }

            for (int i = 0; i < size(); i++) {
                File file = (File) elementAt(i);
                if (file.getName().equals(aFile.getName())) {
                    alreadyThere = true;
                    break;
                }
            } // end for

            if (!alreadyThere) {
                addElement(aFile);
                m_totalSize += aFile.length();
            }

            return !alreadyThere;
        } // end addUniqueElement()

        long getTotalSize() {
            return m_totalSize;
        }

    } // end class UniqueFileVector
} // end ReverseWorker
