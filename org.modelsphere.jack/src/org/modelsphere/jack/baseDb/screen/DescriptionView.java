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

package org.modelsphere.jack.baseDb.screen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.EventObject;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.model.*;
import org.modelsphere.jack.baseDb.screen.plugins.ExternalDocumentEditor;
import org.modelsphere.jack.baseDb.screen.plugins.LookupDescriptionEditor;
import org.modelsphere.jack.baseDb.screen.plugins.MultiExternalDocumentEditor;
import org.modelsphere.jack.baseDb.screen.plugins.MultiLookupDescriptionEditor;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;

public class DescriptionView extends ScreenView implements ListDataListener {

    protected JTable table = null;
    protected DescriptionModel descrModel = null;

    public DescriptionView(ScreenContext screenContext) {
        super(screenContext, APPLY_ACTION);
    }

    public final ScreenModel getModel() {
        return descrModel;
    }

    // overriding methods must call super.setModel
    public void setModel(DescriptionModel newModel) {
        if (table != null)
            deinstallPanel();
        initPanel();
        descrModel = newModel;

        JComboBox lazyCombo = new JComboBox();
        int rowheight = lazyCombo.getPreferredSize().height;

        table = new JTable(new DescriptionTableModel())/*
                                                        * { public void validate(){
                                                        * super.validate(); Dimension dim = table.
                                                        * getPreferredSize(); table
                                                        * .getTableHeader() .getc } }
                                                        */;
        table.setName("Design"); // NOT LOCALIZABLE - For QA
        table.setRowHeight(rowheight); // for comboBox of cellEditor
        table.getTableHeader().setReorderingAllowed(false);
        TableColumn column = table.getColumnModel().getColumn(1);
        column.setCellEditor(new DescriptionEditor());
        column.setCellRenderer(new DescriptionRenderer());

        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.getVerticalScrollBar().setName("VScrollBar"); // NOT
        // LOCALIZABLE
        // - For QA
        Container contentPanel = getContentPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(scrollpane, BorderLayout.CENTER);
        contentPanel.validate();

        TableColumn column0 = table.getColumnModel().getColumn(0);
        column0.setPreferredWidth(100);
        column.setPreferredWidth(300);

        descrModel.addListDataListener(this);

        // Code rajouté par AQ - Gino devrait modifier la condition pour qu'on
        // puisse utiliser le code même si on n'est pas en debug.
        boolean qaGeneration = false; // pour ne pas que la génération
        // s'effectue systématiquement dès qu'on
        // ouvre la fenêtre de propriétés,
        // mettre à false
        if (Debug.isDebug() && qaGeneration)
            this.getFieldsAndEditorsForQA();

    }

    // overriding methods must call super.deinstallPanel
    public void deinstallPanel() {
        super.deinstallPanel();
        if (descrModel != null)
            descrModel.removeListDataListener(this);
    }

    public final Color getSelectionBackground() {
        return table.getSelectionBackground();
    }

    public final Color getSelectionForeground() {
        return table.getSelectionForeground();
    }

    // pour AQ - sert à générer les champs et les éditeurs des fenêtres de
    // propriétés

    private void getFieldsAndEditorsForQA() {

        String title = "";
        java.util.ArrayList ligne = new java.util.ArrayList();
        DescriptionTableModel tableDescrModel = (DescriptionTableModel) table.getModel();
        int nbRow = table.getRowCount();
        String fieldValue = tableDescrModel.getValueAt(0, 1).toString();

        title = "const " + "LIST OF CHAMPS" + " lc" + fieldValue + " = {...}" + "\n"; // NOT
        // LOCALIZABLE
        // QA
        for (int i = 0; i < table.getRowCount(); i++) {
            String fieldName = tableDescrModel.getValueAt(i, 0).toString();
            String editorName = descrModel.getEditorName(i);
            ligne.add(i, "\t" + "{" + '"' + fieldName + '"' + "," + " " + editorName + "Editeur"
                    + "}" + "\n"); // NOT
            // LOCALIZABLE
            // QA
        }
        writeFile(title, ligne, fieldValue);
    }

