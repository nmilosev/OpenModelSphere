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

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.event.CellEditorListener;
import javax.swing.filechooser.FileFilter;

class ImageViewer extends AbstractViewer {

    private static final String TITLE = "Choose a file"; // NOT LOCALIZABLE, not
    // displayed for now
    protected static final String EMPTY = "";

    private JLabel m_label = new JLabel();

    protected JLabel getLabel() {
        return m_label;
    }

    private Image m_currentImage = null;

    protected Image getCurrentImage() {
        return m_currentImage;
    }

    protected void setCurrentImage(Image image) {
        m_currentImage = image;
    }

    // draw image
    private void drawImage(JLabel label, Image image) {
        ImageIcon icon = new ImageIcon(image);
        label.setIcon(icon);
    }

    private TableCellRenderer renderer = new TableCellRenderer() {
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Image image = (Image) value;
            if (image != null) {
                drawImage(m_label, image);
                m_currentImage = image;
            } // end if

            return m_label;
        }
    };

    private TableCellEditor editor = new TableCellEditor() {
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            setEditorInfo(table, row);
            Image image = (Image) value;
            if (image != null) {
                m_currentImage = image;
            } // end if

            return m_label;
        }

        public Object getCellEditorValue() {
            return Boolean.TRUE;
        }

        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        public void removeCellEditorListener(CellEditorListener l) {
        }

        public void addCellEditorListener(CellEditorListener l) {
        }

        public void cancelCellEditing() {
        }

        public boolean stopCellEditing() {
            return true;
        }
    };

    private FileFilter filter = new FileFilter() {
        public boolean accept(File f) {
            boolean accepted = false;

            if (f.isDirectory())
                return true;

            String filename = f.getName();
            int idx = filename.indexOf('.');
            if (idx != -1) {
                String ext = filename.substring(1 + idx);
                if (ext.equals("jpg") || ext.equals("gif")) { // NOT
                    // LOCALIZABLE,
                    // file
                    // extension
                    accepted = true;
                }
            } // end if
            return accepted;
        } // end accept()

        public String getDescription() {
            return "JPG and GIF Images";
        } // NOT LOCALIZABLE, not displayed for now
    };

    private static File g_lastVisitedFile = null; // only used in the following
    // block

    private MouseListener m_listener = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            JFileChooser fc = (g_lastVisitedFile == null) ? new JFileChooser() : new JFileChooser(
                    g_lastVisitedFile);
            fc.setFileFilter(filter);
            int returnVal = fc.showOpenDialog(m_label);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                g_lastVisitedFile = fc.getSelectedFile();
                String filename = g_lastVisitedFile.getAbsolutePath();
                Image image = Toolkit.getDefaultToolkit().getImage(filename);
                drawImage(m_label, image);
            } // end if
        } // end mousePressed()
    };

    protected MouseListener getListener() {
        return m_listener;
    }

    ImageViewer() {
        super();
        super.setViewers(renderer, editor);
        MouseListener listener = getListener();
        getLabel().addMouseListener(listener);
    }
} // end ImageViewer
