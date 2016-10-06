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
import java.util.HashMap;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSProject.html">DbSMSProject</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbBEStyle extends DbSMSStyle {

    //Meta

    public static final MetaField fLineColorDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("lineColorDbBEUseCase"));
    public static final MetaField fAlphanumericIdentifierFontDbBEUseCase = new MetaField(
            LocaleMgr.db.getString("alphanumericIdentifierFontDbBEUseCase"));
    public static final MetaField fNumericIdentifierFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("numericIdentifierFontDbBEUseCase"));
    public static final MetaField fDashStyleDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("dashStyleDbBEUseCase"));
    public static final MetaField fTextColorDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("textColorDbBEUseCase"));
    public static final MetaField fDisplayHierAlphanumericIdDbBEUseCase = new MetaField(
            LocaleMgr.db.getString("displayHierAlphanumericIdDbBEUseCase"));
    public static final MetaField fDisplayHierNumericIdDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("displayHierNumericIdDbBEUseCase"));
    public static final MetaField fHighlightDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("highlightDbBEUseCase"));
    public static final MetaField fDisplayQualiferDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("displayQualiferDbBEUseCase"));
    public static final MetaField fBackgroundColorDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("backgroundColorDbBEUseCase"));
    public static final MetaRelation1 fReferringProjectBe = new MetaRelation1(LocaleMgr.db
            .getString("referringProjectBe"), 0);
    public static final MetaField fHighlightDbBEActor = new MetaField(LocaleMgr.db
            .getString("highlightDbBEActor"));
    public static final MetaField fDisplayQualiferDbBEActor = new MetaField(LocaleMgr.db
            .getString("displayQualiferDbBEActor"));
    public static final MetaField fLineColorDbBEActor = new MetaField(LocaleMgr.db
            .getString("lineColorDbBEActor"));
    public static final MetaField fDashStyleDbBEActor = new MetaField(LocaleMgr.db
            .getString("dashStyleDbBEActor"));
    public static final MetaField fBackgroundColorDbBEActor = new MetaField(LocaleMgr.db
            .getString("backgroundColorDbBEActor"));
    public static final MetaField fTextColorDbBEActor = new MetaField(LocaleMgr.db
            .getString("textColorDbBEActor"));
    public static final MetaField fHighlightDbBEStore = new MetaField(LocaleMgr.db
            .getString("highlightDbBEStore"));
    public static final MetaField fLineColorDbBEStore = new MetaField(LocaleMgr.db
            .getString("lineColorDbBEStore"));
    public static final MetaField fDisplayQualiferDbBEStore = new MetaField(LocaleMgr.db
            .getString("displayQualiferDbBEStore"));
    public static final MetaField fBackgroundColorDbBEStore = new MetaField(LocaleMgr.db
            .getString("backgroundColorDbBEStore"));
    public static final MetaField fTextColorDbBEStore = new MetaField(LocaleMgr.db
            .getString("textColorDbBEStore"));
    public static final MetaField fDashStyleDbBEStore = new MetaField(LocaleMgr.db
            .getString("dashStyleDbBEStore"));
    public static final MetaField fHighlightDbBEFlow = new MetaField(LocaleMgr.db
            .getString("highlightDbBEFlow"));
    public static final MetaField fLineColorDbBEFlow = new MetaField(LocaleMgr.db
            .getString("lineColorDbBEFlow"));
    public static final MetaField fDisplayLabelDbBEFlow = new MetaField(LocaleMgr.db
            .getString("displayLabelDbBEFlow"));
    public static final MetaField fDisplayQualiferDbBEFlow = new MetaField(LocaleMgr.db
            .getString("displayQualiferDbBEFlow"));
    public static final MetaField fDashStyleDbBEFlow = new MetaField(LocaleMgr.db
            .getString("dashStyleDbBEFlow"));
    public static final MetaField fBackgroundColorDbBEContextGo = new MetaField(LocaleMgr.db
            .getString("backgroundColorDbBEContextGo"));
    public static final MetaField fLineColorDbBEContextGo = new MetaField(LocaleMgr.db
            .getString("lineColorDbBEContextGo"));
    public static final MetaField fTextColorDbBEContextGo = new MetaField(LocaleMgr.db
            .getString("textColorDbBEContextGo"));
    public static final MetaField fNameFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("nameFontDbBEUseCase"));
    public static final MetaField fAliasFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("aliasFontDbBEUseCase"));
    public static final MetaField fPhysicalNameFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("physicalNameFontDbBEUseCase"));
    public static final MetaField fResourcesFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("resourcesFontDbBEUseCase"));
    public static final MetaField fNameFontDbBEStore = new MetaField(LocaleMgr.db
            .getString("nameFontDbBEStore"));
    public static final MetaField fIdentifierFontDbBEStore = new MetaField(LocaleMgr.db
            .getString("identifierFontDbBEStore"));
    public static final MetaField fAliasFontDbBEStore = new MetaField(LocaleMgr.db
            .getString("aliasFontDbBEStore"));
    public static final MetaField fPhysicalNameFontDbBEStore = new MetaField(LocaleMgr.db
            .getString("physicalNameFontDbBEStore"));
    public static final MetaField fNameFontDbBEActor = new MetaField(LocaleMgr.db
            .getString("nameFontDbBEActor"));
    public static final MetaField fIdentifierFontDbBEActor = new MetaField(LocaleMgr.db
            .getString("identifierFontDbBEActor"));
    public static final MetaField fAliasFontDbBEActor = new MetaField(LocaleMgr.db
            .getString("aliasFontDbBEActor"));
    public static final MetaField fPhysicalNameFontDbBEActor = new MetaField(LocaleMgr.db
            .getString("physicalNameFontDbBEActor"));
    public static final MetaField fIdentifierFontDbBEFlow = new MetaField(LocaleMgr.db
            .getString("identifierFontDbBEFlow"));
    public static final MetaField fNameFontDbBEFlow = new MetaField(LocaleMgr.db
            .getString("nameFontDbBEFlow"));
    public static final MetaField fAliasFontDbBEFlow = new MetaField(LocaleMgr.db
            .getString("aliasFontDbBEFlow"));
    public static final MetaField fPhysicalNameFontDbBEFlow = new MetaField(LocaleMgr.db
            .getString("physicalNameFontDbBEFlow"));
    public static final MetaField fFrequencyFontDbBEFlow = new MetaField(LocaleMgr.db
            .getString("frequencyFontDbBEFlow"));
    public static final MetaField fEmissionConditionFontDbBEFlow = new MetaField(LocaleMgr.db
            .getString("emissionConditionFontDbBEFlow"));
    public static final MetaField fFixedCostFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("fixedCostFontDbBEUseCase"));
    public static final MetaField fHighlightDbBEContextGo = new MetaField(LocaleMgr.db
            .getString("highlightDbBEContextGo"));
    public static final MetaField fDashStyleDbBEContextGo = new MetaField(LocaleMgr.db
            .getString("dashStyleDbBEContextGo"));
    public static final MetaField fFrameHeaderFont = new MetaField(LocaleMgr.db
            .getString("frameHeaderFont"));
    public static final MetaField fDescriptionFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("descriptionFontDbBEUseCase"));
    public static final MetaField fUdfFontDbBEStore = new MetaField(LocaleMgr.db
            .getString("udfFontDbBEStore"));
    public static final MetaField fUdfFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("udfFontDbBEUseCase"));
    public static final MetaField fUdfFontDbBEFlow = new MetaField(LocaleMgr.db
            .getString("udfFontDbBEFlow"));
    public static final MetaField fUdfFontDbBEActor = new MetaField(LocaleMgr.db
            .getString("udfFontDbBEActor"));
    public static final MetaField fDescriptionFontDbBEStore = new MetaField(LocaleMgr.db
            .getString("descriptionFontDbBEStore"));
    public static final MetaField fDescriptionFontDbBEFlow = new MetaField(LocaleMgr.db
            .getString("descriptionFontDbBEFlow"));
    public static final MetaField fDescriptionFontDbBEActor = new MetaField(LocaleMgr.db
            .getString("descriptionFontDbBEActor"));
    public static final MetaField fDefinitionFontDbBEActor = new MetaField(LocaleMgr.db
            .getString("definitionFontDbBEActor"));
    public static final MetaField fVolumeFontDbBEStore = new MetaField(LocaleMgr.db
            .getString("volumeFontDbBEStore"));
    public static final MetaField fTotalTimeFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("totalTimeFontDbBEUseCase"));
    public static final MetaField fTotalCostFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("totalCostFontDbBEUseCase"));
    public static final MetaField fSynchronizationRuleFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("synchronizationRuleFontDbBEUseCase"));
    public static final MetaField fResourceTimeFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("resourceTimeFontDbBEUseCase"));
    public static final MetaField fResourceCostFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("resourceCostFontDbBEUseCase"));
    public static final MetaField fPartialTimeFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("partialTimeFontDbBEUseCase"));
    public static final MetaField fPartialCostFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("partialCostFontDbBEUseCase"));
    public static final MetaField fFixedTimeFontDbBEUseCase = new MetaField(LocaleMgr.db
            .getString("fixedTimeFontDbBEUseCase"));
    public static final MetaField fDisplayStereotypeIcon = new MetaField(LocaleMgr.db
            .getString("displayStereotypeIcon"));
    public static final MetaField fDisplayStereotypeName = new MetaField(LocaleMgr.db
            .getString("displayStereotypeName"));
    public static final MetaField fDisplayStereotypeOnly = new MetaField(LocaleMgr.db
            .getString("displayStereotypeOnly"));
    public static final MetaField fLineColorDbBEContextCell = new MetaField(LocaleMgr.db
            .getString("lineColorDbBEContextCell"));
    public static final MetaField fHighlightDbBEContextCell = new MetaField(LocaleMgr.db
            .getString("highlightDbBEContextCell"));
    public static final MetaField fDashStyleDbBEContextCell = new MetaField(LocaleMgr.db
            .getString("dashStyleDbBEContextCell"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbBEStyle"),
            DbBEStyle.class, new MetaField[] { fLineColorDbBEUseCase,
                    fAlphanumericIdentifierFontDbBEUseCase, fNumericIdentifierFontDbBEUseCase,
                    fDashStyleDbBEUseCase, fTextColorDbBEUseCase,
                    fDisplayHierAlphanumericIdDbBEUseCase, fDisplayHierNumericIdDbBEUseCase,
                    fHighlightDbBEUseCase, fDisplayQualiferDbBEUseCase,
                    fBackgroundColorDbBEUseCase, fReferringProjectBe, fHighlightDbBEActor,
                    fDisplayQualiferDbBEActor, fLineColorDbBEActor, fDashStyleDbBEActor,
                    fBackgroundColorDbBEActor, fTextColorDbBEActor, fHighlightDbBEStore,
                    fLineColorDbBEStore, fDisplayQualiferDbBEStore, fBackgroundColorDbBEStore,
                    fTextColorDbBEStore, fDashStyleDbBEStore, fHighlightDbBEFlow,
                    fLineColorDbBEFlow, fDisplayLabelDbBEFlow, fDisplayQualiferDbBEFlow,
                    fDashStyleDbBEFlow, fBackgroundColorDbBEContextGo, fLineColorDbBEContextGo,
                    fTextColorDbBEContextGo, fNameFontDbBEUseCase, fAliasFontDbBEUseCase,
                    fPhysicalNameFontDbBEUseCase, fResourcesFontDbBEUseCase, fNameFontDbBEStore,
                    fIdentifierFontDbBEStore, fAliasFontDbBEStore, fPhysicalNameFontDbBEStore,
                    fNameFontDbBEActor, fIdentifierFontDbBEActor, fAliasFontDbBEActor,
                    fPhysicalNameFontDbBEActor, fIdentifierFontDbBEFlow, fNameFontDbBEFlow,
                    fAliasFontDbBEFlow, fPhysicalNameFontDbBEFlow, fFrequencyFontDbBEFlow,
                    fEmissionConditionFontDbBEFlow, fFixedCostFontDbBEUseCase,
                    fHighlightDbBEContextGo, fDashStyleDbBEContextGo, fFrameHeaderFont,
                    fDescriptionFontDbBEUseCase, fUdfFontDbBEStore, fUdfFontDbBEUseCase,
                    fUdfFontDbBEFlow, fUdfFontDbBEActor, fDescriptionFontDbBEStore,
                    fDescriptionFontDbBEFlow, fDescriptionFontDbBEActor, fDefinitionFontDbBEActor,
                    fVolumeFontDbBEStore, fTotalTimeFontDbBEUseCase, fTotalCostFontDbBEUseCase,
                    fSynchronizationRuleFontDbBEUseCase, fResourceTimeFontDbBEUseCase,
                    fResourceCostFontDbBEUseCase, fPartialTimeFontDbBEUseCase,
                    fPartialCostFontDbBEUseCase, fFixedTimeFontDbBEUseCase, fDisplayStereotypeIcon,
                    fDisplayStereotypeName, fDisplayStereotypeOnly, fLineColorDbBEContextCell,
                    fHighlightDbBEContextCell, fDashStyleDbBEContextCell }, MetaClass.MATCHABLE
                    | MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSStyle.metaClass);

            fLineColorDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_lineColorDbBEUseCase"));
            fAlphanumericIdentifierFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_alphanumericIdentifierFontDbBEUseCase"));
            fNumericIdentifierFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_numericIdentifierFontDbBEUseCase"));
            fDashStyleDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_dashStyleDbBEUseCase"));
            fTextColorDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_textColorDbBEUseCase"));
            fDisplayHierAlphanumericIdDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_displayHierAlphanumericIdDbBEUseCase"));
            fDisplayHierNumericIdDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_displayHierNumericIdDbBEUseCase"));
            fHighlightDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_highlightDbBEUseCase"));
            fDisplayQualiferDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_displayQualiferDbBEUseCase"));
            fBackgroundColorDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_backgroundColorDbBEUseCase"));
            fReferringProjectBe.setJField(DbBEStyle.class.getDeclaredField("m_referringProjectBe"));
            fHighlightDbBEActor.setJField(DbBEStyle.class.getDeclaredField("m_highlightDbBEActor"));
            fDisplayQualiferDbBEActor.setJField(DbBEStyle.class
                    .getDeclaredField("m_displayQualiferDbBEActor"));
            fLineColorDbBEActor.setJField(DbBEStyle.class.getDeclaredField("m_lineColorDbBEActor"));
            fDashStyleDbBEActor.setJField(DbBEStyle.class.getDeclaredField("m_dashStyleDbBEActor"));
            fBackgroundColorDbBEActor.setJField(DbBEStyle.class
                    .getDeclaredField("m_backgroundColorDbBEActor"));
            fTextColorDbBEActor.setJField(DbBEStyle.class.getDeclaredField("m_textColorDbBEActor"));
            fHighlightDbBEStore.setJField(DbBEStyle.class.getDeclaredField("m_highlightDbBEStore"));
            fLineColorDbBEStore.setJField(DbBEStyle.class.getDeclaredField("m_lineColorDbBEStore"));
            fDisplayQualiferDbBEStore.setJField(DbBEStyle.class
                    .getDeclaredField("m_displayQualiferDbBEStore"));
            fBackgroundColorDbBEStore.setJField(DbBEStyle.class
                    .getDeclaredField("m_backgroundColorDbBEStore"));
            fTextColorDbBEStore.setJField(DbBEStyle.class.getDeclaredField("m_textColorDbBEStore"));
            fDashStyleDbBEStore.setJField(DbBEStyle.class.getDeclaredField("m_dashStyleDbBEStore"));
            fHighlightDbBEFlow.setJField(DbBEStyle.class.getDeclaredField("m_highlightDbBEFlow"));
            fLineColorDbBEFlow.setJField(DbBEStyle.class.getDeclaredField("m_lineColorDbBEFlow"));
            fDisplayLabelDbBEFlow.setJField(DbBEStyle.class
                    .getDeclaredField("m_displayLabelDbBEFlow"));
            fDisplayQualiferDbBEFlow.setJField(DbBEStyle.class
                    .getDeclaredField("m_displayQualiferDbBEFlow"));
            fDashStyleDbBEFlow.setJField(DbBEStyle.class.getDeclaredField("m_dashStyleDbBEFlow"));
            fBackgroundColorDbBEContextGo.setJField(DbBEStyle.class
                    .getDeclaredField("m_backgroundColorDbBEContextGo"));
            fLineColorDbBEContextGo.setJField(DbBEStyle.class
                    .getDeclaredField("m_lineColorDbBEContextGo"));
            fTextColorDbBEContextGo.setJField(DbBEStyle.class
                    .getDeclaredField("m_textColorDbBEContextGo"));
            fNameFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_nameFontDbBEUseCase"));
            fAliasFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_aliasFontDbBEUseCase"));
            fPhysicalNameFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_physicalNameFontDbBEUseCase"));
            fResourcesFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_resourcesFontDbBEUseCase"));
            fNameFontDbBEStore.setJField(DbBEStyle.class.getDeclaredField("m_nameFontDbBEStore"));
            fIdentifierFontDbBEStore.setJField(DbBEStyle.class
                    .getDeclaredField("m_identifierFontDbBEStore"));
            fAliasFontDbBEStore.setJField(DbBEStyle.class.getDeclaredField("m_aliasFontDbBEStore"));
            fPhysicalNameFontDbBEStore.setJField(DbBEStyle.class
                    .getDeclaredField("m_physicalNameFontDbBEStore"));
            fNameFontDbBEActor.setJField(DbBEStyle.class.getDeclaredField("m_nameFontDbBEActor"));
            fIdentifierFontDbBEActor.setJField(DbBEStyle.class
                    .getDeclaredField("m_identifierFontDbBEActor"));
            fAliasFontDbBEActor.setJField(DbBEStyle.class.getDeclaredField("m_aliasFontDbBEActor"));
            fPhysicalNameFontDbBEActor.setJField(DbBEStyle.class
                    .getDeclaredField("m_physicalNameFontDbBEActor"));
            fIdentifierFontDbBEFlow.setJField(DbBEStyle.class
                    .getDeclaredField("m_identifierFontDbBEFlow"));
            fNameFontDbBEFlow.setJField(DbBEStyle.class.getDeclaredField("m_nameFontDbBEFlow"));
            fAliasFontDbBEFlow.setJField(DbBEStyle.class.getDeclaredField("m_aliasFontDbBEFlow"));
            fPhysicalNameFontDbBEFlow.setJField(DbBEStyle.class
                    .getDeclaredField("m_physicalNameFontDbBEFlow"));
            fFrequencyFontDbBEFlow.setJField(DbBEStyle.class
                    .getDeclaredField("m_frequencyFontDbBEFlow"));
            fEmissionConditionFontDbBEFlow.setJField(DbBEStyle.class
                    .getDeclaredField("m_emissionConditionFontDbBEFlow"));
            fFixedCostFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_fixedCostFontDbBEUseCase"));
            fHighlightDbBEContextGo.setJField(DbBEStyle.class
                    .getDeclaredField("m_highlightDbBEContextGo"));
            fDashStyleDbBEContextGo.setJField(DbBEStyle.class
                    .getDeclaredField("m_dashStyleDbBEContextGo"));
            fFrameHeaderFont.setJField(DbBEStyle.class.getDeclaredField("m_frameHeaderFont"));
            fDescriptionFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_descriptionFontDbBEUseCase"));
            fUdfFontDbBEStore.setJField(DbBEStyle.class.getDeclaredField("m_udfFontDbBEStore"));
            fUdfFontDbBEUseCase.setJField(DbBEStyle.class.getDeclaredField("m_udfFontDbBEUseCase"));
            fUdfFontDbBEFlow.setJField(DbBEStyle.class.getDeclaredField("m_udfFontDbBEFlow"));
            fUdfFontDbBEActor.setJField(DbBEStyle.class.getDeclaredField("m_udfFontDbBEActor"));
            fDescriptionFontDbBEStore.setJField(DbBEStyle.class
                    .getDeclaredField("m_descriptionFontDbBEStore"));
            fDescriptionFontDbBEFlow.setJField(DbBEStyle.class
                    .getDeclaredField("m_descriptionFontDbBEFlow"));
            fDescriptionFontDbBEActor.setJField(DbBEStyle.class
                    .getDeclaredField("m_descriptionFontDbBEActor"));
            fDefinitionFontDbBEActor.setJField(DbBEStyle.class
                    .getDeclaredField("m_definitionFontDbBEActor"));
            fVolumeFontDbBEStore.setJField(DbBEStyle.class
                    .getDeclaredField("m_volumeFontDbBEStore"));
            fTotalTimeFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_totalTimeFontDbBEUseCase"));
            fTotalCostFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_totalCostFontDbBEUseCase"));
            fSynchronizationRuleFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_synchronizationRuleFontDbBEUseCase"));
            fResourceTimeFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_resourceTimeFontDbBEUseCase"));
            fResourceCostFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_resourceCostFontDbBEUseCase"));
            fPartialTimeFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_partialTimeFontDbBEUseCase"));
            fPartialCostFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_partialCostFontDbBEUseCase"));
            fFixedTimeFontDbBEUseCase.setJField(DbBEStyle.class
                    .getDeclaredField("m_fixedTimeFontDbBEUseCase"));
            fDisplayStereotypeIcon.setJField(DbBEStyle.class
                    .getDeclaredField("m_displayStereotypeIcon"));
            fDisplayStereotypeName.setJField(DbBEStyle.class
                    .getDeclaredField("m_displayStereotypeName"));
            fDisplayStereotypeOnly.setJField(DbBEStyle.class
                    .getDeclaredField("m_displayStereotypeOnly"));
            fLineColorDbBEContextCell.setJField(DbBEStyle.class
                    .getDeclaredField("m_lineColorDbBEContextCell"));
            fHighlightDbBEContextCell.setJField(DbBEStyle.class
                    .getDeclaredField("m_highlightDbBEContextCell"));
            fDashStyleDbBEContextCell.setJField(DbBEStyle.class
                    .getDeclaredField("m_dashStyleDbBEContextCell"));

            fReferringProjectBe.setOppositeRel(DbSMSProject.fBeDefaultStyle);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    private static HashMap fieldMap;
    public static String[] be_listOptionTabs;
    public static final String SEQUENCE_KEYWORD = "%#";
    public static Object[][] be_optionValueGroups;
    public static String[] be_optionGroupHeaders;
    public static MetaField[][] be_optionGroups;
    public static MetaField[] be_colorOptions;
    public static Color[] be_colorOptionDefaultValues;
    public static MetaField[] be_fontOptions;
    public static Font[] be_fontOptionDefaultValues;
    public static MetaField[] be_displayOptions;
    public static Boolean[] be_displayOptionDefaultValues;
    public static Boolean[] be_lineOptionDefaultValues;
    public static MetaField[] be_lineOptions;
    public static String DEFAULT_STYLE_NAME = LocaleMgr.misc.getString("DefaultBEStyle");
    private static final Color TRANSPARENT = new Color(255, 255, 255, 0);
    public static String UML_USE_CASE_STYLE_NAME = LocaleMgr.misc.getString("umlUseCaseStyle");
    public static String UML_SEQUENCE_STYLE_NAME = LocaleMgr.misc.getString("umlSequenceStyle");
    public static String UML_STATE_STYLE_NAME = LocaleMgr.misc.getString("umlStateStyle");
    public static String UML_COLLABORATION_STYLE_NAME = LocaleMgr.misc
            .getString("umlCollaborationStyle");
    public static String UML_ACTIVITY_STYLE_NAME = LocaleMgr.misc.getString("umlActivityStyle");
    public static String UML_COMPONENT_STYLE_NAME = LocaleMgr.misc.getString("umlComponentStyle");
    public static String UML_DEPLOYMENT_STYLE_NAME = LocaleMgr.misc.getString("umlDeploymentStyle");

    static {
        org.modelsphere.sms.db.util.DbInitialization.initBeStyle();
    }

    //Instance variables
    SrColor m_lineColorDbBEUseCase;
    SrFont m_alphanumericIdentifierFontDbBEUseCase;
    SrFont m_numericIdentifierFontDbBEUseCase;
    SrBoolean m_dashStyleDbBEUseCase;
    SrColor m_textColorDbBEUseCase;
    SrBoolean m_displayHierAlphanumericIdDbBEUseCase;
    SrBoolean m_displayHierNumericIdDbBEUseCase;
    SrBoolean m_highlightDbBEUseCase;
    SrBoolean m_displayQualiferDbBEUseCase;
    SrColor m_backgroundColorDbBEUseCase;
    DbSMSProject m_referringProjectBe;
    SrBoolean m_highlightDbBEActor;
    SrBoolean m_displayQualiferDbBEActor;
    SrColor m_lineColorDbBEActor;
    SrBoolean m_dashStyleDbBEActor;
    SrColor m_backgroundColorDbBEActor;
    SrColor m_textColorDbBEActor;
    SrBoolean m_highlightDbBEStore;
    SrColor m_lineColorDbBEStore;
    SrBoolean m_displayQualiferDbBEStore;
    SrColor m_backgroundColorDbBEStore;
    SrColor m_textColorDbBEStore;
    SrBoolean m_dashStyleDbBEStore;
    SrBoolean m_highlightDbBEFlow;
    SrColor m_lineColorDbBEFlow;
    SrBoolean m_displayLabelDbBEFlow;
    SrBoolean m_displayQualiferDbBEFlow;
    SrBoolean m_dashStyleDbBEFlow;
    SrColor m_backgroundColorDbBEContextGo;
    SrColor m_lineColorDbBEContextGo;
    SrColor m_textColorDbBEContextGo;
    SrFont m_nameFontDbBEUseCase;
    SrFont m_aliasFontDbBEUseCase;
    SrFont m_physicalNameFontDbBEUseCase;
    SrFont m_resourcesFontDbBEUseCase;
    SrFont m_nameFontDbBEStore;
    SrFont m_identifierFontDbBEStore;
    SrFont m_aliasFontDbBEStore;
    SrFont m_physicalNameFontDbBEStore;
    SrFont m_nameFontDbBEActor;
    SrFont m_identifierFontDbBEActor;
    SrFont m_aliasFontDbBEActor;
    SrFont m_physicalNameFontDbBEActor;
    SrFont m_identifierFontDbBEFlow;
    SrFont m_nameFontDbBEFlow;
    SrFont m_aliasFontDbBEFlow;
    SrFont m_physicalNameFontDbBEFlow;
    SrFont m_frequencyFontDbBEFlow;
    SrFont m_emissionConditionFontDbBEFlow;
    SrFont m_fixedCostFontDbBEUseCase;
    SrBoolean m_highlightDbBEContextGo;
    SrBoolean m_dashStyleDbBEContextGo;
    SrFont m_frameHeaderFont;
    SrFont m_descriptionFontDbBEUseCase;
    SrFont m_udfFontDbBEStore;
    SrFont m_udfFontDbBEUseCase;
    SrFont m_udfFontDbBEFlow;
    SrFont m_udfFontDbBEActor;
    SrFont m_descriptionFontDbBEStore;
    SrFont m_descriptionFontDbBEFlow;
    SrFont m_descriptionFontDbBEActor;
    SrFont m_definitionFontDbBEActor;
    SrFont m_volumeFontDbBEStore;
    SrFont m_totalTimeFontDbBEUseCase;
    SrFont m_totalCostFontDbBEUseCase;
    SrFont m_synchronizationRuleFontDbBEUseCase;
    SrFont m_resourceTimeFontDbBEUseCase;
    SrFont m_resourceCostFontDbBEUseCase;
    SrFont m_partialTimeFontDbBEUseCase;
    SrFont m_partialCostFontDbBEUseCase;
    SrFont m_fixedTimeFontDbBEUseCase;
    SrBoolean m_displayStereotypeIcon;
    SrBoolean m_displayStereotypeName;
    SrBoolean m_displayStereotypeOnly;
    SrColor m_lineColorDbBEContextCell;
    SrBoolean m_highlightDbBEContextCell;
    SrBoolean m_dashStyleDbBEContextCell;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbBEStyle() {
    }

    /**
     * Creates an instance of DbBEStyle.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbBEStyle(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**

 **/
    public void initOptions() throws DbException {
        for (int i = 0; i < be_optionGroups.length; i++) {
            for (int j = 0; j < be_optionGroups[i].length; j++) {
                basicSet(be_optionGroups[i][j], be_optionValueGroups[i][j]);
            }
        }
    }

    /**

 **/
    public void initNullOptions() throws DbException {
        if (getAncestor() != null)
            return;
        for (int i = 0; i < be_optionGroups.length; i++) {
            for (int j = 0; j < be_optionGroups[i].length; j++) {
                if (get(be_optionGroups[i][j]) == null)
                    basicSet(be_optionGroups[i][j], be_optionValueGroups[i][j]);
            }
        }
    }

    /**
     * @param srcstyle
     *            org.modelsphere.jack.baseDb.db.DbObject
     **/
    public void copyOptions(DbObject srcStyle) throws DbException {
        for (int i = 0; i < be_optionGroups.length; i++) {
            for (int j = 0; j < be_optionGroups[i].length; j++) {
                MetaField metaField = be_optionGroups[i][j];
                basicSet(metaField, ((DbBEStyle) srcStyle).find(metaField));
            }
        }
    }

    /**
     * @param name
     *            java.lang.String
     * @return metafield
     **/
    public final MetaField getMetaField(String name) {
        if (fieldMap == null) {
            MetaField[] fields = metaClass.getAllMetaFields();
            fieldMap = new HashMap(fields.length + fields.length / 2);
            for (int i = 0; i < fields.length; i++)
                fieldMap.put(fields[i].getJName(), fields[i]);
        }
        return (MetaField) fieldMap.get(name);
    }

    //Setters

    /**
     * Sets the "process border" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process border" property
     **/
    public final void setLineColorDbBEUseCase(Color value) throws DbException {
        basicSet(fLineColorDbBEUseCase, value);
    }

    /**
     * Sets the "process alphanumerical identifier" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process alphanumerical identifier" property
     **/
    public final void setAlphanumericIdentifierFontDbBEUseCase(Font value) throws DbException {
        basicSet(fAlphanumericIdentifierFontDbBEUseCase, value);
    }

    /**
     * Sets the "process numerical identifier" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process numerical identifier" property
     **/
    public final void setNumericIdentifierFontDbBEUseCase(Font value) throws DbException {
        basicSet(fNumericIdentifierFontDbBEUseCase, value);
    }

    /**
     * Sets the "process dash style" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process dash style" property
     **/
    public final void setDashStyleDbBEUseCase(Boolean value) throws DbException {
        basicSet(fDashStyleDbBEUseCase, value);
    }

    /**
     * Sets the "process text" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process text" property
     **/
    public final void setTextColorDbBEUseCase(Color value) throws DbException {
        basicSet(fTextColorDbBEUseCase, value);
    }

    /**
     * Sets the "process hierarchical alphanumeric identifier" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process hierarchical alphanumeric identifier" property
     **/
    public final void setDisplayHierAlphanumericIdDbBEUseCase(Boolean value) throws DbException {
        basicSet(fDisplayHierAlphanumericIdDbBEUseCase, value);
    }

    /**
     * Sets the "process hierarchical numeric identifier" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process hierarchical numeric identifier" property
     **/
    public final void setDisplayHierNumericIdDbBEUseCase(Boolean value) throws DbException {
        basicSet(fDisplayHierNumericIdDbBEUseCase, value);
    }

    /**
     * Sets the "process highlight" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process highlight" property
     **/
    public final void setHighlightDbBEUseCase(Boolean value) throws DbException {
        basicSet(fHighlightDbBEUseCase, value);
    }

    /**
     * Sets the "process qualifiers" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process qualifiers" property
     **/
    public final void setDisplayQualiferDbBEUseCase(Boolean value) throws DbException {
        basicSet(fDisplayQualiferDbBEUseCase, value);
    }

    /**
     * Sets the "process background" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process background" property
     **/
    public final void setBackgroundColorDbBEUseCase(Color value) throws DbException {
        basicSet(fBackgroundColorDbBEUseCase, value);
    }

    /**
     * Sets the referringprojectbpm object associated to a DbBEStyle's instance.
     * 
     * @param value
     *            the referringprojectbpm object to be associated
     **/
    public final void setReferringProjectBe(DbSMSProject value) throws DbException {
        basicSet(fReferringProjectBe, value);
    }

    /**
     * Sets the "external entity highlight" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "external entity highlight" property
     **/
    public final void setHighlightDbBEActor(Boolean value) throws DbException {
        basicSet(fHighlightDbBEActor, value);
    }

    /**
     * Sets the "external entity qualifiers" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "external entity qualifiers" property
     **/
    public final void setDisplayQualiferDbBEActor(Boolean value) throws DbException {
        basicSet(fDisplayQualiferDbBEActor, value);
    }

    /**
     * Sets the "external entity border" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "external entity border" property
     **/
    public final void setLineColorDbBEActor(Color value) throws DbException {
        basicSet(fLineColorDbBEActor, value);
    }

    /**
     * Sets the "external entity dash style" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "external entity dash style" property
     **/
    public final void setDashStyleDbBEActor(Boolean value) throws DbException {
        basicSet(fDashStyleDbBEActor, value);
    }

    /**
     * Sets the "external entity background" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "external entity background" property
     **/
    public final void setBackgroundColorDbBEActor(Color value) throws DbException {
        basicSet(fBackgroundColorDbBEActor, value);
    }

    /**
     * Sets the "external entity text" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "external entity text" property
     **/
    public final void setTextColorDbBEActor(Color value) throws DbException {
        basicSet(fTextColorDbBEActor, value);
    }

    /**
     * Sets the "store highlight" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "store highlight" property
     **/
    public final void setHighlightDbBEStore(Boolean value) throws DbException {
        basicSet(fHighlightDbBEStore, value);
    }

    /**
     * Sets the "store border" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "store border" property
     **/
    public final void setLineColorDbBEStore(Color value) throws DbException {
        basicSet(fLineColorDbBEStore, value);
    }

    /**
     * Sets the "store qualifiers" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "store qualifiers" property
     **/
    public final void setDisplayQualiferDbBEStore(Boolean value) throws DbException {
        basicSet(fDisplayQualiferDbBEStore, value);
    }

    /**
     * Sets the "store background" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "store background" property
     **/
    public final void setBackgroundColorDbBEStore(Color value) throws DbException {
        basicSet(fBackgroundColorDbBEStore, value);
    }

    /**
     * Sets the "store text" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "store text" property
     **/
    public final void setTextColorDbBEStore(Color value) throws DbException {
        basicSet(fTextColorDbBEStore, value);
    }

    /**
     * Sets the "store dash style" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "store dash style" property
     **/
    public final void setDashStyleDbBEStore(Boolean value) throws DbException {
        basicSet(fDashStyleDbBEStore, value);
    }

    /**
     * Sets the "flow highlight" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "flow highlight" property
     **/
    public final void setHighlightDbBEFlow(Boolean value) throws DbException {
        basicSet(fHighlightDbBEFlow, value);
    }

    /**
     * Sets the "flow line" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "flow line" property
     **/
    public final void setLineColorDbBEFlow(Color value) throws DbException {
        basicSet(fLineColorDbBEFlow, value);
    }

    /**
     * Sets the "flow label" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "flow label" property
     **/
    public final void setDisplayLabelDbBEFlow(Boolean value) throws DbException {
        basicSet(fDisplayLabelDbBEFlow, value);
    }

    /**
     * Sets the "flow qualifiers" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "flow qualifiers" property
     **/
    public final void setDisplayQualiferDbBEFlow(Boolean value) throws DbException {
        basicSet(fDisplayQualiferDbBEFlow, value);
    }

    /**
     * Sets the "flow dash style" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "flow dash style" property
     **/
    public final void setDashStyleDbBEFlow(Boolean value) throws DbException {
        basicSet(fDashStyleDbBEFlow, value);
    }

    /**
     * Sets the "frame background" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "frame background" property
     **/
    public final void setBackgroundColorDbBEContextGo(Color value) throws DbException {
        basicSet(fBackgroundColorDbBEContextGo, value);
    }

    /**
     * Sets the "frame border" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "frame border" property
     **/
    public final void setLineColorDbBEContextGo(Color value) throws DbException {
        basicSet(fLineColorDbBEContextGo, value);
    }

    /**
     * Sets the "frame text" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "frame text" property
     **/
    public final void setTextColorDbBEContextGo(Color value) throws DbException {
        basicSet(fTextColorDbBEContextGo, value);
    }

    /**
     * Sets the "process name" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process name" property
     **/
    public final void setNameFontDbBEUseCase(Font value) throws DbException {
        basicSet(fNameFontDbBEUseCase, value);
    }

    /**
     * Sets the "process alias" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process alias" property
     **/
    public final void setAliasFontDbBEUseCase(Font value) throws DbException {
        basicSet(fAliasFontDbBEUseCase, value);
    }

    /**
     * Sets the "process physical name" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process physical name" property
     **/
    public final void setPhysicalNameFontDbBEUseCase(Font value) throws DbException {
        basicSet(fPhysicalNameFontDbBEUseCase, value);
    }

    /**
     * Sets the "process resources" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process resources" property
     **/
    public final void setResourcesFontDbBEUseCase(Font value) throws DbException {
        basicSet(fResourcesFontDbBEUseCase, value);
    }

    /**
     * Sets the "store name" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "store name" property
     **/
    public final void setNameFontDbBEStore(Font value) throws DbException {
        basicSet(fNameFontDbBEStore, value);
    }

    /**
     * Sets the "store identifier" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "store identifier" property
     **/
    public final void setIdentifierFontDbBEStore(Font value) throws DbException {
        basicSet(fIdentifierFontDbBEStore, value);
    }

    /**
     * Sets the "store alias" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "store alias" property
     **/
    public final void setAliasFontDbBEStore(Font value) throws DbException {
        basicSet(fAliasFontDbBEStore, value);
    }

    /**
     * Sets the "store physical name" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "store physical name" property
     **/
    public final void setPhysicalNameFontDbBEStore(Font value) throws DbException {
        basicSet(fPhysicalNameFontDbBEStore, value);
    }

    /**
     * Sets the "external entity name" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "external entity name" property
     **/
    public final void setNameFontDbBEActor(Font value) throws DbException {
        basicSet(fNameFontDbBEActor, value);
    }

    /**
     * Sets the "external entity identifier" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "external entity identifier" property
     **/
    public final void setIdentifierFontDbBEActor(Font value) throws DbException {
        basicSet(fIdentifierFontDbBEActor, value);
    }

    /**
     * Sets the "external entity alias" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "external entity alias" property
     **/
    public final void setAliasFontDbBEActor(Font value) throws DbException {
        basicSet(fAliasFontDbBEActor, value);
    }

    /**
     * Sets the "external entity physical name" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "external entity physical name" property
     **/
    public final void setPhysicalNameFontDbBEActor(Font value) throws DbException {
        basicSet(fPhysicalNameFontDbBEActor, value);
    }

    /**
     * Sets the "flow identifier" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "flow identifier" property
     **/
    public final void setIdentifierFontDbBEFlow(Font value) throws DbException {
        basicSet(fIdentifierFontDbBEFlow, value);
    }

    /**
     * Sets the "flow name" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "flow name" property
     **/
    public final void setNameFontDbBEFlow(Font value) throws DbException {
        basicSet(fNameFontDbBEFlow, value);
    }

    /**
     * Sets the "flow alias" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "flow alias" property
     **/
    public final void setAliasFontDbBEFlow(Font value) throws DbException {
        basicSet(fAliasFontDbBEFlow, value);
    }

    /**
     * Sets the "flow physical name" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "flow physical name" property
     **/
    public final void setPhysicalNameFontDbBEFlow(Font value) throws DbException {
        basicSet(fPhysicalNameFontDbBEFlow, value);
    }

    /**
     * Sets the "flow frequency" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "flow frequency" property
     **/
    public final void setFrequencyFontDbBEFlow(Font value) throws DbException {
        basicSet(fFrequencyFontDbBEFlow, value);
    }

    /**
     * Sets the "flow emission condition" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "flow emission condition" property
     **/
    public final void setEmissionConditionFontDbBEFlow(Font value) throws DbException {
        basicSet(fEmissionConditionFontDbBEFlow, value);
    }

    /**
     * Sets the "process fixed cost" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process fixed cost" property
     **/
    public final void setFixedCostFontDbBEUseCase(Font value) throws DbException {
        basicSet(fFixedCostFontDbBEUseCase, value);
    }

    /**
     * Sets the "frame highlight" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "frame highlight" property
     **/
    public final void setHighlightDbBEContextGo(Boolean value) throws DbException {
        basicSet(fHighlightDbBEContextGo, value);
    }

    /**
     * Sets the "frame dash style" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "frame dash style" property
     **/
    public final void setDashStyleDbBEContextGo(Boolean value) throws DbException {
        basicSet(fDashStyleDbBEContextGo, value);
    }

    /**
     * Sets the "frame header" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "frame header" property
     **/
    public final void setFrameHeaderFont(Font value) throws DbException {
        basicSet(fFrameHeaderFont, value);
    }

    /**
     * Sets the "process description" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process description" property
     **/
    public final void setDescriptionFontDbBEUseCase(Font value) throws DbException {
        basicSet(fDescriptionFontDbBEUseCase, value);
    }

    /**
     * Sets the "store user properties" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "store user properties" property
     **/
    public final void setUdfFontDbBEStore(Font value) throws DbException {
        basicSet(fUdfFontDbBEStore, value);
    }

    /**
     * Sets the "process user properties" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process user properties" property
     **/
    public final void setUdfFontDbBEUseCase(Font value) throws DbException {
        basicSet(fUdfFontDbBEUseCase, value);
    }

    /**
     * Sets the "flow user properties" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "flow user properties" property
     **/
    public final void setUdfFontDbBEFlow(Font value) throws DbException {
        basicSet(fUdfFontDbBEFlow, value);
    }

    /**
     * Sets the "external entity user properties" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "external entity user properties" property
     **/
    public final void setUdfFontDbBEActor(Font value) throws DbException {
        basicSet(fUdfFontDbBEActor, value);
    }

    /**
     * Sets the "store description" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "store description" property
     **/
    public final void setDescriptionFontDbBEStore(Font value) throws DbException {
        basicSet(fDescriptionFontDbBEStore, value);
    }

    /**
     * Sets the "flow description" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "flow description" property
     **/
    public final void setDescriptionFontDbBEFlow(Font value) throws DbException {
        basicSet(fDescriptionFontDbBEFlow, value);
    }

    /**
     * Sets the "external entity description" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "external entity description" property
     **/
    public final void setDescriptionFontDbBEActor(Font value) throws DbException {
        basicSet(fDescriptionFontDbBEActor, value);
    }

    /**
     * Sets the "external entity definition" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "external entity definition" property
     **/
    public final void setDefinitionFontDbBEActor(Font value) throws DbException {
        basicSet(fDefinitionFontDbBEActor, value);
    }

    /**
     * Sets the "store volume" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "store volume" property
     **/
    public final void setVolumeFontDbBEStore(Font value) throws DbException {
        basicSet(fVolumeFontDbBEStore, value);
    }

    /**
     * Sets the "process total time" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process total time" property
     **/
    public final void setTotalTimeFontDbBEUseCase(Font value) throws DbException {
        basicSet(fTotalTimeFontDbBEUseCase, value);
    }

    /**
     * Sets the "process total cost" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process total cost" property
     **/
    public final void setTotalCostFontDbBEUseCase(Font value) throws DbException {
        basicSet(fTotalCostFontDbBEUseCase, value);
    }

    /**
     * Sets the "process synchronization rule" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process synchronization rule" property
     **/
    public final void setSynchronizationRuleFontDbBEUseCase(Font value) throws DbException {
        basicSet(fSynchronizationRuleFontDbBEUseCase, value);
    }

    /**
     * Sets the "process resource time" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process resource time" property
     **/
    public final void setResourceTimeFontDbBEUseCase(Font value) throws DbException {
        basicSet(fResourceTimeFontDbBEUseCase, value);
    }

    /**
     * Sets the "process resource cost" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process resource cost" property
     **/
    public final void setResourceCostFontDbBEUseCase(Font value) throws DbException {
        basicSet(fResourceCostFontDbBEUseCase, value);
    }

    /**
     * Sets the "process partial time" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process partial time" property
     **/
    public final void setPartialTimeFontDbBEUseCase(Font value) throws DbException {
        basicSet(fPartialTimeFontDbBEUseCase, value);
    }

    /**
     * Sets the "process partial cost" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process partial cost" property
     **/
    public final void setPartialCostFontDbBEUseCase(Font value) throws DbException {
        basicSet(fPartialCostFontDbBEUseCase, value);
    }

    /**
     * Sets the "process fixed time" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "process fixed time" property
     **/
    public final void setFixedTimeFontDbBEUseCase(Font value) throws DbException {
        basicSet(fFixedTimeFontDbBEUseCase, value);
    }

    /**
     * Sets the "stereotype icon" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "stereotype icon" property
     **/
    public final void setDisplayStereotypeIcon(Boolean value) throws DbException {
        basicSet(fDisplayStereotypeIcon, value);
    }

    /**
     * Sets the "stereotype name" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "stereotype name" property
     **/
    public final void setDisplayStereotypeName(Boolean value) throws DbException {
        basicSet(fDisplayStereotypeName, value);
    }

    /**
     * Sets the "stereotype only" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "stereotype only" property
     **/
    public final void setDisplayStereotypeOnly(Boolean value) throws DbException {
        basicSet(fDisplayStereotypeOnly, value);
    }

    /**
     * Sets the "cell boundary color" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "cell boundary color" property
     **/
    public final void setLineColorDbBEContextCell(Color value) throws DbException {
        basicSet(fLineColorDbBEContextCell, value);
    }

    /**
     * Sets the "highlight cell boundaries" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "highlight cell boundaries" property
     **/
    public final void setHighlightDbBEContextCell(Boolean value) throws DbException {
        basicSet(fHighlightDbBEContextCell, value);
    }

    /**
     * Sets the "dash style cell boundaries" property of a DbBEStyle's instance.
     * 
     * @param value
     *            the "dash style cell boundaries" property
     **/
    public final void setDashStyleDbBEContextCell(Boolean value) throws DbException {
        basicSet(fDashStyleDbBEContextCell, value);
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
     * Gets the "process border" of a DbBEStyle's instance.
     * 
     * @return the "process border"
     **/
    public final Color getLineColorDbBEUseCase() throws DbException {
        return (Color) get(fLineColorDbBEUseCase);
    }

    /**
     * Gets the "process alphanumerical identifier" of a DbBEStyle's instance.
     * 
     * @return the "process alphanumerical identifier"
     **/
    public final Font getAlphanumericIdentifierFontDbBEUseCase() throws DbException {
        return (Font) get(fAlphanumericIdentifierFontDbBEUseCase);
    }

    /**
     * Gets the "process numerical identifier" of a DbBEStyle's instance.
     * 
     * @return the "process numerical identifier"
     **/
    public final Font getNumericIdentifierFontDbBEUseCase() throws DbException {
        return (Font) get(fNumericIdentifierFontDbBEUseCase);
    }

    /**
     * Gets the "process dash style" of a DbBEStyle's instance.
     * 
     * @return the "process dash style"
     **/
    public final Boolean getDashStyleDbBEUseCase() throws DbException {
        return (Boolean) get(fDashStyleDbBEUseCase);
    }

    /**
     * Gets the "process text" of a DbBEStyle's instance.
     * 
     * @return the "process text"
     **/
    public final Color getTextColorDbBEUseCase() throws DbException {
        return (Color) get(fTextColorDbBEUseCase);
    }

    /**
     * Gets the "process hierarchical alphanumeric identifier" of a DbBEStyle's instance.
     * 
     * @return the "process hierarchical alphanumeric identifier"
     **/
    public final Boolean getDisplayHierAlphanumericIdDbBEUseCase() throws DbException {
        return (Boolean) get(fDisplayHierAlphanumericIdDbBEUseCase);
    }

    /**
     * Gets the "process hierarchical numeric identifier" of a DbBEStyle's instance.
     * 
     * @return the "process hierarchical numeric identifier"
     **/
    public final Boolean getDisplayHierNumericIdDbBEUseCase() throws DbException {
        return (Boolean) get(fDisplayHierNumericIdDbBEUseCase);
    }

    /**
     * Gets the "process highlight" of a DbBEStyle's instance.
     * 
     * @return the "process highlight"
     **/
    public final Boolean getHighlightDbBEUseCase() throws DbException {
        return (Boolean) get(fHighlightDbBEUseCase);
    }

    /**
     * Gets the "process qualifiers" of a DbBEStyle's instance.
     * 
     * @return the "process qualifiers"
     **/
    public final Boolean getDisplayQualiferDbBEUseCase() throws DbException {
        return (Boolean) get(fDisplayQualiferDbBEUseCase);
    }

    /**
     * Gets the "process background" of a DbBEStyle's instance.
     * 
     * @return the "process background"
     **/
    public final Color getBackgroundColorDbBEUseCase() throws DbException {
        return (Color) get(fBackgroundColorDbBEUseCase);
    }

    /**
     * Gets the referringprojectbpm object associated to a DbBEStyle's instance.
     * 
     * @return the referringprojectbpm object
     **/
    public final DbSMSProject getReferringProjectBe() throws DbException {
        return (DbSMSProject) get(fReferringProjectBe);
    }

    /**
     * Gets the "external entity highlight" of a DbBEStyle's instance.
     * 
     * @return the "external entity highlight"
     **/
    public final Boolean getHighlightDbBEActor() throws DbException {
        return (Boolean) get(fHighlightDbBEActor);
    }

    /**
     * Gets the "external entity qualifiers" of a DbBEStyle's instance.
     * 
     * @return the "external entity qualifiers"
     **/
    public final Boolean getDisplayQualiferDbBEActor() throws DbException {
        return (Boolean) get(fDisplayQualiferDbBEActor);
    }

    /**
     * Gets the "external entity border" of a DbBEStyle's instance.
     * 
     * @return the "external entity border"
     **/
    public final Color getLineColorDbBEActor() throws DbException {
        return (Color) get(fLineColorDbBEActor);
    }

    /**
     * Gets the "external entity dash style" of a DbBEStyle's instance.
     * 
     * @return the "external entity dash style"
     **/
    public final Boolean getDashStyleDbBEActor() throws DbException {
        return (Boolean) get(fDashStyleDbBEActor);
    }

    /**
     * Gets the "external entity background" of a DbBEStyle's instance.
     * 
     * @return the "external entity background"
     **/
    public final Color getBackgroundColorDbBEActor() throws DbException {
        return (Color) get(fBackgroundColorDbBEActor);
    }

    /**
     * Gets the "external entity text" of a DbBEStyle's instance.
     * 
     * @return the "external entity text"
     **/
    public final Color getTextColorDbBEActor() throws DbException {
        return (Color) get(fTextColorDbBEActor);
    }

    /**
     * Gets the "store highlight" of a DbBEStyle's instance.
     * 
     * @return the "store highlight"
     **/
    public final Boolean getHighlightDbBEStore() throws DbException {
        return (Boolean) get(fHighlightDbBEStore);
    }

    /**
     * Gets the "store border" of a DbBEStyle's instance.
     * 
     * @return the "store border"
     **/
    public final Color getLineColorDbBEStore() throws DbException {
        return (Color) get(fLineColorDbBEStore);
    }

    /**
     * Gets the "store qualifiers" of a DbBEStyle's instance.
     * 
     * @return the "store qualifiers"
     **/
    public final Boolean getDisplayQualiferDbBEStore() throws DbException {
        return (Boolean) get(fDisplayQualiferDbBEStore);
    }

    /**
     * Gets the "store background" of a DbBEStyle's instance.
     * 
     * @return the "store background"
     **/
    public final Color getBackgroundColorDbBEStore() throws DbException {
        return (Color) get(fBackgroundColorDbBEStore);
    }

    /**
     * Gets the "store text" of a DbBEStyle's instance.
     * 
     * @return the "store text"
     **/
    public final Color getTextColorDbBEStore() throws DbException {
        return (Color) get(fTextColorDbBEStore);
    }

    /**
     * Gets the "store dash style" of a DbBEStyle's instance.
     * 
     * @return the "store dash style"
     **/
    public final Boolean getDashStyleDbBEStore() throws DbException {
        return (Boolean) get(fDashStyleDbBEStore);
    }

    /**
     * Gets the "flow highlight" of a DbBEStyle's instance.
     * 
     * @return the "flow highlight"
     **/
    public final Boolean getHighlightDbBEFlow() throws DbException {
        return (Boolean) get(fHighlightDbBEFlow);
    }

    /**
     * Gets the "flow line" of a DbBEStyle's instance.
     * 
     * @return the "flow line"
     **/
    public final Color getLineColorDbBEFlow() throws DbException {
        return (Color) get(fLineColorDbBEFlow);
    }

    /**
     * Gets the "flow label" of a DbBEStyle's instance.
     * 
     * @return the "flow label"
     **/
    public final Boolean getDisplayLabelDbBEFlow() throws DbException {
        return (Boolean) get(fDisplayLabelDbBEFlow);
    }

    /**
     * Gets the "flow qualifiers" of a DbBEStyle's instance.
     * 
     * @return the "flow qualifiers"
     **/
    public final Boolean getDisplayQualiferDbBEFlow() throws DbException {
        return (Boolean) get(fDisplayQualiferDbBEFlow);
    }

    /**
     * Gets the "flow dash style" of a DbBEStyle's instance.
     * 
     * @return the "flow dash style"
     **/
    public final Boolean getDashStyleDbBEFlow() throws DbException {
        return (Boolean) get(fDashStyleDbBEFlow);
    }

    /**
     * Gets the "frame background" of a DbBEStyle's instance.
     * 
     * @return the "frame background"
     **/
    public final Color getBackgroundColorDbBEContextGo() throws DbException {
        return (Color) get(fBackgroundColorDbBEContextGo);
    }

    /**
     * Gets the "frame border" of a DbBEStyle's instance.
     * 
     * @return the "frame border"
     **/
    public final Color getLineColorDbBEContextGo() throws DbException {
        return (Color) get(fLineColorDbBEContextGo);
    }

    /**
     * Gets the "frame text" of a DbBEStyle's instance.
     * 
     * @return the "frame text"
     **/
    public final Color getTextColorDbBEContextGo() throws DbException {
        return (Color) get(fTextColorDbBEContextGo);
    }

    /**
     * Gets the "process name" of a DbBEStyle's instance.
     * 
     * @return the "process name"
     **/
    public final Font getNameFontDbBEUseCase() throws DbException {
        return (Font) get(fNameFontDbBEUseCase);
    }

    /**
     * Gets the "process alias" of a DbBEStyle's instance.
     * 
     * @return the "process alias"
     **/
    public final Font getAliasFontDbBEUseCase() throws DbException {
        return (Font) get(fAliasFontDbBEUseCase);
    }

    /**
     * Gets the "process physical name" of a DbBEStyle's instance.
     * 
     * @return the "process physical name"
     **/
    public final Font getPhysicalNameFontDbBEUseCase() throws DbException {
        return (Font) get(fPhysicalNameFontDbBEUseCase);
    }

    /**
     * Gets the "process resources" of a DbBEStyle's instance.
     * 
     * @return the "process resources"
     **/
    public final Font getResourcesFontDbBEUseCase() throws DbException {
        return (Font) get(fResourcesFontDbBEUseCase);
    }

    /**
     * Gets the "store name" of a DbBEStyle's instance.
     * 
     * @return the "store name"
     **/
    public final Font getNameFontDbBEStore() throws DbException {
        return (Font) get(fNameFontDbBEStore);
    }

    /**
     * Gets the "store identifier" of a DbBEStyle's instance.
     * 
     * @return the "store identifier"
     **/
    public final Font getIdentifierFontDbBEStore() throws DbException {
        return (Font) get(fIdentifierFontDbBEStore);
    }

    /**
     * Gets the "store alias" of a DbBEStyle's instance.
     * 
     * @return the "store alias"
     **/
    public final Font getAliasFontDbBEStore() throws DbException {
        return (Font) get(fAliasFontDbBEStore);
    }

    /**
     * Gets the "store physical name" of a DbBEStyle's instance.
     * 
     * @return the "store physical name"
     **/
    public final Font getPhysicalNameFontDbBEStore() throws DbException {
        return (Font) get(fPhysicalNameFontDbBEStore);
    }

    /**
     * Gets the "external entity name" of a DbBEStyle's instance.
     * 
     * @return the "external entity name"
     **/
    public final Font getNameFontDbBEActor() throws DbException {
        return (Font) get(fNameFontDbBEActor);
    }

    /**
     * Gets the "external entity identifier" of a DbBEStyle's instance.
     * 
     * @return the "external entity identifier"
     **/
    public final Font getIdentifierFontDbBEActor() throws DbException {
        return (Font) get(fIdentifierFontDbBEActor);
    }

    /**
     * Gets the "external entity alias" of a DbBEStyle's instance.
     * 
     * @return the "external entity alias"
     **/
    public final Font getAliasFontDbBEActor() throws DbException {
        return (Font) get(fAliasFontDbBEActor);
    }

    /**
     * Gets the "external entity physical name" of a DbBEStyle's instance.
     * 
     * @return the "external entity physical name"
     **/
    public final Font getPhysicalNameFontDbBEActor() throws DbException {
        return (Font) get(fPhysicalNameFontDbBEActor);
    }

    /**
     * Gets the "flow identifier" of a DbBEStyle's instance.
     * 
     * @return the "flow identifier"
     **/
    public final Font getIdentifierFontDbBEFlow() throws DbException {
        return (Font) get(fIdentifierFontDbBEFlow);
    }

    /**
     * Gets the "flow name" of a DbBEStyle's instance.
     * 
     * @return the "flow name"
     **/
    public final Font getNameFontDbBEFlow() throws DbException {
        return (Font) get(fNameFontDbBEFlow);
    }

    /**
     * Gets the "flow alias" of a DbBEStyle's instance.
     * 
     * @return the "flow alias"
     **/
    public final Font getAliasFontDbBEFlow() throws DbException {
        return (Font) get(fAliasFontDbBEFlow);
    }

    /**
     * Gets the "flow physical name" of a DbBEStyle's instance.
     * 
     * @return the "flow physical name"
     **/
    public final Font getPhysicalNameFontDbBEFlow() throws DbException {
        return (Font) get(fPhysicalNameFontDbBEFlow);
    }

    /**
     * Gets the "flow frequency" of a DbBEStyle's instance.
     * 
     * @return the "flow frequency"
     **/
    public final Font getFrequencyFontDbBEFlow() throws DbException {
        return (Font) get(fFrequencyFontDbBEFlow);
    }

    /**
     * Gets the "flow emission condition" of a DbBEStyle's instance.
     * 
     * @return the "flow emission condition"
     **/
    public final Font getEmissionConditionFontDbBEFlow() throws DbException {
        return (Font) get(fEmissionConditionFontDbBEFlow);
    }

    /**
     * Gets the "process fixed cost" of a DbBEStyle's instance.
     * 
     * @return the "process fixed cost"
     **/
    public final Font getFixedCostFontDbBEUseCase() throws DbException {
        return (Font) get(fFixedCostFontDbBEUseCase);
    }

    /**
     * Gets the "frame highlight" of a DbBEStyle's instance.
     * 
     * @return the "frame highlight"
     **/
    public final Boolean getHighlightDbBEContextGo() throws DbException {
        return (Boolean) get(fHighlightDbBEContextGo);
    }

    /**
     * Gets the "frame dash style" of a DbBEStyle's instance.
     * 
     * @return the "frame dash style"
     **/
    public final Boolean getDashStyleDbBEContextGo() throws DbException {
        return (Boolean) get(fDashStyleDbBEContextGo);
    }

    /**
     * Gets the "frame header" of a DbBEStyle's instance.
     * 
     * @return the "frame header"
     **/
    public final Font getFrameHeaderFont() throws DbException {
        return (Font) get(fFrameHeaderFont);
    }

    /**
     * Gets the "process description" of a DbBEStyle's instance.
     * 
     * @return the "process description"
     **/
    public final Font getDescriptionFontDbBEUseCase() throws DbException {
        return (Font) get(fDescriptionFontDbBEUseCase);
    }

    /**
     * Gets the "store user properties" of a DbBEStyle's instance.
     * 
     * @return the "store user properties"
     **/
    public final Font getUdfFontDbBEStore() throws DbException {
        return (Font) get(fUdfFontDbBEStore);
    }

    /**
     * Gets the "process user properties" of a DbBEStyle's instance.
     * 
     * @return the "process user properties"
     **/
    public final Font getUdfFontDbBEUseCase() throws DbException {
        return (Font) get(fUdfFontDbBEUseCase);
    }

    /**
     * Gets the "flow user properties" of a DbBEStyle's instance.
     * 
     * @return the "flow user properties"
     **/
    public final Font getUdfFontDbBEFlow() throws DbException {
        return (Font) get(fUdfFontDbBEFlow);
    }

    /**
     * Gets the "external entity user properties" of a DbBEStyle's instance.
     * 
     * @return the "external entity user properties"
     **/
    public final Font getUdfFontDbBEActor() throws DbException {
        return (Font) get(fUdfFontDbBEActor);
    }

    /**
     * Gets the "store description" of a DbBEStyle's instance.
     * 
     * @return the "store description"
     **/
    public final Font getDescriptionFontDbBEStore() throws DbException {
        return (Font) get(fDescriptionFontDbBEStore);
    }

    /**
     * Gets the "flow description" of a DbBEStyle's instance.
     * 
     * @return the "flow description"
     **/
    public final Font getDescriptionFontDbBEFlow() throws DbException {
        return (Font) get(fDescriptionFontDbBEFlow);
    }

    /**
     * Gets the "external entity description" of a DbBEStyle's instance.
     * 
     * @return the "external entity description"
     **/
    public final Font getDescriptionFontDbBEActor() throws DbException {
        return (Font) get(fDescriptionFontDbBEActor);
    }

    /**
     * Gets the "external entity definition" of a DbBEStyle's instance.
     * 
     * @return the "external entity definition"
     **/
    public final Font getDefinitionFontDbBEActor() throws DbException {
        return (Font) get(fDefinitionFontDbBEActor);
    }

    /**
     * Gets the "store volume" of a DbBEStyle's instance.
     * 
     * @return the "store volume"
     **/
    public final Font getVolumeFontDbBEStore() throws DbException {
        return (Font) get(fVolumeFontDbBEStore);
    }

    /**
     * Gets the "process total time" of a DbBEStyle's instance.
     * 
     * @return the "process total time"
     **/
    public final Font getTotalTimeFontDbBEUseCase() throws DbException {
        return (Font) get(fTotalTimeFontDbBEUseCase);
    }

    /**
     * Gets the "process total cost" of a DbBEStyle's instance.
     * 
     * @return the "process total cost"
     **/
    public final Font getTotalCostFontDbBEUseCase() throws DbException {
        return (Font) get(fTotalCostFontDbBEUseCase);
    }

    /**
     * Gets the "process synchronization rule" of a DbBEStyle's instance.
     * 
     * @return the "process synchronization rule"
     **/
    public final Font getSynchronizationRuleFontDbBEUseCase() throws DbException {
        return (Font) get(fSynchronizationRuleFontDbBEUseCase);
    }

    /**
     * Gets the "process resource time" of a DbBEStyle's instance.
     * 
     * @return the "process resource time"
     **/
    public final Font getResourceTimeFontDbBEUseCase() throws DbException {
        return (Font) get(fResourceTimeFontDbBEUseCase);
    }

    /**
     * Gets the "process resource cost" of a DbBEStyle's instance.
     * 
     * @return the "process resource cost"
     **/
    public final Font getResourceCostFontDbBEUseCase() throws DbException {
        return (Font) get(fResourceCostFontDbBEUseCase);
    }

    /**
     * Gets the "process partial time" of a DbBEStyle's instance.
     * 
     * @return the "process partial time"
     **/
    public final Font getPartialTimeFontDbBEUseCase() throws DbException {
        return (Font) get(fPartialTimeFontDbBEUseCase);
    }

    /**
     * Gets the "process partial cost" of a DbBEStyle's instance.
     * 
     * @return the "process partial cost"
     **/
    public final Font getPartialCostFontDbBEUseCase() throws DbException {
        return (Font) get(fPartialCostFontDbBEUseCase);
    }

    /**
     * Gets the "process fixed time" of a DbBEStyle's instance.
     * 
     * @return the "process fixed time"
     **/
    public final Font getFixedTimeFontDbBEUseCase() throws DbException {
        return (Font) get(fFixedTimeFontDbBEUseCase);
    }

    /**
     * Gets the "stereotype icon" of a DbBEStyle's instance.
     * 
     * @return the "stereotype icon"
     **/
    public final Boolean getDisplayStereotypeIcon() throws DbException {
        return (Boolean) get(fDisplayStereotypeIcon);
    }

    /**
     * Gets the "stereotype name" of a DbBEStyle's instance.
     * 
     * @return the "stereotype name"
     **/
    public final Boolean getDisplayStereotypeName() throws DbException {
        return (Boolean) get(fDisplayStereotypeName);
    }

    /**
     * Gets the "stereotype only" of a DbBEStyle's instance.
     * 
     * @return the "stereotype only"
     **/
    public final Boolean getDisplayStereotypeOnly() throws DbException {
        return (Boolean) get(fDisplayStereotypeOnly);
    }

    /**
     * Gets the "cell boundary color" of a DbBEStyle's instance.
     * 
     * @return the "cell boundary color"
     **/
    public final Color getLineColorDbBEContextCell() throws DbException {
        return (Color) get(fLineColorDbBEContextCell);
    }

    /**
     * Gets the "highlight cell boundaries" of a DbBEStyle's instance.
     * 
     * @return the "highlight cell boundaries"
     **/
    public final Boolean getHighlightDbBEContextCell() throws DbException {
        return (Boolean) get(fHighlightDbBEContextCell);
    }

    /**
     * Gets the "dash style cell boundaries" of a DbBEStyle's instance.
     * 
     * @return the "dash style cell boundaries"
     **/
    public final Boolean getDashStyleDbBEContextCell() throws DbException {
        return (Boolean) get(fDashStyleDbBEContextCell);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
