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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Iterator;

final class PluginClassLoader extends ClassLoader {
    PluginClassLoader(ClassLoader parent) {
        super(parent);
    }

    protected Class findClass(String name) throws ClassNotFoundException {
        if (PluginLoader.getPluginsPath() == null)
            throw new ClassNotFoundException(name);

        String classfilename = findInPath(name.replace('.', File.separatorChar) + ".class"); //NOT LOCALIZABLE, file extension
        byte[] classData = loadClassBytes(classfilename);
        if (classData == null)
            throw new ClassNotFoundException();

        Class c = defineClass(null, classData, 0, classData.length);
        if (c != null)
            return c;
        throw new ClassNotFoundException(name);
    }

    private String findInPath(String resname) {
        File file = null;
        Iterator<String> iter = PluginLoader.getPluginsPath().iterator();
        while (iter.hasNext()) {
            String path = iter.next();
            if (path == null)
                continue;
            try {
                file = new File(path, resname);
                if (!file.exists() || !file.canRead())
                    file = null;
            } catch (Exception e) {
                file = null;
                continue;
            }
            if (file != null)
                break;
        } //end while

        return file == null ? null : file.getAbsolutePath();
    }

    private byte[] loadClassBytes(String classFileName) {
        try {
            FileInputStream in = new FileInputStream(classFileName);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try {
                int ch;
                while ((ch = in.read()) != -1) {
                    byte b = (byte) (ch);
                    buffer.write(b);
                }
            } finally {
                in.close();
            }
            return buffer.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    protected URL findResource(String name) {
        String foundres = findInPath(name);
        if (foundres == null)
            return null;
        try {
            return new File(foundres).toURL();
        } catch (Exception e) {
            return null;
        }
    }

}
