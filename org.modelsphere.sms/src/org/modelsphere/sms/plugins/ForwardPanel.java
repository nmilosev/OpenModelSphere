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

package org.modelsphere.sms.plugins;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.screen.ScreenTabPanel;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.srtool.forward.EditionCode;
import org.modelsphere.jack.srtool.forward.JackForwardEngineeringPlugin;
import org.modelsphere.jack.srtool.forward.PropertyScreenPreviewInfo;
import org.modelsphere.jack.srtool.forward.Rule;
import org.modelsphere.jack.srtool.forward.RuleException;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.templates.SQLForwardEngineeringPlugin;

public final class ForwardPanel extends JPanel implements ScreenTabPanel {

    private DbSemanticalObject semanticalObject;
    private ForwardEditorPane editPane;
    private ScrollPane scrollPane = new ScrollPane();
    private JackForwardEngineeringPlugin forward;
    private JButton editBtn = null;
    private JButton parseBtn = null;
    private MetaClass metaClass;
    private boolean m_isEditable = false;
    private JScrollPane m_htmlView;
    private JPanel m_borderPanel;
    private JPanel controlPanel = null;
    private JButton[] jButtonList;
    private Vector vecButton = new Vector();
    private ForwardPanelOptions m_options;
    private static final String WRITING_ERROR = LocaleMgr.message.getString("WRITING_ERROR");
    private PropertyScreenPreviewInfo m_info;

    //
    // Constructor and method called by the constructor
    //
    public ForwardPanel(DbSemanticalObject aSemanticalObject,
            JackForwardEngineeringPlugin aForward, boolean isEditable) throws DbException {
        super();

        Class[] supportedClasses = null;
        try {
            Debug.assert2(aForward != null);
            PropertyScreenPreviewInfo info = aForward.getPropertyScreenPreviewInfo();
            Debug.assert2((info != null) && (aSemanticalObject != null));
            supportedClasses = info.getSupportedClasses();
        } catch (Exception e) {
        }

        semanticalObject = aSemanticalObject;
        // metaClass = aSemanticalObject.getMetaClass();
        forward = aForward;
        m_info = forward.getPropertyScreenPreviewInfo();
        String contentType = m_info.getContentType();
        m_isEditable = isEditable;
        m_options = new ForwardPanelOptions(contentType, !m_isEditable);
        initPanel(supportedClasses);
    }

