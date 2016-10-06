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

package org.modelsphere.jack.srtool.reverse.engine;

import java.util.*;
import java.lang.reflect.*;

public final class SrxCustomElement implements SrxElement {
    // Fields used to access and convert the SRX Value to a compatible DB Value
    private String hook; // Name of the method to call for getting the object
    // value.
    // Hook signature = ""public Object hookName() throws DbException""
    // The return object must be a value object assignable on a Db metaField.
    private Parser parser; // If specified, the parser will be called to get the
    // Object to set from the String value
    private String fieldTag; // The tag representing the value

    // see fields for comments
    private SrxCustomElement(String fieldTag, Parser parser, String hook) {
        this.fieldTag = fieldTag;
        this.parser = parser;
        this.hook = hook;
    }

    // see fields for comments
    public SrxCustomElement(String hook) {
        this(null, null, hook);
    }

    // see fields for comments
    public SrxCustomElement(String fieldTag, Parser parser) {
        this(fieldTag, parser, null);
    }

    public Object getValue(Object hookContainer, HashMap currentObject) throws Exception {
        if (currentObject == null)
            return null;
        Object result = null;
        if (hook != null && hookContainer != null) {
            // try{
            Method hookmethod = hookContainer.getClass().getMethod(hook, null);
            result = hookmethod.invoke(hookContainer, null);
            // }
            /*
             * catch (NoSuchMethodException e){ Debug.trace("Invalid Hook for fieldTag <" + fieldTag
             * + ">, hook <" + hook + "> : NoSuchMethodException" ); //result =
             * ReverseBuilder.INVALID_OBJECT; throw e; } catch (IllegalAccessException e){
             * Debug.trace("Invalid Hook for fieldTag <" + fieldTag +">, hook <" + hook +
             * "> : IllegalAccessException" ); //result = ReverseBuilder.INVALID_OBJECT; throw e; }
             * catch (InvocationTargetException e){
             * Debug.trace("Exception occurs in Hook for fieldTag <" + fieldTag +">, hook <" + hook
             * + "> : " + e.getTargetException()); //result = ReverseBuilder.INVALID_OBJECT; throw
             * e; } catch (IllegalArgumentException e){ Debug.trace("Invalid Hook for fieldTag <" +
             * fieldTag +">, hook <" + hook + "> : IllegalArgumentException" ); //result =
             * ReverseBuilder.INVALID_OBJECT; e. throw e; }
             */
        } else
            result = (parser != null && fieldTag != null) ? parser.parse((String) currentObject
                    .get(fieldTag)) : null;
        return result;
    }

    public String getDebugInfo(HashMap currentObject) throws Exception {
        return "[SrxCustomElement] tag= " + fieldTag + ", hook= " + hook + ", parser= " + parser
                + ", tagvalue= " + currentObject.get(fieldTag); // NOT LOCALIZABLE
    }
}
