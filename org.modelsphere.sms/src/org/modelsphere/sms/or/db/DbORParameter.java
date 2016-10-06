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
import javax.swing.Icon;
import org.modelsphere.sms.or.db.util.AnyORObject;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/oracle/db/DbORAParameter.html" >DbORAParameter</A>,
 * <A HREF="../../../../../org/modelsphere/sms/or/ibm/db/DbIBMParameter.html" >DbIBMParameter</A>,
 * <A HREF= "../../../../../org/modelsphere/sms/or/informix/db/DbINFParameter.html"
 * >DbINFParameter</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/generic/db/DbGEParameter.html" >DbGEParameter</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html"
 * >DbSMSObjectImport</A>.<br>
 **/
public abstract class DbORParameter extends DbSMSParameter {

    // Meta

    public static final MetaRelation1 fType = new MetaRelation1(LocaleMgr.db.getString("type"), 0);
    public static final MetaField fLength = new MetaField(LocaleMgr.db.getString("length"));
    public static final MetaField fNbDecimal = new MetaField(LocaleMgr.db.getString("nbDecimal"));
    public static final MetaField fReference = new MetaField(LocaleMgr.db.getString("reference"));

    public static final MetaClass metaClass = new MetaClass(
            LocaleMgr.db.getString("DbORParameter"), DbORParameter.class, new MetaField[] { fType,
                    fLength, fNbDecimal, fReference }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSParameter.metaClass);
            metaClass.setIcon("dborparameter.gif");

            fType.setJField(DbORParameter.class.getDeclaredField("m_type"));
            fType.setFlags(MetaField.COPY_REFS | MetaField.INTEGRABLE_BY_NAME);
            fType.setRendererPluginName("DbFullNameInTip;DbORTypeClassifier");
            fLength.setJField(DbORParameter.class.getDeclaredField("m_length"));
            fNbDecimal.setJField(DbORParameter.class.getDeclaredField("m_nbDecimal"));
            fReference.setJField(DbORParameter.class.getDeclaredField("m_reference"));

            fType.setOppositeRel(DbORTypeClassifier.fTypedParameters);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    private static Icon inIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbORParameter.class, "resources/dborparameterin.gif");
    private static Icon outIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbORParameter.class, "resources/dborparameterout.gif");
    private static Icon inOutIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbORParameter.class, "resources/dborparameterinout.gif");

    // Instance variables
    DbORTypeClassifier m_type;
    SrInteger m_length;
    SrInteger m_nbDecimal;
    boolean m_reference;

    // Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORParameter() {
    }

    /**
     * Creates an instance of DbORParameter.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORParameter(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setReference(Boolean.FALSE);
    }

    /**
     * @param form
     *            int
     * @return icon
     **/
    public final Icon getSemanticalIcon(int form) throws DbException {
        switch (getPassingConvention().getValue()) {
        case SMSPassingConvention.IN:
            return inIcon;
        case SMSPassingConvention.OUT:
            return outIcon;
        default:
            return inOutIcon;
        }
    }

    // Setters

    /**
     * Sets the type object associated to a DbORParameter's instance.
     * 
     * @param value
     *            the type object to be associated
     **/
    public final void setType(DbORTypeClassifier value) throws DbException {
        basicSet(fType, value);
    }

    /**
     * Sets the "length" property of a DbORParameter's instance.
     * 
     * @param value
     *            the "length" property
     **/
    public final void setLength(Integer value) throws DbException {
        basicSet(fLength, value);
    }

    /**
     * Sets the "nbr. decimals" property of a DbORParameter's instance.
     * 
     * @param value
     *            the "nbr. decimals" property
     **/
    public final void setNbDecimal(Integer value) throws DbException {
        basicSet(fNbDecimal, value);
    }

    /**
     * Sets the "reference" property of a DbORParameter's instance.
     * 
     * @param value
     *            the "reference" property
     **/
    public final void setReference(Boolean value) throws DbException {
        basicSet(fReference, value);
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
     * Gets the type object associated to a DbORParameter's instance.
     * 
     * @return the type object
     **/
    public final DbORTypeClassifier getType() throws DbException {
        return (DbORTypeClassifier) get(fType);
    }

    /**
     * Gets the "length" of a DbORParameter's instance.
     * 
     * @return the "length"
     **/
    public final Integer getLength() throws DbException {
        return (Integer) get(fLength);
    }

    /**
     * Gets the "nbr. decimals" of a DbORParameter's instance.
     * 
     * @return the "nbr. decimals"
     **/
    public final Integer getNbDecimal() throws DbException {
        return (Integer) get(fNbDecimal);
    }

    /**
     * Gets the "reference" property's Boolean value of a DbORParameter's instance.
     * 
     * @return the "reference" property's Boolean value
     * @deprecated use isReference() method instead
     **/
    public final Boolean getReference() throws DbException {
        return (Boolean) get(fReference);
    }

    /**
     * Tells whether a DbORParameter's instance is reference or not.
     * 
     * @return boolean
     **/
    public final boolean isReference() throws DbException {
        return getReference().booleanValue();
    }

}
