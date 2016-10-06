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
import java.awt.print.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.text.ChangedCharSetException;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTML;
import javax.swing.text.html.parser.DTD;
import javax.swing.text.html.parser.Parser;
import javax.swing.text.html.parser.TagElement;

import org.modelsphere.jack.international.LocaleMgr;

public final class JTextComponentPrintManager implements Printable, Pageable, Runnable {

    private List printList;
    private List pages;
    private PageFormat curPageFormat;
    private PrinterJob printerJob;
    private int pageCount;
    private Font font = new Font("Monospaced", Font.PLAIN, 8); // NOT
    // LOCALIZABLE
    // // DO NOT
    // CHANGE THE
    // FONT (EVEN
    // SIZE, IT WONT
    // WORK
    // PROPERLY!

    private JTextComponent textComponent;
    private String tempText = "";

    private String jobTitle = null;

    public JTextComponentPrintManager(JTextComponent textComponent, String jobTitle) {
        if (textComponent == null)
            throw new IllegalArgumentException("null JTextComponent"); // NOT
        // LOCALIZABLE
        else {
            final String text = textComponent.getText();
            printList = new ArrayList();
            if ((textComponent instanceof JEditorPane)
                    && (((JEditorPane) textComponent).getContentType().indexOf("html") != 0)) { // NOT LOCALIZABLE
                StringReader reader = new StringReader(text);
                try {
                    DTD dtd = null;
                    dtd = DTD.getDTD("html32"); // NOT LOCALIZABLE
                    Parser parser = new Parser(dtd) {
                        protected void handleText(char text[]) {
                            tempText = tempText.concat(new String(text));
                        }

                        protected void handleEmptyTag(TagElement tag)
                                throws ChangedCharSetException {
                            HTML.Tag htmltag = tag.getHTMLTag();
                            if (htmltag.toString().toLowerCase().equals(
                                    HTML.Tag.BR.toString().toLowerCase())) {
                                tempText = tempText.concat("\n"); // NOT
                                // LOCALIZABLE
                            }
                        }

                        protected void handleEndTag(TagElement tag) {
                            HTML.Tag htmltag = tag.getHTMLTag();
                            if (htmltag.toString().toLowerCase().equals(
                                    HTML.Tag.TD.toString().toLowerCase())) {
                                tempText = tempText.concat(" "); // NOT
                                // LOCALIZABLE
                            } else if (htmltag.toString().toLowerCase().equals(
                                    HTML.Tag.TR.toString().toLowerCase())) {
                                tempText = tempText.concat("\n"); // NOT
                                // LOCALIZABLE
                            }
                        }
                    };
                    parser.parse(reader);
                    printList.add(tempText);
                } catch (Exception e) {
                    printList.add(text);
                }
            } else {
                printList.add(text);
            }
        }
        this.jobTitle = jobTitle;
    }

    private int calculatePageCount(PageFormat pf) {
        if (pf == null)
            pf = printerJob.defaultPage();
        if (pf != curPageFormat)
            pages = repaginate(pf, null);
        curPageFormat = pf;
        pageCount = pages.size();
        return pageCount;
    }

    public int getNumberOfPages() {
        return calculatePageCount(curPageFormat);
    }

    public PageFormat getPageFormat(int pageIndex) {
        return curPageFormat;
    }

    public Printable getPrintable(int pageIndex) {
        return this;
    }

    public void print() {
        new Thread(this, "Print").start(); // NOT LOCALIZABLE
    }

    public void run() {
        printerJob = PrinterJob.getPrinterJob();
        // printerJob.setPageable(this);
        printerJob.setPrintable(this);
        // PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        printerJob.setJobName(jobTitle == null ? LocaleMgr.misc.getString("untitled") : jobTitle);
        // if (printerJob.printDialog(attr))
        if (printerJob.printDialog())
            try {
                printerJob.print();

            } catch (PrinterException e) {
                long i = 0;
            }
    }

    public int print(Graphics g, PageFormat pf, int idx) throws PrinterException {
        g.setFont(font);
        g.setColor(Color.black);

        curPageFormat = pf;
        pages = repaginate(pf, g);

        if (pages == null || idx >= pages.size())
            return Printable.NO_SUCH_PAGE;

        renderPage(g, pf, idx);
        return Printable.PAGE_EXISTS;
    }

    private ArrayList repaginate(PageFormat pf, Graphics g) {
        if (pf == null || g == null) {
            return new ArrayList();
        }

        int maxh = (int) pf.getImageableHeight();
        int maxw = (int) pf.getImageableWidth();
        int lineh = font.getSize();
        ArrayList pgs = new ArrayList();
        Iterator it = printList.iterator();
        FontMetrics fm = g.getFontMetrics();

        while (it.hasNext())
            try {
                String item = it.next().toString();
                ArrayList page = new ArrayList();
                int pageh = 0;
                BufferedReader reader = new BufferedReader(new StringReader(item));
                String line;
                while ((line = reader.readLine()) != null) {
                    int beginIndex = 0;
                    do {
                        if (pageh + lineh > maxh) {
                            pgs.add(page);
                            page = new ArrayList();
                            pageh = 0;
                        }
                        int endIndex = getStringEndIndex(fm, line, beginIndex, maxw);
                        page.add(line.substring(beginIndex, endIndex));
                        beginIndex = endIndex;
                        pageh += lineh;
                    } while (beginIndex < line.length());
                }
                pgs.add(page);

            } catch (IOException e) {
            }

        return pgs;
    }

    private int getStringEndIndex(FontMetrics fm, String str, int beginIndex, int widthLine) {
        if (fm.stringWidth(str.substring(beginIndex)) <= widthLine)
            return str.length();

        int low = 1; // at least one char.
        int high = str.length() - beginIndex;
        int middle;
        while (low + 1 < high) {
            middle = (low + high) / 2;
            if (fm.stringWidth(str.substring(beginIndex, beginIndex + middle)) > widthLine)
                high = middle;
            else
                low = middle;
        }
        return low + beginIndex;
    }

    private void renderPage(Graphics g, PageFormat pf, int idx) {
        int xo = (int) pf.getImageableX();
        int yo = (int) pf.getImageableY();
        int y = font.getSize();
        ArrayList page = (ArrayList) pages.get(idx);
        Iterator iter = page.iterator();
        while (iter.hasNext()) {
            String line = (String) iter.next();
            if (line != null && line.length() > 0) // To avoid a bug in 1.2.x
                g.drawString(line, xo, y + yo);
            y += font.getSize();
        }
    }
}
