/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.oo.java;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.sms.Module;
import org.modelsphere.sms.actions.ActionFactory;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.oo.actions.OOActionConstants;
import org.modelsphere.sms.oo.graphic.MultiplicityLabel;
import org.modelsphere.sms.oo.graphic.OOAssociation;
import org.modelsphere.sms.oo.graphic.OOInheritance;
import org.modelsphere.sms.oo.graphic.OOPackage;
import org.modelsphere.sms.oo.graphic.RoleLabel;
import org.modelsphere.sms.oo.java.actions.JavaActionConstants;
import org.modelsphere.sms.oo.java.actions.JavaActionFactory;
import org.modelsphere.sms.oo.java.db.ApplClasses;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVCompilationUnit;
import org.modelsphere.sms.oo.java.db.DbJVConstructor;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVInitBlock;
import org.modelsphere.sms.oo.java.db.DbJVMethod;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.db.DbJVParameter;
import org.modelsphere.sms.oo.java.graphic.AdtBox;
import org.modelsphere.sms.oo.java.popup.JVPopupMenuPool;

public final class JavaModule extends Module {
    private static JavaModule singleton;

    static {
        singleton = new JavaModule();
    }

    private JavaModule() {
    }

    public static final JavaModule getSingleton() {
        return singleton;
    }

    // ///////////////////////////////////////////////////////////////////
    // Initialisation methods

    public void loadMeta() {
        ApplClasses.getFinalClasses();
    }

    public void initMeta() {
    }

    public void initIntegrity() {
        new JavaSemanticalIntegrity();
    }

    public void initToolkits() {
        new JVToolkit();
    }

    public void initAndInstallOtherToolBars(DefaultMainFrame frame) {
    }

    //
    // ///////////////////////////////////////////////////////////////////

    // ///////////////////////////////////////////////////////////////////
    // Method used during initialisation

    protected ActionFactory getActionFactory() {
        return JavaActionFactory.getSingleton();
    }

    public Object[] getPopupMenuMapping() {
        return new Object[] { DbJVPackage.class, JVPopupMenuPool.jvPackage, DbJVClassModel.class,
                JVPopupMenuPool.jvClassModel, DbJVMethod.class, JVPopupMenuPool.jvMethod,
                DbJVInitBlock.class, JVPopupMenuPool.jvInitBlock, DbJVCompilationUnit.class,
                JVPopupMenuPool.jvCompilationUnit, DbJVParameter.class,
                JVPopupMenuPool.jvParameter, DbJVConstructor.class, JVPopupMenuPool.jvMethod,
                DbJVClass.class, JVPopupMenuPool.jvClass, DbJVDataMember.class,
                JVPopupMenuPool.jvField, AdtBox.class, JVPopupMenuPool.jvClassGo, OOPackage.class,
                JVPopupMenuPool.jvPackageGo, OOInheritance.class, JVPopupMenuPool.jvInheritance,
                OOAssociation.class, JVPopupMenuPool.jvAssociation, MultiplicityLabel.class,
                JVPopupMenuPool.jvMultiplicityLabel, RoleLabel.class, JVPopupMenuPool.jvRoleLabel, };
    }

    //
    // ///////////////////////////////////////////////////////////////////

    public AbstractApplicationAction[] getModifierActions() {
        SMSActionsStore actionsStore = SMSActionsStore.getSingleton();
        return new AbstractApplicationAction[] {
                actionsStore.getAction(JavaActionConstants.JAVA_SET_VISIBILITY),
                actionsStore.getAction(OOActionConstants.OO_DIAGRAM_ABSTRACT_MODIFIER),
                actionsStore.getAction(JavaActionConstants.JAVA_DIAGRAM_STATIC_MODIFIER),
                actionsStore.getAction(JavaActionConstants.JAVA_DIAGRAM_FINAL_MODIFIER) };
    }

}
