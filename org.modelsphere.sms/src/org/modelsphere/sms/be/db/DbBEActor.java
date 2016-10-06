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
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.baseDb.util.Terminology;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEModel.html">DbBEModel</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEActorQualifier.html">DbBEActorQualifier</A>,
 * <A HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbBEActor extends DbSMSClassifier {

    //Meta

    public static final MetaRelationN fFirstEndFlows = new MetaRelationN(LocaleMgr.db
            .getString("firstEndFlows"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fSecondEndFlows = new MetaRelationN(LocaleMgr.db
            .getString("secondEndFlows"), 0, MetaRelationN.cardN);
    public static final MetaField fIdentifier = new MetaField(LocaleMgr.db.getString("identifier"));
    public static final MetaField fDefinition = new MetaField(LocaleMgr.db.getString("definition"));
    public static final MetaField fControl = new MetaField(LocaleMgr.db.getString("control"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbBEActor"),
            DbBEActor.class, new MetaField[] { fFirstEndFlows, fSecondEndFlows, fIdentifier,
                    fDefinition, fControl }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSClassifier.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbBEActorQualifier.metaClass });
            metaClass.setIcon("dbbeentity.gif");

            fFirstEndFlows.setJField(DbBEActor.class.getDeclaredField("m_firstEndFlows"));
            fSecondEndFlows.setJField(DbBEActor.class.getDeclaredField("m_secondEndFlows"));
            fIdentifier.setJField(DbBEActor.class.getDeclaredField("m_identifier"));
            fDefinition.setJField(DbBEActor.class.getDeclaredField("m_definition"));
            fDefinition.setRendererPluginName("LookupDescription");
            fControl.setJField(DbBEActor.class.getDeclaredField("m_control"));

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    public static final MetaField[] notationFields = new MetaField[] { fName, fPhysicalName,
            fAlias, fDescription, fDefinition, fIdentifier, fUmlStereotype };

    //Instance variables
    DbRelationN m_firstEndFlows;
    DbRelationN m_secondEndFlows;
    SrInteger m_identifier;
    String m_definition;
    boolean m_control;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEActor() {
    }

    /**
     * Creates an instance of DbBEActor.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbBEActor(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        DbBEModel model = (DbBEModel) getCompositeOfType(DbBEModel.metaClass);
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        Terminology term = terminologyUtil.findModelTerminology(model);
        setName(term.getTerm(metaClass));
        setIdentifier(((DbBEModel) getComposite()).getNextActorIdentifier());
    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.BPM;

    }

    //Setters

    /**
     * Sets the "identifier" property of a DbBEActor's instance.
     * 
     * @param value
     *            the "identifier" property
     **/
    public final void setIdentifier(Integer value) throws DbException {
        basicSet(fIdentifier, value);
    }

    /**
     * Sets the "definition" property of a DbBEActor's instance.
     * 
     * @param value
     *            the "definition" property
     **/
    public final void setDefinition(String value) throws DbException {
        basicSet(fDefinition, value);
    }

    /**
     * Sets the "control" property of a DbBEActor's instance.
     * 
     * @param value
     *            the "control" property
     **/
    public final void setControl(Boolean value) throws DbException {
        basicSet(fControl, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fFirstEndFlows)
                ((DbBEFlow) value).setFirstEnd(this);
            else if (metaField == fSecondEndFlows)
                ((DbBEFlow) value).setSecondEnd(this);
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
     * Gets the list of flows associated to a DbBEActor's instance.
     * 
     * @return the list of flows.
     **/
    public final DbRelationN getFirstEndFlows() throws DbException {
        return (DbRelationN) get(fFirstEndFlows);
    }

    /**
     * Gets the list of flows associated to a DbBEActor's instance.
     * 
     * @return the list of flows.
     **/
    public final DbRelationN getSecondEndFlows() throws DbException {
        return (DbRelationN) get(fSecondEndFlows);
    }

    /**
     * Gets the "identifier" of a DbBEActor's instance.
     * 
     * @return the "identifier"
     **/
    public final Integer getIdentifier() throws DbException {
        return (Integer) get(fIdentifier);
    }

    /**
     * Gets the "definition" property of a DbBEActor's instance.
     * 
     * @return the "definition" property
     **/
    public final String getDefinition() throws DbException {
        return (String) get(fDefinition);
    }

    /**
     * Gets the "control" property's Boolean value of a DbBEActor's instance.
     * 
     * @return the "control" property's Boolean value
     * @deprecated use isControl() method instead
     **/
    public final Boolean getControl() throws DbException {
        return (Boolean) get(fControl);
    }

    /**
     * Tells whether a DbBEActor's instance is control or not.
     * 
     * @return boolean
     **/
    public final boolean isControl() throws DbException {
        return getControl().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
