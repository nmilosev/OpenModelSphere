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
package org.modelsphere.sms.oo.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.oo.db.srtypes.*;
import org.modelsphere.sms.oo.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import java.util.HashMap;

/**
<b>Direct subclass(es)/subinterface(s) : </b>none.<br>

    <b>Composites : </b><A HREF="../../../../../org/modelsphere/sms/db/DbSMSProject.html">DbSMSProject</A>.<br>
    <b>Components : </b>none.<br>
 **/
public final class DbOOStyle extends DbSMSStyle {

  //Meta

  public static final MetaField fOojv_methodSignatureDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_methodSignatureDisplayed"));
  public static final MetaField fLineColorInterface
    = new MetaField(LocaleMgr.db.getString("lineColorInterface"));
  public static final MetaField fOojv_memberInheritedFromInterfacePrefix
    = new MetaField(LocaleMgr.db.getString("oojv_memberInheritedFromInterfacePrefix"));
  public static final MetaField fOojv_innerClassFont
    = new MetaField(LocaleMgr.db.getString("oojv_innerClassFont"));
  public static final MetaField fLineColorClass
    = new MetaField(LocaleMgr.db.getString("lineColorClass"));
  public static final MetaField fDashStyleComposition
    = new MetaField(LocaleMgr.db.getString("dashStyleComposition"));
  public static final MetaField fOojv_associationAttributeDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_associationAttributeDisplayed"));
  public static final MetaField fTextColorClass
    = new MetaField(LocaleMgr.db.getString("textColorClass"));
  public static final MetaField fOojv_classQualifiedNameDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_classQualifiedNameDisplayed"));
  public static final MetaField fHighlightAggregation
    = new MetaField(LocaleMgr.db.getString("highlightAggregation"));
  public static final MetaField fLineColorAggregation
    = new MetaField(LocaleMgr.db.getString("lineColorAggregation"));
  public static final MetaField fOojv_inheritedMethodDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_inheritedMethodDisplayed"));
  public static final MetaField fBackgroundColorInterface
    = new MetaField(LocaleMgr.db.getString("backgroundColorInterface"));
  public static final MetaField fOojv_nonFinalModifierDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_nonFinalModifierDisplayed"));
  public static final MetaField fBackgroundColorClass
    = new MetaField(LocaleMgr.db.getString("backgroundColorClass"));
  public static final MetaField fOojv_importedClassPrefix
    = new MetaField(LocaleMgr.db.getString("oojv_importedClassPrefix"));
  public static final MetaField fOojv_abstractModifierPrefix
    = new MetaField(LocaleMgr.db.getString("oojv_abstractModifierPrefix"));
  public static final MetaField fOojv_transientModifierDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_transientModifierDisplayed"));
  public static final MetaField fHighlightComposition
    = new MetaField(LocaleMgr.db.getString("highlightComposition"));
  public static final MetaField fOojv_publicVisibilityPrefix
    = new MetaField(LocaleMgr.db.getString("oojv_publicVisibilityPrefix"));
  public static final MetaField fOojv_interfaceNameFont
    = new MetaField(LocaleMgr.db.getString("oojv_interfaceNameFont"));
  public static final MetaField fOojv_inheritedAttributeDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_inheritedAttributeDisplayed"));
  public static final MetaField fOojv_nonStaticModifierDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_nonStaticModifierDisplayed"));
  public static final MetaField fOojv_associationAttributeNameLabelDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_associationAttributeNameLabelDisplayed"));
  public static final MetaField fOojv_packageModifierDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_packageModifierDisplayed"));
  public static final MetaField fDashStyleException
    = new MetaField(LocaleMgr.db.getString("dashStyleException"));
  public static final MetaField fLineColorException
    = new MetaField(LocaleMgr.db.getString("lineColorException"));
  public static final MetaField fOojv_inheritedMemberFont
    = new MetaField(LocaleMgr.db.getString("oojv_inheritedMemberFont"));
  public static final MetaField fOojv_finalModifierPrefix
    = new MetaField(LocaleMgr.db.getString("oojv_finalModifierPrefix"));
  public static final MetaField fOojv_protectedModifierDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_protectedModifierDisplayed"));
  public static final MetaField fOojv_abstractModifierDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_abstractModifierDisplayed"));
  public static final MetaField fHighlightClass
    = new MetaField(LocaleMgr.db.getString("highlightClass"));
  public static final MetaField fDashStyleAggregation
    = new MetaField(LocaleMgr.db.getString("dashStyleAggregation"));
  public static final MetaField fOojv_staticModifierDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_staticModifierDisplayed"));
  public static final MetaField fOojv_fieldFont
    = new MetaField(LocaleMgr.db.getString("oojv_fieldFont"));
  public static final MetaField fOojv_finalModifierDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_finalModifierDisplayed"));
  public static final MetaField fDashStyleClass
    = new MetaField(LocaleMgr.db.getString("dashStyleClass"));
  public static final MetaField fOojv_methodDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_methodDisplayed"));
  public static final MetaField fOojv_hidingMemberPrefix
    = new MetaField(LocaleMgr.db.getString("oojv_hidingMemberPrefix"));
  public static final MetaField fBackgroundColorException
    = new MetaField(LocaleMgr.db.getString("backgroundColorException"));
  public static final MetaField fOojv_nonTransientModifierDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_nonTransientModifierDisplayed"));
  public static final MetaField fOojv_staticModifierPrefix
    = new MetaField(LocaleMgr.db.getString("oojv_staticModifierPrefix"));
  public static final MetaField fOojv_methodFont
    = new MetaField(LocaleMgr.db.getString("oojv_methodFont"));
  public static final MetaField fTextColorException
    = new MetaField(LocaleMgr.db.getString("textColorException"));
  public static final MetaField fOojv_protectedVisibilityPrefix
    = new MetaField(LocaleMgr.db.getString("oojv_protectedVisibilityPrefix"));
  public static final MetaField fOojv_abstractClassNameFont
    = new MetaField(LocaleMgr.db.getString("oojv_abstractClassNameFont"));
  public static final MetaField fOojv_inheritedInnerClassDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_inheritedInnerClassDisplayed"));
  public static final MetaField fOojv_nonAbstractModifierDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_nonAbstractModifierDisplayed"));
  public static final MetaField fOojv_privateModifierDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_privateModifierDisplayed"));
  public static final MetaField fOojv_transientModifierPrefix
    = new MetaField(LocaleMgr.db.getString("oojv_transientModifierPrefix"));
  public static final MetaField fLineColorComposition
    = new MetaField(LocaleMgr.db.getString("lineColorComposition"));
  public static final MetaField fOojv_overridingMemberPrefix
    = new MetaField(LocaleMgr.db.getString("oojv_overridingMemberPrefix"));
  public static final MetaField fOojv_privateVisibilityPrefix
    = new MetaField(LocaleMgr.db.getString("oojv_privateVisibilityPrefix"));
  public static final MetaField fOojv_classNameFont
    = new MetaField(LocaleMgr.db.getString("oojv_classNameFont"));
  public static final MetaField fTextColorInterface
    = new MetaField(LocaleMgr.db.getString("textColorInterface"));
  public static final MetaField fHighlightException
    = new MetaField(LocaleMgr.db.getString("highlightException"));
  public static final MetaField fOojv_exceptionNameFont
    = new MetaField(LocaleMgr.db.getString("oojv_exceptionNameFont"));
  public static final MetaField fOojv_innerClassDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_innerClassDisplayed"));
  public static final MetaField fHighlightInterface
    = new MetaField(LocaleMgr.db.getString("highlightInterface"));
  public static final MetaField fOojv_publicModifierDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_publicModifierDisplayed"));
  public static final MetaField fOojv_attributeDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_attributeDisplayed"));
  public static final MetaField fDashStyleInterface
    = new MetaField(LocaleMgr.db.getString("dashStyleInterface"));
  public static final MetaField fOojv_packageVisibilityPrefix
    = new MetaField(LocaleMgr.db.getString("oojv_packageVisibilityPrefix"));
  public static final MetaField fOojv_stereotypeNameFont
    = new MetaField(LocaleMgr.db.getString("oojv_stereotypeNameFont"));
  public static final MetaField fOojv_inheritedMethodSignatureDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_inheritedMethodSignatureDisplayed"));
  public static final MetaRelation1 fReferringProjectOo
    = new MetaRelation1(LocaleMgr.db.getString("referringProjectOo"), 0);
  public static final MetaField fOojv_associationNameDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_associationNameDisplayed"));
  public static final MetaField fOojv_associationAttributeLabelFont
    = new MetaField(LocaleMgr.db.getString("oojv_associationAttributeLabelFont"));
  public static final MetaField fOojv_multiplicityFont
    = new MetaField(LocaleMgr.db.getString("oojv_multiplicityFont"));
  public static final MetaField fOojv_associationNameFont
    = new MetaField(LocaleMgr.db.getString("oojv_associationNameFont"));
  public static final MetaField fOojv_umlForceItalicOnAbstract
    = new MetaField(LocaleMgr.db.getString("oojv_umlForceItalicOnAbstract"));
  public static final MetaField fOojv_umlConstraintDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_umlConstraintDisplayed"));
  public static final MetaField fOojv_umlStereotypeDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_umlStereotypeDisplayed"));
  public static final MetaField fOojv_umlForceUnderlineOnStatic
    = new MetaField(LocaleMgr.db.getString("oojv_umlForceUnderlineOnStatic"));
  public static final MetaField fOojv_umlStereotypeIconDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_umlStereotypeIconDisplayed"));
  public static final MetaField fOojv_umlTypeBeforeName
    = new MetaField(LocaleMgr.db.getString("oojv_umlTypeBeforeName"));
  public static final MetaField fHighlightRealizationInheritance
    = new MetaField(LocaleMgr.db.getString("highlightRealizationInheritance"));
  public static final MetaField fDashStyleRealizationInheritance
    = new MetaField(LocaleMgr.db.getString("dashStyleRealizationInheritance"));
  public static final MetaField fOojv_umlInterfaceShownAsCircle
    = new MetaField(LocaleMgr.db.getString("oojv_umlInterfaceShownAsCircle"));
  public static final MetaField fOojv_associationAttributeTypeLabelDisplayed
    = new MetaField(LocaleMgr.db.getString("oojv_associationAttributeTypeLabelDisplayed"));
  public static final MetaField fOojv_showOnlyStereotypeIcon
    = new MetaField(LocaleMgr.db.getString("oojv_showOnlyStereotypeIcon"));
  public static final MetaField fOojv_umlAssociationDirection
    = new MetaField(LocaleMgr.db.getString("oojv_umlAssociationDirection"));
  public static final MetaField fOojv_hideParameterNamesInSignatures
    = new MetaField(LocaleMgr.db.getString("oojv_hideParameterNamesInSignatures"));

