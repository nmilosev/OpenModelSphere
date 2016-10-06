/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.plugins.report;

// Jack

// Sms

// JDK
import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.text.html.HTML;

import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.tree.CheckTreeModel;
import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.io.IoUtil;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.plugins.report.model.*;

public class HtmlGenerator {

    private static final String TAB = "    "; //4 spaces use as TAB   NOT LOCALIZABLE, property key
    private static final String htmlExtension = "."
            + ExtensionFileFilter.htmlFileFilter.getExtension(); // NOT LOCALIZABLE
    private static final String jpegExtension = "."
            + ExtensionFileFilter.jpgFileFilter.getExtension(); // NOT LOCALIZABLE

    // HTML Attributes
    private static String WIDTH = HTML.Attribute.WIDTH.toString().toUpperCase(); // NOT LOCALIZABLE
    private static String HEIGHT = HTML.Attribute.HEIGHT.toString().toUpperCase(); // NOT LOCALIZABLE
    private static String BORDER = HTML.Attribute.BORDER.toString().toUpperCase(); // NOT LOCALIZABLE
    private static String BORDERCOLOR = "BORDERCOLOR"; // NOT LOCALIZABLE
    private static String BGCOLOR = HTML.Attribute.BGCOLOR.toString().toUpperCase(); // NOT LOCALIZABLE
    private static String ALIGN = HTML.Attribute.ALIGN.toString().toUpperCase(); // NOT LOCALIZABLE
    private static String VALIGN = HTML.Attribute.VALIGN.toString().toUpperCase(); // NOT LOCALIZABLE
    private static String NOWRAP = HTML.Attribute.NOWRAP.toString().toUpperCase(); // NOT LOCALIZABLE
    private static String CELLPADDING = HTML.Attribute.CELLPADDING.toString().toUpperCase(); // NOT LOCALIZABLE
    private static String CELLSPACING = HTML.Attribute.CELLSPACING.toString().toUpperCase(); // NOT LOCALIZABLE
    private static String NAME = HTML.Attribute.NAME.toString().toUpperCase(); // NOT LOCALIZABLE
    private static String FRAMEBORDER = HTML.Attribute.FRAMEBORDER.toString().toUpperCase(); // NOT LOCALIZABLE
    private static String COLS = HTML.Attribute.COLS.toString().toUpperCase(); // NOT LOCALIZABLE
    private static String SRC = HTML.Attribute.SRC.toString().toUpperCase(); // NOT LOCALIZABLE
    private static String HSPACE = HTML.Attribute.HSPACE.toString().toUpperCase(); // NOT LOCALIZABLE
    private static String VSPACE = HTML.Attribute.VSPACE.toString().toUpperCase(); // NOT LOCALIZABLE

    // special characters
    private static String LFCR = "\r\n"; // NOT LOCALIZABLE
    private static String LT = "<"; // NOT LOCALIZABLE
    private static String GT = ">"; // NOT LOCALIZABLE
    private static String SPACE = " "; // NOT LOCALIZABLE
    private static String QUOTE = "\""; // NOT LOCALIZABLE
    private static String EQUAL = "="; // NOT LOCALIZABLE

    // general HTML tags
    private static String htmlOpening = LT + "HTML" + GT + LFCR; // NOT LOCALIZABLE
    private static String htmlClosing = LT + "/HTML" + GT + LFCR; // NOT LOCALIZABLE
    private static String framesetOpening = "FRAMESET"; // NOT LOCALIZABLE
    private static String framesetClosing = "/FRAMESET"; // NOT LOCALIZABLE
    private static String frameOpening = "FRAME"; // NOT LOCALIZABLE
    private static String frameClosing = "/FRAME"; // NOT LOCALIZABLE
    private static String headerOpening = LT + "HEADER" + GT + LFCR; // NOT LOCALIZABLE
    private static String headerClosing = LT + "/HEADER" + GT + LFCR; // NOT LOCALIZABLE
    private static String titleOpening = LT + "TITLE" + GT + LFCR; // NOT LOCALIZABLE
    private static String titleClosing = LT + "/TITLE" + GT + LFCR; // NOT LOCALIZABLE
    private static String bodyOpening = LT + "BODY" + GT + LFCR; // NOT LOCALIZABLE
    private static String bodyClosing = LT + "/BODY" + GT + LFCR; // NOT LOCALIZABLE
    private static String centerOpening = LT + "CENTER" + GT + LFCR; // NOT LOCALIZABLE
    private static String centerClosing = LT + "/CENTER" + GT + LFCR; // NOT LOCALIZABLE
    private static String h1Opening = LT + "H1" + GT + LFCR; // NOT LOCALIZABLE
    private static String h1Closing = LT + "/H1" + GT + LFCR; // NOT LOCALIZABLE
    private static String unorderedListOpening = LT + "UL" + GT + LFCR; // NOT LOCALIZABLE
    private static String unorderedListClosing = LT + "/UL" + GT + LFCR; // NOT LOCALIZABLE
    private static String listItemOpening = LT + "LI" + GT + LFCR; // NOT LOCALIZABLE
    private static String listItemClosing = LT + "/LI" + GT + LFCR; // NOT LOCALIZABLE
    private static String boldOpening = LT + "B" + GT + LFCR; // NOT LOCALIZABLE
    private static String boldClosing = LT + "/B" + GT + LFCR; // NOT LOCALIZABLE
    private static String breakLine = LT + "BR" + GT + LFCR; // NOT LOCALIZABLE
    private static String hRule = LT + "HR" + GT + LFCR; // NOT LOCALIZABLE
    private static String linkOpening = LT + "A HREF=\"{0}#{1}\" TARGET=\"{2}\"" + GT + LFCR; // NOT LOCALIZABLE
    private static String linkClosing = LT + "/A" + GT + LFCR; // NOT LOCALIZABLE
    private static String anchorOpening = LT + "A NAME=\"{0}\"" + GT + LFCR; // NOT LOCALIZABLE
    private static String anchorClosing = linkClosing; // NOT LOCALIZABLE
    private static String imageOpening = LT + "IMG SRC=\"{0}\"" + GT + LFCR; // NOT LOCALIZABLE
    private static String imageClosing = ""; // NOT LOCALIZABLE
    private static String fontOpening = LT + "FONT COLOR=\"{0}\"" + GT + LFCR; // NOT LOCALIZABLE
    private static String fontClosing = LT + "/FONT" + GT + LFCR; // NOT LOCALIZABLE
    private static String divOpening = LT + "DIV ALIGN=\"{0}\"" + GT + LFCR; // NOT LOCALIZABLE
    private static String divClosing = LT + "/DIV " + GT + LFCR; // NOT LOCALIZABLE

