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
import java.util.ArrayList;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSProject.html">DbSMSProject</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/be/db/DbBESingleZoneDisplay.html">
 * DbBESingleZoneDisplay</A>.<br>
 **/
public final class DbBENotation extends DbSMSNotation {

    //Meta

    public static final MetaRelation1 fReferringProjectBe = new MetaRelation1(LocaleMgr.db
            .getString("DbBENotation.referringProjectBe"), 0);
    public static final MetaRelationN fDiagrams = new MetaRelationN(LocaleMgr.db
            .getString("diagrams"), 0, MetaRelationN.cardN);
    public static final MetaField fBuiltIn = new MetaField(LocaleMgr.db.getString("builtIn"));
    public static final MetaField fUseCaseShape = new MetaField(LocaleMgr.db
            .getString("useCaseShape"));
    public static final MetaField fFlowZoneOrientation = new MetaField(LocaleMgr.db
            .getString("flowZoneOrientation"));
    public static final MetaField fUseCaseDefaultWidth = new MetaField(LocaleMgr.db
            .getString("useCaseDefaultWidth"));
    public static final MetaField fUseCaseDefaultHeight = new MetaField(LocaleMgr.db
            .getString("useCaseDefaultHeight"));
    public static final MetaField fStoreShape = new MetaField(LocaleMgr.db.getString("storeShape"));
    public static final MetaField fStoreZoneOrientation = new MetaField(LocaleMgr.db
            .getString("storeZoneOrientation"));
    public static final MetaField fStoreDefaultWidth = new MetaField(LocaleMgr.db
            .getString("storeDefaultWidth"));
    public static final MetaField fStoreDefaultHeight = new MetaField(LocaleMgr.db
            .getString("storeDefaultHeight"));
    public static final MetaField fStoreIdPrefix = new MetaField(LocaleMgr.db
            .getString("storeIdPrefix"));
    public static final MetaField fActorShape = new MetaField(LocaleMgr.db.getString("actorShape"));
    public static final MetaField fActorZoneOrientation = new MetaField(LocaleMgr.db
            .getString("actorZoneOrientation"));
    public static final MetaField fActorDefaultWidth = new MetaField(LocaleMgr.db
            .getString("actorDefaultWidth"));
    public static final MetaField fActorDefaultHeight = new MetaField(LocaleMgr.db
            .getString("actorDefaultHeight"));
    public static final MetaField fActorIdPrefix = new MetaField(LocaleMgr.db
            .getString("actorIdPrefix"));
    public static final MetaField fTerminologyName = new MetaField(LocaleMgr.db
            .getString("terminologyName"));
    public static final MetaField fDisplayFrameBox = new MetaField(LocaleMgr.db
            .getString("displayFrameBox"));
    public static final MetaField fCellTitleBoxed = new MetaField(LocaleMgr.db
            .getString("cellTitleBoxed"));
    public static final MetaField fCenterDisplay = new MetaField(LocaleMgr.db
            .getString("centerDisplay"));
    public static final MetaField fConstraintCenter = new MetaField(LocaleMgr.db
            .getString("constraintCenter"));
    public static final MetaField fDefButtomBorder = new MetaField(LocaleMgr.db
            .getString("defButtomBorder"));
    public static final MetaField fDefRightBorder = new MetaField(LocaleMgr.db
            .getString("defRightBorder"));
    public static final MetaField fDefVerticalAlignment = new MetaField(LocaleMgr.db
            .getString("defVerticalAlignment"));
    public static final MetaField fDefHorizontalAlignment = new MetaField(LocaleMgr.db
            .getString("defHorizontalAlignment"));
    public static final MetaField fHasFrame = new MetaField(LocaleMgr.db.getString("hasFrame"));
    public static final MetaField fDisplayStereotypesAsIcons = new MetaField(LocaleMgr.db
            .getString("displayStereotypesAsIcons"));
    public static final MetaField fUseCaseZoneOrientation = new MetaField(LocaleMgr.db
            .getString("useCaseZoneOrientation"));
    public static final MetaField fFlowIdPrefix = new MetaField(LocaleMgr.db
            .getString("flowIdPrefix"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbBENotation"),
            DbBENotation.class, new MetaField[] { fReferringProjectBe, fDiagrams, fBuiltIn,
                    fUseCaseShape, fFlowZoneOrientation, fUseCaseDefaultWidth,
                    fUseCaseDefaultHeight, fStoreShape, fStoreZoneOrientation, fStoreDefaultWidth,
                    fStoreDefaultHeight, fStoreIdPrefix, fActorShape, fActorZoneOrientation,
                    fActorDefaultWidth, fActorDefaultHeight, fActorIdPrefix, fTerminologyName,
                    fDisplayFrameBox, fCellTitleBoxed, fCenterDisplay, fConstraintCenter,
                    fDefButtomBorder, fDefRightBorder, fDefVerticalAlignment,
                    fDefHorizontalAlignment, fHasFrame, fDisplayStereotypesAsIcons,
                    fUseCaseZoneOrientation, fFlowIdPrefix }, MetaClass.MATCHABLE
                    | MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSNotation.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbBESingleZoneDisplay.metaClass });

