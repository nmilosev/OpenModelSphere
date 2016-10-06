package org.modelsphere.plugins.layout.nodes.sugiyama;

import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;

/**
 * Represents the first phase of the Sugiyama algorithm: Identifying and removing every cycle found
 * in the source graph in order to perform the next stages and then restoring those cycles at the
 * very end.
 * 
 * @author Gabriel
 * 
 */
public abstract class CycleRemover {

    /**
     * Inspects 'g' and removes appropriate edges in order to make 'g' acyclic after the function
     * returns.
     * 
     * @param graph
     *            the graph
     * @throws LayoutException
     *             If an error prevents from completing the operation.
     */
    public abstract void removeCycles(Graph graph) throws LayoutException;

    /**
     * Restores previously removed edges, if any, so that 'g' gets back to the state it was before
     * 'removeCycles' was called.
     * 
     * @param graph
     *            the graph
     * @throws LayoutException
     *             If an error prevents from completing the operation.
     */
    public abstract void restoreCycles(Graph graph) throws LayoutException;
}