    // Table related tags
    private static String tableOpening = "TABLE"; // NOT LOCALIZABLE
    private static String tableClosing = "/TABLE"; // NOT LOCALIZABLE
    private static String tableHeaderCellOpening = "TH"; // NOT LOCALIZABLE
    private static String tableHeaderCellClosing = "/TH"; // NOT LOCALIZABLE
    private static String tableCellOpening = "TD"; // NOT LOCALIZABLE
    private static String tableCellClosing = "/TD"; // NOT LOCALIZABLE
    private static String tableRowOpening = LT + "TR" + GT + LFCR; // NOT LOCALIZABLE
    private static String tableRowClosing = LT + "/TR" + GT + LFCR; // NOT LOCALIZABLE
    private static String emptyCell = "&nbsp;"; // NOT LOCALIZABLE

    private static String NO_NAME = LocaleMgr.misc.getString("noName");// NOT LOCALIZABLE

    private ReportModel model;
    private String htmlDirectory;
    private Controller controller;

    public HtmlGenerator(ReportModel model, Controller controller) throws IOException, DbException {
        this.model = model;
        this.controller = controller;
        htmlDirectory = model.getOptions().getOutputDirectory();

        CheckTreeModel treeModel = model.getTreeReprentation();
        CheckTreeNode root = (CheckTreeNode) treeModel.getRoot();
        CheckTreeNode node;

        generateMainFile(model);

        Enumeration enumeration = root.children();
        while (enumeration.hasMoreElements() && controller.checkPoint()) {
            node = (CheckTreeNode) enumeration.nextElement();
            if (node.isSelected()) {
                generateHtmlDocument(node, model);
            }
        }
    }

    private void generateHtmlDocument(CheckTreeNode node, ReportModel model) throws IOException,
            DbException {
        ConceptProperties conceptProperties = (ConceptProperties) node.getUserObject();
        FileOutputStream outputStream;

        String fileName = conceptProperties.getProperty(ConceptProperties.TABLE_GENERAL_GROUP,
                ConceptProperties.FILENAME_PROPERTY_KEY).getValue().toString();
        String title = conceptProperties.getProperty(ConceptProperties.TABLE_GENERAL_GROUP,
                ConceptProperties.TITLE_PROPERTY_KEY).getValue().toString();

        try {
            outputStream = new FileOutputStream(new File(htmlDirectory, fileName));
        } catch (FileNotFoundException ex) {
            String errorMsg = MessageFormat.format(LocaleMgr.misc.getString("InvalidFileNameIn0"),
                    new Object[] { node.toString() });
            throw new IOException(errorMsg); // NOT LOCALIZABLE
        }

        // <HTML>
        outputStream.write(htmlOpening.getBytes());
        writeDocumentHeader(outputStream, title);
        // <BODY>
        //outputStream.write(bodyOpening.getBytes());
        outputStream.write(new String(LT + "BODY").getBytes()); // NOT LOCALIZABLE
        String backgroundImage = getBackgroundImage(htmlDirectory);
        if (backgroundImage != null)
            outputStream.write(new String(" BACKGROUND=\"" + backgroundImage + "\"").getBytes()); // NOT LOCALIZABLE
        outputStream.write(GT.getBytes());

        // title
        writeDocumentTitle(outputStream, title);

        // properties table
        //generateHtmlTable(outputStream, node);
        generateHtmlDocumentContent(outputStream, node);

        // end of the document
        // </BODY>
        outputStream.write(bodyClosing.getBytes());
        // </HTML>
        outputStream.write(htmlClosing.getBytes());

        outputStream.close();
        controller.println(TAB + htmlDirectory + File.separator + fileName);
    }

    private String getBackgroundImage(String OutputDirectory) {
        if (model.getOptions().getUseBackgroundImage()
                && model.getOptions().getBackgroundImage().length() > 0) {
            String fileName = model.getOptions().getBackgroundImage();
            File file = new File(fileName);
            if (!file.canRead())
                return null;
            String destFileName = OutputDirectory + File.separator + file.getName();
            try {
                IoUtil.copyFile(file, new File(destFileName), true, true);
                controller.println(TAB + file.getName());
            } catch (IOException ex) {
                controller.println(ex.getMessage());
                controller.incrementErrorsCounter();
                return null;
            }
            return file.getName();
        } else
            return null;
    }