            fReferringProjectBe.setJField(DbBENotation.class
                    .getDeclaredField("m_referringProjectBe"));
            fDiagrams.setJField(DbBENotation.class.getDeclaredField("m_diagrams"));
            fDiagrams.setFlags(MetaField.HUGE_RELN);
            fBuiltIn.setJField(DbBENotation.class.getDeclaredField("m_builtIn"));
            fUseCaseShape.setJField(DbBENotation.class.getDeclaredField("m_useCaseShape"));
            fFlowZoneOrientation.setJField(DbBENotation.class
                    .getDeclaredField("m_flowZoneOrientation"));
            fUseCaseDefaultWidth.setJField(DbBENotation.class
                    .getDeclaredField("m_useCaseDefaultWidth"));
            fUseCaseDefaultHeight.setJField(DbBENotation.class
                    .getDeclaredField("m_useCaseDefaultHeight"));
            fStoreShape.setJField(DbBENotation.class.getDeclaredField("m_storeShape"));
            fStoreZoneOrientation.setJField(DbBENotation.class
                    .getDeclaredField("m_storeZoneOrientation"));
            fStoreDefaultWidth
                    .setJField(DbBENotation.class.getDeclaredField("m_storeDefaultWidth"));
            fStoreDefaultHeight.setJField(DbBENotation.class
                    .getDeclaredField("m_storeDefaultHeight"));
            fStoreIdPrefix.setJField(DbBENotation.class.getDeclaredField("m_storeIdPrefix"));
            fActorShape.setJField(DbBENotation.class.getDeclaredField("m_actorShape"));
            fActorZoneOrientation.setJField(DbBENotation.class
                    .getDeclaredField("m_actorZoneOrientation"));
            fActorDefaultWidth
                    .setJField(DbBENotation.class.getDeclaredField("m_actorDefaultWidth"));
            fActorDefaultHeight.setJField(DbBENotation.class
                    .getDeclaredField("m_actorDefaultHeight"));
            fActorIdPrefix.setJField(DbBENotation.class.getDeclaredField("m_actorIdPrefix"));
            fTerminologyName.setJField(DbBENotation.class.getDeclaredField("m_terminologyName"));
            fDisplayFrameBox.setJField(DbBENotation.class.getDeclaredField("m_displayFrameBox"));
            fCellTitleBoxed.setJField(DbBENotation.class.getDeclaredField("m_cellTitleBoxed"));
            fCenterDisplay.setJField(DbBENotation.class.getDeclaredField("m_centerDisplay"));
            fConstraintCenter.setJField(DbBENotation.class.getDeclaredField("m_constraintCenter"));
            fDefButtomBorder.setJField(DbBENotation.class.getDeclaredField("m_defButtomBorder"));
            fDefRightBorder.setJField(DbBENotation.class.getDeclaredField("m_defRightBorder"));
            fDefVerticalAlignment.setJField(DbBENotation.class
                    .getDeclaredField("m_defVerticalAlignment"));
            fDefHorizontalAlignment.setJField(DbBENotation.class
                    .getDeclaredField("m_defHorizontalAlignment"));
            fHasFrame.setJField(DbBENotation.class.getDeclaredField("m_hasFrame"));
            fDisplayStereotypesAsIcons.setJField(DbBENotation.class
                    .getDeclaredField("m_displayStereotypesAsIcons"));
            fUseCaseZoneOrientation.setJField(DbBENotation.class
                    .getDeclaredField("m_useCaseZoneOrientation"));
            fFlowIdPrefix.setJField(DbBENotation.class.getDeclaredField("m_flowIdPrefix"));

