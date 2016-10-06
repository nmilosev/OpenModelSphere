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
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSProject.html">DbSMSProject</A>.<br>
 * <b>Components : </b>none.<br>
 **/
public final class DbORCommonItemStyle extends DbSMSStyle {

    //Meta

    public static final MetaField fOr_nameDescriptor = new MetaField(LocaleMgr.db
            .getString("or_nameDescriptor"));
    public static final MetaField fLineColorDbORCommonItem = new MetaField(LocaleMgr.db
            .getString("lineColorDbORCommonItem"));
    public static final MetaField fDescriptorFontDbORCommonItem = new MetaField(LocaleMgr.db
            .getString("descriptorFontDbORCommonItem"));
    public static final MetaField fDashStyleDbORCommonItem = new MetaField(LocaleMgr.db
            .getString("dashStyleDbORCommonItem"));
    public static final MetaField fTextColorDbORCommonItem = new MetaField(LocaleMgr.db
            .getString("textColorDbORCommonItem"));
    public static final MetaField fHighlightDbORCommonItem = new MetaField(LocaleMgr.db
            .getString("highlightDbORCommonItem"));
    public static final MetaField fBackgroundColorDbORCommonItem = new MetaField(LocaleMgr.db
            .getString("backgroundColorDbORCommonItem"));
    public static final MetaField fOr_commonItemLengthDecimalsDisplay = new MetaField(LocaleMgr.db
            .getString("or_commonItemLengthDecimalsDisplay"));
    public static final MetaField fOr_commonItemLengthDecimalsFont = new MetaField(LocaleMgr.db
            .getString("or_commonItemLengthDecimalsFont"));
    public static final MetaRelation1 fReferringProjectCommonItem = new MetaRelation1(LocaleMgr.db
            .getString("referringProjectCommonItem"), 0);
    public static final MetaField fOr_commonItemNullValueFont = new MetaField(LocaleMgr.db
            .getString("or_commonItemNullValueFont"));
    public static final MetaField fOr_commonItemTypeFont = new MetaField(LocaleMgr.db
            .getString("or_commonItemTypeFont"));
    public static final MetaField fOr_commonItemTypeDisplay = new MetaField(LocaleMgr.db
            .getString("or_commonItemTypeDisplay"));
    public static final MetaField fOr_commonItemNullValueDisplay = new MetaField(LocaleMgr.db
            .getString("or_commonItemNullValueDisplay"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORCommonItemStyle"), DbORCommonItemStyle.class, new MetaField[] {
            fOr_nameDescriptor, fLineColorDbORCommonItem, fDescriptorFontDbORCommonItem,
            fDashStyleDbORCommonItem, fTextColorDbORCommonItem, fHighlightDbORCommonItem,
            fBackgroundColorDbORCommonItem, fOr_commonItemLengthDecimalsDisplay,
            fOr_commonItemLengthDecimalsFont, fReferringProjectCommonItem,
            fOr_commonItemNullValueFont, fOr_commonItemTypeFont, fOr_commonItemTypeDisplay,
            fOr_commonItemNullValueDisplay }, MetaClass.MATCHABLE | MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSStyle.metaClass);

            fOr_nameDescriptor.setJField(DbORCommonItemStyle.class
                    .getDeclaredField("m_or_nameDescriptor"));
            fLineColorDbORCommonItem.setJField(DbORCommonItemStyle.class
                    .getDeclaredField("m_lineColorDbORCommonItem"));
            fDescriptorFontDbORCommonItem.setJField(DbORCommonItemStyle.class
                    .getDeclaredField("m_descriptorFontDbORCommonItem"));
            fDashStyleDbORCommonItem.setJField(DbORCommonItemStyle.class
                    .getDeclaredField("m_dashStyleDbORCommonItem"));
            fTextColorDbORCommonItem.setJField(DbORCommonItemStyle.class
                    .getDeclaredField("m_textColorDbORCommonItem"));
            fHighlightDbORCommonItem.setJField(DbORCommonItemStyle.class
                    .getDeclaredField("m_highlightDbORCommonItem"));
            fBackgroundColorDbORCommonItem.setJField(DbORCommonItemStyle.class
                    .getDeclaredField("m_backgroundColorDbORCommonItem"));
            fOr_commonItemLengthDecimalsDisplay.setJField(DbORCommonItemStyle.class
                    .getDeclaredField("m_or_commonItemLengthDecimalsDisplay"));
            fOr_commonItemLengthDecimalsFont.setJField(DbORCommonItemStyle.class
                    .getDeclaredField("m_or_commonItemLengthDecimalsFont"));
            fReferringProjectCommonItem.setJField(DbORCommonItemStyle.class
                    .getDeclaredField("m_referringProjectCommonItem"));
            fOr_commonItemNullValueFont.setJField(DbORCommonItemStyle.class
                    .getDeclaredField("m_or_commonItemNullValueFont"));
            fOr_commonItemTypeFont.setJField(DbORCommonItemStyle.class
                    .getDeclaredField("m_or_commonItemTypeFont"));
            fOr_commonItemTypeDisplay.setJField(DbORCommonItemStyle.class
                    .getDeclaredField("m_or_commonItemTypeDisplay"));
            fOr_commonItemNullValueDisplay.setJField(DbORCommonItemStyle.class
                    .getDeclaredField("m_or_commonItemNullValueDisplay"));

