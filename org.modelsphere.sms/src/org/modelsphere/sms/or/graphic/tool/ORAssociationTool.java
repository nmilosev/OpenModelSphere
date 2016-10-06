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

package org.modelsphere.sms.or.graphic.tool;

import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Locale;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.ZoneBox;
import org.modelsphere.jack.graphic.tool.LineTool;
import org.modelsphere.jack.graphic.zone.CellID;
import org.modelsphere.jack.graphic.zone.Zone;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORAssociationGo;
import org.modelsphere.sms.or.db.DbORChoiceOrSpecialization;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTableGo;
import org.modelsphere.sms.or.db.srtypes.ORChoiceSpecializationCategory;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.or.graphic.ORChoiceSpecialization;
import org.modelsphere.sms.or.graphic.ORTableBox;
import org.modelsphere.sms.or.graphic.ORView;
import org.modelsphere.sms.or.international.LocaleMgr;

public class ORAssociationTool extends LineTool {
    public static final String kAssociationCreationTool = LocaleMgr.screen
            .getString("AssociationCreationTool");
    public static final String kRelationshipCreationTool = LocaleMgr.screen
            .getString("RelationshipCreationTool");

    private static int segment = 0;

    TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    public static final Image kImageAssociationRightAngleCreationTool = GraphicUtil.loadImage(
            LocaleMgr.class, "resources/associationrightangle.gif"); // NOT
    // LOCALIZABLE
    // -
    // tool
    // image
    public static final Image kImageAssociationCreationTool = GraphicUtil.loadImage(
            LocaleMgr.class, "resources/association.gif"); // NOT

    // LOCALIZABLE
    // -
    // tool
    // image
    // public static final Image kImageAssociationCreationTool =
    // org.modelsphere.sms.or.international.LocaleMgr.screen.getImage("AssociationCreationTool");

    public ORAssociationTool(String text, String[] tooltips, Image image, Image[] secondaryImages) {
        super(0, text, tooltips, image, secondaryImages, 0);
        setVisible(ScreenPerspective.isFullVersion()); 
    }

    public ORAssociationTool() {
        super(0, null, null, null, null, 0);
        setVisible(ScreenPerspective.isFullVersion()); 
    }

    @Override
    protected final boolean isSourceAcceptable(GraphicNode source) {
        if (source instanceof ORView) {
            try {
                DbObject dbo = ((ORView) source).getSemanticalObject();
                dbo.getDb().beginReadTrans();
                if (terminologyUtil.getModelLogicalMode(dbo.getComposite()) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                    dbo.getDb().commitTrans();
                    return false;
                }
                dbo.getDb().commitTrans();
            } catch (DbException dbe) {
                return false;
            }
        }

        boolean acceptable = false;

        if (source instanceof ORTableBox) {
            acceptable = true;
        } else if (source instanceof ORChoiceSpecialization) {
            acceptable = true;
        } //end if

        return acceptable;
    }

