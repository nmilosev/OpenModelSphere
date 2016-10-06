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

package org.modelsphere.jack.plugins.io;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JLabel;

public class ImageUtilities {
    private static MediaTracker tracker = new MediaTracker(new JLabel());
    private static int nextTrackerID = 0;

    private ImageUtilities() {
    }

    private static int nextTrackerID() {
        synchronized (tracker) {
            nextTrackerID++;
        }
        return nextTrackerID;
    }

    public static Image loadImage(URL url) {
        if (url == null) {
            return null;
        }
        Image image = null;
        try {
            image = Toolkit.getDefaultToolkit().getImage(url);
            if (image != null) {
                synchronized (tracker) {
                    int id = nextTrackerID();
                    tracker.addImage(image, id);
                    try {
                        tracker.waitForID(id);
                    } catch (InterruptedException e) {
                    }
                    tracker.removeImage(image, id);
                }
                int width = image.getWidth(null);
                int height = image.getHeight(null);
                int size = Math.max(width, height);
                if (size != 32) {
                    if (width > height) {
                        image = image
                                .getScaledInstance(32, height * 32 / width, Image.SCALE_SMOOTH);
                    } else if (width < height) {
                        image = image
                                .getScaledInstance(width * 32 / height, 32, Image.SCALE_SMOOTH);
                    } else {
                        image = image.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                    }
                }
            }
        } catch (Exception e) {
        }
        return image;
    }

}
