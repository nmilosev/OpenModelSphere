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
package org.modelsphere.sms.oo.java.features;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSInheritanceGo;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlConstraint;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.db.util.Extensibility;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOOAdtGo;
import org.modelsphere.sms.oo.db.DbOOAssociation;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.db.DbOOAssociationGo;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.java.db.DbJVAssociation;
import org.modelsphere.sms.oo.java.db.DbJVAssociationEnd;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVInheritance;
import org.modelsphere.sms.oo.java.db.DbJVPrimitiveType;
import org.modelsphere.sms.oo.java.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORAllowableValue;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORAssociationGo;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTableGo;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.srtypes.ORDomainCategory;
import org.modelsphere.sms.or.db.util.AnyORObject;

public final class JavaToDataModelConversion {

    private static final String kJavaTypes = LocaleMgr.misc.getString("javaTypes");

    private DbObject m_parent;
    private JavaToDataModelParameters m_parameters;
    private DbORDataModel m_orModel;
    private Map<DbJVClass, DbORTable> m_classMap = new HashMap<DbJVClass, DbORTable>();
    private Map<DbOOAssociation, List<ORAssociationData>> m_associations = new HashMap<DbOOAssociation, List<ORAssociationData>>();
    private Map<DbSMSInheritanceGo, DbORAssociation> m_inheritances = new HashMap<DbSMSInheritanceGo, DbORAssociation>();

    private MetaClass[] metaClasses;
    private DbORDomainModel domainModel = null;

    public JavaToDataModelConversion(DbObject parent, JavaToDataModelParameters parameters) {
        m_parameters = parameters;
        m_parent = parent;
    }

    public void convert(DbOOAbsPackage ooPackage) throws DbException {

        m_orModel = AnyORObject.createDataModel(m_parent, m_parameters.getTargetSystem());
        m_orModel.setName(ooPackage.getName());
        int rootID = AnyORObject.getRootIDFromTargetSystem(m_parameters.getTargetSystem());
        metaClasses = AnyORObject.getTargetMetaClasses(rootID);

        //pass 1: create tables and columns
        DbEnumeration dbEnum = ooPackage.getComponents().elements(DbJVClass.metaClass);

        //for each class of the package
        while (dbEnum.hasMoreElements()) {
            DbJVClass clas = (DbJVClass) dbEnum.nextElement();
            if (clas.isInterface())
                continue;

            if (isEnumeration(clas)) {
                convertEnumerationToDomain(clas);
            } else if (!isDataType(clas)) {
                convertClassToTable(clas);
            }
        } //end while
        dbEnum.close();

        //pass 2: create association between tables
        dbEnum = ooPackage.getComponents().elements(DbJVClass.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVClass clas = (DbJVClass) dbEnum.nextElement();
            DbORTable table = m_classMap.get(clas);
            convertAssociations(clas, table);
        } //end while
        dbEnum.close();

        //pass 3: create diagrams, if asked by the user
        if (m_parameters.generateDiagrams) {
            dbEnum = ooPackage.getComponents().elements(DbOODiagram.metaClass);
            DbORDiagram firstDiagram = null;

            while (dbEnum.hasMoreElements()) {
                DbOODiagram ooDiagram = (DbOODiagram) dbEnum.nextElement();
                DbORDiagram orDiagram = convertDiagram(ooDiagram);
                if (firstDiagram == null) {
                    firstDiagram = orDiagram;
                }
            } //while
            dbEnum.close();

            if (firstDiagram != null) {
                showAndReveal(firstDiagram);
            }
        } //end if
    } //end convert()

    private void convertEnumerationToDomain(DbJVClass clas) throws DbException {
        String name = clas.getName();
        DbORDomainModel domModel = getDomainModel();
        DbORDomain domain = (DbORDomain) domModel.findComponentByName(DbORDomain.metaClass, name);
        if (domain == null) {
            domain = new DbORDomain(domModel);
            domain.setName(name);
        }

        domain.setCategory(ORDomainCategory.getInstance(ORDomainCategory.DOMAIN));

        DbRelationN relN = clas.getComponents();
        DbEnumeration enu = relN.elements(DbJVDataMember.metaClass);
        while (enu.hasMoreElements()) {
            DbJVDataMember attr = (DbJVDataMember) enu.nextElement();
            DbORAllowableValue value = new DbORAllowableValue(domain);
            value.setValue(attr.getName());
        }
        enu.close();
    } //end convertClassToDomain()

