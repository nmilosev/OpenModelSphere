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
 * This class is used to generate a CSS file that will be used with the HTML
 * report.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class CSSFileGenerator
{
	/** File path of the CSS file */
	private String filepath;

	/**
	 * Constructor which sets the default path for the CSS file
	 */
	public CSSFileGenerator()
	{
		this.filepath = HTMLGenerationHelper.getProjectFolder() + "\\css\\style.css";
	}

	/**
	 * generateFile Creates the file with the given path from filepath and
	 * writes the necessary CSS code to properly format the HTML report.
	 * 
	 * @throws IOException
	 *             If an error occurs with the inputs or outputs
	 */
	public void generateFile() throws IOException
	{
		FileOutputStream file = new FileOutputStream(filepath);
		IndentWriter writer = new IndentWriter(file, 2, "UTF-8");

		//Set theme-sensitive variables
		String titleIcon = ThemeManager.getThemeManager().getTitleBgIcon();
		String titleFgColor = ThemeManager.getThemeManager().getTitleFgColor();
		String borderColor = ThemeManager.getThemeManager().getBorderColor();

		writer.println("body");
		writer.println("{");
		writer.indent();
		writer.println("padding: 0px;");
		writer.println("margin: 0px;");
		writer.println("font-family: \"Arial\";");
		writer.println("font-size: 12px;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("#interface");
		writer.println("{");
		writer.indent();
		writer.println("width: 100%;");
		writer.println("position: relative;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("#interface #left_side");
		writer.println("{");
		writer.indent();
		writer.println("width: 30%;");
		writer.println("height: 100%;");
		writer.println("float: left;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("#interface #left_side .container");
		writer.println("{");
		writer.indent();
		writer.println("height: 49%;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("#interface #right_side");
		writer.println("{");
		writer.indent();
		writer.println("width: 70%;");
		writer.println("height: 100%;");
		writer.println("float: left;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("#interface #right_side .container");
		writer.println("{");
		writer.indent();
		writer.println("height: 99%;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("#properties table");
		writer.println("{");
		writer.indent();
		writer.println("border-collapse: collapse;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("#properties th, td");
		writer.println("{");
		writer.indent();
		writer.println("text-align: left;");
		writer.println("border-right: solid 1px #d9d9d9;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("#properties tr");
		writer.println("{");
		writer.indent();
		writer.println("border-bottom: solid 1px #d9d9d9;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("#properties input");
		writer.println("{");
		writer.indent();
		writer.println("margin-left: 45%;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("#properties .fullText");
		writer.println("{");
		writer.indent();
		writer.println("float: left;");
		writer.println("width: 90%;");
		writer.println("max-height: 20px;");
		writer.println("overflow: hidden;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("#properties .showTextButton");
		writer.println("{");
		writer.indent();
		writer.println("float: right;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println(".pageContainer");
		writer.println("{");
		writer.indent();
		writer.println("position: relative;");
		writer.println("margin: 0px;");
		writer.println("padding: 0px;");
		writer.println("background-position: right bottom;");
		writer.println("background-repeat: no-repeat;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println(".pageContainer div");
		writer.println("{");
		writer.indent();
		writer.println("position: absolute;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println(".pageContainer div:hover");
		writer.println("{");
		writer.println("}");
		writer.println();

		writer.println(".imageContainer");
		writer.println("{");
		writer.indent();
		writer.println("position: relative;");
		writer.println("margin: 0px;");
		writer.println("padding: 0px;");
		writer.println("background-position: right bottom;");
		writer.println("background-repeat: no-repeat;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println(".imageContainer div");
		writer.println("{");
		writer.indent();
		writer.println("position: absolute;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println(".imageContainer div:hover");
		writer.println("{");
		writer.indent();
		writer.println("cursor: pointer;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("#maximized");
		writer.println("{");
		writer.indent();
		writer.println("position: absolute;");
		writer.println("width: 99.6%;");
		writer.println("height: 99%;");
		writer.println("background-color: #fff;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println(".container");
		writer.println("{");
		writer.indent();
		writer.println("margin-right: 5px;");
		writer.println("margin-bottom: 5px;");
		writer.println("border: solid 1px " + borderColor + ";");
		writer.println("overflow: auto;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println(".container .title");
		writer.println("{");
		writer.indent();
		writer.println("padding: 3px;");
		writer.println("background-image: url(../images/" + titleIcon + ");");
		writer.println("background-repeat: repeat-x;");
		writer.println("color: " + titleFgColor + ";");
		writer.println("font-size: 14px;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println(".container .contents");
		writer.println("{");
		writer.indent();
		writer.println("width: 100%;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println(".container .contents ul");
		writer.println("{");
		writer.indent();
		writer.println("margin-top: 0px;");
		writer.println("padding-left: 16px;");
		writer.println("list-style-type: none;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println(".container .contents li a img");
		writer.println("{");
		writer.indent();
		writer.println("border: none;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println(".container .contents li img");
		writer.println("{");
		writer.indent();
		writer.println("vertical-align: middle;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println(".collapsed_list ul");
		writer.println("{");
		writer.indent();
		writer.println("display: none;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println(".maximize_icon");
		writer.println("{");
		writer.indent();
		writer.println("float: right;");
		writer.println("cursor: pointer;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println("#longTextBox");
		writer.println("{");
		writer.indent();
		writer.println("display: none;");
		writer.println("position: absolute;");
		writer.println("width: 400px;");
		writer.println("height: 300px;");
		writer.println("overflow: auto;");
		writer.println("border: 2px solid #0734da;");
		writer.println("padding: 20px;");
		writer.println("background-color: #fff;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println(".closeButton");
		writer.println("{");
		writer.indent();
		writer.println("position: absolute;");
		writer.println("top: 0px;");
		writer.println("right: 0px;");
		writer.println("width: 19px;");
		writer.println("height: 19px;");
		writer.println("text-align: center;");
		writer.println("border-left: 2px solid #0734da;");
		writer.println("border-bottom: 2px solid #0734da;");
		writer.unindent();
		writer.println("}");
		writer.println();

		writer.println(".closeButton:hover");
		writer.println("{");
		writer.indent();
		writer.println("cursor: pointer;");
		writer.unindent();
		writer.println("}");

		writer.close();
		HTMLGenerationHelper.newFileGenerated();
	}
}