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

package org.modelsphere.jack.awt.beans.impl;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.Serializable;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.beans.BeanDialog;
import org.modelsphere.jack.debug.Debug;

public class BeanDialogImpl implements BeanDialog {
    private static final int ICON_KIND = BeanInfo.ICON_COLOR_16x16;
    private static final int ROW_HEIGHT = 20;
    private static final int BEAN_TYPE = 0;
    private static final int PROPERTYLIST_TYPE = 1;

    // Dialog's model
    private int m_modelType; // EITHER BEAN_TYPE OR ARRAYLIST_TYPE
    private Serializable m_bean = null;
    private BeanInfo m_beaninfo = null;
    private ArrayList m_primitiveList = null;
    private static int g_defaultTooltipDismissDelay = 0;

    // CONSTRUCTORS
    public BeanDialogImpl(Serializable bean, BeanInfo beaninfo) {
        m_modelType = BEAN_TYPE;
        m_bean = bean;
        m_beaninfo = beaninfo;
        g_defaultTooltipDismissDelay = ToolTipManager.sharedInstance().getDismissDelay();
    }

    public BeanDialogImpl(ArrayList primitiveList) {
        m_modelType = PROPERTYLIST_TYPE;
        m_primitiveList = primitiveList;
        g_defaultTooltipDismissDelay = ToolTipManager.sharedInstance().getDismissDelay();
    }

    // ////////////////////////////////////////////
    // IMPLEMENTS BeanDialog
    public void showPropertyDialog(JFrame owner) {
        JDialog dialog = new JDialog(owner);
        showDialog(dialog);
    }

    public void showPropertyDialog(JDialog owner) {
        JDialog dialog = new JDialog(owner);
        showDialog(dialog);
    }

    //
    // /////////////////////////////////////////////////

    public ArrayList getPrimitiveList() {
        return m_primitiveList;
    }

    private void showDialog(JDialog dialog) {
        switch (m_modelType) {
        case BEAN_TYPE:
            BeanDescriptor desc = m_beaninfo.getBeanDescriptor();
            if (desc != null) {
                String title = desc.getDisplayName();
                dialog.setTitle(title);
            }
            break;
        }

        Container parent = dialog.getContentPane();

        JPanel panel = createPanel();
        JScrollPane scrollpane = new JScrollPane(panel);
        parent.add(scrollpane);

        dialog.pack();
        AwtUtil.centerWindow(dialog);
        dialog.setVisible(true);
    } // end showDialog()

    private PropertyTable m_table = null;

    public JTable getTable() {
        return m_table;
    }

