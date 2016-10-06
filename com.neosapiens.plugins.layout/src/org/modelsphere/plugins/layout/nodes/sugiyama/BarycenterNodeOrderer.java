package org.modelsphere.plugins.layout.nodes.sugiyama;

import java.util.*;

import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;
import org.modelsphere.plugins.layout.nodes.layerers.LayeredGraph;

/**
 * The Class BarycenterNodeOrderer.
 */
public class BarycenterNodeOrderer extends NodeOrderer {

    /** The DEFAUL t_ directio n_ hint. */
    static public DirectionHint DEFAULT_DIRECTION_HINT = DirectionHint.TopDown;

    /** The direction hint. */
    private DirectionHint directionHint;

    /** The graph. */
    private LayeredGraph graph;

    /**
     * Instantiates a new barycenter node orderer.
     */
    public BarycenterNodeOrderer() {
        directionHint = DEFAULT_DIRECTION_HINT;
    }

    /**
     * Sets the direction hint.
     * 
     * @param hint
     *            the new direction hint
     */
    public void setDirectionHint(DirectionHint hint) {
        directionHint = hint;
    }

    /**
     * Gets the direction hint.
     * 
     * @return the direction hint
     */
    public DirectionHint getDirectionHint() {
        return directionHint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.plugins.layout.nodes.sugiyama.NodeOrderer#orderNodes(
     * org.modelsphere.plugins .layout.nodes.sugiyama.LayeredGraph)
     */
    @Override
    public void orderNodes(LayeredGraph graph) throws LayoutException {

        if (graph == null) {
            throw new NullPointerException("graph");
        }

        this.graph = graph;

        if (directionHint == DirectionHint.BottomUp) {
            int topLayer = graph.getTopLayer();
            for (int layer = 1; layer <= topLayer; layer++) {
                orderNodesOnLayer(layer, layer - 1);
            }
        } else {
            int topLayer = graph.getTopLayer();
            for (int layer = topLayer - 1; layer >= 0; layer--) {
                orderNodesOnLayer(layer, layer + 1);
            }
        }
    }

    /**
     * <p>
     * Re-order every vertex on layer <tt>currentLayer</tt> by only considering the position of
     * their neighbors in <tt>fixedLayers</tt>.
     * <p>
     * Each vertex gets the position that corresponds the average of his neighbors' position
     * (referred to as the <i>barycenter</i>). If more than one nodes have the same computed
     * barycenter, they are organized arbitrarily.
     * 
     * @param currentLayer
     *            the current layer
     * @param fixedLayer
     *            the fixed layer
     */
    private void orderNodesOnLayer(int currentLayer, int fixedLayer) {
        Set<Vertex> currentLayerNodes = graph.verticesOnLayer(currentLayer);
        List<Vertex> order = new ArrayList<Vertex>(currentLayerNodes.size());
        Set<Vertex> secondPassNodes = new HashSet<Vertex>();

        // FIRST PASS: order nodes that have neighbors on the fixed layer.
        for (Vertex n : currentLayerNodes) {
            Set<Vertex> neighbors = graph.neighborsOnLayer(n, fixedLayer);

            // if the current node has no neighbors on the fixed layer.
            if (neighbors.size() == 0) {
                // keep them for second pass.
                secondPassNodes.add(n);
            } else {
                computeBarycenter(n, neighbors);
            }
        }
        // SECOND PASS: Order nodes that have neighbors on the current layer.
        for (Vertex n : secondPassNodes) {
            Set<Vertex> neighbors = graph.neighborsOnLayer(n, currentLayer);

            if (neighbors.size() != 0) {
                computeBarycenter(n, neighbors);
            }
        }
        graph.setLayerOrder(currentLayer, order);
    }

    /**
     * Compute barycenter.
     * 
     * @param n
     *            the n
     * @param neighbors
     *            the neighbors
     */
    private void computeBarycenter(Vertex n, Set<Vertex> neighbors) {
        int sum = 0;
        for (Vertex neighbor : neighbors) {
            sum += graph.getOrder(neighbor);
        }
        int barycenter = sum / neighbors.size();

        graph.setOrder(n, barycenter);
    }
}
