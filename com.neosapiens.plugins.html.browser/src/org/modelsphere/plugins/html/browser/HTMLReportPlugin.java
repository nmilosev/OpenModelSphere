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

package org.modelsphere.plugins.html.browser;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginServices;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;
import org.modelsphere.plugins.html.browser.international.LocaleMgr;

/**
 * This is the main class of the plug-in.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
@SuppressWarnings("deprecation")
public class HTMLReportPlugin implements Plugin2
{
	/** Necessary for plug-in execution */
	@Override
	public OptionGroup getOptionGroup()
	{
		return null;
	}

	/** Necessary for plug-in execution */
	@Override
	public PluginAction getPluginAction()
	{
		return null;
	}

	/** Necessary for plug-in execution */
	@Override
	public boolean doListenSelection()
	{
		return false;
	}

	/**
	 * The function that is ran when the plug-in is called.
	 * 
	 * @param ev
	 *            The action event needed to execute the plug-in
	 * @throws Exception
	 */
	@Override
	public void execute(ActionEvent ev) throws Exception
	{
		if (PluginServices.getSelectedSemanticalObjects().length > 1)
		{
			String message = LocaleMgr.getInstance().getString("multipleSelectionsError");
			JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(),
					message);
		}

		else
		{
			DbObject object = PluginServices.getSelectedSemanticalObjects()[0];

			//Create the controller
			String actionName = LocaleMgr.getInstance().getString("pluginAction");
			DefaultController controller = new DefaultController(actionName, false, null);

			//Create the worker
			HTMLReportWorker worker = new HTMLReportWorker("pluginAction", object);

			//Start the worker
			controller.start(worker);
		}
	}

	/** Necessary for plug-in execution */
	@Override
	public PluginSignature getSignature()
	{
		return null;
	}

	/** Necessary for plug-in execution */
	@Override
	public Class<? extends Object>[] getSupportedClasses()
	{
		return null;
	}

	/** Necessary for plug-in execution */
	@Override
	public String installAction(DefaultMainFrame frame, MainFrameMenu menuManager)
	{
		return null;
	}
}