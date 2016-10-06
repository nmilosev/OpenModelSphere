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

package org.modelsphere.jack.baseDb.meta;

import java.awt.Image;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.*;

import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.srtypes.SrType;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.international.LocaleMgr;

/**
 * The central concept of the meta package. Each DbObject instance is associated to a MetaClass (in
 * a similar manner than any <code>java.lang.Object</code> is associated to a
 * <code>java.lang.Class</code>). <br>
 * <br>
 * MetaClass provides the following functionalities: <li>getGUIName(): returns the display name
 * (singular or plural) of a meta class. The string is language-dependent. <li>getIcon(): returns
 * the icon associated to a meta-class and used in the explorer. <li>getMetaFields(): returns the
 * list all meta-fields. <li>getSuperMetaClass(): returns the meta-class from which it inherits. <li>
 * getSubMetaClasses(): returns the list of meta-classes inheriting from this class.
 * 
 */
public final class MetaClass {

    /* Flags in MetaClass.flags */
    public final static int ACCESS_CTL = 0x0001;
    public final static int CLUSTER_ROOT = 0x0002;
    public final static int NAMING_ROOT = 0x0004;
    public final static int MATCHABLE = 0x0008;
    public final static int NO_UDF = 0x0010;
    public final static int UML_EXTENSIBILITY_FILTER = 0x0020;

    private static HashMap metaClasses = new HashMap();
    private static int maxLevel = 0;

    private String GUIName;
    private Icon icon = null;
    private MetaClass superMetaClass;
    private MetaClass[] subMetaClasses;
    private MetaClass[] componentMetaClasses;
    private MetaField[] metaFields;
    private MetaField[] allMetaFields;
    private Class jClass;
    private int level; // DbObject is level 0
    private int seqNo;
    private int flags;

    // Called once during application initialization, immediatly after loading
    // all the meta classes.
    public static void initMetaClasses() {
        Iterator iter = metaClasses.values().iterator();
        while (iter.hasNext())
            ((MetaClass) iter.next()).initAux();
    }

    private void initAux() {
        if (subMetaClasses != null)
            return; // already initialized
        try {
            jClass.getMethod("initMeta", null).invoke(null, null); // NOT
            // LOCALIZABLE
            // method
            // name
        } catch (Exception e) {
        }
        subMetaClasses = new MetaClass[0];
        allMetaFields = metaFields;
        level = 0;
        if (superMetaClass != null) {
            superMetaClass.initAux(); // insure super is initialized
            level = superMetaClass.level + 1;
            if (maxLevel < level)
                maxLevel = level;
            superMetaClass.addSubMetaClass(this);
            allMetaFields = new MetaField[superMetaClass.allMetaFields.length + metaFields.length];
            System.arraycopy(superMetaClass.allMetaFields, 0, allMetaFields, 0,
                    superMetaClass.allMetaFields.length);
            System.arraycopy(metaFields, 0, allMetaFields, superMetaClass.allMetaFields.length,
                    metaFields.length);
        }

        // Check that metaFields have valid types: primitive, String or SrType
        // (but no array).
        for (int i = 0; i < metaFields.length; i++) {
            MetaField metafield = metaFields[i];
            if (metafield instanceof MetaRelationship)
                continue;

            Field field = metafield.getJField();
            if (field == null) {
                String msg = MessageFormat.format("({0}/{1}) : Field is null for {2}", // NOT LOCALIZABLE
                        // RuntimeException
                        new Object[] { new Integer(i), new Integer(metaFields.length), metafield });
                throw new RuntimeException(msg);
            }

            Class type = field.getType();
            if (!(type.isPrimitive() || type == String.class || SrType.class.isAssignableFrom(type))) {
                String msg = MessageFormat.format("({0}/{1}) : {2} is an invalid type for {3}", // NOT
                        // LOCALIZABLE
                        // RuntimeException
                        new Object[] { new Integer(i), new Integer(metaFields.length), type,
                                metafield });
                throw new RuntimeException(msg); // NOT LOCALIZABLE
                // RuntimeException
            }
        } // end for
    } // end initAux()

    public static MetaClass find(String className) {
        return (MetaClass) metaClasses.get(className);
    }

    public static Enumeration enumMetaClasses() {
        return DbObject.metaClass.enumMetaClassHierarchy(false);
    }

    public static int getNbMetaClasses() {
        return metaClasses.size();
    }

