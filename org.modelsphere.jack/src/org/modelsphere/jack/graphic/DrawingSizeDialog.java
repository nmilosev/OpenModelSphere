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

package org.modelsphere.jack.graphic;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.ThinBevelBorder;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

@SuppressWarnings("serial")
public class DrawingSizeDialog extends JDialog implements ChangeListener {

    private DrawingSizeCanvas canvas;
    private JSlider scaleSlider;
    private JTextField scaleTF;
    private JLabel sizeLabel;
    private int printScale;
    private Dimension nbPages;
    private Rectangle2D contentRect;
    private Rectangle2D initialContentRect;
    private BufferedImage preview;
    private Diagram previewDiagram;
    private double scale;
    private double xIncr, yIncr;
    private int minX, minY;
    private boolean accepted = false;

    public DrawingSizeDialog(Component comp, Diagram diagram) {
        super((comp instanceof Frame ? (Frame) comp : (Frame) SwingUtilities.getAncestorOfClass(
                Frame.class, comp)), LocaleMgr.screen.getString("setDrawingArea"), true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int canvasWidth = Math.min(screenSize.width, screenSize.height) / 3;
        Rectangle drawingArea = diagram.getDrawingArea();
        int width = Math.max(drawingArea.width, drawingArea.height) * 2;
        scale = Math.max(20, width / canvasWidth);
        printScale = diagram.getPrintScale();
        nbPages = new Dimension(diagram.getNbPages());
        Dimension2D pageSize = diagram.getPageSize();
        xIncr = pageSize.getWidth() / scale;
        yIncr = pageSize.getHeight() / scale;
        minX = minY = 1;
        previewDiagram = diagram;
        Enumeration enumeration = diagram.components();
        while (enumeration.hasMoreElements()) {
            GraphicComponent gc = (GraphicComponent) enumeration.nextElement();
            if (contentRect == null)
                contentRect = new Rectangle(gc.getRectangle());
            else
                contentRect.add(gc.getRectangle());
        }
        if (contentRect != null) {
            minX = (int) (1 + (contentRect.getX() + contentRect.getWidth()) / pageSize.getWidth());
            minY = (int) (1 + (contentRect.getY() + contentRect.getHeight()) / pageSize.getHeight());
            contentRect.setRect(contentRect.getX() / scale, contentRect.getY() / scale, contentRect
                    .getWidth()
                    / scale, contentRect.getHeight() / scale);
            //            contentRect.y /= scale;
            //            contentRect.width /= scale;
            //            contentRect.height /= scale;

            initialContentRect = (Rectangle2D) contentRect.clone();
        }

        // Scale Label
        JLabel scaleLabel = new JLabel(LocaleMgr.screen.getString("printerScaling"));
        scaleTF = new JTextField(Integer.toString(printScale), 3);
        JPanel scalePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        scalePanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        scalePanel.add(scaleLabel);
        scalePanel.add(scaleTF);
        scaleTF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processScaleTF();
            }
        });

        //Scale Slider
        scaleSlider = new JSlider(JSlider.HORIZONTAL, 5, 200, printScale);
        scaleSlider.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        scaleSlider.setMajorTickSpacing(20);
        scaleSlider.setMinorTickSpacing(5);
        scaleSlider.setPaintTicks(true);
        scaleSlider.setPaintLabels(true);
        scaleSlider.addChangeListener(this);

        // Canvas Panel
        sizeLabel = new JLabel();
        sizeLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        refreshSizeLabel();
        canvas = new DrawingSizeCanvas();
        canvas.setPreferredSize(new Dimension(canvasWidth, canvasWidth));
        canvas.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));
        JPanel canvasPanel = new JPanel(new BorderLayout());
        canvasPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));
        canvasPanel.add(sizeLabel, BorderLayout.NORTH);
        canvasPanel.add(canvas, BorderLayout.CENTER);

        // Button Panel
        JButton OKBtn = new JButton(LocaleMgr.screen.getString("OK"));
        JButton cancelBtn = new JButton(LocaleMgr.screen.getString("Cancel"));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 6, 0));
        AwtUtil.normalizeComponentDimension(new JButton[] { OKBtn, cancelBtn });
        btnPanel.add(OKBtn);
        btnPanel.add(cancelBtn);

        OKBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                accepted = true;
                dispose();
            }
        });
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(scalePanel);
        contentPane.add(scaleSlider);
        contentPane.add(canvasPanel);
        contentPane.add(btnPanel);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(OKBtn);

        updatePreview();

        Dimension dim = AwtUtil.getBestDialogSize();
        setSize(Math.min(600, dim.width), dim.height);
        setLocationRelativeTo(comp);
    }

    private void updatePreview() {
        if (preview != null) {
            preview.flush();
        }
        Rectangle rect = previewDiagram.getContentRect();
        if (rect != null) {
            int w = (int) ((rect.x + rect.width) / scale);
            int h = (int) ((rect.y + rect.height) / scale);

            if (w < 4 || h < 4) {
                preview = null;
            } else {
                preview = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = preview.createGraphics();
                g.setClip(0, 0, rect.x + rect.width, rect.y + rect.height);
                float zoom = previewDiagram.getMainView().getZoomFactor();
                AffineTransform at = AffineTransform.getScaleInstance(1D / (scale * zoom),
                        1D / (scale * zoom));
                g.setTransform(at);

                previewDiagram.paint(g, previewDiagram.getMainView(),
                        RenderingFlags.ERASE_BACKGROUND, GraphicComponent.DRAW_WHOLE, 0, rect.x
                                + rect.width, 0, rect.y + rect.height);
                g.dispose();
            }

        } else {
            preview = null;
        }
        canvas.repaint();
    }

    public final boolean isAccepted() {
        return accepted;
    }

    public final Dimension getNbPages() {
        return nbPages;
    }

    public final Dimension getPan() {
        if (contentRect == null || initialContentRect == null) {
            return new Dimension(0, 0);
        }
        return new Dimension((int) ((contentRect.getX() - initialContentRect.getX()) * scale),
                (int) ((contentRect.getY() - initialContentRect.getY()) * scale));
    }

    public final int getPrintScale() {
        return printScale;
    }

    private void refreshSizeLabel() {
        String pattern = LocaleMgr.screen.getString("drawingAreaInPages");
        String text = MessageFormat.format(pattern, new Object[] { new Integer(nbPages.height),
                new Integer(nbPages.width) });
        sizeLabel.setText(text);
    }

    private void processScaleTF() {
        int newScale = printScale;
        try {
            newScale = Integer.parseInt(scaleTF.getText());
            newScale = Math.max(1, newScale);
        } catch (NumberFormatException e) {
        }

        if (printScale != newScale) {
            double scale = (float) printScale / newScale;
            xIncr *= scale;
            yIncr *= scale;
            if (contentRect != null) {
                minX = (int) (1 + ((contentRect.getX() + contentRect.getWidth()) / xIncr));
                minY = (int) (1 + ((contentRect.getY() + contentRect.getHeight()) / yIncr));
            }
            nbPages.width = (int) Math.max(minX, (nbPages.getWidth() / scale));
            nbPages.height = (int) Math.max(minY, (nbPages.getHeight() / scale));
            printScale = newScale;
            refreshSizeLabel();
            updatePreview();
            canvas.repaint();
        }
        scaleSlider.setValue(printScale);
        scaleTF.setText(Integer.toString(printScale));
    }

    @SuppressWarnings("serial")
    private class DrawingSizeCanvas extends JPanel {

        private Color selectionBackground;
        private Color nonSelectionBackground;
        private Color selectionForeground;
        private Color nonSelectionForeground;
        private Color hightlight;
        private Point2D dragStart;
        private Rectangle2D dragStartRect;

        public DrawingSizeCanvas() {
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (contentRect != null && contentRect.contains(e.getPoint())) {
                        if (dragStart == null) {
                            dragStart = e.getPoint();
                            dragStartRect = (Rectangle2D) contentRect.clone();
                            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                        }
                        return;
                    }
                    setDrawingSize(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    dragStart = null;
                    if (contentRect != null && contentRect.contains(e.getPoint())) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                }

            });
            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    if (dragStart != null && initialContentRect != null && contentRect != null) {
                        Point dragTarget = e.getPoint();
                        double deltax = dragStartRect.getX() + dragTarget.x - dragStart.getX();
                        double deltay = dragStartRect.getY() + dragTarget.y - dragStart.getY();
                        contentRect = new Rectangle2D.Double(deltax, deltay, initialContentRect
                                .getWidth(), initialContentRect.getHeight());
                        constraints(contentRect);
                        processScaleTF();
                        minX = (int) (1 + ((contentRect.getX() + contentRect.getWidth()) / xIncr));
                        minY = (int) (1 + ((contentRect.getY() + contentRect.getHeight()) / yIncr));
                        repaint();
                        return;
                    }
                    setDrawingSize(e);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (dragStart == null && contentRect != null
                            && contentRect.contains(e.getPoint())) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                }

            });
        }

        private void constraints(Rectangle2D rect) {
            if (rect.getX() < 0) {
                rect.setRect(0, rect.getY(), rect.getWidth(), rect.getHeight());
            }
            if (rect.getY() < 0) {
                rect.setRect(rect.getX(), 0, rect.getWidth(), rect.getHeight());
            }
            if (rect.getX() + rect.getWidth() > (nbPages.width * xIncr)) {
                // in some cases, like for images, areas can be outside the drawing area, make sure
                // we do not adjust x or y in this case.
                if (rect.getWidth() <= (nbPages.width * xIncr))
                    rect.setRect((nbPages.width * xIncr) - rect.getWidth(), rect.getY(), rect
                            .getWidth(), rect.getHeight());
            }
            if (rect.getY() + rect.getHeight() >= (int) (nbPages.height * yIncr)) {
                if (rect.getHeight() <= (nbPages.height * yIncr))
                    rect.setRect(rect.getX(), (nbPages.height * yIncr) - rect.getHeight(), rect
                            .getWidth(), rect.getHeight());
            }
        }

        private void setDrawingSize(MouseEvent e) {
            Dimension size = getSize();
            int x = Math.min(e.getX(), size.width);
            int y = Math.min(e.getY(), size.height);
            x = Math.max(1 + (int) (x / xIncr), minX);
            y = Math.max(1 + (int) (y / yIncr), minY);
            if (nbPages.width != x || nbPages.height != y) {
                nbPages.width = x;
                nbPages.height = y;
                refreshSizeLabel();
                repaint();
            }
        }

        public void paintComponent(Graphics g) {
            Rectangle clipRect = g.getClipBounds();
            g.setColor(nonSelectionBackground);
            g.fillRect(clipRect.x, clipRect.y, clipRect.width, clipRect.height);
            g.setColor(selectionBackground);
            g.fillRect(0, 0, (int) (nbPages.width * xIncr), (int) (nbPages.height * yIncr));

            g.setColor(nonSelectionForeground);
            int width = clipRect.x + clipRect.width;
            int height = clipRect.y + clipRect.height;
            int i;
            for (i = 0;; i++) {
                int x = (int) (i * xIncr);
                if (x > width)
                    break;
                g.drawLine(x, 0, x, height);
            }
            for (i = 0;; i++) {
                int y = (int) (i * yIncr);
                if (y > height)
                    break;
                g.drawLine(0, y, width, y);
            }

            g.setColor(selectionForeground);
            Stroke stroke = ((Graphics2D) g).getStroke();
            ((Graphics2D) g).setStroke(new BasicStroke(3f));
            g.drawRect(0, 0, (int) (nbPages.width * xIncr), (int) (nbPages.height * yIncr));
            ((Graphics2D) g).setStroke(stroke);

            if (contentRect != null) {
                if (preview != null) {
                    g.drawImage(preview, (int) (contentRect.getX() - initialContentRect.getX()),
                            (int) (contentRect.getY() - initialContentRect.getY()), null);
                }
                g.setColor(hightlight);
                g.drawRect((int) contentRect.getX(), (int) contentRect.getY(), (int) contentRect
                        .getWidth(), (int) contentRect.getHeight());
            }
        }

        @Override
        public void updateUI() {
            super.updateUI();

            selectionBackground = UIManager.getColor("List.background");
            nonSelectionBackground = AwtUtil.darker(UIManager.getColor("Panel.background"), 0.95f);
            //            selectionForeground = new Color(150, 150, 150);
            selectionForeground = UIManager.getColor("List.selectionBackground");
            nonSelectionForeground = new Color(120, 120, 120);
            hightlight = AwtUtil.darker(selectionBackground, 0.6f);
            //            selectionBackground = UIManager.getColor("List.selectionBackground");
            //            nonSelectionBackground = UIManager.getColor("List.background");
            //            selectionForeground = UIManager.getColor("List.selectionForeground");
            //            nonSelectionForeground = UIManager.getColor("List.foreground");
        }

    }

    /////////////////////////////////////
    // ChangeListener SUPPORT
    //
    public void stateChanged(ChangeEvent e) {
        scaleTF.setText(Integer.toString(scaleSlider.getValue()));
        processScaleTF();
    }
    //
    // End of ChangeListener SUPPORT
    ////////////////////////////////////////
}
