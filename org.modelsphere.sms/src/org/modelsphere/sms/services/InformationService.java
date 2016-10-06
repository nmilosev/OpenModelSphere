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

package org.modelsphere.sms.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;

import org.modelsphere.jack.services.Service;

/**
 * 
 * A very simple service. It displays the current time on the server to the client, and closes the
 * connection.
 * 
 */
public final class InformationService implements Service {

    public void serve(InputStream i, OutputStream o) throws IOException {
        ServiceList.getSingleInstance();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(o));
        out.println(new Date());
        out.close();
        i.close();
    }

    /*
     * DO NOT REMOVE : will be useful in a repository-based version public void serve(InputStream i,
     * OutputStream o) throws IOException { //Read the object ServiceList.getSingleInstance();
     * ObjectInputStream input = new ObjectInputStream(i); ObjectOutputStream output = new
     * ObjectOutputStream(o); try { //Read the object InformationStructure struct =
     * (InformationStructure)input.readObject(); String addr = struct.IPaddress; if (!
     * struct.allUsers.containsKey(addr)) { struct.allUsers.put(addr, struct);
     * struct.userList.add(addr); } //end if
     * 
     * output.writeObject(struct); } catch (ClassNotFoundException ex) { } //end try
     * 
     * output.close(); input.close(); } //end serve()
     * 
     * public static InformationStructure createInfoStructure() { InformationStructure struct = new
     * InformationStructure(); return struct; }
     * 
     * public static String getMessage(InformationStructure struct) { int nb =
     * struct.userList.size(); String msg = nb + " users connected:";
     * 
     * Iterator iter = struct.userList.iterator(); if (iter.hasNext()) { String key =
     * (String)iter.next(); InformationStructure currStruct =
     * (InformationStructure)struct.allUsers.get(key); msg += currStruct.toString(); if (!
     * iter.hasNext()) msg += ", "; } //end if
     * 
     * return msg; } //end getMessage()
     * 
     * //Singleton private static InformationService g_singleInstance = null; private
     * InformationService() {} public static InformationService getSingleton() { if
     * (g_singleInstance == null) { g_singleInstance = new InformationService(); }
     * 
     * return g_singleInstance; } //end getSingleton()
     * 
     * // // Inner class // public static class InformationStructure implements Serializable {
     * String username; String IPaddress = ""; String OS; Date date; ArrayList userList = new
     * ArrayList(); HashMap allUsers = new HashMap();
     * 
     * InformationStructure() { username = System.getProperty("user.name"); OS =
     * System.getProperty("os.name"); date = new Date();
     * 
     * try { InetAddress addr = InetAddress.getLocalHost(); IPaddress = addr.getHostAddress(); }
     * catch (UnknownHostException ex) { } } //end InformationStructure()
     * 
     * public String toString() { return username + " (" + IPaddress + ", connected @ " +
     * date.toString() + ")\n"; } } //end InformationStructure
     */
} // end InformationService
