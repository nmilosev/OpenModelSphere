package org.modelsphere.plugins.layout.nodes.layerers;

import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;

/**
 * Second stage of the Sugiyama algorithm: Layering stage.
 * 
 * @author Gabriel
 * 
 */
public abstract class Layerer {

    /**
     * Generates an upward layering of a directed graph.
     * 
     * @param graph
     *            the graph
     * @return A layered version of 'graph'.
     * @throws LayoutException
     *             If an error prevents the algorithm completion.
     */
    public abstract LayeredGraph performLayering(Graph graph) throws LayoutException;

}
