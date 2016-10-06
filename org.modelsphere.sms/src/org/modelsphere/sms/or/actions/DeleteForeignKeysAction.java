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
package org.modelsphere.sms.or.actions;

import java.text.MessageFormat;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORFKeyColumn;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.util.ForeignKey;
import org.modelsphere.sms.or.db.util.PrimaryKey;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.or.screen.DeleteKeysAndRulesFrame;
import org.modelsphere.sms.or.screen.GeneratePrimaryKeysFrame;
import org.modelsphere.sms.or.screen.DeleteKeysAndRulesFrame.DeleteKeysOptions;

public final class DeleteForeignKeysAction extends AbstractApplicationAction implements
        SelectionActionListener {

    private static final String kDelKeysAndReferentialRules = LocaleMgr.action
            .getString("delKeysAndReferentialRules");
    public static String kNotifyNoChange = LocaleMgr.action.getString("notifyNoChange");

    public static enum KeyAndRules {
        PK, UK, FK, RULES
    };

    private TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    DeleteForeignKeysAction() {
        super(kDelKeysAndReferentialRules + "...");
        this.setMnemonic(LocaleMgr.action.getMnemonic("delKeysAndReferentialRules"));
        setEnabled(false);
        setVisible(ScreenPerspective.isFullVersion());
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] activeObjs = ORActionFactory.getActiveObjects();

        boolean enabled = false;

        if (activeObjs != null) {
            enabled = true;

            if (activeObjs[0] instanceof DbORDataModel) {
                DbORDataModel dataModel = (DbORDataModel) activeObjs[0];
                if (terminologyUtil.getModelLogicalMode(dataModel) == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    enabled = false;
            } else {
                DbObject dataModel = terminologyUtil.isCompositeDataModel(activeObjs[0]);
                if (dataModel == null)
                    enabled = false;
                else if (terminologyUtil.getModelLogicalMode(dataModel) == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    enabled = false;
            }
        }
        setEnabled(enabled);
    }

    protected final void doActionPerformed() {
        try {
            DbObject[] activeObjs = ORActionFactory.getActiveObjects();
            DbObject obj = activeObjs[0];
            DbORDataModel dataModel;

            //get data model
            obj.getDb().beginReadTrans();
            if (obj instanceof DbORDataModel) {
                dataModel = ((DbORDataModel) obj);
            } else {
                dataModel = (DbORDataModel) obj.getCompositeOfType(DbORDataModel.metaClass);
            }
            obj.getDb().commitTrans();

            if (dataModel == null)
                return;

            //get number of keys and rules
            int[] occurrences = countOccurences(dataModel);

            //open dialog
            JFrame mainFrame = ApplicationContext.getDefaultMainFrame();
            String title = kDelKeysAndReferentialRules;
            DeleteKeysAndRulesFrame dialog = new DeleteKeysAndRulesFrame(mainFrame, title,
                    occurrences);
            dialog.setVisible(true);

            //perform action
            if (!dialog.hasCancelled()) {
                DeleteKeysAndRulesFrame.DeleteKeysOptions options = dialog.getOptions();
                deleteKeysAndRules(dataModel, options);
            } //end if

        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        } //end try
    } //end doActionPerformed()

    private void deleteKeysAndRules(DbORDataModel dataModel, DeleteKeysOptions options)
            throws DbException {
        dataModel.getDb().beginTrans(Db.WRITE_TRANS, kDelKeysAndReferentialRules);
        int nbPUKs = 0;
        int nbFKs = 0;
        int nbRules = 0;

        if (options.deletePrimaryKeys || options.deleteUniqueKeys) {
            nbPUKs = PrimaryKey.deleteKeys(dataModel, options);
        }

        if (options.deleteForeignKeys) {
            nbFKs = ForeignKey.deleteForeignKeys(dataModel, options);
        }

        if (options.deleteRules) {
            nbRules = deleteReferentialRules(dataModel, options);
        }

        //if at least one object deleted 
        if ((nbPUKs + nbFKs + nbRules) > 0) {
            String pattern = LocaleMgr.action.getString("01ObjectsDeleted");
            String text = "";

            if (nbPUKs > 0) {
                String dboname = DbORPrimaryUnique.metaClass.getGUIName(true).toLowerCase();
                String msg = MessageFormat.format(pattern, nbPUKs, dboname);
                text += msg + "\n";
            }

            if (nbFKs > 0) {
                String dboname = DbORForeign.metaClass.getGUIName(true).toLowerCase();
                String msg = MessageFormat.format(pattern, nbFKs, dboname);
                text += msg + "\n";
            }

            if (nbRules > 0) {
                String dboname = LocaleMgr.action.getString("ReferentialRules");
                String msg = MessageFormat.format(pattern, nbRules, dboname);
                text += msg + "\n";
            }

            JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(), text,
                    ApplicationContext.getApplicationName(), JOptionPane.INFORMATION_MESSAGE);
        } //end if

        dataModel.getDb().commitTrans();
    }

    private int[] countOccurences(DbORDataModel dataModel) throws DbException {
        int[] occurrences = new int[KeyAndRules.values().length];
        dataModel.getDb().beginReadTrans();

        DbEnumeration dbEnum = dataModel.getComponents().elements(DbORAbsTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbEnumeration enum2 = dbEnum.nextElement().getComponents().elements();
            while (enum2.hasMoreElements()) {
                DbObject dbo = enum2.nextElement();

                if (dbo instanceof DbORPrimaryUnique) {
                    boolean primary = ((DbORPrimaryUnique) dbo).isPrimary();
                    int idx = primary ? KeyAndRules.PK.ordinal() : KeyAndRules.UK.ordinal();
                    occurrences[idx]++;
                } else if (dbo instanceof DbORForeign) {
                    occurrences[KeyAndRules.FK.ordinal()]++;
                }
            }
            enum2.close();
        }
        dbEnum.close();

        dbEnum = dataModel.getComponents().elements(DbORAssociation.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORAssociation assoc = (DbORAssociation) dbEnum.nextElement();
            DbORAssociationEnd backEnd = assoc.getBackEnd();
            DbORAssociationEnd frontEnd = assoc.getFrontEnd();
            int nbRules = countReferentialRules(backEnd, frontEnd);
            occurrences[KeyAndRules.RULES.ordinal()] += nbRules;
        }
        dbEnum.close();

        dataModel.getDb().commitTrans();

        return occurrences;
    }

    private int countReferentialRules(DbORAssociationEnd backEnd, DbORAssociationEnd frontEnd)
            throws DbException {
        DbORAssociationEnd parentSide = findParentSide(backEnd, frontEnd);

        int nbRules = 0;
        if (parentSide == null) {
            //no referential rules for one-to-one or many-to-many associations
            return nbRules;
        }

        if (parentSide.getInsertRule() != null) {
            nbRules++;
        }
        if (parentSide.getUpdateRule() != null) {
            nbRules++;
        }
        if (parentSide.getDeleteRule() != null) {
            nbRules++;
        }

        DbORAssociationEnd childSide = parentSide.getOppositeEnd();
        if (childSide.getInsertRule() != null) {
            nbRules++;
        }
        if (childSide.getUpdateRule() != null) {
            nbRules++;
        }
        if (childSide.getDeleteRule() != null) {
            nbRules++;
        }

        return nbRules;
    } //end countReferentialRules()

    private int deleteReferentialRules(DbORDataModel dataModel, DeleteKeysOptions options)
            throws DbException {
        int nbRules = 0;
        DbEnumeration dbEnum = dataModel.getComponents().elements(DbORAssociation.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORAssociation assoc = (DbORAssociation) dbEnum.nextElement();
            DbORAssociationEnd backEnd = assoc.getBackEnd();
            DbORAssociationEnd frontEnd = assoc.getFrontEnd();
            nbRules += deleteReferentialRules(backEnd, frontEnd, options);
        }
        dbEnum.close();
        return nbRules;
    }

    private int deleteReferentialRules(DbORAssociationEnd backEnd, DbORAssociationEnd frontEnd,
            DeleteKeysOptions options) throws DbException {
        int nbRules = 0;
        DbORAssociationEnd parentSide = findParentSide(backEnd, frontEnd);

        if (parentSide == null) {
            //no referential rules for one-to-one or many-to-many associations
            return nbRules;
        }

        DbORAssociationEnd childSide = parentSide.getOppositeEnd();

        if (options.deleteInsertRules) {
            nbRules += parentSide.getInsertRule() == null ? 0 : 1;
            nbRules += childSide.getInsertRule() == null ? 0 : 1;

            parentSide.setInsertRule(null);
            childSide.setInsertRule(null);
        }

        if (options.deleteUpdateRules) {
            nbRules += parentSide.getUpdateRule() == null ? 0 : 1;
            nbRules += childSide.getUpdateRule() == null ? 0 : 1;

            parentSide.setUpdateRule(null);
            childSide.setUpdateRule(null);
        }

        if (options.deleteDeleteRules) {
            nbRules += parentSide.getDeleteRule() == null ? 0 : 1;
            nbRules += childSide.getDeleteRule() == null ? 0 : 1;

            parentSide.setDeleteRule(null);
            childSide.setDeleteRule(null);
        }

        return nbRules;
    } //end deleteReferentialRules()

    //find parent side according its multiplicity
    private DbORAssociationEnd findParentSide(DbORAssociationEnd backEnd,
            DbORAssociationEnd frontEnd) throws DbException {
        DbORAssociationEnd parentSide = null;
        int backMult = backEnd.getMultiplicity().getValue();
        int frontMult = frontEnd.getMultiplicity().getValue();

        boolean backOne = (backMult == SMSMultiplicity.OPTIONAL)
                || (backMult == SMSMultiplicity.EXACTLY_ONE);
        boolean frontOne = (frontMult == SMSMultiplicity.OPTIONAL)
                || (frontMult == SMSMultiplicity.EXACTLY_ONE);

        if (backOne && !frontOne) {
            parentSide = frontEnd;
        } else if (!backOne && frontOne) {
            parentSide = backEnd;
        }

        return parentSide;
    }

    protected int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }
} //end DeleteForeignKeysAction