  public static final MetaClass metaClass = new MetaClass(
    LocaleMgr.db.getString("DbOOStyle"), DbOOStyle.class,
    new MetaField[] {fOojv_methodSignatureDisplayed,
      fLineColorInterface,
      fOojv_memberInheritedFromInterfacePrefix,
      fOojv_innerClassFont,
      fLineColorClass,
      fDashStyleComposition,
      fOojv_associationAttributeDisplayed,
      fTextColorClass,
      fOojv_classQualifiedNameDisplayed,
      fHighlightAggregation,
      fLineColorAggregation,
      fOojv_inheritedMethodDisplayed,
      fBackgroundColorInterface,
      fOojv_nonFinalModifierDisplayed,
      fBackgroundColorClass,
      fOojv_importedClassPrefix,
      fOojv_abstractModifierPrefix,
      fOojv_transientModifierDisplayed,
      fHighlightComposition,
      fOojv_publicVisibilityPrefix,
      fOojv_interfaceNameFont,
      fOojv_inheritedAttributeDisplayed,
      fOojv_nonStaticModifierDisplayed,
      fOojv_associationAttributeNameLabelDisplayed,
      fOojv_packageModifierDisplayed,
      fDashStyleException,
      fLineColorException,
      fOojv_inheritedMemberFont,
      fOojv_finalModifierPrefix,
      fOojv_protectedModifierDisplayed,
      fOojv_abstractModifierDisplayed,
      fHighlightClass,
      fDashStyleAggregation,
      fOojv_staticModifierDisplayed,
      fOojv_fieldFont,
      fOojv_finalModifierDisplayed,
      fDashStyleClass,
      fOojv_methodDisplayed,
      fOojv_hidingMemberPrefix,
      fBackgroundColorException,
      fOojv_nonTransientModifierDisplayed,
      fOojv_staticModifierPrefix,
      fOojv_methodFont,
      fTextColorException,
      fOojv_protectedVisibilityPrefix,
      fOojv_abstractClassNameFont,
      fOojv_inheritedInnerClassDisplayed,
      fOojv_nonAbstractModifierDisplayed,
      fOojv_privateModifierDisplayed,
      fOojv_transientModifierPrefix,
      fLineColorComposition,
      fOojv_overridingMemberPrefix,
      fOojv_privateVisibilityPrefix,
      fOojv_classNameFont,
      fTextColorInterface,
      fHighlightException,
      fOojv_exceptionNameFont,
      fOojv_innerClassDisplayed,
      fHighlightInterface,
      fOojv_publicModifierDisplayed,
      fOojv_attributeDisplayed,
      fDashStyleInterface,
      fOojv_packageVisibilityPrefix,
      fOojv_stereotypeNameFont,
      fOojv_inheritedMethodSignatureDisplayed,
      fReferringProjectOo,
      fOojv_associationNameDisplayed,
      fOojv_associationAttributeLabelFont,
      fOojv_multiplicityFont,
      fOojv_associationNameFont,
      fOojv_umlForceItalicOnAbstract,
      fOojv_umlConstraintDisplayed,
      fOojv_umlStereotypeDisplayed,
      fOojv_umlForceUnderlineOnStatic,
      fOojv_umlStereotypeIconDisplayed,
      fOojv_umlTypeBeforeName,
      fHighlightRealizationInheritance,
      fDashStyleRealizationInheritance,
      fOojv_umlInterfaceShownAsCircle,
      fOojv_associationAttributeTypeLabelDisplayed,
      fOojv_showOnlyStereotypeIcon,
      fOojv_umlAssociationDirection,
      fOojv_hideParameterNamesInSignatures}, MetaClass.MATCHABLE | MetaClass.NO_UDF);

/**
    For internal use only.
 **/
  public static void initMeta() {
    try {
      metaClass.setSuperMetaClass(DbSMSStyle.metaClass);

      fOojv_methodSignatureDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_methodSignatureDisplayed"));
      fLineColorInterface.setJField(DbOOStyle.class.getDeclaredField("m_lineColorInterface"));
      fOojv_memberInheritedFromInterfacePrefix.setJField(DbOOStyle.class.getDeclaredField("m_oojv_memberInheritedFromInterfacePrefix"));
      fOojv_innerClassFont.setJField(DbOOStyle.class.getDeclaredField("m_oojv_innerClassFont"));
      fLineColorClass.setJField(DbOOStyle.class.getDeclaredField("m_lineColorClass"));
      fDashStyleComposition.setJField(DbOOStyle.class.getDeclaredField("m_dashStyleComposition"));
      fOojv_associationAttributeDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_associationAttributeDisplayed"));
      fTextColorClass.setJField(DbOOStyle.class.getDeclaredField("m_textColorClass"));
      fOojv_classQualifiedNameDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_classQualifiedNameDisplayed"));
      fHighlightAggregation.setJField(DbOOStyle.class.getDeclaredField("m_highlightAggregation"));
      fLineColorAggregation.setJField(DbOOStyle.class.getDeclaredField("m_lineColorAggregation"));
      fOojv_inheritedMethodDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_inheritedMethodDisplayed"));
      fBackgroundColorInterface.setJField(DbOOStyle.class.getDeclaredField("m_backgroundColorInterface"));
      fOojv_nonFinalModifierDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_nonFinalModifierDisplayed"));
      fBackgroundColorClass.setJField(DbOOStyle.class.getDeclaredField("m_backgroundColorClass"));
      fOojv_importedClassPrefix.setJField(DbOOStyle.class.getDeclaredField("m_oojv_importedClassPrefix"));
      fOojv_abstractModifierPrefix.setJField(DbOOStyle.class.getDeclaredField("m_oojv_abstractModifierPrefix"));
      fOojv_transientModifierDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_transientModifierDisplayed"));
      fHighlightComposition.setJField(DbOOStyle.class.getDeclaredField("m_highlightComposition"));
      fOojv_publicVisibilityPrefix.setJField(DbOOStyle.class.getDeclaredField("m_oojv_publicVisibilityPrefix"));
      fOojv_interfaceNameFont.setJField(DbOOStyle.class.getDeclaredField("m_oojv_interfaceNameFont"));
      fOojv_inheritedAttributeDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_inheritedAttributeDisplayed"));
      fOojv_nonStaticModifierDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_nonStaticModifierDisplayed"));
      fOojv_associationAttributeNameLabelDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_associationAttributeNameLabelDisplayed"));
      fOojv_packageModifierDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_packageModifierDisplayed"));
      fDashStyleException.setJField(DbOOStyle.class.getDeclaredField("m_dashStyleException"));
      fLineColorException.setJField(DbOOStyle.class.getDeclaredField("m_lineColorException"));
      fOojv_inheritedMemberFont.setJField(DbOOStyle.class.getDeclaredField("m_oojv_inheritedMemberFont"));
      fOojv_finalModifierPrefix.setJField(DbOOStyle.class.getDeclaredField("m_oojv_finalModifierPrefix"));
      fOojv_protectedModifierDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_protectedModifierDisplayed"));
      fOojv_abstractModifierDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_abstractModifierDisplayed"));
      fHighlightClass.setJField(DbOOStyle.class.getDeclaredField("m_highlightClass"));
      fDashStyleAggregation.setJField(DbOOStyle.class.getDeclaredField("m_dashStyleAggregation"));
      fOojv_staticModifierDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_staticModifierDisplayed"));
      fOojv_fieldFont.setJField(DbOOStyle.class.getDeclaredField("m_oojv_fieldFont"));
      fOojv_finalModifierDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_finalModifierDisplayed"));
      fDashStyleClass.setJField(DbOOStyle.class.getDeclaredField("m_dashStyleClass"));
      fOojv_methodDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_methodDisplayed"));
      fOojv_hidingMemberPrefix.setJField(DbOOStyle.class.getDeclaredField("m_oojv_hidingMemberPrefix"));
      fBackgroundColorException.setJField(DbOOStyle.class.getDeclaredField("m_backgroundColorException"));
      fOojv_nonTransientModifierDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_nonTransientModifierDisplayed"));
      fOojv_staticModifierPrefix.setJField(DbOOStyle.class.getDeclaredField("m_oojv_staticModifierPrefix"));
      fOojv_methodFont.setJField(DbOOStyle.class.getDeclaredField("m_oojv_methodFont"));
      fTextColorException.setJField(DbOOStyle.class.getDeclaredField("m_textColorException"));
      fOojv_protectedVisibilityPrefix.setJField(DbOOStyle.class.getDeclaredField("m_oojv_protectedVisibilityPrefix"));
      fOojv_abstractClassNameFont.setJField(DbOOStyle.class.getDeclaredField("m_oojv_abstractClassNameFont"));
      fOojv_inheritedInnerClassDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_inheritedInnerClassDisplayed"));
      fOojv_nonAbstractModifierDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_nonAbstractModifierDisplayed"));
      fOojv_privateModifierDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_privateModifierDisplayed"));
      fOojv_transientModifierPrefix.setJField(DbOOStyle.class.getDeclaredField("m_oojv_transientModifierPrefix"));
      fLineColorComposition.setJField(DbOOStyle.class.getDeclaredField("m_lineColorComposition"));
      fOojv_overridingMemberPrefix.setJField(DbOOStyle.class.getDeclaredField("m_oojv_overridingMemberPrefix"));
      fOojv_privateVisibilityPrefix.setJField(DbOOStyle.class.getDeclaredField("m_oojv_privateVisibilityPrefix"));
      fOojv_classNameFont.setJField(DbOOStyle.class.getDeclaredField("m_oojv_classNameFont"));
      fTextColorInterface.setJField(DbOOStyle.class.getDeclaredField("m_textColorInterface"));
      fHighlightException.setJField(DbOOStyle.class.getDeclaredField("m_highlightException"));
      fOojv_exceptionNameFont.setJField(DbOOStyle.class.getDeclaredField("m_oojv_exceptionNameFont"));
      fOojv_innerClassDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_innerClassDisplayed"));
      fHighlightInterface.setJField(DbOOStyle.class.getDeclaredField("m_highlightInterface"));
      fOojv_publicModifierDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_publicModifierDisplayed"));
      fOojv_attributeDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_attributeDisplayed"));
      fDashStyleInterface.setJField(DbOOStyle.class.getDeclaredField("m_dashStyleInterface"));
      fOojv_packageVisibilityPrefix.setJField(DbOOStyle.class.getDeclaredField("m_oojv_packageVisibilityPrefix"));
      fOojv_stereotypeNameFont.setJField(DbOOStyle.class.getDeclaredField("m_oojv_stereotypeNameFont"));
      fOojv_inheritedMethodSignatureDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_inheritedMethodSignatureDisplayed"));
      fReferringProjectOo.setJField(DbOOStyle.class.getDeclaredField("m_referringProjectOo"));
      fOojv_associationNameDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_associationNameDisplayed"));
      fOojv_associationAttributeLabelFont.setJField(DbOOStyle.class.getDeclaredField("m_oojv_associationAttributeLabelFont"));
      fOojv_multiplicityFont.setJField(DbOOStyle.class.getDeclaredField("m_oojv_multiplicityFont"));
      fOojv_associationNameFont.setJField(DbOOStyle.class.getDeclaredField("m_oojv_associationNameFont"));
      fOojv_umlForceItalicOnAbstract.setJField(DbOOStyle.class.getDeclaredField("m_oojv_umlForceItalicOnAbstract"));
      fOojv_umlConstraintDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_umlConstraintDisplayed"));
      fOojv_umlStereotypeDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_umlStereotypeDisplayed"));
      fOojv_umlForceUnderlineOnStatic.setJField(DbOOStyle.class.getDeclaredField("m_oojv_umlForceUnderlineOnStatic"));
      fOojv_umlStereotypeIconDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_umlStereotypeIconDisplayed"));
      fOojv_umlTypeBeforeName.setJField(DbOOStyle.class.getDeclaredField("m_oojv_umlTypeBeforeName"));
      fHighlightRealizationInheritance.setJField(DbOOStyle.class.getDeclaredField("m_highlightRealizationInheritance"));
      fDashStyleRealizationInheritance.setJField(DbOOStyle.class.getDeclaredField("m_dashStyleRealizationInheritance"));
      fOojv_umlInterfaceShownAsCircle.setJField(DbOOStyle.class.getDeclaredField("m_oojv_umlInterfaceShownAsCircle"));
      fOojv_associationAttributeTypeLabelDisplayed.setJField(DbOOStyle.class.getDeclaredField("m_oojv_associationAttributeTypeLabelDisplayed"));
      fOojv_showOnlyStereotypeIcon.setJField(DbOOStyle.class.getDeclaredField("m_oojv_showOnlyStereotypeIcon"));
      fOojv_umlAssociationDirection.setJField(DbOOStyle.class.getDeclaredField("m_oojv_umlAssociationDirection"));
      fOojv_hideParameterNamesInSignatures.setJField(DbOOStyle.class.getDeclaredField("m_oojv_hideParameterNamesInSignatures"));

      fReferringProjectOo.setOppositeRel(DbSMSProject.fOoDefaultStyle);

    }
    catch (Exception e) { throw new RuntimeException("Meta init"); }
  }

