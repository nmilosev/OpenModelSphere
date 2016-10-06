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

import org.modelsphere.plugins.html.browser.io.IndentWriter;

/**
 * This class is used to generate a JavaScript file that will be used with the
 * HTML
 * report. It will contain the functions needed to make the report work
 * correctly.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class JSFunctionsFileGenerator
{
	/** File path of the functions file */
	private String filepath;

	/**
	 * Constructor which sets the default path for the functions file
	 */
	public JSFunctionsFileGenerator()
	{
		this.filepath = HTMLGenerationHelper.getProjectFolder() + "\\js\\functions.js";
	}

	/**
	 * generateFile Creates the file with the given path from filepath and
	 * writes the necessary JavaScript code for the functions needed by the
	 * report.
	 * 
	 * @throws IOException
	 *             If an error occurs with the inputs or outputs
	 */
	public void generateFile() throws IOException
	{
		FileOutputStream file = new FileOutputStream(filepath);
		IndentWriter writer = new IndentWriter(file, 2, "UTF-8");

		writer.println("function collapseList(htmlLiElement)");
		writer.println("{");
		writer.indent();
		{
			writer.println("var links = htmlLiElement.getElementsByTagName(\"a\");");
			writer.println("links[0].setAttribute(\"onclick\", \"expandList(this.parentNode); return false;\");");
			writer.println("links[0].innerHTML = \"<img src=\\\"images/expand.jpg\\\" alt=\\\"Expand\\\" />\";");
			writer.println("htmlLiElement.className = \"collapsed_list\";");
		}
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("function expandList(htmlLiElement)");
		writer.println("{");
		writer.indent();
		{
			writer.println("var links = htmlLiElement.getElementsByTagName(\"a\");");
			writer.println("links[0].setAttribute(\"onclick\", \"collapseList(this.parentNode); return false;\");");
			writer.println("links[0].innerHTML = \"<img src=\\\"images/collapse.jpg\\\" alt=\\\"Collapse\\\" />\";");
			writer.println("htmlLiElement.className = \"\";");
		}
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("var containingElement = null;");
		writer.println("var nextSibling = null;");
		writer.println();

		writer.println("function maximize(container)");
		writer.println("{");
		writer.indent();
		{
			writer.println("var maximizedContainer = document.getElementById(\"maximized\");");
			writer.println("if (maximizedContainer != null)");
			writer.println("{");
			writer.indent();
			{
				writer.println("document.getElementById(\"interface\").removeChild(maximizedContainer);");
				writer.println("containingElement.insertBefore(container, nextSibling);");
				writer.println("maximizedContainer.removeAttribute(\"id\");");
			}
			writer.unindent();
			writer.println("}");
			writer.println();
			writer.println("containingElement = container.parentNode;");
			writer.println("nextSibling = container.nextSibling;");
			writer.println("containingElement.removeChild(container);");
			writer.println("document.getElementById(\"interface\").appendChild(container);");
			writer.println("container.id = \"maximized\";");
		}
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("function minimize(container)");
		writer.println("{");
		writer.indent();
		{
			writer.println("document.getElementById(\"interface\").removeChild(container);");
			writer.println("containingElement.insertBefore(container, nextSibling);");
			writer.println("container.removeAttribute(\"id\");");
		}
		writer.unindent();
		writer.println("}");

		writer.close();
		HTMLGenerationHelper.newFileGenerated();
	}
}