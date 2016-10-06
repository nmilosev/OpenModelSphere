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
package org.modelsphere.sms.or.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.or.oracle.db.*;
import org.modelsphere.sms.or.generic.db.*;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.DbOOMethod;
import org.modelsphere.sms.oo.db.DbOOParameter;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A HREF="../../../../../org/modelsphere/sms/or/db/DbORDomainModel.html"
 * >DbORDomainModel</A>.<br>
 * <b>Components : </b><A HREF="../../../../../org/modelsphere/sms/or/db/DbORAllowableValue.html"
 * >DbORAllowableValue</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORField.html">DbORField</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html"> DbSMSObjectImport</A>.<br>
 **/
public final class DbORDomain extends DbORTypeClassifier {

    // Meta

    public static final MetaField fCategory = new MetaField(LocaleMgr.db.getString("category"));
    public static final MetaField fOrderedCollection = new MetaField(LocaleMgr.db
            .getString("orderedCollection"));
    public static final MetaRelation1 fSourceType = new MetaRelation1(LocaleMgr.db
            .getString("sourceType"), 0);
    public static final MetaRelation1 fUser = new MetaRelation1(LocaleMgr.db.getString("user"), 0);
    public static final MetaField fLength = new MetaField(LocaleMgr.db.getString("length"));
    public static final MetaField fNbDecimal = new MetaField(LocaleMgr.db.getString("nbDecimal"));
    public static final MetaField fNull = new MetaField(LocaleMgr.db.getString("null"));
    public static final MetaField fReference = new MetaField(LocaleMgr.db.getString("reference"));
    public static final MetaField fCaseSensitive = new MetaField(LocaleMgr.db
            .getString("caseSensitive"));
    public static final MetaRelationN fSubDomains = new MetaRelationN(LocaleMgr.db
            .getString("subDomains"), 0, MetaRelationN.cardN);
    public static final MetaRelation1 fSuperDomain = new MetaRelation1(LocaleMgr.db
            .getString("superDomain"), 0);
    public static final MetaRelation1 fOoSourceType = new MetaRelation1(LocaleMgr.db
            .getString("ooSourceType"), 0);
    public static final MetaRelationN fParameters = new MetaRelationN(LocaleMgr.db
            .getString("parameters"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fMethods = new MetaRelationN(LocaleMgr.db
            .getString("methods"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fFields = new MetaRelationN(LocaleMgr.db.getString("fields"),
            0, MetaRelationN.cardN);
    public static final MetaField fPropagated = new MetaField(LocaleMgr.db.getString("propagated"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORDomain"),
            DbORDomain.class, new MetaField[] { fCategory, fOrderedCollection, fSourceType, fUser,
                    fLength, fNbDecimal, fNull, fReference, fCaseSensitive, fSubDomains,
                    fSuperDomain, fOoSourceType, fParameters, fMethods, fFields, fPropagated },
            MetaClass.MATCHABLE | MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORTypeClassifier.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbORAllowableValue.metaClass,
                    DbORField.metaClass });
            metaClass.setIcon("dbordomain.gif");

            fCategory.setJField(DbORDomain.class.getDeclaredField("m_category"));
            fOrderedCollection.setJField(DbORDomain.class.getDeclaredField("m_orderedCollection"));
            fSourceType.setJField(DbORDomain.class.getDeclaredField("m_sourceType"));
            fSourceType.setFlags(MetaField.COPY_REFS | MetaField.INTEGRABLE_BY_NAME);
            fSourceType.setRendererPluginName("DbFullNameInTip;DbORTypeClassifier");
            fUser.setJField(DbORDomain.class.getDeclaredField("m_user"));
            fUser.setFlags(MetaField.COPY_REFS);
            fLength.setJField(DbORDomain.class.getDeclaredField("m_length"));
            fNbDecimal.setJField(DbORDomain.class.getDeclaredField("m_nbDecimal"));
            fNull.setJField(DbORDomain.class.getDeclaredField("m_null"));
            fReference.setJField(DbORDomain.class.getDeclaredField("m_reference"));
            fCaseSensitive.setJField(DbORDomain.class.getDeclaredField("m_caseSensitive"));
            fSubDomains.setJField(DbORDomain.class.getDeclaredField("m_subDomains"));
            fSuperDomain.setJField(DbORDomain.class.getDeclaredField("m_superDomain"));
            fOoSourceType.setJField(DbORDomain.class.getDeclaredField("m_ooSourceType"));
            fParameters.setJField(DbORDomain.class.getDeclaredField("m_parameters"));
            fMethods.setJField(DbORDomain.class.getDeclaredField("m_methods"));
            fFields.setJField(DbORDomain.class.getDeclaredField("m_fields"));
            fPropagated.setJField(DbORDomain.class.getDeclaredField("m_propagated"));
            fPropagated.setRendererPluginName("Boolean");

            fSourceType.setOppositeRel(DbORTypeClassifier.fDomains);
            fUser.setOppositeRel(DbORUser.fDomains);
            fSuperDomain.setOppositeRel(DbORDomain.fSubDomains);
            fOoSourceType.setOppositeRel(DbOOAdt.fUsedByDomains);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    // Instance variables
    ORDomainCategory m_category;
    boolean m_orderedCollection;
    DbORTypeClassifier m_sourceType;
    DbORUser m_user;
    SrInteger m_length;
    SrInteger m_nbDecimal;
    boolean m_null;
    boolean m_reference;
    boolean m_caseSensitive;
    DbRelationN m_subDomains;
    DbORDomain m_superDomain;
    DbOOAdt m_ooSourceType;
    DbRelationN m_parameters;
    DbRelationN m_methods;
    DbRelationN m_fields;
    boolean m_propagated;

    // Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORDomain() {
    }

    /**
     * Creates an instance of DbORDomain.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORDomain(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setCategory(ORDomainCategory.getInstance(ORDomainCategory.DOMAIN));
        setOrderedCollection(Boolean.FALSE);
        setNull(Boolean.TRUE);
        setReference(Boolean.FALSE);
        setCaseSensitive(Boolean.FALSE);
        setPropagated(Boolean.FALSE);
        setName(LocaleMgr.misc.getString("domain"));
    }

    // Setters

    /**
     * Sets the "category" property of a DbORDomain's instance.
     * 
     * @param value
     *            the "category" property
     **/
    public final void setCategory(ORDomainCategory value) throws DbException {
        basicSet(fCategory, value);
    }

    /**
     * Sets the "ordered collection" property of a DbORDomain's instance.
     * 
     * @param value
     *            the "ordered collection" property
     **/
    public final void setOrderedCollection(Boolean value) throws DbException {
        basicSet(fOrderedCollection, value);
    }

    /**
     * Sets the source type object associated to a DbORDomain's instance.
     * 
     * @param value
     *            the source type object to be associated
     **/
    public final void setSourceType(DbORTypeClassifier value) throws DbException {
        basicSet(fSourceType, value);
    }

    /**
     * Sets the user object associated to a DbORDomain's instance.
     * 
     * @param value
     *            the user object to be associated
     **/
    public final void setUser(DbORUser value) throws DbException {
        basicSet(fUser, value);
    }

    /**
     * Sets the "length" property of a DbORDomain's instance.
     * 
     * @param value
     *            the "length" property
     **/
    public final void setLength(Integer value) throws DbException {
        basicSet(fLength, value);
    }

    /**
     * Sets the "nbr. decimals" property of a DbORDomain's instance.
     * 
     * @param value
     *            the "nbr. decimals" property
     **/
    public final void setNbDecimal(Integer value) throws DbException {
        basicSet(fNbDecimal, value);
    }

    /**
     * Sets the "null possible" property of a DbORDomain's instance.
     * 
     * @param value
     *            the "null possible" property
     **/
    public final void setNull(Boolean value) throws DbException {
        basicSet(fNull, value);
    }

    /**
     * Sets the "reference" property of a DbORDomain's instance.
     * 
     * @param value
     *            the "reference" property
     **/
    public final void setReference(Boolean value) throws DbException {
        basicSet(fReference, value);
    }

    /**
     * Sets the "case sensitive" property of a DbORDomain's instance.
     * 
     * @param value
     *            the "case sensitive" property
     **/
    public final void setCaseSensitive(Boolean value) throws DbException {
        basicSet(fCaseSensitive, value);
    }

    /**
     * Sets the super domain object associated to a DbORDomain's instance.
     * 
     * @param value
     *            the super domain object to be associated
     **/
    public final void setSuperDomain(DbORDomain value) throws DbException {
        basicSet(fSuperDomain, value);
    }

    /**
     * Sets the oo source type object associated to a DbORDomain's instance.
     * 
     * @param value
     *            the oo source type object to be associated
     **/
    public final void setOoSourceType(DbOOAdt value) throws DbException {
        basicSet(fOoSourceType, value);
    }

    /**
     * Sets the "propagated" property of a DbORDomain's instance.
     * 
     * @param value
     *            the "propagated" property
     **/
    public final void setPropagated(Boolean value) throws DbException {
        basicSet(fPropagated, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fSubDomains)
                ((DbORDomain) value).setSuperDomain(this);
            else if (metaField == fParameters)
                ((DbOOParameter) value).setDomain(this);
            else if (metaField == fMethods)
                ((DbOOMethod) value).setDomain(this);
            else if (metaField == fFields)
                ((DbOODataMember) value).setDomain(this);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        super.set(relation, neighbor, op);
    }

    // Getters

    /**
     * Gets the "category" of a DbORDomain's instance.
     * 
     * @return the "category"
     **/
    public final ORDomainCategory getCategory() throws DbException {
        return (ORDomainCategory) get(fCategory);
    }

    /**
     * Gets the "ordered collection" property's Boolean value of a DbORDomain's instance.
     * 
     * @return the "ordered collection" property's Boolean value
     * @deprecated use isOrderedCollection() method instead
     **/
    public final Boolean getOrderedCollection() throws DbException {
        return (Boolean) get(fOrderedCollection);
    }

    /**
     * Tells whether a DbORDomain's instance is orderedCollection or not.
     * 
     * @return boolean
     **/
    public final boolean isOrderedCollection() throws DbException {
        return getOrderedCollection().booleanValue();
    }

    /**
     * Gets the source type object associated to a DbORDomain's instance.
     * 
     * @return the source type object
     **/
    public final DbORTypeClassifier getSourceType() throws DbException {
        return (DbORTypeClassifier) get(fSourceType);
    }

    /**
     * Gets the user object associated to a DbORDomain's instance.
     * 
     * @return the user object
     **/
    public final DbORUser getUser() throws DbException {
        return (DbORUser) get(fUser);
    }

    /**
     * Gets the "length" of a DbORDomain's instance.
     * 
     * @return the "length"
     **/
    public final Integer getLength() throws DbException {
        return (Integer) get(fLength);
    }

    /**
     * Gets the "nbr. decimals" of a DbORDomain's instance.
     * 
     * @return the "nbr. decimals"
     **/
    public final Integer getNbDecimal() throws DbException {
        return (Integer) get(fNbDecimal);
    }

    /**
     * Gets the "null possible" property's Boolean value of a DbORDomain's instance.
     * 
     * @return the "null possible" property's Boolean value
     * @deprecated use isNull() method instead
     **/
    public final Boolean getNull() throws DbException {
        return (Boolean) get(fNull);
    }

    /**
     * Tells whether a DbORDomain's instance is null or not.
     * 
     * @return boolean
     **/
    public final boolean isNull() throws DbException {
        return getNull().booleanValue();
    }

    /**
     * Gets the "reference" property's Boolean value of a DbORDomain's instance.
     * 
     * @return the "reference" property's Boolean value
     * @deprecated use isReference() method instead
     **/
    public final Boolean getReference() throws DbException {
        return (Boolean) get(fReference);
    }

    /**
     * Tells whether a DbORDomain's instance is reference or not.
     * 
     * @return boolean
     **/
    public final boolean isReference() throws DbException {
        return getReference().booleanValue();
    }

    /**
     * Gets the "case sensitive" property's Boolean value of a DbORDomain's instance.
     * 
     * @return the "case sensitive" property's Boolean value
     * @deprecated use isCaseSensitive() method instead
     **/
    public final Boolean getCaseSensitive() throws DbException {
        return (Boolean) get(fCaseSensitive);
    }

    /**
     * Tells whether a DbORDomain's instance is caseSensitive or not.
     * 
     * @return boolean
     **/
    public final boolean isCaseSensitive() throws DbException {
        return getCaseSensitive().booleanValue();
    }

    /**
     * Gets the list of sub domains associated to a DbORDomain's instance.
     * 
     * @return the list of sub domains.
     **/
    public final DbRelationN getSubDomains() throws DbException {
        return (DbRelationN) get(fSubDomains);
    }

    /**
     * Gets the super domain object associated to a DbORDomain's instance.
     * 
     * @return the super domain object
     **/
    public final DbORDomain getSuperDomain() throws DbException {
        return (DbORDomain) get(fSuperDomain);
    }

    /**
     * Gets the oo source type object associated to a DbORDomain's instance.
     * 
     * @return the oo source type object
     **/
    public final DbOOAdt getOoSourceType() throws DbException {
        return (DbOOAdt) get(fOoSourceType);
    }

    /**
     * Gets the list of parameters associated to a DbORDomain's instance.
     * 
     * @return the list of parameters.
     **/
    public final DbRelationN getParameters() throws DbException {
        return (DbRelationN) get(fParameters);
    }

    /**
     * Gets the list of methods associated to a DbORDomain's instance.
     * 
     * @return the list of methods.
     **/
    public final DbRelationN getMethods() throws DbException {
        return (DbRelationN) get(fMethods);
    }

    /**
     * Gets the list of fields associated to a DbORDomain's instance.
     * 
     * @return the list of fields.
     **/
    public final DbRelationN getFields() throws DbException {
        return (DbRelationN) get(fFields);
    }

    /**
     * Gets the "propagated" property's Boolean value of a DbORDomain's instance.
     * 
     * @return the "propagated" property's Boolean value
     * @deprecated use isPropagated() method instead
     **/
    public final Boolean getPropagated() throws DbException {
        return (Boolean) get(fPropagated);
    }

    /**
     * Tells whether a DbORDomain's instance is propagated or not.
     * 
     * @return boolean
     **/
    public final boolean isPropagated() throws DbException {
        return getPropagated().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
