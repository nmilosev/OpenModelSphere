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

package org.modelsphere.sms.style;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.ImagePreviewer;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.srtypes.DbtPrefix;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.Module;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.db.util.DbInitialization;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOStyle;
import org.modelsphere.sms.or.db.DbORStyle;

public class PrefixOptionComponent extends OptionComponent {

    private static final int COL_CONCEPT = 0;
    private static final int COL_DISPLAYED = 1;
    private static final int COL_TEXT = 2;
    private static final int COL_IMAGE = 3;
    private static final String DISPLAY_NONE = new String(LocaleMgr.screen.getString("None"));
    private static final String DISPLAY_TEXT = new String(LocaleMgr.screen.getString("Text"));
    private static final String DISPLAY_IMAGE = new String(LocaleMgr.screen.getString("Image"));
    private static final String kprefixFor = new String(LocaleMgr.screen.getString("PrefixFor"));
    private static final String kdisplay = new String(LocaleMgr.screen.getString("DisplayAsNoun"));
    private static final String kprefixText = new String(LocaleMgr.screen.getString("PrefixText"));
    private static final String kprefixImage = new String(LocaleMgr.screen.getString("PrefixImage"));

    transient private static String defaultDirectory = ApplicationContext
            .getDefaultWorkingDirectory();

    private JTable table;

    public PrefixOptionComponent(StyleComponent StyleComponent, MetaField[] metaFields) {
        super(StyleComponent, metaFields);
        jbInit();
    }

    public final void setStyle(DbObject style, boolean refresh) throws DbException {
        super.setStyle(style, refresh);
        ((AbstractTableModel) table.getModel()).fireTableDataChanged();
    }

