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
package org.modelsphere.jack.plugins.xml;

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.ResourceBundle.Control;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.modelsphere.jack.baseDb.international.LocaleMgr;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.plugins.Plugin;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginConstants;
import org.modelsphere.jack.plugins.PluginDescriptor;
import org.modelsphere.jack.plugins.io.ImageUtilities;
import org.modelsphere.jack.plugins.io.PluginContext;
import org.modelsphere.jack.plugins.io.PluginLoader;
import org.modelsphere.jack.plugins.xml.extensions.AbstractPluginExtension;
import org.modelsphere.jack.plugins.xml.extensions.PopupExtension;
import org.modelsphere.jack.plugins.xml.fs.FsFile;
import org.modelsphere.jack.plugins.xml.fs.FsFolder;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlPluginDescriptor implements PluginDescriptor {
    public static final String LICENSE = "license"; // NOT LOCALIZABLE, keyword
    public static final String IMAGE = "image"; // NOT LOCALIZABLE, keyword
    public static final String EXTENSION = "extension"; // NOT LOCALIZABLE, keyword
    
    private static final String ACTION_LABEL = "actionLabel"; 
    private static final String FORMAT = "format";
    private static final float FORMAT_NUMBER = 2.0f;
    
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
	private Map<String, Object> m_attributes = null; 
	public  Map<String, Object> getAttributes() {
		if (m_attributes == null) {
			m_attributes = new HashMap<String, Object>();
		}
		
		return m_attributes;
	}
	
	private PluginContext m_context;
	private FsFile m_pluginXml;
	//private File homeDirectory;

	public static XmlPluginDescriptor create(FsFile pluginXmlFile) throws IOException {
		XmlPluginDescriptor descriptor = new XmlPluginDescriptor();
		
		if (descriptor.m_attributes != null) {
		    descriptor.m_attributes.clear();
		}
		
		descriptor.m_pluginXml = pluginXmlFile;
		boolean successful = descriptor.parseFile(pluginXmlFile);
		
		if (successful) {
			descriptor.enabled = true;
			return descriptor;
		} else {
			return null;
		}
	}
	private XmlPluginDescriptor() {}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	String getActionLabel() {
		Map<String, Object> attributes = getAttributes();
		String label = (String)attributes.get(ACTION_LABEL);
		if (label == null) {
			label = getName();
		}
		
		return label;
	}
	
	@Override
	public String getAuthor() {
		return getLocalizedString(PluginConstants.AUTHOR);
	}

	@Override
	public String getAuthorEmail() {
		return getLocalizedString(PluginConstants.AUTHOREMAIL);
	}

	@Override
	public String getAuthorURL() {
		return getLocalizedString(PluginConstants.AUTHORURL);
	}

	@Override
	public String getClassName() {
		Map<String, Object> attributes = getAttributes();
		String classname = (String) attributes.get(PluginConstants.ID);
		return classname;
	}

	@Override
	public PluginContext getContext() {
		if (m_context == null) {
			PluginLoader loader = PluginLoader.getBuiltInInstance();
			setContext(m_pluginXml, loader); 
		}
		
		return m_context;
	}

	@Override
	public String getDate() {
		return getLocalizedString(PluginConstants.DATE);
	}

	@Override
	public String getDescription() {
		return getLocalizedString(PluginConstants.DESCRIPTION);
	}

	@Override
	public Image getImage() {
		if (m_image == null) {
			PluginContext context = getContext();
			Map<String, Object> attributes = getAttributes();
			String imageResource = context.getLocalizedString((String) attributes.get(IMAGE));
	        if (imageResource != null && imageResource.trim().length() > 0) {
	            URL imageURL = null;
	            try {
	            	FsFolder parent = m_pluginXml.getParent(); 
	            	FsFile imageFile = (FsFile)parent.getNamedDescendent(imageResource); 
	            	
	                if (imageFile != null) {
	                	InputStream input = imageFile.openInputStream();
	                	m_image = ImageIO.read(input);
	                }
	            } catch (IOException e) {
	            }
	        }
		}
		
		if (m_image == null) {
			m_image = getDefaultPluginImage();
		}
		
		return m_image;
	}
	private Image m_image = null;
	
	public Image createImage(String imageName) {
		PluginContext context = getContext();
		Map<String, Object> attributes = getAttributes();
		Image image = null;
		
	    if (imageName != null && imageName.trim().length() > 0) {
            URL imageURL = null;
            try {
            	FsFolder parent = m_pluginXml.getParent(); 
            	FsFile imageFile = (FsFile)parent.getNamedDescendent(imageName); 
            	
                if (imageFile != null) {
                	InputStream input = imageFile.openInputStream();
                	image = ImageIO.read(input);
                }
            } catch (IOException e) {
	        }
		}
		
		if (image == null) {
			image = getDefaultPluginImage();
		}
		
		return image;
	}

	@Override
	public URL getLicenseURL() {
		if (licenseURL == null) {
			String licenseResource = getLocalizedString(LICENSE);
	        if (licenseResource != null && licenseResource.trim().length() > 0) {
	           // try {
	            	FsFolder parent = m_pluginXml.getParent(); 
	            	FsFile licenceFile = (FsFile)parent.getNamedDescendent(licenseResource); 
	            	licenseURL = licenceFile.toURL();
	            	
	            	
	            	
	                //File licenseFile = null; //new File(homeDirectory, licenseResource);
	                //licenseURL = licenseFile.toURI().toURL();
	            //} catch (MalformedURLException e) {
	            //}
	        }
		} 
		
		return licenseURL;
	}
	private URL licenseURL = null;
	
	String getMenuPath() {
		Map<String, Object> attributes = getAttributes();
		return (String)attributes.get("menuPath"); 
	}
	
	@Override
	public String getName() {
		 String name = getLocalizedString(PluginConstants.NAME);
		 return name;
	}
	
	@Override
	public Class<? extends Plugin> getPluginClass() {
		if (pluginClass == null) {
		    grantPermissions();
			String className = getClassName();
			PluginContext context = getContext();
			ClassLoader classLoader = context == null ? null : context.getClassLoader();

			if (classLoader == null) {
		    	classLoader = createClassLoader();
		    	context.setClassLoader(classLoader);
		    }
			
			try {
				pluginClass = (Class<? extends Plugin>) Class.forName(className, true, classLoader);
			} catch (ClassNotFoundException ex) {
				pluginClass = null;
			}
		}
		
		return pluginClass;
	}
	
	//grant all permissions on the clientside
	//required to dynamically load classes in a Java Web Start deployment
	private void grantPermissions() {
        Policy.setPolicy(new Policy() {
            @Override
            public PermissionCollection getPermissions(CodeSource src) {
                Permissions perms = new Permissions();
                perms.add(new AllPermission());
                return(perms);
            }

            public void refresh(){}
        });
    } //end grantPermissions()

    private Class<? extends Plugin> pluginClass;
	
	/*
	String getPopupClasses() {
		return attributes.get("popup"); 
	}
	
	String getPopupSubmenu() {
		return attributes.get("popupSubmenu"); 
	}
	*/

	@Override
	public String getStatusFormattedText() {
		String className = getClassName(); 
		String text = getStatusFormattedText(className); 
		return text; 
	}

	@Override
	public String getStatusText() {
		String className = getClassName(); 
		String text = getStatusText(className); 
		return text; 
	}
	
	public Class<?>[] getSupportedClasses() {
		if (m_supportedClasses == null) {
			List<AbstractPluginExtension> extensions = getPluginExtensions(); 
			List<Class<?>> supportedClassList = new ArrayList<Class<?>>(); 
			
			if (extensions == null) {
			    //Class<?>[] metaClasses = getSupportedClasses();
			    //for (Class<?> metaClass : metaClasses) {
                //    supportedClassList.add(metaClass);
                //} 
			} else {
			    for (AbstractPluginExtension extension : extensions) {
	                if (extension instanceof PopupExtension) {
	                    PopupExtension popupExtension = (PopupExtension)extension;
	                    Class<?>[] metaClasses = popupExtension.getSupportedMetaClasses();;
	                    for (Class<?> metaClass : metaClasses) {
	                        supportedClassList.add(metaClass);
	                    } 
	                }
	            } //end for
			} //end if
			
			m_supportedClasses = new Class<?>[supportedClassList.size()];
			supportedClassList.toArray(m_supportedClasses);
		}
		
		return m_supportedClasses;
	}
	private Class<?>[] m_supportedClasses = null;

	@Override
	public PLUGIN_TYPE getType() {
		Map<String, Object> attributes = getAttributes();
		PLUGIN_TYPE type = PLUGIN_TYPE.toType((String) attributes.get(PluginConstants.TYPE)); 
		return type;
	}

	@Override
	public String getVersion() {
		String version = getLocalizedString(PluginConstants.VERSION);
		if (version == null) {
			version = "Unspecified";
		}
		return version;
	}

	@Override
    public boolean isEnabled() {
        return enabled;
    }

	@Override
	public void setEnabled(boolean enabled) {
        if (this.enabled == enabled)
            return;
        this.enabled = enabled;
        propertyChangeSupport.firePropertyChange("enabled", !this.enabled, this.enabled);
    }
	
    private boolean enabled;
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	@Override
	public void setVersion(String version) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int compareTo(PluginDescriptor o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
    public final String toString() {
        String sign = "";
        sign += getName();
        sign += " ("; // NOT LOCALIZABLE

        if (getVersion() != null) {
            String v = "";
            StringTokenizer st = new StringTokenizer(getVersion(), "$", false); // NOT LOCALIZABLE - Source Safe Tag
            while (st.hasMoreTokens()) {
                v += st.nextToken();
            }
            sign += v;
            sign += " - "; // NOT LOCALIZABLE
        }

        if (getDate() != null)
            sign += getDate();
        else {
            String d = "";
            StringTokenizer st = new StringTokenizer(getDate(), "$", false); // NOT LOCALIZABLE - Source Safe Tag
            while (st.hasMoreTokens()) {
                d += st.nextToken();
            }
            sign += d;
        }

        sign += "), "; // NOT LOCALIZABLE
        sign += ((getAuthor() == null || getAuthor().trim().length() == 0) ? PluginDescriptor.KUnknownAuthor
                : getAuthor());

        return sign;
    }
	
	//
	// private methods
	//
	public String getStatusText(String className) {
        String temp = "";
        temp += getName();
        if (!enabled) {
            temp += " " + kDisabled;
        }
        temp += " ("; // NOT LOCALIZABLE

        String version = getVersion();
        if (version != null) {
            temp += version;
        }

        String date = getDate();
        if (date != null && date.length() > 0) {
            if (version != null)
                temp += " - "; // NOT LOCALIZABLE
            temp += date;
        }

        temp += "), "; // NOT LOCALIZABLE
        String author = getAuthor();
        if (author == null || author.trim().length() == 0) {
            temp += KUnknownAuthor;
        } else {
            temp += author;
        }

        temp += ", " + className; // NOT LOCALIZABLE - HTML Tag

        PluginContext context = getContext();
        URL url = context.getURL();
        if (url != null) {
            temp += "["; // NOT LOCALIZABLE 
            temp += new File(url.getFile()).getPath();
            temp += "], "; // NOT LOCALIZABLE
        }

        String status = context.getStatusText();
        if (status != null) {
            temp += ":  " + status; // NOT LOCALIZABLE - HTML Tag
        }
        return temp;
	}
	
    public String getStatusFormattedText(String className) {
        String temp = "";

        if (enabled)
            temp += "<b>"; // NOT LOCALIZABLE - HTML Tag
        temp += "<font color=\"#000080\">"; // NOT LOCALIZABLE - HTML Tag
        temp += getName();
        temp += "<font color=\"#000000\">"; // NOT LOCALIZABLE - HTML Tag
        if (enabled) {
            temp += "</b>"; // NOT LOCALIZABLE - HTML Tag
        } else {
            temp += " - " + kDisabled;
        }
        temp += " ("; // NOT LOCALIZABLE

        String version = getVersion();
        if (version != null) {
            temp += version;
        }

        String date = getDate();
        if (date != null && date.length() > 0) {
            if (version != null)
                temp += " - "; // NOT LOCALIZABLE
            temp += date;
        }

        temp += ")"; // NOT LOCALIZABLE
        temp += "<br>&nbsp; &nbsp; &nbsp; <b>"; // NOT LOCALIZABLE - HTML Tag

        String author = getAuthor();
        if (author == null || author.trim().length() == 0) {
            temp += KUnknownAuthor;
        } else {
            temp += author;
        }
        temp += "</b>"; // NOT LOCALIZABLE - HTML Tag

        temp += "<br>&nbsp; &nbsp; &nbsp; <i>"; // NOT LOCALIZABLE - HTML Tag
        temp += className;
        temp += "</i>"; // NOT LOCALIZABLE

        PluginContext context = getContext();
        URL url = context.getURL();
        if (url != null) {
            temp += "<br>&nbsp; &nbsp; &nbsp; <i>["; // NOT LOCALIZABLE - HTML Tag
            temp += new File(url.getFile()).getPath();
            temp += "]</i>"; // NOT LOCALIZABLE
        }

        String status = context.getStatusText();
        if (status != null) {
            temp += "<br><b>&nbsp; &nbsp; &nbsp; <font color=\"AA0000\">" + status + "</font></b>";// NOT LOCALIZABLE - HTML Tag
        }
        return temp;
    }
    
	private void setContext(FsFile pluginXmlFile, PluginLoader loader) {
		m_context = loader.createPluginContext(null);
		Map<String, Object> attributes = getAttributes();
		String baseName = (String)attributes.get(PluginConstants.RESOURCEBUNDLE);
		
        if (baseName != null && baseName.trim().length() > 0) {
            try {
            	//String ressName = homeDirectory.getPath() + "_" + baseName; 
            	Locale locale = LocaleMgr.getLocaleFromPreferences(Locale.getDefault(), true); 
            	
            	XMLPluginFileResourceBundleControl control = new XMLPluginFileResourceBundleControl(pluginXmlFile, baseName);
            	String ressName = control.getResourceName(); 
            	ResourceBundle bundle = ResourceBundle.getBundle(ressName, locale, control); 
            	
            	/*
            	InputStream input = XmlPluginUtility.openResource(pluginXmlFile, baseName);
            	*/
            	
            	
                m_context.setResourceBundle(bundle);
            	//control.close(); //closes input
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        String sbuild = (String) attributes.get(PluginConstants.REQUIREDBUILD);
        if (sbuild != null) {
            try {
                int build = Integer.parseInt(sbuild);
                m_context.setBuildRequired(build);
            } catch (Exception e) {
            }
        }
	}

	private boolean parseFile(FsFile pluginXmlFile) throws IOException {
		boolean successful = true; 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        
        try {
            documentBuilder = factory.newDocumentBuilder();
            InputStream input = pluginXmlFile.openInputStream();
            
           // InputStreamReader ir = new InputStreamReader(input);
            //BufferedReader bf = new BufferedReader(ir);
            //String line = bf.readLine(); 
            //System.out.println(line); 
            
            Document document = documentBuilder.parse(input);
            NodeList nodes = document.getChildNodes();
            Node pluginNode = null;
            int count = nodes.getLength();
            for (int i = 0; i < count; i++) {
                Node childNode = nodes.item(i);
                if (childNode.getNodeType() == Node.ELEMENT_NODE
                        && childNode.getNodeName().equals("plugin")) {
                    pluginNode = childNode;
                    break;
                }
            }

            if (pluginNode != null) {
            	Map<String, Object> attributes = getAttributes();
                attributes = loadAttributes(pluginNode);
                successful = (attributes != null);
            } else {
            	successful = false;
            }
            
            input.close();
        } catch (ParserConfigurationException e) {
        	successful = false;
        } catch (SAXException e) {
        	successful = false;
        }
        
        return successful;
	}
	
	private Map<String, Object> loadAttributes(Node pluginNode) {
        if (pluginNode == null)
            return null;

        Map<String, Object> extendedAttributes = getAttributes();
        NamedNodeMap attributes = pluginNode.getAttributes();
        if (attributes != null) {
            // set mandatory attributes
        	Node formatNode = attributes.getNamedItem(FORMAT);
        	String format = (formatNode == null) ? null : formatNode.getNodeValue(); 
        	float formatNumber = (format == null) ? 1.0f : Float.parseFloat(format);
        	
        	if (formatNumber < FORMAT_NUMBER) {
        		return null;
        	}
        	
            Node classNameNode = attributes.getNamedItem(PluginConstants.ID);
            if (classNameNode != null) {
                extendedAttributes.put(PluginConstants.ID, classNameNode.getNodeValue());
            }
        }

        NodeList children = pluginNode.getChildNodes();
        int count = children.getLength();
        
        //read resource bundle
        for (int i = 0; i < count; i++) {
            Node childNode = children.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE)
                continue;
            String name = childNode.getNodeName();
            if (name == null)
                continue;
            
            if (PluginConstants.RESOURCEBUNDLE.equals(name)) {
            	loadAttribute(extendedAttributes, childNode, name);
            	PluginLoader loader = PluginLoader.getClassInstance();
            	setContext(m_pluginXml, loader);
            }
            
        } //end for
        
        //read other attributes
        for (int i = 0; i < count; i++) {
            Node childNode = children.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE)
                continue;
            String name = childNode.getNodeName();
            if (name == null)
                continue;
   
            if (! PluginConstants.RESOURCEBUNDLE.equals(name)) {
            	loadAttribute(extendedAttributes, childNode, name);
            }
        }

        return extendedAttributes;
    }
	
	private void loadAttribute(Map<String, Object> extendedAttributes, Node childNode, String name) {
		Object value = null;
		String text = childNode.getTextContent();
		
		if (EXTENSION.equals(name)) {
        	value = readExtension(childNode);
        } else {
	    	if (text == null || text.length() == 0) {
	    		value = null;
	    	} else {
	    		value = text;
	    	}
        } //end if
    	
    	if (value != null) {
    		if (value instanceof AbstractPluginExtension) {
    			AbstractPluginExtension extension = (AbstractPluginExtension)value;
    			List<AbstractPluginExtension> extensions = (List<AbstractPluginExtension>)extendedAttributes.get(name);
    			if (extensions == null) {
    				extensions = new ArrayList<AbstractPluginExtension>();
    				extendedAttributes.put(name, extensions);
    			}
    			
    			extensions.add(extension);
    		} else {
    			extendedAttributes.put(name, value);
    		}
        } //end if
	}
	
	private AbstractPluginExtension readExtension(Node childNode) {
		AbstractPluginExtension extension;
		NamedNodeMap attrs = childNode.getAttributes();
		Node node = attrs.getNamedItem("className");
		
		if (node == null) {
			return null;
		}
		
		String classname = node.getNodeValue();
		Map<String, String> params = readParameters(childNode);
		
		try {
			Class<?> claz = Class.forName(classname);
			Constructor<?> constr = claz.getConstructor(XmlPluginDescriptor.class, Map.class);
			extension = (AbstractPluginExtension)constr.newInstance(this, params);
		} catch (ClassNotFoundException ex) {
			extension = null;
		} catch (NoSuchMethodException ex) {
			extension = null;
		} catch (InvocationTargetException ex) {
			extension = null;
		} catch (IllegalAccessException ex) {
			extension = null;
		} catch (InstantiationException ex) {
			extension = null;
		} //end try
		
		return extension;
	}
	
	private static Map<String, String> readParameters(Node node) {
		Map<String, String> parameters = new HashMap<String, String>();
		NodeList children = node.getChildNodes();
        int count = children.getLength();
        for (int i = 0; i < count; i++) {
            Node childNode = children.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE)
                continue;
            
            String name = childNode.getNodeName();
            String value = childNode.getTextContent();
            parameters.put(name, value);
        }
        
		return parameters;
	}
	
	private Map<String, String> localizedMap = new HashMap<String, String>();
	private String getLocalizedString(String attrName) {
		String localized = localizedMap.get(attrName); 
		if (localized == null) {
			PluginContext context = getContext();
			Map<String, Object> attributes = getAttributes();
			localized = context.getLocalizedString((String) attributes.get(attrName));
			localizedMap.put(attrName, localized);
		}
		
		return localized;
	}
	
	private static Image DEFAULT_IMAGE = null;
	private static Image getDefaultPluginImage() {
		if (DEFAULT_IMAGE == null) {
			URL url = org.modelsphere.jack.international.LocaleMgr.class.getResource("resources/plugin_32.png");
			DEFAULT_IMAGE = ImageUtilities.loadImage(url);
		}
		
		return DEFAULT_IMAGE;
	}


	private ClassLoader createClassLoader() {
		ClassLoader classLoader = null;
		PluginContext context = getContext();
		URL url = context.getURL();
		
		if (url == null) {
			FsFolder parent = m_pluginXml.getParent(); 
			url = parent.toURL();
			
			/*
			//File home = getHomeDirectory();
			try {
				url = home.toURI().toURL();
			} catch (MalformedURLException ex) {
				url = null;
			}*/
		}
		
        if (url != null) {
            //get classloader from a loaded class
            //ensure Plugin2 is loaded before creating the URLClassLoader
            //required by Java Web Start
            ClassLoader parentCL = Plugin2.class.getClassLoader();
            classLoader = new URLClassLoader(new URL[] { url }, parentCL);
        } else {
            System.out.println("Undefined class loader for " + this);
            classLoader = null;
        }
        
		return classLoader;
	}

	public XmlPlugin getPluginInstance() {
		if (m_plugin == null) {
			m_plugin =  new XmlPlugin(this);
		}

		return m_plugin;
	}
	private XmlPlugin m_plugin;

	public List<AbstractPluginExtension> getPluginExtensions() {
		Map<String, Object> attributes = getAttributes();
		List<AbstractPluginExtension> extensions = (List<AbstractPluginExtension>)attributes.get(EXTENSION);
		return extensions;
	}

	/*
	public String getPopupSubmenu3() {
		String popupSubmenu = null;
		
		List<AbstractPluginExtension> extensions = getPluginExtensions();

		if (extension instanceof PopupExtension) {
			PopupExtension popupExtension = (PopupExtension)extension;
			popupSubmenu = popupExtension.getPopupSubmenu();
		}
		
		return popupSubmenu;
	}*/





}
