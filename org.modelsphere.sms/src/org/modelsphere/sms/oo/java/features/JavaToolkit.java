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
package org.modelsphere.sms.oo.java.features;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class JavaToolkit {

    //Implements Singleton Design Pattern
    private JavaToolkit() {
    }

    private static JavaToolkit g_singleInstance = null;

    public static JavaToolkit getSingleton() {
        if (g_singleInstance == null) {
            g_singleInstance = new JavaToolkit();
        }

        return g_singleInstance;
    } //end getSingleton()

    //
    // public methods
    //
    public Object[] getSystemJarFiles() {
        ArrayList systemJarList = new ArrayList();
        String path = System.getProperty("sun.boot.class.path"); //NOT LOCALIZABLE, property name
        String delim = System.getProperty("path.separator"); //NOT LOCALIZABLE, property name
        StringTokenizer st = new StringTokenizer(path, delim);
        while (st.hasMoreTokens()) {
            systemJarList.add(st.nextToken());
        } //end while

        Object[] objs = systemJarList.toArray();
        return objs;
    } //end getSystemJarFiles() 

    public void fillClassList(Object[] jarFiles, ArrayList packageList, ArrayList classList) {
        for (int i = 0; i < jarFiles.length; i++) {
            String jarFile = jarFiles[i].toString();
            scanJarFile(jarFile, packageList, classList);
        } //end for
    } //end fillClassList()

    //
    // private methods
    //
    private void scanJarFile(String filename, ArrayList packageList, ArrayList classList) {
        try {
            ZipFile file = new ZipFile(filename);
            Enumeration enumeration = file.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) enumeration.nextElement();
                String name = entry.getName();
                String packagename = name.substring(0, name.lastIndexOf('/')).replace('/', '.');
                if (packageList.contains(packagename)) {
                    String className = name.substring(name.lastIndexOf('/') + 1);
                    int idx = className.indexOf('$');
                    if (idx == -1) { //if outer class
                        String qualifiedName = packagename + '.' + className;
                        classList.add(qualifiedName);
                    }
                }
            } //end while

        } catch (IOException ex) {
            //ignore if .jar file cannot be located
        } //end try
    } //end scanJarFile()

    //
    // unit test
    //
    public static void main(String[] args) {
        JavaToolkit toolkit = JavaToolkit.getSingleton();
        ArrayList packageList = new ArrayList();
        packageList.add("java.lang");

        Object[] jarFiles = toolkit.getSystemJarFiles();
        ArrayList classList = new ArrayList();
        toolkit.fillClassList(jarFiles, packageList, classList);

        Iterator iter = classList.iterator();
        while (iter.hasNext()) {
            String className = (String) iter.next();
            System.out.println(className);
        } //end while

        //util.getClassesInPackage("java.lang");
    } //end main
} //end JavaToolkit
