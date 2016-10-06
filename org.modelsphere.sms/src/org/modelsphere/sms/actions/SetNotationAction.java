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

package org.modelsphere.sms.actions;

import java.util.ArrayList;

import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSNotation;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORNotation;

public final class SetNotationAction extends AbstractDomainAction implements
        SelectionActionListener {

    public static final String kNotation = LocaleMgr.action.getString("notation");
    public static final String kSetNotation = LocaleMgr.action.getString("setNotation");

    public static final String kProjectDefaultNotation = LocaleMgr.action
            .getString("ProjectDefaultNotation");

    private TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    private ArrayList dbos = new ArrayList(0);

    private boolean m_bDiagramIsUML = false;

    public SetNotationAction() {
        super(kNotation);
        this.setMnemonic(LocaleMgr.action.getMnemonic("notation"));
        setEnabled(false);
        setVisible(ScreenPerspective.isFullVersion());
    }

    protected final void doActionPerformed() {
        int selidx = getSelectedIndex();
        if (selidx != -1) {
            DbSMSNotation notation = null;
            if (m_bDiagramIsUML)
                notation = (DbSMSNotation) dbos.get(selidx);
            else if (selidx != 0)
                notation = (DbSMSNotation) dbos.get(selidx - 2);

            ApplicationDiagram appDiag = ApplicationContext.getFocusManager().getActiveDiagram();
            DbSMSDiagram diagGo = (DbSMSDiagram) appDiag.getDiagramGO();

            DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
            if (diagGo instanceof DbORDiagram || diagGo instanceof DbBEDiagram) {
                try {
                    Db db = diagGo.getDb();
                    db.beginTrans(Db.WRITE_TRANS, kSetNotation);
                    DbSMSDiagram diagram = (DbSMSDiagram) diagGo;
                    if (diagram instanceof DbORDiagram) {
                        ((DbORDiagram) diagram).setNotation((DbORNotation) notation);
                    } else if (diagram instanceof DbBEDiagram) {
                        DbBEModel model = (DbBEModel) diagram
                                .getCompositeOfType(DbBEModel.metaClass);
                        DbSMSProject project = (DbSMSProject) ApplicationContext.getFocusManager()
                                .getCurrentProject();
                        DbObject obj = project.findComponentByName(DbBENotation.metaClass, model
                                .getTerminologyName());
                        ((DbBEDiagram) diagram).setNotation((DbBENotation) notation);

                    } // end if

                    DbObject comp = diagGo.getComposite();
                    if (!(comp instanceof DbORCommonItemModel)
                            && !(comp instanceof DbORDomainModel)) {
                        DbSMSStyle defaultStyle = (notation == null) ? null : notation
                                .getDefaultStyle();
                        if (defaultStyle != null) {
                            diagram.setStyle(defaultStyle);
                        }
                    }

                    db.commitTrans();

                    ApplicationContext.getDefaultMainFrame().getExplorerPanel().getExplorer()
                            .refresh();

                } catch (DbException e) {
                    org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                            ApplicationContext.getDefaultMainFrame(), e);
                } // end try
            } // end if
        } // end if
    } // end doActionPerformed()

    public final void updateSelectionAction() throws DbException {
        ApplicationDiagram diag = ApplicationContext.getFocusManager().getActiveDiagram();
        int modelLogicalMode = 0;
        String defaultItemName = null;

        if (diag == null) {
            setSelectedIndex(-1);
            setEnabled(false);
            return;
        }

        DbSMSDiagram diagram = (DbSMSDiagram) diag.getDiagramGO();
        if (!(diagram instanceof DbORDiagram) && !(diagram instanceof DbBEDiagram)) {
            setSelectedIndex(-1);
            setEnabled(false);
            return;
        }

        DbProject project = ApplicationContext.getFocusManager().getCurrentProject();
        DbSMSNotation selnotation = null;
        diagram.getDb().beginReadTrans();
        if (diagram instanceof DbORDiagram)
            selnotation = ((DbORDiagram) diagram).getNotation();
        else
            selnotation = ((DbBEDiagram) diagram).getNotation();
        diagram.getDb().commitTrans();

        int selidx = -1;
        dbos.clear();

        DbEnumeration dbEnum = null;
        if (diagram instanceof DbORDiagram) {
            diagram.getDb().beginReadTrans();
            DbObject model = diagram.getComposite();
            diagram.getDb().commitTrans();

            modelLogicalMode = terminologyUtil.getModelLogicalMode(model);
            dbEnum = project.getComponents().elements(DbORNotation.metaClass);
            DbORNotation notaErProj = ((DbSMSProject) project).getErDefaultNotation();
            DbORNotation notaOrProj = ((DbSMSProject) project).getOrDefaultNotation();
            if (notaErProj != null && notaOrProj != null) {
                String name = (modelLogicalMode == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP ? notaErProj
                        .getName()
                        : notaOrProj.getName());
                defaultItemName = kProjectDefaultNotation + " (" + name + ")";
            } else
                defaultItemName = (modelLogicalMode == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP ? kProjectDefaultNotation
                        : kProjectDefaultNotation);
        } else {
            dbEnum = project.getComponents().elements(DbBENotation.metaClass);
            DbBENotation notaBEProj = ((DbSMSProject) project).getBeDefaultNotation();
            if (notaBEProj != null) {
                String name = notaBEProj.getName();
                if (notaBEProj != null)
                    defaultItemName = kProjectDefaultNotation + " (" + name + ")";
                else
                    defaultItemName = kProjectDefaultNotation;
            } else
                defaultItemName = kProjectDefaultNotation;
        }

        int masterNotationID = 0;
        m_bDiagramIsUML = false;
        DbBEModel beModel = (DbBEModel) diagram.getCompositeOfType(DbBEModel.metaClass);
        if (beModel != null) {
            DbBENotation diagramMasterNotation = ((DbBEDiagram) diagram).findMasterNotation();
            masterNotationID = diagramMasterNotation.getNotationID().intValue();
            if (masterNotationID >= 13 && masterNotationID <= 19)
                m_bDiagramIsUML = true;
        }

        while (dbEnum.hasMoreElements()) {
            Object notation = dbEnum.nextElement();
            if (diagram instanceof DbORDiagram) {
                int notationMode = ((DbORNotation) notation).getNotationMode().intValue();
                if (notationMode == modelLogicalMode) {
                    dbos.add(notation);
                }
            } else { // BEDiagram
                if (m_bDiagramIsUML) {
                    if (((DbBENotation) notation).getMasterNotationID().intValue() == masterNotationID)
                        dbos.add(notation);
                } else {
                    int notationID = ((DbBENotation) notation).getMasterNotationID().intValue();
                    if (!(notationID >= 13 && notationID <= 19))
                        dbos.add(notation);
                }
            }

            if (notation == selnotation)
                selidx = dbos.size() - 1;
        }
        dbEnum.close();

        if (m_bDiagramIsUML) {
            Object[] items = new Object[dbos.size()];
            for (int i = 0; i < dbos.size(); i++) {
                items[i] = ((DbSMSNotation) dbos.get(i)).getName();
            }
            setDomainValues(items);
            if (selidx == -1)
                setSelectedIndex(0); // if none, it means the notation is null,
            // so the default project notation is
            // applied
            else
                setSelectedIndex(selidx); // adding a component for the project
            // default notation
        } else {
            Object[] items = new Object[dbos.size() + 2]; // adding a component
            // for the project
            // default notation
            items[0] = defaultItemName;
            items[1] = null;
            for (int i = 0; i < dbos.size(); i++) {
                items[i + 2] = ((DbSMSNotation) dbos.get(i)).getName();
            }

            setDomainValues(items);
            if (selidx == -1)
                setSelectedIndex(0); // if none, it means the notation is null,
            // so the default project notation is
            // applied
            else
                setSelectedIndex(selidx + 2); // adding a component for the
            // project default notation
        }

        setEnabled(true);

    }

    protected int getFeatureSet() {
        return SMSFilter.BPM | SMSFilter.RELATIONAL;
    }

}
