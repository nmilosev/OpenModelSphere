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

package org.modelsphere.sms.be.graphic.tool;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.be.BEModule;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEFlowGo;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.features.Renumbering;
import org.modelsphere.sms.be.graphic.BEActorBox;
import org.modelsphere.sms.be.graphic.BEFlow;
import org.modelsphere.sms.be.graphic.BEFlowLabel;
import org.modelsphere.sms.be.graphic.BEStoreBox;
import org.modelsphere.sms.be.graphic.BEUseCaseBox;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.DbSMSClassifier;

public final class BERenumberingTool extends Tool {
    private Cursor cursor;
    private static final String RENUMBERING = LocaleMgr.screen.getString("Renumbering");
    private static final String CURSOR_FILENAME = "international/resources/renum.gif"; // NOT
    // LOCALIZABLE
    // -
    // cursor
    // image
    private static Image g_renumCursor = null;
    private static final Point HOTSPOT = new Point(0, 12); // left-button point

    // is the hotspot
    // for renum.gif

    public BERenumberingTool(String text, Image image) {
        super(0, text, image);
        cursor = loadDefaultCursor();
    }

    public Cursor getCursor() {
        return cursor;
    }

    protected Cursor loadDefaultCursor() {
        if (g_renumCursor == null) {
            g_renumCursor = GraphicUtil.loadImage(BEModule.class, CURSOR_FILENAME);
        }

        GraphicUtil.waitForImage(g_renumCursor);
        Cursor cursor = AwtUtil.createCursor(g_renumCursor, HOTSPOT, "box"); // NOT
        // LOCALIZABLE,
        // not
        // yet
        return cursor;
    }

    public void mousePressed(MouseEvent e) {
        try {
            int x = e.getX();
            int y = e.getY();

            // get graphic component under cursor position, if any
            int layerMask = 1 << Diagram.LAYER_GRAPHIC;
            GraphicComponent gc = model.graphicAt(view, x, y, layerMask, false);

            if (gc != null) {
                // if processes(use cases), actors or store boxes found
                if (gc instanceof BEUseCaseBox) {
                    DbSMSClassifier semObj = (DbSMSClassifier) ((BEUseCaseBox) gc)
                            .getSemanticalObject();
                    doRenumbering(semObj);
                } else if (gc instanceof BEActorBox) {
                    DbSMSClassifier semObj = (DbSMSClassifier) ((BEActorBox) gc)
                            .getSemanticalObject();
                    doRenumbering(semObj);
                } else if (gc instanceof BEStoreBox) {
                    DbSMSClassifier semObj = (DbSMSClassifier) ((BEStoreBox) gc)
                            .getSemanticalObject();
                    doRenumbering(semObj);
                } // end if
            } else { // no process, actor or store box found
                // so look for flows and flow labels
                layerMask = (1 << Diagram.LAYER_LINE_LABEL) + (1 << Diagram.LAYER_LINE);
                gc = model.graphicAt(view, x, y, layerMask, false);

                if (gc != null) {
                    if (gc instanceof BEFlowLabel) {
                        BEFlowLabel flowLabel = (BEFlowLabel) gc;
                        DbBEFlowGo flowGo = (DbBEFlowGo) flowLabel.getGraphicalObject();
                        String flowIdPattern = flowIdPattern(flowGo);
                        DbObject semObj = flowLabel.getSemanticalObject();
                        doRenumbering(semObj, flowIdPattern);
                    } else if (gc instanceof BEFlow) {
                        BEFlow flow = (BEFlow) gc;
                        DbBEFlowGo flowGo = (DbBEFlowGo) flow.getGraphicalObject();
                        String flowIdPrefrix = flowIdPattern(flowGo);
                        DbObject semObj = flow.getSemanticalObject();
                        doRenumbering(semObj, flowIdPrefrix);
                    }
                }
            } // end if
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        } // end try

        view.toolCompleted();
    }

    private String flowIdPattern(DbBEFlowGo flowGo) throws DbException {
        flowGo.getDb().beginReadTrans();
        DbBEDiagram diagram = (DbBEDiagram) flowGo.getComposite();
        DbBENotation notation = diagram.findNotation();
        String flowIdPattern = notation.getFlowIdPrefix();
        flowGo.getDb().commitTrans();
        return flowIdPattern;
    }

