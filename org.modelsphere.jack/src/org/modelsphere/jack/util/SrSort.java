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

import java.util.Comparator;

import org.modelsphere.jack.debug.TestException;
import org.modelsphere.jack.debug.Testable;

public abstract class SrSort {

    /**
     * Search an element in the <nbElem> first elements of a sorted array using the method
     * <compareTo> of the <Comparable> interface.
     * 
     * Returns the index of the first element if more than one are found. If none is found, returns
     * a negative result: the expression -(result+1) gives the index where to insert the element.
     */
    public static int binarySearchArray(Object[] array, int nbElem, Comparable elem) {
        return binarySearchArray(array, nbElem, elem, (Comparator) null);
    }

    /**
     * Search an element in the <nbElem> first elements of a sorted array using a Comparator object
     * to compare the elements. See above.
     */
    public static int binarySearchArray(Object[] array, int nbElem, Object elem,
            Comparator comparator) {
        int lo = -1;
        int hi = nbElem;
        int mid;
        boolean found = false;

        while (lo + 1 < hi) {
            int cmp;
            mid = (lo + hi) / 2;
            if (comparator == null)
                cmp = ((Comparable) elem).compareTo(array[mid]);
            else
                cmp = comparator.compare(elem, array[mid]);
            if (cmp <= 0) {
                if (cmp == 0)
                    found = true;
                hi = mid;
            } else
                lo = mid;
        }
        return (found ? hi : -(hi + 1));
    }

    public static int binarySearchArray(int[] array, int nbElem, int elem) {
        int lo = -1;
        int hi = nbElem;
        int mid;
        boolean found = false;

        while (lo + 1 < hi) {
            mid = (lo + hi) / 2;
            if (elem <= array[mid]) {
                if (elem == array[mid])
                    found = true;
                hi = mid;
            } else
                lo = mid;
        }
        return (found ? hi : -(hi + 1));
    }

    /**
     * Sort the <nbElem> first elements of an array of Objects using the method <compareTo> of the
     * <Comparable> interface to compare the elements. Algorithm used: list merge.
     */
    public static void sortArray(Object[] array, int nbElem) {
        sortArray(array, nbElem, (Comparator) null);
    }

    /**
     * Sort the <nbElem> first elements of an array of Objects using a Comparator object to compare
     * the elements. Algorithm used: list merge.
     */
    public static void sortArray(Object[] array, int nbElem, Comparator comparator) {
        if (nbElem < 2)
            return;

        // Sort by successive merge of sorted sub-lists whose lengths have same
        // power of 2.
        // The initial sub-lists are all the sequences of elements already
        // sorted.
        //
        int[] linkArray = new int[nbElem]; // initially, link all elements in an
        // input list
        int i;
        for (i = 0; i < nbElem; i++)
            linkArray[i] = i + 1;
        linkArray[nbElem - 1] = -1;

        int[] heads = new int[32]; // one sorted list per power of 2
        for (i = 0; i < heads.length; i++)
            heads[i] = -1;

        int head = 0; // fraction input list in sublists of already sorted
        // elements.
        while (head != -1) {
            int current = head;
            int next;
            int nb = 1;

            while (true) { // scan remaining input elements up to the first one
                // out of sequence.
                next = linkArray[current];
                if (next == -1)
                    break;

                int cmp;
                if (comparator == null)
                    cmp = ((Comparable) array[current]).compareTo(array[next]);
                else
                    cmp = comparator.compare(array[current], array[next]);
                if (cmp <= 0) {
                    current = next;
                    nb++;
                    continue;
                }
                if (nb == 1) { // if first element greater than second, invert
                    // them in a list of 2 elements
                    head = next;
                    next = linkArray[next];
                    linkArray[head] = current;
                    nb = 2;
                }
                linkArray[current] = -1;
                break;
            }

            for (i = 0;; i++) { // compute log(2) length of the list, to get the
                // slot in <heads>
                nb = nb >> 1;
                if (nb == 0)
                    break;
            }

            // If the slot in <heads> is already occupied, merge the 2 lists and
            // put the resulting
            // list in the next slot.
            //
            while (heads[i] != -1) {
                head = mergeLists(array, linkArray, heads[i], head, comparator);
                heads[i] = -1;
                i++;
            }
            heads[i] = head;
            head = next;
        }

        // Merge all sub-lists of length 1, 2, 4, 8, 16, ... to obtain a single
        // sorted list.
        //
        head = -1;
        for (i = 0; i < heads.length; i++) {
            if (heads[i] == -1)
                continue;
            if (head == -1)
                head = heads[i];
            else
                head = mergeLists(array, linkArray, heads[i], head, comparator);
        }

        // Now we have an ordered list; replace the link by the index where to
        // move
        // the element.
        //
        for (i = 0;; i++) {
            int next = linkArray[head];
            linkArray[head] = i;
            if (next == -1)
                break;
            head = next;
        }

        // ove the elements. The list of new position indexes is made up of
        // a number of circular chains; for each chain, invert the chain to have
        // the index of the element which comes here (instead of the index where
        // to move the element), then move the elements of the chain.
        for (i = 0; i < nbElem; i++) {
            int current, prev, next;
            current = linkArray[i]; // index where to move el. <i>.
            if (current == i) // already in place or already moved.
                continue;
            prev = i; // invert the circular chain.
            while (true) {
                next = linkArray[current];
                linkArray[current] = prev;
                if (current == i)
                    break;
                prev = current;
                current = next;
            }
            prev = i; // now move the elements of the inverted chain.
            Object temp = array[i]; // save first el. of the chain
            while (true) {
                current = linkArray[prev]; // index of el. which comes here.
                linkArray[prev] = prev; // makes it point to self, to indicate
                // move done.
                if (current == i)
                    break;
                array[prev] = array[current];
                prev = current;
            }
            array[prev] = temp; // now, moves first el. of the chain.
        }
    }

