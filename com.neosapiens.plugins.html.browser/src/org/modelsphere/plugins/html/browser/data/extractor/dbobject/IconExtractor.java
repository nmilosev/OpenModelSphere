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

import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;

/**
 * This class is used to extract the icons of each element of
 * the project that appears in the explorer so they can be easily be accessible
 * for the creation of the HTML report.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class IconExtractor
{
	/** DbObject from which the icons will be extracted */
	private DbObject object;

	/**
	 * Constructor
	 * 
	 * @param object
	 *            DbObject from which the icons are to be extracted
	 */
	public IconExtractor(DbObject object)
	{
		this.object = object;
	}

	/**
	 * extractIcons is the main function of the class. It lists each component
	 * from object and then extracts the icons from each of them without
	 * duplicating similar icons.
	 * 
	 * @return The icon corresponding to the object
	 * @throws DbException
	 *             If an error occurs while handling a DbObject
	 */
	public Icon extractIcon() throws DbException
	{
		Icon icon = object.getMetaClass().getIcon();

		return icon;
	}
}