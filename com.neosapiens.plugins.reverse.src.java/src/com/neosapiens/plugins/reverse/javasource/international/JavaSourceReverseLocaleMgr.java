package com.neosapiens.plugins.reverse.javasource.international;

import org.modelsphere.sms.international.LocaleMgr;

public class JavaSourceReverseLocaleMgr extends LocaleMgr {

    public static final JavaSourceReverseLocaleMgr misc = new JavaSourceReverseLocaleMgr("com.neosapiens.plugins.reverse.javasource.international.MiscResources");

    protected JavaSourceReverseLocaleMgr(String resName) {
        super(resName);
    }
}
