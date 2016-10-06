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

package org.modelsphere.jack.srtool.forward;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.IntrospectionException;
import java.io.*;

public class ImportClause extends Rule {

    private String m_classBeanFile;
    private File m_classfile; // serialized
    private Serializable m_bean = null;
    private BeanInfo m_beanInfo = null;
    private String m_filename = null;

    public ImportClause(String classBeanFile, File tplFile) {
        // TODO: formatting error?
        setClassBeanFile(classBeanFile);
        int idx = m_classBeanFile.lastIndexOf('.');
        m_filename = m_classBeanFile.substring(idx + 1) + ".ser"; // NOT LOCALIZABLE, file extension
        File parent = tplFile.getParentFile();
        m_classfile = new File(parent, m_filename);
    }

    private void setClassBeanFile(String classBeanFile) {
        // remove enclosing quotes
        m_classBeanFile = classBeanFile.substring(1, classBeanFile.length() - 1);
    }

    public boolean expand(Writer writer, Serializable obj, RuleOptions options)
            throws RuleException {
        // try to load the class
        try {
            Class beanClass = Class.forName(m_classBeanFile);
            try {
                ObjectInputStream input = new ObjectInputStream(new FileInputStream(m_classfile));
                m_bean = (Serializable) input.readObject();
            } catch (IOException ex) {
                // cannot load it, create it
                m_bean = (Serializable) beanClass.newInstance();
            }
        } catch (ClassNotFoundException ex) {
            // throw new RuleException : File Not Found, file doesn't exist or
            // in a different directory from .tpl file.
        } catch (IllegalAccessException ex) {
            // throw new RuleException : .class ill-formed
        } catch (InstantiationException ex) {
            // throw new RuleException : .class ill-formed
        }

        try {
            m_beanInfo = Introspector.getBeanInfo(m_bean.getClass());
        } catch (IntrospectionException ex) {
            // throw new RuleException : .class ill-formed
        }

        return true;
    } // end expand()

    public Serializable getBean() {
        return m_bean;
    }

    public BeanInfo getBeanInfo() {
        return m_beanInfo;
    }

    public String getFileName() {
        return m_filename;
    }

} // end ImportClause
