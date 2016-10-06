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

package org.modelsphere.plugins.html.browser.htmlgeneration.images;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.modelsphere.plugins.html.browser.htmlgeneration.HTMLGenerationHelper;

/**
 * This class is used to generate files containing the necessary images for
 * the report's interface display.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class InterfaceImagesGenerator
{
	/** The size of a buffer */
	private static final int BUFFER_SIZE = 8 * 1024;

	/** The folder in which to save the images */
	private String folderpath;

	/**
	 * Constructor
	 */
	public InterfaceImagesGenerator()
	{
		folderpath = HTMLGenerationHelper.getProjectFolder() + "\\images\\";
	}

	/**
	 * This function generates the images files for the project
	 * 
	 * @throws IOException
	 */
	public void generateImages() throws IOException
	{
		//Theme-independent icons
		generateImage("close.png");
		generateImage("collapse.jpg");
		generateImage("collapseall.gif");
		generateImage("expand.jpg");
		generateImage("expandall.gif");
		generateImage("font_size_decrease.png");
		generateImage("font_size_increase.png");
		generateImage("help.gif");
		generateImage("longText.png");
		generateImage("print.gif");
		generateImage("space.jpg");
		generateImage("transparent.gif");
		generateImage("zoom_in.gif");
		generateImage("zoom_in_disabled.gif");
		generateImage("zoom_out.gif");
		generateImage("zoom_out_disabled.gif");

		//Theme-sensitive icons
		ThemeManager manager = ThemeManager.getThemeManager();
		generateImage(manager.getMaximizeIcon());
		generateImage(manager.getMinimizeIcon());
		generateImage(manager.getTitleBgIcon());
	}

	private void generateImage(String imageName) throws IOException
	{
		InputStream input = getImageStream(imageName);
		File outputFile = new File(folderpath, imageName);
		FileOutputStream output = new FileOutputStream(outputFile);
		copyFile(input, output);
		HTMLGenerationHelper.newFileGenerated();
	}

	/**
	 * This function gets the image stream.
	 * 
	 * @param filename
	 *            The name of the file
	 * @return The input stream for the image
	 * @throws IOException
	 */
	private InputStream getImageStream(String filename) throws IOException
	{
		Class<?> claz = this.getClass();
		URL url = claz.getResource(filename);
		if (url == null)
		{
			throw new FileNotFoundException();
		}

		InputStream input = url.openStream();
		return input;
	}

	/**
	 * This functions copies a file.
	 * 
	 * @param input
	 *            the file to copy
	 * @param output
	 *            the destination of the copy
	 * @throws IOException
	 */
	private void copyFile(InputStream input, OutputStream output) throws IOException
	{
		byte buffer[] = new byte[BUFFER_SIZE];
		int len = BUFFER_SIZE;

		while (true)
		{
			len = input.read(buffer, 0, BUFFER_SIZE);
			if (len < 0)
				break;
			output.write(buffer, 0, len);
		}
	}
}