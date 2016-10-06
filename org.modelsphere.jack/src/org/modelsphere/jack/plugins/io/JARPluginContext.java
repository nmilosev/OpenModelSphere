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

package org.modelsphere.jack.plugins.io;

import java.net.URL;

import org.modelsphere.jack.plugins.PluginSignature;

public class JARPluginContext extends PluginContext {
    private static final String MANIFEST = "JARPluginContext.manifest";
    private static final String JAR = "JARPluginContext.jar";
    public static final String IMAGE_URL = "JARPluginContext.imageURL";

    private PluginSignature signature;

    public JARPluginContext(PluginLoader loader, URL pluginURL) {
        super(loader, pluginURL);
    }

    protected void setPluginManifest(PluginManifest manifest) {
        put(MANIFEST, manifest);
    }

    public PluginManifest getPluginManifest() {
        return (PluginManifest) get(MANIFEST);
    }

    protected void setJAR(URL jar) {
        put(JAR, jar);
    }

    public URL getJAR() {
        return (URL) get(JAR);
    }

    public PluginSignature getSignature() {
        return signature;
    }

    void setSignature(PluginSignature signature) {
        this.signature = signature;
    }

}
