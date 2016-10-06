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

package org.modelsphere.jack.baseDb.db.srtypes;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.debug.Debug;

/* <equals> is not defined on SrImage; so it returns always false.
 */
public final class SrImage extends SrType {

    static final long serialVersionUID = -8403545200349487970L;
    /*
     * Since SrImage is immutable, we are sure the image cannot be changed; so we can keep a
     * transient Java representation of the image without bothering about refreshing it.
     */
    private transient Image image;

    int width;
    int height;
    int[] pixels;

    // Parameterless constructor
    public SrImage() {
    }

    public SrImage(Image image) {
        this.image = image;
        width = image.getWidth(null);
        height = image.getHeight(null);
        pixels = new int[width * height];
        PixelGrabber pGrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
        try {
            pGrabber.grabPixels();
        } catch (InterruptedException e) {
        }
    }

    /**
     * This method must be used to get the image in the correct application format.
     * 
     * @return the image as an Image object
     */
    public final Object toApplType() {
        if (image == null) {
            MemoryImageSource imgSrc = new MemoryImageSource(width, height, pixels, 0, width);
            image = Toolkit.getDefaultToolkit().createImage(imgSrc);
        }
        return image;
    }

    public final void dbFetch(Db db) throws DbException {
        db.fetch(this);
        db.fetch(pixels);
        if (pixels == null) {
            Debug.trace("SrImage:  Null pixels in dbFetch");
        }
    }

    public final void dbCluster(Db db, Object parent) throws DbException {
        db.cluster(this, parent);
        if (pixels == null) {
            Debug.trace("SrImage:  Null pixels in dbCluster");
        }
        db.cluster(pixels, this);
    }

}
