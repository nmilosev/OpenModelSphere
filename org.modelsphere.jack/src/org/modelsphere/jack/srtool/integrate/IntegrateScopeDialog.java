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

package org.modelsphere.jack.srtool.integrate;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.tree.*;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class IntegrateScopeDialog extends JDialog implements ActionListener {

    private static final String SCOPE_PARAM_DIR = "ParamDir"; // NOT LOCALIZABLE, property key
    public static final int INTEGRATION = 0;
    public static final int GENERATION = 1;

    private static final String INTEGRATION_SCOPE = LocaleMgr.screen.getString("integScope");
    private static final String GENERATION_SCOPE = LocaleMgr.screen.getString("generationScope");
    private static final String kLoadTitle = LocaleMgr.screen.getString("loadTitle");
    private static final String kSaveTitle = LocaleMgr.screen.getString("saveTitle");

    private static final ExtensionFileFilter scopeFileFilter = new ExtensionFileFilter("scp",
            LocaleMgr.misc.getString("ScopeFileDescription"));

    private CheckTreeNode fieldTree;
    private CheckableTree tree;
    private JTextField scopeFileName = new JTextField(LocaleMgr.misc.getString("DefaultScopeFile"));
    private JPanel containerPanel = new JPanel();
    private JPanel controlBtnPanel = new JPanel();
    private GridLayout gridLayout1 = new GridLayout();
    private JButton loadBtn = new JButton(LocaleMgr.screen.getString("loadDots"));
    private JButton saveBtn = new JButton(LocaleMgr.screen.getString("saveDots"));
    private JButton closeBtn = new JButton(LocaleMgr.screen.getString("OK"));
    private File scopeFile = null;
    private int m_mode; // either INTEGRATION or GENERATION

    // Constructor called by the main application
    public IntegrateScopeDialog(CheckTreeNode fieldTree, File scopeParamFile, int mode) {
        super(ApplicationContext.getDefaultMainFrame(), true);
        String scopeDir = getScopeParamDir();
        init(fieldTree, scopeParamFile, scopeDir, mode);
    } // end IntegrateScopeDialog()

    // Constructor called by unit testing
    public IntegrateScopeDialog(CheckTreeNode fieldTree, File scopeParamFile, Dialog owner,
            String scopeDir, int mode) {
        super(owner, true);
        init(fieldTree, scopeParamFile, scopeDir, mode);
    } // end IntegrateScopeDialog()

    // Called by one of the constructors
    private void init(CheckTreeNode fieldTree, File scopeParamFile, String scopeDir, int mode) {
        m_mode = mode;
        this.fieldTree = fieldTree;
        tree = new CheckableTree(fieldTree);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        if (scopeParamFile != null)
            setScopeFile(scopeParamFile);

        jbInit();
        this.pack();
        Dimension bestSize = AwtUtil.getBestDialogSize();

        setSize(Math.max(getWidth(), 400), bestSize.height);
        this.setLocationRelativeTo(ApplicationContext.getDefaultMainFrame());
    } // end init()

    void jbInit() {
        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        // Graphic Design Utility. Keep it into comments when modification are
        // done.

        /*
         * loadBtn.setText(''Load''); saveBtn.setText(''Save''); closeBtn.setText(''OK'');
         */
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // this.setTitle(kTitle);
        switch (m_mode) {
        case INTEGRATION:
            setTitle(INTEGRATION_SCOPE);
            break;
        case GENERATION:
            setTitle(GENERATION_SCOPE);
            break;
        } // end switch()

        getContentPane().add(containerPanel);
        containerPanel.setLayout(new GridBagLayout());
        scopeFileName.setEditable(false);
        JScrollPane scrollPanel = new JScrollPane(tree);
        gridLayout1.setHgap(5);
        controlBtnPanel.setLayout(gridLayout1);

        containerPanel.add(scopeFileName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(12, 12, 6, 12),
                0, 0));

        containerPanel
                .add(scrollPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 12, 5, 12), 0, 0));

        scrollPanel.setBorder(BorderFactory.createLineBorder(Color.darkGray));

        controlBtnPanel.add(loadBtn);
        controlBtnPanel.add(saveBtn);
        controlBtnPanel.add(closeBtn);
        containerPanel.add(controlBtnPanel, new GridBagConstraints(0, 2,
                GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST,
                GridBagConstraints.NONE, new Insets(12, 12, 12, 12), 0, 0));

        AwtUtil.normalizeComponentDimension(new JButton[] { loadBtn, saveBtn, closeBtn });

        loadBtn.addActionListener(this);
        saveBtn.addActionListener(this);
        closeBtn.addActionListener(this);

        getRootPane().setDefaultButton(closeBtn);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public final void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadBtn)
            loadScope();
        else if (e.getSource() == saveBtn)
            saveScope();
        else if (e.getSource() == closeBtn)
            dispose();
    }

    private void refreshFileName() {
        scopeFileName.setText(scopeFile != null ? scopeFile.getName() : "");
    }

    private void saveScope() {
        String scopeDir = getScopeDir();
        File file = (scopeFile != null ? scopeFile : (scopeDir != null ? new File(scopeDir) : null));
        ExtensionFileFilter[] filters = null;
        AwtUtil.FileAndFilter selection = AwtUtil.showSaveAsDialog(this, kSaveTitle, scopeFileFilter, filters, file);
        file = (selection == null) ? null : selection.getFile();
        
        if (file == null)
            return;
        try {
            saveScope(fieldTree, file);
            setScopeFile(file);
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(this, e);
        }
    }

    private void loadScope() {
        String scopeDir = getScopeDir();
        JFileChooser chooser = new JFileChooser(scopeDir);
        chooser.addChoosableFileFilter(scopeFileFilter);
        chooser.setFileFilter(scopeFileFilter);
        chooser.setDialogTitle(kLoadTitle);

        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            return;
        File file = chooser.getSelectedFile();
        if (file == null)
            return;

        try {
            loadScope(fieldTree, file);
            setScopeFile(file);
            tree.repaint();
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(this, e);
        }
    }

    private static String g_scopeDir;

    private String getScopeDir() {
        if (g_scopeDir == null) { // if was not initialized
            PropertiesSet appSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
            g_scopeDir = ApplicationContext.getDefaultWorkingDirectory();
        }

        return g_scopeDir;
    } // end getScopeDir()

    private void setScopeFile(File file) {
        scopeFile = file;
        g_scopeDir = file.getParent();
        setScopeParamDir(g_scopeDir);
        refreshFileName();
    }

    /*
     * Convert the integration scope <fieldTree> to a serializable format and serialize it to
     * <file>. In the serializable format, the property field is converted as follows: MetaClass ->
     * String containing the qualified class name MetaField -> String containing the field name
     * (unqualified) MetaField[] -> String[] containing the field names (unqualified) null -> null
     * (UDF entry).
     */
    public static void saveScope(CheckTreeNode fieldTree, File file) throws Exception {
        SavedNode[] savedClasses = new SavedNode[fieldTree.getChildCount()];
        for (int i = 0; i < savedClasses.length; i++) {
            CheckTreeNode classNode = (CheckTreeNode) fieldTree.getChildAt(i);
            MetaClass metaClass = (MetaClass) classNode.getUserObject();
            savedClasses[i] = new SavedNode(metaClass.getJClass().getName(), false, classNode
                    .toString(), classNode.isSelected());
            SavedNode[] savedFields = new SavedNode[classNode.getChildCount()];
            savedClasses[i].children = savedFields;
            for (int j = 0; j < savedFields.length; j++) {
                CheckTreeNode fieldNode = (CheckTreeNode) classNode.getChildAt(j);
                Object property = fieldNode.getUserObject();
                Object savedProp = null;
                boolean custom = false;
                if (property instanceof MetaField)
                    savedProp = ((MetaField) property).getJName();
                else if (property instanceof MetaClass)
                    savedProp = ((MetaClass) property).getJClass().getName();
                else if (property instanceof String) {
                    savedProp = property;
                    custom = true;
                } else if (property instanceof MetaField[]) {
                    MetaField[] fields = (MetaField[]) property;
                    savedProp = new String[fields.length];
                    for (int k = 0; k < fields.length; k++)
                        ((String[]) savedProp)[k] = fields[k].getJName();
                }
                savedFields[j] = new SavedNode(savedProp, custom, fieldNode.toString(), fieldNode
                        .isSelected());
            }
        }

        SavedNode savedRoot = new SavedNode(null, false, null, true);
        savedRoot.children = savedClasses;
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(savedRoot);
        out.close();
    }

    /*
     * Load from <file> the serialized form of an integration scope, then set the <selected> fields
     * of the integration scope <fieldTree> from the corresponding settings of the serialized form.
     * Note that the structure of <fieldTree> is not changed, only the nodes present in both
     * <fieldTree> and the serialized form are modified in <fieldTree>.
     */
    public static void loadScope(CheckTreeNode fieldTree, File file) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        SavedNode savedRoot = (SavedNode) in.readObject();
        in.close();

        SavedNode[] savedClasses = savedRoot.children;
        for (int i = 0; i < savedClasses.length; i++) {
            MetaClass metaClass = MetaClass.find((String) savedClasses[i].property);
            if (metaClass == null) // obsolete metaClass
                continue;
            CheckTreeNode classNode = findNode(fieldTree, metaClass);
            if (classNode == null) // imcompatible scopes if not found (ex.
                // Oracle vs Informix scope)
                continue;
            classNode.setSelected(savedClasses[i].selected);
            SavedNode[] savedFields = savedClasses[i].children;
            for (int j = 0; j < savedFields.length; j++) {
                Object savedProp = savedFields[j].property;
                Object property = convSavedProp(metaClass, savedProp);
                if (property == null && savedProp != null)
                    continue;
                CheckTreeNode fieldNode = findNode(classNode, property);
                if (fieldNode != null)
                    fieldNode.setSelected(savedFields[j].selected);
            }
        }
    }

    public static CheckTreeNode loadTemplateScope(Object plugin, String fileName) throws Exception {
        ObjectInputStream in = new ObjectInputStream(plugin.getClass()
                .getResourceAsStream(fileName));
        SavedNode savedRoot = (SavedNode) in.readObject();
        in.close();

        CheckTreeNode fieldTree = new CheckTreeNode(null, true, true);
        SavedNode[] savedClasses = savedRoot.children;
        for (int i = 0; i < savedClasses.length; i++) {
            if (!savedClasses[i].selected)
                continue;
            MetaClass metaClass = MetaClass.find((String) savedClasses[i].property);
            if (metaClass == null) // obsolete metaClass
                continue;
            CheckTreeNode classNode = new CheckTreeNode(metaClass, true, true, savedClasses[i].name);
            fieldTree.add(classNode);

            SavedNode[] savedFields = savedClasses[i].children;
            for (int j = 0; j < savedFields.length; j++) {
                if (!savedFields[j].selected)
                    continue;
                Object savedProp = savedFields[j].property;
                Object property = null;
                if (savedFields[j].custom)
                    property = savedFields[j].property;
                else
                    property = convSavedProp(metaClass, savedProp);
                if (property == null && savedProp != null)
                    continue;
                classNode.add(new CheckTreeNode(property, false, true, savedFields[j].name));
            }
        }
        return fieldTree;
    }

    // Return null if obsolete property or if UDF (if UDF, savedProp is null)
    private static Object convSavedProp(MetaClass metaClass, Object savedProp) {
        if (savedProp instanceof String) { // MetaClass or MetaField name
            if (((String) savedProp).startsWith("m_")) // NOT LOCALIZABLE
                return metaClass.getMetaField((String) savedProp);
            else
                return MetaClass.find((String) savedProp);
        } else if (savedProp instanceof String[]) { // array of field names
            // representing a path
            String[] strs = (String[]) savedProp;
            MetaField[] fields = new MetaField[strs.length];
            for (int i = 0; i < strs.length; i++) {
                if (i == 0)
                    fields[i] = metaClass.getMetaField(strs[i]);
                else
                    fields[i] = ((MetaRelationship) fields[i - 1]).getOppositeRel(null)
                            .getMetaClass().getMetaField(strs[i]);
                if (fields[i] == null)
                    return null;
            }
            return fields;
        }
        return null;
    }

    private static CheckTreeNode findNode(CheckTreeNode parentNode, Object property) {
        int nb = parentNode.getChildCount();
        for (int i = 0; i < nb; i++) {
            CheckTreeNode childNode = (CheckTreeNode) parentNode.getChildAt(i);
            Object childProp = childNode.getUserObject();
            if (property instanceof MetaField[]) {
                if (childProp instanceof MetaField[]
                        && DbObject.valuesAreEqual(property, childProp))
                    return childNode;
            } else if (property == childProp)
                return childNode;
        }
        return null;
    }

    private static class SavedNode implements Serializable {

        static final long serialVersionUID = 0;

        Object property; // 5 possibles values: String (Custom Action, MetaClass
        // or MetaField name), String[] (MetaField names),
        // or null (UDF).
        boolean custom; // Indicate if the property represent a custom field
        String name;
        boolean selected;
        SavedNode[] children;

        SavedNode(Object property, boolean custom, String name, boolean selected) {
            this.property = property;
            this.custom = custom;
            this.name = name;
            this.selected = selected;
            children = null;
        }
    } // End of class SavedNode

    public final File getScopeFile() {
        return scopeFile;
    }

    public String getScopeParamDir() {
        PropertiesSet appSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
        String dir = ApplicationContext.getDefaultWorkingDirectory();
        return appSet.getPropertyString(IntegrateScopeDialog.class, SCOPE_PARAM_DIR, dir);
    }

    public void setScopeParamDir(String dir) {
        PropertiesSet appSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
        appSet.setProperty(IntegrateScopeDialog.class, SCOPE_PARAM_DIR, dir);
    }

    //
    // UNIT TEST
    //
    /*
     * public static final void main(String[] args) { org.modelsphere.sms.Application.initMeta();
     * PropertiesManager.installDefaultPropertiesSet(); JDialog owner = new JDialog();
     * org.modelsphere.sms.SMSIntegrateModelUtil util =
     * org.modelsphere.sms.SMSIntegrateModelUtil.getSingleInstance();
     * org.modelsphere.sms.or.db.DbORDataModel model = new
     * org.modelsphere.sms.or.generic.db.DbGEDataModel(); CheckTreeNode fieldTree =
     * util.getFieldTree(model); File scopeParamFile = null; //may be null String scopedir =
     * "D:\\java\\mahogany_v1.1\\plugins\\classes\\org\\grandite\\sms\\plugins\\oracle" ; //NOT
     * LOCALIZABLE, unit test IntegrateScopeDialog dialog = new IntegrateScopeDialog(fieldTree,
     * scopeParamFile, owner, scopedir, GENERATION); AwtUtil.centerWindow(dialog); dialog.pack();
     * dialog.setVisible(true); } //end main()
     */
} // end IntegrateScopeDialog

