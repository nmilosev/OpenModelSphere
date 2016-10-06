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

import java.awt.Image;
import java.text.MessageFormat;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.KeyTool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.util.PrimaryKey;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.or.screen.GeneratePrimaryKeysFrame;

final class GeneratePrimaryKeysAction extends AbstractApplicationAction implements
        SelectionActionListener {

    private static final String kGenPrimaryKeysDots = LocaleMgr.action
            .getString("genPrimaryKeysDots");
    private static final Icon kGenPrimaryKeysIcon = getIcon();
    private TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    GeneratePrimaryKeysAction() {
        super(kGenPrimaryKeysDots, kGenPrimaryKeysIcon);
        this.setMnemonic(LocaleMgr.action.getMnemonic("genPrimaryKeysDots"));
        setEnabled(true);
    }

    private static Icon getIcon() {
        Image image = GraphicUtil.loadImage(KeyTool.class, "resources/pk.gif"); // NOT
        // LOCALIZABLE
        // -
        // image
        ImageIcon icon = new ImageIcon(image);
        return null; // return icon;
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
    } // end updateSelectionAction()

    protected final void doActionPerformed() {
        try {
            DbORDataModel dataModel;
            DbObject[] activeObjs = ORActionFactory.getActiveObjects();
            activeObjs[0].getDb().beginReadTrans();
            if (activeObjs[0] instanceof DbORDataModel) {
                dataModel = ((DbORDataModel) activeObjs[0]);
            } else {
                dataModel = (DbORDataModel) activeObjs[0]
                        .getCompositeOfType(DbORDataModel.metaClass);
            }
            activeObjs[0].getDb().commitTrans();

            if (dataModel == null)
                return;

            // get model name
            dataModel.getDb().beginReadTrans();
            String dataModelName = dataModel.getName();
            dataModel.getDb().commitTrans();

            // Open dialog
            JFrame mainFrame = ApplicationContext.getDefaultMainFrame();
            String title = LocaleMgr.action.getString("genPrimaryKeys");
            GeneratePrimaryKeysFrame dialog = new GeneratePrimaryKeysFrame(mainFrame, title,
                    dataModel);
            dialog.setVisible(true);

            if (!dialog.hasCancelled()) {
                PrimaryKey.Options options = dialog.getOptions();
                int nbGenerated = PrimaryKey.getInstance().generate(dataModel, options, title);

                String pattern = LocaleMgr.action.getString("0PrimaryKeysGenerated");
                String msg = MessageFormat.format(pattern, nbGenerated);
                JOptionPane.showMessageDialog(mainFrame, msg, title,
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        } // end try
    } // end doActionPerformed()

    protected int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }
} // GeneratePrimaryKeysAction