            fReferringProjectBe.setOppositeRel(DbSMSProject.fBeDefaultNotation);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    public static final String STORE_ID_PREFIX = LocaleMgr.misc.getString("storeIdPrefix");
    public static final String FLOW_ID_PREFIX = LocaleMgr.misc.getString("flowIdPrefix");
    public static final String ACTOR_ID_PREFIX = LocaleMgr.misc.getString("actorIdPrefix");
    public static String[] listOptionTabs;
    public static MetaField[][] optionGroups;
    public static Object[][] optionValueGroups;
    public static String[] optionGroupComponents;
    public static String[] optionGroupHeaders;
    public static MetaField[] flowOptions;
    public static Object[] storeOptionDefaultValues;
    public static Object[] actorOptionDefaultValues;
    public static MetaField[] actorOptions;
    public static Object[] flowOptionDefaultValues;
    public static MetaField[] storeOptions;
    public static Object[] useCaseOptionDefaultValues;
    public static MetaField[] useCaseOptions;
    private static final int ACTOR_DEFAULT_HEIGHT = 100;
    private static final int USECASE_DEFAULT_HEIGHT = 120;
    private static final int ACTOR_DEFAULT_WIDTH = 100;
    private static final int STORE_DEFAULT_WIDTH = 100;
    private static final int USECASE_DEFAULT_WIDTH = 100;
    private static final int STORE_DEFAULT_HEIGHT = 40;
    public static final String DATARUN_SPM = LocaleMgr.misc.getString("datarun_spm");
    public static final String DATARUN_BPM = LocaleMgr.misc.getString("datarun_bpm");
    public static final String DATARUN_ISA = LocaleMgr.misc.getString("datarun_isa");
    public static final String GANE_SARSON = LocaleMgr.misc.getString("gane_sarson");
    public static final String MERISE = LocaleMgr.misc.getString("merise");
    public static final String YOURDON_DEMARCO = LocaleMgr.misc.getString("yourdon_demarco");
    public static final String WARD_MELLOR = LocaleMgr.misc.getString("ward_Mellor");
    public static final String FUNCTIONAL_DIAGRAM = LocaleMgr.misc.getString("funtional_diagram");
    public static final String MERISE_MCT = LocaleMgr.misc.getString("merise_mct");
    public static final String MERISE_FLOW_SCHEMA = LocaleMgr.misc.getString("merise_flow_schema");
    public static final String MERISE_OOM = LocaleMgr.misc.getString("merise_oom");
    public static final String P_PLUS = LocaleMgr.misc.getString("p+");
    public static final String P_PLUS_OPAL = LocaleMgr.misc.getString("p+_opal");
    public static final String UML_USE_CASE = LocaleMgr.misc.getString("uml_use_case");
    public static final String UML_SEQUENCE_DIAGRAM = LocaleMgr.misc
            .getString("uml_sequence_diagram");
    public static final String UML_STATE_DIAGRAM = LocaleMgr.misc.getString("uml_state_diagram");
    public static final String UML_COLLABORATION_DIAGRAM = LocaleMgr.misc
            .getString("uml_collaboration_diagram");
    public static final String MESSAGE_MODELING = LocaleMgr.misc.getString("message_modeling");
    public static final String OBJECT_LIFE_CYCLE = LocaleMgr.misc.getString("object_life_cycle");
    public static final String UML_CLASS_DIAGRAM = LocaleMgr.misc.getString("uml_class_diagram");

