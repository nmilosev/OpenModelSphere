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

package org.modelsphere.sms;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.actions.ApplicationActionEvent;
import org.modelsphere.jack.actions.ApplicationActionListener;
import org.modelsphere.jack.awt.RunningPanel;
import org.modelsphere.jack.awt.StatusBar;
import org.modelsphere.jack.awt.StatusBarModel;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRAM;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.CurrentProjectEvent;
import org.modelsphere.jack.srtool.CurrentProjectListener;
import org.modelsphere.jack.srtool.reverse.jdbc.ActiveConnectionListener;
import org.modelsphere.jack.srtool.reverse.jdbc.ActiveConnectionManager;
import org.modelsphere.jack.srtool.reverse.jdbc.ConnectionInfoDialog;
import org.modelsphere.jack.srtool.services.ConnectionMessage;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.international.LocaleMgr;

public class MainFrameStatusBarModel implements StatusBarModel, CurrentProjectListener,
        DbRefreshListener {
    private static final String NO_PROJECT_NAME_VALUE = LocaleMgr.message
            .getString("NoActiveProject");
    private static final String NO_PROJECT_MEMORY_VALUE = "";
    private static final String UNKNOWN_MEMORY_VALUE = LocaleMgr.screen.getString("NEW");
    private static final String kPojectLabel = LocaleMgr.misc.getString("Project");
    private static final String kTotalLabel = LocaleMgr.misc.getString("Total");
    private static final String kFreeLabel = LocaleMgr.misc.getString("Free");
    private static final String kStatusBarProjectPathAndName01 = LocaleMgr.misc
            .getString("statusBarProjectPathAndName01");
    private static final String KB = LocaleMgr.misc.getString("KB");

    private static final int PROJECT_NAME_WIDTH = 350;
    private static final int MEMORY_TYPE_WIDTH = 100;

    private static final Icon connectInfoImage = GraphicUtil.loadIcon(
            MainFrameStatusBarModel.class, "or/db/resources/dbordatabase.gif"); // NOT
    // LOCALIZABLE,
    // filename

    private DbProject currentProject;
    private RunningPanel messagePanel = StatusBar.MESSAGE_PANEL;
    private JLabel currentProjectName = new JLabel();
    private JLabel currentProjectMemoryType = new JLabel();
    private JLabel connectInfo = new JLabel(connectInfoImage);

    // Debug only
    private JLabel currentVMTotalMemory = new JLabel();
    private JLabel currentVMFreeMemory = new JLabel();
    private Timer vMUpdater;
    private boolean debugInfoVisible = false;
    private boolean showConnectInfo = false;

    private ActiveConnectionListener connectionlistener = new ActiveConnectionListener() {
        public void activeConnectionChanged(ConnectionMessage cm) {
            refreshConnection(cm, false);
        }
    };

    public MainFrameStatusBarModel() {
        this(false);
    }

    public MainFrameStatusBarModel(boolean debug) {
        ApplicationContext.getFocusManager().addCurrentProjectListener(this);
        // (SMSFilter.RELATIONAL)){
        showConnectInfo = true;
        // }
        try {
            setCurrentProject(null);
        } catch (DbException e) {
        } // will never occur

        // connectInfo.setBorder(null);
        // connectInfo.setFocusable(false);
        // connectInfo.setFocusPainted(false);

        refreshConnection(ActiveConnectionManager.getActiveConnectionMessage(), true);
        ActiveConnectionManager.addActiveConnectionListener(connectionlistener);

        messagePanel.setMessageColor(Color.black);
        messagePanel.setBarForegroundColor(new Color(10, 50, 100));
        currentProjectName.setForeground(Color.black);
        currentProjectMemoryType.setForeground(Color.black);
        messagePanel.setFont(new Font(messagePanel.getFont().getName(), Font.PLAIN, 11));
        currentProjectName
                .setFont(new Font(currentProjectName.getFont().getName(), Font.PLAIN, 11));
        currentProjectMemoryType.setFont(new Font(currentProjectMemoryType.getFont().getName(),
                Font.PLAIN, 11));

        // Debug info fields
        if (org.modelsphere.jack.debug.Debug.isDebug()) {
            debugInfoVisible = debug;
            currentVMTotalMemory.setForeground(Color.black);
            currentVMFreeMemory.setForeground(Color.black);
            currentVMTotalMemory.setFont(new Font(currentVMTotalMemory.getFont().getName(),
                    Font.PLAIN, 11));
            currentVMFreeMemory.setFont(new Font(currentVMFreeMemory.getFont().getName(),
                    Font.PLAIN, 11));
            currentVMTotalMemory.setHorizontalAlignment(SwingConstants.RIGHT);
            currentVMFreeMemory.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        // install some listeners for updating file name
        ApplicationContext.getActionStore().getAction(AbstractActionsStore.PROJECT_SAVE)
                .addApplicationActionListener(new ApplicationActionListener() {
                    public void actionPerformed(ApplicationActionEvent aae) {
                        refresh();
                    }
                });
        ApplicationContext.getActionStore().getAction(AbstractActionsStore.PROJECT_SAVE_AS)
                .addApplicationActionListener(new ApplicationActionListener() {
                    public void actionPerformed(ApplicationActionEvent aae) {
                        refresh();
                    }
                });
    }

    private final void setCurrentProject(DbProject proj) throws DbException {
        if (currentProject != null)
            currentProject.removeDbRefreshListener(this);
        currentProject = proj;
        if (currentProject != null)
            currentProject.addDbRefreshListener(this);
        refreshProjectName();
        refreshMemoryType();
    }

    private final void refreshMemoryType() {
        String memoryType = null;
        if (currentProject == null)
            memoryType = new String(NO_PROJECT_MEMORY_VALUE);
        else {
            memoryType = currentProject.getDb().getDBMSName();
            if (memoryType == null)
                memoryType = new String(UNKNOWN_MEMORY_VALUE);
        }
        currentProjectMemoryType.setText(" " + memoryType);
        currentProjectMemoryType.setToolTipText(" " + memoryType);
    }

    private final void refreshConnection(ConnectionMessage cm, boolean init) {
        // Check for uninstall of listeners - Detect if this status bar model is
        // the active one for the application status bar ... if not,
        // and if not initializing, uninstall listeners
        StatusBar status = ApplicationContext.getDefaultMainFrame().getStatusBar();
        if (!init && status != null && status.getModel() != null && status.getModel() != this) {
            ActiveConnectionManager.removeActiveConnectionListener(connectionlistener);
            return;
        }

        connectInfo.setEnabled(cm != null);
        if (cm == null) {
            connectInfo.setToolTipText(ConnectionInfoDialog.kNotConnected);
        } else {
            String info = ConnectionInfoDialog.toHtml(cm, null, true);
            connectInfo.setToolTipText(info);
        }
    }

    private final void refreshProjectName() throws DbException {
        String projectName = (currentProject == null ? NO_PROJECT_NAME_VALUE : currentProject
                .getName());
        String diplayedName = new String(projectName);
        if (currentProject != null)
            if (currentProject.getDb() instanceof DbRAM) {
                String projectPath = (currentProject == null ? null : currentProject
                        .getRamFileName());
                if (projectPath != null) {
                    FontMetrics fm = currentProjectName
                            .getFontMetrics(currentProjectName.getFont());
                    int maxCharWidth = fm.getMaxAdvance();
                    int maxWidth = PROJECT_NAME_WIDTH - (maxCharWidth * 1);
                    String newProjectPath = new String(projectPath);
                    if (SwingUtilities.computeStringWidth(fm, projectName) < maxWidth) {
                        int projectPathCharCount = projectPath.length();
                        boolean fit = false;
                        while (!fit) {
                            diplayedName = MessageFormat.format(kStatusBarProjectPathAndName01,
                                    new Object[] { newProjectPath, projectName });
                            int width = SwingUtilities.computeStringWidth(fm, diplayedName);
                            if (width <= maxWidth) {
                                fit = true;
                            } else {
                                projectPathCharCount = projectPathCharCount
                                        - Math.max(((width - maxWidth) / maxCharWidth), 1);
                                if (projectPathCharCount < 1) {
                                    projectPathCharCount = 1;
                                    fit = true;
                                }
                                newProjectPath = StringUtil.truncateFileName(projectPath,
                                        projectPathCharCount);
                            }
                        }
                    }
                }
            }
        currentProjectName.setText(" " + diplayedName);
        currentProjectName.setToolTipText(" " + diplayedName);
    }

    // used to call an update on displayed project if RAMFileName as been
    // updated
    private void refresh() {
        if (currentProject != null) {
            try {
                currentProject.getDb().beginTrans(Db.READ_TRANS);
                refreshProjectName();
                currentProject.getDb().commitTrans();
            } catch (Exception ex) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(MainFrame
                        .getSingleton(), ex);
            }
        }
    }

    public final void updateMemoryInfo(boolean autoupdate) {
        if (org.modelsphere.jack.debug.Debug.isDebug()) {
            if (!autoupdate) {
                stopAutoUpdateMemoryInfo();
                currentVMTotalMemory.setText(new Long(Runtime.getRuntime().totalMemory())
                        .toString());
                currentVMFreeMemory.setText(new Long(Runtime.getRuntime().freeMemory()).toString());
            } else {
                vMUpdater = new Timer(2000, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        currentVMTotalMemory.setText(new Long(
                                Runtime.getRuntime().totalMemory() / 1024).toString()
                                + " " + KB);
                        currentVMFreeMemory.setText(new Long(
                                Runtime.getRuntime().freeMemory() / 1024).toString()
                                + " " + KB);
                    }
                });
                vMUpdater.start();
            }
        }
    }

    private void stopAutoUpdateMemoryInfo() {
        if (vMUpdater != null && vMUpdater.isRunning()) {
            vMUpdater.stop();
            vMUpdater = null;
        }
    }

    // //////////////////////////////////////////
    // StatusBarModel Support
    //

    public final int getComponentCount() {
        if (debugInfoVisible)
            return 5 + (showConnectInfo ? 1 : 0);
        return 3 + (showConnectInfo ? 1 : 0);
    }

    public final JComponent getComponentAt(int col) {
        if (!showConnectInfo) {
            switch (col) {
            case 0:
                return messagePanel;
            case 1:
                return currentProjectName;
            case 2:
                return currentProjectMemoryType;
            case 3:
                return currentVMTotalMemory;
            case 4:
                return currentVMFreeMemory;
            default:
                return new JLabel("");
            }
        } else {
            switch (col) {
            case 0:
                return messagePanel;
            case 1:
                return currentProjectName;
            case 2:
                return currentProjectMemoryType;
            case 3:
                return connectInfo;
            case 4:
                return currentVMTotalMemory;
            case 5:
                return currentVMFreeMemory;
            default:
                return new JLabel("");
            }
        }
    }

    public final int getWidthAt(int col) {
        if (!showConnectInfo) {
            switch (col) {
            case 0:
                return StatusBar.RELATIVE_WIDTH;
            case 1:
                return PROJECT_NAME_WIDTH;
            case 2:
                return MEMORY_TYPE_WIDTH;
            case 3:
                return 70;
            case 4:
                return 60;
            default:
                return 1;
            }
        } else {
            switch (col) {
            case 0:
                return StatusBar.RELATIVE_WIDTH;
            case 1:
                return PROJECT_NAME_WIDTH;
            case 2:
                return MEMORY_TYPE_WIDTH;
            case 3:
                return connectInfo.getPreferredSize().width + 2;
            case 4:
                return 70;
            case 5:
                return 60;
            default:
                return 1;
            }
        }
    }

    public final JComponent getTitleForComponentAt(int col) {
        if (!showConnectInfo) {
            switch (col) {
            case 0:
                return null;
            case 1:
                return new JLabel(kPojectLabel);
            case 2:
                return null;
            case 3:
                return new JLabel(kTotalLabel);
            case 4:
                return new JLabel(kFreeLabel);
            default:
                return null;
            }
        } else {
            switch (col) {
            case 0:
                return null;
            case 1:
                return new JLabel(kPojectLabel);
            case 2:
                return null;
            case 3:
                return null;
            case 4:
                return new JLabel(kTotalLabel);
            case 5:
                return new JLabel(kFreeLabel);
            default:
                return null;
            }
        }
    }

    public final void startWaitingBar(String message) {
        messagePanel.start(message);
    }

    public final void startWaitingBar(String message, long timeBeforeStarting) {
        messagePanel.start(message, timeBeforeStarting);
    }

    public final void stopWaitingBar(String message) {
        messagePanel.stop(message);
    }

    public final void setMessage(String message) {
        messagePanel.setMessage(message);
        messagePanel.setToolTipText(message);
    }

    //
    // End of StatusBarModel Support
    // //////////////////////////////////////////

    // //////////////////////////////////////////
    // CurrentProjectListener Support
    //
    public final void currentProjectChanged(CurrentProjectEvent cpe) throws DbException {
        setCurrentProject(cpe.getProject());
    }

    //
    // End of CurrentProjectListener Support
    // //////////////////////////////////////////

    // //////////////////////////////////////////
    // DbRefreshListener Support
    //

    public final void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (event.metaField == DbSemanticalObject.fName) {
            refreshProjectName();
        }
    }

    //
    // End of DbRefreshListener Support
    // //////////////////////////////////////////

}
