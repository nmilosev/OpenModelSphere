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

package org.modelsphere.jack.srtool.forward;

import java.util.*;

import org.modelsphere.jack.gui.task.*;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

public abstract class ForwardWorker extends Worker {
    private static final String WARNING_ONE_FILE_DUPLICATED = LocaleMgr.message
            .getString("WARNING_ONE_FILE_DUPLICATED");
    private static final String WARNING_SOME_FILES_DUPLICATED = LocaleMgr.message
            .getString("WARNING_SOME_FILES_DUPLICATED");
    private static final String SOME_FILES_MAY_HAVE_BEEN_OVERWRITTEN = LocaleMgr.message
            .getString("SOME_FILES_MAY_HAVE_BEEN_OVERWRITTEN");
    private static NameComparator comparator = new NameComparator();

    // Check if some files have been overwritten by other ones in the generation
    // process
    private int checkDuplicates(ArrayList generatedFiles) {
        // sort them on their names
        int len = generatedFiles.size();
        Object[] fileArray = generatedFiles.toArray();
        Arrays.sort(fileArray, comparator);

        // check if there are two consecutive files with the same name
        int nbDuplicates = 0;
        for (int i = 0; i < (len - 1); i++) {
            int comparison = comparator.compare(fileArray[i], fileArray[i + 1]);
            if (comparison == 0) {
                nbDuplicates++;
            }
        } // end for

        return nbDuplicates;
    }

    // Display how many files have been generated
    protected void terminateRunJob(JackForwardEngineeringPlugin forward, ArrayList generatedFiles) {
        // get controller
        Controller controller = getController();
        String message;

        // check if they are duplicates in generatedFiles
        int nbDuplicates = checkDuplicates(generatedFiles);
        if (nbDuplicates > 0) {
            if (nbDuplicates == 1) {
                message = WARNING_ONE_FILE_DUPLICATED;
                controller.println(message);
            } else {
                String pattern = WARNING_SOME_FILES_DUPLICATED;
                message = MessageFormat.format(pattern, new Object[] { new Integer(nbDuplicates) });
                controller.println(message);
            } // end if

            message = SOME_FILES_MAY_HAVE_BEEN_OVERWRITTEN;
            controller.println(message);
        } // end if

        message = forward.getFeedBackMessage(generatedFiles);
        controller.println();
        controller.println(message);
    } // terminateRunJob()

    // INNER CLASS(ES)
    private static final class NameComparator implements Comparator {
        NameComparator() {
        }

        public int compare(Object o1, Object o2) {
            String s1 = (String) o1;
            String s2 = (String) o2;

            int comparison = s1.compareTo(s2);
            return comparison;
        } // end compare()
    } // end NameComparator

    private static void test() {
        String s1 = "D:/java/sms/java_gen/Table.sql"; // NOT LOCALIZABLE test
        String s2 = "D:/java/sms/java_gen/Table5.sql"; // NOT LOCALIZABLE test

        int comparison = s1.compareTo(s2);
        System.out.println("comparison = " + comparison); // NOT LOCALIZABLE
        // test
    }

    /*
     * public static final void main(String[] args) { try { test(); } catch (Exception ex) {
     * System.out.println(ex.toString()); }
     * 
     * System.out.println("Press ENTER to quit."); byte[] buf = new byte[256]; try {
     * System.in.read(buf, 0, 256); } catch (java.io.IOException ex) { } } //end main()
     */
} // end ForwardWorker
