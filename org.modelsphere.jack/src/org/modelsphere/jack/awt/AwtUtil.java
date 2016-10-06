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
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.plugins.xml.extensions.SaveFileExtension;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.text.MessageFormat;

public class AwtUtil {
    private static final String kFileReadOnly = LocaleMgr.message.getString("fileReadOnly");
    private static final String kFileExists = LocaleMgr.message.getString("fileExists");

    public static final Icon NULL_ICON = LocaleMgr.screen.getImageIcon("Empty16");

    /**
     * Displays the file chooser dialog for save and returns a File object for the path entered by
     * the user; returns null if the user cancels the dialog. This method validates the result,
     * asking for a confirmation if the file already exists, and checking if the file is read-only.
     * 
     * @parent: component where to center the file chooser.
     * @title: title of the file chooser.
     * @filter: extension filter; may be null.
     * @file: represents a directory or a file; specifies the initial directory of the file chooser;
     *        if file, it becomes the initial setting of the file entry field. May be null.
     * @isSave: indicates a Save instead of a Save As: if true and <file> represents a writable
     *          file, returns it without displaying the file chooser.
     * 
     *          Example: File file = AwtUtil.showSaveAsDialog(parent, title,
     *          ExtensionFileFilter.txtFileFilter, new File(path));
     */
    public static FileAndFilter showSaveAsDialog(Component parent, String title, ExtensionFileFilter filter,
            ExtensionFileFilter[] optionalFilters, File file) {
        return showSaveAsDialog(parent, title, filter, optionalFilters, file, false);
    }

