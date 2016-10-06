/*************************************************************************

Copyright (C) 2008 Grandite

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
import java.util.HashMap;

/**
<b>Direct subclass(es)/subinterface(s) : </b>none.<br>

    <b>Composites : </b><A HREF="../../../../../org/modelsphere/sms/db/DbSMSProject.html">DbSMSProject</A>.<br>
    <b>Components : </b>none.<br>
 **/
public final class DbORStyle extends DbSMSStyle {

  //Meta

  public static final MetaField fOr_columnLinkedToSuperModelPrefix
    = new MetaField(LocaleMgr.db.getString("or_columnLinkedToSuperModelPrefix"));
  public static final MetaField fHighlightDbORTable
    = new MetaField(LocaleMgr.db.getString("highlightDbORTable"));
  public static final MetaField fOr_factTable
    = new MetaField(LocaleMgr.db.getString("or_factTable"));
  public static final MetaField fOr_tableLinkedToSubModelPrefix
    = new MetaField(LocaleMgr.db.getString("or_tableLinkedToSubModelPrefix"));
  public static final MetaField fOr_indexUniqueFont
    = new MetaField(LocaleMgr.db.getString("or_indexUniqueFont"));
  public static final MetaField fOr_columnNullValuePrefix
    = new MetaField(LocaleMgr.db.getString("or_columnNullValuePrefix"));
  public static final MetaField fOr_columnTypeDisplay
    = new MetaField(LocaleMgr.db.getString("or_columnTypeDisplay"));
  public static final MetaField fOr_fkColumnsDisplay
    = new MetaField(LocaleMgr.db.getString("or_fkColumnsDisplay"));
  public static final MetaField fOr_associationParentRoleConnectivitiesDisplay
    = new MetaField(LocaleMgr.db.getString("or_associationParentRoleConnectivitiesDisplay"));
  public static final MetaField fOr_associationConnectivitiesFont
    = new MetaField(LocaleMgr.db.getString("or_associationConnectivitiesFont"));
  public static final MetaField fOr_fkColumnsPrefix
    = new MetaField(LocaleMgr.db.getString("or_fkColumnsPrefix"));
  public static final MetaField fOr_columnAdditionnalDescriptor
    = new MetaField(LocaleMgr.db.getString("or_columnAdditionnalDescriptor"));
  public static final MetaField fOr_columnAdditionnalDescriptorFont
    = new MetaField(LocaleMgr.db.getString("or_columnAdditionnalDescriptorFont"));
  public static final MetaField fOr_tableAdditionnalDescriptor
    = new MetaField(LocaleMgr.db.getString("or_tableAdditionnalDescriptor"));
  public static final MetaField fOr_associationRoleDescriptorFont
    = new MetaField(LocaleMgr.db.getString("or_associationRoleDescriptorFont"));
  public static final MetaField fOr_associationDescriptorFont
    = new MetaField(LocaleMgr.db.getString("or_associationDescriptorFont"));
  public static final MetaField fOr_checkConstraintFont
    = new MetaField(LocaleMgr.db.getString("or_checkConstraintFont"));
  public static final MetaField fOr_nameDescriptor
    = new MetaField(LocaleMgr.db.getString("or_nameDescriptor"));
  public static final MetaField fOr_ukColumnsDisplay
    = new MetaField(LocaleMgr.db.getString("or_ukColumnsDisplay"));
  public static final MetaField fOr_dataModelDashStyle
    = new MetaField(LocaleMgr.db.getString("or_dataModelDashStyle"));
  public static final MetaField fOr_columnLinkedToSubModelPrefix
    = new MetaField(LocaleMgr.db.getString("or_columnLinkedToSubModelPrefix"));
  public static final MetaField fOr_associationChildRoleConnectivitiesDisplay
    = new MetaField(LocaleMgr.db.getString("or_associationChildRoleConnectivitiesDisplay"));
  public static final MetaField fOr_columnLengthDecimalsFont
    = new MetaField(LocaleMgr.db.getString("or_columnLengthDecimalsFont"));
  public static final MetaField fOr_dimensionTable
    = new MetaField(LocaleMgr.db.getString("or_dimensionTable"));
  public static final MetaField fOr_columnDescriptorFont
    = new MetaField(LocaleMgr.db.getString("or_columnDescriptorFont"));
  public static final MetaField fOr_columnDefaultValueFont
    = new MetaField(LocaleMgr.db.getString("or_columnDefaultValueFont"));
  public static final MetaField fOr_fkDescriptorFont
    = new MetaField(LocaleMgr.db.getString("or_fkDescriptorFont"));
  public static final MetaField fOr_indexUniqueDisplay
    = new MetaField(LocaleMgr.db.getString("or_indexUniqueDisplay"));
  public static final MetaField fLineColorDbORTable
    = new MetaField(LocaleMgr.db.getString("lineColorDbORTable"));
  public static final MetaField fOr_pkColumnsDisplay
    = new MetaField(LocaleMgr.db.getString("or_pkColumnsDisplay"));
  public static final MetaField fOr_ukColumnsFont
    = new MetaField(LocaleMgr.db.getString("or_ukColumnsFont"));
  public static final MetaField fOr_ukColumnsPrefix
    = new MetaField(LocaleMgr.db.getString("or_ukColumnsPrefix"));
  public static final MetaField fOr_triggerDescriptorFont
    = new MetaField(LocaleMgr.db.getString("or_triggerDescriptorFont"));
  public static final MetaField fBackgroundColorDbORTable
    = new MetaField(LocaleMgr.db.getString("backgroundColorDbORTable"));
  public static final MetaField fOr_columnDomainSourceTypeDisplay
    = new MetaField(LocaleMgr.db.getString("or_columnDomainSourceTypeDisplay"));
  public static final MetaField fTextColorDbORTable
    = new MetaField(LocaleMgr.db.getString("textColorDbORTable"));
  public static final MetaField fOr_columnDomainSourceTypeFont
    = new MetaField(LocaleMgr.db.getString("or_columnDomainSourceTypeFont"));
  public static final MetaField fOr_tableLinkedToSuperModelPrefix
    = new MetaField(LocaleMgr.db.getString("or_tableLinkedToSuperModelPrefix"));
  public static final MetaField fOr_tableDescriptorFont
    = new MetaField(LocaleMgr.db.getString("or_tableDescriptorFont"));
  public static final MetaField fOr_dataModelHighlight
    = new MetaField(LocaleMgr.db.getString("or_dataModelHighlight"));
  public static final MetaField fOr_pkDescriptorFont
    = new MetaField(LocaleMgr.db.getString("or_pkDescriptorFont"));
  public static final MetaField fOr_pkColumnsPrefix
    = new MetaField(LocaleMgr.db.getString("or_pkColumnsPrefix"));
  public static final MetaField fOr_indexColumnsPrefix
    = new MetaField(LocaleMgr.db.getString("or_indexColumnsPrefix"));
  public static final MetaField fOr_pkDisplay
    = new MetaField(LocaleMgr.db.getString("or_pkDisplay"));
  public static final MetaField fOr_ukDisplay
    = new MetaField(LocaleMgr.db.getString("or_ukDisplay"));
  public static final MetaField fOr_fkDisplay
    = new MetaField(LocaleMgr.db.getString("or_fkDisplay"));
  public static final MetaField fOr_checkDisplay
    = new MetaField(LocaleMgr.db.getString("or_checkDisplay"));
  public static final MetaField fOr_triggerColumnsDisplay
    = new MetaField(LocaleMgr.db.getString("or_triggerColumnsDisplay"));
  public static final MetaField fOr_tableOwnerDisplay
    = new MetaField(LocaleMgr.db.getString("or_tableOwnerDisplay"));
  public static final MetaRelation1 fReferringProjectOr
    = new MetaRelation1(LocaleMgr.db.getString("referringProjectOr"), 0);
  public static final MetaField fDashStyleDbORTable
    = new MetaField(LocaleMgr.db.getString("dashStyleDbORTable"));
  public static final MetaField fBackgroundColorDbORView
    = new MetaField(LocaleMgr.db.getString("backgroundColorDbORView"));
  public static final MetaField fOr_viewDescriptorFont
    = new MetaField(LocaleMgr.db.getString("or_viewDescriptorFont"));
  public static final MetaField fOr_checkParametersDisplay
    = new MetaField(LocaleMgr.db.getString("or_checkParametersDisplay"));
  public static final MetaField fDashStyleDbORView
    = new MetaField(LocaleMgr.db.getString("dashStyleDbORView"));
  public static final MetaField fTextColorDbORView
    = new MetaField(LocaleMgr.db.getString("textColorDbORView"));
  public static final MetaField fHighlightDbORView
    = new MetaField(LocaleMgr.db.getString("highlightDbORView"));
  public static final MetaField fLineColorDbORView
    = new MetaField(LocaleMgr.db.getString("lineColorDbORView"));
  public static final MetaField fOr_columnLengthDecimalsDisplay
    = new MetaField(LocaleMgr.db.getString("or_columnLengthDecimalsDisplay"));
  public static final MetaField fOr_columnDefaultValueDisplay
    = new MetaField(LocaleMgr.db.getString("or_columnDefaultValueDisplay"));
  public static final MetaField fOr_pkColumnsFont
    = new MetaField(LocaleMgr.db.getString("or_pkColumnsFont"));
  public static final MetaField fOr_checkParametersFont
    = new MetaField(LocaleMgr.db.getString("or_checkParametersFont"));
  public static final MetaField fOr_ukDescriptorFont
    = new MetaField(LocaleMgr.db.getString("or_ukDescriptorFont"));
  public static final MetaField fOr_fkColumnsFont
    = new MetaField(LocaleMgr.db.getString("or_fkColumnsFont"));
  public static final MetaField fOr_indexColumnsDisplay
    = new MetaField(LocaleMgr.db.getString("or_indexColumnsDisplay"));
  public static final MetaField fOr_indexTypeFont
    = new MetaField(LocaleMgr.db.getString("or_indexTypeFont"));
  public static final MetaField fOr_indexTypeDisplay
    = new MetaField(LocaleMgr.db.getString("or_indexTypeDisplay"));
  public static final MetaField fOr_triggerDisplay
    = new MetaField(LocaleMgr.db.getString("or_triggerDisplay"));
  public static final MetaField fOr_triggerColumnsFont
    = new MetaField(LocaleMgr.db.getString("or_triggerColumnsFont"));
  public static final MetaField fOr_viewPrefix
    = new MetaField(LocaleMgr.db.getString("or_viewPrefix"));
  public static final MetaField fOr_tableAdditionnalDescriptorFont
    = new MetaField(LocaleMgr.db.getString("or_tableAdditionnalDescriptorFont"));
  public static final MetaField fOr_viewAdditionnalDescriptor
    = new MetaField(LocaleMgr.db.getString("or_viewAdditionnalDescriptor"));
  public static final MetaField fOr_viewAdditionnalDescriptorFont
    = new MetaField(LocaleMgr.db.getString("or_viewAdditionnalDescriptorFont"));
  public static final MetaField fOr_pkDescriptorFormat
    = new MetaField(LocaleMgr.db.getString("or_pkDescriptorFormat"));
  public static final MetaField fOr_fkDescriptorFormat
    = new MetaField(LocaleMgr.db.getString("or_fkDescriptorFormat"));
  public static final MetaField fOr_ukDescriptorFormat
    = new MetaField(LocaleMgr.db.getString("or_ukDescriptorFormat"));
  public static final MetaField fOr_checkConstraintPrefixFont
    = new MetaField(LocaleMgr.db.getString("or_checkConstraintPrefixFont"));
  public static final MetaField fOr_columnPrefixFont
    = new MetaField(LocaleMgr.db.getString("or_columnPrefixFont"));
  public static final MetaField fOr_fkPrefixFont
    = new MetaField(LocaleMgr.db.getString("or_fkPrefixFont"));
  public static final MetaField fOr_indexPrefixFont
    = new MetaField(LocaleMgr.db.getString("or_indexPrefixFont"));
  public static final MetaField fOr_triggerPrefixFont
    = new MetaField(LocaleMgr.db.getString("or_triggerPrefixFont"));
  public static final MetaField fOr_ukPrefixFont
    = new MetaField(LocaleMgr.db.getString("or_ukPrefixFont"));
  public static final MetaField fOr_pkPrefixFont
    = new MetaField(LocaleMgr.db.getString("or_pkPrefixFont"));
  public static final MetaField fOr_tablePrefixFont
    = new MetaField(LocaleMgr.db.getString("or_tablePrefixFont"));
  public static final MetaField fOr_columnTypeFont
    = new MetaField(LocaleMgr.db.getString("or_columnTypeFont"));
  public static final MetaField fOr_columnNullValueDisplay
    = new MetaField(LocaleMgr.db.getString("or_columnNullValueDisplay"));
  public static final MetaField fOr_columnNullValueFont
    = new MetaField(LocaleMgr.db.getString("or_columnNullValueFont"));
  public static final MetaField fOr_columnDisplay
    = new MetaField(LocaleMgr.db.getString("or_columnDisplay"));
  public static final MetaField fOr_indexDescriptorFont
    = new MetaField(LocaleMgr.db.getString("or_indexDescriptorFont"));
  public static final MetaField fOr_indexDisplay
    = new MetaField(LocaleMgr.db.getString("or_indexDisplay"));
  public static final MetaField fOr_indexColumnsFont
    = new MetaField(LocaleMgr.db.getString("or_indexColumnsFont"));
  public static final MetaField fOr_associationChildRoleDescriptorDisplay
    = new MetaField(LocaleMgr.db.getString("or_associationChildRoleDescriptorDisplay"));
  public static final MetaField fOr_associationParentRoleDescriptorDisplay
    = new MetaField(LocaleMgr.db.getString("or_associationParentRoleDescriptorDisplay"));
  public static final MetaField fOr_associationDescriptorDisplay
    = new MetaField(LocaleMgr.db.getString("or_associationDescriptorDisplay"));
  public static final MetaField fOr_umlStereotypeDisplayed
    = new MetaField(LocaleMgr.db.getString("or_umlStereotypeDisplayed"));
  public static final MetaField fOr_umlConstraintDisplayed
    = new MetaField(LocaleMgr.db.getString("or_umlConstraintDisplayed"));
  public static final MetaField fOr_umlStereotypeIconDisplayed
    = new MetaField(LocaleMgr.db.getString("or_umlStereotypeIconDisplayed"));
  public static final MetaField fOr_associationTablesAsRelationships
    = new MetaField(LocaleMgr.db.getString("or_associationTablesAsRelationships"));
  public static final MetaField fOr_associationAsRelationships
    = new MetaField(LocaleMgr.db.getString("or_associationAsRelationships"));
  public static final MetaField fOr_showDependentTables
    = new MetaField(LocaleMgr.db.getString("or_showDependentTables"));
  public static final MetaField fOr_UnidentifyingAssociationsAreDashed
    = new MetaField(LocaleMgr.db.getString("or_UnidentifyingAssociationsAreDashed"));
  public static final MetaField fOr_umlAssociationDirection
    = new MetaField(LocaleMgr.db.getString("or_umlAssociationDirection"));
  public static final MetaRelation1 fReferringProjectEr
    = new MetaRelation1(LocaleMgr.db.getString("referringProjectEr"), 0);
  public static final MetaField fOr_columnChoicePrefix
    = new MetaField(LocaleMgr.db.getString("or_columnChoicePrefix"));
  public static final MetaField fOr_columnSpecializationPrefix
    = new MetaField(LocaleMgr.db.getString("or_columnSpecializationPrefix"));
  public static final MetaField fBackgroundColorDbORChoiceOrSpecialization
    = new MetaField(LocaleMgr.db.getString("backgroundColorDbORChoiceOrSpecialization"));
  public static final MetaField fLineColorDbORChoiceOrSpecialization
    = new MetaField(LocaleMgr.db.getString("lineColorDbORChoiceOrSpecialization"));

