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

package org.modelsphere.jack.awt;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.international.LocaleMgr;

public class JackOptionPane extends JOptionPane {

    public static final int YES_YESTOALL_NO_CANCEL_OPTION = 10;

    private boolean hasYesToAllOption = false;

    private static String yesButtonLabel = LocaleMgr.screen.getString("Yes"); // NOT
    // LOCALIZABLE
    private static String yesToAllButtonLabel = LocaleMgr.screen.getString("YesToAll"); // NOT LOCALIZABLE
    private static String noButtonLabel = LocaleMgr.screen.getString("No"); // NOT
    // LOCALIZABLE
    private static String cancelButtonLabel = LocaleMgr.screen.getString("Cancel"); // NOT LOCALIZABLE
    private static Object[] options = new Object[] { yesButtonLabel, yesToAllButtonLabel,
            noButtonLabel, cancelButtonLabel }; // NOT

    // LOCALIZABLE

    // ************************************************************************
    // //
    // Note : there is no constructor
    // it is too much troule because JOptionPane doesnt support new options
    // like YES_YESTOALL_NO_CANCEL_OPTION
    // so with this class, please use ONLY the followings methods
    //
    // Important :
    // Be careful with YES_OPTION, NO_OPTION and CANCEL_OPTION.
    // They dont mean anything with the "Yes, Yes to All, No, Cancel" option //
    // NOT LOCALIZABLE
    // When using YES_YESTOALL_NO_CANCEL_OPTION , the values return by
    // showConfirmDialog are either 0 (Yes), 1 (Yes to All), 2 (No) or 3(Cancel)
    //
    // Example 1 - Yes, No and Cancel :
    // int returnValue =
    // returnValue = JackOptionPane.showConfirmDialog(null, message, "", // NOT
    // LOCALIZABLE
    // JackOptionPane.YES_NO_CANCEL_OPTION,
    // JackOptionPane.QUESTION_MESSAGE);
    // switch(returnValue){
    // case JackOptionPane.YES_OPTION : /* do something */
    // case JackOptionPane.NO_OPTION : /* do something */
    // case JackOptionPane.CANCEL_OPTION : /* do something */
    // }
    //
    // Example 2 - Yes, Yes To All, No and Cancel :
    // returnValue = JackOptionPane.showConfirmDialog(null, message, "", // NOT
    // LOCALIZABLE
    // JackOptionPane.YES_YESTOALL_NO_CANCEL_OPTION,
    // JackOptionPane.QUESTION_MESSAGE);
    // switch(returnValue){
    // case 0 /* YES */: /* do something */
    // case 1 /* YES TO ALL */: /* do something */
    // case 2 /* NO */: /* do something */
    // case 3 /* CANCEL */: /* do something */
    // }
    // ************************************************************************
    // //

    public void setOptionType(int newType) {
        if (newType == JackOptionPane.YES_YESTOALL_NO_CANCEL_OPTION) {
            hasYesToAllOption = true;
            super.setOptionType(super.YES_NO_CANCEL_OPTION);
        } else {
            hasYesToAllOption = false;
            super.setOptionType(newType);
        }
    }

    public int getOptionType() {
        if (hasYesToAllOption)
            return JackOptionPane.YES_YESTOALL_NO_CANCEL_OPTION;

        return super.getOptionType();
    }

    /*
     * public static int showConfirmDialog(Component parentComponent, Object message){ return
     * showOptionDialog(parentComponent, message, "Select an Option", YES_NO_CANCEL_OPTION,
     * QUESTION_MESSAGE, null, options, options[0]); // NOT LOCALIZABLE }
     */

    // ************************************************************************
    // //
    // JOptionPane doesnt support new options like YES_YESTOALL_NO_CANCEL_OPTION
    // so we have to cheat a little
    // ************************************************************************
    // //
    public static int showConfirmDialog(Component parentComponent, Object message, String title,
            int optionType) {
        if (optionType == YES_YESTOALL_NO_CANCEL_OPTION)
            // we could call showOptionDialog() of this class, but calling it
            // from the superclass is a shortcut
            // in the end, it is the same but a little faster
            return JOptionPane.showOptionDialog(parentComponent, message, title,
                    JOptionPane.YES_NO_CANCEL_OPTION, QUESTION_MESSAGE, null, options, options[0]);
        // return showOptionDialog(parentComponent, message, title,
        // JOptionPane.YES_NO_CANCEL_OPTION, QUESTION_MESSAGE, null, options,
        // options[0]);
        else
            return JOptionPane.showConfirmDialog(parentComponent, message, title, optionType);
    }

    public static int showConfirmDialog(Component parentComponent, Object message, String title,
            int optionType, int messageType) {
        if (optionType == YES_YESTOALL_NO_CANCEL_OPTION)
            return JOptionPane.showOptionDialog(parentComponent, message, title,
                    JOptionPane.YES_NO_CANCEL_OPTION, messageType, null, options, options[0]);
        else
            return JOptionPane.showConfirmDialog(parentComponent, message, title, optionType,
                    messageType);
    }