            fReferringProjectCommonItem.setOppositeRel(DbSMSProject.fOrDefaultCommonItemStyle);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    private static HashMap fieldMap;
    public static String[] commonItem_listOptionTabs;
    public static final String SEQUENCE_KEYWORD = "%#";
    public static Object[][] commonItem_optionValueGroups;
    public static String[] commonItem_optionGroupHeaders;
    public static MetaField[][] commonItem_optionGroups;
    public static MetaField[] commonItem_colorOptions;
    public static Color[] commonItem_colorOptionDefaultValues;
    public static MetaField[] commonItem_fontOptions;
    public static Font[] commonItem_fontOptionDefaultValues;
    public static SMSDisplayDescriptor[] commonItem_descriptorOptionDefaultValues;
    public static MetaField[] commonItem_displayOptions;
    public static Boolean[] commonItem_displayOptionDefaultValues;
    public static MetaField[] commonItem_descriptorOptions;
    public static Boolean[] commonItem_lineOptionDefaultValues;
    public static MetaField[] commonItem_lineOptions;
    public static String DEFAULT_STYLE_NAME = LocaleMgr.misc.getString("DefaultCommonItemStyle");

    static {
        //init style of common item
        org.modelsphere.sms.db.util.DbInitialization.initCommonItemStyle();
    }

    //Instance variables
    SMSDisplayDescriptor m_or_nameDescriptor;
    SrColor m_lineColorDbORCommonItem;
    SrFont m_descriptorFontDbORCommonItem;
    SrBoolean m_dashStyleDbORCommonItem;
    SrColor m_textColorDbORCommonItem;
    SrBoolean m_highlightDbORCommonItem;
    SrColor m_backgroundColorDbORCommonItem;
    SrBoolean m_or_commonItemLengthDecimalsDisplay;
    SrFont m_or_commonItemLengthDecimalsFont;
    DbSMSProject m_referringProjectCommonItem;
    SrFont m_or_commonItemNullValueFont;
    SrFont m_or_commonItemTypeFont;
    SrBoolean m_or_commonItemTypeDisplay;
    SrBoolean m_or_commonItemNullValueDisplay;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORCommonItemStyle() {
    }

    /**
     * Creates an instance of DbORCommonItemStyle.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORCommonItemStyle(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**

 **/
    public void initOptions() throws DbException {
        for (int i = 0; i < commonItem_optionGroups.length; i++) {
            for (int j = 0; j < commonItem_optionGroups[i].length; j++) {
                basicSet(commonItem_optionGroups[i][j], commonItem_optionValueGroups[i][j]);
            }
        }
    }

    /**

 **/
    public void initNullOptions() throws DbException {
        if (getAncestor() != null)
            return;
        for (int i = 0; i < commonItem_optionGroups.length; i++) {
            for (int j = 0; j < commonItem_optionGroups[i].length; j++) {
                if (get(commonItem_optionGroups[i][j]) == null)
                    basicSet(commonItem_optionGroups[i][j], commonItem_optionValueGroups[i][j]);
            }
        }
    }