    private void generateHtmlDocumentContent(OutputStream o, CheckTreeNode node)
            throws DbException, IOException {
        org.modelsphere.sms.plugins.report.model.Properties properties = (org.modelsphere.sms.plugins.report.model.Properties) node
                .getUserObject();

        ConceptProperties conceptProperties = (ConceptProperties) properties;
        ArrayList occurences = conceptProperties.getConcept().getOccurences();

        Iterator x = occurences.iterator();
        while (x.hasNext()) {
            DbObject occurence = (DbObject) x.next();
            TableAlignmentDomain alignmentValue = (TableAlignmentDomain) conceptProperties
                    .getProperty(ConceptProperties.TABLE_GENERAL_GROUP,
                            ConceptProperties.ALIGNMENT_PROPERTY_KEY).getValue();
            if (alignmentValue != null
                    && !alignmentValue.equals(TableAlignmentDomain
                            .getInstance(TableAlignmentDomain.NULL))) {
                String div = MessageFormat.format(divOpening, new Object[] { alignmentValue
                        .getStringValue() });
                o.write(div.getBytes());
            }
            o.write(LT.getBytes());
            o.write(tableOpening.getBytes());
            o.write(GT.getBytes());
            o.write(LFCR.getBytes());
            o.write(tableRowOpening.getBytes());
            o.write(LT.getBytes());
            o.write(tableCellOpening.getBytes());
            o.write(GT.getBytes());

            String anchor = MessageFormat.format(anchorOpening, new Object[] { occurence
                    .getComposite().getName()
                    + "_" + occurence.getName() }); // NOT LOCALIZABLE
            o.write(anchor.getBytes());
            writeTableTitle(o, LocaleMgr.misc.getString("Properties")); // NOT LOCALIZABLE
            o.write(anchorClosing.getBytes());

            o.write(tableRowClosing.getBytes());
            o.write(LT.getBytes());
            o.write(tableCellClosing.getBytes());
            o.write(GT.getBytes());

            o.write(tableRowOpening.getBytes());
            o.write(LT.getBytes());
            o.write(tableCellOpening.getBytes());
            o.write(GT.getBytes());

            if (DbSMSDiagram.metaClass.isAssignableFrom(conceptProperties.getConcept()
                    .getMetaClass()))
                generateDiagramPropretiesTable(o, node, occurence);
            else
                generateHtmlPropertiesTable(o, node, occurence);

            o.write(tableRowClosing.getBytes());
            o.write(LT.getBytes());
            o.write(tableCellClosing.getBytes());
            o.write(GT.getBytes());
            o.write(LT.getBytes());
            o.write(tableClosing.getBytes());
            o.write(GT.getBytes());
            if (alignmentValue != null
                    && !alignmentValue.equals(TableAlignmentDomain
                            .getInstance(TableAlignmentDomain.NULL))) {
                o.write(divClosing.getBytes());
            }
            o.write(breakLine.getBytes());
            o.write(breakLine.getBytes());

            Enumeration enumeration = node.children();
            while (enumeration.hasMoreElements()) {
                CheckTreeNode child = (CheckTreeNode) enumeration.nextElement();
                if ((child.getUserObject() instanceof ConceptComponentProperties)
                        && child.isSelected()) {
                    ConceptComponentProperties componentProperties = (ConceptComponentProperties) child
                            .getUserObject();
                    TableAlignmentDomain conceptComponentalignmentValue = (TableAlignmentDomain) componentProperties
                            .getProperty(ConceptComponentProperties.TABLE_DIMENSIONS_GROUP,
                                    ConceptComponentProperties.ALIGNMENT_PROPERTY_KEY).getValue();
                    if (conceptComponentalignmentValue != null
                            && !conceptComponentalignmentValue.equals(TableAlignmentDomain
                                    .getInstance(TableAlignmentDomain.NULL))) {
                        String div = MessageFormat.format(divOpening,
                                new Object[] { conceptComponentalignmentValue.getStringValue() });
                        o.write(div.getBytes());
                    }
                    o.write(LT.getBytes());
                    o.write(tableOpening.getBytes());
                    o.write(GT.getBytes());
                    o.write(LFCR.getBytes());
                    o.write(tableRowOpening.getBytes());
                    o.write(LT.getBytes());
                    o.write(tableCellOpening.getBytes());
                    o.write(GT.getBytes());

                    writeTableTitle(o, child.toString());

                    o.write(tableRowClosing.getBytes());
                    o.write(LT.getBytes());
                    o.write(tableCellClosing.getBytes());
                    o.write(GT.getBytes());

                    o.write(tableRowOpening.getBytes());
                    o.write(LT.getBytes());
                    o.write(tableCellOpening.getBytes());
                    o.write(GT.getBytes());

                    generateHtmlComponentTable(o, child, occurence);

                    o.write(tableRowClosing.getBytes());
                    o.write(LT.getBytes());
                    o.write(tableCellClosing.getBytes());
                    o.write(GT.getBytes());
                    o.write(LT.getBytes());
                    o.write(tableClosing.getBytes());
                    o.write(GT.getBytes());
                    if (alignmentValue != null
                            && !alignmentValue.equals(TableAlignmentDomain
                                    .getInstance(TableAlignmentDomain.NULL))) {
                        o.write(divClosing.getBytes());
                    }

                    if (enumeration.hasMoreElements()) {
                        o.write(breakLine.getBytes());
                        o.write(breakLine.getBytes());
                    }
                }
            }

            if (x.hasNext())
                o.write(hRule.getBytes());
        }
    }

    private void generateHtmlPropertiesTable(OutputStream o, CheckTreeNode node, DbObject occurence)
            throws DbException, IOException {
        ConceptProperties conceptProperties = (ConceptProperties) node.getUserObject();
        //ArrayList         occurences = conceptProperties.getConcept().getOccurences();
        ArrayList fields = new ArrayList();

        // init fields
        Enumeration enumeration = node.children();
        while (enumeration.hasMoreElements()) {
            CheckTreeNode child = (CheckTreeNode) enumeration.nextElement();
            if (child.isSelected() && (child.getUserObject() instanceof ConceptAttributeProperties))
                fields.add(child.getUserObject());
        }
        // <TABLE>
        o.write(LT.getBytes());
        o.write(tableOpening.getBytes());
        writeTableAttributes(o, conceptProperties);
        o.write(GT.getBytes());
        o.write(LFCR.getBytes());

        generateHtmlPropertiesTableHeader(o, node);

        Iterator x = fields.iterator();
        while (x.hasNext()) {
            // <TR>
            o.write(tableRowOpening.getBytes());

            ConceptAttributeProperties fieldProperties = (ConceptAttributeProperties) x.next();

            // <TD>
            o.write(LT.getBytes());
            o.write(tableCellOpening.getBytes());
            // attributes
            //writeTableHeaderAttributes(o, conceptProperties);
            writeTableCellAttributes(o, fieldProperties);
            o.write(GT.getBytes());
            Color textColor = (Color) fieldProperties.getProperty(
                    ConceptAttributeProperties.COLUMN_COLORS_GROUP,
                    ConceptAttributeProperties.TEXT_PROPERTY_KEY).getValue();
            //o.write(fieldProperties.toString().getBytes());
            writeText(o, fieldProperties.toString(), textColor);
            // </TD>
            o.write(new String(LT + tableCellClosing + GT).getBytes());

            // <TD>
            o.write(LT.getBytes());
            o.write(tableCellOpening.getBytes());
            // attributes
            //writeTableHeaderAttributes(o, conceptProperties);
            writeTableCellAttributes(o, fieldProperties);
            o.write(GT.getBytes());

            Object value = occurence.get(fieldProperties.getMetaField());
            if (value != null) {
                //o.write(value.toString().getBytes());
                //writeValue(o, value);
                writeText(o, value, textColor);
            } else
                o.write(emptyCell.getBytes());

            // </TD>
            o.write(new String(LT + tableCellClosing + GT).getBytes());

            // </TR>
            o.write(tableRowClosing.getBytes());
        }

        // </TABLE>
        o.write(LT.getBytes());
        o.write(tableClosing.getBytes());
        o.write(GT.getBytes());
        o.write(LFCR.getBytes());
    }

