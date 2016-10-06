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

package org.modelsphere.plugins.html.browser.data;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.plugins.html.browser.htmlgeneration.DiagramGenerator;
import org.modelsphere.plugins.html.browser.htmlgeneration.HTMLGenerationHelper;
import org.modelsphere.plugins.html.browser.htmlgeneration.PropertiesFileGenerator;
import org.modelsphere.plugins.html.browser.io.IndentWriter;
import org.modelsphere.sms.db.DbSMSDiagram;

/**
 * This class represent all the data present in a diagram.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class DataDiagram extends DataComponent
{
	/** Rectangle representing the dimension of a diagram */
	//private Rectangle dimension;
	/** Map containing each field name and its values from a diagram */
	private Map<String, String> fields;
	/** Map containing the components and their dimensions of a diagram */
	private Map<DataComponent, ArrayList<Rectangle>> components;
	/** BufferedImage representing the diagram */
	//private BufferedImage diagramImage;

	private DbSMSDiagram diagram;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            the name of the DataDbObject
	 */
	public DataDiagram(DbSMSDiagram diagram, String name)
	{
		super(name);
		this.diagram = diagram;
	}

	/*
	public DataDiagram(String name, BufferedImage diagramImage)
	{
		super(name);

		dimension = new Rectangle();
		fields = new HashMap<String, String>();
		components = new HashMap<DataComponent, ArrayList<Rectangle>>();
		this.diagramImage = diagramImage;
	}*/

	/**
	 * Set the dimensions of the diagram.
	 * 
	 * @param dimension
	 *            a rectangle with the dimensions of the diagram
	 */
	/*
	public void setDimension(Rectangle dimension)
	{
		if (dimension != null)
		{
			this.dimension = dimension;
		}
	}*/

	/**
	 * Obtain the dimension of the diagram.
	 * 
	 * @return the rectangle with the dimensions of the diagram
	 */
	/*
	public Rectangle getDimension()
	{
		return dimension;
	}*/

	/**
	 * Set the fields of the DataDiagram.
	 * 
	 * @param fields
	 *            a map with the fields to set
	 */
	public void setFields(Map<String, String> fields)
	{
		if (fields != null)
		{
			this.fields = fields;
		}
	}

	/**
	 * Obtain the fields of the DataDiagram.
	 * 
	 * @return a map with the fields of the DataDiagram
	 */
	public Map<String, String> getFields()
	{
		return fields;
	}

	/**
	 * Set the components of the DataDiagram.
	 * 
	 * @param components
	 *            a map with the components to set
	 */
	public void setComponents(Map<DataComponent, ArrayList<Rectangle>> components)
	{
		if (components != null)
		{
			this.components = components;
		}
	}

	/**
	 * Obtain the components of the DataDiagram.
	 * 
	 * @return a map with the components of the DataDiagram
	 */
	public Map<DataComponent, ArrayList<Rectangle>> getComponents()
	{
		return components;
	}

	/**
	 * Create the necessary JavaScript relating to the diagram for the HTML
	 * report generation.
	 * 
	 * @param writer
	 *            the IndentWriter used to write the script
	 * @param expandCurrentLevel
	 *            whether we expand or collapse current level
	 * @throws IOException
	 */
	@Override
	public void exportSelfAsHTMLData(IndentWriter writer, boolean expandCurrentLevel) throws IOException, DbException
	{
		//Fields generation
		PropertiesFileGenerator propertiesGenerator = new PropertiesFileGenerator(fields, getJSIndex());
		propertiesGenerator.generateFile();

		//Diagram files generation
		//DiagramGenerator diagramGenerator = new DiagramGenerator(diagramImage, dimension, components, getJSIndex());
		DiagramGenerator diagramGenerator = new DiagramGenerator(diagram, components, getJSIndex());
		diagramGenerator.generateFiles();

		//Explorer generation
		writer.println("<li>");
		writer.indent();
		{
			if (!components.isEmpty())
			{
				writer.println("<a onclick=\"expandList(this.parentNode); return false;\" href=\"#\"><img src=\"images/expand.jpg\" alt=\"Expand\" /></a>");
			}

			else
			{
				writer.println("<img src=\"images/space.jpg\" alt=\"\" />");
			}

			if (getIconIndex() >= 0)
			{
				writer.println("<img src=\"images/icons/icon_" + getIconIndex() + ".png\" alt=\"Icon\" />");
			}

			writer.println("<a onclick=\"OMSObject.printProperties(" + getJSIndex() + ", this); OMSObject.printDiagram(" + getJSIndex() + ", 100, this); return false;\" href=\"#\">" + getName() + "</a>");
			if (!components.isEmpty())
			{
				writer.println("<ul>");
				writer.indent();
				{
					Set<DataComponent> keys = components.keySet();
					for (DataComponent component : keys)
					{
						if (component.isInDiagramOnly())
						{
							component.exportSelfAsHTMLData(writer, false);
							HTMLGenerationHelper.newElementsExtracted();
							if (HTMLGenerationHelper.isCancelled())
							{
								break;
							}
						}
					}
				}
				writer.unindent();
				writer.println("</ul>");
			}
		}
		writer.unindent();
		writer.println("</li>");
	}
}