    // only called by the constructor
    private void initPanel(Class[] supportedClasses) throws DbException {
        setLayout(new BorderLayout());
        editPane = new ForwardEditorPane();

        /*
         * postpone at the refresh to reduce memory leak, see PropertiesFrame if (contentType !=
         * null) editPane.setContentType(contentType);
         */

        editPane.setEditable(false);
        editPane.setText(LocaleMgr.screen.getString("PanelNotRefresh"));

        m_htmlView = new JScrollPane(editPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        // scrollPane.add(editPane);

        m_borderPanel = new JPanel(new BorderLayout());
        m_borderPanel.add(m_htmlView, BorderLayout.CENTER);
        // m_borderPanel.add(scrollPane);

        m_borderPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 5, 5));
        add(m_borderPanel, BorderLayout.CENTER);
        controlPanel = createControlPanel(supportedClasses);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 6, 6));
        add(controlPanel, BorderLayout.SOUTH);
    }

    /*
     * private void addEditionButtons(JPanel panel) { //Edit Button editBtn = new
     * JButton(LocaleMgr.screen.getString("Edit")); editBtn.addActionListener(new ActionListener(){
     * public void actionPerformed(ActionEvent e){ editForward(); } }); vecButton.add(editBtn);
     * panel.add(editBtn);
     * 
     * //Parse Button parseBtn = new JButton(LocaleMgr.screen.getString("Parse"));
     * parseBtn.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e) {
     * //processForward(); } }); vecButton.add(parseBtn); panel.add(parseBtn); }
     */

    private JPanel createControlPanel(final Class[] supportedClasses) throws DbException {

        JPanel panel = new JPanel();
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Refresh button
        JButton refreshBtn = new JButton(LocaleMgr.screen.getString("Refresh"));
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    updateForwardPanel(supportedClasses);
                } catch (DbException ex) {
                    editPane.setText(ex.toString());
                } catch (IOException ex) {
                    editPane.setText(ex.toString());
                }
            }
        });
        vecButton.add(refreshBtn);
        panel.add(refreshBtn);

        /*
         * //OnlineReverse onlineReverse = forward.getOnlineReverse(metaClass); /* if
         * ((onlineReverse != null) && (onlineReverse.canReverse())) { //ENABLE EDITION BUTTONS
         * addEditionButtons(panel); } /
         * 
         * jButtonList = new JButton[vecButton.size()]; vecButton.copyInto(jButtonList);
         * AwtUtil.normalizeButtonDimension(jButtonList);
         */
        return panel;
    }

    private boolean isSupported(Class claz, Class[] supportedClasses) {
        boolean supported = false;

        for (int i = 0; i < supportedClasses.length; i++) {
            if (supportedClasses[i].isAssignableFrom(claz)) {
                supported = true;
                break;
            }
        } // end for

        return supported;
    }

    private void updateForwardPanel(Class[] supportedClasses) throws DbException, IOException {

        if (m_options == null)
            m_options = new ForwardPanelOptions(m_info.getContentType(), !m_isEditable);

        if (editBtn != null)
            editBtn.setEnabled(!m_isEditable); // edit mode disabled if already
        // in edition

        if (parseBtn != null)
            parseBtn.setEnabled(m_isEditable); // parse enabled if in edition

        if (editPane == null)
            return;

        String contentType = editPane.getContentType();
        if (m_options.contentType != null && !contentType.equals(m_options.contentType)) {
            editPane.setContentType(m_options.contentType); // this function
            // call is very slow
            // (600 ms on a 466
            // Mhz/256 Mb RAM)
        }

        if (!m_options.refreshText)
            return;

        Class claz = semanticalObject.getClass();
        if (isSupported(claz, supportedClasses)) {
            Rule rule = forward.getRuleOf(semanticalObject);

            if (rule != null) {
                boolean isInHtml = true;

                if (forward instanceof SQLForwardEngineeringPlugin) {
                    // set default settings
                    SQLForwardEngineeringPlugin genericForward = (SQLForwardEngineeringPlugin) forward;
                    isInHtml = genericForward.isOutputInHTMLFormat();
                } // end if

                semanticalObject.getDb().beginTrans(Db.READ_TRANS);
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                Writer writer = forward.createNewPanelWriter(m_options.isHTMLFormat);
                forward.setWriter(writer);
                String s;

                try {
                    DbObject refObject = null;
                    MetaField[] metafields = null;
                    Controller controller = null;
                    ArrayList excludeList = null;

                    Rule.RuleOptions options = new Rule.RuleOptions(refObject, metafields,
                            controller, excludeList);
                    rule.expand(writer, semanticalObject, options);
                    String unprocessedEditionCode = writer.toString();
                    s = EditionCode.processEditionCode(unprocessedEditionCode);
                } catch (RuleException ex) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    ex.printStackTrace(pw);
                    s = sw.toString();
                } catch (RuntimeException ex) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    ex.printStackTrace(pw);
                    s = sw.toString();
                } // end try

                editPane.setText(s); // Very slow (2503 ms 1st call, 1041 ms
                // next calls, on a 466 Mhz/256 Mb RAM)
                editPane.setCaretPosition(0);
                semanticalObject.getDb().commitTrans();
                setCursor(Cursor.getDefaultCursor());

                // restore value
                if (forward instanceof SQLForwardEngineeringPlugin) {
                    SQLForwardEngineeringPlugin genericForward = (SQLForwardEngineeringPlugin) forward;
                    if (isInHtml) {
                        genericForward.setOutputToHTMLFormat();
                    } else {
                        genericForward.setOutputToASCIIFormat();
                    } // end if
                } // end if
            } // end if
        } // end if
    } // updateForwardPanel()

    public void updateForward(boolean isEditable) throws IOException, DbException {

        PropertyScreenPreviewInfo info = forward.getPropertyScreenPreviewInfo();
        String contentType = info.getContentType();
        Class[] supportedClasses = info.getSupportedClasses();

        if (isEditable != m_isEditable) {
            m_isEditable = isEditable;
            m_options = new ForwardPanelOptions(contentType, !m_isEditable);
        } // end if

        updateForwardPanel(supportedClasses);
    } // end updateForward()

    private void updateForward(boolean isEditable, boolean refreshText) throws IOException,
            DbException {

        updateForward(isEditable);
        m_options.refreshText = refreshText;
    } // end updateForward()

    /*
     * private void editForward() { //update forward panel in plain text updateForward(true); }
     */

    /*
     * //Called by parseForward private void parseSemanticalObject(OnlineReverse onlineReverse,
     * OnlineParser parser, InputStream input, DbSemanticalObject semObj) throws
     * org.modelsphere.jack.baseDb.db.DbException, org.modelsphere.sms.oo.reverse.ReverseException {
     * 
     * Actions actions = onlineReverse.getReverse().getActions(); if (parser == null) parser =
     * onlineReverse.getOnlineParser(input);
     * 
     * //try { if (semObj instanceof DbDataMember) { DbAdt adt = (DbAdt)semObj.getComposite();
     * DbDataMember field = actions.getCurrentField(); parser.parseFieldDeclaration(adt,
     * (DbDataMember)semObj); semObj = field; //semObj.remove(); //remove previous field if parsing
     * successful //semObj = null; } else if (semObj instanceof DbParameter) { DbOperation oper =
     * (DbOperation)semObj.getComposite(); parser.parseFormalParameter(oper, (DbParameter)semObj);
     * //semObj.remove(); //remove previous parameter if parsing successful //semObj = null; } else
     * if (semObj instanceof DbOperation) { DbAdt adt = (DbAdt)semObj.getComposite();
     * parser.parseMethodDeclaration(adt, (DbOperation)semObj); //semObj.remove(); //remove previous
     * method if parsing successful //semObj = null; } //} catch (RuntimeException ex) { //
     * org.modelsphere.jack .util.ExceptionHandler.processUncatchedException(org.modelsphere
     * .sms.oo.MainFrame.getSingleton(), ex); //} }
     */

    /*
     * //Called by processForward() private boolean parseForward(OnlineReverse onlineReverse,
     * InputStream input, Vector obsoleteObjectVector) throws DbException, ReverseException {
     * boolean successful = false; OnlineParser parser = null;
     * 
     * //try { ReverseParameters params = onlineReverse.getReverse().getRevParameters(); parser =
     * onlineReverse.getOnlineParser(input); Actions actions = parser.getActions();
     * 
     * actions.setDestinationProject(semanticalObject); actions.init(null, null, null, null, null,
     * obsoleteObjectVector, params); parseSemanticalObject(onlineReverse, parser, input,
     * semanticalObject);
     * 
     * //if we can reach this line, it's successful successful = true;
     * 
     * //} finally { parser.getActions().exit(null); return successful; //} }
     */

    /*
     * //Called when user press parse button private void processForward() { String text =
     * editPane.getText(); String filteredText = StringUtil.replaceWords(text, "<br>", "\n"); //NOT
     * LOCALIZABLE editPane.setText(filteredText);
     * 
     * boolean successful = false; OnlineReverse onlineReverse = null; java.io.ByteArrayInputStream
     * inputStream = null; OnlineParser parser = null;
     * 
     * try { org.modelsphere.jack.io.LineRandomAccessInput input = new
     * org.modelsphere.jack.io.LineRandomAccessInput(filteredText.getBytes());
     * 
     * String transName = LocaleMgr.screen.getString("OnlineReverse");
     * semanticalObject.getDb().beginTrans(Db.WRITE_TRANS, transName); onlineReverse =
     * forward.getOnlineReverse(metaClass); Actions actions =
     * onlineReverse.getReverse().getActions(); inputStream = input.getInputStream(); parser =
     * onlineReverse.getOnlineParser(inputStream); Vector obsoleteObjectVector = new Vector();
     * 
     * // Get the appropriate actions from onlineReverse if (actions != null)
     * parser.setActions(actions); else parser.setActions(new Actions.DefaultActions());
     * 
     * // Parse with the appropriate reverse org.modelsphere.sms.oo.reverse.Reverse reverse =
     * onlineReverse.getReverse(); InputStreamPositions positions =
     * (InputStreamPositions)reverse.getPositions(); parseForward(onlineReverse, inputStream,
     * obsoleteObjectVector);
     * 
     * input = new org.modelsphere.jack.io.LineRandomAccessInput(filteredText.getBytes());
     * org.modelsphere.sms.oo.reverse.plugins.IncompletedObject.completeAll(
     * positions.getVecOfIncompletedObject(), input, positions, semanticalObject.getDb());
     * 
     * semanticalObject.getDb().commitTrans(); successful = true;
     * 
     * } catch (Exception ex) { org.modelsphere.jack.util.ExceptionHandler.processUncatchedException
     * (org.modelsphere.sms.MainFrame.getSingleton(), ex); } finally { if (parser != null) {
     * parser.resetInput(inputStream); try { inputStream.close(); } catch (IOException ex) {
     * //ignore it } }
     * 
     * if (onlineReverse != null) onlineReverse.clearOnlineParser(); updateForward(successful ?
     * false : true, successful ? true : false); } }
     */

    public final String getTabName() {
        PropertyScreenPreviewInfo info = forward.getPropertyScreenPreviewInfo();
        return info.getTabName();
    }

    // ////////////////////////////////
    // ScreenTabPanel SUPPORT
    //
    public final void activateTab() {
        try {
            updateForward(false);
        } catch (DbException ex) {
            editPane.setText(ex.toString());
        } catch (IOException ex) {
            editPane.setText(ex.toString());
        }
    }

    public final void refresh() throws DbException {
    }

    public final void deinstallPanel() {
    }

    //
    // End of ScreenTabPanel SUPPORT
    // ///////////////////////////////

    /*
     * INNER CLASSES
     */
    private final class ForwardEditorPane extends JEditorPane {
        private String m_forwardText;

        ForwardEditorPane() {
        }

        public void setText(String text) {
            m_forwardText = text;
            try {
                super.setText(text);
            } catch (EmptyStackException ex) {
                // sometimes setText() throws this exception, without any known
                // explanation
                // just ignore it
            }
        }

        // Text is altered by superclass, remove html tags
        public String getText() {

            String text = super.getText();
            String body = text.substring(text.indexOf("<body>") + 6, // Note: 6
                    // ==
                    // sizeof
                    // "<body>"
                    // //NOT
                    // LOCALIZABLE
                    text.lastIndexOf("</body>")); // NOT LOCALIZABLE

            return body;
        }
    } // end FowardEditorPane

    private static class ForwardPanelOptions {
        String contentType;
        boolean isHTMLFormat;
        boolean refreshText = true;

        ForwardPanelOptions(String aContentType, boolean htmlFormat) {
            contentType = aContentType;
            isHTMLFormat = htmlFormat;
        }
    } // end ForwardPanelOptions
}