  public static final MetaClass metaClass = new MetaClass(
    LocaleMgr.db.getString("DbORStyle"), DbORStyle.class,
    new MetaField[] {fOr_columnLinkedToSuperModelPrefix,
      fHighlightDbORTable,
      fOr_factTable,
      fOr_tableLinkedToSubModelPrefix,
      fOr_indexUniqueFont,
      fOr_columnNullValuePrefix,
      fOr_columnTypeDisplay,
      fOr_fkColumnsDisplay,
      fOr_associationParentRoleConnectivitiesDisplay,
      fOr_associationConnectivitiesFont,
      fOr_fkColumnsPrefix,
      fOr_columnAdditionnalDescriptor,
      fOr_columnAdditionnalDescriptorFont,
      fOr_tableAdditionnalDescriptor,
      fOr_associationRoleDescriptorFont,
      fOr_associationDescriptorFont,
      fOr_checkConstraintFont,
      fOr_nameDescriptor,
      fOr_ukColumnsDisplay,
      fOr_dataModelDashStyle,
      fOr_columnLinkedToSubModelPrefix,
      fOr_associationChildRoleConnectivitiesDisplay,
      fOr_columnLengthDecimalsFont,
      fOr_dimensionTable,
      fOr_columnDescriptorFont,
      fOr_columnDefaultValueFont,
      fOr_fkDescriptorFont,
      fOr_indexUniqueDisplay,
      fLineColorDbORTable,
      fOr_pkColumnsDisplay,
      fOr_ukColumnsFont,
      fOr_ukColumnsPrefix,
      fOr_triggerDescriptorFont,
      fBackgroundColorDbORTable,
      fOr_columnDomainSourceTypeDisplay,
      fTextColorDbORTable,
      fOr_columnDomainSourceTypeFont,
      fOr_tableLinkedToSuperModelPrefix,
      fOr_tableDescriptorFont,
      fOr_dataModelHighlight,
      fOr_pkDescriptorFont,
      fOr_pkColumnsPrefix,
      fOr_indexColumnsPrefix,
      fOr_pkDisplay,
      fOr_ukDisplay,
      fOr_fkDisplay,
      fOr_checkDisplay,
      fOr_triggerColumnsDisplay,
      fOr_tableOwnerDisplay,
      fReferringProjectOr,
      fDashStyleDbORTable,
      fBackgroundColorDbORView,
      fOr_viewDescriptorFont,
      fOr_checkParametersDisplay,
      fDashStyleDbORView,
      fTextColorDbORView,
      fHighlightDbORView,
      fLineColorDbORView,
      fOr_columnLengthDecimalsDisplay,
      fOr_columnDefaultValueDisplay,
      fOr_pkColumnsFont,
      fOr_checkParametersFont,
      fOr_ukDescriptorFont,
      fOr_fkColumnsFont,
      fOr_indexColumnsDisplay,
      fOr_indexTypeFont,
      fOr_indexTypeDisplay,
      fOr_triggerDisplay,
      fOr_triggerColumnsFont,
      fOr_viewPrefix,
      fOr_tableAdditionnalDescriptorFont,
      fOr_viewAdditionnalDescriptor,
      fOr_viewAdditionnalDescriptorFont,
      fOr_pkDescriptorFormat,
      fOr_fkDescriptorFormat,
      fOr_ukDescriptorFormat,
      fOr_checkConstraintPrefixFont,
      fOr_columnPrefixFont,
      fOr_fkPrefixFont,
      fOr_indexPrefixFont,
      fOr_triggerPrefixFont,
      fOr_ukPrefixFont,
      fOr_pkPrefixFont,
      fOr_tablePrefixFont,
      fOr_columnTypeFont,
      fOr_columnNullValueDisplay,
      fOr_columnNullValueFont,
      fOr_columnDisplay,
      fOr_indexDescriptorFont,
      fOr_indexDisplay,
      fOr_indexColumnsFont,
      fOr_associationChildRoleDescriptorDisplay,
      fOr_associationParentRoleDescriptorDisplay,
      fOr_associationDescriptorDisplay,
      fOr_umlStereotypeDisplayed,
      fOr_umlConstraintDisplayed,
      fOr_umlStereotypeIconDisplayed,
      fOr_associationTablesAsRelationships,
      fOr_associationAsRelationships,
      fOr_showDependentTables,
      fOr_UnidentifyingAssociationsAreDashed,
      fOr_umlAssociationDirection,
      fReferringProjectEr,
      fOr_columnChoicePrefix,
      fOr_columnSpecializationPrefix,
      fBackgroundColorDbORChoiceOrSpecialization,
      fLineColorDbORChoiceOrSpecialization}, MetaClass.MATCHABLE | MetaClass.NO_UDF);

/**
    For internal use only.
 **/
  public static void initMeta() {
    try {
      metaClass.setSuperMetaClass(DbSMSStyle.metaClass);

      fOr_columnLinkedToSuperModelPrefix.setJField(DbORStyle.class.getDeclaredField("m_or_columnLinkedToSuperModelPrefix"));
      fHighlightDbORTable.setJField(DbORStyle.class.getDeclaredField("m_highlightDbORTable"));
      fOr_factTable.setJField(DbORStyle.class.getDeclaredField("m_or_factTable"));
      fOr_tableLinkedToSubModelPrefix.setJField(DbORStyle.class.getDeclaredField("m_or_tableLinkedToSubModelPrefix"));
      fOr_indexUniqueFont.setJField(DbORStyle.class.getDeclaredField("m_or_indexUniqueFont"));
      fOr_columnNullValuePrefix.setJField(DbORStyle.class.getDeclaredField("m_or_columnNullValuePrefix"));
      fOr_columnTypeDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_columnTypeDisplay"));
      fOr_fkColumnsDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_fkColumnsDisplay"));
      fOr_associationParentRoleConnectivitiesDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_associationParentRoleConnectivitiesDisplay"));
      fOr_associationConnectivitiesFont.setJField(DbORStyle.class.getDeclaredField("m_or_associationConnectivitiesFont"));
      fOr_fkColumnsPrefix.setJField(DbORStyle.class.getDeclaredField("m_or_fkColumnsPrefix"));
      fOr_columnAdditionnalDescriptor.setJField(DbORStyle.class.getDeclaredField("m_or_columnAdditionnalDescriptor"));
      fOr_columnAdditionnalDescriptorFont.setJField(DbORStyle.class.getDeclaredField("m_or_columnAdditionnalDescriptorFont"));
      fOr_tableAdditionnalDescriptor.setJField(DbORStyle.class.getDeclaredField("m_or_tableAdditionnalDescriptor"));
      fOr_associationRoleDescriptorFont.setJField(DbORStyle.class.getDeclaredField("m_or_associationRoleDescriptorFont"));
      fOr_associationDescriptorFont.setJField(DbORStyle.class.getDeclaredField("m_or_associationDescriptorFont"));
      fOr_checkConstraintFont.setJField(DbORStyle.class.getDeclaredField("m_or_checkConstraintFont"));
      fOr_nameDescriptor.setJField(DbORStyle.class.getDeclaredField("m_or_nameDescriptor"));
      fOr_ukColumnsDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_ukColumnsDisplay"));
      fOr_dataModelDashStyle.setJField(DbORStyle.class.getDeclaredField("m_or_dataModelDashStyle"));
      fOr_columnLinkedToSubModelPrefix.setJField(DbORStyle.class.getDeclaredField("m_or_columnLinkedToSubModelPrefix"));
      fOr_associationChildRoleConnectivitiesDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_associationChildRoleConnectivitiesDisplay"));
      fOr_columnLengthDecimalsFont.setJField(DbORStyle.class.getDeclaredField("m_or_columnLengthDecimalsFont"));
      fOr_dimensionTable.setJField(DbORStyle.class.getDeclaredField("m_or_dimensionTable"));
      fOr_columnDescriptorFont.setJField(DbORStyle.class.getDeclaredField("m_or_columnDescriptorFont"));
      fOr_columnDefaultValueFont.setJField(DbORStyle.class.getDeclaredField("m_or_columnDefaultValueFont"));
      fOr_fkDescriptorFont.setJField(DbORStyle.class.getDeclaredField("m_or_fkDescriptorFont"));
      fOr_indexUniqueDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_indexUniqueDisplay"));
      fLineColorDbORTable.setJField(DbORStyle.class.getDeclaredField("m_lineColorDbORTable"));
      fOr_pkColumnsDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_pkColumnsDisplay"));
      fOr_ukColumnsFont.setJField(DbORStyle.class.getDeclaredField("m_or_ukColumnsFont"));
      fOr_ukColumnsPrefix.setJField(DbORStyle.class.getDeclaredField("m_or_ukColumnsPrefix"));
      fOr_triggerDescriptorFont.setJField(DbORStyle.class.getDeclaredField("m_or_triggerDescriptorFont"));
      fBackgroundColorDbORTable.setJField(DbORStyle.class.getDeclaredField("m_backgroundColorDbORTable"));
      fOr_columnDomainSourceTypeDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_columnDomainSourceTypeDisplay"));
      fTextColorDbORTable.setJField(DbORStyle.class.getDeclaredField("m_textColorDbORTable"));
      fOr_columnDomainSourceTypeFont.setJField(DbORStyle.class.getDeclaredField("m_or_columnDomainSourceTypeFont"));
      fOr_tableLinkedToSuperModelPrefix.setJField(DbORStyle.class.getDeclaredField("m_or_tableLinkedToSuperModelPrefix"));
      fOr_tableDescriptorFont.setJField(DbORStyle.class.getDeclaredField("m_or_tableDescriptorFont"));
      fOr_dataModelHighlight.setJField(DbORStyle.class.getDeclaredField("m_or_dataModelHighlight"));
      fOr_pkDescriptorFont.setJField(DbORStyle.class.getDeclaredField("m_or_pkDescriptorFont"));
      fOr_pkColumnsPrefix.setJField(DbORStyle.class.getDeclaredField("m_or_pkColumnsPrefix"));
      fOr_indexColumnsPrefix.setJField(DbORStyle.class.getDeclaredField("m_or_indexColumnsPrefix"));
      fOr_pkDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_pkDisplay"));
      fOr_ukDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_ukDisplay"));
      fOr_fkDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_fkDisplay"));
      fOr_checkDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_checkDisplay"));
      fOr_triggerColumnsDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_triggerColumnsDisplay"));
      fOr_tableOwnerDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_tableOwnerDisplay"));
      fReferringProjectOr.setJField(DbORStyle.class.getDeclaredField("m_referringProjectOr"));
      fDashStyleDbORTable.setJField(DbORStyle.class.getDeclaredField("m_dashStyleDbORTable"));
      fBackgroundColorDbORView.setJField(DbORStyle.class.getDeclaredField("m_backgroundColorDbORView"));
      fOr_viewDescriptorFont.setJField(DbORStyle.class.getDeclaredField("m_or_viewDescriptorFont"));
      fOr_checkParametersDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_checkParametersDisplay"));
      fDashStyleDbORView.setJField(DbORStyle.class.getDeclaredField("m_dashStyleDbORView"));
      fTextColorDbORView.setJField(DbORStyle.class.getDeclaredField("m_textColorDbORView"));
      fHighlightDbORView.setJField(DbORStyle.class.getDeclaredField("m_highlightDbORView"));
      fLineColorDbORView.setJField(DbORStyle.class.getDeclaredField("m_lineColorDbORView"));
      fOr_columnLengthDecimalsDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_columnLengthDecimalsDisplay"));
      fOr_columnDefaultValueDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_columnDefaultValueDisplay"));
      fOr_pkColumnsFont.setJField(DbORStyle.class.getDeclaredField("m_or_pkColumnsFont"));
      fOr_checkParametersFont.setJField(DbORStyle.class.getDeclaredField("m_or_checkParametersFont"));
      fOr_ukDescriptorFont.setJField(DbORStyle.class.getDeclaredField("m_or_ukDescriptorFont"));
      fOr_fkColumnsFont.setJField(DbORStyle.class.getDeclaredField("m_or_fkColumnsFont"));
      fOr_indexColumnsDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_indexColumnsDisplay"));
      fOr_indexTypeFont.setJField(DbORStyle.class.getDeclaredField("m_or_indexTypeFont"));
      fOr_indexTypeDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_indexTypeDisplay"));
      fOr_triggerDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_triggerDisplay"));
      fOr_triggerColumnsFont.setJField(DbORStyle.class.getDeclaredField("m_or_triggerColumnsFont"));
      fOr_viewPrefix.setJField(DbORStyle.class.getDeclaredField("m_or_viewPrefix"));
      fOr_tableAdditionnalDescriptorFont.setJField(DbORStyle.class.getDeclaredField("m_or_tableAdditionnalDescriptorFont"));
      fOr_viewAdditionnalDescriptor.setJField(DbORStyle.class.getDeclaredField("m_or_viewAdditionnalDescriptor"));
      fOr_viewAdditionnalDescriptorFont.setJField(DbORStyle.class.getDeclaredField("m_or_viewAdditionnalDescriptorFont"));
      fOr_pkDescriptorFormat.setJField(DbORStyle.class.getDeclaredField("m_or_pkDescriptorFormat"));
      fOr_fkDescriptorFormat.setJField(DbORStyle.class.getDeclaredField("m_or_fkDescriptorFormat"));
      fOr_ukDescriptorFormat.setJField(DbORStyle.class.getDeclaredField("m_or_ukDescriptorFormat"));
      fOr_checkConstraintPrefixFont.setJField(DbORStyle.class.getDeclaredField("m_or_checkConstraintPrefixFont"));
      fOr_columnPrefixFont.setJField(DbORStyle.class.getDeclaredField("m_or_columnPrefixFont"));
      fOr_fkPrefixFont.setJField(DbORStyle.class.getDeclaredField("m_or_fkPrefixFont"));
      fOr_indexPrefixFont.setJField(DbORStyle.class.getDeclaredField("m_or_indexPrefixFont"));
      fOr_triggerPrefixFont.setJField(DbORStyle.class.getDeclaredField("m_or_triggerPrefixFont"));
      fOr_ukPrefixFont.setJField(DbORStyle.class.getDeclaredField("m_or_ukPrefixFont"));
      fOr_pkPrefixFont.setJField(DbORStyle.class.getDeclaredField("m_or_pkPrefixFont"));
      fOr_tablePrefixFont.setJField(DbORStyle.class.getDeclaredField("m_or_tablePrefixFont"));
      fOr_columnTypeFont.setJField(DbORStyle.class.getDeclaredField("m_or_columnTypeFont"));
      fOr_columnNullValueDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_columnNullValueDisplay"));
      fOr_columnNullValueFont.setJField(DbORStyle.class.getDeclaredField("m_or_columnNullValueFont"));
      fOr_columnDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_columnDisplay"));
      fOr_indexDescriptorFont.setJField(DbORStyle.class.getDeclaredField("m_or_indexDescriptorFont"));
      fOr_indexDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_indexDisplay"));
      fOr_indexColumnsFont.setJField(DbORStyle.class.getDeclaredField("m_or_indexColumnsFont"));
      fOr_associationChildRoleDescriptorDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_associationChildRoleDescriptorDisplay"));
      fOr_associationParentRoleDescriptorDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_associationParentRoleDescriptorDisplay"));
      fOr_associationDescriptorDisplay.setJField(DbORStyle.class.getDeclaredField("m_or_associationDescriptorDisplay"));
      fOr_umlStereotypeDisplayed.setJField(DbORStyle.class.getDeclaredField("m_or_umlStereotypeDisplayed"));
      fOr_umlConstraintDisplayed.setJField(DbORStyle.class.getDeclaredField("m_or_umlConstraintDisplayed"));
      fOr_umlStereotypeIconDisplayed.setJField(DbORStyle.class.getDeclaredField("m_or_umlStereotypeIconDisplayed"));
      fOr_associationTablesAsRelationships.setJField(DbORStyle.class.getDeclaredField("m_or_associationTablesAsRelationships"));
      fOr_associationAsRelationships.setJField(DbORStyle.class.getDeclaredField("m_or_associationAsRelationships"));
      fOr_showDependentTables.setJField(DbORStyle.class.getDeclaredField("m_or_showDependentTables"));
      fOr_UnidentifyingAssociationsAreDashed.setJField(DbORStyle.class.getDeclaredField("m_or_UnidentifyingAssociationsAreDashed"));
      fOr_umlAssociationDirection.setJField(DbORStyle.class.getDeclaredField("m_or_umlAssociationDirection"));
      fReferringProjectEr.setJField(DbORStyle.class.getDeclaredField("m_referringProjectEr"));
      fOr_columnChoicePrefix.setJField(DbORStyle.class.getDeclaredField("m_or_columnChoicePrefix"));
      fOr_columnSpecializationPrefix.setJField(DbORStyle.class.getDeclaredField("m_or_columnSpecializationPrefix"));
      fBackgroundColorDbORChoiceOrSpecialization.setJField(DbORStyle.class.getDeclaredField("m_backgroundColorDbORChoiceOrSpecialization"));
      fLineColorDbORChoiceOrSpecialization.setJField(DbORStyle.class.getDeclaredField("m_lineColorDbORChoiceOrSpecialization"));

      fReferringProjectOr.setOppositeRel(DbSMSProject.fOrDefaultStyle);
      fReferringProjectEr.setOppositeRel(DbSMSProject.fErDefaultStyle);

    }
    catch (Exception e) { throw new RuntimeException("Meta init"); }
  }

