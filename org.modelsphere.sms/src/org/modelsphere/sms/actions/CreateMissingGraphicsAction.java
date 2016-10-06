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

package org.modelsphere.sms.actions;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.FocusManager;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.be.db.DbBEContextGo;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSCommonItemGo;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSNotice;
import org.modelsphere.sms.db.DbSMSNoticeGo;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.db.DbSMSPackageGo;
import org.modelsphere.sms.db.util.AnyGo;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.db.DbOOAssociation;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.db.DbOOInheritance;
import org.modelsphere.sms.oo.db.DbOOPackage;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.preference.DiagramAutomaticCreationOptionGroup;

@SuppressWarnings("serial")
public final class CreateMissingGraphicsAction extends AbstractApplicationAction implements
        SelectionActionListener {

    public static final String kAddMissingGraphics = LocaleMgr.action
            .getString("addMissingGraphics");

    CreateMissingGraphicsAction() {
        super(kAddMissingGraphics);
    }

    protected final void doActionPerformed(ActionEvent e) {
        Point pos = getDiagramLocation(e); // if triggered from the diagram, create all graphics at the click position
        DbObject[] diagrams;
        Object focusObject = ApplicationContext.getFocusManager().getFocusObject();
        if (focusObject instanceof ApplicationDiagram)
            diagrams = new DbObject[] { ((ApplicationDiagram) focusObject).getDiagramGO() };
        else
            diagrams = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();

        try {
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, diagrams, kAddMissingGraphics);
            for (int i = 0; i < diagrams.length; i++) {
                if (diagrams[i] instanceof DbOODiagram)
                    createOOGraphics((DbOODiagram) diagrams[i], pos);
                else if (diagrams[i] instanceof DbBEDiagram)
                    createBEGraphics((DbBEDiagram) diagrams[i], pos);
                else
                    createORGraphics((DbORDiagram) diagrams[i], pos);

                createOtherGraphics((DbSMSDiagram) diagrams[i], pos);
            } //end for
            DbMultiTrans.commitTrans(diagrams);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }

    public final void performAction(DbSMSDiagram diagram) {

        if (diagram == null)
            return;

        try {
            diagram.getDb().beginWriteTrans(kAddMissingGraphics);
            Point pos = new Point(0, 0);
            if (diagram instanceof DbOODiagram)
                createOOGraphics((DbOODiagram) diagram, pos);
            else if (diagram instanceof DbBEDiagram)
                createBEGraphics((DbBEDiagram) diagram, pos);
            else
                createORGraphics((DbORDiagram) diagram, pos);

            createOtherGraphics((DbSMSDiagram) diagram, pos);
            diagram.getDb().commitTrans();
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }

    // Create graphic reps for all classes, associations and inheritances in the package
    // that do not have already a graphical rep.
    public static void createOOGraphics(DbOODiagram diagram, Point pos) throws DbException {
        // First pass: create graphics for associations and inheritances.
        DbOOAbsPackage pack = (DbOOAbsPackage) diagram.getComposite();
        DbEnumeration dbEnum = pack.getComponents().elements(DbOOClass.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbOOClass adt = (DbOOClass) dbEnum.nextElement();
            createOOLineGraphics(diagram, adt, pos);
        }
        dbEnum.close();

        // Second pass: create graphics for classes (except inner classes).
        dbEnum = pack.getComponents().elements(DbOOClass.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbOOClass adt = (DbOOClass) dbEnum.nextElement();
            if (DbGraphic.getFirstGraphicalObject(diagram, adt) == null)
                AnyGo.createClassifierGo(diagram, adt, pos);
        }
        dbEnum.close();
        // Third pass: create graphics for Packages.
        dbEnum = pack.getComponents().elements(DbOOPackage.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbOOPackage aPackage = (DbOOPackage) dbEnum.nextElement();
            if (DbGraphic.getFirstGraphicalObject(diagram, aPackage) == null) {
                DbSMSPackageGo packGo = new DbSMSPackageGo(diagram, aPackage);
                if (pos != null)
                    packGo.setRectangle(new Rectangle(pos));
            }
        }
        dbEnum.close();
    }

    private static void createOOLineGraphics(DbOODiagram diagram, DbOOClass adt, Point pos)
            throws DbException {
        // Create a graphic rep for each "extends" inheritance
        DbEnumeration dbEnum = adt.getSuperInheritances().elements();
        while (dbEnum.hasMoreElements()) {
            DbOOInheritance inher = (DbOOInheritance) dbEnum.nextElement();
            if (DbGraphic.getFirstGraphicalObject(diagram, inher) == null)
                AnyGo.createOOInheritanceGo(diagram, inher, pos);
        }
        dbEnum.close();

        dbEnum = adt.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            if (dbo instanceof DbOOClass) { // repeat the process recursively on inner classes
                createOOLineGraphics(diagram, (DbOOClass) dbo, pos);
            } else if (dbo instanceof DbOODataMember) {
                // Create a graphic rep for all the associations with a navigable end in this package
                DbOOAssociationEnd end = ((DbOODataMember) dbo).getAssociationEnd();
                if (end == null || !end.isNavigable())
                    continue;
                DbOOAssociation assoc = (DbOOAssociation) end.getComposite();
                if (DbGraphic.getFirstGraphicalObject(diagram, assoc) == null)
                    AnyGo.createOOAssociationGo(diagram, assoc, pos);
            }
        }
        dbEnum.close();
    }

    public static void createORGraphics(DbORDiagram diagram, Point pos) throws DbException {
        DbSMSPackage pack = (DbSMSPackage) diagram.getComposite();
        if (pack instanceof DbORDataModel || pack instanceof DbORDomainModel)
            createTableModelGraphics(pack, diagram, pos);
        else if (pack instanceof DbORCommonItemModel)
            createCommonItemModelGraphics(pack, diagram, pos);
    }

    // Create a graphic rep for all associations that do not have one,
    // then create a graphic rep for all tables that do not have one.
    private static void createTableModelGraphics(DbSMSPackage pack, DbORDiagram diagram, Point pos)
            throws DbException {
        DbEnumeration dbEnum = pack.getComponents().elements(DbORAssociation.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORAssociation assoc = (DbORAssociation) dbEnum.nextElement();
            DbObject go = DbGraphic.getFirstGraphicalObject(diagram, assoc);
            if (go == null) {
                AnyGo.createORAssociationGo(diagram, assoc, pos);
            }
        }
        dbEnum.close();

        dbEnum = pack.getComponents().elements(DbSMSClassifier.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSClassifier adt = (DbSMSClassifier) dbEnum.nextElement();
            if (DbGraphic.getFirstGraphicalObject(diagram, adt) == null)
                AnyGo.createClassifierGo(diagram, adt, pos);
        }
        dbEnum.close();
    }

    public static void createCommonItemModelGraphics(DbSMSPackage pack, DbORDiagram diagram,
            Point pos) throws DbException {
        DbEnumeration dbEnum = pack.getComponents().elements(DbORCommonItem.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORCommonItem commonItem = (DbORCommonItem) dbEnum.nextElement();
            if (DbGraphic.getFirstGraphicalObject(diagram, commonItem) == null) {
                DbSMSCommonItemGo go = new DbSMSCommonItemGo(diagram, commonItem);
                if (pos != null)
                    go.setRectangle(new Rectangle(pos));
            }
        }
        dbEnum.close();
    }

    public static void createBEGraphics(DbBEDiagram diagram, Point pos) throws DbException {
        DbBEUseCase useCase = (DbBEUseCase) diagram.getComposite();
        //flow
        DbEnumeration dbEnum = useCase.getComponents().elements(DbBEFlow.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEFlow flow = (DbBEFlow) dbEnum.nextElement();
            if (DbGraphic.getFirstGraphicalObject(diagram, flow) == null)
                AnyGo.createBEFlowGo(diagram, flow, pos);
        }
        dbEnum.close();
        //box
        dbEnum = useCase.getComponents().elements(DbSMSClassifier.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSClassifier adt = (DbSMSClassifier) dbEnum.nextElement();
            if (DbGraphic.getFirstGraphicalObject(diagram, adt) == null)
                AnyGo.createClassifierGo(diagram, adt, pos);
        }
        dbEnum.close();
        //add frame
        if (DiagramAutomaticCreationOptionGroup.isCreateBPMDiagramFrame()) {
            if (DbGraphic.getFirstGraphicalObject(diagram, useCase, null, DbBEContextGo.metaClass) == null) {
                BEUtility util = BEUtility.getSingleInstance();
                DbBEContextGo contextGo = util.createFrame(diagram, useCase);
                if (contextGo != null) {
                    DefaultMainFrame frame = null;
                    try {
                        Class claz = Class.forName("org.modelsphere.sms.MainFrame"); //NOT LOCALIZABLE
                        java.lang.reflect.Method method = claz.getDeclaredMethod("getSingleton",
                                new Class[] {}); //NOT LOCALIZABLE
                        frame = (DefaultMainFrame) method.invoke(null, new Object[] {});
                    } catch (Exception ex) {
                        ex.printStackTrace(System.out);
                    }

                    ApplicationDiagram appDiagram = new ApplicationDiagram(useCase, diagram,
                            SMSToolkit.getToolkit(diagram).createGraphicalComponentFactory(), frame
                                    .getDiagramsToolGroup());
                    Dimension dim = Diagram.getPageSize(diagram.getPageFormat(), diagram
                            .getPrintScale().intValue());
                    int x = (int) (dim.getWidth() * 0.2 / 2);
                    int y = (int) (dim.getHeight() * 0.15 / 2);
                    int width = (int) (dim.getWidth() * 0.8);
                    int height = (int) (dim.getHeight() * 0.85);
                    appDiagram.delete();
                    contextGo.setRectangle(new Rectangle(x, y, width, height));
                } //end if
            } //end if
        } //end if 
    } //end createBEGraphics 

    public static void createOtherGraphics(DbSMSDiagram diagram, Point pos) throws DbException {
        DbSMSPackage pack = (DbSMSPackage) diagram.getCompositeOfType(DbSMSPackage.metaClass);
        if (pack == null)
            return;

        //for each notice not present in diagram, create graphical representation
        DbEnumeration dbEnum = null;
        if (pack instanceof DbBEModel)
            dbEnum = pack.getComponents().elementAt(0).getComponents().elements(
                    DbSMSNotice.metaClass);
        else
            dbEnum = pack.getComponents().elements(DbSMSNotice.metaClass);

        while (dbEnum.hasMoreElements()) {
            DbSMSNotice notice = (DbSMSNotice) dbEnum.nextElement();
            DbEnumeration dbEnum2 = notice.getNoticeGos().elements(DbSMSNoticeGo.metaClass);
            if (!dbEnum2.hasMoreElements()) {
                DbSMSNoticeGo go = new DbSMSNoticeGo(diagram);
                go.setNotice(notice);

                if (pos != null)
                    go.setRectangle(new Rectangle(pos, new Dimension(DbSMSNoticeGo.DEFAULT_WIDTH,
                            DbSMSNoticeGo.DEFAULT_HEIGHT)));

            } //end if
            dbEnum2.close();
        } //end while 
        dbEnum.close();

        //for each submodel not present in diagram, create graphical representation
        dbEnum = pack.getComponents().elements(DbSMSPackage.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSPackage submodel = (DbSMSPackage) dbEnum.nextElement();
            DbEnumeration dbEnum2 = submodel.getPackageGos().elements(DbSMSPackageGo.metaClass);
            if (!dbEnum2.hasMoreElements()) {
                DbSMSPackageGo go = new DbSMSPackageGo(diagram, submodel);
                if (go != null)
                    go.setAutoFit(Boolean.valueOf(true));
            } //end if
            dbEnum2.close();
        } //end while 
        dbEnum.close();

    } //end createOtherGraphics()

    @Override
    public void updateSelectionAction() throws DbException {
        setEnabled(FocusManager.getSingleton().getActiveDiagram() != null);
    }

} //end CreateMissingGraphicsAction