    private void doRenumbering(DbObject semObj) throws DbException {
        doRenumbering(semObj, null);
    }

    private void doRenumbering(DbObject semObj, String flowIdPattern) throws DbException {
        semObj.getDb().beginWriteTrans(RENUMBERING);

        // compute new identifier
        int id = computeIdentifier();

        // find out if identifier already exists
        DbObject object = findObjectByIdentifier(semObj, id, flowIdPattern);
        if (object != null) {
            // swap identifiers
            int oldId = getIdentifier(semObj, flowIdPattern);
            setIdentifier(object, oldId, flowIdPattern);
        }

        setIdentifier(semObj, id, flowIdPattern);

        // refresh diagram
        Object focusObject = ApplicationContext.getFocusManager().getFocusObject();
        if (focusObject instanceof ApplicationDiagram) {
            ApplicationDiagram diag = (ApplicationDiagram) focusObject;
            diag.getDiagramInternalFrame().refresh();
        } // end if

        semObj.getDb().commitTrans();
    } // end doRenumbering

    private int computeIdentifier() {
        Renumbering renumbering = Renumbering.getSingleInstance();

        // get the counter's current value
        int id = renumbering.getCounter().intValue();
        if (id < 0) { // limit reached, re-init to 0
            id = 0;
        }

        // increment counter for the next call
        int inc = renumbering.getIncrement().intValue();
        renumbering.setCounter(new Integer(id + inc));

        return id;
    } // end computeIdentifier()

    // find out if an object (process, actor or store) has already the same
    // identifier
    // in the same IdSpace. If it does, returns it; elsewhere returns null.
    private DbObject findObjectByIdentifier(DbObject semObj, int id, String flowIdPattern)
            throws DbException {
        DbObject object = null;

        // Different kinds of object have different kinds of ID space
        if (semObj instanceof DbBEUseCase) {
            DbBEUseCase usecase = (DbBEUseCase) semObj;
            object = findUsecaseInComposite(usecase, id);
        } else if (semObj instanceof DbBEActor) {
            DbBEActor actor = (DbBEActor) semObj;
            object = findActorInModel(actor, id);
        } else if (semObj instanceof DbBEStore) {
            DbBEStore store = (DbBEStore) semObj;
            object = findStoreInModel(store, id);
        } else if (semObj instanceof DbBEFlow) {
            DbBEFlow flow = (DbBEFlow) semObj;
            object = findFlowInComposite(flow, id, flowIdPattern);
        } // end if

        return object;
    } // end findObjectByIdentifier

    private DbBEActor findActorInModel(DbBEActor actor, int id) throws DbException {
        DbBEActor anotherActor = null;

        DbBEModel model = (DbBEModel) actor.getCompositeOfType(DbBEModel.metaClass);
        DbRelationN relationN = model.getComponents();
        DbEnumeration dbEnum = relationN.elements(DbBEActor.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEActor currActor = (DbBEActor) dbEnum.nextElement();
            Integer i = currActor.getIdentifier();
            int currId = (i == null) ? 0 : i.intValue();
            if (currId == id) {
                anotherActor = currActor; // found it!
                break;
            }
        } // end while
        dbEnum.close();

        return anotherActor;
    } // end findActorInModel

    private DbBEStore findStoreInModel(DbBEStore store, int id) throws DbException {
        DbBEStore anotherStore = null;

        DbBEModel model = (DbBEModel) store.getCompositeOfType(DbBEModel.metaClass);
        DbRelationN relationN = model.getComponents();
        DbEnumeration dbEnum = relationN.elements(DbBEStore.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEStore currStore = (DbBEStore) dbEnum.nextElement();
            Integer i = currStore.getIdentifier();
            int currId = (i == null) ? 0 : i.intValue();
            if (currId == id) {
                anotherStore = currStore; // found it!
                break;
            }
        } // end while
        dbEnum.close();

        return anotherStore;
    } // end findStoreInModel

    private DbBEFlow findFlowInComposite(DbBEFlow flow, int id, String flowIdPattern)
            throws DbException {
        DbBEFlow anotherFlow = null;

        DbBEUseCase usecase = (DbBEUseCase) flow.getCompositeOfType(DbBEUseCase.metaClass);
        DbRelationN relationN = usecase.getComponents();
        DbEnumeration dbEnum = relationN.elements(DbBEFlow.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEFlow currFlow = (DbBEFlow) dbEnum.nextElement();
            String strId = currFlow.getIdentifier();
            int currId = getIntValue(strId, flowIdPattern);
            if (currId == id) {
                anotherFlow = currFlow; // found it!
                break;
            }
        } // end while
        dbEnum.close();

        return anotherFlow;
    } // end findFlowInComposite

