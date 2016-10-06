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

package org.modelsphere.jack.awt;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;

public class PopupButton extends JButton {
    private static final int ICONS_GAP = 6; // space between the 2 icons
    private static final int ARROW_SIZE = 6;

    private class Renderer extends JButton {
        void redirectEvent(MouseEvent e) {
            processEvent(new MouseEvent(this, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e
                    .getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e
                    .isPopupTrigger(), e.getButton()));
        }

        @Override
        public void repaint() {
            super.repaint();
            PopupButton.this.repaint();
        }

        @Override
        public Container getParent() {
            // we need to override if we want the button UI to paint correctly
            // on a toolbar.
            return PopupButton.this.getParent();
        }

    }

    private class MultiIcon implements Icon {
        private Icon icon1;
        private Icon icon2;
        private Image image;
        private boolean paintDisabled; // if used as default Icon, icon1 (if

        // ImageIcon) will be grayed as defined
        // by the LF - set to false when used to
        // wrap disabled icon (effect assumed by
        // icon1 itself)

        public MultiIcon(Icon icon1, Icon icon2, boolean paintDisabled) {
            this.icon1 = icon1;
            this.icon2 = icon2;
            this.paintDisabled = paintDisabled;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            if (icon1 != null) {
                if (paintDisabled && icon1 instanceof ImageIcon && !c.isEnabled()) {
                    Icon disabledIcon1 = UIManager.getLookAndFeel().getDisabledIcon((JComponent) c,
                            icon1);
                    disabledIcon1.paintIcon(c, g, x, y);
                } else {
                    icon1.paintIcon(c, g, x, y);
                }
            }

            if (icon2 != null) {
                Insets insets = ((PopupButton) c).getInsets();
                int innerbuttonWidth = getRenderer().getWidth();
                // need a better way to compute the x location of the arrow to
                // fit any L&F
                int availableWidth = c.getWidth() - innerbuttonWidth;
                int adj = Math.min(insets.right, 4) / 2;

                icon2
                        .paintIcon(c, g,
                                innerbuttonWidth + (availableWidth - ARROW_SIZE) / 2 - adj, y);
            }
        }

        public int getIconWidth() {
            int width = 0;
            if (icon1 != null)
                width += icon1.getIconWidth();
            if (icon2 != null)
                width += icon2.getIconWidth() + ICONS_GAP;
            return width;
        }

        public int getIconHeight() {
            int height = 0;
            if (icon1 != null)
                height = icon1.getIconHeight();
            if (icon2 != null)
                height = Math.max(height, icon2.getIconHeight());
            return height;
        }

    }

    private static class ArrowIcon implements Icon {

        ArrowIcon() {
        }

        public int getIconWidth() {
            return ARROW_SIZE;
        }

        public int getIconHeight() {
            return ARROW_SIZE;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color oldColor = g.getColor();
            boolean enabled = c.isEnabled();

            int i = 0;
            int j = 0;

            int size = Math.max(ARROW_SIZE / 2, 2);
            int middle = ARROW_SIZE / 2;

            y = (c.getHeight() - size) / 2;

            g.translate(x, y);

            if (enabled)
                g.setColor(UIManager.getColor("controlDkShadow"));
            else
                g.setColor(UIManager.getColor("controlShadow"));

            j = 0;
            for (i = size - 1; i >= 0; i--) {
                g.drawLine(middle - i, j, middle + i, j);
                j++;
            }

            g.translate(-x, -y);

            g.setColor(oldColor);
        }

    }

    private static class EmptyIcon implements Icon {
        @Override
        public int getIconHeight() {
            return 16;
        }

        @Override
        public int getIconWidth() {
            return 16;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
        }
    }

    private class UIListener implements MouseListener, MouseMotionListener {

