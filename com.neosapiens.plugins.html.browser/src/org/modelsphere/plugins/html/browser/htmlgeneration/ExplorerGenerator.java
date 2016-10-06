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

import java.io.FileOutputStream;
import java.io.IOException;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.plugins.html.browser.data.DataComponent;
import org.modelsphere.plugins.html.browser.international.LocaleMgr;
import org.modelsphere.plugins.html.browser.io.IndentWriter;

/**
 * This class is used to generate the file for the explorer that will be used
 * with
 * the HTML report.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class ExplorerGenerator
{
	private DataComponent dataRoot;

	public ExplorerGenerator(DataComponent dataRoot)
	{
		this.dataRoot = dataRoot;
	}

	/**
	 * generateExplorer Creates the file that implements the explorer
	 * within the HTML report.
	 * 
	 * @throws IOException
	 *             If an input or output exception occurs
	 */
	public void generateExplorer() throws IOException, DbException
	{
		FileOutputStream file = new FileOutputStream(HTMLGenerationHelper.getProjectFolder() + "\\data\\explorer.html");
		IndentWriter fileWriter = new IndentWriter(file, 2, "UTF-8");

		generateExplorerToolBar(fileWriter);

		/*
		fileWriter.println("<a onclick=\"openAll(); return false;\" href=\"#\">" + LocaleMgr.getInstance().getString("openAll") + "</a>" +
				" - " +
				"<a onclick=\"closeAll(); return false;\" href=\"#\">" + LocaleMgr.getInstance().getString("closeAll") + "</a>");
		*/

		fileWriter.println("<ul>");
		fileWriter.indent();
		{
			boolean expandFirstLevel = true;
			dataRoot.exportSelfAsHTMLData(fileWriter, expandFirstLevel);
		}
		fileWriter.unindent();
		fileWriter.println("</ul>");
		fileWriter.close();
	}

	private void generateExplorerToolBar(IndentWriter writer) throws IOException
	{
		writer.println("<table style=\"width: 100%;\">");
		writer.println("<tr><td style=\"width: 100%; text-align: right;\">");
		writer.indent();

		//decrease font size
		String pattern = "<img src=\"images/font_size_decrease.png\" title=\"{0}\" onclick=\"javascript:decreaseFontSize();\" />";
		String tooltip = LocaleMgr.getInstance().getString("fontSizeDecrease");
		String line = MessageFormat.format(pattern, new Object[] { tooltip });
		writer.println(line);

		//increase font size
		pattern = "<img src=\"images/font_size_increase.png\" title=\"{0}\" onclick=\"javascript:increaseFontSize();\" />";
		tooltip = LocaleMgr.getInstance().getString("fontSizeIncrease");
		line = MessageFormat.format(pattern, new Object[] { tooltip });
		writer.println(line);

		//print
		pattern = "<img src=\"images/print.gif\" title=\"{0}\" onclick=\"javascript:print();\" />";
		tooltip = LocaleMgr.getInstance().getString("print");
		line = MessageFormat.format(pattern, new Object[] { tooltip });
		writer.println(line);

		//open all
		pattern = "<img src=\"images/expandall.gif\" title=\"{0}\" onclick=\"openAll();\" />";
		tooltip = LocaleMgr.getInstance().getString("openAll");
		line = MessageFormat.format(pattern, new Object[] { tooltip });
		writer.println(line);

		//close all
		pattern = "<img src=\"images/collapseall.gif\" title=\"{0}\" onclick=\"closeAll();\" />";
		tooltip = LocaleMgr.getInstance().getString("closeAll");
		line = MessageFormat.format(pattern, new Object[] { tooltip });
		writer.println(line);

		writer.unindent();
		writer.println("</td></tr>");
		writer.println("</table>");
	}

}
