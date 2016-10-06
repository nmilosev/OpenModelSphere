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

package org.modelsphere.sms.screen.plugins;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;

import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.ImagePreviewer;
import org.modelsphere.jack.baseDb.screen.MultiDefaultRenderer;
import org.modelsphere.jack.baseDb.screen.plugins.SrImageEditor;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.srtool.screen.design.DesignTableModel;
import org.modelsphere.sms.be.BEModule;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.screen.plugins.util.QualifierBuiltinImages;

/**
 * 
 * Editor for fields: DbORIndex.fConstraint, DbORPrimaryUnique.fIndex, DbORForeign.fIndex
 * 
 */
public final class MultiDbBEQualifierIconEditor implements TableCellEditor {
    public static final Object[] BUILTIN_IMAGES;
    private static final String kSelect = LocaleMgr.screen.getString("SelectImage_");

    protected EventListenerList listenerList = new EventListenerList();
    transient protected ChangeEvent changeEvent = null;
    private Object oldValue;
    private Component comp = null;
    private Image image;
    protected final ActionListener listener = new ComboActionListener();

    private int customIndex = -1;
    private int noneIndex = -1;

    static {
        ArrayList images = new ArrayList();
        try {
            int count = 1;
            String fileName = new Integer(count).toString();
            while (fileName.length() < 4)
                fileName = "0" + fileName; // NOT LOCALIZABLE
            Image image = GraphicUtil.loadImage(
                    org.modelsphere.sms.be.international.LocaleMgr.class,
                    "international/resources/qualifiers/" + fileName + ".gif"); // NOT LOCALIZABLE

            while (image != null) {
                images.add(image);
                count++;
                fileName = new Integer(count).toString();
                while (fileName.length() < 4)
                    fileName = "0" + fileName; // NOT LOCALIZABLE
                image = GraphicUtil.loadImage(org.modelsphere.sms.be.international.LocaleMgr.class,
                        "international/resources/qualifiers/" + fileName + ".gif"); // NOT
                // LOCALIZABLE
            }
        } catch (Exception e) {
            Debug.trace(e);
        }
        
        BUILTIN_IMAGES = images.toArray();
    }

    public final Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        oldValue = value;
        DesignTableModel model = (DesignTableModel) table.getModel();
        try {
            JComboBox combo = new JComboBox();
            int selInd = -1;
            Collection values = getSelectionSet(value);
            Iterator iter = values.iterator();
            while (iter.hasNext()) {
                Object avalue = iter.next();
                combo.addItem(avalue);
                if (avalue == value)
                    selInd = combo.getItemCount() - 1;
            }

            combo.setMaximumRowCount(10);
            combo.setSelectedIndex(selInd != -1 ? selInd : noneIndex);
            combo.addActionListener(listener);
            comp = combo;
            if (isSelected) {
                comp.setBackground(table.getBackground());
                comp.setForeground(table.getForeground());
            } else {
                comp.setBackground(table.getBackground());
                comp.setForeground(table.getForeground());
            }
            configureJComboBox(combo);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(table, ex);
        }
        return comp;
    }

    protected final Collection getSelectionSet(Object value) {
        ArrayList values = new ArrayList();
        values.add(MultiDefaultRenderer.kNone);
        values.add(kSelect);
        //values.addAll(Arrays.asList(BUILTIN_IMAGES));
        values.addAll(QualifierBuiltinImages.getBuiltinImages());
        if (value != null && !values.contains(value))
            values.add(2, value);
        noneIndex = 0;
        customIndex = 1;
        return values;
    }

    protected void configureJComboBox(JComboBox combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Image) {
                    setIcon(new ImageIcon((Image) value) {
                        public int getIconHeight() {
                            int height = super.getIconHeight();
                            return height > 30 ? 30 : height;
                        }

                    });
                    setText("");
                    setHorizontalTextPosition(SwingConstants.LEFT);
                } else {
                    setText(value.toString());
                    setIcon(null);
                }
                return this;
            }
        });
    }

    public final Object getCellEditorValue() {
        if (image != null)
            return image;
        if (((JComboBox) comp).getSelectedIndex() == noneIndex)
            return null;
        Object value = ((JComboBox) comp).getSelectedItem();
        return value;
    }

    public boolean isCellEditable(EventObject e) {
        if (e instanceof MouseEvent) {
            return (((MouseEvent) e)).getClickCount() >= 2;
        }
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    protected void fireEditingStopped() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
            }
        }
    }

    protected void fireEditingCanceled() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
            }
        }
    }

    class ComboActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (((JComboBox) comp).getSelectedIndex() == customIndex) {
                JFileChooser fc = (SrImageEditor.g_lastVisitedFile == null) ? new JFileChooser()
                        : new JFileChooser(SrImageEditor.g_lastVisitedFile);
                fc.setFileFilter(ExtensionFileFilter.allImagesFilter);
                fc.setAccessory(new ImagePreviewer(fc));

                Dimension dim = fc.getPreferredSize();
                fc.setPreferredSize(new Dimension(dim.width, dim.height + 75));

                int returnVal = fc.showOpenDialog(comp);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    SrImageEditor.g_lastVisitedFile = fc.getSelectedFile();
                    String filename = SrImageEditor.g_lastVisitedFile.getAbsolutePath();
                    image = Toolkit.getDefaultToolkit().getImage(filename);
                    // We must wait for the image to complete loading otherwise
                    // Db will capture the loaded part of the image in SrImage.
                    GraphicUtil.waitForImage(image);
                    stopCellEditing();
                } else
                    cancelCellEditing();
                ((JComboBox) comp).removeActionListener(this);
            }
        }
    }

}
