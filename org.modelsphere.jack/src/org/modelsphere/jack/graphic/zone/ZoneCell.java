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

package org.modelsphere.jack.graphic.zone;

public class ZoneCell implements Cell {
    private Object object; // the object from which is taken the data (may be null)
    private Object data; // the data to be rendered
    private CellRenderingOption option;
    private CellEditor editor;
    private Object paintData = null; // data calculated by renderer.getDimension to be passed to renderer.paint
    private int dataWidth = 0; // calculated by renderer.getDimension
    private boolean selected = false;
    private boolean editable = true;

    public ZoneCell(Object newObject, Object newData, CellRenderingOption newOption,
            CellEditor newCellEditor) {
        object = newObject;
        data = newData;
        option = newOption;
        editor = newCellEditor;
    }

    public ZoneCell(Object newObject, Object newData) {
        object = newObject;
        data = newData;
        option = null;
        editor = null;
    }

    public ZoneCell(Object newObject, Object newData, boolean editable) {
        object = newObject;
        data = newData;
        option = null;
        editor = null;
        this.editable = editable;
    }

    public final Object getObject() {
        return object;
    }

    public final Object getCellData() {
        return data;
    }

    public final void setObject(Object newObject) {
        object = newObject;
    }

    public final void setCellData(Object newData) {
        data = newData;
    }

    public final Object getPaintData() {
        return paintData;
    }

    public final void setPaintData(Object paintData) {
        this.paintData = paintData;
    }

    public final int getDataWidth() {
        return dataWidth;
    }

    public final void setDataWidth(int dataWidth) {
        this.dataWidth = dataWidth;
    }

    public final boolean isSelected() {
        return selected;
    }

    public final void setSelected(boolean newSelected) {
        selected = newSelected;
    }

    public final CellRenderingOption getCellRenderingOption() {
        return option;
    }

    public final CellEditor getCellEditor() {
        return editor;
    }

    public final void setCellEditor(CellEditor cellEditor) {
        editor = cellEditor;
    }

    public final void setEditable(boolean editable) {
        this.editable = editable;
    }

    public final boolean isEditable() {
        return editable;
    }
}
