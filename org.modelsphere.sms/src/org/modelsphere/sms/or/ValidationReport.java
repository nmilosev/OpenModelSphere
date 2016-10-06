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
package org.modelsphere.sms.or;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.text.JTextComponent;

import org.modelsphere.jack.awt.TextViewerFrame;
import org.modelsphere.jack.awt.html.InternalLink;
import org.modelsphere.jack.awt.html.InternalLinkAdapter;
import org.modelsphere.jack.awt.html.InternalLinkSet;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.plugins.PluginServices;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.or.db.DbORProcedure;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.international.LocaleMgr;

public final class ValidationReport {

    private static final String EOL = "<br> " + System.getProperty("line.separator"); // NOT LOCALIZABLE
    private static final String SPACE = "&nbsp; "; // NOT LOCALIZABLE
    private static final String INDENT = SPACE + SPACE + SPACE;

    private static final String kTitle = LocaleMgr.misc.getString("validationTitle");
    private static final String kNote = LocaleMgr.misc.getString("Note");
    private static final String kNoErrorFound = LocaleMgr.misc.getString("NoErrorFound");
    private static final String kErrorCount0 = LocaleMgr.misc.getString("ErrorCount0");
    private static final String kWarningCount0 = LocaleMgr.misc.getString("WarningCount0");
    private static final String kShowProperties = LocaleMgr.misc.getString("ShowProperties");

    private static URL showPropertyImageURL = null;
    private InternalLinkSet linkSet = new InternalLinkSet();
    private StringWriter writer = new StringWriter();
    private TextViewerFrame frame;
    private String[] validationMessages;
    private Boolean[] errorFlags;
    private String[] genericValidationMessages;
    private Boolean[] genericErrorFlags;

    private int errorCount = 0;
    private int warningCount = 0;

    public StringBuffer warningString = new StringBuffer();
    public StringBuffer errorString = new StringBuffer();

    public ValidationReport(String[] validationMessages, Boolean[] errorFlags,
            String[] genericValidationMessages, Boolean[] genericErrorFlags) throws DbException {
        this.validationMessages = validationMessages;
        this.errorFlags = errorFlags;
        this.genericValidationMessages = genericValidationMessages;
        this.genericErrorFlags = genericErrorFlags;
    }

    static {
        showPropertyImageURL = ORValidationPlugin.class
                .getResource("international/resources/properties.gif");
    }

    private String getCompositeName(DbObject dbo) throws DbException {
        String compositeName = "";

        if (dbo.getComposite() instanceof DbORTable || dbo.getComposite() instanceof DbORView
                || dbo.getComposite() instanceof DbORProcedure) {
            compositeName = dbo.getComposite().getName() + "."; //NOT LOCALIZABLE
        }
        return compositeName;
    }

    /*****************************************************************************
     * Report Methods
     ****************************************************************************/
    public void showReport(int errorCount, int warningCount) {
        this.errorCount = errorCount;
        this.warningCount = warningCount;

        writeHTMLPageHeader();
        writeAllErrorsAndWarnings(errorString, warningString);
        writeHTMLPageFooter();

        frame = PluginServices.showTextInternalFrame(kTitle, writer.toString(), true);
        JEditorPane editorpane = (JEditorPane) frame.getTextPanel();
        editorpane.addHyperlinkListener(new InternalLinkAdapter() {
            protected int internalLinkActivated(DbObject dbo, String action) throws DbException {
                return InternalLinkAdapter.NONE;
            }

            protected void displayMessage(String message) {
                frame.setStatusText(message);
            }

            protected JTextComponent getTextComponent() {
                return frame.getTextPanel();
            }

            protected InternalLinkSet getInternalLinkSet() {
                return linkSet;
            }
        });

        writer.flush();
        try {
            writer.close();
        } catch (IOException e) {
        }
    }

    public void printGenericError(int msgIndex, StringBuffer buffer, DbSemanticalObject semObj,
            boolean withComposite) throws DbException {
        this.printError(msgIndex, true, buffer, semObj, withComposite, false, -1, null);
    }

    public void printGenericError(int msgIndex, StringBuffer buffer, DbSemanticalObject semObj,
            boolean withComposite, boolean withConceptName, int extraMsgIndex) throws DbException {
        this.printError(msgIndex, true, buffer, semObj, withComposite, withConceptName,
                extraMsgIndex, null);
    }

    public void printGenericError(int msgIndex, StringBuffer buffer, DbSemanticalObject semObj,
            boolean withComposite, boolean withConceptName, int extraMsgIndex,
            DbSemanticalObject semObjFound) throws DbException {
        this.printError(msgIndex, true, buffer, semObj, withComposite, withConceptName,
                extraMsgIndex, semObjFound);
    }

    public void printError(int msgIndex, StringBuffer buffer, DbSemanticalObject semObj,
            boolean withComposite) throws DbException {
        this.printError(msgIndex, false, buffer, semObj, withComposite, false, -1, null);
    }

    public void printError(int msgIndex, StringBuffer buffer, DbSemanticalObject semObj,
            boolean withComposite, boolean withConceptName, int extraMsgIndex) throws DbException {
        this.printError(msgIndex, false, buffer, semObj, withComposite, withConceptName,
                extraMsgIndex, null);
    }

    public void printError(int msgIndex, StringBuffer buffer, DbSemanticalObject semObj,
            boolean withComposite, boolean withConceptName, int extraMsgIndex,
            DbSemanticalObject semObjFound) throws DbException {
        this.printError(msgIndex, false, buffer, semObj, withComposite, withConceptName,
                extraMsgIndex, semObjFound);
    }

