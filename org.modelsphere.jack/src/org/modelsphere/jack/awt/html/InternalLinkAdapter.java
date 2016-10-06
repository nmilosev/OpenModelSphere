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

package org.modelsphere.jack.awt.html;

import java.awt.Cursor;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.JTextComponent;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.ExceptionHandler;

public abstract class InternalLinkAdapter implements HyperlinkListener {
    private static final String kObjectNotFound = LocaleMgr.misc.getString("ObjectNotFound");
    private static final String kInvalidDb = LocaleMgr.misc.getString("InvalidDb");
    private static final String kUpdated01 = LocaleMgr.misc.getString("updated01");
    private static final String kAlreadyUpdated01 = LocaleMgr.misc.getString("AlreadyUpdated01");
    private static final String kUpdateTrans0 = LocaleMgr.misc.getString("updateTrans0");
    private static final String kDeleted01 = LocaleMgr.misc.getString("deleted01");
    private static final String kAlreadyDeleted01 = LocaleMgr.misc.getString("AlreadyDeleted01");

    // Default Action for Internal Link - Find in Explorer
    public static final String DBO_FIND = "FIND"; // NOT LOCALIZABLE
    public static final String DBO_SHOW_PROPERTIES = "SHOWPROPERTIES"; // NOT
    // LOCALIZABLE

    private InternalLinkSet links;
    private JTextComponent root;

    public static final int NONE = 0; // Link not supported
    public static final int UPDATED = 1; // The dbo has been updated
    public static final int NOT_UPDATED = 2; // The dbo has already been updated
    public static final int DELETED = 3; // The dbo has been deleted
    public static final int NOT_DELETED = 4; // The dbo has already been deleted

    // Return NONE, UPDATED or NOT_UPDATED
    protected int internalLinkActivated(DbObject dbo, String action) throws DbException {
        return NONE;
    }

    /*
     * pm
     */
    protected int internalLinkActivated(String action) throws DbException {
        return NONE;
    }

    protected void displayMessage(String message) {
    }

    protected abstract JTextComponent getTextComponent();

    protected abstract InternalLinkSet getInternalLinkSet();

    public final void hyperlinkUpdate(HyperlinkEvent e) {
        if (root == null) {
            root = getTextComponent();
            if (root == null)
                return;
            root.setToolTipText(null);
        }
        if (links == null) {
            links = getInternalLinkSet();
            if (links == null)
                return;
        }
        if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
            root.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            InternalLink link = links.getInternalLinkFor(e.getDescription());
            if (link != null && link.imageUrl != null && link.text != null) {
                root.setToolTipText(link.text);
                // displayMessage(link.text);
            }
        } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
            root.setCursor(Cursor.getDefaultCursor());
            root.setToolTipText(null);
            // displayMessage(null);
        } else if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                InternalLink link = links.getInternalLinkFor(e.getDescription());
                DbObject linkDbo = link.getDbObject();
                if (linkDbo != null) {
                    if (!linkDbo.getDb().isValid()) {
                        displayMessage(kInvalidDb);
                        return;
                    }
                    linkDbo.getDb().beginTrans(Db.READ_TRANS);
                    if (!linkDbo.isAlive()) {
                        displayMessage(kObjectNotFound);
                        linkDbo.getDb().commitTrans();
                        return;
                    }
                    linkDbo.getDb().commitTrans();
                }

                if (link == null)
                    return;
                if (link.getAction() == null || link.getAction().equals(DBO_FIND)) {
                    displayMessage(" "); // NOT LOCALIZABLE
                    ApplicationContext.getDefaultMainFrame().findInExplorer(link.getDbObject());
                    return;
                } else if (link.getAction().equals(DBO_SHOW_PROPERTIES)) {
                    displayMessage(" "); // NOT LOCALIZABLE
                    linkDbo.getDb().beginTrans(Db.READ_TRANS);
                    ApplicationContext.getDefaultMainFrame().addPropertyInternalFrame(linkDbo);
                    linkDbo.getDb().commitTrans();
                    return;
                }

                int status;
                Db db;
                if (linkDbo != null) {
                    db = linkDbo.getDb();
                    String guiName = linkDbo.getMetaClass().getGUIName();
                    db.beginTrans(Db.WRITE_TRANS, MessageFormat.format(kUpdateTrans0,
                            new Object[] { guiName }));
                    status = internalLinkActivated(linkDbo, link.getAction());
                } else { // clean-up array of occurrences
                    db = ApplicationContext.getFocusManager().getCurrentProject().getDb();
                    String transactionName = "";
                    db.beginTrans(Db.WRITE_TRANS, transactionName);
                    status = internalLinkActivated(link.getAction());
                } // end if

                if (status == NONE) {
                    db.abortTrans();
                    displayMessage(" "); // NOT LOCALIZABLE
                    return;
                }

                if (linkDbo != null) {
                    // switch (status)
                    if (status == UPDATED)
                        displayMessage(MessageFormat.format(kUpdated01, new Object[] {
                                linkDbo.getMetaClass().getGUIName(), linkDbo.getName() }));
                    else if (status == NOT_UPDATED)
                        displayMessage(MessageFormat.format(kAlreadyUpdated01, new Object[] {
                                linkDbo.getMetaClass().getGUIName(), linkDbo.getName() }));
                    else if (status == DELETED)
                        displayMessage(MessageFormat.format(kDeleted01, new Object[] {
                                linkDbo.getMetaClass().getGUIName(), linkDbo.getName() }));
                    else if (status == NOT_DELETED)
                        displayMessage(MessageFormat.format(kAlreadyDeleted01, new Object[] {
                                linkDbo.getMetaClass().getGUIName(), linkDbo.getName() }));
                }

                db.commitTrans();
            } catch (Exception ex) {
                ExceptionHandler.processUncatchedException(root, ex);
            }
        }
    }
}