    private boolean isEnumeration(DbJVClass clas) throws DbException {
        DbSMSStereotype stereotype = clas.getUmlStereotype();
        String name = (stereotype == null) ? null : stereotype.getName();
        boolean isEnumeration = Extensibility.ENUMERATION.equals(name);
        return isEnumeration;
    }

    private boolean isDataType(DbJVClass clas) throws DbException {
        DbSMSStereotype stereotype = clas.getUmlStereotype();
        String name = (stereotype == null) ? null : stereotype.getName();
        boolean isEnumeration = "dataType".equals(name);
        return isEnumeration;
    }

    private void convertClassToTable(DbJVClass clas) throws DbException {

        if (m_parameters.stategy == JavaToDataModelParameters.InheritanceStrategy.ONE_TABLE_PER_CONCRETE_CLASS) {
            //do not convert an abstract class into a table
            if (clas.isAbstract()) {
                return;
            }
        } else if (m_parameters.stategy == JavaToDataModelParameters.InheritanceStrategy.ONE_TABLE_PER_INHERITANCE_TREE) {
            //do not convert an sublass into a table
            DbJVInheritance inheritance = getSuperClassInheritance(clas);
            if (inheritance != null) {
                return;
            }
        } //end if

        //create the table
        DbORTable table = (DbORTable) m_orModel.createComponent(metaClasses[AnyORObject.I_TABLE]);
        table.setName(clas.getName());
        setLink(clas, table);
        m_classMap.put(clas, table);
        DbORColumn idColumn = null;
        Map<DbJVDataMember, DbORColumn> map = new HashMap<DbJVDataMember, DbORColumn>();

        //if there are fields identified as part of pk 
        List<DbJVDataMember> pkFields = getPKFields(clas);
        if ((m_parameters.generatePK) && (pkFields.isEmpty())) {
            idColumn = generateIDColumn(clas, table);
        }

        boolean categoryColumnRequired = convertFieldsToColumns(map, table, clas);
        if (categoryColumnRequired) {
            addCategoryColumn(map, table, clas);
        }

        if (m_parameters.generatePK) {
            generatePK(clas, table, map, pkFields, idColumn);
        }
    } //end convertClassToTable()

    private boolean convertFieldsToColumns(Map<DbJVDataMember, DbORColumn> map, DbORTable table,
            DbJVClass clas) throws DbException {
        boolean categoryColumnRequired = false;

        if (m_parameters.stategy == JavaToDataModelParameters.InheritanceStrategy.ONE_TABLE_PER_CONCRETE_CLASS) {
            DbJVInheritance inheritance = getSuperClassInheritance(clas);

            if (inheritance != null) {
                DbJVClass superClass = (DbJVClass) inheritance.getSuperClass();
                if (superClass.isAbstract()) {
                    convertFieldsToColumns(map, table, superClass);
                }
            } //end if
        } //end if

        //for each field of the class
        DbEnumeration enum2 = clas.getComponents().elements(DbJVDataMember.metaClass);
        while (enum2.hasMoreElements()) {
            DbJVDataMember field = (DbJVDataMember) enum2.nextElement();
            if (field.getAssociationEnd() != null || field.isStatic())
                continue;
            DbORColumn column = (DbORColumn) table
                    .createComponent(metaClasses[AnyORObject.I_COLUMN]);
            column.setName(field.getName());
            column.setType(getORType(field.getType()));
            setLink(field, column);
            map.put(field, column);
        } //end while
        enum2.close();

        if (m_parameters.stategy == JavaToDataModelParameters.InheritanceStrategy.ONE_TABLE_PER_INHERITANCE_TREE) {
            DbRelationN relN = clas.getSubInheritances();
            DbEnumeration enu = relN.elements(DbJVInheritance.metaClass);
            while (enu.hasMoreElements()) {
                DbJVInheritance inher = (DbJVInheritance) enu.nextElement();
                DbJVClass subClass = (DbJVClass) inher.getSubClass();
                convertFieldsToColumns(map, table, subClass);
                categoryColumnRequired = true;
            }
            enu.close();
        }

        return categoryColumnRequired;
    } //end convertFieldsToColumns()

