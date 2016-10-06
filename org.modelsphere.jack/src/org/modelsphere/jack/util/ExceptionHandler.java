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

import java.awt.Component;
import java.awt.Font;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.StringTokenizer;

import javax.swing.*;

import org.modelsphere.jack.awt.NullFrame;
import org.modelsphere.jack.awt.StatusBar;
import org.modelsphere.jack.awt.TextViewerDialog;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.debug.Log;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.python.core.PyException;

/**
 * A class that handlers uncaught exceptions.
 * 
 * <pre>
 * try {
 *     performRiskyOperations();
 * } catch (Throwable th) {
 *     MainFrame mf = MainFrame.getSingleton();
 *     ExceptionHandler.processUncatchedException(mf, th);
 * } //end try
 * </pre>
 * 
 * @author Marco Savard
 */
public final class ExceptionHandler {

    public static void throwRealException(Throwable th) throws DbException {
        if (th instanceof InvocationTargetException)
            th = ((InvocationTargetException) th).getTargetException();
        if (th instanceof TargetRuntimeException)
            th = ((TargetRuntimeException) th).getTargetException();
        if (th instanceof DbException)
            throw (DbException) th;
        if (th instanceof RuntimeException)
            throw (RuntimeException) th;
        throw new RuntimeException(th.toString());
    }

    public static void processUncatchedException(Component comp, Throwable th) {
        Debug.trace("Enterring ExceptionHandler.processUncatchedException.");
        // If Db.abortAllTrans() doesn't work, at least catch the runtime
        // exception
        // otherwise the whole application will freeze.. [MS]
        try {
            Db.abortAllTrans();
        } catch (RuntimeException ex) {
            th = ex;
        }

        if (th instanceof RuntimeException) {
            String sMessage = ((RuntimeException) th).getMessage();
            if (sMessage != null) {
                if (sMessage.equalsIgnoreCase("Invalid Db") == true) {

                    // //
                    // if we are in debug mode, continue treatment,
                    // otherwise hide the problem to the use (focusmanager has
                    // failed to update)

                    if (Debug.isDebug() == false) {
                        ApplicationContext.getFocusManager().setNullProject();
                        ApplicationContext.getFocusManager().update();
                        return;
                    }
                }
            }
        }

        DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
        if (mf != null) {
            StatusBar sb = mf.getStatusBar();
            if (sb != null)
                sb.stopWaitingBar(" ");
        }

        // get the actual exception
        if (th instanceof InvocationTargetException)
            th = ((InvocationTargetException) th).getTargetException();
        else if (th instanceof TargetRuntimeException)
            th = ((TargetRuntimeException) th).getTargetException();

        Log.add(th);

        // handle exception (or error)
        if (th instanceof ExceptionMessage) {
            showErrorMessage(comp, th.getMessage(), ((ExceptionMessage) th).getErrorType());
        } else if (th instanceof DbException || th instanceof IOException) {
            showErrorMessage(comp, th.getMessage() == null ? th.getClass().getName() : th
                    .getMessage());
        } else {
            String title = LocaleMgr.message.getString("applError");
            String message = LocaleMgr.message.getString("applErrorMessage") + "\n\n"
                    + th.toString();

            if (th instanceof PyException) {
                title = LocaleMgr.message.getString("pythonError");
                message = LocaleMgr.message.getString("pythonErrorMessage") + "\n\n"
                        + th.toString();
            }

            ByteArrayOutputStream outStr = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(outStr);
            out.println("\n"); // NOT LOCALIZABLE
            if (th instanceof PyException)
                out.println(LocaleMgr.message.getString("pythonErrorMessage"));
            out.println(LocaleMgr.message.getString("errorDetails"));
            th.printStackTrace(out);
            out.close();
            String detail = outStr.toString();
            System.out.println(detail);
            showErrorMessage(comp, message, detail, title);
        }
    }

    // Because messages can be generated by a lot of subclasses of Exception,
    // they are not necessary suitable to be displayed in a MessageDialog.
    // Some messages, like the one returned by ParseException, contain several
    // hard-coded carriage returns, which is fine for an output in a file,
    // but innappropriate to be shown in a dialog window. This function
    // converts the message to be used conveniently in a GUI form. This
    // function should not have any effect if there's no carriage return in
    // the original message. [MS]
    private static final int MESSAGE_MAX_LENGTH = 400;
    private static final int MARGIN = 40;
    private static final String EOL = "\n"; // NOT LOCALIZABLE. DO NOT replace

