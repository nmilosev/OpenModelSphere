package org.modelsphere.plugins.layout.nodes.sugiyama;

import java.util.List;

import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;
import org.modelsphere.plugins.layout.nodes.layerers.LayeredGraph;

/**
 * The Class QuickSmartNodeOrderer.
 */
public class QuickSmartNodeOrderer extends NodeOrderer {

    /**
     * <p>
     * Simple two-phase use of QuickNodeOrderer in an <i>smart</i> way.
     * <p>
     * Make the graph proper if it isn't already.
     * <p>
     * Does nothing if no edges are crossing.
     * <p>
     * Does one pass bottom-up and the other top-down. Keep the best result.
     * 
     * @param layeredGraph
     *            the layered graph
     * @throws LayoutException
     *             the layout exception
     */
    @Override
    public void orderNodes(LayeredGraph layeredGraph) throws LayoutException {

        if (layeredGraph == null) {
            throw new NullPointerException();
        }

        if (layeredGraph.getNbCrossings() == 0) {
            return;
        }

        if (!layeredGraph.isProper()) {
            layeredGraph.makeProper();
        }

        QuickNodeOrderer orderer = new QuickNodeOrderer();

        orderer.setDirectionHint(DirectionHint.TopDown);
        orderer.orderNodes(layeredGraph);
        List<List<Vertex>> topDownLayerOrder = layeredGraph.getAllLayerOrder();
        int topDownCrossingCount = layeredGraph.getNbCrossings();

        orderer.setDirectionHint(DirectionHint.BottomUp);
        orderer.orderNodes(layeredGraph);
        int bottomUpCrossingCount = layeredGraph.getNbCrossings();

        if (topDownCrossingCount < bottomUpCrossingCount) {
            layeredGraph.setAllLayerOrder(topDownLayerOrder);
        }
    }
}
