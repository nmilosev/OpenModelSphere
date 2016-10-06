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
package org.modelsphere.sms.features;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Spawner {

    public static void main(String[] args) {
        try {

            // program files\microsoft office\office\winword.exe
            String cmdline = "cmd /c c:\\test.doc";
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(cmdline);

            // put a BufferedReader on the ls output

            InputStream inputstream = proc.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            // read the ls output

            String line;
            while ((line = bufferedreader.readLine()) != null) {
                System.out.println(line);
            }

            int result = proc.waitFor();
            System.out.println("result = " + result);

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    } // end main()

} // end Spawner
