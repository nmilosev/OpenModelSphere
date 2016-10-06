package org.modelsphere.plugins.layout.nodes.sugiyama;

import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.plugins.layout.nodes.layerers.LayeredGraph;

/**
 * The third stage of the Sugiyama algorithm: Node ordering stage.
 * 
 * The problem to solve: given a layered directed graph, find an ordering for each node layer that
 * minimizes the number of crossing edges.
 * 
 * @author Gabriel
 */
public abstract class NodeOrderer {

    /**
     * Order every node of every layer to minimize edge crossing.
     * 
     * @param layeredGraph
     *            An un-ordered layered directed graph. After the method returns, the order of each
     *            Node inside each layer is arranged in a way to minimize edge crossing.
     * @throws LayoutException
     *             the layout exception
     */
    public abstract void orderNodes(LayeredGraph layeredGraph) throws LayoutException;
}
