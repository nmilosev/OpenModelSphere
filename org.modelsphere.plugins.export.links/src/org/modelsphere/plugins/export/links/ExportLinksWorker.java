/*************************************************************************

Copyright (C) 2009 neosapiens inc./MSSS

This file is part of Link Report plug-in.

Link Report plug-in is free software; you can redistribute it and/or modify
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

Link Report plug-in has been developed by neosapiens inc. for
the purposes of the Ministère de la Santé et des Services Sociaux
du Québec (MSSS).
 
You can reach neosapiens inc. at: 
  http://www.neosapiens.com

 **********************************************************************/
package org.modelsphere.plugins.export.links;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.plugins.export.links.GenerationTarget.ProjectDirective;
import org.modelsphere.plugins.export.links.international.LocaleMgr;
import org.modelsphere.plugins.export.links.wrappers.DbProjectWrapper;
import org.modelsphere.plugins.export.links.wrappers.Text;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.preference.DirectoryOptionGroup;

public class ExportLinksWorker extends Worker {
    private DbObject[] m_dbos;
    private int m_nbFilesGenerated = 0;

    public ExportLinksWorker(DbObject[] dbos) {
        m_dbos = dbos;
    }

    @Override
    protected String getJobTitle() {
        String title = LocaleMgr.misc.getString("GenerateLinkReports");
        return title;
    }

    @Override
    protected void runJob() throws Exception {

        //get targets
        List<GenerationTarget> targets = GenerationTarget.getTargets();
        m_nbFilesGenerated = 0;

        //add each selected object in the project
        DbProjectWrapper project = DbProjectWrapper.getInstance();
        project.clear();

        for (DbObject dbo : m_dbos) {
            if (dbo instanceof DbSMSProject) {
                DbSMSProject p = (DbSMSProject) dbo;
                project.addProject(p);
            } else if (dbo instanceof DbORDataModel) {
                DbORDataModel model = (DbORDataModel) dbo;
                project.addDataModel(model);
            } else if (dbo instanceof DbSMSLinkModel) {
                DbSMSLinkModel model = (DbSMSLinkModel) dbo;
                project.addLinkModel(model);
            } //end if
        } //end if

        //How many link models?
        Controller controller = this.getController();
        int nbLinkModels = project.getLinkModels().size();
        int nbLinks = project.getNbLinks();
        controller.println("# " + DbSMSLinkModel.metaClass.getGUIName(true) + " : " + nbLinkModels);
        controller.println("# " + DbSMSLink.metaClass.getGUIName(true) + " : " + nbLinks);
        controller.println();

        //for each generation target
        for (GenerationTarget target : targets) {
            generateProject(target, project);
        } //end for

        //if success
        if (controller.getErrorsCount() == 0) {
            String pattern = LocaleMgr.misc.getString("Success0FilesGenerated");
            String msg = MessageFormat.format(pattern, m_nbFilesGenerated);
            controller.println(msg);
        }
    } //end runJob()

    private void generateProject(GenerationTarget target, DbProjectWrapper project) {

        //for all directives
        Controller controller = getController();
        String defaultFolderName = DirectoryOptionGroup.getDefaultWorkingDirectory();
        File defaultFolder = new File(defaultFolderName);
        Text text = new Text();

        //get template file
        File directiveFile = target.getTemplateFile();
        List<ProjectDirective> directives = target.getProjectDirectives();
        for (ProjectDirective directive : directives) {
            //get template file (.vm)
            String templateName = directive.getTemplateName();
            File templateFile = new File(directiveFile.getParentFile(), templateName);
            boolean found = templateFile.exists();

            //create writer
            String pattern = directive.getOutputFilePattern();
            String baseName = directive.getBaseName();
            String localBaseName = LocaleMgr.misc.getString(baseName);
            String filename = MessageFormat.format(pattern, localBaseName);
            File outputFile = new File(defaultFolder, filename);

            try {
                FileWriter writer = new FileWriter(outputFile);
                VelocityWriter vw = new VelocityWriter(templateFile, target.getRoot(), writer);

                vw.setVariable("text", text, controller);
                vw.setVariable("project", project, controller);
                vw.writeObject(controller);
                vw.close();

                pattern = LocaleMgr.misc.getString("Generated0");
                String msg = MessageFormat.format(pattern, outputFile.getPath());
                controller.println(msg);
                m_nbFilesGenerated++;
            } catch (IOException ex) {
                String msg = ex.toString();
                controller.println(msg);
                controller.incrementErrorsCounter();
            } //end try

        } //end for

    } //end generateProject

}
