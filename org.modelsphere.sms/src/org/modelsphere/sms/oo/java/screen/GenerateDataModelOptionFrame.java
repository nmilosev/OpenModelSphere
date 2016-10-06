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
package org.modelsphere.sms.oo.java.screen;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.DbLookupDialog;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.sms.db.DbSMSBuiltInTypeNode;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.db.DbSMSUserDefinedPackage;
import org.modelsphere.sms.db.util.AnySemObject;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVPrimitiveType;
import org.modelsphere.sms.oo.java.features.JavaToDataModelParameters;
import org.modelsphere.sms.oo.java.features.JavaToDataModelParameters.InheritanceStrategy;
import org.modelsphere.sms.oo.java.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.plugins.TargetSystem;

public class GenerateDataModelOptionFrame extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final String kUnspecified = LocaleMgr.screen.getString("Unspecified");
    private static final String kLinkModel = LocaleMgr.screen.getString("linkModel");
    private static final String kTargetSystem = LocaleMgr.screen.getString("TargetSystem");
    private static final String kGenerateUnder = LocaleMgr.screen.getString("GenerateUnder");

    private DbSMSProject m_project = null;
    private DbOOAbsPackage m_ooPackage = null;
    private DbORBuiltInType m_defaultLongType = null;

    public DbObject destination = null;
    public boolean cancel = true;

    private static JavaToDataModelParameters m_parameters = new JavaToDataModelParameters();

    public JavaToDataModelParameters getParameters() {
        return m_parameters;
    }

    JPanel containerPanel = new JPanel();
    JPanel controlBtnPanel = new JPanel();

    JLabel targetSystemLabel = new JLabel(kTargetSystem + " :");
    JComboBox targetSystemCombo = new JComboBox();

    JLabel genUnderLabel = new JLabel(kGenerateUnder + " :"); //NOT LOCALIZABLE
    JTextField genUnderTxFld = new JTextField(kUnspecified);
    JButton destinationLookUp = new JButton("..."); //NOT LOCALIZABLE

    JButton optionsBtn = new JButton();

    JLabel inheritanceConversionLabel = new JLabel(LocaleMgr.screen
            .getString("InheritanceConversion"));
    JComboBox inheritanceConversionCombo = new JComboBox();
    JLabel categoryLabel = new JLabel(LocaleMgr.screen.getString("SubtypeDiscriminator"));
    JTextField categoryTxFld = new JTextField();

    JCheckBox createIdentifiers = new JCheckBox(LocaleMgr.screen.getString("CreateSurrogateKeys"));
    JLabel identifierLabel = new JLabel(LocaleMgr.screen.getString("NameOfSurrogateKey"));
    JTextField identifierTxFld = new JTextField();
    JLabel identifierTypeLabel = new JLabel(LocaleMgr.screen.getString("TypeOfSurrogateKey"));
    JComboBox identifierTypeCombo = new JComboBox();

    JCheckBox createLinksCheckBox = new JCheckBox(LocaleMgr.screen
            .getString("CreateSemanticLinksFromClasses"));
    JLabel linkModelLabel = new JLabel(kLinkModel + " :");//NOT LOCALIZABLE
    JTextField linkModelTxFld = new JTextField(kUnspecified);
    JButton linkModelLookUp = new JButton("..."); //NOT LOCALIZABLE

    JCheckBox createDiagramCheckBox = new JCheckBox(LocaleMgr.screen.getString("CreateDiagrams"));

    JButton okButton = new JButton(LocaleMgr.screen.getString("Generate"));
    JButton cancelButton = new JButton(LocaleMgr.screen.getString("Cancel"));

    GridLayout gridLayout1 = new GridLayout();

    public GenerateDataModelOptionFrame(Frame frame, DbOOAbsPackage ooPackage) throws DbException {
        super(frame, LocaleMgr.screen.getString("GenerateDataModel"), true);
        m_ooPackage = ooPackage;
        DbSMSProject project = (ooPackage == null) ? null : (DbSMSProject) ooPackage
                .getCompositeOfType(DbSMSProject.metaClass);

        if (project != null) {
            m_parameters.classModel = (ooPackage instanceof DbJVClassModel) ? (DbJVClassModel) ooPackage
                    : (DbJVClassModel) ooPackage.getCompositeOfType(DbJVClassModel.metaClass);

            m_project = (DbSMSProject) ooPackage.getCompositeOfType(DbSMSProject.metaClass);
        }

        m_parameters.linkModel = (project == null) ? null : project.getDefaultLinkModel();

        initContents();
        initValues();

        this.pack();
        this.setLocationRelativeTo(frame);
    }

    private void initContents() {
        //main panel
        containerPanel.setLayout(new GridBagLayout());
        getContentPane().add(containerPanel);
        gridLayout1.setHgap(5);
        controlBtnPanel.setLayout(gridLayout1);
        genUnderTxFld.setEditable(false);
        linkModelTxFld.setEditable(false);

        int row = 0;

        //target system
        containerPanel.add(targetSystemLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 0, 12), 0, 0));
        containerPanel.add(targetSystemCombo, new GridBagConstraints(1, row, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 0, 12),
                175, 5));
        row++;

        //generate under
        containerPanel.add(genUnderLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 12, 0, 12), 0, 0));
        containerPanel.add(genUnderTxFld, new GridBagConstraints(1, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 12),
                175, 5));
        containerPanel.add(destinationLookUp, new GridBagConstraints(2, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 12), 0, 0));
        row++;

        //advanced options
        containerPanel.add(optionsBtn, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 0, 12), 0, 0));
        row++;

        //inheritance conversion
        containerPanel.add(inheritanceConversionLabel, new GridBagConstraints(0, row, 1, 1, 0.0,
                0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 0, 12),
                0, 0));
        containerPanel.add(inheritanceConversionCombo, new GridBagConstraints(1, row, 1, 1, 1.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(12, 0, 0,
                        12), 175, 5));
        row++;

        containerPanel.add(categoryLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 36, 0, 6), 0, 0));
        containerPanel.add(categoryTxFld, new GridBagConstraints(1, row, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 12),
                175, 5));
        row++;

        //create identifier
        containerPanel.add(createIdentifiers, new GridBagConstraints(0, row,
                GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 12, 0, 0), 0, 0));
        row++;

        containerPanel.add(identifierLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 36, 0, 6), 0, 0));
        containerPanel.add(identifierTxFld, new GridBagConstraints(1, row, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 12),
                175, 5));
        row++;

        containerPanel.add(identifierTypeLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 36, 0, 6), 0, 0));
        containerPanel.add(identifierTypeCombo, new GridBagConstraints(1, row, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 12),
                175, 5));
        row++;

        //create links
        containerPanel.add(createLinksCheckBox, new GridBagConstraints(0, row,
                GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 12, 0, 0), 0, 0));
        row++;

        containerPanel.add(linkModelLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 24, 5, 6), 0, 0));
        containerPanel.add(linkModelTxFld, new GridBagConstraints(1, row, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 0, 5, 12),
                175, 5));
        containerPanel.add(linkModelLookUp, new GridBagConstraints(2, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 5, 12), 0, 0));
        row++;

        //create diagram
        containerPanel.add(createDiagramCheckBox, new GridBagConstraints(0, row,
                GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 12, 0, 0), 0, 0));
        row++;

        //control button panel
        containerPanel.add(controlBtnPanel, new GridBagConstraints(0, row,
                GridBagConstraints.REMAINDER, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
                GridBagConstraints.NONE, new Insets(17, 12, 12, 12), 0, 0));
        controlBtnPanel.add(okButton, null);
        controlBtnPanel.add(cancelButton, null);
        row++;

        targetSystemCombo.addActionListener(this);
        createIdentifiers.addActionListener(this);
        createLinksCheckBox.addActionListener(this);
        linkModelLookUp.addActionListener(this);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        destinationLookUp.addActionListener(this);
        inheritanceConversionCombo.addActionListener(this);
        optionsBtn.addActionListener(this);

        getRootPane().setDefaultButton(okButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        new HotKeysSupport(this, cancelButton, null);
    }

    private void initValues() throws DbException {

        //generate under..
        DbSMSProject project = (m_ooPackage == null) ? null : (DbSMSProject) m_ooPackage
                .getCompositeOfType(DbSMSProject.metaClass);
        destination = project;
        String text = (destination == null) ? kUnspecified : destination.getName();
        genUnderTxFld.setText(text);

        //target system
        if (project != null) {
            List<Serializable> targets = TargetSystem.getORTargetSystem(project);
            for (int i = 0; i < targets.size(); i++) { // there is at least the LOGICAL target system
                DbSMSTargetSystem target = (DbSMSTargetSystem) targets.get(i);
                targetSystemCombo.addItem(new DefaultComparableElement(target, target.getName()
                        + ' ' + target.getVersion()));
                addBuiltInTypes(target);
                m_parameters.putLongType(target);
            }

            DbSMSTargetSystem logicalTarget = (DbSMSTargetSystem) targets.get(0);
            m_parameters.setTargetSystem(logicalTarget);
            targetSystemCombo.setSelectedIndex(0);
        }

        showOptions(m_parameters.showOptions);

        //inheritance conversion
        int idx = m_parameters.stategy.ordinal();
        String tx1 = LocaleMgr.screen.getString("OneTablePerClass");
        String tx2 = LocaleMgr.screen.getString("OneTablePerConcreteClass");
        String tx3 = LocaleMgr.screen.getString("OneTablePerInheritanceTree");
        inheritanceConversionCombo.addItem(new DefaultComparableElement(
                InheritanceStrategy.ONE_TABLE_PER_CLASS, tx1));
        inheritanceConversionCombo.addItem(new DefaultComparableElement(
                InheritanceStrategy.ONE_TABLE_PER_CONCRETE_CLASS, tx2));
        inheritanceConversionCombo.addItem(new DefaultComparableElement(
                InheritanceStrategy.ONE_TABLE_PER_INHERITANCE_TREE, tx3));
        inheritanceConversionCombo.setSelectedIndex(idx);
        categoryTxFld.setText(m_parameters.category);
        boolean isInheritanceTreeStrategy = (m_parameters.stategy == InheritanceStrategy.ONE_TABLE_PER_INHERITANCE_TREE);
        categoryLabel.setEnabled(isInheritanceTreeStrategy);
        categoryTxFld.setEnabled(isInheritanceTreeStrategy);

        //create identifiers 
        createIdentifiers.setSelected(m_parameters.generatePK);
        identifierTxFld.setText(m_parameters.id);
        identifierLabel.setEnabled(createIdentifiers.isSelected());
        identifierTxFld.setEnabled(createIdentifiers.isSelected());
        identifierTypeLabel.setEnabled(createIdentifiers.isSelected());
        identifierTypeCombo.setEnabled(createIdentifiers.isSelected());
        resetIdentifierTypeCombo(identifierTypeCombo);

        //link model
        createLinksCheckBox.setSelected(m_parameters.createLinks);
        linkModelTxFld.setText(m_parameters.linkModel == null ? kUnspecified
                : m_parameters.linkModel.getName());
        linkModelLabel.setEnabled(createLinksCheckBox.isSelected());
        linkModelTxFld.setEnabled(createLinksCheckBox.isSelected());
        linkModelLookUp.setEnabled(createLinksCheckBox.isSelected());

        //create diagram
        createDiagramCheckBox.setSelected(m_parameters.generateDiagrams);

        //OK button
        okButton.setEnabled(isValidForGeneration());
    }

    private Map<DbSMSTargetSystem, List<TypeInfo>> m_types = new HashMap<DbSMSTargetSystem, List<TypeInfo>>();

    private void addBuiltInTypes(DbSMSTargetSystem target) throws DbException {
        DbSMSBuiltInTypePackage pack = target.getBuiltInTypePackage();
        List<TypeInfo> types = new ArrayList<TypeInfo>();
        DbEnumeration enu = pack.getComponents().elements(DbORBuiltInType.metaClass);
        while (enu.hasMoreElements()) {
            DbORBuiltInType type = (DbORBuiltInType) enu.nextElement();
            String typename = type.buildFullNameString();
            TypeInfo info = new TypeInfo(type, typename);
            types.add(info);
        } //end while
        enu.close();

        Collections.sort(types, typeComparator);
        m_types.put(target, types);
    }

    private final boolean isValidForGeneration() {
        if (destination != null) {
            if (createLinksCheckBox.isSelected()) {
                return (m_parameters.linkModel != null);
            } else {
                return true;
            }
        } else
            return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == targetSystemCombo) {
            DbSMSTargetSystem target = (DbSMSTargetSystem) ((DefaultComparableElement) targetSystemCombo
                    .getSelectedItem()).object;
            m_parameters.setTargetSystem(target);
            m_parameters.identifierType = null;
            resetIdentifierTypeCombo(identifierTypeCombo);
        } else if (e.getSource() == createLinksCheckBox) {
            linkModelLabel.setEnabled(createLinksCheckBox.isSelected());
            linkModelTxFld.setEnabled(createLinksCheckBox.isSelected());
            linkModelLookUp.setEnabled(createLinksCheckBox.isSelected());
            okButton.setEnabled(isValidForGeneration());
        } else if (e.getSource() == linkModelLookUp) {
            try {
                m_ooPackage.getDb().beginTrans(Db.READ_TRANS); //close by DbLookupDialog
                DefaultComparableElement item = DbLookupDialog.selectDbo(
                        GenerateDataModelOptionFrame.this, kLinkModel, null, m_project.getDb(),
                        AnySemObject.getAllDbSMSLinkModel(m_project), m_parameters.linkModel, null,
                        true);
                if (item != null) {
                    m_ooPackage.getDb().beginTrans(Db.READ_TRANS);
                    m_parameters.linkModel = (DbSMSLinkModel) item.object;
                    linkModelTxFld.setText(m_parameters.linkModel.getName());
                    m_ooPackage.getDb().commitTrans();
                }
                okButton.setEnabled(isValidForGeneration());
            } catch (DbException dbE) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), dbE);
            }
        } else if (e.getSource() == destinationLookUp) {
            Object item = DbTreeLookupDialog.selectOne(GenerateDataModelOptionFrame.this,
                    kGenerateUnder, new DbObject[] { m_project }, new MetaClass[] {
                            DbSMSProject.metaClass, DbSMSUserDefinedPackage.metaClass }, null,
                    null, destination);
            if (item != null) {
                try {
                    m_ooPackage.getDb().beginTrans(Db.READ_TRANS);
                    destination = (DbObject) item;
                    genUnderTxFld.setText(destination.getName());
                    okButton.setEnabled(isValidForGeneration());
                    m_ooPackage.getDb().commitTrans();
                } catch (DbException dbE) {
                    org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                            ApplicationContext.getDefaultMainFrame(), dbE);
                }
            }
        } else if (e.getSource() == optionsBtn) {
            m_parameters.showOptions = !(m_parameters.showOptions);
            showOptions(m_parameters.showOptions);
        } else if (e.getSource() == inheritanceConversionCombo) {
            m_parameters.stategy = (InheritanceStrategy) ((DefaultComparableElement) inheritanceConversionCombo
                    .getSelectedItem()).object;
            boolean isInheritanceTreeStrategy = (m_parameters.stategy == InheritanceStrategy.ONE_TABLE_PER_INHERITANCE_TREE);
            categoryLabel.setEnabled(isInheritanceTreeStrategy);
            categoryTxFld.setEnabled(isInheritanceTreeStrategy);
        } else if (e.getSource() == createIdentifiers) {
            identifierLabel.setEnabled(createIdentifiers.isSelected());
            identifierTxFld.setEnabled(createIdentifiers.isSelected());
            identifierTypeLabel.setEnabled(createIdentifiers.isSelected());
            identifierTypeCombo.setEnabled(createIdentifiers.isSelected());
        } else if (e.getSource() == okButton) {
            DbSMSTargetSystem target = (DbSMSTargetSystem) ((DefaultComparableElement) targetSystemCombo
                    .getSelectedItem()).object;
            m_parameters.setTargetSystem(target);
            DefaultComparableElement elem = (DefaultComparableElement) inheritanceConversionCombo
                    .getSelectedItem();
            m_parameters.stategy = (InheritanceStrategy) elem.object;
            m_parameters.category = categoryTxFld.getText();
            m_parameters.generatePK = createIdentifiers.isSelected();
            m_parameters.id = identifierTxFld.getText();
            m_parameters.createLinks = createLinksCheckBox.isSelected();
            m_parameters.generateDiagrams = createDiagramCheckBox.isSelected();
            m_parameters.showOptions = optionsBtn.isSelected();

            elem = (DefaultComparableElement) identifierTypeCombo.getSelectedItem();
            TypeInfo info = (TypeInfo) elem.object;
            m_parameters.identifierType = info.m_builtInType;

            cancel = false;
            dispose();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }

    private void resetIdentifierTypeCombo(JComboBox typeCombo) {
        if (m_parameters.getTargetSystem() != null) {
            m_defaultLongType = m_parameters.getLongType(m_parameters.getTargetSystem());
            List<TypeInfo> types = m_types.get(m_parameters.getTargetSystem());
            typeCombo.removeAllItems();
            int index = 0;
            int selectedIndex = 0;

            if (m_parameters.identifierType == null) {
                m_parameters.identifierType = m_defaultLongType;
            }

            if (types != null) {
                for (TypeInfo type : types) {
                    if (type.m_builtInType.equals(m_parameters.identifierType)) {
                        selectedIndex = index;
                    }

                    String text = type.m_typename;
                    typeCombo.addItem(new DefaultComparableElement(type, text));
                    index++;
                }
            }

            if (typeCombo.getItemCount() > selectedIndex) {
                typeCombo.setSelectedIndex(selectedIndex);
            }
        } //end if
    }

    private void showOptions(boolean showOptions) {
        inheritanceConversionLabel.setVisible(showOptions);
        inheritanceConversionCombo.setVisible(showOptions);
        categoryLabel.setVisible(showOptions);
        categoryTxFld.setVisible(showOptions);
        createIdentifiers.setVisible(showOptions);
        identifierLabel.setVisible(showOptions);
        identifierTxFld.setVisible(showOptions);
        identifierTypeLabel.setVisible(showOptions);
        identifierTypeCombo.setVisible(showOptions);
        createLinksCheckBox.setVisible(showOptions);
        linkModelLabel.setVisible(showOptions);
        linkModelTxFld.setVisible(showOptions);
        linkModelLookUp.setVisible(showOptions);
        createDiagramCheckBox.setVisible(showOptions);

        String options = LocaleMgr.screen.getString("Options");
        String text = showOptions ? "<< " + options : options + " >>";
        optionsBtn.setText(text);

        this.pack();
    }

    private DbJVPrimitiveType longType = null;

    private DbJVPrimitiveType getLongType(DbJVClass clas) throws DbException {
        if (longType != null) {
            return longType;
        }

        longType = getPrimitiveType(clas, "long");
        return longType;
    }

    private DbJVPrimitiveType getPrimitiveType(DbJVClass clas, String primitiveName)
            throws DbException {
        DbJVPrimitiveType primitive = null;
        DbSMSBuiltInTypePackage typePackage = getJavaBuiltinTypePackage(clas);
        DbRelationN relN = typePackage.getComponents();
        DbEnumeration enu = relN.elements(DbJVPrimitiveType.metaClass);

        while (enu.hasMoreElements()) {
            DbJVPrimitiveType type = (DbJVPrimitiveType) enu.nextElement();
            String typename = type.getName();
            if (primitiveName.equals(typename)) {
                primitive = type;
                break;
            }
        } //end while
        enu.close();

        return primitive;
    }

    private DbSMSBuiltInTypePackage javaBuiltinTypePackage = null;

    private DbSMSBuiltInTypePackage getJavaBuiltinTypePackage(DbJVClass clas) throws DbException {
        if (javaBuiltinTypePackage != null) {
            return javaBuiltinTypePackage;
        }

        DbSMSProject project = (DbSMSProject) clas.getCompositeOfType(DbSMSProject.metaClass);
        DbRelationN relN = project.getComponents();
        DbEnumeration enu = relN.elements(DbSMSBuiltInTypeNode.metaClass);

        while (enu.hasMoreElements()) {
            DbSMSBuiltInTypeNode typeNode = (DbSMSBuiltInTypeNode) enu.nextElement();
            DbRelationN relN2 = typeNode.getComponents();
            DbEnumeration enu2 = relN2.elements(DbSMSBuiltInTypePackage.metaClass);

            while (enu2.hasMoreElements()) {
                DbSMSBuiltInTypePackage typePackage = (DbSMSBuiltInTypePackage) enu2.nextElement();
                String name = typePackage.getName();
                if (name.startsWith("Java")) {
                    javaBuiltinTypePackage = typePackage;
                    break;
                } //end if
            } //end while
            enu2.close();
        } //end while
        enu.close();

        return javaBuiltinTypePackage;
    }

    //
    // inner classes
    //

    private static class TypeInfo {
        DbORBuiltInType m_builtInType;
        String m_typename;

        TypeInfo(DbORBuiltInType builtInType, String typename) {
            m_builtInType = builtInType;
            m_typename = typename;
        }
    }

    private TypeComparator typeComparator = new TypeComparator();

    private static class TypeComparator implements Comparator<TypeInfo> {

        @Override
        public int compare(TypeInfo o1, TypeInfo o2) {
            String s1 = o1.m_typename;
            String s2 = o2.m_typename;
            int comparison = s1.compareTo(s2);
            return comparison;
        }

    }

    // *************
    // DEMO FUNCTION
    // *************
    private static void createAndShowGUI() throws DbException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
        }

        //Create and set up the window.
        JFrame frame = new JFrame("ImportJavaClassFilesDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DbOOAbsPackage pack = null;
        GenerateDataModelOptionFrame dialog = new GenerateDataModelOptionFrame(frame, pack);
        dialog.setVisible(true);

        /*
         * boolean done = false; do { try { Thread.sleep(200); } catch (InterruptedException ex) {}
         * 
         * if (!dialog.isShowing()) { dialog.dispose(); dialog = null; done = true; } } while (!
         * done); System.exit(0);
         */
    } //end runDemo()

    //Run the demo
    //
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (DbException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

} //end GenerateDataModelOptionFrame

