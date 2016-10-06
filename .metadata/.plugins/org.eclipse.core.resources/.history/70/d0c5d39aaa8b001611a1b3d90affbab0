package org.modelsphere.plugins.layout.nodes.kandinsky.planarization;

/**
 * The Class QueueElement.
 * 
 * @param <T>
 *            the generic type
 * @param <C>
 *            the generic type
 */
public class QueueElement<T, C> implements Comparable<QueueElement<T, C>> {

    /** The comparable value. */
    private C comparableValue;

    /** The element. */
    private T element;

    /**
     * Instantiates a new queue element.
     * 
     * @param element
     *            the element
     * @param comparableValue
     *            the comparable value
     */
    QueueElement(T element, C comparableValue) {
        this.comparableValue = comparableValue;
        this.element = element;
    }

    /**
     * Compare to.
     * 
     * @param o
     *            the o
     * @return the int
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(QueueElement<T, C> o) {
        if (comparableValue instanceof Comparable<?>) {
            Comparable<C> comparable = (Comparable<C>) comparableValue;
            return comparable.compareTo(o.comparableValue);
        }
        throw new ClassCastException("comparableValue must implements Comparable interface.");
    }

    /**
     * Gets the comparable value.
     * 
     * @return the comparable value
     */
    public C getComparableValue() {
        return comparableValue;
    }

    /**
     * Sets the comparable value.
     * 
     * @param comparableValue
     *            the new comparable value
     */
    public void setComparableValue(C comparableValue) {
        this.comparableValue = comparableValue;
    }

    /**
     * Gets the element.
     * 
     * @return the element
     */
    public T getElement() {
        return element;
    }
}