    public MetaClass(String GUIName, Class jClass, MetaField[] metaFields, int flags) {
        this.GUIName = GUIName;
        this.superMetaClass = null;
        this.subMetaClasses = null; // initialized by initMetaClasses()
        this.componentMetaClasses = new MetaClass[0];
        this.jClass = jClass;
        this.metaFields = metaFields;
        this.allMetaFields = null; // initialized by initMetaClasses()
        this.level = 0; // initialized by initMetaClasses()
        this.seqNo = metaClasses.size();
        this.flags = flags;

        metaClasses.put(jClass.getName(), this);
        for (int i = 0; i < metaFields.length; i++) {
            metaFields[i].setMetaClass(this);
        }
    }

    public final String getGUIName() {
        return getGUIName(false, true);
    }

    public final String getGUIName(boolean plural) {
        return getGUIName(plural, true);
    }

    /**
     * Returns the display name (singular or plural) of a meta class. The string is
     * language-dependent.
     * 
     * @param plural
     *            (optional, default false) if true, returns the plural form of the meta class name
     * @param withTargetSys
     *            (optional, default true) if true, concatenate the target system to the name
     * @return
     */
    public final String getGUIName(boolean plural, boolean withTargetSys) {
        String firstSubstring = null;
        String equivalent = getGUIEquivalent(GUIName);
        int i = equivalent.indexOf(';');

        if (plural)
            firstSubstring = (i == -1 ? equivalent + 's' : equivalent.substring(i + 1));
        else
            firstSubstring = (i == -1 ? equivalent : equivalent.substring(0, i));

        if (withTargetSys) {
            return firstSubstring;
        } else {
            int tgDelim = firstSubstring.indexOf(" - ");
            return (tgDelim == -1 ? firstSubstring : firstSubstring.substring(tgDelim + 3));
        }
    } // end getGUIName

    /**
     * Returns the icon associated to a meta-class and used in the explorer.
     * 
     * @return the icon representing this meta class
     */

    public final Icon getIcon() {
        for (MetaClass metaClass = this; metaClass != null; metaClass = metaClass.superMetaClass) {
            if (metaClass.icon != null)
                return metaClass.icon;
        }
        return null;
    }

    public final void setIcon(String gifName) {
        String relImageName = "resources/" + gifName; // NOT LOCALIZABLE
        Image image = GraphicUtil.loadImage(jClass, relImageName);
        if (image != null)
            icon = new javax.swing.ImageIcon(image);
    }

    /**
     * Returns the meta-class from which it inherits.
     * 
     * @return the super-class, if any
     */
    public final MetaClass getSuperMetaClass() {
        return superMetaClass;
    }

    public final void setSuperMetaClass(MetaClass superMetaClass) {
        this.superMetaClass = superMetaClass;
    }

    /**
     * Returns the list of meta-classes inheriting from this class.
     * 
     * @return the list (eventually empty) of subclasses.
     */
    public final MetaClass[] getSubMetaClasses() {
        return subMetaClasses;
    }

    /**
     * Returns the list of meta-classes that can be components of this meta-class.
     * 
     * @return the list (eventually empty) of subclasses.
     */
    public final MetaClass[] getComponentMetaClasses() {
        return getComponentMetaClasses(false, false);
    }

    public final void setComponentMetaClasses(MetaClass[] componentMetaClasses) {
        this.componentMetaClasses = componentMetaClasses;
    }

    public final Class getJClass() {
        return jClass;
    }

    /**
     * Returns the list all the meta-fields of this meta-class. This includes meta-relationships.
     * 
     * @return the list of meta-fields defined on this meta-class
     */
    public final MetaField[] getMetaFields() {
        return metaFields;
    }

    /**
     * Returns the list all the meta-fields of this meta-class, and all its superclasses. This
     * includes meta-relationships.
     * 
     * @return the list of meta-fields defined on this meta-class
     */
    public final MetaField[] getAllMetaFields() {
        return allMetaFields;
    }

    /**
     * Returns the meta-field that has this name.
     * 
     * @return the meta-field, null if no meta-field has this name.
     */
    public final MetaField getMetaField(String name) {
        for (int i = allMetaFields.length; --i >= 0;) {
            String n = allMetaFields[i].getJName();
            if (name.equals(n))
                return allMetaFields[i];
        }
        return null;
    }

    public final int getLevel() {
        return level;
    }

    public final int getSeqNo() {
        return seqNo;
    }

    public final int getFlags() {
        return flags;
    }

    public final String toString() {
        return getGUIName();
    }

