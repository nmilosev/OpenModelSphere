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
import org.modelsphere.sms.*;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.preference.DiagramAutomaticCreationOptionGroup;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.baseDb.util.Terminology;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEUseCase.html">DbBEUseCase</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEUseCaseGo.html">DbBEUseCaseGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEUseCaseGo.html">DbBEUseCaseGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEStoreGo.html">DbBEStoreGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEActorGo.html">DbBEActorGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEFlowGo.html">DbBEFlowGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEContextGo.html">DbBEContextGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSUserTextGo.html">DbSMSUserTextGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSStampGo.html">DbSMSStampGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSImageGo.html">DbSMSImageGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSPackageGo.html">DbSMSPackageGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSLineGo.html">DbSMSLineGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSFreeGraphicGo.html">DbSMSFreeGraphicGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSNoticeGo.html">DbSMSNoticeGo</A>.<br>
 **/
public final class DbBEDiagram extends DbSMSDiagram {

    //Meta

    public static final MetaRelation1 fNotation = new MetaRelation1(LocaleMgr.db
            .getString("notation"), 0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbBEDiagram"),
            DbBEDiagram.class, new MetaField[] { fNotation }, MetaClass.CLUSTER_ROOT
                    | MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSDiagram.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbBEUseCaseGo.metaClass,
                    DbBEUseCaseGo.metaClass, DbBEStoreGo.metaClass, DbBEActorGo.metaClass,
                    DbBEFlowGo.metaClass, DbBEContextGo.metaClass });
            metaClass.setIcon("dbbediagram.gif");

            fNotation.setJField(DbBEDiagram.class.getDeclaredField("m_notation"));
            fNotation.setFlags(MetaField.COPY_REFS);

            fNotation.setOppositeRel(DbBENotation.fDiagrams);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbBENotation m_notation;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEDiagram() {
    }

    /**
     * Creates an instance of DbBEDiagram.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbBEDiagram(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(getComposite());
        setName(term.getTerm(metaClass));
    }

    /**
     * @return style
     **/
    public final DbSMSStyle findStyle() throws DbException {
        DbObject semObj = this.getComposite();
        DbSMSStyle style = null;
        style = getStyle();
        if (style == null) {
            style = ((DbSMSProject) getProject()).getBeDefaultStyle();
        }
        return style;
    }

    /**
     * @return notation
     **/
    public final DbBENotation findNotation() throws DbException {

        DbBENotation notation = getNotation();
        if (notation == null) {
            notation = ((DbSMSProject) getProject()).getBeDefaultNotation();
        }
        return notation;

    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.BPM;

    }

    /**
     * @return notation
     **/
    public final DbBENotation findMasterNotation() throws DbException {

        DbBENotation notation = getNotation();
        if (notation == null) {
            notation = ((DbSMSProject) getProject()).getBeDefaultNotation();
        }

        int masterNotationID = notation.getMasterNotationID().intValue();
        DbEnumeration projectEnum = getProject().getComponents().elements(DbBENotation.metaClass);
        DbBENotation masterNotation = null;
        while (projectEnum.hasMoreElements()) {
            DbBENotation dbBENota = (DbBENotation) projectEnum.nextElement();
            if (dbBENota.getNotationID().intValue() == masterNotationID) {
                masterNotation = dbBENota;
                break;
            }
        }
        projectEnum.close();

        return masterNotation;
    }

    //Setters

    /**
     * Sets the notation object associated to a DbBEDiagram's instance.
     * 
     * @param value
     *            the notation object to be associated
     **/
    public final void setNotation(DbBENotation value) throws DbException {
        basicSet(fNotation, value);
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
     * Gets the notation object associated to a DbBEDiagram's instance.
     * 
     * @return the notation object
     **/
    public final DbBENotation getNotation() throws DbException {
        return (DbBENotation) get(fNotation);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
