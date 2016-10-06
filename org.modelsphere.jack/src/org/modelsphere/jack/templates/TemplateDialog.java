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

package org.modelsphere.jack.templates;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.Serializable;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.beans.impl.AbstractProperty;
import org.modelsphere.jack.awt.beans.impl.PropertyDialog;
import org.modelsphere.jack.awt.tree.*;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.debug.TestableWindow;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.forward.*;
import org.modelsphere.jack.srtool.integrate.IntegrateScopeDialog;

public class TemplateDialog extends JDialog implements TestableWindow {
    JPanel panel1 = new JPanel();
    Border border1;
    TitledBorder titledBorder1;
    TitledBorder titledBorder2;
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
    TitledBorder titledBorder3;
    TitledBorder titledBorder4;
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JButton jButton1 = new JButton();
    JButton jButton2 = new JButton();
    JButton jButton3 = new JButton();
    JButton jButton4 = new JButton();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JPanel jPanel4 = new JPanel(); // filling
    JPanel jPanel5 = new JPanel(); // filling
    private ArrayList m_tmpls;
    private ArrayList m_conds;
    private VariableScope m_variableList;
    private ArrayList m_propertyList;
    private CheckTreeNode m_fieldTree;
    private String m_scopedir = null;

    private static final String NONE = LocaleMgr.screen.getString("None");
    private static final String PARAMETER_FILE = LocaleMgr.screen.getString("ParameterFile");
    private static final String CONDITIONS = LocaleMgr.screen.getString("Conditions");
    private static final String SET_SCOPE = LocaleMgr.screen.getString("SetScope");
    private static final String CANCEL = LocaleMgr.screen.getString("Cancel");
    private static final String GENERATE = LocaleMgr.screen.getString("Generate");
    private static final String GENERATION_VARIABLES = LocaleMgr.screen
            .getString("SetGenerationVariables");
    private static final String CLOSE = LocaleMgr.screen.getString("Close");

    private TemplateDialog(Frame frame, String title, boolean modal) {
        super(frame, title, modal);
    }

    private TemplateDialog(Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
    }

    private static final String TITLE = LocaleMgr.screen
            .getString("GENERATION_FROM_TEMPLATES_TITLE");

    // Two constructors
    public TemplateDialog(Frame frame, ArrayList tmpls, ArrayList conds,
            VariableScope variableList, CheckTreeNode fieldTree, String scopeDir) {
        this(frame, TITLE, false);
        init(tmpls, conds, variableList, fieldTree, scopeDir);
    }

    public TemplateDialog(Dialog owner, ArrayList tmpls, ArrayList conds,
            VariableScope variableList, CheckTreeNode fieldTree, String scopeDir) {
        this(owner, TITLE, false);
        init(tmpls, conds, variableList, fieldTree, scopeDir);
    }

    // Called by one of the two constructors
    private void init(ArrayList tmpls, ArrayList conds, VariableScope variableList,
            CheckTreeNode fieldTree, String scopeDir) {
        m_tmpls = tmpls;
        m_conds = conds;

        // build a property list from variables
        m_variableList = variableList;
        m_propertyList = buildPropertyListFromVariables(variableList);

        m_fieldTree = fieldTree;
        m_scopedir = setScopeDir(scopeDir);
        jbInit();
        if ((m_propertyList == null) || (m_propertyList.isEmpty())) {
            jButton3.setEnabled(false);
        } // end if
    } // end init()

    private String setScopeDir(String scopeDir) {
        String result = scopeDir;
        // set result to null if there's no .scp file in the directory
        return result;
    }

    public void center() {
        AwtUtil.centerWindow(this);
    }

    private ArrayList getSelectedRules(TemplateCheckableList list) {
        ArrayList selectedRules = new ArrayList();
        TreeModel model = list.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        Enumeration enumeration = root.preorderEnumeration();
        boolean atLeastOneSelected = false;
        while (enumeration.hasMoreElements()) {
            Object element = enumeration.nextElement();
            if (element instanceof TemplateCheckListNode) {
                TemplateCheckListNode node = (TemplateCheckListNode) element;
                if (node.isSelected()) {
                    Rule rule = node.m_rule;
                    selectedRules.add(rule);
                } // end if
            } // end if
        }// end while

        return selectedRules;
    }

