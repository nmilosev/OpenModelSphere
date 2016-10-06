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
package org.modelsphere.sms.be.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.be.db.srtypes.*;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEDiagram.html">DbBEDiagram</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEContextCell.html">DbBEContextCell</A>.<br>
 **/
public final class DbBEContextGo extends DbSMSClassifierGo {

    //Meta

    public static final MetaField fHide = new MetaField(LocaleMgr.db.getString("hide"));

    public static final MetaClass metaClass = new MetaClass(
            LocaleMgr.db.getString("DbBEContextGo"), DbBEContextGo.class,
            new MetaField[] { fHide }, MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSClassifierGo.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbBEContextCell.metaClass });
            metaClass.setIcon("dbbecontextgo.gif");

            fHide.setJField(DbBEContextGo.class.getDeclaredField("m_hide"));
            fHide.setVisibleInScreen(false);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    boolean m_hide;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEContextGo() {
    }

    /**
     * Creates an instance of DbBEContextGo.
     * 
     * @param composite
     *            org.modelsphere.sms.db.DbSMSDiagram
     * @param classifier
     *            org.modelsphere.sms.db.DbSMSClassifier
     **/
    public DbBEContextGo(DbSMSDiagram composite, DbSMSClassifier classifier) throws DbException {
        super(composite, classifier);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        this.setAutoFit(Boolean.FALSE);
        this.setHide(Boolean.FALSE);
        DbBEModel model = (DbBEModel) getCompositeOfType(DbBEModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(model);
        setName(term.getTerm(metaClass));
    }

    /**
     * @return boolean
     **/
    public boolean isDeletable() throws DbException {
        return false;
    }

    //Setters

    /**
     * Sets the "hide" property of a DbBEContextGo's instance.
     * 
     * @param value
     *            the "hide" property
     **/
    public final void setHide(Boolean value) throws DbException {
        basicSet(fHide, value);
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
     * Gets the "hide" property's Boolean value of a DbBEContextGo's instance.
     * 
     * @return the "hide" property's Boolean value
     * @deprecated use isHide() method instead
     **/
    public final Boolean getHide() throws DbException {
        return (Boolean) get(fHide);
    }

    /**
     * Tells whether a DbBEContextGo's instance is hide or not.
     * 
     * @return boolean
     **/
    public final boolean isHide() throws DbException {
        return getHide().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
