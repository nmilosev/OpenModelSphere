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

package org.modelsphere.sms.style;

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
import org.modelsphere.sms.be.db.DbBEStyle;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOOStyle;
import org.modelsphere.sms.or.db.DbORCommonItemStyle;
import org.modelsphere.sms.or.db.DbORDomainStyle;
import org.modelsphere.sms.or.db.DbORStyle;

@SuppressWarnings("serial")
public class StyleFrame extends JInternalFrame implements DbRefreshListener,
        PropertyChangeListener, VetoableChangeListener, DbDataEntryFrame, ChangeListener {

    private static final String APPLY = LocaleMgr.screen.getString("Apply");
    private static final String CLOSE = LocaleMgr.screen.getString("Close");
    private static final String CANCEL = LocaleMgr.screen.getString("Cancel");
    private static final String DATA = LocaleMgr.misc.getString("Data");
    private static final String DOMAIN = LocaleMgr.misc.getString("Domain");
    private static final String COMMON_ITEM = LocaleMgr.misc.getString("CommonItem");
    //private static final String BE = LocaleMgr.misc.getString("BusinessProcess");

    private static final String STYLES = LocaleMgr.screen.getString("0Styles");
    private static final String WANT_APPLY_CHANGE = LocaleMgr.screen.getString("WantApplyChanges");
    private static final String STYLE_MODIFICATION = LocaleMgr.action
            .getString("styleModification");
    private static final String HELP = org.modelsphere.jack.international.LocaleMgr.screen
            .getString("help");

    private static int _lastSelectedIndex = 0;

    private JButton helpBtn = new JButton(HELP);
    private JButton applyBtn = new JButton(APPLY);
    private JButton closeCancelBtn = new JButton(CLOSE);
    private JButton cancelBtn = new JButton(CANCEL); // just for normalize
    // function
    private JButton[] jButtonList = new JButton[] { applyBtn, closeCancelBtn, helpBtn };
    private JButton[] jPossibleButtonList = new JButton[] { applyBtn, closeCancelBtn, cancelBtn,
            helpBtn };

    private boolean inClose;
    private DbObject style;
    private DbSMSProject project;
    private StyleBankComponent ooStylebankComponent;
    private StyleComponent ooStyleComponent;
    private StyleBankComponent orStylebankComponent;
    private StyleComponent orStyleComponent;
    private StyleBankComponent domainStylebankComponent;
    private StyleComponent domainStyleComponent;
    private StyleBankComponent commonItemStylebankComponent;
    private StyleComponent commonItemStyleComponent;
    private StyleBankComponent beStylebankComponent;
    private StyleComponent beStyleComponent;

    public static String[] getListOptionTabs(Class<?> classStyle, String listOptionTabs) {
        return (String[]) getStyleStaticField(classStyle, listOptionTabs);
    }

    public static MetaField[][] getOptionGroups(Class<?> classStyle, String optionGroupsName) {
        return (MetaField[][]) getStyleStaticField(classStyle, optionGroupsName);
    }

    public static Object[][] getOptionValueGroups(Class<?> classStyle, String optionValueGroupsName) {
        return (Object[][]) getStyleStaticField(classStyle, optionValueGroupsName);
    }

    public static String[] getOptionGroupHeaders(Class<?> classStyle, String optionGroupHeadersName) {
        return (String[]) getStyleStaticField(classStyle, optionGroupHeadersName);
    }

    private static Object getStyleStaticField(Class<?> classStyle, String name) {
        try {
            Field field = classStyle.getDeclaredField(name);
            return field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Access to field DbSMSStyle." + name);
        }
    }

    public static void showStyleFrame(DbSMSProject project) {
        StyleFrame styleFrame = getStyleFrame(project);
        if (styleFrame == null) {
            styleFrame = new StyleFrame();
            ApplicationContext.getDefaultMainFrame().getJDesktopPane().add(styleFrame,
                    DefaultMainFrame.PROPERTY_LAYER);
            styleFrame.setVisible(true);
            try {
                styleFrame.setProject(project);
            } catch (Exception ex) {
                styleFrame.close();
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), ex);
                return;
            }
        }
        try {
            styleFrame.setIcon(false);
            styleFrame.setSelected(true);
        } catch (PropertyVetoException e) {
        }
    }

    private static StyleFrame getStyleFrame(DbSMSProject project) {
        StyleFrame frameFound = null;
        JInternalFrame[] frames = ApplicationContext.getDefaultMainFrame()
                .getDataEntryInternalFrames();
        for (int i = 0; i < frames.length; i++) {
            if (frames[i] instanceof StyleFrame && ((StyleFrame) frames[i]).project == project) {
                frameFound = (StyleFrame) frames[i];
                break;
            }
        }
        return frameFound;
    }

    private StyleFrame() {
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

        // main tabbed pane/
        JTabbedPane intermediateTabbedPane = new JTabbedPane();
        getContentPane().add(intermediateTabbedPane, BorderLayout.CENTER);

        //Class Model section...
        if (ScreenPerspective.isFullVersion()) {
            String[] ooOptionTabs = getListOptionTabs(DbOOStyle.class, "oojv_listOptionTabs"); // NOT LOCALIZABLE
            for (int i = 0; i < ooOptionTabs.length; i = i + 3) {
                String[] optionsName = new String[] { ooOptionTabs[i], ooOptionTabs[i + 1],
                        ooOptionTabs[i + 2] };
                ooStyleComponent = new StyleComponent(this, DbOOStyle.metaClass, optionsName);
                ooStylebankComponent = new StyleBankComponent(this, DbOOStyle.metaClass, optionsName);
                JSplitPane classSP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ooStylebankComponent,
                        ooStyleComponent);
                
                String CLASS = LocaleMgr.misc.getString("Class");
                intermediateTabbedPane.addTab(CLASS, classSP);
            }
        } //end if

        // Data Model section... 
        String[] orOptionTabs = getListOptionTabs(DbORStyle.class, "or_listOptionTabs"); // NOT LOCALIZABLE
        for (int i = 0; i < orOptionTabs.length; i = i + 3) {
            String[] optionsName = new String[] { orOptionTabs[i], orOptionTabs[i + 1],
                    orOptionTabs[i + 2] };
            orStyleComponent = new StyleComponent(this, DbORStyle.metaClass, optionsName);
            orStylebankComponent = new StyleBankComponent(this, DbORStyle.metaClass, optionsName);
            JSplitPane dataSP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, orStylebankComponent,
                    orStyleComponent);
            intermediateTabbedPane.addTab(DATA, dataSP);

        }

        // Domain Model section... 
        String[] domainOptionTabs = getListOptionTabs(DbORDomainStyle.class,
                "domain_listOptionTabs"); // NOT LOCALIZABLE
        for (int i = 0; i < domainOptionTabs.length; i = i + 3) {
            String[] optionsName = new String[] { domainOptionTabs[i], domainOptionTabs[i + 1],
                    domainOptionTabs[i + 2] };
            domainStyleComponent = new StyleComponent(this, DbORDomainStyle.metaClass, optionsName);
            domainStylebankComponent = new StyleBankComponent(this, DbORDomainStyle.metaClass,
                    optionsName);
            JSplitPane dataSP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    domainStylebankComponent, domainStyleComponent);
            intermediateTabbedPane.addTab(DOMAIN, dataSP);

        }

        // CommonItem Model section... 
        String[] commonItemOptionTabs = getListOptionTabs(DbORCommonItemStyle.class,
                "commonItem_listOptionTabs"); // NOT
        // LOCALIZABLE
        for (int i = 0; i < commonItemOptionTabs.length; i = i + 3) {
            String[] optionsName = new String[] { commonItemOptionTabs[i],
                    commonItemOptionTabs[i + 1], commonItemOptionTabs[i + 2] };
            commonItemStyleComponent = new StyleComponent(this, DbORCommonItemStyle.metaClass,
                    optionsName);
            commonItemStylebankComponent = new StyleBankComponent(this,
                    DbORCommonItemStyle.metaClass, optionsName);
            JSplitPane dataSP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    commonItemStylebankComponent, commonItemStyleComponent);
            intermediateTabbedPane.addTab(COMMON_ITEM, dataSP);

        }

        // Business Process Model section... 
        if (ScreenPerspective.isFullVersion()) {
            String[] beOptionTabs = getListOptionTabs(DbBEStyle.class, "be_listOptionTabs"); // NOT LOCALIZABLE
            for (int i = 0; i < beOptionTabs.length; i = i + 3) {
                String[] optionsName = new String[] { beOptionTabs[i], beOptionTabs[i + 1],
                        beOptionTabs[i + 2] };
                beStyleComponent = new StyleComponent(this, DbBEStyle.metaClass, optionsName);
                beStylebankComponent = new StyleBankComponent(this, DbBEStyle.metaClass, optionsName);
                JSplitPane dataSP = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, beStylebankComponent,
                        beStyleComponent);
                
                String BEandUML = LocaleMgr.misc.getString("ProcessAndUML");
                intermediateTabbedPane.addTab(BEandUML, dataSP);
            } //end for
        } //end if

        // Control Button Panel 
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
        
        // Set the default style for ooStylebankComponent instance
        setStyle((DbObject) project.getOoDefaultStyle());
        if (ooStylebankComponent != null) {
        	ooStylebankComponent.setProject(ooStyleComponent, style, project);
        }
        
        // Set the default style for orStylebankComponent instance
        setStyle((DbObject) project.getOrDefaultStyle());
        if (orStylebankComponent != null) {
        	orStylebankComponent.setProject(orStyleComponent, style, project);
        }
        
        // Set the default style for domainStylebankComponent instance
        setStyle((DbObject) project.getOrDefaultDomainStyle());
        if (domainStylebankComponent != null) {
        	domainStylebankComponent.setProject(domainStyleComponent, style, project);
        }
        
        // Set the default style for commonItemStylebankComponent instance
        setStyle((DbObject) project.getOrDefaultCommonItemStyle());
        if (commonItemStylebankComponent != null) {
        	commonItemStylebankComponent.setProject(commonItemStyleComponent, style, project);
        }
        
        // Set the default style for beStylebankComponent instance
        setStyle((DbObject) project.getBeDefaultStyle());
        if (beStylebankComponent != null) {
        	beStylebankComponent.setProject(beStyleComponent, style, project);
        }
        
        project.getDb().commitTrans();
        
        DbObject.fComposite.addDbRefreshListener(ooStylebankComponent, project);
        DbSMSStyle.fName.addDbRefreshListener(ooStylebankComponent, project);
        project.addDbRefreshListener(this, DbRefreshListener.CALL_ONCE);
        inClose = false;
    }

    public final void setStyle(DbObject newStyle) throws DbException {
        if (style == newStyle)
            return;
        if (style != null)
            style.removeDbRefreshListener(this);
        style = newStyle;
        style.addDbRefreshListener(this, DbRefreshListener.CALL_ONCE);
        setApply(false);
    }

    final DbObject getStyle() {
        return style;
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
        setTitle(MessageFormat.format(STYLES, new Object[] { project.getFullDisplayName() }));
    }

    private void applyChanges() {
        try {
            style.getDb().beginTrans(Db.WRITE_TRANS, STYLE_MODIFICATION);
            if (style.getClass() == DbOOStyle.class) {
                ooStyleComponent.applyChanges();
                ooStyleComponent.resetChanges();
            } else if (style.getClass() == DbORStyle.class) {
                orStyleComponent.applyChanges();
                orStyleComponent.resetChanges();
            } else if (style.getClass() == DbORDomainStyle.class) {
                domainStyleComponent.applyChanges();
                domainStyleComponent.resetChanges();
            } else if (style.getClass() == DbORCommonItemStyle.class) {
                commonItemStyleComponent.applyChanges();
                commonItemStyleComponent.resetChanges();
            } else if (style.getClass() == DbBEStyle.class) {
                beStyleComponent.applyChanges();
                beStyleComponent.resetChanges();
            }
            style.getDb().commitTrans();
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
            DbObject.fComposite.removeDbRefreshListener(ooStylebankComponent);
            DbSMSStyle.fName.removeDbRefreshListener(ooStylebankComponent);
            if (style != null)
                style.removeDbRefreshListener(this);
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
        // else { // style change
        // if (style.getTransStatus() != Db.OBJ_REMOVED)
        // styleComponent.setStyle(style, true);
        // }
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
        // if (style.isAlive()) // if not valid, stylebankComponent.refresh()
        // will set the default style.
        // styleComponent.setStyle(style, true);
        // stylebankComponent.refresh();
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