    private void addCategoryColumn(Map<DbJVDataMember, DbORColumn> map, DbORTable table,
            DbJVClass clas) throws DbException {
        DbORColumn column = (DbORColumn) table.createComponent(metaClasses[AnyORObject.I_COLUMN]);
        column.setName(m_parameters.category);
        DbJVClassModel model = (DbJVClassModel) clas.getCompositeOfType(DbJVClassModel.metaClass);
        DbJVPrimitiveType intType = JavaToDataModelParameters.getIntType(model);
        column.setType(getORType(intType));
    }

    private DbORColumn generateIDColumn(DbJVClass clas, DbORTable table) throws DbException {
        DbORColumn idColumn = null;

        //create the long 'id'
        idColumn = (DbORColumn) table.createComponent(metaClasses[AnyORObject.I_COLUMN]);
        idColumn.setName(m_parameters.id);

        //set its type
        //DbJVPrimitiveType longType = getLongType(clas);
        idColumn.setType(m_parameters.identifierType);

        return idColumn;
    }

    private void generatePK(DbJVClass clas, DbORTable table, Map<DbJVDataMember, DbORColumn> map,
            List<DbJVDataMember> pkFields, DbORColumn idColumn) throws DbException {
        //create the primary key
        DbORPrimaryUnique pk = (DbORPrimaryUnique) table
                .createComponent(metaClasses[AnyORObject.I_PRIMARYUNIQUE]);

        if (idColumn != null) {
            pk.addToColumns(idColumn);
            idColumn.setNull(false);
        } else {
            for (DbJVDataMember field : pkFields) {
                DbORColumn col = map.get(field);
                pk.addToColumns(col);
                col.setNull(false);
            } //end for
        } //end if 
    } //end generatePK()

    private List<DbJVDataMember> getPKFields(DbJVClass clas) throws DbException {
        List<DbJVDataMember> pkFields = new ArrayList<DbJVDataMember>();
        DbEnumeration enu = clas.getComponents().elements(DbJVDataMember.metaClass);
        while (enu.hasMoreElements()) {
            DbJVDataMember field = (DbJVDataMember) enu.nextElement();
            DbRelationN relN = field.getUmlConstraints();
            DbEnumeration enu2 = relN.elements(DbSMSUmlConstraint.metaClass);

            while (enu2.hasMoreElements()) {
                DbSMSUmlConstraint constr = (DbSMSUmlConstraint) enu2.nextElement();
                String name = constr.getName();
                if ("pk".equals(name)) {
                    pkFields.add(field);
                    break;
                }
            } //end while
            enu2.close();

        } //end while
        enu.close();
        return pkFields;
    }

