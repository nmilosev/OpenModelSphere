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

import org.modelsphere.plugins.html.browser.htmlgeneration.images.ThemeManager;
import org.modelsphere.plugins.html.browser.io.IndentWriter;

/**
 * This class is used to generate a JavaScript file that will contain the
 * functions to be
 * used with the interface of the HTML report.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class InterfaceFunctionsFileGenerator
{
	/** File path of the functions file */
	private String filepath;

	/**
	 * Constructor which sets the default path for the functions file
	 */
	public InterfaceFunctionsFileGenerator()
	{
		this.filepath = HTMLGenerationHelper.getProjectFolder() + "\\js\\index.js";
	}

	/**
	 * generateFile Creates the file with the given path from filepath and
	 * writes the necessary JavaScript code to properly use the HTML report's
	 * interface.
	 * 
	 * @throws IOException
	 *             If an error occurs with the inputs or outputs
	 */
	public void generateFile() throws IOException
	{
		FileOutputStream file = new FileOutputStream(filepath);
		IndentWriter writer = new IndentWriter(file, 2, "UTF-8");

		String maximizeIcon = ThemeManager.getThemeManager().getMaximizeIcon();
		String minimizeIcon = ThemeManager.getThemeManager().getMinimizeIcon();

		writer.println("function initialize()");
		writer.println("{");
		writer.indent();
		{
			writer.println("var request = null;");
			writer.println();
			writer.println("if (window.ActiveXObject)");
			writer.indent();
			{
				writer.println("request = new ActiveXObject(\"Microsoft.XMLHTTP\");");
			}
			writer.unindent();
			writer.println();
			writer.println("else if (window.XMLHttpRequest)");
			writer.indent();
			{
				writer.println("request = new XMLHttpRequest();");
			}
			writer.unindent();
			writer.println();
			writer.println("request.open(\"GET\", \"data/explorer.html\", false);");
			writer.println("request.send(null);");
			writer.println("if(request.readyState == 4)");
			writer.println("{");
			writer.indent();
			{
				writer.println("document.getElementById(\"project\").innerHTML = request.responseText;");
			}
			writer.unindent();
			writer.println("}");
			writer.println();
			writer.println("resizeInterface();");
			writer.println("initResizeableContainer();");
			writer.println("window.onresize = resizeInterface;");
		}
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("function resizeInterface()");
		writer.println("{");
		writer.indent();
		{
			writer.println("var height = 0;");
			writer.println("if (navigator.appName.indexOf(\"Microsoft\") != -1)");
			writer.indent();
			{
				writer.println("height = document.documentElement.clientHeight;");
			}
			writer.unindent();
			writer.println();
			writer.println("else");
			writer.indent();
			{
				writer.println("height = window.innerHeight;");
			}
			writer.unindent();
			writer.println();
			writer.println("document.getElementById(\"interface\").style.height = height + \"px\";");

		}
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("function initResizeableContainer()");
		writer.println("{");
		writer.indent();
		{
			writer.println("containers = document.getElementsByTagName(\"div\");");
			writer.println("for (var i = 0; i < containers.length; i++)");
			writer.println("{");
			writer.indent();
			{
				writer.println("if (containers[i].className == \"container\")");
				writer.println("{");
				writer.indent();
				{
					writer.println("var titleBar = containers[i].getElementsByTagName(\"div\")[0];");
					writer.println("var icon = document.createElement(\"span\");");
					writer.println("icon.className = \"maximize_icon\";");
					writer.println("icon.innerHTML = \"<img src=\\\"images/" + maximizeIcon + "\\\" alt=\\\"Maximize\\\" />\";");
					writer.println("icon.setAttribute(\"onclick\", \"maximize(this.parentNode.parentNode); switchResizeableFunction(this);\");");
					writer.println("titleBar.appendChild(icon);");
				}
				writer.unindent();
				writer.println("}");
			}
			writer.unindent();
			writer.println("}");
		}
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("function switchResizeableFunction(icon)");
		writer.println("{");
		writer.indent();
		{
			writer.println("if (icon.attributes[1].nodeValue == \"maximize(this.parentNode.parentNode); switchResizeableFunction(this);\")");
			writer.println("{");
			writer.indent();
			{
				writer.println("icon.setAttribute(\"onclick\", \"minimize(this.parentNode.parentNode); switchResizeableFunction(this);\");");
				writer.println("icon.innerHTML = \"<img src=\\\"images/" + minimizeIcon + "\\\" alt=\\\"Minimize\\\" />\";");
			}
			writer.unindent();
			writer.println("}");
			writer.println();
			writer.println("else");
			writer.println("{");
			writer.indent();
			{
				writer.println("icon.setAttribute(\"onclick\", \"maximize(this.parentNode.parentNode); switchResizeableFunction(this);\");");
				writer.println("icon.innerHTML = \"<img src=\\\"images/" + maximizeIcon + "\\\" alt=\\\"Maximize\\\" />\";");
			}
			writer.unindent();
			writer.println("}");
		}
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("function openAll()");
		writer.println("{");
		writer.indent();
		{
			writer.println("var explorer = document.getElementById(\"project\");");
			writer.println("var links = explorer.getElementsByTagName(\"a\");");
			writer.println("for (var i = 0; i < links.length; i++)");
			writer.println("{");
			writer.indent();
			{
				writer.println("var content = links[i].firstChild;");
				writer.println("if (content.nodeName == \"img\" || content.nodeName == \"IMG\")");
				writer.println("{");
				writer.indent();
				{
					writer.println("if (content.src.match(\"expand.jpg\"))");
					writer.println("{");
					writer.indent();
					{
						writer.println("var li = links[i].parentNode");
						writer.println("expandList(li);");
					}
					writer.unindent();
					writer.println("}");
				}
				writer.unindent();
				writer.println("}");
			}
			writer.unindent();
			writer.println("}");
		}
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("function closeAll()");
		writer.println("{");
		writer.indent();
		{
			writer.println("var explorer = document.getElementById(\"project\");");
			writer.println("var links = explorer.getElementsByTagName(\"a\");");
			writer.println("for (var i = 0; i < links.length; i++)");
			writer.println("{");
			writer.indent();
			{
				writer.println("var content = links[i].firstChild;");
				writer.println("if (content.nodeName == \"img\" || content.nodeName == \"IMG\")");
				writer.println("{");
				writer.indent();
				{
					writer.println("if (content.src.match(\"collapse.jpg\"))");
					writer.println("{");
					writer.indent();
					{
						writer.println("var li = links[i].parentNode");
						writer.println("collapseList(li);");
					}
					writer.unindent();
					writer.println("}");
				}
				writer.unindent();
				writer.println("}");
			}
			writer.unindent();
			writer.println("}");
		}
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("function showFullText(text)");
		writer.println("{");
		writer.indent();
		{
			writer.println("var textBox = document.getElementById(\"longTextBox\");");
			writer.println("if (window.innerWidth != null)");
			writer.indent();
			{
				writer.println("textBox.style.left = (window.innerWidth / 2 - 200) + \"px\";");
			}
			writer.unindent();
			writer.println("else");
			writer.indent();
			{
				writer.println("textBox.style.left = (document.documentElement.clientWidth / 2 - 200) + \"px\";");
			}
			writer.unindent();
			writer.println();
			writer.println("if (window.innerHeight != null)");
			writer.indent();
			{
				writer.println("textBox.style.top = (window.innerHeight / 2 - 250) + \"px\";");
			}
			writer.unindent();
			writer.println("else");
			writer.indent();
			{
				writer.println("textBox.style.top = (document.documentElement.clientHeight / 2 - 150) + \"px\";");
			}
			writer.unindent();
			writer.println();
			writer.println("var textBoxContent = document.getElementById(\"longTextBoxContent\");");
			writer.println("textBoxContent.innerHTML = text.innerHTML;");
			writer.println("textBox.style.display = \"block\";");
		}
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("function hideFullText()");
		writer.println("{");
		writer.indent();
		{
			writer.println("var textBox = document.getElementById(\"longTextBox\");");
			writer.println("textBox.style.display = \"none\";");
		}
		writer.unindent();
		writer.println("}");

		writer.println("var min=8");
		writer.println("var max=24");
		writer.println("function increaseFontSize() {");
		writer.indent();
		{
			writer.println("var p = document.getElementsByTagName('div');");
			writer.println("for(i=0;i<p.length;i++) {");
			writer.indent();
			{
				writer.println("if(p[i].style.fontSize) {");
				writer.println("  var s = parseInt(p[i].style.fontSize.replace(\"px\",\"\"));");
				writer.println("} else {");
				writer.println("  var s = 12;");
				writer.println("}");
				writer.println("if(s!=max) {");
				writer.println("  s += 1;");
				writer.println("}");
				writer.println("p[i].style.fontSize = s+\"px\";");
			}
			writer.unindent();
			writer.println("}");
		}
		writer.unindent();
		writer.println("}");

		writer.println();
		writer.println("function decreaseFontSize() {");
		writer.indent();
		{
			writer.println("var p = document.getElementsByTagName('div');");
			writer.println("for(i=0;i<p.length;i++) {");
			writer.indent();
			{
				writer.println("if(p[i].style.fontSize) {");
				writer.println("  var s = parseInt(p[i].style.fontSize.replace(\"px\",\"\"));");
				writer.println("} else {");
				writer.println("  var s = 12;");
				writer.println("}");
				writer.println("if(s!=min) {");
				writer.println("  s -= 1;");
				writer.println("}");
				writer.println("p[i].style.fontSize = s+\"px\";");
			}
			writer.unindent();
			writer.println("}");
		}
		writer.unindent();
		writer.println("}");

		writer.close();
		HTMLGenerationHelper.newFileGenerated();
	}
}