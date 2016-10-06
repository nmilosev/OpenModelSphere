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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.plugins.html.browser.htmlgeneration.HTMLGenerationHelper;
import org.modelsphere.plugins.html.browser.htmlgeneration.PropertiesFileGenerator;
import org.modelsphere.plugins.html.browser.io.IndentWriter;

/**
 * This class represent all the data present in a DbObject.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class DataDbObject extends DataComponent
{
	/** Map containing each field name and its values */
	private Map<String, String> fields;
	/** ArrayList containing the components of an object */
	private ArrayList<DataComponent> components;
	/** ArrayList containing the diagrams of an object */
	private ArrayList<DataComponent> diagrams;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            the name of the DataDbObject
	 */
	public DataDbObject(String name)
	{
		super(name);

		fields = new HashMap<String, String>();
		components = new ArrayList<DataComponent>();
		diagrams = new ArrayList<DataComponent>();
	}

	/**
	 * Set the fields of the DataDbObject.
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
	 * Obtain the fields of the DataDbObject.
	 * 
	 * @return a map with the fields of the DataDbObject
	 */
	public Map<String, String> getFields()
	{
		return fields;
	}

	/**
	 * Add a component to the DataDbObject.
	 * 
	 * @param component
	 *            the component to add
	 */
	public void addComponent(DataComponent component)
	{
		if (!components.contains(component))
		{
			components.add(component);
		}
	}

	/**
	 * Remove a component to the DataDbObject.
	 * 
	 * @param component
	 *            the component to remove
	 */
	public void removeComponent(DataComponent component)
	{
		if (components.contains(component))
		{
			components.remove(component);
		}
	}

	/**
	 * Obtain the components of the DataDbObject.
	 * 
	 * @return the components of the DataDbObject
	 */
	public ArrayList<DataComponent> getComponents()
	{
		return components;
	}

	/**
	 * Add a diagram to the DataDbObject.
	 * 
	 * @param diagram
	 *            the diagram to add
	 */
	public void addDiagram(DataComponent diagram)
	{
		if (!diagrams.contains(diagram))
		{
			diagrams.add(diagram);
		}
	}

	/**
	 * Remove a diagram to the DataDbObject.
	 * 
	 * @param diagram
	 *            the diagram to remove
	 */
	public void removeDiagram(DataComponent diagram)
	{
		if (diagrams.contains(diagram))
		{
			diagrams.remove(diagram);
		}
	}

	/**
	 * Obtain the diagrams of the DataDbObject.
	 * 
	 * @return the diagrams of the DataDbObject
	 */
	public ArrayList<DataComponent> getDiagrams()
	{
		return diagrams;
	}

	/**
	 * Create the necessary JavaScript relating to this object for the HTML
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

		//Explorer generation
		if (!components.isEmpty() || !diagrams.isEmpty())
		{
			String text = expandCurrentLevel ? "" : "collapsed_list";
			writer.println("<li class=\"" + text + "\">");
		}
		else
		{
			writer.println("<li>");
		}

		writer.indent();
		{
			if (!components.isEmpty() || !diagrams.isEmpty())
			{
				String image = expandCurrentLevel ? "collapse.jpg" : "expand.jpg";
				String altText = expandCurrentLevel ? "Collapse" : "Expand";
				String pattern = "<a onclick=\"expandList(this.parentNode); return false;\" href=\"#\"><img src=\"images/{0}\" alt=\"{1}\" /></a>";
				String msg = MessageFormat.format(pattern, new Object[] { image, altText });
				writer.println(msg);
			}

			else
			{
				writer.println("<img src=\"images/space.jpg\" alt=\"\" />");
			}

			if (getIconIndex() >= 0)
			{
				writer.println("<img src=\"images/icons/icon_" + getIconIndex() + ".png\" alt=\"Icon\" />");
			}

			writer.println("<a onclick=\"OMSObject.printProperties(" + getJSIndex() + ", this); return false;\" href=\"#\">" + getName() + "</a>");

			if (!components.isEmpty() || !diagrams.isEmpty())
			{
				writer.println("<ul>");
				writer.indent();
				{
					for (DataComponent diagram : diagrams)
					{
						diagram.exportSelfAsHTMLData(writer, false);
					}

					for (DataComponent component : components)
					{
						component.exportSelfAsHTMLData(writer, false);
						HTMLGenerationHelper.newElementsExtracted();
						if (HTMLGenerationHelper.isCancelled())
						{
							break;
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