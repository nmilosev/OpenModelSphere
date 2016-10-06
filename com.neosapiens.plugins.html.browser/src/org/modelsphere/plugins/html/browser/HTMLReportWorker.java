/*************************************************************************

This file is part of Open ModelSphere HTML Reports Project.

Open ModelSphere HTML Reports is free software; you can redistribute
it and/or modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3 of the
License, or (at your option) any later version.

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

You can contact us at :
http://www.javaforge.com/project/3219

 **********************************************************************/

package org.modelsphere.plugins.html.browser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.plugins.PluginServices;
import org.modelsphere.plugins.html.browser.data.DataComponent;
import org.modelsphere.plugins.html.browser.data.extractor.DataExtractor;
import org.modelsphere.plugins.html.browser.data.extractor.DiagramExtractionHandler;
import org.modelsphere.plugins.html.browser.data.extractor.SemanticObjectExtractionHandler;
import org.modelsphere.plugins.html.browser.htmlgeneration.CSSFileGenerator;
import org.modelsphere.plugins.html.browser.htmlgeneration.ExplorerGenerator;
import org.modelsphere.plugins.html.browser.htmlgeneration.HTMLGenerationHelper;
import org.modelsphere.plugins.html.browser.htmlgeneration.IconsGenerator;
import org.modelsphere.plugins.html.browser.htmlgeneration.InterfaceFileGenerator;
import org.modelsphere.plugins.html.browser.htmlgeneration.InterfaceFunctionsFileGenerator;
import org.modelsphere.plugins.html.browser.htmlgeneration.JSClassesFileGenerator;
import org.modelsphere.plugins.html.browser.htmlgeneration.JSFunctionsFileGenerator;
import org.modelsphere.plugins.html.browser.htmlgeneration.images.InterfaceImagesGenerator;
import org.modelsphere.plugins.html.browser.htmlgeneration.images.ThemeManager;
import org.modelsphere.plugins.html.browser.international.LocaleMgr;
import org.modelsphere.sms.preference.DirectoryOptionGroup;

