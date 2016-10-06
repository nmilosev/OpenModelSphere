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

package org.modelsphere.plugins.html.browser.data.extractor.dbobject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.SemanticalModel;
import org.modelsphere.jack.srtool.explorer.Explorer;
import org.modelsphere.sms.db.DbSMSDiagram;

/**
 * This class is used to extract the components of a project from a point of
 * entry
 * defined by a DbObject set through the constructor so they can be easily be
 * accessible
 * for the creation of the HTML report.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class ComponentsExtractor
{
	/** DbObject from which the components will be extracted */
	private DbObject object;

	/**
	 * Constructor
	 * 
	 * @param object
	 *            DbObject from which the components are to be extracted
	 */
	public ComponentsExtractor(DbObject object)
	{
		this.object = object;
	}

	/**
	 * extractComponents is the main function of the class. It lists each
	 * component from object and then extracts its child components. The results
	 * are stored in a map.
	 * 
	 * @return A map containing the names of
	 *         each component and their child components
	 * @throws DbException
	 *             If an error occurs while handling a DbObject
	 */
	public Map<String, ArrayList<DbObject>> extractComponents() throws DbException
	{
		LinkedHashMap<String, ArrayList<DbObject>> groupedComponents = new LinkedHashMap<String, ArrayList<DbObject>>();

		DbEnumeration components = object.getComponents().elements();
		while (components.hasMoreElements())
		{
			SemanticalModel model = ApplicationContext.getSemanticalModel();
			DbObject component = components.nextElement();
			boolean isVisible = model.isVisibleOnScreen(object, component, Explorer.class);
			boolean isDiagram = component instanceof DbSMSDiagram;
			if (isVisible && !isDiagram)
			{
				String typeName = component.getMetaClass().getGUIName(true);
				boolean firstOfItsType = !groupedComponents.containsKey(typeName);
				if (firstOfItsType)
				{
					groupedComponents.put(typeName, new ArrayList<DbObject>());
				}

				groupedComponents.get(typeName).add(component);
			}
		}
		components.close();

		return groupedComponents;
	}
}