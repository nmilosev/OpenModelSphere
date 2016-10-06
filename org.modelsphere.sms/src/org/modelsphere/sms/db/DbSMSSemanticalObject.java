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
package org.modelsphere.sms.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.db.util.AnySemObject;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSPackage.html">DbSMSPackage</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSTypedElement.html">DbSMSTypedElement</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSConstraint.html">DbSMSConstraint</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSIndex.html">DbSMSIndex</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSClassifier.html">DbSMSClassifier</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSAbstractRelationship.html"
 * >DbSMSAbstractRelationship</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSAbstractRelationshipEnd.html"
 * >DbSMSAbstractRelationshipEnd</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSTargetSystem.html">DbSMSTargetSystem</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSUser.html">DbSMSUser</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSCommonItem.html">DbSMSCommonItem</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSNotice.html">DbSMSNotice</A>, <A
 * HREF="../../../../org/modelsphere/sms/oo/java/db/DbJVCompilationUnit.html"
 * >DbJVCompilationUnit</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/oracle/db/DbORATablespace.html">DbORATablespace</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/oracle/db/DbORALobStorage.html">DbORALobStorage</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/oracle/db/DbORAAbsPartition.html">DbORAAbsPartition</A>,
 * <A HREF="../../../../org/modelsphere/sms/or/oracle/db/DbORAFile.html">DbORAFile</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/oracle/db/DbORARollbackSegment.html"
 * >DbORARollbackSegment</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/oracle/db/DbORASequence.html">DbORASequence</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/oracle/db/DbORARedoLogFile.html">DbORARedoLogFile</A>,
 * <A HREF="../../../../org/modelsphere/sms/or/ibm/db/DbIBMTablespace.html">DbIBMTablespace</A>, <A
 * HREF
 * ="../../../../org/modelsphere/sms/or/ibm/db/DbIBMDbPartitionGroup.html">DbIBMDbPartitionGroup<
 * /A>, <A
 * HREF="../../../../org/modelsphere/sms/or/ibm/db/DbIBMBufferPool.html">DbIBMBufferPool</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/ibm/db/DbIBMSequence.html">DbIBMSequence</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/informix/db/DbINFDbspace.html">DbINFDbspace</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/informix/db/DbINFSbspace.html">DbINFSbspace</A>, <A
 * HREF="../../../../org/modelsphere/sms/be/db/DbBEQualifier.html">DbBEQualifier</A>, <A
 * HREF="../../../../org/modelsphere/sms/be/db/DbBEResource.html">DbBEResource</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public abstract class DbSMSSemanticalObject extends DbSemanticalObject {

    //Meta

    public static final MetaRelationN fSubCopies = new MetaRelationN(LocaleMgr.db
            .getString("subCopies"), 0, MetaRelationN.cardN);
    public static final MetaRelation1 fSuperCopy = new MetaRelation1(LocaleMgr.db
            .getString("superCopy"), 0);
    public static final MetaRelationN fTargetLinks = new MetaRelationN(LocaleMgr.db
            .getString("targetLinks"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fSourceLinks = new MetaRelationN(LocaleMgr.db
            .getString("sourceLinks"), 0, MetaRelationN.cardN);
    public static final MetaRelation1 fUmlStereotype = new MetaRelation1(LocaleMgr.db
            .getString("umlStereotype"), 0);
    public static final MetaRelationN fUmlConstraints = new MetaRelationN(LocaleMgr.db
            .getString("umlConstraints"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fObjectImports = new MetaRelationN(LocaleMgr.db
            .getString("objectImports"), 0, MetaRelationN.cardN);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbSMSSemanticalObject"), DbSMSSemanticalObject.class, new MetaField[] {
            fSubCopies, fSuperCopy, fTargetLinks, fSourceLinks, fUmlStereotype, fUmlConstraints,
            fObjectImports }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSemanticalObject.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbSMSObjectImport.metaClass });

            fSubCopies.setJField(DbSMSSemanticalObject.class.getDeclaredField("m_subCopies"));
            fSuperCopy.setJField(DbSMSSemanticalObject.class.getDeclaredField("m_superCopy"));
            fSuperCopy.setRendererPluginName("DbSuperCopy");
            fTargetLinks.setJField(DbSMSSemanticalObject.class.getDeclaredField("m_targetLinks"));
            fSourceLinks.setJField(DbSMSSemanticalObject.class.getDeclaredField("m_sourceLinks"));
            fUmlStereotype.setJField(DbSMSSemanticalObject.class
                    .getDeclaredField("m_umlStereotype"));
            fUmlStereotype.setFlags(MetaField.COPY_REFS);
            fUmlConstraints.setJField(DbSMSSemanticalObject.class
                    .getDeclaredField("m_umlConstraints"));
            fUmlConstraints.setFlags(MetaField.COPY_REFS);
            fObjectImports.setJField(DbSMSSemanticalObject.class
                    .getDeclaredField("m_objectImports"));

            fSuperCopy.setOppositeRel(DbSMSSemanticalObject.fSubCopies);
            fTargetLinks.setOppositeRel(DbSMSLink.fTargetObjects);
            fSourceLinks.setOppositeRel(DbSMSLink.fSourceObjects);
            fUmlStereotype.setOppositeRel(DbSMSStereotype.fStereotypedObjects);
            fUmlConstraints.setOppositeRel(DbSMSUmlConstraint.fConstrainedObjects);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_subCopies;
    DbSMSSemanticalObject m_superCopy;
    DbRelationN m_targetLinks;
    DbRelationN m_sourceLinks;
    DbSMSStereotype m_umlStereotype;
    DbRelationN m_umlConstraints;
    DbRelationN m_objectImports;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSSemanticalObject() {
    }

    /**
     * Creates an instance of DbSMSSemanticalObject.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSSemanticalObject(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @param value
     *            org.modelsphere.sms.db.DbSMSSemanticalObject
     **/
    public void setSuperCopy(DbSMSSemanticalObject value) throws DbException {
        if (AnySemObject.supportsSuper(getMetaClass()))
            basicSet(fSuperCopy, value);
    }

    //Setters

    /**
     * Adds an element to or removes an element from the list of target links associated to a
     * DbSMSSemanticalObject's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setTargetLinks(DbSMSLink value, int op) throws DbException {
        setRelationNN(fTargetLinks, value, op);
    }

    /**
     * Adds an element to the list of target links associated to a DbSMSSemanticalObject's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToTargetLinks(DbSMSLink value) throws DbException {
        setTargetLinks(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of target links associated to a DbSMSSemanticalObject's
     * instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromTargetLinks(DbSMSLink value) throws DbException {
        setTargetLinks(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Adds an element to or removes an element from the list of source links associated to a
     * DbSMSSemanticalObject's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setSourceLinks(DbSMSLink value, int op) throws DbException {
        setRelationNN(fSourceLinks, value, op);
    }

    /**
     * Adds an element to the list of source links associated to a DbSMSSemanticalObject's instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToSourceLinks(DbSMSLink value) throws DbException {
        setSourceLinks(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of source links associated to a DbSMSSemanticalObject's
     * instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromSourceLinks(DbSMSLink value) throws DbException {
        setSourceLinks(value, Db.REMOVE_FROM_RELN);
    }

    /**
     * Sets the uml stereotype object associated to a DbSMSSemanticalObject's instance.
     * 
     * @param value
     *            the uml stereotype object to be associated
     **/
    public final void setUmlStereotype(DbSMSStereotype value) throws DbException {
        basicSet(fUmlStereotype, value);
    }

    /**
     * Adds an element to or removes an element from the list of uml constraints associated to a
     * DbSMSSemanticalObject's instance.
     * 
     * @param value
     *            an element to be added to or removed from the list.
     * @param op
     *            Either Db.ADD_TO_RELN or Db.REMOVE_FROM_RELN
     **/
    public final void setUmlConstraints(DbSMSUmlConstraint value, int op) throws DbException {
        setRelationNN(fUmlConstraints, value, op);
    }

    /**
     * Adds an element to the list of uml constraints associated to a DbSMSSemanticalObject's
     * instance.
     * 
     * @param value
     *            the element to be added.
     **/
    public final void addToUmlConstraints(DbSMSUmlConstraint value) throws DbException {
        setUmlConstraints(value, Db.ADD_TO_RELN);
    }

    /**
     * Removes an element from the list of uml constraints associated to a DbSMSSemanticalObject's
     * instance.
     * 
     * @param value
     *            the element to be removed.
     **/
    public final void removeFromUmlConstraints(DbSMSUmlConstraint value) throws DbException {
        setUmlConstraints(value, Db.REMOVE_FROM_RELN);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fSubCopies)
                ((DbSMSSemanticalObject) value).setSuperCopy(this);
            else if (metaField == fSuperCopy)
                setSuperCopy((DbSMSSemanticalObject) value);
            else if (metaField == fTargetLinks)
                setTargetLinks((DbSMSLink) value, Db.ADD_TO_RELN);
            else if (metaField == fSourceLinks)
                setSourceLinks((DbSMSLink) value, Db.ADD_TO_RELN);
            else if (metaField == fUmlConstraints)
                setUmlConstraints((DbSMSUmlConstraint) value, Db.ADD_TO_RELN);
            else if (metaField == fObjectImports)
                ((DbSMSObjectImport) value).setSourceObject(this);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        if (relation == fTargetLinks)
            setTargetLinks((DbSMSLink) neighbor, op);
        else if (relation == fSourceLinks)
            setSourceLinks((DbSMSLink) neighbor, op);
        else if (relation == fUmlConstraints)
            setUmlConstraints((DbSMSUmlConstraint) neighbor, op);
        else
            super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the list of sub-copies associated to a DbSMSSemanticalObject's instance.
     * 
     * @return the list of sub-copies.
     **/
    public final DbRelationN getSubCopies() throws DbException {
        return (DbRelationN) get(fSubCopies);
    }

    /**
     * Gets the super-copy object associated to a DbSMSSemanticalObject's instance.
     * 
     * @return the super-copy object
     **/
    public final DbSMSSemanticalObject getSuperCopy() throws DbException {
        return (DbSMSSemanticalObject) get(fSuperCopy);
    }

    /**
     * Gets the list of target links associated to a DbSMSSemanticalObject's instance.
     * 
     * @return the list of target links.
     **/
    public final DbRelationN getTargetLinks() throws DbException {
        return (DbRelationN) get(fTargetLinks);
    }

    /**
     * Gets the list of source links associated to a DbSMSSemanticalObject's instance.
     * 
     * @return the list of source links.
     **/
    public final DbRelationN getSourceLinks() throws DbException {
        return (DbRelationN) get(fSourceLinks);
    }

    /**
     * Gets the uml stereotype object associated to a DbSMSSemanticalObject's instance.
     * 
     * @return the uml stereotype object
     **/
    public final DbSMSStereotype getUmlStereotype() throws DbException {
        return (DbSMSStereotype) get(fUmlStereotype);
    }

    /**
     * Gets the list of uml constraints associated to a DbSMSSemanticalObject's instance.
     * 
     * @return the list of uml constraints.
     **/
    public final DbRelationN getUmlConstraints() throws DbException {
        return (DbRelationN) get(fUmlConstraints);
    }

    /**
     * Gets the list of imports associated to a DbSMSSemanticalObject's instance.
     * 
     * @return the list of imports.
     **/
    public final DbRelationN getObjectImports() throws DbException {
        return (DbRelationN) get(fObjectImports);
    }

}
