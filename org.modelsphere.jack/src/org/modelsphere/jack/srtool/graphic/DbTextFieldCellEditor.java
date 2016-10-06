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

package org.modelsphere.jack.srtool.graphic;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.modelsphere.jack.awt.NumericTextField;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.*;
import org.modelsphere.jack.graphic.zone.*;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.StringUtil;

public class DbTextFieldCellEditor extends TextFieldCellEditor {

    private String className;
    private MetaField metaField;
    private boolean insertOnEnter;
    private Object theValue = null;

    public DbTextFieldCellEditor(MetaField metaField, boolean insertOnEnter) {
        this.className = null;
        this.metaField = metaField;
        this.insertOnEnter = insertOnEnter;
    }

    public DbTextFieldCellEditor(String className, MetaField metaField, boolean insertOnEnter) {
        this.className = className;
        this.metaField = metaField;
        this.insertOnEnter = insertOnEnter;
    }

    public JComponent getComponent(ZoneBox box, CellID cellID, ZoneCell value, DiagramView view,
            Rectangle cellRect) {
        if (this.metaField == null)
            return null;

        this.box = box;
        this.cellID = cellID;
        this.value = value;
        diagram = box.getDiagram();
        setText();
        Dimension size = textField.getPreferredSize();
        textField.setPreferredSize(new Dimension(size.width + 50, size.height));
        textField.selectAll();
        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    diagram.removeEditor(CellEditor.CANCEL);
                    break;
                case KeyEvent.VK_UP:
                    diagram.removeEditor(CellEditor.UP_ARROW_ENDING);
                    break;
                case KeyEvent.VK_DOWN:
                    diagram.removeEditor(CellEditor.DOWN_ARROW_ENDING);
                    break;
                case KeyEvent.VK_ENTER:
                    diagram.removeEditor(e.isControlDown() ? CellEditor.INSERT_ROW_ENDING
                            : CellEditor.NORMAL_ENDING);
                    break;
                }
            }
        });
        return textField;
    }

    public final void setText() {
        String text = "";
        DbObject semObj = (DbObject) value.getObject();
        try {
            semObj.getDb().beginTrans(Db.READ_TRANS);
            theValue = semObj.get(metaField);
            semObj.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                    ((ApplicationDiagram) diagram).getMainView(), ex);
        }
        if (theValue instanceof Integer) {
            textField = new NumericTextField(NumericTextField.INTEGER);
            text = theValue.toString();
        } else if (theValue instanceof Double) {
            textField = new NumericTextField(NumericTextField.DOUBLE);
            text = theValue.toString();
        } else if (theValue instanceof Long) {
            textField = new NumericTextField(NumericTextField.LONG);
            text = theValue.toString();
        } else {
            textField = new JTextField();
            text = (String) theValue;
        }
        textField.setText(text);
    }

    public final void stopEditing(int endCode) {
        if (endCode == CellEditor.CANCEL)
            return;
        DbObject semObj = (DbObject) value.getObject();
        Db db = semObj.getDb();
        String clsName = (className != null ? className : semObj.getMetaClass().getGUIName());
        try {
            String text = textField.getText();
            if ((theValue instanceof Integer || theValue instanceof Double || theValue instanceof Long)
                    && StringUtil.isEmptyString(text))
                return;
            if (theValue instanceof Integer)
                theValue = new Integer(text);
            else if (theValue instanceof Double)
                theValue = new Double(text);
            else if (theValue instanceof Long)
                theValue = new Long(text);
            else
                theValue = text;
            String pattern = LocaleMgr.action.getString("change01");
            db.beginTrans(Db.WRITE_TRANS, MessageFormat.format(pattern, new Object[] { clsName,
                    metaField.getGUIName() }));
            semObj.set(metaField, theValue);
            db.commitTrans();

            switch (endCode) {
            case CellEditor.INSERT_ROW_ENDING:
                if (!insertOnEnter)
                    break;
                pattern = LocaleMgr.action.getString("add0");
                db.beginTrans(Db.WRITE_TRANS, MessageFormat.format(pattern,
                        new Object[] { clsName }));
                DbObject newObj = semObj.copyComponent();
                DbObject parent = semObj.getComposite();

                if (newObj != null) {
                    DbRelationN comps = parent.getComponents();
                    parent.reinsert(DbObject.fComponents, comps.size() - 1,
                            comps.indexOf(semObj) + 1);
                }
                db.commitTrans();
                if (newObj != null) {
                    CellID newCellID = ((MatrixZone) cellID.zone).getCellID(newObj,
                            ((MatrixCellID) cellID).col);
                    ((ApplicationDiagram) diagram).editCell(box, newCellID);
                }
                break;

            case CellEditor.UP_ARROW_ENDING:
            case CellEditor.DOWN_ARROW_ENDING:
                if (!(cellID.zone instanceof MatrixZone))
                    break;
                int rowCount = ((MatrixZone) cellID.zone).getRowCount();
                if (rowCount < 2)
                    break;
                int row = ((MatrixCellID) cellID).row;
                if (endCode == CellEditor.DOWN_ARROW_ENDING) {
                    row++;
                    if (row >= rowCount)
                        row = 0;
                } else {
                    row = (row > 0 ? row - 1 : rowCount - 1);
                }
                CellID newCellID = new MatrixCellID(cellID.zone, row, ((MatrixCellID) cellID).col);
                ((ApplicationDiagram) diagram).editCell(box, newCellID);
                break;
            }
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                    ((ApplicationDiagram) diagram).getMainView(), ex);
        }
    }
}
