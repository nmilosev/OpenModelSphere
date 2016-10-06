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
 * report. The file contains the classes necessary to make the report work.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class JSClassesFileGenerator
{
	/** File path of the classes file */
	private String filepath;

	/**
	 * Constructor which sets the default path for the classes file
	 */
	public JSClassesFileGenerator()
	{
		this.filepath = HTMLGenerationHelper.getProjectFolder() + "\\js\\classes.js";
	}

	/**
	 * generateFile Creates the file with the given path from filepath and
	 * writes the necessary JavaScript code of the classes to be used in the
	 * report.
	 * 
	 * @throws IOException
	 *             If an error occurs with the inputs or outputs
	 */
	public void generateFile() throws IOException
	{
		FileOutputStream file = new FileOutputStream(filepath);
		IndentWriter writer = new IndentWriter(file, 2, "UTF-8");

		writer.println("function OMSObject()");
		writer.println("{");
		writer.println("}");
		writer.println();

		writer.println("OMSObject.printProperties = function(index, link)");
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
			writer.println("request.open(\"GET\", \"data/properties_\" + index + \".html\", false);");
			writer.println("request.send(null);");
			writer.println("if(request.readyState == 4)");
			writer.println("{");
			writer.indent();
			{
				writer.println("if (link != null)");
				writer.indent();
				{
					writer.println("document.getElementById(\"elementName\").innerHTML = link.innerHTML;");
				}
				writer.unindent();
				writer.println();
				writer.println("else");
				writer.indent();
				{
					writer.println("document.getElementById(\"elementName\").innerHTML = \"\";");
				}
				writer.unindent();
				writer.println();
				writer.println("document.getElementById(\"properties\").innerHTML = request.responseText;");
			}
			writer.unindent();
			writer.println("}");
		}
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("OMSObject.printDiagram = function(index, zoom, link)");
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
			writer.println("request.open(\"GET\", \"data/diagram_\" + index + \"_\" + zoom + \".html\", false);");
			writer.println("request.send(null);");
			writer.println("if(request.readyState == 4)");
			writer.println("{");
			writer.indent();
			{
				writer.println("document.getElementById(\"diagramName\").innerHTML = link.innerHTML;");
				writer.println("document.getElementById(\"model\").innerHTML = request.responseText;");
			}
			writer.unindent();
			writer.println("}");
		}
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.close();
		HTMLGenerationHelper.newFileGenerated();
	}
}