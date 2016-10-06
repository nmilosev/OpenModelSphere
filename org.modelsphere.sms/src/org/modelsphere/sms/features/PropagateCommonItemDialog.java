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

package org.modelsphere.sms.features;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.oo.db.DbOOClassModel;
import org.modelsphere.sms.oo.db.srtypes.OOTypeUseStyle;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.international.LocaleMgr;

public class PropagateCommonItemDialog extends JDialog {

    private boolean m_changesmade = false;
    private JPanel mainPanel = new JPanel(new GridBagLayout());
    private JPanel optionalPanel = new JPanel(new GridBagLayout());
    private JPanel mandatoryPanel = new JPanel(new GridBagLayout());
    JPanel buttonPanel = new JPanel(new GridBagLayout());
    private JCheckBox nameChkBox = new JCheckBox();
    private JCheckBox physNameChkBox = new JCheckBox();
    private JCheckBox aliasChkBox = new JCheckBox();
    private JCheckBox descripChkBox = new JCheckBox();
    private JCheckBox stereotypeChkBox = new JCheckBox();
    private JLabel typeLabel = new JLabel();
    private JLabel lengthLabel = new JLabel();
    private JLabel nbDecLabel = new JLabel();
    private JLabel nullLabel = new JLabel();
    private JLabel ooPropLabel = new JLabel();
    private JButton cancelBtn = new JButton();
    private JButton propaglBtn = new JButton();
    private DbObject[] m_semObjs;
    private String m_title;
    private static int g_options = 0;

    private static final int NAME = 1;
    private static final int PHYSICAL_NAME = 2;
    private static final int ALIAS = 4;
    private static final int DESCRIPTION = 8;
    private static final int STEREOTYPE = 16;

    private static final String OPTIONAL_PROPS = LocaleMgr.screen.getString("OptionalProperties");
    private static final String MANDATORY_PROPS = LocaleMgr.screen.getString("MandatoryProperties");
    private static final String CANCEL = LocaleMgr.screen.getString("Cancel");
    private static final String PROPAGATE = LocaleMgr.screen.getString("Propagate");

    private static final String NAME2 = DbSemanticalObject.fName.getGUIName();
    private static final String PHYSICAL_NAME2 = DbSemanticalObject.fPhysicalName.getGUIName();
    private static final String ALIAS2 = DbSemanticalObject.fAlias.getGUIName();
    private static final String DESC = DbSemanticalObject.fDescription.getGUIName();
    private static final String UML_STEREO = DbSMSStereotype.metaClass.getGUIName();
    private static final String TYPE = DbORColumn.fType.getGUIName();
    private static final String LEN = DbORColumn.fLength.getGUIName();
    private static final String NB_DECS = DbORColumn.fNbDecimal.getGUIName();
    private static final String NULL = DbORColumn.fNull.getGUIName();
    private static final String OO_PROPERTIES = LocaleMgr.screen.getString("OOProperties");

    private TitledBorder optionalTitle = new TitledBorder(OPTIONAL_PROPS);
    private TitledBorder mandatoryTitle = new TitledBorder(MANDATORY_PROPS);
    private ImageIcon mandatoryIcon = LocaleMgr.screen.getImageIcon("mandatoryIcon");

