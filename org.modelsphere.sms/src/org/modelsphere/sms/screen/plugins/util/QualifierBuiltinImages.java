/*************************************************************************

Copyright (C) 2010 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.screen.plugins.util;

import java.awt.Image;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.sms.be.international.resources.qualifiers.LocaleMgr;

public class QualifierBuiltinImages {

    private static List<Image> _builtinImages = null; 
    public static List<Image> getBuiltinImages() {
        if (_builtinImages == null) {
            _builtinImages = createBuiltinImageList();
        }
        
        return _builtinImages;
    }
    
    private static List<Image> createBuiltinImageList() {
        List<Image> builtinImages = new ArrayList<Image>();
        Class<?> claz = LocaleMgr.class;
        Image image; 
        int counter=1;
        
        DecimalFormat df = (DecimalFormat)DecimalFormat.getIntegerInstance();
        df.applyPattern("0000"); 
        
        do {
            String filename = df.format(counter++) + ".gif"; 
            
            try {
                image = GraphicUtil.loadImage(claz, filename); 
            } catch (RuntimeException ex) {
                image = null; //stop
            }
            
            if (image != null) {
                builtinImages.add(image); 
            }
        } while (image != null); 

        return builtinImages;
    }
    
    public static void main(String[] args) {
        List<Image> images = getBuiltinImages();
        System.out.println("Found " + images.size() + " built-in images.");
    }

}
