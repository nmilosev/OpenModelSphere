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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.plugins.html.browser.data.DataComponent;
import org.modelsphere.plugins.html.browser.data.DataDbObject;
import org.modelsphere.plugins.html.browser.data.DataDbObjectsGroup;
import org.modelsphere.plugins.html.browser.data.DataDiagram;
import org.modelsphere.plugins.html.browser.data.extractor.smsdiagram.SMSDiagramExtractor;
import org.modelsphere.sms.db.DbSMSDiagram;

/**
 * This class is used to extract the necessary information of a project from a
 * point of entry defined by a DbObject set through the constructor so it can
 * be easily be accessible for the creation of the HTML report.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class DbObjectExtractor
{
	/** DbObject from which the information will be extracted */
	private DbObject object;

	/**
	 * Constructor
	 * 
	 * @param object
	 *            DbObject from which the information will be extracted
	 */
	public DbObjectExtractor(DbObject object)
	{
		this.object = object;
	}

	/**
	 * extractDbObject is the main function of the class. It lists the fields,
	 * the components, the diagrams and the icons of object in a DataDbObject to
	 * then be used to generate the HTML report.
	 * 
	 * @return An object containing all the information from object
	 * @throws DbException
	 *             If an error occurs while handling a DbObject
	 */
	public DataDbObject extractDbObject() throws DbException
	{
		String name = object.getName();
		DataDbObject extractedObject = new DataDbObject(name);

		FieldsExtractor fields = new FieldsExtractor(object);
		extractedObject.setFields(fields.extractFields());

		IconExtractor iconExtractor = new IconExtractor(object);
		extractedObject.setIcon(iconExtractor.extractIcon());

		ComponentsExtractor components = new ComponentsExtractor(object);
		Map<String, ArrayList<DbObject>> groupedComponents = components.extractComponents();
		Set<String> keys = groupedComponents.keySet();
		HashMap<DbObject, DataComponent> dbObjectAssociations = new HashMap<DbObject, DataComponent>();
		dbObjectAssociations.put(object, extractedObject);

		Iterator<String> keysIterator = keys.iterator();
		String keyName;
		while (keysIterator.hasNext())
		{
			keyName = keysIterator.next();
			ArrayList<DbObject> objects = groupedComponents.get(keyName);
			if (objects.size() > 1)
			{
				DataDbObjectsGroup composite = new DataDbObjectsGroup(keyName);
				for (int i = 0; i < objects.size(); i++)
				{
					DbObjectExtractor currentObjectExtractor = new DbObjectExtractor(objects.get(i));
					DataDbObject dataObject = currentObjectExtractor.extractDbObject();
					dbObjectAssociations.put(objects.get(i), dataObject);
					composite.add(dataObject);
				}

				composite.sortComponents();
				extractedObject.addComponent(composite);
			}

			else
			{
				DbObjectExtractor currentObjectExtractor = new DbObjectExtractor(objects.get(0));
				DataDbObject dataObject = currentObjectExtractor.extractDbObject();
				dbObjectAssociations.put(objects.get(0), dataObject);
				extractedObject.addComponent(dataObject);
			}
		}

		DiagramsExtractor diagramsExtractor = new DiagramsExtractor(object);
		ArrayList<DbSMSDiagram> diagrams = diagramsExtractor.extractDiagrams();
		for (DbSMSDiagram diagram : diagrams)
		{
			SMSDiagramExtractor diagramExtractor = new SMSDiagramExtractor(diagram, dbObjectAssociations);
			DataDiagram dataDiagram = diagramExtractor.extractDbSMSDiagram();
			extractedObject.addDiagram(dataDiagram);
		}

		return extractedObject;
	}
}