    private void generateHtmlPropertiesTableHeader(OutputStream o, CheckTreeNode node)
            throws IOException, DbException {
        ConceptProperties conceptProperties = (ConceptProperties) node.getUserObject();

        // <TR>
        o.write(tableRowOpening.getBytes());

        // *************************************************************************
        // Property
        // *************************************************************************
        o.write(LT.getBytes());
        o.write(tableHeaderCellOpening.getBytes());
        // attributes
        writeTableHeaderAttributes(o, conceptProperties);
        o.write(new String(" " + WIDTH + "=\"50%\"").getBytes()); // NOT LOCALIZABLE
        o.write(GT.getBytes());

        //o.write(LocaleMgr.misc.getString("Property").getBytes());

        Color textColor = (Color) conceptProperties.getProperty(
                ConceptProperties.TABLE_COLORS_GROUP, ConceptProperties.HEADERTEXT_PROPERTY_KEY)
                .getValue();
        writeText(o, LocaleMgr.misc.getString("Property"), textColor); // NOT LOCALIZABLE

        // </TH>
        o.write(new String(LT + tableHeaderCellClosing + GT).getBytes());

        // *************************************************************************
        // Value
        // *************************************************************************
        o.write(LT.getBytes());
        o.write(tableHeaderCellOpening.getBytes());
        // attributes
        writeTableHeaderAttributes(o, conceptProperties);
        o.write(new String(" " + WIDTH + "=\"50%\"").getBytes()); // NOT LOCALIZABLE
        o.write(GT.getBytes());

        //o.write(LocaleMgr.misc.getString("Value").getBytes());
        writeText(o, LocaleMgr.misc.getString("Value"), textColor); // NOT LOCALIZABLE

        // </TH>
        o.write(new String(LT + tableHeaderCellClosing + GT).getBytes());

        // </TR>
        o.write(tableRowClosing.getBytes());

    }

    public void writeTableAttributes(OutputStream o, ConceptProperties conceptProperties)
            throws IOException, DbException {
        Object borderColorValue = conceptProperties.getProperty(
                ConceptProperties.TABLE_COLORS_GROUP, ConceptProperties.BORDERCOLOR_PROPERTY_KEY)
                .getValue();
        writeStringAttribute(o, BORDERCOLOR, borderColorValue);

        Object borderValue = conceptProperties.getProperty(
                ConceptProperties.TABLE_DIMENSIONS_GROUP, ConceptProperties.BORDER_PROPERTY_KEY)
                .getValue();
        writeStringAttribute(o, BORDER, borderValue);

        Object widthValue = conceptProperties.getProperty(ConceptProperties.TABLE_DIMENSIONS_GROUP,
                ConceptProperties.WIDTH_PROPERTY_KEY).getValue();
        UnitDomain widthUnit = (UnitDomain) conceptProperties.getProperty(
                ConceptProperties.TABLE_DIMENSIONS_GROUP, ConceptProperties.WIDTHUNIT_PROPERTY_KEY)
                .getValue();
        writeStringAttribute(o, WIDTH, widthValue, widthUnit);
        /*
         * Object heightValue =
         * conceptProperties.getProperty(conceptProperties.TABLE_DIMENSIONS_GROUP,
         * conceptProperties.HEIGHT_PROPERTY_KEY).getValue(); UnitDomain heightUnit =
         * (UnitDomain)conceptProperties.getProperty(conceptProperties.TABLE_DIMENSIONS_GROUP,
         * conceptProperties.HEIGHTUNIT_PROPERTY_KEY).getValue(); writeStringAttribute(o, HEIGHT,
         * heightValue, heightUnit);
         */
        Object cellPaddingValue = conceptProperties.getProperty(
                ConceptProperties.TABLE_DIMENSIONS_GROUP,
                ConceptProperties.CELLPADDING_PROPERTY_KEY).getValue();
        writeStringAttribute(o, CELLPADDING, cellPaddingValue);

        Object cellSpacingValue = conceptProperties.getProperty(
                ConceptProperties.TABLE_DIMENSIONS_GROUP,
                ConceptProperties.CELLSPACING_PROPERTY_KEY).getValue();
        writeStringAttribute(o, CELLSPACING, cellSpacingValue);

        Object hMarginValue = conceptProperties.getProperty(
                ConceptProperties.TABLE_DIMENSIONS_GROUP,
                ConceptProperties.HORIZONTALMARGIN_PROPERTY_KEY).getValue();
        writeStringAttribute(o, HSPACE, hMarginValue);

        Object vMarginValue = conceptProperties.getProperty(
                ConceptProperties.TABLE_DIMENSIONS_GROUP,
                ConceptProperties.VERTICALMARGIN_PROPERTY_KEY).getValue();
        writeStringAttribute(o, VSPACE, vMarginValue);
    }

    public void writeTableHeaderAttributes(OutputStream o, ConceptProperties conceptProperties)
            throws IOException, DbException {
        Object headerColorValue = conceptProperties.getProperty(
                ConceptProperties.TABLE_COLORS_GROUP,
                ConceptProperties.HEADERBACKGROUND_PROPERTY_KEY).getValue();
        writeStringAttribute(o, BGCOLOR, headerColorValue);
    }

    /*
     * public void writeTableHeaderCellAttributes(OutputStream o, ConceptAttributeProperties
     * conceptAttributeProperties) throws IOException, DbException { Object widthValue =
     * conceptAttributeProperties.getProperty(conceptAttributeProperties.COLUMN_DIMENSIONS_GROUP,
     * conceptAttributeProperties.WIDTH_PROPERTY_KEY).getValue(); writeStringAttribute(o, WIDTH,
     * widthValue);
     * 
     * Object heightValue =
     * conceptAttributeProperties.getProperty(conceptAttributeProperties.COLUMN_DIMENSIONS_GROUP,
     * conceptAttributeProperties.HEIGHT_PROPERTY_KEY).getValue(); writeStringAttribute(o, HEIGHT,
     * heightValue); }
     */