    @Override
    protected final boolean isDestAcceptable(GraphicNode source, GraphicNode dest) {
        if (dest instanceof ORView) {
            try {
                DbObject dbo = ((ORView) dest).getSemanticalObject();
                dbo.getDb().beginReadTrans();
                if (terminologyUtil.getModelLogicalMode(dbo.getComposite()) == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                    dbo.getDb().commitTrans();
                    return false;
                }
                dbo.getDb().commitTrans();
            } catch (DbException dbe) {
                return false;
            }
        }

        boolean acceptable = false;

        if (source instanceof ORTableBox) {
            acceptable = true;
        } else if (source instanceof ORChoiceSpecialization) {
            acceptable = true;
        } //end if

        return acceptable;
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

    @Override
    protected final void createLine(GraphicNode source, GraphicNode dest, Polygon originPolygon) {
        try {
            // start transaction
            DbSMSDiagram diagGO = (DbSMSDiagram) ((ApplicationDiagram) model).getDiagramGO();
            String txName = LocaleMgr.action.getString("AssociationCreation");
            diagGO.getDb().beginTrans(Db.WRITE_TRANS, txName);

            // association between tables, or between table and choice/spec?
            if ((source instanceof ORTableBox) && (dest instanceof ORTableBox)) {
                ORTableBox sourceTable = (ORTableBox) source;
                ORTableBox destTable = (ORTableBox) dest;
                createTableAssociation((DbORDiagram) diagGO, sourceTable, destTable, originPolygon);
            } else if ((source instanceof ORChoiceSpecialization)
                    ^ (dest instanceof ORChoiceSpecialization)) {
                ORChoiceSpecialization choiceSpec = null;
                ORTableBox tableBox = null;

                if (source instanceof ORChoiceSpecialization) {
                    choiceSpec = (ORChoiceSpecialization) source;
                } else if (source instanceof ORTableBox) {
                    tableBox = (ORTableBox) source;
                }

                if (dest instanceof ORChoiceSpecialization) {
                    choiceSpec = (ORChoiceSpecialization) dest;
                } else if (dest instanceof ORTableBox) {
                    tableBox = (ORTableBox) dest;
                }

                //create link from table to choiceSpec
                boolean childIsSource = source.equals(tableBox); 
                createChildAssociation((DbORDiagram) diagGO, choiceSpec, tableBox, childIsSource);
            } else {
                //refresh diagram
                Object focusObject = ApplicationContext.getFocusManager().getFocusObject();
                if (focusObject instanceof ApplicationDiagram) {
                    ApplicationDiagram diag = (ApplicationDiagram) focusObject;
                    diag.getDiagramInternalFrame().refresh();
                } // end if
            } //end if

            // commit transaction
            diagGO.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                    ApplicationContext.getDefaultMainFrame(), ex);
        }
    } // end createLine()

    private void createChildAssociation(DbORDiagram diagGO, ORChoiceSpecialization choice,
            ORTableBox childTableBox, boolean childIsSource) throws DbException {

        //get semantic and graphical objects
        DbORTableGo choiceGo = (DbORTableGo) choice.getGraphicalObject();
        DbORTableGo childTableGo = (DbORTableGo) childTableBox.getGraphicalObject();
        DbORChoiceOrSpecialization choiceClassifier = (DbORChoiceOrSpecialization) choiceGo
                .getClassifier();
        DbORTable childClassifier = (DbORTable) childTableGo.getClassifier();
        
        //a choice/spec cannot be linked again to its parent table
        DbORTable parentTable = choiceClassifier.getParentTable();
        if (parentTable.equals(childClassifier)) {
            //refresh diagram
            Object focusObject = ApplicationContext.getFocusManager().getFocusObject();
            if (focusObject instanceof ApplicationDiagram) {
                ApplicationDiagram diag = (ApplicationDiagram) focusObject;
                diag.getDiagramInternalFrame().refresh();
            } // end if
        } else {
            ORChoiceSpecializationCategory category = choiceClassifier.getCategory(); 
            int value = category.getValue();
            boolean isSpec = value == ORChoiceSpecializationCategory.SPECIALIZATION;
            SMSMultiplicity childMult, choiceMult; 
            
            if (isSpec) {
                choiceMult = SMSMultiplicity.getInstance(SMSMultiplicity.OPTIONAL);
                childMult = SMSMultiplicity.getInstance(SMSMultiplicity.EXACTLY_ONE);
            } else { //if choice
                int choiceSide = (childIsSource) ? SMSMultiplicity.EXACTLY_ONE : SMSMultiplicity.MANY;
                int childSide  = (childIsSource) ? SMSMultiplicity.MANY : SMSMultiplicity.EXACTLY_ONE;
                choiceMult = SMSMultiplicity.getInstance(choiceSide);
                childMult = SMSMultiplicity.getInstance(childSide);
            } //end if
                                   
            // String term = terminology.getTerm(DbORAssociation.metaClass);
            
            //create an association between PARENT and child,
            //BUT the association's graphical object is between CHOICE and child
            DbORAssociation assSO = new DbORAssociation(parentTable, choiceMult,
                    childClassifier, childMult);
            DbORAssociationGo assGO = new DbORAssociationGo(diagGO, choiceGo, childTableGo, assSO);
            assSO.setChoiceOrSpecialization(choiceClassifier); 
            
            assGO.setRightAngle(rightAngle ? Boolean.TRUE : Boolean.FALSE);
            DbGraphic.createPolyline(assGO, true);

            initAssociationRoles(assSO, choiceClassifier, isSpec, childIsSource);
        }
    }