  static final long serialVersionUID = 0;
  private static HashMap fieldMap;
  public static MetaField[] or_displayOptions;
  public static Boolean[] or_displayOptionDefaultValues;
  public static MetaField[] or_descriptorOptions;
  public static Font[] or_fontOptionDefaultValues;
  public static MetaField[] or_descriptorFormatOptions;
  public static Color[] or_colorOptionDefaultValues;
  public static MetaField[][] or_optionGroups;
  public static Object[][] or_optionValueGroups;
  public static String[] or_optionGroupHeaders;
  public static String[] or_listOptionTabs;
  public static MetaField[] or_lineOptions;
  public static Boolean[] or_lineOptionDefaultValues;
  public static MetaField[] or_prefixOptions;
  public static DbtPrefix[] or_prefixOptionDefaultValues;
  public static MetaField[] or_fontOptions;
  public static SMSDisplayDescriptor[] or_descriptorOptionDefaultValues;
  public static final String SEQUENCE_KEYWORD = "%#";
  public static MetaField[] or_colorOptions;
  public static ORDescriptorFormat[] or_descriptorFormatOptionDefaultValues;
  public static String DEFAULT_STYLE_NAME = LocaleMgr.misc.getString( "DefaultORStyle");
  public static String ENTITY_RELATIONSHIP_STYLE_NAME = LocaleMgr.misc.getString( "DefaultERStyle");

  static {
    org.modelsphere.sms.db.util.DbInitialization.initOrStyle();
}


