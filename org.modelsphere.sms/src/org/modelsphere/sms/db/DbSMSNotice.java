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

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../org/modelsphere/sms/be/db/DbBEUseCase.html">DbBEUseCase</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSPackage.html">DbSMSPackage</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbSMSNotice extends DbSMSSemanticalObject {

    //Meta

    public static final MetaRelationN fNoticeGos = new MetaRelationN(LocaleMgr.db
            .getString("noticeGos"), 0, MetaRelationN.cardN);
    public static final MetaField fExternalDocument = new MetaField(LocaleMgr.db
            .getString("externalDocument"));
    public static final MetaField fAlignment = new MetaField(LocaleMgr.db.getString("alignment"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbSMSNotice"),
            DbSMSNotice.class, new MetaField[] { fNoticeGos, fExternalDocument, fAlignment }, 0);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setIcon("dbsmsnotice.gif");

            fNoticeGos.setJField(DbSMSNotice.class.getDeclaredField("m_noticeGos"));
            fExternalDocument.setJField(DbSMSNotice.class.getDeclaredField("m_externalDocument"));
            fExternalDocument.setRendererPluginName("ExternalDocument");
            fAlignment.setJField(DbSMSNotice.class.getDeclaredField("m_alignment"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0L;

    //Instance variables
    DbRelationN m_noticeGos;
    String m_externalDocument;
    SMSHorizontalAlignment m_alignment;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSNotice() {
    }

    /**
     * Creates an instance of DbSMSNotice.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSNotice(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    //Setters

    /**
     * Sets the "external document" property of a DbSMSNotice's instance.
     * 
     * @param value
     *            the "external document" property
     **/
    public final void setExternalDocument(String value) throws DbException {
        basicSet(fExternalDocument, value);
    }

    /**
     * Sets the "alignment" property of a DbSMSNotice's instance.
     * 
     * @param value
     *            the "alignment" property
     **/
    public final void setAlignment(SMSHorizontalAlignment value) throws DbException {
        basicSet(fAlignment, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fNoticeGos)
                ((DbSMSNoticeGo) value).setNotice(this);
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

    //Getters

    /**
     * Gets the list of notice graphical objects associated to a DbSMSNotice's instance.
     * 
     * @return the list of notice graphical objects.
     **/
    public final DbRelationN getNoticeGos() throws DbException {
        return (DbRelationN) get(fNoticeGos);
    }

    /**
     * Gets the "external document" property of a DbSMSNotice's instance.
     * 
     * @return the "external document" property
     **/
    public final String getExternalDocument() throws DbException {
        return (String) get(fExternalDocument);
    }

    /**
     * Gets the "alignment" of a DbSMSNotice's instance.
     * 
     * @return the "alignment"
     **/
    public final SMSHorizontalAlignment getAlignment() throws DbException {
        return (SMSHorizontalAlignment) get(fAlignment);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
