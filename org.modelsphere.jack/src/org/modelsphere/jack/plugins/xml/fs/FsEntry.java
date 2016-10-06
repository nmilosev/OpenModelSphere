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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public abstract class FsEntry {
	
	protected File m_file = null; //atomic file
	protected ZipFileAndURLUnion m_composite = null;
	protected ZipEntry m_entry = null; //atomic entry
	protected FsFolder m_parent = null;
	//protected URL m_url; 
	
	public String getSimpleName() {
		String name; 
		
		if (m_file != null) {
			name = m_file.getName();
		} else if (m_entry != null) {
			String entryName = m_entry.getName();
			
			if (m_entry.isDirectory()) {
				int len = entryName.length();
				int idx = entryName.lastIndexOf('/', len-2);
				name = (idx == -1) ? entryName : entryName.substring(idx+1, len-1);
			} else {
				int idx = entryName.lastIndexOf('/');
				name = (idx == -1) ? entryName : entryName.substring(idx+1);
			}
		} else if (m_composite != null) {
		    name = m_composite.getName();
		    //name = m_url.getFile();
		} else {
		    name = "???"; 
		} //end if
		
		return name;
	}
	
	public String getQualifiedName() {
	    
		String name; 
		
		if (m_file != null) {
		    name = m_file.getPath();
		} else if (m_entry != null) {
		    name = m_entry.getName(); 
		} else if (m_composite != null) {
		    name = m_composite.toString();
		} else {
		    name = "???"; 
		} //end if
		
		return name;
	}
	
	@Override
	public String toString() {
		return getQualifiedName();
	}
	
	public abstract void copyTo(FsFolder copy) throws IOException;
	
	public abstract FsFolder getParent();

	public static FsEntry createElement(FsFolder parent, File file) {
		FsEntry element; 
		
		if (file.isDirectory()) {
			element = FsFolder.createFolder(parent, file);
		} else {
			String filename = file.getName();
			int idx = filename.lastIndexOf('.');
			String ext = (idx == -1) ? null : filename.substring(idx + 1);
			boolean zipped = "zip".equals(ext) || "jar".equals(ext);
			element = zipped ? FsFolder.createFolder(parent, file) : FsFile.createFile(parent, file);
		}
		return element;
	}
	
	/*
    public static FsEntry createElement(FsFolder parent, URL url, ZipEntry ze) {
        // TODO Auto-generated method stub
        return null;
    }*/
    
	
	public static FsEntry createElement(FsFolder parent, ZipFileAndURLUnion composite, ZipEntry entry) {
		FsEntry element = null; 
		
		String prefix = parent.getQualifiedName(); 
		
		boolean directory = entry.isDirectory();
		String entryName = entry.getName();
		
		
		if (entryName.startsWith(prefix)) {
			String subEntryName = prefix; 
			
			String suffix = entryName.substring(prefix.length()); 
			StringTokenizer st = new StringTokenizer(suffix, "/"); 
			
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				String elementName = (st.hasMoreTokens() || directory) ? (token + "/") : token;
				subEntryName += elementName; 
				
				if (st.hasMoreTokens()) {
					ZipEntry subEntry = composite.getEntry(subEntryName);
					if (subEntry == null) {
						int i=0;i++;
					}
					FsEntry elem = composite.getInstance(parent, subEntry); 
					
					if (elem == null) {
						elem = createFolder(parent, subEntryName, composite, subEntry);
					}
					
					if (elem instanceof FsFolder) {
						parent = (FsFolder)elem;
					} 
				} else {
					//boolean directory = entry.isDirectory();
					element = directory ? 
						createFolder(parent, elementName, composite, entry) : 
						FsFile.createFile(parent, elementName, composite, entry);
				}
			}
		} else {
			
			String subEntryName = "";; 

		StringTokenizer st = new StringTokenizer(entryName, "/"); 
		
		
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			String elementName = (st.hasMoreTokens() || directory) ? (token + "/") : token;
			subEntryName += elementName; 
			
			if (st.hasMoreTokens()) {
				ZipEntry subEntry = composite.getEntry(subEntryName);
				FsEntry elem = composite.getInstance(parent, subEntry);
				
				if (elem == null) {
					elem = createFolder(parent, subEntryName, composite, subEntry);
				}
				
				if (elem instanceof FsFolder) {
					parent = (FsFolder)elem;
				} 
			} else {
				//boolean directory = entry.isDirectory();
				element = directory ? 
					createFolder(parent, elementName, composite, entry) : 
					FsFile.createFile(parent, elementName, composite, entry);
			}
		} //end while
		}
		
		return element;
	}

	public URL toURL() {
		URL url = null; 
		 
		try {
			if (m_file != null) {
				url = m_file.toURI().toURL();
			} else if (m_entry != null) {
				String zipPath = m_composite.getName();
				String entry = m_entry.getName();
				url = new URL("jar:file:" + zipPath + "!/" + entry);
			} else if (m_composite != null) {
			    url = m_composite.toURL();
			}
		} catch (IOException ex) {
		//} catch (MalformedURLException ex) {
		}
		
		return url;
	}
	
	static FsEntry createFolderElement(FsFolder parent, String filename, ZipFileAndURLUnion composite, ZipEntry entry) {
		boolean directory = entry.isDirectory();
		
		if (! directory) {
			int idx = filename.lastIndexOf('.');
			String ext = (idx == -1) ? null : filename.substring(idx + 1);
			directory = "zip".equals(ext);
		}
		
		FsEntry element = directory ? createFolder(parent, filename, composite, entry) : FsFile.createFile(parent, filename, composite, entry); 
		return element;
	}

	private static FsFolder createFolder(FsFolder parent, String foldername, ZipFileAndURLUnion composite, ZipEntry entry) {
		FsFolder element = FsFolder.createFolder(parent, foldername, composite, entry); 
		return element;
	}
	
	//
	// inner class
	//
	static class ZipFileAndURLUnion {
	    private ZipFile _zipfile;
	    private URL _url; 

	    public ZipFileAndURLUnion(ZipFile file) {
	        _zipfile = file;
	    }

        public URL toURL() {
            URL url = null;
           
            try {
                if (_zipfile != null) {
                    String name =_zipfile.getName();
                    url = new URL(name); 
                } else if (_url != null) {
                    url = _url;
                }
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            
            return url;
        }

        public ZipFileAndURLUnion(URL url) {
	        _url = url;
        }
        
        public boolean isURL() {
            return (_url != null);
        }
        
        public String getName() {
            String name = null; 
            
            if (_zipfile != null) {
                name = _zipfile.getName(); 
            } else if (_url != null) {
                name = _url.getFile();
            }
            
            return name;
        }
        
        @Override
        public String toString() {
            String name = null; 
            
            if (_zipfile != null) {
                name = _zipfile.toString(); 
            } else if (_url != null) {
                name = _url.toString();
            }
            
            return name;
        }
        
        public ZipEntry getEntry(String entryName) {
            ZipEntry entry = null; 
            
            if (_zipfile != null) {
                entry = _zipfile.getEntry(entryName); 
            } else if (_url != null) {
                try {
                    URLConnection con = _url.openConnection();
                    con.connect(); 
                    InputStream is = con.getInputStream();
                    ZipInputStream zis = new ZipInputStream(is);
                    ZipEntry ze; 
                    
                    while ((ze = zis.getNextEntry()) != null) {
                        String name = ze.getName();
                        if (entryName.equals(name)) {
                            entry = ze;
                            break;
                        }
                    } //end while
                } catch (IOException ex) {
                    entry = null; 
                } //end try
            } //end if
            
            return entry;
        }
        
        public FsEntry getInstance(FsFolder parent, ZipEntry subEntry) {
            FsEntry elem = null; 
            
            if (_zipfile != null) {
                elem = parent.getInstance(_zipfile, subEntry);
            } else if (_url != null) {
                elem = parent.getInstance(null, subEntry);
                
                //elem = parent.getInstance(_url, subEntry);
            } else {
                elem = null; 
            }

            return elem;
        }

        public InputStream getInputStream(ZipEntry entry) throws IOException {
            InputStream input = null; 
            
            if (_zipfile != null) {
                input = _zipfile.getInputStream(entry); 
            } else if (_url != null) {
                try {
                    URLConnection con = _url.openConnection(); 
                    con.connect(); 
                    InputStream is = con.getInputStream();
                    ZipInputStream zis = new ZipInputStream(is);
                    ZipEntry ze; 
                    String entryName = entry.getName();
                    
                    
                    while ((ze = zis.getNextEntry()) != null) {
                        String name = ze.getName(); 
                        if (entryName.equals(name)) {
                            
                           // long size = ze.getSize(); 
                            
                            input = readContent(zis, ze); 
                           // String line = br.readLine(); 
                          //  System.out.println(line); 
                            
                           // long size = ze.getSize(); 
                            
                           // byte[] buffer = new byte[256];
                           // zis.read(buffer, 0, 80); 
                            
                            //size = ze.getSize(); 
                            //InputStream input = zipfile.getInputStream(entry); 
                            //input = zis;
                            break;
                        }
                    } //end while
                    
                    /*
                    InputStreamReader ir2 = new InputStreamReader(input);
                    BufferedReader br2 = new BufferedReader(ir2); 
                    String line = br2.readLine(); 
                    System.out.println(line); 
                    */
                } catch (IOException ex) {
                    input = null; 
                }
            }
            
            return input;
        }

        private static final int BUFFER_SIZE = 256;
        private InputStream readContent(ZipInputStream zis, ZipEntry ze) throws IOException {
            byte[] buffer = new byte[BUFFER_SIZE];
            //int offset = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            
            while (true) {
                int len = zis.read(buffer, 0, BUFFER_SIZE);
                
                if (len == -1) {
                    break;
                } else {
                    bos.write(buffer, 0, len); 
                    //offset += BUFFER_SIZE;
                }
            } //end while
            bos.close(); 
            
            byte[] content = bos.toByteArray(); 
            
            InputStream input = new ByteArrayInputStream(content);
            return input; 
            
            /*
            InputStreamReader ir = new InputStreamReader(bs); 
            BufferedReader br = new BufferedReader(ir);
            boolean end = false;
            String line; 
            
            while (! end) {
                line = br.readLine();
                System.out.println(line); 
                
                if (line == null) {
                    end = true;
                }
            } //end while
            */
            
        }

        public Enumeration<? extends ZipEntry> entries() {
            Enumeration<? extends ZipEntry> entries = null; 
            
            if (_zipfile != null) {
                entries = _zipfile.entries(); 
            } else if (_url != null) {
                try {
                    URLConnection con = _url.openConnection(); 
                    con.connect(); 
                    InputStream is = con.getInputStream();
                    ZipInputStream zis = new ZipInputStream(is);
                    List<ZipEntry> entryList = new ArrayList<ZipEntry>();
                    ZipEntry ze; 
                    
                    while ((ze = zis.getNextEntry()) != null) {
                        entryList.add(ze);           
                    } //end while
                
                    
                    entries = Collections.enumeration(entryList);
                } catch (IOException ex) {
                    entries = null; 
                }
            } //end if
            
            return entries;
        }

	}


	
	
	
	

	

}
