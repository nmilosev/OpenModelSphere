/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.screen.plugins;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbRoot;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.screen.DbLookupDialog;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.screen.model.ReflectionDescriptionModel;
import org.modelsphere.jack.baseDb.screen.model.ScreenModel;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.be.features.MergeDialog;
import org.modelsphere.sms.be.features.MergeUseCase;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSUserDefinedPackage;
import org.modelsphere.sms.international.LocaleMgr;

public class DbBEUseCaseExternalEditor extends JCheckBox implements
        org.modelsphere.jack.baseDb.screen.Editor {
    private static final String EXPLODED_PROCESS_CANNOT_EXTERNALIZE = LocaleMgr.message
            .getString("AnExploded0CannotBeExternal");
    private static final String EXTERNAL_CANNOT_BE_UNSET0 = LocaleMgr.message
            .getString("TheExternalStatusIsIrreversible0");
    private static final String EXTERNALIZATION_IS_IRREVERSIBLE = LocaleMgr.message
            .getString("ExternalizingA0IsIrreversible");
    private static final String SET_TO_EXTERNAL = LocaleMgr.message
            .getString("SetToExternalPermanently");
    private static final String LEAVE_TO_INTERNAL = LocaleMgr.message.getString("LeaveToInternal");

    org.modelsphere.jack.awt.AbstractTableCellEditor actionListener = null;

    public final java.awt.Component getTableCellEditorComponent(ScreenView screenView,
            org.modelsphere.jack.awt.AbstractTableCellEditor tableCellEditorListener, Object value,
            boolean isSelected, int row, int column) {
        setHorizontalAlignment(javax.swing.JLabel.CENTER);
        setSelected(value != null && ((Boolean) value).booleanValue());
        if (isSelected) {
            setBackground(screenView.getSelectionBackground());
            setForeground(screenView.getSelectionForeground());
        }
        actionListener = tableCellEditorListener;
        ScreenModel model = screenView.getModel();

        DbBEUseCase process = null;
        if (model instanceof ReflectionDescriptionModel) {
            ReflectionDescriptionModel m = (ReflectionDescriptionModel) model;
            if (m.getDbObject() instanceof DbBEUseCase) {
                process = (DbBEUseCase) m.getDbObject();
            }
        } // end if

        if (process != null) {
            final DbBEUseCase finalProcess = process;
            Boolean b = (Boolean) value;
            final boolean finalIsSelected = b.booleanValue();
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean isActionPossible = verifyAction(finalProcess, finalIsSelected);

                    if (isActionPossible) {
                        if (actionListener != null)
                            actionListener.actionPerformed(e);
                    } else {
                        setSelected(false);
                        actionListener.cancelCellEditing();
                    } // end if
                }
            });
        } // end if

        return this;
    }

    // Also called by MultiDbBEUseCaseExternalEditor
    static boolean verifyAction(DbBEUseCase process, boolean selected) {
        boolean isActionPossible = true;

        try {
            process.getDb().beginReadTrans();
            Terminology terminology = TerminologyUtil.getInstance().findModelTerminology(
                    process.getComposite());
            String term = terminology.getTerm(DbBEUseCase.metaClass);

            BEUtility util = BEUtility.getSingleInstance();
            DbBEDiagram diag = util.getExplodedDiagram(process);
            setMergeParameters(null, null);
            if (diag != null) {
                String msg = MessageFormat.format(EXPLODED_PROCESS_CANNOT_EXTERNALIZE,
                        new Object[] { term.toLowerCase() });
                JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(), msg,
                        ApplicationContext.getApplicationName(), JOptionPane.WARNING_MESSAGE);
                isActionPossible = false;
            } else {
                if (selected) {
                    String MERGE_NOW = LocaleMgr.message.getString("MergeNow");
                    String CANCEL = LocaleMgr.screen.getString("Cancel");
                    String msg = MessageFormat.format(EXTERNAL_CANNOT_BE_UNSET0,
                            new Object[] { term.toLowerCase() });
                    int result = JOptionPane.showOptionDialog(ApplicationContext
                            .getDefaultMainFrame(), msg, ApplicationContext.getApplicationName(),
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
                            new String[] { MERGE_NOW, CANCEL }, CANCEL);

                    if (result == 0) {
                        result = mergeNow(process);
                    }

                    isActionPossible = (result == 0);
                } else {
                    String YES = LocaleMgr.screen.getString("Yes");
                    String NO = LocaleMgr.screen.getString("No");
                    String msg = MessageFormat.format(EXTERNALIZATION_IS_IRREVERSIBLE,
                            new Object[] { term.toLowerCase() });
                    int result = JOptionPane.showOptionDialog(ApplicationContext
                            .getDefaultMainFrame(), msg, ApplicationContext.getApplicationName(),
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
                            new String[] { YES, NO }, NO);

                    isActionPossible = (result == 0);
                } // end if
            } // end if

            process.getDb().commitTrans();

        } catch (DbException ex) {
            isActionPossible = false;
        } // end try

        if ((g_chosenContext != null) && (g_externalProcess != null)) {
            int result = 1; // cancel
            DbBEUseCase selection = MergeDialog.selectOptions(g_chosenContext, g_externalProcess); // static
            // variables
            // update,
            // mergeQualifiers,
            // mergeResources
            // and
            // mergeComments
            // are
            // set

            if (selection != null) {
                new MergeUseCase(g_chosenContext, g_externalProcess, MergeDialog.update,
                        MergeDialog.mergeQualifiers, MergeDialog.mergeResources,
                        MergeDialog.mergeComments);
                result = 0;
            }

            isActionPossible = (result == 0);
        } // end if

        return isActionPossible;
    } // end verifyAction

    private static int mergeNow(final DbBEUseCase process) throws DbException {
        int result;

        // find all other contexts
        DbBEUseCase context = findContextOf(process);
        final ArrayList contextList = getAllContexts(context);

        if (contextList.isEmpty()) {
            String msg = LocaleMgr.screen.getString("YourProjectMustContainAtLeastOneOtherContext");
            JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(), msg,
                    ApplicationContext.getApplicationName(), JOptionPane.INFORMATION_MESSAGE);
            result = 1; // cancel
        } else {
            String title = LocaleMgr.screen.getString("SelectContext");
            process.getDb().beginReadTrans();
            DefaultComparableElement elem = DbLookupDialog.selectDbo(ApplicationContext
                    .getDefaultMainFrame(), title, (Point) null, process.getDb(), contextList,
                    (DbBEUseCase) contextList.get(0), null, true);
            // DbLookupDialog.selectDbo closes the read transaction

            if (elem != null) {
                DbBEUseCase chosenContext = (DbBEUseCase) elem.object;
                setMergeParameters(chosenContext, process);
                result = 0;
            } else {
                result = 1; // cancel
            } // end if
        } // end if

        return result;
    } // end mergeNow()

    private static DbBEUseCase findContextOf(DbBEUseCase process) throws DbException {
        while (!process.isContext()) {
            process = (DbBEUseCase) process.getCompositeOfType(DbBEUseCase.metaClass);

            if (process == null)
                break;
        }

        return process;
    } // end findContextOf

    private static ArrayList getAllContexts(DbBEUseCase context) throws DbException {
        ArrayList contextList = new ArrayList();

        DbRoot root = (DbRoot) context.getCompositeOfType(DbRoot.metaClass);
        DbRelationN relN = root.getComponents();
        DbEnumeration dbEnum = relN.elements(DbSMSProject.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSProject proj = (DbSMSProject) dbEnum.nextElement();
            getAllContextsOfComposite(contextList, proj, context);
        } // end while
        dbEnum.close();

        return contextList;
    } // end getAllContexts()

    private static void getAllContextsOfComposite(ArrayList contextList,
            DbSemanticalObject composite, DbBEUseCase originalContext) throws DbException {
        DbRelationN relN = composite.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEModel.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEModel model = (DbBEModel) dbEnum.nextElement();
            getAllContextsOfModel(contextList, model, originalContext);
        } // end while
        dbEnum.close();

        dbEnum = relN.elements(DbSMSUserDefinedPackage.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSUserDefinedPackage pack = (DbSMSUserDefinedPackage) dbEnum.nextElement();
            getAllContextsOfComposite(contextList, composite, originalContext);
        } // end while
        dbEnum.close();
    } // end getAllContextsOfComposite()

    private static void getAllContextsOfModel(ArrayList contextList, DbBEModel model,
            DbBEUseCase originalContext) throws DbException {
        DbRelationN relN = model.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEUseCase.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEUseCase context = (DbBEUseCase) dbEnum.nextElement();

            if ((context.isContext()) && !context.equals(originalContext)) {
                contextList.add(context);
            }
        } // end while
        dbEnum.close();
    } // end getAllContextsOfModel()

    //
    // Merge parameters
    //
    private static DbBEUseCase g_chosenContext;
    private static DbBEUseCase g_externalProcess;

    private static void setMergeParameters(DbBEUseCase chosenContext, DbBEUseCase externalProcess) {
        g_chosenContext = chosenContext;
        g_externalProcess = externalProcess;
    } // end setMergeParameters()

    public final boolean stopCellEditing() {
        actionListener = null;
        return true;
    }

    public final Object getCellEditorValue() {
        return (isSelected() ? Boolean.TRUE : Boolean.FALSE);
    }

    @Override
    public int getClickCountForEditing() {
        return 2;
    }
}