    public PropagateCommonItemDialog(Frame frame, String title, boolean modal, DbObject[] semObjs) {
        super(frame, title, modal);
        m_title = title;
        m_semObjs = semObjs;

        try {
            jbInit();
            init();
            pack();
            addListeners();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public PropagateCommonItemDialog() {
        this(null, "", false, null);
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(new BorderLayout());
        nameChkBox.setText(NAME2);
        physNameChkBox.setText(PHYSICAL_NAME2);
        aliasChkBox.setText(ALIAS2);
        descripChkBox.setText(DESC);
        stereotypeChkBox.setText(UML_STEREO);
        typeLabel.setToolTipText("");
        typeLabel.setText(TYPE);
        typeLabel.setIcon(mandatoryIcon);
        lengthLabel.setText(LEN);
        lengthLabel.setIcon(mandatoryIcon);
        nbDecLabel.setText(NB_DECS);
        nbDecLabel.setIcon(mandatoryIcon);
        nullLabel.setText(NULL);
        nullLabel.setIcon(mandatoryIcon);
        ooPropLabel.setText(OO_PROPERTIES);
        ooPropLabel.setIcon(mandatoryIcon);
        cancelBtn.setText(CANCEL);
        propaglBtn.setText(PROPAGATE);

        optionalPanel.setBorder(optionalTitle);
        mandatoryPanel.setBorder(mandatoryTitle);
        AwtUtil.normalizeComponentDimension(new JButton[] { propaglBtn, cancelBtn });

        this.getContentPane().add(mainPanel);
        mainPanel.add(optionalPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(12, 12, 11, 5), 20,
                0));

        optionalPanel.add(nameChkBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 11), 0,
                0));
        optionalPanel.add(physNameChkBox, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 1), 0,
                0));
        optionalPanel.add(aliasChkBox, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 1), 0,
                0));
        optionalPanel.add(descripChkBox, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 11), 0,
                0));
        optionalPanel.add(stereotypeChkBox, new GridBagConstraints(0, 4, 1, 1, 0.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 11, 11),
                0, 0));
        optionalPanel.add(Box.createHorizontalStrut(150),
                new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        mainPanel.add(mandatoryPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(12, 6, 11, 11), 20,
                0));

        mandatoryPanel.add(typeLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(4, 3, 4, 11), 0,
                0));
        mandatoryPanel.add(lengthLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(4, 3, 4, 11), 0,
                0));
        mandatoryPanel.add(nbDecLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(4, 3, 4, 11), 0,
                0));
        mandatoryPanel.add(nullLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(4, 3, 4, 11), 0,
                0));
        mandatoryPanel.add(ooPropLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(4, 3, 11, 11),
                0, 0));
        mandatoryPanel.add(Box.createHorizontalStrut(150),
                new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        mainPanel.add(buttonPanel, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        buttonPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        buttonPanel.add(propaglBtn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 12, 12, 6), 0, 0));
        buttonPanel.add(cancelBtn, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 5, 12, 11), 0, 0));

    }

    private void init() {
        nameChkBox.setSelected((g_options & NAME) != 0);
        physNameChkBox.setSelected((g_options & PHYSICAL_NAME) != 0);
        aliasChkBox.setSelected((g_options & ALIAS) != 0);
        descripChkBox.setSelected((g_options & DESCRIPTION) != 0);
        stereotypeChkBox.setSelected((g_options & STEREOTYPE) != 0);
    } //end init()

    private void addListeners() {
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        propaglBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                propagateCommonItems();
                dispose();
            }
        });
    }

    //
    // PRIVATE METHODS
    //
    private void propagateCommonItems() {
        g_options = getOptions();

        m_changesmade = false;

        try {
            m_semObjs[0].getDb().beginWriteTrans(m_title);
            int nb = m_semObjs.length;
            for (int i = 0; i < nb; i++) {
                DbObject semObj = m_semObjs[i];
                if (semObj instanceof DbORCommonItemModel) {
                    DbORCommonItemModel model = (DbORCommonItemModel) semObj;
                    propagateCommonItemValues(model, g_options);
                } else if (semObj instanceof DbORCommonItem) {
                    DbORCommonItem item = (DbORCommonItem) semObj;
                    propagateCommonItemValues(item, g_options);
                } //end if
                else if (semObj instanceof DbORDataModel) {
                    DbObject tableOrView = null;
                    DbEnumeration orTableEnum = semObj.getComponents().elements(
                            DbORAbsTable.metaClass);
                    while (orTableEnum.hasMoreElements()) {
                        tableOrView = orTableEnum.nextElement();
                        DbEnumeration orColumnEnum = tableOrView.getComponents().elements(
                                DbORColumn.metaClass);
                        DbORCommonItem item = null;
                        while (orColumnEnum.hasMoreElements()) {
                            DbORColumn parm = (DbORColumn) orColumnEnum.nextElement();
                            item = (DbORCommonItem) parm.get(DbORColumn.fCommonItem);
                            if (item != null)
                                propagateCommonItemValues(item, parm, false, g_options);
                        }
                        orColumnEnum.close();
                    }
                    orTableEnum.close();
                } else if (semObj instanceof DbORColumn) {
                    DbORCommonItem item = (DbORCommonItem) semObj.get(DbORColumn.fCommonItem);
                    if (item != null)
                        propagateCommonItemValues(item, semObj, false, g_options);
                } else if (semObj instanceof DbORAbsTable) {
                    DbEnumeration orColumnEnum = semObj.getComponents().elements(
                            DbORColumn.metaClass);
                    DbORCommonItem item = null;
                    while (orColumnEnum.hasMoreElements()) {
                        DbORColumn parm = (DbORColumn) orColumnEnum.nextElement();
                        item = (DbORCommonItem) parm.get(DbORColumn.fCommonItem);
                        if (item != null)
                            propagateCommonItemValues(item, parm, false, g_options);
                    }
                    orColumnEnum.close();
                } else if (semObj instanceof DbOOClassModel) {
                    DbObject tableOrView = null;
                    DbEnumeration orTableEnum = semObj.getComponents()
                            .elements(DbOOClass.metaClass);
                    while (orTableEnum.hasMoreElements()) {
                        tableOrView = orTableEnum.nextElement();
                        DbEnumeration orColumnEnum = tableOrView.getComponents().elements(
                                DbJVDataMember.metaClass);
                        DbORCommonItem item = null;
                        while (orColumnEnum.hasMoreElements()) {
                            DbJVDataMember parm = (DbJVDataMember) orColumnEnum.nextElement();
                            item = (DbORCommonItem) parm.get(DbJVDataMember.fCommonItem);
                            if (item != null)
                                propagateCommonItemValues(item, parm, true, g_options);
                        }
                        orColumnEnum.close();
                    }
                    orTableEnum.close();
                } else if (semObj instanceof DbOOClass) {
                    DbEnumeration orColumnEnum = semObj.getComponents().elements(
                            DbJVDataMember.metaClass);
                    DbORCommonItem item = null;
                    while (orColumnEnum.hasMoreElements()) {
                        DbJVDataMember parm = (DbJVDataMember) orColumnEnum.nextElement();
                        item = (DbORCommonItem) parm.get(DbJVDataMember.fCommonItem);
                        if (item != null)
                            propagateCommonItemValues(item, parm, true, g_options);
                    }
                    orColumnEnum.close();
                } else if (semObj instanceof DbJVDataMember) {
                    DbORCommonItem item = (DbORCommonItem) semObj.get(DbJVDataMember.fCommonItem);
                    if (item != null)
                        propagateCommonItemValues(item, semObj, true, g_options);
                }
            } //end for
            m_semObjs[0].getDb().commitTrans();

            if (m_changesmade == false)
                JOptionPane.showMessageDialog(this, LocaleMgr.screen
                        .getString("NoCommonItemToPropagate"), ApplicationContext.getApplicationName(),
                        JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);

        }
    } //end propagateCommonItems()

    private int getOptions() {
        int options = 0;

        options += nameChkBox.isSelected() ? NAME : 0;
        options += physNameChkBox.isSelected() ? PHYSICAL_NAME : 0;
        options += aliasChkBox.isSelected() ? ALIAS : 0;
        options += descripChkBox.isSelected() ? DESCRIPTION : 0;
        options += stereotypeChkBox.isSelected() ? STEREOTYPE : 0;

        return options;
    } //end getOptions()

    private void propagateCommonItemValues(DbORCommonItemModel model, int options)
            throws DbException {
        DbRelationN relN = model.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORCommonItem.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORCommonItem item = (DbORCommonItem) dbEnum.nextElement();
            propagateCommonItemValues(item, options);
        } //end while
        dbEnum.close();
    } //end propagateCommonItemValues()

    private void propagateCommonItemValues(DbORCommonItem item, DbObject concept,
            boolean isObjectOriented, int options) throws DbException {

        m_changesmade = true;

        //get mandatory properties
        DbORTypeClassifier type = item.getType();
        Integer len = item.getLength();
        Integer dec = item.getNbDecimal();
        boolean isNull = item.getNull();

        //get optional properties
        String name = item.getName();
        String physicalName = item.getPhysicalName();
        String alias = item.getAlias();
        String desc = item.getDescription();
        DbSMSStereotype stereotype = item.getUmlStereotype();

        if (!isObjectOriented) {
            DbORColumn column = (DbORColumn) concept;
            column.setType(type);
            column.setLength(len);
            column.setNbDecimal(dec);
            column.setNull(Boolean.valueOf(isNull));

            //set optional properties
            if ((options & NAME) != 0) {
                column.setName(name);
            }

            if ((options & PHYSICAL_NAME) != 0) {
                column.setPhysicalName(physicalName);
            }

            if ((options & ALIAS) != 0) {
                column.setAlias(alias);
            }

            if ((options & DESCRIPTION) != 0) {
                column.setDescription(desc);
            }

            if ((options & STEREOTYPE) != 0) {
                column.setUmlStereotype(stereotype);
            }
        } //end while
        else {

            //get object properties
            DbOOAdt ooType = item.getOoType();
            String typeUse = item.getTypeUse();
            OOTypeUseStyle style = item.getTypeUseStyle();
            JVVisibility visib = (JVVisibility) item.getVisibility();
            boolean isStatic = item.isStatic();
            boolean isFinal = item.isFinal();
            boolean isTransient = item.isTransient();
            boolean isVolatile = item.isVolatile();

            //set mandatory oo properties
            DbJVDataMember field = (DbJVDataMember) concept;
            field.setType(ooType);
            field.setTypeUse(typeUse);
            field.setTypeUseStyle(style);
            field.setVisibility(visib);
            field.setStatic(Boolean.valueOf(isStatic));
            field.setFinal(new Boolean(isFinal));
            field.setTransient(new Boolean(isTransient));
            field.setVolatile(new Boolean(isVolatile));

            //set optional properties
            if ((options & NAME) != 0) {
                field.setName(name);
            }

            if ((options & ALIAS) != 0) {
                field.setAlias(alias);
            }

            if ((options & DESCRIPTION) != 0) {
                field.setDescription(desc);
            }

            if ((options & STEREOTYPE) != 0) {
                field.setUmlStereotype(stereotype);
            }
        } //end while
    }

    private void propagateCommonItemValues(DbORCommonItem item, int options) throws DbException {

        //get mandatory properties
        DbORTypeClassifier type = item.getType();
        Integer len = item.getLength();
        Integer dec = item.getNbDecimal();
        boolean isNull = item.getNull();

        //get optional properties
        String name = item.getName();
        String physicalName = item.getPhysicalName();
        String alias = item.getAlias();
        String desc = item.getDescription();
        DbSMSStereotype stereotype = item.getUmlStereotype();

        //for each column
        DbRelationN relN = item.getColumns();
        DbEnumeration dbEnum = relN.elements(DbORColumn.metaClass);
        while (dbEnum.hasMoreElements()) {

            m_changesmade = true;

            //set mandatory properties
            DbORColumn column = (DbORColumn) dbEnum.nextElement();
            column.setType(type);
            column.setLength(len);
            column.setNbDecimal(dec);
            column.setNull(Boolean.valueOf(isNull));

            //set optional properties
            if ((options & NAME) != 0) {
                column.setName(name);
            }

            if ((options & PHYSICAL_NAME) != 0) {
                column.setPhysicalName(physicalName);
            }

            if ((options & ALIAS) != 0) {
                column.setAlias(alias);
            }

            if ((options & DESCRIPTION) != 0) {
                column.setDescription(desc);
            }

            if ((options & STEREOTYPE) != 0) {
                column.setUmlStereotype(stereotype);
            }
        } //end while
        dbEnum.close();

        //get object properties
        DbOOAdt ooType = item.getOoType();
        String typeUse = item.getTypeUse();
        OOTypeUseStyle style = item.getTypeUseStyle();
        JVVisibility visib = (JVVisibility) item.getVisibility();
        boolean isStatic = item.isStatic();
        boolean isFinal = item.isFinal();
        boolean isTransient = item.isTransient();
        boolean isVolatile = item.isVolatile();

        //for each field
        relN = item.getFields();
        dbEnum = relN.elements(DbJVDataMember.metaClass);
        while (dbEnum.hasMoreElements()) {

            m_changesmade = true;

            //set mandatory oo properties
            DbJVDataMember field = (DbJVDataMember) dbEnum.nextElement();
            field.setType(ooType);
            field.setTypeUse(typeUse);
            field.setTypeUseStyle(style);
            field.setVisibility(visib);
            field.setStatic(Boolean.valueOf(isStatic));
            field.setFinal(new Boolean(isFinal));
            field.setTransient(new Boolean(isTransient));
            field.setVolatile(new Boolean(isVolatile));

            //set optional properties
            if ((options & NAME) != 0) {
                field.setName(name);
            }

            if ((options & ALIAS) != 0) {
                field.setAlias(alias);
            }

            if ((options & DESCRIPTION) != 0) {
                field.setDescription(desc);
            }

            if ((options & STEREOTYPE) != 0) {
                field.setUmlStereotype(stereotype);
            }
        } //end while
        dbEnum.close();
    } //end propagateCommonItemValues()

    //
    // UNIT TEST
    //
    public static void main(String args[]) {

        //create dialog and display it
        PropagateCommonItemDialog dialog = new PropagateCommonItemDialog(null,
                "Propagate Common Items Towards Columns", true, null); //NOT LOCALIZABLE, unit test
        dialog.pack();
        dialog.setVisible(true);

    } //end main()
} //end PropagateCommonItemDialog