    public static FileAndFilter showSaveAsDialog(Component parent, String title, ExtensionFileFilter defaultFilter,
            ExtensionFileFilter[] optionalFilters, File file, boolean isSave) {
        if (isSave && file != null) {
            if (!file.exists() || file.canWrite()) {
                FileAndFilter selection = new FileAndFilter(file, defaultFilter);  
                return selection;
            }
            
            String message = MessageFormat.format(kFileReadOnly, new Object[] { file.getName() });
            JOptionPane.showMessageDialog(parent, message, ApplicationContext.getApplicationName(),
                    JOptionPane.ERROR_MESSAGE);
        }

        final JFileChooser chooser = new JFileChooserFix(file);
        chooser.setDialogTitle(title);
        if (file != null /* && file.isFile() */)
            chooser.setSelectedFile(file);
        
        //add filters
        if (defaultFilter != null) { //add default filter in first position
            chooser.addChoosableFileFilter(defaultFilter);
        }
        if (optionalFilters != null) { //add optional filters, if any
            SaveFileExtension.addPlugableFileFilters(chooser, optionalFilters);   
        }
        if (defaultFilter != null) { //set default after adding other filters (circumvent a Swing bug)
            chooser.setFileFilter(defaultFilter);
        }
        
        chooser.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent ev) {
				String prop = ev.getPropertyName();
				if (JFileChooser.FILE_FILTER_CHANGED_PROPERTY.equals(prop)) {
					changeDefaultExtension(chooser);
				} //end if
			} //end propertyChange()	
        });
        
        Dimension dim = chooser.getPreferredSize();
        // add extra space to the chooser (not evaluated correctly - as of java
        // 1.3.1)
        chooser.setPreferredSize(new Dimension(dim.width + 75, dim.height + 75));
        while (true) {
            int result = chooser.showSaveDialog(parent);
            if (result != JFileChooser.APPROVE_OPTION)
                return null;
            file = chooser.getSelectedFile();
            if (file == null)
                return null;
            if (file.getName().indexOf('.') == -1 && defaultFilter != null)
                file = new File(file.getPath() + '.' + defaultFilter.getExtension());
            if (!file.exists())
                break;

            if (file.canWrite()) {
                String message = MessageFormat.format(kFileExists, new Object[] { file.getName(),
                        "\'" });
                int rc = JOptionPane.showConfirmDialog(parent, message,
                        ApplicationContext.getApplicationName(), JOptionPane.YES_NO_CANCEL_OPTION);
                if (rc == JOptionPane.YES_OPTION)
                    break;
                if (rc == JOptionPane.CANCEL_OPTION)
                    return null;
            } else {
                String message = MessageFormat.format(kFileReadOnly,
                        new Object[] { file.getName() });
                JOptionPane.showMessageDialog(parent, message, ApplicationContext.getApplicationName(),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        
        FileFilter filter = chooser.getFileFilter();
        FileAndFilter selection = new FileAndFilter(file, filter);  
        return selection;
    }
    
	private static void changeDefaultExtension(JFileChooser chooser) {
		
		FileFilter filter = chooser.getFileFilter();
		if (filter instanceof ExtensionFileFilter) {
			ExtensionFileFilter extFilter = (ExtensionFileFilter)filter; 
			String newExt = extFilter.getExtension();
			
			File file = chooser.getSelectedFile(); 
			String oldName = file.getName();
			int idx = oldName.lastIndexOf('.'); 
			String newName = (idx == -1) ? oldName : oldName.substring(0, idx); 
			newName += "." + newExt;
			
			File newFile = new File(file.getParentFile(), newName);
			chooser.setSelectedFile(newFile);
		}		
	} //end changeDefaultExtension()

    /**
     * Show the panel popup component at a proper location. This method is used by the set of action
     * components using a popup menu as container and by some graphic tools.
     * 
     * This method check for horizontal bondaries to ensure that the popup will become entirely
     * visible.
     * 
     * @param activationComponent
     * @param popup
     */
    public static void showPopupPanel(Component activationComponent, JPopupMenu popup) {
        if (popup == null || activationComponent == null)
            return;
        Dimension dim = popup.getPreferredSize();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Point loc = activationComponent.getLocationOnScreen();
        int x = 0;
        if (loc.x + dim.width > screen.width) {
            x = activationComponent.getWidth() - dim.width; // transfert to the
            // anchor to south
            // east
        }
        popup.show(activationComponent, x, 0);
    }

    // This is a fix for JFileChooser (as of 1.4.0). The JFileChooser clear the
    // selected file (default file name)
    // when changing directory (even if directory selection is not allowed).
    private static class JFileChooserFix extends JFileChooser {
        JFileChooserFix(File currentDirectory) {
            super(currentDirectory);
        }

        public void setSelectedFile(File file) {
            File old = getSelectedFile();
            if (old == null && file == null)
                return;
            if (file == null && old != null) {
                String name = old.getName();
                super.setSelectedFile(new File(getCurrentDirectory(), name));
            } else {
                super.setSelectedFile(file);
            }
        }
    }

    /**
     * @return if the Diagram has been add in a java Frame container this method will return the
     *         Frame, or else it will return null.
     */
    public static Frame getParentFrame(Component comp) {
        Component frame;

        while ((frame = comp.getParent()) != null && !(frame instanceof Frame))
            comp = frame;
        return (Frame) frame;
    }

    public static void centerWindow(Window win) {
        // Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = win.getSize();
        if (frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        win.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
    }

    public static void rightAlignWindow(Window win) {
        // aligns the window on the right
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = win.getSize();
        if (frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        win.setLocation((screenSize.width - frameSize.width) / 1,
                (screenSize.height - frameSize.height) / 2);
    }

    // This method give the JButton a windows explorer buttons feeling
    public static void formatJButton(JButton button, String tooltips) {
        button.setToolTipText(tooltips);
    }

    // Create an innactive JButton with a separator look
    public static JButton createJButtonLineSeparator() {
        JButton button = new JButton(LocaleMgr.screen.getImageIcon("Separator"));
        button.setToolTipText(null);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPressedIcon(LocaleMgr.screen.getImageIcon("Separator"));
        button.setDisabledIcon(LocaleMgr.screen.getImageIcon("Separator"));
        button.setDisabledSelectedIcon(LocaleMgr.screen.getImageIcon("Separator"));
        button.setRolloverIcon(LocaleMgr.screen.getImageIcon("Separator"));
        button.setRolloverSelectedIcon(LocaleMgr.screen.getImageIcon("Separator"));
        button.setSelectedIcon(LocaleMgr.screen.getImageIcon("Separator"));
        button.setIcon(LocaleMgr.screen.getImageIcon("Separator"));
        button.setMaximumSize(new Dimension(8, button.getPreferredSize().height));
        button.setPreferredSize(new Dimension(8, button.getPreferredSize().height));
        button.setContentAreaFilled(false);
        button.setRolloverEnabled(false);
        button.setRequestFocusEnabled(false);
        return button;
    }

    // load an Image and create a Cursor with it
    public static Cursor loadCursor(Class loadFromThisClass, String imageName, Point hotspot,
            String cursorName, boolean addPlusSymbol) {
        Image image = GraphicUtil.loadImage(loadFromThisClass, imageName);
        if (image == null)
            return null;
        GraphicUtil.waitForImage(image);
        return createCursor(image, hotspot, cursorName, addPlusSymbol);
    }

    public static Cursor loadCursor(Class loadFromThisClass, String imageName, Point hotspot,
            String cursorName) {
        return loadCursor(loadFromThisClass, imageName, hotspot, cursorName, false);
    }

    public static Cursor createCursor(Image image, Point hotspot, String cursorName) {
        return createCursor(image, hotspot, cursorName, false);
    }

    public static Cursor createCursor(Image image, Point hotspot, String cursorName,
            boolean addPlusSymbol) {
        Cursor c = null;
        Toolkit tk = Toolkit.getDefaultToolkit();
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        Dimension curSize = tk.getBestCursorSize(width, height);
        if (curSize.width == 0) { // if 0, the java implementation doesn't
            // support custom cursor.
            return new Cursor(Cursor.DEFAULT_CURSOR);
        }
        int x = 0;
        int y = 0;
        if (width != curSize.width || height != curSize.height) {
            x = (curSize.width - width) / 2;
            y = (curSize.height - height) / 2;
        }
        BufferedImage bufImage = new BufferedImage(curSize.width, curSize.height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufImage.createGraphics();
        g.setColor(new Color(0, 0, 0, 0)); // Transparent
        g.fillRect(0, 0, curSize.width, curSize.height);
        g.drawImage(image, x, y, null);
        int delta = 1; // Todo ... could be used to set the location of the +
        if (addPlusSymbol && (curSize.width - width) > 7 && (curSize.width - width) > 7) {
            g.setColor(Color.black);
            g.drawRect(delta, delta, 8, 8);
            g.drawLine(delta + 4, delta + 2, delta + 4, delta + 6);
            g.drawLine(delta + 2, delta + 4, delta + 6, delta + 4);
        }
        g.dispose();
        image = bufImage;
        hotspot = new Point(x + hotspot.x, y + hotspot.y);
        try {
            c = tk.createCustomCursor(image, hotspot, cursorName);
        } catch (Exception e) {
            c = new Cursor(Cursor.DEFAULT_CURSOR);
            if (Debug.isDebug()) {
                StringWriter strout = new StringWriter();
                PrintWriter out = new PrintWriter(strout);
                e.printStackTrace(out);
                Debug.trace(strout.getBuffer()); // NOT LOCALIZABLE
                Debug.trace(image == null ? "Null image for cursor" : "Image Size: "
                        + image.getWidth(null) + ", " + image.getHeight(null)); // NOT LOCALIZABLE
                Debug.trace("Hotspot: " + hotspot); // NOT LOCALIZABLE
                Debug.trace("Occurred for cursor name: " + cursorName); // NOT
                // LOCALIZABLE
            }
        }
        return c;
    }

    /**
     * Normalization of the preferred size for all buttons in a specific panel
     * 
     * @deprecated (use normalizeComponentDimension);
     * @param jButtonList
     */
    public static void normalizeButtonDimension(JButton[] jButtonList) {
        normalizeComponentDimension(jButtonList, null);
    }

    /**
     * 
     * Normalization of the preferred size for all buttons in a specific panel
     * 
     * @deprecated (use normalizeComponentDimension);
     * @param jButtonList
     * @param jPossibleButtonList
     */
    public static void normalizeButtonDimension(JButton[] jButtonList, JButton[] jPossibleButtonList) {
        normalizeComponentDimension(jButtonList, jPossibleButtonList);
    }

    /**
     * Normalization of the size for all JComponent.
     * 
     * @param jButtonList
     */
    public static void normalizeComponentDimension(JComponent[] jButtonList) {
        normalizeComponentDimension(jButtonList, null);
    }

    /**
     * 
     * Normalization of the size for all JComponent.
     * 
     * @param jButtonList
     * @param jPossibleButtonList
     */
    public static void normalizeComponentDimension(JComponent[] jButtonList,
            JComponent[] jPossibleButtonList) {
        int i;
        int buttonWidth = 0;
        int buttonHeigth = 0;

        // We must set the preferred size to Null if we want change it more than
        // once
        for (i = 0; i < jButtonList.length; i++) {
            jButtonList[i].setPreferredSize(null);
        }

        // Calculating the maximum preferred size
        if (jPossibleButtonList == null) {
            jPossibleButtonList = jButtonList;
        }
        for (i = 0; i < jPossibleButtonList.length; i++) {
            buttonWidth = Math.max(buttonWidth, jPossibleButtonList[i].getPreferredSize().width);
            buttonHeigth = Math.max(buttonHeigth, jPossibleButtonList[i].getPreferredSize().height);
        }

        // Affectation of the same preferred size for all buttons
        buttonWidth = Math.min(buttonWidth, 100); // limit the width of small
        // buttons to 100 if there
        // are buttons larger than
        // 100
        for (i = 0; i < jButtonList.length; i++) {
            int width = Math.max(buttonWidth, jButtonList[i].getPreferredSize().width);
            Dimension size = new Dimension(width, buttonHeigth);
            jButtonList[i].setPreferredSize(size);
            jButtonList[i].setMinimumSize(size);
        }
    }

    public static int getStringWidth(String str) {
        JLabel label = new JLabel();
        int labelWidth = 0;

        if (str != null) {
            label.setText(str);
            FontMetrics fm = label.getFontMetrics(label.getFont());
            labelWidth = SwingUtilities.computeStringWidth(fm, str);
        }
        return labelWidth;

    }

    /**
     * return black or white depend on HSB parameters of iColor
     */
    public static Color getContrastBlackOrWhite(Color iColor) {

        float[] hsbVals = new float[3];
        float h, b;
        int red, green, blue;
        int hInt, bInt;

        red = iColor.getRed();
        green = iColor.getGreen();
        blue = iColor.getBlue();
        Color.RGBtoHSB(red, green, blue, hsbVals);
        h = hsbVals[0];
        b = hsbVals[2];
        bInt = (int) (b * 100);
        hInt = (int) (h * 100);
        if (bInt > 80) {
            if ((hInt > 63) && (hInt < 76)) {
                return Color.white;
            } else {
                return Color.black;
            }
        } else {
            return Color.white;
        }
    }

    public static Color brighter(Color color, float factor) {
        // Apply same logic as in Color
        if (color == null)
            return null;
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        int i = (int) (1.0 / (1.0 - factor));
        if (r == 0 && g == 0 && b == 0) {
            return Color.black;
        }
        if (r > 0 && r < i)
            r = i;
        if (g > 0 && g < i)
            g = i;
        if (b > 0 && b < i)
            b = i;

        return new Color(Math.min((int) (r / factor), 255), Math.min((int) (g / factor), 255), Math
                .min((int) (b / factor), 255));
    }

    public static Color darker(Color color, float factor) {
        // Apply same logic as in Color
        if (color == null)
            return null;
        return new Color(Math.max((int) (color.getRed() * factor), 0), Math.max((int) (color
                .getGreen() * factor), 0), Math.max((int) (color.getBlue() * factor), 0));
    }

    public static Color midColor(Color color1, Color color2) {
        if (color1 == null)
            return color2;
        if (color2 == null)
            return color1;
        return new Color((color1.getRed() + color2.getRed()) / 2, (color1.getGreen() + color2
                .getGreen()) / 2, (color1.getBlue() + color2.getBlue()) / 2);
    }

    public static Dimension getBestDialogSize() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        if (dim.width >= 1000 && dim.height >= 800) {
            return new Dimension(840, 600);
        }
        if (dim.width >= 800 && dim.height >= 600) {
            return new Dimension(700, 500);
        }
        return new Dimension(630, 450);
    }
    
    //
    // inner class
    //
    public static class FileAndFilter {
        private File _file;
        private FileFilter _filter;

        public FileAndFilter(File file, FileFilter filter) {
            _file = file;
            _filter = filter;
        }

        public File getFile() {
            return _file;
        }

        public FileFilter getFileFilter() {
            return _filter;
        }

    }

}
