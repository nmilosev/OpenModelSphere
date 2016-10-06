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
public final class DbORDomainStyle extends DbSMSStyle {

    //Meta

    public static final MetaField fOr_nameDescriptor = new MetaField(LocaleMgr.db
            .getString("or_nameDescriptor"));
    public static final MetaField fHighlightDbORDomain = new MetaField(LocaleMgr.db
            .getString("highlightDbORDomain"));
    public static final MetaField fDashStyleDbORDomain = new MetaField(LocaleMgr.db
            .getString("dashStyleDbORDomain"));
    public static final MetaField fTextColorDbORDomain = new MetaField(LocaleMgr.db
            .getString("textColorDbORDomain"));
    public static final MetaField fDescriptorFontDbORDomain = new MetaField(LocaleMgr.db
            .getString("descriptorFontDbORDomain"));
    public static final MetaField fBackgroundColorDbORDomain = new MetaField(LocaleMgr.db
            .getString("backgroundColorDbORDomain"));
    public static final MetaField fLineColorDbORDomain = new MetaField(LocaleMgr.db
            .getString("lineColorDbORDomain"));
    public static final MetaRelation1 fReferringProjectDomain = new MetaRelation1(LocaleMgr.db
            .getString("referringProjectDomain"), 0);
    public static final MetaField fOr_fieldLengthDecimalsDisplay = new MetaField(LocaleMgr.db
            .getString("or_fieldLengthDecimalsDisplay"));
    public static final MetaField fOr_fieldLengthDecimalsFont = new MetaField(LocaleMgr.db
            .getString("or_fieldLengthDecimalsFont"));
    public static final MetaField fOr_fieldTypeFont = new MetaField(LocaleMgr.db
            .getString("or_fieldTypeFont"));
    public static final MetaField fOr_fieldDefaultValueDisplay = new MetaField(LocaleMgr.db
            .getString("or_fieldDefaultValueDisplay"));
    public static final MetaField fOr_fieldDefaultValueFont = new MetaField(LocaleMgr.db
            .getString("or_fieldDefaultValueFont"));
    public static final MetaField fOr_fieldDisplay = new MetaField(LocaleMgr.db
            .getString("or_fieldDisplay"));
    public static final MetaField fOr_fieldDescriptorFont = new MetaField(LocaleMgr.db
            .getString("or_fieldDescriptorFont"));
    public static final MetaField fOr_fieldTypeDisplay = new MetaField(LocaleMgr.db
            .getString("or_fieldTypeDisplay"));
    public static final MetaField fOr_domainOrderedCollectionPrefix = new MetaField(LocaleMgr.db
            .getString("or_domainOrderedCollectionPrefix"));
    public static final MetaField fOr_domainNonOrderedCollectionPrefix = new MetaField(LocaleMgr.db
            .getString("or_domainNonOrderedCollectionPrefix"));
    public static final MetaField fOr_domainOwnerDisplay = new MetaField(LocaleMgr.db
            .getString("or_domainOwnerDisplay"));
    public static final MetaField fOr_domainLengthDecimalsFont = new MetaField(LocaleMgr.db
            .getString("or_domainLengthDecimalsFont"));
    public static final MetaField fOr_domainLengthDecimalsDisplay = new MetaField(LocaleMgr.db
            .getString("or_domainLengthDecimalsDisplay"));

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db
            .getString("DbORDomainStyle"), DbORDomainStyle.class,
            new MetaField[] { fOr_nameDescriptor, fHighlightDbORDomain, fDashStyleDbORDomain,
                    fTextColorDbORDomain, fDescriptorFontDbORDomain, fBackgroundColorDbORDomain,
                    fLineColorDbORDomain, fReferringProjectDomain, fOr_fieldLengthDecimalsDisplay,
                    fOr_fieldLengthDecimalsFont, fOr_fieldTypeFont, fOr_fieldDefaultValueDisplay,
                    fOr_fieldDefaultValueFont, fOr_fieldDisplay, fOr_fieldDescriptorFont,
                    fOr_fieldTypeDisplay, fOr_domainOrderedCollectionPrefix,
                    fOr_domainNonOrderedCollectionPrefix, fOr_domainOwnerDisplay,
                    fOr_domainLengthDecimalsFont, fOr_domainLengthDecimalsDisplay },
            MetaClass.MATCHABLE | MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSStyle.metaClass);

            fOr_nameDescriptor.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_or_nameDescriptor"));
            fHighlightDbORDomain.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_highlightDbORDomain"));
            fDashStyleDbORDomain.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_dashStyleDbORDomain"));
            fTextColorDbORDomain.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_textColorDbORDomain"));
            fDescriptorFontDbORDomain.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_descriptorFontDbORDomain"));
            fBackgroundColorDbORDomain.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_backgroundColorDbORDomain"));
            fLineColorDbORDomain.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_lineColorDbORDomain"));
            fReferringProjectDomain.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_referringProjectDomain"));
            fOr_fieldLengthDecimalsDisplay.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_or_fieldLengthDecimalsDisplay"));
            fOr_fieldLengthDecimalsFont.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_or_fieldLengthDecimalsFont"));
            fOr_fieldTypeFont.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_or_fieldTypeFont"));
            fOr_fieldDefaultValueDisplay.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_or_fieldDefaultValueDisplay"));
            fOr_fieldDefaultValueFont.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_or_fieldDefaultValueFont"));
            fOr_fieldDisplay.setJField(DbORDomainStyle.class.getDeclaredField("m_or_fieldDisplay"));
            fOr_fieldDescriptorFont.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_or_fieldDescriptorFont"));
            fOr_fieldTypeDisplay.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_or_fieldTypeDisplay"));
            fOr_domainOrderedCollectionPrefix.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_or_domainOrderedCollectionPrefix"));
            fOr_domainNonOrderedCollectionPrefix.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_or_domainNonOrderedCollectionPrefix"));
            fOr_domainOwnerDisplay.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_or_domainOwnerDisplay"));
            fOr_domainLengthDecimalsFont.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_or_domainLengthDecimalsFont"));
            fOr_domainLengthDecimalsDisplay.setJField(DbORDomainStyle.class
                    .getDeclaredField("m_or_domainLengthDecimalsDisplay"));

            fReferringProjectDomain.setOppositeRel(DbSMSProject.fOrDefaultDomainStyle);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    private static HashMap fieldMap;
    public static String[] domain_listOptionTabs;
    public static final String SEQUENCE_KEYWORD = "%#";
    public static String[] domain_optionGroupHeaders;
    public static MetaField[][] domain_optionGroups;
    public static Object[][] domain_optionValueGroups;
    public static MetaField[] domain_colorOptions;
    public static Color[] domain_colorOptionDefaultValues;
    public static MetaField[] domain_fontOptions;
    public static Font[] domain_fontOptionDefaultValues;
    public static SMSDisplayDescriptor[] domain_descriptorOptionDefaultValues;
    public static MetaField[] domain_displayOptions;
    public static Boolean[] domain_displayOptionDefaultValues;
    public static MetaField[] domain_descriptorOptions;
    public static Boolean[] domain_lineOptionDefaultValues;
    public static MetaField[] domain_lineOptions;
    public static DbtPrefix[] domain_prefixOptionDefaultValues;
    public static MetaField[] domain_prefixOptions;
    public static String DEFAULT_STYLE_NAME = LocaleMgr.misc.getString("DefaultDomainStyle");

    static {
        //init style of domain
        org.modelsphere.sms.db.util.DbInitialization.initDomainStyle();
    }

    //Instance variables
    SMSDisplayDescriptor m_or_nameDescriptor;
    SrBoolean m_highlightDbORDomain;
    SrBoolean m_dashStyleDbORDomain;
    SrColor m_textColorDbORDomain;
    SrFont m_descriptorFontDbORDomain;
    SrColor m_backgroundColorDbORDomain;
    SrColor m_lineColorDbORDomain;
    DbSMSProject m_referringProjectDomain;
    SrBoolean m_or_fieldLengthDecimalsDisplay;
    SrFont m_or_fieldLengthDecimalsFont;
    SrFont m_or_fieldTypeFont;
    SrBoolean m_or_fieldDefaultValueDisplay;
    SrFont m_or_fieldDefaultValueFont;
    SrBoolean m_or_fieldDisplay;
    SrFont m_or_fieldDescriptorFont;
    SrBoolean m_or_fieldTypeDisplay;
    DbtPrefix m_or_domainOrderedCollectionPrefix;
    DbtPrefix m_or_domainNonOrderedCollectionPrefix;
    SrBoolean m_or_domainOwnerDisplay;
    SrFont m_or_domainLengthDecimalsFont;
    SrBoolean m_or_domainLengthDecimalsDisplay;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORDomainStyle() {
    }

    /**
     * Creates an instance of DbORDomainStyle.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORDomainStyle(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**

 **/
    public void initOptions() throws DbException {
        for (int i = 0; i < domain_optionGroups.length; i++) {
            for (int j = 0; j < domain_optionGroups[i].length; j++) {
                basicSet(domain_optionGroups[i][j], domain_optionValueGroups[i][j]);
            }
        }
    }

    /**

 **/
    public void initNullOptions() throws DbException {
        if (getAncestor() != null)
            return;
        for (int i = 0; i < domain_optionGroups.length; i++) {
            for (int j = 0; j < domain_optionGroups[i].length; j++) {
                if (get(domain_optionGroups[i][j]) == null)
                    basicSet(domain_optionGroups[i][j], domain_optionValueGroups[i][j]);
            }
        }
    }

    /**
     * @param srcstyle
     *            org.modelsphere.jack.baseDb.db.DbObject
     **/
    public void copyOptions(DbObject srcStyle) throws DbException {
        for (int i = 0; i < domain_optionGroups.length; i++) {
            for (int j = 0; j < domain_optionGroups[i].length; j++) {
                MetaField metaField = domain_optionGroups[i][j];
                basicSet(metaField, ((DbORDomainStyle) srcStyle).find(metaField));
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
     * Sets the "descriptor" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "descriptor" property
     **/
    public final void setOr_nameDescriptor(SMSDisplayDescriptor value) throws DbException {
        basicSet(fOr_nameDescriptor, value);
    }

    /**
     * Sets the "domain highlight" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "domain highlight" property
     **/
    public final void setHighlightDbORDomain(Boolean value) throws DbException {
        basicSet(fHighlightDbORDomain, value);
    }

    /**
     * Sets the "domain dash style" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "domain dash style" property
     **/
    public final void setDashStyleDbORDomain(Boolean value) throws DbException {
        basicSet(fDashStyleDbORDomain, value);
    }

    /**
     * Sets the "domain text" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "domain text" property
     **/
    public final void setTextColorDbORDomain(Color value) throws DbException {
        basicSet(fTextColorDbORDomain, value);
    }

    /**
     * Sets the "domain descriptor" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "domain descriptor" property
     **/
    public final void setDescriptorFontDbORDomain(Font value) throws DbException {
        basicSet(fDescriptorFontDbORDomain, value);
    }

    /**
     * Sets the "domain background" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "domain background" property
     **/
    public final void setBackgroundColorDbORDomain(Color value) throws DbException {
        basicSet(fBackgroundColorDbORDomain, value);
    }

    /**
     * Sets the "domain border" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "domain border" property
     **/
    public final void setLineColorDbORDomain(Color value) throws DbException {
        basicSet(fLineColorDbORDomain, value);
    }

    /**
     * Sets the referringprojectdomain object associated to a DbORDomainStyle's instance.
     * 
     * @param value
     *            the referringprojectdomain object to be associated
     **/
    public final void setReferringProjectDomain(DbSMSProject value) throws DbException {
        basicSet(fReferringProjectDomain, value);
    }

    /**
     * Sets the "field length and decimals" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "field length and decimals" property
     **/
    public final void setOr_fieldLengthDecimalsDisplay(Boolean value) throws DbException {
        basicSet(fOr_fieldLengthDecimalsDisplay, value);
    }

    /**
     * Sets the "field length and decimals" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "field length and decimals" property
     **/
    public final void setOr_fieldLengthDecimalsFont(Font value) throws DbException {
        basicSet(fOr_fieldLengthDecimalsFont, value);
    }

    /**
     * Sets the "field type" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "field type" property
     **/
    public final void setOr_fieldTypeFont(Font value) throws DbException {
        basicSet(fOr_fieldTypeFont, value);
    }

    /**
     * Sets the "field initial value" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "field initial value" property
     **/
    public final void setOr_fieldDefaultValueDisplay(Boolean value) throws DbException {
        basicSet(fOr_fieldDefaultValueDisplay, value);
    }

    /**
     * Sets the "field initial value" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "field initial value" property
     **/
    public final void setOr_fieldDefaultValueFont(Font value) throws DbException {
        basicSet(fOr_fieldDefaultValueFont, value);
    }

    /**
     * Sets the "field" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "field" property
     **/
    public final void setOr_fieldDisplay(Boolean value) throws DbException {
        basicSet(fOr_fieldDisplay, value);
    }

    /**
     * Sets the "field descriptor" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "field descriptor" property
     **/
    public final void setOr_fieldDescriptorFont(Font value) throws DbException {
        basicSet(fOr_fieldDescriptorFont, value);
    }

    /**
     * Sets the "field type" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "field type" property
     **/
    public final void setOr_fieldTypeDisplay(Boolean value) throws DbException {
        basicSet(fOr_fieldTypeDisplay, value);
    }

    /**
     * Sets the domain ordered collection object associated to a DbORDomainStyle's instance.
     * 
     * @param value
     *            the domain ordered collection object to be associated
     **/
    public final void setOr_domainOrderedCollectionPrefix(DbtPrefix value) throws DbException {
        basicSet(fOr_domainOrderedCollectionPrefix, value);
    }

    /**
     * Sets the domain non-ordered collection object associated to a DbORDomainStyle's instance.
     * 
     * @param value
     *            the domain non-ordered collection object to be associated
     **/
    public final void setOr_domainNonOrderedCollectionPrefix(DbtPrefix value) throws DbException {
        basicSet(fOr_domainNonOrderedCollectionPrefix, value);
    }

    /**
     * Sets the "domain user" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "domain user" property
     **/
    public final void setOr_domainOwnerDisplay(Boolean value) throws DbException {
        basicSet(fOr_domainOwnerDisplay, value);
    }

    /**
     * Sets the "domain length and decimals" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "domain length and decimals" property
     **/
    public final void setOr_domainLengthDecimalsFont(Font value) throws DbException {
        basicSet(fOr_domainLengthDecimalsFont, value);
    }

    /**
     * Sets the "domain length and decimals" property of a DbORDomainStyle's instance.
     * 
     * @param value
     *            the "domain length and decimals" property
     **/
    public final void setOr_domainLengthDecimalsDisplay(Boolean value) throws DbException {
        basicSet(fOr_domainLengthDecimalsDisplay, value);
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
     * Gets the "descriptor" of a DbORDomainStyle's instance.
     * 
     * @return the "descriptor"
     **/
    public final SMSDisplayDescriptor getOr_nameDescriptor() throws DbException {
        return (SMSDisplayDescriptor) get(fOr_nameDescriptor);
    }

    /**
     * Gets the "domain highlight" of a DbORDomainStyle's instance.
     * 
     * @return the "domain highlight"
     **/
    public final Boolean getHighlightDbORDomain() throws DbException {
        return (Boolean) get(fHighlightDbORDomain);
    }

    /**
     * Gets the "domain dash style" of a DbORDomainStyle's instance.
     * 
     * @return the "domain dash style"
     **/
    public final Boolean getDashStyleDbORDomain() throws DbException {
        return (Boolean) get(fDashStyleDbORDomain);
    }

    /**
     * Gets the "domain text" of a DbORDomainStyle's instance.
     * 
     * @return the "domain text"
     **/
    public final Color getTextColorDbORDomain() throws DbException {
        return (Color) get(fTextColorDbORDomain);
    }

    /**
     * Gets the "domain descriptor" of a DbORDomainStyle's instance.
     * 
     * @return the "domain descriptor"
     **/
    public final Font getDescriptorFontDbORDomain() throws DbException {
        return (Font) get(fDescriptorFontDbORDomain);
    }

    /**
     * Gets the "domain background" of a DbORDomainStyle's instance.
     * 
     * @return the "domain background"
     **/
    public final Color getBackgroundColorDbORDomain() throws DbException {
        return (Color) get(fBackgroundColorDbORDomain);
    }

    /**
     * Gets the "domain border" of a DbORDomainStyle's instance.
     * 
     * @return the "domain border"
     **/
    public final Color getLineColorDbORDomain() throws DbException {
        return (Color) get(fLineColorDbORDomain);
    }

    /**
     * Gets the referringprojectdomain object associated to a DbORDomainStyle's instance.
     * 
     * @return the referringprojectdomain object
     **/
    public final DbSMSProject getReferringProjectDomain() throws DbException {
        return (DbSMSProject) get(fReferringProjectDomain);
    }

    /**
     * Gets the "field length and decimals" of a DbORDomainStyle's instance.
     * 
     * @return the "field length and decimals"
     **/
    public final Boolean getOr_fieldLengthDecimalsDisplay() throws DbException {
        return (Boolean) get(fOr_fieldLengthDecimalsDisplay);
    }

    /**
     * Gets the "field length and decimals" of a DbORDomainStyle's instance.
     * 
     * @return the "field length and decimals"
     **/
    public final Font getOr_fieldLengthDecimalsFont() throws DbException {
        return (Font) get(fOr_fieldLengthDecimalsFont);
    }

    /**
     * Gets the "field type" of a DbORDomainStyle's instance.
     * 
     * @return the "field type"
     **/
    public final Font getOr_fieldTypeFont() throws DbException {
        return (Font) get(fOr_fieldTypeFont);
    }

    /**
     * Gets the "field initial value" of a DbORDomainStyle's instance.
     * 
     * @return the "field initial value"
     **/
    public final Boolean getOr_fieldDefaultValueDisplay() throws DbException {
        return (Boolean) get(fOr_fieldDefaultValueDisplay);
    }

    /**
     * Gets the "field initial value" of a DbORDomainStyle's instance.
     * 
     * @return the "field initial value"
     **/
    public final Font getOr_fieldDefaultValueFont() throws DbException {
        return (Font) get(fOr_fieldDefaultValueFont);
    }

    /**
     * Gets the "field" of a DbORDomainStyle's instance.
     * 
     * @return the "field"
     **/
    public final Boolean getOr_fieldDisplay() throws DbException {
        return (Boolean) get(fOr_fieldDisplay);
    }

    /**
     * Gets the "field descriptor" of a DbORDomainStyle's instance.
     * 
     * @return the "field descriptor"
     **/
    public final Font getOr_fieldDescriptorFont() throws DbException {
        return (Font) get(fOr_fieldDescriptorFont);
    }

    /**
     * Gets the "field type" of a DbORDomainStyle's instance.
     * 
     * @return the "field type"
     **/
    public final Boolean getOr_fieldTypeDisplay() throws DbException {
        return (Boolean) get(fOr_fieldTypeDisplay);
    }

    /**
     * Gets the domain ordered collection object associated to a DbORDomainStyle's instance.
     * 
     * @return the domain ordered collection object
     **/
    public final DbtPrefix getOr_domainOrderedCollectionPrefix() throws DbException {
        return (DbtPrefix) get(fOr_domainOrderedCollectionPrefix);
    }

    /**
     * Gets the domain non-ordered collection object associated to a DbORDomainStyle's instance.
     * 
     * @return the domain non-ordered collection object
     **/
    public final DbtPrefix getOr_domainNonOrderedCollectionPrefix() throws DbException {
        return (DbtPrefix) get(fOr_domainNonOrderedCollectionPrefix);
    }

    /**
     * Gets the "domain user" of a DbORDomainStyle's instance.
     * 
     * @return the "domain user"
     **/
    public final Boolean getOr_domainOwnerDisplay() throws DbException {
        return (Boolean) get(fOr_domainOwnerDisplay);
    }

    /**
     * Gets the "domain length and decimals" of a DbORDomainStyle's instance.
     * 
     * @return the "domain length and decimals"
     **/
    public final Font getOr_domainLengthDecimalsFont() throws DbException {
        return (Font) get(fOr_domainLengthDecimalsFont);
    }

    /**
     * Gets the "domain length and decimals" of a DbORDomainStyle's instance.
     * 
     * @return the "domain length and decimals"
     **/
    public final Boolean getOr_domainLengthDecimalsDisplay() throws DbException {
        return (Boolean) get(fOr_domainLengthDecimalsDisplay);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
