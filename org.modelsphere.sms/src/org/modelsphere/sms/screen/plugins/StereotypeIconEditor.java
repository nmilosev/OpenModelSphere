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
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JDialog;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.screen.Editor;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.screen.model.ScreenModel;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.db.DbSMSPackageGo;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.oo.db.DbOOAdtGo;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.db.DbOOPackage;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORTableGo;

public class StereotypeIconEditor implements Editor {

    public StereotypeIconEditor() {
    }

    public Component getTableCellEditorComponent(ScreenView screenView,
            AbstractTableCellEditor tableCellEditorListener, Object value, boolean isSelected,
            int row, int column) {

        if ((value != null) && !(value instanceof Image))
            return null;

        Image image = (Image) value;

        ScreenModel model = screenView.getModel();
        DbObject obj = (DbObject) model.getParentValue(0);
        if ((obj != null) && (obj instanceof DbSMSStereotype)) {
            try {
                DbSMSStereotype stereotype = (DbSMSStereotype) obj;
                StereotypeIconChooser chooser = new StereotypeIconChooser(stereotype);
                MainFrame owner = MainFrame.getSingleton();
                JDialog dialog = chooser.getDialog(owner);
                dialog.setVisible(true);
                if (!dialog.isVisible()) {
                    if (!chooser.isCancelled()) {
                        Db db = stereotype.getDb();
                        String transName = chooser.getTransName();
                        db.beginWriteTrans(transName);
                        stereotype.setIcon(chooser.getImage());
                        refreshStereotypedObjects(owner, stereotype);
                        db.commitTrans();
                    }
                } // end if
            } catch (DbException ex) {
            } // end try
        }
        return null;
    } // end getTableCellEditorComponent()

    // called by StereotypeIconEditor.getTableCellEditorComponent() &
    // MultiStereotypeIconEditor.getTableCellEditorComponent()
    static void refreshStereotypedObjects(MainFrame owner, DbSMSStereotype stereotype)
            throws DbException {
        ArrayList internalFrameList = new ArrayList(); // list of affected
        // internal frames
        DbRelationN relN = stereotype.getStereotypedObjects();

        // look for all the class diagrams affected
        DbEnumeration dbEnum = relN.elements(DbJVClass.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVClass claz = (DbJVClass) dbEnum.nextElement();
            DbRelationN relN2 = claz.getClassifierGos();
            DbEnumeration enum2 = relN2.elements(DbOOAdtGo.metaClass);

            while (enum2.hasMoreElements()) {
                DbOOAdtGo adtGo = (DbOOAdtGo) enum2.nextElement();
                DbOODiagram diag = (DbOODiagram) adtGo.getCompositeOfType(DbOODiagram.metaClass);
                DiagramInternalFrame frame = (DiagramInternalFrame) owner
                        .getDiagramInternalFrame(diag);
                if (!internalFrameList.contains(frame)) {
                    internalFrameList.add(frame);
                }
            } // end while
            enum2.close();
        } // end while
        dbEnum.close();

        dbEnum = relN.elements(DbOOPackage.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbOOPackage ooPack = (DbOOPackage) dbEnum.nextElement();
            DbRelationN relN2 = ooPack.getPackageGos();
            DbEnumeration enum2 = relN2.elements(DbSMSPackageGo.metaClass);
            while (enum2.hasMoreElements()) {
                DbSMSPackageGo packGo = (DbSMSPackageGo) enum2.nextElement();
                DbOODiagram diag = (DbOODiagram) packGo.getCompositeOfType(DbORDiagram.metaClass);
                DiagramInternalFrame frame = (DiagramInternalFrame) owner
                        .getDiagramInternalFrame(diag);
                if (!internalFrameList.contains(frame)) {
                    internalFrameList.add(frame);
                }
            } // end while
            enum2.close();
        } // end while
        dbEnum.close();

        // look for all the relational diagrams affected
        dbEnum = relN.elements(DbORAbsTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORAbsTable table = (DbORAbsTable) dbEnum.nextElement();
            DbRelationN relN2 = table.getClassifierGos();
            DbEnumeration enum2 = relN2.elements(DbORTableGo.metaClass);
            while (enum2.hasMoreElements()) {
                DbORTableGo tabGo = (DbORTableGo) enum2.nextElement();
                DbORDiagram diag = (DbORDiagram) tabGo.getCompositeOfType(DbORDiagram.metaClass);
                DiagramInternalFrame frame = (DiagramInternalFrame) owner
                        .getDiagramInternalFrame(diag);
                if (!internalFrameList.contains(frame)) {
                    internalFrameList.add(frame);
                }
            } // end while
            enum2.close();
        } // end while
        dbEnum.close();

        // Refresh all internal frames affected
        Iterator iter = internalFrameList.iterator();
        while (iter.hasNext()) {
            DiagramInternalFrame frame = (DiagramInternalFrame) iter.next();
            if (frame != null) {
                frame.refresh();
            }
        } // end while
        internalFrameList.clear();

    } // end refreshStereotypedObjects()

    public boolean stopCellEditing() {
        return true;
    }

    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public int getClickCountForEditing() {
        return 2;
    }
} // end StereotypeIconEditor