  static final long serialVersionUID = 0;
  private static HashMap fieldMap;
  public static MetaField[] oojv_displayOptions;
  public static MetaField[] oojv_fontOptions;
  public static MetaField[] oojv_prefixOptions;
  public static Font[] oojv_fontOptionDefaultValues;
  public static String[] oojv_optionGroupHeaders;
  public static MetaField[] oojv_modifierDisplayOptions;
  public static MetaField[] oojv_colorOptions;
  public static MetaField[] oojv_lineOptions;
  public static MetaField[][] oojv_optionGroups;
  public static Object[][] oojv_optionValueGroups;
  public static Boolean[] oojv_lineOptionDefaultValues;
  public static Color[] oojv_colorOptionDefaultValues;
  public static Boolean[] oojv_displayOptionDefaultValues;
  public static String[] oojv_listOptionTabs;
  public static Boolean[] oojv_modifierDisplayOptionDefaultValues;
  public static DbtPrefix[] oojv_prefixOptionDefaultValues;
  public static String DEFAULT_STYLE_NAME = LocaleMgr.misc.getString( "DefaultOOStyle");
  public static Boolean[] oojv_advancedDisplayOptionDefaultValues;
  public static MetaField[] oojv_advancedDisplayOptions;

  static {
    org.modelsphere.sms.db.util.DbInitialization.initOoStyle();
  }

