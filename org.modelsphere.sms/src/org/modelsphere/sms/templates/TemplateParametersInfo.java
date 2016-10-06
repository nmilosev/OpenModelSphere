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

package org.modelsphere.sms.templates;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;

public class TemplateParametersInfo implements BeanInfo {
    private Image m_image;

    public TemplateParametersInfo(Image image) {
        m_image = image;
    }

    public Image getIcon(int iconKind) {
        return m_image;
    }

    public BeanInfo[] getAdditionalBeanInfo() {
        return null;
    }

    public MethodDescriptor[] getMethodDescriptors() {
        return null;
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
        return null;
    }

    public int getDefaultPropertyIndex() {
        return 0;
    }

    public int getDefaultEventIndex() {
        return 0;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] descs;
        try {
            descs = new PropertyDescriptor[] { new ParameterFileDescriptor(),
                    new GenerateOwnerDescriptor(), new GenerateDefValuesDescriptor(),
                    new GenerateHeadersDescriptor(), new GenerateLowercaseKeywordsDescriptor(),
                    new GenerateTableColumnCommentsDescriptor(), new IndentationDescriptor() };
        } catch (IntrospectionException ex) {
            descs = null;
        }
        return descs;
    } // end getPropertyDescriptors()

    public BeanDescriptor getBeanDescriptor() {
        return new TemplateParametersDescriptor(TemplateParameters.class);
    }

    //
    // INNER CLASSES
    //
    private static class TemplateParametersDescriptor extends BeanDescriptor {
        TemplateParametersDescriptor(Class claz) {
            super(claz);
        }

        public String getDisplayName() {
            return "Generation Options";
        }

        public String getShortDescription() {
            return "Sets some generation options.";
        }
    }

    // PARAMETER FILE
    private static class ParameterFileDescriptor extends PropertyDescriptor {
        ParameterFileDescriptor() throws IntrospectionException {
            super("parameterFile", TemplateParameters.class);
        }

        public String getDisplayName() {
            return "Output folder";
        }

        public String getShortDescription() {
            return "It will generate comments on each table.";
        }
    }

    // GENERATE OBJECT WITH OWNER
    private static class GenerateOwnerDescriptor extends PropertyDescriptor {
        GenerateOwnerDescriptor() throws IntrospectionException {
            super("generateOwner", TemplateParameters.class);
        }

        public String getDisplayName() {
            return "Generate Owner";
        }

        public String getShortDescription() {
            return "It will generate comments on each table.";
        }
    }

    // GENERATE DEFAULT VALUES
    private static class GenerateDefValuesDescriptor extends PropertyDescriptor {
        GenerateDefValuesDescriptor() throws IntrospectionException {
            super("generateDefaultValues", TemplateParameters.class);
        }

        public String getDisplayName() {
            return "Generate Default Values";
        }

        public String getShortDescription() {
            return "It will generate default values for columns.";
        }
    }

    // GENERATE HEADERS
    private static class GenerateHeadersDescriptor extends PropertyDescriptor {
        GenerateHeadersDescriptor() throws IntrospectionException {
            super("generateHeaders", TemplateParameters.class);
        }

        public String getDisplayName() {
            return "Generate Headers";
        }

        public String getShortDescription() {
            return "It will generate default values for columns.";
        }
    }

    // LOWERCASE KEYWORDS
    private static class GenerateLowercaseKeywordsDescriptor extends PropertyDescriptor {
        GenerateLowercaseKeywordsDescriptor() throws IntrospectionException {
            super("lowerCaseKeywords", TemplateParameters.class);
        }

        public String getDisplayName() {
            return "Generate Lowercase Keywords";
        }

        public String getShortDescription() {
            return "It will generate default values for columns.";
        }
    }

    // TABLE AND COLUMN COMMENTS OPTION
    private static class GenerateTableColumnCommentsDescriptor extends PropertyDescriptor {
        GenerateTableColumnCommentsDescriptor() throws IntrospectionException {
            super("tableColumnComments", TemplateParameters.class);
        }

        public String getDisplayName() {
            return "Generate Table and Column Comments";
        }

        public String getShortDescription() {
            return "It will generate default values for columns.";
        }
    }

    // INDENTATION
    private static class IndentationDescriptor extends PropertyDescriptor {
        IndentationDescriptor() throws IntrospectionException {
            super("indentation", TemplateParameters.class);
        }

        public String getDisplayName() {
            return "Generate Table and Column Comments";
        }

        public String getShortDescription() {
            return "It will generate default values for columns.";
        }
    }
} // end TemplateParametersInfo
