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
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.jack.baseDb.util.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORColumn.html" >DbORColumn</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORField.html">DbORField</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html"
 * >DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORAttribute extends DbSMSStructuralFeature {

    // Meta

    public static final MetaRelation1 fType = new MetaRelation1(LocaleMgr.db.getString("type"), 0);
    public static final MetaField fLength = new MetaField(LocaleMgr.db.getString("length"));
    public static final MetaField fNbDecimal = new MetaField(LocaleMgr.db.getString("nbDecimal"));

    public static final MetaClass metaClass = new MetaClass(
            LocaleMgr.db.getString("DbORAttribute"), DbORAttribute.class, new MetaField[] { fType,
                    fLength, fNbDecimal }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSStructuralFeature.metaClass);

            fType.setJField(DbORAttribute.class.getDeclaredField("m_type"));
            fType.setFlags(MetaField.COPY_REFS | MetaField.INTEGRABLE_BY_NAME);
            fType.setScreenOrder("<initialValue");
            fType.setRendererPluginName("DbFullNameInTip;DbORTypeClassifier");
            fLength.setJField(DbORAttribute.class.getDeclaredField("m_length"));
            fNbDecimal.setJField(DbORAttribute.class.getDeclaredField("m_nbDecimal"));

            fType.setOppositeRel(DbORTypeClassifier.fTypedAttributes);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    // Instance variables
    DbORTypeClassifier m_type;
    SrInteger m_length;
    SrInteger m_nbDecimal;

    // Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORAttribute() {
    }

    /**
     * Creates an instance of DbORAttribute.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORAttribute(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        DbORDataModel dataModel = (DbORDataModel) getCompositeOfType(DbORDataModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(dataModel);
        setName(term.getTerm(metaClass));
    }

    /**
     * @return string
     **/
    public String getLengthNbDecimal() throws DbException {
        String lengthNbDecimal;

        Integer length = getLength();
        if (length == null)
            return null;
        lengthNbDecimal = "(" + length.toString(); // NOT LOCALIZABLE
        Integer nbDecimal = getNbDecimal();
        if (nbDecimal == null)
            lengthNbDecimal = lengthNbDecimal + ")"; // NOT LOCALIZABLE
        else
            lengthNbDecimal = lengthNbDecimal + ", " + nbDecimal.toString() + ")"; // NOT LOCALIZABLE

        return lengthNbDecimal;
    }

    // Setters

    /**
     * Sets the type object associated to a DbORAttribute's instance.
     * 
     * @param value
     *            the type object to be associated
     **/
    public final void setType(DbORTypeClassifier value) throws DbException {
        basicSet(fType, value);
    }

    /**
     * Sets the "length" property of a DbORAttribute's instance.
     * 
     * @param value
     *            the "length" property
     **/
    public final void setLength(Integer value) throws DbException {
        basicSet(fLength, value);
    }

    /**
     * Sets the "nbr. decimals" property of a DbORAttribute's instance.
     * 
     * @param value
     *            the "nbr. decimals" property
     **/
    public final void setNbDecimal(Integer value) throws DbException {
        basicSet(fNbDecimal, value);
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

    // Getters

    /**
     * Gets the type object associated to a DbORAttribute's instance.
     * 
     * @return the type object
     **/
    public final DbORTypeClassifier getType() throws DbException {
        return (DbORTypeClassifier) get(fType);
    }

    /**
     * Gets the "length" of a DbORAttribute's instance.
     * 
     * @return the "length"
     **/
    public final Integer getLength() throws DbException {
        return (Integer) get(fLength);
    }

    /**
     * Gets the "nbr. decimals" of a DbORAttribute's instance.
     * 
     * @return the "nbr. decimals"
     **/
    public final Integer getNbDecimal() throws DbException {
        return (Integer) get(fNbDecimal);
    }

}
