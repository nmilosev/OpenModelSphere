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
import javax.swing.Icon;
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.util.DefaultComparableElement;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEModel.html">DbBEModel</A>, <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEUseCase.html">DbBEUseCase</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEUseCase.html">DbBEUseCase</A>, <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEDiagram.html">DbBEDiagram</A>, <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEFlow.html">DbBEFlow</A>, <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEUseCaseResource.html">DbBEUseCaseResource</A>,
 * <A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBEUseCaseQualifier.html">DbBEUseCaseQualifier<
 * /A>, <A HREF="../../../../../org/modelsphere/sms/db/DbSMSNotice.html">DbSMSNotice</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSObjectImport.html">DbSMSObjectImport</A>.<br>
 **/
public final class DbBEUseCase extends DbSMSClassifier {

    //Meta

    public static final MetaField fNumericIdentifier = new MetaField(LocaleMgr.db
            .getString("numericIdentifier"));
    public static final MetaField fAlphanumericIdentifier = new MetaField(LocaleMgr.db
            .getString("alphanumericIdentifier"));
    public static final MetaField fExternal = new MetaField(LocaleMgr.db.getString("external"));
    public static final MetaField fSynchronizationRule = new MetaField(LocaleMgr.db
            .getString("synchronizationRule"));
    public static final MetaRelationN fFirstEndFlows = new MetaRelationN(LocaleMgr.db
            .getString("firstEndFlows"), 0, MetaRelationN.cardN);
    public static final MetaRelationN fSecondEndFlows = new MetaRelationN(LocaleMgr.db
            .getString("secondEndFlows"), 0, MetaRelationN.cardN);
    public static final MetaField fFixedTime = new MetaField(LocaleMgr.db.getString("fixedTime"));
    public static final MetaField fFixedTimeUnit = new MetaField(LocaleMgr.db
            .getString("fixedTimeUnit"));
    public static final MetaField fResourceTime = new MetaField(LocaleMgr.db
            .getString("resourceTime"));
    public static final MetaField fResourceTimeUnit = new MetaField(LocaleMgr.db
            .getString("resourceTimeUnit"));
    public static final MetaField fPartialTime = new MetaField(LocaleMgr.db
            .getString("partialTime"));
    public static final MetaField fPartialTimeUnit = new MetaField(LocaleMgr.db
            .getString("partialTimeUnit"));
    public static final MetaField fTotalTime = new MetaField(LocaleMgr.db.getString("totalTime"));
    public static final MetaField fTotalTimeUnit = new MetaField(LocaleMgr.db
            .getString("totalTimeUnit"));
    public static final MetaField fFixedCost = new MetaField(LocaleMgr.db.getString("fixedCost"));
    public static final MetaField fResourceCost = new MetaField(LocaleMgr.db
            .getString("resourceCost"));
    public static final MetaField fPartialCost = new MetaField(LocaleMgr.db
            .getString("partialCost"));
    public static final MetaField fTotalCost = new MetaField(LocaleMgr.db.getString("totalCost"));
    public static final MetaField fControl = new MetaField(LocaleMgr.db.getString("control"));
    public static final MetaField fSourceAlphanumericIdentifier = new MetaField(LocaleMgr.db
            .getString("sourceAlphanumericIdentifier"));
    public static final MetaField fSourceNumericIdentifier = new MetaField(LocaleMgr.db
            .getString("sourceNumericIdentifier"));
    public static final MetaField fTerminologyName = new MetaField(LocaleMgr.db
            .getString("terminologyName"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbBEUseCase"),
            DbBEUseCase.class, new MetaField[] { fNumericIdentifier, fAlphanumericIdentifier,
                    fExternal, fSynchronizationRule, fFirstEndFlows, fSecondEndFlows, fFixedTime,
                    fFixedTimeUnit, fResourceTime, fResourceTimeUnit, fPartialTime,
                    fPartialTimeUnit, fTotalTime, fTotalTimeUnit, fFixedCost, fResourceCost,
                    fPartialCost, fTotalCost, fControl, fSourceAlphanumericIdentifier,
                    fSourceNumericIdentifier, fTerminologyName },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSClassifier.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbBEUseCase.metaClass,
                    DbBEDiagram.metaClass, DbBEFlow.metaClass, DbBEUseCaseResource.metaClass,
                    DbBEUseCaseQualifier.metaClass, DbSMSNotice.metaClass });
            metaClass.setIcon("dbbeprocess.gif");