	private void initAssociationRoles(DbORAssociation assSO,
            DbORChoiceOrSpecialization choiceClassifier, boolean isSpec, boolean childIsSource) throws DbException {
        DbORAssociationEnd frontEnd = assSO.getFrontEnd(); //front end: the parent table side
        DbORAssociationEnd backEnd = assSO.getBackEnd();   //back end: the child table side
        
        if (isSpec) {
            backEnd.setNavigable(true); 
            frontEnd.setNavigable(false); 
        } else {
            backEnd.setNavigable(! childIsSource); 
            frontEnd.setNavigable(childIsSource); 
        }
        
        //ORChoiceSpecializationCategory catg = choiceClassifier.getCategory();
        String objName = DbORAssociationEnd.metaClass.getGUIName();
        DbORTable parent = choiceClassifier.getParentTable();
        String parentName = parent.getName();
        
        DbORTable childTable = (DbORTable)backEnd.getClassifier();
        String childName = childTable.getName();

        boolean isFrench = (Locale.getDefault().equals(Locale.FRENCH));
        String backName, frontName;

        if (isFrench) {
            //in French: "Role Choix" (adjective after noun)
            backName = objName + " " + parentName;
            frontName = objName + " " + childName;
        } else {
            //in English: "Choice Role" (adjective before noun)
            backName = parentName + " " + objName;
            frontName = childName + " " + objName;
        } //end if

        backEnd.setName(backName);
        frontEnd.setName(frontName);
        
        if (isSpec) {
            addKeyDependency(backEnd);
        }
    }

    private void addKeyDependency(DbORAssociationEnd childEnd) throws DbException {
        DbORTable childTable = (DbORTable)childEnd.getClassifier();
        DbORPrimaryUnique key = findPK(childTable);
        if (key == null) {
            key = (DbORPrimaryUnique) SMSToolkit.getToolkit(
                    SMSToolkit.APPLICATION_DIAGRAM_MASK).createDbObject(
                    DbORPrimaryUnique.metaClass, childTable, null);
                key.setPrimary(Boolean.TRUE);
        }
        
        childEnd.addToDependentConstraints(key);
    }
    
    private DbORPrimaryUnique findPK(DbORTable childTable) throws DbException {
        DbORPrimaryUnique pk = null;
        DbEnumeration enu = childTable.getComponents().elements(DbORPrimaryUnique.metaClass); 
        while (enu.hasMoreElements()) {
            DbORPrimaryUnique puk = (DbORPrimaryUnique)enu.nextElement();
            if (puk.isPrimary()) {
                pk = puk;
                break;
            }
        }
        enu.close(); 
        
        return pk;
   
    }

