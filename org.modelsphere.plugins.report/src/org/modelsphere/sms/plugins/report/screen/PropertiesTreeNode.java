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

package org.modelsphere.sms.plugins.report.screen;

// Jack

// Sms

// JDK
import java.awt.Color;
import java.io.File;

import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.baseDb.db.srtypes.IntDomain;
import org.modelsphere.sms.plugins.report.PropertiesSet;
import org.modelsphere.sms.plugins.report.model.*;
import org.modelsphere.sms.preference.DirectoryOptionGroup;

public class PropertiesTreeNode extends CheckTreeNode {

    private static final String selectedKey = "selected"; // NOT LOCALIZABLE

    // options key
    private static final String generateIndexKey = "generateIndex"; // NOT LOCALIZABLE
    private static final String useBackgroundImageKey = "usebackgroundimage"; // NOT LOCALIZABLE
    private static final String backgroundImageKey = "backgroundImage"; // NOT LOCALIZABLE
    private static final String outputDirectoryKey = "outputDirectory"; // NOT LOCALIZABLE
    private static final String diagramDirectoryKey = "diagramDirectory"; // NOT LOCALIZABLE
    private static final String welcomeConceptKey = "welcomeConcept"; // NOT LOCALIZABLE

    public PropertiesTreeNode(Object value, boolean allowchildren, boolean selected) {
        super(value, allowchildren, selected);
    }

    public void update(PropertiesSet set) {
        Object userObject = getUserObject();

        if (userObject instanceof Properties)
            update(set, (Properties) userObject);
        else if (userObject instanceof ReportOptions)
            update(set, (ReportOptions) userObject);
    }

    private void update(PropertiesSet set, ReportOptions options) {
        set.setProperty(generateIndexKey, options.getGenerateIndex());
        set.setProperty(useBackgroundImageKey, options.getUseBackgroundImage());
        set.setProperty(outputDirectoryKey, options.getOutputDirectory());
        set.setProperty(backgroundImageKey, options.getBackgroundImage());
        set.setProperty(diagramDirectoryKey, options.getDiagramDirectory());

        if (options.getWelcomeConceptNode() != null)
            set.setProperty(welcomeConceptKey, options.getWelcomeConceptNode().toString());
    }

    private void update(PropertiesSet set, Properties properties) {
        //Properties      properties = (Properties)getUserObject();
        PropertyGroup[] groups = properties.getProperties();
        String key;
        Object value;

        key = properties.getBaseKey() + "." + selectedKey; // NOT LOCALIZABLE
        set.setProperty(key, this.isSelected());

        for (int i = 0; i < groups.length; i++) {

            Property[] property = groups[i].properties;
            for (int j = 0; j < property.length; j++) {
                key = properties.getBaseKey() + "." + property[j].getKey(); // NOT LOCALIZABLE
                value = property[j].getValue();

                if (value instanceof UnitDomain) {
                    UnitDomain domain = (UnitDomain) value;
                    set.setProperty(key, domain.getValue()/*
                                                           * (String)property[j].getValue().toString(
                                                           * )
                                                           */);
                } else if (value instanceof TableAlignmentDomain) {
                    TableAlignmentDomain domain = (TableAlignmentDomain) value;
                    set.setProperty(key, domain.getValue()/*
                                                           * (String)property[j].getValue().toString(
                                                           * )
                                                           */);
                } else if (value instanceof ColumnHorizontalAlignmentDomain) {
                    ColumnHorizontalAlignmentDomain domain = (ColumnHorizontalAlignmentDomain) value;
                    set.setProperty(key, domain.getValue()/*
                                                           * (String)property[j].getValue().toString(
                                                           * )
                                                           */);
                } else if (value instanceof ColumnVerticalAlignmentDomain) {
                    ColumnVerticalAlignmentDomain domain = (ColumnVerticalAlignmentDomain) value;
                    set.setProperty(key, domain.getValue()/*
                                                           * (String)property[j].getValue().toString(
                                                           * )
                                                           */);
                } else if (value instanceof Color) {
                    Color color = (Color) value;
                    set.setProperty(key + ".r", color.getRed());
                    set.setProperty(key + ".g", color.getGreen());
                    set.setProperty(key + ".b", color.getBlue());
                } else if (value instanceof Boolean) {
                    Boolean bool = (Boolean) value;
                    set.setProperty(key, bool.booleanValue()/*
                                                             * (String)property[j].getValue().toString
                                                             * ()
                                                             */);
                } else
                    set.setProperty(key, (String) property[j].getValue().toString());

            }
        }
    }