    static {
        // useCase options
        useCaseOptions = new MetaField[] { fUseCaseDefaultWidth, fUseCaseDefaultHeight,
                fUseCaseShape, fUseCaseZoneOrientation };

        useCaseOptionDefaultValues = new Object[] { new Integer(USECASE_DEFAULT_WIDTH),
                new Integer(USECASE_DEFAULT_HEIGHT),
                SMSNotationShape.getInstance(SMSNotationShape.ROUND_RECTANGLE),
                SMSZoneOrientation.getInstance(SMSZoneOrientation.HORIZONTAL) };

        // actor options
        actorOptions = new MetaField[] { fActorDefaultWidth, fActorDefaultHeight, fActorIdPrefix,
                fActorShape, fActorZoneOrientation };

        actorOptionDefaultValues = new Object[] { new Integer(ACTOR_DEFAULT_WIDTH),
                new Integer(ACTOR_DEFAULT_HEIGHT), ACTOR_ID_PREFIX,
                SMSNotationShape.getInstance(SMSNotationShape.SHADOW_RECTANGLE),
                SMSZoneOrientation.getInstance(SMSZoneOrientation.HORIZONTAL) };

        // store options
        storeOptions = new MetaField[] { fStoreDefaultWidth, fStoreDefaultHeight, fStoreIdPrefix,
                fStoreShape, fStoreZoneOrientation };

        storeOptionDefaultValues = new Object[] { new Integer(STORE_DEFAULT_WIDTH),
                new Integer(STORE_DEFAULT_HEIGHT), STORE_ID_PREFIX,
                SMSNotationShape.getInstance(SMSNotationShape.OPENED_RECTANGLE_RIGHT),
                SMSZoneOrientation.getInstance(SMSZoneOrientation.PERPENDICULAR) };

        // flow options
        flowOptions = new MetaField[] { fFlowIdPrefix };

        flowOptionDefaultValues = new Object[] { FLOW_ID_PREFIX };

        listOptionTabs = new String[] { "optionGroups", "optionValueGroups", "optionGroupHeaders",
                "optionGroupComponents" }; // NOT LOCALIZABLE - field name

        optionGroups = new MetaField[][] { useCaseOptions, actorOptions, storeOptions, flowOptions };

        optionValueGroups = new Object[][] { useCaseOptionDefaultValues, actorOptionDefaultValues,
                storeOptionDefaultValues, flowOptionDefaultValues };

        optionGroupHeaders = new String[] { LocaleMgr.screen.getString("Process"),
                LocaleMgr.screen.getString("Entity"), LocaleMgr.screen.getString("Store"),
                LocaleMgr.screen.getString("Flow") };

        optionGroupComponents = new String[] {
                "org.modelsphere.sms.be.notation.UseCaseComponent",
                "org.modelsphere.sms.be.notation.ActorComponent", // NOT LOCALIZABLE - Class name
                "org.modelsphere.sms.be.notation.StoreComponent",
                "org.modelsphere.sms.be.notation.FlowComponent" }; // NOT LOCALIZABLE - Class name

    }

    //Instance variables
    DbSMSProject m_referringProjectBe;
    DbRelationN m_diagrams;
    boolean m_builtIn;
    SMSNotationShape m_useCaseShape;
    SMSZoneOrientation m_flowZoneOrientation;
    int m_useCaseDefaultWidth;
    int m_useCaseDefaultHeight;
    SMSNotationShape m_storeShape;
    SMSZoneOrientation m_storeZoneOrientation;
    int m_storeDefaultWidth;
    int m_storeDefaultHeight;
    String m_storeIdPrefix;
    SMSNotationShape m_actorShape;
    SMSZoneOrientation m_actorZoneOrientation;
    int m_actorDefaultWidth;
    int m_actorDefaultHeight;
    String m_actorIdPrefix;
    String m_terminologyName;
    SrBoolean m_displayFrameBox;
    SrBoolean m_cellTitleBoxed;
    SrBoolean m_centerDisplay;
    SrBoolean m_constraintCenter;
    SrBoolean m_defButtomBorder;
    SrBoolean m_defRightBorder;
    SrInteger m_defVerticalAlignment;
    SrInteger m_defHorizontalAlignment;
    SrBoolean m_hasFrame;
    SrBoolean m_displayStereotypesAsIcons;
    SMSZoneOrientation m_useCaseZoneOrientation;
    String m_flowIdPrefix;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBENotation() {
    }

