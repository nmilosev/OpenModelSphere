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

package org.modelsphere.sms.plugins.report.model;

// Jack

// Sms

// JDK
import java.awt.Color;

import javax.swing.text.html.HTML;

import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.sms.plugins.report.LocaleMgr;

public class ConceptComponentProperties implements Properties {

    public static final String WIDTH_PROPERTY_KEY = "Width"; // NOT LOCALIZABLE
    //  public static final String HEIGHT_PROPERTY_KEY           = "Height";          // NOT LOCALIZABLE
    public static final String BORDER_PROPERTY_KEY = "Border"; // NOT LOCALIZABLE
    public static final String HEADER_PROPERTY_KEY = "Header"; // NOT LOCALIZABLE
    public static final String HEADERBACKGROUND_PROPERTY_KEY = "HeaderBackground";// NOT LOCALIZABLE
    public static final String HEADERTEXT_PROPERTY_KEY = "HeaderText"; // NOT LOCALIZABLE

    public static final String BORDERCOLOR_PROPERTY_KEY = "BorderColor"; // NOT LOCALIZABLE
    public static final String WIDTHUNIT_PROPERTY_KEY = "WidthUnit"; // NOT LOCALIZABLE
    //  public static final String HEIGHTUNIT_PROPERTY_KEY       = "HeightUnit";      // NOT LOCALIZABLE
    public static final String CELLSPACING_PROPERTY_KEY = "CellSpacing"; // NOT LOCALIZABLE
    public static final String CELLPADDING_PROPERTY_KEY = "CellPadding"; // NOT LOCALIZABLE

    public static final String HORIZONTALMARGIN_PROPERTY_KEY = "HorizontalMargin";// NOT LOCALIZABLE
    public static final String VERTICALMARGIN_PROPERTY_KEY = "VerticalMargin"; // NOT LOCALIZABLE

    public static final String ALIGNMENT_PROPERTY_KEY = "Alignment"; // NOT LOCALIZABLE

    public static final String TABLE_COLORS_GROUP = LocaleMgr.misc.getString("TableColors");
    public static final String TABLE_DIMENSIONS_GROUP = LocaleMgr.misc.getString("TableDimensions");
    public static final String TABLE_UNITS_GROUP = LocaleMgr.misc.getString("TableUnits");

    private PropertyGroup[] groups = new PropertyGroup[] {
    // Group 1
            new PropertyGroup(TABLE_COLORS_GROUP, // NOT LOCALIZABLE
                    new Property[] {
                            new Property(LocaleMgr.misc.getString(HEADERBACKGROUND_PROPERTY_KEY),
                                    HEADERBACKGROUND_PROPERTY_KEY, new Color(1, 147, 137)), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(HEADERTEXT_PROPERTY_KEY),
                                    HEADERTEXT_PROPERTY_KEY, Color.white), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(BORDERCOLOR_PROPERTY_KEY),
                                    BORDERCOLOR_PROPERTY_KEY, new Color(39, 61, 119)) // NOT LOCALIZABLE
                    }),

            // Group 2
            new PropertyGroup(TABLE_DIMENSIONS_GROUP, // NOT LOCALIZABLE
                    new Property[] {
                            new Property(LocaleMgr.misc.getString(WIDTH_PROPERTY_KEY),
                                    WIDTH_PROPERTY_KEY, new Integer(600)), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(WIDTHUNIT_PROPERTY_KEY),
                                    WIDTHUNIT_PROPERTY_KEY, new UnitDomain(2)), // NOT LOCALIZABLE
                            //        new Property(LocaleMgr.misc.getString(HEIGHT_PROPERTY_KEY), HEIGHT_PROPERTY_KEY, new Integer(0)), // NOT LOCALIZABLE
                            //        new Property(LocaleMgr.misc.getString(HEIGHTUNIT_PR/OPERTY_KEY), HEIGHTUNIT_PROPERTY_KEY, new UnitDomain(1)), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(BORDER_PROPERTY_KEY),
                                    BORDER_PROPERTY_KEY, new Integer(2)), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(CELLSPACING_PROPERTY_KEY),
                                    CELLSPACING_PROPERTY_KEY, new Integer(0)), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(CELLPADDING_PROPERTY_KEY),
                                    CELLPADDING_PROPERTY_KEY, new Integer(3)), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(HORIZONTALMARGIN_PROPERTY_KEY),
                                    HORIZONTALMARGIN_PROPERTY_KEY, new Integer(0)), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(VERTICALMARGIN_PROPERTY_KEY),
                                    VERTICALMARGIN_PROPERTY_KEY, new Integer(0)), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(ALIGNMENT_PROPERTY_KEY),
                                    ALIGNMENT_PROPERTY_KEY, TableAlignmentDomain
                                            .getInstance(TableAlignmentDomain.LEFT),
                                    HTML.Attribute.ALIGN) // NOT LOCALIZABLE
                    })
    /*
     * ,
     * 
     * // Group 3 new PropertyGroup( TABLE_UNITS_GROUP, // NOT LOCALIZABLE new Property[]{ new
     * Property(LocaleMgr.misc.getString(WIDTHUNIT_PROPERTY_KEY), WIDTHUNIT_PROPERTY_KEY, new
     * UnitDomain(1)), // NOT LOCALIZABLE new
     * Property(LocaleMgr.misc.getString(HEIGHTUNIT_PROPERTY_KEY), HEIGHTUNIT_PROPERTY_KEY, new
     * UnitDomain(1)) // NOT LOCALIZABLE } )
     */
    };

    private MetaClass m_metaClass;
    private MetaField m_metaField;
    private MetaClass m_parameter;

    /*
     * public ConceptComponentProperties(MetaClass metaClass, MetaField metaField) { m_metaClass =
     * metaClass; m_metaField = metaField; }
     */

    public ConceptComponentProperties(MetaClass metaClass, MetaField metaField, MetaClass parameter) {
        //this(metaClass, metaField);
        m_metaClass = metaClass;
        m_metaField = metaField;
        m_parameter = parameter;
    }

    public MetaClass getComponentMetaClass() {
        return m_parameter;
    }

    public PropertyGroup[] getProperties() {
        return groups;
    }

    public String toString() {
        /*
         * if(m_parameter!=null) return m_parameter.getGUIName(true, true);
         * 
         * return m_metaField.getGUIName();
         */
        return m_parameter.getGUIName(true, true);
    }

    public String getBaseKey() {
        /*
         * if(m_parameter==null) return
         * m_metaClass.getJClass().getName()+"."+m_metaField.getJName();
         */
        return m_metaClass.getJClass().getName() + "." + m_metaField.getJName() + "."
                + m_parameter.getJClass().getName(); // NOT LOCALIZABLE

        //return m_metaField.getMetaClass().getJClass().getName()+"."+m_metaField.getJName();
    }

    public Property getProperty(String groupName, String propertyKey) {
        PropertyGroup group = getPropertyGroup(groupName);

        if (group != null) {
            for (int i = 0; i < group.properties.length; i++) {
                if (group.properties[i].getKey().equals(propertyKey))
                    return group.properties[i];
            }
        }

        return null;
    }

    public PropertyGroup getPropertyGroup(String groupName) {
        for (int i = 0; i < groups.length; i++) {
            if (groups[i].toString().equals(groupName))
                return groups[i];
        }

        return null;
    }

}