        private void update(MouseEvent e, boolean exited) {
            Rectangle rect = computeInnerArea();
            if (!exited && rect.contains(e.getPoint())) {
                useRenderer = true;
                blockActionEvents = false;
            } else {
                useRenderer = false;
                blockActionEvents = true;
            }
            getRenderer().redirectEvent(e);
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            update(e, false);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            update(e, false);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            update(e, true);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            update(e, false);
            if (isEnabled() && popup != null) {
                if (popup.isVisible()) {
                    popup.setVisible(false);
                } else if (!computeInnerArea().contains(e.getPoint())) {
                    AwtUtil.showPopupPanel(PopupButton.this, popup);
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            update(e, false);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            update(e, false);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            update(e, false);
        }

    }

    public static class PopupPanel extends JPopupMenu {
        PopupPanel() {
            setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        }

        public void show(Component invoker, int x, int y) {
            if (invoker != null) {
                y += invoker.getHeight(); // add the height of the invoker to
                // locate the popup at the bottom of
                // this invoker
            }
            super.show(invoker, x, y);
        }
    }

    private ArrowIcon arrowIcon;

    private boolean useRenderer = false;

    private Renderer renderer;

    private PopupPanel popup;

    private Rectangle innerArea;

    private Object[] values;

    private boolean blockActionEvents = true;

    public PopupButton() {
        super();
        init();
    }

    public PopupButton(Action a) {
        super(a);
        init();
    }

    public PopupButton(Icon icon) {
        super(icon);
        init();
    }

    public PopupButton(String text, Icon icon) {
        super(text, icon);
        init();
    }

    public PopupButton(String text) {
        super(text);
        init();
    }

    private void init() {
        UIListener listener = new UIListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public PopupPanel getPopup() {
        return popup;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        if (this.values == values)
            return;
        this.values = values;

        if (popup != null)
            popup.setVisible(false);

        popup = createPopupPanel();
        invalidate();
    }

    protected PopupPanel createPopupPanel() {
        return new PopupPanel();
    }

    public Rectangle computeInnerArea() {
        if (innerArea == null) {
            int x = getInsets().left + ((MultiIcon) getIcon()).icon1.getIconWidth()
                    + getInsets().right;
            Rectangle bounds = getBounds();
            innerArea = new Rectangle(0, 0, x, bounds.height);
        }
        return innerArea;
    }

    public void setIcon(Icon icon) {
        if (icon == null) {
            icon = new EmptyIcon();
        }
        super.setIcon(new MultiIcon(icon, arrowIcon, true));
        getRenderer().setIcon(icon);
        invalidate();
    }

    public void setDisabledIcon(Icon icon) {
        if (icon == null) {
            icon = new EmptyIcon();
        }
        super.setDisabledIcon(new MultiIcon(icon, arrowIcon, false));
        getRenderer().setDisabledIcon(icon);
        invalidate();
    }

    @Override
    public void setMargin(Insets m) {
        super.setMargin(m);
        getRenderer().setMargin(m);
    }

    @Override
    public void setBorder(Border b) {
        super.setBorder(b);
        getRenderer().setBorder(b);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        getRenderer().setText(text);
    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
        getRenderer().setSelected(b);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        getRenderer().setEnabled(b);
    }

    @Override
    public void setHideActionText(boolean hideActionText) {
        super.setHideActionText(hideActionText);
        getRenderer().setHideActionText(hideActionText);
    }

    @Override
    public void setHorizontalAlignment(int alignment) {
        super.setHorizontalAlignment(alignment);
        getRenderer().setHorizontalAlignment(alignment);
    }

    @Override
    public void setHorizontalTextPosition(int textPosition) {
        super.setHorizontalTextPosition(textPosition);
        getRenderer().setHorizontalTextPosition(textPosition);
    }

    @Override
    public void setVerticalAlignment(int alignment) {
        super.setVerticalAlignment(alignment);
        getRenderer().setVerticalAlignment(alignment);
    }

    @Override
    public void setVerticalTextPosition(int textPosition) {
        super.setVerticalTextPosition(textPosition);
        getRenderer().setVerticalTextPosition(textPosition);
    }

    @Override
    public void setIconTextGap(int iconTextGap) {
        super.setIconTextGap(iconTextGap);
        getRenderer().setIconTextGap(iconTextGap);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        innerArea = null; // force recompute of the inner area
        super.setBounds(x, y, width, height);
        getRenderer().setBounds(0, 0, width - ARROW_SIZE - ICONS_GAP, height);
    }

    @Override
    public void setBorderPainted(boolean b) {
        super.setBorderPainted(b);
        getRenderer().setBorderPainted(b);
    }

    @Override
    public void setRolloverEnabled(boolean b) {
        super.setRolloverEnabled(b);
        getRenderer().setRolloverEnabled(b);
    }

    @Override
    public void setContentAreaFilled(boolean b) {
        super.setContentAreaFilled(b);
        getRenderer().setContentAreaFilled(b);
    }

    public void hidePopup() {
        if (popup != null) {
            popup.setVisible(false);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (useRenderer) {
            getRenderer().paint(g);
        }
    }

    @Override
    public void validate() {
        super.validate();
        getRenderer().validate();
    }

    @Override
    public void invalidate() {
        innerArea = null;
        super.invalidate();
        getRenderer().invalidate();
    }

    protected final Renderer getRenderer() {
        if (renderer == null) {
            renderer = new Renderer();
            renderer.setFocusable(false);
            renderer.setOpaque(false);
            renderer.setSelected(isSelected());
            renderer.setDefaultCapable(false);
            renderer.setMargin(getMargin());
            renderer.setText(getText());
            renderer.setBorder(getBorder());
            renderer.setIconTextGap(getIconTextGap());
            renderer.setEnabled(isEnabled());
            renderer.setFocusPainted(isFocusPainted());
            renderer.setHideActionText(getHideActionText());
            renderer.setHorizontalAlignment(getHorizontalAlignment());
            renderer.setHorizontalTextPosition(getHorizontalTextPosition());
            renderer.setVerticalAlignment(getVerticalAlignment());
            renderer.setVerticalTextPosition(getVerticalTextPosition());
            renderer.setContentAreaFilled(isContentAreaFilled());
            renderer.setBorderPainted(isBorderPainted());
        }
        return renderer;
    }

    @Override
    public void updateUI() {
        super.updateUI();

        if (arrowIcon == null) {
            arrowIcon = new ArrowIcon();
        }

        setIconTextGap(0);
        setFocusable(false);
        getRenderer().updateUI();

        if (popup != null)
            popup.updateUI();
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setSize(new Dimension(800, 600));
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {

            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            // System.out.println(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JToolBar toolbar = new JToolBar();
        frame.getContentPane().add(toolbar, BorderLayout.NORTH);

        PopupButton testComp = new PopupButton();
        JButton testComp1 = new JButton();
        JButton testComp2 = new JButton();
        JButton testComp3 = new JButton();
        JButton testComp4 = new JButton();

        MediaTracker tracker = new MediaTracker(frame);
        Image image = Toolkit.getDefaultToolkit().getImage(
                PopupButton.class.getResource("undo.gif"));
        tracker.addImage(image, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Icon icon = new ImageIcon(image);
        testComp.setIcon(icon);

        icon = new ImageIcon(image);
        testComp1.setIcon(icon);
        testComp1.setFocusable(false);
        testComp2.setIcon(icon);
        testComp2.setFocusable(false);
        testComp3.setIcon(icon);
        testComp3.setFocusable(false);
        testComp4.setIcon(icon);
        testComp4.setFocusable(false);

        testComp1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    SwingUtilities.updateComponentTreeUI(frame);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        testComp2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(frame);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        testComp3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager
                            .setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(frame);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        testComp4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(frame);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        toolbar.add(testComp);
        toolbar.add(testComp1);
        toolbar.add(testComp2);
        toolbar.add(testComp3);
        toolbar.add(testComp4);

        AwtUtil.centerWindow(frame);
        frame.setVisible(true);
    }

    protected void fireActionPerformed(int id, String command) {
        getPopup().setVisible(false);
        super.fireActionPerformed(new ActionEvent(this, id, command));
    }

    @Override
    protected void fireActionPerformed(ActionEvent event) {
        if (!blockActionEvents)
            super.fireActionPerformed(event);
    }

}
