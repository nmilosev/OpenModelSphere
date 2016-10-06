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

package org.modelsphere.sms.plugins.jdbc.bridge;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectScope;
import org.modelsphere.jack.srtool.reverse.jdbc.JdbcMetaInfo;
import org.modelsphere.jack.srtool.reverse.jdbc.JdbcReader;
import org.modelsphere.jack.srtool.services.ConnectionMessage;
import org.modelsphere.jack.srtool.services.ConnectionService;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.SrSort;
import org.modelsphere.sms.or.db.DbORUser;
import org.modelsphere.sms.or.features.dbms.DBMSReverseOptions;
import org.modelsphere.sms.or.features.dbms.DefaultReverseWizardOptionPage;
import org.modelsphere.sms.plugins.TargetSystem;
import org.modelsphere.sms.plugins.TargetSystemInfo;

public final class JdbcReverseSpecificOptionPage extends DefaultReverseWizardOptionPage {

    private SpecificPanel specificOptionPanel = new SpecificPanel();

    JdbcReverseSpecificOptionPage() {
        super();
    }

    public final boolean terminate(Object opt, boolean saveData) {
        DBMSReverseOptions options = (DBMSReverseOptions) opt;
        if (!saveData) {
            options = null;
            return true;
        }
        // save GUI setting
        options.reverseObjectUser = reverseObjectUserCheckBox.isSelected();
        ObjectScope userScope = null;
        userScope = ObjectScope.findConceptInObjectScopeWithConceptName(options.getObjectsScope(),
                DbORUser.metaClass.getGUIName());
        userScope.isSelected = options.reverseObjectUser;

        ConnectionMessage connectionMessage = options.getConnection();
        if (connectionMessage == null)
            return false;
        terminateSpecific(options);

        // Must set the catalog option before the namelist is built because it is used
        // as an argument in JDBC.
        ((JdbcReverseOptions) options.getSpecificDBMSOptions())
                .setCatalogOption(specificOptionPanel.getCatalog());

        // Must set the target system Id to be used for the reverse process
        options.targetSystemId = specificOptionPanel.getTargetSystem();

        options.nameList = (ArrayList) this.getNameList(options);
        restructureNameListData(options);
        terminateSpecific(options);
        return true;
    }

    // Add a scope for the selection of a catalog
    protected JPanel getSpecificOptionsPanel() {
        return specificOptionPanel;
    }

    // Add gui elements to the panel
    protected void initSpecific(DBMSReverseOptions options) {
        try {
            JdbcReverseOptions specific = (JdbcReverseOptions) options.getSpecificDBMSOptions();
            DatabaseMetaData dbmd = ConnectionService.getConnection(
                    options.getConnection().connectionId).getMetaData();
            specificOptionPanel.setCatalogTerm(dbmd.getCatalogTerm());

            populateCatalogList(options);
            populateTargetSystem(options);
        } catch (SQLException ex) {
            if (Debug.isDebug())
                ex.printStackTrace(); // Should not continue, an error in the catalog scope is fatal for the reverse
        }
    }

    protected void terminateSpecific(DBMSReverseOptions options) {
        //specificOptionPanel = null;
    }