    private void convertAssociations(DbJVClass clas, DbORTable table) throws DbException {

        //for each association of the class
        DbEnumeration dbEnum = clas.getComponents().elements(DbJVAssociation.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVAssociation assoc = (DbJVAssociation) dbEnum.nextElement();

            DbJVAssociationEnd frontEnd = (DbJVAssociationEnd) assoc.getFrontEnd();
            DbJVAssociationEnd backEnd = (DbJVAssociationEnd) assoc.getBackEnd();
            DbJVClass frontClass = (DbJVClass) frontEnd.getAssociationMember().getCompositeOfType(
                    DbJVClass.metaClass);
            DbJVClass backClass = (DbJVClass) backEnd.getAssociationMember().getCompositeOfType(
                    DbJVClass.metaClass);

            if ((m_parameters.stategy == JavaToDataModelParameters.InheritanceStrategy.ONE_TABLE_PER_CLASS)
                    || (m_parameters.stategy == JavaToDataModelParameters.InheritanceStrategy.ONE_TABLE_PER_INHERITANCE_TREE)) {
                DbORTable frontTable = m_classMap.get(frontClass);
                DbORTable backTable = m_classMap.get(backClass);
                if (frontTable == null || backTable == null)
                    continue;

                convertAssociation(assoc, frontEnd, backEnd, frontTable, backTable, 1, 1);

            } else if (m_parameters.stategy == JavaToDataModelParameters.InheritanceStrategy.ONE_TABLE_PER_CONCRETE_CLASS) {

                List<DbJVClass> concreteFrontClasses = new ArrayList<DbJVClass>();
                findConcreteClasses(frontClass, concreteFrontClasses);

                List<DbJVClass> concreteBackClasses = new ArrayList<DbJVClass>();
                findConcreteClasses(backClass, concreteBackClasses);

                for (DbJVClass concreteFrontClass : concreteFrontClasses) {
                    for (DbJVClass concreteBackClass : concreteBackClasses) {
                        DbORTable frontTable = m_classMap.get(concreteFrontClass);
                        DbORTable backTable = m_classMap.get(concreteBackClass);
                        convertAssociation(assoc, frontEnd, backEnd, frontTable, backTable,
                                concreteFrontClasses.size(), concreteBackClasses.size());
                    }
                }
            } //end if 
        } //end while
        dbEnum.close();

        //convert the inheritance, if any
        DbJVInheritance inheritance = getSuperClassInheritance(clas);
        if (inheritance != null) {
            if (m_parameters.stategy == JavaToDataModelParameters.InheritanceStrategy.ONE_TABLE_PER_CLASS) {
                convertInheritancesIntoAssociations(clas);
            }
        } //end if

        //for each subclass
        if (m_parameters.stategy == JavaToDataModelParameters.InheritanceStrategy.ONE_TABLE_PER_INHERITANCE_TREE) {
            DbRelationN relN = clas.getSubInheritances();
            DbEnumeration enu = relN.elements(DbJVInheritance.metaClass);
            while (enu.hasMoreElements()) {
                DbJVInheritance inher = (DbJVInheritance) enu.nextElement();
                DbJVClass subClass = (DbJVClass) inher.getSubClass();
                convertAssociations(subClass, table);
            }
            enu.close();
        }

    } //end convertAssociation()

    private void findConcreteClasses(DbJVClass clas, List<DbJVClass> list) throws DbException {
        if (!clas.isAbstract()) {
            list.add(clas);
        } else {
            DbRelationN relN = clas.getSubInheritances();
            DbEnumeration enu = relN.elements(DbJVInheritance.metaClass);
            while (enu.hasMoreElements()) {
                DbJVInheritance inher = (DbJVInheritance) enu.nextElement();
                DbJVClass subClass = (DbJVClass) inher.getSubClass();
                findConcreteClasses(subClass, list);
            }
            enu.close();
        } //end if
    }

    private void convertAssociation(DbJVAssociation assoc, DbJVAssociationEnd frontEnd,
            DbJVAssociationEnd backEnd, DbORTable frontTable, DbORTable backTable, int nbFrontEnds,
            int nbBackEnds) throws DbException {
        SMSMultiplicity frontMult = frontEnd.getMultiplicity();
        SMSMultiplicity backMult = backEnd.getMultiplicity();
        if (frontMult.isMaxN() && backMult.isMaxN()) { // make an intersection table
            DbORTable interTable = (DbORTable) m_orModel
                    .createComponent(metaClasses[AnyORObject.I_TABLE]);
            interTable.setName(assoc.getName());
            DbORAssociation frontAssoc = new DbORAssociation(frontTable, frontMult, interTable,
                    SMSMultiplicity.getInstance(SMSMultiplicity.EXACTLY_ONE));
            DbORAssociation backAssoc = new DbORAssociation(backTable, backMult, interTable,
                    SMSMultiplicity.getInstance(SMSMultiplicity.EXACTLY_ONE));
            frontAssoc.getFrontEnd().setNavigable(Boolean.valueOf(frontEnd.isNavigable()));
            backAssoc.getFrontEnd().setNavigable(Boolean.valueOf(backEnd.isNavigable()));
            DbORPrimaryUnique pKey = (DbORPrimaryUnique) interTable
                    .createComponent(metaClasses[AnyORObject.I_PRIMARYUNIQUE]);
            pKey.addToAssociationDependencies(frontAssoc.getBackEnd());
            pKey.addToAssociationDependencies(backAssoc.getBackEnd());
            setLink(assoc, interTable);
        } else {

            DbORAssociation orAssoc = new DbORAssociation(frontTable, frontMult, backTable,
                    backMult);
            copyFields(orAssoc, assoc);

            applyXOR(orAssoc, nbFrontEnds, nbBackEnds);

            List<ORAssociationData> list = m_associations.get(assoc);
            if (list == null) {
                list = new ArrayList<ORAssociationData>();
                m_associations.put(assoc, list);
            }

            ORAssociationData data = new ORAssociationData();
            data.orAssoc = orAssoc;
            data.frontTable = frontTable;
            data.backTable = backTable;
            data.hasPolyline = ((nbFrontEnds == 1) && (nbBackEnds == 1));
            list.add(data);

            setLink(assoc, orAssoc);
        } //end if
    }

