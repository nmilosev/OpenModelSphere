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
package org.modelsphere.jack.plugins.xml.extensions;

import java.util.HashMap;
import java.util.Map;

import org.modelsphere.jack.plugins.io.PluginContext;
import org.modelsphere.jack.plugins.xml.XmlPluginDescriptor;

public abstract class AbstractPluginExtension {

    protected XmlPluginDescriptor _xmlDescriptor;
    private Map<String, String> m_parameters = new HashMap<String, String>();

    protected AbstractPluginExtension(XmlPluginDescriptor xmlDescriptor, Map<String, String> parameters) {
        _xmlDescriptor = xmlDescriptor;
        m_parameters = parameters;
    }

    public abstract void createPluginAction(XmlPluginDescriptor pluginDescriptor);

    public String getParameter(String key) {
        String value = getLocalizedString(_xmlDescriptor, key);
        return value;
    }

    private Map<String, String> localizedMap = new HashMap<String, String>();
    private String getLocalizedString(XmlPluginDescriptor pluginDescriptor, String attrName) {
        String localized = localizedMap.get(attrName); 

        if (localized == null) {
            String paramName = (String)m_parameters.get(attrName);
            PluginContext context = pluginDescriptor.getContext();
            localized = context.getLocalizedString(paramName);
            localizedMap.put(attrName, localized);
        }

        return localized;
    }

    public String getActionKey() {
        Class<?> claz = _xmlDescriptor.getPluginClass();
        String actionName = claz.getName();
        return actionName;
    }



}
