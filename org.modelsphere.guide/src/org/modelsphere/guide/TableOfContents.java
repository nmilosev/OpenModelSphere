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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.w3c.dom.Node;

class TableOfContents {
	private IndentWriter enTOC; 
	private IndentWriter frTOC; 
	
	TableOfContents(File output) throws IOException {
		enTOC = createEnTOC(output, "tree_en.xml");
		frTOC = createEnTOC(output, "tree_fr.xml");
	}

	public void println(String string) {
		enTOC.println(string);
		frTOC.println(string);
	}

	public void indent() {
		enTOC.indent();
		frTOC.indent();
	}

	public void unindent() {
		enTOC.unindent();
		frTOC.unindent();
	}

	public void close() {
		enTOC.close();
		frTOC.close();	
	}
	
	public void beginCategory(String enTitle, String frTitle) {
		enTOC.println("<branch id=\"" + id(enTitle) + "\"" + ">");
		frTOC.println("<branch id=\"" + id(frTitle) + "\"" + ">");	
		indent();
		
		enTOC.println("<branchText>" + enTitle + "</branchText>");
		frTOC.println("<branchText>" + frTitle + "</branchText>");
	}

	public void endCategory() {
		unindent();
		println("</branch>");	
	}
	
	public void printLeafText(String enTitle, String frTitle) {
		enTOC.println("<leafText>" + enTitle + "</leafText>");
		frTOC.println("<leafText>" + frTitle + "</leafText>");
	}
	
	public void printLink(String filename) {
		enTOC.println("<link>html/en_" + filename + "</link>");
		frTOC.println("<link>html/fr_" + filename + "</link>");
	}
	
	//
	// private methods
	//
	
	private IndentWriter createEnTOC(File output, String filename) throws IOException {
		File file = new File(output, filename);
		IndentWriter writer = new IndentWriter(new FileWriter(file), true, 2); 
		
		String header = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><?xml-stylesheet type=\"text/xsl\" href=\"xmlTree.xsl\"?><!DOCTYPE tree SYSTEM 'tree.dtd'>";
		writer.println(header);
		
		return writer;
	} //end createEnTOC()

	//Remove non-letter characters from the title
	private static StringBuffer g_buffer = new StringBuffer();
	private String id(String title) {
		int nb = title.length(); 
		for (int i=0; i<nb; i++) {
			char ch = title.charAt(i);
			if (Character.isLetter(ch)) {
				g_buffer.append(ch);
			}
		} //end for 
		
		String id = g_buffer.toString();
		g_buffer.delete(0, g_buffer.length()); 
		return id;
	} //end if()
	
	
} //end TableOfContents