    private void copyFields(DbORAssociation orAssoc, DbJVAssociation ooAssoc) throws DbException {
        DbOOAssociationEnd ooFrontEnd = ooAssoc.getFrontEnd();
        DbOOAssociationEnd ooBackEnd = ooAssoc.getBackEnd();

        String name = ooAssoc.getName();
        String frontName = ooFrontEnd.getName();
        if (frontName == null) {
            DbOODataMember field = ooFrontEnd.getAssociationMember();
            frontName = field.getName();
        }

        String backName = ooBackEnd.getName();
        if (backName == null) {
            DbOODataMember field = ooBackEnd.getAssociationMember();
            backName = field.getName();
        }

        orAssoc.setName(name);
        DbORAssociationEnd orFrontEnd = orAssoc.getFrontEnd();
        DbORAssociationEnd orBackEnd = orAssoc.getBackEnd();

        orFrontEnd.setNavigable(Boolean.valueOf(ooFrontEnd.isNavigable()));
        orBackEnd.setNavigable(Boolean.valueOf(ooBackEnd.isNavigable()));

        orFrontEnd.setName(frontName);
        orBackEnd.setName(backName);

        orFrontEnd.setAlias(ooFrontEnd.getAlias());
        orBackEnd.setAlias(ooBackEnd.getAlias());

    }

    private void applyXOR(DbORAssociation orAssoc, int nbFrontEnds, int nbBackEnds)
            throws DbException {
        boolean xor = false;

        DbORAssociationEnd frontEnd = orAssoc.getFrontEnd();
        SMSMultiplicity frontMult = frontEnd.getMultiplicity();

        DbORAssociationEnd backEnd = orAssoc.getBackEnd();
        SMSMultiplicity backMult = backEnd.getMultiplicity();

        if (nbBackEnds > 1) {
            if (frontMult.getValue() == SMSMultiplicity.EXACTLY_ONE) {
                frontEnd.setMultiplicity(SMSMultiplicity.getInstance(SMSMultiplicity.OPTIONAL));
                xor = true;
            } else if (frontMult.getValue() == SMSMultiplicity.ONE_OR_MORE) {
                frontEnd.setMultiplicity(SMSMultiplicity.getInstance(SMSMultiplicity.MANY));
            }
        } //end if

        if (nbFrontEnds > 1) {
            if (backMult.getValue() == SMSMultiplicity.EXACTLY_ONE) {
                backEnd.setMultiplicity(SMSMultiplicity.getInstance(SMSMultiplicity.OPTIONAL));
                xor = true;
            } else if (backMult.getValue() == SMSMultiplicity.ONE_OR_MORE) {
                backEnd.setMultiplicity(SMSMultiplicity.getInstance(SMSMultiplicity.MANY));
            }
        } //end if

        if (xor) {
            DbSMSProject proj = (DbSMSProject) orAssoc.getCompositeOfType(DbSMSProject.metaClass);
            DbSMSUmlConstraint xorConstr = getXorConstraint(proj);
            orAssoc.addToUmlConstraints(xorConstr);
        }
    } //end applyXOR()

    private DbSMSUmlConstraint xorConstraint = null;

    private DbSMSUmlConstraint getXorConstraint(DbSMSProject proj) throws DbException {
        if (xorConstraint != null) {
            return xorConstraint;
        }

        DbRelationN relN = proj.getComponents();
        DbEnumeration enu = relN.elements(DbSMSUmlExtensibility.metaClass);
        while (enu.hasMoreElements()) {
            DbSMSUmlExtensibility ext = (DbSMSUmlExtensibility) enu.nextElement();
            DbRelationN relN2 = ext.getComponents();
            DbEnumeration enu2 = relN2.elements(DbSMSUmlConstraint.metaClass);
            while (enu2.hasMoreElements()) {
                DbSMSUmlConstraint constr = (DbSMSUmlConstraint) enu2.nextElement();
                String name = constr.getName();
                if ("xor".equals(name)) {
                    xorConstraint = constr;
                    break;
                }
            } //end while
            enu2.close();
        } //end while
        enu.close();

        return xorConstraint;
    }

