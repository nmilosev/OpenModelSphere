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
package org.modelsphere.sms.be.db.util;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.modelsphere.jack.awt.SelectDialog;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.graphic.zone.TableCell;
import org.modelsphere.jack.graphic.zone.TableZone;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEActorGo;
import org.modelsphere.sms.be.db.DbBEContextCell;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEStoreGo;
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.graphic.BEContext;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.db.srtypes.SMSHorizontalAlignment;
import org.modelsphere.sms.db.srtypes.SMSVerticalAlignment;
import org.modelsphere.sms.db.util.DbInitialization;
import org.modelsphere.sms.db.util.Extensibility;
import org.modelsphere.sms.preference.DiagramAutomaticCreationOptionGroup;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BEUtility {
    private static final String EXTERNAL_SUFFIX = LocaleMgr.misc.getString("ExternalSuffix");
    private static final String SELECT_NOTATION = LocaleMgr.misc
            .getString("SelectWhichKindOfDiagram");

    public static final int CREATE_FRAME_CALLER = 0;
    public static final int CREATE_ACTOR_CELL_CALLER = 1;
    public static final int INSERT_CELL_CALLER = 2;

    public enum InsertAction {
        NO_ACTION, CREATE_ROLE, CREATE_ACTOR
    };

    private double[] m_rowHeights = null;
    private double[] m_colWidths = null;

    public double[] getRowHeights() {
        return m_rowHeights;
    }

    public double[] getColWidths() {
        return m_colWidths;
    }

    //Singleton Design Pattern 
    private BEUtility() {
    }

    private static BEUtility g_singleInstance = null;

    public static BEUtility getSingleInstance() {
        if (g_singleInstance == null) {
            g_singleInstance = new BEUtility();
        }

        return g_singleInstance;
    } //end getSingleInstance()

    //
    // factory methods
    //
    //called by org.modelsphere.sms.be.graphic.tool.BEExplodeTool.addDiagramElement() & explodeUseCase()
    private static final int MARGIN_GAP = 20;

    public DbBEDiagram createBEDiagram(DbBEUseCase context, DbBENotation notation)
            throws DbException {

        DbBEDiagram beDiagram = new DbBEDiagram(context);
        beDiagram.getDb().beginWriteTrans("");

        beDiagram.setNotation(notation);
        initDiagram(beDiagram, notation);

        //Diagram named according its notation
        String name = "";
        notation.getDb().beginReadTrans();
        int Id = notation.getMasterNotationID().intValue();
        notation.getDb().commitTrans();
        if (Id >= 13 && Id <= 19) {
            name = notation.getName();
            beDiagram.setName(name);
        }
        setDiagramStyle(beDiagram, notation);

        //If it is a sequence diagram, look for each storeGo and move it above 
        //the role line.
        if (Id == DbInitialization.UML_SEQUENCE_DIAGRAM) {
            BEUtility util = BEUtility.getSingleInstance();
            DbRelationN relN = beDiagram.getComponents();
            DbEnumeration dbEnum = relN.elements(DbBEContextGo.metaClass);
            //for each context frame (usually there is just one frame per diagram)
            while (dbEnum.hasMoreElements()) {
                DbBEContextGo contextGo = (DbBEContextGo) dbEnum.nextElement();
                Rectangle frameRect = contextGo.getRectangle();
                int xPos = frameRect.x + (frameRect.width / 2);
                int yPos = frameRect.y;
                int cellCounter = 0;

                DbRelationN relN2 = contextGo.getComponents();
                DbEnumeration enum2 = relN2.elements(DbBEContextCell.metaClass);

                //for each cell of the context frame
                while (enum2.hasMoreElements()) {
                    DbBEContextCell cell = (DbBEContextCell) enum2.nextElement();

                    //for each role (store) attached to the current cell
                    DbRelationN relN3 = cell.getStoreGos();
                    DbEnumeration enum3 = relN3.elements(DbBEStoreGo.metaClass);
                    while (enum3.hasMoreElements()) {
                        DbSMSClassifierGo storeGo = (DbBEStoreGo) enum3.nextElement();
                        moveGO(storeGo, cellCounter, yPos, frameRect);
                    } //end while
                    enum3.close();

                    //for each actor attached to the current cell
                    relN3 = cell.getActorGos();
                    enum3 = relN3.elements(DbBEActorGo.metaClass);
                    while (enum3.hasMoreElements()) {
                        DbBEActorGo actorGo = (DbBEActorGo) enum3.nextElement();
                        moveGO(actorGo, cellCounter, yPos, frameRect);
                    }
                    enum3.close();

                    cellCounter++;
                }
                enum2.close();
            } //end while
            dbEnum.close();
        } //end if

        beDiagram.getDb().commitTrans();

        return beDiagram;
    } //end createBEDiagram()

    private void moveGO(DbSMSClassifierGo go, int cellCounter, int yPos, Rectangle frameRect)
            throws DbException {
        Rectangle storeRect = go.getRectangle();

        //compute the x position and move the store go above the role line
        //if cellCounter == 0, then relativePos = (cellCounter+1)/(cellCounter+2)
        //                                      = (0+1) / (0+2) 
        //                                      =  1 / 2  (in the middle of the frame)
        double relativePos = (cellCounter + 1.0) / (cellCounter + 2.0);
        int x = frameRect.x + (int) (frameRect.width * relativePos);
        storeRect = new Rectangle(x - (storeRect.width / 2), yPos, storeRect.width,
                storeRect.height);
        go.setRectangle(storeRect);
    }

    public void setDiagramStyle(DbBEDiagram beDiagram, DbBENotation notation) throws DbException {
        int notationId = notation.getMasterNotationID().intValue();
        DbObject style = null;
        switch (notationId) {
        case DbInitialization.UML_USE_CASE: {
            style = beDiagram.getProject().findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_USE_CASE_STYLE_NAME);
            if (style != null)
                beDiagram.setStyle((DbBEStyle) style);
        }
            break;
        case DbInitialization.UML_ACTIVITY_DIAGRAM: {
            style = beDiagram.getProject().findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_ACTIVITY_STYLE_NAME);
            if (style != null)
                beDiagram.setStyle((DbBEStyle) style);
        }
            break;
        case DbInitialization.UML_COLLABORATION_DIAGRAM: {
            style = beDiagram.getProject().findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_COLLABORATION_STYLE_NAME);
            if (style != null)
                beDiagram.setStyle((DbBEStyle) style);
        }
            break;
        case DbInitialization.UML_COMPONENT_DIAGRAM: {
            style = beDiagram.getProject().findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_COMPONENT_STYLE_NAME);
            if (style != null)
                beDiagram.setStyle((DbBEStyle) style);
        }
            break;
        case DbInitialization.UML_DEPLOYMENT_DIAGRAM: {
            style = beDiagram.getProject().findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_DEPLOYMENT_STYLE_NAME);
            if (style != null)
                beDiagram.setStyle((DbBEStyle) style);
        }
            break;
        case DbInitialization.UML_SEQUENCE_DIAGRAM: {
            style = beDiagram.getProject().findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_SEQUENCE_STYLE_NAME);
            if (style != null)
                beDiagram.setStyle((DbBEStyle) style);
        }
            break;
        case DbInitialization.UML_STATE_DIAGRAM: {
            style = beDiagram.getProject().findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.UML_STATE_STYLE_NAME);
            if (style != null)
                beDiagram.setStyle((DbBEStyle) style);
        }
            break;
        default: {
            style = beDiagram.getProject().findComponentByName(DbBEStyle.metaClass,
                    DbBEStyle.DEFAULT_STYLE_NAME);
            if (style != null)
                beDiagram.setStyle((DbBEStyle) style);
        }
            break;
        }
        if (style == null)
            throw new RuntimeException("Unable to set style for this diagram.");
    }

    //called by org.modelsphere.sms.be.graphic.tool.BEExplodeTool.addDiagramElement() & explodeUseCase()
    public DbBENotation chooseChildNotation(DbBEUseCase usecase, DbBENotation notation)
            throws DbException {
        if (notation == null) {
            DbSMSProject proj = (DbSMSProject) usecase.getProject();
            notation = proj.getBeDefaultNotation();
        }
        DbSMSProject project = (DbSMSProject) notation.getProject();
        int notationId = notation.getMasterNotationID().intValue();
        JFrame frame = null;
        try {
            Class claz = Class.forName("org.modelsphere.sms.MainFrame"); //NOT LOCALIZABLE
            java.lang.reflect.Method method = claz
                    .getDeclaredMethod("getSingleton", new Class[] {}); //NOT LOCALIZABLE
            frame = (JFrame) method.invoke(null, new Object[] {});
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }

        if (notationId == DbInitialization.UML_USE_CASE) {
            String name = Terminology.findTerminologyByName(DbBENotation.UML_USE_CASE).getTerm(
                    DbBEUseCase.metaClass).toLowerCase();

            //choose between usecase, sequence, collabo, statechart, activity..
            Object[] list = new Object[] { DbBENotation.UML_USE_CASE,
                    DbBENotation.UML_SEQUENCE_DIAGRAM, DbBENotation.UML_COLLABORATION_DIAGRAM,
                    DbBENotation.UML_STATE_DIAGRAM, DbInitialization.UML_ACTIVITY_DIAGRAM_TXT };

            ////
            // add all UML notations, including the user-defined notations based on UML master notations.

            ArrayList<String> al = new ArrayList<String>();
            DbEnumeration projectEnum = project.getComponents().elements(DbBENotation.metaClass);
            while (projectEnum.hasMoreElements()) {
                DbBENotation dbBENota = (DbBENotation) projectEnum.nextElement();
                int masterNotationId = dbBENota.getMasterNotationID().intValue();
                if (masterNotationId == 13 //use case
                        || masterNotationId == 14 //sequence
                        || masterNotationId == 15 //collaboration
                        || masterNotationId == 16 //state
                        || masterNotationId == 17 //activity
                )
                    al.add(dbBENota.getName());
            }
            projectEnum.close();

            Object[] objects = al.toArray();
            String msg = MessageFormat.format(SELECT_NOTATION, new Object[] { name });
            int idx = SelectDialog.select(frame, msg, objects, (Icon[]) null, 0, true, true);
            if (idx == -1)
                return null;
            notation = (DbBENotation) project.findComponentByName(DbBENotation.metaClass,
                    objects[idx].toString());
        } //end if

        if (notation != null) {
            //if the use case is already exploded, we must keep the initial terminology name
            boolean keepName = false;
            DbEnumeration dbEnum = usecase.getComponents().elements(DbBEDiagram.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbObject comp = dbEnum.nextElement();
                if (comp instanceof DbBEDiagram) {
                    keepName = true;
                    break;
                }
            } //end while
            dbEnum.close();

            if (keepName == false) {
                DbEnumeration projectEnum = notation.getProject().getComponents().elements(
                        DbBENotation.metaClass);
                String masterNotationName = "";
                int masterNotationID = notation.getMasterNotationID().intValue();
                DbBENotation masterNotation = null;
                while (projectEnum.hasMoreElements()) {
                    DbBENotation dbBENota = (DbBENotation) projectEnum.nextElement();
                    if (dbBENota.getNotationID().intValue() == masterNotationID) {
                        masterNotation = dbBENota;
                        break;
                    }
                }
                projectEnum.close();

                String notationName = masterNotation.getName();
                String tname = usecase.getTerminologyName();
                if (tname == null)
                    usecase.setTerminologyName(notationName);
                else if (tname.equals(""))
                    usecase.setTerminologyName(notationName);
                else if (false == tname.equals(notationName))
                    usecase.setTerminologyName(notationName);
            }
        }
        return notation;
    } //end chooseChildNotation()

    public static void updateUseCaseTerminology(DbObject useCaseOrModel) throws DbException {
        updateUseCaseTerminology(useCaseOrModel, true);
    }

    public static void updateUseCaseTerminology(DbObject useCaseOrModel, boolean integrityCall)
            throws DbException {

        ////
        // get the composite's terminology

        DbObject composite = useCaseOrModel;
        if (useCaseOrModel instanceof DbBEUseCase)
            composite = useCaseOrModel.getComposite();

        String compositeTerm = null;
        if (composite instanceof DbBEUseCase)
            compositeTerm = ((DbBEUseCase) composite).getTerminologyName();
        else if (composite instanceof DbBEModel)
            compositeTerm = ((DbBEModel) composite).getTerminologyName();

        //set the new terminology unless it's a leaf
        ArrayList<DbBEUseCase> useCaseList = new ArrayList<DbBEUseCase>();
        DbEnumeration dbEnum = useCaseOrModel.getComponents().elements(DbBEUseCase.metaClass);
        boolean isLeaf = true;
        while (dbEnum.hasMoreElements()) {
            useCaseList.add((DbBEUseCase) dbEnum.nextElement());
            isLeaf = false;
        }
        dbEnum.close();

        dbEnum = useCaseOrModel.getComponents().elements(DbBEDiagram.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEDiagram diagram = (DbBEDiagram) dbEnum.nextElement();
            DbBENotation notation = null;
            if (compositeTerm != null && integrityCall) {
                notation = (DbBENotation) diagram.getProject().findComponentByName(
                        DbBENotation.metaClass, compositeTerm);
                diagram.setNotation(notation);
                getSingleInstance().setDiagramStyle(diagram, notation);
            }
            isLeaf = false;
        }
        dbEnum.close();

        if (isLeaf)
            ((DbBEUseCase) useCaseOrModel).setTerminologyName("");
        else if (integrityCall) {
            if (useCaseOrModel instanceof DbBEUseCase)
                ((DbBEUseCase) useCaseOrModel).setTerminologyName(compositeTerm);
            else
                ((DbBEModel) useCaseOrModel).setTerminologyName(compositeTerm);
        }

        for (int i = 0; i < useCaseList.size(); i++)
            updateUseCaseTerminology(useCaseList.get(i), integrityCall);
    }

    public boolean isLeaf(DbObject eventDbo) throws DbException {
        DbEnumeration dbEnum = eventDbo.getComponents().elements(DbBEUseCase.metaClass);
        boolean isLeaf = true;
        if (dbEnum.hasMoreElements()) {
            dbEnum.nextElement();
            isLeaf = false;
        }
        dbEnum.close();

        if (!isLeaf)
            return isLeaf;

        dbEnum = eventDbo.getComponents().elements(DbBEDiagram.metaClass);
        if (dbEnum.hasMoreElements()) {
            dbEnum.nextElement();
            isLeaf = false;
        }
        dbEnum.close();
        return isLeaf;
    }

    private void initDiagram(DbBEDiagram beDiagram, DbBENotation notation) throws DbException {
        if (DiagramAutomaticCreationOptionGroup.isCreateBPMDiagramFrame()) {
            DbSemanticalObject so = (DbSemanticalObject) beDiagram.getComposite();
            DefaultMainFrame frame = null;
            try {
                Class claz = Class.forName("org.modelsphere.sms.MainFrame"); //NOT LOCALIZABLE
                java.lang.reflect.Method method = claz.getDeclaredMethod("getSingleton",
                        new Class[] {}); //NOT LOCALIZABLE
                frame = (DefaultMainFrame) method.invoke(null, new Object[] {});
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
            ApplicationDiagram diagram = new ApplicationDiagram(so, beDiagram, SMSToolkit
                    .getToolkit(beDiagram).createGraphicalComponentFactory(), frame
                    .getDiagramsToolGroup());
            DbBEContextGo contextGo = createFrame(beDiagram, (DbSMSClassifier) so);

            Dimension dim = ApplicationDiagram.getPageSize(beDiagram.getPageFormat(), beDiagram
                    .getPrintScale().intValue());
            if (dim == null) {
                return;
            }

            int margin = computeMargin(notation); //compute margin according default size of notation components 
            int x = margin + MARGIN_GAP;
            int y = (int) (dim.getHeight() * 0.15 / 2);
            int width = (int) (dim.getWidth() - (2 * (margin + MARGIN_GAP)));
            int height = (int) (dim.getHeight() * 0.85);
            diagram.delete();

            int masterNotationID = notation.getMasterNotationID().intValue();

            if (contextGo != null) {
                if (masterNotationID == DbInitialization.UML_USE_CASE)
                    contextGo.setRectangle(new Rectangle(x * 2, y, width / 2, height / 2));
                else
                    contextGo.setRectangle(new Rectangle(x, y, width, height));

            }
        } //end if
    } //end initDiagram()

    //the margin at the left and at the right of a frame must be wide enough
    //to hold a graphical representation of an actor, a store or a process
    private int computeMargin(DbBENotation notation) throws DbException {
        int margin = 0;

        Integer iValue = notation.getActorDefaultWidth();
        int value = (iValue == null) ? 0 : iValue.intValue();
        if (margin < value)
            margin = value;

        iValue = notation.getStoreDefaultWidth();
        value = (iValue == null) ? 0 : iValue.intValue();
        if (margin < value)
            margin = value;

        iValue = notation.getUseCaseDefaultWidth();
        value = (iValue == null) ? 0 : iValue.intValue();
        if (margin < value)
            margin = value;

        return margin;
    } //end computeMargin()

    public DbBEUseCase createUseCase(DbObject composite, DbBENotation notation) throws DbException {
        DbBEUseCase process;

        try {
            process = new DbBEUseCase(composite);
        } catch (NullPointerException ex) {
            process = null;
        } //end try

        return process;
    } //end createUseCase()

    public DbBEActor createActor(DbObject composite, DbBENotation notation) throws DbException {
        DbBEActor actor;

        try {
            actor = new DbBEActor(composite);
        } catch (NullPointerException ex) {
            actor = null;
        } //end try

        return actor;
    } //end createActor()

    public DbBEStore createStore(DbObject composite, DbBENotation notation) throws DbException {
        DbBEStore store;

        try {
            store = new DbBEStore(composite);
        } catch (NullPointerException ex) {
            store = null;
        } //end try

        return store;
    } //end createStore()

    public DbBEFlow createFlow(DbObject composite, DbBENotation notation) throws DbException {
        DbBEFlow flow;

        try {
            flow = new DbBEFlow(composite);
        } catch (NullPointerException ex) {
            flow = null;
        } //end try 

        return flow;
    } //end createFlow() 

    public void initFlow(DbBEFlow flow, DbBENotation notation) throws DbException {
        if (notation.getName().equals(DbBENotation.UML_USE_CASE)) {
            DbSMSProject project = (DbSMSProject) notation
                    .getCompositeOfType(DbSMSProject.metaClass);
            DbSMSUmlExtensibility extensibility = project.getUmlExtensibility();
            DbSMSClassifier end1 = flow.getFirstEnd();
            DbSMSClassifier end2 = flow.getSecondEnd();

            //if between two use cases
            if ((end1 instanceof DbBEUseCase) && (end2 instanceof DbBEUseCase)) {
                DbSMSStereotype stereo = Extensibility.findByName(extensibility, "uses"); //NOT LOCALIZABLE, stereotype name
                if (stereo != null) {
                    flow.setUmlStereotype(stereo);
                } //end if
            } else if ((end1 instanceof DbBEUseCase) || (end2 instanceof DbBEUseCase)) {
                flow.setArrowFirstEnd(Boolean.FALSE);
                flow.setArrowSecondEnd(Boolean.FALSE);
                DbSMSStereotype stereo = Extensibility.findByName(extensibility, "communicates"); //NOT LOCALIZABLE, stereotype name
                if (stereo != null) {
                    flow.setUmlStereotype(stereo);
                } //end if
            } else {
                //should not occur 
            } //end if
        } //end if

    } //end initFlow()

    public DbBEContextGo createFrame(DbBEDiagram diagram, DbSMSClassifier classifier)
            throws DbException {
        DbBENotation notation = diagram.getNotation();
        if (notation == null) {
            return null;
        }

        int masterNotationID = notation.getMasterNotationID().intValue();
        Boolean hasFrame = notation.getHasFrame();
        if ((hasFrame == null) || (!hasFrame.booleanValue())) {
            return null;
        }

        DbBEContextGo frame = new DbBEContextGo(diagram, classifier);
        InsertAction action = InsertAction.NO_ACTION;

        int x = 0;
        if (masterNotationID == DbInitialization.UML_SEQUENCE_DIAGRAM) {
            action = InsertAction.CREATE_ACTOR;

            if (classifier instanceof DbBEUseCase) {
                DbBEUseCase context = (DbBEUseCase) classifier;
                x += createActorCells(frame, notation, context, x);
            } //end if
        } //end if

        createCell(frame, notation, x, 0, CREATE_FRAME_CALLER, action);

        return frame;
        //return null;
    } //end createFrame()

    private int createActorCells(DbBEContextGo frame, DbBENotation notation, DbBEUseCase context,
            int xPos) throws DbException {
        ArrayList actorList = new ArrayList();
        DbRelationN relN = context.getFirstEndFlows();
        DbEnumeration dbEnum = relN.elements(DbBEFlow.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEFlow flow = (DbBEFlow) dbEnum.nextElement();
            DbSMSClassifier secondEnd = flow.getSecondEnd();
            if (secondEnd instanceof DbBEActor) {
                //create cell 
                DbBEContextCell cell = createCell(frame, notation, xPos++, 0,
                        CREATE_ACTOR_CELL_CALLER, InsertAction.NO_ACTION);
                cell.setDescription(null);
                actorList.add(secondEnd);
            }
        } //end while
        dbEnum.close();

        relN = context.getSecondEndFlows();
        dbEnum = relN.elements(DbBEFlow.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEFlow flow = (DbBEFlow) dbEnum.nextElement();
            DbSMSClassifier firstEnd = flow.getFirstEnd();
            if (firstEnd instanceof DbBEActor) {
                //create cell 
                DbBEContextCell cell = createCell(frame, notation, xPos++, 0,
                        CREATE_ACTOR_CELL_CALLER, InsertAction.NO_ACTION);
                cell.setDescription(null);
                actorList.add(firstEnd);
            }
        } //end while
        dbEnum.close();

        DbBEDiagram diagram = (DbBEDiagram) frame.getCompositeOfType(DbBEDiagram.metaClass);
        Dimension dim = ApplicationDiagram.getPageSize(diagram.getPageFormat(), diagram
                .getPrintScale().intValue());
        int margin = computeMargin(notation); //compute margin according default size of notation components 
        int rectX = margin + MARGIN_GAP;
        int width = (int) (dim.getWidth() - (2 * (margin + MARGIN_GAP)));
        int cellWidth = (width / (xPos + 1));

        for (int i = 0; i < xPos; i++) {
            DbBEActor actor = (DbBEActor) actorList.get(i);
            DbBEActorGo go = new DbBEActorGo(diagram, actor);
            Rectangle r = go.getRectangle();
            r.x = rectX + i * cellWidth + (cellWidth / 2) - (r.width / 2);
            go.setRectangle(r);
        } //end for  

        return xPos;
    } //end createActorCells()

    public DbBENotation getMasterNotation(DbBENotation notation) throws DbException {
        int masterNotationID = notation.getMasterNotationID().intValue();
        DbBENotation beNotation = null;
        DbEnumeration projectEnum = notation.getProject().getComponents().elements(
                DbBENotation.metaClass);
        while (projectEnum.hasMoreElements()) {
            DbBENotation dbBENota = (DbBENotation) projectEnum.nextElement();
            if (dbBENota.getNotationID().intValue() == masterNotationID) {
                beNotation = dbBENota;
                break;
            }
        }
        projectEnum.close();
        return beNotation;
    }

    public DbBEContextCell createCell(DbBEContextGo frame, DbBENotation notation, int x, int y,
            int caller, InsertAction action) throws DbException {

        notation = getMasterNotation(notation);

        DbBEContextCell cell = new DbBEContextCell(frame, x, y);
        cell.setBottomBorder(notation.getDefButtomBorder());
        cell.setRightBorder(notation.getDefRightBorder());
        int horizontal = notation.getDefHorizontalAlignment().intValue();
        int vertical = notation.getDefVerticalAlignment().intValue();
        SMSHorizontalAlignment horizontalAlign = DbInitialization
                .convertToHorizontalAlignment(horizontal);
        SMSVerticalAlignment verticalAlign = DbInitialization.convertToVerticalAlignment(vertical);
        cell.setHorizontalJustification2(horizontalAlign);
        cell.setVerticalJustification2(verticalAlign);

        //If it's a sequence diagram, and if there is no actor above the role line, 
        //then create a role (store) above the role line  (When a sequence diagram is 
        //created by specifying a use case, if the use case is linked to an actor, the
        //actor is placed above the role line, so we don't have to create a store above
        //this role line).
        if (notation.getNotationID().intValue() == DbInitialization.UML_SEQUENCE_DIAGRAM) {
            if (caller != CREATE_ACTOR_CELL_CALLER) {

                Terminology terminology = Terminology.findTerminology(notation);
                DbBEModel model = (DbBEModel) frame.getCompositeOfType(DbBEModel.metaClass);
                DbBEDiagram diagram = (DbBEDiagram) frame.getCompositeOfType(DbBEDiagram.metaClass);
                DbSMSClassifierGo go = null;

                if (action == InsertAction.CREATE_ACTOR) {
                    DbBEActor actor = new DbBEActor(model);
                    MetaClass mc = DbBEActor.metaClass;
                    String term = (terminology == null) ? mc.getGUIName() : terminology.getTerm(mc);
                    actor.setName(term);
                    DbBEActorGo actorGo = new DbBEActorGo(diagram, actor);
                    actorGo.setCell(cell);
                    go = actorGo;
                } else if (action == InsertAction.CREATE_ROLE) {
                    DbBEStore role = new DbBEStore(model);
                    MetaClass mc = DbBEContextCell.metaClass;
                    String term = (terminology == null) ? mc.getGUIName() : terminology.getTerm(mc);
                    role.setName(term);

                    DbBEStoreGo roleGo = new DbBEStoreGo(diagram, role);
                    roleGo.setCell(cell);
                    go = roleGo;
                }

                if (go != null) {
                    Rectangle roleRect = go.getRectangle();
                    Rectangle frameRect = frame.getRectangle();
                    double relPos = (x + 1.0) / (x + 2.0);
                    int xPos = frameRect.x + (int) (frameRect.width * relPos);
                    int yPos = frameRect.y;
                    roleRect = new Rectangle(xPos, yPos, roleRect.width, roleRect.height);
                    go.setRectangle(roleRect);

                }

            }
        } //end if

        return cell;
    } //end createCell()

    //
    // getDisplayText() methods
    // 
    public String getDisplayText(DbBEUseCase usecase) throws DbException {
        String displayText = null;

        //usecase.getDb().beginReadTrans(); fait planter si appelé dans un Worker
        if (!(usecase.isExternal())) {
            String name = usecase.getSemanticalName(DbObject.LONG_FORM);
            displayText = name + " (" + usecase.getName() + ")";
        } else {
            String extAlphaId = usecase.getSourceAlphanumericIdentifier();
            if (extAlphaId != null && extAlphaId.length() > 0) {
                displayText = extAlphaId + EXTERNAL_SUFFIX;
            } else {
                String extNumId = usecase.getSourceNumericIdentifier();
                if (extNumId != null && extNumId.length() > 0) {
                    displayText = extNumId + EXTERNAL_SUFFIX;
                } else {
                    displayText = usecase.getSemanticalName(DbObject.LONG_FORM) + EXTERNAL_SUFFIX;
                } //end if
            } //end if
        } //end if
        //usecase.getDb().commitTrans();

        return displayText;
    } //end getDisplayText()

    public String getDisplayText(DbBEStore store) throws DbException {
        String name = store.getSemanticalName(DbObject.SHORT_FORM);
        return name;
    } //end getDisplayText()

    public String getDisplayText(DbBEActor actor) throws DbException {
        String name = actor.getSemanticalName(DbObject.SHORT_FORM);
        return name;
    } //end getDisplayText()

    public String getDisplayText(DbBEFlow flow) throws DbException {
        String name = flow.getSemanticalName(DbObject.SHORT_FORM);
        name = name + " (" + flow.getName() + ")";
        return name;
    } //end getDisplayText()

    public String getDisplayText(DbBEContextGo frame) throws DbException {
        DbBEDiagram diagram = (DbBEDiagram) frame.getCompositeOfType(DbBEDiagram.metaClass);
        DbBENotation notation = diagram.getNotation();
        MetaClass mc = DbBEContextGo.metaClass;
        Terminology terminology = Terminology.findTerminology(notation);
        String term = (terminology == null) ? mc.getGUIName() : terminology.getTerm(mc);
        return term;
    } //end getDisplayText()

    public String getDisplayText(DbBEContextCell cell) throws DbException {
        cell.getDb().beginReadTrans();
        DbBEDiagram diagram = (DbBEDiagram) cell.getCompositeOfType(DbBEDiagram.metaClass);
        DbBENotation notation = diagram.getNotation();

        cell.getDb().commitTrans();
        MetaClass mc = DbBEContextCell.metaClass;
        Terminology terminology = Terminology.findTerminology(notation);
        String term = (terminology == null) ? mc.getGUIName() : terminology.getTerm(mc);

        String text = MessageFormat.format("{0} ({1},{2})", new Object[] { term, cell.getX(),
                cell.getY() });
        return text;
    } //end getDisplayText()

    public DbBEDiagram getExplodedDiagram(DbBEUseCase process) throws DbException {
        DbBEDiagram explodedDiagram = null;

        DbEnumeration dbEnum = process.getComponents().elements(DbBEDiagram.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbObject comp = dbEnum.nextElement();
            if (comp instanceof DbBEDiagram) {
                explodedDiagram = (DbBEDiagram) comp;
                break;
            }
        } //end while
        dbEnum.close();

        return explodedDiagram;
    } //end isExplodableProcess()

    //
    // other services (public methods)
    //
    //Called by BESelectToolCommand.constraintCellBoundary()
    private static final int GAP = 6;

    public Rectangle getCellRectangle(DbBEContextCell cell) throws DbException {
        //compute header size
        DbBEContextGo contextGo = (DbBEContextGo) cell.getCompositeOfType(DbBEContextGo.metaClass);
        BEContext beContext = (BEContext) contextGo.getGraphicPeer();
        if (beContext == null) {
            return null;
        } else {
            TableZone zone = beContext.getCenterZone();
            Rectangle frameRect = contextGo.getRectangle();
            Rectangle tableRect = zone.getRectangle(frameRect);
            int dy = tableRect.y;
            Rectangle rect = new Rectangle(frameRect.x + GAP, frameRect.y + dy + GAP,
                    frameRect.width - (GAP * 2), frameRect.height - dy - (GAP * 2));

            computeCellDimensions(contextGo); //compute m_rowHeights & m_rowHeights

            //Rectangle frameRect = contextGo.getRectangle();
            int xCell = cell.getX().intValue();
            int yCell = cell.getY().intValue();

            Rectangle cellRect = TableCell.computeCellRectangle(rect, xCell, yCell, m_rowHeights,
                    m_colWidths);
            return cellRect;
        }
    } //end getCellRectangle()

    //Compute m_rowHeights & m_rowHeights
    //called by SMSSelectTool.getFrameSize()
    public void computeCellDimensions(DbBEContextGo go) throws DbException {
        TableZone zone = new TableZone((String) null);
        //  for each cell, add it in table zone
        DbRelationN relN = go.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEContextCell.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEContextCell cell = (DbBEContextCell) dbEnum.nextElement();
            int x = cell.getX().intValue();
            int y = cell.getY().intValue();
            double wx = cell.getXweight().doubleValue();
            double wy = cell.getYweight().doubleValue();
            boolean rightBorder = cell.isRightBorder();
            boolean bottomBorder = cell.isBottomBorder();
            String text = cell.getDescription();
            Font font = cell.getFont();
            SMSVerticalAlignment verticalAlign = cell.getVerticalJustification2();
            SMSHorizontalAlignment horizontalAlign = cell.getHorizontalJustification2();
            int vJustification = DbInitialization.convertToVerticalAlignment(verticalAlign);
            int hJustification = DbInitialization.convertToHorizontalAlignment(horizontalAlign);
            TableCell tableCell = new TableCell(x, y, wx, wy, rightBorder, bottomBorder, text,
                    font, hJustification, vJustification);
            zone.add(tableCell);
        } //end while
        dbEnum.close();

        zone.computeSizeOfRowsAndColumns();
        m_rowHeights = zone.getRowHeights();
        m_colWidths = zone.getColWidths();
    } //end getNbOfCells()

    public void initAccordingNotation(DbBEDiagram diagram) throws DbException {
        DbBENotation notation = diagram.getNotation();
        if (notation == null) {
            return;
        }

        if (notation.isBuiltIn()) {
            String name = notation.getName();
            int masterID = notation.getMasterNotationID().intValue();

            //No frame in a UML collaboration diagram
            if (masterID == DbInitialization.UML_COLLABORATION_DIAGRAM) {
                removeFrame(diagram);
            }
        } //end if   
    } //end initAccordingNotation()

    private void removeFrame(DbBEDiagram diagram) throws DbException {
        DbRelationN relN = diagram.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEContextGo.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEContextGo frame = (DbBEContextGo) dbEnum.nextElement();
            frame.remove();
        } //end while
        dbEnum.close();
    } //end removeFrame()

    public final String getSemanticalName(int form, DbBEQualifier qualifier, Double rate)
            throws DbException {
        String r = rate.toString();
        return (qualifier != null ? qualifier.getName() : r);
    }

    public DbObject getComposite(DbBEDiagram diagGO, int x, int y) throws DbException {
        DbObject composite = null;

        //for each frame
        DbRelationN relN = diagGO.getComponents();
        DbEnumeration dbEnum = relN.elements(DbBEContextGo.metaClass);
        BEUtility util = BEUtility.getSingleInstance();
        while (dbEnum.hasMoreElements()) {
            //if it contains <x,y>
            DbBEContextGo go = (DbBEContextGo) dbEnum.nextElement();
            Rectangle rect = go.getRectangle();
            if (rect.contains(x, y)) {
                computeCellDimensions(go);
                //for each cell
                DbRelationN relN2 = go.getComponents();
                DbEnumeration enum2 = relN2.elements(DbBEContextCell.metaClass);
                while (enum2.hasMoreElements()) {
                    DbBEContextCell cell = (DbBEContextCell) enum2.nextElement();
                    Rectangle cellRect = getCellRect(cell, getRowHeights(), getColWidths());
                    if (cellRect.contains(x, y)) {
                        composite = cell;
                        break;
                    }
                } //end while
                enum2.close();
                if (composite != null)
                    break;
            } //end if
        } //end while
        dbEnum.close();

        if (composite == null)
            composite = diagGO;

        return composite;
    } //end getComposite() 

    private Rectangle getCellRect(DbBEContextCell cell, double[] rowHeights, double[] colWidths)
            throws DbException {
        DbBEContextGo contextGo = (DbBEContextGo) cell.getCompositeOfType(DbBEContextGo.metaClass);
        Rectangle contextRect = contextGo.getRectangle();

        int x = cell.getX().intValue();
        int y = cell.getY().intValue();

        Rectangle rect = TableCell.computeCellRectangle(contextRect, x, y, rowHeights, colWidths);
        return rect;
    } //end getCellRect()

} //end BEUtility
