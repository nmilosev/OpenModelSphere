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

package org.modelsphere.jack.srtool.screen.design;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.baseDb.screen.MultiDefaultRenderer;
import org.modelsphere.jack.baseDb.screen.ScreenPlugins;
import org.modelsphere.jack.baseDb.screen.plugins.MultiDbSemanticalObjectRenderer;
import org.modelsphere.jack.baseDb.screen.plugins.MultiExternalDocumentEditor;
import org.modelsphere.jack.baseDb.screen.plugins.MultiLookupDescriptionEditor;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.features.SafeMode;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.util.ExceptionHandler;

/**
 *
 */
public class DesignTable extends JTable {
    private class DesignRenderer extends JLabel implements TableCellRenderer {

        protected Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

        public DesignRenderer() {
            super();
            setOpaque(true);
            setBorder(noFocusBorder);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            DesignTableModel model = (DesignTableModel) table.getModel();
            if (column > 0) {
                String pluginName = model.getRendererAt(row);
                if (pluginName != null) {
                    TableCellRenderer delegate = ScreenPlugins.getMultiRenderer(pluginName);
                    if (delegate == null) {
                        delegate = (getDesignTableModel().getFields().get(row) instanceof MetaRelationship ? MultiDbSemanticalObjectRenderer.singleton
                                : MultiDefaultRenderer.singleton);
                    }
                    if (delegate != null) {
                        Component delegatedComponent = delegate.getTableCellRendererComponent(
                                table, value, isSelected, hasFocus, row, column);

                        // For column 1
                        boolean valuesDiffer = model.isValuesDiffer(row);
                        if (valuesDiffer && !isSelected) {
                            delegatedComponent.setForeground(table.getForeground());
                            delegatedComponent.setBackground(DesignPanel.DIFFER_NO_FOCUS_COLOR);
                        } else {
                            if (isSelected) {
                                delegatedComponent.setForeground(table.getSelectionForeground());
                                delegatedComponent.setBackground(table.getSelectionBackground());
                            } else {
                                RowData rowd = (RowData) getDesignTableModel().getData().get(row);
                                if (rowd.marked == true) {

                                    // //
                                    // there is a problem with the
                                    // LookupDescription delegate, its font
                                    // remains black

                                    delegatedComponent.setForeground(TerminologyUtil
                                            .getMarkedMetaFieldsFontColor());
                                    delegatedComponent.setBackground(TerminologyUtil
                                            .getMarkedMetaFieldsBackColor());
                                } else {
                                    delegatedComponent.setForeground(table.getForeground());
                                    delegatedComponent.setBackground(table.getBackground());
                                }
                            }
                        }

                        return delegatedComponent;
                    }
                }
            }

            // For column 0
            boolean valuesDiffer = model.isValuesDiffer(row);
            if (valuesDiffer && !isSelected) {
                setForeground(table.getForeground());
                setBackground(DesignPanel.DIFFER_NO_FOCUS_COLOR);
            } else {
                if (isSelected) {
                    setForeground(table.getSelectionForeground());
                    setBackground(table.getSelectionBackground());
                } else {
                    RowData rowd = (RowData) getDesignTableModel().getData().get(row);
                    if (rowd.marked == true) {
                        setForeground(TerminologyUtil.getMarkedMetaFieldsFontColor());
                        setBackground(TerminologyUtil.getMarkedMetaFieldsBackColor());
                    } else {
                        setForeground(table.getForeground());
                        setBackground(table.getBackground());
                    }
                }
            }

            Font font = table.getFont();
            if (font != null && column == 0 && valuesDiffer) {
                int style = font.getStyle();
                if ((style & Font.BOLD) != 0)
                    style = style & ~Font.BOLD;
                else
                    style = style | Font.BOLD;
                font = new Font(font.getName(), style, font.getSize());
            }

            if ((font != null && column == 0 && model.getMetaFieldAt(row) == DbUDFValue.fValue)) {
                int style = font.getStyle();
                if ((style & Font.ITALIC) != 0)
                    style = style & ~Font.ITALIC;
                else
                    style = style | Font.ITALIC;
                font = new Font(font.getName(), style, font.getSize());
            }

            setFont(font);

            if (hasFocus) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder")); // NOT
                // LOCALIZABLE
                if (!valuesDiffer && table.isCellEditable(row, column)) {
                    setForeground(UIManager.getColor("Table.focusCellForeground")); // NOT
                    // LOCALIZABLE
                    setBackground(UIManager.getColor("Table.focusCellBackground")); // NOT
                    // LOCALIZABLE
                }
            } else {
                setBorder(noFocusBorder);
            }

            setValue(value);

            // ---- begin optimization to avoid painting background ----
            Color back = getBackground();
            boolean colorMatch = (back != null) && (back.equals(table.getBackground()))
                    && table.isOpaque();
            setOpaque(!colorMatch);
            // ---- end optimization to aviod painting background ----

            return this;
        }

        public void validate() {
        }

        public void revalidate() {
        }

        public void repaint(long tm, int x, int y, int width, int height) {
        }

        public void repaint(Rectangle r) {
        }

        protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            if (propertyName == "text") { // NOT LOCALIZABLE - property
                super.firePropertyChange(propertyName, oldValue, newValue);
            }
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        }

        protected void setValue(Object value) {
            String strValue = (value == null) ? "" : value.toString();
            if (strValue.equals("NaN")) {
                strValue = "Undefined";
            }

            setText(strValue);
        } // end setValue()

    } // end DesignRenderer

    private final class DesignEditor extends DefaultCellEditor {
        private HashMap<String, TableCellEditor> editorSamples = new HashMap<String, TableCellEditor>();
        private TableCellEditor delegate;

        public DesignEditor() {
            super(new JTextField());
            setClickCountToStart(2);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            DesignTableModel model = (DesignTableModel) table.getModel();
            String pluginName = model.getEditorAt(row);
            if (pluginName != null) {
                delegate = ScreenPlugins.getMultiEditor(pluginName);
            }
            if (delegate != null) {
                return delegate.getTableCellEditorComponent(table, value, isSelected, row, column);
            }
            return null;

        }

        public boolean stopCellEditing() {
            if (delegate == null)
                return super.stopCellEditing();
            if (!delegate.stopCellEditing()) {
                fireEditingStopped();
                return false;
            }
            fireEditingStopped();
            return true;
        }

        public void cancelCellEditing() {
            if (delegate == null) {
                super.cancelCellEditing();
                return;
            }
            delegate.cancelCellEditing();
            fireEditingCanceled();
        }

        public Object getCellEditorValue() {
            if (delegate == null)
                return super.getCellEditorValue();
            return delegate.getCellEditorValue();
        }

        public boolean isCellEditable(EventObject e) {
            DbObject[] dbos = getDesignTableModel().getDbObjects();
            boolean readonly = false; 
            boolean editableInViewerContext = false; 
            
            try {
                DbMultiTrans.beginTrans(Db.READ_TRANS, dbos, "");
                for (int i = 0; !readonly && i < dbos.length; i++) {
                    readonly = dbos[i].getProject().isIsLocked();
                }
                DbMultiTrans.commitTrans(dbos);
            } catch (DbException e1) {
                ExceptionHandler.processUncatchedException(DesignTable.this, e1);
                return false;
            }
            if (readonly)
                return false;
            
            boolean editable = true;

            if (e instanceof MouseEvent) {
                int row = rowAtPoint(((MouseEvent) e).getPoint());
                if (row > -1) {
                	DesignTableModel model = (DesignTableModel)getModel();
                    String name = model.getEditorAt(row);
                    TableCellEditor editor = editorSamples.get(name);
                    
                    if (editor == null) {
                        editor = ScreenPlugins.getMultiEditor(name);
                        editorSamples.put(name, editor);
                    }
                    if (editor != null) {
                    	editable = editor.isCellEditable(e);
                    	RowData rowData = model.getRowData(row); 
                    	MetaClass mc = rowData.metaclass; 
                    	editableInViewerContext = isEditableInViewerContext(editor, mc);
                    }
                }
            } else {
            	editable = super.isCellEditable(e);
            } //end if
            
            //The model viewer does not permit editiong, but the lookup description editor
            //works to allow the user to view the complete description
            if ((! ScreenPerspective.isFullVersion() && (! editableInViewerContext))) {
            	editable = false;
            } //end if
            
            return editable;
        }

        private boolean isEditableInViewerContext(TableCellEditor editor, MetaClass mc) {
        	//by default, a viewer cannot edit cells..
			boolean editableInViewerContext = false;
			
			//..but the following cases are permitted
			if (editor instanceof MultiLookupDescriptionEditor) {
				editableInViewerContext = true;
			} else if (editor instanceof MultiExternalDocumentEditor) {
				editableInViewerContext = true;
			} else {
				String name = mc.getJClass().getSimpleName();
				
				//cannot import sms.db.DbSMSNotice
				editableInViewerContext = "DbSMSNotice".equals(name); 
				
			} //end if
			
			return editableInViewerContext;
		}

		public boolean shouldSelectCell(EventObject anEvent) {
            if (delegate == null)
                return super.shouldSelectCell(anEvent);
            return delegate.shouldSelectCell(anEvent);
        }

        public void addCellEditorListener(CellEditorListener l) {
            if (delegate != null)
                delegate.addCellEditorListener(l);
            super.addCellEditorListener(l);
        }

        public void removeCellEditorListener(CellEditorListener l) {
            if (delegate != null)
                delegate.removeCellEditorListener(l);
            super.removeCellEditorListener(l);
        }

    }

    private DesignPanel designPanel;

    /**
     * @param dm
     */
    public DesignTable(DesignPanel designPanel) {
        this.designPanel = designPanel;
        init();
    }

    private void init() {
        setModel(new DesignTableModel(this));
        getTableHeader().setReorderingAllowed(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        setColumnSelectionAllowed(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setFillsViewportHeight(true);
        setShowVerticalLines(false);
        setShowHorizontalLines(true);
        //setIntercellSpacing(new Dimension(0,1));

        TableColumn column = getColumnModel().getColumn(1);
        column.setCellEditor(new DesignEditor());
        column.setCellRenderer(new DesignRenderer());
        column = getColumnModel().getColumn(0);
        column.setCellRenderer(new DesignRenderer());

    }

    DesignTableModel getDesignTableModel() {
        return (DesignTableModel) getModel();
    }

    DesignPanel getDesignPanel() {
        return designPanel;
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setGridColor(AwtUtil.darker(getBackground(), 0.9f));
        //         setSelectionBackground(getBackground());
        //         setSelectionForeground(getForeground());

    }

}