    private void convertInheritancesIntoAssociations(DbJVClass clas) throws DbException {
        DbJVInheritance inheritance = getSuperClassInheritance(clas);

        if (inheritance != null) {
            DbJVClass superClass = (DbJVClass) inheritance.getSuperClass();
            DbORTable table = m_classMap.get(clas);
            DbORTable superTable = m_classMap.get(superClass);
            if (superTable != null) {

                DbORAssociation orAssoc = new DbORAssociation(superTable, SMSMultiplicity
                        .getInstance(SMSMultiplicity.OPTIONAL), table, SMSMultiplicity
                        .getInstance(SMSMultiplicity.EXACTLY_ONE));
                orAssoc.getFrontEnd().setNavigable(Boolean.FALSE);
                DbORPrimaryUnique pKey = (DbORPrimaryUnique) table
                        .createComponent(metaClasses[AnyORObject.I_PRIMARYUNIQUE]);
                pKey.addToAssociationDependencies(orAssoc.getBackEnd());

                DbRelationN relN = inheritance.getInheritanceGos();
                DbEnumeration enu = relN.elements(DbSMSInheritanceGo.metaClass);
                while (enu.hasMoreElements()) {
                    DbSMSInheritanceGo go = (DbSMSInheritanceGo) enu.nextElement();
                    m_inheritances.put(go, orAssoc);
                } //end while
                enu.close();
            }
        } //end while
    } //end convertInheritancesIntoAssociations()

    private DbJVInheritance getSuperClassInheritance(DbJVClass clas) throws DbException {
        DbJVInheritance superInheritance = null;

        DbEnumeration dbEnum = clas.getSuperInheritances().elements(DbJVInheritance.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVInheritance inher = (DbJVInheritance) dbEnum.nextElement();
            DbJVClass supClas = (DbJVClass) inher.getSuperClass();
            if (!supClas.isInterface()) {
                superInheritance = inher;
                break;
            }
        }
        dbEnum.close();

        return superInheritance;
    }

    private DbORTypeClassifier getORType(DbOOAdt javaType) throws DbException {
        if (javaType == null)
            return null;
        DbORTypeClassifier type = getLinkedORType(javaType);
        if (type != null)
            return type;
        String name = javaType.getName();
        if (name == null)
            return null;

        DbSMSBuiltInTypePackage pack = m_parameters.getTargetSystem().getBuiltInTypePackage();
        type = (DbORTypeClassifier) pack.findComponentByName(DbORBuiltInType.metaClass, name);
        if (type == null) {
            DbORDomainModel domModel = getDomainModel();
            type = (DbORTypeClassifier) domModel.findComponentByName(DbORDomain.metaClass, name);
            if (type == null) {
                type = new DbORDomain(domModel);
                type.setName(name);
            }
        }
        setLink(javaType, type);
        return type;
    }

    private DbORTypeClassifier getLinkedORType(DbOOAdt javaType) throws DbException {
        DbORTypeClassifier type = null;
        DbEnumeration dbEnum = javaType.getSourceLinks().elements();
        while (dbEnum.hasMoreElements()) {
            DbSMSLink link = (DbSMSLink) dbEnum.nextElement();
            DbEnumeration enum2 = link.getTargetObjects().elements();
            while (enum2.hasMoreElements()) {
                DbObject targetObj = enum2.nextElement();
                if (targetObj instanceof DbORTypeClassifier
                        && AnyORObject.getTargetSystem(targetObj) == m_parameters.getTargetSystem()) {
                    type = (DbORTypeClassifier) targetObj;
                    break;
                }
            }
            enum2.close();
            if (type != null)
                break;
        }
        dbEnum.close();
        return type;
    }