  //Instance variables
  DbtPrefix m_or_columnLinkedToSuperModelPrefix;
  SrBoolean m_highlightDbORTable;
  DbtPrefix m_or_factTable;
  DbtPrefix m_or_tableLinkedToSubModelPrefix;
  SrFont m_or_indexUniqueFont;
  DbtPrefix m_or_columnNullValuePrefix;
  SrBoolean m_or_columnTypeDisplay;
  SrBoolean m_or_fkColumnsDisplay;
  SrBoolean m_or_associationParentRoleConnectivitiesDisplay;
  SrFont m_or_associationConnectivitiesFont;
  DbtPrefix m_or_fkColumnsPrefix;
  SMSDisplayDescriptor m_or_columnAdditionnalDescriptor;
  SrFont m_or_columnAdditionnalDescriptorFont;
  SMSDisplayDescriptor m_or_tableAdditionnalDescriptor;
  SrFont m_or_associationRoleDescriptorFont;
  SrFont m_or_associationDescriptorFont;
  SrFont m_or_checkConstraintFont;
  SMSDisplayDescriptor m_or_nameDescriptor;
  SrBoolean m_or_ukColumnsDisplay;
  SrBoolean m_or_dataModelDashStyle;
  DbtPrefix m_or_columnLinkedToSubModelPrefix;
  SrBoolean m_or_associationChildRoleConnectivitiesDisplay;
  SrFont m_or_columnLengthDecimalsFont;
  DbtPrefix m_or_dimensionTable;
  SrFont m_or_columnDescriptorFont;
  SrFont m_or_columnDefaultValueFont;
  SrFont m_or_fkDescriptorFont;
  SrBoolean m_or_indexUniqueDisplay;
  SrColor m_lineColorDbORTable;
  SrBoolean m_or_pkColumnsDisplay;
  SrFont m_or_ukColumnsFont;
  DbtPrefix m_or_ukColumnsPrefix;
  SrFont m_or_triggerDescriptorFont;
  SrColor m_backgroundColorDbORTable;
  SrBoolean m_or_columnDomainSourceTypeDisplay;
  SrColor m_textColorDbORTable;
  SrFont m_or_columnDomainSourceTypeFont;
  DbtPrefix m_or_tableLinkedToSuperModelPrefix;
  SrFont m_or_tableDescriptorFont;
  SrBoolean m_or_dataModelHighlight;
  SrFont m_or_pkDescriptorFont;
  DbtPrefix m_or_pkColumnsPrefix;
  DbtPrefix m_or_indexColumnsPrefix;
  SrBoolean m_or_pkDisplay;
  SrBoolean m_or_ukDisplay;
  SrBoolean m_or_fkDisplay;
  SrBoolean m_or_checkDisplay;
  SrBoolean m_or_triggerColumnsDisplay;
  SrBoolean m_or_tableOwnerDisplay;
  DbSMSProject m_referringProjectOr;
  SrBoolean m_dashStyleDbORTable;
  SrColor m_backgroundColorDbORView;
  SrFont m_or_viewDescriptorFont;
  SrBoolean m_or_checkParametersDisplay;
  SrBoolean m_dashStyleDbORView;
  SrColor m_textColorDbORView;
  SrBoolean m_highlightDbORView;
  SrColor m_lineColorDbORView;
  SrBoolean m_or_columnLengthDecimalsDisplay;
  SrBoolean m_or_columnDefaultValueDisplay;
  SrFont m_or_pkColumnsFont;
  SrFont m_or_checkParametersFont;
  SrFont m_or_ukDescriptorFont;
  SrFont m_or_fkColumnsFont;
  SrBoolean m_or_indexColumnsDisplay;
  SrFont m_or_indexTypeFont;
  SrBoolean m_or_indexTypeDisplay;
  SrBoolean m_or_triggerDisplay;
  SrFont m_or_triggerColumnsFont;
  DbtPrefix m_or_viewPrefix;
  SrFont m_or_tableAdditionnalDescriptorFont;
  SMSDisplayDescriptor m_or_viewAdditionnalDescriptor;
  SrFont m_or_viewAdditionnalDescriptorFont;
  ORDescriptorFormat m_or_pkDescriptorFormat;
  ORDescriptorFormat m_or_fkDescriptorFormat;
  ORDescriptorFormat m_or_ukDescriptorFormat;
  SrFont m_or_checkConstraintPrefixFont;
  SrFont m_or_columnPrefixFont;
  SrFont m_or_fkPrefixFont;
  SrFont m_or_indexPrefixFont;
  SrFont m_or_triggerPrefixFont;
  SrFont m_or_ukPrefixFont;
  SrFont m_or_pkPrefixFont;
  SrFont m_or_tablePrefixFont;
  SrFont m_or_columnTypeFont;
  SrBoolean m_or_columnNullValueDisplay;
  SrFont m_or_columnNullValueFont;
  SrBoolean m_or_columnDisplay;
  SrFont m_or_indexDescriptorFont;
  SrBoolean m_or_indexDisplay;
  SrFont m_or_indexColumnsFont;
  SrBoolean m_or_associationChildRoleDescriptorDisplay;
  SrBoolean m_or_associationParentRoleDescriptorDisplay;
  SrBoolean m_or_associationDescriptorDisplay;
  SrBoolean m_or_umlStereotypeDisplayed;
  SrBoolean m_or_umlConstraintDisplayed;
  SrBoolean m_or_umlStereotypeIconDisplayed;
  SrBoolean m_or_associationTablesAsRelationships;
  SrBoolean m_or_associationAsRelationships;
  SrBoolean m_or_showDependentTables;
  SrBoolean m_or_UnidentifyingAssociationsAreDashed;
  SrBoolean m_or_umlAssociationDirection;
  DbSMSProject m_referringProjectEr;
  DbtPrefix m_or_columnChoicePrefix;
  DbtPrefix m_or_columnSpecializationPrefix;
  SrColor m_backgroundColorDbORChoiceOrSpecialization;
  SrColor m_lineColorDbORChoiceOrSpecialization;

  //Constructors

/**
    Parameter-less constructor.  Required by Java Beans Conventions.
 **/
  public DbORStyle() {}

/**
    Creates an instance of DbORStyle.
    @param composite the object which will contain the newly-created instance
 **/
  public DbORStyle(DbObject composite) throws DbException {
    super(composite);
    setDefaultInitialValues();
  }

  private void setDefaultInitialValues() throws DbException {
  }

/**

 **/
  public void initOptions() throws DbException {
    for (int i = 0;  i < or_optionGroups.length;  i++) {
      for (int j = 0;  j < or_optionGroups[i].length;  j++) {
        basicSet(or_optionGroups[i][j], or_optionValueGroups[i][j]);
      }
    }
  }

/**

 **/
  public void initNullOptions() throws DbException {
    if (getAncestor() != null)
      return;
    for (int i = 0;  i < or_optionGroups.length;  i++) {
      for (int j = 0;  j < or_optionGroups[i].length;  j++) {
        if (get(or_optionGroups[i][j]) == null)
          basicSet(or_optionGroups[i][j], or_optionValueGroups[i][j]);
      }
    }
  }

/**

    @param srcstyle org.modelsphere.jack.baseDb.db.DbObject
 **/
  public void copyOptions(DbObject srcStyle) throws DbException {
    for (int i = 0;  i < or_optionGroups.length;  i++) {
      for (int j = 0;  j < or_optionGroups[i].length;  j++) {
        MetaField metaField = or_optionGroups[i][j];
        basicSet(metaField, ((DbORStyle)srcStyle).find(metaField));
      }
    }
  }

/**

    @param name java.lang.String
    @return metafield
 **/
  public final MetaField getMetaField(String name) {
    if (fieldMap == null) {
      MetaField[] fields = metaClass.getAllMetaFields();
      fieldMap = new HashMap(fields.length + fields.length/2);
      for (int i = 0;  i < fields.length;  i++)
        fieldMap.put(fields[i].getJName(), fields[i]);
    }
    return (MetaField)fieldMap.get(name);
  }