    public void writeTableCellAttributes(OutputStream o,
            ConceptAttributeProperties conceptAttributeProperties) throws IOException, DbException {

        Object heightValue = conceptAttributeProperties.getProperty(
                ConceptAttributeProperties.COLUMN_DIMENSIONS_GROUP,
                ConceptAttributeProperties.HEIGHT_PROPERTY_KEY).getValue();
        UnitDomain heightUnit = (UnitDomain) conceptAttributeProperties.getProperty(
                ConceptAttributeProperties.COLUMN_DIMENSIONS_GROUP,
                ConceptAttributeProperties.HEIGHTUNIT_PROPERTY_KEY).getValue();
        writeStringAttribute(o, HEIGHT, heightValue, heightUnit);

        Object alignValue = conceptAttributeProperties.getProperty(
                ConceptAttributeProperties.COLUMN_ALIGNMENT_GROUP,
                ConceptAttributeProperties.HORIZONTAL_PROPERTY_KEY).getValue();
        writeStringAttribute(o, ALIGN, alignValue);

        Object valignValue = conceptAttributeProperties.getProperty(
                ConceptAttributeProperties.COLUMN_ALIGNMENT_GROUP,
                ConceptAttributeProperties.VERTICAL_PROPERTY_KEY).getValue();
        writeStringAttribute(o, VALIGN, valignValue);

        Object backgroundValue = conceptAttributeProperties.getProperty(
                ConceptAttributeProperties.COLUMN_COLORS_GROUP,
                ConceptAttributeProperties.BACKGROUND_PROPERTY_KEY).getValue();
        writeStringAttribute(o, BGCOLOR, backgroundValue);

        Boolean noWrapValue = (Boolean) conceptAttributeProperties.getProperty(
                ConceptAttributeProperties.COLUMN_GENERAL_GROUP,
                ConceptAttributeProperties.NOWRAP_PROPERTY_KEY).getValue();
        writeBooleanAttribute(o, NOWRAP, noWrapValue);
    }

    public void writeValue(OutputStream o, Object value) throws IOException, DbException {
        if (value instanceof Color) {
            Color color = (Color) value;
            String str = "#"; // NOT LOCALIZABLE
            String red = Integer.toHexString(color.getRed()).toUpperCase();
            String green = Integer.toHexString(color.getGreen()).toUpperCase();
            String blue = Integer.toHexString(color.getBlue()).toUpperCase();

            // if hexadecimal value is only one digit, we add a zero on the left
            str = str + (red.length() == 1 ? "0" + red : red)
                    + (green.length() == 1 ? "0" + green : green)
                    + (blue.length() == 1 ? "0" + blue : blue); // NOT LOCALIZABLE

            o.write(str.getBytes());
        } else if (value instanceof DbObject) {
            DbObject dbo = (DbObject) value;
            CheckTreeNode node = model.getConceptTreeNodeFromName(dbo.getMetaClass().toString());
            String fileName;

            if ((node != null) && node.isSelected()) {
                if (node.getUserObject() instanceof ConceptProperties) {
                    ConceptProperties conceptProperties = (ConceptProperties) node.getUserObject();
                    fileName = conceptProperties.getProperty(ConceptProperties.TABLE_GENERAL_GROUP,
                            ConceptProperties.FILENAME_PROPERTY_KEY).getValue().toString();
                } else
                    fileName = node.toString() + htmlExtension;

                String compositeName = dbo.getComposite().getName() != null ? dbo.getComposite()
                        .getName() : NO_NAME;
                String link = MessageFormat.format(linkOpening, new Object[] { fileName,
                        dbo.getComposite().getName() + "_" + dbo.getName(), mainFrame }); // NOT LOCALIZABLE

                o.write(link.getBytes());
                String dboName = dbo.getName() != null ? dbo.getName() : NO_NAME;
                o.write(dboName.getBytes());
                o.write(linkClosing.getBytes());
            } else {
                String dboName = dbo.getName() != null ? dbo.getName() : NO_NAME;
                o.write(dboName.getBytes());
            }

        } else if (value instanceof UnitDomain) {
            UnitDomain domain = (UnitDomain) value;
            if (UnitDomain.getInstance(UnitDomain.PERCENT).equals(domain))
                o.write(new String("%").getBytes());
        } else if (value instanceof ColumnAlignmentDomain) {
            o.write(((ColumnAlignmentDomain) value).getStringValue().getBytes());
        } else if (value instanceof ColumnHorizontalAlignmentDomain) {
            o.write(((ColumnHorizontalAlignmentDomain) value).getStringValue().getBytes());
        } else if (value instanceof ColumnVerticalAlignmentDomain) {
            o.write(((ColumnVerticalAlignmentDomain) value).getStringValue().getBytes());
        } else if (value instanceof Font) {
            o.write(((Font) value).getName().getBytes());
        } else {
            o.write(value.toString().getBytes());
        }
    }

    public void writeText(OutputStream o, Object text, Color color) throws IOException, DbException {
        String colorString = "#"; // NOT LOCALIZABLE
        String red = Integer.toHexString(color.getRed()).toUpperCase();
        String green = Integer.toHexString(color.getGreen()).toUpperCase();
        String blue = Integer.toHexString(color.getBlue()).toUpperCase();

        colorString = colorString + (red.length() == 1 ? "0" + red : red)
                + (green.length() == 1 ? "0" + green : green)
                + (blue.length() == 1 ? "0" + blue : blue); // NOT LOCALIZABLE

        String tagOpening = MessageFormat.format(fontOpening, new Object[] { colorString });
        o.write(tagOpening.getBytes());
        //o.write(text.getBytes());
        writeValue(o, text);
        o.write(fontClosing.getBytes());

    }

    public void writeAttributes(OutputStream o, Property[] properties) throws IOException {
        for (int i = 0; i < properties.length; i++) {
            o.write(SPACE.getBytes());
            o.write(SPACE.getBytes());
        }
    }

    public void writeDocumentHeader(OutputStream o, String title) throws IOException {
        // <HEADER>
        o.write(headerOpening.getBytes());
        // <TITLE>
        o.write(titleOpening.getBytes());

        o.write(title.getBytes());

        // </TITLE>
        o.write(titleClosing.getBytes());
        // </HEADER>
        o.write(headerClosing.getBytes());
    }

    public void writeDocumentTitle(OutputStream o, String title) throws IOException {
        // <CENTER>
        o.write(centerOpening.getBytes());
        // <H1>
        o.write(h1Opening.getBytes());

        o.write(title.getBytes());

        // </H1>
        o.write(h1Closing.getBytes());
        // </CENTER>
        o.write(centerClosing.getBytes());
    }

    /*
     * public void writeListItem(OutputStream o, String item) throws IOException {
     * o.write(unorderedListOpening.getBytes()); o.write(listItemOpening.getBytes());
     * o.write(item.getBytes()); o.write(breakLine.getBytes()); o.write(listItemClosing.getBytes());
     * o.write(unorderedListClosing.getBytes()); }
     */