    private DbORDomainModel getDomainModel() throws DbException {
        if (domainModel == null) {
            DbEnumeration dbEnum = m_parent.getComponents().elements(DbORDomainModel.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbORDomainModel domModel = (DbORDomainModel) dbEnum.nextElement();
                if (domModel.getTargetSystem() == m_parameters.getTargetSystem()
                        && kJavaTypes.equals(domModel.getName())) {
                    domainModel = domModel;
                    break;
                }
            }
            dbEnum.close();
            if (domainModel == null) {
                domainModel = new DbORDomainModel(m_parent, m_parameters.getTargetSystem());
                domainModel.setName(kJavaTypes);
            }
        }
        return domainModel;
    }

    private void setLink(DbSMSSemanticalObject sourceObj, DbSMSSemanticalObject targetObj)
            throws DbException {
        if (m_parameters.linkModel == null)
            return;

        DbSMSLink link = new DbSMSLink(m_parameters.linkModel);
        link.addToSourceObjects(sourceObj);
        link.addToTargetObjects(targetObj);
        link.setName(sourceObj.getName());
    }

    private DbORDiagram convertDiagram(DbOODiagram ooDiagram) throws DbException {
        DbORDiagram orDiagram = new DbORDiagram(m_orModel);
        Map<DbORTable, DbORTableGo> map = new HashMap<DbORTable, DbORTableGo>();

        //convert each class graphical object
        DbRelationN relN = ooDiagram.getComponents();
        DbEnumeration enu = relN.elements(DbOOAdtGo.metaClass);
        while (enu.hasMoreElements()) {
            DbOOAdtGo go = (DbOOAdtGo) enu.nextElement();
            DbSMSClassifier classifier = go.getClassifier();
            if (classifier instanceof DbJVClass) {
                DbJVClass claz = (DbJVClass) classifier;
                DbORTable table = m_classMap.get(claz);
                DbORTableGo tableGo = new DbORTableGo(orDiagram, table);
                Rectangle rect = go.getRectangle();
                tableGo.setRectangle(rect);
                map.put(table, tableGo);
            } //end if
        } //end while
        enu.close();

        //convert each association graphical object
        enu = relN.elements(DbOOAssociationGo.metaClass);
        while (enu.hasMoreElements()) {
            DbOOAssociationGo go = (DbOOAssociationGo) enu.nextElement();

            //get association 
            DbOOAssociation ooAssoc = (DbOOAssociation) go.getAssociation();
            List<ORAssociationData> orAssocList = m_associations.get(ooAssoc);
            if (orAssocList != null) {
                for (ORAssociationData data : orAssocList) {
                    if (data.orAssoc != null) {
                        //data.
                        DbORTableGo frontEndGo = map.get(data.frontTable);
                        DbORTableGo backEndGo = map.get(data.backTable);

                        DbORAssociationGo go2 = new DbORAssociationGo(orDiagram, frontEndGo,
                                backEndGo, data.orAssoc);
                        if (data.hasPolyline) {
                            Polygon polyline = go.getPolyline();
                            go2.setPolyline(polyline);
                        }
                    }
                } //end if
            } //end for
        } //end while
        enu.close();

        for (DbSMSInheritanceGo go : m_inheritances.keySet()) {
            DbORAssociation orAssoc = m_inheritances.get(go);
            DbJVInheritance inher = (DbJVInheritance) go.getInheritance();

            //get front end
            DbJVClass superClas = (DbJVClass) inher.getSuperClass();
            DbORTable frontEnd = m_classMap.get(superClas);
            DbORTableGo frontEndGo = map.get(frontEnd);

            //get back end
            DbJVClass subClas = (DbJVClass) inher.getSubClass();
            DbORTable backEnd = m_classMap.get(subClas);
            DbORTableGo backEndGo = map.get(backEnd);

            DbORAssociationGo go2 = new DbORAssociationGo(orDiagram, frontEndGo, backEndGo, orAssoc);
            Polygon polyline = go.getPolyline();
            go2.setPolyline(polyline);
        } //end for 

        return orDiagram;
    } //end convertDiagram

    private void showAndReveal(DbORDiagram diagram) {
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        try {
            mainFrame.addDiagramInternalFrame(diagram);
            mainFrame.findInExplorer(diagram);
        } catch (DbException ex) {
            //do not show if error
        }
    }

    private static class ORAssociationData {

        public boolean hasPolyline = true;
        public DbORTable backTable;
        public DbORTable frontTable;
        public DbORAssociation orAssoc;

    }

} //end JavaToDataModelConversion

