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

package org.modelsphere.sms.screen.plugins;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.SwingConstants;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.ImagePreviewer;
import org.modelsphere.jack.baseDb.screen.Editor;
import org.modelsphere.jack.baseDb.screen.MultiDefaultRenderer;
import org.modelsphere.jack.baseDb.screen.Renderer;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.screen.model.ScreenModel;
import org.modelsphere.jack.baseDb.screen.plugins.SrImageEditor;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.sms.be.BEModule;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.screen.plugins.util.QualifierBuiltinImages;

/**
 * 
 * Editor for fields: DbORIndex.fConstraint, DbORPrimaryUnique.fIndex, DbORForeign.fIndex
 * 
 */
public final class DbBEQualifierIconEditor implements Editor {

    private static final Object[] BUILTIN_IMAGES;
    private static final String kSelect = LocaleMgr.screen.getString("SelectImage_");

    private int customIndex = -1;
    private int noneIndex = -1;
    private Object oldValue;
    private Image image;
    private Renderer renderer;
    private Component comp = null;
    protected final ActionListener listener = new ComboActionListener();
    private ScreenView screenView;
    private AbstractTableCellEditor editor;

    static {
        ArrayList images = new ArrayList();
        try {
            int count = 1;
            String fileName = new Integer(count).toString();
            while (fileName.length() < 4)
                fileName = "0" + fileName; // NOT LOCALIZABLE
            Image image = GraphicUtil.loadImage(BEModule.class,
                    "international/resources/qualifiers/" + fileName + ".gif"); // NOT LOCALIZABLE

            while (image != null) {
                images.add(image);
                count++;
                fileName = new Integer(count).toString();
                while (fileName.length() < 4)
                    fileName = "0" + fileName; // NOT LOCALIZABLE
                image = GraphicUtil.loadImage(BEModule.class, "international/resources/qualifiers/"
                        + fileName + ".gif"); // NOT
                // LOCALIZABLE
            }
        } catch (Exception e) {
            Debug.trace(e);
        }
        BUILTIN_IMAGES = images.toArray();
    }

    public final Component getTableCellEditorComponent(ScreenView screenView,
            AbstractTableCellEditor editor, Object value, boolean isSelected, int row, int column) {
        oldValue = value;
        ScreenModel model = screenView.getModel();
        renderer = model.getRenderer(row, column);
        this.editor = editor;
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
            configureJComboBox(combo);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(screenView, ex);
        }
        return comp;
    }

    public final boolean stopCellEditing() {
        return true;
    }

    public final Object getCellEditorValue() {
        if (image != null)
            return image;
        if (((JComboBox) comp).getSelectedIndex() == noneIndex)
            return null;
        Object value = ((JComboBox) comp).getSelectedItem();
        return value;
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

    class ComboActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (((JComboBox) comp).getSelectedIndex() == customIndex) {
                JFileChooser fc = (SrImageEditor.g_lastVisitedFile == null) ? new JFileChooser()
                        : new JFileChooser(SrImageEditor.g_lastVisitedFile);
                fc.setFileFilter(ExtensionFileFilter.allImagesFilter);
                fc.setAccessory(new ImagePreviewer(fc));
                Dimension dim = fc.getPreferredSize();
                fc.setPreferredSize(new Dimension(dim.width, dim.height + 75));

                int returnVal = fc.showOpenDialog(screenView);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    SrImageEditor.g_lastVisitedFile = fc.getSelectedFile();
                    String filename = SrImageEditor.g_lastVisitedFile.getAbsolutePath();
                    image = Toolkit.getDefaultToolkit().getImage(filename);
                    // We must wait for the image to complete loading otherwise
                    // Db will capture the loaded part of the image in SrImage.
                    GraphicUtil.waitForImage(image);
                } // end if
                ((JComboBox) comp).removeActionListener(this);

            }
            if (editor != null) {
                editor.actionPerformed(e);
            }
        }
    }

    @Override
    public int getClickCountForEditing() {
        return 2;
    }
}