  //Setters

/**
    Sets the column having a supercolumn object associated to a DbORStyle's instance.

    @param value the column having a supercolumn object to be associated

 **/
  public final void setOr_columnLinkedToSuperModelPrefix(DbtPrefix value) throws DbException {
    basicSet(fOr_columnLinkedToSuperModelPrefix, value);
  }

/**
    Sets the "table highlight" property of a DbORStyle's instance.

    @param value the "table highlight" property

 **/
  public final void setHighlightDbORTable(Boolean value) throws DbException {
    basicSet(fHighlightDbORTable, value);
  }

/**
    Sets the fact table object associated to a DbORStyle's instance.

    @param value the fact table object to be associated

 **/
  public final void setOr_factTable(DbtPrefix value) throws DbException {
    basicSet(fOr_factTable, value);
  }

/**
    Sets the table having subtables object associated to a DbORStyle's instance.

    @param value the table having subtables object to be associated

 **/
  public final void setOr_tableLinkedToSubModelPrefix(DbtPrefix value) throws DbException {
    basicSet(fOr_tableLinkedToSubModelPrefix, value);
  }

/**
    Sets the "index unique" property of a DbORStyle's instance.

    @param value the "index unique" property

 **/
  public final void setOr_indexUniqueFont(Font value) throws DbException {
    basicSet(fOr_indexUniqueFont, value);
  }

/**
    Sets the column null value object associated to a DbORStyle's instance.

    @param value the column null value object to be associated

 **/
  public final void setOr_columnNullValuePrefix(DbtPrefix value) throws DbException {
    basicSet(fOr_columnNullValuePrefix, value);
  }

/**
    Sets the "column type" property of a DbORStyle's instance.

    @param value the "column type" property

 **/
  public final void setOr_columnTypeDisplay(Boolean value) throws DbException {
    basicSet(fOr_columnTypeDisplay, value);
  }

/**
    Sets the "foreign key columns" property of a DbORStyle's instance.

    @param value the "foreign key columns" property

 **/
  public final void setOr_fkColumnsDisplay(Boolean value) throws DbException {
    basicSet(fOr_fkColumnsDisplay, value);
  }

/**
    Sets the "association not navigable role connectivities" property of a DbORStyle's instance.

    @param value the "association not navigable role connectivities" property

 **/
  public final void setOr_associationParentRoleConnectivitiesDisplay(Boolean value) throws DbException {
    basicSet(fOr_associationParentRoleConnectivitiesDisplay, value);
  }

/**
    Sets the "association role connectivities" property of a DbORStyle's instance.

    @param value the "association role connectivities" property

 **/
  public final void setOr_associationConnectivitiesFont(Font value) throws DbException {
    basicSet(fOr_associationConnectivitiesFont, value);
  }

/**
    Sets the foreign key columns object associated to a DbORStyle's instance.

    @param value the foreign key columns object to be associated

 **/
  public final void setOr_fkColumnsPrefix(DbtPrefix value) throws DbException {
    basicSet(fOr_fkColumnsPrefix, value);
  }

/**
    Sets the "column additional descriptor" property of a DbORStyle's instance.

    @param value the "column additional descriptor" property

 **/
  public final void setOr_columnAdditionnalDescriptor(SMSDisplayDescriptor value) throws DbException {
    basicSet(fOr_columnAdditionnalDescriptor, value);
  }

/**
    Sets the "column additional descriptor" property of a DbORStyle's instance.

    @param value the "column additional descriptor" property

 **/
  public final void setOr_columnAdditionnalDescriptorFont(Font value) throws DbException {
    basicSet(fOr_columnAdditionnalDescriptorFont, value);
  }

/**
    Sets the "table additional descriptor" property of a DbORStyle's instance.

    @param value the "table additional descriptor" property

 **/
  public final void setOr_tableAdditionnalDescriptor(SMSDisplayDescriptor value) throws DbException {
    basicSet(fOr_tableAdditionnalDescriptor, value);
  }

/**
    Sets the "association role descriptor" property of a DbORStyle's instance.

    @param value the "association role descriptor" property

 **/
  public final void setOr_associationRoleDescriptorFont(Font value) throws DbException {
    basicSet(fOr_associationRoleDescriptorFont, value);
  }

/**
    Sets the "association descriptor" property of a DbORStyle's instance.

    @param value the "association descriptor" property

 **/
  public final void setOr_associationDescriptorFont(Font value) throws DbException {
    basicSet(fOr_associationDescriptorFont, value);
  }

/**
    Sets the "check constraint" property of a DbORStyle's instance.

    @param value the "check constraint" property

 **/
  public final void setOr_checkConstraintFont(Font value) throws DbException {
    basicSet(fOr_checkConstraintFont, value);
  }

/**
    Sets the "descriptor" property of a DbORStyle's instance.

    @param value the "descriptor" property

 **/
  public final void setOr_nameDescriptor(SMSDisplayDescriptor value) throws DbException {
    basicSet(fOr_nameDescriptor, value);
  }

/**
    Sets the "unique key columns" property of a DbORStyle's instance.

    @param value the "unique key columns" property

 **/
  public final void setOr_ukColumnsDisplay(Boolean value) throws DbException {
    basicSet(fOr_ukColumnsDisplay, value);
  }

/**
    Sets the "data model dash style" property of a DbORStyle's instance.

    @param value the "data model dash style" property

 **/
  public final void setOr_dataModelDashStyle(Boolean value) throws DbException {
    basicSet(fOr_dataModelDashStyle, value);
  }

/**
    Sets the column having subcolumns object associated to a DbORStyle's instance.

    @param value the column having subcolumns object to be associated

 **/
  public final void setOr_columnLinkedToSubModelPrefix(DbtPrefix value) throws DbException {
    basicSet(fOr_columnLinkedToSubModelPrefix, value);
  }

/**
    Sets the "association navigable role connectivities" property of a DbORStyle's instance.

    @param value the "association navigable role connectivities" property

 **/
  public final void setOr_associationChildRoleConnectivitiesDisplay(Boolean value) throws DbException {
    basicSet(fOr_associationChildRoleConnectivitiesDisplay, value);
  }

/**
    Sets the "column length and decimals" property of a DbORStyle's instance.

    @param value the "column length and decimals" property

 **/
  public final void setOr_columnLengthDecimalsFont(Font value) throws DbException {
    basicSet(fOr_columnLengthDecimalsFont, value);
  }

/**
    Sets the dimension table object associated to a DbORStyle's instance.

    @param value the dimension table object to be associated

 **/
  public final void setOr_dimensionTable(DbtPrefix value) throws DbException {
    basicSet(fOr_dimensionTable, value);
  }

/**
    Sets the "column descriptor" property of a DbORStyle's instance.

    @param value the "column descriptor" property

 **/
  public final void setOr_columnDescriptorFont(Font value) throws DbException {
    basicSet(fOr_columnDescriptorFont, value);
  }

/**
    Sets the "column initial value" property of a DbORStyle's instance.

    @param value the "column initial value" property

 **/
  public final void setOr_columnDefaultValueFont(Font value) throws DbException {
    basicSet(fOr_columnDefaultValueFont, value);
  }

/**
    Sets the "foreign key descriptor" property of a DbORStyle's instance.

    @param value the "foreign key descriptor" property

 **/
  public final void setOr_fkDescriptorFont(Font value) throws DbException {
    basicSet(fOr_fkDescriptorFont, value);
  }

/**
    Sets the "index unique" property of a DbORStyle's instance.

    @param value the "index unique" property

 **/
  public final void setOr_indexUniqueDisplay(Boolean value) throws DbException {
    basicSet(fOr_indexUniqueDisplay, value);
  }

/**
    Sets the "table border" property of a DbORStyle's instance.

    @param value the "table border" property

 **/
  public final void setLineColorDbORTable(Color value) throws DbException {
    basicSet(fLineColorDbORTable, value);
  }

/**
    Sets the "primary key columns" property of a DbORStyle's instance.

    @param value the "primary key columns" property

 **/
  public final void setOr_pkColumnsDisplay(Boolean value) throws DbException {
    basicSet(fOr_pkColumnsDisplay, value);
  }

/**
    Sets the "unique key columns" property of a DbORStyle's instance.

    @param value the "unique key columns" property

 **/
  public final void setOr_ukColumnsFont(Font value) throws DbException {
    basicSet(fOr_ukColumnsFont, value);
  }

/**
    Sets the unique key columns object associated to a DbORStyle's instance.

    @param value the unique key columns object to be associated

 **/
  public final void setOr_ukColumnsPrefix(DbtPrefix value) throws DbException {
    basicSet(fOr_ukColumnsPrefix, value);
  }

/**
    Sets the "trigger descriptor" property of a DbORStyle's instance.

    @param value the "trigger descriptor" property

 **/
  public final void setOr_triggerDescriptorFont(Font value) throws DbException {
    basicSet(fOr_triggerDescriptorFont, value);
  }

/**
    Sets the "table background" property of a DbORStyle's instance.

    @param value the "table background" property

 **/
  public final void setBackgroundColorDbORTable(Color value) throws DbException {
    basicSet(fBackgroundColorDbORTable, value);
  }

/**
    Sets the "column domain source type" property of a DbORStyle's instance.

    @param value the "column domain source type" property

 **/
  public final void setOr_columnDomainSourceTypeDisplay(Boolean value) throws DbException {
    basicSet(fOr_columnDomainSourceTypeDisplay, value);
  }

/**
    Sets the "table text" property of a DbORStyle's instance.

    @param value the "table text" property

 **/
  public final void setTextColorDbORTable(Color value) throws DbException {
    basicSet(fTextColorDbORTable, value);
  }

/**
    Sets the "column domain source type" property of a DbORStyle's instance.

    @param value the "column domain source type" property

 **/
  public final void setOr_columnDomainSourceTypeFont(Font value) throws DbException {
    basicSet(fOr_columnDomainSourceTypeFont, value);
  }

/**
    Sets the table having a supertable object associated to a DbORStyle's instance.

    @param value the table having a supertable object to be associated

 **/
  public final void setOr_tableLinkedToSuperModelPrefix(DbtPrefix value) throws DbException {
    basicSet(fOr_tableLinkedToSuperModelPrefix, value);
  }

/**
    Sets the "table descriptor" property of a DbORStyle's instance.

    @param value the "table descriptor" property

 **/
  public final void setOr_tableDescriptorFont(Font value) throws DbException {
    basicSet(fOr_tableDescriptorFont, value);
  }

/**
    Sets the "data model highlight" property of a DbORStyle's instance.

    @param value the "data model highlight" property

 **/
  public final void setOr_dataModelHighlight(Boolean value) throws DbException {
    basicSet(fOr_dataModelHighlight, value);
  }

/**
    Sets the "primary key descriptor" property of a DbORStyle's instance.

    @param value the "primary key descriptor" property

 **/
  public final void setOr_pkDescriptorFont(Font value) throws DbException {
    basicSet(fOr_pkDescriptorFont, value);
  }

/**
    Sets the primary key columns object associated to a DbORStyle's instance.

    @param value the primary key columns object to be associated

 **/
  public final void setOr_pkColumnsPrefix(DbtPrefix value) throws DbException {
    basicSet(fOr_pkColumnsPrefix, value);
  }

/**
    Sets the index columns object associated to a DbORStyle's instance.

    @param value the index columns object to be associated

 **/
  public final void setOr_indexColumnsPrefix(DbtPrefix value) throws DbException {
    basicSet(fOr_indexColumnsPrefix, value);
  }

/**
    Sets the "primary key" property of a DbORStyle's instance.

    @param value the "primary key" property

 **/
  public final void setOr_pkDisplay(Boolean value) throws DbException {
    basicSet(fOr_pkDisplay, value);
  }

/**
    Sets the "unique key" property of a DbORStyle's instance.

    @param value the "unique key" property

 **/
  public final void setOr_ukDisplay(Boolean value) throws DbException {
    basicSet(fOr_ukDisplay, value);
  }

/**
    Sets the "foreign key" property of a DbORStyle's instance.

    @param value the "foreign key" property

 **/
  public final void setOr_fkDisplay(Boolean value) throws DbException {
    basicSet(fOr_fkDisplay, value);
  }

/**
    Sets the "check constraint" property of a DbORStyle's instance.

    @param value the "check constraint" property

 **/
  public final void setOr_checkDisplay(Boolean value) throws DbException {
    basicSet(fOr_checkDisplay, value);
  }

/**
    Sets the "trigger columns" property of a DbORStyle's instance.

    @param value the "trigger columns" property

 **/
  public final void setOr_triggerColumnsDisplay(Boolean value) throws DbException {
    basicSet(fOr_triggerColumnsDisplay, value);
  }

/**
    Sets the "table user" property of a DbORStyle's instance.

    @param value the "table user" property

 **/
  public final void setOr_tableOwnerDisplay(Boolean value) throws DbException {
    basicSet(fOr_tableOwnerDisplay, value);
  }

/**
    Sets the referringprojector object associated to a DbORStyle's instance.

    @param value the referringprojector object to be associated

 **/
  public final void setReferringProjectOr(DbSMSProject value) throws DbException {
    basicSet(fReferringProjectOr, value);
  }

/**
    Sets the "table dash style" property of a DbORStyle's instance.

    @param value the "table dash style" property

 **/
  public final void setDashStyleDbORTable(Boolean value) throws DbException {
    basicSet(fDashStyleDbORTable, value);
  }

/**
    Sets the "view background" property of a DbORStyle's instance.

    @param value the "view background" property

 **/
  public final void setBackgroundColorDbORView(Color value) throws DbException {
    basicSet(fBackgroundColorDbORView, value);
  }

/**
    Sets the "view descriptor" property of a DbORStyle's instance.

    @param value the "view descriptor" property

 **/
  public final void setOr_viewDescriptorFont(Font value) throws DbException {
    basicSet(fOr_viewDescriptorFont, value);
  }

/**
    Sets the "check constraint column" property of a DbORStyle's instance.

    @param value the "check constraint column" property

 **/
  public final void setOr_checkParametersDisplay(Boolean value) throws DbException {
    basicSet(fOr_checkParametersDisplay, value);
  }

/**
    Sets the "view dash style" property of a DbORStyle's instance.

    @param value the "view dash style" property

 **/
  public final void setDashStyleDbORView(Boolean value) throws DbException {
    basicSet(fDashStyleDbORView, value);
  }

/**
    Sets the "view text" property of a DbORStyle's instance.

    @param value the "view text" property

 **/
  public final void setTextColorDbORView(Color value) throws DbException {
    basicSet(fTextColorDbORView, value);
  }

/**
    Sets the "view highlight" property of a DbORStyle's instance.

    @param value the "view highlight" property

 **/
  public final void setHighlightDbORView(Boolean value) throws DbException {
    basicSet(fHighlightDbORView, value);
  }

/**
    Sets the "view border" property of a DbORStyle's instance.

    @param value the "view border" property

 **/
  public final void setLineColorDbORView(Color value) throws DbException {
    basicSet(fLineColorDbORView, value);
  }

/**
    Sets the "column length and decimals" property of a DbORStyle's instance.

    @param value the "column length and decimals" property

 **/
  public final void setOr_columnLengthDecimalsDisplay(Boolean value) throws DbException {
    basicSet(fOr_columnLengthDecimalsDisplay, value);
  }

/**
    Sets the "column initial value" property of a DbORStyle's instance.

    @param value the "column initial value" property

 **/
  public final void setOr_columnDefaultValueDisplay(Boolean value) throws DbException {
    basicSet(fOr_columnDefaultValueDisplay, value);
  }

/**
    Sets the "primary key columns" property of a DbORStyle's instance.

    @param value the "primary key columns" property

 **/
  public final void setOr_pkColumnsFont(Font value) throws DbException {
    basicSet(fOr_pkColumnsFont, value);
  }

/**
    Sets the "check constraint column" property of a DbORStyle's instance.

    @param value the "check constraint column" property

 **/
  public final void setOr_checkParametersFont(Font value) throws DbException {
    basicSet(fOr_checkParametersFont, value);
  }

/**
    Sets the "unique key descriptor" property of a DbORStyle's instance.

    @param value the "unique key descriptor" property

 **/
  public final void setOr_ukDescriptorFont(Font value) throws DbException {
    basicSet(fOr_ukDescriptorFont, value);
  }

/**
    Sets the "foreign key columns" property of a DbORStyle's instance.

    @param value the "foreign key columns" property

 **/
  public final void setOr_fkColumnsFont(Font value) throws DbException {
    basicSet(fOr_fkColumnsFont, value);
  }

/**
    Sets the "index columns" property of a DbORStyle's instance.

    @param value the "index columns" property

 **/
  public final void setOr_indexColumnsDisplay(Boolean value) throws DbException {
    basicSet(fOr_indexColumnsDisplay, value);
  }

/**
    Sets the "index cluster" property of a DbORStyle's instance.

    @param value the "index cluster" property

 **/
  public final void setOr_indexTypeFont(Font value) throws DbException {
    basicSet(fOr_indexTypeFont, value);
  }

/**
    Sets the "index cluster" property of a DbORStyle's instance.

    @param value the "index cluster" property

 **/
  public final void setOr_indexTypeDisplay(Boolean value) throws DbException {
    basicSet(fOr_indexTypeDisplay, value);
  }

/**
    Sets the "trigger" property of a DbORStyle's instance.

    @param value the "trigger" property

 **/
  public final void setOr_triggerDisplay(Boolean value) throws DbException {
    basicSet(fOr_triggerDisplay, value);
  }

/**
    Sets the "trigger columns" property of a DbORStyle's instance.

    @param value the "trigger columns" property

 **/
  public final void setOr_triggerColumnsFont(Font value) throws DbException {
    basicSet(fOr_triggerColumnsFont, value);
  }

/**
    Sets the view object associated to a DbORStyle's instance.

    @param value the view object to be associated

 **/
  public final void setOr_viewPrefix(DbtPrefix value) throws DbException {
    basicSet(fOr_viewPrefix, value);
  }

/**
    Sets the "table additional descriptor" property of a DbORStyle's instance.

    @param value the "table additional descriptor" property

 **/
  public final void setOr_tableAdditionnalDescriptorFont(Font value) throws DbException {
    basicSet(fOr_tableAdditionnalDescriptorFont, value);
  }

/**
    Sets the "view additional descriptor" property of a DbORStyle's instance.

    @param value the "view additional descriptor" property

 **/
  public final void setOr_viewAdditionnalDescriptor(SMSDisplayDescriptor value) throws DbException {
    basicSet(fOr_viewAdditionnalDescriptor, value);
  }

/**
    Sets the "view additional descriptor" property of a DbORStyle's instance.

    @param value the "view additional descriptor" property

 **/
  public final void setOr_viewAdditionnalDescriptorFont(Font value) throws DbException {
    basicSet(fOr_viewAdditionnalDescriptorFont, value);
  }

/**
    Sets the "primary column descriptor" property of a DbORStyle's instance.

    @param value the "primary column descriptor" property

 **/
  public final void setOr_pkDescriptorFormat(ORDescriptorFormat value) throws DbException {
    basicSet(fOr_pkDescriptorFormat, value);
  }

/**
    Sets the "foreign column descriptor" property of a DbORStyle's instance.

    @param value the "foreign column descriptor" property

 **/
  public final void setOr_fkDescriptorFormat(ORDescriptorFormat value) throws DbException {
    basicSet(fOr_fkDescriptorFormat, value);
  }

/**
    Sets the "unique column descriptor" property of a DbORStyle's instance.

    @param value the "unique column descriptor" property

 **/
  public final void setOr_ukDescriptorFormat(ORDescriptorFormat value) throws DbException {
    basicSet(fOr_ukDescriptorFormat, value);
  }

/**
    Sets the "check constraint prefix" property of a DbORStyle's instance.

    @param value the "check constraint prefix" property

 **/
  public final void setOr_checkConstraintPrefixFont(Font value) throws DbException {
    basicSet(fOr_checkConstraintPrefixFont, value);
  }

/**
    Sets the "column prefix" property of a DbORStyle's instance.

    @param value the "column prefix" property

 **/
  public final void setOr_columnPrefixFont(Font value) throws DbException {
    basicSet(fOr_columnPrefixFont, value);
  }

/**
    Sets the "foreign key prefix" property of a DbORStyle's instance.

    @param value the "foreign key prefix" property

 **/
  public final void setOr_fkPrefixFont(Font value) throws DbException {
    basicSet(fOr_fkPrefixFont, value);
  }

/**
    Sets the "index prefix" property of a DbORStyle's instance.

    @param value the "index prefix" property

 **/
  public final void setOr_indexPrefixFont(Font value) throws DbException {
    basicSet(fOr_indexPrefixFont, value);
  }

/**
    Sets the "trigger prefix" property of a DbORStyle's instance.

    @param value the "trigger prefix" property

 **/
  public final void setOr_triggerPrefixFont(Font value) throws DbException {
    basicSet(fOr_triggerPrefixFont, value);
  }

/**
    Sets the "unique key prefix" property of a DbORStyle's instance.

    @param value the "unique key prefix" property

 **/
  public final void setOr_ukPrefixFont(Font value) throws DbException {
    basicSet(fOr_ukPrefixFont, value);
  }

/**
    Sets the "primary key prefix" property of a DbORStyle's instance.

    @param value the "primary key prefix" property

 **/
  public final void setOr_pkPrefixFont(Font value) throws DbException {
    basicSet(fOr_pkPrefixFont, value);
  }

/**
    Sets the "table prefix" property of a DbORStyle's instance.

    @param value the "table prefix" property

 **/
  public final void setOr_tablePrefixFont(Font value) throws DbException {
    basicSet(fOr_tablePrefixFont, value);
  }

/**
    Sets the "column type descriptor" property of a DbORStyle's instance.

    @param value the "column type descriptor" property

 **/
  public final void setOr_columnTypeFont(Font value) throws DbException {
    basicSet(fOr_columnTypeFont, value);
  }

/**
    Sets the "column null value" property of a DbORStyle's instance.

    @param value the "column null value" property

 **/
  public final void setOr_columnNullValueDisplay(Boolean value) throws DbException {
    basicSet(fOr_columnNullValueDisplay, value);
  }

/**
    Sets the "column null value" property of a DbORStyle's instance.

    @param value the "column null value" property

 **/
  public final void setOr_columnNullValueFont(Font value) throws DbException {
    basicSet(fOr_columnNullValueFont, value);
  }

/**
    Sets the "column" property of a DbORStyle's instance.

    @param value the "column" property

 **/
  public final void setOr_columnDisplay(Boolean value) throws DbException {
    basicSet(fOr_columnDisplay, value);
  }

/**
    Sets the "index descriptor" property of a DbORStyle's instance.

    @param value the "index descriptor" property

 **/
  public final void setOr_indexDescriptorFont(Font value) throws DbException {
    basicSet(fOr_indexDescriptorFont, value);
  }

/**
    Sets the "index" property of a DbORStyle's instance.

    @param value the "index" property

 **/
  public final void setOr_indexDisplay(Boolean value) throws DbException {
    basicSet(fOr_indexDisplay, value);
  }

/**
    Sets the "index columns" property of a DbORStyle's instance.

    @param value the "index columns" property

 **/
  public final void setOr_indexColumnsFont(Font value) throws DbException {
    basicSet(fOr_indexColumnsFont, value);
  }

/**
    Sets the "association navigable role descriptor" property of a DbORStyle's instance.

    @param value the "association navigable role descriptor" property

 **/
  public final void setOr_associationChildRoleDescriptorDisplay(Boolean value) throws DbException {
    basicSet(fOr_associationChildRoleDescriptorDisplay, value);
  }

/**
    Sets the "association not navigable role descriptor" property of a DbORStyle's instance.

    @param value the "association not navigable role descriptor" property

 **/
  public final void setOr_associationParentRoleDescriptorDisplay(Boolean value) throws DbException {
    basicSet(fOr_associationParentRoleDescriptorDisplay, value);
  }

/**
    Sets the "association descriptor" property of a DbORStyle's instance.

    @param value the "association descriptor" property

 **/
  public final void setOr_associationDescriptorDisplay(Boolean value) throws DbException {
    basicSet(fOr_associationDescriptorDisplay, value);
  }

/**
    Sets the "uml stereotype" property of a DbORStyle's instance.

    @param value the "uml stereotype" property

 **/
  public final void setOr_umlStereotypeDisplayed(Boolean value) throws DbException {
    basicSet(fOr_umlStereotypeDisplayed, value);
  }

/**
    Sets the "uml constraint" property of a DbORStyle's instance.

    @param value the "uml constraint" property

 **/
  public final void setOr_umlConstraintDisplayed(Boolean value) throws DbException {
    basicSet(fOr_umlConstraintDisplayed, value);
  }

/**
    Sets the "uml stereotype icon" property of a DbORStyle's instance.

    @param value the "uml stereotype icon" property

 **/
  public final void setOr_umlStereotypeIconDisplayed(Boolean value) throws DbException {
    basicSet(fOr_umlStereotypeIconDisplayed, value);
  }

/**
    Sets the "show association tables as relationships" property of a DbORStyle's instance.

    @param value the "show association tables as relationships" property

 **/
  public final void setOr_associationTablesAsRelationships(Boolean value) throws DbException {
    basicSet(fOr_associationTablesAsRelationships, value);
  }

/**
    Sets the "show associations as relationships" property of a DbORStyle's instance.

    @param value the "show associations as relationships" property

 **/
  public final void setOr_associationAsRelationships(Boolean value) throws DbException {
    basicSet(fOr_associationAsRelationships, value);
  }

/**
    Sets the "show dependent tables" property of a DbORStyle's instance.

    @param value the "show dependent tables" property

 **/
  public final void setOr_showDependentTables(Boolean value) throws DbException {
    basicSet(fOr_showDependentTables, value);
  }

/**
    Sets the "unidentifying associations are dashed" property of a DbORStyle's instance.

    @param value the "unidentifying associations are dashed" property

 **/
  public final void setOr_UnidentifyingAssociationsAreDashed(Boolean value) throws DbException {
    basicSet(fOr_UnidentifyingAssociationsAreDashed, value);
  }

/**
    Sets the "association direction" property of a DbORStyle's instance.

    @param value the "association direction" property

 **/
  public final void setOr_umlAssociationDirection(Boolean value) throws DbException {
    basicSet(fOr_umlAssociationDirection, value);
  }

/**
    Sets the referringprojecter object associated to a DbORStyle's instance.

    @param value the referringprojecter object to be associated

 **/
  public final void setReferringProjectEr(DbSMSProject value) throws DbException {
    basicSet(fReferringProjectEr, value);
  }

/**
    Sets the choice column object associated to a DbORStyle's instance.

    @param value the choice column object to be associated

 **/
  public final void setOr_columnChoicePrefix(DbtPrefix value) throws DbException {
    basicSet(fOr_columnChoicePrefix, value);
  }

/**
    Sets the specialization column object associated to a DbORStyle's instance.

    @param value the specialization column object to be associated

 **/
  public final void setOr_columnSpecializationPrefix(DbtPrefix value) throws DbException {
    basicSet(fOr_columnSpecializationPrefix, value);
  }

/**
    Sets the "choice/specialization background" property of a DbORStyle's instance.

    @param value the "choice/specialization background" property

 **/
  public final void setBackgroundColorDbORChoiceOrSpecialization(Color value) throws DbException {
    basicSet(fBackgroundColorDbORChoiceOrSpecialization, value);
  }

/**
    Sets the "choice/specialization border" property of a DbORStyle's instance.

    @param value the "choice/specialization border" property

 **/
  public final void setLineColorDbORChoiceOrSpecialization(Color value) throws DbException {
    basicSet(fLineColorDbORChoiceOrSpecialization, value);
  }

/**
    
 **/
  public void set(MetaField metaField, Object value) throws DbException {
    if (metaField.getMetaClass() == metaClass) {
      basicSet(metaField, value);
    }
    else super.set(metaField, value);
  }

/**
    
 **/
  public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
    super.set(relation, neighbor, op);
  }

  //Getters

