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

package org.modelsphere.jack.util;

import java.util.*;

public class SrVector extends AbstractList implements Cloneable, java.io.Serializable {

    private static final int INCR_ALLOC = 4;

    private Object[] array;
    private int incrAlloc;
    private int nbElem;

    public SrVector() {
        this(INCR_ALLOC);
    }

    public SrVector(int incrAlloc) {
        array = new Object[incrAlloc];
        this.incrAlloc = incrAlloc;
        nbElem = 0;
    }

    public SrVector(Collection c) {
        array = c.toArray();
        nbElem = array.length;
        incrAlloc = INCR_ALLOC;
    }

    public Object clone() {
        try {
            SrVector vector = (SrVector) super.clone();
            vector.array = new Object[array.length];
            System.arraycopy(array, 0, vector.array, 0, nbElem);
            return vector;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public final void addElement(Object obj) {
        if (nbElem == array.length)
            adjustCapacity(1);
        array[nbElem] = obj;
        nbElem++;
    }

    public final void insertElementAt(Object obj, int index) {
        if (index > nbElem)
            throw new ArrayIndexOutOfBoundsException(index + " > " + nbElem); // NOT
        // LOCALIZABLE
        if (nbElem == array.length)
            adjustCapacity(1);
        if (index < nbElem)
            System.arraycopy(array, index, array, index + 1, nbElem - index);
        array[index] = obj;
        nbElem++;
    }

    public final void add(int index, Object obj) {
        insertElementAt(obj, index);
    }

    /**
     * Insert in the vector at position <index> the <extNb> elements at position <extIndex> in
     * <extArray>. if <extArray> is null, insert <extNb> null elements (<extIndex> ignored).
     */
    public final void insertElements(int index, Object[] extArray, int extIndex, int extNb) {
        replaceElements(index, 0, extArray, extIndex, extNb);
    }

    public final boolean removeElement(Object obj) {
        int i = indexOf(obj);
        if (i >= 0) {
            removeElementAt(i);
            return true;
        }
        return false;
    }

    public final void removeElementAt(int index) {
        if (index >= nbElem)
            throw new ArrayIndexOutOfBoundsException(index + " >= " + nbElem); // NOT
        // LOCALIZABLE
        nbElem--;
        if (index < nbElem)
            System.arraycopy(array, index + 1, array, index, nbElem - index);
        array[nbElem] = null; /* to let gc do its work */
        adjustCapacity(0);
    }

    public final boolean remove(Object obj) {
        return removeElement(obj);
    }

    public final Object remove(int index) {
        Object oldObj = array[index];
        removeElementAt(index);
        return oldObj;
    }

    public final void removeElements(int index, int nb) {
        replaceElements(index, nb, (Object[]) null, 0, 0);
    }

    protected final void removeRange(int from, int to) {
        removeElements(from, to - from);
    }

    public final void clear() {
        removeElements(0, nbElem);
    }

    /* <indices> must be in increasing order. */
    public final void removeElements(int[] indices) {
        if (indices.length == 0)
            return;
        int i = 0;
        while (true) {
            int ind = indices[i] + 1;
            i++;
            int nb = (i < indices.length ? indices[i] : nbElem) - ind;
            if (nb < 0)
                throw new IllegalArgumentException();
            if (nb > 0)
                System.arraycopy(array, ind, array, ind - i, nb);
            if (i == indices.length)
                break;
        }
        i = nbElem - indices.length;
        while (true) {
            nbElem--;
            array[nbElem] = null; /* to let gc do its work */
            if (nbElem == i)
                break;
        }
        adjustCapacity(0);
    }

    public final void reinsert(int oldIndex, int newIndex) {
        if (oldIndex <= newIndex) {
            newIndex--;
            if (oldIndex >= newIndex)
                return;
            if (newIndex >= nbElem)
                throw new ArrayIndexOutOfBoundsException(newIndex + " >= " + nbElem); // NOT LOCALIZABLE
            Object obj = array[oldIndex];
            System.arraycopy(array, oldIndex + 1, array, oldIndex, newIndex - oldIndex);
            array[newIndex] = obj;
        } else {
            if (oldIndex >= nbElem)
                throw new ArrayIndexOutOfBoundsException(oldIndex + " >= " + nbElem); // NOT LOCALIZABLE
            Object obj = array[oldIndex];
            System.arraycopy(array, newIndex, array, newIndex + 1, oldIndex - newIndex);
            array[newIndex] = obj;
        }
    }

    /**
     * Replace in the vector the <nb> elements at position <index> by the <extNb> elements at
     * position <extIndex> in <extArray>. if <extArray> is null, replace the <nb> elements by
     * <extNb> null elements (<extIndex> ignored).
     */
    public final void replaceElements(int index, int nb, Object[] extArray, int extIndex, int extNb) {
        if (index + nb > nbElem)
            throw new ArrayIndexOutOfBoundsException(index + " + " + nb + " > " + nbElem); // NOT LOCALIZABLE

        int diff = extNb - nb;
        if (diff != 0) {
            if (diff > 0)
                adjustCapacity(diff);
            int nbMove = nbElem - index - nb;
            if (nbMove > 0)
                System.arraycopy(array, index + nb, array, index + extNb, nbMove);
            nbElem += diff;
            if (diff < 0) {
                int i = nbElem - diff;
                while (--i >= nbElem)
                    array[i] = null; /* to let gc do its work */
                adjustCapacity(0);
            }
        }

        if (extNb != 0) {
            if (extArray == null) {
                for (int i = 0; i < extNb; i++)
                    array[index + i] = null;
            } else
                System.arraycopy(extArray, extIndex, array, index, extNb);
        }
    }

    public final void replaceElementsFromVector(int index, int nb, SrVector extVector,
            int extIndex, int extNb) {
        if (extIndex + extNb > extVector.nbElem)
            throw new ArrayIndexOutOfBoundsException(extIndex + " + " + extNb + " > "
                    + extVector.nbElem); // NOT LOCALIZABLE
        replaceElements(index, nb, extVector.array, extIndex, extNb);
    }

    public final Object elementAt(int index) {
        if (index >= nbElem)
            throw new ArrayIndexOutOfBoundsException(index + " >= " + nbElem); // NOT
        // LOCALIZABLE
        return array[index];
    }

    public final Object get(int index) {
        return elementAt(index);
    }

    public final void getElements(int index, int nb, Object[] extArray, int extIndex) {
        if (nb == 0)
            return;
        if (index + nb > nbElem)
            throw new ArrayIndexOutOfBoundsException(index + " + " + nb + " > " + nbElem); // NOT LOCALIZABLE
        System.arraycopy(array, index, extArray, extIndex, nb);
    }

    public final void setElementAt(Object obj, int index) {
        if (index >= nbElem)
            throw new ArrayIndexOutOfBoundsException(index + " >= " + nbElem); // NOT
        // LOCALIZABLE
        array[index] = obj;
    }

    public final Object set(int index, Object obj) {
        Object oldObj = array[index];
        setElementAt(obj, index);
        return oldObj;
    }

    public final void setElements(int index, int nb, Object[] extArray, int extIndex) {
        if (nb == 0)
            return;
        if (index + nb > nbElem)
            throw new ArrayIndexOutOfBoundsException(index + " + " + nb + " > " + nbElem); // NOT LOCALIZABLE
        System.arraycopy(extArray, extIndex, array, index, nb);
    }

    public final int indexOf(Object elem) {
        return indexOf(elem, 0);
    }

    public final int indexOf(Object elem, int index) {
        for (int i = index; i < nbElem; i++) {
            if (elem == array[i])
                return i;
        }
        return -1;
    }

    public final int lastIndexOf(Object elem) {
        for (int i = nbElem - 1; i >= 0; i--) {
            if (elem == array[i])
                return i;
        }
        return -1;
    }

    public final Object[] toArray() {
        Object[] extArray = new Object[nbElem];
        System.arraycopy(array, 0, extArray, 0, nbElem);
        return extArray;
    }

    public final Object[] toArray(Object[] extArray) {
        System.arraycopy(array, 0, extArray, 0, nbElem);
        return extArray;
    }

    public final boolean contains(Object elem) {
        return (indexOf(elem) > -1);
    }

    public final int indexOfUsingEquals(Object elem, int index) {
        for (int i = index; i < nbElem; i++) {
            if (elem.equals(array[i]))
                return i;
        }
        return -1;
    }

    public final int indexOfUsingEquals(Object elem) {
        return indexOfUsingEquals(elem, 0);
    }

    public final boolean containsUsingEquals(Object elem) {
        return (indexOfUsingEquals(elem) > -1);
    }

    public final int size() {
        return nbElem;
    }

    public final Enumeration elements() {
        return new Enumeration() {

            private int index = 0;

            public boolean hasMoreElements() {
                return index < nbElem;
            }

            public Object nextElement() {
                if (index >= nbElem)
                    throw new NoSuchElementException();
                return array[index++];
            }
        };
    }

    /**
     * Sort a vector using the method <compareTo> of the <Comparable> interface.
     */
    public final void sort() {
        SrSort.sortArray(array, nbElem);
    }

    /**
     * Sort a vector using a Comparator object to compare the elements.
     */
    public final void sort(Comparator comparator) {
        SrSort.sortArray(array, nbElem, comparator);
    }

    /**
     * Search an element in a sorted vector using the method <compareTo> of the <Comparable>
     * interface.
     * 
     * Returns the index of the first element if more than one are found. If none is found, returns
     * a negative result: the expression -(result+1) gives the index where to insert the element.
     */
    public final int binarySearch(Comparable elem) {
        return SrSort.binarySearchArray(array, nbElem, elem);
    }

    /**
     * Search an element in a sorted vector using a Comparator object to compare the elements. See
     * binarySearch(elem) above.
     */
    public final int binarySearch(Object elem, Comparator comparator) {
        return SrSort.binarySearchArray(array, nbElem, elem, comparator);
    }

    private final void adjustCapacity(int incrElem) {
        int spare = array.length - (nbElem + incrElem);
        int newSpare = (incrAlloc > array.length / 8 ? incrAlloc : array.length / 8);
        if (spare >= 0 && spare < newSpare * 2)
            return;

        Object[] oldArray = array;
        array = new Object[nbElem + incrElem + newSpare];
        System.arraycopy(oldArray, 0, array, 0, nbElem);
    }
}