  //Instance variables
  SrBoolean m_oojv_methodSignatureDisplayed;
  SrColor m_lineColorInterface;
  DbtPrefix m_oojv_memberInheritedFromInterfacePrefix;
  SrFont m_oojv_innerClassFont;
  SrColor m_lineColorClass;
  SrBoolean m_dashStyleComposition;
  SrBoolean m_oojv_associationAttributeDisplayed;
  SrColor m_textColorClass;
  SrBoolean m_oojv_classQualifiedNameDisplayed;
  SrBoolean m_highlightAggregation;
  SrColor m_lineColorAggregation;
  SrBoolean m_oojv_inheritedMethodDisplayed;
  SrColor m_backgroundColorInterface;
  SrBoolean m_oojv_nonFinalModifierDisplayed;
  SrColor m_backgroundColorClass;
  DbtPrefix m_oojv_importedClassPrefix;
  DbtPrefix m_oojv_abstractModifierPrefix;
  SrBoolean m_oojv_transientModifierDisplayed;
  SrBoolean m_highlightComposition;
  DbtPrefix m_oojv_publicVisibilityPrefix;
  SrFont m_oojv_interfaceNameFont;
  SrBoolean m_oojv_inheritedAttributeDisplayed;
  SrBoolean m_oojv_nonStaticModifierDisplayed;
  SrBoolean m_oojv_associationAttributeNameLabelDisplayed;
  SrBoolean m_oojv_packageModifierDisplayed;
  SrBoolean m_dashStyleException;
  SrColor m_lineColorException;
  SrFont m_oojv_inheritedMemberFont;
  DbtPrefix m_oojv_finalModifierPrefix;
  SrBoolean m_oojv_protectedModifierDisplayed;
  SrBoolean m_oojv_abstractModifierDisplayed;
  SrBoolean m_highlightClass;
  SrBoolean m_dashStyleAggregation;
  SrBoolean m_oojv_staticModifierDisplayed;
  SrFont m_oojv_fieldFont;
  SrBoolean m_oojv_finalModifierDisplayed;
  SrBoolean m_dashStyleClass;
  SrBoolean m_oojv_methodDisplayed;
  DbtPrefix m_oojv_hidingMemberPrefix;
  SrColor m_backgroundColorException;
  SrBoolean m_oojv_nonTransientModifierDisplayed;
  DbtPrefix m_oojv_staticModifierPrefix;
  SrFont m_oojv_methodFont;
  SrColor m_textColorException;
  DbtPrefix m_oojv_protectedVisibilityPrefix;
  SrFont m_oojv_abstractClassNameFont;
  SrBoolean m_oojv_inheritedInnerClassDisplayed;
  SrBoolean m_oojv_nonAbstractModifierDisplayed;
  SrBoolean m_oojv_privateModifierDisplayed;
  DbtPrefix m_oojv_transientModifierPrefix;
  SrColor m_lineColorComposition;
  DbtPrefix m_oojv_overridingMemberPrefix;
  DbtPrefix m_oojv_privateVisibilityPrefix;
  SrFont m_oojv_classNameFont;
  SrColor m_textColorInterface;
  SrBoolean m_highlightException;
  SrFont m_oojv_exceptionNameFont;
  SrBoolean m_oojv_innerClassDisplayed;
  SrBoolean m_highlightInterface;
  SrBoolean m_oojv_publicModifierDisplayed;
  SrBoolean m_oojv_attributeDisplayed;
  SrBoolean m_dashStyleInterface;
  DbtPrefix m_oojv_packageVisibilityPrefix;
  SrFont m_oojv_stereotypeNameFont;
  SrBoolean m_oojv_inheritedMethodSignatureDisplayed;
  DbSMSProject m_referringProjectOo;
  SrBoolean m_oojv_associationNameDisplayed;
  SrFont m_oojv_associationAttributeLabelFont;
  SrFont m_oojv_multiplicityFont;
  SrFont m_oojv_associationNameFont;
  SrBoolean m_oojv_umlForceItalicOnAbstract;
  SrBoolean m_oojv_umlConstraintDisplayed;
  SrBoolean m_oojv_umlStereotypeDisplayed;
  SrBoolean m_oojv_umlForceUnderlineOnStatic;
  SrBoolean m_oojv_umlStereotypeIconDisplayed;
  SrBoolean m_oojv_umlTypeBeforeName;
  SrBoolean m_highlightRealizationInheritance;
  SrBoolean m_dashStyleRealizationInheritance;
  SrBoolean m_oojv_umlInterfaceShownAsCircle;
  SrBoolean m_oojv_associationAttributeTypeLabelDisplayed;
  SrBoolean m_oojv_showOnlyStereotypeIcon;
  SrBoolean m_oojv_umlAssociationDirection;
  SrBoolean m_oojv_hideParameterNamesInSignatures;

  //Constructors

/**
    Parameter-less constructor.  Required by Java Beans Conventions.
 **/
  public DbOOStyle() {}

/**
    Creates an instance of DbOOStyle.
    @param composite the object which will contain the newly-created instance
 **/
  public DbOOStyle(DbObject composite) throws DbException {
    super(composite);
    setDefaultInitialValues();
  }

  private void setDefaultInitialValues() throws DbException {
  }

/**

    @param srcstyle org.modelsphere.jack.baseDb.db.DbObject
 **/
  public void copyOptions(DbObject srcStyle) throws DbException {
    for (int i = 0;  i < oojv_optionGroups.length;  i++) {
      for (int j = 0;  j < oojv_optionGroups[i].length;  j++) {
        MetaField metaField = oojv_optionGroups[i][j];
        basicSet(metaField, ((DbSMSStyle)srcStyle).find(metaField));
      }
    }
  }

/**

    @param name java.lang.String
    @return metafield
 **/
  public MetaField getMetaField(String name) {
    if (fieldMap == null) {
      MetaField[] fields = metaClass.getAllMetaFields();
      fieldMap = new HashMap(fields.length + fields.length/2);
      for (int i = 0;  i < fields.length;  i++)
        fieldMap.put(fields[i].getJName(), fields[i]);
    }
    return (MetaField)fieldMap.get(name);
  }

/**

 **/
  public void initOptions() throws DbException {
    for (int i = 0;  i < oojv_optionGroups.length;  i++) {
      for (int j = 0;  j < oojv_optionGroups[i].length;  j++) {
        basicSet(oojv_optionGroups[i][j], oojv_optionValueGroups[i][j]);
      }
    }
  }

/**

 **/
  public void initNullOptions() throws DbException {
    if (getAncestor() != null)
      return;
    for (int i = 0;  i < oojv_optionGroups.length;  i++) {
      for (int j = 0;  j < oojv_optionGroups[i].length;  j++) {
        if (get(oojv_optionGroups[i][j]) == null)
          basicSet(oojv_optionGroups[i][j], oojv_optionValueGroups[i][j]);
      }
    }
  }

  //Setters

/**
    Sets the "method signature" property of a DbOOStyle's instance.

    @param value the "method signature" property

 **/
  public final void setOojv_methodSignatureDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_methodSignatureDisplayed, value);
  }

/**
    Sets the "interface border" property of a DbOOStyle's instance.

    @param value the "interface border" property

 **/
  public final void setLineColorInterface(Color value) throws DbException {
    basicSet(fLineColorInterface, value);
  }

/**
    Sets the member inherited from interface object associated to a DbOOStyle's instance.

    @param value the member inherited from interface object to be associated

 **/
  public final void setOojv_memberInheritedFromInterfacePrefix(DbtPrefix value) throws DbException {
    basicSet(fOojv_memberInheritedFromInterfacePrefix, value);
  }

/**
    Sets the "inner class" property of a DbOOStyle's instance.

    @param value the "inner class" property

 **/
  public final void setOojv_innerClassFont(Font value) throws DbException {
    basicSet(fOojv_innerClassFont, value);
  }

/**
    Sets the "class border" property of a DbOOStyle's instance.

    @param value the "class border" property

 **/
  public final void setLineColorClass(Color value) throws DbException {
    basicSet(fLineColorClass, value);
  }

/**
    Sets the "composition dash style" property of a DbOOStyle's instance.

    @param value the "composition dash style" property

 **/
  public final void setDashStyleComposition(Boolean value) throws DbException {
    basicSet(fDashStyleComposition, value);
  }

/**
    Sets the "association field" property of a DbOOStyle's instance.

    @param value the "association field" property

 **/
  public final void setOojv_associationAttributeDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_associationAttributeDisplayed, value);
  }

/**
    Sets the "class text" property of a DbOOStyle's instance.

    @param value the "class text" property

 **/
  public final void setTextColorClass(Color value) throws DbException {
    basicSet(fTextColorClass, value);
  }

/**
    Sets the "class fully qualified name" property of a DbOOStyle's instance.

    @param value the "class fully qualified name" property

 **/
  public final void setOojv_classQualifiedNameDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_classQualifiedNameDisplayed, value);
  }

/**
    Sets the "aggregation highlight" property of a DbOOStyle's instance.

    @param value the "aggregation highlight" property

 **/
  public final void setHighlightAggregation(Boolean value) throws DbException {
    basicSet(fHighlightAggregation, value);
  }

/**
    Sets the "aggregation" property of a DbOOStyle's instance.

    @param value the "aggregation" property

 **/
  public final void setLineColorAggregation(Color value) throws DbException {
    basicSet(fLineColorAggregation, value);
  }

/**
    Sets the "inherited method" property of a DbOOStyle's instance.

    @param value the "inherited method" property

 **/
  public final void setOojv_inheritedMethodDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_inheritedMethodDisplayed, value);
  }

/**
    Sets the "interface background" property of a DbOOStyle's instance.

    @param value the "interface background" property

 **/
  public final void setBackgroundColorInterface(Color value) throws DbException {
    basicSet(fBackgroundColorInterface, value);
  }

/**
    Sets the "non final" property of a DbOOStyle's instance.

    @param value the "non final" property

 **/
  public final void setOojv_nonFinalModifierDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_nonFinalModifierDisplayed, value);
  }

/**
    Sets the "class background" property of a DbOOStyle's instance.

    @param value the "class background" property

 **/
  public final void setBackgroundColorClass(Color value) throws DbException {
    basicSet(fBackgroundColorClass, value);
  }

/**
    Sets the imported class object associated to a DbOOStyle's instance.

    @param value the imported class object to be associated

 **/
  public final void setOojv_importedClassPrefix(DbtPrefix value) throws DbException {
    basicSet(fOojv_importedClassPrefix, value);
  }

/**
    Sets the abstract modifier object associated to a DbOOStyle's instance.

    @param value the abstract modifier object to be associated

 **/
  public final void setOojv_abstractModifierPrefix(DbtPrefix value) throws DbException {
    basicSet(fOojv_abstractModifierPrefix, value);
  }

