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

package org.modelsphere.jack.baseDb.db;

/**
 * This interface allows to implements enumerators that need to be closed.
 * <p>
 * This interface defines the following behavior on <code>nextElement</code>:
 * <p>
 * <code>nextElement()</code> may be called only after a successful call to
 * <code>hasMoreElements()</code>, and will return exactly the element retrieved by
 * <code>hasMoreElements()</code>; <code>nextElement()</code> increments the cursor, which allows
 * the next call to <code>hasMoreElements()</code> to retrieve a further element. So two consecutive
 * calls to <code>hasMoreElements()</code> will retrieve the same element (cursor not updated); two
 * consecutive calls to <code>nextElement()</code> will throw a RuntimeException on the second call.
 */
public interface DbEnumeration {

    /**
     * @return true if more elements are available in the enumeration, false otherwise.
     * @throws DbException
     *             Thrown if the enumeration has been closed or if the transaction has been closed.
     */
    public boolean hasMoreElements() throws DbException;

    /**
     * @return The next object.
     * @throws DbException
     *             Thrown if the enumeration has been closed or if the transaction has been closed.
     */
    public DbObject nextElement() throws DbException;

    /**
     * Close the enumeration. Once an enumeration has been close, any calls to hasMoreElements() or
     * nextElement() will throws a DbException.
     */
    public void close();
}
