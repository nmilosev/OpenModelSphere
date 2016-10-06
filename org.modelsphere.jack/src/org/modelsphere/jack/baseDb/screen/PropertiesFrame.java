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
package org.modelsphere.jack.baseDb.screen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.*;
import java.beans.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.event.DbListener;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.international.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.CascadingJInternalFrame;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.ExceptionHandler;

public class PropertiesFrame extends CascadingJInternalFrame implements Printable, Pageable,
        PropertyChangeListener, VetoableChangeListener, LocaleChangeListener, DbListener,
        DbDataEntryFrame, DbRefreshListener {
    // Print information
    private PageFormat pageFormat;
    private double zoomFactor = .80;
    Dimension nbPages = null;
    Dimension pageSize;

    private static final String POINTCASCADENAME = "PROPERTIES_FRAME"; // NOT LOCALIZABLE, property key
    public static final String kPropertyPrint = LocaleMgr.screen.getString("PropertyPrint");

    public static final int TYPE_PROPERTY = 1;
    public static final int TYPE_UDF = 2;

    protected ScreenContext screenContext;
    protected DbProject project;
    protected DbObject semObj;
    protected int type = TYPE_PROPERTY;

    private String titlePattern;
    private boolean hasChanged = false;
    private boolean inClose = false;
    private JButton applyBtn = new JButton(LocaleMgr.screen.getString("Apply"));
    private JButton closeCancelBtn = new JButton();
    // context-sensitive help
    private JButton helpBtn = new JButton(LocaleMgr.screen.getString("help"));
    // end of context-sensitive help
    private JButton[] jButtonList = new JButton[] { applyBtn, closeCancelBtn, helpBtn };
    private JComponent viewPanel = null; // a JTabbedPane if multiple
    // screenViews, otherwise the
    // screenView itself
    private JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    // For calculate the absolute preferred size of all possible buttons
    private JButton cancelBtn = new JButton(LocaleMgr.screen.getString("Cancel"));
    private JButton closeBtn = new JButton(LocaleMgr.screen.getString("Close"));
    private JButton[] jPossibleButtonList = new JButton[] { applyBtn, cancelBtn, closeBtn, helpBtn };

    private PropertiesFrameHeader header = null;

    public PropertiesFrame(ScreenContext screenContext, DbObject semObj, ScreenTabPanel[] panels)
            throws DbException {
        this(screenContext, semObj, panels, LocaleMgr.screen.getString("{0}Properties"));
    }

    public PropertiesFrame(ScreenContext screenContext, DbObject semObj, ScreenTabPanel[] panels,
            String titlePattern) throws DbException {
        super("", POINTCASCADENAME, true);
        this.screenContext = screenContext;
        this.semObj = semObj;
        project = semObj.getProject();
        this.titlePattern = titlePattern;
        setResizable(true);
        setIconifiable(true);
        setMaximizable(true);
        setClosable(true);
        createScreenPanels(panels);
        controlPanel = createControlPanel();
        AwtUtil.normalizeComponentDimension(jButtonList, jPossibleButtonList);
        adjustControlPanel();
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addPropertyChangeListener(this);
        addVetoableChangeListener(this);

        header = createPropertiesFrameHeader(semObj, semObj.getComposite());
        if (header != null)
            header.refresh();

        JPanel borderPanel = new JPanel(new BorderLayout());
        borderPanel.add(viewPanel, BorderLayout.CENTER);
        // A line appear between the two sets of buttons for better look when
        // there is only one view
        // borderPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.gray));

        JPanel borderPanel2 = new JPanel(new BorderLayout());
        borderPanel2.setBorder(BorderFactory.createEmptyBorder(1, 6, 0, 6));
        borderPanel2.add(borderPanel, BorderLayout.CENTER);

        if (header != null)
            getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(borderPanel2, BorderLayout.CENTER);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
        refreshTitle();

        Icon icon = null;
        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        if (terminologyUtil.isDataModel(semObj)) {
            int nMode = terminologyUtil.getModelLogicalMode(semObj);
            if (nMode == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                icon = terminologyUtil.getConceptualModelIcon();
        } else {
            DbObject dbObject = terminologyUtil.isCompositeDataModel(semObj);
            if (dbObject != null) {
                int nMode = terminologyUtil.getModelLogicalMode(dbObject);
                if (nMode == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                    if (terminologyUtil.isObjectEntityOrAssociation(semObj)) {
                        if (terminologyUtil.isObjectAssociation(semObj))
                            icon = terminologyUtil.getAssociationIcon();
                    } else if (terminologyUtil.isObjectArc(semObj))
                        icon = terminologyUtil.getArcIcon();
                }
            }
        }

        if (icon == null) {
            if (terminologyUtil.isObjectUseCase(semObj))
                icon = terminologyUtil.getUseCaseIcon(semObj);
            else {
                Terminology term = terminologyUtil.findModelTerminology(semObj);
                icon = term.getIcon(semObj.getMetaClass());
            }
        }
        if (icon != null)
            setFrameIcon(icon);
        semObj.addDbRefreshListener(this);
        LocaleChangeManager.getLocaleChangeManager().addLocaleChangeListener(this);
        Db.addDbListener(this);
        // initPrinter();
    }

    private void refreshTitle() throws DbException {
        String name = semObj.getSemanticalName(DbObject.LONG_FORM);
        setTitle(MessageFormat.format(titlePattern, new Object[] { name }));
    }

    protected Icon getSemanticalIcon(DbObject semObject) throws DbException {
        Icon icon = semObject.getSemanticalIcon(DbObject.SHORT_FORM);
        return icon;
    }

    // Set a tab panel only if more than one view.
    private void createScreenPanels(ScreenTabPanel[] panels) {
        panels[0].activateTab();
        if (panels.length == 1)
            viewPanel = (JComponent) panels[0];
        else {
            viewPanel = new JTabbedPane();
            ((JTabbedPane) viewPanel).addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    screenContext.selectionChanged(PropertiesFrame.this);
                    ScreenTabPanel panel = (ScreenTabPanel) ((JTabbedPane) viewPanel)
                            .getSelectedComponent();
                    panel.activateTab();
                }
            });
            for (int i = 0; i < panels.length; i++)
                ((JTabbedPane) viewPanel).addTab(panels[i].getTabName(), (JComponent) panels[i]);
        }

        for (int i = 0; i < panels.length; i++) {
            if (panels[i] instanceof ScreenView)
                ((ScreenView) panels[i]).setPropertiesFrame(this);
        }
    }

    protected PropertiesFrameHeader createPropertiesFrameHeader(DbObject dbo, DbObject parentDbo) {
        return new PropertiesFrameHeader(dbo, parentDbo);
    }

    private void adjustControlPanel() {
        applyBtn.setEnabled(hasChanged);
        closeCancelBtn.setText(hasChanged ? LocaleMgr.screen.getString("Cancel") : LocaleMgr.screen
                .getString("Close"));
        closeCancelBtn.getParent().validate();
    }

    public final void setHasChanged() {
        if (!hasChanged) {
            hasChanged = true;
            adjustControlPanel();
        }
    }

    public final void commit() {
        try {
            String pattern = LocaleMgr.screen.getString("{0}Commit");
            semObj.getDb().beginTrans(Db.WRITE_TRANS,
                    MessageFormat.format(pattern, new Object[] { getTitle() }));
            if (viewPanel instanceof JTabbedPane) {
                JTabbedPane tabPanel = (JTabbedPane) viewPanel;
                int i, nb;
                for (i = 0, nb = tabPanel.getComponentCount(); i < nb; i++) {
                    Component comp = tabPanel.getComponentAt(i);
                    if (comp instanceof ScreenView)
                        ((ScreenView) comp).commit();
                }
            } else {
                ((ScreenView) viewPanel).commit();
            }
            semObj.getDb().commitTrans();
            resetHasChanged();
        } catch (Exception e) {
            if (e instanceof DbMaxConnectivityException) {
                DbMaxConnectivityException max = (DbMaxConnectivityException) e;
                String message = MessageFormat.format(LocaleMgr.message
                        .getString("maxConnectivity"), new Object[] { max.conflictObjClassName,
                        max.conflictObjName, max.oppositeObjName });
                JOptionPane.showMessageDialog(this, message, ApplicationContext.getApplicationName(),
                        JOptionPane.ERROR_MESSAGE);
            } else
                ExceptionHandler.processUncatchedException(this, e);
        }
    }

    private void resetHasChanged() {
        hasChanged = false;
        if (viewPanel instanceof JTabbedPane) {
            JTabbedPane tabPanel = (JTabbedPane) viewPanel;
            int i, nb;
            for (i = 0, nb = tabPanel.getComponentCount(); i < nb; i++) {
                Component comp = tabPanel.getComponentAt(i);
                if (comp instanceof ScreenView)
                    ((ScreenView) comp).resetHasChanged();
            }
        } else {
            ((ScreenView) viewPanel).resetHasChanged();
        }
        adjustControlPanel();
    }

    public final DbObject getSemanticalObject() {
        return semObj;
    }

    public final int getType() {
        return type;
    }

    public final void setType(int type) {
        this.type = type;
    }

    public final ScreenTabPanel getCurrentPanel() {
        return (viewPanel instanceof JTabbedPane ? (ScreenTabPanel) ((JTabbedPane) viewPanel)
                .getSelectedComponent() : (ScreenTabPanel) viewPanel);
    }

    // ///////////////////////////////////////
    // DbDataEntryFrame SUPPORT
    //
    public final DbProject getProject() {
        return project;
    }

    public final DbObject[] getSelection() {
        ScreenTabPanel panel = getCurrentPanel();
        if (panel instanceof ScreenView)
            return ((ScreenView) panel).getSelection();
        return new DbObject[0];
    }

    /**
     * ask the propetyFrame to close it self, return true if request is accepted
     * 
     * @param onThisAction
     *            an action name that will be dsiplayed to the user if the propertyframe contains
     *            modifications
     */
    public final boolean requestClose(String onThisAction) {
        if (acceptClose(onThisAction)) {
            close();
            return true;
        }
        return false;
    }

    private boolean acceptClose(String onThisAction) {
        boolean closeAccepted = true;
        if (hasChanged) {
            try {
                setIcon(false);
                moveToFront();
                setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
            }
            String msg;
            if (onThisAction == null)
                msg = LocaleMgr.message.getString("ApplyChanges?");
            else {
                String pattern = LocaleMgr.message.getString("ApplyChangesBefore{0}?");
                msg = MessageFormat.format(pattern, new Object[] { onThisAction });
            }
            int userChoice = JOptionPane.showConfirmDialog(this, msg, getTitle(),
                    JOptionPane.YES_NO_CANCEL_OPTION);
            switch (userChoice) {
            case JOptionPane.OK_OPTION:
                commit();
                break;
            case JOptionPane.NO_OPTION:
                break;
            default:
                closeAccepted = false;
                break;
            }
        }
        return closeAccepted;
    }

    public final void refresh() throws DbException {
        if (!semObj.isAlive()) {
            close();
            return;
        }
        refreshTitle();
        if (viewPanel instanceof JTabbedPane) {
            JTabbedPane tabPanel = (JTabbedPane) viewPanel;
            for (int i = 0; i < tabPanel.getComponentCount(); i++) {
                ScreenTabPanel panel = (ScreenTabPanel) tabPanel.getComponentAt(i);
                panel.refresh();
            }
        } else {
            ((ScreenTabPanel) viewPanel).refresh();
        }
    }

    public final void stopEditing() {
        if (viewPanel instanceof JTabbedPane) {
            JTabbedPane tabPanel = (JTabbedPane) viewPanel;
            for (int i = 0; i < tabPanel.getComponentCount(); i++) {
                Component comp = tabPanel.getComponentAt(i);
                if (comp instanceof ScreenView)
                    ((ScreenView) comp).stopEditing();
            }
        } else {
            ((ScreenView) viewPanel).stopEditing();
        }
    }

    //
    // End of DbDataEntryFrame SUPPORT
    // ///////////////////////////////////////

    /**
     * To close the window, you must call this method (do not call dispose).
     */
    public final void close() {
        inClose = true;
        Db.removeDbListener(this);
        try {
            setClosed(true);
        } catch (PropertyVetoException e) {
        } // should not happen
    }

    // inClose is false if close triggered by clicking on the close box.
    public final void vetoableChange(PropertyChangeEvent ev) throws PropertyVetoException {
        if (ev.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY)
                && ((Boolean) ev.getNewValue()).booleanValue()) {
            if (inClose)
                return;
            stopEditing();
            if (!acceptClose(null))
                throw new PropertyVetoException("", ev);
        }
    }

    public final void propertyChange(PropertyChangeEvent ev) {
        if (ev.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY)
                && ((Boolean) ev.getNewValue()).booleanValue()) {
            deinstallPanel();
        }
    }

    // overriden
    public void deinstallPanel() {
        deinstallAllPanels();
        LocaleChangeManager.getLocaleChangeManager().removeLocaleChangeListener(this);
        semObj.removeDbRefreshListener(this);
    }

    private void deinstallAllPanels() {
        if (viewPanel instanceof JTabbedPane) {
            JTabbedPane tabPanel = (JTabbedPane) viewPanel;
            for (int i = 0; i < tabPanel.getComponentCount(); i++) {
                ScreenTabPanel panel = (ScreenTabPanel) tabPanel.getComponentAt(i);
                panel.deinstallPanel();
            }
        } else {
            ((ScreenTabPanel) viewPanel).deinstallPanel();
        }
    }

    private JPanel createControlPanel() {
        jbInit();
        return controlPanel;
    }

    void jbInit() {
        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        // Graphic Design Utility. Keep it into comments when modification are
        // done.

        /*
         * helpBtn.setText("Help"); //NOT LOCALIZABLE applyBtn.setText("Apply"); //NOT LOCALIZABLE
         * closeCancelBtn.setText("Cancel"); //NOT LOCALIZABLE findButton.setMnemonic('F');
         */// NOT LOCALIZABLE
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // context
        helpBtn.setEnabled(screenContext.isHelpInstalled());
        helpBtn.setMnemonic(LocaleMgr.screen.getMnemonic("help"));
        helpBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                screenContext.showHelpFrame(semObj);
            }
        });
        // context
        applyBtn.setMnemonic(LocaleMgr.screen.getMnemonic("Apply"));
        applyBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                commit();
            }
        });
        closeCancelBtn.setMnemonic(LocaleMgr.screen.getMnemonic("Close"));
        closeCancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        controlPanel.add(applyBtn);
        controlPanel.add(closeCancelBtn);
        // HIDEHELPforV1//controlPanel.add(helpBtn);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(6, 7, 6, 1)); // x
        // +
        // 5
        // (default
        // gap)
        // =
        // standard
        // controlPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.gray),
        // BorderFactory.createEmptyBorder(12,7,6,6)));
        new HotKeysSupport(this, closeCancelBtn, helpBtn);
    }

    // //////////////////////////////
    // DbRefreshListener SUPPORT
    //
    public void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
        if (semObj.getTransStatus() == Db.OBJ_REMOVED)
            close();
        else if (e.metaField == DbSemanticalObject.fName) {
            refreshTitle();
            if (header != null)
                header.refresh();
        }
    }

    //
    // End of DbRefreshListener SUPPORT
    // ///////////////////////////////

    // db has been created.
    public void dbCreated(Db db) {
    }

    // db has been terminated. This is not possible to use this db.
    public void dbTerminated(Db db) {
        if (semObj.getDb() == db) {
            close();
        }
    }

    // //////////////////////////////
    // LocaleChangeListener SUPPORT
    //
    public void localeChanged(LocaleChangeEvent e) {
        adjustControlPanel();
        applyBtn.setText(LocaleMgr.screen.getString("Apply"));
        if (viewPanel instanceof JTabbedPane) {
            JTabbedPane tabPanel = (JTabbedPane) viewPanel;
            int count = tabPanel.getComponentCount();
            for (int i = 0; i < count; i++) {
                Component comp = tabPanel.getComponentAt(i);
                if (comp instanceof LocaleChangeListener)
                    ((LocaleChangeListener) comp).localeChanged(e);
            }
        } else if (viewPanel instanceof LocaleChangeListener) {
            ((LocaleChangeListener) viewPanel).localeChanged(e);
        }
        this.pack();
    }

    //
    // End of LocaleChangeListener SUPPORT
    // ///////////////////////////////

    public final void doPrint() {
        /*
         * PrinterJob printerJob = PrinterJob.getPrinterJob();
         * printerJob.setJobName(kPropertyPrint); printerJob.setPageable(this); if
         * (printerJob.printDialog()){ try { printerJob.print(); } catch (PrinterException ex){
         * Debug.trace(ex.getMessage()); } }
         */
    }

    private void initPrinter() {
        // on devrait fournir le page setup
        pageFormat = getPageFormat(0);
        pageFormat.setOrientation(PageFormat.LANDSCAPE);

        pageSize = getPageSize(pageFormat, (int) (zoomFactor * 100));
        /*
         * calcule du nombres de pages. verifie si la division est complete, si il y a un reste
         * ajouter une page.
         */
        nbPages = new Dimension(getWidth() / pageSize.width
                + (getWidth() % pageSize.width == 0 ? 0 : 1), getHeight() / pageSize.height
                + (getHeight() % pageSize.height == 0 ? 0 : 1));
    }

    private Dimension getNbPages() {
        if (nbPages == null) {
            nbPages = new Dimension(getWidth() / pageSize.width
                    + (getWidth() % pageSize.width == 0 ? 0 : 1), getHeight() / pageSize.height
                    + (getHeight() % pageSize.height == 0 ? 0 : 1));
        }
        return nbPages;
    }

    private Dimension getPageSize(PageFormat pageFormat, int printScale) {
        return new Dimension((int) (pageFormat.getImageableWidth() * 100.0 / printScale),
                (int) (pageFormat.getImageableHeight() * 100.0 / printScale));
    }

    // Printable
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
            throws PrinterException {

        pageFormat.setOrientation(PageFormat.LANDSCAPE);
        int x = getPageSize().width * (pageIndex % getNbPages().width);
        int y = getPageSize().height * (pageIndex / getNbPages().width);

        graphics.translate((int) (pageFormat.getImageableX() - x * zoomFactor), (int) (pageFormat
                .getImageableY() - y * zoomFactor));
        ((Graphics2D) graphics).scale(zoomFactor, zoomFactor);

        Rectangle rect = this.getBounds();
        Stroke oldStroke = ((Graphics2D) graphics).getStroke();
        Graphics2D g2D = (Graphics2D) graphics.create(rect.x, rect.y, rect.width, rect.height);
        paintAll(g2D);
        g2D.setStroke(oldStroke);
        return PAGE_EXISTS;
    }

    // Pageable
    public int getNumberOfPages() {
        return nbPages.width * nbPages.height;
    }

    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        return (pageFormat == null ? PrinterJob.getPrinterJob().defaultPage() : pageFormat);
    }

    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        return this;
    }

    public Dimension getPageSize() {
        return pageSize;
    }
}
