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

import org.modelsphere.plugins.html.browser.international.LocaleMgr;
import org.modelsphere.plugins.html.browser.io.IndentWriter;

/**
 * This class is used to generate a file containing the necessary HTML code for
 * the report's interface display.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class InterfaceFileGenerator
{
	/** File path of the interface file */
	private String filepath;

	/**
	 * Constructor which sets the default path for the interface file
	 */
	public InterfaceFileGenerator()
	{
		this.filepath = HTMLGenerationHelper.getProjectFolder() + "\\index.html";
	}

	/**
	 * generateFile Creates the file with the given path from filepath and
	 * writes the necessary HTML code to properly display the interface of the
	 * HTML report.
	 * 
	 * @throws IOException
	 *             If an error occurs with the inputs or outputs
	 */
	public void generateFile() throws IOException
	{
		FileOutputStream file = new FileOutputStream(filepath);
		IndentWriter writer = new IndentWriter(file, 2, "UTF-8");

		writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		writer.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		writer.println("<head>");
		writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		writer.println("<title>" + LocaleMgr.getInstance().getString("reportTitle") + " : " + HTMLGenerationHelper.getProjectName() + "</title>");
		writer.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\" />");
		writer.println("<script type=\"text/javascript\" src=\"js/classes.js\"></script>");
		writer.println("<script type=\"text/javascript\" src=\"js/functions.js\"></script>");
		writer.println("<script type=\"text/javascript\" src=\"js/index.js\"></script>");
		writer.println("</head>");
		writer.println("<body onload=\"initialize();\">");
		writer.indent();
		{
			writer.println("<div id=\"interface\">");
			writer.indent();
			{
				writer.println("<div id=\"left_side\">");
				writer.indent();
				{
					writer.println("<div class=\"container\">");
					writer.indent();
					{
						writer.println("<div class=\"title\">" + org.modelsphere.sms.international.LocaleMgr.misc.getString("Explorer") + "</div>");
						writer.println("<div id=\"project\" class=\"contents\"></div>");
					}
					writer.unindent();
					writer.println("</div>");

					writer.println("<div class=\"container\">");
					writer.indent();
					{
						writer.println("<div class=\"title\">" + org.modelsphere.jack.srtool.international.LocaleMgr.screen.getString("DesignPanel") + " : <span id=\"elementName\"></span></div>");
						writer.println("<div id=\"properties\" class=\"contents\">");
						writer.indent();
						{
							writer.println("<table style=\"width: 100%;\">");
							writer.indent();
							{
								writer.println("<tr>");
								writer.indent();
								{
									writer.println("<th style=\"width: 30%; text-align: left;\">" + LocaleMgr.getInstance().getString("property") + "</th>");
									writer.println("<th style=\"width: 70%; text-align: left;\">" + LocaleMgr.getInstance().getString("value") + "</th>");
								}
								writer.unindent();
								writer.println("</tr>");
							}
							writer.unindent();
							writer.println("</table>");
						}
						writer.unindent();
						writer.println("</div>");
					}
					writer.unindent();
					writer.println("</div>");
				}
				writer.unindent();
				writer.println("</div>");

				writer.println("<div id=\"right_side\">");
				writer.indent();
				{
					writer.println("<div class=\"container\">");
					writer.indent();
					{
						writer.println("<div class=\"title\">" + LocaleMgr.getInstance().getString("diagramView") + " : <span id=\"diagramName\"></span></div>");
						writer.println("<div id=\"model\" class=\"contents\"></div>");
					}
					writer.unindent();
					writer.println("</div>");
				}
				writer.unindent();
				writer.println("</div>");
			}
			writer.unindent();
			writer.println("</div>");

			writer.println("<div id=\"longTextBox\">");
			writer.indent();
			{
				writer.println("<div class=\"closeButton\" onclick=\"hideFullText();\"><img src=\"images/close.png\" alt=\"X\" /></div>");
				writer.println("<div id=\"longTextBoxContent\"></div>");
			}
			writer.unindent();
			writer.println("</div>");
		}
		writer.unindent();
		writer.println("</body>");
		writer.println("</html>");

		writer.close();
		HTMLGenerationHelper.newFileGenerated();
	}
}