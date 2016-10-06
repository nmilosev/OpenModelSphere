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

import org.modelsphere.jack.baseDb.db.*;

public final class DbtPrefix extends DbtAbstract {

    static final long serialVersionUID = -5347017687841136401L;

    public static final int DISPLAY_NONE = 0;
    public static final int DISPLAY_TEXT = 1;
    public static final int DISPLAY_IMAGE = 2;

    int displayedComponent;
    String text;
    /*
     * BEWARE: cannot change the image without changing the name; method <equals> compare the name
     * of the image, not the image itself.
     */
    String imageName;
    SrImage image;

    //Parameterless constructor
    public DbtPrefix() {
    }

    /**
     * Possible values for displayedComponent: DISPLAY_NONE, DISPLAY_TEXT, DISPLAY_IMAGE
     */
    public DbtPrefix(int displayedComponent, String text, String imageName, Image jImage) {
        this.displayedComponent = displayedComponent;
        this.text = text;
        this.imageName = imageName;
        this.image = (jImage != null ? new SrImage(jImage) : null);
    }

    public final DbtAbstract duplicate() {
        // Note that we also duplicate the field <image> because it's a SrType
        return new DbtPrefix(displayedComponent, text, imageName, getImage());
    }

    public final int getDisplayedComponent() {
        return displayedComponent;
    }

    public final void setDisplayedComponent(int value) {
        displayedComponent = value;
    }

    public final String getText() {
        return text;
    }

    public final void setText(String text) {
        this.text = text;
    }

    public final String getImageName() {
        return imageName;
    }

    public final Image getImage() {
        return (image == null ? null : (Image) image.toApplType());
    }

    public final void setImage(String imageName, Image jImage) {
        this.imageName = imageName;
        this.image = (jImage != null ? new SrImage(jImage) : null);
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof DbtPrefix))
            return false;
        DbtPrefix prefix = (DbtPrefix) obj;
        return (displayedComponent == prefix.displayedComponent
                && DbObject.valuesAreEqual(text, prefix.text) && DbObject.valuesAreEqual(imageName,
                prefix.imageName));
    }

    public String toString() {
        String str = "";
        switch (displayedComponent) {
        case (DISPLAY_TEXT):
            str = (text != null) ? "text=\"" + text + "\"" : "text=\"" + "\""; // NOT LOCALIZABLE
            break;
        case (DISPLAY_IMAGE):
            str = (image != null) ? "image=\"" + imageName + "\"" : "image=\"" + "\""; // NOT LOCALIZABLE
            break;
        default:
            str = "not displayed"; // NOT LOCALIZABLE RuntimeException
            break;
        }
        return str;
    }

    public final void dbFetch(Db db) throws DbException {
        db.fetch(this);
        if (image != null)
            image.dbFetch(db);
    }

    public final void dbCluster(Db db, Object parent) throws DbException {
        db.cluster(this, parent);
        if (image != null)
            image.dbCluster(db, this);
    }

    public static DbtPrefix getDefaultPrefix() {
        DbtPrefix prefix = new DbtPrefix(DbtPrefix.DISPLAY_TEXT, "?", null, null);
        return prefix;
    }
}