/**
    Sets the "transient" property of a DbOOStyle's instance.

    @param value the "transient" property

 **/
  public final void setOojv_transientModifierDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_transientModifierDisplayed, value);
  }

/**
    Sets the "composition highlight" property of a DbOOStyle's instance.

    @param value the "composition highlight" property

 **/
  public final void setHighlightComposition(Boolean value) throws DbException {
    basicSet(fHighlightComposition, value);
  }

/**
    Sets the public visibility object associated to a DbOOStyle's instance.

    @param value the public visibility object to be associated

 **/
  public final void setOojv_publicVisibilityPrefix(DbtPrefix value) throws DbException {
    basicSet(fOojv_publicVisibilityPrefix, value);
  }

/**
    Sets the "interface name" property of a DbOOStyle's instance.

    @param value the "interface name" property

 **/
  public final void setOojv_interfaceNameFont(Font value) throws DbException {
    basicSet(fOojv_interfaceNameFont, value);
  }

/**
    Sets the "inherited field" property of a DbOOStyle's instance.

    @param value the "inherited field" property

 **/
  public final void setOojv_inheritedAttributeDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_inheritedAttributeDisplayed, value);
  }

/**
    Sets the "non static" property of a DbOOStyle's instance.

    @param value the "non static" property

 **/
  public final void setOojv_nonStaticModifierDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_nonStaticModifierDisplayed, value);
  }

/**
    Sets the "association name field label" property of a DbOOStyle's instance.

    @param value the "association name field label" property

 **/
  public final void setOojv_associationAttributeNameLabelDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_associationAttributeNameLabelDisplayed, value);
  }

/**
    Sets the "default access" property of a DbOOStyle's instance.

    @param value the "default access" property

 **/
  public final void setOojv_packageModifierDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_packageModifierDisplayed, value);
  }

/**
    Sets the "exception dash style" property of a DbOOStyle's instance.

    @param value the "exception dash style" property

 **/
  public final void setDashStyleException(Boolean value) throws DbException {
    basicSet(fDashStyleException, value);
  }

/**
    Sets the "exception border" property of a DbOOStyle's instance.

    @param value the "exception border" property

 **/
  public final void setLineColorException(Color value) throws DbException {
    basicSet(fLineColorException, value);
  }

/**
    Sets the "inherited member" property of a DbOOStyle's instance.

    @param value the "inherited member" property

 **/
  public final void setOojv_inheritedMemberFont(Font value) throws DbException {
    basicSet(fOojv_inheritedMemberFont, value);
  }

/**
    Sets the final modifier object associated to a DbOOStyle's instance.

    @param value the final modifier object to be associated

 **/
  public final void setOojv_finalModifierPrefix(DbtPrefix value) throws DbException {
    basicSet(fOojv_finalModifierPrefix, value);
  }

/**
    Sets the "protected" property of a DbOOStyle's instance.

    @param value the "protected" property

 **/
  public final void setOojv_protectedModifierDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_protectedModifierDisplayed, value);
  }

/**
    Sets the "abstract" property of a DbOOStyle's instance.

    @param value the "abstract" property

 **/
  public final void setOojv_abstractModifierDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_abstractModifierDisplayed, value);
  }

/**
    Sets the "class highlight" property of a DbOOStyle's instance.

    @param value the "class highlight" property

 **/
  public final void setHighlightClass(Boolean value) throws DbException {
    basicSet(fHighlightClass, value);
  }

/**
    Sets the "aggregation dash style" property of a DbOOStyle's instance.

    @param value the "aggregation dash style" property

 **/
  public final void setDashStyleAggregation(Boolean value) throws DbException {
    basicSet(fDashStyleAggregation, value);
  }

/**
    Sets the "static" property of a DbOOStyle's instance.

    @param value the "static" property

 **/
  public final void setOojv_staticModifierDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_staticModifierDisplayed, value);
  }

/**
    Sets the "field" property of a DbOOStyle's instance.

    @param value the "field" property

 **/
  public final void setOojv_fieldFont(Font value) throws DbException {
    basicSet(fOojv_fieldFont, value);
  }

/**
    Sets the "final" property of a DbOOStyle's instance.

    @param value the "final" property

 **/
  public final void setOojv_finalModifierDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_finalModifierDisplayed, value);
  }

/**
    Sets the "class dash style" property of a DbOOStyle's instance.

    @param value the "class dash style" property

 **/
  public final void setDashStyleClass(Boolean value) throws DbException {
    basicSet(fDashStyleClass, value);
  }

/**
    Sets the "method" property of a DbOOStyle's instance.

    @param value the "method" property

 **/
  public final void setOojv_methodDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_methodDisplayed, value);
  }

/**
    Sets the hiding member object associated to a DbOOStyle's instance.

    @param value the hiding member object to be associated

 **/
  public final void setOojv_hidingMemberPrefix(DbtPrefix value) throws DbException {
    basicSet(fOojv_hidingMemberPrefix, value);
  }

/**
    Sets the "exception background" property of a DbOOStyle's instance.

    @param value the "exception background" property

 **/
  public final void setBackgroundColorException(Color value) throws DbException {
    basicSet(fBackgroundColorException, value);
  }

/**
    Sets the "non transient" property of a DbOOStyle's instance.

    @param value the "non transient" property

 **/
  public final void setOojv_nonTransientModifierDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_nonTransientModifierDisplayed, value);
  }

/**
    Sets the static modifier object associated to a DbOOStyle's instance.

    @param value the static modifier object to be associated

 **/
  public final void setOojv_staticModifierPrefix(DbtPrefix value) throws DbException {
    basicSet(fOojv_staticModifierPrefix, value);
  }

/**
    Sets the "method" property of a DbOOStyle's instance.

    @param value the "method" property

 **/
  public final void setOojv_methodFont(Font value) throws DbException {
    basicSet(fOojv_methodFont, value);
  }

/**
    Sets the "exception text" property of a DbOOStyle's instance.

    @param value the "exception text" property

 **/
  public final void setTextColorException(Color value) throws DbException {
    basicSet(fTextColorException, value);
  }

/**
    Sets the protected visibility object associated to a DbOOStyle's instance.

    @param value the protected visibility object to be associated

 **/
  public final void setOojv_protectedVisibilityPrefix(DbtPrefix value) throws DbException {
    basicSet(fOojv_protectedVisibilityPrefix, value);
  }

/**
    Sets the "abstract class name" property of a DbOOStyle's instance.

    @param value the "abstract class name" property

 **/
  public final void setOojv_abstractClassNameFont(Font value) throws DbException {
    basicSet(fOojv_abstractClassNameFont, value);
  }

/**
    Sets the "inherited inner class" property of a DbOOStyle's instance.

    @param value the "inherited inner class" property

 **/
  public final void setOojv_inheritedInnerClassDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_inheritedInnerClassDisplayed, value);
  }

/**
    Sets the "non abstract" property of a DbOOStyle's instance.

    @param value the "non abstract" property

 **/
  public final void setOojv_nonAbstractModifierDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_nonAbstractModifierDisplayed, value);
  }

/**
    Sets the "private" property of a DbOOStyle's instance.

    @param value the "private" property

 **/
  public final void setOojv_privateModifierDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_privateModifierDisplayed, value);
  }

/**
    Sets the transient modifier object associated to a DbOOStyle's instance.

    @param value the transient modifier object to be associated

 **/
  public final void setOojv_transientModifierPrefix(DbtPrefix value) throws DbException {
    basicSet(fOojv_transientModifierPrefix, value);
  }

/**
    Sets the "composition" property of a DbOOStyle's instance.

    @param value the "composition" property

 **/
  public final void setLineColorComposition(Color value) throws DbException {
    basicSet(fLineColorComposition, value);
  }

/**
    Sets the overriding member object associated to a DbOOStyle's instance.

    @param value the overriding member object to be associated

 **/
  public final void setOojv_overridingMemberPrefix(DbtPrefix value) throws DbException {
    basicSet(fOojv_overridingMemberPrefix, value);
  }

/**
    Sets the private visibility object associated to a DbOOStyle's instance.

    @param value the private visibility object to be associated

 **/
  public final void setOojv_privateVisibilityPrefix(DbtPrefix value) throws DbException {
    basicSet(fOojv_privateVisibilityPrefix, value);
  }

/**
    Sets the "class name" property of a DbOOStyle's instance.

    @param value the "class name" property

 **/
  public final void setOojv_classNameFont(Font value) throws DbException {
    basicSet(fOojv_classNameFont, value);
  }

/**
    Sets the "interface text" property of a DbOOStyle's instance.

    @param value the "interface text" property

 **/
  public final void setTextColorInterface(Color value) throws DbException {
    basicSet(fTextColorInterface, value);
  }

/**
    Sets the "exception highlight" property of a DbOOStyle's instance.

    @param value the "exception highlight" property

 **/
  public final void setHighlightException(Boolean value) throws DbException {
    basicSet(fHighlightException, value);
  }

/**
    Sets the "exception name" property of a DbOOStyle's instance.

    @param value the "exception name" property

 **/
  public final void setOojv_exceptionNameFont(Font value) throws DbException {
    basicSet(fOojv_exceptionNameFont, value);
  }

