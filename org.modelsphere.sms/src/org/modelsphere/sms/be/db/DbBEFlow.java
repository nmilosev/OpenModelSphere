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
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.SMSFilter;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEUseCase.html">DbBEUseCase</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEFlowQualifier.html">DbBEFlowQualifier</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbBEFlow extends DbSMSAbstractRelationship {

    //Meta

    public static final MetaChoice fFirstEnd = new MetaChoice(LocaleMgr.db.getString("firstEnd"), 0);
    public static final MetaChoice fSecondEnd = new MetaChoice(LocaleMgr.db.getString("secondEnd"),
            0);
    public static final MetaField fIdentifier = new MetaField(LocaleMgr.db.getString("identifier"));
    public static final MetaField fControl = new MetaField(LocaleMgr.db.getString("control"));
    public static final MetaField fFrequency = new MetaField(LocaleMgr.db.getString("frequency"));
    public static final MetaField fDiscreteContinous = new MetaField(LocaleMgr.db
            .getString("discreteContinous"));
    public static final MetaField fEmissionCondition = new MetaField(LocaleMgr.db
            .getString("emissionCondition"));
    public static final MetaRelationN fFlowGos = new MetaRelationN(LocaleMgr.db
            .getString("flowGos"), 0, MetaRelationN.cardN);
    public static final MetaField fArrowFirstEnd = new MetaField(LocaleMgr.db
            .getString("arrowFirstEnd"));
    public static final MetaField fArrowSecondEnd = new MetaField(LocaleMgr.db
            .getString("arrowSecondEnd"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbBEFlow"),
            DbBEFlow.class, new MetaField[] { fFirstEnd, fSecondEnd, fIdentifier, fControl,
                    fFrequency, fDiscreteContinous, fEmissionCondition, fFlowGos, fArrowFirstEnd,
                    fArrowSecondEnd }, MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSAbstractRelationship.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbBEFlowQualifier.metaClass });
            metaClass.setIcon("dbbeflow.gif");

            fFirstEnd.setJField(DbBEFlow.class.getDeclaredField("m_firstEnd"));
            fSecondEnd.setJField(DbBEFlow.class.getDeclaredField("m_secondEnd"));
            fIdentifier.setJField(DbBEFlow.class.getDeclaredField("m_identifier"));
            fControl.setJField(DbBEFlow.class.getDeclaredField("m_control"));
            fFrequency.setJField(DbBEFlow.class.getDeclaredField("m_frequency"));
            fDiscreteContinous.setJField(DbBEFlow.class.getDeclaredField("m_discreteContinous"));
            fEmissionCondition.setJField(DbBEFlow.class.getDeclaredField("m_emissionCondition"));
            fFlowGos.setJField(DbBEFlow.class.getDeclaredField("m_flowGos"));
            fArrowFirstEnd.setJField(DbBEFlow.class.getDeclaredField("m_arrowFirstEnd"));
            fArrowFirstEnd.setScreenOrder("<secondEnd");
            fArrowSecondEnd.setJField(DbBEFlow.class.getDeclaredField("m_arrowSecondEnd"));
            fArrowSecondEnd.setScreenOrder("<identifier");

            fFirstEnd.setOppositeRels(new MetaRelation[] { DbBEStore.fFirstEndFlows,
                    DbBEActor.fFirstEndFlows, DbBEUseCase.fFirstEndFlows });
            fSecondEnd.setOppositeRels(new MetaRelation[] { DbBEStore.fSecondEndFlows,
                    DbBEActor.fSecondEndFlows, DbBEUseCase.fSecondEndFlows });

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    public static final MetaField[] notationFields = new MetaField[] { fName, fPhysicalName,
            fAlias, fDescription, fEmissionCondition, fFrequency, fIdentifier, fUmlStereotype };

    //Instance variables
    DbSMSClassifier m_firstEnd;
    DbSMSClassifier m_secondEnd;
    String m_identifier;
    boolean m_control;
    SrInteger m_frequency;
    BEDiscreteContinous m_discreteContinous;
    String m_emissionCondition;
    DbRelationN m_flowGos;
    boolean m_arrowFirstEnd;
    boolean m_arrowSecondEnd;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEFlow() {
    }

    /**
     * Creates an instance of DbBEFlow.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbBEFlow(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setDiscreteContinous(BEDiscreteContinous.getInstance(BEDiscreteContinous.DISCRETE));
        DbBEUseCase composite = (DbBEUseCase) getCompositeOfType(DbBEUseCase.metaClass);
        if (composite != null)
            setIdentifier(composite.getNextFlowIdentifier());
        setArrowSecondEnd(Boolean.TRUE);
    }

    /**
     * @param form
     *            int
     * @return string
     **/
    public String getSemanticalName(int form) throws DbException {
        return (form == SHORT_FORM ? getIdentifier() : buildLongIDString());
    }

    /**
     * @return string
     **/
    private String buildLongIDString() throws DbException {
        String id = getIdentifier();
        if ((id == null) || StringUtil.isEmptyString(id.toString()))
            id = "";

        DbObject parent = this;
        while (true) {
            parent = parent.getComposite();
            if (parent == null || parent instanceof DbRoot || parent instanceof DbProject
                    || (parent.getMetaClass().getFlags() & MetaClass.NAMING_ROOT) != 0
                    || (parent instanceof DbBEUseCase && ((DbBEUseCase) parent).isContext()))
                break;
            String parentName = parent.getSemanticalName(DbObject.SHORT_FORM);
            if ((parentName == null) || StringUtil.isEmptyString(parentName.toString()))
                parentName = "";

            id = parentName + "." + id; //NOT LOCALIZABLE
        }
        return id;

    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.BPM;

    }

    //Setters

    /**
     * Sets the extremity 1 object associated to a DbBEFlow's instance.
     * 
     * @param value
     *            the extremity 1 object to be associated
     **/
    public final void setFirstEnd(DbSMSClassifier value) throws DbException {
        basicSet(fFirstEnd, value);
    }

    /**
     * Sets the extremity 2 object associated to a DbBEFlow's instance.
     * 
     * @param value
     *            the extremity 2 object to be associated
     **/
    public final void setSecondEnd(DbSMSClassifier value) throws DbException {
        basicSet(fSecondEnd, value);
    }

    /**
     * Sets the "identifier" property of a DbBEFlow's instance.
     * 
     * @param value
     *            the "identifier" property
     **/
    public final void setIdentifier(String value) throws DbException {
        basicSet(fIdentifier, value);
    }

    /**
     * Sets the "control" property of a DbBEFlow's instance.
     * 
     * @param value
     *            the "control" property
     **/
    public final void setControl(Boolean value) throws DbException {
        basicSet(fControl, value);
    }

    /**
     * Sets the "frequency" property of a DbBEFlow's instance.
     * 
     * @param value
     *            the "frequency" property
     **/
    public final void setFrequency(Integer value) throws DbException {
        basicSet(fFrequency, value);
    }

    /**
     * Sets the "discrete / continous" property of a DbBEFlow's instance.
     * 
     * @param value
     *            the "discrete / continous" property
     **/
    public final void setDiscreteContinous(BEDiscreteContinous value) throws DbException {
        basicSet(fDiscreteContinous, value);
    }

    /**
     * Sets the "emission condition" property of a DbBEFlow's instance.
     * 
     * @param value
     *            the "emission condition" property
     **/
    public final void setEmissionCondition(String value) throws DbException {
        basicSet(fEmissionCondition, value);
    }

    /**
     * Sets the "extremity 1 arrow" property of a DbBEFlow's instance.
     * 
     * @param value
     *            the "extremity 1 arrow" property
     **/
    public final void setArrowFirstEnd(Boolean value) throws DbException {
        basicSet(fArrowFirstEnd, value);
    }

    /**
     * Sets the "extremity 2 arrow" property of a DbBEFlow's instance.
     * 
     * @param value
     *            the "extremity 2 arrow" property
     **/
    public final void setArrowSecondEnd(Boolean value) throws DbException {
        basicSet(fArrowSecondEnd, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fFlowGos)
                ((DbBEFlowGo) value).setFlow(this);
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
     * Gets the extremity 1 object associated to a DbBEFlow's instance.
     * 
     * @return the extremity 1 object
     **/
    public final DbSMSClassifier getFirstEnd() throws DbException {
        return (DbSMSClassifier) get(fFirstEnd);
    }

    /**
     * Gets the extremity 2 object associated to a DbBEFlow's instance.
     * 
     * @return the extremity 2 object
     **/
    public final DbSMSClassifier getSecondEnd() throws DbException {
        return (DbSMSClassifier) get(fSecondEnd);
    }

    /**
     * Gets the "identifier" property of a DbBEFlow's instance.
     * 
     * @return the "identifier" property
     **/
    public final String getIdentifier() throws DbException {
        return (String) get(fIdentifier);
    }

    /**
     * Gets the "control" property's Boolean value of a DbBEFlow's instance.
     * 
     * @return the "control" property's Boolean value
     * @deprecated use isControl() method instead
     **/
    public final Boolean getControl() throws DbException {
        return (Boolean) get(fControl);
    }

    /**
     * Tells whether a DbBEFlow's instance is control or not.
     * 
     * @return boolean
     **/
    public final boolean isControl() throws DbException {
        return getControl().booleanValue();
    }

    /**
     * Gets the "frequency" of a DbBEFlow's instance.
     * 
     * @return the "frequency"
     **/
    public final Integer getFrequency() throws DbException {
        return (Integer) get(fFrequency);
    }

    /**
     * Gets the "discrete / continous" of a DbBEFlow's instance.
     * 
     * @return the "discrete / continous"
     **/
    public final BEDiscreteContinous getDiscreteContinous() throws DbException {
        return (BEDiscreteContinous) get(fDiscreteContinous);
    }

    /**
     * Gets the "emission condition" property of a DbBEFlow's instance.
     * 
     * @return the "emission condition" property
     **/
    public final String getEmissionCondition() throws DbException {
        return (String) get(fEmissionCondition);
    }

    /**
     * Gets the list of graphical objects associated to a DbBEFlow's instance.
     * 
     * @return the list of graphical objects.
     **/
    public final DbRelationN getFlowGos() throws DbException {
        return (DbRelationN) get(fFlowGos);
    }

    /**
     * Gets the "extremity 1 arrow" property's Boolean value of a DbBEFlow's instance.
     * 
     * @return the "extremity 1 arrow" property's Boolean value
     * @deprecated use isArrowFirstEnd() method instead
     **/
    public final Boolean getArrowFirstEnd() throws DbException {
        return (Boolean) get(fArrowFirstEnd);
    }

    /**
     * Tells whether a DbBEFlow's instance is arrowFirstEnd or not.
     * 
     * @return boolean
     **/
    public final boolean isArrowFirstEnd() throws DbException {
        return getArrowFirstEnd().booleanValue();
    }

    /**
     * Gets the "extremity 2 arrow" property's Boolean value of a DbBEFlow's instance.
     * 
     * @return the "extremity 2 arrow" property's Boolean value
     * @deprecated use isArrowSecondEnd() method instead
     **/
    public final Boolean getArrowSecondEnd() throws DbException {
        return (Boolean) get(fArrowSecondEnd);
    }

    /**
     * Tells whether a DbBEFlow's instance is arrowSecondEnd or not.
     * 
     * @return boolean
     **/
    public final boolean isArrowSecondEnd() throws DbException {
        return getArrowSecondEnd().booleanValue();
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