    private void populateCatalogList(DBMSReverseOptions options) throws SQLException {
        JdbcCatalogListBuilder builder = new JdbcCatalogListBuilder();
        ConnectionMessage connectionMsg = options.getConnection();
        JdbcReader reader = new JdbcReader(builder, connectionMsg.connectionId);
        try {
            reader.getCatalogInfo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        specificOptionPanel.setCatalogElements(builder.getCatalogElements());
        specificOptionPanel.setCatalog(((JdbcReverseOptions) options.getSpecificDBMSOptions())
                .getCatalogOption());
    }

    private List getNameList(DBMSReverseOptions options) {
        JdbcNameListBuilder builder = new JdbcNameListBuilder();
        ConnectionMessage connectionMsg = options.getConnection();
        ObjectScope[] objectScope = options.getObjectsScope();
        try {
            JdbcReader reader = new JdbcReader(builder, connectionMsg.connectionId);
            for (int i = 0; i < objectScope.length; i++) {
                if (objectScope[i].SQLGetID != null) {
                    int conceptId = objectScope[i].conceptID;
                    switch (conceptId) {
                    case JdbcReverseToolkitPlugin.USERID:
                        getUserNames(reader);
                        builder.installUser(((Integer) objectScope[i].SQLGetID).intValue());
                        break;
                    case JdbcReverseToolkitPlugin.TYPEID:
                        if (objectScope[i].isSelected)
                            getTypeNames(options, reader);
                        builder.installType(((Integer) objectScope[i].SQLGetID).intValue());
                        break;
                    case JdbcReverseToolkitPlugin.TABLEID:
                        if (objectScope[i].isSelected)
                            getTableNames(options, reader);
                        builder.installTable(((Integer) objectScope[i].SQLGetID).intValue());
                        break;
                    case JdbcReverseToolkitPlugin.VIEWID:
                        if (objectScope[i].isSelected)
                            getViewNames(options, reader);
                        builder.installView(((Integer) objectScope[i].SQLGetID).intValue());
                        break;
                    case JdbcReverseToolkitPlugin.PROCID:
                        if (objectScope[i].isSelected)
                            getProcNames(options, reader);
                        builder.installProc(((Integer) objectScope[i].SQLGetID).intValue());
                        break;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Should not continue, an error in the name list is fatal for the reverse
        } catch (Exception ex) {
            ex.printStackTrace(); // Should not continue, an error in the name list is fatal for the reverse
        }
        return builder.getNameList();
    }

    private void getUserNames(JdbcReader aReader) throws SQLException, Exception {
        aReader.getSchemaInfo();
    }

    private void getTypeNames(DBMSReverseOptions options, JdbcReader aReader) throws SQLException,
            Exception {
        aReader.getTypeInfo();
    }

    private void getTableNames(DBMSReverseOptions options, JdbcReader aReader) throws SQLException,
            Exception {
        String[] tableTypes = JdbcReverseOptions.getTableTypes();
        String catalog = ((JdbcReverseOptions) options.getSpecificDBMSOptions()).getCatalogOption();
        // Use the catalog scope
        // The user scope is null because we have to retreive all tables with users
        aReader.getTableInfo(catalog, null, JdbcMetaInfo.ZERO_OR_MORE, tableTypes);
    }

    private void getViewNames(DBMSReverseOptions options, JdbcReader aReader) throws SQLException,
            Exception {
        String[] viewTypes = JdbcReverseOptions.getViewTypes();
        String catalog = ((JdbcReverseOptions) options.getSpecificDBMSOptions()).getCatalogOption();
        // Use the catalog scope
        // The user scope is null because we have to retreive all tables with users
        aReader.getViewInfo(catalog, null, JdbcMetaInfo.ZERO_OR_MORE, viewTypes);
    }

    private void getProcNames(DBMSReverseOptions options, JdbcReader aReader) throws SQLException,
            Exception {
        String catalog = ((JdbcReverseOptions) options.getSpecificDBMSOptions()).getCatalogOption();
        // Use the catalog scope
        // The user scope is null because we have to retreive all tables with users
        aReader.getProcedureInfo(catalog, null, JdbcMetaInfo.ZERO_OR_MORE);
    }

    private void populateTargetSystem(DBMSReverseOptions options) throws SQLException {
        JdbcReverseOptions jdbcOptions = (JdbcReverseOptions) options.getSpecificDBMSOptions();
        ArrayList tsList = (ArrayList) jdbcOptions.getTargetSystems();
        ArrayList compareList = new ArrayList();
        TargetSystemInfo targetInfo = null;

        for (int i = 0; i < tsList.size(); i++) {
            targetInfo = (TargetSystemInfo) tsList.get(i);
            compareList.add(new DefaultComparableElement(targetInfo, targetInfo.getName() + " "
                    + targetInfo.getVersion()));
        }
        DefaultComparableElement[] ts = new DefaultComparableElement[compareList.size()];
        compareList.toArray(ts);
        CollationComparator comparator = new CollationComparator();
        SrSort.sortArray(ts, ts.length, comparator);
        specificOptionPanel.setTargetSystemElements(ts);
        int targetId = options.targetSystemId;
        if (targetId == -1)
            targetId = TargetSystem.SGBD_LOGICAL;
        specificOptionPanel.setTargetSystem(targetId);
    }

    static class SpecificPanel extends JPanel {

        private static final String catalogLabelKey = LocaleMgr.misc.getString("Catalog");
        private static final String targetLabelKey = LocaleMgr.misc.getString("TargetSystem");

        private TitledBorder specificOptionsPanelBorder = new TitledBorder(LocaleMgr.misc
                .getString("DBMSOptions"));

        private JComboBox catalogCombo = new JComboBox();
        private JComboBox targetCombo = new JComboBox();
        private JLabel catalogLabel = new JLabel(catalogLabelKey);
        private JLabel targetLabel = new JLabel(targetLabelKey);

        SpecificPanel() {
            super(new GridBagLayout());
            init();
        }

        private void init() {
            setBorder(specificOptionsPanelBorder);

            add(catalogLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(12, 12, 6, 6), 0, 0));
            catalogCombo.setEditable(true);
            add(catalogCombo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets(12, 0, 6, 12), 0, 0));
            add(Box.createHorizontalGlue(), new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

            add(targetLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(0, 12, 12, 6), 0, 0));
            targetCombo.setEditable(false);
            add(targetCombo, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 0, 12, 12), 0, 0));
            add(Box.createHorizontalGlue(), new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

            add(Box.createVerticalGlue(), new GridBagConstraints(0, 2, 3, 1, 0.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }

        public String getCatalog() {
            return (String) catalogCombo.getSelectedItem();
        }

        public int getTargetSystem() {
            DefaultComparableElement element = (DefaultComparableElement) targetCombo
                    .getSelectedItem();
            if (element == null)
                return -1;
            TargetSystemInfo targetInfo = (TargetSystemInfo) element.object;
            if (targetInfo == null)
                return -1;
            return targetInfo.getID();
        }

        public void setTargetSystemElements(Object[] elements) {
            targetCombo.removeAllItems();
            if (elements == null)
                return;

            for (int i = 0; i < elements.length; i++)
                targetCombo.addItem(elements[i]);
        }

        public void setTargetSystem(int targetid) {
            if (targetid == -1)
                targetCombo.setSelectedIndex(-1);

            int count = targetCombo.getItemCount();
            DefaultComparableElement element = null;
            for (int i = 0; i < count; i++) {
                Object item = targetCombo.getItemAt(i);
                if (!(item instanceof DefaultComparableElement))
                    continue;
                element = (DefaultComparableElement) item;
                if (((TargetSystemInfo) element.object).getID() == targetid) {
                    targetCombo.setSelectedIndex(i);
                    break;
                }
            }
        }

        public void setCatalog(String catalog) {
            catalogCombo.setSelectedItem(catalog);
        }

        public void setCatalogElements(Object[] elements) {
            catalogCombo.removeAllItems();
            if (elements == null)
                return;

            for (int i = 0; i < elements.length; i++)
                catalogCombo.addItem(elements[i]);
        }

        public void setCatalogTerm(String term) {
            if (term == null || term.length() == 0)
                catalogLabel.setText(catalogLabelKey);
            else
                catalogLabel.setText(term);
            revalidate();
        }

    }

}
