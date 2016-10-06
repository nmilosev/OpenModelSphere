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

import java.util.Arrays;

import org.modelsphere.jack.baseDb.db.*;

// We keep the <loginNo> of each DbLogin instead of a direct reference,
// because we cannot have a direct reference to an object that is outside the project graph.
// BEWARE: a LoginList field has no meaning outside its project; so it must be nulled at de-serialization.
public final class DbtLoginList extends DbtAbstract {

    static final long serialVersionUID = 0L;

    int[] loginNos;

    // Parameterless constructor
    public DbtLoginList() {
    }

    public DbtLoginList(DbLogin[] logins) throws DbException {
        loginNos = new int[logins.length];
        for (int i = 0; i < logins.length; i++)
            loginNos[i] = logins[i].getNo().intValue();
    }

    // Called by duplicate() method; must duplicate recursively all the
    // non-primitive instance variables.
    private DbtLoginList(int[] loginNos) {
        this.loginNos = new int[loginNos.length];
        System.arraycopy(loginNos, 0, this.loginNos, 0, loginNos.length);
    }

    public final DbtAbstract duplicate() {
        return new DbtLoginList(loginNos);
    }

    public final DbLogin[] getLogins(Db db) throws DbException {
        DbLogin[] logins = new DbLogin[loginNos.length];
        DbLoginNode loginNode = db.getLoginNode();
        int nb = 0;
        for (int i = 0; i < loginNos.length; i++) {
            DbLogin login = loginNode.getLogin(loginNos[i]);
            if (login != null) // the login may have been deleted.
                logins[nb++] = login;
        }
        if (nb != logins.length) {
            DbLogin[] oldLogins = logins;
            logins = new DbLogin[nb];
            System.arraycopy(oldLogins, 0, logins, 0, nb);
        }
        return logins;
    }

    public final boolean hasAccess(Db db) throws DbException {
        DbLoginUser user = db.getLogin();
        DbLogin[] logins = getLogins(db);
        for (int i = 0; i < logins.length; i++) {
            if (isMember(user, logins[i]))
                return true;
        }
        return false;
    }

    private boolean isMember(DbLoginUser user, DbLogin login) throws DbException {
        if (login instanceof DbLoginGroup) {
            DbRelationN members = ((DbLoginGroup) login).getMembers();
            for (int i = 0; i < members.size(); i++) {
                if (isMember(user, (DbLogin) members.elementAt(i)))
                    return true;
            }
            return false;
        }
        return (user == login);
    }

    public final boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof DbtLoginList))
            return false;
        return Arrays.equals(loginNos, ((DbtLoginList) obj).loginNos);
    }

    public final String toString() {
        return ""; // would need to start a transaction to get user names.
    }

    public final void dbFetch(Db db) throws DbException {
        db.fetch(this);
        db.fetch(loginNos);
    }

    public final void dbCluster(Db db, Object parent) throws DbException {
        db.cluster(this, parent);
        db.cluster(loginNos, this);
    }
}