    public void refresh(PropertiesSet set) {
        Object userObject = getUserObject();

        if (userObject instanceof Properties)
            refresh(set, (Properties) userObject);
        else if (userObject instanceof ReportOptions)
            refresh(set, (ReportOptions) userObject);
    }

    private void refresh(PropertiesSet set, ReportOptions options) {
        options.setGenerateIndex(set.getPropertyBoolean(generateIndexKey, Boolean.FALSE)
                .booleanValue());
        options.setUseBackgroundImage(set.getPropertyBoolean(useBackgroundImageKey, Boolean.FALSE)
                .booleanValue());
        options.setOutputDirectory(set.getPropertyString(outputDirectoryKey, DirectoryOptionGroup
                .getHTMLGenerationDirectory()));
        options.setBackgroundImage(set.getPropertyString(backgroundImageKey, ""));
        options.setDiagramDirectory(set.getPropertyString(diagramDirectoryKey, ""));

        if (options.getWelcomeConceptNode() != null)
            set.setProperty(welcomeConceptKey, options.getWelcomeConceptNode().toString());
        options.setWelcomeConceptNode(options.getModel().getConceptTreeNodeFromName(
                set.getPropertyString(welcomeConceptKey, "")));
    }

    private void refresh(PropertiesSet set, Properties properties) {
        //Properties      properties = (Properties)getUserObject();
        PropertyGroup[] groups = properties.getProperties();
        String key;
        Object value;

        key = properties.getBaseKey() + "." + selectedKey; // NOT LOCALIZABLE
        this.setSelected(set.getPropertyBoolean(key, Boolean.FALSE).booleanValue());

        for (int i = 0; i < groups.length; i++) {

            Property[] property = groups[i].properties;
            for (int j = 0; j < property.length; j++) {
                key = properties.getBaseKey() + "." + property[j].getKey(); // NOT LOCALIZABLE
                value = property[j].getValue();

                if (value instanceof File)
                    property[j].setValue(new File(set.getPropertyString(key, ""))); // NOT LOCALIZABLE
                else if (value instanceof UnitDomain) {
                    property[j].setValue(UnitDomain.getInstance(set.getPropertyInteger(key,
                            new Integer(1)).intValue())); // NOT LOCALIZABLE
                } else if (value instanceof TableAlignmentDomain) {
                    property[j].setValue(TableAlignmentDomain.getInstance(set.getPropertyInteger(
                            key, new Integer(1)).intValue())); // NOT LOCALIZABLE
                } else if (value instanceof ColumnHorizontalAlignmentDomain) {
                    property[j].setValue(ColumnHorizontalAlignmentDomain.getInstance(set
                            .getPropertyInteger(key, new Integer(1)).intValue())); // NOT LOCALIZABLE
                } else if (value instanceof ColumnVerticalAlignmentDomain) {
                    property[j].setValue(ColumnVerticalAlignmentDomain.getInstance(set
                            .getPropertyInteger(key, new Integer(1)).intValue())); // NOT LOCALIZABLE
                } else if (value instanceof Color) {
                    int r, g, b;
                    r = set.getPropertyInteger(key + ".r", new Integer(255)).intValue();
                    g = set.getPropertyInteger(key + ".g", new Integer(255)).intValue();
                    b = set.getPropertyInteger(key + ".b", new Integer(255)).intValue();
                    property[j].setValue(new Color(r, g, b));
                    // or
                    /*
                     * Color color = (Color)value; color.setRed(r); color.setGreen(g);
                     * color.setBlue(b);
                     */
                } else if (value instanceof Boolean) {
                    property[j].setValue(set.getPropertyBoolean(key, Boolean.FALSE)); // NOT LOCALIZABLE
                } else
                    property[j].setValue(set.getPropertyString(key, "")); // NOT LOCALIZABLE
                //System.out.println("key: "+properties[j].getKey());
            }
        }
    }

    private static final String PACKAGE_NAME = "org.modelsphere.sms.plugins.report.model."; //NOT LOCALIZABLE
    private static final String DOMAIN_SUFFIX_NAME = "Domain"; //NOT LOCALIZABLE

    private static IntDomain getDomain(String domainFileName) {
        IntDomain domain = null;
        String className = PACKAGE_NAME + domainFileName + DOMAIN_SUFFIX_NAME;
        try {
            domain = (IntDomain) Class.forName(className).newInstance();
        } catch (Exception ex) {
        }
        return domain;
    }
}
