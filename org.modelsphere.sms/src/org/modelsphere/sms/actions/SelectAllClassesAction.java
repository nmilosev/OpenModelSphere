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

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.srtool.actions.AbstractSelectAction;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.graphic.BEUseCaseBox;
import org.modelsphere.sms.be.text.BETextUtil;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.or.db.DbORDataModel;

final class SelectAllClassesAction extends AbstractSelectAction {

    private static final String kSelectAllClasses = LocaleMgr.action.getString("selectAllClasses");
    private static final String kSelectAllTables = LocaleMgr.action.getString("selectAllTables");
    private static final String kSelectAllEntities = LocaleMgr.action
            .getString("selectAllEntities");

    public static final char kMnemonicClasses = LocaleMgr.action.getMnemonic("selectAllClasses");
    public static final char kMnemonicTables = LocaleMgr.action.getMnemonic("selectAllTables");
    public static final char kMnemonicEntities = LocaleMgr.action.getMnemonic("selectAllEntities");
    public static final char kMnemonicProcesses = LocaleMgr.action.getMnemonic("selectAllProcess");

    public static final KeyStroke kAcceleratorClasses = KeyStroke.getKeyStroke(LocaleMgr.action
            .getAccelerator("selectAllClasses"), ActionEvent.CTRL_MASK);
    public static final KeyStroke kAcceleratorTables = KeyStroke.getKeyStroke(LocaleMgr.action
            .getAccelerator("selectAllTables"), ActionEvent.CTRL_MASK);
    public static final KeyStroke kAcceleratorEntities = KeyStroke.getKeyStroke(LocaleMgr.action
            .getAccelerator("selectAllEntities"), ActionEvent.CTRL_MASK);
    public static final KeyStroke kAcceleratorProcesses = KeyStroke.getKeyStroke(LocaleMgr.action
            .getAccelerator("selectAllProcess"), ActionEvent.CTRL_MASK);

    boolean useCase = false;

    SelectAllClassesAction() {
        super(kSelectAllClasses);
        setMnemonic(kMnemonicClasses);
        setAccelerator(kAcceleratorClasses);
    }

    protected final void doActionPerformed() {
        if (useCase)
            doActionPerformed(BEUseCaseBox.class);
        else
            doActionPerformed(GraphicNode.class);
    }

    public final void currentFocusChanged(Object oldFocusObject, Object focusObject)
            throws DbException {
        String name = null;
        useCase = false;
        if (focusObject instanceof ApplicationDiagram) {
            DbObject diagram = ((ApplicationDiagram) focusObject).getDiagramGO();
            DbObject composite = diagram.getComposite();
            if (composite instanceof DbSMSPackage) {
                useCase = false;
                DbSMSPackage pack = (DbSMSPackage) composite;
                if (pack instanceof DbJVClassModel || pack instanceof DbJVPackage) {
                    name = kSelectAllClasses;
                    setMnemonic(kMnemonicClasses);
                    setAccelerator(null);
                } else if (pack instanceof DbORDataModel) {
                    TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
                    if (terminologyUtil.getModelLogicalMode(pack) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                        name = kSelectAllEntities;
                        setAccelerator(kAcceleratorEntities);
                        setMnemonic(kMnemonicEntities);
                    } else {
                        name = kSelectAllTables;
                        setAccelerator(null);
                        setMnemonic(kMnemonicTables);
                    }
                }
                useCase = false;
            } else if (composite instanceof DbBEUseCase) {
                setAccelerator(kAcceleratorProcesses);
                setMnemonic(kMnemonicProcesses);
                name = BETextUtil.getSingleton().getSelectAllText(composite, DbBEUseCase.metaClass);
                useCase = true;
            }
        }
        if (name == null) {
            setEnabled(false);
        } else {
            putValue(Action.NAME, name);
            setEnabled(true);
        }
    }
}
