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

package org.modelsphere.jack.preference.context;

import org.modelsphere.jack.baseDb.db.DbException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Defines a component for storing/restoring project's contextual information relative to the
 * workspace.
 * 
 * <p>
 * Utilities method are provided by ContextUtils for storing and restoring a DbObject.
 * 
 * @author gpelletier
 * 
 * @see ContextUtils#readDOMElement(Element)
 * @see ContextUtils#writeDOMElement(Document, Element, org.modelsphere.jack.baseDb.db.DbObject)
 */
public interface ContextComponent {
    /**
     * @return Unique identifier for the component.
     */
    public String getID();

    /**
     * Load the context using the specified node.
     * <p>
     * This method is called within a read transaction (for each active Db instance).
     * 
     * @param configurationElement
     *            The configuration element containing the stored data.
     * @throws DbException
     */
    public void loadContext(Element configurationElement) throws DbException;

    /**
     * Append this context's data to the provided configuration element.
     * <p>
     * This method is called within a read transaction (for each active Db instance).
     * 
     * @param document
     *            The DOM Document to used for creating the sub elements.
     * @param configurationElement
     *            The configuration element to store data to.
     * @throws DbException
     * @return true if the context data has been stored.
     */
    public boolean saveContext(Document document, Element configurationElement) throws DbException;
}
