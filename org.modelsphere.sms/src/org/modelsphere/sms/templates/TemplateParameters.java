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

import java.io.File;
import java.io.Serializable;

public class TemplateParameters implements Serializable {

    public TemplateParameters() {
    }

    // PARAMETER FILE
    private File m_parameterFile = null;

    public File getParameterFile() {
        return m_parameterFile;
    }

    public void setParameterFile(File parameterFile) {
        m_parameterFile = parameterFile;
    }

    // GENERATE OBJECT WITH OWNER
    private Boolean m_generateOwner = Boolean.FALSE;

    public Boolean getGenerateOwner() {
        return m_generateOwner;
    }

    public void setGenerateOwner(Boolean generateOwner) {
        m_generateOwner = generateOwner;
    }

    // GENERATE DEFAULT VALUES
    private Boolean m_generateDefaultValues = Boolean.TRUE;

    public Boolean getGenerateDefaultValues() {
        return m_generateDefaultValues;
    }

    public void setGenerateDefaultValues(Boolean generateDefaultValues) {
        m_generateDefaultValues = generateDefaultValues;
    }

    // GENERATE HEADERS
    private Boolean m_generateHeaders = Boolean.TRUE;

    public Boolean getGenerateHeaders() {
        return m_generateHeaders;
    }

    public void setGenerateHeaders(Boolean generateHeaders) {
        m_generateHeaders = generateHeaders;
    }

    // LOWERCASE
    private Boolean m_lowerCaseKeywords = Boolean.FALSE;

    public Boolean getLowerCaseKeywords() {
        return m_lowerCaseKeywords;
    }

    public void setLowerCaseKeywords(Boolean lowerCaseKeywords) {
        m_lowerCaseKeywords = lowerCaseKeywords;
    }

    // TABLE AND COLUMN COMMENTS OPTION
    private Boolean m_tableColumnComments = Boolean.FALSE;

    public Boolean getTableColumnComments() {
        return m_tableColumnComments;
    }

    public void setTableColumnComments(Boolean tableColumnComments) {
        m_tableColumnComments = tableColumnComments;
    }

    // INDENTATION
    private Integer m_indentation = new Integer(2);

    public Integer getIndentation() {
        return m_indentation;
    }

    public void setIndentation(Integer indentation) {
        m_indentation = indentation;
    }
}