    /*
     * Merge 2 sorted lists in a single one.
     */
    private static int mergeLists(Object[] array, int[] linkArray, int head1, int head2,
            Comparator comparator) {
        int head = -1;
        int prev = -1;

        while (true) {
            int cmp;
            if (comparator == null)
                cmp = ((Comparable) array[head1]).compareTo(array[head2]);
            else
                cmp = comparator.compare(array[head1], array[head2]);
            if (cmp == 0) /* if elements are equal, compare their position. */
                cmp = head1 - head2;
            if (cmp > 0) {
                int temp = head1;
                head1 = head2;
                head2 = temp;
            }
            if (prev == -1)
                head = head1;
            else
                linkArray[prev] = head1;
            prev = head1;
            head1 = linkArray[head1];
            if (head1 == -1)
                break;
        }
        linkArray[prev] = head2;
        return head;
    }

    // UNIT TEST
    private static class SrSortImpl extends SrSort implements Testable {
        SrSortImpl() {
        }

        public void test() throws TestException {
            if (!Testable.ENABLE_TEST) {

                class StringComparator implements Comparator {
                    public int compare(Object o1, Object o2) {
                        String s = (String) o1;
                        return s.compareTo((String) o2);
                    }
                } // end StringComparator

                // perform the sort
                String[] array = new String[] { "Delta", "Beta", "Epsilon", "Alpha", "Gamma" }; // NOT LOCALIZABLE, unit test
                Comparator comparator = new StringComparator();
                sortArray(array, array.length, comparator);

                // check the result
                if (!(array[0].equals("Alpha")) || !(array[4].equals("Gamma"))) { // NOT
                    // LOCALIZABLE,
                    // unit
                    // test
                    throw new TestException("error"); // NOT LOCALIZABLE, unit
                    // test
                }
            } // end if
        } // end test()
    } // end SrSortImpl

    public static Testable createInstanceForTesting() {
        Testable testable = new SrSortImpl();
        return testable;
    }

    // Unit test
    public static void main(String[] args) {
        Testable testable = SrSort.createInstanceForTesting();
        try {
            testable.test();
            System.out.println("success");
        } catch (TestException ex) {
            System.out.println("failure");
        }
    }
} // end SrSort

