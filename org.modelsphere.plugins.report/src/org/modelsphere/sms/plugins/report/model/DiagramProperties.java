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
import java.io.File;

import javax.swing.text.html.HTML;

import org.modelsphere.sms.plugins.report.LocaleMgr;

// Sms

public class DiagramProperties implements Properties {

    public static final String DIAGRAM_GENERAL_GROUP = LocaleMgr.misc.getString("DiagramGeneral");

    public static final String ALIGNMENT_PROPERTY_KEY = "Alignment"; // NOT LOCALIZABLE
    public static final String FILENAME_PROPERTY_KEY = "FileName"; // NOT LOCALIZABLE
    public static final String TITLE_PROPERTY_KEY = "Title"; // NOT LOCALIZABLE

    private PropertyGroup[] groups = new PropertyGroup[] {
    // Group 4
    new PropertyGroup(DIAGRAM_GENERAL_GROUP, // NOT LOCALIZABLE
            new Property[] {
                    new Property(LocaleMgr.misc.getString(FILENAME_PROPERTY_KEY),
                            FILENAME_PROPERTY_KEY, new File(".")), // NOT LOCALIZABLE
                    new Property(LocaleMgr.misc.getString(TITLE_PROPERTY_KEY), TITLE_PROPERTY_KEY,
                            new String()), // NOT LOCALIZABLE
                    new Property(LocaleMgr.misc.getString(ALIGNMENT_PROPERTY_KEY),
                            ALIGNMENT_PROPERTY_KEY, TableAlignmentDomain.getInstance(1),
                            HTML.Attribute.ALIGN) // NOT LOCALIZABLE
            }) };

    private Concept m_concept;

    public DiagramProperties(Concept concept) {
        m_concept = concept;
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