/**
    Sets the "inner class" property of a DbOOStyle's instance.

    @param value the "inner class" property

 **/
  public final void setOojv_innerClassDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_innerClassDisplayed, value);
  }

/**
    Sets the "interface highlight" property of a DbOOStyle's instance.

    @param value the "interface highlight" property

 **/
  public final void setHighlightInterface(Boolean value) throws DbException {
    basicSet(fHighlightInterface, value);
  }

/**
    Sets the "public" property of a DbOOStyle's instance.

    @param value the "public" property

 **/
  public final void setOojv_publicModifierDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_publicModifierDisplayed, value);
  }

/**
    Sets the "field" property of a DbOOStyle's instance.

    @param value the "field" property

 **/
  public final void setOojv_attributeDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_attributeDisplayed, value);
  }

/**
    Sets the "interface dash style" property of a DbOOStyle's instance.

    @param value the "interface dash style" property

 **/
  public final void setDashStyleInterface(Boolean value) throws DbException {
    basicSet(fDashStyleInterface, value);
  }

/**
    Sets the default access visibility object associated to a DbOOStyle's instance.

    @param value the default access visibility object to be associated

 **/
  public final void setOojv_packageVisibilityPrefix(DbtPrefix value) throws DbException {
    basicSet(fOojv_packageVisibilityPrefix, value);
  }

/**
    Sets the "stereotype name" property of a DbOOStyle's instance.

    @param value the "stereotype name" property

 **/
  public final void setOojv_stereotypeNameFont(Font value) throws DbException {
    basicSet(fOojv_stereotypeNameFont, value);
  }

/**
    Sets the "inherited method signature" property of a DbOOStyle's instance.

    @param value the "inherited method signature" property

 **/
  public final void setOojv_inheritedMethodSignatureDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_inheritedMethodSignatureDisplayed, value);
  }

/**
    Sets the project object associated to a DbOOStyle's instance.

    @param value the project object to be associated

 **/
  public final void setReferringProjectOo(DbSMSProject value) throws DbException {
    basicSet(fReferringProjectOo, value);
  }

/**
    Sets the "association name" property of a DbOOStyle's instance.

    @param value the "association name" property

 **/
  public final void setOojv_associationNameDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_associationNameDisplayed, value);
  }

/**
    Sets the "association field label" property of a DbOOStyle's instance.

    @param value the "association field label" property

 **/
  public final void setOojv_associationAttributeLabelFont(Font value) throws DbException {
    basicSet(fOojv_associationAttributeLabelFont, value);
  }

/**
    Sets the "multiplicity" property of a DbOOStyle's instance.

    @param value the "multiplicity" property

 **/
  public final void setOojv_multiplicityFont(Font value) throws DbException {
    basicSet(fOojv_multiplicityFont, value);
  }

/**
    Sets the "association name" property of a DbOOStyle's instance.

    @param value the "association name" property

 **/
  public final void setOojv_associationNameFont(Font value) throws DbException {
    basicSet(fOojv_associationNameFont, value);
  }

/**
    Sets the "force italic on abstract methods" property of a DbOOStyle's instance.

    @param value the "force italic on abstract methods" property

 **/
  public final void setOojv_umlForceItalicOnAbstract(Boolean value) throws DbException {
    basicSet(fOojv_umlForceItalicOnAbstract, value);
  }

/**
    Sets the "uml constraint" property of a DbOOStyle's instance.

    @param value the "uml constraint" property

 **/
  public final void setOojv_umlConstraintDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_umlConstraintDisplayed, value);
  }

/**
    Sets the "uml stereotype" property of a DbOOStyle's instance.

    @param value the "uml stereotype" property

 **/
  public final void setOojv_umlStereotypeDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_umlStereotypeDisplayed, value);
  }

/**
    Sets the "force underline on static members" property of a DbOOStyle's instance.

    @param value the "force underline on static members" property

 **/
  public final void setOojv_umlForceUnderlineOnStatic(Boolean value) throws DbException {
    basicSet(fOojv_umlForceUnderlineOnStatic, value);
  }

/**
    Sets the "uml stereotype icon" property of a DbOOStyle's instance.

    @param value the "uml stereotype icon" property

 **/
  public final void setOojv_umlStereotypeIconDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_umlStereotypeIconDisplayed, value);
  }

/**
    Sets the "apply java style (name after type)" property of a DbOOStyle's instance.

    @param value the "apply java style (name after type)" property

 **/
  public final void setOojv_umlTypeBeforeName(Boolean value) throws DbException {
    basicSet(fOojv_umlTypeBeforeName, value);
  }

/**
    Sets the "realization inheritance highlight" property of a DbOOStyle's instance.

    @param value the "realization inheritance highlight" property

 **/
  public final void setHighlightRealizationInheritance(Boolean value) throws DbException {
    basicSet(fHighlightRealizationInheritance, value);
  }

/**
    Sets the "realization inheritance dash style" property of a DbOOStyle's instance.

    @param value the "realization inheritance dash style" property

 **/
  public final void setDashStyleRealizationInheritance(Boolean value) throws DbException {
    basicSet(fDashStyleRealizationInheritance, value);
  }

/**
    Sets the "interfaces shown as circles" property of a DbOOStyle's instance.

    @param value the "interfaces shown as circles" property

 **/
  public final void setOojv_umlInterfaceShownAsCircle(Boolean value) throws DbException {
    basicSet(fOojv_umlInterfaceShownAsCircle, value);
  }

/**
    Sets the "association type field label" property of a DbOOStyle's instance.

    @param value the "association type field label" property

 **/
  public final void setOojv_associationAttributeTypeLabelDisplayed(Boolean value) throws DbException {
    basicSet(fOojv_associationAttributeTypeLabelDisplayed, value);
  }

/**
    Sets the "show only stereotype icons" property of a DbOOStyle's instance.

    @param value the "show only stereotype icons" property

 **/
  public final void setOojv_showOnlyStereotypeIcon(Boolean value) throws DbException {
    basicSet(fOojv_showOnlyStereotypeIcon, value);
  }

/**
    Sets the "association direction" property of a DbOOStyle's instance.

    @param value the "association direction" property

 **/
  public final void setOojv_umlAssociationDirection(Boolean value) throws DbException {
    basicSet(fOojv_umlAssociationDirection, value);
  }

