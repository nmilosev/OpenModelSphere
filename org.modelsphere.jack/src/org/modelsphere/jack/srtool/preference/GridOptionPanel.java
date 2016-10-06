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

package org.modelsphere.jack.srtool.preference;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modelsphere.jack.awt.ComponentTestable;
import org.modelsphere.jack.awt.ThinBevelBorder;
import org.modelsphere.jack.graphic.Grid;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.srtool.international.LocaleMgr;

@SuppressWarnings("serial")
public class GridOptionPanel extends JPanel implements ComponentTestable {
    private static final String PREVIEW = LocaleMgr.screen.getString("Preview");
    private static final String SHOW_GRID = LocaleMgr.screen.getString("ShowGrid");
    //	private static final String GRID_ACTIVE = LocaleMgr.screen.getString("ActiveGrid");
    private static final String COLUMNS_PER_PAGE = LocaleMgr.screen.getString("columnsPerPage");
    private static final String ROWS_PER_PAGE = LocaleMgr.screen.getString("rowsPerPage");
    private static final String LINE_COLOR = LocaleMgr.screen.getString("LineColor");
    private static final String PERCENTAGE = LocaleMgr.screen.getString("PortionOfLineDrawn");

    private class PreviewPanel extends JPanel {
        PreviewPanel() {
            setLayout(null);
            setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            paintPreview(g);
        }

        private void paintPreview(Graphics g) {
            //fill grid background
            Insets insets = getInsets();
            int x = insets.left;
            int y = insets.top;
            int width = getWidth() - insets.left - insets.right;
            int height = getHeight() - insets.top - insets.bottom;
            Rectangle oldClip = g.getClipBounds();

            g.setClip(x, y, width, height);

            g.setColor(Color.WHITE);
            g.fillRect(x, y, width, height);

            Color lineColor = colorPanel.getBackground();
            g.setColor(lineColor);
            boolean showGrid = showGridChkBox.isSelected();
            if (showGrid) {
                int columnsPerPage = ((Integer) columnSpinner.getValue()).intValue();
                int rowsPerPage = ((Integer) rowSpinner.getValue()).intValue();
                int portionOfLine = linePortionSlider.getValue();

                if (columnsPerPage <= 0)
                    columnsPerPage = 1;

                if (rowsPerPage <= 0)
                    rowsPerPage = 1;

                int xdelta = 320 / columnsPerPage;
                int ydelta = 480 / rowsPerPage;
                int xline = (xdelta * portionOfLine) / 200;
                int yline = (ydelta * portionOfLine) / 200;
                int i, j = 0;
                for (i = 0; i <= width; i += xdelta) {
                    for (j = 0; j <= height; j += ydelta) {
                        if (j > 0)
                            g.drawLine(x + i, y + j, x + i + xline, y + j);

                        if (i > 0)
                            g.drawLine(x + i, y + j, x + i, y + j + yline);

                        g.drawLine(x + i + xdelta - xline, y + j + ydelta, x + i + xdelta, y + j
                                + ydelta);
                        g.drawLine(x + i + xdelta, y + j + ydelta - yline, x + i + xdelta, y + j
                                + ydelta);
                    } //end for
                    g.drawLine(x + i + xdelta, y + j, x + i + xdelta, y + j + yline);
                } //end for
                g.drawLine(x + i, y + j, x + i + xline, y + j);
            } //end if

            g.setClip(oldClip);
        } //end paintPreview()

    }

    private JPanel optionsPanel = new JPanel();
    private JPanel previewPanel = new PreviewPanel();
    private TitledBorder previewTitledBorder;
    private JCheckBox showGridChkBox = new JCheckBox();
    private JLabel columnLabel = new JLabel();
    private JSpinner columnSpinner = new JSpinner();
    private JSpinner rowSpinner = new JSpinner();
    private JLabel rowLabel = new JLabel();
    private JSlider linePortionSlider = new JSlider();
    private JButton lineColorBtn = new JButton();
    private JLabel percentageLabel = new JLabel();
    private JPanel colorPanel = new JPanel();
    private JLabel linePortionLabel = new JLabel();
    private JColorChooser colorChooser = new JColorChooser();