    private ArrayList getSetConditions(ModifierCheckableList list) {
        ArrayList setConditions = new ArrayList();
        TreeModel model = list.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        Enumeration enumeration = root.preorderEnumeration();
        boolean atLeastOneSelected = false;
        while (enumeration.hasMoreElements()) {
            Object element = enumeration.nextElement();
            if (element instanceof ModifierCheckListNode) {
                ModifierCheckListNode node = (ModifierCheckListNode) element;
                VariableDecl.VariableStructure variable = node.getVariable();
                Boolean value = node.isSelected() ? Boolean.TRUE : Boolean.FALSE;
                variable.setValue(value);
                setConditions.add(variable);
            } // end if
        }// end while

        return setConditions;
    }

    private void enableGenerateButton(TemplateCheckableList list) {
        TreeModel model = list.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        Enumeration enumeration = root.preorderEnumeration();
        boolean atLeastOneSelected = false;
        while (enumeration.hasMoreElements()) {
            Object element = enumeration.nextElement();
            if (element instanceof TemplateCheckListNode) {
                TemplateCheckListNode node = (TemplateCheckListNode) element;
                if (node.isSelected()) {
                    atLeastOneSelected = true;
                    break;
                } // end if
            } // end if
        } // end while

        jButton2.setEnabled(atLeastOneSelected);
    }

    // Listen for when the selection changes.
    private void addListListeners(final TemplateCheckableList list) {
        list.addCheckListener(new CheckListener() {
            public void itemChecked(CheckEvent e) {
                enableGenerateButton(list);
            }
        });
    } // end of addListeners()

    private TemplateCheckableList createTemplateList(String name, ArrayList optionNames) {

        final TemplateCheckableList list = new TemplateCheckableList(name, optionNames);

        // Listen for when the selection changes.
        addListListeners(list);
        return list;
    }

    private ModifierCheckableList createModifierList(String name, ArrayList optionNames) {

        final ModifierCheckableList list = new ModifierCheckableList(name, optionNames);

        return list;
    }

    boolean isCancelled = false;

