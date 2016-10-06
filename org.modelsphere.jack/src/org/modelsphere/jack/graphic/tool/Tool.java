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

package org.modelsphere.jack.graphic.tool;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.srtool.ApplicationContext;

public class Tool implements Cloneable {
    private static final Color MASTER_TOOL_HIGHLIGHT_COLOR = UIManager
            .getColor("List.selectionBackground");

    public List masterActivationListeners = new ArrayList();

    // Tool categories
    public static final int CREATION_TOOL = 0;
    public static final int DRAWING_TOOL = 1;

    protected int userId;
    protected DiagramView view = null;

    protected Diagram model = null;
    protected Image icon = null;
    protected String text = null;
    protected int auxiliaryIndex;
    protected MetaClass[] supportedDiagramMetaClasses;
    protected String toolBarKey = null; // if null, default
    protected boolean alwaysVisible = false;

    private Component component;

    public void setImage(Image I) {
        icon = I;
    }

    public Image getImage() {
        return icon;
    }

    public void setText(String T) {
        text = T;
    }

    // supportedDiagramMetaClasses : if null, this tool support all diagrams
    public Tool(int userId, String text, Image icon) {
        this.userId = userId;
        this.icon = icon;
        this.text = text;
        this.auxiliaryIndex = -1;
    }

    public Tool(int userId) {
        this.userId = userId;
    }

    public void setSupportedDiagramMetaClasses(MetaClass[] supportedDiagramMetaClasses) {
        this.supportedDiagramMetaClasses = supportedDiagramMetaClasses;
    }

    public MetaClass[] getSupportedDiagramMetaClasses() {
        return this.supportedDiagramMetaClasses;
    }

    public final void setToolBar(String toolBarKey) {
        this.toolBarKey = toolBarKey;
    }

    public final void setAlwaysVisible(boolean alwaysVisible) {
        this.alwaysVisible = alwaysVisible;
    }

    public final boolean isAlwaysVisible() {
        return alwaysVisible;
    }
    
    private boolean _visible = true;
    public void setVisible(boolean visible) { _visible = visible; }
    public boolean isVisible() { return _visible; }

    public final String getToolBar() {
        return toolBarKey;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null; // cannot occur
        }
    }

    public final int getUserId() {
        return userId;
    }

    public final Image getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }

    public String getText(DbObject notation) {
        return getText();
    }

    public final int getAuxiliaryIndex() {
        return auxiliaryIndex;
    }

    public final void setAuxiliaryIndex(int idx) {
        auxiliaryIndex = idx;
    }

    public final void setDiagramView(DiagramView view) {
        this.view = view;
        this.model = view.getModel();
    }

    protected DiagramView getDiagramView() {
        return view;
    }

    public void addMasterActivationListener(MasterActivationListener l) {
        if (masterActivationListeners.contains(l))
            return;
        masterActivationListeners.add(l);
    }

    public void removeMasterActivationListener(MasterActivationListener l) {
        masterActivationListeners.remove(l);
    }

    public void fireMasterActivationListeners() {
        for (int i = masterActivationListeners.size(); --i >= 0;) {
            MasterActivationListener listener = (MasterActivationListener) masterActivationListeners
                    .get(i);
            listener.masterActivated(this);
        }
    }

    // Some tools show a temporary drawing (in XOR mode) until the action is
    // complete
    public void paint(Graphics g) {
    }

    public void reset() {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void toolActivated() {
        updateHelp();
    }

    public void toolDesactivated() {
    }

    public void updateHelp() {
    }

    protected final boolean scrollToVisible(int x, int y) {
        if (!(view.getParent() instanceof JViewport))
            return false;
        Rectangle viewRect = ((JViewport) view.getParent()).getViewRect();
        float zoomFactor = view.getZoomFactor();
        if (viewRect.contains((int) (x * zoomFactor), (int) (y * zoomFactor)))
            return false;
        view.scrollRectToVisible(new Rectangle(x, y, 0, 0));
        return true;
    }

    protected final Graphics getGraphics() {
        Graphics g = view.getGraphics();
        return g;
    }

    public Cursor getCursor() {
        return null;
    }

    // Return a new JComponent to be displayed in the toolbar. This component
    // must implement ToolComponent
    protected JComponent createUIComponent() {
        DefaultToolComponent comp = new DefaultToolComponent();
        String toolTip = getText();
        comp.setToolTipText(toolTip);
        Image image = getIcon();
        if (image != null)
            comp.setIcon(new ImageIcon(image));

        comp.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1 && component.isEnabled()) {
                    fireMasterActivationListeners();
                }
            }

        });

        component = comp;
        return comp;
    }

    public static void paintMasterToolEffect(Graphics2D g, JComponent c) {
        Composite composite = ((Graphics2D) g).getComposite();
        Color color = g.getColor();

        g.setColor(MASTER_TOOL_HIGHLIGHT_COLOR);
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.20f));
        Insets insets = c.getInsets();
        g.fillRect(insets.left * 2 / 3, insets.top * 2 / 3, c.getWidth()
                - (insets.left + insets.right) * 2 / 3 + 1, c.getHeight()
                - (insets.top + insets.bottom) * 2 / 3);

        g.setColor(color);
        ((Graphics2D) g).setComposite(composite);
    }

    protected final void setHelpText(String help) {
        ApplicationContext.getDefaultMainFrame().getStatusBar().getModel().setMessage(help);
    }

}
