/*************************************************************************

Copyright (C) 2009 Grandite

This file is part of Open ModelSphere.

Open ModelSphere is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.jack.awt;

import java.awt.*;
import java.lang.reflect.Method;

import javax.swing.LookAndFeel;

public class Gradient {
    private Color color1;
    private Color color2;
    private Boolean gradientsOn = null;

    public Gradient(Color color1, Color color2) {
        this.color1 = color1;
        this.color2 = color2;
        try {
            Method getDesktopPropertyValueMethod = LookAndFeel.class.getMethod(
                    "getDesktopPropertyValue", // NOT LOCALIZABLE
                    new Class[] { String.class, Object.class });
            if (getDesktopPropertyValueMethod != null) {
                gradientsOn = (Boolean) getDesktopPropertyValueMethod.invoke(null, new Object[] {
                        "win.frame.captionGradientsOn", Boolean.FALSE }); // NOT LOCALIZABLE
            }
        } catch (Exception e) {
            gradientsOn = null;
        }
    }

    public void paint(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();
        g.setColor(color1);

        if (color2 != null && gradientsOn != null && gradientsOn.booleanValue()
                && g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            Paint savePaint = g2.getPaint();
            GradientPaint titleGradient = new GradientPaint(x, y, color1, x, y
                    + (int) (height * .80) - 1, color2);
            g2.setPaint(titleGradient);
            g2.fillRect(x, y, width, height);
            g2.setPaint(savePaint);
        } else
            g.fillRect(x, y, x + width - 1, height);

        g.setColor(oldColor);
    }

}