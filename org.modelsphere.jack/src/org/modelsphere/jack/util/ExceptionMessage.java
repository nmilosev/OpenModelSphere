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

package org.modelsphere.jack.util;

import javax.swing.JOptionPane;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;

/**
 * A subclass of DbException, with error, warning or information levels.
 * 
 * @author Marco Savard
 */
public class ExceptionMessage extends DbException {

    // //
    // enumerations for error type

    public static final int E_ERROR = JOptionPane.ERROR_MESSAGE;
    public static final int E_WARNING = JOptionPane.WARNING_MESSAGE;
    public static final int E_INFORMATION = JOptionPane.INFORMATION_MESSAGE;

    // //
    // enumaretions for error level

    public static final int L_NORMAL = 1;
    public static final int L_DETAILED = 2;

    private int errorType;
    private int errorLevel;

    public ExceptionMessage(String message, int errorType, int errorLevel) {
        super(null, message);
        this.errorType = errorType;
        this.errorLevel = errorLevel;
    }

    /**
     * @param db
     * @param message
     */
    public ExceptionMessage(Db db, String message) {
        super(db, message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @return Returns the errorLevel.
     */
    public int getErrorLevel() {
        return errorLevel;
    }

    /**
     * @param errorLevel
     *            The errorLevel to set.
     */
    public void setErrorLevel(int errorLevel) {
        this.errorLevel = errorLevel;
    }

    /**
     * @return Returns the errorType.
     */
    public int getErrorType() {
        return errorType;
    }

    /**
     * @param errorType
     *            The errorType to set.
     */
    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }
}
