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

import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.plugins.html.browser.htmlgeneration.HTMLGenerationHelper;
import org.modelsphere.plugins.html.browser.io.IndentWriter;

/**
 * This is an abstract class that defines the common components present in
 * different data objects.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public abstract class DataComponent implements Comparable<DataComponent>
{
	/** Parameter holding the name of the DataComponent */
	private String name;
	/** Parameter holding the index of the icon for the DataComponent */
	private int iconIndex;
	/** Parameter holding the index of the DataComponent */
	private int jsIndex;
	private boolean isInDiagramOnly;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            the name of the DataComponent
	 */
	public DataComponent(String name)
	{
		this.name = name;
		jsIndex = -1;
		isInDiagramOnly = false;
	}

	/**
	 * Obtain the name of the DataComponent.
	 * 
	 * @return the name of the DataComponent
	 */
	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	/**
	 * Obtain the icon index.
	 * 
	 * @return the icon index.
	 */
	public int getIconIndex()
	{
		return iconIndex;
	}

	/**
	 * Sets the isInDiagramOnly variable to true.
	 */
	public void setInDiagramOnly()
	{
		isInDiagramOnly = true;
	}

	/**
	 * Returns the value of isInDiagramOnly
	 * 
	 * @return the value of isInDiagramOnly
	 */
	public boolean isInDiagramOnly()
	{
		return isInDiagramOnly;
	}

	/**
	 * Sets the icon index
	 * 
	 * @param icon
	 *            an icon to use to set the icon index
	 */
	public void setIcon(Icon icon)
	{
		this.iconIndex = HTMLGenerationHelper.addIcon(icon);
	}

	/**
	 * Obtain the index number of the DataComponent.
	 * 
	 * @return the index number of the DataComponent
	 */
	public int getJSIndex()
	{
		if (jsIndex == -1)
		{
			jsIndex = HTMLGenerationHelper.getNextIndex();
		}

		return jsIndex;
	}

	/**
	 * An abstract function to be implemented to generate the JavaScript of
	 * different objects.
	 * 
	 * @param writer
	 *            the IndentWriter used to write the script
	 * @param expandCurrentLevel
	 *            whether we expand or collapse current level
	 * @throws IOException
	 * @throws DbException
	 */
	public abstract void exportSelfAsHTMLData(IndentWriter writer, boolean expandCurrentLevel) throws IOException, DbException;

	@Override
	public int compareTo(DataComponent that)
	{
		String thisName = this.getName() == null ? "" : this.getName();
		String thatName = that.getName() == null ? "" : that.getName();
		int comparison = thisName.compareTo(thatName);
		return comparison;
	}
}