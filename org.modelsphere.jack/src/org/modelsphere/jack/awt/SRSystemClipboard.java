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

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;

import org.modelsphere.jack.srtool.ApplicationContext;

public class SRSystemClipboard {
    private static String clipboardText;
    private static Image clipboardImage;

    private static WindowListener windowListener = new WindowAdapter() {

        @Override
        public void windowActivated(WindowEvent e) {
            updateFromSystemClipboard();
        }

    };

    static {
        Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(new FlavorListener() {

            @Override
            public void flavorsChanged(FlavorEvent e) {
                updateFromSystemClipboard();
            }
        });

        ApplicationContext.getDefaultMainFrame().addWindowListener(windowListener);

    }

    private static ArrayList<SRSystemClipboardListener> systemClipboardListeners = new ArrayList<SRSystemClipboardListener>();

    private static void fireSystemClipboardListeners() {
        if (systemClipboardListeners == null)
            return;
        for (int i = systemClipboardListeners.size(); --i >= 0;) {
            SRSystemClipboardListener listener = (SRSystemClipboardListener) systemClipboardListeners
                    .get(i);
            listener.systemClipboardContentTypeChanged();
        }
    }

    public static void addSystemClipboardListener(SRSystemClipboardListener l) {
        if (systemClipboardListeners.indexOf(l) == -1)
            systemClipboardListeners.add(l);
    }

    public static void removeSystemClipboardListener(SRSystemClipboardListener l) {
        systemClipboardListeners.remove(l);
    }

    public static boolean containsImage() {
        return containsFlavor(DataFlavor.imageFlavor);
    }

    public static boolean containsText() {
        return containsFlavor(DataFlavor.stringFlavor);
    }

    public static boolean containsFlavor(DataFlavor flavor) {
        if (flavor == DataFlavor.stringFlavor) {
            return clipboardText != null;
        }
        if (flavor == DataFlavor.imageFlavor) {
            return clipboardImage != null;
        }
        return false;
    }

    private static void updateFromSystemClipboard() {
        String newClipboardText = null;
        Image newClipboardImage = null;
        try {
            Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                newClipboardText = (String) t.getTransferData(DataFlavor.stringFlavor);
            }
            if (t != null && t.isDataFlavorSupported(ImageTransferable.imageFlavor)) {
                newClipboardImage = (Image) t.getTransferData(ImageTransferable.imageFlavor);
            }
            if (t != null && t.isDataFlavorSupported(ImageTransferable.javaFileListFlavor)) {
                @SuppressWarnings("unchecked")
                List<File> files = (List<File>) t
                        .getTransferData(ImageTransferable.javaFileListFlavor);
                newClipboardImage = null;
                newClipboardText = null;
                if (files != null && files.size() == 1) {
                    File file = files.get(0);
                    Image temp = ImageTransferable.read(file);
                    if (temp != null) {
                        newClipboardImage = temp;
                    }
                }
                if (newClipboardImage == null && files != null && files.size() > 0) {
                    StringBuilder text = new StringBuilder("");
                    for (int i = 0; i < files.size(); i++) {
                        File file = files.get(i);
                        if (file == null)
                            continue;
                        if (i > 0)
                            text.append("\n");
                        text.append(files.get(i).getPath());
                    }
                    newClipboardText = text.toString();
                }
            }
        } catch (Exception e) {
            newClipboardText = null;
            newClipboardImage = null;
        }

