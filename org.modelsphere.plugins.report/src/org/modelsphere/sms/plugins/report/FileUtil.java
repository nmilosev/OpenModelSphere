/*************************************************************************

Copyright (C) 2008 Grandite

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

package org.modelsphere.sms.plugins.report;

// Awt
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtil {

    public static boolean sameDiskDrive(File validFile1, File validFile2) {
        String sep = System.getProperty("file.separator");
        String drive1, drive2;
        String temp;

        try {
            temp = validFile1.getCanonicalFile().toString();
            drive1 = temp.substring(0, temp.indexOf(sep));

            temp = validFile2.getCanonicalFile().toString();
            drive2 = temp.substring(0, temp.indexOf(sep));

            if (drive1.equals(drive2))
                return true;
        } catch (IOException e) {
        }

        return false;
    }

    public static File getRelativePath(File src, File dst) {
        String sep = System.getProperty("file.separator");
        String relativePath;
        String temp;
        int index;

        ArrayList directoryList1 = new ArrayList();
        ArrayList directoryList2 = new ArrayList();

        temp = src.toString();
        while (temp.indexOf(sep) > -1) {
            directoryList1.add(temp.substring(0, temp.indexOf(sep)));
            temp = temp.substring(temp.indexOf(sep) + 1, temp.length());
        }

        temp = dst.toString();
        while (temp.indexOf(sep) > -1) {
            directoryList2.add(temp.substring(0, temp.indexOf(sep)));
            temp = temp.substring(temp.indexOf(sep) + 1, temp.length());
        }
        relativePath = temp;

        index = 0;
        while ((index < directoryList1.size()) && (index < directoryList2.size())
                && directoryList1.get(index).equals(directoryList2.get(index))) {
            index++;
        }

        int i;
        for (i = directoryList2.size() - 1; i >= index; i--) {
            relativePath = directoryList2.get(i).toString() + sep + relativePath;
        }

        int nbParentDir = directoryList1.size() - index;
        for (i = 0; i < nbParentDir; i++) {
            relativePath = ".." + sep + relativePath;
        }
        if (nbParentDir == 0)
            relativePath = "." + sep + relativePath;

        return new File(relativePath);
    }

}