    public void writeTableTitle(OutputStream o, String item) throws IOException {
        o.write(boldOpening.getBytes());
        o.write(item.getBytes());
        o.write(boldClosing.getBytes());
        o.write(breakLine.getBytes());
    }

    private void generateHtmlComponentTable(OutputStream o, CheckTreeNode node, DbObject occurence)
            throws DbException, IOException {
        //ConceptProperties conceptProperties = (ConceptProperties)node.getUserObject();
        ConceptComponentProperties componentProperties = (ConceptComponentProperties) node
                .getUserObject();
        //ArrayList         occurences = conceptProperties.getConcept().getOccurences();
        DbObject child;
        ArrayList selectedFields = new ArrayList();

        Enumeration fieldNodes = node.children();
        while (fieldNodes.hasMoreElements()) {
            CheckTreeNode fieldNode = (CheckTreeNode) fieldNodes.nextElement();
            if (fieldNode.isSelected())
                selectedFields.add(fieldNode);
        }

        /*
         * o.write(unorderedListOpening.getBytes()); o.write(listItemOpening.getBytes());
         * //o.write(componentProperties.getComponentMetaClass().getGUIName(true,
         * false).getBytes()); o.write(componentProperties.getComponentMetaClass().getGUIName(true,
         * true).getBytes()); o.write(breakLine.getBytes());
         */

        // <TABLE>
        o.write(LT.getBytes());
        o.write(tableOpening.getBytes());
        writeComponentTableAttributes(o, componentProperties);
        o.write(GT.getBytes());
        o.write(LFCR.getBytes());

        generateHtmlComponentTableHeader(o, node, selectedFields);

        DbEnumeration dbEnum = occurence.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            child = (DbObject) dbEnum.nextElement();
            if (componentProperties.getComponentMetaClass().isAssignableFrom(child.getMetaClass())) {
                if (child instanceof DbUDF && !((DbUDF) child).isAUserPropertyOf(occurence)) {
                    continue;
                }
                generateHtmlComponentTableRow(o, node, child, selectedFields);
            }
        }
        dbEnum.close();

        // </TABLE>
        o.write(LT.getBytes());
        o.write(tableClosing.getBytes());
        o.write(GT.getBytes());
        o.write(LFCR.getBytes());

