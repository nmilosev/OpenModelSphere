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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.modelsphere.jack.util.JarUtil;

public class FsFolder extends FsEntry {
	
	//
	// factory methods
	//
    
    //resource can be a URL or a folder 
    public static FsFolder createRoot(String resource) {
        FsFolder root = null;

        //is it a URL?
        try {
            URL url = new URL(resource); 
            root = FsFolder.createRoot(url);
        } catch (MalformedURLException ex) {
            //no, it is a file?
            File file = new File(resource); 
            if (file.exists() && file.isDirectory()) {
                root = FsFolder.createRoot(file);
            }
        } //end try
        
        return root;
    }
    
	public static FsFolder createRoot(File folder) {
		return new FsFolder((FsFolder)null, folder); 
	}
	
	public static FsFolder createRoot(URL url) {
	    return new FsFolder((FsFolder)null, url); 
	}
	
	public static FsFolder createFolder(FsFolder parent, File folder) {
		
		FsFolder folderRef = (FsFolder)parent.getInstance(folder); 
		if (folderRef == null) {
			if (! folder.exists()) {
				folder.mkdirs();
			}
			
			folderRef = new FsFolder(parent, folder);
			String name = folder.getName();
			//parent.addChild(name, folderRef); 
		}
		
		return folderRef; 
	}

	public static FsFolder createFolder(FsFolder parent, String folderName, ZipFileAndURLUnion composite, ZipEntry entry) {
		FsFolder folder;
		FsEntry elem = composite.getInstance(parent, entry);
		
		if (elem instanceof FsFolder) {
			folder = (FsFolder)elem;
			
		} else if (elem instanceof FsFile) {
			folder = null;
		} else {
			folder = new FsFolder(parent, folderName, composite, entry);
		}
		
		return folder; 
	}

	

	//
	// private constructors
	//
	private FsFolder(FsFolder parent, File file) {
		if (parent != null) {
			String name = file.getName();
			parent.addChild(name, this);
			m_parent = parent;
		}
		
		m_file = file;
		//buildChildren();
	}
	
	   private FsFolder(FsFolder parent, URL url) {
	       m_composite = new ZipFileAndURLUnion(url); 
	       // m_url = url;
	        //buildChildren();
	    }
	
	private FsFolder(FsFolder parent, String folderName, ZipFileAndURLUnion composite, ZipEntry entry) {
		if (parent != null) {
			parent.addChild(folderName, this);
			m_parent = parent;
		}
		//parent.addChild(folderName, folder); //important
		m_composite = composite;
		m_entry = entry;
		//buildChildren();
	}
	
	@Override
	public FsFolder getParent() {
		if (m_parent == null) {
			if (m_file != null) {
				File parentFile = m_file.getParentFile();
				m_parent = createRoot(parentFile);
			} else if (m_entry != null) {
				m_parent = null;
			}
		} //end if

		return m_parent;
	}
	
	void addChild(String key, FsEntry child) {
		Map<String, FsEntry> children = getChildren();
		FsEntry c = children.get(key);
		if (c == null) {
			children.put(key, child);
		}
	}
	
	public FsEntry getNamedDescendent(String resourceName) {
		FsEntry namedChild = null;
		FsFolder folder = this;
		boolean directory = resourceName.endsWith("/");
		StringTokenizer st = new StringTokenizer(resourceName, "/");
		
		while (st.hasMoreTokens()) {
			String name = st.nextToken(); 
			if (st.hasMoreTokens() || directory) {
				name += "/";
			}
			
			namedChild = folder.getNamedChild(name);
			if (namedChild instanceof FsFolder) {
				folder = (FsFolder)namedChild;
			}
		}
		
		return namedChild;
	}
	
	private FsEntry getNamedChild(String resourceName) {
		Map<String, FsEntry> children = getChildren();
		FsEntry element = children.get(resourceName);
		
		if ((element == null) && resourceName.endsWith("/")) {
			resourceName = resourceName.substring(0, resourceName.length()-1 );
			 element = children.get(resourceName);
		}
		
		return element;
	}
	