    /**
     * @param srcstyle
     *            org.modelsphere.jack.baseDb.db.DbObject
     **/
    public void copyOptions(DbObject srcStyle) throws DbException {
        for (int i = 0; i < commonItem_optionGroups.length; i++) {
            for (int j = 0; j < commonItem_optionGroups[i].length; j++) {
                MetaField metaField = commonItem_optionGroups[i][j];
                basicSet(metaField, ((DbORCommonItemStyle) srcStyle).find(metaField));
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
     * Sets the "descriptor" property of a DbORCommonItemStyle's instance.
     * 
     * @param value
     *            the "descriptor" property
     **/
    public final void setOr_nameDescriptor(SMSDisplayDescriptor value) throws DbException {
        basicSet(fOr_nameDescriptor, value);
    }

    /**
     * Sets the "common item border" property of a DbORCommonItemStyle's instance.
     * 
     * @param value
     *            the "common item border" property
     **/
    public final void setLineColorDbORCommonItem(Color value) throws DbException {
        basicSet(fLineColorDbORCommonItem, value);
    }

    /**
     * Sets the "common item descriptor" property of a DbORCommonItemStyle's instance.
     * 
     * @param value
     *            the "common item descriptor" property
     **/
    public final void setDescriptorFontDbORCommonItem(Font value) throws DbException {
        basicSet(fDescriptorFontDbORCommonItem, value);
    }

    /**
     * Sets the "common item dash style" property of a DbORCommonItemStyle's instance.
     * 
     * @param value
     *            the "common item dash style" property
     **/
    public final void setDashStyleDbORCommonItem(Boolean value) throws DbException {
        basicSet(fDashStyleDbORCommonItem, value);
    }

    /**
     * Sets the "common item text" property of a DbORCommonItemStyle's instance.
     * 
     * @param value
     *            the "common item text" property
     **/
    public final void setTextColorDbORCommonItem(Color value) throws DbException {
        basicSet(fTextColorDbORCommonItem, value);
    }

    /**
     * Sets the "common item highlight" property of a DbORCommonItemStyle's instance.
     * 
     * @param value
     *            the "common item highlight" property
     **/
    public final void setHighlightDbORCommonItem(Boolean value) throws DbException {
        basicSet(fHighlightDbORCommonItem, value);
    }

    /**
     * Sets the "common item background" property of a DbORCommonItemStyle's instance.
     * 
     * @param value
     *            the "common item background" property
     **/
    public final void setBackgroundColorDbORCommonItem(Color value) throws DbException {
        basicSet(fBackgroundColorDbORCommonItem, value);
    }

    /**
     * Sets the "common item length and decimals" property of a DbORCommonItemStyle's instance.
     * 
     * @param value
     *            the "common item length and decimals" property
     **/
    public final void setOr_commonItemLengthDecimalsDisplay(Boolean value) throws DbException {
        basicSet(fOr_commonItemLengthDecimalsDisplay, value);
    }

    /**
     * Sets the "common item length and decimals" property of a DbORCommonItemStyle's instance.
     * 
     * @param value
     *            the "common item length and decimals" property
     **/
    public final void setOr_commonItemLengthDecimalsFont(Font value) throws DbException {
        basicSet(fOr_commonItemLengthDecimalsFont, value);
    }

    /**
     * Sets the referringprojectcommonitem object associated to a DbORCommonItemStyle's instance.
     * 
     * @param value
     *            the referringprojectcommonitem object to be associated
     **/
    public final void setReferringProjectCommonItem(DbSMSProject value) throws DbException {
        basicSet(fReferringProjectCommonItem, value);
    }

    /**
     * Sets the "common item null value" property of a DbORCommonItemStyle's instance.
     * 
     * @param value
     *            the "common item null value" property
     **/
    public final void setOr_commonItemNullValueFont(Font value) throws DbException {
        basicSet(fOr_commonItemNullValueFont, value);
    }

    /**
     * Sets the "common item type descriptor" property of a DbORCommonItemStyle's instance.
     * 
     * @param value
     *            the "common item type descriptor" property
     **/
    public final void setOr_commonItemTypeFont(Font value) throws DbException {
        basicSet(fOr_commonItemTypeFont, value);
    }

    /**
     * Sets the "common item type" property of a DbORCommonItemStyle's instance.
     * 
     * @param value
     *            the "common item type" property
     **/
    public final void setOr_commonItemTypeDisplay(Boolean value) throws DbException {
        basicSet(fOr_commonItemTypeDisplay, value);
    }

    /**
     * Sets the "common item null value" property of a DbORCommonItemStyle's instance.
     * 
     * @param value
     *            the "common item null value" property
     **/
    public final void setOr_commonItemNullValueDisplay(Boolean value) throws DbException {
        basicSet(fOr_commonItemNullValueDisplay, value);
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
     * Gets the "descriptor" of a DbORCommonItemStyle's instance.
     * 
     * @return the "descriptor"
     **/
    public final SMSDisplayDescriptor getOr_nameDescriptor() throws DbException {
        return (SMSDisplayDescriptor) get(fOr_nameDescriptor);
    }

    /**
     * Gets the "common item border" of a DbORCommonItemStyle's instance.
     * 
     * @return the "common item border"
     **/
    public final Color getLineColorDbORCommonItem() throws DbException {
        return (Color) get(fLineColorDbORCommonItem);
    }

    /**
     * Gets the "common item descriptor" of a DbORCommonItemStyle's instance.
     * 
     * @return the "common item descriptor"
     **/
    public final Font getDescriptorFontDbORCommonItem() throws DbException {
        return (Font) get(fDescriptorFontDbORCommonItem);
    }

    /**
     * Gets the "common item dash style" of a DbORCommonItemStyle's instance.
     * 
     * @return the "common item dash style"
     **/
    public final Boolean getDashStyleDbORCommonItem() throws DbException {
        return (Boolean) get(fDashStyleDbORCommonItem);
    }

    /**
     * Gets the "common item text" of a DbORCommonItemStyle's instance.
     * 
     * @return the "common item text"
     **/
    public final Color getTextColorDbORCommonItem() throws DbException {
        return (Color) get(fTextColorDbORCommonItem);
    }

    /**
     * Gets the "common item highlight" of a DbORCommonItemStyle's instance.
     * 
     * @return the "common item highlight"
     **/
    public final Boolean getHighlightDbORCommonItem() throws DbException {
        return (Boolean) get(fHighlightDbORCommonItem);
    }

    /**
     * Gets the "common item background" of a DbORCommonItemStyle's instance.
     * 
     * @return the "common item background"
     **/
    public final Color getBackgroundColorDbORCommonItem() throws DbException {
        return (Color) get(fBackgroundColorDbORCommonItem);
    }

    /**
     * Gets the "common item length and decimals" of a DbORCommonItemStyle's instance.
     * 
     * @return the "common item length and decimals"
     **/
    public final Boolean getOr_commonItemLengthDecimalsDisplay() throws DbException {
        return (Boolean) get(fOr_commonItemLengthDecimalsDisplay);
    }

    /**
     * Gets the "common item length and decimals" of a DbORCommonItemStyle's instance.
     * 
     * @return the "common item length and decimals"
     **/
    public final Font getOr_commonItemLengthDecimalsFont() throws DbException {
        return (Font) get(fOr_commonItemLengthDecimalsFont);
    }

    /**
     * Gets the referringprojectcommonitem object associated to a DbORCommonItemStyle's instance.
     * 
     * @return the referringprojectcommonitem object
     **/
    public final DbSMSProject getReferringProjectCommonItem() throws DbException {
        return (DbSMSProject) get(fReferringProjectCommonItem);
    }

    /**
     * Gets the "common item null value" of a DbORCommonItemStyle's instance.
     * 
     * @return the "common item null value"
     **/
    public final Font getOr_commonItemNullValueFont() throws DbException {
        return (Font) get(fOr_commonItemNullValueFont);
    }

    /**
     * Gets the "common item type descriptor" of a DbORCommonItemStyle's instance.
     * 
     * @return the "common item type descriptor"
     **/
    public final Font getOr_commonItemTypeFont() throws DbException {
        return (Font) get(fOr_commonItemTypeFont);
    }

    /**
     * Gets the "common item type" of a DbORCommonItemStyle's instance.
     * 
     * @return the "common item type"
     **/
    public final Boolean getOr_commonItemTypeDisplay() throws DbException {
        return (Boolean) get(fOr_commonItemTypeDisplay);
    }

    /**
     * Gets the "common item null value" of a DbORCommonItemStyle's instance.
     * 
     * @return the "common item null value"
     **/
    public final Boolean getOr_commonItemNullValueDisplay() throws DbException {
        return (Boolean) get(fOr_commonItemNullValueDisplay);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
