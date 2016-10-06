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

package org.modelsphere.jack.plugins;

import java.text.MessageFormat;
import java.util.*;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.plugins.PluginDescriptor.PLUGIN_TYPE;
import org.modelsphere.jack.plugins.io.PluginLoader;
import org.modelsphere.jack.srtool.ApplicationContext;

public class PluginsReportBuilder {
    private static final String kPluginLoaded = LocaleMgr.misc.getString("PluginLoaded");
    private static final String kRepoLoaded = LocaleMgr.misc.getString("RepoLoaded");
    private static final String kPluginNotLoaded = LocaleMgr.misc.getString("PluginNotLoaded");
    private static final String kOtherPotentialErrors = LocaleMgr.misc
            .getString("OtherPotentialErrors");
    private static final String kRequiresBuild01 = LocaleMgr.misc.getString("RequireAppl0");

    StringBuffer text = new StringBuffer();

    public PluginsReportBuilder() {
    }

    public void write(PluginsRegistry registry) {
        List<PluginDescriptor> defaultPluginDescriptors = registry.getPluginDescriptors();

        // Use a StringBuffer instead of making an outrageous number of string concatenations.. [MS]
        text.append("<html>"); // NOT LOCALIZABLE - HTML Tag
        text.append("<body bgcolor=\"#FFFFFF\"> <font color=\"#000000\">"); // NOT LOCALIZABLE - HTML Tag
        text.append("<center><br><b><font size=+2>"); // NOT LOCALIZABLE - HTML Tag
        text.append(kPluginLoaded);
        text.append("</font></b></center>"); // NOT LOCALIZABLE - HTML Tag

        ArrayList<PluginDescriptor> sortedPluginInfos = new ArrayList<PluginDescriptor>(
                defaultPluginDescriptors);
        Collections.sort(sortedPluginInfos);

        Iterator<PluginDescriptor> pluginInfoIterator = sortedPluginInfos.iterator();

        while (pluginInfoIterator.hasNext()) {
            PluginDescriptor pluginDescriptor = pluginInfoIterator.next();
            if (!PluginLoader.isValid(pluginDescriptor))
                continue;
            if (pluginDescriptor.getType() == PLUGIN_TYPE.RULE)
                continue;
            if (pluginDescriptor.getPluginClass() == null)
                continue; // skip plugins not loaded (for added plugins)
            String line = (String) pluginDescriptor.getStatusFormattedText();
            if (line != null) {
                text.append("<br><p>" + line);
            }
        }

        text.append("<br><br><hr>"); // NOT LOCALIZABLE - HTML Tag
        text.append("<center><b><font size=+2>"); // NOT LOCALIZABLE - HTML Tag
        text.append(kRepoLoaded);
        text.append("</font></b></center>"); // NOT LOCALIZABLE - HTML Tag

        pluginInfoIterator = sortedPluginInfos.iterator();
        while (pluginInfoIterator.hasNext()) {
            PluginDescriptor pluginDescriptor = pluginInfoIterator.next();
            if (!PluginLoader.isValid(pluginDescriptor))
                continue;
            if (pluginDescriptor.getType() != PLUGIN_TYPE.RULE)
                continue;
            String line = (String) pluginDescriptor.getStatusFormattedText();
            if (line != null) {
                text.append("<br><p>" + line);
            }
        }

        boolean first = true;
        pluginInfoIterator = sortedPluginInfos.iterator();
        while (pluginInfoIterator.hasNext()) {
            PluginDescriptor pluginDescriptor = pluginInfoIterator.next();
            if (!PluginLoader.isValid(pluginDescriptor)) {
                if (pluginDescriptor.getContext().getBuildRequired() > ApplicationContext.APPLICATION_BUILD_ID) {
                    if (first) {
                        text.append("<br><br><hr>"); // NOT LOCALIZABLE - HTML Tag
                        text.append("<center><b><font size=+2>"); // NOT LOCALIZABLE - HTML Tag
                        text.append(kPluginNotLoaded);
                        text.append("</font></b></center>"); // NOT LOCALIZABLE - HTML Tag
                        first = false;
                    }
                    text.append("<br><p>"
                            + pluginDescriptor.getStatusFormattedText()
                            + "<br><b>&nbsp; &nbsp; &nbsp; <font color=\"AA0000\">"
                            + MessageFormat
                                    .format(kRequiresBuild01, new Object[] { pluginDescriptor
                                            .getContext().getBuildRequired() }) + "</font></b>");
                } else if (pluginDescriptor.getPluginClass() != null) {
                    if (first) {
                        text.append("<br><br><hr>"); // NOT LOCALIZABLE - HTML Tag
                        text.append("<center><b><font size=+2>"); // NOT LOCALIZABLE - HTML Tag
                        text.append(kPluginNotLoaded);
                        text.append("</font></b></center>"); // NOT LOCALIZABLE - HTML Tag
                        first = false;
                    }
                    text.append("<br><p>" + pluginDescriptor.getStatusFormattedText());
                }
            }
        }

        first = true;
        pluginInfoIterator = sortedPluginInfos.iterator();
        while (pluginInfoIterator.hasNext()) {
            PluginDescriptor pluginDescriptor = pluginInfoIterator.next();
            if (!PluginLoader.isValid(pluginDescriptor)
                    && pluginDescriptor.getPluginClass() == null
                    && pluginDescriptor.getContext().getBuildRequired() <= ApplicationContext.APPLICATION_BUILD_ID) {
                if (first) {
                    text.append("<br><br><hr>"); // NOT LOCALIZABLE - HTML Tag
                    text.append("<center><b><font size=+2>"); // NOT LOCALIZABLE - HTML Tag
                    text.append(kOtherPotentialErrors);
                    text.append("</font></b></center>"); // NOT LOCALIZABLE - HTML Tag
                    first = false;
                }
                text.append("<br><p>" + pluginDescriptor.getStatusFormattedText());
            }
        }

        text.append("<br><br><br>"); // NOT LOCALIZABLE - HTML Tag
        text.append("</body></html>"); // NOT LOCALIZABLE - HTML Tag
        text.append("</html>"); // NOT LOCALIZABLE - HTML Tag
    }

    private boolean check(PluginDescriptor descriptor) {
        // TODO - Move this somewhere else
        boolean checked = false;
        String className = descriptor.getClassName();
        if (className != null && className.trim().length() > 0)
            checked = true;
        return checked;
    }

    public String toString() {
        return text.toString();
    }

}
