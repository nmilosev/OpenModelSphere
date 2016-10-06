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

/**
 * Ancestor class for all application types that can be used as type of a field in a DbObject. Known
 * descendants: Domain, DbtPrefix, DbtPassword.
 * 
 * A DbtAbstract value is always duplicated in DbObject.get/set methods so that the application
 * always receives a transient copy of the persistent value kept in the database. This is necessary
 * because persistent objects cannot be accessed outside a transaction.
 * 
 * The types allowed for the fields of a SrType are: - primitive types and String - types inheriting
 * from SrType - one-dimension array of primitive types and String
 * 
 * A DbtAbstract class must define the methods <equals> and <duplicate>; <duplicate> must perform a
 * deep copy. If the class contains fields of type SrType or array fields, it must define the
 * methods <dbFetch> and <dbCluster>. See db.doc for more information.
 * 
 * NOTE: there is a different hierarchy for SrTypes that act as wrapper for AWT types; these SrTypes
 * are internal to Db and are never seen by the application (ex: SrRectangle, SrFont, ...).
 */
public abstract class DbtAbstract extends SrType {

    static final long serialVersionUID = 4529188468390439756L;

    // Cannot use clone() in a persistent-capable class.
    // IMPORTANT: must perform a deep copy.
    public abstract DbtAbstract duplicate();

    public Object toApplType() {
        return duplicate();
    }
}
