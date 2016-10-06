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
package org.modelsphere.sms.oo.java.validation;

import java.awt.Cursor;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.modelsphere.jack.awt.TextViewerFrame;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.oo.db.DbOOAbstractMethod;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.util.AnyAdt;
import org.modelsphere.sms.oo.java.db.util.InheritanceCircularityListener;
import org.modelsphere.sms.oo.java.international.LocaleMgr;

// do Java Validation for package classes (not for inner classes)

public class JavaValidation implements HyperlinkListener {

    private static final String URL_TOKENS = ":.,";
    private static final String INTERNAL_URL_TOKENS = "\1\2\3";
    private static final String UNNAMED = "???"; //language-independant

    private static UrlComponentIterator urlCompIterator = new UrlComponentIterator();

    private TextViewerFrame textFrame;
    private StringWriter writer = new StringWriter();
    private HashSet classes = new HashSet();
    private int circularityNbErrors = 0;
    private Db validatingDb = null;

    public JavaValidation() {
    }

    //return the number of errors founds
    public boolean execute(DbObject[] roots) throws DbException {
        int nbErrorsFound = 0;
        validatingDb = roots[0].getDb();
        for (int i = 0; i < roots.length; i++) {
            if (roots[i] instanceof DbJVClass)
                classes.add(roots[i]);
            AnyAdt.getClassesAux(classes, roots[i]);
        }
        boolean validationFailed = doJavaValidation();
        return validationFailed;
    } //end execute()

    private boolean doJavaValidation() throws DbException {
        /*
         * additional classes to validate: - ancestors - types of fields, return types of methods
         * and types of parameters // toVerify:tous les classes additionnels sont ajoutes aux
         * classes a valider, quelques soit leur accessibility (JBuilder valide l'accessility des
         * classes utilises, et ne les valide eux-memes que s'ils sont accessibles).
         */
        findAllClassesToValidate(classes);

        // validation: classes circularity errors (an adt cannot be a subadt of itself, directly or indirectly)
        Vector validatedClasses = new Vector();
        Vector circularClasses = new Vector();
        ArrayList sortedElements = new ArrayList(classes.size());

        for (Iterator iter = classes.iterator(); iter.hasNext();) {
            DbJVClass aClass = (DbJVClass) iter.next();
            sortedElements.add(new DefaultComparableElement(aClass, aClass.getName()));
        }
        Collections.sort(sortedElements, new CollationComparator());

        DbJVClass[] sortedClasses = new DbJVClass[sortedElements.size()];
        for (int i = 0; i < sortedElements.size(); i++) {
            sortedClasses[i] = (DbJVClass) ((DefaultComparableElement) sortedElements.get(i)).object;
        }

        for (int i = 0; i < sortedClasses.length; i++) {
            AnyAdt.validateCircularity(sortedClasses[i], circularClasses, new Vector(),
                    validatedClasses, new InheritanceCircularityListener() {
                        public void circularityPathFound(Vector circularityPath) throws DbException {
                            writeCircularityError(circularityPath);
                        }
                    });
        }
        // validation: classes errors
        for (int i = 0; i < sortedClasses.length; i++) {
            new AdtJavaValidation(sortedClasses[i], circularClasses.contains(sortedClasses[i]),
                    writer);
        }

        // Report java errors, if any
        boolean validationFailed = (writer.getBuffer().length() > 0);
        if (!validationFailed) {
            writer.write(LocaleMgr.validation.getString("NoJavaErrors"));
        }
        writer.write(LocaleMgr.validation.getString("JavaErrors"));

        String text = "<body bgcolor='#ffffff'>" + writer.toString() + "</body>"; //forces a white background [MS]
        textFrame = new TextViewerFrame(LocaleMgr.validation.getString("JavaValidation"), text,
                true);
        ((JEditorPane) textFrame.getTextPanel()).addHyperlinkListener(this);
        textFrame.showTextViewerFrame(MainFrame.getSingleton().getJDesktopPane(),
                MainFrame.PROPERTY_LAYER);

        return validationFailed;
    } //end doJavaValidation()

    private void findAllClassesToValidate(HashSet initialClasses) throws DbException {
        HashSet ancestors = new HashSet();
        HashSet typingClasses = new HashSet();
        HashSet throwedExceptions = new HashSet();
        for (Iterator iter = initialClasses.iterator(); iter.hasNext();) {
            DbJVClass aClass = (DbJVClass) iter.next();
            AnyAdt.findAncestors(aClass, ancestors);
            AnyAdt.findTypingAdts(aClass, typingClasses);
            AnyAdt.findThrowedExceptions(aClass, throwedExceptions);
        }
        HashSet additionalClasses = new HashSet();
        addToClasses(ancestors, additionalClasses);
        addToClasses(typingClasses, additionalClasses);
        addToClasses(throwedExceptions, additionalClasses);
        if (!additionalClasses.isEmpty())
            findAllClassesToValidate(additionalClasses);
    }

