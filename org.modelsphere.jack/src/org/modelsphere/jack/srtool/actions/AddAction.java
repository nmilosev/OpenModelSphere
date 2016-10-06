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

import java.util.ArrayList;

import org.modelsphere.jack.actions.AbstractMenuAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.srtool.AddElement;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;

public abstract class AddAction extends AbstractMenuAction implements SelectionActionListener {

    private boolean[][] mask;
    private AddElement[] elements;

    public AddAction() {
        super(LocaleMgr.action.getString("Add"), LocaleMgr.action.getImageIcon("Add"),
                new Object[] {});
        setVisibilityMode(VISIBILITY_ALWAYS_VISIBLE_IN_MENU | VISIBILITY_ALWAYS_VISIBLE_IN_TOOLBAR);
        setEnabled(false);
        //setVisible(ScreenPerspective.isFullVersion());
    }

    private void init() {
        elements = ApplicationContext.getSemanticalModel().getAddElements();
        if (elements == null) {
            elements = new AddElement[] {};
        }
        
        mask = new boolean[elements.length][MetaClass.getNbMetaClasses()];
        for (int i = 0; i < elements.length; i++) {
            AddElement element = elements[i];
            boolean visibleElement = isVisibleElement(element);
            
            if (visibleElement) {
                MetaClass[] composites = element.getCompositeMetaClasses();
                if (composites == null) {
                    continue;
                }
                
                for (int j = 0; j < composites.length; j++) {
                    // we must flag all subclasses of each composite
                    mask[i][composites[j].getSeqNo()] = true;
                    includeSubMetaClasses(i, composites[j]);
                } //end for
            } //end if
        } //end for
    } //end init()

    protected boolean isVisibleElement(AddElement element) {
        return true;
    }

    // private void updateAddElementTerms() {
    //
    // if (ApplicationContext.getSemanticalModel().getTerminology() != null)
    // init();
    // else {
    // DbObject[] dbObject =
    // ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
    // if (dbObject != null)
    // if (dbObject.length > 0)
    // if (dbObject[0] instanceof DbProject)
    // init();
    // }
    // }

    private void includeSubMetaClasses(int elementIndex, MetaClass metaclass) {
        MetaClass[] subMetaClass = metaclass.getSubMetaClasses();
        if (subMetaClass == null)
            return;
        for (int i = 0; i < subMetaClass.length; i++) {
            mask[elementIndex][subMetaClass[i].getSeqNo()] = true;
            includeSubMetaClasses(elementIndex, subMetaClass[i]);
        }
    }

    public final void updateSelectionAction() throws DbException {
        if (elements == null)
            init();

        // updateAddElementTerms();

        DbObject[] selobjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        if (selobjs == null || selobjs.length == 0) {
            Object focusObject = ApplicationContext.getFocusManager().getFocusObject();
            Object[] allSelObjs = ApplicationContext.getFocusManager().getSelectedObjects();
            // we must check for empty graphical object selection before
            // considering the diagram as the selected object.
            // otherwise, we will give the impression of Add possibilities on
            // the selected graphical object
            if (focusObject instanceof ApplicationDiagram && allSelObjs != null
                    && allSelObjs.length == 0) {
                DbObject diagramGO = ((ApplicationDiagram) focusObject).getDiagramGO();
                selobjs = new DbObject[] { diagramGO };
            } else {
                setEnabled(false);
                setVisible(false);
                return;
            }
        }
        boolean[] elementsincluded = null;
        boolean containsElement = true;
        for (int i = 0; i < selobjs.length && containsElement; i++) {
            int seqNo = selobjs[i].getMetaClass().getSeqNo();
            if (elementsincluded == null) {
                elementsincluded = new boolean[elements.length];
                containsElement = markElements(elementsincluded, seqNo, true);
            } else {
                containsElement = markElements(elementsincluded, seqNo, false);
            }
        }

        if (!containsElement || elementsincluded == null) {
            setEnabled(false);
            setVisible(false);
            return;
        }

        ArrayList values = new ArrayList();
        for (int i = 0; i < elementsincluded.length; i++) {
            if (elementsincluded[i]) {
                values.add(elements[i]);
                elements[i].update();
            }
        }

        for (int i = 0; i < values.size(); i++) {
            String name = ((AddElement) values.get(i)).getName();
            if (name != null)
                if (name.equals("-"))
                    values.set(i, null);
        }

        setDomainValues(values.toArray());
        setEnabled(true);
        setVisible(true);
    }

    // will return false if markedElements contains no elements (all value
    // false)
    private boolean markElements(boolean[] markedElements, int metaClassSeqNo, boolean init) {
        boolean containsElement = false;
        for (int i = 0; i < markedElements.length; i++) {
            if (init)
                markedElements[i] = mask[i][metaClassSeqNo];
            else
                markedElements[i] = mask[i][metaClassSeqNo] && markedElements[i];
            containsElement = markedElements[i] || containsElement;
        }
        return containsElement;
    }

}