        if (newClipboardText != null && newClipboardText.length() == 0)
            newClipboardText = null;
        if (newClipboardText != null || clipboardText != null) {
            if ((newClipboardText == null ^ clipboardText == null)
                    || !newClipboardText.equals(clipboardText)) {
                clipboardText = newClipboardText;
                fireSystemClipboardListeners();
            }
        }
        if (newClipboardImage != clipboardImage) {
            clipboardImage = newClipboardImage;
            fireSystemClipboardListeners();
        }
    }

    /**
     * Note: Minimise calls to this method as getSystemClipboard().getContents(null) is very slow.
     * Use containsText() instead if the actual content is not required (for updating the state of a
     * paste action for example).
     * 
     * @return The text contained in the system clipboard.
     */
    public static String getClipboardText() {
        updateFromSystemClipboard();
        return clipboardText;
    }

    public static void setClipboardText(String text) {
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text),
                    null);
        } catch (Exception e1) {
            // see comments for setClipboardImage(Image image)
            try {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                        new StringSelection(text), null);
            } catch (Exception e2) {
            }
        }
        // ensure listeners are updated since we use the text flavor to clear the system clipboard 
        // when copying db objects which will not trigger a flavor content change if followed by a copy text operation.
        updateFromSystemClipboard();
    }

    /**
     * Note: Minimise calls to this method as getSystemClipboard().getContents(null) is very slow.
     * Use containsImage() instead if the actual content is not required (for updating the state of
     * a paste action for example).
     * 
     * @return The image contained in the system clipboard.
     */
    public static Image getClipboardImage() {
        updateFromSystemClipboard();
        return clipboardImage;
    }

    public static void setClipboardImage(Image image) {
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                    new ImageTransferable(image), null);
        } catch (Exception e1) {
            // Illegal state is thrown when alterning copy operations between 2 running instances.
            // This needs some investigation.  Bug from the VM?  (windows only??)
            // Note that the operation is not fully completed and that the clipboard content is only
            // available on the VM on which the copy has been performed.
            // retrying seems to solve the issue most of the time but not the best fix

            //e1.printStackTrace();
            try {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                        new ImageTransferable(image), null);
            } catch (Exception e2) {
                System.out.println("ds");
            }
        }
    }

    public static final void emptyClipboard() {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(""), null);
    }

    public static boolean transparentImageCopySupported() {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix("png");
        if (!writers.hasNext())
            return false;
        return true;
    }

    private static class ImageTransferable implements Transferable, ClipboardOwner {
        static DataFlavor imageFlavor = DataFlavor.imageFlavor;
        static DataFlavor javaFileListFlavor = DataFlavor.javaFileListFlavor;

        Image image;
        List<File> files;

        ImageTransferable(Image img) {
            this.image = img;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { imageFlavor, javaFileListFlavor };
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return imageFlavor.equals(flavor) || javaFileListFlavor.equals(flavor);
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException,
                IOException {
            if (!isDataFlavorSupported(flavor))
                throw new UnsupportedFlavorException(flavor);

            // TODO:  Need to investigate the lost of quality in the transfered image.  
            // It seems that a compression algo is applied to the image during its transfer to the native 
            // clipboard (jpeg using default compression ratio 0.75?).
            // For now we use a temporary file and copy the file as an additional flavor.  
            // This allows us to specify the quality for the image (if jpgs).
            // Images files seem to be supported for paste operations on most
            // applications supporting paste images.  If this cause problems, we can add a configurable flag to 
            // prevent transfer using files.  Using this method also allows the user to obtain a png or jpg (if
            // png not available) file from the selected objects by using the paste provided by the OS which copy the 
            // temporary file to the specified location.
            if (javaFileListFlavor.equals(flavor) && image instanceof RenderedImage) {
                if (files == null) {
                    files = new ArrayList<File>();
                    File file = copyToFile((RenderedImage) image, "png", 1f);
                    if (file == null) {
                        file = copyToFile((RenderedImage) image, "jpg", 1f);
                    }
                    if (file != null) {
                        files.add(file);
                    }
                }
                return files;
            } else if (imageFlavor.equals(flavor)) {
                return image;
            }
            return null;
        }

        private static File copyToFile(RenderedImage image, String format, float quality)
                throws IOException {
            File file = File.createTempFile("image_", "." + format);
            FileOutputStream out = null;
            boolean ok = false;
            try {
                out = new FileOutputStream(file);
                ok = write(image, format, quality, out);
            } finally {
                if (out != null)
                    out.close();
                if (!ok) {
                    file.delete();
                    file = null;
                } else {
                    file.deleteOnExit();
                }
            }
            return file;
        }

        public void lostOwnership(Clipboard clipboard, Transferable contents) {
        }

        private static boolean write(RenderedImage image, String format, float quality,
                OutputStream out) throws IOException {
            Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix(format);
            if (!writers.hasNext())
                return false;
            ImageWriter writer = writers.next();
            ImageOutputStream imageOut = null;
            imageOut = ImageIO.createImageOutputStream(out);

            writer.setOutput(imageOut);
            ImageWriteParam param = writer.getDefaultWriteParam();
            if (quality >= 0 && param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);
            }
            IIOImage ioImage = new IIOImage(image, null, null);
            writer.write(null, ioImage, param);
            return true;
        }

        private static Image read(File file) {
            if (file == null)
                return null;
            if (!ExtensionFileFilter.allImagesFilter.accept(file)) {
                return null;
            }
            Image image = null;
            try {
                image = Toolkit.getDefaultToolkit().getImage(file.getPath());
            } catch (Exception e) {
                // not an image
            }
            return image;
        }

    }

}
