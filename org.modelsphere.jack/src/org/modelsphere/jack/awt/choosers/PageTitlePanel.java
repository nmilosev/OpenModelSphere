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

package org.modelsphere.jack.awt.choosers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.MessageFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class PageTitlePanel extends JPanel implements ActionListener, DocumentListener {
    private static final String CURRENT_PAGE = LocaleMgr.screen.getString("CurrentPage");
    private static final String CURRENT_ROW = LocaleMgr.screen.getString("CurrentRow");
    private static final String CURRENT_COLUMN = LocaleMgr.screen.getString("CurrentColumn");
    private static final String CURRENT_TIME = LocaleMgr.screen.getString("CurrentTime");
    private static final String CURRENT_DATE = LocaleMgr.screen.getString("CurrentDate");
    private static final String TOTAL_PAGES = LocaleMgr.screen.getString("TotalPages");
    private static final String TOTAL_ROWS = LocaleMgr.screen.getString("TotalRows");
    private static final String TOTAL_COLUMNS = LocaleMgr.screen.getString("TotalColumns");

    private static final String DIAGRAM_NAME = LocaleMgr.screen.getString("DiagramName");
    private static final String PROJECT_NAME = LocaleMgr.screen.getString("ProjectName");
    private static final String FORMAT = LocaleMgr.screen.getString("Format");
    private static final String PREVIEW = LocaleMgr.screen.getString("Preview");
    private static final String DISPLAY = LocaleMgr.screen.getString("displayAsNoun") + "..";
    private static final String DIAGRAM_NAME_DF = LocaleMgr.screen.getString("DiagramName");
    private static final String PROJECT_NAME_DF = LocaleMgr.screen.getString("ProjectName");
    private static final String LEGEND_TITLE = LocaleMgr.screen.getString("legendTitle");
    private static final String ERROR_FORMAT = LocaleMgr.screen.getString("errorFormat");

    private JLabel formatLabel = new JLabel();
    private JTextField formatTextField = new JTextField();
    private JTextArea legendTextArea0_4 = new JTextArea();
    private JLabel previewLabel = new JLabel();
    private JTextField previewTextField = new JTextField();
    private JButton jButton1 = new JButton();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JTextArea legendTextArea5_9 = new JTextArea();
    private OptionPanel m_optionPanel;
    private JPanel legendPanel;

    public PageTitlePanel(TitleOptions titleOptions, OptionPanel optionPanel) {
        try {
            jbInit();
            init(titleOptions);
            m_optionPanel = optionPanel;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Implements ActionListener
    public void actionPerformed(ActionEvent ev) {
        Object src = ev.getSource();

        if (src.equals(formatTextField)) {
            String pattern = formatTextField.getText();
            String preview = getPageTitle(pattern);
            previewTextField.setText(preview);

            if (m_optionPanel != null) {
                m_optionPanel.fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                        ApplicationDiagram.class, ApplicationDiagram.PAGE_TITLE, pattern);
            }
        } //end if
    } // end actionPerformed

    //Implements DocumentListener
    public void insertUpdate(DocumentEvent e) {
        Object src = e.getDocument();
        if (src.equals(formatTextField.getDocument()))
            processFormatTextField();
    }

    public void removeUpdate(DocumentEvent e) {
        Object src = e.getDocument();
        if (src.equals(formatTextField.getDocument()))
            processFormatTextField();
    }

    private void processFormatTextField() {
        if (formatTextField.getText().length() == 0)
            return;
        String pattern = formatTextField.getText();
        String preview = getPageTitle(pattern);
        previewTextField.setText(preview);
        if (m_optionPanel != null) {
            m_optionPanel.fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                    ApplicationDiagram.class, ApplicationDiagram.PAGE_TITLE, pattern);
        }
    }

    public void changedUpdate(DocumentEvent e) {
    }

    public String getPageTitle(String pattern) {
        String title = getPageTitle(pattern, 0, 1, new Dimension(10, 10), DIAGRAM_NAME_DF,
                PROJECT_NAME_DF);
        return title;
    }

    public static String getPageTitle(String pattern, int row, int col, Dimension nbPages,
            String diagramName, String projectName) {
        String title = null;
        int currentPage = row * nbPages.width + col + 1;
        int totalPages = nbPages.width * nbPages.height;

        //get current time & date
        long currentTime = System.currentTimeMillis();
        Date d = new Date(currentTime);
        DateFormat timeFormat = DateFormat.getTimeInstance();
        String time = timeFormat.format(d);
        DateFormat dateFormat = DateFormat.getDateInstance();
        String date = dateFormat.format(d);

        //build the objects
        Object[] objects = new Object[] { new Integer(currentPage), new Integer(row + 1),
                new Integer(col + 1), time, date, new Integer(totalPages),
                new Integer(nbPages.height), //total rows
                new Integer(nbPages.width), //total columns
                diagramName, projectName };
        try {
            title = MessageFormat.format(pattern, objects);
        } catch (IllegalArgumentException ex) {
            title = ERROR_FORMAT;
        }
        return title;
    }

    //
    // private methods
    // 
    private void jbInit() throws Exception {
        this.setLayout(gridBagLayout1);
        formatLabel.setToolTipText("");
        formatLabel.setText(FORMAT);
        previewLabel.setText(PREVIEW);
        previewTextField.setBackground(Color.lightGray);
        previewTextField.setText("");
        jButton1.setText(DISPLAY);
        legendPanel = new JPanel(new GridBagLayout());
        legendPanel.setBorder(new TitledBorder(" " + LEGEND_TITLE + " "));

        this.add(formatLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(18, 9, 11, 5), 0,
                0));
        this.add(formatTextField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(18, 6, 11,
                        11), 0, 0));

        this.add(previewLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(6, 9, 11, 5), 0,
                0));
        this.add(previewTextField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(6, 6, 11,
                        11), 0, 0));

        this.add(legendPanel, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(6, 6, 5, 9), 0, 0));
        legendPanel.add(legendTextArea0_4, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 5,
                        11), 0, 0));
        legendPanel.add(legendTextArea5_9, new GridBagConstraints(2, 0, 2, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 5, 11), 0, 0));

        //init legend
        initLegend(legendTextArea0_4, legendTextArea5_9);
        formatTextField.addActionListener(this);
        formatTextField.getDocument().addDocumentListener(this);
    }

    private void init(TitleOptions titleOptions) {
        String patn = titleOptions.m_patn;
        formatTextField.setText(patn);

        //init preview 
        String text = formatTextField.getText();
        text = getPageTitle(text);
        previewTextField.setText(text);
        previewTextField.setEditable(false);
    } //end init()

    private void initLegend(JTextArea ta1, JTextArea ta2) {

        String text = "{0} " + CURRENT_PAGE + "\n" + //NOT LOCALIZABLE, formatting
                "{1} " + CURRENT_ROW + "\n" + //NOT LOCALIZABLE, formatting
                "{2} " + CURRENT_COLUMN + "\n" + //NOT LOCALIZABLE, formatting
                "{3} " + CURRENT_TIME + "\n" + //NOT LOCALIZABLE, formatting
                "{4} " + CURRENT_DATE; //NOT LOCALIZABLE, formatting
        ta1.setText(text);

        text = "{5} " + TOTAL_PAGES + "\n" + //NOT LOCALIZABLE, formatting
                "{6} " + TOTAL_ROWS + "\n" + //NOT LOCALIZABLE, formatting
                "{7} " + TOTAL_COLUMNS + "\n" + //NOT LOCALIZABLE, formatting
                "{8} " + DIAGRAM_NAME + "\n" + //NOT LOCALIZABLE, formatting
                "{9} " + PROJECT_NAME; //NOT LOCALIZABLE, formatting

        ta2.setText(text);

        Color bgColor = this.getBackground();
        Font font = this.getFont();

        ta1.setBackground(bgColor);
        ta2.setBackground(bgColor);

        ta1.setFont(font);
        ta2.setFont(font);

        ta1.setEditable(false);
        ta2.setEditable(false);
    } //end printLegend()

    //
    // Inner class
    //
    public static class TitleOptions {
        private String m_patn;

        public TitleOptions(String patn) {
            m_patn = patn;
        }
    } //end TitleOptions()

    //
    // main()
    //
    public static void main(String[] args) {
        JFrame frame = new JFrame("title"); //NOT LOCALIZABLE, unit test
        JPanel panel = new PageTitlePanel(null, null);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);

    } //end main()
} //end PageTitlePanel() 
