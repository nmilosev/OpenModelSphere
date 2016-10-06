/*************************************************************************

Copyright (C) 2010 Grandite

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

package org.modelsphere.sms.or.graphic.tool;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.Locale;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.Line;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.or.ORModule;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORAssociationGo;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTableGo;
import org.modelsphere.sms.or.db.srtypes.ORChoiceSpecializationCategory;
import org.modelsphere.sms.or.graphic.ORTableBox;
import org.modelsphere.sms.or.international.LocaleMgr;

public class ORSpecializationTool extends Tool {

    public static final String kSpecializationCreationTool = LocaleMgr.screen
            .getString("SpecializationCreationTool");
    public static final String kChoiceCreationTool = LocaleMgr.screen
            .getString("ChoiceCreationTool");

    private static int segment = 0;
    private int _category;

    protected boolean rightAngle;
    
    private static final String HELP_STEP0 = LocaleMgr.misc.getString("HelpLine0");
    private static final String HELP_STEP1 = LocaleMgr.misc.getString("HelpLine1");
    private Cursor cursor;
    private GraphicNode sourceNode; // may be null
    private Polygon poly = null; // if not null, indicates a line under
    // construction
    private int nbPressed;
    private int xDragged, yDragged;
    private int xFloat, yFloat; // terminal point of the floating segment
    private boolean floatSegDrawn; // indicates that a floating segment (not in

    TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    public static final Image kImageSpecializationCreationTool = 
    	GraphicUtil.loadImage(ORModule.class, "db/resources/dborspecialization.gif");
    public static final Image kImageChoiceCreationTool = GraphicUtil.loadImage(ORModule.class,
            "db/resources/dborchoice.gif");
    
    public ORSpecializationTool(String text, Image image, int category) {
        super(0, text, image);
        setVisible(ScreenPerspective.isFullVersion());
        _category = category;
        cursor = loadDefaultCursor();
    }
    public Cursor getCursor() {
        return cursor;
    }
    
    public final void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            reset();
            view.toolCompleted();
        }
    }
   
    private final void paintFloatSeg(Graphics g) {
        if (!floatSegDrawn)
            return;
        g.setColor(Color.red);
        float zoomFactor = view.getZoomFactor();
        int i = poly.npoints - 1;
        g.drawLine((int) (poly.xpoints[i] * zoomFactor), (int) (poly.ypoints[i] * zoomFactor),
                (int) (xFloat * zoomFactor), (int) (yFloat * zoomFactor));
    }
 
    public final void mousePressed(MouseEvent e) {
        if (poly == null) {
            sourceNode = null;
            Point pt = e.getPoint();

	        GraphicComponent gc = model.graphicAt(view, pt.x, pt.y, 1 << Diagram.LAYER_GRAPHIC,
	                false);
	        if (!(gc instanceof GraphicNode))
	            gc = null;
	        if (isSourceAcceptable((GraphicNode) gc)) {
	            sourceNode = (GraphicNode) gc;
	            if (sourceNode != null)
	                pt = GraphicUtil.rectangleGetCenter(sourceNode.getRectangle());
	            updateHelp();
	
	        } else {
	            view.toolCompleted();
	            return;
	        }

            rightAngle = (auxiliaryIndex == 0);
            poly = new Polygon();
            poly.addPoint(pt.x, pt.y);
            nbPressed = 1;
            floatSegDrawn = false;
            Graphics g = getGraphics();
            if (g != null) {
                paint(g); // show the initial drawing: a small rectangle in the
                // center of the source node
                g.dispose();
            }
            view.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else {
            nbPressed++;
            mouseDragged(e);
        }
        updateHelp();
    }

    public final void mouseDragged(MouseEvent e) {
        Graphics g = getGraphics();
        if (g == null)
            return;
        paintFloatSeg(g); // hide the floating segment
        floatSegDrawn = false;
        Rectangle drawingArea = model.getDrawingArea();
        xDragged = GraphicUtil.confineXToRect(e.getX(), drawingArea);
        yDragged = GraphicUtil.confineYToRect(e.getY(), drawingArea);
        if (!scrollToVisible(xDragged, yDragged)) {
            if (sourceNode == null || poly.npoints > 1
                    || !sourceNode.getRectangle().contains(xDragged, yDragged)) {
                floatSegDrawn = true; // show the floating segment unless it is
                // the first segment and is inside the
                // source node
                xFloat = xDragged;
                yFloat = yDragged;
                if (rightAngle) { // alternate horizontal and vertical segments.
                    int i = poly.npoints - 1;
                    boolean horz = (i == 0 ? Math.abs(xDragged - poly.xpoints[i]) > Math
                            .abs(yDragged - poly.ypoints[i])
                            : poly.xpoints[i] == poly.xpoints[i - 1]);
                    if (horz)
                        yFloat = poly.ypoints[i];
                    else
                        xFloat = poly.xpoints[i];
                }
                paintFloatSeg(g);
            }
        }
        g.dispose();
    }

    public final void mouseReleased(MouseEvent e) {
        // Case 1: cursor inside the source node with an empty polygon, or
        // scrolling mode
        if (!floatSegDrawn) {
            if (poly.npoints == 1 && nbPressed > 1) {
                reset(); // cancel if 2 clicks on the same node without dragging
                view.toolCompleted();
            }
            return;
        }
        // Case 2: cursor on a graphic (may be the source node, but in this
        // case,
        // we have at least one point outside the node).
        GraphicComponent gc = model.graphicAt(view, xDragged, yDragged,
                1 << Diagram.LAYER_GRAPHIC, false);
        if (gc instanceof GraphicNode) {
            Polygon newPoly = poly; // keep a reference, reset() sets poly =
            // null
            reset();
            view.toolCompleted();
            if (!isDestAcceptable(sourceNode, (GraphicNode) gc))
                return;
            Point pt = GraphicUtil.rectangleGetCenter(gc.getRectangle());
            if (rightAngle) {
                int i = newPoly.npoints - 1;
                if (sourceNode == gc) { // if recursive connector, user must
                    // draw at least 2 segments to allow
                    // us to close the figure
                    if (i < 2)
                        return;
                    if (i == 2) {
                        if (newPoly.xpoints[i] == newPoly.xpoints[i - 1])
                            newPoly.addPoint(pt.x, newPoly.ypoints[i]);
                        else
                            newPoly.addPoint(newPoly.xpoints[i], pt.y);
                        i++;
                    }
                }
                if (i == 0) { // add a vertex, a right angle connector has
                    // at least 2 segments.
                    newPoly.addPoint(newPoly.xpoints[i], pt.y);
                } else {
                    if (newPoly.xpoints[i] == newPoly.xpoints[i - 1])
                        newPoly.ypoints[i] = pt.y;
                    else
                        newPoly.xpoints[i] = pt.x;
                }
            }
            newPoly.addPoint(pt.x, pt.y);
            createLine(sourceNode, (GraphicNode) gc, newPoly);
            return;
        }

        // Case 3: cursor not on a graphic: add a point to the polygon.
        int delta = view.getHandleSize();
        int i = poly.npoints - 1;
        boolean newPoint = (Math.abs(xFloat - poly.xpoints[i]) > delta || Math.abs(yFloat
                - poly.ypoints[i]) > delta);
        if (!newPoint) { // If double-click on the same point, close the line.
            Polygon newPoly = poly; // keep a reference, reset() sets poly =
            // null
            reset();
            view.toolCompleted();
            if (newPoly.npoints > 1) {
               if (isDestAcceptable(sourceNode, null))
                   createLine(sourceNode, null, newPoly);
            }
            return;
        }
        Graphics g = getGraphics();
        if (g != null) {
            paint(g); // hide the previous drawing
            floatSegDrawn = false;
            poly.addPoint(xFloat, yFloat);
            paint(g); // show the new polygon
            g.dispose();
        }
    }
    
    public final void paint(Graphics g) {
        if (poly == null)
            return;
        Line.drawSelectedLine(g, view, poly, true);
        paintFloatSeg(g);
    }

    protected Cursor loadDefaultCursor() {
        Image image = this.getImage();
        Cursor cursor = AwtUtil.createCursor(image, new Point(8, 8),
                "box", false); // NOT LOCALIZABLE, not yet
        return cursor;
    }

    protected final boolean isSourceAcceptable(GraphicNode source) {
        boolean acceptable = (source == null);
        return acceptable;
    }

    protected final boolean isDestAcceptable(GraphicNode source, GraphicNode dest) {
        boolean acceptable = false;

        if (dest instanceof ORTableBox) {
            ORTableBox box = (ORTableBox) dest;

            if (box.getTableSO() instanceof DbORTable) {
                acceptable = true;
            } //end if 
        } //end if 

        return acceptable;
    }

    protected final void createLine(GraphicNode source, GraphicNode dest, Polygon originPolygon) {
        ApplicationDiagram diag = (ApplicationDiagram) model;
        DbORDiagram diagGO = (DbORDiagram) diag.getDiagramGO();
        String txName = getText();

        try {
            diagGO.getDb().beginTrans(Db.WRITE_TRANS, txName);

            //get parent table
            DbORTableGo tableGo = ((ORTableBox) dest).getTableGO();

            //create choice or specialization
            DbORTableGo choiceGo = createChoiceOrSpecialization(diagGO, tableGo, originPolygon);

            //create link to parent
            createParentAssociation(diagGO, choiceGo, tableGo, originPolygon);

            diagGO.getDb().commitTrans();
        } catch (Exception ex) {
            DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
            ExceptionHandler.processUncatchedException(mf, ex);
        } //end try
    } //end createLine()

    //
    // private methods
    // 

    private DbORTableGo createChoiceOrSpecialization(DbORDiagram diagGO, DbORTableGo tableGo,
            Polygon originPolygon)
            throws DbException {
        DbORChoiceOrSpecialization choiceSO = createChoiceSpec(diagGO);
        DbORTableGo choiceGO = new DbORTableGo(diagGO, choiceSO);
        
        //compute position
        Rectangle tableRect = tableGo.getRectangle();
        int x = tableRect.x + (tableRect.width / 2);
        int y = originPolygon.ypoints[0];

        Rectangle rect = new Rectangle(x- 20, y - 20, 40, 40); 
        choiceGO.setRectangle(rect);
        
        //set parent table
        DbORTable parentTable = (DbORTable) tableGo.getClassifier();
        choiceSO.setParentTable(parentTable);
        
        return choiceGO;
	} //end createSpecialization()
	
    private DbORChoiceOrSpecialization createChoiceSpec(DbORDiagram diagGO) throws DbException {
        SMSToolkit tk = SMSToolkit.getToolkit(diagGO);
        DbORChoiceOrSpecialization choiceSO = (DbORChoiceOrSpecialization) tk.createDbObject(
                DbORChoiceOrSpecialization.metaClass, diagGO.getComposite(), null);
        ORChoiceSpecializationCategory categ = ORChoiceSpecializationCategory
                .getInstance(_category);
        choiceSO.setCategory(categ);

        if (_category == ORChoiceSpecializationCategory.CHOICE) {
            choiceSO.setName(LocaleMgr.misc.getString("Choice"));
        } else if (_category == ORChoiceSpecializationCategory.SPECIALIZATION) {
            choiceSO.setName(LocaleMgr.misc.getString("Specialization"));
        }

        return choiceSO;
    }

    private void createParentAssociation(DbORDiagram diagGO, DbORTableGo choiceGo,
            DbORTableGo parentTableGo, Polygon originPolygon) throws DbException {
    	SMSMultiplicity choiceMult = SMSMultiplicity.getInstance(SMSMultiplicity.EXACTLY_ONE);
        SMSMultiplicity parentMult = SMSMultiplicity.getInstance(SMSMultiplicity.UNDEFINED);
        
        DbORChoiceOrSpecialization choiceSpec = (DbORChoiceOrSpecialization)choiceGo.getClassifier();
        DbORAbsTable backClassifier = (DbORAbsTable)parentTableGo.getClassifier();
        
        DbORAssociation assocSO = new DbORAssociation(choiceSpec, choiceMult,
                backClassifier, parentMult);
        DbORAssociationGo assocGO = new DbORAssociationGo(diagGO, choiceGo, parentTableGo,
                assocSO);
        assocSO.setChoiceOrSpecialization(choiceSpec);
        choiceSpec.setMultiplicity(choiceMult);
        
        assocGO.setPolyline(originPolygon);
        assocGO.setRightAngle(true);

        //do not show parent association in explorer and db list
        assocSO.setHideFlags(DbObject.HIDE_IN_EXPLORER);
        assocSO.getFrontEnd().setHideFlags(DbObject.HIDE_IN_DB_LIST);

        setAssociationRoleNames(assocSO);
    }

    private void setAssociationRoleNames(DbORAssociation assSO) throws DbException {
        DbORAssociationEnd backEnd = assSO.getBackEnd();
        DbORAssociationEnd frontEnd = assSO.getFrontEnd();

        String objName = DbORAssociationEnd.metaClass.getGUIName();
        String choiceSpecKind = (_category == ORChoiceSpecializationCategory.CHOICE) ? LocaleMgr.misc
                .getString("Choice") : LocaleMgr.misc.getString("Specialization");

        boolean isFrench = (Locale.getDefault().equals(Locale.FRENCH));
        String parent = LocaleMgr.misc.getString("Parent");
        String backName, frontName;

        if (isFrench) {
            //in French: "Role Choix" (adjective after noun)
            backName = objName + " " + choiceSpecKind;
            frontName = objName + " " + parent;
        } else {
            //in English: "Choice Role" (adjective before noun)
            backName = choiceSpecKind + " " + objName;
            frontName = parent + " " + objName;
        } //end if

        backEnd.setName(backName);
        frontEnd.setName(frontName);
    }

    public void convertAssociationGOtoRelationshipGO(DbSMSDiagram diagram, Polygon originPolygon,
            DbSMSClassifierGo srcClassifierGO, DbSMSClassifierGo assocClassifierGO,
            DbSMSClassifierGo destClassifierGO, DbORAssociation srcArc, DbORAssociation destArc,
            DbORAssociationGo originAssocGo) {
        try {
            diagram.getDb().beginTrans(Db.WRITE_TRANS,
                    LocaleMgr.action.getString("convertToConceptualDots"));
            Rectangle assocTableRect = assocClassifierGO.getRectangle();
            int nbPoints = originPolygon.npoints;
            Point point = computeRelationshipMetrics(originPolygon.xpoints, originPolygon.ypoints,
                    nbPoints, assocTableRect);
            int xPos = point.x;
            int yPos = point.y;
            Rectangle rect = new Rectangle(xPos, yPos, assocTableRect.width, assocTableRect.height);
            assocClassifierGO.setRectangle(rect);
            DbORAssociationGo srcArcGO = new DbORAssociationGo(diagram, srcClassifierGO,
                    assocClassifierGO, srcArc);
            DbORAssociationGo destArcGO = new DbORAssociationGo(diagram, destClassifierGO,
                    assocClassifierGO, destArc);
            // transfert des caractéristiques de l'association originale vers
            // les arcs
            boolean isRightAngle = originAssocGo.isRightAngle();
            srcArcGO.setDashStyle(originAssocGo.getDashStyle());
            destArcGO.setDashStyle(originAssocGo.getDashStyle());
            srcArcGO.setHighlight(originAssocGo.getHighlight());
            destArcGO.setHighlight(originAssocGo.getHighlight());
            srcArcGO.setLineColor(originAssocGo.getLineColor());
            destArcGO.setLineColor(originAssocGo.getLineColor());

            boolean isFirstPortion = true;
            Polygon poly1 = computePolygon(originPolygon, xPos, yPos, isFirstPortion, isRightAngle);
            Polygon poly2 = computePolygon(originPolygon, xPos, yPos, !(isFirstPortion),
                    isRightAngle);

            if (originPolygon.xpoints[0] < originPolygon.xpoints[originPolygon.npoints - 1]) {
                srcArcGO.setPolyline(poly1);
                srcArcGO.setRightAngle(isRightAngle ? Boolean.TRUE : Boolean.FALSE);
                destArcGO.setPolyline(poly2);
                destArcGO.setRightAngle(isRightAngle ? Boolean.TRUE : Boolean.FALSE);
            } else { // must swap polygons in order for the entities to connect
                // properly
                srcArcGO.setPolyline(poly2);
                srcArcGO.setRightAngle(isRightAngle ? Boolean.TRUE : Boolean.FALSE);
                destArcGO.setPolyline(poly1);
                destArcGO.setRightAngle(isRightAngle ? Boolean.TRUE : Boolean.FALSE);
            }
            //DbORAssociationEnd arcEndSO1 = srcArc.getArcEnd();
            diagram.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }// end convertAssociationGOtoRelationshipGO

    /*
    protected final void createLine(GraphicNode source, GraphicNode dest, Polygon originPolygon) {
        try {
            // get front-nd and back-end tables
            DbSMSDiagram diagGO = (DbSMSDiagram) ((ApplicationDiagram) model).getDiagramGO();
            diagGO.getDb().beginTrans(Db.WRITE_TRANS,
                    LocaleMgr.action.getString("AssociationCreation"));
            DbORAbsTable frontClassifier = ((ORTableBox) source).getTableSO();
            DbORAbsTable backClassifier = ((ORTableBox) dest).getTableSO();
            DbORTableGo frontTableGo = ((ORTableBox) source).getTableGO();
            DbORTableGo backTableGo = ((ORTableBox) dest).getTableGO();

            DbORDiagram dgm = (DbORDiagram) diagGO;
            DbORDataModel dataModel = (DbORDataModel) frontClassifier
                    .getCompositeOfType(DbORDataModel.metaClass);
            TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
            Terminology terminology = terminologyUtil.findModelTerminology(dataModel);
            Rectangle rect = null;

            int operationalMode = dataModel.getOperationalMode();
            if (operationalMode == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                DbORAssociation firstAscSO = null;
                DbORAssociation lastAscSO = null;
                DbORAssociationGo firstAscGO = null;
                DbORAssociationGo lastAscGO = null;
                boolean isFrontTableRelationship = ((DbORTable) frontClassifier).isIsAssociation();
                boolean isBackTableRelationship = ((DbORTable) backClassifier).isIsAssociation();

                // In ER mode, frontEnd will be always UNDEFINED and must be
                // hidden
                SMSMultiplicity frontMult = SMSMultiplicity.getInstance(SMSMultiplicity.UNDEFINED);
                SMSMultiplicity sourceBackMult = SMSMultiplicity.getInstance(SMSMultiplicity.MANY);
                SMSMultiplicity destBackMult = SMSMultiplicity
                        .getInstance(SMSMultiplicity.EXACTLY_ONE);

                // create association between 2 entity
                if (!isFrontTableRelationship && !isBackTableRelationship) {
                    // create the association Table (Relationship)
                    DbORTable middleClassifier = AnyORObject.createERRelationship(dataModel);

                    // place middleTableGo between the front-end and
                    // back-end-table
                    DbORTableGo middleTableGo = new DbORTableGo(diagGO, middleClassifier);
                    Rectangle middleTableRect = middleTableGo.getRectangle();

                    int nbPoints = originPolygon.npoints;
                    Point point = computeRelationshipMetrics(originPolygon.xpoints,
                            originPolygon.ypoints, nbPoints, middleTableRect);
                    int xPos = point.x;
                    int yPos = point.y;
                    rect = new Rectangle(xPos, yPos, middleTableRect.width, middleTableRect.height);
                    middleTableGo.setRectangle(rect);

                    firstAscSO = new DbORAssociation(frontClassifier, frontMult, middleClassifier,
                            sourceBackMult);
                    firstAscSO.setIsArc(Boolean.TRUE);
                    firstAscGO = new DbORAssociationGo(diagGO, frontTableGo, middleTableGo,
                            firstAscSO);
                    lastAscSO = new DbORAssociation(backClassifier, frontMult, middleClassifier,
                            destBackMult);
                    lastAscSO.setIsArc(Boolean.TRUE);
                    lastAscGO = new DbORAssociationGo(diagGO, backTableGo, middleTableGo, lastAscSO);

                    boolean isFirstPortion = true;
                    Polygon poly1 = computePolygon(originPolygon, xPos, yPos, isFirstPortion,
                            rightAngle);
                    Polygon poly2 = computePolygon(originPolygon, xPos, yPos, !(isFirstPortion),
                            rightAngle);

                    if (originPolygon.xpoints[0] < originPolygon.xpoints[originPolygon.npoints - 1]) {
                        firstAscGO.setPolyline(poly1);
                        firstAscGO.setRightAngle(rightAngle ? Boolean.TRUE : Boolean.FALSE);
                        lastAscGO.setPolyline(poly2);
                        lastAscGO.setRightAngle(rightAngle ? Boolean.TRUE : Boolean.FALSE);
                    } else { // must swap polygons in order for the entities to
                        // connect properly
                        firstAscGO.setPolyline(poly2);
                        firstAscGO.setRightAngle(rightAngle ? Boolean.TRUE : Boolean.FALSE);
                        lastAscGO.setPolyline(poly1);
                        lastAscGO.setRightAngle(rightAngle ? Boolean.TRUE : Boolean.FALSE);
                    }
                    DbORAssociationEnd arcEndSO1 = firstAscSO.getArcEnd();
                }
                // create association between 1 entity and 1 relationship
                else if ((isFrontTableRelationship && !isBackTableRelationship)
                        || (!isFrontTableRelationship && isBackTableRelationship)) {
                    // String term =
                    // terminology.getTerm(DbORAssociation.metaClass);

                    if (isFrontTableRelationship) { // Relationship towards
                        // Entity
                        Polygon invertedPolygon = new Polygon();
                        firstAscSO = new DbORAssociation(backClassifier, frontMult,
                                frontClassifier, sourceBackMult);
                        firstAscSO.setIsArc(Boolean.TRUE);
                        firstAscGO = new DbORAssociationGo(diagGO, backTableGo, frontTableGo,
                                firstAscSO);
                        // N.B. the direction is inverse for keep the
                        // relationship always like destination
                        invertedPolygon = this.invertDirection(originPolygon);
                        firstAscGO.setPolyline(invertedPolygon);
                        firstAscGO.setRightAngle(rightAngle ? Boolean.TRUE : Boolean.FALSE);
                        DbORAssociationEnd arcEndSO1 = firstAscSO.getArcEnd();

                    } else { // Entity towards Relationship
                        firstAscSO = new DbORAssociation(frontClassifier, frontMult,
                                backClassifier, sourceBackMult);
                        firstAscSO.setIsArc(Boolean.TRUE);
                        firstAscGO = new DbORAssociationGo(diagGO, frontTableGo, backTableGo,
                                firstAscSO);
                        firstAscGO.setPolyline(originPolygon);
                        firstAscGO.setRightAngle(rightAngle ? Boolean.TRUE : Boolean.FALSE);
                        DbORAssociationEnd arcEndSO1 = firstAscSO.getArcEnd();
                    }
                }
            } else { // if not Entity relationship
                SMSMultiplicity frontMult = SMSMultiplicity.getInstance(SMSMultiplicity.MANY);
                SMSMultiplicity backMult = SMSMultiplicity.getInstance(SMSMultiplicity.EXACTLY_ONE);
                // String term = terminology.getTerm(DbORAssociation.metaClass);
                DbORAssociation assSO = new DbORAssociation(frontClassifier, frontMult,
                        backClassifier, backMult);
                DbORAssociationGo assGO = new DbORAssociationGo(diagGO, frontTableGo, backTableGo,
                        assSO);
                assGO.setPolyline(originPolygon);
                assGO.setRightAngle(rightAngle ? Boolean.TRUE : Boolean.FALSE);
            } // end if

            diagGO.getDb().commitTrans();

            // //
            // enter edition mode on the newly created box

            if (operationalMode == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                if (rect != null) {
                    GraphicComponent gcPressed = model.graphicAt(this.view, rect.x, rect.y,
                            0xffffffff, false);
                    if (gcPressed != null) {
                        if (gcPressed instanceof ZoneBox) {
                            ZoneBox box = (ZoneBox) gcPressed;
                            try {
                                Zone zone = box.getNameZone();
                                CellID cellID = zone.cellAt(0, 0, 0);
                                if (zone != null && cellID != null)
                                    ((ApplicationDiagram) model).setEditor(view, zone.getBox(),
                                            cellID);
                            } catch (Exception ex) { // do nothing 
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    } // end createLine()
    */

    public static Point computeRelationshipMetrics(int[] xpoints, int[] ypoints, int nbPoints,
            Rectangle association) {

        if (nbPoints == 1)
            return new Point(xpoints[0], ypoints[0]);

        if (xpoints[nbPoints - 1] <= xpoints[0]) { // reverse arrays

            int[] xtemp = new int[nbPoints];
            int[] ytemp = new int[nbPoints];

            for (int i = 0, j = nbPoints - 1; i < nbPoints; i++, j--) {
                xtemp[i] = xpoints[j];
                ytemp[i] = ypoints[j];
            }
            xpoints = xtemp;
            ypoints = ytemp;
        }

        int x1 = xpoints[0];
        int x2 = xpoints[nbPoints - 1];
        int deltaX = 0;
        deltaX = x2 - x1;
        double middle = 0;

        middle = x1 + (deltaX / 2) + association.width / 2;
        Point presult = getPoint(nbPoints, xpoints, ypoints, middle);
        presult.y -= association.height;

        return presult;
    }

    private static Point getPoint(int nbPoints, int[] xpoints, int[] ypoints, double middle) {

        double image = 0;
        double x1 = 0, y1 = 0;
        double x2 = 0, y2 = 0;

        if (nbPoints < 2) {
            x1 = xpoints[0];
            y1 = ypoints[0];
            x2 = xpoints[0];
            y2 = ypoints[0];
            segment = 1;
        } else {
            x1 = xpoints[0];
            y1 = ypoints[0];
            x2 = xpoints[1];
            y2 = ypoints[1];

            int nbSegments = nbPoints - 1;
            int nTotalLength = 0;
            double[] aLengths = new double[nbSegments]; // compute total length
            for (int i = 0; i < nbPoints - 1; i++) {
                aLengths[i] = java.lang.Math.sqrt(java.lang.Math
                        .pow(xpoints[i + 1] - xpoints[i], 2)
                        + java.lang.Math.pow(ypoints[i + 1] - ypoints[i], 2));
                nTotalLength += aLengths[i];
            }
            int submiddle = nTotalLength / 2; // locate which segment middle
            // belongs to
            int dynaLength = 0, i = 0;
            for (i = 0; i < nbSegments; i++) {
                dynaLength += aLengths[i];
                if (submiddle <= dynaLength)
                    break;
            }
            int segmentIndex = i;
            segment = segmentIndex + 1; // set the member data for class scope
            // computations
            double hyp = aLengths[segmentIndex];
            double hypPrime = aLengths[segmentIndex] - (dynaLength - submiddle);
            double ratio = hypPrime / hyp;
            if (xpoints[segmentIndex + 1] >= xpoints[segmentIndex])
                middle = xpoints[segmentIndex] + ratio
                        * (xpoints[segmentIndex + 1] - xpoints[segmentIndex]);
            else
                middle = xpoints[segmentIndex] - ratio
                        * (xpoints[segmentIndex] - xpoints[segmentIndex + 1]);

            x1 = xpoints[segmentIndex];
            y1 = ypoints[segmentIndex];
            x2 = xpoints[segmentIndex + 1];
            y2 = ypoints[segmentIndex + 1];
        }

        y1 *= (-1);
        y2 *= (-1);

        double deltaX = x2 - x1; // simple computation of the image of middle
        if (deltaX != 0) { // in the function for segment (straight line in a
            // plane)
            double slope = (y2 - y1) / (x2 - x1);
            double offset = y1 - (slope * x1);
            image = slope * middle + offset;
        } else {
            y1 *= (-1);
            y2 *= (-1);
            if (y2 >= y1)
                image = y1 + ((y2 - y1) / 2);
            else if (y2 < y1)
                image = y2 + ((y1 - y2) / 2);
        }
        if (image < 0)
            image *= -1;

        return new Point((int) middle, (int) image);
    }

    // if firstPortion is true
    // compute the polyline between the source table and the middle table, and
    // return it
    // else (if second portion)
    // compute the polyline between the destination table and the middle table,
    // and return it
    //
    // TODO : compute right-angle polylines if variable rightAngle is true

    private Polygon computePolygon(Polygon originalPolygon, int xPos, int yPos,
            boolean firstPortion, boolean isRightAngle) {

        int resultNPoints = 0;
        int nbPoints = originalPolygon.npoints;

        int[] xpoints = originalPolygon.xpoints;
        int[] ypoints = originalPolygon.ypoints;

        if (xpoints[nbPoints - 1] <= xpoints[0]) { // reverse arrays

            int[] xtemp = new int[nbPoints];
            int[] ytemp = new int[nbPoints];

            for (int i = 0, j = nbPoints - 1; i < nbPoints; i++, j--) {
                xtemp[i] = xpoints[j];
                ytemp[i] = ypoints[j];
            }
            xpoints = xtemp;
            ypoints = ytemp;
        }

        // assemble the points arrays

        int[] resultXpoints = null;
        int[] resultYpoints = null;
        if (firstPortion) {

            int nbpoints = 0;

            if (nbPoints == 2)
                nbpoints = 2;
            else
                nbpoints = segment + 1;

            resultXpoints = new int[nbpoints];
            resultYpoints = new int[nbpoints];

            for (int j = 0; j < nbpoints - 1; j++) {
                resultXpoints[j] = xpoints[j];
                resultYpoints[j] = ypoints[j];
            }
            resultXpoints[nbpoints - 1] = xPos;
            resultYpoints[nbpoints - 1] = yPos;

            if (isRightAngle && nbpoints == 2) {
                nbpoints++;
                int[] result3Xpoints = new int[nbpoints];
                int[] result3Ypoints = new int[nbpoints];

                result3Xpoints[0] = resultXpoints[0];
                result3Ypoints[0] = resultYpoints[0] + 1;
                for (int i = 1; i < nbpoints; i++) {
                    result3Xpoints[i] = resultXpoints[i - 1];
                    result3Ypoints[i] = resultYpoints[i - 1];
                }
                resultXpoints = result3Xpoints;
                resultYpoints = result3Ypoints;
            }

            resultNPoints = nbpoints;
        } else {
            int nSize = 2;
            if (nbPoints != 2)
                nSize = nbPoints - segment + 1;

            resultXpoints = new int[nSize];
            resultYpoints = new int[nSize];

            for (int j = nbPoints - 1, k = 0; j > nbPoints - nSize; j--, k++) {
                resultXpoints[k] = xpoints[j];
                resultYpoints[k] = ypoints[j];
            }
            resultXpoints[nSize - 1] = xPos;
            resultYpoints[nSize - 1] = yPos;

            if (isRightAngle && nSize == 2) {
                nSize++;
                int[] result3Xpoints = new int[nSize];
                int[] result3Ypoints = new int[nSize];

                result3Xpoints[0] = resultXpoints[0];
                result3Ypoints[0] = resultYpoints[0] + 1;
                for (int i = 1; i < nSize; i++) {
                    result3Xpoints[i] = resultXpoints[i - 1];
                    result3Ypoints[i] = resultYpoints[i - 1];
                }
                resultXpoints = result3Xpoints;
                resultYpoints = result3Ypoints;
            }

            resultNPoints = nSize;
        }

        Polygon poly = new Polygon(resultXpoints, resultYpoints, resultNPoints);
        return poly;

    } // end computePolygon()

    public final void reset() {
        if (poly == null)
            return;
        Graphics g = getGraphics();
        if (g != null) {
            getDiagramView().areaDamaged(poly.getBounds());
            paint(g);
            g.dispose();
        }
        view.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        poly = null;
        updateHelp();
    }
    
    public void updateHelp() {
        String help = null;
        if (poly != null) {
            String escape = KeyEvent.getKeyText(KeyEvent.VK_ESCAPE);
            help = MessageFormat.format(HELP_STEP1, escape);
        }
        if (help == null) {
            help = HELP_STEP0;
        }
        setHelpText(help);
    }
    /*
     * private Polygon invertDirection(Polygon originalPolygon) { Polygon invertedPolygon = new
     * Polygon(); int nbPoints = originalPolygon.npoints;
     * 
     * for (int i = (nbPoints - 1); i >= 0; i--) {
     * invertedPolygon.addPoint(originalPolygon.xpoints[i], originalPolygon.ypoints[i]); } return
     * invertedPolygon; } // end invertDirection()
     */


}
