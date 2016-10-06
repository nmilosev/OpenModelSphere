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

import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.plugins.html.browser.data.DataComponent;

/**
 * This class is used to extract the data of a DbObject if this DbObject can be
 * handled by one of the handlers from DataExtractor. The result is placed in a
 * DataComponent class object.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class DataExtractor
{
	/** ArrayList which contains all the handlers for data extraction */
	private ArrayList<DataExtractionHandler> handlers;

	/**
	 * Constructor
	 */
	public DataExtractor()
	{
		handlers = new ArrayList<DataExtractionHandler>();
	}

	/**
	 * Extract the information from a DbObject if it can be handled by the
	 * handlers.
	 * 
	 * @param dbObject
	 *            DbObject which must be handled to extract its data
	 * @return a DataComponent class object containing the extracted data from
	 *         the handled object
	 * @throws DbException
	 *             If an error occurs while handling a DbObject
	 */
	public DataComponent extract(DbObject dbObject) throws DbException
	{
		for (int i = 0; i < handlers.size(); i++)
		{
			if (handlers.get(i).canHandle(dbObject))
			{
				return handlers.get(i).extract(dbObject);
			}
		}

		return null;
	}

	/**
	 * Add an handler to the list.
	 * 
	 * @return true if the handler was added, false otherwise
	 */
	public boolean addHandler(DataExtractionHandler handler)
	{
		if (!handlers.contains(handler))
		{
			handlers.add(handler);

			return true;
		}

		return false;
	}

	/**
	 * Remove an handler to the list.
	 * 
	 * @return true if the handler was removed, false otherwise
	 */
	public boolean removeHandler(DataExtractionHandler handler)
	{
		if (handlers.contains(handler))
		{
			handlers.remove(handler);

			return true;
		}

		return false;
	}
}