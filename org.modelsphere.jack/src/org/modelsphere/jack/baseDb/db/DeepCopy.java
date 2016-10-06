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

package org.modelsphere.jack.baseDb.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.modelsphere.jack.baseDb.db.DbUDF.UDFMap;
import org.modelsphere.jack.baseDb.meta.*;

/**
 * Perform a deep copy, i.e. copy all the DbObjects passed in parameters in the destination
 * composite. If the DbObjects have descendants, they are deeply created in the destination.
 * <p>
 * All the source objects must be in the same project, and a source object cannot be a descendant of
 * another source object. The destination composite may be in a different database; in this case, a
 * read transaction must be active on the source database, and a write transaction must be active on
 * the destination database.
 * <p>
 * The copied objects have all their non-relation fields set to the value of the source. For
 * relation fields, the following rules apply:
 * <li>If the neighbor of the source object is copied, link the copy of the source to the copy of
 * the neighbor.
 * <li>Else if the relation is not marked 'copy links', set the link to null in the copied object.
 * <li>Else if the copy operation is within a single project, link the copied object to the same
 * neighbor as the source object.
 * <li>Else (copy from one project to another), find in the destination project the destination
 * neighbor (i.e. the object having the same semantic identifier as the source neighbor), then link
 * the copied object to the destination neighbor, or set the link to null if the destination
 * neighbor does not exist.
 * 
 */
public final class DeepCopy {

    private DbProject srcProject;
    private DbProject destProject;
    private DeepCopyCustomizer customizer;
    private ArrayList srcRoots = new ArrayList();
    private HashSet srcObjects = new HashSet();
    private UDFMap udfMap = null;
    private ArrayList objsToRemove = new ArrayList();

    // beginMatching must be done prior to DeepCopy object instantiation.
    public DeepCopy(DbProject srcProject, DbProject destProject, DeepCopyCustomizer customizer)
            throws DbException {
        this.srcProject = srcProject;
        this.destProject = destProject;
        this.customizer = customizer;
        srcProject.setMatchingObject(destProject);
    }

    public final void setUdfMap(UDFMap udfMap) {
        this.udfMap = udfMap;
    }

    public final void create(DbObject[] srcObjs, DbObject destComposite,
            boolean namePrefixedWithCopyOf) throws DbException {
        for (int i = 0; i < srcObjs.length; i++)
            create(srcObjs[i], destComposite, namePrefixedWithCopyOf);
    }

    public final DbObject create(DbObject srcObj, DbObject destComposite,
            boolean namePrefixedWithCopyOf) throws DbException {
        srcRoots.add(srcObj);
        DbObject copy = createAux(srcObj, destComposite, namePrefixedWithCopyOf);
        return copy;
    }

    // Must be called after all calls to <create>, to complete the copy operation.
    public final void fill() throws DbException {
        Iterator iter = srcObjects.iterator();
        while (iter.hasNext()) {
            DbObject srcObj = (DbObject) iter.next();
            copyUdfValues(srcObj);
            fillAux(srcObj);
        }
        if (customizer != null)
            customizer.endCopy(srcRoots);
        for (int i = 0; i < objsToRemove.size(); i++)
            ((DbObject) objsToRemove.get(i)).remove();
    }

    // deepCopy step 1: create all the objects without links, except composite 
    private DbObject createAux(DbObject srcObj, DbObject destComposite,
            boolean namePrefixedWithCopyOf) throws DbException {
        srcObj.db.fetch(srcObj);
        MetaClass destClass = srcObj.getMetaClass();
        if (customizer != null)
            destClass = customizer.getDestMetaClass(srcObj, destComposite);
        if (!destClass.compositeIsAllowed(destComposite.getMetaClass()))
            return null;

        if (!srcObjects.add(srcObj))
            throw new RuntimeException("Copied more than once"); // NOT LOCALIZABLE RuntimeException
        DbObject destObj = srcObj.duplicate(destClass, false);
        destObj.db = destComposite.db;
        destObj.project = destComposite.project;
        destObj.m_ts = DbObject.getNextTs();
        destObj.setTransStatus(Db.OBJ_ADDED);
        srcObj.setMatchingObject(destObj);
        destObj.setComposite(destComposite);
        if (srcObj.db != destObj.db && destObj instanceof DbSemanticalObject) {
            destObj.basicSet(DbSemanticalObject.fWriteAccessList, null);
            destObj.basicSet(DbSemanticalObject.fAdminAccessList, null);
        }
        if (customizer != null)
            customizer.initFields(srcObj, destObj, namePrefixedWithCopyOf);

        if (srcObj instanceof DbUDF)
            return destObj;

        DbRelationN srcComponents = (DbRelationN) srcObj.basicGet(DbObject.fComponents);
        if (srcComponents == null)
            return destObj;

        for (int i = 0; i < srcComponents.size(); i++) {
            DbObject dbo = srcComponents.elementAt(i);
            if (dbo.getMatchingObject() == null) // do not copy if it is already a copy 
                createAux(dbo, destObj, false); //prefix "Copy Of" not propagated to components..
        }

        return destObj;
    }

