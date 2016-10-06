package org.modelsphere.jack.srtool;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.*;

import org.modelsphere.jack.graphic.GraphicUtil;

@SuppressWarnings("serial")
final class SplashImage extends JPanel implements java.awt.image.ImageObserver {
    private java.awt.Image image;
    private Point versionPosition;
    private Point buildPosition;
    private Point textPosition;
    private static final Color FOREGROUND = new Color(255, 255, 255);
    private Splash splash;
    private String statusText = null;

    private JButton closeButton;

    private Rectangle textBounds;

    //constructors
    public SplashImage(Splash splash, boolean closable) {
        this.splash = splash;
        init(closable);
    }

    private void init(boolean closable) {
        setLayout(null);
        if (closable) {
            ImageIcon icon = new ImageIcon(GraphicUtil.loadImage(ApplicationContext.class,
                    "international/resources/closeButton.jpg"));
            closeButton = new JButton(icon);
            closeButton.setMargin(new Insets(-2, -5, -2, -4));
            closeButton.setBackground(Color.BLACK);
            closeButton.setOpaque(true);
            closeButton.setFocusPainted(false);
            closeButton.repaint();
            closeButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    splash.dispose();
                }
            });
            add(closeButton);
        }
    }

    //paint image
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GraphicUtil.configureGraphics(g2);

        splash.initGraphics();

        Color previousColor = g.getColor();

        g2.setColor(new Color(255, 255, 255, 0));
        g2.fillRect(getX(), getY(), getWidth(), getHeight());

        if (image != null)
            g.drawImage(image, 0, 0, this);

        //draw version and build number
        drawVersionAndBuildNumber(g2);

        //draw copyright
        drawCopyright(g2);

        if (statusText != null && textBounds != null) {
            g.setFont(Splash.STATUS_FONT);
            g.setColor(FOREGROUND);
            Rectangle oldclip = g.getClipBounds();
            // preserve a right margin
            g.setClip(textBounds);
            g.drawString(statusText, textBounds.x, textBounds.y + textBounds.height / 2);
            g.setClip(oldclip);
        }
        g.setColor(previousColor);
    }

    private void drawVersionAndBuildNumber(Graphics2D g2) {
        //draw version number
        if (versionPosition != null) {
            g2.setFont(Splash.VERSION_FONT);
            int x = versionPosition.x - 20;
            int y = versionPosition.y - 8;
            g2.setColor(FOREGROUND);
            g2.drawString(splash.version, x, y);
        }

        //draw build version
        if (buildPosition != null) {
            g2.setFont(Splash.BUILD_FONT);
            g2.setColor(FOREGROUND);
            int x = buildPosition.x - 20;
            int y = buildPosition.y - 8;
            g2.drawString(Splash.buildString, x, y);
        }
    }

    private void drawCopyright(Graphics2D g2) {
        g2.setFont(Splash.STATUS_FONT);
        g2.setColor(FOREGROUND);
        FontMetrics fm = g2.getFontMetrics(Splash.STATUS_FONT);
        int dy = fm.getHeight() - 3;

        int x = 6;
        int y = 210;

        StringTokenizer st = new StringTokenizer(Splash.copyrightString, "\n");
        while (st.hasMoreTokens()) {
            String line = st.nextToken();
            g2.drawString(line, x, y);
            y += dy;
        }
    }

    public void setVersionPosition(Point newVersionPosition) {
        versionPosition = newVersionPosition;
    }

    public void setBuildPosition(Point newPosition) {
        buildPosition = newPosition;
    }

    public void setStatusText(String text) {
        this.statusText = text;
        if (textBounds != null) {
            RepaintManager.currentManager(this).addDirtyRegion(this, textBounds.x, textBounds.y,
                    textBounds.width, textBounds.height);
            RepaintManager.currentManager(this).paintDirtyRegions();
        }
    }

    public String getStatusText() {
        return statusText == null ? "" : statusText;
    }

    public void setTextPosition(Point position) {
        this.textPosition = position;
        if (textPosition != null)
            textBounds = new Rectangle(textPosition.x, textPosition.y, getWidth() - Splash.X_LOC
                    - 6, 30);
    }

    public void setImage(Image newImage) {
        image = newImage;
        invalidate();
    }

    @Override
    public Dimension getPreferredSize() {
        if (image != null) {
            return new Dimension(image.getWidth(null), image.getHeight(null));
        }
        return super.getPreferredSize();
    }

    @Override
    public void doLayout() {
        if (closeButton != null && image != null) {
            Dimension prefSize = closeButton.getPreferredSize();
            int x = (int) ((image.getWidth(null)) - (prefSize.width * 1.5) - 2);
            int y = 4;
            closeButton
                    .setBounds(new Rectangle(x, y, (int) (prefSize.width * 1.5), prefSize.height));
        }
    }

}