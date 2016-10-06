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

package org.modelsphere.jack.srtool.list;

import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.SemanticalModel;
import org.modelsphere.jack.srtool.explorer.Explorer;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.SrVector;

/**
 * 
 * 
 * Contains all data for one model row
 * 
 */

final class RowData {
    SrVector values;
    SrVector semObjectFullNameValues = new SrVector();
    DbObject neighbor;
    DbObject composite;
    ListTableModel model;
    TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    RowData(ListTableModel model, DbObject neighbor, SrVector values, ArrayList columns,
            boolean load) throws DbException {
        this.model = model;
        this.values = values;
        this.neighbor = neighbor;
        this.composite = neighbor.getComposite();
        if (load)
            loadData(columns);
        else
            initSemObjectFullNameValues();
    }

    public Object getValue(int idx) {

        if (values != null)
            if (idx < values.size())
                return values.get(idx);

        return null;
    }

    void loadData(ArrayList columns) throws DbException {
        if (values == null)
            values = new SrVector();
        values.clear();
        composite = neighbor.getComposite();
        for (int i = 0; i < columns.size() && i < ListTableModel.MAX_COLUMNS_COUNT; i++) {
            Object value = null;
            ColumnInfo column = (ColumnInfo) columns.get(i);
            if (column.udf != null) { // udf
                value = neighbor.get(column.udf);
            } else if (column.objectType) { // object type
                value = neighbor.getMetaClass().getGUIName(false, true);
            } else if (column.customColumn != null) { // custom column -
                // delegate
                value = column.customColumn.getValue(model.root, neighbor);
                if (value instanceof DbObject) {
                    value = new DefaultComparableElement(value, ApplicationContext
                            .getSemanticalModel().getDisplayText((DbObject) value,
                                    DbObject.LONG_FORM, null, ListTableModel.class),
                            ApplicationContext.getSemanticalModel().getImage((DbObject) value,
                                    ListTableModel.class));
                }
            } else if (column.metafield == DbObject.fComponents) {
                // sequences
                DbObject directComposite = neighbor.getComposite();
                int index = -1;
                DbEnumeration dbEnum = directComposite.getComponents().elements(
                        neighbor.getMetaClass());
                while (dbEnum.hasMoreElements()) {
                    index++;
                    if (dbEnum.nextElement() == neighbor)
                        break;
                }
                dbEnum.close();
                value = new Integer(index);
            } else if (column.metafield == DbObject.fComposite) {
                value = composite;
                if (value == model.root || composite.getComposite() == model.root) {
                    DbObject dbo = (DbObject) value;
                    String displayText = "";
                    if (terminologyUtil.isObjectLine(dbo))
                        displayText = ApplicationContext.getSemanticalModel().getDisplayText(
                                (DbObject) value, Explorer.class);
                    else
                        displayText = ApplicationContext.getSemanticalModel().getDisplayText(
                                (DbObject) value, SemanticalModel.NAME_CUSTOM_FORM, model.root,
                                ListTableModel.class);

                    value = new DefaultComparableElement(value, displayText, ApplicationContext
                            .getSemanticalModel().getImage((DbObject) value, ListTableModel.class));

                } else {
                    DbObject dbo = (DbObject) value;
                    String displayText = ApplicationContext.getSemanticalModel().getDisplayText(
                            (DbObject) value, DbObject.LONG_FORM, null, ListTableModel.class);
                    value = new DefaultComparableElement(value, displayText, ApplicationContext
                            .getSemanticalModel().getImage((DbObject) value, ListTableModel.class));
                }
            } else if (column.path != null) {
                value = column.path.get(neighbor);
            } else if (neighbor.hasField(column.metafield)) {

                if (terminologyUtil.isObjectLine(neighbor)
                        && column.metafield == DbSemanticalObject.fName)
                    value = ApplicationContext.getSemanticalModel().getDisplayText(neighbor,
                            Explorer.class);
                else
                    value = neighbor.get(column.metafield);
                if (value instanceof DbObject) {
                    if (value == model.root
                            || ((DbObject) value).getCompositeOfType(model.root.getMetaClass()) == model.root)
                        value = new DefaultComparableElement(value, ApplicationContext
                                .getSemanticalModel().getDisplayText((DbObject) value,
                                        SemanticalModel.NAME_CUSTOM_FORM, model.root,
                                        ListTableModel.class), ApplicationContext
                                .getSemanticalModel().getImage((DbObject) value,
                                        ListTableModel.class));
                    else
                        value = new DefaultComparableElement(value, ApplicationContext
                                .getSemanticalModel().getDisplayText((DbObject) value,
                                        DbObject.LONG_FORM, null, ListTableModel.class),
                                ApplicationContext.getSemanticalModel().getImage((DbObject) value,
                                        ListTableModel.class));
                }
            }
            values.add(value);
        }
        initSemObjectFullNameValues();
    }

    private void initSemObjectFullNameValues() throws DbException {
        semObjectFullNameValues.clear();
        for (int i = 0; i < values.size(); i++) {
            Object value = values.get(i);
            if ((value instanceof DefaultComparableElement)
                    && (((DefaultComparableElement) value).object instanceof DbSemanticalObject)) {
                String displayText = ApplicationContext.getSemanticalModel().getDisplayText(
                        (DbObject) ((DefaultComparableElement) value).object, DbObject.LONG_FORM,
                        null, ListTableModel.class);
                semObjectFullNameValues.add(displayText);
            } else
                semObjectFullNameValues.add(null);
        }
    }

    void restructurate(int oldindex, int newindex) {
        Object oldValue = values.get(oldindex);
        Object newValue = values.get(newindex);
        values.remove(oldindex);
        values.add(oldindex, newValue);
        values.remove(newindex);
        values.add(newindex, oldValue);
        oldValue = semObjectFullNameValues.get(oldindex);
        newValue = semObjectFullNameValues.get(newindex);
        semObjectFullNameValues.remove(oldindex);
        semObjectFullNameValues.add(oldindex, newValue);
        semObjectFullNameValues.remove(newindex);
        semObjectFullNameValues.add(newindex, oldValue);
    }
}