    private void addListeners() {
        final TemplateDialog thisDialog = this;

        // cancel button
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                isCancelled = true;
                thisDialog.dispose();
            } // end actionPerformed()
        });

        // generate button
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                m_selectedRules = getSelectedRules(thisDialog.m_list);
                thisDialog.dispose();
            } // end actionPerformed()
        });

        // scope button
        if (m_scopedir == null) {
            jButton4.setEnabled(false);
        } else {
            jButton4.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    // create a dialog and popup it now
                    File scopeParamFile = null;
                    IntegrateScopeDialog dialog = new IntegrateScopeDialog(m_fieldTree,
                            scopeParamFile, thisDialog, m_scopedir, IntegrateScopeDialog.GENERATION);
                    dialog.setVisible(true);
                } // end actionPerformed()
            });
        }

        // options button
        if (m_propertyList == null) {
            jButton3.setEnabled(false);
        } else {
            final JDialog owner = this;
            jButton3.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    if (!m_propertyList.isEmpty()) {
                        // create property panel
                        JDialog dialog2 = new PropertyDialog(owner, GENERATION_VARIABLES, CLOSE,
                                m_propertyList);

                        // and display it
                        dialog2.pack();
                        dialog2.setSize((int) (dialog2.getWidth() * 3), dialog2.getHeight()); // it was not wide enough
                        AwtUtil.centerWindow(dialog2);
                        dialog2.setVisible(true);

                        // reset variables' value according property list
                        resetValue(m_variableList, m_propertyList);
                    } // end if
                } // end actionPerformed()
            });
        } // end if
    } // addListeners()

    // called by sms.or.dbms.features.DefaultForwardWizardObjectsPage and
    // TemplateDialog
    public static ArrayList buildPropertyListFromVariables(VariableScope variableScope) {
        ArrayList propertyList = new ArrayList();

        // For each external variable
        Iterator iter = variableScope.getIterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            VariableDecl.VariableStructure variable = (VariableDecl.VariableStructure) entry
                    .getValue();
            if (variable.isExtern()) {
                int type = variable.getType();
                if (type == VariableDecl.BOOLEAN) {
                    String name = variable.getDisplayName();
                    Boolean value = (Boolean) variable.getValue();
                    propertyList.add(new AbstractProperty.BooleanProperty(name, value
                            .booleanValue()));
                } // end if
            } // end if
        } // end while

        return propertyList;
    } // end buildPropertyList()

    // reset variables' value according property list
    // called by sms.or.dbms.features.DefaultForwardWizardObjectsPage and
    // TemplateDialog
    public static void resetValue(VariableScope variableScope, ArrayList propertyList) {
        Iterator iter = propertyList.iterator();
        while (iter.hasNext()) {
            AbstractProperty property = (AbstractProperty) iter.next();
            String name = property.getName();
            Serializable value = property.getValue();
            Iterator iter2 = variableScope.getIterator();
            while (iter2.hasNext()) {
                Map.Entry entry = (Map.Entry) iter2.next();
                VariableDecl.VariableStructure variable = (VariableDecl.VariableStructure) entry
                        .getValue();
                if (variable.getDisplayName().equals(name)) {
                    variable.setValue(value);
                    break;
                } // end if
            } // end while
        } // end while
    } // end resetValue()

    private ArrayList m_selectedRules = null;

    public ArrayList getSelectedRules() {
        return m_selectedRules;
    }

    private ArrayList m_setConditions = null; // set by the Generate button

    public void getSetConditions(VariableScope varlist) {
        if (m_setConditions != null) {
            Iterator iter = m_setConditions.iterator();
            while (iter.hasNext()) {
                VariableDecl.VariableStructure variable = (VariableDecl.VariableStructure) iter
                        .next();
                String varname = variable.getName();
                Serializable value = variable.getValue();
                varlist.setVariable(varname, value);
            } // end while
        } // end if
    } // end getSetConditions()

    private TemplateCheckableList m_list = null;

    private void initialize() {
        // localize button names
        jButton1.setText(CANCEL);
        jButton2.setText(GENERATE);
        jButton3.setText(GENERATION_VARIABLES + "...");
        jButton4.setText(SET_SCOPE);

        BorderLayout layout2 = new BorderLayout();
        BorderLayout layout3 = new BorderLayout();
        jPanel2.setLayout(layout2);

        // Add listeners
        addListeners();

        // Create templates
        String name = LocaleMgr.screen.getString("Templates");
        m_list = createTemplateList(name, m_tmpls);

        // Create the scroll pane and add the tree to it.
        JScrollPane listView = new JScrollPane(m_list);
        jPanel2.add(listView, "Center"); // NOT LOCALIZABLE, swing constant

        enableGenerateButton(m_list);
    }

    private void jbInit() {
        border1 = BorderFactory.createRaisedBevelBorder();
        titledBorder1 = new TitledBorder("1");
        titledBorder2 = new TitledBorder("2");
        titledBorder3 = new TitledBorder(LocaleMgr.screen.getString("TemplatesToProcess"));
        titledBorder4 = new TitledBorder(LocaleMgr.screen.getString("Conditions"));
        this.addWindowListener(new TemplateDialog_this_windowAdapter(this));
        panel1.setLayout(gridBagLayout1);
        jPanel1.setLayout(gridBagLayout2);
        jPanel2.setBorder(titledBorder3);
        getContentPane().add(panel1);
        panel1.add(jPanel1, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.SOUTH,
                GridBagConstraints.HORIZONTAL, new Insets(6, 12, 12, 12), 0, 0));

        // First row : SetScope, SetParams, Filling
        jPanel1.add(jButton4, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 12), 30,
                0));
        jPanel1.add(jButton3, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 12), 30,
                0));
        jPanel1.add(jPanel4, new GridBagConstraints(2, 0, 2, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        // Second row : Filling, Generate, Cancel
        jPanel1.add(jPanel5, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(jButton2, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(6, 0, 0, 12), 20,
                0));
        jPanel1.add(jButton1, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(6, 0, 0, 12), 20,
                0));

        panel1.add(jPanel2, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(12, 12, 6, 6), 0, 0));

        initialize();
    }

    // OK
    void button1_actionPerformed(ActionEvent e) {
        dispose();
    }

    // Cancel
    void button2_actionPerformed(ActionEvent e) {
        dispose();
    }

    void this_windowClosing(WindowEvent e) {
        dispose();
    }

    static class TemplateCheckableList extends CheckableTree {

        public TemplateCheckableList(String name, ArrayList optionNames) {
            super(getDefaultModel(name, optionNames));
        }

        protected static CheckTreeModel getDefaultModel(String name, ArrayList optionNames) {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode(name, true);
            DefaultMutableTreeNode node;

            // 1) fill externalDisplayList
            ArrayList externalDisplayList = new ArrayList();
            for (int i = 0; i < optionNames.size(); i++) {
                Rule rule = (Rule) optionNames.get(i);
                ExternModifier externModifier = rule.externModifier;
                ExternalDisplay display = null;
                if (externModifier != null) {
                    String value = externModifier.getValue();
                    if (value != null) {
                        display = new ExternalDisplay(value, rule);
                    } else {
                        String ruleName = rule.m_ruleName;
                        display = new ExternalDisplay("DISPLAY=\"" + ruleName + "\"", rule); // NOT LOCALIZABLE
                    } // end if
                } // end if

                externalDisplayList.add(display);
            } // end for

            // 2) sort externalDisplayList
            Collections.sort(externalDisplayList);

            // 3 add checklistnode
            for (int i = 0; i < externalDisplayList.size(); i++) {
                ExternalDisplay display = (ExternalDisplay) externalDisplayList.get(i);
                Rule rule = display.getRule();
                String displayName = display.getDisplayName();
                boolean checked = display.getEnabled();
                node = new TemplateCheckListNode(rule, displayName, checked);
                root.add(node);
            } // end for

            return new CheckTreeModel(root);
        }
    } // end of TemplateCheckableList

    static class ModifierCheckableList extends CheckableTree {

        public ModifierCheckableList(String name, ArrayList optionNames) {
            super(getDefaultModel(name, optionNames));
        }

        protected static CheckTreeModel getDefaultModel(String name, ArrayList optionNames) {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode(name, true);
            DefaultMutableTreeNode node;

            for (int i = 0; i < optionNames.size(); i++) {
                VariableDecl.VariableStructure variable = (VariableDecl.VariableStructure) optionNames
                        .get(i);
                Object value = variable.getValue();
                boolean isSelected = false;
                if (value.equals(Boolean.TRUE)) {
                    isSelected = true;
                }
                node = new ModifierCheckListNode(variable, isSelected);
                root.add(node);
            } // end for

            return new CheckTreeModel(root);
        }
    } // end of TemplateCheckableList

    //
    // INNER CLASSES
    //
    private static final class TemplateCheckListNode extends CheckTreeNode {
        Rule m_rule;

        TemplateCheckListNode(Rule rule, String displayName, boolean checked) {
            super(displayName, false, checked);
            m_rule = rule;
        }
    } // end TemplateCheckListNode

    private static final class ModifierCheckListNode extends CheckTreeNode {
        VariableDecl.VariableStructure m_variable;
        boolean m_selected;

        ModifierCheckListNode(VariableDecl.VariableStructure variable, boolean isSelected) {
            super(variable.getName(), false, isSelected);
            m_variable = variable;
            m_selected = isSelected;
        }

        VariableDecl.VariableStructure getVariable() {
            return m_variable;
        }
    } // end TemplateCheckListNode

    // INNER CLASS
    private static class ExternalDisplay implements Comparable {
        private String m_display;
        private int m_order = 100; // default order
        private Rule m_rule;
        private boolean m_enabled = false; // not selected by default

        private void setOption(String param) {
            Debug.trace("SETOPTION(" + param + ")"); // NOT LOCALIZABLE
            int equalIdx = param.indexOf('=');
            String option, value;
            if (equalIdx != -1) {
                option = param.substring(0, equalIdx);
                value = param.substring(equalIdx + 1);
            } else {
                option = param;
                value = null;
            }

            if (option.equals("DISPLAY")) { // NOT LOCALIZABLE
                this.m_display = value.substring(1, value.length() - 1); // remove
                // quotes
            } else if (option.equals("ORDER")) { // NOT LOCALIZABLE
                int intValue = Integer.parseInt(value);
                this.m_order = intValue;
            } else if (option.equals("ENABLED")) { // NOT LOCALIZABLE
                this.m_enabled = true;
            }
        } // setOption()

        ExternalDisplay(String value, Rule rule) {
            m_rule = rule;
            // options are comma-separated
            StringTokenizer st = new StringTokenizer(value, ",");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                setOption(token);
            } // end while
        } // end ExternalDisplay

        Rule getRule() {
            return m_rule;
        }

        String getDisplayName() {
            return m_display;
        }

        boolean getEnabled() {
            return m_enabled;
        }

        public int compareTo(Object obj) {
            if (!(obj instanceof ExternalDisplay)) {
                throw new ClassCastException();
            }

            ExternalDisplay display = (ExternalDisplay) obj;
            int diff = this.m_order - display.m_order;
            if (diff != 0) {
                return diff;
            } else {
                // if order are equal, sort by alphabetical order
                return this.m_display.compareTo(display.m_display);
            }
        }
    } // end ExternalDisplay

    //
    // UNIT TEST
    //
    private static org.modelsphere.jack.srtool.forward.VariableScope getVarList() {
        org.modelsphere.jack.srtool.forward.VariableScope variableScope = new org.modelsphere.jack.srtool.forward.VariableScope(
                "test"); // NOT LOCALIZABLE, unit test
        final boolean isExtern = true;
        try {
            VariableDecl.declare(variableScope, "alpha", VariableDecl.BOOLEAN, new BooleanModifier(
                    true), isExtern); // NOT LOCALIZABLE,
            // unit test
            VariableDecl.declare(variableScope, "beta", VariableDecl.BOOLEAN, new BooleanModifier(
                    false), isExtern); // NOT LOCALIZABLE,
            // unit test
        } catch (RuleException ex) {
            // do nothing, just return an empty variable list
        }

        return variableScope;
    }

    private static void test(Object fieldTreeObject, String scopedir) {
        CheckTreeNode fieldTree = (CheckTreeNode) fieldTreeObject;
        ArrayList m_tmpls = new ArrayList();
        ArrayList m_conds = new ArrayList();
        JDialog owner = new JDialog();
        org.modelsphere.jack.srtool.forward.VariableScope varList = getVarList();

        // create the dialog
        TemplateDialog diag = new TemplateDialog(owner, m_tmpls, m_conds, varList, fieldTree,
                scopedir);
        diag.pack();
        diag.center();
        diag.setVisible(true);
    } // end test

    public TemplateDialog() {
    }

    public Window createTestWindow(Container owner) {
        CheckTreeNode fieldTree = (CheckTreeNode) getTestFieldTree();
        ArrayList m_tmpls = new ArrayList();
        ArrayList m_conds = new ArrayList();
        JDialog owner2 = new JDialog();
        org.modelsphere.jack.srtool.forward.VariableScope varList = getVarList();

        // create the dialog
        String scopedir = "D:\\java\\modelsphere\\plugins\\classes\\org\\grandite\\sms\\plugins\\oracle"; // NOT
        // LOCALIZABLE,
        // filename
        TemplateDialog diag = new TemplateDialog(owner2, m_tmpls, m_conds, varList, fieldTree,
                scopedir);
        diag.pack();
        diag.center();
        return diag;
    }

    private static Object getTestFieldTree() {
        Object fieldTree = null;
        try {
            // org.modelsphere.sms.Application.initMeta()
            Class claz = Class.forName("org.modelsphere.sms.Application"); // NOT
            // LOCALIZABLE,
            // unit
            // test
            java.lang.reflect.Method method = claz.getDeclaredMethod("initMeta", new Class[] {}); // NOT LOCALIZABLE, unit test
            method.invoke(null, new Object[] {});

            // util =
            // org.modelsphere.sms.SMSIntegrateModelUtil.getSingleInstance()
            Class modelUtilClass = Class.forName("org.modelsphere.sms.SMSIntegrateModelUtil"); // NOT
            // LOCALIZABLE,
            // unit
            // test
            method = modelUtilClass.getDeclaredMethod("getSingleInstance", new Class[] {}); // NOT LOCALIZABLE, unit test
            Object modelUtil = method.invoke(null, new Object[] {});

            // model = new org.modelsphere.sms.or.generic.db.DbGEDataModel();
            Class dataModelClass = Class.forName("org.modelsphere.sms.or.generic.db.DbGEDataModel"); // NOT
            // LOCALIZABLE,
            // unit
            // test
            java.lang.reflect.Constructor constr = dataModelClass.getConstructor(new Class[] {});
            Object model = constr.newInstance(new Object[] {});

            // fieldTree = util.getFieldTree(model);
            Class pack = Class.forName("org.modelsphere.sms.db.DbSMSPackage"); // NOT
            // LOCALIZABLE,
            // unit
            // test
            method = modelUtilClass.getDeclaredMethod("getFieldTree", new Class[] { pack }); // NOT LOCALIZABLE, unit test
            fieldTree = method.invoke(modelUtil, new Object[] { model });
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }

        return fieldTree;
    } // end getTestFieldTree

    // Unit test
    // Use reflection to create sms objects to avoid compile-time
    // sms-dependency.
    public static void main(String[] args) {
        try {
            Object fieldTree = getTestFieldTree();
            String scopedir = "D:\\java\\modelsphere\\plugins\\classes\\org\\grandite\\sms\\plugins\\oracle"; // NOT
            // LOCALIZABLE,
            // filename
            test(fieldTree, scopedir);

        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        } // end try
    } // end main()
} // end of TemplateDialog

// TemplateDialog_this_windowAdapter
class TemplateDialog_this_windowAdapter extends WindowAdapter {
    TemplateDialog adaptee;

    TemplateDialog_this_windowAdapter(TemplateDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void windowClosing(WindowEvent e) {
        adaptee.this_windowClosing(e);
        adaptee.isCancelled = true;
    }
}