	FsEntry getInstance(File file) {
		String name = file.getName();
		Map<String, FsEntry> children = getChildren();
		FsEntry instance = children.get(name); 
		return instance; 
	}
	
	FsEntry getInstance(ZipFile zipfile, ZipEntry entry) {
		String name = entry.getName();
		Map<String, FsEntry> children = getChildren();
		FsEntry instance = children.get(name); 
		return instance;
	}
	
	public List<FsEntry> getFolderElements() {
		
		if (m_elements == null) {
			m_elements = new ArrayList<FsEntry>();
			 Map<String, FsEntry> children = getChildren();
			 Set<String> names = children.keySet(); 
			for (String name : names) {
				FsEntry elem = children.get(name); 
				m_elements.add(elem);
			}
		}

		return m_elements;
	}
	private List<FsEntry> m_elements = null;
	
	/*
	private Map<String, AbsFolderElement> m_children = null; 
	private Map<String, AbsFolderElement> getChildren() {
		if (m_children == null) {
			m_children = new HashMap<String, AbsFolderElement>();
			buildChildren(m_children);
		}
		
		return m_children;
	}*/
	
	private Map<String, FsEntry> getChildren() {
		if (m_children == null) {
			m_children = new HashMap<String, FsEntry>(); 
			
			String name = getQualifiedName(); 
			if ("com/".equals(name)) {
				int i=0; i++;
			}
			
			buildChildren();	
		}
		
		return m_children;
	}
	private Map<String, FsEntry> m_children = null;
	
	private void buildChildren() {
		if (m_file != null) {
		    buildChildren(m_file); 
		//} else if (m_url != null) {
        //    buildChildren(m_url); 
		} else if (m_composite != null) {
		    if (m_composite.isURL()) {
		        URL url = m_composite.toURL(); 
		        buildChildren(url); 
		    } else {
		        buildChildren(m_composite);
		    }
	    } //end if
	} //end buildChildren()

    private void buildChildren(ZipFileAndURLUnion composite) {
       // if (composite.)
        
	    String foldername = getQualifiedName();
        
        if ("com/neosapiens/plugins/codegen/simplecsharp/".equals(foldername)) {
            int i=0;
            i++;
        }
        
        Enumeration<? extends ZipEntry> entries = composite.entries(); 
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String entryName = entry.getName(); 
            
            //if entry part of folder
            if (entryName.startsWith(foldername) && ! entryName.equals(foldername)) {
                FsEntry element = m_children.get(entryName); 
                if (element == null) {
                    element = FsEntry.createElement(this, composite, entry);
                    m_children.put(entryName, element);
                }
            }

            //AbsFolderElement newElement = AbsFolderElement.createElement(this, m_zipfile, entry);
            //String simpleName = newElement.getSimpleName();
            
            //AbsFolderElement element = m_children.get(simpleName); 
            //if (element == null) {
            //  m_children.put(simpleName, newElement);
            //}
        } //end while

