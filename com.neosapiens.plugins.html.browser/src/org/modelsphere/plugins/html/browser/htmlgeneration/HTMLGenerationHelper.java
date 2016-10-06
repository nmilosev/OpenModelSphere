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

package org.modelsphere.plugins.html.browser.htmlgeneration;

import java.text.MessageFormat;
import java.util.ArrayList;

import javax.swing.Icon;

import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.plugins.html.browser.international.LocaleMgr;

/**
 * This class is used to help in the creation of the data files which each
 * contains all the information of a given element extracted from a project.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class HTMLGenerationHelper
{
	/** A counter that indicates the next element in the index */
	private static int nextIndex;

	/** The number of files generated */
	private static int numberOfFilesGenerated;

	/** The list of icons */
	private static ArrayList<Icon> icons;

	/** The path for the project folder */
	private static String projectFolder;

	/** The name of the project */
	private static String projectName;

	/** The number of elements extracted */
	private static int numberOfElementsExtracted;

	/** Indicates if the operation has been cancelled by the user */
	private static boolean cancelled;

	/** Current controller */
	private static Controller controller;

	/**
	 * initializeIndex sets the nextIndex variable to zero
	 */
	public static void initialize()
	{
		nextIndex = 0;
		numberOfFilesGenerated = 0;
		icons = new ArrayList<Icon>();
		projectFolder = "";
		projectName = "";
		numberOfElementsExtracted = 0;
		cancelled = false;
		controller = null;
	}

	/**
	 * getNextIndex increments the nextIndex variable by one
	 * 
	 * @return int The current value of nextIndex
	 */
	public static synchronized int getNextIndex()
	{
		return nextIndex++;
	}

	/**
	 * Gives the number of files generated
	 * 
	 * @return Number of files generated
	 */
	public static synchronized int getNbFilesGenerated()
	{
		return numberOfFilesGenerated;
	}

	/**
	 * Increments the number of files that have been created
	 */
	public static synchronized void newFileGenerated()
	{
		numberOfFilesGenerated++;
	}

	/**
	 * Clears the list of icons
	 */
	public static synchronized void clearIcon()
	{
		icons.clear();
	}

	/**
	 * This function adds an icon to the list
	 * 
	 * @param icon
	 *            The icon to add
	 * @return the number of icons in the list or -1 if the icon to add is null
	 */
	public static synchronized int addIcon(Icon icon)
	{
		if (icon == null)
		{
			return -1;
		}

		if (!icons.contains(icon))
		{
			icons.add(icon);
			return icons.size() - 1;
		}

		else
		{
			return icons.indexOf(icon);
		}
	}

	/**
	 * This function returns the icon at a specific index
	 * 
	 * @param index
	 *            the index of the icon to get
	 * @return The icon at index
	 */
	public static synchronized Icon getIcon(int index)
	{
		if (index >= 0 && index < icons.size())
		{
			return icons.get(index);
		}

		return null;
	}

	/**
	 * Returns the number of icons
	 * 
	 * @return the number of icons
	 */
	public static synchronized int getNbIcons()
	{
		return icons.size();
	}

	/**
	 * Sets the project's folder to a given string
	 * 
	 * @param folder
	 *            the string to set as project folder
	 */
	public static synchronized void setProjectFolder(String folder)
	{
		projectFolder = folder;
	}

	/**
	 * Returns the project folder
	 * 
	 * @return the string of the project folder
	 */
	public static synchronized String getProjectFolder()
	{
		return projectFolder;
	}

	/**
	 * Sets the project name
	 * 
	 * @param name
	 *            The string to set as project name
	 */
	public static synchronized void setProjectName(String name)
	{
		projectName = name;
	}

	/**
	 * Returns the project name
	 * 
	 * @return The string of the project name
	 */
	public static synchronized String getProjectName()
	{
		return projectName;
	}

	/**
	 * Increments the number of elements that have been extracted
	 */
	public static synchronized void newElementsExtracted()
	{
		numberOfElementsExtracted++;
		if (!controller.checkPoint())
		{
			cancelled = true;
		}

		if (numberOfElementsExtracted % 200 == 0)
		{
			String message = LocaleMgr.getInstance().getString("htmlGenerationCheckpoint");
			controller.println(MessageFormat.format(message, numberOfElementsExtracted));
		}
	}

	/**
	 * Returns the current state of the operation
	 * 
	 * @return true if the operation has been cancelled, false if the operation
	 *         should continue
	 */
	public static synchronized boolean isCancelled()
	{
		return cancelled;
	}

	/**
	 * Sets the current controller
	 * 
	 * @param newController
	 *            The new controller
	 */
	public static synchronized void setController(Controller newController)
	{
		controller = newController;
	}

	public static synchronized void displayDiagramSizeError()
	{
		controller.println(LocaleMgr.getInstance().getString("diagramSizeError"));
	}
}