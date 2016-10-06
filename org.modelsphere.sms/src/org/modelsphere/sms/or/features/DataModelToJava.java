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

package org.modelsphere.sms.or.features;

import java.util.HashMap;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.sms.db.DbSMSBuiltInTypePackage;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.java.db.DbJVAssociation;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVInheritance;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.db.DbJVPrimitiveType;
import org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;
import org.modelsphere.sms.or.db.DbORAbsTable;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.international.LocaleMgr;

public final class DataModelToJava {

    private static final String kDataTypes = LocaleMgr.misc.getString("dataTypePackName");

    private DbORDataModel model;
    private DbOOAbsPackage parentPack;
    private DbSMSLinkModel linkModel;
    private JVVisibility fieldVis;
    private DbJVPackage newPack;
    private DbSMSBuiltInTypePackage javaTypePack;
    private DbJVPackage dataTypePack = null;
    private HashMap tableMap = new HashMap();

    public DataModelToJava(DbORDataModel model, DbOOAbsPackage parentPack,
            DbSMSLinkModel linkModel, JVVisibility fieldVis) throws DbException {
        this.model = model;
        this.parentPack = parentPack;
        this.linkModel = linkModel;
        this.fieldVis = fieldVis;
        newPack = new DbJVPackage(parentPack);
        newPack.setName(model.getName());
        javaTypePack = parentPack.getTargetSystem().getBuiltInTypePackage();

        DbEnumeration dbEnum = model.getComponents().elements(DbORAbsTable.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORAbsTable table = (DbORAbsTable) dbEnum.nextElement();
            DbJVClass clas = new DbJVClass(newPack, JVClassCategory
                    .getInstance(JVClassCategory.CLASS));
            clas.setName(table.getName());
            setLink(table, clas);
            tableMap.put(table, clas);

            DbEnumeration enum2 = table.getComponents().elements(DbORColumn.metaClass);
            while (enum2.hasMoreElements()) {
                DbORColumn column = (DbORColumn) enum2.nextElement();
                if (column.isForeign())
                    continue;
                DbJVDataMember field = new DbJVDataMember(clas);
                field.setName(column.getName());
                field.setType(getJavaType(column.getType()));
                field.setVisibility(fieldVis);
                setLink(column, field);
            }
            enum2.close();
        }
        dbEnum.close();

        dbEnum = model.getComponents().elements(DbORAssociation.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORAssociation assoc = (DbORAssociation) dbEnum.nextElement();
            DbORAssociationEnd frontEnd = assoc.getFrontEnd();
            DbORAssociationEnd backEnd = assoc.getBackEnd();
            SMSMultiplicity frontMult = frontEnd.getMultiplicity();
            SMSMultiplicity backMult = backEnd.getMultiplicity();
            DbJVClass frontAdt = (DbJVClass) tableMap.get(frontEnd.getClassifier());
            DbJVClass backAdt = (DbJVClass) tableMap.get(backEnd.getClassifier());

            if (createInheritance(frontEnd, frontMult, frontAdt, backMult, backAdt) != null)
                continue;
            if (createInheritance(backEnd, backMult, backAdt, frontMult, frontAdt) != null)
                continue;
            DbJVDataMember frontField = new DbJVDataMember(frontAdt);
            DbJVDataMember backField = new DbJVDataMember(backAdt);
            frontField.setVisibility(fieldVis);
            backField.setVisibility(fieldVis);
            frontField.setRoleDefaultInitialValues(backField, frontMult);
            backField.setRoleDefaultInitialValues(frontField, backMult);
            DbJVAssociation jvAssoc = new DbJVAssociation(frontField, frontMult, backField,
                    backMult);
            jvAssoc.setName(assoc.getName());
            jvAssoc.getFrontEnd().setNavigable(Boolean.valueOf(frontEnd.isNavigable()));
            jvAssoc.getBackEnd().setNavigable(Boolean.valueOf(backEnd.isNavigable()));
            setLink(assoc, jvAssoc);
        }
        dbEnum.close();
    }

    private DbJVInheritance createInheritance(DbORAssociationEnd subEnd, SMSMultiplicity subMult,
            DbJVClass subAdt, SMSMultiplicity superMult, DbJVClass superAdt) throws DbException {
        if (subMult.getValue() == SMSMultiplicity.EXACTLY_ONE
                && subEnd.getDependentConstraints().size() == 1
                && (superMult.getValue() == SMSMultiplicity.OPTIONAL || superMult.getValue() == SMSMultiplicity.EXACTLY_ONE)) {
            DbORPrimaryUnique puKey = (DbORPrimaryUnique) subEnd.getDependentConstraints()
                    .elementAt(0);
            if (puKey.getAssociationDependencies().size() == 1) {
                DbJVInheritance inher = new DbJVInheritance(subAdt, superAdt);
                return inher;
            }
        }
        return null;
    }

    private DbOOAdt getJavaType(DbORTypeClassifier colType) throws DbException {
        if (colType == null)
            return null;
        DbOOAdt type = getLinkedJavaType(colType);
        if (type != null)
            return type;
        String name = colType.getName();
        if (name == null)
            return null;
        type = (DbOOAdt) javaTypePack.findComponentByName(DbJVPrimitiveType.metaClass, name);
        if (type == null) {
            DbJVPackage typePack = getDataTypePack();
            type = (DbOOAdt) typePack.findComponentByName(DbJVClass.metaClass, name);
            if (type == null) {
                type = new DbJVClass(typePack, JVClassCategory.getInstance(JVClassCategory.CLASS));
                type.setName(name);
            }
        }
        setLink(colType, type);
        return type;
    }

    private DbOOAdt getLinkedJavaType(DbORTypeClassifier colType) throws DbException {
        DbOOAdt type = null;
        DbEnumeration dbEnum = colType.getSourceLinks().elements();
        while (dbEnum.hasMoreElements()) {
            DbSMSLink link = (DbSMSLink) dbEnum.nextElement();
            if (link.getTargetObjects().size() != 1)
                continue;
            DbObject targetObj = link.getTargetObjects().elementAt(0);
            if (targetObj instanceof DbJVClass || targetObj instanceof DbJVPrimitiveType) {
                type = (DbOOAdt) targetObj;
                break;
            }
        }
        dbEnum.close();
        return type;
    }

    private DbJVPackage getDataTypePack() throws DbException {
        if (dataTypePack == null) {
            dataTypePack = (DbJVPackage) parentPack.findComponentByName(DbJVPackage.metaClass,
                    kDataTypes);
            if (dataTypePack == null) {
                dataTypePack = new DbJVPackage(parentPack);
                dataTypePack.setName(kDataTypes);
            }
        }
        return dataTypePack;
    }

    private void setLink(DbSMSSemanticalObject sourceObj, DbSMSSemanticalObject targetObj)
            throws DbException {
        if (linkModel == null)
            return;
        DbSMSLink link = new DbSMSLink(linkModel);
        link.addToSourceObjects(sourceObj);
        link.addToTargetObjects(targetObj);
        link.setName(sourceObj.getName());
    }
}
