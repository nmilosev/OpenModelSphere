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
import java.util.Vector;

/**
 * 
 * A very simple service. It returns a vector which contents the current DB variable of the
 * application to the client, let client to change the DB variable, get the modified DB variable and
 * closes the connection. Client must know how to manage DB variable.
 * 
 */
/*
 * Future extension: write other objects in vector for the client (for example: a header object, a
 * information object (info not contained in DB variable))
 */

public final class DbReverseService implements org.modelsphere.jack.services.Service {
    private Vector vec_reply = new Vector();

    public void serve(InputStream i, OutputStream o) throws IOException {
        ServiceList.getSingleInstance();

        // ObjectOutputStream out = new ObjectOutputStream(o);

        /*
         * TODO: rewrite body vec_reply.removeAllElements(); Db currentDb = Db.getSingleton();
         * 
         * vec_reply.addElement(currentDb); out.writeObject(vec_reply);
         * 
         * ObjectInputStream in = new ObjectInputStream(i);
         * 
         * try { Vector vec = (Vector)in.readObject(); db.Db db = (db.Db)vec.elementAt(0);
         * //Db.setSingleton(db);
         * 
         * } catch (ClassNotFoundException e) {}
         * 
         * 
         * 
         * out.close(); in.close();
         */
    }
}
