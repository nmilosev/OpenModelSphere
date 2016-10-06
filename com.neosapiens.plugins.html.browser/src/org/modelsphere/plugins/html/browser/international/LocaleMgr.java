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

package org.modelsphere.plugins.html.browser.international;

/**
 * This class is used to determine and obtain the resources file
 * containing the parameter names in the language of the application
 * or set a default resource file if the language is not supported
 * by the plugin.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class LocaleMgr extends org.modelsphere.sms.international.LocaleMgr
{
	/**
	 * LocaleMgr that will point to the resources used to determine
	 * the language of the application and of the reports generated
	 */
	public static LocaleMgr localeManager = null;

	/**
	 * Constructor.
	 * 
	 * @param resName
	 *            the name of the resource used
	 */
	private LocaleMgr(String resName)
	{
		super(resName);
	}

	/**
	 * Obtain the resources file that contain the parameter names in the local
	 * language of the application.
	 * 
	 * @return the LocaleMgr resource used to obtain the proper parameter names
	 *         in the correct language
	 */
	public static LocaleMgr getInstance()
	{
		if (localeManager == null)
		{
			localeManager = new LocaleMgr("org.modelsphere.plugins.html.browser.international.Resources");
		}

		return localeManager;
	}
}