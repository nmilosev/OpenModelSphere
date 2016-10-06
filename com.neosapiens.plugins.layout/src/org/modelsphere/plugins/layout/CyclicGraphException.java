/**
 * 
 */
package org.modelsphere.plugins.layout;

import org.modelsphere.jack.srtool.features.layout.LayoutAlgorithm;
import org.modelsphere.jack.srtool.features.layout.LayoutException;

/**
 * Throw this exception when you expect an acyclic graph and you did not receive one.
 * 
 * @author David
 * 
 */
@SuppressWarnings("serial")
public class CyclicGraphException extends LayoutException {

    /**
     * Constructor with default message which is : This graph contains at least one cycle. Be sure
     * to use acyclic graph with this method.
     */
    public CyclicGraphException(LayoutAlgorithm source) {
        this(source, "This graph contains at least one cycle. "
                + "Be sure to use acyclic graph with this method.");
    }

    /**
     * Instantiates a new cyclic graph exception.
     * 
     * @param message
     *            the message
     */
    public CyclicGraphException(LayoutAlgorithm source, String message) {
        super(source, message);
    }

}