    private void createTableAssociation(DbORDiagram diagGO, ORTableBox frontTableBox,
            ORTableBox backTableBox, Polygon originPolygon) throws DbException {
        // get front-end and back-end tables
        DbORTableGo frontTableGo = frontTableBox.getTableGO();
        DbORTableGo backTableGo = backTableBox.getTableGO();
        DbORAbsTable frontClassifier = frontTableBox.getTableSO();
        DbORAbsTable backClassifier = backTableBox.getTableSO();

        DbORDataModel dataModel = (DbORDataModel) frontClassifier
                .getCompositeOfType(DbORDataModel.metaClass);
        //TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        //Terminology terminology = terminologyUtil.findModelTerminology(dataModel);
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
            SMSMultiplicity destBackMult = SMSMultiplicity.getInstance(SMSMultiplicity.EXACTLY_ONE);

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
                firstAscGO = new DbORAssociationGo(diagGO, frontTableGo, middleTableGo, firstAscSO);
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
                //DbORAssociationEnd arcEndSO1 = firstAscSO.getArcEnd();
            }
            // create association between 1 entity and 1 relationship
            else if ((isFrontTableRelationship && !isBackTableRelationship)
                    || (!isFrontTableRelationship && isBackTableRelationship)) {
                // String term =
                // terminology.getTerm(DbORAssociation.metaClass);

                if (isFrontTableRelationship) { // Relationship towards
                    // Entity
                    Polygon invertedPolygon = new Polygon();
                    firstAscSO = new DbORAssociation(backClassifier, frontMult, frontClassifier,
                            sourceBackMult);
                    firstAscSO.setIsArc(Boolean.TRUE);
                    firstAscGO = new DbORAssociationGo(diagGO, backTableGo, frontTableGo,
                            firstAscSO);
                    // N.B. the direction is inverse for keep the
                    // relationship always like destination
                    invertedPolygon = this.invertDirection(originPolygon);
                    firstAscGO.setPolyline(invertedPolygon);
                    firstAscGO.setRightAngle(rightAngle ? Boolean.TRUE : Boolean.FALSE);
                    //DbORAssociationEnd arcEndSO1 = firstAscSO.getArcEnd();

                } else { // Entity towards Relationship
                    firstAscSO = new DbORAssociation(frontClassifier, frontMult, backClassifier,
                            sourceBackMult);
                    firstAscSO.setIsArc(Boolean.TRUE);
                    firstAscGO = new DbORAssociationGo(diagGO, frontTableGo, backTableGo,
                            firstAscSO);
                    firstAscGO.setPolyline(originPolygon);
                    firstAscGO.setRightAngle(rightAngle ? Boolean.TRUE : Boolean.FALSE);
                    //DbORAssociationEnd arcEndSO1 = firstAscSO.getArcEnd();
                }
            }
        } else { // if not Entity relationship
            SMSMultiplicity frontMult = SMSMultiplicity.getInstance(SMSMultiplicity.MANY);
            SMSMultiplicity backMult = SMSMultiplicity.getInstance(SMSMultiplicity.EXACTLY_ONE);
            // String term = terminology.getTerm(DbORAssociation.metaClass);
            DbORAssociation assSO = new DbORAssociation(frontClassifier, frontMult, backClassifier,
                    backMult);
            DbORAssociationGo assGO = new DbORAssociationGo(diagGO, frontTableGo, backTableGo,
                    assSO);
            assGO.setPolyline(originPolygon);
            assGO.setRightAngle(rightAngle ? Boolean.TRUE : Boolean.FALSE);
        } // end if

       

        // //
        // enter edition mode on the newly created box

        if (operationalMode == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
            if (rect != null) {
                GraphicComponent gcPressed = model.graphicAt(this.view, rect.x, rect.y, 0xffffffff,
                        false);
                if (gcPressed != null) {
                    if (gcPressed instanceof ZoneBox) {
                        ZoneBox box = (ZoneBox) gcPressed;
                        try {
                            Zone zone = box.getNameZone();
                            CellID cellID = zone.cellAt(0, 0, 0);
                            if (zone != null && cellID != null)
                                ((ApplicationDiagram) model).setEditor(view, zone.getBox(), cellID);
                        } catch (Exception ex) { /* do nothing */
                        }
                    }
                }
            }
        }
    }

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

    private static Polygon computePolygon(Polygon originalPolygon, int xPos, int yPos,
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

    private Polygon invertDirection(Polygon originalPolygon) {
        Polygon invertedPolygon = new Polygon();
        int nbPoints = originalPolygon.npoints;

        for (int i = (nbPoints - 1); i >= 0; i--) {
            invertedPolygon.addPoint(originalPolygon.xpoints[i], originalPolygon.ypoints[i]);
        }
        return invertedPolygon;
    } // end invertDirection()

}
