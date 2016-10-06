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

package org.modelsphere.jack.baseDb.db.event;

import org.modelsphere.jack.baseDb.db.Db;

/**
 * Listens for transaction start or termination. <br>
 * <br>
 * Clients can register transaction listeners, which are called at the begin and the end of each not
 * nested transaction. Use the static methods <code>Db.addDbTransListener(listener)</code> to
 * register a transaction listener,
 * <code>Db.removeDbTransListener(listener)<code> to unregister it.  Their method 
 * <code>dbTransBegun(db)<code> is called immediately after a transaction has started, 
 * and their method dbTransEnded(db) is called immediately after the transaction was 
 * terminated (commit or abort).
 * 
 */
public interface DbTransListener {

    /**
     * Called when the transaction begins.
     */
    public void dbTransBegun(Db db);

    /**
     * Called when the transaction ends (is committed or is aborted).
     */
    public void dbTransEnded(Db db);
}
