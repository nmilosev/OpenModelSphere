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

package org.modelsphere.sms.notation;

//Java
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Field;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.screen.DbDataEntryFrame;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.db.DbSMSNotation;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORNotation;

@SuppressWarnings("serial")
public class NotationFrame extends JInternalFrame implements DbRefreshListener,
        PropertyChangeListener, VetoableChangeListener, DbDataEntryFrame, ChangeListener {

    private static final String APPLY = LocaleMgr.screen.getString("Apply");
    private static final String CLOSE = LocaleMgr.screen.getString("Close");
    private static final String CANCEL = LocaleMgr.screen.getString("Cancel");
    private static final String NOTATIONS = LocaleMgr.screen.getString("Notations");
    private static final String WANT_APPLY_CHANGE = LocaleMgr.screen.getString("WantApplyChanges");
    private static final String NOTATION_MODIFICATION = LocaleMgr.action
            .getString("updateNotation");
    private static final String HELP = org.modelsphere.jack.international.LocaleMgr.screen
            .getString("help");

    private static final String DATA_MODEL = LocaleMgr.misc.getString("Data");
    private static final String BUSINESS_PROCESS_MODEL = LocaleMgr.misc
            .getString("BusinessProcess");
    private static final String UML_MODEL = "UML"; // NOT LOCALIZABLE

    private static int _lastSelectedIndex = 0;

    private JButton helpBtn = new JButton(HELP);
    private JButton applyBtn = new JButton(APPLY);
    private JButton closeCancelBtn = new JButton(CLOSE);
    private JButton cancelBtn = new JButton(CANCEL); // just for normalize
    // function
    private JButton[] jButtonList = new JButton[] { applyBtn, closeCancelBtn, helpBtn };
    private JButton[] jPossibleButtonList = new JButton[] { applyBtn, closeCancelBtn, cancelBtn,
            helpBtn };

    private NotationBankComponent orNotationbankComponent;
    private NotationComponent orNotationComponent;

    private NotationBankComponent beNotationbankComponent;
    private NotationComponent beNotationComponent;

    private NotationBankComponent umlNotationbankComponent;
    private NotationComponent umlNotationComponent;

    private boolean inClose;
    private DbSMSNotation notation;
    private DbSMSProject project;

    public static void showNotationFrame(DbSMSProject project) {
        NotationFrame notationFrame = getNotationFrame(project);
        if (notationFrame == null) {
            notationFrame = new NotationFrame();
            ApplicationContext.getDefaultMainFrame().getJDesktopPane().add(notationFrame,
                    DefaultMainFrame.PROPERTY_LAYER);
            notationFrame.setVisible(true);
            try {
                notationFrame.setProject(project);
            } catch (Exception ex) {
                notationFrame.close();
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), ex);
                return;
            }
        }
        try {
            notationFrame.setIcon(false);
            notationFrame.setSelected(true);
        } catch (PropertyVetoException e) {
        }
    }

    public static String[] getListOptionTabs(Class<?> classNotation, String listOptionTabs) {
        return (String[]) getNotationStaticField(classNotation, listOptionTabs);
    }

    public static MetaField[][] getOptionGroups(Class<?> classNotation, String optionGroupsName) {
        return (MetaField[][]) getNotationStaticField(classNotation, optionGroupsName);
    }

    public static Object[][] getOptionValueGroups(Class<?> classNotation,
            String optionValueGroupsName) {
        return (Object[][]) getNotationStaticField(classNotation, optionValueGroupsName);
    }

    public static String[] getOptionGroupHeaders(Class<?> classNotation,
            String optionGroupHeadersName) {
        return (String[]) getNotationStaticField(classNotation, optionGroupHeadersName);
    }

    public static String[] getOptionGroupComponents(Class<?> classNotation,
            String optionGroupComponentsName) {
        return (String[]) getNotationStaticField(classNotation, optionGroupComponentsName);
    }

    private static Object getNotationStaticField(Class<?> classNotation, String name) {
        try {
            Field field = classNotation.getDeclaredField(name);
            return field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Cannot access " + classNotation.getName() + "." + name);
        }
    }

    private static NotationFrame getNotationFrame(DbSMSProject project) {
        NotationFrame frameFound = null;
        JInternalFrame[] frames = ApplicationContext.getDefaultMainFrame()
                .getDataEntryInternalFrames();
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] instanceof NotationFrame
                    && ((NotationFrame) frames[i]).project == project) {
                frameFound = (NotationFrame) frames[i];
                break;
            }
        }
        return frameFound;
    }

    private NotationFrame() {
        super("", true, true, true, true); // resizable, closable, maximizable,
        // iconifiable
        jbInit();
        // set default icon
        ImageIcon icon = new ImageIcon(ApplicationContext.APPLICATION_IMAGE_ICON);
        setFrameIcon(icon);
    }

    private void jbInit() {
        setSize(ApplicationContext.getDefaultMainFrame().getDefaultInternalFrameSize());

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addPropertyChangeListener(this);
        addVetoableChangeListener(this);

        // main tabbed pane 
        JTabbedPane intermediateTabbedPane = new JTabbedPane();
        getContentPane().add(intermediateTabbedPane, BorderLayout.CENTER);

        // data Model section... 
        String[] orOptionTabs = getListOptionTabs(DbORNotation.class, "listOptionTabs"); // NOT LOCALIZABLE
        for (int i = 0; i < orOptionTabs.length; i = i + 4) {
            String[] optionsName = new String[] { orOptionTabs[i], orOptionTabs[i + 1],
                    orOptionTabs[i + 2], orOptionTabs[i + 3] };
            orNotationComponent = new NotationComponent(this, DbORNotation.metaClass,
                    DbORNotation.fBuiltIn, optionsName);
            orNotationbankComponent = new NotationBankComponent(this, DbORNotation.metaClass,
                    optionsName, false);
            JSplitPane dataSP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    orNotationbankComponent, orNotationComponent);
            intermediateTabbedPane.addTab(DATA_MODEL, dataSP);
        }

        // Process model section... 
        if (ScreenPerspective.isFullVersion()) {
            String[] beOptionTabs = getListOptionTabs(DbBENotation.class, "listOptionTabs"); // NOT LOCALIZABLE
            for (int i = 0; i < beOptionTabs.length; i = i + 4) {
                String[] optionsName = new String[] { beOptionTabs[i], beOptionTabs[i + 1],
                        beOptionTabs[i + 2], beOptionTabs[i + 3] };
                beNotationComponent = new NotationComponent(this, DbBENotation.metaClass,
                        DbBENotation.fBuiltIn, optionsName);
                beNotationbankComponent = new NotationBankComponent(this, DbBENotation.metaClass,
                        optionsName, false);
                JSplitPane dataSP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                        beNotationbankComponent, beNotationComponent);
                intermediateTabbedPane.addTab(BUSINESS_PROCESS_MODEL, dataSP);
            }

            // UML model section... 
            for (int i = 0; i < beOptionTabs.length; i = i + 4) {
                String[] optionsName = new String[] { beOptionTabs[i], beOptionTabs[i + 1],
                        beOptionTabs[i + 2], beOptionTabs[i + 3] };
                umlNotationComponent = new NotationComponent(this, DbBENotation.metaClass,
                        DbBENotation.fBuiltIn, optionsName);
                umlNotationbankComponent = new NotationBankComponent(this, DbBENotation.metaClass,
                        optionsName, true);
                JSplitPane dataSP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                        umlNotationbankComponent, umlNotationComponent);
                intermediateTabbedPane.addTab(UML_MODEL, dataSP);
            }
        } //end if

        /** Control Button Panel */
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(12, 6, 6, 6));
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
        btnPanel.add(applyBtn);
        btnPanel.add(closeCancelBtn);
        // HIDEHELPforV1//btnPanel.add(helpBtn);

        applyBtn.setEnabled(false);
        applyBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyChanges();
            }
        });

        closeCancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        helpBtn.setEnabled(ApplicationContext.getDefaultMainFrame().isHelpInstalled());
        helpBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                helpBtn_actionPerformed(e);
            }
        });

        AwtUtil.normalizeComponentDimension(jButtonList, jPossibleButtonList);

        intermediateTabbedPane.setSelectedIndex(_lastSelectedIndex);
        intermediateTabbedPane.addChangeListener(this);
    }

    private void setProject(DbSMSProject project) throws DbException {
        this.project = project;
        project.getDb().beginTrans(Db.READ_TRANS);
        refreshTitle();
        setNotation((DbSMSNotation) project.getOrDefaultNotation());
        
        if (orNotationbankComponent != null) {
        	orNotationbankComponent.setProject(orNotationComponent, notation, project);
        }
        
        if (beNotationbankComponent != null) {
        	beNotationbankComponent.setProject(beNotationComponent, null, project);
        }
        
        if (umlNotationbankComponent != null) {
        	umlNotationbankComponent.setProject(umlNotationComponent, null, project);
        }
        
        project.getDb().commitTrans();
        DbObject.fComposite.addDbRefreshListener(orNotationbankComponent, project);
        DbObject.fComposite.addDbRefreshListener(beNotationbankComponent, project);
        DbObject.fComposite.addDbRefreshListener(umlNotationbankComponent, project);
        DbSMSNotation.fName.addDbRefreshListener(orNotationbankComponent, project);
        DbSMSNotation.fName.addDbRefreshListener(beNotationbankComponent, project);
        DbSMSNotation.fName.addDbRefreshListener(umlNotationbankComponent, project);
        project.addDbRefreshListener(this, DbRefreshListener.CALL_ONCE);
        inClose = false;
    }

    public final void setNotation(DbSMSNotation newNotation) throws DbException {
        if (notation == newNotation)
            return;
        if (notation != null)
            notation.removeDbRefreshListener(this);
        notation = newNotation;
        notation.addDbRefreshListener(this, DbRefreshListener.CALL_ONCE);
        setApply(false);
    }

    final DbSMSNotation getNotation() {
        return notation;
    }

    public final void setApply(boolean state) {
        applyBtn.setEnabled(state);
        closeCancelBtn.setText(state ? CANCEL : CLOSE);
        closeCancelBtn.getParent().validate();
    }

    final boolean confirmApply(boolean cancel) {
        if (applyBtn.isEnabled()) {
            try {
                setIcon(false);
                moveToFront();
                setSelected(true);
            } catch (PropertyVetoException e) {
            }
            int rc = JOptionPane.showConfirmDialog(this, WANT_APPLY_CHANGE, getTitle(),
                    (cancel ? JOptionPane.YES_NO_CANCEL_OPTION : JOptionPane.YES_NO_OPTION));
            if (rc == JOptionPane.YES_OPTION)
                applyChanges();
            return (rc != JOptionPane.CANCEL_OPTION);
        }
        return true;
    }

    private void refreshTitle() throws DbException {
        setTitle(MessageFormat.format(NOTATIONS, new Object[] { project.getFullDisplayName() }));
    }

    private void applyChanges() {
        try {
            project.getDb().beginTrans(Db.WRITE_TRANS, NOTATION_MODIFICATION);
            orNotationComponent.applyChanges();
            orNotationComponent.resetChanges();
            beNotationComponent.applyChanges();
            beNotationComponent.resetChanges();
            umlNotationComponent.applyChanges();
            umlNotationComponent.resetChanges();
            project.getDb().commitTrans();
            setApply(false);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    // context
    private void helpBtn_actionPerformed(ActionEvent e) {
        ApplicationContext.getDefaultMainFrame().displayJavaHelp("preference"); // NOT
        // LOCALIZABLE
    }

    // context

    public final void vetoableChange(PropertyChangeEvent ev) throws PropertyVetoException {
        if (ev.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY)
                && ((Boolean) ev.getNewValue()).booleanValue()) {
            if (inClose)
                return;
            if (!confirmApply(true))
                throw new PropertyVetoException("", ev);
        }
    }

    public final void propertyChange(PropertyChangeEvent ev) {
        if (ev.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY)
                && ((Boolean) ev.getNewValue()).booleanValue()) {
            DbObject.fComposite.removeDbRefreshListener(orNotationbankComponent);
            DbSMSNotation.fName.removeDbRefreshListener(orNotationbankComponent);
            DbObject.fComposite.removeDbRefreshListener(beNotationbankComponent);
            DbSMSNotation.fName.removeDbRefreshListener(beNotationbankComponent);
            DbObject.fComposite.removeDbRefreshListener(umlNotationbankComponent);
            DbSMSNotation.fName.removeDbRefreshListener(umlNotationbankComponent);
            if (notation != null)
                notation.removeDbRefreshListener(this);
            if (project != null)
                project.removeDbRefreshListener(this);
        }
    }

    public final void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (event.dbo == project) {
            if (project.getTransStatus() == Db.OBJ_REMOVED)
                close();
            else if (project.hasChanged(DbSemanticalObject.fName))
                refreshTitle();
        }
    }

    // ///////////////////////////////////////
    // DbDataEntryFrame SUPPORT
    //
    public final DbProject getProject() {
        return project;
    }

    public final DbObject[] getSelection() {
        return new DbObject[0];
    }

    public final boolean requestClose(String onThisAction) {
        if (!confirmApply(true))
            return false;
        close();
        return true;
    }

    public final void refresh() throws DbException {
        if (!project.isAlive()) {
            close();
            return;
        }
        refreshTitle();
    }

    //
    // End of DbDataEntryFrame SUPPORT
    // ///////////////////////////////////////

    /**
     * To close the window, you must call this method (do not call dispose).
     */
    public final void close() {
        inClose = true;
        try {
            setClosed(true);
        } catch (PropertyVetoException e) {
        } // should not happen
    }

    public void updateUI() {
        super.updateUI();
        /** Background */
        getContentPane().setBackground(UIManager.getColor("Panel.background"));
    }

    @Override
    public void stateChanged(ChangeEvent ev) {
        Object src = ev.getSource();
        if (src instanceof JTabbedPane) {
            JTabbedPane pane = (JTabbedPane) src;
            int idx = pane.getSelectedIndex();
            _lastSelectedIndex = idx;
        }
    } //end stateChanged()

}