    public JPanel createPanel() {
        m_table = createPropertyTable();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_table, BorderLayout.CENTER);
        return panel;
    }

    private PropertyTable createPropertyTable() {
        BeanFrameInfo info = new BeanFrameInfo();
        // draw table
        PropertyTableModel dm = null;
        switch (m_modelType) {
        case BEAN_TYPE:
            dm = new PropertyTableModel(m_bean, m_beaninfo, info);
            break;
        case PROPERTYLIST_TYPE:
            dm = new PropertyTableModel(m_primitiveList, info);
            break;
        } // end switch

        PropertyTable table = new PropertyTable(this, dm);
        table.setName("Design"); // NOT LOCALIZABLE - For QA
        table.setRowHeight(ROW_HEIGHT);
        table.getTableHeader().setReorderingAllowed(false);

        return table;
    } // createPropertyTable()

    public void stopCellEditing() {
        // if a row is being editing, stop the row's edition
        int row = m_table.getEditingRow();
        if (row != -1) {
            TableCellEditor editor = m_table.getCellEditor(row, 1);
            editor.stopCellEditing();
        }
    }

    private static class PropertyTableModel extends AbstractTableModel {
        private int g_counter;
        private int m_modelType;
        private Serializable m_bean;
        private ArrayList m_propertyList;
        private int m_nbrows;
        private Image m_icon;
        private String m_title;
        private BeanFrameInfo m_info;

        // one for each property
        private String[] m_names;
        private Method[] m_readMethods;
        private Method[] m_writeMethods;
        private Class[] m_propertyTypes;

        PropertyTableModel(ArrayList propertyList, BeanFrameInfo beanFrameInfo) {
            m_modelType = PROPERTYLIST_TYPE;
            m_propertyList = propertyList;
            m_info = beanFrameInfo;

            m_nbrows = 0;
            int nb = propertyList.size();
            m_names = new String[nb];
            m_readMethods = new Method[nb];
            m_writeMethods = new Method[nb];
            m_propertyTypes = new Class[nb];

            Iterator iterator = propertyList.iterator();
            while (iterator.hasNext()) {
                AbstractProperty.BooleanProperty prop = (AbstractProperty.BooleanProperty) iterator
                        .next();
                m_names[m_nbrows] = prop.getName();
                Class claz = AbstractProperty.BooleanProperty.class;
                try {
                    m_readMethods[m_nbrows] = claz.getDeclaredMethod("getValue", new Class[] {}); // NOT LOCALIZABLE
                    m_writeMethods[m_nbrows] = claz.getDeclaredMethod("setValue",
                            new Class[] { Object.class }); // NOT
                    // LOCALIZABLE

                    Class type = prop.getType();
                    String typename = type.toString();
                    m_propertyTypes[m_nbrows] = type;

                    m_nbrows++;
                } catch (Exception ex) {
                    Debug.trace(ex);
                }
            } // end while
        } // end PropertyTableModel()

        PropertyTableModel(Serializable bean, BeanInfo info, BeanFrameInfo beanFrameInfo) {
            m_modelType = BEAN_TYPE;
            m_bean = bean;
            m_info = beanFrameInfo;

            BeanDescriptor desc = info.getBeanDescriptor();
            if (desc != null) {
                m_title = desc.getDisplayName();
            }

            m_icon = info.getIcon(BeanInfo.ICON_COLOR_16x16);
            m_nbrows = 0;
            PropertyDescriptor[] propDescs = info.getPropertyDescriptors();
            if (propDescs != null) {
                int nb = propDescs.length;
                m_names = new String[nb];
                m_readMethods = new Method[nb];
                m_writeMethods = new Method[nb];
                m_propertyTypes = new Class[nb];

                for (int i = 0; i < nb; i++) {
                    PropertyDescriptor propDesc = propDescs[i];

                    if (!propDesc.isHidden()) {
                        m_names[m_nbrows] = propDesc.getDisplayName();
                        m_readMethods[m_nbrows] = propDesc.getReadMethod();
                        m_writeMethods[m_nbrows] = propDesc.getWriteMethod();
                        Class type = propDesc.getPropertyType();
                        String typename = type.toString();
                        m_propertyTypes[m_nbrows] = type;

                        // get editor class
                        Class editorClass = propDesc.getPropertyEditorClass();
                        if (editorClass == null) {
                            Class targetType = m_propertyTypes[m_nbrows];
                        } // end if

                        m_nbrows++;
                    } // end if
                } // end for
            } // end if
        } // end PropertyTableModel()

        String getTitle() {
            return m_title;
        }

        // ////////////////////
        // Implement TableModel
        public int getColumnCount() {
            return 2;
        }

        public String getColumnName(int columnIndex) {
            return (columnIndex == 0) ? m_info.getName() : m_info.getValue();
        }

        public int getRowCount() {
            return m_nbrows;
        }

        public Class getColumnClass(int columnIndex) {
            return Object.class;
        }

        public Class getRowClass(int rowIndex) {
            return m_propertyTypes[rowIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            // property names in first column are not editable
            if (columnIndex == 0) {
                return false;
            }

            // a property without setter method is not editiable
            Method writeMethod = m_writeMethods[rowIndex];
            if (writeMethod == null) {
                return false;
            }

            // otherwise, it is editable
            return true;
        }

        private Serializable getVariable(int idx) {
            Serializable variable = null;
            switch (m_modelType) {
            case BEAN_TYPE:
                variable = m_bean;
                break;
            case PROPERTYLIST_TYPE:
                variable = (Serializable) m_propertyList.get(idx);
                break;
            //
            } // end switch
            return variable;
        } // end getVariable()

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return m_names[rowIndex];
            } else {
                Method readMethod = m_readMethods[rowIndex];
                Class type = m_propertyTypes[rowIndex];
                Object value = null;

                if (readMethod != null) {
                    try {
                        Serializable variable = getVariable(rowIndex);
                        value = readMethod.invoke(variable, new Object[] {});
                    } catch (InvocationTargetException ex) {
                    } catch (IllegalAccessException ex) {
                    } // end try
                } // end if

                return value;
            } // end if
        } // end getValueAt()

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 1) {
                Method writeMethod = m_writeMethods[rowIndex];
                if (writeMethod != null) {
                    try {
                        Object[] params = new Object[] { aValue };
                        Serializable variable = getVariable(rowIndex);
                        writeMethod.invoke(variable, params); // throws
                        // IllegalArgumentException
                        // if params is
                        // not the
                        // actual type
                        // (get its
                        // superclass
                        // and try
                        // again?)

                        // notify property panel : m_info.setDirty(), furthur
                        // extension
                        // set the tree.
                    } catch (InvocationTargetException ex) {
                    } catch (IllegalAccessException ex) {
                    } // end try
                } // end if
            } // end if
        } // end setValueAt()

        public void addTableModelListener(TableModelListener l) {
        }

        public void removeTableModelListener(TableModelListener l) {
        }

        //
        // ///////////////

        Image getIcon() {
            return m_icon;
        }
    } // end PropertyTableModel

    // Property table
    private static class PropertyTable extends JTable {
        private PropertyTableModel m_model;
        private TableCellRenderer[] m_renderers;
        private TableCellEditor[] m_editors;
        private JTable m_table;

        PropertyTable(final BeanDialogImpl dialog, PropertyTableModel dm) {
            super(dm);
            m_model = dm;
            m_table = this;
            setRenderersAndEditors(dm);
            this.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    dialog.stopCellEditing();
                }
            });
            this.addMouseMotionListener(new MouseMotionAdapter() {
                private int m_oldVAlue = -1;

                public void mouseMoved(MouseEvent ev) {
                    // Just working for bean info
                    if (dialog.m_beaninfo == null)
                        return;

                    // Is mouse over a new cell?
                    Point ePoint = ev.getPoint();
                    int row = rowAtPoint(ePoint);
                    if (row < 0) {
                        ToolTipManager.sharedInstance().setDismissDelay(
                                g_defaultTooltipDismissDelay);
                        m_table.setToolTipText("");
                        m_oldVAlue = row;
                    } else if (row != m_oldVAlue) {
                        displayTooltip(dialog.m_beaninfo, row);
                        m_oldVAlue = row; // for the next call
                    }
                } // end mouseMoved

                private void displayTooltip(BeanInfo info, int row) {
                    PropertyDescriptor desc = info.getPropertyDescriptors()[row];
                    String text = desc.getShortDescription(); // NOT
                    // LOCALIZABLE,
                    // html tags
                    ToolTipManager.sharedInstance().setDismissDelay(g_defaultTooltipDismissDelay);
                    m_table.setToolTipText(text); // NOT LOCALIZABLE
                } // end displayTooltip()
            });
        } // end PropertyTable()

        private void setRenderersAndEditors(PropertyTableModel dm) {
            int nbRows = dm.getRowCount();
            m_renderers = new TableCellRenderer[nbRows];
            m_editors = new TableCellEditor[nbRows];

            CellViewerMapping mapping = CellViewerMapping.getSingleton();
            for (int i = 0; i < nbRows; i++) {
                Class claz = dm.getRowClass(i);
                TableCellViewer cellViewer = mapping.getCellViewer(claz);
                m_renderers[i] = cellViewer.getCellRenderer();
                m_editors[i] = cellViewer.getCellEditor();
            } // end for
        } // end setRenderers()

        public TableCellRenderer getCellRenderer(int row, int column) {
            Object value = getValueAt(row, column);
            TableCellRenderer renderer = null;
            // System.out.println("step 1");

            if (column == 1) {
                renderer = m_renderers[row];
            }

            if (renderer == null) {
                renderer = super.getCellRenderer(row, column);
            }

            return renderer;
        }

        public TableCellEditor getCellEditor(int row, int column) {
            Object value = getValueAt(row, column);
            TableCellEditor editor = null;

            if (column == 1) {
                editor = m_editors[row];
            }

            if (editor == null) {
                editor = super.getCellEditor(row, column);
            }

            return editor;
        }

        public void editingStopped(ChangeEvent e) {
            super.editingStopped(e);
        }
    } // end PropertyTable

    //
    // UNIT TEST
    //
    private static class SampleBean implements Serializable {
        SampleBean() {
        }

        private Boolean m_generateComments = Boolean.TRUE;

        public Boolean getGenerateComments() {
            return m_generateComments;
        }

        public void setGenerateComments(Boolean generateComments) {
            m_generateComments = generateComments;
        }

        private String m_physicalSpecs = "value"; // NOT LOCALIZABLE, unit test

        public String getPhysicalSpecs() {
            return m_physicalSpecs;
        }

        public void setPhysicalSpecs(String physicalSpecs) {
            m_physicalSpecs = physicalSpecs;
        }

        private String m_physicalSpecs2 = "value2"; // NOT LOCALIZABLE, unit

        // test
        public String getPhysicalSpecs2() {
            return m_physicalSpecs2;
        }

        public void setPhysicalSpecs2(String physicalSpecs2) {
            m_physicalSpecs2 = physicalSpecs2;
        }

        private Image m_image = null;

        public Image getImage() {
            return m_image;
        }

        public void setImage(Image image) {
            m_image = image;
        }
    } // end SampleBean

    //
    // BEAN DESCRIPTOR
    //
    private static class SampleBeanDescription extends BeanDescriptor {
        SampleBeanDescription(Class claz) {
            super(claz);
        }

        public String getDisplayName() {
            return "Generation Options";
        } // NOT LOCALIZABLE, unit test

        public String getShortDescription() {
            return "Sets some generation options.";
        } // NOT LOCALIZABLE, unit test
    }

    //
    // PROPERTY DESCRIPTOR
    //
    private static class GenerateCommentDescriptor extends java.beans.PropertyDescriptor {
        GenerateCommentDescriptor() throws IntrospectionException {
            super("generateComments", SampleBean.class);
        } // NOT LOCALIZABLE, unit test

        public String getDisplayName() {
            return "Generate Comments";
        } // NOT LOCALIZABLE, unit test

        public String getShortDescription() {
            return "It will generate comments on each table.";
        } // NOT LOCALIZABLE, unit test
    }

    private static class PhysicalSpecsDescriptor extends java.beans.PropertyDescriptor {
        PhysicalSpecsDescriptor() throws IntrospectionException {
            super("physicalSpecs", SampleBean.class);
        } // NOT LOCALIZABLE, unit test

        public String getDisplayName() {
            return "Generate Physical Specs";
        } // NOT LOCALIZABLE, unit test

        public String getShortDescription() {
            return "It will generate physical specifications such as ....";
        } // NOT LOCALIZABLE, unit test
    }

    private static class PhysicalSpecsDescriptor2 extends java.beans.PropertyDescriptor {
        PhysicalSpecsDescriptor2() throws IntrospectionException {
            super("physicalSpecs2", SampleBean.class);
        } // NOT LOCALIZABLE, unit test

        public String getDisplayName() {
            return "Generate Physical Specs2";
        } // NOT LOCALIZABLE, unit test

        public String getShortDescription() {
            return "It will generate physical specifications such as ....";
        } // NOT LOCALIZABLE, unit test
    }

    private static class ImageDescriptor extends java.beans.PropertyDescriptor {
        ImageDescriptor() throws IntrospectionException {
            super("image", SampleBean.class);
        } // NOT LOCALIZABLE, unit test

        public String getDisplayName() {
            return "Image";
        } // NOT LOCALIZABLE, unit test

        public String getShortDescription() {
            return "It will generate physical specifications such as ....";
        } // NOT LOCALIZABLE, unit test
    }

    //
    // BEANINFO
    //
    private static class SampleBeanInfo implements BeanInfo {
        private java.awt.Image m_image;

        SampleBeanInfo(java.awt.Image image) {
            m_image = image;
        }

        public java.awt.Image getIcon(int iconKind) {
            return m_image;
        }

        public BeanInfo[] getAdditionalBeanInfo() {
            return null;
        }

        public java.beans.MethodDescriptor[] getMethodDescriptors() {
            return null;
        }

        public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
            return null;
        }

        public int getDefaultPropertyIndex() {
            return 0;
        }

        public int getDefaultEventIndex() {
            return 0;
        }

        public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
            java.beans.PropertyDescriptor[] descs;
            try {
                descs = new java.beans.PropertyDescriptor[] { new GenerateCommentDescriptor(),
                        new PhysicalSpecsDescriptor(), new PhysicalSpecsDescriptor2(),
                        new ImageDescriptor() };
            } catch (IntrospectionException ex) {
                descs = null;
            }
            return descs;
        }

        public BeanDescriptor getBeanDescriptor() {
            return new SampleBeanDescription(SampleBean.class);
        }
    }

    //
    // MAIN
    //
    // /*
    public static void main(String[] args) throws NoSuchMethodException, IntrospectionException,
            ClassNotFoundException, InterruptedException {
        JFrame mainframe = new JFrame("BeanInfo"); // NOT LOCALIZABLE, unit test
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image image = mainframe.getIconImage();
        Container contentpane = mainframe.getContentPane();

        // create bean
        // SampleBean bean = new SampleBean();
        // BeanInfo info = new SampleBeanInfo(image);
        // BeanDialog dialog = new BeanDialogImpl(bean, info);

        // create propertyList
        ArrayList propertyList = new ArrayList();
        propertyList.add(new AbstractProperty.BooleanProperty("generate user", true)); // NOT LOCALIZABLE, unit test
        propertyList.add(new AbstractProperty.BooleanProperty("physical specs", false)); // NOT LOCALIZABLE, unit test
        BeanDialog dialog = new BeanDialogImpl(propertyList);

        // display the dialog
        dialog.showPropertyDialog(mainframe);

    } // end main()
    // */

    /*
     * private static class PropertyDialog extends JDialog { PropertyDialog() { } //end
     * PropertyDialog() } //end PropertyDialog
     * 
     * public static void main(String[] args) { //Create a property dialog JFrame mainframe = new
     * JFrame("BeanInfo"); //NOT LOCALIZABLE, unit test
     * mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     * 
     * //add property dialog JDialog dialog = new PropertyDialog(); mainframe.add(dialog);
     * 
     * mainframe.setVisible(true); } //end main()
     */
} // end BeanFrame
