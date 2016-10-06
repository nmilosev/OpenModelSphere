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
package org.modelsphere.sms.or.informix.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.informix.db.srtypes.*;
import org.modelsphere.sms.or.informix.international.LocaleMgr;
import org.modelsphere.sms.or.db.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.or.db.util.AnyORObject;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFOperationLibrary.html"
 * >DbINFOperationLibrary</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../../org/modelsphere/sms/or/informix/db/DbINFParameter.html"
 * >DbINFParameter</A>, <A
 * HREF="../../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbINFProcedure extends DbORProcedure {

    //Meta

    public static final MetaField fDba = new MetaField(LocaleMgr.db.getString("dba"));
    public static final MetaField fReturnTypeLength = new MetaField(LocaleMgr.db
            .getString("returnTypeLength"));
    public static final MetaField fReturnTypeNbDecimal = new MetaField(LocaleMgr.db
            .getString("returnTypeNbDecimal"));
    public static final MetaField fSpecificName = new MetaField(LocaleMgr.db
            .getString("specificName"));
    public static final MetaField fExternalName = new MetaField(LocaleMgr.db
            .getString("externalName"));
    public static final MetaField fLanguage = new MetaField(LocaleMgr.db.getString("language"));
    public static final MetaField fDocument = new MetaField(LocaleMgr.db.getString("document"));
    public static final MetaField fListingFileName = new MetaField(LocaleMgr.db
            .getString("listingFileName"));
    public static final MetaField fVariant = new MetaField(LocaleMgr.db.getString("variant"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbINFProcedure"), DbINFProcedure.class, new MetaField[] { fDba,
            fReturnTypeLength, fReturnTypeNbDecimal, fSpecificName, fExternalName, fLanguage,
            fDocument, fListingFileName, fVariant }, MetaClass.MATCHABLE);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbORProcedure.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbINFParameter.metaClass });

            fDba.setJField(DbINFProcedure.class.getDeclaredField("m_dba"));
            fDba.setScreenOrder("<function");
            fReturnTypeLength
                    .setJField(DbINFProcedure.class.getDeclaredField("m_returnTypeLength"));
            fReturnTypeLength.setScreenOrder("<javaMethod");
            fReturnTypeNbDecimal.setJField(DbINFProcedure.class
                    .getDeclaredField("m_returnTypeNbDecimal"));
            fSpecificName.setJField(DbINFProcedure.class.getDeclaredField("m_specificName"));
            fExternalName.setJField(DbINFProcedure.class.getDeclaredField("m_externalName"));
            fLanguage.setJField(DbINFProcedure.class.getDeclaredField("m_language"));
            fDocument.setJField(DbINFProcedure.class.getDeclaredField("m_document"));
            fDocument.setScreenOrder(">javaMethod");
            fDocument.setRendererPluginName("LookupDescription");
            fListingFileName.setJField(DbINFProcedure.class.getDeclaredField("m_listingFileName"));
            fVariant.setJField(DbINFProcedure.class.getDeclaredField("m_variant"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_dba;
    SrInteger m_returnTypeLength;
    SrInteger m_returnTypeNbDecimal;
    String m_specificName;
    String m_externalName;
    INFProcedureLanguage m_language;
    String m_document;
    String m_listingFileName;
    INFProcedureVariant m_variant;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbINFProcedure() {
    }

    /**
     * Creates an instance of DbINFProcedure.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbINFProcedure(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setDba(Boolean.FALSE);
        setName(LocaleMgr.misc.getString("procedure"));
    }

    //Setters

    /**
     * Sets the "dba privileges" property of a DbINFProcedure's instance.
     * 
     * @param value
     *            the "dba privileges" property
     **/
    public final void setDba(Boolean value) throws DbException {
        basicSet(fDba, value);
    }

    /**
     * Sets the "return type length" property of a DbINFProcedure's instance.
     * 
     * @param value
     *            the "return type length" property
     **/
    public final void setReturnTypeLength(Integer value) throws DbException {
        basicSet(fReturnTypeLength, value);
    }

    /**
     * Sets the "return type nbr. decimals" property of a DbINFProcedure's instance.
     * 
     * @param value
     *            the "return type nbr. decimals" property
     **/
    public final void setReturnTypeNbDecimal(Integer value) throws DbException {
        basicSet(fReturnTypeNbDecimal, value);
    }

    /**
     * Sets the "specific name" property of a DbINFProcedure's instance.
     * 
     * @param value
     *            the "specific name" property
     **/
    public final void setSpecificName(String value) throws DbException {
        basicSet(fSpecificName, value);
    }

    /**
     * Sets the "external name" property of a DbINFProcedure's instance.
     * 
     * @param value
     *            the "external name" property
     **/
    public final void setExternalName(String value) throws DbException {
        basicSet(fExternalName, value);
    }

    /**
     * Sets the "language" property of a DbINFProcedure's instance.
     * 
     * @param value
     *            the "language" property
     **/
    public final void setLanguage(INFProcedureLanguage value) throws DbException {
        basicSet(fLanguage, value);
    }

    /**
     * Sets the "document" property of a DbINFProcedure's instance.
     * 
     * @param value
     *            the "document" property
     **/
    public final void setDocument(String value) throws DbException {
        basicSet(fDocument, value);
    }

    /**
     * Sets the "listing file name" property of a DbINFProcedure's instance.
     * 
     * @param value
     *            the "listing file name" property
     **/
    public final void setListingFileName(String value) throws DbException {
        basicSet(fListingFileName, value);
    }

    /**
     * Sets the "variant" property of a DbINFProcedure's instance.
     * 
     * @param value
     *            the "variant" property
     **/
    public final void setVariant(INFProcedureVariant value) throws DbException {
        basicSet(fVariant, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the "dba privileges" property's Boolean value of a DbINFProcedure's instance.
     * 
     * @return the "dba privileges" property's Boolean value
     * @deprecated use isDba() method instead
     **/
    public final Boolean getDba() throws DbException {
        return (Boolean) get(fDba);
    }

    /**
     * Tells whether a DbINFProcedure's instance is dba or not.
     * 
     * @return boolean
     **/
    public final boolean isDba() throws DbException {
        return getDba().booleanValue();
    }

    /**
     * Gets the "return type length" of a DbINFProcedure's instance.
     * 
     * @return the "return type length"
     **/
    public final Integer getReturnTypeLength() throws DbException {
        return (Integer) get(fReturnTypeLength);
    }

    /**
     * Gets the "return type nbr. decimals" of a DbINFProcedure's instance.
     * 
     * @return the "return type nbr. decimals"
     **/
    public final Integer getReturnTypeNbDecimal() throws DbException {
        return (Integer) get(fReturnTypeNbDecimal);
    }

    /**
     * Gets the "specific name" property of a DbINFProcedure's instance.
     * 
     * @return the "specific name" property
     **/
    public final String getSpecificName() throws DbException {
        return (String) get(fSpecificName);
    }

    /**
     * Gets the "external name" property of a DbINFProcedure's instance.
     * 
     * @return the "external name" property
     **/
    public final String getExternalName() throws DbException {
        return (String) get(fExternalName);
    }

    /**
     * Gets the "language" of a DbINFProcedure's instance.
     * 
     * @return the "language"
     **/
    public final INFProcedureLanguage getLanguage() throws DbException {
        return (INFProcedureLanguage) get(fLanguage);
    }

    /**
     * Gets the "document" property of a DbINFProcedure's instance.
     * 
     * @return the "document" property
     **/
    public final String getDocument() throws DbException {
        return (String) get(fDocument);
    }

    /**
     * Gets the "listing file name" property of a DbINFProcedure's instance.
     * 
     * @return the "listing file name" property
     **/
    public final String getListingFileName() throws DbException {
        return (String) get(fListingFileName);
    }

    /**
     * Gets the "variant" of a DbINFProcedure's instance.
     * 
     * @return the "variant"
     **/
    public final INFProcedureVariant getVariant() throws DbException {
        return (INFProcedureVariant) get(fVariant);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