    private void writeFile(String title, java.util.ArrayList ligne, String fieldValue) {

        String directoryName = System.getProperty("user.dir"); // NOT
        // LOCALIZABLE
        // property
        String separator = System.getProperty("file.separator"); // NOT
        // LOCALIZABLE
        // property
        String extensionFile = ".txt"; // NOT LOCALIZABLE extention
        String fileName = fieldValue.trim();
        File file = null;
        FileWriter fileWriter = null;
        File dir = new File(directoryName + separator + "aq", ""); // NOT
        // LOCALIZABLE
        // QA
        dir.mkdirs();
        try {
            file = new File(directoryName + separator + "aq" + separator + fileName + extensionFile); // NOT
            // LOCALIZABLE
            // QA
            if (file.exists()) {
                fileName = fieldValue.trim() + "1"; // pas génial, mais pas le
                // temps de chercher autre
                // chose pour le moment...
                // // NOT LOCALIZABLE QA
                file = new File(directoryName + separator + "aq" + separator + fileName
                        + extensionFile); // NOT
                // LOCALIZABLE
                // QA
            }
            file.createNewFile();
            fileWriter = new FileWriter(file);
        } catch (FileNotFoundException exception) {
        } catch (IOException exc) {
        }

        try {
            fileWriter.write(title);
            for (int i = 0; i < ligne.size(); i++) {
                String toWrite = ligne.get(i).toString();
                fileWriter.write(toWrite);
            }
            fileWriter.write("\n"); // / Espace entre les concepts. // NOT
            // LOCALIZABLE QA
        }

        catch (java.io.IOException exc) {
        }
        try {
            fileWriter.close();
        } catch (FileNotFoundException exception) {
        } catch (IOException exc) {
        }
    }

    public final boolean isCellEditable(int row, int column) {
        return table.getModel().isCellEditable(row, column);
    }

    public final void stopEditing() {
        if (table != null && table.isEditing())
            table.getCellEditor().stopCellEditing();
    }

    // ////////////////////////////////////
    // ListDataListener SUPPORT
    //
    public final void intervalAdded(ListDataEvent e) {
        // should never be called
    }

    public final void intervalRemoved(ListDataEvent e) {
        // should never be called
    }

    public final void contentsChanged(ListDataEvent e) {
        DescriptionTableModel tableModel = (DescriptionTableModel) table.getModel();
        tableModel.fireTableCellUpdated(e.getIndex0(), 1);
        if (descrModel.hasChanged()) // user change, not db refresh
            setHasChanged();
    }

    //
    // End of ListDataListener SUPPORT
    // /////////////////////////////////////////

    class DescriptionTableModel extends AbstractTableModel {

        public final boolean isCellEditable(int row, int col) {
            return (col == 1 && isApplyPanel() && descrModel.isEditable(row));
        }

        public final String getColumnName(int col) {
            return (col == 0 ? LocaleMgr.screen.getString("Property") : LocaleMgr.screen
                    .getString("Value"));
        }

        public final void setValueAt(Object aValue, int row, int col) {
            if (col == 1)
                descrModel.setElementAt(aValue, row);
        }

        public final int getColumnCount() {
            return 2;
        }

        public final int getRowCount() {
            return descrModel.getSize();
        }

        public final Object getValueAt(int row, int col) {
            return (col == 0 ? descrModel.getGUIName(row) : descrModel.getElementAt(row));
        }
    }

    class DescriptionEditor extends org.modelsphere.jack.awt.AbstractTableCellEditor {
        HashMap<String, Editor> editorSamples = new HashMap<String, Editor>();

        transient private Editor delegate;

        DescriptionEditor() {
            setClickCountForEditing(2);
        }

