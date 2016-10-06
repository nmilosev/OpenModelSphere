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

package org.modelsphere.plugins.html.browser.htmlgeneration;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.plugins.html.browser.data.DataComponent;
import org.modelsphere.plugins.html.browser.international.LocaleMgr;
import org.modelsphere.plugins.html.browser.io.IndentWriter;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.db.DbSMSDiagram;

/**
 * This class is used to generate a file for a diagram that will be used with
 * the HTML
 * report.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class DiagramGenerator
{
	/**
	 * The list of the components contained in the diagram with their associated
	 * dimensions within it.
	 */
	private Map<DataComponent, ArrayList<Rectangle>> components;

	/** A number to identify the diagram */
	private int index;

	//The underlying diagram
	private DbSMSDiagram diagram;

	//The list of allowed zoom factors
	private static final float[] ZOOM_FACTORS = new float[] { 0.25f, 0.5f, 0.75f, 0.9f, 1.0f, 1.2f, 1.5f, 2.0f };

	/**
	 * Constructor
	 * 
	 * @param diagram
	 *            The underlying diagram
	 * @param components
	 *            A Map of the components contained in the diagram with their
	 *            associated dimensions within it.
	 * @param index
	 *            A number to help identify the diagram
	 */
	public DiagramGenerator(DbSMSDiagram diagram, Map<DataComponent, ArrayList<Rectangle>> components, int index)
	{
		this.diagram = diagram;
		this.components = components;
		this.index = index;
	}

	/**
	 * generateFiles Creates the files necessary to manipulate the diagram
	 * within the HTML report. It creates a jpeg file with the diagram and a
	 * HTML file to describe the position and size of the components inside the
	 * diagram so they can be mapped and clickable in the report.
	 * 
	 * @throws IOException
	 *             If an input or output exception occurs
	 */
	public void generateFiles() throws IOException, DbException
	{
		DiagramInternalFrame diagramInternalFrame = MainFrame.getSingleton().getDiagramInternalFrame(diagram);
		boolean deleteApplicationDiagram = false;
		ApplicationDiagram appDiagram;

		if (diagramInternalFrame != null)
		{
			appDiagram = diagramInternalFrame.getDiagram();
		}
		else
		{
			DbSemanticalObject so = (DbSemanticalObject) diagram.getComposite();
			SMSToolkit kit = SMSToolkit.getToolkit(so);
			appDiagram = new ApplicationDiagram(so, diagram, kit.createGraphicalComponentFactory(),
					MainFrame.getSingleton().getDiagramsToolGroup());
			deleteApplicationDiagram = true;
		} //end if

		if (appDiagram != null)
		{
			//generate the images
			generateImageFiles(appDiagram);

			//generate the diagram

			for (int i = 0; i < ZOOM_FACTORS.length; i++)
			{
				generateDiagramFile(appDiagram, i);
			}

			if (deleteApplicationDiagram)
				appDiagram.delete();
		}

		//String filename = MessageFormat.format("diagram_{0}_{1}_{2}.jpeg", new Object[] { this.index, 0, 0 });

		/*
		if (rectangle != null)
		{
			writer.println("<div style=\"width: " + rectangle.width + "px; height: " + rectangle.height + "px; " +
					"background-image: url(images/diagrams/" + filename + ");\" " +
					"class=\"imageContainer\">");
		}
		else
		{
			writer.println("<div style=\"width: " + 0 + "px; height: " + 0 + "px; " +
					"class=\"imageContainer\">");
		}*/
	}

	private Rectangle generateDiagramFile(ApplicationDiagram appDiagram, int zoomIndex) throws IOException, DbException
	{
		//generate the HTML file for this diagram
		String pattern = "{0}/data/diagram_{1}_{2}.html";
		float zoomFactor = ZOOM_FACTORS[zoomIndex];
		int zoom = (int) (100 * zoomFactor);
		String filename = MessageFormat.format(pattern, new Object[] { HTMLGenerationHelper.getProjectFolder(), this.index, zoom });

		//String htmlFilepath = this.getHtmlFilePath();
		FileOutputStream file = new FileOutputStream(filename);
		IndentWriter writer = new IndentWriter(file, 2, "UTF-8");

		//generate diagram tool bar 
		generateDiagramToolBar(writer, zoomIndex);

		//generate diagram pages
		Rectangle rectangle = generateDiagramPages(writer, appDiagram, zoomFactor);

		/*
		rectangle = appDiagram.getContentRect();

		//Enlarge the rectangle to include the origin (0,0)
		if (rectangle != null)
		{
			rectangle = new Rectangle(0, 0, rectangle.width + rectangle.x, rectangle.height + rectangle.y);
		}
		*/

		//image container: from here, the mouse cursor appears as a pointer above each figure
		writer.println("<div class=\"imageContainer\" />");

		writer.indent();
		{
			Set<DataComponent> keys = components.keySet();
			for (DataComponent key : keys)
			{
				ArrayList<Rectangle> rectangles = components.get(key);
				String name = key.getName();

				//for each diagram figure
				for (Rectangle position : rectangles)
				{
					String top = Integer.toString((int) (position.y * zoomFactor));
					String left = Integer.toString((int) (position.x * zoomFactor));
					String width = Integer.toString((int) (position.width * zoomFactor));
					String height = Integer.toString((int) (position.height * zoomFactor));

					pattern = "<div style=\"top: {0}px; left: {1}px; width: {2}px; height:{3}px;\" onclick=\"OMSObject.printProperties({4}, null);\" />";
					String msg = MessageFormat.format(pattern, new Object[] { top, left, width, height, key.getJSIndex() });
					writer.println(msg);

					pattern = "<img src=\"images/transparent.gif\" title=\"{0}\" width=\"{1}px\" height=\"{2}px\" />";
					msg = MessageFormat.format(pattern, new Object[] { name, width, height });
					writer.indent();
					writer.println(msg);
					writer.unindent();

					writer.println("</div>");
				} //end for				
			} //end for	
		}
		writer.unindent();
		writer.println("</div>");

		writer.close();

		HTMLGenerationHelper.newFileGenerated();

		return rectangle;
	} //end generateDiagramFile()

	private void generateDiagramToolBar(IndentWriter writer, int zoomIndex) throws IOException
	{
		writer.println("<table style=\"width: 100%;\">");

		writer.println("<tr><td style=\"width: 100%; text-align: right;\">");
		generateZoomCombo(writer, zoomIndex);
		writer.println("</td><td>");

		if (zoomIndex == (ZOOM_FACTORS.length - 1))
		{
			String pattern = "<img src=\"images/zoom_in_disabled.gif\" title=\"{0}\" />";
			String tooltip = LocaleMgr.getInstance().getString("noHigherResolution");
			String line = MessageFormat.format(pattern, new Object[] { tooltip });
			writer.println(line);
		}
		else
		{
			float zoomFactor = ZOOM_FACTORS[zoomIndex + 1];
			int zoom = (int) (zoomFactor * 100);
			String pattern = "<img src=\"images/zoom_in.gif\" title=\"{0}\" onclick=\"OMSObject.printDiagram({1}, {2}, this);\" />";
			String tooltip = LocaleMgr.getInstance().getString("zoomIn");
			String line = MessageFormat.format(pattern, new Object[] { tooltip, this.index, zoom });
			writer.println(line);
		} //end if

		writer.println("</td><td>");

		if (zoomIndex == 0)
		{
			String pattern = "<img src=\"images/zoom_out_disabled.gif\" title=\"{0}\" />";
			String tooltip = LocaleMgr.getInstance().getString("noLowerResolution");
			String line = MessageFormat.format(pattern, new Object[] { tooltip });
			writer.println(line);
		}
		else
		{
			float zoomFactor = ZOOM_FACTORS[zoomIndex - 1];
			int zoom = (int) (zoomFactor * 100);
			String pattern = "<img src=\"images/zoom_out.gif\" title=\"{0}\" onclick=\"OMSObject.printDiagram({1}, {2}, this);\" />";
			String tooltip = LocaleMgr.getInstance().getString("zoomOut");
			String line = MessageFormat.format(pattern, new Object[] { tooltip, this.index, zoom });
			writer.println(line);
		} //end if

		writer.println("</td></tr>");
		writer.println("</table>");
		writer.println("<br/>");
	}

	private static final String FORM_NAME = "zoomForm";
	private static final String SELECTION_NAME = "zoomSelection";

	private void generateZoomCombo(IndentWriter writer, int zoomIndex) throws IOException
	{
		writer.println("<form name=" + FORM_NAME + " >");

		String pattern = "<select name={0} size=1 onchange=\"OMSObject.printDiagram({1}, document.{2}.{0}.options[document.{2}.{0}.selectedIndex].value, this);\" >";
		String msg = MessageFormat.format(pattern, new Object[] { SELECTION_NAME, this.index, FORM_NAME });
		writer.println(msg);

		//onchange="alert(document.forms[0].nom_client.options[document.forms[0].nom_client.selectedIndex].value);"

		for (int i = 0; i < ZOOM_FACTORS.length; i++)
		{
			float zoomFactor = ZOOM_FACTORS[i];
			int zoom = (int) (zoomFactor * 100);
			pattern = "<option value=\"{0}\" {1}>{0} %";
			String isDefault = (i == zoomIndex) ? "selected" : "";
			msg = MessageFormat.format(pattern, new Object[] { zoom, isDefault });
			writer.println(msg);
		} //end for

		writer.println("</select>");
		writer.println("</form>");
		writer.println("");
	}

	private Rectangle generateImageFiles(ApplicationDiagram appDiagram) throws IOException
	{
		String diagramPath = HTMLGenerationHelper.getProjectFolder() + "\\images\\diagrams\\";
		File diagramFolder = new File(diagramPath);

		//for each page..
		Dimension dim = appDiagram.getNbPages();

		//print page dimension
		Dimension size = appDiagram.getPageSize();
		Rectangle rectangle = new Rectangle(0, 0, size.width, size.height);

		for (int y = 0; y < dim.height; y++)
		{
			for (int x = 0; x < dim.width; x++)
			{
				//generate the .jpeg file
				int i = (y * dim.width) + x;
				int scale = 100;
				Image image = appDiagram.createImage(i, scale);

				BufferedImage buffer = GraphicUtil.toBufferedImage(image);
				String filename = MessageFormat.format("diagram_{0}_{1}_{2}.jpeg", new Object[] { this.index, y, x });
				File page = new File(diagramFolder, filename);
				GraphicUtil.saveImageToJPEGFile(buffer, page, 1.0f);
				HTMLGenerationHelper.newFileGenerated();
			}
		} //end for

		return rectangle;
	}

	private Rectangle generateDiagramPages(IndentWriter writer, ApplicationDiagram appDiagram, float zoomFactor) throws IOException
	{
		//for each page..
		Dimension dim = appDiagram.getNbPages();

		//print page dimension
		Dimension size = appDiagram.getPageSize();
		Rectangle rectangle = new Rectangle(0, 0, size.width, size.height);
		String patn = "<div style=\"width: {0}px; height: {1}px; \" class=\"pageContainer\" >";
		String width = Integer.toString(size.width);
		String height = Integer.toString(size.height);
		String line = MessageFormat.format(patn, new Object[] { width, height });
		writer.println(line);
		writer.indent();

		for (int y = 0; y < dim.height; y++)
		{
			for (int x = 0; x < dim.width; x++)
			{
				String filename = MessageFormat.format("diagram_{0}_{1}_{2}.jpeg", new Object[] { this.index, y, x });

				//add the reference to the image in the HTML file
				String top = Integer.toString((int) (y * size.height * zoomFactor));
				String left = Integer.toString((int) (x * size.width * zoomFactor));
				width = Integer.toString((int) (size.width * zoomFactor));
				height = Integer.toString((int) (size.height * zoomFactor));
				String folder = "images/diagrams";

				//start <div>
				patn = "<div style=\"top: {0}px; left: {1}px; width: {2}px; height: {3}px; \" >";
				line = MessageFormat.format(patn, new Object[] { top, left, width, height });
				writer.println(line);

				//write <img>
				patn = "<img src=\"{0}/{1}\" width=\"{2}\" height=\"{3}\" />";
				line = MessageFormat.format(patn, new Object[] { folder, filename, width, height, });
				writer.indent();
				writer.println(line);
				writer.unindent();

				//end <div>
				writer.println("</div>");
			}
		} //end for

		writer.unindent();
		writer.println();

		return rectangle;
	}
}