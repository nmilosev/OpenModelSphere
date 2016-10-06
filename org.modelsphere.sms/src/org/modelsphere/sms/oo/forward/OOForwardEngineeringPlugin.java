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

package org.modelsphere.sms.oo.forward;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.io.HTMLStringWriter;
import org.modelsphere.jack.srtool.forward.RuleException;
import org.modelsphere.jack.srtool.forward.Template;
import org.modelsphere.jack.srtool.forward.VariableDecl;
import org.modelsphere.jack.srtool.forward.VariableScope;
import org.modelsphere.sms.actions.GenerateFromTemplatesAction;
import org.modelsphere.sms.templates.GenericForwardEngineeringPlugin;

/**
 * This class provides common services for OO code generation plug-ins.
 * 
 */
public abstract class OOForwardEngineeringPlugin extends GenericForwardEngineeringPlugin {

    protected Template getFileNotFoundRule() {
        Template templ = new Template("fileNotFoundRule", "File not Found"); //NOT LOCALIZABLE
        return templ;
    }

    public void setOutputToHTMLFormat() {
        //look for a html variable
        String condition = "html"; //NOT LOCALIZABLE
        VariableScope vars = getVarScope();
        ArrayList conds = GenerateFromTemplatesAction.getConditions(vars);
        Iterator iter = conds.iterator();
        while (iter.hasNext()) {
            VariableDecl.VariableStructure variable = (VariableDecl.VariableStructure) iter.next();
            String varName = variable.getName();
            if (varName.equals(condition)) {
                variable.setValue(Boolean.TRUE);
                break;
            }
        } //end while
    }

    public void setOutputToASCIIFormat() {
        //look for a html variable
        String condition = "html"; //NOT LOCALIZABLE
        VariableScope varScope = getVarScope();
        ArrayList conds = GenerateFromTemplatesAction.getConditions(varScope);
        Iterator iter = conds.iterator();
        while (iter.hasNext()) {
            VariableDecl.VariableStructure variable = (VariableDecl.VariableStructure) iter.next();
            String varName = variable.getName();
            if (varName.equals(condition)) {
                variable.setValue(Boolean.FALSE);
                break;
            }
        } //end while
    }

    public Writer createNewPanelWriter(boolean isHTMLformat) {
        //override write() methods to replace '<' by &lt;,
        //'>' by &gt; and '&' by &amp;

        if (isHTMLformat) {
            setOutputToHTMLFormat();
            return new HTMLStringWriter();
        } else {
            return new StringWriter();
        }
    }

    protected void forwardTo(DbObject semObj, ArrayList generatedFiles) throws DbException,
            IOException, RuleException {
        // has to be coded
    }
}
