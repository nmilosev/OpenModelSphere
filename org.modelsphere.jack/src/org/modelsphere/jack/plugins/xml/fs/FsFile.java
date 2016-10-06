/*************************************************************************

Copyright (C) 2009 by neosapiens inc.

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

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/

package org.modelsphere.jack.plugins.xml.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.modelsphere.jack.io.IoUtil;

public class FsFile extends FsEntry {
	
	//
	// factory methods
	//
	public static FsFile createFile(FsFolder parent, File file) {
		String fileName = file.getName();
		FsFile fileRef = (FsFile)parent.getInstance(file); 
		if (fileRef == null) {
			fileRef = new FsFile(parent, file); 
		}
		
		return fileRef; 
	}

	public static FsFile createFile(FsFolder parent, String filename, ZipFileAndURLUnion composite, ZipEntry entry) {
		//find parent
		boolean directory = entry.isDirectory(); 
		String entryName = entry.getName();
		int idx = entryName.lastIndexOf('/');
		FsFile fileRef = null;
		
		if (! directory) {
			//fileRef = (FileRef)parent.getInstance(zipfile, entry);
			fileRef = new FsFile(parent, filename, composite, entry);
		} else {
			String parentName = entryName.substring(0, idx+1); 
			String basename = entryName.substring(idx+1);
			ZipEntry subentry = composite.getEntry(parentName);
			//FolderRef folderRef = FolderRef.createFolder(parent, parentName, zipfile, subentry);
			filename = entryName.substring(idx+1); 
			//fileRef = new FileRef(parent, filename, zipfile, entry); 
			fileRef = new FsFile(parent, parentName, composite, subentry); //FileRef.createFile(parent, parentName, zipfile, subentry);
		} //end if
		
		
		return fileRef; 
	}
	
	public static FsFile createFile(URL url) {
		FsFile fileRef = null;
		String protocol = url.getProtocol();
		
		try {
			if ("file".equals(protocol)) {
				String encoded = url.getFile();
	            String decoded = URLDecoder.decode(encoded, "UTF-8"); //important
	            File file = new File(decoded);
	            File parent = file.getParentFile();
	            FsFolder folder = FsFolder.createRoot(parent); 
	            fileRef = new FsFile(folder, file);
			} else if ("jar".equals(protocol)) {
				String encoded = getEncodedJarName(url);
	            String decoded = URLDecoder.decode(encoded, "UTF-8"); //important
	            
	            File file = new File(decoded);
	            ZipFile zipfile = new ZipFile(file);
	            String entryName = getEntryName(url, zipfile);
	            
	            File parentFile = file.getParentFile();
	            FsFolder parent = FsFolder.createRoot(parentFile);
	            
	            String folderEntryName = "";
	            StringTokenizer st = new StringTokenizer(entryName, "/");
	            while (st.hasMoreTokens()) {
	            	String token = st.nextToken();
	            	if (st.hasMoreTokens()) {
	            		token += "/"; 
	            	}
	            	
	            	folderEntryName += token; 
	            	ZipEntry entry = zipfile.getEntry(folderEntryName);
	            	//AbsFolderElement elem = parent.getInstance(zipfile, entry); 
	            	ZipFileAndURLUnion composite = new ZipFileAndURLUnion(zipfile);
	            	FsEntry elem = FsEntry.createFolderElement(parent, folderEntryName, composite, entry); 
	            	if (elem instanceof FsFolder) {
	            		parent = (FsFolder)elem;
	            	} else if (elem instanceof FsFile) {
	            		fileRef = (FsFile)elem;
	            	}
	            	
	            	/*
	            	if (elem == null) {
	            		if (st.hasMoreTokens()) {
	            			parent = FolderRef.createFolder(parent, token, zipfile, entry); 
	            		} else {
	            			fileRef = createFile(parent, entryName, zipfile, entry);
	            		}
	            	}*/
	            } //end while

	    		//ZipEntry entry = zipfile.getEntry(entryName);
	    		
	            
	            //FolderRef folder = FolderRef.createRoot(parent); 
	            //fileRef = new FileRef(folder, file);
			} else {
				fileRef = null;
			} //end if
		
		} catch (IOException ex) {
	       ex.printStackTrace();
	       fileRef = null;
        } //end if
		
		return fileRef;
	}
	
	//
	// private constructors
	//
	
	private FsFile(FsFolder parent, File file) {
		String filename = file.getName();
		parent.addChild(filename, this);
		m_parent = parent;
		m_file = file;
	}

	private FsFile(FsFolder parent, String filename, ZipFileAndURLUnion composite, ZipEntry entry) {
		parent.addChild(filename, this);
		m_parent = parent;
		m_composite = composite;
		m_entry = entry;
		
		if ("com".equals(filename)) {
			int i=0;
			i++;
		}
	}
	
	//
	// public methods
	//
	
	public FsFolder getParent() {
		if (m_parent == null) {
			if (m_file != null) {
				File parentFile = m_file.getParentFile();
				m_parent = FsFolder.createRoot(parentFile);
			} else if (m_entry != null) {
				m_parent = null;
			}
		} //end if

		return m_parent;
	}
			
	public InputStream openInputStream() throws IOException {
		InputStream input = null; 
		
		if (m_file != null) {
			input = new FileInputStream(m_file);
			//m_input = input;
		} else if (m_entry != null) {
		    input = m_composite.getInputStream(m_entry); 
		}
		
		return input;
	}
	
	//
	// private methods
	//
	
	private static String getEncodedJarName(URL url) throws UnsupportedEncodingException {
		String jarname = url.toExternalForm();
		int index = Math.max(jarname.indexOf(".jar!"), jarname.indexOf(".zip!")); 
        if (index > -1)
            jarname = jarname.substring(0, index + 4);
        index = jarname.indexOf("jar:");
        if (index > -1) {
            jarname = jarname.substring(4);
        }
        index = jarname.indexOf("file:/");
        if (index > -1) {
            jarname = jarname.substring(6);
        }
        index = jarname.indexOf("file:");
        if (index > -1) {
            jarname = jarname.substring(5);
        }
		
		return jarname;
	}
	
	private static String getEntryName(URL url, ZipFile zipfile) {
		String jarname = url.toExternalForm();
		int idx = Math.max(jarname.indexOf(".jar!"), jarname.indexOf(".zip!")); 
		String entryName = (idx == -1) ? null : jarname.substring(idx+6);
		return entryName;
	}

	@Override public void copyTo(FsFolder destFolder) throws IOException {
		FsFile copy = null;
		
		//create file
		if (m_file != null) {
			File parent = destFolder.m_file;
			String filename = m_file.getName();
			File newFile = new File(parent, filename);
			copy = createFile(destFolder, newFile);
			copyTo(copy);
		}else if (m_composite != null) {
			String foldername = getSimpleName();
			String filename = getSimpleName();
			File parent = destFolder.m_file;
			File newFile = new File(parent, filename);
			copy = FsFile.createFile(destFolder, newFile);
			copyTo(copy);
		}
	}

	private void copyTo(FsFile copy) throws IOException {
		if (m_file != null) {
			copy.m_file.createNewFile();
			IoUtil.copyFile(m_file, copy.m_file, true, false);
		} else {
			copy.m_file.createNewFile();
			InputStream input = m_composite.getInputStream(m_entry);
			OutputStream output = new FileOutputStream(copy.m_file); 
			
			 byte[] buffer = new byte[8 * 1024];
	         int count = 0;
	            do {
	            	output.write(buffer, 0, count);
	                count = input.read(buffer, 0, buffer.length);
	            } while (count != -1);

	            output.close();
		}
	}



	/*
	public void close() throws IOException {
		if (m_file != null) {
			m_input.close();
		} else if (m_entry != null) {
			m_zipfile.close();
		}
	}*/







}
