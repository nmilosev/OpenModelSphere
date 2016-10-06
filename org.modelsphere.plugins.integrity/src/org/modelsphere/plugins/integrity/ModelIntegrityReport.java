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

package org.modelsphere.plugins.integrity;

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
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.explorer.Explorer;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.or.db.*;

/**
 * @author pierrem
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ModelIntegrityReport {

    private static final String kTitleInteg = LocaleMgr.misc.getString("titleIntegrity");
    private static final String kTitleClean = LocaleMgr.misc.getString("titleCleanup");
    private static final String kNote = LocaleMgr.misc.getString("Note");
    private static final String kReportLine = LocaleMgr.misc.getString("integrityReportLine");
    //private static final String kReportErrorFollow    = LocaleMgr.misc.getString("integrityReportErrorFollow");
    //private static final String kReportWarningFollow  = LocaleMgr.misc.getString("integrityReportWarningFollow");
    private static final String kCleanOneTip = LocaleMgr.misc.getString("CleanOneTip");
    private static final String kCleanRuleTip = LocaleMgr.misc.getString("CleanRuleTip");
    private static final String kCleanAllTip = LocaleMgr.misc.getString("CleanAllTip");

    // ramené de AbstractIntegrity

    private static final String EOL = "<br> " + System.getProperty("line.separator"); // NOT LOCALIZABLE
    private static final String BR = "<br> "; // NOT LOCALIZABLE
    private static final String SPACE = "&nbsp; "; // NOT LOCALIZABLE
    private static final String INDENT = SPACE + SPACE + SPACE; // NOT LOCALIZABLE 

    protected static final String kClickToCorrect = LocaleMgr.misc.getString("ClickToCorrect"); // NOT LOCALIZABLE
    protected static final String kContainError01 = LocaleMgr.misc.getString("containError01"); // NOT LOCALIZABLE
    protected static final String kNoErrorFound = LocaleMgr.misc.getString("NoErrorFound"); // NOT LOCALIZABLE
    protected static final String kErrorCount0 = LocaleMgr.misc.getString("ErrorCount0"); // NOT LOCALIZABLE
    protected static final String kWarningCount0 = LocaleMgr.misc.getString("WarningCount0"); // NOT LOCALIZABLE
    protected static final String kShowProperties = LocaleMgr.misc.getString("ShowProperties"); // NOT LOCALIZABLE
    protected static final String kSolution0 = LocaleMgr.misc.getString("Solution0"); // NOT LOCALIZABLE
    protected static final String kPuceErrorText = LocaleMgr.misc.getString("puceErreurText"); // NOT LOCALIZABLE
    protected static final String kPuceWarningText = LocaleMgr.misc.getString("puceWarningText"); // NOT LOCALIZABLE

    public StringBuffer warningStrBuffer = new StringBuffer();
    public StringBuffer errorStrBuffer = new StringBuffer();
    private static URL showPropertyImageURL = null;
    private static URL showDeleteImageURL = null;
    private static URL showDeleteSectionImageURL = null;
    private static URL showDeleteAlImageURL = null;
    private static URL showPuceErrorImageURL = null;
    private static URL showPuceWarningImageURL = null;
    private InternalLinkSet links = new InternalLinkSet();
    private StringWriter writer = new StringWriter();
    private TextViewerFrame frame;
    private static boolean cleanUpMode = false;

    static {
        showPropertyImageURL = ModelIntegrityReport.class.getResource("properties.gif"); // NOT LOCALIZABLE
        showDeleteImageURL = ModelIntegrityReport.class.getResource("delete_one.gif"); // NOT LOCALIZABLE
        showDeleteSectionImageURL = LocaleMgr.misc.getUrl("Delete_rule"); // NOT LOCALIZABLE
        showDeleteAlImageURL = LocaleMgr.misc.getUrl("Delete_all"); // NOT LOCALIZABLE
        showPuceErrorImageURL = LocaleMgr.misc.getUrl("puce_erreur"); // NOT LOCALIZABLE
        showPuceWarningImageURL = LocaleMgr.misc.getUrl("puce_warning"); // NOT LOCALIZABLE 
    }

    // reçu en parametre  
    private String[] cleanUpSectionLinkStr;
    private String[] reportMessages;
    private Boolean[] errorFlags;
    protected int modelErrorCount = 0;
    protected int modelWarningCount = 0;

    ModelIntegrityReport(String[] reportMessages, Boolean[] errorFlags) throws DbException {
        this(reportMessages, errorFlags, null);
    }

    ModelIntegrityReport(String[] reportMessages, Boolean[] errorFlags,
            String[] cleanUpSectionLinkStr) throws DbException {
        this.reportMessages = reportMessages;
        this.errorFlags = errorFlags;
        if (cleanUpSectionLinkStr != null)
            this.cleanUpSectionLinkStr = cleanUpSectionLinkStr;
    }

    /*****************************************************************************
     * Report Methods
     ****************************************************************************/
    public void showReport(DbObject model, int modelErrorCount, int modelWarningCount, int operation)
            throws DbException, IOException {// ???
        this.modelErrorCount = modelErrorCount;
        this.modelWarningCount = modelWarningCount;

        writeHTMLPageHeader(model, operation);
        writeAllErrorsAndWarnings(errorStrBuffer, warningStrBuffer);
        writeHTMLPageFooter();

        String title = operation == AbstractIntegrity.INTEGRITY ? kTitleInteg : kTitleClean;
        frame = PluginServices.showTextInternalFrame(title, writer.toString(), true);
        JEditorPane editorpane = (JEditorPane) frame.getTextPanel();
        editorpane.addHyperlinkListener(new InternalLinkAdapter() {
            /*
             * Hyperliens avec Dbobject associés
             */
            protected int internalLinkActivated(DbObject dbo, String action) throws DbException {
                int status = InternalLinkAdapter.NONE;

                if (action == null) {
                    status = InternalLinkAdapter.NONE;
                    // Corrections des DataModel 
                } else if (action.equals(OrIntegrity.COLUMN_SET_NOT_NULL)) {
                    if (OrIntegrity.correctPKUKColumnsNullity((DbORColumn) dbo)) {
                        status = InternalLinkAdapter.UPDATED;
                    } else {
                        status = InternalLinkAdapter.NOT_UPDATED;
                    }
                } else if (action.equals(OrIntegrity.FK_COLUMNS_SET_NULL)
                        || action.equals(OrIntegrity.FK_MULTIPLICITY_SET_MIN_1)) {
                    if (OrIntegrity.correctChildMinConnectivity0(dbo)) {
                        status = InternalLinkAdapter.UPDATED;
                    } else {
                        status = InternalLinkAdapter.NOT_UPDATED;
                    }
                } else if (action.equals(OrIntegrity.FK_COLUMNS_SET_NOT_NULL)
                        || action.equals(OrIntegrity.FK_MULTIPLICITY_SET_MIN_0)) {
                    if (OrIntegrity.correctChildMinConnectivity1(dbo)) {
                        status = InternalLinkAdapter.UPDATED;
                    } else {
                        status = InternalLinkAdapter.NOT_UPDATED;
                    }
                } else if (action.equals(OrIntegrity.FK_CREATE_UNIQUE_INDEX)
                        || action.equals(OrIntegrity.FK_MULTIPLICITY_SET_MAX_N)) {
                    if (OrIntegrity.correctParentMaxConnectivity1(dbo)) {
                        status = InternalLinkAdapter.UPDATED;
                    } else {
                        status = InternalLinkAdapter.NOT_UPDATED;
                    }
                } else if (action.equals(OrIntegrity.FK_CREATE_NON_UNIQUE_INDEX)
                        || action.equals(OrIntegrity.FK_MULTIPLICITY_SET_MAX_1)) {
                    if (OrIntegrity.correctParentMaxConnectivityN(dbo)) {
                        status = InternalLinkAdapter.UPDATED;
                    } else {
                        status = InternalLinkAdapter.NOT_UPDATED;
                    }
                    // fin Corrections des DataModel
                } else if (action.equals(AbstractIntegrity.DELETE_ONE)) { //Epuration
                    if (AbstractIntegrity.deleteOneOccurence(dbo)) {
                        status = InternalLinkAdapter.DELETED;
                    } else {
                        status = InternalLinkAdapter.NOT_DELETED;
                    }
                }

                if (status == InternalLinkAdapter.UPDATED) {
                    dbo.setValidationStatus(DbObject.VALIDATION_OK);
                }

                return status;
            }

            /*
             * Hyperliens sans Dbobject associés
             */
            protected int internalLinkActivated(String action) throws DbException {
                if (action == null)
                    return InternalLinkAdapter.NONE;

                for (int i = 0; i < cleanUpSectionLinkStr.length; i++) {
                    String strAct = cleanUpSectionLinkStr[i];
                    if (action.equals(strAct)) {
                        if (AbstractIntegrity.deleteSectionsOccurences(i))
                            return InternalLinkAdapter.DELETED;
                        return InternalLinkAdapter.NOT_DELETED;
                    }
                }

                if (action.equals(AbstractIntegrity.DELETE_ALL)) {
                    if (AbstractIntegrity.deleteAllSectionOccurences())
                        return InternalLinkAdapter.DELETED;
                    return InternalLinkAdapter.NOT_DELETED;
                }

                return InternalLinkAdapter.NONE;
            }

            protected void displayMessage(String message) {
                frame.setStatusText(message);
            }

            protected JTextComponent getTextComponent() {
                return frame.getTextPanel();
            }

            protected InternalLinkSet getInternalLinkSet() {
                return links;
            }
        });

        writer.flush();
        try {
            writer.close();
        } catch (IOException e) {
        }
    }

    /*
     * Méthodes printError
     */

    //- MSG-4: semObj
    public void printError(int ruleMsgIndex, StringBuffer buffer, DbSemanticalObject semObj,
            boolean withComposite) throws DbException {
        this.printError(ruleMsgIndex, buffer, semObj, withComposite, false, -1, null, null, false,
                null, null);
    }

    //- MSG-5: semObj
    public void printError(int ruleMsgIndex, StringBuffer buffer, DbSemanticalObject semObj,
            boolean withComposite, boolean withConceptName) throws DbException {
        this.printError(ruleMsgIndex, buffer, semObj, withComposite, withConceptName, -1, null,
                null, false, null, null);
    }

    //- MSG-6: semObj - extraMsg
    public void printError(int ruleMsgIndex, StringBuffer buffer, DbSemanticalObject semObj,
            boolean withComposite, boolean withConceptName, int extraMsgIndex) throws DbException {
        this.printError(ruleMsgIndex, buffer, semObj, withComposite, withConceptName,
                extraMsgIndex, null, null, false, null, null);
    }

    //- MSG-7a: semObj - extraMsg - semObjFound
    public void printError(int ruleMsgIndex, StringBuffer buffer, DbSemanticalObject semObj,
            boolean withComposite, boolean withConceptName, int extraMsgIndex,
            DbSemanticalObject semObjFound) throws DbException {
        this.printError(ruleMsgIndex, buffer, semObj, withComposite, withConceptName,
                extraMsgIndex, semObjFound, null, false, null, null);
    }

    //- MSG-7b: semObj - solution_1
    public void printError(int ruleMsgIndex, StringBuffer buffer, DbSemanticalObject semObj,
            boolean withComposite, boolean withConceptName, boolean withSolution, String solution1)
            throws DbException {
        this.printError(ruleMsgIndex, buffer, semObj, withComposite, withConceptName, -1, null,
                null, withSolution, solution1, null);
    }

    //- MSG-7c semObj - extraMsg - StringSemObj
    public void printError(int ruleMsgIndex, StringBuffer buffer, DbSemanticalObject semObj,
            boolean withComposite, boolean withConceptName, int extraMsgIndex, String StringSemObj)
            throws DbException {
        this.printError(ruleMsgIndex, buffer, semObj, withComposite, withConceptName,
                extraMsgIndex, null, StringSemObj, false, null, null);
    }

    //- MSG-8a: semObj - solution_1 - solution_2
    public void printError(int ruleMsgIndex, StringBuffer buffer, DbSemanticalObject semObj,
            boolean withComposite, boolean withConceptName, boolean withSolution, String solution1,
            String solution2) throws DbException {
        this.printError(ruleMsgIndex, buffer, semObj, withComposite, withConceptName, -1, null,
                null, withSolution, solution1, solution2);
    }

    /*
     * ****** Méthode principale *******
     */
    //- MSG-11 
    public void printError(int ruleMsgIndex, StringBuffer buffer, DbSemanticalObject semObj,
            boolean withComposite, boolean withConceptName, int extraMsgIndex,
            DbSemanticalObject semObjFound, String msgStrXtra, boolean withSolution,
            String solution1, String solution2) throws DbException {

        String conceptName = "";
        String conceptNameFound = "";
        String errorStr = "";

        // ------Titre de section
        if (ruleMsgIndex < errorFlags.length) {
            if ((errorFlags[ruleMsgIndex] != null)
                    && (errorFlags[ruleMsgIndex].booleanValue() == false)) {
                printRuleSectionTitle(ruleMsgIndex, buffer);
                errorFlags[ruleMsgIndex] = Boolean.TRUE;
            }
        }

        //*** MESSAGE D'ERREUR ***
        if (withConceptName) {
            conceptName = semObj.getMetaClass().getGUIName(false, false);
            errorStr = errorStr + conceptName + SPACE;
        }
        errorStr = errorStr + getLinksForDbObject(semObj, withComposite, true);

        if (extraMsgIndex != -1) {
            errorStr = errorStr + SPACE + reportMessages[extraMsgIndex];
        }
        if (semObjFound != null) {
            errorStr = errorStr + getLinksForDbObject(semObjFound, withComposite, true);
        }
        if (msgStrXtra != null) {
            errorStr = errorStr + SPACE + msgStrXtra;
        }
        if (withSolution) {
            if (solution1 != null) {
                errorStr = errorStr + BR + INDENT + AbstractIntegrity.kSolution1 + solution1;
            }
            if (solution2 != null) {
                errorStr = errorStr + BR + INDENT + INDENT + AbstractIntegrity.kSolution2
                        + solution2;
            }
        }
        if (cleanUpMode) {
            String delete_1 = this.getSolutionLink(semObj, AbstractIntegrity.DELETE_ONE,
                    kCleanOneTip, showDeleteImageURL);
            errorStr = errorStr + SPACE + SPACE + delete_1;
        }
        buffer.append(errorStr + "\n");//NOT LOCALIZABLE
    }

    private void printRuleSectionTitle(int msgIndex, StringBuffer buffer) throws DbException {
        buffer.append("\n");//NOT LOCALIZABLE
        buffer.append(kReportLine + "\n");//NOT LOCALIZABLE
        buffer.append(reportMessages[msgIndex] + "\n");//NOT LOCALIZABLE
        buffer.append(kReportLine + "\n");//NOT LOCALIZABLE
        if (cleanUpMode) {
            String deleteSectionOcc = this.getCleanupLink(cleanUpSectionLinkStr[msgIndex],
                    kCleanRuleTip, showDeleteSectionImageURL);
            buffer.append(deleteSectionOcc + "\n\n");//NOT LOCALIZABLE
        }
    }

    private void writeAllErrorsAndWarnings(StringBuffer errorStrBuffer,
            StringBuffer warningStrBuffer) {
        writer.write("<PRE><CODE>");

        if (modelErrorCount > 0) {
            writer.write("<b>");
            writer.write("\n\n");
            writer.write("<IMG SRC=\"" + showPuceErrorImageURL + "\" ALIGN=BOTTOM ALT=\""
                    + "\" BORDER=0>" + SPACE);
            writer.write("<font size =\"5\">" + "<font face=\"Tahoma\">"
                    + "<font color=\"#840000\">" + kPuceErrorText + "</font>" + "</font>"
                    + "</font>");
            writer.write("</b>" + "\n");
            writer.write(errorStrBuffer.toString());
            writer.write("\n");//NOT LOCALIZABLE
        }

        if (modelWarningCount > 0) {
            writer.write("<b>");
            writer.write("\n\n");
            writer.write("<IMG SRC=\"" + showPuceWarningImageURL + "\" ALIGN=BOTTOM ALT=\""
                    + "\" BORDER=0>" + SPACE);
            writer.write("<font size =\"5\">" + "<font face=\"Tahoma\">"
                    + "<font color=\"#8b4513\">" + kPuceWarningText + "</font>" + "</font>"
                    + "</font>");
            writer.write("</b>" + "\n");
            writer.write(warningStrBuffer.toString());
        }

        writer.write("</CODE></PRE>");
    }

    public String getLinksForDbObject(DbObject dbo, boolean withComposite, boolean includeProperties)
            throws DbException {
        String link = "";
        String name = "";

        if (withComposite) {
            String compositeName = this.getCompositeName(dbo);
            name = compositeName + getDisplayName(dbo);
        } else
            name = getDisplayName(dbo);
        InternalLink linkExplorer = links.createInternalLink(dbo, InternalLinkAdapter.DBO_FIND,
                name);

        InternalLink linkProp = null;
        if (includeProperties)
            linkProp = links.createInternalLink(dbo, InternalLinkAdapter.DBO_SHOW_PROPERTIES,
                    kShowProperties, showPropertyImageURL);

        link = linkExplorer + (linkProp == null ? "" : SPACE + linkProp.toString());
        return link;
    }

    public void setCleanUpMode(boolean isCleanupMode) {
        cleanUpMode = isCleanupMode;
    }

    private String getCompositeName(DbObject dbo) throws DbException {
        String compositeName = "";

        if (dbo.getComposite() instanceof DbORTable || dbo.getComposite() instanceof DbORView
                || dbo.getComposite() instanceof DbORAssociation) {
            DbObject compDbo = dbo.getComposite();
            compositeName = getDisplayName(compDbo) + "."; //NOT LOCALIZABLE
        }
        return compositeName;
    }

    protected String getSolutionLink(DbObject dbo, String actionIdentifier, String SolutionString)
            throws DbException {
        return this.getSolutionLink(dbo, actionIdentifier, SolutionString, null);
    }

    protected String getSolutionLink(DbObject dbo, String actionIdentifier, String SolutionString,
            URL imageUrl) throws DbException {
        String solutionLink = "";

        solutionLink = links.createInternalLink(dbo, actionIdentifier, SolutionString, imageUrl)
                .toString();

        return solutionLink;
    }

    protected String getCleanupLink(String actionIdentifier, String SolutionString, URL imageUrl)
            throws DbException {
        String operationLink = "";

        operationLink = links.createActionLink(actionIdentifier, SolutionString, imageUrl)
                .toString();

        return operationLink;
    }

    private void writeHTMLPageHeader(DbObject model, int operation) throws DbException {
        writer.write("<html>"); // NOT LOCALIZABLE
        writer.write("<head>"); // NOT LOCALIZABLE
        String title = operation == AbstractIntegrity.INTEGRITY ? kTitleInteg : kTitleClean;
        writer.write("<TR><TD CLASS=Titre><FONT SIZE=+2>" + title);
        writer.write("</FONT></TD></TR>");
        writer.write("</head>"); // NOT LOCALIZABLE
        writer.write("<body bgcolor=\"#FFFFFF\">"); // NOT LOCALIZABLE
        writer.write("<i>" + kNote + "</i>" + EOL + EOL); // NOT LOCALIZABLE
        writer.write("<hr>"); // NOT LOCALIZABLE
        writer.write("<b>"); // NOT LOCALIZABLE
        if (model instanceof DbBEModel) {
            writer.write(MessageFormat.format(AbstractBEIntegrity.kBeModel,
                    new Object[] { getLinksForDbObject(model, false, false) })
                    + EOL);
        } else {
            writer.write(MessageFormat.format(AbstractORIntegrity.kDataModel,
                    new Object[] { getLinksForDbObject(model, false, false) })
                    + EOL);
        }
        writer.write("</b>");
    }

    private void writeHTMLPageFooter() throws DbException {
        writer.write("<b><i>"); // NOT LOCALIZABLE
        writer.write(EOL + EOL);
        if (modelErrorCount > 0)
            writer.write(MessageFormat.format(kErrorCount0, new Object[] { new Integer(
                    modelErrorCount) })
                    + EOL);
        if (modelWarningCount > 0)
            writer.write(MessageFormat.format(kWarningCount0, new Object[] { new Integer(
                    modelWarningCount) })
                    + EOL);
        if (modelErrorCount == 0 && modelWarningCount == 0)
            writer.write(kNoErrorFound + EOL);
        writer.write("</i></b>"); // NOT LOCALIZABLE

        if (cleanUpMode) {
            String deleteAllOcc = this.getCleanupLink(AbstractIntegrity.DELETE_ALL, kCleanAllTip,
                    showDeleteAlImageURL);
            writer.write(EOL + deleteAllOcc + EOL);
        }

        writer.write(EOL + EOL);
        writer.write("</body>"); // NOT LOCALIZABLE
        writer.write("</html>"); // NOT LOCALIZABLE
    }

    /*
     * retourne le nom à afficher
     */
    private String getDisplayName(DbObject dbo) throws DbException {
        String dboDisplayName = null;
        if (dbo.getName() != null) {
            if (dbo.getName().length() != 0)
                dboDisplayName = dbo.getName();
            else {
                dboDisplayName = ApplicationContext.getSemanticalModel().getDisplayText(dbo,
                        Explorer.class);
            }
        } else {
            dboDisplayName = ApplicationContext.getSemanticalModel().getDisplayText(dbo,
                    Explorer.class);
        }
        return dboDisplayName;
    }//end getDisplayName

}
