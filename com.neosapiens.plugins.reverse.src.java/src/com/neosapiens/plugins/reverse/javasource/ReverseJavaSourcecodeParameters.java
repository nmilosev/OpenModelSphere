/*************************************************************************

Copyright (C) 2009 by neosapiens inc.

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

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/
/*************************************************************************

 Copyright (C) 2009 by neosapiens inc.

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

 You can reach neosapiens inc. at: 

 neosapiens inc.
 1236 Gaudias-Petitclerc
 Qu&eacute;bec, Qc, G1Y 3G2
 CANADA
 Telephone: 418-561-8403
 Fax: 418-650-2375
 http://www.neosapiens.com/
 Email: marco.savard@neosapiens.com
 gino.pelletier@neosapiens.com

 **********************************************************************/

package com.neosapiens.plugins.reverse.javasource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.modelsphere.jack.properties.PropertySet;
import org.modelsphere.sms.db.DbSMSProject;
import com.neosapiens.plugins.reverse.javasource.ui.WizardParameters;


public class ReverseJavaSourcecodeParameters extends WizardParameters {

    PropertySet properties;
    private static final String CREATE_DIAGRAMS = "CREATE_DIAGRAMS";
    private static final String CREATE_FIELDS = "CREATE_FIELDS";
    private static final String CREATE_METHODS = "CREATE_METHODS";
    private File m_selectedFolder;
    private DbSMSProject m_project;
    private List<File> m_filesToImport = new ArrayList<File>();
    
    public long totalSize = 0L;
    public int nbFilesScanned = 0;
    public int nbFoldersScanned = 0;
    public int nbJavaFiles = 0;
    /*
    public int nbClassFiles = 0;
    public int nbJarFiles = 0;
    */
    public boolean createNewProject = false;
    public boolean createDiagrams = true;
    public boolean createFields = true;
    public boolean createMethods = true;
    
    
    private void loadProperties() {
        properties = PropertySet.getInstance(ReverseJavaSourcecodeParameters.class);
        String b = properties.getProperty(CREATE_DIAGRAMS);
        createDiagrams = "true".equals(b);

        b = properties.getProperty(CREATE_FIELDS);
        createFields = "true".equals(b);

        b = properties.getProperty(CREATE_METHODS);
        createMethods = "true".equals(b);
    }
    
    public void saveProperties() {
        properties.setProperty(CREATE_DIAGRAMS, Boolean.toString(createDiagrams));
        properties.setProperty(CREATE_FIELDS, Boolean.toString(createFields));
        properties.setProperty(CREATE_METHODS, Boolean.toString(createMethods));
    }
    
    public ReverseJavaSourcecodeParameters() {
        loadProperties();
    }
    
    
    public void setSelectedFolder(File folder) {
        m_selectedFolder = folder;
    }
    

    public File getSelectedFolder() {
        return m_selectedFolder;
    }
    
    
    public DbSMSProject getOutputProject() {
        return createNewProject ? null : m_project;
    }
    

    public void setOutputProject(DbSMSProject project) {
        m_project = project;
    }
    
    
    public List<File> getFilesToImport() {
        return m_filesToImport;
    }
    

    public void addFileToImport(File file) {
        m_filesToImport.add(file);
    }
    
    
    public void clearFilesToImport() {
        m_filesToImport.clear();
    }
    
    public boolean mustCreateDiagrams(){
        return createDiagrams;
    }
    
    public boolean mustCreateMethods(){
        return createMethods;
    }
    
    public boolean mustCreateFields(){
        return createFields;
    }
}
