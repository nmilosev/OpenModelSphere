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

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.plugins.html.browser.international.LocaleMgr;

/**
 * This class is used to extract the fields of a project and their values from a
 * point of entry defined by a DbObject set through the constructor so they can
 * be easily be accessible for the creation of the HTML report.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class FieldsExtractor
{
	/** DbObject from which the fields will be extracted */
	private DbObject object;

	/**
	 * Constructor
	 * 
	 * @param object
	 *            DbObject from which the fields are to be extracted
	 */
	public FieldsExtractor(DbObject object)
	{
		this.object = object;
	}

	/**
	 * extractFields is the main function of the class. It lists each
	 * field from object and then extracts its value. The results
	 * are stored in a map.
	 * 
	 * @return A map containing the names of
	 *         each field and their value
	 * @throws DbException
	 *             If an error occurs while handling a DbObject
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> extractFields() throws DbException
	{
		LinkedHashMap<String, String> fieldsMap = new LinkedHashMap<String, String>();

		ArrayList<MetaField> fields = object.getMetaClass().getScreenMetaFields();
		fields = sortFields(fields);
		for (int i = 0; i < fields.size(); i++)
		{
			MetaField metaField = fields.get(i);
			String fieldName = metaField.getGUIName();
			Object fieldValueAsObject = object.get(metaField);
			String fieldValueAsString;

			if (fieldValueAsObject != null)
			{
				fieldValueAsString = fieldValueAsObject.toString();
			}
			else
			{
				fieldValueAsString = "<i>" + LocaleMgr.getInstance().getString("nullValue") + "</i>";
			}

			fieldsMap.put(fieldName, fieldValueAsString);
		}

		return fieldsMap;
	}

	/**
	 * sortFields is a function that sorts the fields so they are grouped
	 * correctly which eases the manipulation of those fields.
	 * 
	 * @param fields
	 *            An ArrayList of MetaField to sort
	 * @return the sorted list of fields
	 */
	private ArrayList<MetaField> sortFields(ArrayList<MetaField> fields)
	{
		ArrayList<MetaField> sortedFields = new ArrayList<MetaField>();
		int createdAtIndex = -1;
		int modifiedAtIndex = -1;
		int nameIndex = -1;

		for (int i = 0; i < fields.size(); i++)
		{
			MetaField metaField = fields.get(i);
			if (DbObject.fCreationTime == metaField)
			{
				createdAtIndex = i;
			}

			else if (DbObject.fModificationTime == metaField)
			{
				modifiedAtIndex = i;
			}

			else if (DbSemanticalObject.fName == metaField)
			{
				nameIndex = i;
			}

			else
			{
				sortedFields.add(metaField);
			}
		}

		if (createdAtIndex != -1)
		{
			sortedFields.add(fields.get(createdAtIndex));
		}

		if (modifiedAtIndex != -1)
		{
			sortedFields.add(fields.get(modifiedAtIndex));
		}

		if (nameIndex != -1)
		{
			sortedFields.add(0, fields.get(nameIndex));
		}

		return sortedFields;
	}
}