    private void jbInit() {
        table = new JTable(new PrefixModel());
        table.setRowSelectionAllowed(false);
        optionPanel = new JScrollPane(table);
        // Show images with a lookup button.
        ///*
        AbstractTableCellEditor imageEditor = new AbstractTableCellEditor() {
            private PrefixImageCellData newPrefixImageCellData;

            public Component getTableCellEditorComponent(final JTable table, final Object value,
                    boolean isSelected, int row, int column) {
                newPrefixImageCellData = (PrefixImageCellData) value;
                JPanel panel = new JPanel(new BorderLayout());
                JLabel label = new JLabel();
                JLabel labelSpace = new JLabel("  ");
                JButton lookupBtn = new JButton("...");
                if (((PrefixImageCellData) value).image != null) {
                    Image image = resizePrefixImage(((PrefixImageCellData) value).image, label);
                    label.setIcon(new ImageIcon(image));
                }
                panel.setBackground(table.getBackground());
                panel.add(labelSpace, BorderLayout.WEST);
                panel.add(label, BorderLayout.CENTER);
                panel.add(lookupBtn, BorderLayout.EAST);
                lookupBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        JFileChooser chooser = new JFileChooser(defaultDirectory);
                        chooser.setAccessory(new ImagePreviewer(chooser));
                        chooser.addChoosableFileFilter(ExtensionFileFilter.allImagesFilter);
                        int retval = chooser.showDialog(table, null);
                        if (retval == JFileChooser.APPROVE_OPTION) {
                            File theFile = chooser.getSelectedFile();
                            defaultDirectory = theFile.getParent();
                            if (theFile != null) {
                                Image image = null;
                                String imageName = "file:" + theFile.getAbsolutePath(); //NOT LOCALIZABLE
                                try {
                                    URL url = new URL(imageName);
                                    image = Toolkit.getDefaultToolkit().getImage(url);
                                    GraphicUtil.waitForImage((Component) ev.getSource(), image);
                                } catch (Exception e) {
                                    imageName = null;
                                }
                                newPrefixImageCellData = new PrefixImageCellData(image, imageName);
                            }
                        }
                        stopCellEditing();
                    }
                });
                return panel;
            }

            public Object getCellEditorValue() {
                return newPrefixImageCellData;
            }
        };

        /* If called to get a tool tip, value is null */
        TableCellRenderer imageRenderer = new TableCellRenderer() {
            JPanel panel;
            JLabel label;
            JLabel labelSpace;

            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                if (panel == null) {
                    panel = new JPanel(new BorderLayout());
                    label = new JLabel();
                    labelSpace = new JLabel("  ");
                    panel.setBackground(table.getBackground());
                    panel.add(labelSpace, BorderLayout.WEST);
                    panel.add(label, BorderLayout.CENTER);
                    panel.add(new JButton("..."), BorderLayout.EAST);
                }
                if (value != null && ((PrefixImageCellData) value).image != null) {
                    Image image = resizePrefixImage(((PrefixImageCellData) value).image, label);
                    label.setIcon(new ImageIcon(image));
                } else
                    label.setIcon(null);
                return panel;
            }
        };

        /////////////////////////////////////
        AbstractTableCellEditor choiceEditor = new AbstractTableCellEditor() {
            private JComboBox combo = new JComboBox();

            {
                combo.addItem(DISPLAY_NONE);
                combo.addItem(DISPLAY_TEXT);
                combo.addItem(DISPLAY_IMAGE);
            }

            public Component getTableCellEditorComponent(final JTable table, final Object value,
                    boolean isSelected, int row, int column) {

                ChoiceCellData data = (ChoiceCellData) value;
                combo.setSelectedItem(data.value);
                return combo;
            }

            public Object getCellEditorValue() {
                return combo.getSelectedObjects()[0];
            }
        };

        TableCellRenderer choiceRenderer = new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                String cellText = (value == null) ? "" : ((ChoiceCellData) value).value;
                Component c = new JLabel(cellText);

                if (table != null) {
                    TableCellRenderer defaultRanderer = table.getDefaultRenderer(String.class);
                    if (defaultRanderer != null) {
                        Component defaultComp = defaultRanderer.getTableCellRendererComponent(
                                table, value, isSelected, hasFocus, row, column);
                        if (defaultComp != null) {
                            if (defaultComp instanceof Label) {
                                ((Label) defaultComp).setText(cellText);
                                c = defaultComp;
                            } else if (defaultComp instanceof JLabel) {
                                ((JLabel) defaultComp).setText(cellText);
                                c = defaultComp;
                            }
                        }
                    }
                }
                return c;
            }
        };
        /////////////////////////////////////

        table.setDefaultEditor(PrefixImageCellData.class, imageEditor);
        table.setDefaultEditor(ChoiceCellData.class, choiceEditor);
        table.setDefaultRenderer(PrefixImageCellData.class, imageRenderer);
        table.setDefaultRenderer(ChoiceCellData.class, choiceRenderer);
        table.setRowHeight(20);
    }

    class PrefixModel extends AbstractTableModel {

        public String getColumnName(int col) {
            String key;
            if (col == COL_CONCEPT)
                key = kprefixFor;
            else if (col == COL_DISPLAYED)
                key = kdisplay;
            else if (col == COL_TEXT)
                key = kprefixText;
            else
                // COL_IMAGE
                key = kprefixImage;
            return key;
        }

        public Class getColumnClass(int col) {
            if (col == COL_DISPLAYED)
                return ChoiceCellData.class;
            else if (col == COL_IMAGE)
                return PrefixImageCellData.class;
            else
                return String.class;
        }

        public boolean isCellEditable(int row, int col) {
            return col > 0;
        }

        public void setValueAt(Object aValue, int row, int col) {
            DbtPrefix prefix = (DbtPrefix) values[row];
            int displayedComponent = (prefix == null) ? 0 : prefix.getDisplayedComponent();
            String text = (prefix == null) ? "" : prefix.getText();
            String imageName = (prefix == null) ? "" : prefix.getImageName();
            Image image = (prefix == null) ? null : prefix.getImage();

            if (col == COL_DISPLAYED) {
                String s = (String) aValue;
                int newDisplayedSelected = displayedComponent;
                if (s.equals(DISPLAY_TEXT))
                    newDisplayedSelected = DbtPrefix.DISPLAY_TEXT;
                else if (s.equals(DISPLAY_IMAGE))
                    newDisplayedSelected = DbtPrefix.DISPLAY_IMAGE;
                else
                    // DISPLAY_NONE
                    newDisplayedSelected = DbtPrefix.DISPLAY_NONE;
                setValue(new DbtPrefix(newDisplayedSelected, text, imageName, image), row);
            } else if (col == COL_TEXT) {
                setValue(new DbtPrefix(displayedComponent, (String) aValue, imageName, image), row);
            } else { // COL_IMAGE
                setValue(new DbtPrefix(displayedComponent, text,
                        ((PrefixImageCellData) aValue).imageName,
                        ((PrefixImageCellData) aValue).image), row);
            }
        }

        public int getColumnCount() {
            return 4;
        }

        public int getRowCount() {
            return metaFields.length;
        }

        public Object getValueAt(int row, int col) {
            DbtPrefix prefix = (DbtPrefix) values[row];
            if (prefix == null) {
                if (style instanceof DbSMSStyle) {
                    DbSMSStyle dbStyle = (DbSMSStyle) style;
                    MetaField mf = metaFields[row];
                    prefix = getDefaultPrefix(dbStyle, mf);

                    try {
                        Db db = dbStyle.getDb();
                        int mode = db.getTransMode();
                        if (mode == Db.TRANS_NONE) {
                            db
                                    .beginWriteTrans(LocaleMgr.action
                                            .getString("addMissingStyleElement"));
                            dbStyle.set(mf, prefix);
                            db.commitTrans();
                        }

                    } catch (DbException ex) {
                        //
                    }
                } //end if 
            } //end if

            if (col == COL_CONCEPT) {
                String guiName = metaFields[row].getGUIName();
                return guiName;
            } else if (col == COL_DISPLAYED) {
                int value = (prefix == null) ? DbtPrefix.DISPLAY_TEXT : prefix
                        .getDisplayedComponent();
                ChoiceCellData data = new ChoiceCellData(value);
                return data;
            } else if (col == COL_TEXT) {
                String text = (prefix == null) ? "?" : prefix.getText();
                return text;
            } else { // COL_IMAGE
                Image image = (prefix == null) ? null : prefix.getImage();
                String imageName = (prefix == null) ? null : prefix.getImageName();
                PrefixImageCellData data = new PrefixImageCellData(image, imageName);
                return data;
            } //end if
        } //end getValueAt()
    }

    private Image resizePrefixImage(Image image, ImageObserver observer) {
        int rowH = table.getRowHeight();
        int imageW = image.getWidth(observer);
        int imageH = image.getHeight(observer);
        Image resizedImage = image;
        if (imageW > rowH || imageH > rowH) {
            imageW = (imageW > rowH) ? rowH : imageW;
            imageH = (imageH > rowH) ? rowH : imageH;
            resizedImage = image.getScaledInstance(imageW, imageH, Image.SCALE_SMOOTH);
        }
        return resizedImage;
    }

    class PrefixImageCellData {
        public Image image;
        public String imageName;

        PrefixImageCellData(Image newImage, String newImageName) {
            image = newImage;
            imageName = newImageName;
        }
    }

    class ChoiceCellData {
        public String value;

        ChoiceCellData(int choiceValue) {
            switch (choiceValue) {
            case DbtPrefix.DISPLAY_NONE:
                value = DISPLAY_NONE;
                break;
            case DbtPrefix.DISPLAY_TEXT:
                value = DISPLAY_TEXT;
                break;
            case DbtPrefix.DISPLAY_IMAGE:
                value = DISPLAY_IMAGE;
                break;
            }
        }
    }

    public static DbtPrefix getDefaultPrefix(DbSMSStyle dbStyle, MetaField mf) {
        DbtPrefix prefix = null;

        if ((DbOOStyle.fSourcePrefix.equals(mf)) && (DbORStyle.fSourcePrefix.equals(mf))) {
            prefix = DbInitialization.getSourcePrefix();
        } else if ((DbOOStyle.fTargetPrefix.equals(mf)) && (DbORStyle.fTargetPrefix.equals(mf))) {
            prefix = DbInitialization.getTargetPrefix();
        } else if ((DbOOStyle.fErrorPrefix.equals(mf)) && (DbORStyle.fErrorPrefix.equals(mf))) {
            prefix = DbInitialization.getErrorPrefix();
        } else if ((DbOOStyle.fWarningPrefix.equals(mf)) && (DbORStyle.fWarningPrefix.equals(mf))) {
            prefix = DbInitialization.getWarningPrefix();
        } else {
            prefix = DbtPrefix.getDefaultPrefix();
        } //end if 

        return prefix;
    } //end getDefaultPrefix()

}
