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
package org.modelsphere.sms.oo.java.actions;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.SemanticalModel;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSInheritanceGo;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSLinkGo;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSNotice;
import org.modelsphere.sms.db.DbSMSNoticeGo;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOOAdtGo;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.db.DbOOAssociationGo;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.db.srtypes.OOAggregation;
import org.modelsphere.sms.oo.java.db.DbJVAssociation;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVInheritance;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;
import org.modelsphere.sms.oo.java.db.util.JavaUtility;
import org.modelsphere.sms.oo.java.international.LocaleMgr;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ApplyPatternAction extends AbstractDomainAction implements SelectionActionListener {
    private static final String APPLY_DESIGN_PATTERN = LocaleMgr.action
            .getString("ApplyDesignPattern");
    private static final String APPLYING_DESIGN_PATTERN_PTN = LocaleMgr.action
            .getString("ApplyingDesignPattern");
    private static final String FACTORY_METHOD_TXT = LocaleMgr.action.getString("FactoryMethod");
    private static final String PROTOTYPE_TXT = LocaleMgr.action.getString("Prototype");
    private static final String SINGLETON_TXT = LocaleMgr.action.getString("Singleton");
    private static final String ADAPTER_TXT = LocaleMgr.action.getString("Adapter");
    private static final String COMPOSITE_TXT = LocaleMgr.action.getString("Composite");
    private static final String PROXY_TXT = LocaleMgr.action.getString("Proxy");
    private static final String CHAIN_OF_RESPONSABILITY_TXT = LocaleMgr.action
            .getString("ChainOfResponsability");
    private static final String COMMAND_TXT = LocaleMgr.action.getString("Command");
    private static final String ITERATOR_TXT = LocaleMgr.action.getString("Iterator");
    private static final String OBSERVER_TXT = LocaleMgr.action.getString("Observer");
    private static final String STATE_TXT = LocaleMgr.action.getString("State");
    private static final String STRATEGY_TXT = LocaleMgr.action.getString("Strategy");
    private static final String TEMPLATE_METHOD_TXT = LocaleMgr.action.getString("TemplateMethod");

    // pattern IDs
    private static final int FACTORY_METHOD = 0;
    private static final int PROTOTYPE = 1;
    private static final int SINGLETON = 2;
    // SEPARATOR = 3
    private static final int ADAPTER = 4;
    private static final int COMPOSITE = 5;
    private static final int PROXY = 6;
    // SEPARATOR = 7
    private static final int CHAIN_OF_RESPONSABILITY = 8;
    private static final int COMMAND = 9;
    private static final int ITERATOR = 10;
    private static final int OBSERVER = 11;
    private static final int STATE = 12;
    private static final int STRATEGY = 13;
    private static final int TEMPLATE_METHOD = 14;

    private static final JavaUtility.JavaInfo UNKNOWN_JAVA_VERSION = null;

    // pattern names (warning : must follow the IDs)
    private static final String[] PATTERN_NAMES = new String[] { FACTORY_METHOD_TXT, PROTOTYPE_TXT,
            SINGLETON_TXT,
            null, // separator
            ADAPTER_TXT, COMPOSITE_TXT, PROXY_TXT,
            null, // separator
            CHAIN_OF_RESPONSABILITY_TXT, COMMAND_TXT, ITERATOR_TXT, OBSERVER_TXT, STATE_TXT,
            STRATEGY_TXT, TEMPLATE_METHOD_TXT, };

    ApplyPatternAction() {
        super(APPLY_DESIGN_PATTERN);
        setDomainValues(PATTERN_NAMES);
    }

    protected final void doActionPerformed(ActionEvent e) {
        GraphicComponent gc = getGraphicComponent(e);
        if (gc == null)
            return;
        DbSMSClassifierGo go = (DbSMSClassifierGo) ((ActionInformation) gc).getGraphicalObject();
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        DbJVClass claz = (DbJVClass) semObjs[0];
        int chosenPattern = getSelectedIndex();
        String transName = MessageFormat.format(APPLYING_DESIGN_PATTERN_PTN,
                new String[] { PATTERN_NAMES[chosenPattern] });

        try {
            claz.getDb().beginWriteTrans(transName);

            switch (chosenPattern) {
            // CREATIONAL DESIGN PATTERNS
            case SINGLETON:
                applySingleton(semObjs, go);
                break;
            case FACTORY_METHOD:
                applyFactoryMethod(semObjs, go);
                break;
            case PROTOTYPE:
                applyPrototype(semObjs, go);
                break;
            // STRUCTURAL DESIGN PATTERN
            case ADAPTER:
                applyAdapter(semObjs, go);
                break;
            case COMPOSITE:
                applyComposite(semObjs, go);
                break;
            case PROXY:
                applyProxy(semObjs, go);
                break;
            // BEHAVIORAL DESIGN PATTERM
            case COMMAND:
                applyCommand(semObjs, go);
                break;
            case CHAIN_OF_RESPONSABILITY:
                applyChainOfResponsability(semObjs, go);
                break;
            case ITERATOR:
                applyIterator(semObjs, go);
                break;
            case OBSERVER:
                applyObserver(semObjs, go);
                break;
            case STATE:
                applyState(semObjs, go);
                break;
            case STRATEGY:
                applyStrategy(semObjs, go);
                break;
            case TEMPLATE_METHOD:
                applyTemplateMethod(semObjs, go);
                break;
            } // end switch()

            int j = 0;
            claz.getDb().commitTrans();
        } catch (DbException ex) {
            // TODO : explain..
        }
    } // end doActionPerformed()

    // This action is available only from a diagram, with a single data member
    // selected.
    public final void updateSelectionAction() throws DbException {
        ApplicationDiagram diag = ApplicationContext.getFocusManager().getActiveDiagram();
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();

        // TODO?
    }

    protected int getFeatureSet() {
        return SMSFilter.JAVA;
    }

    //
    // private methods
    //
    private void applySingleton(DbObject[] semObjs, DbSMSClassifierGo go) throws DbException {
        DbOODiagram diagram = (DbOODiagram) go.getCompositeOfType(DbOODiagram.metaClass);
        JavaUtility util = JavaUtility.getSingleton();

        for (int i = 0; i < semObjs.length; i++) {
            DbJVClass claz = (DbJVClass) semObjs[i];
            String name = claz.getName();
            name = name + "Singleton";
            claz.setName(name);
            DbOOAdtGo originalClassGo = util.getGraphicalObject(claz, diagram);

            // create single instance
            DbJVDataMember field = new DbJVDataMember(claz);
            field.setName("singleInstance");
            field.setStatic(Boolean.TRUE);
            field.setVisibility(JVVisibility.getInstance(JVVisibility.PRIVATE));
            field.setType(claz);
            field.setInitialValue("null");

            // create accessor method
            DbJVMethod accessor = new DbJVMethod(claz);
            accessor.setName("getInstance");
            accessor.setStatic(Boolean.TRUE);
            accessor.setVisibility(JVVisibility.getInstance(JVVisibility.PUBLIC));
            accessor.setSynchronized(Boolean.TRUE);
            accessor.setReturnType(claz);
            String body = "{\n" + "  if (singleInstance == null) {\n" + "    singleInstance = new "
                    + name + "();\n" + "   } //end if\n" + "  \n" + "  return singleInstance;\n"
                    + "} //end getInstance()\n\n";
            accessor.setBody(body);

            // find the constructor, if any

            // create the private constructor
            DbJVConstructor constructor = new DbJVConstructor(claz);
            constructor.setVisibility(JVVisibility.getInstance(JVVisibility.PRIVATE));

            // create a note
            StringBuffer desc = new StringBuffer();
            // String uncapitalized = util.uncapitalize(originalName);
            desc.append("Client code:\n");
            desc.append(name + " instance = " + name + ".getInstance();\n");

            Rectangle rect = originalClassGo.getRectangle();
            originalClassGo.setRectangle(new Rectangle(rect.x, rect.y, 150, rect.height));
            DbSMSNoticeGo noteGo = util.createNoteAndGO(diagram, originalClassGo, desc.toString(),
                    5, 0);
            DbSMSNotice note = noteGo.getNotice();

        } // end for
    } // end applySingleton()

    private void applyFactoryMethod(DbObject[] semObjs, DbSMSClassifierGo go) throws DbException {
        DbOODiagram diagram = (DbOODiagram) go.getCompositeOfType(DbOODiagram.metaClass);
        JavaUtility util = JavaUtility.getSingleton();
        DbSMSLinkModel linkModel = util.getLinkModel(go);

        for (int i = 0; i < semObjs.length; i++) {
            DbJVClass originalClass = (DbJVClass) semObjs[i];
            String originalName = originalClass.getName();
            // DbOOAdtGo originalClassGo = getGraphicalObject(originalClass,
            // diagram);

            // Create abstract creator
            DbOOAdtGo abstractCreatorGo = util.createClassAndGO(diagram, originalClass,
                    JVClassCategory.getInstance(JVClassCategory.INTERFACE), 2, -2);
            DbJVClass abstractCreator = (DbJVClass) abstractCreatorGo.getClassifier();
            abstractCreator.setName(originalName + "Creator");

            DbJVMethod method = new DbJVMethod(abstractCreator);
            method.setName("create" + originalName);
            method.setReturnType(originalClass);

            // Create concrete creator
            DbOOAdtGo concreteCreatorGo = util.createClassAndGO(diagram, originalClass,
                    JVClassCategory.getInstance(JVClassCategory.CLASS), 2, 0);
            DbJVClass concreteCreator = (DbJVClass) concreteCreatorGo.getClassifier();
            concreteCreator.setName(originalName + "ConcreteCreator");

            DbJVMethod method2 = new DbJVMethod(concreteCreator);
            method2.setName("create" + originalName);
            method2.setReturnType(originalClass);
            String body = "{\n" + "  " + originalName + " instance = new " + originalName + "();\n"
                    + "  return instance;\n" + "} //end create" + originalName + "()\n\n";
            method2.setBody(body);

            // inheritance between creators
            DbJVInheritance inher = new DbJVInheritance(concreteCreator, abstractCreator);
            DbSMSInheritanceGo inherGo = new DbSMSInheritanceGo(diagram, concreteCreatorGo,
                    abstractCreatorGo, inher);

            // link between concrete creator and original class
            DbSMSLink link = new DbSMSLink(linkModel);
            link.addToSourceObjects(concreteCreator);
            link.addToTargetObjects(originalClass);
            DbOOAdtGo originalClassGo = util.getGraphicalObject(originalClass, diagram);
            DbSMSLinkGo linkGo = new DbSMSLinkGo(diagram, concreteCreatorGo, originalClassGo, link);
            // linkGo.setRightAngle(Boolean.FALSE);

            // create a note
            StringBuffer desc = new StringBuffer();
            String uncapitalized = util.uncapitalize(originalName);
            desc.append("Client code:\n");
            desc.append(originalName + "Creator creator = new " + originalName
                    + "ConcreteCreator();\n");
            desc.append(originalName + " " + uncapitalized + " = creator.create" + originalName
                    + "();\n");
            Rectangle rect = originalClassGo.getRectangle();
            originalClassGo.setRectangle(new Rectangle(rect.x, rect.y, 150, rect.height));
            DbSMSNoticeGo noteGo = util.createNoteAndGO(diagram, originalClassGo, desc.toString(),
                    7, -2);
            DbSMSNotice note = noteGo.getNotice();
        } // end for
    } // applyFactoryMethod()

    private void applyPrototype(DbObject[] semObjs, DbSMSClassifierGo go) throws DbException {
        JavaUtility util = JavaUtility.getSingleton();
        DbOODiagram diagram = (DbOODiagram) go.getCompositeOfType(DbOODiagram.metaClass);
        DbSMSLinkModel linkModel = util.getLinkModel(go);
        DbJVClassModel classModel = (DbJVClassModel) semObjs[0]
                .getCompositeOfType(DbJVClassModel.metaClass);
        DbJVClass cloneableClass = (DbJVClass) util.findReflectClass(classModel,
                "java.lang.Cloneable", UNKNOWN_JAVA_VERSION);

        for (int i = 0; i < semObjs.length; i++) {
            DbJVClass originalClass = (DbJVClass) semObjs[i];
            String originalName = originalClass.getName() + "Prototype";
            DbOOAdtGo originalClassGo = util.getGraphicalObject(originalClass, diagram);
            originalClass.setName(originalName);

            // Create Cloneable as superinterface
            DbOOAdtGo cloneableClassGo = new DbOOAdtGo(diagram, cloneableClass);
            util.moveGraphicalRep(cloneableClassGo, originalClassGo, 0, -2);
            DbJVInheritance inher = new DbJVInheritance(originalClass, cloneableClass);
            DbSMSInheritanceGo inherGo = new DbSMSInheritanceGo(diagram, originalClassGo,
                    cloneableClassGo, inher);

            // create duplicate method
            DbJVMethod duplicate = new DbJVMethod(originalClass);
            duplicate.setName("duplicate");
            duplicate.setVisibility(JVVisibility.getInstance(JVVisibility.PUBLIC));
            duplicate.setReturnType(originalClass);
            String body = "{\n" + "  " + originalName + " prototype = this.clone();\n"
                    + "  return prototype;\n" + "} //end duplicate()\n\n";
            duplicate.setBody(body);

            // create a note
            StringBuffer desc = new StringBuffer();
            String uncapitalized = util.uncapitalize(originalName);
            desc.append("Client code:\n");
            desc.append(originalName + " " + uncapitalized + "1 = new " + originalName + "();\n");
            desc.append(originalName + " " + uncapitalized + "2 = " + uncapitalized
                    + "1.duplicate();\n");
            DbSMSNoticeGo noteGo = util.createNoteAndGO(diagram, originalClassGo, desc.toString(),
                    4, -1);
            DbSMSNotice note = noteGo.getNotice();
        } // end for
    } // end applyPrototype()

    private void applyAdapter(DbObject[] semObjs, DbSMSClassifierGo go) throws DbException {
        DbOODiagram diagram = (DbOODiagram) go.getCompositeOfType(DbOODiagram.metaClass);
        JavaUtility util = JavaUtility.getSingleton();
        DbSMSLinkModel linkModel = util.getLinkModel(go);

        // Verify if all selected classes have at least one method
        DbJVClass inappropriateClass = null;
        for (int i = 0; i < semObjs.length; i++) {
            DbJVClass originalClass = (DbJVClass) semObjs[i];
            DbRelationN relN = originalClass.getComponents();
            DbEnumeration dbEnum = relN.elements(DbJVMethod.metaClass);
            boolean atLeastOneMethod = dbEnum.hasMoreElements();
            dbEnum.close();
            if (!atLeastOneMethod) {
                inappropriateClass = originalClass;
                break;
            }
        } // end for

        // Display error message, if Adapter not applicable
        if (inappropriateClass != null) {
            final String msg = MessageFormat
                    .format(
                            "The class {0} does not have any method.\nThe Adapter design pattern is applicable only on classes containing methods.",
                            new Object[] { inappropriateClass.getName() });
            final Component comp = MainFrame.getSingleton();
            JOptionPane.showMessageDialog(comp, msg);
            return;
        } // end if

        for (int i = 0; i < semObjs.length; i++) {
            DbJVClass originalClass = (DbJVClass) semObjs[i];
            String originalName = originalClass.getName();
            DbOOAdtGo originalClassGo = util.getGraphicalObject(originalClass, diagram);

            // Create an adapter class
            DbOOAdtGo adapterGo = util.createClassAndGO(diagram, originalClass, JVClassCategory
                    .getInstance(JVClassCategory.CLASS), 3, 0);
            DbJVClass adapter = (DbJVClass) adapterGo.getClassifier();
            adapter.setName(originalName + "Adapter");

            // Create adaptee field
            DbJVDataMember field = new DbJVDataMember(adapter);
            field.setName("adaptee");
            field.setType(originalClass);

            // Create adapter constructors
            StringBuffer params = new StringBuffer();
            DbRelationN relN = originalClass.getComponents();
            DbEnumeration dbEnum = relN.elements(DbJVConstructor.metaClass);
            if (!dbEnum.hasMoreElements()) {
                DbJVConstructor adapteeConstr = new DbJVConstructor(originalClass);
                DbJVConstructor constr = createAdapterConstructor(originalClass, adapter,
                        adapteeConstr, params);
                // use default constructor
            } else {
                while (dbEnum.hasMoreElements()) {
                    DbJVConstructor adapteeConstr = (DbJVConstructor) dbEnum.nextElement();
                    DbJVConstructor constr = createAdapterConstructor(originalClass, adapter,
                            adapteeConstr, params);
                } // end while
            }
            dbEnum.close();

            // link between adapter and adaptee
            DbSMSLink link = new DbSMSLink(linkModel);
            link.addToSourceObjects(adapter);
            link.addToTargetObjects(originalClass);
            DbSMSLinkGo linkGo = new DbSMSLinkGo(diagram, adapterGo, originalClassGo, link);

            // create a note
            StringBuffer desc = new StringBuffer();
            desc.append("Client code:\n");
            desc.append(originalName + "Adapter adapter = new " + originalName + "Adapter("
                    + params + ");\n");

            // For each adaptee method
            params = new StringBuffer();
            relN = originalClass.getComponents();
            dbEnum = relN.elements(DbJVMethod.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbJVMethod method = (DbJVMethod) dbEnum.nextElement();
                // store original name & body
                String originalMethodName = method.getName();
                String originalBody = method.getBody();

                // set adapter name & body
                String capitalized = util.capitalize(originalMethodName);
                method.setName("adapted" + capitalized);
                String body = getAdapterMethodBody(method, originalMethodName, capitalized, params);
                method.setBody(body);

                // clone the method
                SemanticalModel model = ApplicationContext.getSemanticalModel();
                model.paste(new DbJVMethod[] { method }, new DbJVClass[] { adapter }, "", true);

                // restore original name & body
                method.setName(originalMethodName);
                method.setBody(originalBody);

                // append to the note
                desc.append("adapter.adapted" + capitalized + "(" + params + ");\n");
            } // end while
            dbEnum.close();

            // finish the note
            Rectangle rect = originalClassGo.getRectangle();
            originalClassGo.setRectangle(new Rectangle(rect.x, rect.y, 150, rect.height));
            DbSMSNoticeGo noteGo = util.createNoteAndGO(diagram, originalClassGo, desc.toString(),
                    5, -2);
            DbSMSNotice note = noteGo.getNotice();
        } // end for
    } // end applyAdapter()

    private DbJVConstructor createAdapterConstructor(DbJVClass adaptee, DbJVClass adapter,
            DbJVConstructor adapteeConstr, StringBuffer params) throws DbException {
        DbJVConstructor constr = new DbJVConstructor(adapter);

        // copy each parameter
        DbRelationN relN = adapteeConstr.getComponents();
        DbEnumeration dbEnum = relN.elements(DbJVParameter.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVParameter adapteeParam = (DbJVParameter) dbEnum.nextElement();
            DbJVParameter adapterParam = new DbJVParameter(constr);
            adapterParam.setName(adapteeParam.getName());
            adapterParam.setType(adapteeParam.getType());
            params.append(adapteeParam.getName());

            if (dbEnum.hasMoreElements()) {
                params.append(", ");
            }
        } // end while
        dbEnum.close();

        String adapteeName = adaptee.getName();
        String body = "{\n" + "  " + adapteeName + " instance = new " + adapteeName + "(" + params
                + ");\n" + "  this.adaptee = instance;\n" + "} //end " + adapteeName
                + "Adapter()\n\n";
        constr.setBody(body);

        return constr;
    }

    private String getAdapterMethodBody(DbJVMethod method, String originalMethodName,
            String capitalized, StringBuffer params) throws DbException {
        StringBuffer body = new StringBuffer();
        body.append("{\n  ");

        DbOOAdt returnType = method.getReturnType();
        if (returnType != null) {
            body.append("return  ");
        }

        body.append("adaptee.");
        body.append(originalMethodName);
        body.append("(");
        DbRelationN relN = method.getComponents();
        DbEnumeration dbEnum = relN.elements(DbJVParameter.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVParameter param = (DbJVParameter) dbEnum.nextElement();
            String name = param.getName();
            params.append(name);

            if (dbEnum.hasMoreElements())
                params.append(", ");
        } // end while
        dbEnum.close();
        body.append(params);
        body.append(");\n");
        body.append(") //end adapted" + capitalized + "()\n\n");

        return body.toString();
    } // end getAdapterMethodBody()

    private void applyComposite(DbObject[] semObjs, DbSMSClassifierGo go) throws DbException {
        DbOODiagram diagram = (DbOODiagram) go.getCompositeOfType(DbOODiagram.metaClass);
        JavaUtility util = JavaUtility.getSingleton();
        DbSMSLinkModel linkModel = util.getLinkModel(go);

        for (int i = 0; i < semObjs.length; i++) {

            DbJVClass originalClass = (DbJVClass) semObjs[i];
            String originalName = originalClass.getName();
            originalClass.setName(originalName + "Leaf");
            DbOOAdtGo originalClassGo = util.getGraphicalObject(originalClass, diagram);

            // Create abstract creator
            DbOOAdtGo componentGo = util.createClassAndGO(diagram, originalClass, JVClassCategory
                    .getInstance(JVClassCategory.CLASS), 1, -3);
            DbJVClass component = (DbJVClass) componentGo.getClassifier();
            component.setName(originalName + "Component");

            // Create concrete creator
            DbOOAdtGo compositeGo = util.createClassAndGO(diagram, originalClass, JVClassCategory
                    .getInstance(JVClassCategory.CLASS), 4, 0);
            DbJVClass composite = (DbJVClass) compositeGo.getClassifier();
            composite.setName(originalName + "Composite");

            // Create inheritances
            DbJVInheritance inher = new DbJVInheritance(originalClass, component);
            DbSMSInheritanceGo inherGo = new DbSMSInheritanceGo(diagram, originalClassGo,
                    componentGo, inher);
            DbJVInheritance inher2 = new DbJVInheritance(composite, component);
            DbSMSInheritanceGo inherGo2 = new DbSMSInheritanceGo(diagram, compositeGo, componentGo,
                    inher2);

            // Create composition
            DbJVDataMember componentsField = new DbJVDataMember(composite);
            componentsField.setName("components");
            componentsField.setType(component);
            componentsField.setTypeUse("[]");
            DbJVDataMember compositeField = new DbJVDataMember(component);
            compositeField.setName("composite");
            compositeField.setType(composite);
            DbJVAssociation assoc = new DbJVAssociation(compositeField, SMSMultiplicity
                    .getInstance(SMSMultiplicity.OPTIONAL), componentsField, SMSMultiplicity
                    .getInstance(SMSMultiplicity.MANY));
            DbOOAssociationEnd assocEnd = assoc.getBackEnd();
            assocEnd.setAggregation(OOAggregation.getInstance(OOAggregation.COMPOSITE));
            DbOOAssociationGo assosGo = new DbOOAssociationGo(diagram, componentGo, compositeGo,
                    assoc);

            // create a note
            StringBuffer desc = new StringBuffer();
            desc.append("Client code:\n");
            desc.append("void process" + originalName + "Composite(" + originalName
                    + "Composite composite) {\n");
            desc.append(". " + originalName + "Component[] components = composite.components;\n");
            desc.append(". for (int i=0; i<components.length; i++) {\n");
            desc.append(".   " + originalName + "Component component = components[i];\n");
            desc.append(".   if (component instanceof " + originalName + "Composite) {\n");
            desc.append(".     " + originalName + "Composite composite = (" + originalName
                    + "Composite)component;\n");
            desc.append(".     process" + originalName + "Composite(composite); //recursion\n");
            desc.append(".   } else if (component instanceof " + originalName + "Leaf) {\n");
            desc.append(".     " + originalName + "Leaf leaf = (" + originalName
                    + "Leaf)component;\n");
            desc.append(".     process" + originalName + "Leaf(leaf);\n");
            desc.append(".   } //end if\n");
            desc.append(". } //end for\n");
            Rectangle rect = originalClassGo.getRectangle();
            originalClassGo.setRectangle(new Rectangle(rect.x, rect.y, 150, rect.height));
            DbSMSNoticeGo noteGo = util.createNoteAndGO(diagram, originalClassGo, desc.toString(),
                    6, -3);
            DbSMSNotice note = noteGo.getNotice();
        } // end for
    } // applyComposite()

    private void applyProxy(DbObject[] semObjs, DbSMSClassifierGo go) throws DbException {
        DbOODiagram diagram = (DbOODiagram) go.getCompositeOfType(DbOODiagram.metaClass);
        JavaUtility util = JavaUtility.getSingleton();
        DbSMSLinkModel linkModel = util.getLinkModel(go);

        for (int i = 0; i < semObjs.length; i++) {

            DbJVClass originalClass = (DbJVClass) semObjs[i];
            String originalName = originalClass.getName();
            originalClass.setName(originalName + "Service");
            DbOOAdtGo originalClassGo = util.getGraphicalObject(originalClass, diagram);

            // Create abstract service
            DbOOAdtGo abstractServiceGo = util.createClassAndGO(diagram, originalClass,
                    JVClassCategory.getInstance(JVClassCategory.INTERFACE), 2, -2);
            DbJVClass abstractService = (DbJVClass) abstractServiceGo.getClassifier();
            abstractService.setName("AbstractService");

            // Create concrete proxy
            DbOOAdtGo proxyGo = util.createClassAndGO(diagram, originalClass, JVClassCategory
                    .getInstance(JVClassCategory.CLASS), 4, 0);
            DbJVClass proxy = (DbJVClass) proxyGo.getClassifier();
            proxy.setName("ServiceProxy");

            // Add proxy contents
            DbJVDataMember field = new DbJVDataMember(proxy);
            field.setName("realService");
            field.setType(abstractService);

            DbJVConstructor constr = new DbJVConstructor(proxy);
            DbJVParameter param = new DbJVParameter(constr);
            param.setName("service");
            param.setType(abstractService);
            String body = "{\n" + "  this.realService = service;\n" + "} //end ServiceProxy()\n\n";
            constr.setBody(body);

            // Create inheritances
            DbJVInheritance inher = new DbJVInheritance(originalClass, abstractService);
            DbSMSInheritanceGo inherGo = new DbSMSInheritanceGo(diagram, originalClassGo,
                    abstractServiceGo, inher);
            DbJVInheritance inher2 = new DbJVInheritance(proxy, abstractService);
            DbSMSInheritanceGo inherGo2 = new DbSMSInheritanceGo(diagram, proxyGo,
                    abstractServiceGo, inher2);

            // Add request methods
            DbJVMethod method = new DbJVMethod(abstractService);
            method.setName("request");

            DbJVMethod method2 = new DbJVMethod(originalClass);
            method2.setName("request");

            DbJVMethod method3 = new DbJVMethod(proxy);
            method3.setName("request");
            body = "{\n" + "  realService.request();\n" + "} //end request()\n\n";
            method3.setBody(body);

            // create a note
            StringBuffer desc = new StringBuffer();
            desc.append("Client code:\n");
            desc
                    .append(originalName + "Service baseService = new " + originalName
                            + "Service();\n");
            desc.append("AbstractService proxyService = new AbstractService(baseService);\n");
            desc.append("proxyService.request();\n");
            Rectangle rect = originalClassGo.getRectangle();
            originalClassGo.setRectangle(new Rectangle(rect.x, rect.y, 150, rect.height));
            DbSMSNoticeGo noteGo = util.createNoteAndGO(diagram, originalClassGo, desc.toString(),
                    8, -3);
            DbSMSNotice note = noteGo.getNotice();
        } // end for
    } // end applyProxy

    private void applyCommand(DbObject[] semObjs, DbSMSClassifierGo go) throws DbException {
        DbOODiagram diagram = (DbOODiagram) go.getCompositeOfType(DbOODiagram.metaClass);
        JavaUtility util = JavaUtility.getSingleton();
        DbSMSLinkModel linkModel = util.getLinkModel(go);

        for (int i = 0; i < semObjs.length; i++) {
            DbJVClass originalClass = (DbJVClass) semObjs[i];
            String originalName = originalClass.getName();
            originalClass.setName(originalName + "Command");
            DbOOAdtGo originalClassGo = util.getGraphicalObject(originalClass, diagram);

            DbJVMethod method1 = new DbJVMethod(originalClass);
            method1.setName("execute");

            // Create abstract creator
            DbOOAdtGo componentGo = util.createClassAndGO(diagram, originalClass, JVClassCategory
                    .getInstance(JVClassCategory.INTERFACE), 2, -3);
            DbJVClass component = (DbJVClass) componentGo.getClassifier();
            component.setName("AbstractCommand");

            DbJVMethod method2 = new DbJVMethod(component);
            method2.setName("execute");
            method2.setAbstract(Boolean.TRUE);

            // Create concrete creator
            DbOOAdtGo compositeGo = util.createClassAndGO(diagram, originalClass, JVClassCategory
                    .getInstance(JVClassCategory.CLASS), 4, 0);
            DbJVClass composite = (DbJVClass) compositeGo.getClassifier();
            composite.setName("MetaCommand");

            // Concrete constructor
            DbJVConstructor constr = new DbJVConstructor(composite);
            DbJVParameter param = new DbJVParameter(constr);
            param.setName("subcommands");
            param.setType(component);
            param.setTypeUse("[]");
            String constrBody = "{\n" + "  this.subcommands = subcommands;\n"
                    + "} //end MetaCommand()\n\n";
            constr.setBody(constrBody);

            // Concrete aggregate execute
            DbJVMethod method3 = new DbJVMethod(composite);
            method3.setName("execute");
            String method3Body = "{\n" + "  for(int i=0; i<subcommands.length; i++) {\n"
                    + "    subcommands[i].execute();\n" + "  } //end for\n"
                    + "} //end execute()\n\n";
            method3.setBody(method3Body);

            // Create inheritances
            DbJVInheritance inher = new DbJVInheritance(originalClass, component);
            DbSMSInheritanceGo inherGo = new DbSMSInheritanceGo(diagram, originalClassGo,
                    componentGo, inher);
            DbJVInheritance inher2 = new DbJVInheritance(composite, component);
            DbSMSInheritanceGo inherGo2 = new DbSMSInheritanceGo(diagram, compositeGo, componentGo,
                    inher2);

            // Create aggregation
            DbJVDataMember componentsField = new DbJVDataMember(composite);
            componentsField.setName("subcommands");
            componentsField.setType(component);
            componentsField.setTypeUse("[]");
            DbJVDataMember compositeField = new DbJVDataMember(component);
            DbJVAssociation assoc = new DbJVAssociation(compositeField, SMSMultiplicity
                    .getInstance(SMSMultiplicity.OPTIONAL), componentsField, SMSMultiplicity
                    .getInstance(SMSMultiplicity.ONE_OR_MORE));
            DbOOAssociationEnd backEnd = assoc.getBackEnd();
            backEnd.setAggregation(OOAggregation.getInstance(OOAggregation.AGGREGATE));
            DbOOAssociationEnd frontEnd = assoc.getFrontEnd();
            frontEnd.setNavigable(Boolean.FALSE);
            DbOOAssociationGo assosGo = new DbOOAssociationGo(diagram, componentGo, compositeGo,
                    assoc);

            // create a note
            StringBuffer desc = new StringBuffer();
            desc.append("Client code:\n");
            desc.append("AbstractCommand baseCommand = new " + originalName + "Command();\n");
            desc.append("MetaCommand metaCommand =\n");
            desc.append("  new MetaCommand(new AbstractCommand[] {baseCommand));\n");
            desc.append("metaCommand.execute(); //invokes baseCommand.execute()\n");
            Rectangle rect = originalClassGo.getRectangle();
            originalClassGo.setRectangle(new Rectangle(rect.x, rect.y, 150, rect.height));
            DbSMSNoticeGo noteGo = util.createNoteAndGO(diagram, originalClassGo, desc.toString(),
                    8, -4);
            DbSMSNotice note = noteGo.getNotice();
        } // end for
    } // applyCommand()

    private void applyChainOfResponsability(DbObject[] semObjs, DbSMSClassifierGo go)
            throws DbException {
        DbOODiagram diagram = (DbOODiagram) go.getCompositeOfType(DbOODiagram.metaClass);
        JavaUtility util = JavaUtility.getSingleton();
        DbSMSLinkModel linkModel = util.getLinkModel(go);
        DbJVClassModel classModel = (DbJVClassModel) semObjs[0]
                .getCompositeOfType(DbJVClassModel.metaClass);

        for (int i = 0; i < semObjs.length; i++) {
            DbJVClass originalClass = (DbJVClass) semObjs[i];
            String originalName = originalClass.getName();
            originalClass.setName(originalName + "Handler");
            DbOOAdtGo originalClassGo = util.getGraphicalObject(originalClass, diagram);

            // Create abstract handler
            DbOOAdtGo abstractHandlerGo = util.createClassAndGO(diagram, originalClass,
                    JVClassCategory.getInstance(JVClassCategory.INTERFACE), 3, -2);
            DbJVClass abstractHandler = (DbJVClass) abstractHandlerGo.getClassifier();
            abstractHandler.setName("Handler");

            DbJVMethod method1 = new DbJVMethod(abstractHandler);
            method1.setName("handleMessage");
            method1.setAbstract(Boolean.TRUE);

            // Create inheritances
            DbJVInheritance inher = new DbJVInheritance(originalClass, abstractHandler);
            DbSMSInheritanceGo inherGo = new DbSMSInheritanceGo(diagram, originalClassGo,
                    abstractHandlerGo, inher);

            // Create association
            DbJVDataMember successorField = new DbJVDataMember(originalClass);
            successorField.setName("successorHandler");
            successorField.setType(abstractHandler);
            DbJVDataMember dummyField = new DbJVDataMember(originalClass);
            DbJVAssociation assoc = new DbJVAssociation(dummyField, SMSMultiplicity
                    .getInstance(SMSMultiplicity.OPTIONAL), successorField, SMSMultiplicity
                    .getInstance(SMSMultiplicity.OPTIONAL));
            DbOOAssociationEnd backEnd = assoc.getBackEnd();
            backEnd.setAggregation(OOAggregation.getInstance(OOAggregation.NONE));
            DbOOAssociationEnd frontEnd = assoc.getFrontEnd();
            frontEnd.setNavigable(Boolean.FALSE);
            DbOOAssociationGo assosGo = new DbOOAssociationGo(diagram, abstractHandlerGo,
                    originalClassGo, assoc);

            // create concrete handler constructor
            DbJVConstructor constr = new DbJVConstructor(originalClass);
            DbJVParameter param = new DbJVParameter(constr);
            param.setName("successor");
            param.setType(abstractHandler);
            String constrBody = "{\n" + "  this.successorHandler = successor;\n" + "} //end "
                    + originalName + "Handler()\n\n";
            constr.setBody(constrBody);

            // create concrete handler methods
            DbJVMethod method2 = new DbJVMethod(originalClass);
            method2.setName("handleMessage");
            String body = "{\n" + "  if (canHandle()) {\n" + "    handlerMethod();\n"
                    + "  } else {\n" + "    if (successorHandler != null)\n"
                    + "      successorHandler.handleMessage();\n" + "  } //end if\n"
                    + "} //end handleMessage()\n\n";
            method2.setBody(body);

            DbJVMethod method3 = new DbJVMethod(originalClass);
            method3.setName("canHandle");
            method3.setVisibility(JVVisibility.getInstance(JVVisibility.PRIVATE));
            DbOOAdt booleanType = util.findBuiltinType(classModel, "boolean");
            method3.setReturnType(booleanType);
            body = "{\n" + "  return true;\n" + "} //end canHandle()\n\n";
            method3.setBody(body);

            DbJVMethod method4 = new DbJVMethod(originalClass);
            method4.setName("handlerMethod");
            method4.setVisibility(JVVisibility.getInstance(JVVisibility.PRIVATE));

            // create a note
            StringBuffer desc = new StringBuffer();
            desc.append("Client code:\n");
            desc.append("Handler successor = new " + originalName + "Handler();\n");
            desc.append("Handler handler = new " + originalName + "Handler(successor);\n");
            desc.append("handler.handleMessage();\n");
            Rectangle rect = originalClassGo.getRectangle();
            originalClassGo.setRectangle(new Rectangle(rect.x, rect.y, 150, rect.height));
            DbSMSNoticeGo noteGo = util.createNoteAndGO(diagram, originalClassGo, desc.toString(),
                    6, 0);
            DbSMSNotice note = noteGo.getNotice();
        } // end for
    } // end applyChainOfResponsability()

    private void applyIterator(DbObject[] semObjs, DbSMSClassifierGo go) throws DbException {
        DbOODiagram diagram = (DbOODiagram) go.getCompositeOfType(DbOODiagram.metaClass);
        JavaUtility util = JavaUtility.getSingleton();
        DbSMSLinkModel linkModel = util.getLinkModel(go);
        DbJVClassModel classModel = (DbJVClassModel) semObjs[0]
                .getCompositeOfType(DbJVClassModel.metaClass);
        DbJVClass iteratorClass = (DbJVClass) util.findReflectClass(classModel,
                "java.util.Iterator", UNKNOWN_JAVA_VERSION);
        DbJVClass collectionClass = (DbJVClass) util.findReflectClass(classModel,
                "java.util.Collection", UNKNOWN_JAVA_VERSION);
        DbJVClass objectClass = (DbJVClass) util.findReflectClass(classModel, "java.lang.Object",
                UNKNOWN_JAVA_VERSION);
        DbOOAdt booleanType = util.findBuiltinType(classModel, "boolean");

        for (int i = 0; i < semObjs.length; i++) {
            DbJVClass originalClass = (DbJVClass) semObjs[i];
            String originalName = originalClass.getName();
            DbOOAdtGo originalClassGo = util.getGraphicalObject(originalClass, diagram);
            DbOOAdtGo iteratorClassGo = new DbOOAdtGo(diagram, iteratorClass);
            util.moveGraphicalRep(iteratorClassGo, originalClassGo, 4, -3);

            // create iterator
            DbOOAdtGo iteratorGo = util.createClassAndGO(diagram, originalClass, JVClassCategory
                    .getInstance(JVClassCategory.CLASS), 4, 0);
            DbJVClass iterator = (DbJVClass) iteratorGo.getClassifier();
            iterator.setName(originalName + "Iterator");

            DbJVDataMember field = new DbJVDataMember(iterator);
            field.setName("collection");

            field.setType(collectionClass);

            DbJVConstructor constr = new DbJVConstructor(iterator);
            DbJVParameter param = new DbJVParameter(constr);
            param.setName("collection");
            param.setType(collectionClass);
            String constrBody = "{\n" + "  this.collection = collection;\n" + "} //end "
                    + originalName + "Iterator()\n\n";
            constr.setBody(constrBody);

            DbJVMethod method1 = new DbJVMethod(iterator);
            method1.setName("next");
            method1.setReturnType(objectClass);
            String body = "{\n" + "  return collection.getIterator().next();\n"
                    + "} //end next()\n\n";
            method1.setBody(body);

            DbJVMethod method2 = new DbJVMethod(iterator);
            method2.setName("hasNext");
            method2.setReturnType(booleanType);
            body = "{\n" + "  return collection.getIterator().hasNext();\n" + "} //end next()\n\n";
            method2.setBody(body);

            DbJVMethod method3 = new DbJVMethod(iterator);
            method3.setName("remove");
            body = "{\n" + "  collection.getIterator().remove();\n" + "} //end next()\n\n";
            method3.setBody(body);

            // create inheritance
            DbJVInheritance inher = new DbJVInheritance(iterator, iteratorClass);
            DbSMSInheritanceGo inherGo = new DbSMSInheritanceGo(diagram, iteratorGo,
                    iteratorClassGo, inher);

            // create collection
            DbOOAdtGo collectionGo = util.createClassAndGO(diagram, originalClass, JVClassCategory
                    .getInstance(JVClassCategory.CLASS), 1, -2);
            DbJVClass collection = (DbJVClass) collectionGo.getClassifier();
            collection.setName(originalName + "Collection");

            DbJVMethod method4 = new DbJVMethod(collection);
            method4.setName("getIterator");
            method4.setReturnType(iteratorClass);
            body = "{\n" + "  return new " + originalName + "Iterator(elements);\n"
                    + "} //end getIterator()\n\n";
            method4.setBody(body);

            // Create aggregation
            DbJVDataMember componentsField = new DbJVDataMember(collection);
            componentsField.setName("elements");
            componentsField.setType(collectionClass);
            componentsField.setElementType(originalClass);
            componentsField.setInitialValue("new ArrayList()");
            DbJVDataMember compositeField = new DbJVDataMember(originalClass);
            DbJVAssociation assoc = new DbJVAssociation(compositeField, SMSMultiplicity
                    .getInstance(SMSMultiplicity.EXACTLY_ONE), componentsField, SMSMultiplicity
                    .getInstance(SMSMultiplicity.MANY));
            DbOOAssociationEnd backEnd = assoc.getBackEnd();
            backEnd.setAggregation(OOAggregation.getInstance(OOAggregation.AGGREGATE));
            DbOOAssociationEnd frontEnd = assoc.getFrontEnd();
            frontEnd.setNavigable(Boolean.FALSE);
            componentsField.setTypeUse("");
            DbOOAssociationGo assosGo = new DbOOAssociationGo(diagram, originalClassGo,
                    collectionGo, assoc);

            DbJVMethod method5 = new DbJVMethod(collection);
            method5.setName("add");
            DbJVParameter param5 = new DbJVParameter(method5);
            param5.setName("element");
            param5.setType(originalClass);
            body = "{\n" + "  collection.add(element);\n" + "} //end add()\n\n";
            method5.setBody(body);

            DbJVMethod method6 = new DbJVMethod(collection);
            method6.setName("remove");
            DbJVParameter param6 = new DbJVParameter(method6);
            param6.setName("element");
            param6.setType(originalClass);
            body = "{\n" + "  collection.remove(element);\n" + "} //end remove()\n\n";
            method6.setBody(body);

            // link between collection and iterator
            DbSMSLink link = new DbSMSLink(linkModel);
            link.addToSourceObjects(collection);
            link.addToTargetObjects(iterator);
            DbSMSLinkGo linkGo = new DbSMSLinkGo(diagram, collectionGo, iteratorGo, link);
            linkGo.setRightAngle(Boolean.FALSE);

            // create a note
            StringBuffer desc = new StringBuffer();
            desc.append("Client code:\n");
            desc.append(originalName + "Collection collection = new " + originalName
                    + "Collection();\n");
            desc.append(originalName + " element1 = new " + originalName + "();\n");
            desc.append("collection.add(element1);\n");
            desc.append("...\n");
            desc.append("Iterator iter = collection.getIterator();\n");
            desc.append("while (iter.hasNext()) {\n");
            desc.append(". " + originalName + " element = (" + originalName + ")iter.next();\n");
            desc.append("} //end while");
            Rectangle rect = originalClassGo.getRectangle();
            originalClassGo.setRectangle(new Rectangle(rect.x, rect.y, 150, rect.height));
            DbSMSNoticeGo noteGo = util.createNoteAndGO(diagram, originalClassGo, desc.toString(),
                    8, -3);
            DbSMSNotice note = noteGo.getNotice();
        } // end for
    } // end applyIterator

    private void applyObserver(DbObject[] semObjs, DbSMSClassifierGo go) throws DbException {
        DbOODiagram diagram = (DbOODiagram) go.getCompositeOfType(DbOODiagram.metaClass);
        JavaUtility util = JavaUtility.getSingleton();
        DbSMSLinkModel linkModel = util.getLinkModel(go);
        DbJVClassModel classModel = (DbJVClassModel) semObjs[0]
                .getCompositeOfType(DbJVClassModel.metaClass);
        DbJVClass eventListenerClass = (DbJVClass) util.findReflectClass(classModel,
                "java.util.EventListener", UNKNOWN_JAVA_VERSION);
        DbJVClass eventObjectClass = (DbJVClass) util.findReflectClass(classModel,
                "java.util.EventObject", UNKNOWN_JAVA_VERSION);
        DbJVClass objectClass = (DbJVClass) util.findReflectClass(classModel, "java.lang.Object",
                UNKNOWN_JAVA_VERSION);

        for (int i = 0; i < semObjs.length; i++) {
            DbJVClass originalClass = (DbJVClass) semObjs[i];
            String originalName = originalClass.getName();
            originalClass.setName(originalName + "Event");
            DbOOAdtGo originalClassGo = util.getGraphicalObject(originalClass, diagram);

            // create eventObject g.o.
            DbOOAdtGo eventObjectClassGo = new DbOOAdtGo(diagram, eventObjectClass);
            util.moveGraphicalRep(eventObjectClassGo, originalClassGo, 0, -2);

            // create inheritance
            DbJVInheritance inher = new DbJVInheritance(originalClass, eventObjectClass);
            DbSMSInheritanceGo inherGo = new DbSMSInheritanceGo(diagram, originalClassGo,
                    eventObjectClassGo, inher);

            // create eventListener g.o.
            DbOOAdtGo eventListenerClassGo = new DbOOAdtGo(diagram, eventListenerClass);
            util.moveGraphicalRep(eventListenerClassGo, originalClassGo, 4, -2);

            // create listener
            DbOOAdtGo listenerGo = util.createClassAndGO(diagram, originalClass, JVClassCategory
                    .getInstance(JVClassCategory.INTERFACE), 4, 0);
            DbJVClass listener = (DbJVClass) listenerGo.getClassifier();
            listener.setName(originalName + "Listener");

            DbJVMethod method = new DbJVMethod(listener);
            String uncapitalized = util.uncapitalize(originalName);
            method.setName(uncapitalized + "Performed");

            DbJVParameter param = new DbJVParameter(method);
            param.setName("event");
            param.setType(originalClass);

            // create inheritance2
            DbJVInheritance inher2 = new DbJVInheritance(listener, eventListenerClass);
            DbSMSInheritanceGo inher2Go = new DbSMSInheritanceGo(diagram, listenerGo,
                    eventListenerClassGo, inher2);

            // create listenable
            DbOOAdtGo listenableGo = util.createClassAndGO(diagram, originalClass, JVClassCategory
                    .getInstance(JVClassCategory.INTERFACE), 2, 2);
            DbJVClass listenable = (DbJVClass) listenableGo.getClassifier();
            listenable.setName(originalName + "Listenable");

            DbJVMethod method2 = new DbJVMethod(listenable);
            method2.setName("add" + originalName + "Listener");
            method2.setSynchronized(Boolean.TRUE);

            DbJVParameter param2 = new DbJVParameter(method2);
            param2.setName(uncapitalized + "Listener");
            param2.setType(listener);

            DbJVMethod method3 = new DbJVMethod(listenable);
            method3.setName("remove" + originalName + "Listener");
            method3.setSynchronized(Boolean.TRUE);

            DbJVParameter param3 = new DbJVParameter(method3);
            param3.setName(uncapitalized + "Listener");
            param3.setType(listener);

            // add members to event
            DbJVDataMember field = new DbJVDataMember(originalClass);
            field.setName("source");
            field.setType(listenable);

            DbJVConstructor constr = new DbJVConstructor(originalClass);
            String body = "{\n" + "  this.source = source;\n" + "} //end " + originalName
                    + "Event()\n\n";
            constr.setBody(body);

            DbJVParameter param4 = new DbJVParameter(constr);
            param4.setName("source");
            param4.setType(listenable);

            DbJVMethod method4 = new DbJVMethod(originalClass);
            method4.setName("getSource");
            method4.setReturnType(objectClass);
            body = "{\n" + "  return source;\n" + "} //end getSource()\n\n";
            method4.setBody(body);

            // link between listener and event
            DbSMSLink link = new DbSMSLink(linkModel);
            link.addToSourceObjects(listener);
            link.addToTargetObjects(originalClass);
            DbSMSLinkGo linkGo = new DbSMSLinkGo(diagram, listenerGo, originalClassGo, link);
            // linkGo.setRightAngle(Boolean.FALSE);

            // link between listenable and listener
            DbSMSLink link2 = new DbSMSLink(linkModel);
            link2.addToSourceObjects(listenable);
            link2.addToTargetObjects(listener);
            DbSMSLinkGo link2Go = new DbSMSLinkGo(diagram, listenableGo, listenerGo, link2);

            // create a note
            StringBuffer desc = new StringBuffer();
            desc.append("Client code:\n");
            desc.append("Widget widget = new Widget(); //implements " + originalName
                    + "Listenable\n");
            desc.append("widget.add" + originalName + "Listener(new " + originalName
                    + "Listener() {\n");
            desc
                    .append(". public " + uncapitalized + "Performed(" + originalName
                            + "Event ev) {\n");
            desc.append(".   Object src = ev.getSource();\n");
            desc.append(". } //end\n");
            desc.append("});\n");
            Rectangle rect = originalClassGo.getRectangle();
            originalClassGo.setRectangle(new Rectangle(rect.x, rect.y, 150, rect.height));
            DbSMSNoticeGo noteGo = util.createNoteAndGO(diagram, originalClassGo, desc.toString(),
                    9, 1);
            DbSMSNotice note = noteGo.getNotice();
        } // end for
    } // end applyObserver()

    private void applyState(DbObject[] semObjs, DbSMSClassifierGo go) throws DbException {
        DbOODiagram diagram = (DbOODiagram) go.getCompositeOfType(DbOODiagram.metaClass);
        JavaUtility util = JavaUtility.getSingleton();
        DbSMSLinkModel linkModel = util.getLinkModel(go);

        // Verify if all selected classes have at least one instance field
        DbJVClass inappropriateClass = null;
        for (int i = 0; i < semObjs.length; i++) {
            DbJVClass originalClass = (DbJVClass) semObjs[i];
            DbRelationN relN = originalClass.getComponents();
            DbEnumeration dbEnum = relN.elements(DbJVDataMember.metaClass);
            boolean atLeastOneInstanceField = false;
            while (dbEnum.hasMoreElements()) {
                DbJVDataMember field = (DbJVDataMember) dbEnum.nextElement();
                if (!field.isStatic()) {
                    atLeastOneInstanceField = true;
                    break;
                }
            } // end while
            dbEnum.close();

            if (!atLeastOneInstanceField) {
                inappropriateClass = originalClass;
                break;
            }
        } // end for

        // Display error message, if State not applicable
        if (inappropriateClass != null) {
            final String msg = MessageFormat
                    .format(
                            "The class {0} does not have any field, this is a stateless class.\nThe State design pattern is applicable only on stateful classes.",
                            new Object[] { inappropriateClass.getName() });
            final Component comp = MainFrame.getSingleton();
            JOptionPane.showMessageDialog(comp, msg);
            return;
        } // end if

        for (int i = 0; i < semObjs.length; i++) {
            DbJVClass originalClass = (DbJVClass) semObjs[i];
            String originalName = originalClass.getName();
            originalClass.setName("Stateful" + originalName);
            DbOOAdtGo originalClassGo = util.getGraphicalObject(originalClass, diagram);

            // create state class
            DbOOAdtGo stateClassGo = util.createClassAndGO(diagram, originalClass, JVClassCategory
                    .getInstance(JVClassCategory.CLASS), 3, 0);
            DbJVClass stateClass = (DbJVClass) stateClassGo.getClassifier();
            stateClass.setName(originalName + "State");

            // move each instance field in state class
            DbRelationN relN = originalClass.getComponents();
            DbEnumeration dbEnum = relN.elements(DbJVDataMember.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbJVDataMember field = (DbJVDataMember) dbEnum.nextElement();
                if (!field.isStatic()) {
                    field.setComposite(stateClass);
                    field.setVisibility(JVVisibility.getInstance(JVVisibility.PROTECTED));
                } // end if
            } // end while
            dbEnum.close();

            // create a state field
            DbJVDataMember field = new DbJVDataMember(originalClass);
            field.setName("state");
            field.setType(stateClass);
            field.setInitialValue("new " + originalName + "State()");

            // and its getter method
            DbJVMethod method = new DbJVMethod(originalClass);
            method.setName("getState");
            method.setReturnType(stateClass);
            String body = "{\n" + "  return state;\n" + "} //end getState()\n\n";
            method.setBody(body);

            // link between state and stateful class
            DbSMSLink link = new DbSMSLink(linkModel);
            link.addToSourceObjects(originalClass);
            link.addToTargetObjects(stateClass);
            DbSMSLinkGo linkGo = new DbSMSLinkGo(diagram, originalClassGo, stateClassGo, link);

            // create inner classes
            DbJVClass initStateClass = new DbJVClass(stateClass, JVClassCategory
                    .getInstance(JVClassCategory.CLASS));
            initStateClass.setName("InitialState");
            initStateClass.setVisibility(JVVisibility.getInstance(JVVisibility.PRIVATE));
            initStateClass.setStatic(Boolean.TRUE);
            DbJVInheritance inher = new DbJVInheritance(initStateClass, stateClass);

            DbJVClass finalStateClass = new DbJVClass(stateClass, JVClassCategory
                    .getInstance(JVClassCategory.CLASS));
            finalStateClass.setName("FinalState");
            finalStateClass.setVisibility(JVVisibility.getInstance(JVVisibility.PRIVATE));
            finalStateClass.setStatic(Boolean.TRUE);
            DbJVInheritance inher2 = new DbJVInheritance(finalStateClass, stateClass);

            // create init field
            DbJVDataMember field2 = new DbJVDataMember(stateClass);
            field2.setName("initialState");
            field2.setType(initStateClass);
            field2.setInitialValue(originalName + "State.initiate()");

            // create init method
            DbJVMethod method2 = new DbJVMethod(stateClass);
            method2.setName("initiate");
            method2.setStatic(Boolean.TRUE);
            method2.setReturnType(initStateClass);
            body = "{\n" + "  if (initialState == null)\n"
                    + "    initialState = new InitialState();\n" + "  \n"
                    + "  return initialState;\n" + "} //end initiate()\n\n";
            method2.setBody(body);

            // create a note
            StringBuffer desc = new StringBuffer();
            desc.append("Client code:\n");
            desc.append("Stateful" + originalName + " instance = new Stateful" + originalName
                    + "();\n");
            desc.append(originalName + "State state = instance.getState();\n");
            Rectangle rect = originalClassGo.getRectangle();
            originalClassGo.setRectangle(new Rectangle(rect.x, rect.y, 150, rect.height));
            DbSMSNoticeGo noteGo = util.createNoteAndGO(diagram, originalClassGo, desc.toString(),
                    5, -2);
            DbSMSNotice note = noteGo.getNotice();
        } // end for
    } // end applyState()

    private void applyStrategy(DbObject[] semObjs, DbSMSClassifierGo go) throws DbException {
        DbOODiagram diagram = (DbOODiagram) go.getCompositeOfType(DbOODiagram.metaClass);
        JavaUtility util = JavaUtility.getSingleton();
        DbSMSLinkModel linkModel = util.getLinkModel(go);

        for (int i = 0; i < semObjs.length; i++) {
            DbJVClass originalClass = (DbJVClass) semObjs[i];
            String originalName = originalClass.getName();
            originalClass.setName(originalName + "Strategy");
            DbOOAdtGo originalClassGo = util.getGraphicalObject(originalClass, diagram);

            // create abstract strategy
            DbOOAdtGo abstractStrategyGo = util.createClassAndGO(diagram, originalClass,
                    JVClassCategory.getInstance(JVClassCategory.INTERFACE), 0, -2);
            DbJVClass abstractStrategy = (DbJVClass) abstractStrategyGo.getClassifier();
            abstractStrategy.setName("AbstractStrategy");

            // create inheritances
            DbJVInheritance inher = new DbJVInheritance(originalClass, abstractStrategy);
            DbSMSInheritanceGo inherGo = new DbSMSInheritanceGo(diagram, originalClassGo,
                    abstractStrategyGo, inher);

            // create abstract and concrete method
            DbJVMethod method = new DbJVMethod(abstractStrategy);
            method.setName("useStrategy");

            DbJVMethod method2 = new DbJVMethod(originalClass);
            method2.setName("useStrategy");

            // create context and its contents
            DbOOAdtGo contextGo = util.createClassAndGO(diagram, originalClass, JVClassCategory
                    .getInstance(JVClassCategory.CLASS), 3, -2);
            DbJVClass context = (DbJVClass) contextGo.getClassifier();
            context.setName("Context");

            DbJVDataMember field = new DbJVDataMember(context);
            field.setName("strategy");
            field.setType(abstractStrategy);

            DbJVConstructor constr = new DbJVConstructor(context);
            DbJVParameter param = new DbJVParameter(constr);
            param.setName("strategy");
            param.setType(abstractStrategy);
            String body = "{\n" + "  this.strategy = strategy;\n" + "} //end Context()\n\n";
            constr.setBody(body);

            DbJVMethod method3 = new DbJVMethod(context);
            method3.setName("perform");
            body = "{\n" + "  strategy.useStrategy();\n" + "} //end perform()\n\n";
            method3.setBody(body);

            // link between context and abstract strategy
            DbSMSLink link = new DbSMSLink(linkModel);
            link.addToSourceObjects(context);
            link.addToTargetObjects(abstractStrategy);
            DbSMSLinkGo linkGo = new DbSMSLinkGo(diagram, contextGo, abstractStrategyGo, link);

            // create a note
            StringBuffer desc = new StringBuffer();
            desc.append("Client code:\n");
            desc.append("AbstractStrategy strategy = new " + originalName + "Strategy();\n");
            desc.append("Contect context = new Context(strategy);\n");
            desc.append("context.perform(); //apply the strategy");
            Rectangle rect = originalClassGo.getRectangle();
            originalClassGo.setRectangle(new Rectangle(rect.x, rect.y, 150, rect.height));
            DbSMSNoticeGo noteGo = util.createNoteAndGO(diagram, originalClassGo, desc.toString(),
                    5, 0);
            DbSMSNotice note = noteGo.getNotice();
        } // end for
    } // end applyStrategy()

    private void applyTemplateMethod(DbObject[] semObjs, DbSMSClassifierGo go) throws DbException {
        DbOODiagram diagram = (DbOODiagram) go.getCompositeOfType(DbOODiagram.metaClass);
        JavaUtility util = JavaUtility.getSingleton();
        DbSMSLinkModel linkModel = util.getLinkModel(go);

        for (int i = 0; i < semObjs.length; i++) {
            DbJVClass originalClass = (DbJVClass) semObjs[i];
            String originalName = originalClass.getName();
            DbOOAdtGo originalClassGo = util.getGraphicalObject(originalClass, diagram);

            // create abstract class
            DbOOAdtGo abstractClassGo = util.createClassAndGO(diagram, originalClass,
                    JVClassCategory.getInstance(JVClassCategory.CLASS), 0, -2);
            DbJVClass abstractClass = (DbJVClass) abstractClassGo.getClassifier();
            abstractClass.setAbstract(Boolean.TRUE);
            abstractClass.setName("Abstract" + originalName);

            // create inheritances
            DbJVInheritance inher = new DbJVInheritance(originalClass, abstractClass);
            DbSMSInheritanceGo inherGo = new DbSMSInheritanceGo(diagram, originalClassGo,
                    abstractClassGo, inher);

            // create abstract and concrete method
            DbJVMethod method = new DbJVMethod(abstractClass);
            method.setName("primitiveOperation");
            method.setAbstract(Boolean.TRUE);
            method.setVisibility(JVVisibility.getInstance(JVVisibility.PROTECTED));

            DbJVMethod method2 = new DbJVMethod(originalClass);
            method2.setName("primitiveOperation");
            method2.setVisibility(JVVisibility.getInstance(JVVisibility.PROTECTED));

            DbJVMethod method3 = new DbJVMethod(abstractClass);
            method3.setName("templateMethod");
            String body = "{\n" + "  primitiveOperation();\n" + "} //end templateMethod()\n\n";
            method3.setBody(body);

            // create a note
            StringBuffer desc = new StringBuffer();
            desc.append("Client code:\n");
            desc.append("Abstract" + originalName + " instance = new " + originalName + "();\n");
            desc.append("instance.templateMethod();\n");
            Rectangle rect = originalClassGo.getRectangle();
            originalClassGo.setRectangle(new Rectangle(rect.x, rect.y, 150, rect.height));
            DbSMSNoticeGo noteGo = util.createNoteAndGO(diagram, originalClassGo, desc.toString(),
                    3, -2);
            DbSMSNotice note = noteGo.getNotice();
        } // end for
    } // end applyStrategy()
} // end ApplyPatternAction()
