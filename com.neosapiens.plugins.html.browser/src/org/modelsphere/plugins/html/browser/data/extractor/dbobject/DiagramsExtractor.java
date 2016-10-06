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

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.sms.db.DbSMSDiagram;

/**
 * This class is used to extract the diagrams of a project from a
 * point of entry defined by a DbObject set through the constructor so they can
 * be easily be accessible for the creation of the HTML report.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class DiagramsExtractor
{
	/** DbObject from which the diagrams will be extracted */
	private DbObject object;

	/**
	 * Constructor
	 * 
	 * @param object
	 *            DbObject from which the diagrams are to be extracted
	 */
	public DiagramsExtractor(DbObject object)
	{
		this.object = object;
	}

	/**
	 * extractDiagrams is the main function of the class. It lists each
	 * diagram from object. The results
	 * are stored in an ArrayList.
	 * 
	 * @return A list containing the diagrams
	 * @throws DbException
	 *             If an error occurs while handling a DbObject
	 */
	public ArrayList<DbSMSDiagram> extractDiagrams() throws DbException
	{
		ArrayList<DbSMSDiagram> diagrams = new ArrayList<DbSMSDiagram>();

		DbEnumeration diagramObjects = object.getComponents().elements(DbSMSDiagram.metaClass);
		while (diagramObjects.hasMoreElements())
		{
			DbSMSDiagram diagram = (DbSMSDiagram) diagramObjects.nextElement();
			diagrams.add(diagram);
		}
		diagramObjects.close();

		return diagrams;
	}
}