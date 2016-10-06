package org.modelsphere.plugins.layout.nodes.kandinsky.planarization;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * The Class PriorityQueueExtend.
 * 
 * @param <T>
 *            the generic type
 * @param <C>
 *            the generic type
 */
public class PriorityQueueExtend<T, C> extends PriorityQueue<QueueElement<T, C>> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6824966818955710887L;

    /**
     * Instantiates a new priority queue extend.
     */
    public PriorityQueueExtend() {
        super();
    }

    /**
     * Instantiates a new priority queue extend.
     * 
     * @param comparator
     *            the comparator
     */
    public PriorityQueueExtend(Comparator<QueueElement<T, C>> comparator) {
        super(11, comparator);
    }

    /**
     * Contains element.
     * 
     * @param element
     *            the element
     * @return true, if successful
     */
    public boolean containsElement(T element) {
        for (QueueElement<T, C> qe : this) {
            if (qe.getElement() == null && element == null) {
                return true;
            }
            if (qe.getElement().equals(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Contains all elements.
     * 
     * @param elements
     *            the elements
     * @return true, if successful
     */
    public boolean containsAllElements(Collection<T> elements) {
        for (T element : elements) {
            if (!containsElement(element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds the.
     * 
     * @param element
     *            the element
     * @param comparableValue
     *            the comparable value
     * @return true, if successful
     */
    public boolean add(T element, C comparableValue) {
        return offer(element, comparableValue);
    }

    /**
     * Offer.
     * 
     * @param element
     *            the element
     * @param comparableValue
     *            the comparable value
     * @return true, if successful
     */
    public boolean offer(T element, C comparableValue) {
        QueueElement<T, C> queueElement = new QueueElement<T, C>(element, comparableValue);
        return offer(queueElement);
    }

    /**
     * Removes the element.
     * 
     * @param element
     *            the element
     * @return true, if successful
     */
    public boolean removeElement(T element) {
        for (QueueElement<T, C> qe : this) {
            if (qe.getElement() == null && element == null) {
                return remove(qe);
            }
            if (qe.getElement().equals(element)) {
                return remove(qe);
            }
        }
        return false;
    }

    /**
     * Sets the value.
     * 
     * @param element
     *            the element
     * @param comparableValue
     *            the comparable value
     */
    public void setValue(T element, C comparableValue) {
        for (QueueElement<T, C> qe : this) {
            if (qe.getElement() == null && element == null) {
                remove(qe);
                qe.setComparableValue(comparableValue);
                offer(qe);
                return;
            }
            if (qe.getElement().equals(element)) {
                remove(qe);
                qe.setComparableValue(comparableValue);
                offer(qe);
                return;
            }
        }
        throw new InvalidParameterException(
                "element must exist in PriorityQueueExtand before setting a new value.");
    }

}
