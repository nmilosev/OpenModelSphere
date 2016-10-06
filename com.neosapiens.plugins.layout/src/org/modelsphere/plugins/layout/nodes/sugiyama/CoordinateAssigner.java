package org.modelsphere.plugins.layout.nodes.sugiyama;

import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.plugins.layout.nodes.layerers.LayeredGraph;

/**
 * The fourth stage of the Sugiyama algorithm: Coordinate assignment.
 * 
 * Given an ordered-layered-directed graph, finds appropriate coordinates for each nodes.
 * 
 * @author Gabriel
 */
public abstract class CoordinateAssigner {

    /**
     * Assigns coordinates to each node in <code>graph</code>.
     * 
     * @param layeredGraph
     *            An ordered-layered-directed graph. Note that, for each node, its order inside the
     *            layer depends on its X coordinate before the call. When the call returns, every
     *            node is expected to have been assigned a position.
     * @throws LayoutException
     *             the layout exception
     */
    public abstract void assignCoordinates(LayeredGraph layeredGraph) throws LayoutException;
}
