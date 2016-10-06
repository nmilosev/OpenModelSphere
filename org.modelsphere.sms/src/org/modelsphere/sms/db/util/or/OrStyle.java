/*************************************************************************

Copyright (C) 2010 Grandite

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

package org.modelsphere.sms.db.util.or;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import org.modelsphere.jack.baseDb.db.srtypes.DbtPrefix;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.KeyTool;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.Module;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.db.srtypes.SMSDisplayDescriptor;
import org.modelsphere.sms.oo.db.DbOOStyle;
import org.modelsphere.sms.or.db.DbORStyle;
import org.modelsphere.sms.or.db.srtypes.ORDescriptorFormat;

public class OrStyle {
	
	//image
	private static final Image kKeyImage = GraphicUtil.loadImage(KeyTool.class, "resources/pk.gif"); // NOT LOCALIZABLE - image
    private static final Image kSuperObjImage = GraphicUtil.loadImage(Module.class,
    	"db/resources/super-object.gif"); // NOT LOCALIZABLE - image
    private static final Image kSubObjImage = GraphicUtil.loadImage(Module.class,
    	"db/resources/sub-object.gif"); // NOT LOCALIZABLE - image
    private static final Image kSourceImage = GraphicUtil.loadImage(Module.class,
    	"db/resources/source.gif"); // NOT LOCALIZABLE - image
    private static final Image kTargetImage = GraphicUtil.loadImage(Module.class,
    	"db/resources/target.gif"); // NOT LOCALIZABLE - image
    private static final Image kErrorImage = GraphicUtil.loadImage(Module.class,
    	"db/resources/message_error.gif"); // NOT LOCALIZABLE - image
    private static final Image kWarningImage = GraphicUtil.loadImage(Module.class,
    	"db/resources/message_warning.gif"); // NOT LOCALIZABLE -image
    
    //prefixes
    private static final DbtPrefix keyPrefix = new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "PK", null,
            kKeyImage);
    private static final DbtPrefix sourcePrefix = new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "S", null,
            kSourceImage);
    private static final DbtPrefix targetPrefix = new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "D", null,
            kTargetImage);
    private static final DbtPrefix errorPrefix = new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "!", null,
            kErrorImage);
    private static final DbtPrefix warningPrefix = new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "!",
            null, kWarningImage);

	public static void init() {
		
		List<DisplayOption> options = new ArrayList<DisplayOption>();
		
		options.add(new DisplayOption(DbORStyle.fOr_associationDescriptorDisplay, false)); 
		options.add(new DisplayOption(DbORStyle.fOr_associationChildRoleConnectivitiesDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_associationChildRoleDescriptorDisplay, false)); 
		options.add(new DisplayOption(DbORStyle.fOr_associationParentRoleConnectivitiesDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_associationParentRoleDescriptorDisplay, false)); 
		
		options.add(new DisplayOption(DbORStyle.fOr_umlAssociationDirection, false)); 
		options.add(new DisplayOption(DbORStyle.fOr_checkDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_checkParametersDisplay, true)); 
		
		options.add(new DisplayOption(DbORStyle.fOr_columnDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_columnDefaultValueDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_columnDomainSourceTypeDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_columnLengthDecimalsDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_columnNullValueDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_columnTypeDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_fkDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_fkColumnsDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_indexDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_indexColumnsDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_indexTypeDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_indexUniqueDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_pkDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_pkColumnsDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_tableOwnerDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_triggerDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_triggerColumnsDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_ukDisplay, true)); 
		options.add(new DisplayOption(DbORStyle.fOr_ukColumnsDisplay, true)); 
		
		if (ScreenPerspective.isFullVersion()) {
			options.add(new DisplayOption(DbORStyle.fOr_umlConstraintDisplayed, true)); 
			options.add(new DisplayOption(DbORStyle.fOr_umlStereotypeDisplayed, true)); 
			options.add(new DisplayOption(DbORStyle.fOr_umlStereotypeIconDisplayed, true)); 
		}
		
		options.add(new DisplayOption(DbORStyle.fOr_associationAsRelationships, false)); 
		options.add(new DisplayOption(DbORStyle.fOr_associationTablesAsRelationships, false)); 
		options.add(new DisplayOption(DbORStyle.fOr_showDependentTables, false)); 
		options.add(new DisplayOption(DbORStyle.fOr_UnidentifyingAssociationsAreDashed, false)); 
		
		// Set display options
        DbORStyle.or_displayOptions = DisplayOption.getOptionMetaFields(options); 
        DbORStyle.or_displayOptionDefaultValues = DisplayOption.getOptionDefaultValues(options); 

        DbORStyle.or_displayOptionDefaultValues = new Boolean[] { 
        		Boolean.FALSE,// fOr_associationDescriptorDisplay,
                Boolean.TRUE, // fOr_associationChildRoleConnectivitiesDisplay,
                Boolean.FALSE, // fOr_associationChildRoleDescriptorDisplay,
                Boolean.TRUE, // fOr_associationParentRoleConnectivitiesDisplay,
                Boolean.FALSE, // fOr_associationParentRoleDescriptorDisplay,
                
                Boolean.FALSE, // fOr_umlAssociationDirection
                Boolean.TRUE, // fOr_checkDisplay,
                Boolean.TRUE, // fOr_checkParametersDisplay,
                
                Boolean.TRUE, // fOr_columnDisplay,
                Boolean.TRUE, // fOr_columnDefaultValueDisplay,
                Boolean.TRUE, // fOr_columnDomainSourceTypeDisplay,
                Boolean.TRUE, // fOr_columnLengthDecimalsDisplay,
                Boolean.TRUE, // fOr_columnNullValueDisplay,
                Boolean.TRUE, // fOr_columnTypeDisplay,
                Boolean.TRUE, // fOr_fkDisplay,
                Boolean.TRUE, // fOr_fkColumnsDisplay,
                Boolean.TRUE, // fOr_indexDisplay,
                Boolean.TRUE, // fOr_indexColumnsDisplay,
                Boolean.TRUE, // fOr_indexTypeDisplay,
                Boolean.TRUE, // fOr_indexUniqueDisplay,
                Boolean.TRUE, // fOr_pkDisplay,
                Boolean.TRUE, // fOr_pkColumnsDisplay,
                Boolean.TRUE, // fOr_tableOwnerDisplay
                Boolean.TRUE, // fOr_triggerDisplay,
                Boolean.TRUE, // fOr_triggerColumnsDisplay,
                Boolean.TRUE, // fOr_ukDisplay,
                Boolean.TRUE, // fOr_ukColumnsDisplay
                Boolean.TRUE, // fOr_umlConstraintDisplayed,
                Boolean.TRUE, // fOr_umlStereotypeDisplayed,
                Boolean.TRUE, // fOr_umlStereotypeIconDisplayed
                Boolean.FALSE, // DbORStyle.fOr_associationAsRelationships,
                Boolean.FALSE, // DbORStyle.fOr_associationTablesAsRelationships,
                Boolean.FALSE, // DbORStyle.fOr_showDependentTables,
                Boolean.FALSE, // DbORStyle.fOr_UnidentifyingAssociationsAreDashed,
        };

        // Display Descriptor options
        DbORStyle.or_descriptorOptions = new MetaField[] { DbORStyle.fOr_nameDescriptor,
                DbORStyle.fOr_columnAdditionnalDescriptor,
                DbORStyle.fOr_tableAdditionnalDescriptor, DbORStyle.fOr_viewAdditionnalDescriptor };

        DbORStyle.or_descriptorOptionDefaultValues = new SMSDisplayDescriptor[] {
                SMSDisplayDescriptor.getInstance(SMSDisplayDescriptor.NAME), // fOr_nameDescriptor
                SMSDisplayDescriptor.getInstance(SMSDisplayDescriptor.NOT_DISPLAYED), // fOr_columnAdditionnalDescriptor
                SMSDisplayDescriptor.getInstance(SMSDisplayDescriptor.NOT_DISPLAYED), // fOr_tableAdditionnalDescriptor
                SMSDisplayDescriptor.getInstance(SMSDisplayDescriptor.NOT_DISPLAYED) // fOr_viewAdditionnalDescriptor
        };

        // Font options
        DbORStyle.or_fontOptions = new MetaField[] { DbORStyle.fOr_associationDescriptorFont,
                DbORStyle.fOr_associationConnectivitiesFont,
                DbORStyle.fOr_associationRoleDescriptorFont, DbORStyle.fOr_checkConstraintFont,
                DbORStyle.fOr_checkParametersFont, DbORStyle.fOr_checkConstraintPrefixFont,
                DbORStyle.fOr_columnDescriptorFont, DbORStyle.fOr_columnAdditionnalDescriptorFont,
                DbORStyle.fOr_columnDefaultValueFont, DbORStyle.fOr_columnDomainSourceTypeFont,
                DbORStyle.fOr_columnLengthDecimalsFont, DbORStyle.fOr_columnNullValueFont,
                DbORStyle.fOr_columnPrefixFont, DbORStyle.fOr_columnTypeFont,
                DbORStyle.fOr_fkDescriptorFont, DbORStyle.fOr_fkColumnsFont,
                DbORStyle.fOr_fkPrefixFont, DbORStyle.fOr_indexDescriptorFont,
                DbORStyle.fOr_indexColumnsFont, DbORStyle.fOr_indexPrefixFont,
                DbORStyle.fOr_indexTypeFont, DbORStyle.fOr_indexUniqueFont,
                DbORStyle.fOr_pkDescriptorFont, DbORStyle.fOr_pkColumnsFont,
                DbORStyle.fOr_pkPrefixFont, DbORStyle.fOr_tableAdditionnalDescriptorFont,
                DbORStyle.fOr_tableDescriptorFont, DbORStyle.fOr_tablePrefixFont,
                DbORStyle.fOr_triggerDescriptorFont, DbORStyle.fOr_triggerColumnsFont,
                DbORStyle.fOr_triggerPrefixFont, DbORStyle.fOr_ukDescriptorFont,
                DbORStyle.fOr_ukColumnsFont, DbORStyle.fOr_ukPrefixFont,
                DbORStyle.fOr_viewDescriptorFont, DbORStyle.fOr_viewAdditionnalDescriptorFont,
                DbOOStyle.fSms_packageNameFont, };

        Font plainFont = new Font("Arial", Font.PLAIN, 8);
        //Font italicFont = new Font("Arial", Font.ITALIC, 8);
        Font boldFont = new Font("Arial", Font.BOLD, 8);
        DbORStyle.or_fontOptionDefaultValues = new Font[] { plainFont, // fOr_associationDescriptorFont,
                plainFont, // fOr_associationConnectivitiesFont,
                plainFont, // fOr_associationRoleDescriptorFont,
                plainFont, // fOr_checkConstraintFont,
                plainFont, // fOr_checkParametersFont,
                plainFont, // fOr_checkConstraintPrefixFont
                plainFont, // fOr_columnDescriptorFont,
                plainFont, // fOr_columnAdditionnalDescriptorFont,
                plainFont, // fOr_columnDefaultValueFont,
                plainFont, // fOr_columnDomainSourceTypeFont,
                plainFont, // fOr_columnLengthDecimalsFont,
                plainFont, // fOr_columnNullValueFont,
                plainFont, // fOr_columnPrefixFont
                plainFont, // fOr_columnTypeFont,
                plainFont, // fOr_fkDescriptorFont,
                plainFont, // fOr_fkColumnsFont,
                plainFont, // fOr_fkPrefixFont
                plainFont, // fOr_indexDescriptorFont,
                plainFont, // fOr_indexColumnsFont,
                plainFont, // fOr_indexPrefixFont
                plainFont, // fOr_indexTypeFont,
                plainFont, // fOr_indexUniqueFont,
                plainFont, // fOr_pkDescriptorFont,
                plainFont, // fOr_pkColumnsFont,
                plainFont, // fOr_pkPrefixFont
                plainFont, // fOr_tableAdditionnalDescriptorFont,
                boldFont, // fOr_tableDescriptorFont,
                plainFont, // fOr_tablePrefixFont
                plainFont, // fOr_triggerDescriptorFont,
                plainFont, // fOr_triggerColumnsFont,
                plainFont, // fOr_triggerPrefixFont
                plainFont, // fOr_ukDescriptorFont,
                plainFont, // fOr_ukColumnsFont,
                plainFont, // fOr_ukPrefixFont
                boldFont, // fOr_viewDescriptorFont,
                plainFont, // fOr_viewAdditionnalDescriptorFont,
                plainFont, // fSms_packageNameFont,
        };

        // Color options
        DbORStyle.or_colorOptions = new MetaField[] {
                DbORStyle.fLineColorDbSMSAssociation,
                // fLineColorDbSMSInheritance,
                DbSMSStyle.fBackgroundColorDbSMSPackage, DbSMSStyle.fLineColorDbSMSPackage,
                DbSMSStyle.fTextColorDbSMSPackage, DbORStyle.fBackgroundColorDbORTable,
                DbORStyle.fLineColorDbORTable, DbORStyle.fTextColorDbORTable,
                DbORStyle.fBackgroundColorDbORView, DbORStyle.fLineColorDbORView,
                DbORStyle.fTextColorDbORView, DbORStyle.fBackgroundColorDbORChoiceOrSpecialization,
                DbORStyle.fLineColorDbORChoiceOrSpecialization, DbORStyle.fTextColorDbSMSNotice,
                DbORStyle.fBackgroundColorDbSMSNotice, DbORStyle.fLineColorDbSMSNotice,
                DbSMSStyle.fLineColorDbSMSGraphicalLink };

        Color lightBlue = new Color(204, 240, 255);
        Color darkMauve = new Color(5, 5, 161);
        Color lightMagenta = new Color(240, 240, 255);
        Color lightYellow = new Color(255, 255, 204);
        // Color lightGreen = new Color(191, 227, 206);
        Color darkYellow = new Color(255, 185, 31, 140);

        DbORStyle.or_colorOptionDefaultValues = new Color[] { Color.black, // fLineColorDbSMSAssociation
                // Color.black, // fLineColorDbSMSInheritance,
                darkYellow, // fBackgroundColorDbSMSPackage
                Color.black, // fLineColorDbSMSPackage
                Color.black, // fTextColorDbSMSPackage
                lightMagenta, // fBackgroundColorDbORTable
                darkMauve, // fLineColorDbORTable
                Color.black, // fTextColorDbORTable
                lightBlue, // fBackgroundColorDbORView
                Color.black, // fLineColorDbORView
                Color.black, // fTextColorDbORView
                lightMagenta, // fBackgroundColorDbORChoiceSpec
                darkMauve, // fLineColorDbORChoiceSpec
                Color.black, // fTextColorDbSMSNotice
                lightYellow, // fBackgroundColorDbSMSNotice
                Color.black, // fLineColorDbSMSNotice
                Color.black, // fLineColorDbSMSGraphicalLink,
        };

        // Line Style Options
        DbORStyle.or_lineOptions = new MetaField[] {
                DbORStyle.fDashStyleDbSMSAssociation,
                DbORStyle.fHighlightDbSMSAssociation,
                // fDashStyleDbSMSInheritance,
                // fHighlightDbSMSInheritance,
                DbSMSStyle.fDashStyleDbSMSPackage, DbSMSStyle.fHighlightDbSMSPackage,
                DbORStyle.fDashStyleDbORTable, DbORStyle.fHighlightDbORTable,
                DbORStyle.fDashStyleDbORView, DbORStyle.fHighlightDbORView,
                DbORStyle.fHighlightDbSMSNotice, DbORStyle.fDashStyleDbSMSNotice,
                DbSMSStyle.fDashStyleDbSMSGraphicalLink, DbSMSStyle.fHighlightDbSMSGraphicalLink };

        DbORStyle.or_lineOptionDefaultValues = new Boolean[] { Boolean.FALSE, // fDashStyleDbSMSAssociation
                Boolean.FALSE, // fHighlightDbSMSAssociation
                // Boolean.FALSE, // fDashStyleDbSMSInheritance
                // Boolean.FALSE, // fHighlightDbSMSInheritance
                Boolean.FALSE, // fDashStyleDbSMSPackage
                Boolean.FALSE, // fHighlightDbSMSPackage
                Boolean.FALSE, // fDashStyleDbORTable
                Boolean.FALSE, // fHighlightDbORTable
                Boolean.FALSE, // fDashStyleDbORView
                Boolean.FALSE, // fHighlightDbORView
                Boolean.FALSE, // fHighlightDbSMSNotice
                Boolean.FALSE, // fDashStyleDbSMSNotice
                Boolean.TRUE, // fDashStyleDbSMSLinkGo
                Boolean.FALSE, // fHighlightDbSMSLinkGo
        };

        // Prefix options
        DbORStyle.or_prefixOptions = new MetaField[] { 
        		DbORStyle.fOr_columnLinkedToSubModelPrefix,
                DbORStyle.fOr_columnLinkedToSuperModelPrefix, 
                DbORStyle.fOr_columnNullValuePrefix,
                DbORStyle.fOr_fkColumnsPrefix, 
                DbORStyle.fOr_indexColumnsPrefix,
                DbORStyle.fOr_pkColumnsPrefix, 
                DbORStyle.fOr_columnChoicePrefix,
                DbORStyle.fOr_columnSpecializationPrefix,
                DbORStyle.fOr_tableLinkedToSubModelPrefix,
                DbORStyle.fOr_tableLinkedToSuperModelPrefix, 
                DbORStyle.fOr_ukColumnsPrefix,
                DbORStyle.fOr_viewPrefix, 
                DbORStyle.fSourcePrefix, DbORStyle.fTargetPrefix, DbORStyle.fErrorPrefix,
                DbORStyle.fWarningPrefix };

        // final Image kPKImage = GraphicUtil.loadImage(Tool.class,
        // "resources/pk.gif"); // NOT LOCALIZABLE - image
        // final Image kSourceImage = GraphicUtil.loadImage(Module.class,
        // "db/resources/source.gif"); // NOT LOCALIZABLE - image
        // final Image kTargetImage = GraphicUtil.loadImage(Module.class,
        // "db/resources/target.gif"); // NOT LOCALIZABLE - image

        DbORStyle.or_prefixOptionDefaultValues = new DbtPrefix[] {
                new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "v", null, kSubObjImage), // fOr_columnLinkedToSubModelPrefix,
                new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "^", null, kSuperObjImage), // fOr_columnLinkedToSuperModelPrefix,
                new DbtPrefix(DbtPrefix.DISPLAY_NONE, "Ø", null, null), // fOr_columnNullValuePrefix,
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "FK%#", null, null), // fOr_fkColumnsPrefix,
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "X%#", null, null), // fOr_indexColumnsPrefix,
                keyPrefix, // fOr_pkColumnsPrefix,
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "©", null, null), //DbORStyle.fOr_columnChoicePrefix,
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "@", null, null), //DbORStyle.fOr_columnSpecializationPrefix,
                new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "v", null, kSubObjImage), // fOr_tableLinkedToSubModelPrefix,
                new DbtPrefix(DbtPrefix.DISPLAY_IMAGE, "^", null, kSuperObjImage), // fOr_tableLinkedToSuperModelPrefix,
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "<%#>", null, null), // fOr_ukColumnsPrefix,
                new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "(V)", null, null), // fOr_viewPrefix,
                sourcePrefix, // fSourcePrefix,
                targetPrefix, // fTargetPrefix,
                errorPrefix, // fErrorPrefix,
                warningPrefix // fWarningPrefix,
        };

        // Descriptor Format
        DbORStyle.or_descriptorFormatOptions = new MetaField[] { DbORStyle.fOr_fkDescriptorFormat,
                DbORStyle.fOr_pkDescriptorFormat, DbORStyle.fOr_ukDescriptorFormat };

        DbORStyle.or_descriptorFormatOptionDefaultValues = new ORDescriptorFormat[] {
                new ORDescriptorFormat(false, false, false), // fOr_fkDescriptorFormat,
                new ORDescriptorFormat(false, false, true), // fOr_pkDescriptorFormat,
                new ORDescriptorFormat(false, false, false), // fOr_ukDescriptorFormat
        };

        DbORStyle.or_listOptionTabs = new String[] { "or_optionGroups", "or_optionValueGroups",
                "or_optionGroupHeaders" };

        DbORStyle.or_optionGroups = new MetaField[][] { DbORStyle.or_displayOptions,
                DbORStyle.or_descriptorOptions, DbORStyle.or_descriptorFormatOptions,
                DbORStyle.or_fontOptions, DbORStyle.or_prefixOptions, DbORStyle.or_colorOptions,
                DbORStyle.or_lineOptions };

        DbORStyle.or_optionValueGroups = new Object[][] { DbORStyle.or_displayOptionDefaultValues,
                DbORStyle.or_descriptorOptionDefaultValues,
                DbORStyle.or_descriptorFormatOptionDefaultValues,
                DbORStyle.or_fontOptionDefaultValues, DbORStyle.or_prefixOptionDefaultValues,
                DbORStyle.or_colorOptionDefaultValues, DbORStyle.or_lineOptionDefaultValues };

        DbORStyle.or_optionGroupHeaders = new String[] {
                org.modelsphere.sms.or.international.LocaleMgr.screen.getString("StructureDisplay"),
                org.modelsphere.sms.or.international.LocaleMgr.screen
                        .getString("DescriptorsDisplay"),
                org.modelsphere.sms.or.international.LocaleMgr.screen.getString("DescriptorFormat"),
                org.modelsphere.sms.or.international.LocaleMgr.screen.getString("Font"),
                org.modelsphere.sms.or.international.LocaleMgr.screen.getString("Prefix"),
                org.modelsphere.sms.or.international.LocaleMgr.screen.getString("Color"),
                org.modelsphere.sms.or.international.LocaleMgr.screen.getString("LineStyle") };
	}
	
	//
	// inner class
	//
	private static class DisplayOption {
		private MetaField _metaField; 
		private boolean _value; 
		
		public DisplayOption(MetaField option, boolean value) {
			_metaField = option;
			_value = value;
		}

		public static MetaField[] getOptionMetaFields(List<DisplayOption> options) {
			List<MetaField> mfList = new ArrayList<MetaField>(); 
			for (DisplayOption option : options) {
				mfList.add(option._metaField);
			}
			
			MetaField[] metaFields = new MetaField[mfList.size()];
			mfList.toArray(metaFields); 
			return metaFields;
		}
		

		public static Boolean[] getOptionDefaultValues(List<DisplayOption> options) {
			List<Boolean> valueList = new ArrayList<Boolean>(); 
			for (DisplayOption option : options) {
				valueList.add(option._value);
			}
			
			Boolean[] values = new Boolean[valueList.size()];
			valueList.toArray(values); 
			return values;
		}
		
	}
	

}
