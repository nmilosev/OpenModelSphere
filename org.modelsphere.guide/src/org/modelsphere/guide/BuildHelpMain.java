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
package org.modelsphere.guide;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BuildHelpMain {

	/*
	 * This program reads toc.xml in input and generates 
	 * tree_en.xml and tree_fr.xml, the TOC of help files 
	 * in both languages.
	 */
	public static void main(String[] args) {
		String inputFileName = "./help_sms/toc.xml";
		String outputFolder =  "./help_sms/pages/";
		
		File file = new File(inputFileName);
		boolean exists = file.exists(); 
		
		if (exists) {
			File output = new File(outputFolder);
			builtTOC(file, output);
		} //end if
	} //end main()
	
	private static void builtTOC(File file, File output) {
		//parse the XML
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			readDocument(doc, output);			
		} catch (Exception ex) { 
			ex.toString();
		} //end try
	} //end builtTOC()

	private static void readDocument(Document doc, File output) throws IOException {
		//create the TOCs
		TableOfContents tocs = new TableOfContents(output);
		tocs.println("<tree>");
		tocs.indent();
		
		//read categories
		Node root = doc.getFirstChild(); 
		NodeList categories = root.getChildNodes();
		int nb = categories.getLength(); 
		for (int i=0; i<nb; i++) {
			Node child = categories.item(i);
			String name = child.getNodeName(); 
			if (name.equals("category")) {
				readCategory(child, tocs);
			} //end if
		} //end for
		
		tocs.unindent();
		tocs.println("</tree>");
		tocs.close();
		
		System.out.println("tree_en.xml and tree_fr.xml generated");
	} //end readDocument()

	private static void readCategory(Node node, TableOfContents tocs) {
		NamedNodeMap attrs = node.getAttributes();
		
		Node en = attrs.getNamedItem("en");
		String enTitle = en.getNodeValue();
		Node fr = attrs.getNamedItem("fr"); 
		String frTitle = fr.getNodeValue(); 
		tocs.beginCategory(enTitle, frTitle);
		
		NodeList books = node.getChildNodes();
		int nb = books.getLength(); 
		for (int i=0; i<nb; i++) {
			Node child = books.item(i);
			String name = child.getNodeName();
			
			if (name.equals("category")) {
				readCategory(child, tocs);
			} //end if
			
			if (name.equals("book")) {
				readBook(child, tocs);
			} //end if
		} //end for
		
		tocs.endCategory();
	} //end readCategory()

	private static void readBook(Node book, TableOfContents tocs) {
		NamedNodeMap attrs = book.getAttributes();
		
		Node en = attrs.getNamedItem("en");
		String enTitle = en.getNodeValue();
		Node fr = attrs.getNamedItem("fr"); 
		String frTitle = fr.getNodeValue(); 
		Node file = attrs.getNamedItem("file"); 
		String filename = file.getNodeValue(); 		
		
		generateEntry(enTitle, frTitle, filename, tocs);
	}

	private static void generateEntry(String enTitle, String frTitle, String filename, TableOfContents tocs) {
		tocs.println("<leaf>");
		tocs.indent();
		
		tocs.printLeafText(enTitle, frTitle);
		tocs.printLink(filename);
		
		tocs.unindent();
		tocs.println("</leaf>");
	} //end generateEntry()

} //end BuildHelpTableOfContent
