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
package com.neosapiens.plugins.codegen;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.util.JarUtil;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.or.db.DbORTable;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.neosapiens.plugins.codegen.wrappers.DbClassWrapper;

public class GenerationTarget {
    private File m_templateFile;
    private String m_targetName;
    private File m_root;

    private String m_description;

    public String getDescription() {
        return m_description;
    }

    private GenerationTarget(File template, File root, String targetName, String description) {
        m_templateFile = template;
        m_targetName = targetName;
        m_description = description;
        m_root = root;
    }

    public File getTemplateFile() {
        return m_templateFile;
    }

    public String getName() {
        return m_targetName;
    }

    @Override
    public String toString() {
        return getName();
    }

    private List<Directive> m_directives = new ArrayList<Directive>();

    public List<Directive> getDirectives() {
        return m_directives;
    }

    private static List<GenerationTarget> m_targets;

    public static List<GenerationTarget> getTargets() {
        if (m_targets != null) {
            return m_targets;
        }

        m_targets = new ArrayList<GenerationTarget>();

        URL location = getClassLocation();

        if (location == null)
            return m_targets;

        if (location.getProtocol().equals("file")) {
            String encoded = location.getFile();
            File classFileLocation = null;
            try {
                String decoded = URLDecoder.decode(encoded, "UTF-8"); //important
                classFileLocation = new File(decoded);

            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            } //end try
            if (classFileLocation != null) {
                File root = classFileLocation;
                String classname = GenerationTarget.class.getName();
                int levels = 1 + countNbChars(classname, '.');

                //iterate to the root
                for (int i = 0; i < levels; i++) {
                    root = root.getParentFile();
                } //end while

                scanFolder(classFileLocation.getParentFile(), root, m_targets);
            }
        } else if (location.getProtocol().equals("jar")) {
            String jarname = location.toExternalForm();
            
            try {
				jarname = URLDecoder.decode(jarname, "UTF-8");
				int index = jarname.indexOf(".jar!");
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
	            scanJar(new File(jarname), new File(System.getProperty("java.io.tmpdir")), m_targets);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
        }

        return m_targets;
    }

    private static int countNbChars(String classname, char c) {
        int nbChars = 0;

        for (int i = 0; i < classname.length(); i++) {
            if (c == classname.charAt(i)) {
                nbChars++;
            }
        }

        return nbChars;
    }

    private static void scanJar(File jarfile, File root, List<GenerationTarget> targets) {
        JarFile jar = null;
        try {
            jar = new JarFile(jarfile);
        } catch (IOException e1) {
        }
        if (jar == null)
            return;
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) entries.nextElement();
            if (jarEntry.isDirectory()) {
                continue;
            }
            // expand .vm also but do not create a target.  They are needed during generation.
            if (jarEntry.getName().endsWith("templates.xml") || jarEntry.getName().endsWith(".vm")) {
                File tempfile = null;
                try {
                    tempfile = JarUtil.createTemporaryFile(jar, jarEntry);
                } catch (IOException e) {
                }

                if (tempfile != null && jarEntry.getName().endsWith("templates.xml")) {
                    GenerationTarget target = createTarget(tempfile, root);
                    if (target != null) {
                        targets.add(target);
                    }
                }
            }
        }
    } //end scanJar

    private static void scanFolder(File folder, File root, List<GenerationTarget> targets) {
        File[] entries = folder.listFiles();
        for (File entry : entries) {
            if (entry.isDirectory()) {
                File file = new File(entry, "templates.xml");
                if (file.exists()) {
                    GenerationTarget target = createTarget(file, root);
                    if (target != null) {
                        targets.add(target);
                    }
                }
            }
        } //end for
    } //end scanFolder

    //factory
    private static GenerationTarget createTarget(File templateFile, File root) {
        GenerationTarget target;

        //read XML document
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.parse(templateFile);
            NodeList nodes = doc.getElementsByTagName("templates");

            //get name
            Node node = nodes.item(0);
            NamedNodeMap attrs = node.getAttributes();
            Node attr = attrs.getNamedItem("name");
            String templateName = attr.getNodeValue();

            //get description
            String description = "??";
            Locale locale = Locale.getDefault();
            String language = locale.getLanguage();
            nodes = doc.getElementsByTagName(language);

            int nb = nodes.getLength();
            for (int i = 0; i < nb; i++) {
                node = nodes.item(i);
                String text = node.getTextContent();
                text = text.replace("\n", " ");
                text = text.replaceAll("  ", " ");
                text = text.replaceAll("  ", " ");
                description = text.trim();
            }

            target = new GenerationTarget(templateFile, root, templateName, description);
            addDirectives(doc, nodes, target);

        } catch (ParserConfigurationException ex) {
            target = null;
        } catch (IOException ex) {
            target = null;
        } catch (SAXException ex) {
            target = null;
        } //end try

        return target;
    }

    private static void addDirectives(Document doc, NodeList nodes, GenerationTarget target) {
        //get packages directives
        nodes = doc.getElementsByTagName("package");
        int nb = nodes.getLength();
        for (int i = 0; i < nb; i++) {
            Node node = nodes.item(i);
            target.createPackageDirective(node);
        }

        //get class directives
        nodes = doc.getElementsByTagName("class");
        nb = nodes.getLength();
        for (int i = 0; i < nb; i++) {
            Node node = nodes.item(i);
            target.createClassDirective(node);
        }

        //get table directives
        nodes = doc.getElementsByTagName("table");
        nb = nodes.getLength();
        for (int i = 0; i < nb; i++) {
            Node node = nodes.item(i);
            target.createTableDirective(node);
        }

    } //end addDirectives

    private void createPackageDirective(Node node) {
        NamedNodeMap attrs = node.getAttributes();
        Node attr = attrs.getNamedItem("file");
        String templateFile = attr.getNodeValue();

        attr = attrs.getNamedItem("output");
        String pattern = attr.getNodeValue();

        String condition = null;

        Directive directive = new Directive(DbJVPackage.metaClass, templateFile, pattern, condition);
        m_directives.add(directive);
    }

    private void createClassDirective(Node node) {
        NamedNodeMap attrs = node.getAttributes();
        Node attr = attrs.getNamedItem("file");
        String templateFile = attr.getNodeValue();

        attr = attrs.getNamedItem("output");
        String pattern = attr.getNodeValue();

        attr = attrs.getNamedItem("condition");
        String condition = (attr == null) ? null : attr.getNodeValue();

        Directive directive = new Directive(DbJVClass.metaClass, templateFile, pattern, condition);
        m_directives.add(directive);
    }

    private void createTableDirective(Node node) {
        NamedNodeMap attrs = node.getAttributes();
        Node attr = attrs.getNamedItem("file");
        String templateFile = attr.getNodeValue();

        attr = attrs.getNamedItem("output");
        String pattern = attr.getNodeValue();

        attr = attrs.getNamedItem("condition");
        String condition = (attr == null) ? null : attr.getNodeValue();

        Directive directive = new Directive(DbORTable.metaClass, templateFile, pattern, condition);
        m_directives.add(directive);
    }

    static class Directive {
        protected MetaClass m_metaClass;
        protected String m_templateFile;
        protected String m_pattern;
        protected String m_condition;

        protected Directive(MetaClass metaClass, String templateFile, String pattern,
                String condition) {
            m_metaClass = metaClass;
            m_templateFile = templateFile;
            m_pattern = pattern;
            m_condition = condition;
        }

        public String getTemplateName() {
            return m_templateFile;
        }

        public String getOutputFilePattern() {
            return m_pattern;
        }

        public boolean checkClassCondition(DbClassWrapper claz) {
            boolean checked;

            if (m_condition == null) {
                checked = true;
            } else {
                try {
                    Method method = claz.getClass().getDeclaredMethod(m_condition, null);
                    Object result = method.invoke(claz, null);
                    if (result instanceof Boolean) {
                        Boolean b = (Boolean) result;
                        checked = (b.booleanValue());
                    } else {
                        checked = false;
                    } //end if
                } catch (NoSuchMethodException ex) {
                    checked = false;
                } catch (InvocationTargetException ex) {
                    checked = false;
                } catch (IllegalAccessException ex) {
                    checked = false;
                } //end try
            } //end if

            return checked;
        }
    }

    //
    // private methods
    //
    private static URL getClassLocation() {
        //get location of GenerationTarget.class
        Class<?> thisClass = GenerationTarget.class;
        URL url = thisClass.getResource(thisClass.getSimpleName() + ".class");
        return url;
    } //end getClassLocation()

    public boolean doesSupport(DbObject[] dbos) {
        boolean doesSupport = false;

        List<Directive> directives = getDirectives();
        for (Directive directive : directives) {
            for (DbObject dbo : dbos) {
                MetaClass mc = dbo.getMetaClass();

                //if directive.m_metaClass is mc, or component of mc
                boolean assignable = directive.m_metaClass.isAssignableFrom(mc);
                boolean compositeAllowed = directive.m_metaClass.compositeIsAllowed(mc);

                if (assignable || compositeAllowed) {
                    doesSupport = true;
                    break;
                } //end if
            } //end for 
        } //end for

        return doesSupport;
    }

    public File getRoot() {
        return m_root;
    }

}
