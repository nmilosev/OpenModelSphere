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

package org.modelsphere.sms.db.util;

import java.util.Enumeration;
import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.sms.be.db.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.oo.db.*;
import org.modelsphere.sms.oo.java.db.DbJVCompilationUnit;
import org.modelsphere.sms.or.db.*;

public abstract class AnySemObject {

    private static final MetaClass[] classesWithSuper = new MetaClass[] { DbORAbsTable.metaClass,
            DbORColumn.metaClass, DbORPrimaryUnique.metaClass, DbORCheck.metaClass,
            DbORIndex.metaClass, DbORTrigger.metaClass, DbORAssociation.metaClass,
            DbBEFlow.metaClass };

    private static final MetaClass[] classesWithoutPhysName = new MetaClass[] { DbUDF.metaClass,
            DbProject.metaClass, DbSMSLinkModel.metaClass, DbSMSLink.metaClass,
            DbSMSUserDefinedPackage.metaClass, DbSMSTargetSystem.metaClass,
            DbSMSBuiltInTypeNode.metaClass, DbSMSBuiltInTypePackage.metaClass,
            DbSMSUmlExtensibility.metaClass, DbSMSStereotype.metaClass,
            DbSMSUmlConstraint.metaClass, DbORBuiltInType.metaClass, DbORUserNode.metaClass,
            DbOOAbsPackage.metaClass, DbOOAdt.metaClass, DbOOAssociation.metaClass,
            DbOOAssociationEnd.metaClass, DbOODataMember.metaClass, DbOOOperation.metaClass,
            DbOOParameter.metaClass, DbJVCompilationUnit.metaClass };

    private static final MetaClass[] classesWithoutLinks = new MetaClass[] {
            DbSMSLinkModel.metaClass, DbSMSLink.metaClass, DbSMSTargetSystem.metaClass,
            DbSMSUmlExtensibility.metaClass, DbSMSUmlConstraint.metaClass,
            DbSMSStereotype.metaClass };

    private static boolean[] classesWithSuperBM = null;
    private static boolean[] classesWithoutPhysNameBM = null;
    private static boolean[] classesWithoutLinksBM = null;

    public static boolean supportsSuper(MetaClass metaClass) {
        if (classesWithSuperBM == null)
            classesWithSuperBM = initBitMap(classesWithSuper);
        return classesWithSuperBM[metaClass.getSeqNo()];
    }

    public static boolean supportsPhysicalName(MetaClass metaClass) {
        if (classesWithoutPhysNameBM == null)
            classesWithoutPhysNameBM = initBitMap(classesWithoutPhysName);
        return (DbSemanticalObject.class.isAssignableFrom(metaClass.getJClass()) && !classesWithoutPhysNameBM[metaClass
                .getSeqNo()]);
    }

    public static boolean supportsLinks(MetaClass metaClass) {
        if (classesWithoutLinksBM == null)
            classesWithoutLinksBM = initBitMap(classesWithoutLinks);
        return (DbSMSSemanticalObject.class.isAssignableFrom(metaClass.getJClass()) && !classesWithoutLinksBM[metaClass
                .getSeqNo()]);
    }

    public static final ArrayList getAllDbSMSLinkModel(DbSMSProject project) throws DbException {
        ArrayList array = new ArrayList();
        DbEnumeration dbEnum = project.componentTree(DbSMSLinkModel.metaClass);
        while (dbEnum.hasMoreElements()) {
            array.add(dbEnum.nextElement());
        }
        return array;

    }

    private static boolean[] initBitMap(MetaClass[] metaClasses) {
        boolean[] bitMap = new boolean[MetaClass.getNbMetaClasses()];
        for (int i = 0; i < metaClasses.length; i++) {
            Enumeration enumeration = metaClasses[i].enumMetaClassHierarchy(false);
            while (enumeration.hasMoreElements())
                bitMap[((MetaClass) enumeration.nextElement()).getSeqNo()] = true;
        }
        return bitMap;
    }

    public static DbSMSSemanticalObject getSuperCopy(DbSMSSemanticalObject subDbo)
            throws DbException {

        if (subDbo == null)
            return null;

        DbSMSSemanticalObject superDbo = null;
        if (subDbo instanceof DbORDataModel) {
            DbObject composite = subDbo.getComposite();
            if (composite instanceof DbORDataModel)
                superDbo = (DbSMSSemanticalObject) composite;
        } else {
            superDbo = subDbo.getSuperCopy();
        }
        return superDbo;
    }

    // Return the sub of <superDbo> in the sub-schema, if any
    public static DbSMSSemanticalObject getSubCopy(DbSMSSemanticalObject superDbo,
            DbSMSSemanticalObject subParent) throws DbException {
        DbRelationN subDbos = superDbo.getSubCopies();
        for (int i = 0; i < subDbos.size(); i++) {
            DbSMSSemanticalObject subDbo = (DbSMSSemanticalObject) subDbos.elementAt(i);
            if (subDbo.getComposite() == subParent)
                return subDbo;
        }
        return null;
    }
}