/**
    Sets the "hide parameter names in signatures" property of a DbOOStyle's instance.

    @param value the "hide parameter names in signatures" property

 **/
  public final void setOojv_hideParameterNamesInSignatures(Boolean value) throws DbException {
    basicSet(fOojv_hideParameterNamesInSignatures, value);
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
    Gets the "method signature" of a DbOOStyle's instance.

    @return the "method signature"

 **/
  public final Boolean getOojv_methodSignatureDisplayed() throws DbException {
    return (Boolean)get(fOojv_methodSignatureDisplayed);
  }

/**
    Gets the "interface border" of a DbOOStyle's instance.

    @return the "interface border"

 **/
  public final Color getLineColorInterface() throws DbException {
    return (Color)get(fLineColorInterface);
  }

/**
    Gets the member inherited from interface object associated to a DbOOStyle's instance.

    @return the member inherited from interface object

 **/
  public final DbtPrefix getOojv_memberInheritedFromInterfacePrefix() throws DbException {
    return (DbtPrefix)get(fOojv_memberInheritedFromInterfacePrefix);
  }

/**
    Gets the "inner class" of a DbOOStyle's instance.

    @return the "inner class"

 **/
  public final Font getOojv_innerClassFont() throws DbException {
    return (Font)get(fOojv_innerClassFont);
  }

/**
    Gets the "class border" of a DbOOStyle's instance.

    @return the "class border"

 **/
  public final Color getLineColorClass() throws DbException {
    return (Color)get(fLineColorClass);
  }

/**
    Gets the "composition dash style" of a DbOOStyle's instance.

    @return the "composition dash style"

 **/
  public final Boolean getDashStyleComposition() throws DbException {
    return (Boolean)get(fDashStyleComposition);
  }

/**
    Gets the "association field" of a DbOOStyle's instance.

    @return the "association field"

 **/
  public final Boolean getOojv_associationAttributeDisplayed() throws DbException {
    return (Boolean)get(fOojv_associationAttributeDisplayed);
  }

/**
    Gets the "class text" of a DbOOStyle's instance.

    @return the "class text"

 **/
  public final Color getTextColorClass() throws DbException {
    return (Color)get(fTextColorClass);
  }

/**
    Gets the "class fully qualified name" of a DbOOStyle's instance.

    @return the "class fully qualified name"

 **/
  public final Boolean getOojv_classQualifiedNameDisplayed() throws DbException {
    return (Boolean)get(fOojv_classQualifiedNameDisplayed);
  }

/**
    Gets the "aggregation highlight" of a DbOOStyle's instance.

    @return the "aggregation highlight"

 **/
  public final Boolean getHighlightAggregation() throws DbException {
    return (Boolean)get(fHighlightAggregation);
  }

/**
    Gets the "aggregation" of a DbOOStyle's instance.

    @return the "aggregation"

 **/
  public final Color getLineColorAggregation() throws DbException {
    return (Color)get(fLineColorAggregation);
  }

/**
    Gets the "inherited method" of a DbOOStyle's instance.

    @return the "inherited method"

 **/
  public final Boolean getOojv_inheritedMethodDisplayed() throws DbException {
    return (Boolean)get(fOojv_inheritedMethodDisplayed);
  }

/**
    Gets the "interface background" of a DbOOStyle's instance.

    @return the "interface background"

 **/
  public final Color getBackgroundColorInterface() throws DbException {
    return (Color)get(fBackgroundColorInterface);
  }

/**
    Gets the "non final" of a DbOOStyle's instance.

    @return the "non final"

 **/
  public final Boolean getOojv_nonFinalModifierDisplayed() throws DbException {
    return (Boolean)get(fOojv_nonFinalModifierDisplayed);
  }

/**
    Gets the "class background" of a DbOOStyle's instance.

    @return the "class background"

 **/
  public final Color getBackgroundColorClass() throws DbException {
    return (Color)get(fBackgroundColorClass);
  }

/**
    Gets the imported class object associated to a DbOOStyle's instance.

    @return the imported class object

 **/
  public final DbtPrefix getOojv_importedClassPrefix() throws DbException {
    return (DbtPrefix)get(fOojv_importedClassPrefix);
  }

/**
    Gets the abstract modifier object associated to a DbOOStyle's instance.

    @return the abstract modifier object

 **/
  public final DbtPrefix getOojv_abstractModifierPrefix() throws DbException {
    return (DbtPrefix)get(fOojv_abstractModifierPrefix);
  }

/**
    Gets the "transient" of a DbOOStyle's instance.

    @return the "transient"

 **/
  public final Boolean getOojv_transientModifierDisplayed() throws DbException {
    return (Boolean)get(fOojv_transientModifierDisplayed);
  }

/**
    Gets the "composition highlight" of a DbOOStyle's instance.

    @return the "composition highlight"

 **/
  public final Boolean getHighlightComposition() throws DbException {
    return (Boolean)get(fHighlightComposition);
  }

/**
    Gets the public visibility object associated to a DbOOStyle's instance.

    @return the public visibility object

 **/
  public final DbtPrefix getOojv_publicVisibilityPrefix() throws DbException {
    return (DbtPrefix)get(fOojv_publicVisibilityPrefix);
  }

/**
    Gets the "interface name" of a DbOOStyle's instance.

    @return the "interface name"

 **/
  public final Font getOojv_interfaceNameFont() throws DbException {
    return (Font)get(fOojv_interfaceNameFont);
  }

/**
    Gets the "inherited field" of a DbOOStyle's instance.

    @return the "inherited field"

 **/
  public final Boolean getOojv_inheritedAttributeDisplayed() throws DbException {
    return (Boolean)get(fOojv_inheritedAttributeDisplayed);
  }

/**
    Gets the "non static" of a DbOOStyle's instance.

    @return the "non static"

 **/
  public final Boolean getOojv_nonStaticModifierDisplayed() throws DbException {
    return (Boolean)get(fOojv_nonStaticModifierDisplayed);
  }

/**
    Gets the "association name field label" of a DbOOStyle's instance.

    @return the "association name field label"

 **/
  public final Boolean getOojv_associationAttributeNameLabelDisplayed() throws DbException {
    return (Boolean)get(fOojv_associationAttributeNameLabelDisplayed);
  }

/**
    Gets the "default access" of a DbOOStyle's instance.

    @return the "default access"

 **/
  public final Boolean getOojv_packageModifierDisplayed() throws DbException {
    return (Boolean)get(fOojv_packageModifierDisplayed);
  }

/**
    Gets the "exception dash style" of a DbOOStyle's instance.

    @return the "exception dash style"

 **/
  public final Boolean getDashStyleException() throws DbException {
    return (Boolean)get(fDashStyleException);
  }

/**
    Gets the "exception border" of a DbOOStyle's instance.

    @return the "exception border"

 **/
  public final Color getLineColorException() throws DbException {
    return (Color)get(fLineColorException);
  }

/**
    Gets the "inherited member" of a DbOOStyle's instance.

    @return the "inherited member"

 **/
  public final Font getOojv_inheritedMemberFont() throws DbException {
    return (Font)get(fOojv_inheritedMemberFont);
  }

/**
    Gets the final modifier object associated to a DbOOStyle's instance.

    @return the final modifier object

 **/
  public final DbtPrefix getOojv_finalModifierPrefix() throws DbException {
    return (DbtPrefix)get(fOojv_finalModifierPrefix);
  }

/**
    Gets the "protected" of a DbOOStyle's instance.

    @return the "protected"

 **/
  public final Boolean getOojv_protectedModifierDisplayed() throws DbException {
    return (Boolean)get(fOojv_protectedModifierDisplayed);
  }

/**
    Gets the "abstract" of a DbOOStyle's instance.

    @return the "abstract"

 **/
  public final Boolean getOojv_abstractModifierDisplayed() throws DbException {
    return (Boolean)get(fOojv_abstractModifierDisplayed);
  }

/**
    Gets the "class highlight" of a DbOOStyle's instance.

    @return the "class highlight"

 **/
  public final Boolean getHighlightClass() throws DbException {
    return (Boolean)get(fHighlightClass);
  }

/**
    Gets the "aggregation dash style" of a DbOOStyle's instance.

    @return the "aggregation dash style"

 **/
  public final Boolean getDashStyleAggregation() throws DbException {
    return (Boolean)get(fDashStyleAggregation);
  }

/**
    Gets the "static" of a DbOOStyle's instance.

    @return the "static"

 **/
  public final Boolean getOojv_staticModifierDisplayed() throws DbException {
    return (Boolean)get(fOojv_staticModifierDisplayed);
  }

/**
    Gets the "field" of a DbOOStyle's instance.

    @return the "field"

 **/
  public final Font getOojv_fieldFont() throws DbException {
    return (Font)get(fOojv_fieldFont);
  }

/**
    Gets the "final" of a DbOOStyle's instance.

    @return the "final"

 **/
  public final Boolean getOojv_finalModifierDisplayed() throws DbException {
    return (Boolean)get(fOojv_finalModifierDisplayed);
  }

/**
    Gets the "class dash style" of a DbOOStyle's instance.

    @return the "class dash style"

 **/
  public final Boolean getDashStyleClass() throws DbException {
    return (Boolean)get(fDashStyleClass);
  }

/**
    Gets the "method" of a DbOOStyle's instance.

    @return the "method"

 **/
  public final Boolean getOojv_methodDisplayed() throws DbException {
    return (Boolean)get(fOojv_methodDisplayed);
  }

/**
    Gets the hiding member object associated to a DbOOStyle's instance.

    @return the hiding member object

 **/
  public final DbtPrefix getOojv_hidingMemberPrefix() throws DbException {
    return (DbtPrefix)get(fOojv_hidingMemberPrefix);
  }

/**
    Gets the "exception background" of a DbOOStyle's instance.

    @return the "exception background"

 **/
  public final Color getBackgroundColorException() throws DbException {
    return (Color)get(fBackgroundColorException);
  }

/**
    Gets the "non transient" of a DbOOStyle's instance.

    @return the "non transient"

 **/
  public final Boolean getOojv_nonTransientModifierDisplayed() throws DbException {
    return (Boolean)get(fOojv_nonTransientModifierDisplayed);
  }

/**
    Gets the static modifier object associated to a DbOOStyle's instance.

    @return the static modifier object

 **/
  public final DbtPrefix getOojv_staticModifierPrefix() throws DbException {
    return (DbtPrefix)get(fOojv_staticModifierPrefix);
  }

/**
    Gets the "method" of a DbOOStyle's instance.

    @return the "method"

 **/
  public final Font getOojv_methodFont() throws DbException {
    return (Font)get(fOojv_methodFont);
  }

/**
    Gets the "exception text" of a DbOOStyle's instance.

    @return the "exception text"

 **/
  public final Color getTextColorException() throws DbException {
    return (Color)get(fTextColorException);
  }

/**
    Gets the protected visibility object associated to a DbOOStyle's instance.

    @return the protected visibility object

 **/
  public final DbtPrefix getOojv_protectedVisibilityPrefix() throws DbException {
    return (DbtPrefix)get(fOojv_protectedVisibilityPrefix);
  }

/**
    Gets the "abstract class name" of a DbOOStyle's instance.

    @return the "abstract class name"

 **/
  public final Font getOojv_abstractClassNameFont() throws DbException {
    return (Font)get(fOojv_abstractClassNameFont);
  }

/**
    Gets the "inherited inner class" of a DbOOStyle's instance.

    @return the "inherited inner class"

 **/
  public final Boolean getOojv_inheritedInnerClassDisplayed() throws DbException {
    return (Boolean)get(fOojv_inheritedInnerClassDisplayed);
  }

/**
    Gets the "non abstract" of a DbOOStyle's instance.

    @return the "non abstract"

 **/
  public final Boolean getOojv_nonAbstractModifierDisplayed() throws DbException {
    return (Boolean)get(fOojv_nonAbstractModifierDisplayed);
  }

/**
    Gets the "private" of a DbOOStyle's instance.

    @return the "private"

 **/
  public final Boolean getOojv_privateModifierDisplayed() throws DbException {
    return (Boolean)get(fOojv_privateModifierDisplayed);
  }

/**
    Gets the transient modifier object associated to a DbOOStyle's instance.

    @return the transient modifier object

 **/
  public final DbtPrefix getOojv_transientModifierPrefix() throws DbException {
    return (DbtPrefix)get(fOojv_transientModifierPrefix);
  }

/**
    Gets the "composition" of a DbOOStyle's instance.

    @return the "composition"

 **/
  public final Color getLineColorComposition() throws DbException {
    return (Color)get(fLineColorComposition);
  }

/**
    Gets the overriding member object associated to a DbOOStyle's instance.

    @return the overriding member object

 **/
  public final DbtPrefix getOojv_overridingMemberPrefix() throws DbException {
    return (DbtPrefix)get(fOojv_overridingMemberPrefix);
  }

/**
    Gets the private visibility object associated to a DbOOStyle's instance.

    @return the private visibility object

 **/
  public final DbtPrefix getOojv_privateVisibilityPrefix() throws DbException {
    return (DbtPrefix)get(fOojv_privateVisibilityPrefix);
  }

/**
    Gets the "class name" of a DbOOStyle's instance.

    @return the "class name"

 **/
  public final Font getOojv_classNameFont() throws DbException {
    return (Font)get(fOojv_classNameFont);
  }

/**
    Gets the "interface text" of a DbOOStyle's instance.

    @return the "interface text"

 **/
  public final Color getTextColorInterface() throws DbException {
    return (Color)get(fTextColorInterface);
  }

/**
    Gets the "exception highlight" of a DbOOStyle's instance.

    @return the "exception highlight"

 **/
  public final Boolean getHighlightException() throws DbException {
    return (Boolean)get(fHighlightException);
  }

/**
    Gets the "exception name" of a DbOOStyle's instance.

    @return the "exception name"

 **/
  public final Font getOojv_exceptionNameFont() throws DbException {
    return (Font)get(fOojv_exceptionNameFont);
  }

/**
    Gets the "inner class" of a DbOOStyle's instance.

    @return the "inner class"

 **/
  public final Boolean getOojv_innerClassDisplayed() throws DbException {
    return (Boolean)get(fOojv_innerClassDisplayed);
  }

/**
    Gets the "interface highlight" of a DbOOStyle's instance.

    @return the "interface highlight"

 **/
  public final Boolean getHighlightInterface() throws DbException {
    return (Boolean)get(fHighlightInterface);
  }

/**
    Gets the "public" of a DbOOStyle's instance.

    @return the "public"

 **/
  public final Boolean getOojv_publicModifierDisplayed() throws DbException {
    return (Boolean)get(fOojv_publicModifierDisplayed);
  }

/**
    Gets the "field" of a DbOOStyle's instance.

    @return the "field"

 **/
  public final Boolean getOojv_attributeDisplayed() throws DbException {
    return (Boolean)get(fOojv_attributeDisplayed);
  }

/**
    Gets the "interface dash style" of a DbOOStyle's instance.

    @return the "interface dash style"

 **/
  public final Boolean getDashStyleInterface() throws DbException {
    return (Boolean)get(fDashStyleInterface);
  }

/**
    Gets the default access visibility object associated to a DbOOStyle's instance.

    @return the default access visibility object

 **/
  public final DbtPrefix getOojv_packageVisibilityPrefix() throws DbException {
    return (DbtPrefix)get(fOojv_packageVisibilityPrefix);
  }

/**
    Gets the "stereotype name" of a DbOOStyle's instance.

    @return the "stereotype name"

 **/
  public final Font getOojv_stereotypeNameFont() throws DbException {
    return (Font)get(fOojv_stereotypeNameFont);
  }

/**
    Gets the "inherited method signature" of a DbOOStyle's instance.

    @return the "inherited method signature"

 **/
  public final Boolean getOojv_inheritedMethodSignatureDisplayed() throws DbException {
    return (Boolean)get(fOojv_inheritedMethodSignatureDisplayed);
  }

/**
    Gets the project object associated to a DbOOStyle's instance.

    @return the project object

 **/
  public final DbSMSProject getReferringProjectOo() throws DbException {
    return (DbSMSProject)get(fReferringProjectOo);
  }

/**
    Gets the "association name" of a DbOOStyle's instance.

    @return the "association name"

 **/
  public final Boolean getOojv_associationNameDisplayed() throws DbException {
    return (Boolean)get(fOojv_associationNameDisplayed);
  }

/**
    Gets the "association field label" of a DbOOStyle's instance.

    @return the "association field label"

 **/
  public final Font getOojv_associationAttributeLabelFont() throws DbException {
    return (Font)get(fOojv_associationAttributeLabelFont);
  }

/**
    Gets the "multiplicity" of a DbOOStyle's instance.

    @return the "multiplicity"

 **/
  public final Font getOojv_multiplicityFont() throws DbException {
    return (Font)get(fOojv_multiplicityFont);
  }

/**
    Gets the "association name" of a DbOOStyle's instance.

    @return the "association name"

 **/
  public final Font getOojv_associationNameFont() throws DbException {
    return (Font)get(fOojv_associationNameFont);
  }

/**
    Gets the "force italic on abstract methods" of a DbOOStyle's instance.

    @return the "force italic on abstract methods"

 **/
  public final Boolean getOojv_umlForceItalicOnAbstract() throws DbException {
    return (Boolean)get(fOojv_umlForceItalicOnAbstract);
  }

/**
    Gets the "uml constraint" of a DbOOStyle's instance.

    @return the "uml constraint"

 **/
  public final Boolean getOojv_umlConstraintDisplayed() throws DbException {
    return (Boolean)get(fOojv_umlConstraintDisplayed);
  }

/**
    Gets the "uml stereotype" of a DbOOStyle's instance.

    @return the "uml stereotype"

 **/
  public final Boolean getOojv_umlStereotypeDisplayed() throws DbException {
    return (Boolean)get(fOojv_umlStereotypeDisplayed);
  }

/**
    Gets the "force underline on static members" of a DbOOStyle's instance.

    @return the "force underline on static members"

 **/
  public final Boolean getOojv_umlForceUnderlineOnStatic() throws DbException {
    return (Boolean)get(fOojv_umlForceUnderlineOnStatic);
  }

/**
    Gets the "uml stereotype icon" of a DbOOStyle's instance.

    @return the "uml stereotype icon"

 **/
  public final Boolean getOojv_umlStereotypeIconDisplayed() throws DbException {
    return (Boolean)get(fOojv_umlStereotypeIconDisplayed);
  }

/**
    Gets the "apply java style (name after type)" of a DbOOStyle's instance.

    @return the "apply java style (name after type)"

 **/
  public final Boolean getOojv_umlTypeBeforeName() throws DbException {
    return (Boolean)get(fOojv_umlTypeBeforeName);
  }

/**
    Gets the "realization inheritance highlight" of a DbOOStyle's instance.

    @return the "realization inheritance highlight"

 **/
  public final Boolean getHighlightRealizationInheritance() throws DbException {
    return (Boolean)get(fHighlightRealizationInheritance);
  }

/**
    Gets the "realization inheritance dash style" of a DbOOStyle's instance.

    @return the "realization inheritance dash style"

 **/
  public final Boolean getDashStyleRealizationInheritance() throws DbException {
    return (Boolean)get(fDashStyleRealizationInheritance);
  }

/**
    Gets the "interfaces shown as circles" of a DbOOStyle's instance.

    @return the "interfaces shown as circles"

 **/
  public final Boolean getOojv_umlInterfaceShownAsCircle() throws DbException {
    return (Boolean)get(fOojv_umlInterfaceShownAsCircle);
  }

/**
    Gets the "association type field label" of a DbOOStyle's instance.

    @return the "association type field label"

 **/
  public final Boolean getOojv_associationAttributeTypeLabelDisplayed() throws DbException {
    return (Boolean)get(fOojv_associationAttributeTypeLabelDisplayed);
  }

/**
    Gets the "show only stereotype icons" of a DbOOStyle's instance.

    @return the "show only stereotype icons"

 **/
  public final Boolean getOojv_showOnlyStereotypeIcon() throws DbException {
    return (Boolean)get(fOojv_showOnlyStereotypeIcon);
  }

/**
    Gets the "association direction" of a DbOOStyle's instance.

    @return the "association direction"

 **/
  public final Boolean getOojv_umlAssociationDirection() throws DbException {
    return (Boolean)get(fOojv_umlAssociationDirection);
  }

/**
    Gets the "hide parameter names in signatures" of a DbOOStyle's instance.

    @return the "hide parameter names in signatures"

 **/
  public final Boolean getOojv_hideParameterNamesInSignatures() throws DbException {
    return (Boolean)get(fOojv_hideParameterNamesInSignatures);
  }

  public MetaClass getMetaClass() {
    return metaClass;
  }

}
