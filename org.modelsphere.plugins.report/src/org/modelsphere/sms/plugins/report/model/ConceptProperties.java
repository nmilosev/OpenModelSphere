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

// JDK
import java.awt.Color;

import javax.swing.text.html.HTML;

import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.plugins.report.LocaleMgr;
import org.modelsphere.sms.preference.DirectoryOptionGroup;

public class ConceptProperties implements Properties {

    private static final String htmlExtension = "."
            + ExtensionFileFilter.htmlFileFilter.getExtension();

    public static final String WIDTH_PROPERTY_KEY = "Width"; // NOT LOCALIZABLE
    //  public static final String HEIGHT_PROPERTY_KEY           = "Height";          // NOT LOCALIZABLE
    public static final String BORDER_PROPERTY_KEY = "Border"; // NOT LOCALIZABLE
    public static final String HEADER_PROPERTY_KEY = "Header"; // NOT LOCALIZABLE
    public static final String HEADERBACKGROUND_PROPERTY_KEY = "HeaderBackground";// NOT LOCALIZABLE
    public static final String HEADERTEXT_PROPERTY_KEY = "HeaderText"; // NOT LOCALIZABLE
    public static final String FILENAME_PROPERTY_KEY = "FileName"; // NOT LOCALIZABLE
    public static final String TITLE_PROPERTY_KEY = "Title"; // NOT LOCALIZABLE

    public static final String CELLSPACING_PROPERTY_KEY = "CellSpacing"; // NOT LOCALIZABLE
    public static final String CELLPADDING_PROPERTY_KEY = "CellPadding"; // NOT LOCALIZABLE
    public static final String HORIZONTALMARGIN_PROPERTY_KEY = "HorizontalMargin";// NOT LOCALIZABLE
    public static final String VERTICALMARGIN_PROPERTY_KEY = "VerticalMargin"; // NOT LOCALIZABLE

    public static final String BORDERCOLOR_PROPERTY_KEY = "BorderColor"; // NOT LOCALIZABLE

    public static final String WIDTHUNIT_PROPERTY_KEY = "WidthUnit"; // NOT LOCALIZABLE
    //  public static final String HEIGHTUNIT_PROPERTY_KEY       = "HeightUnit";      // NOT LOCALIZABLE
    public static final String ALIGNMENT_PROPERTY_KEY = "Alignment"; // NOT LOCALIZABLE

    public static final String TABLE_COLORS_GROUP = LocaleMgr.misc.getString("TableColors");
    public static final String TABLE_DIMENSIONS_GROUP = LocaleMgr.misc.getString("TableDimensions");
    public static final String TABLE_GENERAL_GROUP = LocaleMgr.misc.getString("TableGeneral");

    private PropertyGroup[] groups = new PropertyGroup[] {
    // Group 1
            new PropertyGroup(TABLE_COLORS_GROUP, // NOT LOCALIZABLE
                    new Property[] {
                            new Property(LocaleMgr.misc.getString(HEADERBACKGROUND_PROPERTY_KEY),
                                    HEADERBACKGROUND_PROPERTY_KEY, new Color(183, 179, 162),
                                    HTML.Attribute.BGCOLOR), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(HEADERTEXT_PROPERTY_KEY),
                                    HEADERTEXT_PROPERTY_KEY, new Color(39, 61, 119),
                                    HTML.Attribute.BGCOLOR), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(BORDERCOLOR_PROPERTY_KEY),
                                    BORDERCOLOR_PROPERTY_KEY, new Color(39, 61, 119),
                                    HTML.Attribute.BGCOLOR) // NOT LOCALIZABLE
                    }),

            // Group 2
            new PropertyGroup(TABLE_DIMENSIONS_GROUP, // NOT LOCALIZABLE
                    new Property[] {
                            new Property(LocaleMgr.misc.getString(WIDTH_PROPERTY_KEY),
                                    WIDTH_PROPERTY_KEY, new Integer(600), HTML.Attribute.WIDTH), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(WIDTHUNIT_PROPERTY_KEY),
                                    WIDTHUNIT_PROPERTY_KEY, UnitDomain
                                            .getInstance(UnitDomain.PIXEL)), // NOT LOCALIZABLE
                            //        new Property(LocaleMgr.misc.getString(HEIGHT_PROPERTY_KEY), HEIGHT_PROPERTY_KEY, new Integer(0), HTML.Attribute.HEIGHT), // NOT LOCALIZABLE
                            //        new Property(LocaleMgr.misc.getString(HEIGHTUNIT_PROPERTY_KEY), HEIGHTUNIT_PROPERTY_KEY, UnitDomain.getInstance(1)), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(BORDER_PROPERTY_KEY),
                                    BORDER_PROPERTY_KEY, new Integer(2), HTML.Attribute.BORDER), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(CELLSPACING_PROPERTY_KEY),
                                    CELLSPACING_PROPERTY_KEY, new Integer(0)), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(CELLPADDING_PROPERTY_KEY),
                                    CELLPADDING_PROPERTY_KEY, new Integer(3)), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(HORIZONTALMARGIN_PROPERTY_KEY),
                                    HORIZONTALMARGIN_PROPERTY_KEY, new Integer(0)), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(VERTICALMARGIN_PROPERTY_KEY),
                                    VERTICALMARGIN_PROPERTY_KEY, new Integer(0)) // NOT LOCALIZABLE
                    }),

            // Group 3

            new PropertyGroup(TABLE_GENERAL_GROUP, // NOT LOCALIZABLE
                    new Property[] {
                            new Property(LocaleMgr.misc.getString(FILENAME_PROPERTY_KEY),
                                    FILENAME_PROPERTY_KEY, new String()/* new File(".") */), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(TITLE_PROPERTY_KEY),
                                    TITLE_PROPERTY_KEY, new String()), // NOT LOCALIZABLE
                            new Property(LocaleMgr.misc.getString(ALIGNMENT_PROPERTY_KEY),
                                    ALIGNMENT_PROPERTY_KEY, TableAlignmentDomain
                                            .getInstance(TableAlignmentDomain.LEFT),
                                    HTML.Attribute.ALIGN) // NOT LOCALIZABLE
                    }) };

    private Concept m_concept;

    public ConceptProperties(Concept concept) {
        m_concept = concept;

        String dir = DirectoryOptionGroup.getHTMLGenerationDirectory();
        //String name = m_concept.getMetaClass().getGUIName(true, false);
        String name = m_concept.toString();//m_concept.getMetaClass().getGUIName(true, true);
        groups[2/* 3 */].properties[0].setValue(StringUtil.getValideFileName(name + htmlExtension,
                true));
        groups[2/* 3 */].properties[1].setValue(name);
    }

    public PropertyGroup[] getProperties() {
        return groups;
    }

    public Concept getConcept() {
        return m_concept;
    }

    public String toString() {
        return m_concept.toString();
    }

    public String getBaseKey() {
        return m_concept.getMetaClass().getJClass().getName();
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