    /**
     * Creates an instance of DbBENotation.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbBENotation(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setBuiltIn(Boolean.FALSE);
        setDisplayFrameBox(Boolean.TRUE);
        setCellTitleBoxed(Boolean.FALSE);
        setCenterDisplay(Boolean.FALSE);
        setConstraintCenter(Boolean.FALSE);
        setDefButtomBorder(Boolean.TRUE);
        setDefRightBorder(Boolean.TRUE);
        setDefVerticalAlignment(new Integer(0));
        setDefHorizontalAlignment(new Integer(0));
        setHasFrame(Boolean.TRUE);
        setDisplayStereotypesAsIcons(Boolean.FALSE);
    }

    /**
     * Get all Use Case's zones
     * 
     * @return arraylist
     **/
    public final ArrayList getUseCaseZones() throws DbException {
        return getAllZonesOfType(BEZoneStereotype.getInstance(BEZoneStereotype.USECASE));
    }

    /**
     * Get all Actor's zones
     * 
     * @return arraylist
     **/
    public final ArrayList getActorZones() throws DbException {
        return getAllZonesOfType(BEZoneStereotype.getInstance(BEZoneStereotype.ACTOR));
    }

    /**
     * Get all Flow's zones
     * 
     * @return arraylist
     **/
    public final ArrayList getFlowZones() throws DbException {
        return getAllZonesOfType(BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
    }

    /**
     * Get all Store's zones
     * 
     * @return arraylist
     **/
    public final ArrayList getStoreZones() throws DbException {
        return getAllZonesOfType(BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
    }

    /**
     * @param stereotype
     *            org.modelsphere.sms.be.db.srtypes.BEZoneStereotype
     * @return arraylist
     **/
    private ArrayList getAllZonesOfType(BEZoneStereotype stereotype) throws DbException {
        ArrayList zones = new ArrayList();
        DbEnumeration dbEnum = this.getComponents().elements(DbBESingleZoneDisplay.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbBESingleZoneDisplay zone = (DbBESingleZoneDisplay) dbEnum.nextElement();
            if (zone.getStereotype().equals(stereotype))
                zones.add(zone);
        }
        dbEnum.close();
        return zones;
    }

    /**

 **/
    public void initOptions() throws DbException {

        for (int i = 0; i < optionGroups.length; i++) {
            for (int j = 0; j < optionGroups[i].length; j++) {
                basicSet(optionGroups[i][j], optionValueGroups[i][j]);
            }
        }

    }

    /**
     * @param srcnotation
     *            org.modelsphere.sms.be.db.DbBENotation
     **/
    public void copyOptions(DbBENotation srcNotation) throws DbException {

        // copy all options fields
        for (int i = 0; i < optionGroups.length; i++) {
            for (int j = 0; j < optionGroups[i].length; j++) {
                MetaField metaField = optionGroups[i][j];
                basicSet(metaField, srcNotation.get(metaField));
            }
        }
        // copy all zone
        DbEnumeration dbEnum = srcNotation.getComponents()
                .elements(DbBESingleZoneDisplay.metaClass);
        ArrayList zones = new ArrayList();
        while (dbEnum.hasMoreElements())
            zones.add(dbEnum.nextElement());
        dbEnum.close();
        if (zones.size() > 0) {
            DbObject[] sources = new DbObject[zones.size()];
            for (int i = 0; i < sources.length; i++)
                sources[i] = (DbObject) zones.get(i);
            DbObject.deepCopy(sources, this, null);
        }
    }

    //Setters

    /**
     * Sets the project object associated to a DbBENotation's instance.
     * 
     * @param value
     *            the project object to be associated
     **/
    public final void setReferringProjectBe(DbSMSProject value) throws DbException {
        basicSet(fReferringProjectBe, value);
    }

    /**
     * Sets the "built in?" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "built in?" property
     **/
    public final void setBuiltIn(Boolean value) throws DbException {
        basicSet(fBuiltIn, value);
    }

    /**
     * Sets the "shape" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "shape" property
     **/
    public final void setUseCaseShape(SMSNotationShape value) throws DbException {
        basicSet(fUseCaseShape, value);
    }

    /**
     * Sets the "orientation" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "orientation" property
     **/
    public final void setFlowZoneOrientation(SMSZoneOrientation value) throws DbException {
        basicSet(fFlowZoneOrientation, value);
    }

    /**
     * Sets the "width" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "width" property
     **/
    public final void setUseCaseDefaultWidth(Integer value) throws DbException {
        basicSet(fUseCaseDefaultWidth, value);
    }

    /**
     * Sets the "height" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "height" property
     **/
    public final void setUseCaseDefaultHeight(Integer value) throws DbException {
        basicSet(fUseCaseDefaultHeight, value);
    }

    /**
     * Sets the "shape" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "shape" property
     **/
    public final void setStoreShape(SMSNotationShape value) throws DbException {
        basicSet(fStoreShape, value);
    }

    /**
     * Sets the "orientation" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "orientation" property
     **/
    public final void setStoreZoneOrientation(SMSZoneOrientation value) throws DbException {
        basicSet(fStoreZoneOrientation, value);
    }

    /**
     * Sets the "width" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "width" property
     **/
    public final void setStoreDefaultWidth(Integer value) throws DbException {
        basicSet(fStoreDefaultWidth, value);
    }

    /**
     * Sets the "height" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "height" property
     **/
    public final void setStoreDefaultHeight(Integer value) throws DbException {
        basicSet(fStoreDefaultHeight, value);
    }

    /**
     * Sets the "formatting string" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "formatting string" property
     **/
    public final void setStoreIdPrefix(String value) throws DbException {
        basicSet(fStoreIdPrefix, value);
    }

    /**
     * Sets the "shape" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "shape" property
     **/
    public final void setActorShape(SMSNotationShape value) throws DbException {
        basicSet(fActorShape, value);
    }

    /**
     * Sets the "orientation" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "orientation" property
     **/
    public final void setActorZoneOrientation(SMSZoneOrientation value) throws DbException {
        basicSet(fActorZoneOrientation, value);
    }

    /**
     * Sets the "width" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "width" property
     **/
    public final void setActorDefaultWidth(Integer value) throws DbException {
        basicSet(fActorDefaultWidth, value);
    }

    /**
     * Sets the "height" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "height" property
     **/
    public final void setActorDefaultHeight(Integer value) throws DbException {
        basicSet(fActorDefaultHeight, value);
    }

    /**
     * Sets the "formatting string" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "formatting string" property
     **/
    public final void setActorIdPrefix(String value) throws DbException {
        basicSet(fActorIdPrefix, value);
    }

    /**
     * Sets the "terminology" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "terminology" property
     **/
    public final void setTerminologyName(String value) throws DbException {
        basicSet(fTerminologyName, value);
    }

    /**
     * Sets the "display frame box" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "display frame box" property
     **/
    public final void setDisplayFrameBox(Boolean value) throws DbException {
        basicSet(fDisplayFrameBox, value);
    }

    /**
     * Sets the "cell title within a box" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "cell title within a box" property
     **/
    public final void setCellTitleBoxed(Boolean value) throws DbException {
        basicSet(fCellTitleBoxed, value);
    }

    /**
     * Sets the "display center of cells" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "display center of cells" property
     **/
    public final void setCenterDisplay(Boolean value) throws DbException {
        basicSet(fCenterDisplay, value);
    }

    /**
     * Sets the "constraint to center" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "constraint to center" property
     **/
    public final void setConstraintCenter(Boolean value) throws DbException {
        basicSet(fConstraintCenter, value);
    }

    /**
     * Sets the "bottom border default value" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "bottom border default value" property
     **/
    public final void setDefButtomBorder(Boolean value) throws DbException {
        basicSet(fDefButtomBorder, value);
    }

    /**
     * Sets the "right border default value" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "right border default value" property
     **/
    public final void setDefRightBorder(Boolean value) throws DbException {
        basicSet(fDefRightBorder, value);
    }

    /**
     * Sets the "vertical aligment default value" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "vertical aligment default value" property
     **/
    public final void setDefVerticalAlignment(Integer value) throws DbException {
        basicSet(fDefVerticalAlignment, value);
    }

    /**
     * Sets the "horizontal aligment default value" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "horizontal aligment default value" property
     **/
    public final void setDefHorizontalAlignment(Integer value) throws DbException {
        basicSet(fDefHorizontalAlignment, value);
    }

    /**
     * Sets the "has frame" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "has frame" property
     **/
    public final void setHasFrame(Boolean value) throws DbException {
        basicSet(fHasFrame, value);
    }

    /**
     * Sets the "display stereotypes as icons" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "display stereotypes as icons" property
     **/
    public final void setDisplayStereotypesAsIcons(Boolean value) throws DbException {
        basicSet(fDisplayStereotypesAsIcons, value);
    }

    /**
     * Sets the "orientation" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "orientation" property
     **/
    public final void setUseCaseZoneOrientation(SMSZoneOrientation value) throws DbException {
        basicSet(fUseCaseZoneOrientation, value);
    }

    /**
     * Sets the "formatting string" property of a DbBENotation's instance.
     * 
     * @param value
     *            the "formatting string" property
     **/
    public final void setFlowIdPrefix(String value) throws DbException {
        basicSet(fFlowIdPrefix, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fDiagrams)
                ((DbBEDiagram) value).setNotation(this);
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
     * Gets the project object associated to a DbBENotation's instance.
     * 
     * @return the project object
     **/
    public final DbSMSProject getReferringProjectBe() throws DbException {
        return (DbSMSProject) get(fReferringProjectBe);
    }

    /**
     * Gets the list of diagrams associated to a DbBENotation's instance.
     * 
     * @return the list of diagrams.
     **/
    public final DbRelationN getDiagrams() throws DbException {
        return (DbRelationN) get(fDiagrams);
    }

    /**
     * Gets the "built in?" property's Boolean value of a DbBENotation's instance.
     * 
     * @return the "built in?" property's Boolean value
     * @deprecated use isBuiltIn() method instead
     **/
    public final Boolean getBuiltIn() throws DbException {
        return (Boolean) get(fBuiltIn);
    }

    /**
     * Tells whether a DbBENotation's instance is builtIn or not.
     * 
     * @return boolean
     **/
    public final boolean isBuiltIn() throws DbException {
        return getBuiltIn().booleanValue();
    }

    /**
     * Gets the "shape" of a DbBENotation's instance.
     * 
     * @return the "shape"
     **/
    public final SMSNotationShape getUseCaseShape() throws DbException {
        return (SMSNotationShape) get(fUseCaseShape);
    }

    /**
     * Gets the "orientation" of a DbBENotation's instance.
     * 
     * @return the "orientation"
     **/
    public final SMSZoneOrientation getFlowZoneOrientation() throws DbException {
        return (SMSZoneOrientation) get(fFlowZoneOrientation);
    }

    /**
     * Gets the "width" property of a DbBENotation's instance.
     * 
     * @return the "width" property
     **/
    public final Integer getUseCaseDefaultWidth() throws DbException {
        return (Integer) get(fUseCaseDefaultWidth);
    }

    /**
     * Gets the "height" property of a DbBENotation's instance.
     * 
     * @return the "height" property
     **/
    public final Integer getUseCaseDefaultHeight() throws DbException {
        return (Integer) get(fUseCaseDefaultHeight);
    }

    /**
     * Gets the "shape" of a DbBENotation's instance.
     * 
     * @return the "shape"
     **/
    public final SMSNotationShape getStoreShape() throws DbException {
        return (SMSNotationShape) get(fStoreShape);
    }

    /**
     * Gets the "orientation" of a DbBENotation's instance.
     * 
     * @return the "orientation"
     **/
    public final SMSZoneOrientation getStoreZoneOrientation() throws DbException {
        return (SMSZoneOrientation) get(fStoreZoneOrientation);
    }

    /**
     * Gets the "width" property of a DbBENotation's instance.
     * 
     * @return the "width" property
     **/
    public final Integer getStoreDefaultWidth() throws DbException {
        return (Integer) get(fStoreDefaultWidth);
    }

    /**
     * Gets the "height" property of a DbBENotation's instance.
     * 
     * @return the "height" property
     **/
    public final Integer getStoreDefaultHeight() throws DbException {
        return (Integer) get(fStoreDefaultHeight);
    }

    /**
     * Gets the "formatting string" property of a DbBENotation's instance.
     * 
     * @return the "formatting string" property
     **/
    public final String getStoreIdPrefix() throws DbException {
        return (String) get(fStoreIdPrefix);
    }

    /**
     * Gets the "shape" of a DbBENotation's instance.
     * 
     * @return the "shape"
     **/
    public final SMSNotationShape getActorShape() throws DbException {
        return (SMSNotationShape) get(fActorShape);
    }

    /**
     * Gets the "orientation" of a DbBENotation's instance.
     * 
     * @return the "orientation"
     **/
    public final SMSZoneOrientation getActorZoneOrientation() throws DbException {
        return (SMSZoneOrientation) get(fActorZoneOrientation);
    }

    /**
     * Gets the "width" property of a DbBENotation's instance.
     * 
     * @return the "width" property
     **/
    public final Integer getActorDefaultWidth() throws DbException {
        return (Integer) get(fActorDefaultWidth);
    }

    /**
     * Gets the "height" property of a DbBENotation's instance.
     * 
     * @return the "height" property
     **/
    public final Integer getActorDefaultHeight() throws DbException {
        return (Integer) get(fActorDefaultHeight);
    }

    /**
     * Gets the "formatting string" property of a DbBENotation's instance.
     * 
     * @return the "formatting string" property
     **/
    public final String getActorIdPrefix() throws DbException {
        return (String) get(fActorIdPrefix);
    }

    /**
     * Gets the "terminology" property of a DbBENotation's instance.
     * 
     * @return the "terminology" property
     **/
    public final String getTerminologyName() throws DbException {
        return (String) get(fTerminologyName);
    }

    /**
     * Gets the "display frame box" of a DbBENotation's instance.
     * 
     * @return the "display frame box"
     **/
    public final Boolean getDisplayFrameBox() throws DbException {
        return (Boolean) get(fDisplayFrameBox);
    }

    /**
     * Gets the "cell title within a box" of a DbBENotation's instance.
     * 
     * @return the "cell title within a box"
     **/
    public final Boolean getCellTitleBoxed() throws DbException {
        return (Boolean) get(fCellTitleBoxed);
    }

    /**
     * Gets the "display center of cells" of a DbBENotation's instance.
     * 
     * @return the "display center of cells"
     **/
    public final Boolean getCenterDisplay() throws DbException {
        return (Boolean) get(fCenterDisplay);
    }

    /**
     * Gets the "constraint to center" of a DbBENotation's instance.
     * 
     * @return the "constraint to center"
     **/
    public final Boolean getConstraintCenter() throws DbException {
        return (Boolean) get(fConstraintCenter);
    }

    /**
     * Gets the "bottom border default value" of a DbBENotation's instance.
     * 
     * @return the "bottom border default value"
     **/
    public final Boolean getDefButtomBorder() throws DbException {
        return (Boolean) get(fDefButtomBorder);
    }

    /**
     * Gets the "right border default value" of a DbBENotation's instance.
     * 
     * @return the "right border default value"
     **/
    public final Boolean getDefRightBorder() throws DbException {
        return (Boolean) get(fDefRightBorder);
    }

    /**
     * Gets the "vertical aligment default value" of a DbBENotation's instance.
     * 
     * @return the "vertical aligment default value"
     **/
    public final Integer getDefVerticalAlignment() throws DbException {
        return (Integer) get(fDefVerticalAlignment);
    }

    /**
     * Gets the "horizontal aligment default value" of a DbBENotation's instance.
     * 
     * @return the "horizontal aligment default value"
     **/
    public final Integer getDefHorizontalAlignment() throws DbException {
        return (Integer) get(fDefHorizontalAlignment);
    }

    /**
     * Gets the "has frame" of a DbBENotation's instance.
     * 
     * @return the "has frame"
     **/
    public final Boolean getHasFrame() throws DbException {
        return (Boolean) get(fHasFrame);
    }

    /**
     * Gets the "display stereotypes as icons" of a DbBENotation's instance.
     * 
     * @return the "display stereotypes as icons"
     **/
    public final Boolean getDisplayStereotypesAsIcons() throws DbException {
        return (Boolean) get(fDisplayStereotypesAsIcons);
    }

    /**
     * Gets the "orientation" of a DbBENotation's instance.
     * 
     * @return the "orientation"
     **/
    public final SMSZoneOrientation getUseCaseZoneOrientation() throws DbException {
        return (SMSZoneOrientation) get(fUseCaseZoneOrientation);
    }

    /**
     * Gets the "formatting string" property of a DbBENotation's instance.
     * 
     * @return the "formatting string" property
     **/
    public final String getFlowIdPrefix() throws DbException {
        return (String) get(fFlowIdPrefix);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
