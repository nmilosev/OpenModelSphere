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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/
package org.modelsphere.sms.features.startupwizard;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.*;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel.CheckingMode;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.gui.wizard2.AbstractPage;
import org.modelsphere.jack.gui.wizard2.SectionHeader;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.jack.util.FileUtils;
import org.modelsphere.sms.features.international.LocaleMgr;
import org.modelsphere.sms.oo.java.db.DbJVPackage;

@SuppressWarnings("serial")
class UMLClassPackagePage extends AbstractPage {
    private static final String kJava_Packages_to_Reverse = LocaleMgr.screen
            .getString("Java_Packages_to_Reverse");
    private static final String kTitle = LocaleMgr.screen.getString("Packages");

    private StartupWizardModel model = null;

    private static class JARNode extends DefaultMutableTreeNode {
        String displayText;
        Icon icon;

        JARNode(File jarFile) {
            super(jarFile, true);
            displayText = jarFile.getName();
            icon = FileUtils.getIcon(jarFile);
        }

        @Override
        public String toString() {
            return displayText;
        }

        public Icon getIcon() {
            return icon;
        }

    }

    private static class PackageNode extends DefaultMutableTreeNode {
        String displayText;
        Icon icon;

        PackageNode(String packageName) {
            super(packageName, true);
            icon = DbJVPackage.metaClass.getIcon();
            int idx = packageName.lastIndexOf(".");
            if (idx > -1 && packageName.length() > idx + 1) {
                displayText = packageName.substring(idx + 1);
            } else {
                displayText = packageName;
            }
        }

        @Override
        public String toString() {
            return displayText;
        }

        public Icon getIcon() {
            return icon;
        }

    }

    private class PackagesModel extends DefaultTreeModel {
        private File homeJRE;

        private PackageNode javaLangNode;

        public PackagesModel(File homeJRE) {
            super(new DefaultMutableTreeNode("Root"));
            this.homeJRE = homeJRE;
            try {
                load();
            } catch (IOException e) {
                ExceptionHandler.processUncatchedException(UMLClassPackagePage.this, e);
            }
        }

        private void load() throws IOException {
            if (homeJRE == null) {
                return;
            }

            DefaultMutableTreeNode root = ((DefaultMutableTreeNode) getRoot());

            for (Iterator<String> jars = StartupWizardModel.DEFAULT_JRE_JARS.iterator(); jars
                    .hasNext();) {
                String jar = jars.next();
                File jarfile = new File(homeJRE, jar);
                if (jarfile.exists()) {
                    JARNode jarnode = new JARNode(jarfile);
                    root.add(jarnode);
                    load(jarnode, jarfile);
                }
            }
        }

        private void load(JARNode parent, File jarfile) throws IOException {
            JarFile jar = new JarFile(jarfile);
            try {
                Enumeration<JarEntry> entries = jar.entries();
                ArrayList<String> packages = new ArrayList<String>();
                if (entries != null) {
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();
                        if (jarEntry.isDirectory())
                            continue;
                        String name = jarEntry.getName();
                        if (!name.endsWith(".class")) {
                            continue;
                        }

                        String packagename = name.substring(0, name.length() - 6);
                        packagename = packagename.replace("/", ".");
                        packagename = packagename.replace(File.separator, ".");
                        int idx = packagename.lastIndexOf(".");
                        if (idx > -1) {
                            packagename = packagename.substring(0, idx);
                        }
                        if (!packages.contains(packagename))
                            packages.add(packagename);
                    }
                }

                HashMap<String, PackageNode> packageNodes = new HashMap<String, PackageNode>();
                boolean rtJAR = jarfile.getName().equals("rt.jar");

                // create a node for each package
                for (String apackage : packages) {
                    PackageNode node = new PackageNode(apackage);
                    packageNodes.put(apackage, node);

                    // ensure each parent package has a node
                    int idx = apackage.lastIndexOf(".");
                    while (idx > -1) {
                        apackage = apackage.substring(0, idx);
                        node = new PackageNode(apackage);
                        if (rtJAR && apackage.equals("java.lang")) {
                            javaLangNode = node;
                        }
                        packageNodes.put(apackage, node);
                        idx = apackage.lastIndexOf(".");
                    }
                }

                // sort
                ArrayList<String> keys = new ArrayList<String>(packageNodes.keySet());
                Collections.sort(keys);

                for (Iterator<String> packagesIterator = keys.iterator(); packagesIterator
                        .hasNext();) {
                    String apackage = packagesIterator.next();
                    PackageNode node = packageNodes.get(apackage);
                    DefaultMutableTreeNode nodeParent = parent;
                    int idx = apackage.lastIndexOf(".");
                    if (idx > -1) {
                        apackage = apackage.substring(0, idx);
                        nodeParent = packageNodes.get(apackage);
                    }
                    nodeParent.add(node);
                }
            } finally {
                jar.close();
            }
        }