    public static int showConfirmDialog(Component parentComponent, Object message, String title,
            int optionType, int messageType, Icon icon) {
        if (optionType == YES_YESTOALL_NO_CANCEL_OPTION)
            return JOptionPane.showOptionDialog(parentComponent, message, title,
                    JOptionPane.YES_NO_CANCEL_OPTION, messageType, icon, options, options[0]);
        else
            return JOptionPane.showConfirmDialog(parentComponent, message, title, optionType,
                    messageType, icon);
    }

    public static int showOptionDialog(Component parentComponent, Object message, String title,
            int optionType, int messageType, Icon icon, Object[] options, Object initialValue) {
        if (optionType == YES_YESTOALL_NO_CANCEL_OPTION)
            return JOptionPane.showOptionDialog(parentComponent, message, title,
                    JOptionPane.YES_NO_CANCEL_OPTION, messageType, icon, options, initialValue);
        else
            return JOptionPane.showOptionDialog(parentComponent, message, title, optionType,
                    messageType, icon, options, initialValue);
    }

    // Internal Dialogs
    public static int showInternalConfirmDialog(Component parentComponent, Object message,
            String title, int optionType) {
        if (optionType == YES_YESTOALL_NO_CANCEL_OPTION)
            return JOptionPane.showInternalOptionDialog(parentComponent, message, title,
                    JOptionPane.YES_NO_CANCEL_OPTION, QUESTION_MESSAGE, null, options, options[0]);
        else
            return JOptionPane.showInternalConfirmDialog(parentComponent, message, title,
                    optionType);
    }

    public static int showInternalConfirmDialog(Component parentComponent, Object message,
            String title, int optionType, int messageType) {
        if (optionType == YES_YESTOALL_NO_CANCEL_OPTION)
            return JOptionPane.showInternalOptionDialog(parentComponent, message, title,
                    JOptionPane.YES_NO_CANCEL_OPTION, messageType, null, options, options[0]);
        else
            return JOptionPane.showInternalConfirmDialog(parentComponent, message, title,
                    optionType, messageType);
    }

    public static int showInternalConfirmDialog(Component parentComponent, Object message,
            String title, int optionType, int messageType, Icon icon) {
        if (optionType == YES_YESTOALL_NO_CANCEL_OPTION)
            return JOptionPane.showInternalOptionDialog(parentComponent, message, title,
                    JOptionPane.YES_NO_CANCEL_OPTION, messageType, icon, options, options[0]);
        else
            return JOptionPane.showInternalConfirmDialog(parentComponent, message, title,
                    optionType, messageType, icon);
    }

    public static int showInternalOptionDialog(Component parentComponent, Object message,
            String title, int optionType, int messageType, Icon icon, Object[] options,
            Object initialValue) {
        if (optionType == YES_YESTOALL_NO_CANCEL_OPTION)
            return JOptionPane.showInternalOptionDialog(parentComponent, message, title,
                    JOptionPane.YES_NO_CANCEL_OPTION, messageType, icon, options, initialValue);
        else
            return JOptionPane.showInternalOptionDialog(parentComponent, message, title,
                    optionType, messageType, icon, options, initialValue);
    }

    // *************
    // DEMO FUNCTION
    // *************
    private static void runDemo() {
        int returnValue;
        returnValue = JackOptionPane.showConfirmDialog(null, "pane1"); // NOT
        // LOCALIZABLE
        Debug.trace(new Integer(returnValue));

        returnValue = JackOptionPane.showConfirmDialog(null, "pane2", "title2",
                JackOptionPane.YES_NO_CANCEL_OPTION); // NOT LOCALIZABLE
        Debug.trace(new Integer(returnValue));

        returnValue = JackOptionPane.showConfirmDialog(null, "pane3", "title3",
                JackOptionPane.YES_YESTOALL_NO_CANCEL_OPTION); // NOT
        // LOCALIZABLE
        Debug.trace(new Integer(returnValue));

        // javax.swing.JFrame frame = new javax.swing.JFrame();
        // javax.swing.JPanel panel = new javax.swing.JPanel();
        // javax.swing.JScrollPane pane = new javax.swing.JScrollPane(panel);

        // frame.getContentPane().add(pane);
        // frame.setVisible(true);
        // returnValue = SMSOptionPane.showInternalConfirmDialog(panel, "pane4",
        // "title4", SMSOptionPane.YES_NO_CANCEL_OPTION); // NOT LOCALIZABLE
        // Debug.trace(new Integer(returnValue));

        // System.exit(0);
    }

    // RUN THE DEMO
    /*
     * public static void main(String[] args) { runDemo(); }
     */
}
