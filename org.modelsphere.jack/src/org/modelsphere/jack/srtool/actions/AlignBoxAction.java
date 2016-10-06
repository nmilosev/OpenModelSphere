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

package org.modelsphere.jack.srtool.actions;

import java.awt.Point;
import java.awt.Rectangle;

import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.ZoneBox;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;

@SuppressWarnings("serial")
public final class AlignBoxAction extends AbstractDomainAction implements SelectionActionListener {
    private static final String kAlignBoxAction = LocaleMgr.action.getString("AlignBox");

    public AlignBoxAction() {
        super(kAlignBoxAction, false);
        setDomainValues(AlignBoxDomain.stringPossibleValues);
        setVisible(true);
        setEnabled(false);
    }

    protected final void doActionPerformed() {
        int idx = getSelectedIndex();
        AlignBoxDomain value = AlignBoxDomain.objectPossibleValues[idx];
        int alignment = value.getValue();
        
        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        try {
        	//find new position of alignment
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, objects, kAlignBoxAction);
            
            //find upperLeft and lowerRight, based on 1st box
            ZoneBox box0 = (ZoneBox) objects[0];
            Rectangle box0GoRec = box0.getRectangle();
            DbObject box0Go = ((ActionInformation) box0).getGraphicalObject();
            //Rectangle box0GoRec = (Rectangle) box0Go.get(DbGraphic.fGraphicalObjectRectangle);
            Point upperLeft = new Point(box0GoRec.x, box0GoRec.y);
            Point lowerRight = new Point(box0GoRec.x + box0GoRec.width, box0GoRec.y + box0GoRec.height);
            
            for (int i = 0; i < objects.length; i++) {
                ZoneBox box = (ZoneBox) objects[i];
                Rectangle boxRec = box.getRectangle();
                findContainerRectangle(boxRec, upperLeft, lowerRight, alignment);
            } //end for
            
            //move each box to its new position
            for (int i = 0; i < objects.length; i++) {
                ZoneBox box = (ZoneBox) objects[i];
                DbObject boxGo = ((ActionInformation) box).getGraphicalObject();
                Rectangle boxRec = box.getRectangle();
                performAlignment(boxRec, upperLeft, lowerRight, alignment);
                boxGo.set(DbGraphic.fGraphicalObjectRectangle, boxRec);
            } 
           
            //refresh diagram
            refreshDiagram(box0, upperLeft, lowerRight);
            
            DbMultiTrans.commitTrans(objects);
            
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }
    
	private void refreshDiagram(GraphicComponent figure, Point upperLeft, Point lowerRight) throws DbException {
		Diagram diagram = figure.getDiagram(); 
        if (diagram instanceof ApplicationDiagram) {
        	ApplicationDiagram appDiag = (ApplicationDiagram)diagram;
        	DiagramInternalFrame frame = appDiag.getDiagramInternalFrame();
        	frame.refresh();
        }
        
		 //future optimization : refresh only the containing rectangle
        int width = lowerRight.x - upperLeft.x;
        int height = lowerRight.y - upperLeft.y;
        Rectangle refreshRectangle = new Rectangle(upperLeft.x, upperLeft.y, width, height);
	}

	private void findContainerRectangle(Rectangle boxRec, Point upperLeft, Point lowerRight, int alignment) {
    	if (boxRec.x < upperLeft.x) {
    		upperLeft.x = boxRec.x;
    	} 

    	if (boxRec.y < upperLeft.y) {
    		upperLeft.y = boxRec.y;
    	} 

    	if ((boxRec.x + boxRec.width) > lowerRight.x) {
    		lowerRight.x = boxRec.x + boxRec.width;
    	}
    	
    	if ((boxRec.y + boxRec.height) > lowerRight.y) {
    		lowerRight.y = boxRec.y + boxRec.height;
    	}
    }
	
