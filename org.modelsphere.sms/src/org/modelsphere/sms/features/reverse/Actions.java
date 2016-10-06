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

package org.modelsphere.sms.features.reverse;

import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Vector;

import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.reverse.file.ReverseException;
import org.modelsphere.jack.srtool.reverse.file.ReverseParameters;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.oo.db.DbOOAbstractMethod;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVParameter;

public interface Actions extends org.modelsphere.jack.srtool.reverse.file.Actions {

    // DIFFERENT KINDS OF BLOCK OF STATEMENTS
    public static final int UNDEFINED_BLOCK = 0;
    public static final int METHOD_BLOCK = 1;
    public static final int INIT_BLOCK = 2;
    public static final int OBJECT_INIT_BLOCK = 3;

    public abstract void init(
            ObjectInputStream from_server,
            // org.modelsphere.sms.db.DbSMSProject destinationProject,
            DbSMSPackage targetPackage, String aFilename, java.io.InputStream input,
            Vector aVecOfIncompletedObject, Vector objectsToDeleteVector, HashMap diagMap,
            ReverseParameters parameters) throws org.modelsphere.jack.baseDb.db.DbException;

    // public abstract void processPackage(String packageName, String separator)
    // throws ReverseException;
    // public abstract void processImportDeclaration(String importDeclaration)
    // throws ReverseException;
    public abstract void processClass(DbJVClass clas, String className, short modifiers,
            int adtType, boolean isCompiledClass) throws ReverseException;

    public abstract void processInterface(DbJVClass interfac, String interfaceName,
            short modifiers, int adtType, boolean isCompiledClass) throws ReverseException;

    // public abstract void processExtension(String extensionClassName, String
    // separator)
    // throws ReverseException;
    // public abstract void processImplementation(String
    // implementationClassNames, String separator)
    // throws ReverseException;
    public abstract DbJVDataMember getCurrentField();

    public abstract void processField(DbJVDataMember field, String typeName, String fieldName,
            short modifiers, String optInitialValue) throws ReverseException;

    public abstract void processMethod(DbOOAbstractMethod oper, String resultTypeName,
            String methodName, short modifiers) throws ReverseException;

    // public abstract void startOfParameterClause();

    public abstract void processParameter(DbJVParameter param, String typeName, String paramName,
            short modifiers, boolean isOperationParam) throws ReverseException;

    // public abstract void incrementMethodComplexity();
    // public abstract void processException(String className)
    // throws ReverseException;
    // public abstract void processBuiltin(String primitiveName);
    // public abstract void processReferencedAdt(String className, int adtKind)
    // throws ReverseException;
    // public abstract org.modelsphere.sms.oo.java.db.DbJVDataMember
    // getCurrentField();
    // public abstract void terminateMethod()
    // throws ReverseException;
    // public abstract void endOfAdt();
    // public abstract void exit(java.io.ObjectOutputStream to_server);

    //
    // INNER CLASS
    //
    public class DefaultActions implements Actions {

        public void setDestinationProject(DbSemanticalObject semObj) {
        }

        public void init(
                ObjectInputStream from_server,
                // org.modelsphere.sms.db.DbSMSProject destinationProject,
                DbSMSPackage targetPack, String aFilename, java.io.InputStream input,
                Vector aVecOfIncompletedObject, Vector objectsToDeleteVector, HashMap diagMap,
                ReverseParameters parameters) throws org.modelsphere.jack.baseDb.db.DbException {
        }

        public void processPackage(String packageName, String separator) {
        }

        public void processImportDeclaration(String importDeclaration) {
        }

        public void processClass(DbJVClass clas, String className, short modifiers, int adtType,
                boolean isCompiledClass) {
        }

        public void processInterface(DbJVClass interfac, String interfaceName, short modifiers,
                int adtType, boolean isCompiledClass) {
        }

        public void processExtension(String extensionClassName, String separator) {
        }

        public void processImplementation(String implementationClassNames, String separator) {
        }

        public void processField(DbJVDataMember field, String typeName, String fieldName,
                short modifiers, String optInitialValue) {
            Debug.trace("Variable " + fieldName + " of type " + typeName);
        }

        public void processMethod(DbOOAbstractMethod oper, String resultTypeName,
                String methodName, short modifiers) {
        }

        public void startOfParameterClause() {
        }

        public void processParameter(DbJVParameter param, String typeName, String paramName,
                short modifiers, boolean isOperationParam) {
        }

        public void incrementMethodComplexity() {
        }

        public void processException(String className) {
        }

        public void processBuiltin(String primitiveName) {
        }

        public void processReferencedAdt(String className, int adtKind) {
        }

        public org.modelsphere.sms.oo.java.db.DbJVDataMember getCurrentField() {
            return null;
        }

        public void terminateMethod() {
        }

        public void endOfAdt() {
        }

        public void exit(java.io.ObjectOutputStream to_server) {
        }
    }
}
