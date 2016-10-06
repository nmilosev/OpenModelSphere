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

package org.modelsphere.jack.baseDb.assistant;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.net.URL;
import java.util.*;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.international.LocaleMgr;

public abstract class JHtmlBallonHelp extends JInternalFrame implements MouseListener,
        ListSelectionListener {
    private static final String kShowHelpExplorer = LocaleMgr.screen.getString("ShowHelpExplorer");
    private static final String kHideHelpExplorer = LocaleMgr.screen.getString("HideHelpExplorer");

    protected Vector indexList;
    protected Vector indexPages;
    protected Vector categoryList;
    protected Vector bookList;
    protected Vector htmlPagesTocList;
    protected Vector vec;
    protected Vector searchVector;
    protected Vector titles;

    protected JButton backButton = null;
    protected JButton exitButton = null;
    protected JButton refreshButton = null;
    protected JButton forwardButton = null;
    protected JButton printButton = null;
    protected JButton showHideButton = null;
    // protected JButton findButton = null;

    protected JEditorPane baloonHelpPane;
    protected JScrollPane baloonHelpView;
    protected JScrollPane listScrollPane;
    protected JScrollPane findListScrollPane;

    protected JList list;
    protected JList findList;
    protected JTabbedPane tabbedPane = new JTabbedPane();
    protected JTextField indexField;
    protected JPanel indexPane;
    protected JPanel findPane;
    // protected JTextField findTextField;
    protected JTextField searchTextField;

    protected String keyWord = "";
    protected String findTextFieldEntry = "";

    protected URL helpURL;
    protected URL startURL;
    protected URL link;
    protected URL[] historyUrl = new URL[10];
    protected URL currentURL;

    protected int i = 0;
    protected int numberOfEntries = 0;
    protected int[] pos = new int[400];
    protected int select = 0;
    protected int x = 0;
    protected int j = 0;

    protected String helpRootDirectory;

    public JHtmlBallonHelp(String helpRootDirectory) {
        super(LocaleMgr.screen.getString("help"), true, true, true, true); // resizable,
        // closable,
        // maximizable,
        // iconifiable
        this.helpRootDirectory = helpRootDirectory;
        getIndexPages();
        getIndexEntries();

        // //////Table Of Contents
        // Create the nodes.
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(getRootNodeName());
        createNodes(top);

        // Create a tree that allows one selection at a time.
        JTree tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer
                .setClosedIcon(new ImageIcon(LocaleMgr.class.getResource("resources/booknode.gif")));
        renderer.setOpenIcon(new ImageIcon(LocaleMgr.class.getResource("resources/opennode.gif")));
        renderer.setLeafIcon(new ImageIcon(LocaleMgr.class.getResource("resources/leafnode.gif")));
        tree.setCellRenderer(renderer);

        // Listen for when the selection changes.
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) (e.getPath()
                        .getLastPathComponent());
                Object nodeInfo = node.getUserObject();
                if (node.isLeaf()) {
                    BookInfo book = (BookInfo) nodeInfo;
                    displayURL(book.bookURL, true);
                } else {
                    displayURL(helpURL, false);
                }
            }
        });

        // Create the scroll pane and add the tree to it.
        JScrollPane treeView = new JScrollPane(tree);

        // //end of TOC

        // ///INDEX PANE START
        // /Index TextField
        indexField = new JTextField(20);
        indexField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                synchIndexFieldList();
            }
        });

        // List
        list = new JList(indexList);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(this);
        listScrollPane = new JScrollPane(list);
        listScrollPane.setMinimumSize(new Dimension(100, 50));

        // Index Panel
        indexPane = new JPanel(new BorderLayout());
        indexPane.add(indexField, BorderLayout.NORTH);
        indexPane.add(listScrollPane, BorderLayout.CENTER);
        // /////INDEX PANE STOP

        // Find Pane
        // ///Find TextField
        // findTextField = new JTextField();
        // findTextField.setMaximumSize (new Dimension(100, 20));
        // findTextField.setAlignmentY (-45);
        // findTextField.setToolTipText("Find in Document"); // NOT LOCALIZABLE

        // /Search TextField
        searchTextField = new JTextField();
        searchTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JSearch search = new JSearch(searchTextField.getText(),
                        JHtmlBallonHelp.this.helpRootDirectory);
                searchVector = search.getSearchResultVector();

                titles = search.getTitle();
                findList.setListData(titles);
                Debug.trace(titles);
            }
        });

        // find result list
        findList = new JList();
        findList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        findList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {

                if (e.getValueIsAdjusting())
                    return;
                JList theList = (JList) e.getSource();
                if (theList.isSelectionEmpty()) {
                    helpURL = getHtmlFile("welcome.html"); // NOT LOCALIZABLE -
                    // link
                } else {
                    int index = theList.getSelectedIndex();
                    helpURL = ((URL) searchVector.elementAt(index));
                    displayURL(helpURL, true);
                }
            }
        });
        findListScrollPane = new JScrollPane(findList);
        findListScrollPane.setMinimumSize(new Dimension(100, 50));

        // find panel
        findPane = new JPanel(new BorderLayout());
        findPane.add(findListScrollPane, BorderLayout.CENTER);
        findPane.add(searchTextField, BorderLayout.NORTH);
        // Find Pane

        // /CONSTRUCTING HELP FRAME
        // THE Toolbar
        JToolBar toolBar = new JToolBar();
        addButtons(toolBar);
        // toolBar.add (findTextField);
        toolBar.setFloatable(false);

        // THE Tab Pane
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        tabbedPane.addTab(LocaleMgr.screen.getString("TOC"), treeView);
        tabbedPane.addTab(LocaleMgr.screen.getString("Index"), indexPane);
        tabbedPane.addTab(LocaleMgr.screen.getString("Find"), findPane);
        tabbedPane.setSelectedIndex(0);

        // THE HTML Viewer
        baloonHelpPane = new JEditorPane();
        baloonHelpPane.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        baloonHelpPane.setEditable(false);
        baloonHelpPane.addHyperlinkListener(new HyperlinkListener() {
            public final void hyperlinkUpdate(HyperlinkEvent e) {
                HyperlinkEvent.EventType type = e.getEventType();
                if (type == HyperlinkEvent.EventType.ENTERED)
                    baloonHelpPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                else if (type == HyperlinkEvent.EventType.EXITED)
                    baloonHelpPane.setCursor(Cursor.getDefaultCursor());
                else if (type == HyperlinkEvent.EventType.ACTIVATED) {
                    link = e.getURL();
                    displayURL(link, true);
                }
            }
        });
        baloonHelpView = new JScrollPane(baloonHelpPane);
        baloonHelpView.setPreferredSize(new Dimension(400, 500));
        initBaloonHelp();

        Container contentPane = getContentPane();
        contentPane.add(toolBar, BorderLayout.NORTH);
        contentPane.add(tabbedPane, BorderLayout.WEST);
        contentPane.add(baloonHelpView, BorderLayout.CENTER);
        pack();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    /**
     * To close the window, you must call this method (do not call dispose).
     */
    public final void close() {
        try {
            setClosed(true);
        } catch (PropertyVetoException e) {
        } // should not happen
    }

    // /CONSTRUCTING HELP FRAME END

    private URL getHtmlFile(String fileName) {
        URL helpURL;
        String pattern;
        if (fileName.endsWith(".html")) // NOT LOCALIZABLE
            pattern = "File:{0}/{1}"; // NOT LOCALIZABLE
        else
            pattern = "File:{0}/{1}.html"; // NOT LOCALIZABLE
        String file = java.text.MessageFormat.format(pattern, new Object[] { helpRootDirectory,
                fileName });
        try {
            helpURL = new URL(file);
        } catch (java.net.MalformedURLException e) {
            org.modelsphere.jack.debug.Debug.trace("getHtmlFile bad URL:" + file); // NOT LOCALIZABLE
            helpURL = null;
        }
        return helpURL;
    }

    // public URL getResource(String resName){
    // return classForGetResource.getResource(resName);
    // }

    // /INDEX PROPERTIES
    public void synchIndexFieldList() {
        int j = 0;
        j = numberOfEntries / 2;
        String s = indexField.getText();

        for (int h = 0; h < j; h++) {
            String indexEntry = ((String) indexList.elementAt(h));
            String indexURL = ((String) indexPages.elementAt(h));
            if (indexEntry.equals(s)) {
                list.setSelectedIndex(h);
                helpURL = getHtmlFile(indexURL);
                displayURL(helpURL, false);
                break;
            }
        }
    }

    public void valueChanged(ListSelectionEvent e) {

        if (e.getValueIsAdjusting())
            return;
        JList theList = (JList) e.getSource();
        if (theList.isSelectionEmpty()) {
            helpURL = getHtmlFile("welcome"); // NOT LOCALIZABLE
        } else {
            int index = theList.getSelectedIndex();
            String page = ((String) indexPages.elementAt(index));
            helpURL = getHtmlFile(page);
            displayURL(helpURL, true);
        }
    }

    protected Vector parseList(String indexList) {
        Vector v = new Vector(10);
        StringTokenizer tokenizer = new StringTokenizer(indexList, ","); // NOT
        // LOCALIZABLE
        while (tokenizer.hasMoreTokens()) {
            String index = tokenizer.nextToken();
            v.addElement(index);
            numberOfEntries++;
        }
        return v;
    }

    public void getIndexEntries() {
        // getting the list for the index
        ResourceBundle indexResource;
        try {
            indexResource = ResourceBundle.getBundle(getHelpPackageName() + "index"); // NOT LOCALIZABLE
            String indexEntry = indexResource.getString("index"); // NOT
            // LOCALIZABLE
            indexList = parseList(indexEntry);
        } catch (MissingResourceException e) {
            Debug.trace("Unable to parse properties file.");
            return;
        }
    }

    public void getIndexPages() {
        // getting the corresponding html page for the selected index
        ResourceBundle indexPagesResource;
        try {
            indexPagesResource = ResourceBundle.getBundle(getHelpPackageName() + "html"); // NOT LOCALIZABLE
            String indexPage = indexPagesResource.getString("html"); // NOT
            // LOCALIZABLE
            indexPages = parseList(indexPage);
        } catch (MissingResourceException e) {
            Debug.trace("Unable to parse properties file.");
            return;
        }
    }

    // ///INDEX PROPERTIES END

    protected final void addButtons(JToolBar toolBar) {

        // Exit button
        exitButton = new JButton(new ImageIcon(LocaleMgr.class
                .getResource("resources/exitover.gif")));
        exitButton.setPressedIcon(new ImageIcon(LocaleMgr.class
                .getResource("resources/exitover.gif")));
        exitButton.setRolloverEnabled(true);
        exitButton.setRolloverIcon(new ImageIcon(LocaleMgr.class
                .getResource("resources/exitover.gif")));
        exitButton.setToolTipText(LocaleMgr.screen.getString("ExitHelp"));
        exitButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        exitButton.addMouseListener(this);
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        toolBar.add(exitButton);

        // Back button
        backButton = new JButton(new ImageIcon(LocaleMgr.class
                .getResource("resources/backover.gif")));
        backButton.setPressedIcon(new ImageIcon(LocaleMgr.class
                .getResource("resources/backover.gif")));
        backButton.setRolloverEnabled(true);
        backButton.setRolloverIcon(new ImageIcon(LocaleMgr.class
                .getResource("resources/backover.gif")));
        backButton.setToolTipText(LocaleMgr.screen.getString("Back"));
        backButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setEnabled(false);
        backButton.addMouseListener(this);
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
        toolBar.add(backButton);

        // forward button
        forwardButton = new JButton(new ImageIcon(LocaleMgr.class
                .getResource("resources/forwardover.gif")));
        forwardButton.setPressedIcon(new ImageIcon(LocaleMgr.class
                .getResource("resources/forwardover.gif")));
        forwardButton.setRolloverEnabled(true);
        forwardButton.setRolloverIcon(new ImageIcon(LocaleMgr.class
                .getResource("resources/forwardover.gif")));
        forwardButton.setToolTipText(LocaleMgr.screen.getString("Forward"));
        forwardButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        forwardButton.setBorderPainted(false);
        forwardButton.setFocusPainted(false);
        forwardButton.setEnabled(false);
        forwardButton.addMouseListener(this);
        forwardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goNext();
            }
        });
        toolBar.add(forwardButton);

        // refresh button
        refreshButton = new JButton(new ImageIcon(LocaleMgr.class
                .getResource("resources/refresh.gif")));
        refreshButton.setPressedIcon(new ImageIcon(LocaleMgr.class
                .getResource("resources/refresh.gif")));
        refreshButton.setRolloverEnabled(true);
        refreshButton.setRolloverIcon(new ImageIcon(LocaleMgr.class
                .getResource("resources/refresh.gif")));
        refreshButton.setToolTipText(LocaleMgr.screen.getString("ReloadCurrentDoc"));
        refreshButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.addMouseListener(this);
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reload();
            }
        });
        toolBar.add(refreshButton);

        // Print button
        printButton = new JButton(new ImageIcon(LocaleMgr.class.getResource("resources/print.gif")));
        printButton
                .setPressedIcon(new ImageIcon(LocaleMgr.class.getResource("resources/print.gif")));
        printButton.setRolloverEnabled(true);
        printButton.setRolloverIcon(new ImageIcon(LocaleMgr.class
                .getResource("resources/print.gif")));
        printButton.setToolTipText(LocaleMgr.screen.getString("PrintCurrentDocument"));
        printButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        printButton.setBorderPainted(false);
        printButton.setFocusPainted(false);
        printButton.addMouseListener(this);
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                print();
            }
        });
        toolBar.add(printButton);

        // Show button
        showHideButton = new JButton(new ImageIcon(LocaleMgr.class
                .getResource("resources/hide.gif")));
        showHideButton.setPressedIcon(new ImageIcon(LocaleMgr.class
                .getResource("resources/hide.gif")));
        showHideButton.setRolloverEnabled(true);
        showHideButton.setRolloverIcon(new ImageIcon(LocaleMgr.class
                .getResource("resources/hide.gif")));
        showHideButton.setToolTipText(kHideHelpExplorer);
        showHideButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        showHideButton.setBorderPainted(false);
        showHideButton.setFocusPainted(false);
        showHideButton.addMouseListener(this);
        showHideButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tabbedPane.isVisible()) {
                    showHideToc(true);
                } else {
                    showHideToc(false);
                }
            }
        });
        toolBar.add(showHideButton);
        /*
         * toolBar.addSeparator(new Dimension(65,0));
         * 
         * //Find button findButton = new JButton(new
         * ImageIcon(Application.class.getResource("resources/find.gif"))); // NOT LOCALIZABLE
         * findButton.setPressedIcon(new
         * ImageIcon(Application.class.getResource("resources/find.gif"))); // NOT LOCALIZABLE
         * findButton.setRolloverEnabled(true); findButton.setRolloverIcon(new
         * ImageIcon(Application.class.getResource("resources/find.gif"))); // NOT LOCALIZABLE
         * findButton.setToolTipText("Show Next Occurence"); // NOT LOCALIZABLE
         * findButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
         * findButton.setBorderPainted(false); findButton.setFocusPainted(false);
         * findButton.addMouseListener(this); findButton.addActionListener(new
         * java.awt.event.ActionListener() { public void actionPerformed(ActionEvent e) {
         * selectOccurence(); } }); toolBar.add(findButton);
         */
    }

    // //////////////////////////////////////////////
    // MouseListener Support
    //

    public void mouseClicked(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
            JButton button = ((JButton) e.getSource());
            if (!button.isEnabled()) {
                button.setBorderPainted(false);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
            JButton button = ((JButton) e.getSource());
            if (button.isEnabled()) {
                button.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
            JButton button = ((JButton) e.getSource());
            button.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        }
    }

    public void mouseEntered(MouseEvent e) {
        JButton button = ((JButton) e.getSource());
        if (button.isEnabled()) {
            button.setBorderPainted(true);
        }
    }

    public void mouseExited(MouseEvent e) {
        JButton button = ((JButton) e.getSource());
        button.setBorderPainted(false);
    }

    // //////////////////////////////////////////////
    // MouseListener Support END
    //

    // ////Navigation Methods

    // contructing url array for Back and Forward buttons
    protected final void addStack(URL u) {
        i++;
        historyUrl[i] = u;
        backButton.setEnabled(true);
        if (i == 9) {
            for (int j = 0; j <= 8; j++) {
                historyUrl[j] = historyUrl[j + 1];
            }
            i--;
        }
    }

    // reload
    protected final void reload() {
        helpURL = getHtmlFile("empty"); // NOT LOCALIZABLE
        URL reloadURL = currentURL;
        displayURL(helpURL, false);
        displayURL(reloadURL, false);
    }

    // print
    protected final void print() {
        PrintJob job = Toolkit.getDefaultToolkit().getPrintJob(
                (Frame) SwingUtilities.getAncestorOfClass(Frame.class, this),
                LocaleMgr.misc.getString("Help"), null);
        if (job != null) {
            Graphics pg = job.getGraphics();
            pg.setFont(new Font("arial", Font.PLAIN, 10)); // NOT LOCALIZABLE
            baloonHelpPane.print(pg);
            pg.dispose();
            job.end();
        }
    }

    // forward
    public final void goNext() {

        try {
            i++;
            displayURL(historyUrl[i], false);
            backButton.setEnabled(true);
            if (historyUrl[i + 1] == null) {
                forwardButton.setEnabled(false);
            }
            if (i == 8) {
                forwardButton.setEnabled(false);
            }
        } catch (ArrayIndexOutOfBoundsException o) {
            i = i - 1;
        }

    }

    // back
    public final void goBack() {

        try {
            i--;
            displayURL(historyUrl[i], false);
            forwardButton.setEnabled(true);
            if (i == 0) {
                backButton.setEnabled(false);
            } else {
            }
        } catch (ArrayIndexOutOfBoundsException o) {
            i = i + 1;
        }
    }

    // Showing or Hiding the TabPane (for the show/hide button)
    public final void showHideToc(boolean visible) {
        if (visible == true) {
            tabbedPane.setVisible(false);
            showHideButton
                    .setIcon(new ImageIcon(LocaleMgr.class.getResource("resources/show.gif")));
            showHideButton.setPressedIcon(new ImageIcon(LocaleMgr.class
                    .getResource("resources/show.gif")));
            showHideButton.setRolloverIcon(new ImageIcon(LocaleMgr.class
                    .getResource("resources/show.gif")));
            showHideButton.setToolTipText(kShowHelpExplorer);
        }
        if (visible == false) {
            tabbedPane.setVisible(true);
            showHideButton
                    .setIcon(new ImageIcon(LocaleMgr.class.getResource("resources/hide.gif")));
            showHideButton.setPressedIcon(new ImageIcon(LocaleMgr.class
                    .getResource("resources/hide.gif")));
            showHideButton.setRolloverIcon(new ImageIcon(LocaleMgr.class
                    .getResource("resources/hide.gif")));
            showHideButton.setToolTipText(kHideHelpExplorer);
        }
    }

    // ////Navigation Methods END

    // //TOC PROPERTIES
    protected Vector parseToc(String tocList) {
        Vector v = new Vector(10);
        StringTokenizer tokenizer = new StringTokenizer(tocList, ","); // NOT
        // LOCALIZABLE
        while (tokenizer.hasMoreTokens()) {
            String index = tokenizer.nextToken();
            v.addElement(index);
        }
        return v;
    }

    // populating the TOC via properties files
    private void createNodes(DefaultMutableTreeNode top) {

        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;

        ResourceBundle TocCategoryResource;
        ResourceBundle TocBookResource;
        ResourceBundle TocHtmlPagesResource;

        // properties for the nodes
        TocCategoryResource = ResourceBundle.getBundle(getHelpPackageName() + "category"); // NOT LOCALIZABLE

        // properties for the entries for each nodes
        TocBookResource = ResourceBundle.getBundle(getHelpPackageName() + "book"); // NOT LOCALIZABLE

        // properties for the html pages corresponding to each node entries
        TocHtmlPagesResource = ResourceBundle.getBundle(getHelpPackageName() + "htmlPagesToc"); // NOT LOCALIZABLE

        for (int c = 0; c < 30; c++)
            try {
                String bookNode = ("book" + c); // NOT LOCALIZABLE
                String bookCategory = ("category" + c); // NOT LOCALIZABLE
                String bookHtmlPage = ("html" + c); // NOT LOCALIZABLE

                String tocCategory = TocCategoryResource.getString(bookCategory);
                String tocBook = TocBookResource.getString(bookNode);
                String tocHtmlPage = TocHtmlPagesResource.getString(bookHtmlPage);

                categoryList = parseToc(tocCategory);

                // adding nodes
                category = new DefaultMutableTreeNode(tocCategory);
                top.add(category);

                // adding entries for each nodes
                bookList = parseToc(tocBook);
                htmlPagesTocList = parseToc(tocHtmlPage);

                for (int b = 0; b < bookList.size(); b++) {
                    String bookEntry = ((String) bookList.elementAt(b));
                    String bookHtml = ((String) htmlPagesTocList.elementAt(b));
                    book = new DefaultMutableTreeNode(
                            new BookInfo(bookEntry, getHtmlFile(bookHtml)));

                    category.add(book);
                }
                if (bookCategory == null)
                    break;
            } catch (MissingResourceException e) {
                break;
            }
    }

    // Find methods

    // Parsing the EditorPane
    protected Vector parseText(String input) {
        Vector v = new Vector(10);
        StringTokenizer tokenizer = new StringTokenizer(input, " "); // NOT
        // LOCALIZABLE
        while (tokenizer.hasMoreTokens()) {
            String index = tokenizer.nextToken();
            v.addElement(index);
        }
        return v;
    }

    // Returns the position of the first letter of
    // the word specified in the FindTextField.
    public final void getPosition(String entry) {
        try {
            Document doc = baloonHelpPane.getDocument();
            String docString = doc.getText(0, doc.getLength());
            vec = parseText(docString);
            for (int v = 0; v < vec.size(); v++) {
                String pageEntry = ((String) vec.elementAt(v));
                if (pageEntry.equalsIgnoreCase(entry)) {
                    pos[j] = docString.indexOf(pageEntry, select + entry.length());
                    j++;
                    select = pos[j - 1];
                }
            }
            j = 0;
        } catch (BadLocationException b) {
        }
    }

    /*
     * //Select the occurence from the word //specified in the FindTextField protected final void
     * selectOccurence(){ findTextFieldEntry = findTextField.getText (); if
     * (keyWord.equals(findTextFieldEntry)){ baloonHelpPane.select (pos[x], pos[x] +
     * findTextFieldEntry.length()); x++; } else{ keyWord = findTextFieldEntry; for (int a=0;
     * a<pos.length; a++) pos[a]=0; getPosition(keyWord); baloonHelpPane.select (pos[0], pos[0] +
     * findTextFieldEntry.length()); x=1; } if (pos[x]==0){ x=0; } }
     */

    private void initBaloonHelp() {
        try {
            helpURL = getHtmlFile("welcome"); // NOT LOCALIZABLE
            baloonHelpPane.setPage(helpURL);
            historyUrl[i] = helpURL;
            currentURL = helpURL;
        } catch (Exception e) {
        }
    }

    public final void display(Object helpObjectKey) {
        displayURL(getHtmlFile(getURLForObjectKey(helpObjectKey)), true);
    }

    private void displayURL(URL nUrl, boolean stack) {
        try {
            baloonHelpPane.setPage(nUrl);
            currentURL = nUrl;
            if (stack)
                addStack(nUrl);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, LocaleMgr.message.getString("helpnotfound"));
        }
    }

    // //////////////////////////////////////
    // Abstract Methods
    //
    public abstract String getHelpPackageName();

    public abstract String getRootNodeName();

    public abstract String getURLForObjectKey(Object helpObjectKey);
    //
    // End of Abstract Method
    // //////////////////////////////////////
}