    public final MetaClass getCommonSuperMetaClass(MetaClass otherClass) {
        MetaClass thisClass = this;
        while (thisClass.level > otherClass.level)
            thisClass = thisClass.superMetaClass;
        while (otherClass.level > thisClass.level)
            otherClass = otherClass.superMetaClass;
        while (thisClass != otherClass) {
            thisClass = thisClass.superMetaClass;
            otherClass = otherClass.superMetaClass;
        }
        return thisClass;
    }

    public final boolean isAssignableFrom(MetaClass metaClass) {
        if (metaClass == null)
            return false;
        return jClass.isAssignableFrom(metaClass.getJClass());
    }

    // <this> is included in the enumeration.
    public final Enumeration enumMetaClassHierarchy(boolean leafOnly) {
        return new EnumMetaClassHierarchy(this, leafOnly);
    }

    public final boolean compositeIsAllowed(MetaClass compositeMetaClass) {
        for (MetaClass metaClass = compositeMetaClass; metaClass != null; metaClass = metaClass.superMetaClass) {
            for (int i = 0; i < metaClass.componentMetaClasses.length; i++) {
                if (metaClass.componentMetaClasses[i].jClass.isAssignableFrom(jClass))
                    return true;
            }
        }
        return false;
    }

    /**
     * Returns an array of booleans indicating the metaClasses that may have <this> as descendant in
     * the composition hierarchy.
     **/
    public final boolean[] markCompositePaths() {
        boolean[] processed = new boolean[metaClasses.size()];
        boolean[] selected = new boolean[metaClasses.size()];
        Enumeration enumeration = enumMetaClassHierarchy(true);
        while (enumeration.hasMoreElements())
            ((MetaClass) enumeration.nextElement()).markCompositePathsAux(processed, selected);
        return selected;
    }

    /**
     * Return an array of booleans indicating the metaClasses that may have any of mClasses as
     * descendant in the composition hierarchy.
     */
    public static boolean[] markCompositePaths(MetaClass[] mClasses) {
        boolean[] processed = new boolean[metaClasses.size()];
        boolean[] selected = new boolean[metaClasses.size()];
        for (int i = 0; i < mClasses.length; i++) {
            Enumeration enumeration = mClasses[i].enumMetaClassHierarchy(true);
            while (enumeration.hasMoreElements())
                ((MetaClass) enumeration.nextElement()).markCompositePathsAux(processed, selected);
        }
        return selected;
    }

    private void markCompositePathsAux(boolean[] processed, boolean[] selected) {
        if (processed[seqNo])
            return;
        processed[seqNo] = true;
        Enumeration enumeration = MetaClass.enumMetaClasses();
        while (enumeration.hasMoreElements()) {
            MetaClass metaClass = (MetaClass) enumeration.nextElement();
            for (int i = 0; i < metaClass.componentMetaClasses.length; i++) {
                if (metaClass.componentMetaClasses[i].jClass.isAssignableFrom(jClass)) {
                    Enumeration enumeration2 = metaClass.enumMetaClassHierarchy(false);
                    while (enumeration2.hasMoreElements()) {
                        metaClass = (MetaClass) enumeration2.nextElement();
                        selected[metaClass.seqNo] = true;
                        if (metaClass.getSubMetaClasses().length == 0)
                            metaClass.markCompositePathsAux(processed, selected);
                    }
                    break;
                }
            }
        }
    }

    /**
     * Returns an array of booleans indicating the metaClasses (leaf) that may have <this> as parent
     * in the composition hierarchy.
     */
    public final boolean[] markComponentsPaths(boolean leafOnly) {
        boolean[] processed = new boolean[metaClasses.size()];
        boolean[] selected = new boolean[metaClasses.size()];
        MetaClass metaClass = this;
        while (metaClass != null) {
            if (processed[metaClass.seqNo])
                continue;
            processed[metaClass.seqNo] = true;
            for (int i = 0; i < metaClass.componentMetaClasses.length; i++) {
                Enumeration enumeration = metaClass.componentMetaClasses[i]
                        .enumMetaClassHierarchy(leafOnly);
                while (enumeration.hasMoreElements()) {
                    selected[((MetaClass) enumeration.nextElement()).seqNo] = true;
                }
            }
            metaClass = metaClass.getSuperMetaClass();
        }
        return selected;
    }