/**
    Gets the column having a supercolumn object associated to a DbORStyle's instance.

    @return the column having a supercolumn object

 **/
  public final DbtPrefix getOr_columnLinkedToSuperModelPrefix() throws DbException {
    return (DbtPrefix)get(fOr_columnLinkedToSuperModelPrefix);
  }

/**
    Gets the "table highlight" of a DbORStyle's instance.

    @return the "table highlight"

 **/
  public final Boolean getHighlightDbORTable() throws DbException {
    return (Boolean)get(fHighlightDbORTable);
  }

/**
    Gets the fact table object associated to a DbORStyle's instance.

    @return the fact table object

 **/
  public final DbtPrefix getOr_factTable() throws DbException {
    return (DbtPrefix)get(fOr_factTable);
  }

/**
    Gets the table having subtables object associated to a DbORStyle's instance.

    @return the table having subtables object

 **/
  public final DbtPrefix getOr_tableLinkedToSubModelPrefix() throws DbException {
    return (DbtPrefix)get(fOr_tableLinkedToSubModelPrefix);
  }

/**
    Gets the "index unique" of a DbORStyle's instance.

    @return the "index unique"

 **/
  public final Font getOr_indexUniqueFont() throws DbException {
    return (Font)get(fOr_indexUniqueFont);
  }

/**
    Gets the column null value object associated to a DbORStyle's instance.

    @return the column null value object

 **/
  public final DbtPrefix getOr_columnNullValuePrefix() throws DbException {
    return (DbtPrefix)get(fOr_columnNullValuePrefix);
  }

/**
    Gets the "column type" of a DbORStyle's instance.

    @return the "column type"

 **/
  public final Boolean getOr_columnTypeDisplay() throws DbException {
    return (Boolean)get(fOr_columnTypeDisplay);
  }

/**
    Gets the "foreign key columns" of a DbORStyle's instance.

    @return the "foreign key columns"

 **/
  public final Boolean getOr_fkColumnsDisplay() throws DbException {
    return (Boolean)get(fOr_fkColumnsDisplay);
  }

/**
    Gets the "association not navigable role connectivities" of a DbORStyle's instance.

    @return the "association not navigable role connectivities"

 **/
  public final Boolean getOr_associationParentRoleConnectivitiesDisplay() throws DbException {
    return (Boolean)get(fOr_associationParentRoleConnectivitiesDisplay);
  }