    private void copyUdfValues(DbObject srcObj) throws DbException {
        DbRelationN srcUdfValues = (DbRelationN) srcObj.basicGet(DbObject.fUdfValues);
        if (srcUdfValues == null)
            return;
        DbObject destObj = srcObj.getMatchingObject();
        for (int i = 0; i < srcUdfValues.size(); i++) {
            DbUDFValue srcUdfVal = (DbUDFValue) srcUdfValues.elementAt(i);
            DbUDF srcUdf = (DbUDF) srcUdfVal.getComposite();
            DbUDF destUdf = srcUdf;
            if (srcObj.project != destObj.project
                    || srcObj.getMetaClass() != destObj.getMetaClass()) {
                if (udfMap == null)
                    udfMap = new UDFMap(destObj.project);
                destUdf = udfMap.findAdd(destObj.getMetaClass(), srcUdf.getName(), srcUdf
                        .getValueType());
            }
            new DbUDFValue(destUdf, destObj, srcUdfVal.getValue());
        }
    }

    /*
     * deepCopy step 2: copy all the links. For each link: if the source neighbor is part of the
     * copy set, establish the link with the copy of the neighbor; otherwise, check that the
     * relation is copyable; if so: if same graph, establish the link with the source neighbor
     * itself if max card (neighbor side) allows it; if different graph. search for an object in the
     * destination graph with the same semantical id (using the matchingObject facility).
     */
    private void fillAux(DbObject srcObj) throws DbException {
        DbObject destObj = srcObj.getMatchingObject();
        MetaClass commonClass = srcObj.getMetaClass().getCommonSuperMetaClass(
                destObj.getMetaClass());
        MetaField[] allMetaFields = commonClass.getAllMetaFields();
        for (int i = 0; i < allMetaFields.length; i++) {
            MetaField metaField = allMetaFields[i];
            if (!(metaField instanceof MetaRelationship))
                continue;
            MetaRelationship relation = (MetaRelationship) metaField;
            if (relation instanceof MetaRelationN) {
                if (relation == DbObject.fComponents || relation == DbObject.fUdfValues)
                    continue;
                MetaRelationship oppRel = relation.getOppositeRel(null);
                DbRelationN dbRelN = (DbRelationN) srcObj.basicGet(relation);
                if (dbRelN == null)
                    continue;
                int nb = dbRelN.size();
                int iDst = 0;
                for (int iSrc = 0; iSrc < nb; iSrc++) {
                    DbObject srcNeighbor = (DbObject) dbRelN.elementAt(iSrc);
                    DbObject destNeighbor = null;
                    if (srcObjects.contains(srcNeighbor))
                        destNeighbor = srcNeighbor.getMatchingObject(); // in the copy set
                    else if (oppRel.getMaxCard() > 1) {
                        if (customizer != null)
                            destNeighbor = customizer.resolveLink(srcObj, relation, srcNeighbor);
                        else if ((relation.getFlags() & MetaField.COPY_REFS) != 0)
                            destNeighbor = srcNeighbor.findMatchingObject();
                    }
                    if (destNeighbor != null) {
                        // Relations are populated from both sides. So the relation may be partly populated
                        // (in disorder) from already processed neighbors; so we have to reorder it.
                        boolean added;
                        if (oppRel.getMaxCard() == 1)
                            added = destNeighbor.basicSet(oppRel, destObj, iDst);
                        else
                            added = destObj.setRelationNN((MetaRelationN) relation, destNeighbor,
                                    Db.ADD_TO_RELN, iDst, -1);
                        if (!added) {
                            DbRelationN destDbRelN = (DbRelationN) destObj.basicGet(relation);
                            if (!(destDbRelN instanceof DbHugeRAMRelationN)) { // optimization
                                int iDst2 = destDbRelN.indexOf(destNeighbor);
                                if (iDst != iDst2)
                                    destObj.basicReinsert((MetaRelationN) relation, iDst2, iDst);
                            }
                        }
                        iDst++;
                    }
                }
            } else {
                if (destObj.basicGet(relation) != null)
                    continue;
                DbObject srcNeighbor = (DbObject) srcObj.basicGet(relation);
                if (srcNeighbor == null)
                    continue;
                DbObject destNeighbor = null;
                if (srcObjects.contains(srcNeighbor))
                    destNeighbor = srcNeighbor.getMatchingObject(); // in the copy set
                else {
                    if (customizer != null)
                        destNeighbor = customizer.resolveLink(srcObj, relation, srcNeighbor);
                    else if ((relation.getFlags() & MetaField.COPY_REFS) != 0)
                        destNeighbor = srcNeighbor.findMatchingObject();
                    if (destNeighbor != null) {
                        MetaRelationship oppRel = relation.getOppositeRel(destNeighbor);
                        if (oppRel.getMaxCard() == 1 && destNeighbor.basicGet(oppRel) != null)
                            destNeighbor = null;
                    }
                }
                if (destNeighbor != null)
                    destObj.basicSet(relation, destNeighbor);
                else if (srcObj.getMinCard(relation) > 0) // getMinCard on the source object, getMinCard may need the link
                    objsToRemove.add(destObj); // it does not matter if added more than once
            }
        }
    }
}