    //ComponentTestable requires a parameterless constructor
    public GridOptionPanel() {
    }

    public GridOptionPanel(GridOptions options, OptionPanel optionPanel) {
        try {
            init();
            init(options);
            addListeners(options, optionPanel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void init() throws Exception {
        previewTitledBorder = new TitledBorder(PREVIEW);
        setLayout(new GridBagLayout());
        optionsPanel.setLayout(new GridBagLayout());

        JPanel previewContainer = new JPanel(new BorderLayout());
        previewContainer.add(previewPanel, BorderLayout.CENTER);

        previewContainer.setBorder(previewTitledBorder);
        //previewPanel.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));
        showGridChkBox.setText(SHOW_GRID);
        //activeGridChkBox.setText(GRID_ACTIVE);

        columnLabel.setText(COLUMNS_PER_PAGE);
        rowLabel.setText(ROWS_PER_PAGE);
        lineColorBtn.setText("...");
        lineColorBtn.setIconTextGap(0);
        percentageLabel.setText("");
        colorPanel.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));
        colorPanel.setPreferredSize(new Dimension(lineColorBtn.getPreferredSize().height,
                lineColorBtn.getPreferredSize().height));
        linePortionLabel.setText(PERCENTAGE + ":");

        optionsPanel.add(showGridChkBox, new GridBagConstraints(0, 0, 4, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 12, 0), 0, 0));
        //optionsPanel.add(activeGridChkBox,     new GridBagConstraints(1, 0, 2, 1, 0.0, 1.0
        //        ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        //		optionsPanel.add(new JSeparator(JSeparator.HORIZONTAL), new GridBagConstraints(0, 1, 4, 1, 1.0, 0.0, GridBagConstraints.WEST,
        //				GridBagConstraints.HORIZONTAL, new Insets(0, 4, 6, 0), 0, 0));

        optionsPanel.add(columnLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 6, 6), 0,
                0));
        optionsPanel.add(columnSpinner, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 0), 12, 0));
        optionsPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(2, 2, 1, 1, 1, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        optionsPanel.add(rowLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 24, 6, 6), 0,
                0));
        optionsPanel.add(rowSpinner, new GridBagConstraints(1, 3, 2, 1, 0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 0), 12, 0));
        optionsPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(2, 3, 1, 1, 1, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        optionsPanel.add(new JLabel(LINE_COLOR + ":"), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 24, 6, 6), 0, 0));
        optionsPanel.add(colorPanel, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 6), 0, 0));
        optionsPanel.add(lineColorBtn, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 0), 0, 0));

        optionsPanel.add(linePortionLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 24, 6, 6), 0, 0));
        optionsPanel.add(percentageLabel, new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 0), 0, 0));

        optionsPanel.add(linePortionSlider, new GridBagConstraints(0, 6, 4, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 60, 0, 0), 0,
                0));

        this.add(optionsPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(12, 6, 6, 6), 0, 0));
        this.add(previewContainer, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0));
    }

    //
    // private methods
    //
    private void init(GridOptions options) {
        enableGrid(options.m_showGrid);
        //activeGridChkBox.setSelected(options.m_gridActive);

        //spinner value
        columnSpinner.setValue(new Integer(options.m_columnsPerPage));
        rowSpinner.setValue(new Integer(options.m_rowsPerPage));

        //color value
        colorChooser.setColor(options.m_lineColor);
        colorPanel.setBackground(options.m_lineColor);

        //slider value
        linePortionSlider.setMinimum(1);
        linePortionSlider.setMaximum(100);
        linePortionSlider.setValue(options.m_portionOfLine);

        int value = linePortionSlider.getValue();
        String text = Integer.toString(value) + "%";
        percentageLabel.setText(text);
    } //end init()

    private void enableGrid(boolean showGrid) {
        showGridChkBox.setSelected(showGrid);
        columnSpinner.setEnabled(showGrid);
        rowSpinner.setEnabled(showGrid);
        linePortionSlider.setEnabled(showGrid);
        lineColorBtn.setEnabled(showGrid);
        columnLabel.setEnabled(showGrid);
        rowLabel.setEnabled(showGrid);
        linePortionLabel.setEnabled(showGrid);
        percentageLabel.setEnabled(showGrid);
    } //end showGrid()

    private void addListeners(final GridOptions options, final OptionPanel optionPanel) {
        showGridChkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                boolean selected = showGridChkBox.isSelected();
                enableGrid(selected);

                if (optionPanel != null) {
                    optionPanel.fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                            Grid.class, Grid.PROPERTY_HIDE_GRID, Boolean.valueOf(!selected));
                }
                updatePreview();
            } //end actionPerformed()
        });
        /*
         * activeGridChkBox.addActionListener(new ActionListener() { public void
         * actionPerformed(ActionEvent ev) { if (optionPanel != null) {
         * optionPanel.fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET, Grid.class,
         * Grid.PROPERTY_GRID_ACTIVE, Boolean.valueOf(activeGridChkBox.isSelected())); } } //end
         * actionPerformed() });
         */
        linePortionSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ev) {
                int value = linePortionSlider.getValue();
                String text = Integer.toString(value) + "%";
                percentageLabel.setText(text);

                if (optionPanel != null) {
                    optionPanel.fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                            Grid.class, Grid.PROPERTY_PERCENT_OF_CELL, new Integer(value));
                }

                updatePreview();
            } //end stateChanged()
        });

        lineColorBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                Color newColor = JColorChooser.showDialog(GridOptionPanel.this, LINE_COLOR,
                        options.m_lineColor);
                if (newColor != null) {
                    colorPanel.setBackground(newColor);

                    if (optionPanel != null) {
                        int value = newColor.getRGB();
                        optionPanel.fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                                Grid.class, Grid.PROPERTY_GRID_COLOR, new Integer(value));
                    }

                    updatePreview();
                }
            } //end actionPerformed()
        });

        columnSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ev) {
                if (optionPanel != null) {
                    int columnsPerPage = ((Integer) columnSpinner.getValue()).intValue();
                    if (columnsPerPage < 1)
                        columnsPerPage = 1;
                    else if (columnsPerPage > 20)
                        columnsPerPage = 20;

                    columnSpinner.setValue(new Integer(columnsPerPage));

                    optionPanel
                            .fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                                    Grid.class, Grid.PROPERTY_NB_OF_CELLS_IN_X, new Integer(
                                            columnsPerPage));
                }

                updatePreview();
            } //end stateChanged()
        });

        rowSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ev) {
                if (optionPanel != null) {
                    int rowsPerPage = ((Integer) rowSpinner.getValue()).intValue();
                    if (rowsPerPage < 1)
                        rowsPerPage = 1;
                    else if (rowsPerPage > 20)
                        rowsPerPage = 20;

                    rowSpinner.setValue(new Integer(rowsPerPage));

                    optionPanel.fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                            Grid.class, Grid.PROPERTY_NB_OF_CELLS_IN_Y, new Integer(rowsPerPage));
                }

                updatePreview();
            } //end stateChanged()
        });
    } //end addListeners()

    private void updatePreview() {
        previewPanel.repaint();
    } //end updatePreview()

    //
    // inner class
    //
    public static class GridOptions {
        boolean m_showGrid = false;
        boolean m_gridActive = false;
        int m_columnsPerPage = 16;
        int m_rowsPerPage = 24;
        Color m_lineColor = Color.lightGray;
        int m_portionOfLine = 5;

        public GridOptions(boolean showGrid, boolean gridActive, int x, int y, int perc, Color c) {
            m_showGrid = showGrid;
            m_gridActive = gridActive;
            m_columnsPerPage = x;
            m_rowsPerPage = y;
            m_lineColor = c;
            m_portionOfLine = perc;
        }

        private GridOptions() {
        } //default values
    } //end GridOptions

    //
    // Implements ComponentTestable
    // 
    public JComponent createOccurrence() {
        GridOptions options = new GridOptions();
        JPanel panel = new GridOptionPanel(options, null);
        return panel;
    } //end createOccurrence()

} //end GridOptionPanel

