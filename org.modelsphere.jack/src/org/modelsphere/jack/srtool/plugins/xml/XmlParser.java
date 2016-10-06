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

package org.modelsphere.jack.srtool.plugins.xml;

import java.io.*;
import java.util.Hashtable;
import java.util.Stack;

import org.modelsphere.jack.debug.Debug;
import org.xml.sax.*;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

//
// Simple demonstration of using SAX event-driven APIs directly.
//
// This shows the most significant SAX callback APIs:
//
//	- The Parser class can represent an XML parser.
//
//	- This DocumentHandler just prints its input in more or
//	  less the form in which the source document held it.
//	  Almost any SAX based application will implement this.
//
//	- There is also an ErrorHandler implementation, which will
//	  treat validation errors as fatal.  SAX normally ignores
//	  such errors, even when used with a validating parser.
//
//
public final class XmlParser implements DocumentHandler {

    private static Writer out;
    private boolean expectValue = false;
    private boolean expectingNameValue = false;
    private boolean expectingTarget = false;
    private boolean expectingTag = false;
    private boolean expectingTagValue = false;
    private boolean expectingMultiplicity = false;
    private boolean expectingOriginTable = false;
    private boolean expectingTargetTable = false;
    private boolean isDebug = false;
    private int newOccurrenceNameEmitted = 0;
    private Hashtable idTable = new Hashtable(); // contains XMI ID for further references
    private Stack elementStack = new Stack(); // contains all XMI elements (class, name, type)
    private Stack occurrenceNameStack = new Stack(); // contains names of XMI elements which are occurrences (such as class names, attr names, etc.)

    // Constants
    private static String kOccurrenceType = "SR_OCCURRENCE"; // NOT LOCALIZABLE
    private static String kXmiId = "XMI.id"; // NOT LOCALIZABLE
    private static String kXmiExporter = "XMI.exporter"; // NOT LOCALIZABLE
    private static String kTarget = "target"; // NOT LOCALIZABLE
    private static String kXmiReference = "XMI.reference"; // NOT LOCALIZABLE

    // Keep current XMI name & SRX name
    private String currentXmiName = "";
    private String currentSrxName = "";
    private String currentXmiId = "";
    private int currentSrxId = XmiMapping.UNKNOWN;
    private static String eol = "\n"; // NOT LOCALIZABLE

    // To deal with tagged value
    private static String ktaggedValue = "taggedValue"; // NOT LOCALIZABLE
    private static String kTaggedValue = "TaggedValue"; // NOT LOCALIZABLE
    private boolean taggedValueMode = false;
    private static String kTag = "tag"; // NOT LOCALIZABLE
    private static String kValue = "value"; // NOT LOCALIZABLE

    // for writing in a file, call this function with isNative == true
    public static void setNativeLineSeparator(boolean isNative) {
        eol = (isNative ? System.getProperty("line.separator") : "\n"); // NOT LOCALIZABLE
    }

    // here are all the SAX DocumentHandler methods
    public void setDocumentLocator(Locator l) {
        // we'd record this if we needed to resolve relative URIs
        // in content or attributes, or wanted to give diagnostics.
    }

    public static void setWriter(Writer aWriter) {
        out = aWriter;
    }

    public void startDocument() throws SAXException {
        emit("--***** DO NOT MODIFY HEADER *****" + eol); // NOT LOCALIZABLE
        emit("--Interface: RDM-OOJ Interface" + eol); // NOT LOCALIZABLE
        emit("--Version:   1.0" + eol); // NOT LOCALIZABLE
        emit("--Database:  none" + eol); // NOT LOCALIZABLE
        emit("--DBMSID:" + eol); // NOT LOCALIZABLE
        emit("--DBMS:" + eol); // NOT LOCALIZABLE
        emit("--Script:" + eol); // NOT LOCALIZABLE
        emit("--******** END OF HEADER *********" + eol); // NOT LOCALIZABLE
    }

    public void endDocument() throws SAXException {
        try {
            out.write(eol);
            out.flush();
            out = null;
        } catch (IOException e) {
            throw new SAXException("I/O error", e); // NOT LOCALIZABLE
        }
    }

    private boolean isMain = true;
    private boolean enabledOutput = true;