/**
    Gets the "association role connectivities" of a DbORStyle's instance.

    @return the "association role connectivities"

 **/
  public final Font getOr_associationConnectivitiesFont() throws DbException {
    return (Font)get(fOr_associationConnectivitiesFont);
  }

/**
    Gets the foreign key columns object associated to a DbORStyle's instance.

    @return the foreign key columns object

 **/
  public final DbtPrefix getOr_fkColumnsPrefix() throws DbException {
    return (DbtPrefix)get(fOr_fkColumnsPrefix);
  }

/**
    Gets the "column additional descriptor" of a DbORStyle's instance.

    @return the "column additional descriptor"

 **/
  public final SMSDisplayDescriptor getOr_columnAdditionnalDescriptor() throws DbException {
    return (SMSDisplayDescriptor)get(fOr_columnAdditionnalDescriptor);
  }

/**
    Gets the "column additional descriptor" of a DbORStyle's instance.

    @return the "column additional descriptor"

 **/
  public final Font getOr_columnAdditionnalDescriptorFont() throws DbException {
    return (Font)get(fOr_columnAdditionnalDescriptorFont);
  }

/**
    Gets the "table additional descriptor" of a DbORStyle's instance.

    @return the "table additional descriptor"

 **/
  public final SMSDisplayDescriptor getOr_tableAdditionnalDescriptor() throws DbException {
    return (SMSDisplayDescriptor)get(fOr_tableAdditionnalDescriptor);
  }

/**
    Gets the "association role descriptor" of a DbORStyle's instance.

    @return the "association role descriptor"

 **/
  public final Font getOr_associationRoleDescriptorFont() throws DbException {
    return (Font)get(fOr_associationRoleDescriptorFont);
  }

/**
    Gets the "association descriptor" of a DbORStyle's instance.

    @return the "association descriptor"

 **/
  public final Font getOr_associationDescriptorFont() throws DbException {
    return (Font)get(fOr_associationDescriptorFont);
  }

/**
    Gets the "check constraint" of a DbORStyle's instance.

    @return the "check constraint"

 **/
  public final Font getOr_checkConstraintFont() throws DbException {
    return (Font)get(fOr_checkConstraintFont);
  }

/**
    Gets the "descriptor" of a DbORStyle's instance.

    @return the "descriptor"

 **/
  public final SMSDisplayDescriptor getOr_nameDescriptor() throws DbException {
    return (SMSDisplayDescriptor)get(fOr_nameDescriptor);
  }

/**
    Gets the "unique key columns" of a DbORStyle's instance.

    @return the "unique key columns"

 **/
  public final Boolean getOr_ukColumnsDisplay() throws DbException {
    return (Boolean)get(fOr_ukColumnsDisplay);
  }

/**
    Gets the "data model dash style" of a DbORStyle's instance.

    @return the "data model dash style"

 **/
  public final Boolean getOr_dataModelDashStyle() throws DbException {
    return (Boolean)get(fOr_dataModelDashStyle);
  }

/**
    Gets the column having subcolumns object associated to a DbORStyle's instance.

    @return the column having subcolumns object

 **/
  public final DbtPrefix getOr_columnLinkedToSubModelPrefix() throws DbException {
    return (DbtPrefix)get(fOr_columnLinkedToSubModelPrefix);
  }

/**
    Gets the "association navigable role connectivities" of a DbORStyle's instance.

    @return the "association navigable role connectivities"

 **/
  public final Boolean getOr_associationChildRoleConnectivitiesDisplay() throws DbException {
    return (Boolean)get(fOr_associationChildRoleConnectivitiesDisplay);
  }

/**
    Gets the "column length and decimals" of a DbORStyle's instance.

    @return the "column length and decimals"

 **/
  public final Font getOr_columnLengthDecimalsFont() throws DbException {
    return (Font)get(fOr_columnLengthDecimalsFont);
  }

/**
    Gets the dimension table object associated to a DbORStyle's instance.

    @return the dimension table object

 **/
  public final DbtPrefix getOr_dimensionTable() throws DbException {
    return (DbtPrefix)get(fOr_dimensionTable);
  }

/**
    Gets the "column descriptor" of a DbORStyle's instance.

    @return the "column descriptor"

 **/
  public final Font getOr_columnDescriptorFont() throws DbException {
    return (Font)get(fOr_columnDescriptorFont);
  }

/**
    Gets the "column initial value" of a DbORStyle's instance.

    @return the "column initial value"

 **/
  public final Font getOr_columnDefaultValueFont() throws DbException {
    return (Font)get(fOr_columnDefaultValueFont);
  }

/**
    Gets the "foreign key descriptor" of a DbORStyle's instance.

    @return the "foreign key descriptor"

 **/
  public final Font getOr_fkDescriptorFont() throws DbException {
    return (Font)get(fOr_fkDescriptorFont);
  }

/**
    Gets the "index unique" of a DbORStyle's instance.

    @return the "index unique"

 **/
  public final Boolean getOr_indexUniqueDisplay() throws DbException {
    return (Boolean)get(fOr_indexUniqueDisplay);
  }

/**
    Gets the "table border" of a DbORStyle's instance.

    @return the "table border"

 **/
  public final Color getLineColorDbORTable() throws DbException {
    return (Color)get(fLineColorDbORTable);
  }

/**
    Gets the "primary key columns" of a DbORStyle's instance.

    @return the "primary key columns"

 **/
  public final Boolean getOr_pkColumnsDisplay() throws DbException {
    return (Boolean)get(fOr_pkColumnsDisplay);
  }

/**
    Gets the "unique key columns" of a DbORStyle's instance.

    @return the "unique key columns"

 **/
  public final Font getOr_ukColumnsFont() throws DbException {
    return (Font)get(fOr_ukColumnsFont);
  }

/**
    Gets the unique key columns object associated to a DbORStyle's instance.

    @return the unique key columns object

 **/
  public final DbtPrefix getOr_ukColumnsPrefix() throws DbException {
    return (DbtPrefix)get(fOr_ukColumnsPrefix);
  }

/**
    Gets the "trigger descriptor" of a DbORStyle's instance.

    @return the "trigger descriptor"

 **/
  public final Font getOr_triggerDescriptorFont() throws DbException {
    return (Font)get(fOr_triggerDescriptorFont);
  }

/**
    Gets the "table background" of a DbORStyle's instance.

    @return the "table background"

 **/
  public final Color getBackgroundColorDbORTable() throws DbException {
    return (Color)get(fBackgroundColorDbORTable);
  }

/**
    Gets the "column domain source type" of a DbORStyle's instance.

    @return the "column domain source type"

 **/
  public final Boolean getOr_columnDomainSourceTypeDisplay() throws DbException {
    return (Boolean)get(fOr_columnDomainSourceTypeDisplay);
  }

/**
    Gets the "table text" of a DbORStyle's instance.

    @return the "table text"

 **/
  public final Color getTextColorDbORTable() throws DbException {
    return (Color)get(fTextColorDbORTable);
  }

/**
    Gets the "column domain source type" of a DbORStyle's instance.

    @return the "column domain source type"

 **/
  public final Font getOr_columnDomainSourceTypeFont() throws DbException {
    return (Font)get(fOr_columnDomainSourceTypeFont);
  }

/**
    Gets the table having a supertable object associated to a DbORStyle's instance.

    @return the table having a supertable object

 **/
  public final DbtPrefix getOr_tableLinkedToSuperModelPrefix() throws DbException {
    return (DbtPrefix)get(fOr_tableLinkedToSuperModelPrefix);
  }

/**
    Gets the "table descriptor" of a DbORStyle's instance.

    @return the "table descriptor"

 **/
  public final Font getOr_tableDescriptorFont() throws DbException {
    return (Font)get(fOr_tableDescriptorFont);
  }

/**
    Gets the "data model highlight" of a DbORStyle's instance.

    @return the "data model highlight"

 **/
  public final Boolean getOr_dataModelHighlight() throws DbException {
    return (Boolean)get(fOr_dataModelHighlight);
  }

/**
    Gets the "primary key descriptor" of a DbORStyle's instance.

    @return the "primary key descriptor"

 **/
  public final Font getOr_pkDescriptorFont() throws DbException {
    return (Font)get(fOr_pkDescriptorFont);
  }

/**
    Gets the primary key columns object associated to a DbORStyle's instance.

    @return the primary key columns object

 **/
  public final DbtPrefix getOr_pkColumnsPrefix() throws DbException {
    return (DbtPrefix)get(fOr_pkColumnsPrefix);
  }

/**
    Gets the index columns object associated to a DbORStyle's instance.

    @return the index columns object

 **/
  public final DbtPrefix getOr_indexColumnsPrefix() throws DbException {
    return (DbtPrefix)get(fOr_indexColumnsPrefix);
  }

/**
    Gets the "primary key" of a DbORStyle's instance.

    @return the "primary key"

 **/
  public final Boolean getOr_pkDisplay() throws DbException {
    return (Boolean)get(fOr_pkDisplay);
  }

/**
    Gets the "unique key" of a DbORStyle's instance.

    @return the "unique key"

 **/
  public final Boolean getOr_ukDisplay() throws DbException {
    return (Boolean)get(fOr_ukDisplay);
  }

/**
    Gets the "foreign key" of a DbORStyle's instance.

    @return the "foreign key"

 **/
  public final Boolean getOr_fkDisplay() throws DbException {
    return (Boolean)get(fOr_fkDisplay);
  }

/**
    Gets the "check constraint" of a DbORStyle's instance.

    @return the "check constraint"

 **/
  public final Boolean getOr_checkDisplay() throws DbException {
    return (Boolean)get(fOr_checkDisplay);
  }

/**
    Gets the "trigger columns" of a DbORStyle's instance.

    @return the "trigger columns"

 **/
  public final Boolean getOr_triggerColumnsDisplay() throws DbException {
    return (Boolean)get(fOr_triggerColumnsDisplay);
  }

/**
    Gets the "table user" of a DbORStyle's instance.

    @return the "table user"

 **/
  public final Boolean getOr_tableOwnerDisplay() throws DbException {
    return (Boolean)get(fOr_tableOwnerDisplay);
  }

/**
    Gets the referringprojector object associated to a DbORStyle's instance.

    @return the referringprojector object

 **/
  public final DbSMSProject getReferringProjectOr() throws DbException {
    return (DbSMSProject)get(fReferringProjectOr);
  }

/**
    Gets the "table dash style" of a DbORStyle's instance.

    @return the "table dash style"

 **/
  public final Boolean getDashStyleDbORTable() throws DbException {
    return (Boolean)get(fDashStyleDbORTable);
  }

/**
    Gets the "view background" of a DbORStyle's instance.

    @return the "view background"

 **/
  public final Color getBackgroundColorDbORView() throws DbException {
    return (Color)get(fBackgroundColorDbORView);
  }

/**
    Gets the "view descriptor" of a DbORStyle's instance.

    @return the "view descriptor"

 **/
  public final Font getOr_viewDescriptorFont() throws DbException {
    return (Font)get(fOr_viewDescriptorFont);
  }

/**
    Gets the "check constraint column" of a DbORStyle's instance.

    @return the "check constraint column"

 **/
  public final Boolean getOr_checkParametersDisplay() throws DbException {
    return (Boolean)get(fOr_checkParametersDisplay);
  }

/**
    Gets the "view dash style" of a DbORStyle's instance.

    @return the "view dash style"

 **/
  public final Boolean getDashStyleDbORView() throws DbException {
    return (Boolean)get(fDashStyleDbORView);
  }

/**
    Gets the "view text" of a DbORStyle's instance.

    @return the "view text"

 **/
  public final Color getTextColorDbORView() throws DbException {
    return (Color)get(fTextColorDbORView);
  }

/**
    Gets the "view highlight" of a DbORStyle's instance.

    @return the "view highlight"

 **/
  public final Boolean getHighlightDbORView() throws DbException {
    return (Boolean)get(fHighlightDbORView);
  }

/**
    Gets the "view border" of a DbORStyle's instance.

    @return the "view border"

 **/
  public final Color getLineColorDbORView() throws DbException {
    return (Color)get(fLineColorDbORView);
  }

/**
    Gets the "column length and decimals" of a DbORStyle's instance.

    @return the "column length and decimals"

 **/
  public final Boolean getOr_columnLengthDecimalsDisplay() throws DbException {
    return (Boolean)get(fOr_columnLengthDecimalsDisplay);
  }