            fNumericIdentifier.setJField(DbBEUseCase.class.getDeclaredField("m_numericIdentifier"));
            fAlphanumericIdentifier.setJField(DbBEUseCase.class
                    .getDeclaredField("m_alphanumericIdentifier"));
            fAlphanumericIdentifier.setScreenOrder(">numericIdentifier");
            fExternal.setJField(DbBEUseCase.class.getDeclaredField("m_external"));
            fExternal.setRendererPluginName("Boolean;DbBEUseCaseExternal");
            fSynchronizationRule.setJField(DbBEUseCase.class
                    .getDeclaredField("m_synchronizationRule"));
            fFirstEndFlows.setJField(DbBEUseCase.class.getDeclaredField("m_firstEndFlows"));
            fSecondEndFlows.setJField(DbBEUseCase.class.getDeclaredField("m_secondEndFlows"));
            fFixedTime.setJField(DbBEUseCase.class.getDeclaredField("m_fixedTime"));
            fFixedTimeUnit.setJField(DbBEUseCase.class.getDeclaredField("m_fixedTimeUnit"));
            fResourceTime.setJField(DbBEUseCase.class.getDeclaredField("m_resourceTime"));
            fResourceTime.setEditable(false);
            fResourceTimeUnit.setJField(DbBEUseCase.class.getDeclaredField("m_resourceTimeUnit"));
            fResourceTimeUnit.setEditable(false);
            fPartialTime.setJField(DbBEUseCase.class.getDeclaredField("m_partialTime"));
            fPartialTime.setEditable(false);
            fPartialTimeUnit.setJField(DbBEUseCase.class.getDeclaredField("m_partialTimeUnit"));
            fPartialTimeUnit.setEditable(false);
            fTotalTime.setJField(DbBEUseCase.class.getDeclaredField("m_totalTime"));
            fTotalTime.setEditable(false);
            fTotalTimeUnit.setJField(DbBEUseCase.class.getDeclaredField("m_totalTimeUnit"));
            fTotalTimeUnit.setEditable(false);
            fFixedCost.setJField(DbBEUseCase.class.getDeclaredField("m_fixedCost"));
            fResourceCost.setJField(DbBEUseCase.class.getDeclaredField("m_resourceCost"));
            fResourceCost.setEditable(false);
            fPartialCost.setJField(DbBEUseCase.class.getDeclaredField("m_partialCost"));
            fPartialCost.setEditable(false);
            fTotalCost.setJField(DbBEUseCase.class.getDeclaredField("m_totalCost"));
            fTotalCost.setEditable(false);
            fControl.setJField(DbBEUseCase.class.getDeclaredField("m_control"));
            fSourceAlphanumericIdentifier.setJField(DbBEUseCase.class
                    .getDeclaredField("m_sourceAlphanumericIdentifier"));
            fSourceNumericIdentifier.setJField(DbBEUseCase.class
                    .getDeclaredField("m_sourceNumericIdentifier"));
            fTerminologyName.setJField(DbBEUseCase.class.getDeclaredField("m_terminologyName"));
            fTerminologyName.setEditable(false);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    private static Icon useCaseIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbBEUseCase.class, "resources/dbbeprocess.gif");
    private static Icon contextIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbBEUseCase.class, "resources/dbbecontext.gif");
    public static final MetaField[] notationFields = new MetaField[] { fName, fPhysicalName,
            fAlias, fDescription, fAlphanumericIdentifier, fNumericIdentifier, fFixedCost,
            fFixedTime, fPartialCost, fPartialTime, fResourceCost, fResourceTime,
            fSynchronizationRule, fTotalCost, fTotalTime, DbBEUseCaseResource.fResource,
            fUmlStereotype };

    //Instance variables
    SrInteger m_numericIdentifier;
    String m_alphanumericIdentifier;
    boolean m_external;
    String m_synchronizationRule;
    DbRelationN m_firstEndFlows;
    DbRelationN m_secondEndFlows;
    SrDouble m_fixedTime;
    BETimeUnit m_fixedTimeUnit;
    SrDouble m_resourceTime;
    BETimeUnit m_resourceTimeUnit;
    SrDouble m_partialTime;
    BETimeUnit m_partialTimeUnit;
    SrDouble m_totalTime;
    BETimeUnit m_totalTimeUnit;
    SrDouble m_fixedCost;
    SrDouble m_resourceCost;
    SrDouble m_partialCost;
    SrDouble m_totalCost;
    boolean m_control;
    String m_sourceAlphanumericIdentifier;
    String m_sourceNumericIdentifier;
    String m_terminologyName;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEUseCase() {
    }

    /**
     * Creates an instance of DbBEUseCase.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbBEUseCase(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        if (isContext()) {
            DbBEModel model = (DbBEModel) getComposite();
            TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
            Terminology term = terminologyUtil.findModelTerminology(model);
            setName(term.getTerm(DbBEUseCaseGo.metaClass));
            setTerminologyName(model.getTerminologyName());
            setNumericIdentifier(new Integer(0));
        } else {
            DbBEUseCase useCase = (DbBEUseCase) getComposite();
            TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
            Terminology term = terminologyUtil.findModelTerminology(useCase);
            setName(term.getTerm(metaClass));
            setNumericIdentifier(((DbBEUseCase) getCompositeOfType(DbBEUseCase.metaClass))
                    .getNextNumericIdentifier());
            String tname = useCase.getTerminologyName();

            DbObject comp = useCase.getComposite();
            String compositeTerminology = null;
            if (comp instanceof DbBEUseCase)
                compositeTerminology = ((DbBEUseCase) comp).getTerminologyName();
            else if (comp instanceof DbBEModel)
                compositeTerminology = ((DbBEModel) comp).getTerminologyName();
            if (tname == null)
                useCase.setTerminologyName(compositeTerminology);
            else if (tname.equals(""))
                useCase.setTerminologyName(compositeTerminology);
        }
    }

    /**
     * @param metafield
     *            org.modelsphere.jack.baseDb.meta.MetaField
     * @return string
     **/
    private String buildLongIDString(MetaField metaField) throws DbException {
        if (metaField instanceof MetaRelationN)
            return null;
        String idString = null;
        Object id = get(metaField);
        if ((id == null) || StringUtil.isEmptyString(id.toString())) {
            if (metaField == fAlphanumericIdentifier) // Overriding alpha with numeric
                id = get(fNumericIdentifier);
        }
        if ((id == null) || StringUtil.isEmptyString(id.toString()))
            idString = "";
        else
            idString = id.toString();

        DbObject parent = this;
        while (true) {
            parent = parent.getComposite();
            if (parent == null || parent instanceof DbRoot || parent instanceof DbProject
                    || (parent.getMetaClass().getFlags() & MetaClass.NAMING_ROOT) != 0
                    || (parent instanceof DbBEUseCase && ((DbBEUseCase) parent).isContext()))
                break;
            String parentIdString = null;
            Object parentId = parent.get(metaField);
            if ((parentId == null) || StringUtil.isEmptyString(parentId.toString())) {
                if (metaField == fAlphanumericIdentifier) // Overriding alpha with numeric
                    parentId = parent.get(fNumericIdentifier);
            }
            if ((parentId == null) || StringUtil.isEmptyString(parentId.toString()))
                parentIdString = "";
            else
                parentIdString = parentId.toString();

            idString = parentIdString + "." + idString; //NOT LOCALIZABLE
        }
        return idString;
    }

    /**
     * @param form
     *            int
     * @return string
     **/
    public String getSemanticalName(int form) throws DbException {
        if (form == SHORT_FORM)
            return (getNumericIdentifier() == null ? null : getNumericIdentifier().toString());
        return buildLongIDString(fAlphanumericIdentifier);
    }

    /**
     * @return string
     **/
    public String getNumericHierID() throws DbException {
        return buildLongIDString(fNumericIdentifier);
    }

    /**
     * @param form
     *            int
     * @return icon
     **/
    public final Icon getSemanticalIcon(int form) throws DbException {
        if (getComposite() instanceof DbBEUseCase)
            return useCaseIcon;
        else
            return contextIcon;

    }

    /**
     * @param identifier
     *            java.lang.String
     * @return flow
     **/
    public DbBEFlow findFlowComponentByID(String identifier) throws DbException {
        DbBEFlow childFound = null;
        DbEnumeration dbEnum = getComponents().elements(DbBEFlow.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBEFlow child = (DbBEFlow) dbEnum.nextElement();
            if (identifier.equals(child.getIdentifier())) {
                childFound = child;
                break;
            }
        }
        dbEnum.close();
        return childFound;

    }

    /**
     * @return integer
     **/
    private Integer getNextNumericIdentifier() throws DbException {
        int maxId = 0;
        DbEnumeration dbEnum = getComponents().elements(DbBEUseCase.metaClass);
        while (dbEnum.hasMoreElements()) {
            Integer intValue = ((DbBEUseCase) dbEnum.nextElement()).getNumericIdentifier();
            if ((intValue != null) && (intValue.intValue() > maxId))
                maxId = intValue.intValue();
        }
        dbEnum.close();
        return new Integer(maxId + 1);

    }

    /**
     * @return boolean
     **/
    public boolean isContext() throws DbException {
        if (getComposite() instanceof DbBEModel)
            return true;
        return false;
    }

    /**
     * @return string
     **/
    String getNextFlowIdentifier() throws DbException {
        int count = 0;
        DbEnumeration dbEnum = getComponents().elements(DbBEFlow.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbObject obj = dbEnum.nextElement();
            count = count + 1;
        }
        dbEnum.close();
        return new String(new Integer(count).toString());

    }

    /**
     * @return string
     **/
    public String getAlphanumericHierID() throws DbException {
        return buildLongIDString(fAlphanumericIdentifier);
    }

    /**
     * @return int
     **/
    protected final int getFeatureSet() {
        return SMSFilter.BPM;

    }

    /**
     * @param metafield
     *            org.modelsphere.jack.baseDb.meta.MetaField
     * @param value
     *            java.lang.Object
     **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fFirstEndFlows)
                ((DbBEFlow) value).setFirstEnd(this);
            else if (metaField == fSecondEndFlows)
                ((DbBEFlow) value).setSecondEnd(this);
            else {
                if (value instanceof DbSMSNotation)
                    basicSet(metaField, ((DbSMSNotation) value).getName());
                else if (value instanceof DefaultComparableElement) {
                    DbBENotation nota = (DbBENotation) (((DefaultComparableElement) value).object);
                    basicSet(metaField, nota.getName());
                } else
                    basicSet(metaField, value);
            }
        } else
            super.set(metaField, value);
    }

    //Setters

    /**
     * Sets the "numeric identifier" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "numeric identifier" property
     **/
    public final void setNumericIdentifier(Integer value) throws DbException {
        basicSet(fNumericIdentifier, value);
    }

    /**
     * Sets the "alphanumeric identifier" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "alphanumeric identifier" property
     **/
    public final void setAlphanumericIdentifier(String value) throws DbException {
        basicSet(fAlphanumericIdentifier, value);
    }

    /**
     * Sets the "external" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "external" property
     **/
    public final void setExternal(Boolean value) throws DbException {
        basicSet(fExternal, value);
    }

    /**
     * Sets the "synchronization rule" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "synchronization rule" property
     **/
    public final void setSynchronizationRule(String value) throws DbException {
        basicSet(fSynchronizationRule, value);
    }

    /**
     * Sets the "fixed time" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "fixed time" property
     **/
    public final void setFixedTime(Double value) throws DbException {
        basicSet(fFixedTime, value);
    }

    /**
     * Sets the "fixed time unit" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "fixed time unit" property
     **/
    public final void setFixedTimeUnit(BETimeUnit value) throws DbException {
        basicSet(fFixedTimeUnit, value);
    }

    /**
     * Sets the "resource time" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "resource time" property
     **/
    public final void setResourceTime(Double value) throws DbException {
        basicSet(fResourceTime, value);
    }

    /**
     * Sets the "resource time unit" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "resource time unit" property
     **/
    public final void setResourceTimeUnit(BETimeUnit value) throws DbException {
        basicSet(fResourceTimeUnit, value);
    }

    /**
     * Sets the "partial time" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "partial time" property
     **/
    public final void setPartialTime(Double value) throws DbException {
        basicSet(fPartialTime, value);
    }

    /**
     * Sets the "partial time unit" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "partial time unit" property
     **/
    public final void setPartialTimeUnit(BETimeUnit value) throws DbException {
        basicSet(fPartialTimeUnit, value);
    }

    /**
     * Sets the "total time" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "total time" property
     **/
    public final void setTotalTime(Double value) throws DbException {
        basicSet(fTotalTime, value);
    }

    /**
     * Sets the "total time unit" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "total time unit" property
     **/
    public final void setTotalTimeUnit(BETimeUnit value) throws DbException {
        basicSet(fTotalTimeUnit, value);
    }

    /**
     * Sets the "fixed cost" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "fixed cost" property
     **/
    public final void setFixedCost(Double value) throws DbException {
        basicSet(fFixedCost, value);
    }

    /**
     * Sets the "resource cost" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "resource cost" property
     **/
    public final void setResourceCost(Double value) throws DbException {
        basicSet(fResourceCost, value);
    }

    /**
     * Sets the "partial cost" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "partial cost" property
     **/
    public final void setPartialCost(Double value) throws DbException {
        basicSet(fPartialCost, value);
    }

    /**
     * Sets the "total cost" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "total cost" property
     **/
    public final void setTotalCost(Double value) throws DbException {
        basicSet(fTotalCost, value);
    }

    /**
     * Sets the "control" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "control" property
     **/
    public final void setControl(Boolean value) throws DbException {
        basicSet(fControl, value);
    }

    /**
     * Sets the "external alphanumeric identifier" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "external alphanumeric identifier" property
     **/
    public final void setSourceAlphanumericIdentifier(String value) throws DbException {
        basicSet(fSourceAlphanumericIdentifier, value);
    }

    /**
     * Sets the "external numeric identifier" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "external numeric identifier" property
     **/
    public final void setSourceNumericIdentifier(String value) throws DbException {
        basicSet(fSourceNumericIdentifier, value);
    }

    /**
     * Sets the "terminology" property of a DbBEUseCase's instance.
     * 
     * @param value
     *            the "terminology" property
     **/
    public final void setTerminologyName(String value) throws DbException {
        basicSet(fTerminologyName, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the "numeric identifier" of a DbBEUseCase's instance.
     * 
     * @return the "numeric identifier"
     **/
    public final Integer getNumericIdentifier() throws DbException {
        return (Integer) get(fNumericIdentifier);
    }

    /**
     * Gets the "alphanumeric identifier" property of a DbBEUseCase's instance.
     * 
     * @return the "alphanumeric identifier" property
     **/
    public final String getAlphanumericIdentifier() throws DbException {
        return (String) get(fAlphanumericIdentifier);
    }

    /**
     * Gets the "external" property's Boolean value of a DbBEUseCase's instance.
     * 
     * @return the "external" property's Boolean value
     * @deprecated use isExternal() method instead
     **/
    public final Boolean getExternal() throws DbException {
        return (Boolean) get(fExternal);
    }

    /**
     * Tells whether a DbBEUseCase's instance is external or not.
     * 
     * @return boolean
     **/
    public final boolean isExternal() throws DbException {
        return getExternal().booleanValue();
    }

    /**
     * Gets the "synchronization rule" property of a DbBEUseCase's instance.
     * 
     * @return the "synchronization rule" property
     **/
    public final String getSynchronizationRule() throws DbException {
        return (String) get(fSynchronizationRule);
    }

    /**
     * Gets the list of flows associated to a DbBEUseCase's instance.
     * 
     * @return the list of flows.
     **/
    public final DbRelationN getFirstEndFlows() throws DbException {
        return (DbRelationN) get(fFirstEndFlows);
    }

    /**
     * Gets the list of flows associated to a DbBEUseCase's instance.
     * 
     * @return the list of flows.
     **/
    public final DbRelationN getSecondEndFlows() throws DbException {
        return (DbRelationN) get(fSecondEndFlows);
    }

    /**
     * Gets the "fixed time" of a DbBEUseCase's instance.
     * 
     * @return the "fixed time"
     **/
    public final Double getFixedTime() throws DbException {
        return (Double) get(fFixedTime);
    }

    /**
     * Gets the "fixed time unit" of a DbBEUseCase's instance.
     * 
     * @return the "fixed time unit"
     **/
    public final BETimeUnit getFixedTimeUnit() throws DbException {
        return (BETimeUnit) get(fFixedTimeUnit);
    }

    /**
     * Gets the "resource time" of a DbBEUseCase's instance.
     * 
     * @return the "resource time"
     **/
    public final Double getResourceTime() throws DbException {
        return (Double) get(fResourceTime);
    }

    /**
     * Gets the "resource time unit" of a DbBEUseCase's instance.
     * 
     * @return the "resource time unit"
     **/
    public final BETimeUnit getResourceTimeUnit() throws DbException {
        return (BETimeUnit) get(fResourceTimeUnit);
    }

    /**
     * Gets the "partial time" of a DbBEUseCase's instance.
     * 
     * @return the "partial time"
     **/
    public final Double getPartialTime() throws DbException {
        return (Double) get(fPartialTime);
    }

    /**
     * Gets the "partial time unit" of a DbBEUseCase's instance.
     * 
     * @return the "partial time unit"
     **/
    public final BETimeUnit getPartialTimeUnit() throws DbException {
        return (BETimeUnit) get(fPartialTimeUnit);
    }

    /**
     * Gets the "total time" of a DbBEUseCase's instance.
     * 
     * @return the "total time"
     **/
    public final Double getTotalTime() throws DbException {
        return (Double) get(fTotalTime);
    }

    /**
     * Gets the "total time unit" of a DbBEUseCase's instance.
     * 
     * @return the "total time unit"
     **/
    public final BETimeUnit getTotalTimeUnit() throws DbException {
        return (BETimeUnit) get(fTotalTimeUnit);
    }

    /**
     * Gets the "fixed cost" of a DbBEUseCase's instance.
     * 
     * @return the "fixed cost"
     **/
    public final Double getFixedCost() throws DbException {
        return (Double) get(fFixedCost);
    }

    /**
     * Gets the "resource cost" of a DbBEUseCase's instance.
     * 
     * @return the "resource cost"
     **/
    public final Double getResourceCost() throws DbException {
        return (Double) get(fResourceCost);
    }

    /**
     * Gets the "partial cost" of a DbBEUseCase's instance.
     * 
     * @return the "partial cost"
     **/
    public final Double getPartialCost() throws DbException {
        return (Double) get(fPartialCost);
    }

    /**
     * Gets the "total cost" of a DbBEUseCase's instance.
     * 
     * @return the "total cost"
     **/
    public final Double getTotalCost() throws DbException {
        return (Double) get(fTotalCost);
    }

    /**
     * Gets the "control" property's Boolean value of a DbBEUseCase's instance.
     * 
     * @return the "control" property's Boolean value
     * @deprecated use isControl() method instead
     **/
    public final Boolean getControl() throws DbException {
        return (Boolean) get(fControl);
    }

    /**
     * Tells whether a DbBEUseCase's instance is control or not.
     * 
     * @return boolean
     **/
    public final boolean isControl() throws DbException {
        return getControl().booleanValue();
    }

    /**
     * Gets the "external alphanumeric identifier" property of a DbBEUseCase's instance.
     * 
     * @return the "external alphanumeric identifier" property
     **/
    public final String getSourceAlphanumericIdentifier() throws DbException {
        return (String) get(fSourceAlphanumericIdentifier);
    }

    /**
     * Gets the "external numeric identifier" property of a DbBEUseCase's instance.
     * 
     * @return the "external numeric identifier" property
     **/
    public final String getSourceNumericIdentifier() throws DbException {
        return (String) get(fSourceNumericIdentifier);
    }

    /**
     * Gets the "terminology" property of a DbBEUseCase's instance.
     * 
     * @return the "terminology" property
     **/
    public final String getTerminologyName() throws DbException {
        return (String) get(fTerminologyName);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