    // it by System.getProperty("line.separator")!
    // not suitable for MessageDialog
    private static String format(String message) {

        String formattedMessage = "";
        final String separator = "  ";
        int position = 0;

        // for each line (separated by '\r' or '\n')
        StringTokenizer st = new StringTokenizer(message, "\r\n"); // NOT
        // LOCALIZABLE,
        // escape
        // code
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            formattedMessage += token;
            position += token.length();

            // replace carriage return by "  "
            if (st.hasMoreTokens()) {
                formattedMessage += separator;
                position += separator.length();

                // if exceeds the margin, change line
                if (position > MARGIN) {
                    formattedMessage += EOL;
                    position = 0;
                } // end if
            } // end if
        } // end while

        // make sure that the error message is not too long
        if (formattedMessage.length() > MESSAGE_MAX_LENGTH) {
            formattedMessage = formattedMessage.substring(0, MESSAGE_MAX_LENGTH);
            formattedMessage += "...";
        }

        return formattedMessage;
    }

    /*
     * The message dialog must be created in the event dispatch thread.
     */
    public static void showErrorMessage(Component comp, String message) {
        String title = ApplicationContext.getApplicationName();
        showErrorMessage(comp, message, null, title);
    }

    public static void showErrorMessage(final Component comp, final String message,
            final int errorType) {
        if (!SwingUtilities.isEventDispatchThread()) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        showErrorMessage(comp, message, errorType);
                    }
                });
            } catch (Exception e) {
            }
        } else {
            JOptionPane.showMessageDialog(comp == null ? NullFrame.singleton : comp, message,
                    ApplicationContext.getApplicationName(), errorType);
        }
    }

    public static void showErrorMessage(final Component comp, final String message,
            final String detail, final String title) {
        if (!SwingUtilities.isEventDispatchThread()) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        showErrorMessage(comp, message, detail, title);
                    }
                });
            } catch (Exception e) {
            }
        } else {
            String msg = message;
            String dtl = detail;

            if (msg == null) {
                try {
                    throw new RuntimeException();
                } catch (RuntimeException ex) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    ex.printStackTrace(pw);
                    msg = ex.toString();
                    dtl = sw.toString();
                }
            }

            // String formattedMessage = format(msg);

            JOptionPane.showMessageDialog(comp == null ? NullFrame.singleton : comp, msg, title,
                    JOptionPane.ERROR_MESSAGE);
            if (dtl != null) {
                JFrame f = ApplicationContext.getDefaultMainFrame();
                TextViewerDialog.showTextDialog(f == null ? NullFrame.singleton : f, title, dtl,
                        false, true);
            }
        }
    }

    public static void showBadInstallationMessage(Component comp, Throwable t,
            String briefDescription) {
        String message = "<html><body>"; // NOT LOCALIZABLE
        Font font = UIManager.getFont("Label.font"); // NOT LOCALIZABLE
        if (font != null) {
            message += "<font size=2 face=\"" + font.getName() + "\">"; // NOT
            // LOCALIZABLE
            if ((font.getStyle() & Font.BOLD) != 0)
                message += "<b>"; // NOT LOCALIZABLE
            if ((font.getStyle() & Font.ITALIC) != 0) // NOT LOCALIZABLE
                message += "<i>"; // NOT LOCALIZABLE
        }
        message += LocaleMgr.misc.getString("ApplBadInstall"); // NOT
        // LOCALIZABLE
        if (t != null) {
            message += "<br>" + t.getMessage(); // NOT LOCALIZABLE
        }
        if (briefDescription != null) {
            message += "<br>" + briefDescription; // NOT LOCALIZABLE
        }
        if (font != null) {
            if ((font.getStyle() & Font.ITALIC) != 0)
                message += "</i>"; // NOT LOCALIZABLE
            if ((font.getStyle() & Font.BOLD) != 0)
                message += "</b>"; // NOT LOCALIZABLE
            message += "</font>"; // NOT LOCALIZABLE
        }
        message += "</body></html>"; // NOT LOCALIZABLE
        showErrorMessage(comp, message, null, ApplicationContext.getApplicationName());
    }

}