        public PackageNode getJavaLangNode() {
            return javaLangNode;
        }

    }

    private Tree tree = new Tree();
    private TreeCheckingModel checkingModel;

    private static class Tree extends CheckboxTree {

        public Icon getIcon(int row) {
            DefaultMutableTreeNode node = getNodeForRow(row);
            if (node != null) {
                if (node instanceof PackageNode) {
                    return ((PackageNode) node).getIcon();
                }
                if (node instanceof JARNode) {
                    return ((JARNode) node).getIcon();
                }
            }
            return null;
        }

        public DefaultMutableTreeNode getNodeForRow(int row) {
            TreePath path = getPathForRow(row);
            if (path != null) {
                Object component = path.getLastPathComponent();
                return ((component != null) && (component instanceof DefaultMutableTreeNode)) ? (DefaultMutableTreeNode) component
                        : null;
            }
            return null;
        }

    }

    private static class Renderer extends DefaultCheckboxTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean arg2,
                boolean arg3, boolean arg4, int row, boolean arg6) {
            Component component = super.getTreeCellRendererComponent(tree, value, arg2, arg3, arg4,
                    row, arg6);
            Icon icon = ((Tree) tree).getIcon(row);
            if (icon != null)
                label.setIcon(icon);
            return component;
        }

    }

    UMLClassPackagePage(StartupWizardModel model) {
        super(new GridBagLayout());
        this.model = model;
        init();
    }

    private void init() {
        SectionHeader header = new SectionHeader(kJava_Packages_to_Reverse);

        add(header, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        add(new JScrollPane(tree), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(6, 24, 0, 0), 0, 0));

        tree.setModel(new PackagesModel(null));
        tree.setRootVisible(false);
        tree.setRowHeight(tree.getRowHeight() + 2);
        tree.setCellRenderer(new Renderer());

    }

    @Override
    public void load() throws DbException {
        PackagesModel treemodel = (PackagesModel) tree.getModel();
        File home = model.getJREHome();
        if ((treemodel.homeJRE == null ^ home == null)
                || (treemodel.homeJRE != null && home != null && !home.equals(treemodel.homeJRE))) {
            treemodel = new PackagesModel(home);
            checkingModel = new DefaultTreeCheckingModel(treemodel);
            tree.setModel(treemodel);
            tree.setCheckingModel(checkingModel);
            checkingModel.setCheckingMode(CheckingMode.SIMPLE);

            if (treemodel.getJavaLangNode() != null) {
                ArrayList<DefaultMutableTreeNode> path = new ArrayList<DefaultMutableTreeNode>();
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) treemodel
                        .getJavaLangNode().getParent();
                while (parent != null) {
                    path.add(0, parent);
                    parent = (DefaultMutableTreeNode) parent.getParent();
                }
                tree.expandPath(new TreePath(path.toArray()));
                path.add(treemodel.getJavaLangNode());
                checkingModel.setCheckingPath(new TreePath(path.toArray()));
            }
            checkingModel.setCheckingMode(CheckingMode.PROPAGATE);
        }

    }

    @Override
    public void save() throws DbException {
        ArrayList<String> packages = new ArrayList<String>();

        if (checkingModel != null) {
            TreePath[] checkedPaths = checkingModel.getCheckingPaths();
            if (checkedPaths != null) {
                for (int i = 0; i < checkedPaths.length; i++) {
                    Object component = checkedPaths[i].getLastPathComponent();
                    if (component instanceof PackageNode) {
                        packages.add((String) ((PackageNode) component).getUserObject());
                    }
                }
            }
        }

        model.setPackages(packages);
    }

    @Override
    public void rollback() throws DbException {
    }

    @Override
    public String getTitle() {
        return kTitle;
    }

}
