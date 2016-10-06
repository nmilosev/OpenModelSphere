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

import org.modelsphere.jack.baseDb.db.*;

/**
 * There are 2 categories of SrTypes: - the wrappers of AWT types. - the application types (must
 * inherit from DbtAbstract).
 * 
 * The types allowed for the fields of a SrType are: - primitive types and String - types inheriting
 * from SrType - one-dimension array of primitive types and String
 * 
 * A SrType must define the method <equals>; it must also define <toApplType> or <duplicate>
 * according to the category. If it contains fields of type SrType or array fields, it must also
 * define <dbFetch> and <dbCluster>.
 */
public abstract class SrType extends PersistentObject {

    static final long serialVersionUID = 2463529016163104741L;

    public abstract Object toApplType();

    public void dbFetch(Db db) throws DbException {
        db.fetch(this);
    }

    public void dbCluster(Db db, Object parent) throws DbException {
        db.cluster(this, parent);
    }
}