    /**
     * Returns the components MetaClasses for this MetaClass.
     */
    public final MetaClass[] getComponentMetaClasses(boolean includeSuper, boolean leafOnly) {
        if (!includeSuper && !leafOnly)
            return componentMetaClasses;

        MetaClass[] metaClasses = null;

        if (includeSuper) {
            boolean[] selected = markComponentsPaths(leafOnly);
            int count = 0;
            for (int i = 0; i < selected.length; i++) {
                if (selected[i])
                    count++;
            }
            metaClasses = new MetaClass[count];
            int loc = 0;
            Enumeration enumeration = MetaClass.enumMetaClasses();
            while (enumeration.hasMoreElements()) {
                MetaClass metaClass = (MetaClass) enumeration.nextElement();
                if (selected[metaClass.seqNo]) {
                    metaClasses[loc] = metaClass;
                    loc++;
                }
            }
        } else {
            // leaf only
            int count = 0;
            for (int i = 0; i < componentMetaClasses.length; i++) {
                if (componentMetaClasses[i].getSubMetaClasses().length == 0)
                    count++;
            }
            metaClasses = new MetaClass[count];
            int loc = 0;
            for (int i = 0; i < componentMetaClasses.length; i++) {
                if (componentMetaClasses[i].getSubMetaClasses().length == 0) {
                    metaClasses[loc] = componentMetaClasses[i];
                    loc++;
                }
            }
        }
        return metaClasses;
    }

    /**
     * Returns all the fields visible on a ScreenView, in the order specified by the method
     * getScreenOrder() of each field.
     */
    public final ArrayList getScreenMetaFields() {
        ArrayList fields = new ArrayList();
        int index = 0;
        for (int i = 0; i < allMetaFields.length; i++) {
            MetaField field = allMetaFields[i];
            if (field instanceof MetaRelationN || !field.isVisibleInScreen())
                continue;
            index = getFieldOrder(field, fields, index);
            fields.add(index, field);
            index++;
        }
        return fields;
    }

    //
    // PRIVATE METHODS
    //
    private String getGUIEquivalent(String guiName) {
        return LocaleMgr.getResourceEquivalent(guiName);
    }

    private int getFieldOrder(MetaField field, ArrayList fields, int index) {
        String order = field.getScreenOrder();
        if (order == null || order.length() == 0)
            return index;
        char pos = order.charAt(0);
        if (pos != '<' && pos != '>')
            return index;
        String name = order.substring(1);
        int i, nb;
        for (i = 0, nb = fields.size(); i < nb; i++) {
            MetaField aField = (MetaField) fields.get(i);
            if (name.equals(aField.getJName().substring(2))) {
                index = (pos == '>' ? i + 1 : i);
                break;
            }
        }
        return index;
    }

    /*
     * I prefer an array to a vector; it's built only once and the access is quicker.
     */
    private final void addSubMetaClass(MetaClass subMetaClass) {
        MetaClass[] oldSubMetaClasses = subMetaClasses;
        subMetaClasses = new MetaClass[oldSubMetaClasses.length + 1];
        System.arraycopy(oldSubMetaClasses, 0, subMetaClasses, 0, oldSubMetaClasses.length);
        subMetaClasses[oldSubMetaClasses.length] = subMetaClass;
    }

    //
    // INNER CLASS
    //

    private static class EnumMetaClassHierarchy implements Enumeration {

        private MetaClass metaClass;
        private boolean leafOnly;
        private int[] indices = new int[maxLevel + 1];
        private int level = 0;

        EnumMetaClassHierarchy(MetaClass metaClass, boolean leafOnly) {
            this.metaClass = metaClass;
            this.leafOnly = leafOnly;
            if (leafOnly && metaClass.getSubMetaClasses().length != 0)
                advance();
        }

        public boolean hasMoreElements() {
            return (metaClass != null);
        }

        public Object nextElement() {
            if (metaClass == null)
                throw new NoSuchElementException();
            MetaClass result = metaClass;
            advance();
            return result;
        }

        private void advance() {
            while (true) {
                if (metaClass.getSubMetaClasses().length != 0) {
                    indices[level] = 0;
                    level++;
                    metaClass = metaClass.getSubMetaClasses()[0];
                } else {
                    while (true) {
                        if (level == 0) {
                            metaClass = null;
                            return;
                        }
                        metaClass = metaClass.getSuperMetaClass();
                        int ind = indices[level - 1] + 1;
                        if (ind < metaClass.getSubMetaClasses().length) {
                            indices[level - 1] = ind;
                            metaClass = metaClass.getSubMetaClasses()[ind];
                            break;
                        }
                        level--;
                    }
                }
                if ((!leafOnly) || metaClass.getSubMetaClasses().length == 0)
                    break;
            }
        }
    } // end EnumMetaClassHierarchy
} // end MetaClass

