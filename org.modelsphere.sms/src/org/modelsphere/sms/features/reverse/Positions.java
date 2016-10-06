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

package org.modelsphere.sms.features.reverse;

import java.util.Stack;
import java.util.Vector;

import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.srtool.reverse.file.IncompletedObjectInterface;
import org.modelsphere.jack.srtool.reverse.file.JackPositions;
import org.modelsphere.sms.oo.db.DbOOAbstractMethod;

/*
 * Returns by Reverse.getPosition()
 */
public abstract class Positions extends JackPositions {

    // methods
    public abstract Vector getVecOfIncompletedObject();

    public abstract Stack getAdtStack();

    public abstract MethodStack getMethodStack();

    public abstract Stack getMethodPosStack();

    public abstract void setCurrentSemanticalObject(DbSemanticalObject semObj);

    public abstract void addIncompletedObject(IncompletedObjectInterface obj);

    public abstract void cleanInputStreamPositions();

    //
    // INNER CLASS
    //
    public static class MethodStack extends Stack {
        public Object peek() {
            return (DbOOAbstractMethod) super.peek();
        }

        public Object pop() {
            DbOOAbstractMethod oper = (DbOOAbstractMethod) super.pop();
            return oper;
        }

        public void push(DbOOAbstractMethod oper) {
            super.push(oper);
        }
    } // end MethodStack
}
