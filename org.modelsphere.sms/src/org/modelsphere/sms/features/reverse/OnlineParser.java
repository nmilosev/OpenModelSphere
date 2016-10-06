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

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVParameter;

public interface OnlineParser extends org.modelsphere.jack.srtool.reverse.file.OnlineParser {
    void setActions(Actions actions);

    Actions getActions();

    public void parseFieldDeclaration(DbOOAdt adt, DbJVDataMember field) throws DbException;

    public void parseFormalParameter(DbJVMethod oper, DbJVParameter param) throws DbException;

    public void parseMethodDeclaration(DbOOAdt adt, DbJVMethod oper) throws DbException;
}
