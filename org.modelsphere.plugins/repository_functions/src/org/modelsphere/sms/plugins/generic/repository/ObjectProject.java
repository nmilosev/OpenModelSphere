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

package org.modelsphere.sms.plugins.generic.repository;

//Java
import java.io.IOException;
import java.io.Writer;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.Connector;
import org.modelsphere.jack.srtool.forward.Modifier;
import org.modelsphere.jack.srtool.forward.RuleException;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSSemanticalObject;

/**
 * For any object, gets the project to which it belongs. <br>
 * Target System : <b>All</b><br>
 * Type : <b>Connector</b><br>
 */
public final class ObjectProject extends Connector.UserConnector {

    private static final String PROJECT_NOT_FOUND_PATTRN = "Object {0} not under any project."; // NOT LOCALIZABLE

    public ObjectProject() {
    } //Parameter-less constructor required by jack.io.Plugins

    //Implements Connector.UserConnector
    public Connector createInstance(String childRule, String oneChildRule) {
        Connector conn = new Connector(null, DbSMSSemanticalObject.fComposite, childRule,
                new String[] { oneChildRule }, DbSMSProject.metaClass, new Modifier[] {});
        setConnector(conn);
        return conn;
    }

    //////////////////
    //OVERRIDES Plugin
    private static final PluginSignature g_signature = new ObjectProjectSignature();

    public PluginSignature getSignature() {
        return g_signature;
    }

    //
    //////////////////

    //Get the object's composite directly under project
    private DbObject getProjectComponent(DbObject object) throws DbException, RuleException {
        DbObject theObject = object;
        DbObject parent;

        do {
            parent = object.getComposite();

            //the object was not a part of a DbSMSProject
            if (parent == null) {
                String name = theObject.getName();
                String msg = MessageFormat.format(PROJECT_NOT_FOUND_PATTRN, new Object[] { name });
                throw new RuleException(msg);
            }

            //We've found an object whose composite is a DbSMSProject
            //Typically, this object is either a data model, or a class model,
            //or a operation library, and so forth.
            if (parent instanceof DbSMSProject) {
                break;
            }

            //object's composite was not a project, so continue the iteration
            //toward the root
            object = parent;
        } while (true);

        return object;
    }

    public boolean expand(Writer output, DbObject object) throws DbException, IOException,
            RuleException {
        boolean expanded = false;

        //Get the object's composite directly under project
        DbObject projComp = getProjectComponent(object);

        //expand it
        expanded = super.expand(output, projComp);

        return expanded;
    }

    private static class ObjectProjectSignature extends PluginSignature {
        private static final String NAME = "ObjectProject"; // NOT LOCALIZABLE
        private static final String REVISION_NUMBER_STR = "$Revision: 4 $"; // NOT LOCALIZABLE
        private static final String AUTHOR = ApplicationContext.APPLICATION_AUTHOR;
        private static final String DATE_STR = "$Date: 2009/04/14 14:00p $"; // NOT LOCALIZABLE
        private static final int BUILD_REQUIRED = 212;

        public ObjectProjectSignature() {
            super(NAME, REVISION_NUMBER_STR, AUTHOR, DATE_STR, BUILD_REQUIRED);
        }
    }
} //end of ObjectProject