    private void addToClasses(HashSet classesToAdd, HashSet additionalClasses) {
        for (Iterator iter = classesToAdd.iterator(); iter.hasNext();) {
            Object obj = iter.next();
            if (obj instanceof DbJVClass) { // can be DbJVPrimitiveType
                DbJVClass aClass = (DbJVClass) obj;
                if (!classes.contains(aClass)) {
                    classes.add(aClass);
                    additionalClasses.add(aClass);
                }
            }
        }
    }

    private void writeCircularityErrorTitle() {
        writer.write(LocaleMgr.validation.getString("ClassesInterfacesCircularity"));
    }

    private void writeCircularityError(Vector adtsPath) throws DbException {
        if (circularityNbErrors == 0) {
            writeCircularityErrorTitle();
        }
        circularityNbErrors++;

        String pattern = LocaleMgr.validation.getString("ErrorNbr0");
        writer.write(MessageFormat.format(pattern,
                new Object[] { new Integer(circularityNbErrors) }));
        writer.write(LocaleMgr.validation.getString("CyclicInheritanceByThePath"));
        boolean first = true;
        for (Enumeration e = adtsPath.elements(); e.hasMoreElements();) {
            if (!first) {
                writer.write(LocaleMgr.validation.getString("PathEnum"));
            }
            writer.write(getHyperLinkSemanticalObject((DbJVClass) e.nextElement()));
            first = false;
        }
        writer.write(LocaleMgr.validation.getString("Return"));
    }

    public static String getHyperLinkSemanticalObject(DbSemanticalObject dbo) throws DbException {
        return getHyperLinkSemanticalObject(dbo, DbObject.LONG_FORM, null);
    }

    public static String getHyperLinkSemanticalObject(DbSemanticalObject dbo, String userName)
            throws DbException {
        return getHyperLinkSemanticalObject(dbo, DbObject.LONG_FORM, userName);
    }

    public static String getHyperLinkSemanticalObject(DbSemanticalObject dbo, int form)
            throws DbException {
        return getHyperLinkSemanticalObject(dbo, form, null);
    }

    private static String getHyperLinkSemanticalObject(DbSemanticalObject dbo, int form,
            String userNameParam) throws DbException {
        String hrefName = buildFullNameString(urlCompIterator, dbo);
        String userName;
        String projectName = toInternalForm(dbo.getProject().getSemanticalName(DbObject.SHORT_FORM));
        projectName += ",0";
        String pattern;

        if (userNameParam == null) {
            if (dbo instanceof DbOOAbstractMethod) {
                userName = ((DbOOAbstractMethod) dbo).buildSignature(DbObject.SHORT_FORM);
            } else {
                userName = dbo.getSemanticalName(form);
            }
        } else
            userName = userNameParam;

        userName = replaceBlanks(userName);

        String hyperlinkFmt = "<a href=\"{0}:{1}\">{2}</a>"; //NOT LOCALIZABLE, hyperklink
        String hyperlink = MessageFormat.format(hyperlinkFmt, new Object[] { projectName, hrefName,
                userName });
        return hyperlink;
    }

