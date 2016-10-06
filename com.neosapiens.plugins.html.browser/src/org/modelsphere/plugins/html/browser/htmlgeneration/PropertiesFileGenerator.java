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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.modelsphere.plugins.html.browser.international.LocaleMgr;
import org.modelsphere.plugins.html.browser.io.IndentWriter;

/**
 * This class is used to generate a HTML file that will be used with the HTML
 * report. It will contain the properties needed to make the report work.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class PropertiesFileGenerator
{
	/** A map containing the properties and their value */
	private Map<String, String> properties;

	/** File path of the properties file */
	private String filepath;

	/**
	 * Constructor which sets the default path for the properties file and
	 * initialises the properties variable.
	 * 
	 * @param properties
	 *            The map containing the properties of a project
	 * @param index
	 *            The current property being treated
	 */
	public PropertiesFileGenerator(Map<String, String> properties, int index)
	{
		this.properties = properties;
		this.filepath = HTMLGenerationHelper.getProjectFolder() + "\\data\\properties_" + index + ".html";
	}

	/**
	 * generateFile Creates the file with the given path from filepath and
	 * writes the necessary HTML code for the properties.
	 * 
	 * @throws IOException
	 *             If an error occurs with the inputs or outputs
	 */
	public void generateFile() throws IOException
	{
		FileOutputStream file = new FileOutputStream(filepath);
		IndentWriter writer = new IndentWriter(file, 2, "UTF-8");

		writer.println("<table style=\"width: 100%;\">");
		writer.indent();
		writer.println("<tr>");
		writer.indent();
		writer.println("<th style=\"width: 30%; text-align: left;\">" + LocaleMgr.getInstance().getString("property") + "</th>");
		writer.println("<th style=\"width: 70%; text-align: left;\">" + LocaleMgr.getInstance().getString("value") + "</th>");
		writer.unindent();
		writer.println("</tr>");

		Set<String> fieldNames = properties.keySet();
		for (String fieldName : fieldNames)
		{
			String fieldValue = properties.get(fieldName);
			if (fieldValue.length() > 40)
			{
				fieldValue = "<span class=\"fullText\">" + fieldValue +
						"</span><img src=\"images/longText.png\" alt=\"\" class=\"showTextButton\" onclick=\"showFullText(this.previousSibling);\" />";
			}

			if (fieldName.matches("(?i).*temps.*") || fieldName.matches("(?i).*time.*"))
			{
				try
				{
					Date date = new Date(Long.decode(fieldValue));
					DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					fieldValue = dateFormat.format(date);
				}

				catch (NumberFormatException e)
				{
					//fieldvalue should not be changed if an exception occurs
				}
			}

			if (fieldValue.equals("true") || fieldValue.equals("false"))
			{
				boolean isTrue = Boolean.parseBoolean(fieldValue);

				fieldValue = "<input type=\"checkbox\" disabled=\"disabled\" ";
				if (isTrue)
				{
					fieldValue += "checked=\"checked\" ";
				}

				fieldValue += "/>";
			}

			writer.println("<tr>");
			writer.indent();
			writer.println("<td>" + fieldName + "</td>");
			writer.println("<td>" + fieldValue + "</td>");
			writer.unindent();
			writer.println("</tr>");
		}

		writer.unindent();
		writer.println("</table>");

		writer.close();
		HTMLGenerationHelper.newFileGenerated();
	}
}