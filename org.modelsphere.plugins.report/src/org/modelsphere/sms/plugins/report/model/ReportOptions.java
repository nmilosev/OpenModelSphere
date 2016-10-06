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
import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.sms.preference.DirectoryOptionGroup;

public class ReportOptions {

    private boolean generateIndex = true;
    private boolean useBackgroundImage = false;
    private String backgroundImagePath = "";
    private String outputDirectory = DirectoryOptionGroup.getHTMLGenerationDirectory();
    private String diagramDir = "";
    private CheckTreeNode welcomeConceptNode;

    private ReportModel model;

    public ReportOptions(ReportModel model) {
        this.model = model;
    }

    public ReportModel getModel() {
        return model;
    }

    public boolean getGenerateIndex() {
        return this.generateIndex;
    }

    public void setGenerateIndex(boolean generateIndex) {
        this.generateIndex = generateIndex;
    }

    public boolean getUseBackgroundImage() {
        return this.useBackgroundImage;
    }

    public void setUseBackgroundImage(boolean useBackgroundImage) {
        this.useBackgroundImage = useBackgroundImage;
    }

    public String getBackgroundImage() {
        return backgroundImagePath;
    }

    public void setBackgroundImage(String newPath) {
        backgroundImagePath = newPath;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String newPath) {
        outputDirectory = newPath;
    }

    public String getDiagramDirectory() {
        return diagramDir;
    }

    public void setDiagramDirectory(String newDir) {
        diagramDir = newDir;
    }

    public CheckTreeNode getWelcomeConceptNode() {
        return this.welcomeConceptNode;
    }

    public void setWelcomeConceptNode(CheckTreeNode welcomeConceptNode) {
        this.welcomeConceptNode = welcomeConceptNode;
    }

}