    private void performAlignment(Rectangle boxRec, Point upperLeft, Point lowerRight, int alignment) {
    	int dx = 0, dy = 0;
    	
    	switch (alignment) {
    	case AlignBoxDomain.TOP:
    		dy = upperLeft.y - boxRec.y;
    		break;
    	case AlignBoxDomain.CENTER:
    		dy = ((upperLeft.y + lowerRight.y) / 2) - boxRec.y - (boxRec.height / 2);
    		break;
    	case AlignBoxDomain.BOTTOM:
    		dy = lowerRight.y - boxRec.y - boxRec.height;
    		break;
    	case AlignBoxDomain.LEFT:
    		dx = upperLeft.x - boxRec.x;
    		break;
    	case AlignBoxDomain.MIDDLE:
    		dx = ((upperLeft.x + lowerRight.x) / 2) - boxRec.x - (boxRec.width / 2);
    		break;
    	case AlignBoxDomain.RIGHT:
    		dx = lowerRight.x - boxRec.x - boxRec.width;
    		break;
    	} 
    	
    	boxRec.translate(dx, dy);
	}

    /*
    private int findAlignmentPosition(Rectangle boxRec, Point upperLeft, Point lowerRight, int alignment) {
    	if (alignment == AlignBoxDomain.TOP) {
            if (newPosition == -1)
            	newPosition = boxRec.y;
            else if (boxRec.y < newPosition)
            	newPosition = boxRec.y;
        } else if (alignment == AlignBoxDomain.RIGHT) {
            if (newPosition == -1)
            	newPosition = boxRec.x + boxRec.width;
            else if ((boxRec.x + boxRec.width) > newPosition)
            	newPosition = boxRec.x + boxRec.width;
        } else if (alignment == AlignBoxDomain.BOTTOM) {
            if (newPosition == -1)
            	newPosition = boxRec.y + boxRec.height;
            else if ((boxRec.y + boxRec.height) > newPosition)
            	newPosition = boxRec.y + boxRec.height;
        } else if (alignment == AlignBoxDomain.LEFT) {
            if (newPosition == -1)
            	newPosition = boxRec.x;
            else if (boxRec.x < newPosition)
            	newPosition = boxRec.x;
        }
    	
    	return newPosition;
	}
	*/

	public final void updateSelectionAction() throws DbException {
        if (!(ApplicationContext.getFocusManager().getFocusObject() instanceof ApplicationDiagram)) {
            setEnabled(false);
            return;
        }

        Object[] objects = ApplicationContext.getFocusManager().getSelectedObjects();
        if (objects.length < 2) {
            setEnabled(false);
            return;
        }

        for (int i = 0; i < objects.length; i++) {
            if (!(objects[i] instanceof ZoneBox && objects[i] instanceof ActionInformation)) {
                setEnabled(false);
                setVisible(false);
                return;
            }
        }
        setEnabled(true);
        setVisible(true);
    }

    // inner class domain
    static class AlignBoxDomain extends IntDomain {
        public static final int TOP = 0;
        public static final int CENTER = 1;
        public static final int BOTTOM = 2;
        public static final int SEPARATOR = 3;
        public static final int LEFT = 4;
        public static final int MIDDLE = 5;
        public static final int RIGHT = 6;
        
        public static final AlignBoxDomain[] objectPossibleValues = new AlignBoxDomain[] {
                new AlignBoxDomain(AlignBoxDomain.TOP), 
                new AlignBoxDomain(AlignBoxDomain.CENTER), 
                new AlignBoxDomain(AlignBoxDomain.BOTTOM), 
                new AlignBoxDomain(AlignBoxDomain.SEPARATOR), 
                new AlignBoxDomain(AlignBoxDomain.LEFT),
                new AlignBoxDomain(AlignBoxDomain.MIDDLE),
                new AlignBoxDomain(AlignBoxDomain.RIGHT) 
        };

        public static final String[] stringPossibleValues = new String[] {
                LocaleMgr.action.getString("AlignTop"), 
                LocaleMgr.action.getString("AlignMiddle"),
                LocaleMgr.action.getString("AlignBottom"),
                null,
                LocaleMgr.action.getString("AlignLeft"),
                LocaleMgr.action.getString("AlignCenter"),
                LocaleMgr.action.getString("AlignRight") 
        };

        public static AlignBoxDomain getInstance(int value) {
            return objectPossibleValues[objectPossibleValues[0].indexOf(value)];
        }

        protected AlignBoxDomain(int value) {
            super(value);
        }

        public final DbtAbstract duplicate() {
            return new AlignBoxDomain(value);
        }

        public final Domain[] getObjectPossibleValues() {
            return objectPossibleValues;
        }

        public final String[] getStringPossibleValues() {
            return stringPossibleValues;
        }
    }
}
