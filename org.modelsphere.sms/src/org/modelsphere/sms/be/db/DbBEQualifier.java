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
import org.modelsphere.sms.SMSFilter;
import javax.swing.Icon;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.sms.be.BEModule;
import org.modelsphere.sms.screen.plugins.MultiDbBEQualifierIconEditor;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEModel.html">DbBEModel</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbBEQualifier extends DbSMSSemanticalObject {

    //Meta

    public static final MetaRelationN fQualifiedUseCases = new MetaRelationN(LocaleMgr.db
            .getString("qualifiedUseCases"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fQualifiedActors = new MetaRelationN(LocaleMgr.db
            .getString("qualifiedActors"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fQualifiedStores = new MetaRelationN(LocaleMgr.db
            .getString("qualifiedStores"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fQualifiedFlows = new MetaRelationN(LocaleMgr.db
            .getString("qualifiedFlows"), 0, MetaRelationN.cardN);
    public static final MetaField fQualifierId = new MetaField(LocaleMgr.db
            .getString("qualifierId"));
    public static final MetaField fIcon = new MetaField(LocaleMgr.db.getString("icon"));
    public static final MetaField fIdentifier = new MetaField(LocaleMgr.db.getString("identifier"));

    public static final MetaClass metaClass = new MetaClass(
            LocaleMgr.db.getString("DbBEQualifier"), DbBEQualifier.class, new MetaField[] {
                    fQualifiedUseCases, fQualifiedActors, fQualifiedStores, fQualifiedFlows,
                    fQualifierId, fIcon, fIdentifier }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSSemanticalObject.metaClass);
            metaClass.setIcon("dbbequalifier.gif");

            fQualifiedUseCases.setJField(DbBEQualifier.class
                    .getDeclaredField("m_qualifiedUseCases"));
            fQualifiedActors.setJField(DbBEQualifier.class.getDeclaredField("m_qualifiedActors"));
            fQualifiedStores.setJField(DbBEQualifier.class.getDeclaredField("m_qualifiedStores"));
            fQualifiedFlows.setJField(DbBEQualifier.class.getDeclaredField("m_qualifiedFlows"));
            fQualifierId.setJField(DbBEQualifier.class.getDeclaredField("m_qualifierId"));
            fQualifierId.setVisibleInScreen(false);
            fQualifierId.setEditable(false);
            fIcon.setJField(DbBEQualifier.class.getDeclaredField("m_icon"));
            fIcon.setRendererPluginName("SrImage;DbBEQualifierIcon");
            fIdentifier.setJField(DbBEQualifier.class.getDeclaredField("m_identifier"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    DbRelationN m_qualifiedUseCases;
    DbRelationN m_qualifiedActors;
    DbRelationN m_qualifiedStores;
    DbRelationN m_qualifiedFlows;
    String m_qualifierId;
    SrImage m_icon;
    String m_identifier;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEQualifier() {
    }

    /**
     * Creates an instance of DbBEQualifier.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbBEQualifier(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setName(LocaleMgr.misc.getString("qualifier"));

    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.BPM;

    }

    /**
     * @param value
     *            java.awt.Image
     **/
    public final void setIcon(Image value) throws DbException {
        basicSet(fIcon, value);
        if (value == null)
            return;
        Object[] imageList = MultiDbBEQualifierIconEditor.BUILTIN_IMAGES;
        for (int i = 0; i < imageList.length; i++) {
            // if the icon we are setting is one of these images
            if (value.equals(imageList[i])) {
                //set the index as the qualifier Id
                String qualifierId = new Integer(i).toString();
                while (qualifierId.length() < 4)
                    qualifierId = "0" + qualifierId; // NOT LOCALIZABLE 
                basicSet(fQualifierId, qualifierId);
                break;
            }
        }
    }

    /**
     * @param metafield
     *            org.modelsphere.jack.baseDb.meta.MetaField
     * @param value
     *            java.lang.Object
     **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fQualifiedUseCases)
                ((DbBEUseCaseQualifier) value).setQualifier(this);
            else if (metaField == fQualifiedActors)
                ((DbBEActorQualifier) value).setQualifier(this);
            else if (metaField == fQualifiedStores)
                ((DbBEStoreQualifier) value).setQualifier(this);
            else if (metaField == fQualifiedFlows)
                ((DbBEFlowQualifier) value).setQualifier(this);
            else if (metaField == fIcon)
                setIcon((Image) value);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    //Setters

    /**
     * Sets the "qualifier id" property of a DbBEQualifier's instance.
     * 
     * @param value
     *            the "qualifier id" property
     **/
    public final void setQualifierId(String value) throws DbException {
        basicSet(fQualifierId, value);
    }

    /**
     * Sets the "identifier" property of a DbBEQualifier's instance.
     * 
     * @param value
     *            the "identifier" property
     **/
    public final void setIdentifier(String value) throws DbException {
        basicSet(fIdentifier, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the list of processes associated to a DbBEQualifier's instance.
     * 
     * @return the list of processes.
     **/
    public final DbRelationN getQualifiedUseCases() throws DbException {
        return (DbRelationN) get(fQualifiedUseCases);
    }

    /**
     * Gets the list of external entities associated to a DbBEQualifier's instance.
     * 
     * @return the list of external entities.
     **/
    public final DbRelationN getQualifiedActors() throws DbException {
        return (DbRelationN) get(fQualifiedActors);
    }

    /**
     * Gets the list of stores associated to a DbBEQualifier's instance.
     * 
     * @return the list of stores.
     **/
    public final DbRelationN getQualifiedStores() throws DbException {
        return (DbRelationN) get(fQualifiedStores);
    }

    /**
     * Gets the list of flows associated to a DbBEQualifier's instance.
     * 
     * @return the list of flows.
     **/
    public final DbRelationN getQualifiedFlows() throws DbException {
        return (DbRelationN) get(fQualifiedFlows);
    }

    /**
     * Gets the "qualifier id" property of a DbBEQualifier's instance.
     * 
     * @return the "qualifier id" property
     **/
    public final String getQualifierId() throws DbException {
        return (String) get(fQualifierId);
    }

    /**
     * Gets the "icon" of a DbBEQualifier's instance.
     * 
     * @return the "icon"
     **/
    public final Image getIcon() throws DbException {
        return (Image) get(fIcon);
    }

    /**
     * Gets the "identifier" property of a DbBEQualifier's instance.
     * 
     * @return the "identifier" property
     **/
    public final String getIdentifier() throws DbException {
        return (String) get(fIdentifier);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