        /*
         * o.write(listItemClosing.getBytes()); o.write(unorderedListClosing.getBytes());
         */
    }

    private void generateHtmlComponentTableHeader(OutputStream o, CheckTreeNode node,
            ArrayList fields) throws DbException, IOException {
        ConceptComponentProperties componentProperties = (ConceptComponentProperties) node
                .getUserObject();

        // <TR>
        o.write(tableRowOpening.getBytes());

        //Enumeration enumeration = node.children();
        Iterator x = fields.iterator();
        while (x.hasNext()/* enum.hasMoreElements() */) {
            CheckTreeNode field = (CheckTreeNode) x.next();//enum.nextElement();
            ComponentAttributeProperties attributeProperties = (ComponentAttributeProperties) field
                    .getUserObject();

            o.write(LT.getBytes());
            o.write(tableHeaderCellOpening.getBytes());
            // attributes
            writeComponentTableHeaderAttributes(o, componentProperties);
            writeComponentTableHeaderCellAttributes(o, attributeProperties);
            o.write(GT.getBytes());

            //o.write(attributeProperties.getMetaField().getGUIName().getBytes());

            Color textColor = (Color) componentProperties.getProperty(
                    ConceptComponentProperties.TABLE_COLORS_GROUP,
                    ConceptComponentProperties.HEADERTEXT_PROPERTY_KEY).getValue();
            writeText(o, attributeProperties.getMetaField().getGUIName(), textColor);

            o.write(LT.getBytes());
            o.write(tableHeaderCellClosing.getBytes());
            o.write(GT.getBytes());
        }

        // </TR>
        o.write(tableRowClosing.getBytes());

    }

    private void generateHtmlComponentTableRow(OutputStream o, CheckTreeNode node,
            DbObject occurence, ArrayList fields) throws DbException, IOException {
        ConceptComponentProperties componentProperties = (ConceptComponentProperties) node
                .getUserObject();

        // <TR>
        o.write(tableRowOpening.getBytes());

        Enumeration enumeration = node.children();
        Iterator x = fields.iterator();
        while (x.hasNext()/* enum.hasMoreElements() */) {
            CheckTreeNode fieldNode = (CheckTreeNode) x.next();//enum.nextElement();
            ComponentAttributeProperties attributeProperties = (ComponentAttributeProperties) fieldNode
                    .getUserObject();

            o.write(LT.getBytes());
            o.write(tableCellOpening.getBytes());
            // attributes
            writeComponentTableCellAttributes(o, attributeProperties);
            o.write(GT.getBytes());

            MetaField metaField = attributeProperties.getMetaField();
            Object value = occurence.get(metaField);
            if (value != null) {
                if ((metaField == DbSemanticalObject.fName)
                        || (metaField == DbSemanticalObject.fPhysicalName)
                        || (metaField == DbSemanticalObject.fAlias)
                        || (metaField == DbSMSDiagram.fName)) {

                    CheckTreeNode conceptNode = model.getConceptTreeNodeFromMetaClass(occurence
                            .getMetaClass());//model.getConceptTreeNodeFromName(occurence.getMetaClass().toString());
                    if (conceptNode != null && conceptNode.isSelected()) {
                        ConceptProperties conceptProperties = (ConceptProperties) conceptNode
                                .getUserObject();
                        String fileName = conceptProperties.getProperty(
                                ConceptProperties.TABLE_GENERAL_GROUP,
                                ConceptProperties.FILENAME_PROPERTY_KEY).getValue().toString();
                        String link = MessageFormat.format(linkOpening, new Object[] { fileName,
                                occurence.getComposite().getName() + "_" + occurence.getName(),
                                mainFrame }); // NOT LOCALIZABLE
                        o.write(link.getBytes());
                    }
                    writeValue(o, value);
                    o.write(linkClosing.getBytes());
                } else {
                    //writeValue(o, value);
                    Color textColor = (Color) attributeProperties.getProperty(
                            ComponentAttributeProperties.COLUMN_COLORS_GROUP,
                            ComponentAttributeProperties.TEXT_PROPERTY_KEY).getValue();
                    writeText(o, value, textColor);
                }
            } else
                o.write(emptyCell.getBytes());

            o.write(LT.getBytes());
            o.write(tableCellClosing.getBytes());
            o.write(GT.getBytes());
        }

        // </TR>
        o.write(tableRowClosing.getBytes());

    }

    public void writeComponentTableAttributes(OutputStream o,
            ConceptComponentProperties componentProperties) throws IOException, DbException {

        Object borderColorValue = componentProperties.getProperty(
                ConceptComponentProperties.TABLE_COLORS_GROUP,
                ConceptComponentProperties.BORDERCOLOR_PROPERTY_KEY).getValue();
        writeStringAttribute(o, BORDERCOLOR, borderColorValue);

        Object borderValue = componentProperties.getProperty(
                ConceptComponentProperties.TABLE_DIMENSIONS_GROUP,
                ConceptComponentProperties.BORDER_PROPERTY_KEY).getValue();
        writeStringAttribute(o, BORDER, borderValue);

        Object widthValue = componentProperties.getProperty(
                ConceptComponentProperties.TABLE_DIMENSIONS_GROUP,
                ConceptComponentProperties.WIDTH_PROPERTY_KEY).getValue();
        UnitDomain widthUnit = (UnitDomain) componentProperties.getProperty(
                ConceptComponentProperties.TABLE_DIMENSIONS_GROUP,
                ConceptComponentProperties.WIDTHUNIT_PROPERTY_KEY).getValue();
        writeStringAttribute(o, WIDTH, widthValue, widthUnit);
        /*
         * Object heightValue =
         * componentProperties.getProperty(componentProperties.TABLE_DIMENSIONS_GROUP,
         * componentProperties.HEIGHT_PROPERTY_KEY).getValue(); UnitDomain heightUnit =
         * (UnitDomain)componentProperties.getProperty(componentProperties.TABLE_DIMENSIONS_GROUP,
         * componentProperties.HEIGHTUNIT_PROPERTY_KEY).getValue(); writeStringAttribute(o, HEIGHT,
         * heightValue, heightUnit);
         */
        Object cellPaddingValue = componentProperties.getProperty(
                ConceptComponentProperties.TABLE_DIMENSIONS_GROUP,
                ConceptComponentProperties.CELLPADDING_PROPERTY_KEY).getValue();
        writeStringAttribute(o, CELLPADDING, cellPaddingValue);

        Object cellSpacingValue = componentProperties.getProperty(
                ConceptComponentProperties.TABLE_DIMENSIONS_GROUP,
                ConceptComponentProperties.CELLSPACING_PROPERTY_KEY).getValue();
        writeStringAttribute(o, CELLSPACING, cellSpacingValue);

        Object hMarginValue = componentProperties.getProperty(
                ConceptComponentProperties.TABLE_DIMENSIONS_GROUP,
                ConceptComponentProperties.HORIZONTALMARGIN_PROPERTY_KEY).getValue();
        writeStringAttribute(o, HSPACE, hMarginValue);

        Object vMarginValue = componentProperties.getProperty(
                ConceptComponentProperties.TABLE_DIMENSIONS_GROUP,
                ConceptComponentProperties.VERTICALMARGIN_PROPERTY_KEY).getValue();
        writeStringAttribute(o, VSPACE, vMarginValue);

    }

    public void writeComponentTableHeaderAttributes(OutputStream o,
            ConceptComponentProperties componentProperties) throws IOException, DbException {
        Object headerBGColorValue = componentProperties.getProperty(
                ConceptComponentProperties.TABLE_COLORS_GROUP,
                ConceptComponentProperties.HEADERBACKGROUND_PROPERTY_KEY).getValue();
        writeStringAttribute(o, BGCOLOR, headerBGColorValue);
    }

    public void writeStringAttribute(OutputStream o, String attribute, Object value)
            throws IOException, DbException {
        if ((value != null) && (value.toString().length() > 0)) {
            o.write(new String(SPACE + attribute + "=\"").getBytes()); // NOT LOCALIZABLE
            //o.write(borderColor.getBytes());
            writeValue(o, value);
            o.write(QUOTE.getBytes());
        }
    }

    public void writeStringAttribute(OutputStream o, String attribute, Object value,
            UnitDomain domain) throws IOException, DbException {
        if ((value != null) && (value.toString().length() > 0)) {
            o.write(new String(SPACE + attribute + "=\"").getBytes()); // NOT LOCALIZABLE
            //o.write(borderColor.getBytes());
            writeValue(o, value);
            writeValue(o, domain);
            o.write(QUOTE.getBytes());
        }
    }

    public void writeBooleanAttribute(OutputStream o, String attribute, Object value)
            throws IOException, DbException {
        if ((value != null) && value.equals(Boolean.TRUE)) {
            o.write(SPACE.getBytes());
            writeValue(o, attribute);
        }
    }

    //private static final String htmlDirectory  = SMSPreferences.getSingleton().getPreferenceAsString(SMSPreferences.HTML_GENERATION_DIRECTORY);
    private static final String mainFileName = "index" + htmlExtension; // NOT LOCALIZABLE
    private static final String mainFrame = "main"; // NOT LOCALIZABLE
    private static final String indexFrame = "index"; // NOT LOCALIZABLE

    private void generateMainFile(ReportModel model) throws IOException, DbException {
        ConceptProperties conceptProperties;
        String welcomeFileName;
        FileOutputStream o;

        if (model.getOptions().getWelcomeConceptNode() != null) {
            conceptProperties = (ConceptProperties) model.getOptions().getWelcomeConceptNode()
                    .getUserObject();
            welcomeFileName = conceptProperties.getProperty(ConceptProperties.TABLE_GENERAL_GROUP,
                    ConceptProperties.FILENAME_PROPERTY_KEY).getValue().toString();
        } else
            welcomeFileName = ""; // NOT LOCALIZABLE

        o = new FileOutputStream(new File(htmlDirectory, mainFileName));

        // <HTML>
        o.write(htmlOpening.getBytes());

        o.write(LT.getBytes());
        o.write(framesetOpening.getBytes());
        //writeStringAttribute(o, BORDER, "0");                                       // NOT LOCALIZABLE
        writeStringAttribute(o, FRAMEBORDER, "YES"); // NOT LOCALIZABLE
        writeStringAttribute(o, COLS, (model.getOptions().getGenerateIndex() ? "20%,*" : "100%")); // NOT LOCALIZABLE
        o.write(GT.getBytes());

        if (model.getOptions().getGenerateIndex()) {
            generateIndexFile(model);

            // index frame
            o.write(LT.getBytes());
            o.write(frameOpening.getBytes());
            writeStringAttribute(o, SRC, indexFileName);
            writeStringAttribute(o, NAME, indexFrame);
            o.write(GT.getBytes());
        }

        // main frame
        o.write(LT.getBytes());
        o.write(frameOpening.getBytes());
        writeStringAttribute(o, SRC, welcomeFileName);
        writeStringAttribute(o, NAME, mainFrame);
        o.write(GT.getBytes());

        o.write(LT.getBytes());
        o.write(framesetClosing.getBytes());
        o.write(GT.getBytes());

        // </HTML>
        o.write(htmlClosing.getBytes());

        o.close();
        controller.println(TAB + htmlDirectory + File.separator + mainFileName);
    }

    private static final String indexFileName = "toc" + htmlExtension; // NOT LOCALIZABLE, file name

    private void generateIndexFile(ReportModel model) throws IOException {
        CheckTreeModel treeModel = model.getTreeReprentation();
        CheckTreeNode root = (CheckTreeNode) treeModel.getRoot();
        CheckTreeNode node;
        FileOutputStream o;
        String fileName;

        o = new FileOutputStream(new File(htmlDirectory, indexFileName));

        // <HTML>
        o.write(htmlOpening.getBytes());

        Enumeration enumeration = root.children();
        while (enumeration.hasMoreElements()) {
            node = (CheckTreeNode) enumeration.nextElement();
            if (node.isSelected()) {

                if (node.getUserObject() instanceof ConceptProperties) {
                    ConceptProperties conceptProperties = (ConceptProperties) node.getUserObject();
                    fileName = conceptProperties.getProperty(ConceptProperties.TABLE_GENERAL_GROUP,
                            ConceptProperties.FILENAME_PROPERTY_KEY).getValue().toString();
                } else
                    fileName = node.toString() + htmlExtension;;

                String link = MessageFormat.format(linkOpening, new Object[] { fileName, "",
                        mainFrame }); // NOT LOCALIZABLE
                o.write(link.getBytes());
                o.write(node.toString().getBytes());
                o.write(linkClosing.getBytes());
                o.write(breakLine.getBytes());
            }
        }

        // </HTML>
        o.write(htmlClosing.getBytes());

        o.close();
        controller.println(TAB + htmlDirectory + File.separator + indexFileName);
    }

    private void generateDiagramPropretiesTable(OutputStream o, CheckTreeNode node,
            DbObject occurence) throws DbException, IOException {
        File diagramsDirectory;

        if (!model.getOptions().getDiagramDirectory().equals("")) // NOT LOCALIZABLE
            diagramsDirectory = new File(htmlDirectory + File.separator
                    + model.getOptions().getDiagramDirectory());
        else
            diagramsDirectory = new File(htmlDirectory);

        if (!diagramsDirectory.exists())
            diagramsDirectory.mkdirs();

        generateHtmlPropertiesTable(o, node, occurence);

        JpegGenerator jpegGenerator = new JpegGenerator((DbSMSDiagram) occurence,
                diagramsDirectory, controller);
        jpegGenerator.generate();

        if (jpegGenerator.getGeneratedFile() == null) {
            writeText(o, LocaleMgr.misc.getString("DiagramIsEmpty"), Color.red); // NOT LOCALIZABLE
        } else {
            String imageReference = (model.getOptions().getDiagramDirectory().equals("") ? jpegGenerator
                    .getGeneratedFile().toString()
                    : model.getOptions().getDiagramDirectory() + File.separator
                            + jpegGenerator.getGeneratedFile().toString());
            String image = MessageFormat.format(imageOpening, new Object[] { imageReference });
            o.write(image.getBytes());
            o.write(imageClosing.getBytes());
        }
    }

    public void writeComponentTableHeaderCellAttributes(OutputStream o,
            ComponentAttributeProperties conceptAttributeProperties) throws IOException,
            DbException {
        Object widthValue = conceptAttributeProperties.getProperty(
                ConceptAttributeProperties.COLUMN_DIMENSIONS_GROUP,
                ConceptAttributeProperties.WIDTH_PROPERTY_KEY).getValue();
        UnitDomain widthUnit = (UnitDomain) conceptAttributeProperties.getProperty(
                ConceptAttributeProperties.COLUMN_DIMENSIONS_GROUP,
                ConceptAttributeProperties.WIDTHUNIT_PROPERTY_KEY).getValue();
        writeStringAttribute(o, WIDTH, widthValue, widthUnit);
    }

    public void writeComponentTableCellAttributes(OutputStream o,
            ComponentAttributeProperties conceptAttributeProperties) throws IOException,
            DbException {

        Object heightValue = conceptAttributeProperties.getProperty(
                ConceptAttributeProperties.COLUMN_DIMENSIONS_GROUP,
                ConceptAttributeProperties.HEIGHT_PROPERTY_KEY).getValue();
        UnitDomain heightUnit = (UnitDomain) conceptAttributeProperties.getProperty(
                ConceptAttributeProperties.COLUMN_DIMENSIONS_GROUP,
                ConceptAttributeProperties.HEIGHTUNIT_PROPERTY_KEY).getValue();
        writeStringAttribute(o, HEIGHT, heightValue, heightUnit);

        Object alignValue = conceptAttributeProperties.getProperty(
                ConceptAttributeProperties.COLUMN_ALIGNMENT_GROUP,
                ConceptAttributeProperties.HORIZONTAL_PROPERTY_KEY).getValue();
        writeStringAttribute(o, ALIGN, alignValue);

        Object valignValue = conceptAttributeProperties.getProperty(
                ConceptAttributeProperties.COLUMN_ALIGNMENT_GROUP,
                ConceptAttributeProperties.VERTICAL_PROPERTY_KEY).getValue();
        writeStringAttribute(o, VALIGN, valignValue);

        Object backgroundValue = conceptAttributeProperties.getProperty(
                ConceptAttributeProperties.COLUMN_COLORS_GROUP,
                ConceptAttributeProperties.BACKGROUND_PROPERTY_KEY).getValue();
        writeStringAttribute(o, BGCOLOR, backgroundValue);

        Boolean noWrapValue = (Boolean) conceptAttributeProperties.getProperty(
                ComponentAttributeProperties.COLUMN_GENERAL_GROUP,
                ConceptAttributeProperties.NOWRAP_PROPERTY_KEY).getValue();
        writeBooleanAttribute(o, NOWRAP, noWrapValue);
    }

}
