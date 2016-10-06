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

package org.modelsphere.plugins.html.browser.data.extractor.smsdiagram;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.plugins.html.browser.data.DataComponent;
import org.modelsphere.plugins.html.browser.data.DataDiagram;
import org.modelsphere.plugins.html.browser.data.extractor.dbobject.DbObjectExtractor;
import org.modelsphere.plugins.html.browser.data.extractor.dbobject.FieldsExtractor;
import org.modelsphere.plugins.html.browser.data.extractor.dbobject.IconExtractor;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSGraphicalObject;

/**
 * This class is used to extract, the dimensions, the fields and their values,
 * as well as every components and their dimensions that form a diagram. The
 * class also return an image of the diagram to be used in the generation of the
 * report.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class SMSDiagramExtractor
{
	/** DbSMSDiagram from which the data are extracted */
	private DbSMSDiagram diagram;
	/** Map which contain the components from an extracted object */
	private Map<DbObject, DataComponent> dbObjectAssociations;

	/**
	 * Constructor
	 * 
	 * @param diagram
	 *            DbSMSDiagram from which the data must be extracted
	 * @param dbObjectAssociations
	 *            Map from which contains the associations of an object with his
	 *            components
	 */
	public SMSDiagramExtractor(DbSMSDiagram diagram, Map<DbObject, DataComponent> dbObjectAssociations)
	{
		this.diagram = diagram;
		this.dbObjectAssociations = dbObjectAssociations;
	}

	/**
	 * Extract all the informations from a diagram (diagram dimensions, fields
	 * and values, components forming a diagram and their dimensions).
	 * 
	 * @return a DataDiagram class object which holds all the informations
	 *         extracted from a diagram
	 * @throws DbException
	 *             If an error occurs while handling a DbObject
	 */
	public DataDiagram extractDbSMSDiagram() throws DbException
	{
		String name = diagram.getName();
		DataDiagram extractedDiagram = new DataDiagram(diagram, name);

		//Obtain and set the diagram's fields
		FieldsExtractor fields = new FieldsExtractor(diagram);
		extractedDiagram.setFields(fields.extractFields());

		IconExtractor iconExtractor = new IconExtractor(diagram);
		extractedDiagram.setIcon(iconExtractor.extractIcon());

		//Add the name and dimensions of all components in a diagram
		DbEnumeration enumeration = diagram.getComponents().elements(DbSMSGraphicalObject.metaClass);
		Map<DataComponent, ArrayList<Rectangle>> components = new HashMap<DataComponent, ArrayList<Rectangle>>();

		//for each diagram's figure
		while (enumeration.hasMoreElements())
		{
			DbSMSGraphicalObject figure = (DbSMSGraphicalObject) enumeration.nextElement();
			DbObject semanticObject = figure.getSO();
			if (semanticObject != null)
			{
				DataComponent component;

				if (dbObjectAssociations == null)
				{
					DbObjectExtractor objectExtractor = new DbObjectExtractor(semanticObject);
					component = objectExtractor.extractDbObject();
					component.setInDiagramOnly();
				}
				else
				{
					component = dbObjectAssociations.get(semanticObject);
					if (component == null)
					{
						DbObjectExtractor objectExtractor = new DbObjectExtractor(semanticObject);
						component = objectExtractor.extractDbObject();
						component.setInDiagramOnly();
					}
				}

				Rectangle rectangle = figure.getRectangle();

				if (rectangle != null)
				{
					if (components.get(component) == null)
					{
						ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
						components.put(component, rectangles);
					}

					List<Rectangle> rectangles = components.get(component);
					rectangles.add(rectangle);;
				}
			} //end if

		} //end while 
		enumeration.close();

		extractedDiagram.setComponents(components);

		return extractedDiagram;
	}

}