        long size = m_entry.getSize(); 
        size++;
    }

    private void buildChildren(File m_file) {
	    if (m_file.isDirectory()) {
            File[] files = m_file.listFiles(); 
            for (File file : files) {
                FsEntry newElement = FsEntry.createElement(this, file);
                String name = newElement.getSimpleName();
                FsEntry element = m_children.get(name); 
                if (element == null) {
                    m_children.put(name, newElement);
                }
                //elements.add(element);
            }
        } else {
            try {
                ZipFile zipfile = new ZipFile(m_file); 
        
                Enumeration<? extends ZipEntry> entries = zipfile.entries(); 
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    
                    //get the name of the last element
                    String name = entry.getName();
                    FsEntry element = getNamedDescendent(name);
                    if (element == null) {
                        ZipFileAndURLUnion parent = new ZipFileAndURLUnion(zipfile);
                        element = FsEntry.createElement(this, parent, entry);
                        
                        if (element == null) {
                            int i=0; i++;
                        }
                        String simpleName = element.getSimpleName();
                        
                        FsEntry e = m_children.get(simpleName); 
                        if (e == null) {
                            m_children.put(simpleName, element);
                        }
                    }
                    
                    /*
                    int len = name.length();
                    int idx = name.lastIndexOf('/', len-2);
                    String simpleName = (idx == -1) ? name : name.substring(idx);
                    AbsFolderElement element = m_children.get(simpleName); 
                    if (element == null) {
                        element = AbsFolderElement.createElement(this, zipfile, entry);
                        m_children.put(simpleName, element);
                    }
                    */
                    boolean dir = entry.isDirectory(); 
                    
                    //AbsFolderElement newElement = AbsFolderElement.createElement(this, zipfile, entry);
                    //String simpleName = newElement.getSimpleName();
                    
                    
                    //elements.add(element);
                } //end while
                
            } catch (IOException ex) {
            }
        } 
    }
    
    private void buildChildren(URL url) {
        String filename = url.getFile();
        char lastChar = filename.charAt(filename.length()-1); 
        
        if (lastChar == '/') {
            buildChildrenOfFolder(url); 
        } else {
            int idx = filename.lastIndexOf('.'); 
            String ext = (idx == -1) ? null : filename.substring(idx+1); 
            if ("zip".equals(ext) || "jar".equals(ext)) {
                buildChildrenOfArchive(url); 
            } //end if
        } //end if
    } //end buildChildren()

    private void buildChildrenOfArchive(URL url) {
        try {
            URLConnection con = url.openConnection();
            con.connect(); 
            
            InputStream is = con.getInputStream();
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry ze = null;
            ZipFileAndURLUnion composite = new ZipFileAndURLUnion(url);
            List<ZipEntry> zes = new ArrayList<ZipEntry>();
            
            while ((ze = zis.getNextEntry()) != null) {
                zes.add(ze);
            } //end for
            
            Map<String, FsEntry> topLevelEntries = new HashMap<String, FsEntry>();
            for (ZipEntry entry : zes) {
                String entryName = entry.getName();   
                FsFile.createFile(this, entryName, composite, entry);
               
                
               // FsEntry element = FsEntry.createElement(null, composite, entry);
                //m_children.put(entryName, element);
                System.out.println(entryName);
            } //end while
        } catch (IOException ex) {
            //do not add children 
            ex.printStackTrace();
         } //end try
            
        // TODO Auto-generated method stub
       // 
    }

    

    private void buildChildrenOfFolder(URL url) {
        try {
            URLConnection con = url.openConnection();
            con.connect(); 
            
            InputStreamReader ir = new InputStreamReader(con.getInputStream()); 
            BufferedReader br = new BufferedReader(ir);
            
            String line;
            
            for (; (line = br.readLine()) != null; ) {
                int idx = line.indexOf("href"); 
                if (idx != -1) {
                    int s1 = line.indexOf('\"', idx); 
                    int s2 = line.indexOf('\"', s1+1);
                    if ((s1 != -1) && (s2 > s1)) {
                        String res = line.substring(s1+1, s2);
                        int s3 = res.lastIndexOf('.'); 
                        if (s3 != -1) {
                            String ext = res.substring(s3+1); 
                            if ("zip".equals(ext) || ("jar".equals(ext))) {
                                String child = url.toString() + res;
                                //String child = this.toString() + res;
                                URL childURL = new URL(child);
                                FsFolder childFolder = new FsFolder(this, childURL);
                                m_children.put(child, childFolder);
                                System.out.println(child); 
                            }
                        }
                    }
                } //end if
            } //end for
         } catch (IOException ex) {
            //do not add children 
             ex.printStackTrace();
         } //end try
    }

    public String getPath() {
		String path = null;
		
		if (m_file != null) {
			path = m_file.getAbsolutePath(); 
		} else if (m_composite != null) {
			path = m_composite.getName();
		}
		
		return path;
	}

	@Override public void copyTo(FsFolder destFolder) throws IOException {
		// copy = null;
		
		//create file
		if (m_file != null) {
			//String foldername = m_file.getName();
			//File target = new File(destFolder.m_file, foldername);
			//copy = createFolder(destFolder, destFolder.m_file);
			
			List<FsEntry> elements = getFolderElements(); 
			for (FsEntry element : elements) {
				String foldername = element.getSimpleName();
				File destParent = destFolder.m_file;
				File newFile = new File(destParent, foldername);
				
				if (element instanceof FsFolder) {
					FsFolder copy = createFolder(destFolder, newFile);
					element.copyTo(copy);
				} else {
					//FileRef copy = FileRef.createFile(destFolder, newFile);
					element.copyTo(destFolder);
				}
			}
			
		} else if (m_composite != null) {
			//create folder		
			String entryName = m_entry.getName();
			int len = entryName.length();
			int idx = entryName.lastIndexOf('/', len-2);
			String suffix = entryName.substring(idx+1, len-1); 
			File folder = new File(destFolder.m_file, suffix);
			folder.mkdirs();
			FsFolder copy = FsFolder.createFolder(destFolder, folder); 
			
			//unzip in folder
			List<FsEntry> entries = getFolderElements();
			for (FsEntry entry : entries) {
				entry.copyTo(copy);
			}
			
			//unzipEntries(folderRef);
			
			
			
			//new ZipInputStream();
			
			/*
			File f = createTemporaryFile(folderRef, m_zipfile, m_entry);
			f.createNewFile();
			FileRef tempfile = FileRef.createFile(destFolder, f); 
			*/
		}
		
		
		
	}

	private void unzipEntries(FsFolder folderRef) throws IOException {
		String prefix = m_entry.getName();
		int len = prefix.length();
		Enumeration<? extends ZipEntry> enu = m_composite.entries();
		
		while (enu.hasMoreElements()) {
			ZipEntry entry = enu.nextElement(); 
			String name = entry.getName(); 
			
			if (! name.equals(prefix) && name.startsWith(prefix)) {
				String suffix = name.substring(len); 
				File file = new File(folderRef.m_file, suffix);
				
				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
					unzipEntry(entry, file);
				}
			}
		}
	} //end unzipEntries()

	private void unzipEntry(ZipEntry entry, File file) throws IOException {
		file.createNewFile();
		FileOutputStream out = new FileOutputStream(file);
		InputStream input = m_composite.getInputStream(entry); 
		byte[] buffer = new byte[8 * 1024];
		
        int count = 0;
        do {
             //out.write(buffer, 0, count);
             count = input.read(buffer, 0, buffer.length);
        } while (count != -1);
        
        out.close();
	}

	private File createTemporaryFile(FsFolder folderRef, ZipFile zipfile, ZipEntry entry) throws IOException {
		String prefix = entry.getName();
		int len = prefix.length();
		Enumeration<? extends ZipEntry> enu = zipfile.entries(); 
		
		while (enu.hasMoreElements()) {
			ZipEntry e = enu.nextElement();
			String name = e.getName(); 
			if (! name.equals(prefix) && name.startsWith(prefix)) {
				String suffix = name.substring(len); 
				File file = new File(folderRef.m_file, suffix);
				if (e.isDirectory()) {
					file.mkdirs();
				} else {
					file.createNewFile();
					InputStream input = zipfile.getInputStream(entry); 
					unzipTo(input, file); 
				}
			}
		} //end while
		
		
		List<FsEntry> elements = folderRef.getFolderElements();
			
		String entryname = entry.getName();
		String resourceURL = "jar:file:" + zipfile.getName() + "!/" + entryname;
		File tmlFile = JarUtil.createTemporaryFile(resourceURL); 
		return tmlFile;
	}

	private void unzipTo(InputStream input, File file) throws IOException {
		
         FileOutputStream out = new FileOutputStream(file);

         byte[] buffer = new byte[8 * 1024];
         int count = 0;
         do {
             out.write(buffer, 0, count);
             count = input.read(buffer, 0, buffer.length);
         } while (count != -1);

         out.close();
		
	}

	public FsFolder createFolder(String folderName) {
		FsFolder folder = null; 
		
		if (m_file != null) {
			File newFolder = new File(m_file, folderName);
			newFolder.mkdirs();
			folder = createFolder(this, newFolder);
		} 
		
		return folder;
	}









	



	

}
