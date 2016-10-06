package org.modelsphere.sms.oo.international.resources;

import java.net.URL;

public class Resource /* extends org.modelsphere.jack.awt.Resource */{

    public static URL getUrl(String filename) {
        URL url = Resource.class.getResource(filename);
        return url;
    }

}