/**
    Gets the "column initial value" of a DbORStyle's instance.

    @return the "column initial value"

 **/
  public final Boolean getOr_columnDefaultValueDisplay() throws DbException {
    return (Boolean)get(fOr_columnDefaultValueDisplay);
  }

/**
    Gets the "primary key columns" of a DbORStyle's instance.

    @return the "primary key columns"

 **/
  public final Font getOr_pkColumnsFont() throws DbException {
    return (Font)get(fOr_pkColumnsFont);
  }

/**
    Gets the "check constraint column" of a DbORStyle's instance.

    @return the "check constraint column"

 **/
  public final Font getOr_checkParametersFont() throws DbException {
    return (Font)get(fOr_checkParametersFont);
  }

/**
    Gets the "unique key descriptor" of a DbORStyle's instance.

    @return the "unique key descriptor"

 **/
  public final Font getOr_ukDescriptorFont() throws DbException {
    return (Font)get(fOr_ukDescriptorFont);
  }

/**
    Gets the "foreign key columns" of a DbORStyle's instance.

    @return the "foreign key columns"

 **/
  public final Font getOr_fkColumnsFont() throws DbException {
    return (Font)get(fOr_fkColumnsFont);
  }

/**
    Gets the "index columns" of a DbORStyle's instance.

    @return the "index columns"

 **/
  public final Boolean getOr_indexColumnsDisplay() throws DbException {
    return (Boolean)get(fOr_indexColumnsDisplay);
  }

/**
    Gets the "index cluster" of a DbORStyle's instance.

    @return the "index cluster"

 **/
  public final Font getOr_indexTypeFont() throws DbException {
    return (Font)get(fOr_indexTypeFont);
  }

/**
    Gets the "index cluster" of a DbORStyle's instance.

    @return the "index cluster"

 **/
  public final Boolean getOr_indexTypeDisplay() throws DbException {
    return (Boolean)get(fOr_indexTypeDisplay);
  }

/**
    Gets the "trigger" of a DbORStyle's instance.

    @return the "trigger"

 **/
  public final Boolean getOr_triggerDisplay() throws DbException {
    return (Boolean)get(fOr_triggerDisplay);
  }

/**
    Gets the "trigger columns" of a DbORStyle's instance.

    @return the "trigger columns"

 **/
  public final Font getOr_triggerColumnsFont() throws DbException {
    return (Font)get(fOr_triggerColumnsFont);
  }

/**
    Gets the view object associated to a DbORStyle's instance.

    @return the view object

 **/
  public final DbtPrefix getOr_viewPrefix() throws DbException {
    return (DbtPrefix)get(fOr_viewPrefix);
  }

/**
    Gets the "table additional descriptor" of a DbORStyle's instance.

    @return the "table additional descriptor"

 **/
  public final Font getOr_tableAdditionnalDescriptorFont() throws DbException {
    return (Font)get(fOr_tableAdditionnalDescriptorFont);
  }

/**
    Gets the "view additional descriptor" of a DbORStyle's instance.

    @return the "view additional descriptor"

 **/
  public final SMSDisplayDescriptor getOr_viewAdditionnalDescriptor() throws DbException {
    return (SMSDisplayDescriptor)get(fOr_viewAdditionnalDescriptor);
  }

/**
    Gets the "view additional descriptor" of a DbORStyle's instance.

    @return the "view additional descriptor"

 **/
  public final Font getOr_viewAdditionnalDescriptorFont() throws DbException {
    return (Font)get(fOr_viewAdditionnalDescriptorFont);
  }

/**
    Gets the "primary column descriptor" of a DbORStyle's instance.

    @return the "primary column descriptor"

 **/
  public final ORDescriptorFormat getOr_pkDescriptorFormat() throws DbException {
    return (ORDescriptorFormat)get(fOr_pkDescriptorFormat);
  }

/**
    Gets the "foreign column descriptor" of a DbORStyle's instance.

    @return the "foreign column descriptor"

 **/
  public final ORDescriptorFormat getOr_fkDescriptorFormat() throws DbException {
    return (ORDescriptorFormat)get(fOr_fkDescriptorFormat);
  }

/**
    Gets the "unique column descriptor" of a DbORStyle's instance.

    @return the "unique column descriptor"

 **/
  public final ORDescriptorFormat getOr_ukDescriptorFormat() throws DbException {
    return (ORDescriptorFormat)get(fOr_ukDescriptorFormat);
  }

/**
    Gets the "check constraint prefix" of a DbORStyle's instance.

    @return the "check constraint prefix"

 **/
  public final Font getOr_checkConstraintPrefixFont() throws DbException {
    return (Font)get(fOr_checkConstraintPrefixFont);
  }

/**
    Gets the "column prefix" of a DbORStyle's instance.

    @return the "column prefix"

 **/
  public final Font getOr_columnPrefixFont() throws DbException {
    return (Font)get(fOr_columnPrefixFont);
  }

/**
    Gets the "foreign key prefix" of a DbORStyle's instance.

    @return the "foreign key prefix"

 **/
  public final Font getOr_fkPrefixFont() throws DbException {
    return (Font)get(fOr_fkPrefixFont);
  }

/**
    Gets the "index prefix" of a DbORStyle's instance.

    @return the "index prefix"

 **/
  public final Font getOr_indexPrefixFont() throws DbException {
    return (Font)get(fOr_indexPrefixFont);
  }

/**
    Gets the "trigger prefix" of a DbORStyle's instance.

    @return the "trigger prefix"

 **/
  public final Font getOr_triggerPrefixFont() throws DbException {
    return (Font)get(fOr_triggerPrefixFont);
  }

/**
    Gets the "unique key prefix" of a DbORStyle's instance.

    @return the "unique key prefix"

 **/
  public final Font getOr_ukPrefixFont() throws DbException {
    return (Font)get(fOr_ukPrefixFont);
  }

/**
    Gets the "primary key prefix" of a DbORStyle's instance.

    @return the "primary key prefix"

 **/
  public final Font getOr_pkPrefixFont() throws DbException {
    return (Font)get(fOr_pkPrefixFont);
  }

/**
    Gets the "table prefix" of a DbORStyle's instance.

    @return the "table prefix"

 **/
  public final Font getOr_tablePrefixFont() throws DbException {
    return (Font)get(fOr_tablePrefixFont);
  }

/**
    Gets the "column type descriptor" of a DbORStyle's instance.

    @return the "column type descriptor"

 **/
  public final Font getOr_columnTypeFont() throws DbException {
    return (Font)get(fOr_columnTypeFont);
  }

/**
    Gets the "column null value" of a DbORStyle's instance.

    @return the "column null value"

 **/
  public final Boolean getOr_columnNullValueDisplay() throws DbException {
    return (Boolean)get(fOr_columnNullValueDisplay);
  }

/**
    Gets the "column null value" of a DbORStyle's instance.

    @return the "column null value"

 **/
  public final Font getOr_columnNullValueFont() throws DbException {
    return (Font)get(fOr_columnNullValueFont);
  }

/**
    Gets the "column" of a DbORStyle's instance.

    @return the "column"

 **/
  public final Boolean getOr_columnDisplay() throws DbException {
    return (Boolean)get(fOr_columnDisplay);
  }

/**
    Gets the "index descriptor" of a DbORStyle's instance.

    @return the "index descriptor"

 **/
  public final Font getOr_indexDescriptorFont() throws DbException {
    return (Font)get(fOr_indexDescriptorFont);
  }

/**
    Gets the "index" of a DbORStyle's instance.

    @return the "index"

 **/
  public final Boolean getOr_indexDisplay() throws DbException {
    return (Boolean)get(fOr_indexDisplay);
  }

/**
    Gets the "index columns" of a DbORStyle's instance.

    @return the "index columns"

 **/
  public final Font getOr_indexColumnsFont() throws DbException {
    return (Font)get(fOr_indexColumnsFont);
  }

/**
    Gets the "association navigable role descriptor" of a DbORStyle's instance.

    @return the "association navigable role descriptor"

 **/
  public final Boolean getOr_associationChildRoleDescriptorDisplay() throws DbException {
    return (Boolean)get(fOr_associationChildRoleDescriptorDisplay);
  }

/**
    Gets the "association not navigable role descriptor" of a DbORStyle's instance.

    @return the "association not navigable role descriptor"

 **/
  public final Boolean getOr_associationParentRoleDescriptorDisplay() throws DbException {
    return (Boolean)get(fOr_associationParentRoleDescriptorDisplay);
  }

/**
    Gets the "association descriptor" of a DbORStyle's instance.

    @return the "association descriptor"

 **/
  public final Boolean getOr_associationDescriptorDisplay() throws DbException {
    return (Boolean)get(fOr_associationDescriptorDisplay);
  }

/**
    Gets the "uml stereotype" of a DbORStyle's instance.

    @return the "uml stereotype"

 **/
  public final Boolean getOr_umlStereotypeDisplayed() throws DbException {
    return (Boolean)get(fOr_umlStereotypeDisplayed);
  }

/**
    Gets the "uml constraint" of a DbORStyle's instance.

    @return the "uml constraint"

 **/
  public final Boolean getOr_umlConstraintDisplayed() throws DbException {
    return (Boolean)get(fOr_umlConstraintDisplayed);
  }

/**
    Gets the "uml stereotype icon" of a DbORStyle's instance.

    @return the "uml stereotype icon"

 **/
  public final Boolean getOr_umlStereotypeIconDisplayed() throws DbException {
    return (Boolean)get(fOr_umlStereotypeIconDisplayed);
  }

/**
    Gets the "show association tables as relationships" of a DbORStyle's instance.

    @return the "show association tables as relationships"

 **/
  public final Boolean getOr_associationTablesAsRelationships() throws DbException {
    return (Boolean)get(fOr_associationTablesAsRelationships);
  }

/**
    Gets the "show associations as relationships" of a DbORStyle's instance.

    @return the "show associations as relationships"

 **/
  public final Boolean getOr_associationAsRelationships() throws DbException {
    return (Boolean)get(fOr_associationAsRelationships);
  }

/**
    Gets the "show dependent tables" of a DbORStyle's instance.

    @return the "show dependent tables"

 **/
  public final Boolean getOr_showDependentTables() throws DbException {
    return (Boolean)get(fOr_showDependentTables);
  }

/**
    Gets the "unidentifying associations are dashed" of a DbORStyle's instance.

    @return the "unidentifying associations are dashed"

 **/
  public final Boolean getOr_UnidentifyingAssociationsAreDashed() throws DbException {
    return (Boolean)get(fOr_UnidentifyingAssociationsAreDashed);
  }

/**
    Gets the "association direction" of a DbORStyle's instance.

    @return the "association direction"

 **/
  public final Boolean getOr_umlAssociationDirection() throws DbException {
    return (Boolean)get(fOr_umlAssociationDirection);
  }

/**
    Gets the referringprojecter object associated to a DbORStyle's instance.

    @return the referringprojecter object

 **/
  public final DbSMSProject getReferringProjectEr() throws DbException {
    return (DbSMSProject)get(fReferringProjectEr);
  }

/**
    Gets the choice column object associated to a DbORStyle's instance.

    @return the choice column object

 **/
  public final DbtPrefix getOr_columnChoicePrefix() throws DbException {
    return (DbtPrefix)get(fOr_columnChoicePrefix);
  }

/**
    Gets the specialization column object associated to a DbORStyle's instance.

    @return the specialization column object

 **/
  public final DbtPrefix getOr_columnSpecializationPrefix() throws DbException {
    return (DbtPrefix)get(fOr_columnSpecializationPrefix);
  }

/**
    Gets the "choice/specialization background" of a DbORStyle's instance.

    @return the "choice/specialization background"

 **/
  public final Color getBackgroundColorDbORChoiceOrSpecialization() throws DbException {
    return (Color)get(fBackgroundColorDbORChoiceOrSpecialization);
  }

/**
    Gets the "choice/specialization border" of a DbORStyle's instance.

    @return the "choice/specialization border"

 **/
  public final Color getLineColorDbORChoiceOrSpecialization() throws DbException {
    return (Color)get(fLineColorDbORChoiceOrSpecialization);
  }

  public MetaClass getMetaClass() {
    return metaClass;
  }

}
