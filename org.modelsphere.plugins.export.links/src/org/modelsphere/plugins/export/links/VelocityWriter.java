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

import java.io.*;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.modelsphere.jack.gui.task.Controller;

public class VelocityWriter {
    private static VelocityEngine g_engine = null;

    private File m_templateFile;
    private File m_root;
    private Writer m_writer;
    private Template m_template = null;
    private VelocityContext m_context = null;

    public VelocityWriter() {
    }

    public VelocityWriter(File templateFile, File root, Writer writer) {
        m_templateFile = templateFile;
        m_writer = writer;
        m_root = root;
    } //end VelocityWriter()

    public void setVariable(String varname, Object variable, Controller controller) {
        initTemplate(controller);
        m_context.put(varname, variable);
    }

    public void writeObject(Controller controller) {
        initTemplate(controller);

        if (m_template != null) {

            try {
                m_template.merge(m_context, m_writer);
            } catch (Exception ex) {
                reportException(ex, controller);
                controller.incrementErrorsCounter();
            } //end try
        } //end if

    } //end writeObject()

    public void close() throws IOException {
        m_writer.close();
    }

    //
    // private methods
    // 

    private VelocityEngine startEngine(Controller controller) {
        VelocityEngine engine;

        try {
            //first, get and initialize an engine  
            engine = new VelocityEngine();

            Properties properties = new Properties();

            String path = m_root.getPath();

            properties.setProperty("file.resource.loader.path", path);
            engine.init(properties);

        } catch (NoClassDefFoundError er) {
            reportException(er, controller);

            controller.println("Quick Fix for .bat users");
            controller.println("  java.exe -classpath=<your-path> org.modelsphere.sms.Application");
            controller
                    .println("  <your-path> should include: velocity-1.6.1.jar, commons-collections-3.2.1.jar, commons-lang-2.4.jar");
            controller.println();

            controller.println("Quick Fix for Eclipse users:");
            controller
                    .println("  You should have: Run->Run Configurations->(Your Configuration Name)->Classpath tab->Add JARs");
            controller
                    .println("  add: velocity-1.6.1.jar, commons-collections-3.2.1.jar, commons-lang-2.4.jar");
            controller.println();

            controller.incrementErrorsCounter();
            engine = null;
        } catch (Exception ex) {
            reportException(ex, controller);
            controller.incrementErrorsCounter();
            engine = null;
        } //end try 

        return engine;
    } //end startEngine()

    private void reportException(Throwable th, Controller controller) {
        StringWriter sw = new StringWriter();
        th.printStackTrace(new PrintWriter(sw));
        String s = sw.toString();
        controller.println(s);
        controller.println();
    }

    private Template initTemplate(Controller controller) {

        //start the engine if it was not done before
        if (g_engine == null) {
            g_engine = startEngine(controller);
        }

        //next, get the Template  
        if (m_context == null) {
            m_context = new VelocityContext();
        }

        if (m_template == null) {
            try {
                m_template = getTemplate(g_engine);
            } catch (Exception ex) {
                reportException(ex, controller);
                controller.incrementErrorsCounter();
                m_template = null;
            }
        } //end try 

        return m_template;
    }

    private static String g_folderName = null;

    private Template getTemplate(VelocityEngine ve) throws Exception {
        if (g_folderName == null) {
            String classname = getClass().getName();
            int idx = classname.lastIndexOf('.');
            String packName = classname.substring(0, idx);
            g_folderName = packName.replace('.', '/') + "/";
        }

        String parent = m_templateFile.getParentFile().getName();
        String templateName = parent + "/" + m_templateFile.getName();

        Template t = ve.getTemplate(g_folderName + templateName);
        return t;
    }
}
