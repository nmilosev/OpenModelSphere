/*************************************************************************

Copyright (C) 2009 neosapiens inc./MSSS

This file is part of Link Report plug-in.

Link Report plug-in is free software; you can redistribute it and/or modify
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

Link Report plug-in has been developed by neosapiens inc. for
the purposes of the Ministère de la Santé et des Services Sociaux
du Québec (MSSS).
 
You can reach neosapiens inc. at: 
  http://www.neosapiens.com

 **********************************************************************/
package org.modelsphere.plugins.export.links;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.modelsphere.jack.util.JarUtil;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class GenerationTarget {

    private File m_templateFile;
    private String m_targetName;
    private File m_root;

    private GenerationTarget(File template, File root, String targetName) {
        m_templateFile = template;
        m_targetName = targetName;
        m_root = root;
    }

    public File getTemplateFile() {
        return m_templateFile;
    }

    public String getName() {
        return m_targetName;
    }

    public String toString() {
        return getName();
    }

    private List<ProjectDirective> directives = new ArrayList<ProjectDirective>();

    public List<ProjectDirective> getProjectDirectives() {
        return directives;
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
                int levels = countNbChars(classname, '.');

                //iterate to the root
                for (int i = 0; i < levels; i++) {
                    root = root.getParentFile();
                } //end while

                scanFolder(classFileLocation.getParentFile(), root, m_targets);
            }
        } else if (location.getProtocol().equals("jar")) {
            String jarname = location.toExternalForm();
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

            //get name
            NodeList nodes = doc.getElementsByTagName("templates");
            Node node = nodes.item(0);
            NamedNodeMap attrs = node.getAttributes();
            Node attr = attrs.getNamedItem("name");
            String templateName = attr.getNodeValue();
            target = new GenerationTarget(templateFile, root, templateName);

            //get class directives
            nodes = doc.getElementsByTagName("project");
            int nb = nodes.getLength();
            for (int i = 0; i < nb; i++) {
                node = nodes.item(i);
                createProjectDirective(target, node);
            }

        } catch (ParserConfigurationException ex) {
            target = null;
        } catch (IOException ex) {
            target = null;
        } catch (SAXException ex) {
            target = null;
        } //end try

        return target;
    }

    private static void createProjectDirective(GenerationTarget target, Node node) {
        NamedNodeMap attrs = node.getAttributes();
        Node attr = attrs.getNamedItem("file");
        String templateFile = attr.getNodeValue();

        attr = attrs.getNamedItem("baseName");
        String baseName = attr.getNodeValue();

        attr = attrs.getNamedItem("output");
        String pattern = attr.getNodeValue();

        ProjectDirective directive = new ProjectDirective(templateFile, baseName, pattern);
        target.directives.add(directive);
    }

    static class ProjectDirective {
        private String m_templateFile;
        private String m_baseName;
        private String m_pattern;

        private ProjectDirective(String templateFile, String baseName, String pattern) {
            m_templateFile = templateFile;
            m_baseName = baseName;
            m_pattern = pattern;
        }

        public String getTemplateName() {
            return m_templateFile;
        }

        public String getBaseName() {
            return m_baseName;
        }

        public String getOutputFilePattern() {
            return m_pattern;
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

    public File getRoot() {
        return m_root;
    }

}