        public final Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            String name = descrModel.getEditorName(row);
            delegate = ScreenPlugins.getEditor(name);
            if (delegate != null) {
                setClickCountForEditing(delegate.getClickCountForEditing());
                int col = table.convertColumnIndexToModel(column);
                Component cmp = delegate.getTableCellEditorComponent(DescriptionView.this, this,
                        value, isSelected, row, col);
                return cmp;
            }
            return null;
        }

        public final boolean stopCellEditing() {
            if (!delegate.stopCellEditing())
                return false;
            fireEditingStopped();
            return true;
        }

        public final Object getCellEditorValue() {
            return delegate.getCellEditorValue();
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            
            //full version allows edition
            boolean editable = true;
            boolean clicked = false;
            boolean editableInViewerContext = false; 
            
            if (anEvent instanceof MouseEvent) {
                int row = table.rowAtPoint(((MouseEvent) anEvent).getPoint());
                if (row > -1) {
                    String name = descrModel.getEditorName(row);
                    Editor editor = editorSamples.get(name);
                    
                    if (editor == null) {
                        editor = ScreenPlugins.getEditor(name);
                        editorSamples.put(name, editor);
                    }
                    if (editor != null) {
                        int clickCount = ((MouseEvent)anEvent).getClickCount(); 
                        int editorClickCount = editor.getClickCountForEditing(); 
                        clicked = clickCount >= editorClickCount;
                        
                        if (descrModel instanceof DescriptionModel) {
                        	ReflectionDescriptionModel model = (ReflectionDescriptionModel)descrModel; 
                        	MetaClass mc = model.getMetaClass();
                        	editableInViewerContext = isEditableInViewerContext(editor, mc);
                        }
                    }
                }
            } //end if
            
            editable = clicked ? true : super.isCellEditable(anEvent);
            
            //The model viewer does not permit editiong, but the lookup description editor
            //works to allow the user to view the complete description
            if ((! ScreenPerspective.isFullVersion() && (! editableInViewerContext))) {
            	editable = false;
            } //end if
            
            return editable;
        } //end isCellEditable()
    }
    
    private boolean isEditableInViewerContext(Editor editor, MetaClass mc) {
    	//by default, a viewer cannot edit cells..
		boolean editableInViewerContext = false;
		
		//..but the following cases are permitted
		if (editor instanceof LookupDescriptionEditor) {
			editableInViewerContext = true;
		} else if (editor instanceof ExternalDocumentEditor) {
			editableInViewerContext = true;
		} else {
			String name = mc.getJClass().getSimpleName();
			
			//cannot import sms.db.DbSMSNotice
			editableInViewerContext = "DbSMSNotice".equals(name); 
			
		} //end if
		
		return editableInViewerContext;
	}

    class DescriptionRenderer implements TableCellRenderer {

        public final Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Renderer delegate = descrModel.getRenderer(row, column);

            Component comp = delegate.getTableCellRendererComponent(DescriptionView.this, value,
                    isSelected, hasFocus, row, table.convertColumnIndexToModel(column));

            if (isSelected == true) {
                comp.setForeground(table.getSelectionForeground());
                comp.setBackground(table.getSelectionBackground());
            } else {
                if (descrModel instanceof DbDescriptionModel) {
                    DbDescriptionModel dbDescModel = (DbDescriptionModel) descrModel;
                    DescriptionField df = (DescriptionField) dbDescModel.getDescriptionField(row);
                    if (df.isEnabled() == false) {
                        comp.setForeground(TerminologyUtil.getMarkedMetaFieldsFontColor());
                        comp.setBackground(TerminologyUtil.getMarkedMetaFieldsBackColor());
                        // dbField.setEditable(false);
                    } else {
                        comp.setForeground(table.getForeground());
                        comp.setBackground(table.getBackground());
                    }
                } else {
                    comp.setForeground(table.getForeground());
                    comp.setBackground(table.getBackground());
                }
            }
            return comp;
        }
    }
}
