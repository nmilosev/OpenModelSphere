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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.modelsphere.jack.graphic.GraphicUtil;

/**
 * This class is used to generate the icon files for the project.
 * 
 * @version 1.0.0
 * @author Open ModelSphere HTML Reports Team
 */
public class IconsGenerator
{
	/** The folder in which the icons are to be saved */
	private String folderpath;

	/**
	 * Constructor
	 */
	public IconsGenerator()
	{
		this.folderpath = HTMLGenerationHelper.getProjectFolder() + "\\images\\icons\\";
	}

	/**
	 * This function generates the icons
	 * 
	 * @throws IOException
	 */
	public void generateIcons() throws IOException
	{
		int nbIcons = HTMLGenerationHelper.getNbIcons();
		for (int i = 0; i < nbIcons; i++)
		{
			String filepath = folderpath + "icon_" + i + ".png";
			Icon icon = HTMLGenerationHelper.getIcon(i);
			BufferedImage image = toBufferedImage(icon);
			ImageIO.write(image, "png", new File(filepath));
			HTMLGenerationHelper.newFileGenerated();
		}
	}

	/**
	 * Extracts a buffered image from an Icon
	 * 
	 * @param icon
	 *            the icon from which to extract a buffered image
	 * @return BufferedImage The buffered image extracted from the icon
	 */
	private BufferedImage toBufferedImage(Icon icon)
	{
		BufferedImage bi = null;

		if (icon instanceof ImageIcon)
		{
			ImageIcon imageIcon = (ImageIcon) icon;
			Image image = imageIcon.getImage();
			bi = GraphicUtil.toBufferedImage(image);
		}

		return bi;
	}
}