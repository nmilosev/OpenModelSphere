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

import java.security.MessageDigest;

import org.modelsphere.jack.baseDb.db.*;

public final class DbtPassword extends DbtAbstract {

    static final long serialVersionUID = 0L;

    private static MessageDigest shaDigest;
    static {
        try {
            shaDigest = MessageDigest.getInstance("SHA"); // NOT LOCALIZABLE
        } catch (Exception e) {
        } // cannot occur
    }

    byte[] encrypted;

    // Parameterless constructor
    public DbtPassword() {
    }

    public DbtPassword(String password) {
        encrypted = shaDigest.digest(password.getBytes());
    }

    // Called by duplicate() method; must duplicate recursively all the
    // non-primitive instance variables.
    private DbtPassword(byte[] encrypted) {
        this.encrypted = new byte[encrypted.length];
        System.arraycopy(encrypted, 0, this.encrypted, 0, encrypted.length);
    }

    public final DbtAbstract duplicate() {
        return new DbtPassword(encrypted);
    }

    public final boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof DbtPassword))
            return false;
        return MessageDigest.isEqual(encrypted, ((DbtPassword) obj).encrypted);
    }

    public final String toString() {
        return "";
    }

    public final void dbFetch(Db db) throws DbException {
        db.fetch(this);
        db.fetch(encrypted);
    }

    public final void dbCluster(Db db, Object parent) throws DbException {
        db.cluster(this, parent);
        db.cluster(encrypted, this);
    }
}