    private DbBEUseCase findUsecaseInComposite(DbBEUseCase usecase, int id) throws DbException {
        DbBEUseCase anotherUsecase = null;

        // the composite of a usecase can be another usecase or a BEModel
        DbObject composite = usecase.getComposite();

        DbRelationN relationN = composite.getComponents();
        DbEnumeration dbEnum = relationN.elements(DbBEUseCase.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEUseCase currUsecase = (DbBEUseCase) dbEnum.nextElement();
            Integer i = currUsecase.getNumericIdentifier();
            int currId = (i == null) ? 0 : i.intValue();
            if (currId == id) {
                anotherUsecase = currUsecase; // found it!
                break;
            }
        } // end while
        dbEnum.close();

        return anotherUsecase;
    } // end findUsecaseInComposite

    // remove F- before..
    private int getIntValue(String id, String flowIdPattern) {
        // if there is a prefix
        if (flowIdPattern != null) {
            int idx = flowIdPattern.lastIndexOf("{0}"); // NOT LOCALIZABLE,
            // pattern
            id = id.substring(idx);

            /*
             * int prefixLen = flowIdPrefix.length(); //if id at least as long as prefix if
             * (id.length() >= prefixLen) { //if id has flowIdPrefix as prefix if (id.substring(0,
             * prefixLen).equals(flowIdPrefix)) { id = id.substring(prefixLen); } //end if } //end
             * if
             */
        } // end if

        int intValue;
        try {
            intValue = Integer.parseInt(id);
        } catch (NumberFormatException ex) {
            intValue = 999;
        }
        return intValue;
    }

    // add F- before
    private String getStrValue(int identifier, String flowIdPattern) {
        String s = "";
        if (flowIdPattern != null) {
            s = MessageFormat.format(flowIdPattern, new Object[] { Integer.toString(identifier) });
        } else {
            s = Integer.toString(identifier);
        } // end if

        return s;
    }

    // setIdentifier()
    private void setIdentifier(DbObject semObj, int identifier, String flowIdPrefrix)
            throws DbException {
        if (semObj instanceof DbBEUseCase) {
            DbBEUseCase usecase = (DbBEUseCase) semObj;
            usecase.setNumericIdentifier(new Integer(identifier));
        } else if (semObj instanceof DbBEActor) {
            DbBEActor actor = (DbBEActor) semObj;
            actor.setIdentifier(new Integer(identifier));
        } else if (semObj instanceof DbBEStore) {
            DbBEStore store = (DbBEStore) semObj;
            store.setIdentifier(new Integer(identifier));
        } else if (semObj instanceof DbBEFlow) {
            DbBEFlow flow = (DbBEFlow) semObj;
            String id = getStrValue(identifier, flowIdPrefrix);
            flow.setIdentifier(id);
        } // end if
    } // end setIdentifier()

    // setIdentifier()
    private int getIdentifier(DbObject semObj, String flowIdPrefix) throws DbException {
        int identifier = 0;

        if (semObj instanceof DbBEUseCase) {
            DbBEUseCase usecase = (DbBEUseCase) semObj;
            Integer i = usecase.getNumericIdentifier();
            if (i != null) {
                identifier = i.intValue();
            }
        } else if (semObj instanceof DbBEActor) {
            DbBEActor actor = (DbBEActor) semObj;
            Integer i = actor.getIdentifier();
            if (i != null) {
                identifier = i.intValue();
            }
        } else if (semObj instanceof DbBEStore) {
            DbBEStore store = (DbBEStore) semObj;
            Integer i = store.getIdentifier();
            if (i != null) {
                identifier = i.intValue();
            }
        } else if (semObj instanceof DbBEFlow) {
            DbBEFlow flow = (DbBEFlow) semObj;
            String id = flow.getIdentifier();
            if (id != null) {
                identifier = getIntValue(id, flowIdPrefix);
            }
        } // end if

        return identifier;
    } // end setIdentifier()

} // end BERenumberingTool()