    public void printError(int msgIndex, boolean isGenericMsg, StringBuffer buffer,
            DbSemanticalObject semObj, boolean withComposite, boolean withConceptName,
            int extraMsgIndex, DbSemanticalObject semObjFound) throws DbException {
        String conceptName = "";
        String conceptNameFound = "";
        String errorStr = "";

        if (isGenericMsg) {
            if (genericErrorFlags[msgIndex] != null) {
                if (genericErrorFlags[msgIndex].booleanValue() == false) {
                    printValidationMsgTitle(msgIndex, isGenericMsg, buffer);
                    genericErrorFlags[msgIndex] = Boolean.TRUE;
                }
            }
        } else {
            int nb = errorFlags.length;
            if (msgIndex < nb) {
                if (errorFlags[msgIndex] != null) {
                    if (errorFlags[msgIndex].booleanValue() == false) {
                        printValidationMsgTitle(msgIndex, isGenericMsg, buffer);
                        errorFlags[msgIndex] = Boolean.TRUE;
                    }
                }
            }
        } //end if

        if (withConceptName) {
            conceptName = semObj.getMetaClass().getGUIName(false, false);
            if (semObjFound != null)
                conceptNameFound = semObjFound.getMetaClass().getGUIName(false, false);
        }

        if (extraMsgIndex != -1) {
            if (semObjFound != null) {
                errorStr = MessageFormat.format(
                        isGenericMsg ? genericValidationMessages[extraMsgIndex]
                                : validationMessages[extraMsgIndex], new Object[] { conceptName,
                                getLinksForDbObject(semObj, withComposite, true),
                                getLinksForDbObject(semObjFound, withComposite, true), EOL });
            } else {
                errorStr = MessageFormat.format(
                        (isGenericMsg ? genericValidationMessages[extraMsgIndex]
                                : validationMessages[extraMsgIndex]), new Object[] { conceptName,
                                getLinksForDbObject(semObj, withComposite, true), EOL });
            }
        } else {
            errorStr = conceptName + getLinksForDbObject(semObj, withComposite, true);// + EOL;
        }
        buffer.append(errorStr + "\n");//NOT LOCALIZABLE
    }

    private void printValidationMsgTitle(int msgIndex, boolean isGenericMsg, StringBuffer buffer) {
        if (isGenericMsg == true)
            buffer.append("\n\n" + genericValidationMessages[msgIndex] + "\n");//NOT LOCALIZABLE
        else
            buffer.append("\n\n" + validationMessages[msgIndex] + "\n");//NOT LOCALIZABLE
        buffer.append(genericValidationMessages[0] + "\n\n");//NOT LOCALIZABLE
    }

    private void writeAllErrorsAndWarnings(StringBuffer errorString, StringBuffer warningString) {
        writer.write("<PRE><CODE>");

        if (errorCount > 0) {
            writer.write("<b>");
            writer.write("\n\n" + genericValidationMessages[1] + "\n");//NOT LOCALIZABLE
            writer.write("</b>");
            writer.write(errorString.toString());
            writer.write("\n");//NOT LOCALIZABLE
        }

        if (warningCount > 0) {
            writer.write("<b>");
            writer.write("\n\n" + genericValidationMessages[2] + "\n");//NOT LOCALIZABLE
            writer.write("</b>");
            writer.write(warningString.toString());
        }

        writer.write("</CODE></PRE>");
    }

    public String getLinksForDbObject(DbObject dbo, boolean withComposite, boolean includeProperties)
            throws DbException {
        String link = "";
        String name = dbo.getName();

        if (withComposite) {
            String compositeName = this.getCompositeName(dbo);
            name = compositeName + dbo.getName();
        }
        InternalLink linkExplorer = linkSet.createInternalLink(dbo, InternalLinkAdapter.DBO_FIND,
                name);

        InternalLink linkProp = null;
        if (includeProperties)
            linkProp = linkSet.createInternalLink(dbo, InternalLinkAdapter.DBO_SHOW_PROPERTIES,
                    kShowProperties, showPropertyImageURL);

        link = linkExplorer + (linkProp == null ? "" : SPACE + linkProp.toString());
        return link;
    }

    private void writeHTMLPageHeader() {
        writer.write("<html>"); // NOT LOCALIZABLE
        writer.write("<head>"); // NOT LOCALIZABLE
        writer.write("<title>"); // NOT LOCALIZABLE
        writer.write(kTitle + EOL);
        writer.write("</title>"); // NOT LOCALIZABLE
        writer.write("</head>"); // NOT LOCALIZABLE
        writer.write("<body bgcolor=\"#FFFFFF\">"); // NOT LOCALIZABLE
        writer.write("<i>" + kNote + "</i>" + EOL + EOL); // NOT LOCALIZABLE
        writer.write("<hr>"); // NOT LOCALIZABLE
        writer.write("<b>"); // NOT LOCALIZABLE
    }

    private void writeHTMLPageFooter() {
        writer.write("<b><i>"); // NOT LOCALIZABLE
        writer.write(EOL + EOL);
        if (errorCount > 0)
            writer.write(MessageFormat.format(kErrorCount0,
                    new Object[] { new Integer(errorCount) })
                    + EOL);
        if (warningCount > 0)
            writer.write(MessageFormat.format(kWarningCount0, new Object[] { new Integer(
                    warningCount) })
                    + EOL);
        if (errorCount == 0 && warningCount == 0)
            writer.write(kNoErrorFound + EOL);
        writer.write("</i></b>"); // NOT LOCALIZABLE
        writer.write(EOL + EOL);
        writer.write("</body>"); // NOT LOCALIZABLE
        writer.write("</html>"); // NOT LOCALIZABLE
    }
}
