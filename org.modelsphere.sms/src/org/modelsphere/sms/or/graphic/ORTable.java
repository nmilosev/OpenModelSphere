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

package org.modelsphere.sms.or.graphic;

import java.awt.Rectangle;
import java.util.HashSet;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbRefreshPassListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.shape.GraphicShape;
import org.modelsphere.jack.graphic.shape.OvalShape;
import org.modelsphere.jack.graphic.shape.RectangleShape;
import org.modelsphere.jack.graphic.shape.RoundRectShape;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORNotation;
import org.modelsphere.sms.or.db.DbORStyle;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTableGo;

public class ORTable extends ORTableBox {

    static {
        BoxRefreshTg boxRefreshTg = new BoxRefreshTg();
        Db.addDbRefreshPassListener(boxRefreshTg);
        MetaField.addDbRefreshListener(boxRefreshTg, null, new MetaField[] {
                DbORTable.fIsAssociation, DbORTable.fIsDependant, });
    }

    public ORTable(org.modelsphere.jack.graphic.Diagram diag, DbORTableGo newTableGO)
            throws DbException {
        super(diag, newTableGO);
        DbORTable table = (DbORTable) newTableGO.getSO();
        boolean isAssociationTable = table.isIsAssociation();
        boolean isDependant = table.isIsDependant();
        setAppropriateShape(newTableGO, isAssociationTable, isDependant);
    }

    //
    // private methods
    //
    private static void setAppropriateShape(DbORTableGo tableGO, boolean isAssociationTable,
            boolean isDependant) throws DbException {
        ORTable peer = (ORTable) tableGO.getGraphicPeer();
        if (peer == null) {
            return;
        }

        GraphicShape oldShape = peer.getShape();
        GraphicShape newShape = RectangleShape.singleton;

        // get style element
        boolean associationTablesAsRelationship = (((Boolean) tableGO
                .find(DbORStyle.fOr_associationTablesAsRelationships)).booleanValue());
        boolean dependantTable = doShowDependentTable(tableGO); 

        if (isAssociationTable) {
            if (associationTablesAsRelationship) {
                newShape = OvalShape.singleton;
            }
        } else if (isDependant) {
            if (dependantTable) {
                newShape = RoundRectShape.singleton;
            }
        } // end if

        peer.setShape(newShape);
        if ((!(oldShape instanceof OvalShape)) && (newShape instanceof OvalShape)) {
            Rectangle r = peer.getRectangle();
            int orig_width = r.width;
            r.width *= 1.5;

            if (isAssociationTable) { // compensate x coordinate for the width
                // change
                int offset = 0;
                if (orig_width >= r.width) {
                    r.x += offset;
                    offset = (orig_width - r.width) / 2;
                } else {
                    offset = (r.width - orig_width) / 2;
                    r.x -= offset;
                }
            }
            peer.setRectangle(r);

        } else if ((oldShape instanceof OvalShape) && !(newShape instanceof OvalShape)) {
            Rectangle r = peer.getRectangle();
            r.width /= 1.5;
            peer.setRectangle(r);
        } // end if

        peer.repaint();

    } // end setAppropriateShape()
    
    static boolean doShowDependentTable(DbORTableGo tableGO) throws DbException {
    	//get notation
    	DbORDiagram diag = (DbORDiagram)tableGO.getCompositeOfType(DbORDiagram.metaClass); 
    	DbORNotation notation = diag.getNotation();
    	Boolean b = (notation == null) ? null : notation.getShowDependentTables(); 
    	boolean showDependentTable = (b == null) ? false : b.booleanValue(); 
    	
    	if (! showDependentTable) {
    		b = (Boolean) tableGO.find(DbORStyle.fOr_showDependentTables);
    		showDependentTable = (b == null) ? false : b.booleanValue();
    	}
    	
	    return showDependentTable;
	}

    //
    // inner classes
    //
    private static class BoxRefreshTg implements DbRefreshListener, DbRefreshPassListener {
        private HashSet refreshedTables = null;

        // implements DbRefreshPassListener
        public void beforeRefreshPass(Db db) throws DbException {
            refreshedTables = null;
        }

        // implements DbRefreshPassListener
        public void afterRefreshPass(Db db) throws DbException {
            refreshedTables = null; // for gc
        }

        // implements DbRefreshListener
        public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
            refreshAfterDbUpdate(e.dbo, e.metaField);
        }

        // private methods
        private void refreshAfterDbUpdate(DbObject obj, MetaField mf) throws DbException {
            if (!(obj instanceof DbORTable)) {
                return;
            }

            DbORTable table = (DbORTable) obj;
            boolean isAssociationTable = table.isIsAssociation();
            boolean isDependant = table.isIsDependant();

            // for each graphical representation
            DbRelationN relN = table.getClassifierGos();
            DbEnumeration dbEnum = relN.elements(DbORTableGo.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORTableGo tableGO = (DbORTableGo) dbEnum.nextElement();
                setAppropriateShape(tableGO, isAssociationTable, isDependant);
            } // end while
            dbEnum.close();
        } // end refreshAfterDbUpdate()
    } // end BoxRefreshTg

} // end ORTable
