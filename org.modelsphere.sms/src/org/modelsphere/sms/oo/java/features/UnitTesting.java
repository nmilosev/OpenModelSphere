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

package org.modelsphere.sms.oo.java.features;

import java.io.IOException;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRAM;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.sms.Application;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVPackage;

public abstract class UnitTesting {

    public UnitTesting() {
    }

    public abstract void showUsage();

    public abstract void reverseEngineer(DbJVClassModel classModel, String filename)
            throws DbException, IOException;

    public String getFilename(String[] args) {
        if (args.length != 1) {
            showUsage();
            throw new IllegalArgumentException("Wrong number of arguments"); // NOT
            // LOCALIZABLE
        }

        String filename = args[0];
        return filename;
    }

    public void perform(String filename) throws DbException, IOException {
        // Display unit test message
        System.out.println("***Warning : This is the main() method of a unit test."); // NOT
        // LOCALIZABLE,
        // stdout
        System.out
                .println("***Call org.modelsphere.sms.Application's main() to start the application."); // NOT
        // LOCALIZABLE,
        // stdout
        System.out.println();

        // Step 1 : create target package
        System.out.println("Initializing and creating a class model..."); // NOT
        // LOCALIZABLE,
        // stdout
        DbJVClassModel model = createTargetPackage();

        // Step 2 : reverse engineer a .java file
        System.out.println("Reverse Engineering..."); // NOT LOCALIZABLE, stdout
        reverseEngineer(model, filename);

        // Step 3 : output result
        System.out.println("Displaying results..."); // NOT LOCALIZABLE, stdout
        displayResults(model);
    }

    //
    // PRIVATE METHODS
    //

    // create target package
    private DbJVClassModel createTargetPackage() throws DbException {
        DbJVClassModel classModel = null;

        // init
        Application.initMeta();
        PropertiesManager.installDefaultPropertiesSet();
        Db db = new DbRAM();
        db.beginWriteTrans("test"); // NOT LOCALIZABLE, stdout
        DbSMSProject project = new DbSMSProject(db.getRoot());
        classModel = new DbJVClassModel(project);

        return classModel;
    } // end createTargetPackage()

    private static void displayResults(DbJVClassModel classModel) throws DbException {
        classModel.getDb().beginReadTrans();
        DbRelationN relationN = classModel.getComponents();
        DbEnumeration dbEnum = relationN.elements(DbJVClass.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVClass claz = (DbJVClass) dbEnum.nextElement();
            displayClass(claz);
        }
        dbEnum.close();

        dbEnum = relationN.elements(DbJVPackage.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVPackage subpack = (DbJVPackage) dbEnum.nextElement();
            displayPackage(subpack);
        }
        dbEnum.close();

        classModel.getDb().commitTrans();
    }

    private static void displayPackage(DbJVPackage pack) throws DbException {
        pack.getDb().beginReadTrans();
        DbRelationN relationN = pack.getComponents();
        DbEnumeration dbEnum = relationN.elements(DbJVClass.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVClass claz = (DbJVClass) dbEnum.nextElement();
            displayClass(claz);
        }
        dbEnum.close();

        dbEnum = relationN.elements(DbJVPackage.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVPackage subpack = (DbJVPackage) dbEnum.nextElement();
            displayPackage(subpack);
        }
        dbEnum.close();

        pack.getDb().commitTrans();
    }

    private static void displayClass(DbJVClass claz) throws DbException {
        boolean done = false;
        String qualifiedName = claz.getName();
        DbSMSSemanticalObject object = claz;

        while (!done) {
            DbObject composite = object.getComposite();
            if (composite instanceof DbJVPackage) {
                DbJVPackage pack = (DbJVPackage) composite;
                qualifiedName = pack.getName() + "." + qualifiedName;
                object = pack;
            } else {
                done = true;
            } // end if
        } // end while

        System.out.println("class = " + qualifiedName); // NOT LOCALIZABLE,
        // stdout
    }
}