    private boolean toProcess(int srxId) {
        boolean toProcess = true;

        if (isMain) {
            if ((srxId != XmiMapping.DATA_TYPE_ID) && (srxId != XmiMapping.CLASS_ID)
                    && (srxId != XmiMapping.ATTRIBUTE_ID) && (srxId != XmiMapping.ASSOCIATION_ID)
                    && (srxId != XmiMapping.CONNECTOR_ID)) {
                toProcess = false;
            }
        }

        return toProcess;
    }

    private void emitNewOccurrence(int srxId, AttributeList attrs) throws SAXException {

        enabledOutput = toProcess(srxId);
        if (!enabledOutput) {
            return;
        }

        currentSrxId = srxId;
        String mappedName = XmiMapping.getMappingName(srxId);
        emit(eol + kOccurrenceType + " " + mappedName + eol);

        // if it's a component, write its composite
        String composite = XmiMapping.getComposite(srxId);
        if (composite != null) {
            if (!occurrenceNameStack.empty()) {
                String compositeName = (String) occurrenceNameStack.peek();
                if (compositeName != null) {
                    emit(composite + " " + compositeName + eol);
                }
            } // end if
        } // end if

        // Keep its XMI.id
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String attr = attrs.getName(i);
                if (attr.equals(kXmiId)) {
                    currentXmiId = attrs.getValue(i);
                } // end if
            } // end for
        } // end if

        // Keep its current XMI name & SRX name
        currentXmiName = XmiMapping.getXmiName(srxId);
        currentSrxName = XmiMapping.getSrxName(srxId);
    }

    private void emitReferencedValue(AttributeList attrs) throws SAXException {
        if (expectingTarget) {
            String previousElement = "";
            if (!elementStack.empty()) {
                previousElement = (String) elementStack.peek();
            }

            int prevSrxId = XmiMapping.getSrxId(previousElement);
            // enabledOutput = toProcess(prevSrxId);
            if (!enabledOutput) {
                return;
            }

            if (attrs != null) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    String attr = attrs.getName(i);
                    if (attr.equals(kTarget)) {
                        String key = attrs.getValue(i);
                        String name = (String) idTable.get(key);
                        if (name != null) {
                            emit(name + eol);
                        }
                        expectingTarget = false;
                    }
                } // end for
            } // end if
        } // end if
    } // end emitReferencedValue()

    private static final String MULTIPLICITY = "MULTIPLICITY"; // NOT LOCALIZABLE
    private static final String ORIGINTABLE = "ORITAB_NAME"; // NOT LOCALIZABLE
    private static final String TARGETTABLE = "DESTTAB_NAME"; // NOT LOCALIZABLE

    public void startElement(String tag, AttributeList attrs) throws SAXException {
        if (isDebug)
            emit("start element" + eol); // NOT LOCALIZABLE

        // keep previous element before adding a new one
        String previousElement = "";
        if (!elementStack.empty()) {
            previousElement = (String) elementStack.peek();
        }

        elementStack.push(tag);

        if (tag.equals(kTaggedValue)) {
            taggedValueMode = true;
            return;
        }

        // ignore <tag> and <value> if outside a <TaggedValue> block
        if (taggedValueMode) {
            if (tag.equals(kTag)) {
                expectingTag = true;
            } else if (tag.equals(kValue)) {
                expectingTagValue = true;
            }
        }

        int srxId = XmiMapping.getSrxId(tag);
        boolean isOccurrenceType = (srxId != XmiMapping.UNKNOWN);

        if (isOccurrenceType) {
            emitNewOccurrence(srxId, attrs);
        } else {

            if (!enabledOutput) {
                return;
            }

            // if tag = 'NAME' and currentXmiName expected is also 'NAME'
            if (tag.equals(currentXmiName)) {
                // ignore if not known in Srx format
                int prevSrxId = XmiMapping.getSrxId(previousElement);
                if (prevSrxId != XmiMapping.UNKNOWN) {
                    expectingNameValue = true;
                }
            } else if (tag.equals(kXmiReference)) {
                emitReferencedValue(attrs);
            } else {
                // ignore if not known in Srx format
                int prevSrxId = XmiMapping.getSrxId(previousElement);
                if (currentSrxId != XmiMapping.UNKNOWN) {
                    int attrId = XmiMapping.getAttrId(currentSrxId, tag);
                    if (attrId != XmiMapping.UNKNOWN) {
                        String mappedAttr = XmiMapping.getMappedAttr(currentSrxId, attrId);

                        if (mappedAttr.equals(MULTIPLICITY)) {
                            // Special case for multiplicity
                            expectingMultiplicity = true;
                        } else if (mappedAttr.equals(ORIGINTABLE)) {
                            // Special case for origin table
                            expectingOriginTable = true;
                            expectingTarget = true;
                        } else if (mappedAttr.equals(TARGETTABLE)) {
                            // Special case for target table
                            expectingTargetTable = true;
                            expectingTarget = true;
                        } else {
                            emit(mappedAttr + " ");
                            expectingTarget = true;
                        } // end if
                    } // end if
                } // end if
            } // end if

        } // end if
    } // end startElement()

    public void endElement(String name) throws SAXException {
        if (isDebug)
            emit("end element" + eol); // NOT LOCALIZABLE

        if (!elementStack.empty()) {
            String removed = (String) elementStack.pop();

            if (name.equals(kTaggedValue)) {
                taggedValueMode = false;
            }

            // is the element removed a concept (as table, column, etc.)?
            int removedSrxId = XmiMapping.getSrxId(removed);
            if (removedSrxId != XmiMapping.UNKNOWN) {
                // does this concept have an identifier?
                String xmiName = XmiMapping.getXmiName(removedSrxId);
                if (!xmiName.equals("")) {
                    if (!occurrenceNameStack.empty()) {
                        occurrenceNameStack.pop(); // yes, so pop its name
                    }
                }
            } // end if
        } // end if
    } // end endElement()

    private void emitNewOccurrenceName(String currentSrxName, String name) throws SAXException {
        emit(currentSrxName + " " + name + eol);
        occurrenceNameStack.push(name);
        // occurrenceNameStack.push(name); //not an error
        // newOccurrenceNameEmitted += 2;

        // store current Xmi Id (as the key) and name (as the value) in the
        // hashtable
        idTable.put(currentXmiId, name);
    }

    private boolean tagFound = false;

    private void emitNewTag(String tag) throws SAXException {
        String previousElement = null;

        // retrieve the actual previous element
        int index = elementStack.size();
        while (previousElement == null) {
            if (!elementStack.empty()) {
                index--;
                previousElement = (String) elementStack.elementAt(index);
            } else {
                previousElement = ""; // exit, to avoid endless loop
            }

            if (previousElement.equals(kTag) || previousElement.equals(kTaggedValue)
                    || previousElement.equals(ktaggedValue)) {
                previousElement = null; // continue to search
            }
        }

        // ignore if not known in Srx format
        int prevSrxId = XmiMapping.getSrxId(previousElement);
        if (!enabledOutput) {
            return;
        }

        if (prevSrxId != XmiMapping.UNKNOWN) {
            int attrId = XmiMapping.getAttrId(prevSrxId, tag);
            if (attrId != XmiMapping.UNKNOWN) {
                String mappedAttr = XmiMapping.getMappedAttr(prevSrxId, attrId);
                emit(mappedAttr + " ");
                tagFound = true;
            }
        }

        expectingTarget = true;
    }

    private void emitNewTagValue(String tag) throws SAXException {
        if (tagFound) {
            if (!tag.equals("")) {
                emit(tag + eol);
            }
        }

        tagFound = false;
    }

    // param chars : its format is '0|1..1|N'
    private void emitMultiplicity(String chars) throws SAXException {
        int idx = chars.indexOf(".");
        String min = chars.substring(0, idx);
        String max = chars.substring(idx + 2);

        boolean is_child = true;
        if (max.charAt(0) == 'N') {
            is_child = false; // if many children, thus it is the parent
        }

        emit("IS_CHILD " + (is_child ? "1" : "0") + eol); // NOT LOCALIZABLE
        emit("MINIMUM " + min + eol); // NOT LOCALIZABLE
        emit("MAXIMUM " + max + eol); // NOT LOCALIZABLE
    } // end emitMultiplicity()

    private void emitOriginTable() throws SAXException {
        emit("ORITAB_NAME "); // target is the orginine table's name //NOT LOCALIZABLE
    }

    private void emitTargetTable() throws SAXException {
        emit("DESTTAB_NAME "); // target is the orginine table's name //NOT
        // LOCALIZABLE
    }

    public void characters(char buf[], int offset, int len) throws SAXException {
        // NOTE: this doesn't escape '&' and '<', but it should
        // do so else the output isn't well formed XML. to do this
        // right, scan the buffer and write '&amp;' and '&lt' as
        // appropriate.
        if (isDebug)
            emit("characters" + eol); // NOT LOCALIZABLE

        String chars = new String(buf, offset, len); // TODO: replace &amp;..

        if (expectingNameValue) {
            emitNewOccurrenceName(currentSrxName, chars);
            expectingNameValue = false;
        } else if (expectingTag) {
            emitNewTag(chars);
            expectingTag = false;
        } else if (expectingTagValue) {
            emitNewTagValue(chars);
            expectingTagValue = false;
        } else if (expectValue) {
            // emitNewTagValue(chars);
            expectValue = false;
        } else if (expectingMultiplicity) {
            emitMultiplicity(chars);
            expectingMultiplicity = false;
        } else if (expectingOriginTable) {
            emitOriginTable();
            expectingOriginTable = false;
        } else if (expectingTargetTable) {
            emitTargetTable();
            expectingTargetTable = false;
        }
    } // end characters()

    public void ignorableWhitespace(char buf[], int offset, int len) throws SAXException {
        // this whitespace ignorable ... so we ignore it!

        // this callback won't be used consistently by all parsers,
        // unless they read the whole DTD. Validating parsers will
        // use it, and currently most SAX nonvalidating ones will
        // also; but nonvalidating parsers might hardly use it,
        // depending on the DTD structure.
    }

    public void processingInstruction(String target, String data) throws SAXException {
        emit("<?");
        emit(target);
        emit(" ");
        emit(data);
        emit("?>");
    }

    // helpers ... wrap I/O exceptions in SAX exceptions, to
    // suit handler signature requirements
    private void emit(String s) throws SAXException {
        try {
            if (s.equals("MULTIPLICITY")) { // NOT LOCALIZABLE
                int i = 0;
            }

            out.write(s);
            out.flush();
        } catch (IOException e) {
            throw new SAXException("I/O error", e); // NOT LOCALIZABLE
        }
    }

    static class MyErrorHandler extends HandlerBase {
        // treat validation errors as fatal
        public void error(SAXParseException e) throws SAXParseException {
            throw e;
        }

        // dump warnings too
        public void warning(SAXParseException err) throws SAXParseException {
            Debug.trace("** Warning" // NOT LOCALIZABLE
                    + ", line " + err.getLineNumber() // NOT LOCALIZABLE
                    + ", uri " + err.getSystemId()); // NOT LOCALIZABLE
            Debug.trace("   " + err.getMessage());
        }
    } // end MyErrorHandler

    static Parser getParser() throws SAXException, ParserConfigurationException {
        //
        // turn it into an in-memory object.
        //

        Parser parser;
        SAXParserFactory spf = SAXParserFactory.newInstance();
        String validation = System.getProperty("javax.xml.parsers.validation", "false"); // NOT LOCALIZABLE
        if (validation.equalsIgnoreCase("true")) // NOT LOCALIZABLE
            spf.setValidating(true);

        SAXParser sp = spf.newSAXParser();
        parser = sp.getParser();
        return parser;
    }

    public static void main(String[] args) throws IOException {
        String filename = "z.xml"; // NOT LOCALIZABLE, main

        try {
            String uri = "file:" + new File(filename).getAbsolutePath(); // NOT LOCALIZABLE, main

            //
            // turn it into an in-memory object.
            //
            Parser parser = getParser();
            parser.setDocumentHandler(new XmlParser());
            parser.setErrorHandler(new MyErrorHandler());
            parser.parse(uri);

        } catch (SAXParseException err) {
            Debug.trace("** Parsing error" // NOT LOCALIZABLE, main
                    + ", line " + err.getLineNumber() // NOT LOCALIZABLE
                    + ", uri " + err.getSystemId()); // NOT LOCALIZABLE
            Debug.trace("   " + err.getMessage());

        } catch (SAXException e) {
            Exception x = e;
            if (e.getException() != null)
                x = e.getException();
            x.printStackTrace();

        } catch (Throwable t) {
            t.printStackTrace();
        }

        byte[] buf = new byte[256];
        Debug.trace("Press ENTER to exit."); // NOT LOCALIZABLE
        System.in.read(buf, 0, 256);
        System.exit(0);
    }
} // end XmlParser