    /*
     * Replace blanks by '???' in qualified names, so: ''Class'' returns ''Class'' '''' returns
     * ''???'' ''Class.'' returns ''Class.???'' ''Class..'' returns ''Class.???.???''
     * 
     * and so on
     */
    private static String replaceBlanks(String userName) {
        String word = UNNAMED;
        String newUserName = "";

        StringTokenizer st = new StringTokenizer(userName, ".", true);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals(".")) {
                if (!word.equals(UNNAMED)) {
                    newUserName = newUserName + word + ".";
                    word = UNNAMED;
                } else {
                    word = UNNAMED + ".";
                } //end if

                if (!word.equals(UNNAMED)) {
                    newUserName = newUserName + word;
                    word = UNNAMED;
                } //end if
            } else {
                word = token;
            } //end if
        } //end while

        newUserName = newUserName + word;
        return newUserName;
    } //end replaceBlanks()

    // This is a temporary patch ... until DbSemanticalObject will provide a way to build a standard naming convention for external needs
    public static final String buildFullNameString(DbObject.ComponentIterator compIter,
            DbSemanticalObject dbo) throws DbException {
        String fullName = dbo.getName();
        if (fullName == null)
            fullName = UNNAMED;

        if (fullName.equals("")) {
            fullName = UNNAMED;
        } //end if

        if (compIter != null)
            fullName = compIter.modifiedComponentName(dbo, fullName);

        if (!(dbo instanceof DbProject)) {
            DbSemanticalObject parent = dbo;
            while (true) {
                parent = (DbSemanticalObject) parent.getComposite();
                if (parent instanceof DbProject)
                    break;
                String parentName = parent.getName();
                if (parentName == null)
                    parentName = UNNAMED;

                if (parentName.equals("")) {
                    parentName = UNNAMED;
                } //end if

                if (compIter != null)
                    parentName = compIter.modifiedComponentName(parent, parentName);
                fullName = parentName + "." + fullName;
            }
        }
        return fullName;
    }

    public DbObject findDbObjectFromHyperLink(Db db, String hl) throws DbException {
        DbObject dbo = null;
        StringTokenizer st = new StringTokenizer(hl, URL_TOKENS);
        if (st.hasMoreTokens())
            dbo = findDbObjectFromToken(st, db.getRoot());
        return dbo;
    }

    private DbObject findDbObjectFromToken(StringTokenizer st, DbObject parent) throws DbException {
        String childName = toExternalForm(st.nextToken());

        //validate if there is more tokens before calling st.nextToken() [MS]
        if (!st.hasMoreTokens()) {
            return null;
        }

        String childIndex = st.nextToken();
        DbObject child = null;
        int idx;
        try {
            idx = Integer.parseInt(childIndex);
            child = findDbObjectFrom(parent, idx, childName);
            if (child != null) {
                while (st.hasMoreTokens()) {
                    child = findDbObjectFromToken(st, child);
                }
            } //end if
        } catch (NumberFormatException ex) {
            //do nothing, just return null (not found)
        }

        return child;
    }

    public DbObject findDbObjectFrom(DbObject dbParent, int index, String semanticalName)
            throws DbException {
        if (dbParent == null) {
            return null;
        }

        //to find unnamed objects
        if (semanticalName.equals(UNNAMED)) {
            semanticalName = "";
        }

        DbObject dboFound = null;
        try {
            DbRelationN relationN = dbParent.getComponents();
            dboFound = relationN.elementAt(index);
            if (dboFound.getSemanticalName(DbObject.SHORT_FORM).equals(semanticalName))
                return dboFound;
        } catch (ArrayIndexOutOfBoundsException ex) {
            dboFound = null;
        }
        DbEnumeration dbEnum = dbParent.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            if (dbo.getSemanticalName(DbObject.SHORT_FORM).equals(semanticalName)) {
                dboFound = dbo;
                break;
            }
        }
        dbEnum.close();
        return dboFound;
    }

    // take all the characters in "text" that match the characteres in URL_TOKENS,
    // convert them into an internal form and return the modified string
    private static String toInternalForm(String text) {
        for (int i = 0; i < URL_TOKENS.length(); i++)
            text = text.replace(URL_TOKENS.charAt(i), INTERNAL_URL_TOKENS.charAt(i));
        return text;
    }

    // undo what toInternalForm has done to a string
    private static String toExternalForm(String text) {
        for (int i = 0; i < URL_TOKENS.length(); i++)
            text = text.replace(INTERNAL_URL_TOKENS.charAt(i), URL_TOKENS.charAt(i));
        return text;
    }

    ///////////////////////////////////
    // HyperlinkListener SUPPORT
    //
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (validatingDb == null)
            return;
        if (e.getEventType() == HyperlinkEvent.EventType.ENTERED)
            textFrame.getTextPanel().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        else if (e.getEventType() == HyperlinkEvent.EventType.EXITED)
            textFrame.getTextPanel().setCursor(Cursor.getDefaultCursor());
        else if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                if (validatingDb.isValid()) {
                    validatingDb.beginTrans(Db.READ_TRANS);
                    String desc = e.getDescription();
                    DbObject dbo = findDbObjectFromHyperLink(validatingDb, desc);
                    if (dbo != null) {
                        MainFrame.getSingleton().findInExplorer(dbo);
                        // I call the setSelected(true) to get back the focus (FocusManager remove the focus)
                        // It will prevent the user from the need to reselect the internal frame before activating another hyperlink
                        textFrame.setSelected(true);
                    }
                    validatingDb.commitTrans();
                    if (dbo == null)
                        javax.swing.JOptionPane.showMessageDialog(textFrame, LocaleMgr.validation
                                .getString("ObjectNotFoundInDb"));
                } else
                    javax.swing.JOptionPane.showMessageDialog(textFrame, LocaleMgr.validation
                            .getString("ModelClosed"));
            } catch (Exception ex) {
                org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(textFrame, ex);
            }
        }
    }

    //
    // End of HyperlinkListener SUPPORT
    ////////////////////////////////////

    private static class UrlComponentIterator implements DbObject.ComponentIterator {
        UrlComponentIterator() {
        }

        public String modifiedComponentName(DbObject component, String currentName)
                throws DbException {
            return toInternalForm(currentName) + ","
                    + component.getComposite().getComponents().indexOf(component);
        }
    }

    public static void main(String[] args) {
        //test replaceBlanks
        String[] tests = new String[] { "Class", "field", "", "Class.", "Class..", ".field",
                "Class.Nested.field", "Class.Nested.", "Class..field", ".Nested.field", "..field",
                "..", "....", "..Class.." };

        int nb = tests.length;
        for (int i = 0; i < nb; i++) {
            String test = tests[i];
            String result = replaceBlanks(test);
            System.out.println("'" + test + "'   gives   '" + result + "'");
        } //end for
    }
} //end JavaValidations
