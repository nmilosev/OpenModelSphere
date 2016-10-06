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

package org.modelsphere.sms.oo.db.util;

import java.awt.Color;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.sms.oo.db.*;
import org.modelsphere.sms.oo.international.LocaleMgr;

/*
 *  Various utilities to find quickly db objects
 *  All functions are static: class FindUtilities has no occurrence
 *  All functions are final: class FindUtilities should not have any subclass
 */
public final class OoUtilities {

    private static final String JAVA_LANG = "java.lang"; //NOT LOCALIZABLE, package name
    private static final String UNRESOLVED_PACKNAMES = "{"
            + LocaleMgr.screen.getString("Unresolved") + "}"; //NOT LOCALIZABLE

    //Implements Singleton Design Pattern
    private OoUtilities() {
    }

    private static OoUtilities g_singleInstance = null;

    public static OoUtilities getSingleton() {
        if (g_singleInstance == null) {
            g_singleInstance = new OoUtilities();
        }

        return g_singleInstance;
    }

    public final String buildDisplayString(DbOOAbstractMethod method, boolean umlOrder,
            boolean hideNameSignature) throws DbException {
        StringBuffer signature = new StringBuffer();
        signature.append(method.getName() + "(");
        DbEnumeration dbEnum = method.getComponents().elements(DbOOParameter.metaClass);

        while (dbEnum.hasMoreElements()) {
            DbOOParameter param = (DbOOParameter) dbEnum.nextElement();
            String paramTypeName = param.buildTypeDisplayString();
            String paramName = param.buildDisplayString();
            if (umlOrder) {
                String paramText = hideNameSignature ? paramTypeName : paramName + " : "
                        + paramTypeName;
                signature.append(paramText);
            } else {
                String paramText = hideNameSignature ? paramTypeName : paramTypeName + " "
                        + paramName;
                signature.append(paramText);
            } //end if

            if (dbEnum.hasMoreElements()) {
                String separator = hideNameSignature ? "," : ", ";
                signature.append(separator);
            }
        } //end while
        dbEnum.close();
        signature.append(")");
        return signature.toString();
    }

    public String getUnknownPackageName() {
        return UNRESOLVED_PACKNAMES;
    }

    public String getJavaLang() {
        return JAVA_LANG;
    }

    // Return semantical Object named soName
    public DbSemanticalObject getNamedChild(DbRelationN relationN, MetaClass metaClass,
            String soName) throws DbException {
        DbObject dbo = null;
        DbSemanticalObject so = null;

        boolean isFound = false;
        DbEnumeration dbEnum = relationN.elements(metaClass);

        while (dbEnum.hasMoreElements()) {
            dbo = (DbObject) dbEnum.nextElement();
            if (dbo instanceof DbSemanticalObject) {
                String name = ((DbSemanticalObject) dbo).getName();

                if ((name != null) && name.equals(soName)) {
                    isFound = true;
                    so = (DbSemanticalObject) dbo;
                    break;
                }
            }
        } // end while
        dbEnum.close();

        if (!isFound)
            so = null;

        return so;
    }

    /*
     * Recursively scan composites, and stop when we meet a composite whose type is one of the types
     * we look for.
     * 
     * component: scan composites of this dbobject types: stop processing when current component has
     * a composite whose type is an element of this array.
     * 
     * Note: we return component whose composite's type is one of the types we look for, rather that
     * to return the composite itself, because composite can easily retrieved from a component, and
     * not the contrary.
     */
    public DbObject findComponentWhoseCompositeTypeIs(DbObject aComponent, Class types[])
            throws DbException {
        DbObject firstComponent = null;
        DbObject component = aComponent;

        loop: while (component != null) {
            DbObject currentComposite = component.getComposite();

            for (int i = 0; i < types.length; i++) {
                Class type = types[i];
                if (type.isInstance(currentComposite)) {
                    firstComponent = component;
                    break loop;
                }
            }

            component = currentComposite;
        } //end loop

        return firstComponent;
    }

    //TO be called from new DbOODiagram();
    public void initOOStyle(DbOOStyle style) throws DbException {
        //Class default values
        style.setBackgroundColorClass(Color.yellow);

        //Interface default values
        style.setBackgroundColorInterface(Color.cyan);

        //Exception default values
        style.setBackgroundColorException(Color.orange);
    } //end initOOStyle()
} //OoUtilities

