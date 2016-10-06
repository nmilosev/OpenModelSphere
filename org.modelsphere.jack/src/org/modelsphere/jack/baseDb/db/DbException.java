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

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * A DbException is thrown when an invalid operation is performed such as accessing objects outside
 * a transaction or attempting to set invalid values. They may also result from invalid states such
 * as trying to commit/abort a transaction when no transactions has been initiated.
 * <p>
 * When a DbException occurs, opened transactions if any exists, are rolled back.
 * <p>
 * Because all the transactions are aborted when a DbException is thrown, the try/catch block should
 * be installed around the outermost transaction (which should be at the outermost level of the
 * command execution). All the methods that access DbObjects should be declared
 * <code>throws DbException</code> and should not catch the DbException themselves. A transaction at
 * the outermost level should be coded as follows:
 * 
 * <pre>
 * import jack.srtool.ApplicationContext;
 * import jack.util.ExceptionHandler;
 * 
 * try {
 *   db.beginWriteTrans(“action”);
 *   performAction();
 *   db.commitTrans();
 * } catch (Exception e) {
 *   MainFrame frame = ApplicationContext.getDefaultMainFrame();
 *   ExceptionHandler.processUncatchedException(frame, e);  
 * } //end try
 * </pre>
 * 
 */
public class DbException extends Exception {
    private Throwable target;

    public DbException(Db db, String message) {
        super(message);
        if (db != null)
            Db.abortAllTrans();
        System.out.println(toString());
    }

    DbException(Db db, String message, Throwable target) {
        super(message);
        this.target = target;
        if (db != null) {
            Db.abortAllTrans();
        }
        System.out.println(toString());
    }

    public Throwable getTargetException() {
        return target;
    }

    public void printStackTrace(PrintStream ps) {
        synchronized (ps) {
            if (target != null) {
                ps.print(getClass().getName());
                target.printStackTrace(ps);
            } else {
                super.printStackTrace(ps);
            }
        }
    }

    public void printStackTrace(PrintWriter pw) {
        synchronized (pw) {
            if (target != null) {
                pw.print(getClass().getName());
                target.printStackTrace(pw);
            } else {
                super.printStackTrace(pw);
            }
        }
    }

    public String getMessage() {
        String message = super.getMessage();
        if (message == null)
            message = "";
        if (target != null) {
            if (message.length() > 0)
                message += "\n";
            message += target.toString();
        }
        return message;
    }

}
