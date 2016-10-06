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

package org.modelsphere.plugins.html.browser.data.extractor;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.plugins.html.browser.data.DataComponent;
import org.modelsphere.plugins.html.browser.data.extractor.dbobject.DbObjectExtractor;

/**
 * This class implement the class DataExtractionHandler to handle the data
 * extraction for the semantic objects.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class SemanticObjectExtractionHandler implements DataExtractionHandler
{
	/**
	 * Verify if the object can be is of the type DbSemanticalObject and can be
	 * handled.
	 * 
	 * @param dbObject
	 *            DbObject which must be checked for handling
	 * @return true if the object can be handled, false otherwise
	 */
	@Override
	public boolean canHandle(DbObject dbObject)
	{
		if (dbObject instanceof DbSemanticalObject)
		{
			return true;
		}

		return false;
	}

	/**
	 * Extract the data of the object.
	 * 
	 * @param dbObject
	 *            DbObject from which the data must be extracted
	 * @return a DataComponent class object containing the extracted data from
	 *         the object
	 * @throws DbException
	 *             If an error occurs while handling a DbObject
	 */
	@Override
	public DataComponent extract(DbObject dbObject) throws DbException
	{
		DbObjectExtractor object = new DbObjectExtractor(dbObject);
		DataComponent extractedObject = object.extractDbObject();

		return extractedObject;
	}
}