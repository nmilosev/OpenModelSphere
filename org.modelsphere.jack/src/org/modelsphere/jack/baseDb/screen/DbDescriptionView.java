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

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.screen.model.*;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.international.LocaleMgr;

public class DbDescriptionView extends DescriptionView {

    protected DbObject dbObj;
    private Font propFont;
    private Font UDFFont;

    public DbDescriptionView(ScreenContext screenContext, DbObject newDbObj) throws DbException {
        super(screenContext);
        dbObj = newDbObj;
        setTabName(LocaleMgr.screen.getString("Design"));
        setModel(createDescrModel());
    }

    protected DbDescriptionModel createDescrModel() throws DbException {
        return new ReflectionDescriptionModel(this, dbObj);
    }

    // overriding methods must call super.setModel
    public void setModel(DescriptionModel newModel) {
        super.setModel(newModel);
        propFont = table.getFont();
        UDFFont = propFont.deriveFont(Font.ITALIC);
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super
                        .getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                                column);

                DescriptionField dbField = (DescriptionField) ((DbDescriptionModel) descrModel)
                        .getDescriptionFieldAt(row);
                setFont((DescriptionField) dbField instanceof UDFDescriptionField ? UDFFont
                        : propFont);

                if (isSelected == true) {
                    setForeground(table.getSelectionForeground());
                    setBackground(table.getSelectionBackground());
                } else {
                    if (dbField.isEnabled() == false) {
                        setForeground(TerminologyUtil.getMarkedMetaFieldsFontColor());
                        setBackground(TerminologyUtil.getMarkedMetaFieldsBackColor());
                        // dbField.setEditable(false);
                    } else {
                        setForeground(table.getForeground());
                        setBackground(table.getBackground());
                    }
                }

                return this;
            }
        });
        /*
         * table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
         * public Component getTableCellRendererComponent(JTable table, Object value, boolean
         * isSelected, boolean hasFocus, int row, int column) { Component comp =
         * super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         * 
         * MetaFieldDescriptionField dbField = (MetaFieldDescriptionField)((DbDescriptionModel
         * )descrModel).getDescriptionFieldAt(row); comp.setFont((DescriptionField)dbField
         * instanceof UDFDescriptionField ? UDFFont : propFont);
         * 
         * if(isSelected == true){ comp.setForeground(table.getSelectionForeground());
         * comp.setBackground(table.getSelectionBackground()); } else{ if(dbField.isEnabled() ==
         * false){ comp.setForeground(TerminologyManager .getMarkedMetaFieldsFontColor());
         * comp.setBackground(TerminologyManager .getMarkedMetaFieldsBackColor());
         * //dbField.setEditable(false); } else{ comp.setForeground(table.getForeground());
         * comp.setBackground(table.getBackground()); } }
         * 
         * return comp; } });
         */
    }

    // called by PropertiesFrame.commit
    public final void commit() throws DbException {
        stopEditing(); // close current cell edition to have the edited value
        // copied to the model.
        ((DbDescriptionModel) descrModel).commit();
    }

    public final void resetHasChanged() {
        descrModel.resetHasChanged();
    }

    public final void refresh() throws DbException {
        stopEditing();
        ((DbDescriptionModel) descrModel).refresh();
    }
}