/**
 * This is an implementation of the worker class of Open ModelSphere. It is used
 * to give feedback through the controller to the user and to assure that the
 * plug-in is executed correctly.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class HTMLReportWorker extends Worker
{
	/** The name of the job */
	private String jobTitle;

	/** The point of entry of the plug-in */
	private DbObject reportRootObject;

	/** The object used to extract the data */
	private DataExtractor extractor;

	/**
	 * Constructor
	 * 
	 * @param jobTitle
	 *            The name of the job
	 * @param reportRootObject
	 *            The DbObject associated with the point of entry
	 */
	public HTMLReportWorker(String jobTitle, DbObject reportRootObject)
	{
		this.jobTitle = jobTitle;
		this.reportRootObject = reportRootObject;
		extractor = new DataExtractor();
		extractor.addHandler(new SemanticObjectExtractionHandler());
		extractor.addHandler(new DiagramExtractionHandler());
	}

	/**
	 * Returns the job title.
	 * 
	 * @return the string of the job title
	 */
	@Override
	protected String getJobTitle()
	{
		return LocaleMgr.getInstance().getString(jobTitle);
	}

	/**
	 * This function runs the plug-in's operation through the worker.
	 * 
	 * @throws Exception
	 */
	@Override
	protected void runJob() throws Exception
	{
		try
		{
			HTMLGenerationHelper.initialize();
			HTMLGenerationHelper.setController(getController());

			PluginServices.multiDbBeginTrans(Db.READ_TRANS, null);
			DataComponent dataToExport = extractData();
			generateReportFiles(dataToExport);	
			PluginServices.multiDbCommitTrans();				
		}

		catch (Throwable e)
		{
			//display error
			getController().println();
			getController().println("*** " + LocaleMgr.getInstance().getString("error") + " ***");
			getController().incrementErrorsCounter();
			
			//dump error stack
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			getController().println(sw.toString());
		}
	}

	/**
	 * This function extracts all the data of the selected project.
	 * 
	 * @return The extracted data in a DataComponent class object.
	 * @throws DbException
	 */
	private DataComponent extractData() throws DbException
	{
		getController().println("*** " + LocaleMgr.getInstance().getString("dataExtractionStart") + " ***");
		getController().println();
		getController().setStatusText(LocaleMgr.getInstance().getString("dataExtractionStatus"));

		DataComponent dataToExport;
		{
			HTMLGenerationHelper.setProjectName(reportRootObject.getProject().getName());
			String message = LocaleMgr.getInstance().getString("extracting");
			getController().println(MessageFormat.format(message, reportRootObject.getName()));
			dataToExport = extractor.extract(reportRootObject);
		}

		getController().println();
		getController().println("*** " + LocaleMgr.getInstance().getString("dataExtractionEnd") + " ***");
		getController().println();
		getController().setStatusText("");
		getController().checkPoint(50);

		return dataToExport;
	}

	/**
	 * This function extracts all the data of the selected project.
	 * 
	 * @param The
	 *            DataComponent extracted by the previous function.
	 * @throws IOException
	 */
	private void generateReportFiles(DataComponent dataRoot) throws IOException, DbException
	{
		// Set current date
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH-mm-ss");
		HTMLGenerationHelper.setProjectFolder(DirectoryOptionGroup.getHTMLGenerationDirectory() + "\\"
				+ HTMLGenerationHelper.getProjectName() + dateFormat.format(date));
		String message = "";

		getController().println("*** " + LocaleMgr.getInstance().getString("htmlGenerationStart") + " ***");
		getController().println();
		getController().setStatusText(LocaleMgr.getInstance().getString("htmlGenerationStatus"));

		// Set current theme
		ThemeManager.getThemeManager().setCurrentTheme(ThemeManager.WINDOWS_XP);

		// Folders creation
		File reportFolder = new File(HTMLGenerationHelper.getProjectFolder());
		if (!reportFolder.mkdirs())
		{
			if (! reportFolder.exists()) {
				String msg = "Cannot create: " + reportFolder.getAbsolutePath();
				throw new IOException(msg);
			}
		}

		message = LocaleMgr.getInstance().getString("createdFolder");
		getController().println(MessageFormat.format(message, reportFolder.getAbsolutePath()));

		File dataFolder = new File(HTMLGenerationHelper.getProjectFolder() + "\\data");
		if (!dataFolder.mkdirs())
		{
			if (! dataFolder.exists())  {
				String msg = "Cannot create: " + dataFolder.getAbsolutePath();
				throw new IOException(msg);
			}
		}

		message = LocaleMgr.getInstance().getString("createdFolder");
		getController().println(MessageFormat.format(message, dataFolder.getAbsolutePath()));

		File imagesFolder = new File(HTMLGenerationHelper.getProjectFolder() + "\\images");
		if (!imagesFolder.mkdirs())
		{
			if (! imagesFolder.exists()) {
				String msg = "Cannot create: " + imagesFolder.getAbsolutePath();
				throw new IOException(msg);
			}
		}

		message = LocaleMgr.getInstance().getString("createdFolder");
		getController().println(MessageFormat.format(message, imagesFolder.getAbsolutePath()));

		File diagramsFolder = new File(HTMLGenerationHelper.getProjectFolder() + "\\images\\diagrams");
		if (!diagramsFolder.mkdirs())
		{
			if (! diagramsFolder.exists()) {
				String msg = "Cannot create: " + diagramsFolder.getAbsolutePath();
				throw new IOException(msg);
			}
		}

		message = LocaleMgr.getInstance().getString("createdFolder");
		getController().println(MessageFormat.format(message, diagramsFolder.getAbsolutePath()));

		File iconsFolder = new File(HTMLGenerationHelper.getProjectFolder() + "\\images\\icons");
		if (!iconsFolder.mkdirs())
		{
			if (! iconsFolder.exists())  {
				String msg = "Cannot create: " + iconsFolder.getAbsolutePath();
				throw new IOException(msg);
			}
		}

		message = LocaleMgr.getInstance().getString("createdFolder");
		getController().println(MessageFormat.format(message, iconsFolder.getAbsolutePath()));

		File jsFolder = new File(HTMLGenerationHelper.getProjectFolder() + "\\js");
		if (!jsFolder.mkdirs())
		{
			if (!jsFolder.exists()) {
				String msg = "Cannot create: " + jsFolder.getAbsolutePath();
				throw new IOException(msg);
			}
		}

		message = LocaleMgr.getInstance().getString("createdFolder");
		getController().println(MessageFormat.format(message, jsFolder.getAbsolutePath()));

		File cssFolder = new File(HTMLGenerationHelper.getProjectFolder() + "\\css");
		if (!cssFolder.mkdirs())
		{
			if (! cssFolder.exists()) {
				String msg = "Cannot create: " + cssFolder.getAbsolutePath();
				throw new IOException(msg);
			}
		}

		message = LocaleMgr.getInstance().getString("createdFolder");
		getController().println(MessageFormat.format(message, cssFolder.getAbsolutePath()));

		// Files creation
		CSSFileGenerator cssGenerator = new CSSFileGenerator();
		cssGenerator.generateFile();

		message = LocaleMgr.getInstance().getString("createdFile");
		getController().println(MessageFormat.format(message, "style.css"));

		InterfaceFileGenerator interfaceGenerator = new InterfaceFileGenerator();
		interfaceGenerator.generateFile();

		message = LocaleMgr.getInstance().getString("createdFile");
		getController().println(MessageFormat.format(message, "index.html"));

		InterfaceImagesGenerator interfaceImagesGenerator = new InterfaceImagesGenerator();
		interfaceImagesGenerator.generateImages();

		InterfaceFunctionsFileGenerator interfaceFunctionsGenerator = new InterfaceFunctionsFileGenerator();
		interfaceFunctionsGenerator.generateFile();

		message = LocaleMgr.getInstance().getString("createdFile");
		getController().println(MessageFormat.format(message, "index.js"));

		JSClassesFileGenerator classesGenerator = new JSClassesFileGenerator();
		classesGenerator.generateFile();

		message = LocaleMgr.getInstance().getString("createdFile");
		getController().println(MessageFormat.format(message, "classes.js"));

		JSFunctionsFileGenerator functionsGenerator = new JSFunctionsFileGenerator();
		functionsGenerator.generateFile();

		message = LocaleMgr.getInstance().getString("createdFile");
		getController().println(MessageFormat.format(message, "functions.js"));

		IconsGenerator iconsGenerator = new IconsGenerator();
		iconsGenerator.generateIcons();

		getController().println(LocaleMgr.getInstance().getString("propertiesFilesGeneration"));

		//generate explorer
		ExplorerGenerator explorerGenerator = new ExplorerGenerator(dataRoot);
		explorerGenerator.generateExplorer();

		if (!HTMLGenerationHelper.isCancelled())
		{
			getController().println();
			message = LocaleMgr.getInstance().getString("htmlGenerationEnd");
			getController().println("*** " + MessageFormat.format(message, HTMLGenerationHelper.getNbFilesGenerated()) + " ***");
			getController().println();
			message = LocaleMgr.getInstance().getString("outputFolder");
			getController().println(MessageFormat.format(message, HTMLGenerationHelper.getProjectFolder()));
			getController().setStatusText("");
			getController().checkPoint(100);
		}
	}
}