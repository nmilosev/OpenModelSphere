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
import java.util.ArrayList;
import java.util.Collections;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.plugins.html.browser.htmlgeneration.HTMLGenerationHelper;
import org.modelsphere.plugins.html.browser.io.IndentWriter;

/**
 * This class represent a collection of DbObjects of the same type.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class DataDbObjectsGroup extends DataComponent
{
	/** ArrayList containing the group of objects */
	private ArrayList<DataComponent> components;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            the name of the DataDbObjectsGroup
	 */
	public DataDbObjectsGroup(String name)
	{
		super(name);

		components = new ArrayList<DataComponent>();
	}

	/**
	 * Add an object to the DataDbObjectsGroup.
	 * 
	 * @param component
	 *            the object to add
	 */
	public void add(DataComponent component)
	{
		if (!components.contains(component))
		{
			components.add(component);
		}
	}

	/**
	 * Remove an object to the DataDbObjectsGroup.
	 * 
	 * @param component
	 *            the object to remove
	 */
	public void remove(DataComponent component)
	{
		if (components.contains(component))
		{
			components.remove(component);
		}
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
		//Explorer generation
		if (!components.isEmpty())
			writer.println("<li class=\"collapsed_list\">");

		else
			writer.println("<li>");

		writer.indent();
		{
			if (!components.isEmpty())
			{
				writer.println("<a onclick=\"expandList(this.parentNode); return false;\" href=\"#\"><img src=\"images/expand.jpg\" alt=\"Expand\" /></a>");
				if (components.get(0).getIconIndex() >= 0)
				{
					writer.println("<img src=\"images/icons/icon_" + components.get(0).getIconIndex() + ".png\" alt=\"Icon\" />");
				}
			}

			else
			{
				writer.println("<img src=\"images/space.jpg\" alt=\"\" />");
			}

			writer.println(getName());

			if (!components.isEmpty())
			{
				writer.println("<ul>");
				writer.indent();
				{
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

	/**
	 * Sort components alphabetically
	 * 
	 */
	public void sortComponents()
	{
		Collections.sort(components);

	}
}