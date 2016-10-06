package org.modelsphere.plugins.layout.nodes.sugiyama;

import java.util.Arrays;

import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;
import org.modelsphere.plugins.layout.nodes.layerers.LayeredGraph;

/**
 * <p>
 * Processes by layer.
 * <p>
 * Nodes are ordered <i>arbitrarily</i> on the first layer.
 * <p>
 * Determine best node order for layer 1 (current layer) by sorting them recursively, <i>quick
 * sort</i>-style, minimizing crossings with layer 0 (fixed layer).
 * <p>
 * Repeat with <tt>currentLayer++</tt> and <tt>fixedLayer++</tt> while there are layers to process.
 * <p>
 * The graph traversal direction can be parameterized via <tt>setDirectionHint</tt>.
 * <p>
 * <strong>Pros:</strong> Less crossings than BarycenterNodeOrderer without performance loss.
 * <p>
 * <strong>Cons:</strong> Not optimal. Since it always works at a two-contiguous-layers level, he
 * takes early decisions that induces otherwise avoidable crossings in subsequent layers. We could
 * say it lacks of overall view.
 * 
 * 
 * @author Gabriel
 * 
 */
public class QuickNodeOrderer extends NodeOrderer {

    /** The DEFAUL t_ directio n_ hint. */
    static public DirectionHint DEFAULT_DIRECTION_HINT = DirectionHint.TopDown;

    /** The direction hint. */
    private DirectionHint directionHint;

    /** The graph. */
    private LayeredGraph graph;

    /**
     * Instantiates a new quick node orderer.
     */
    public QuickNodeOrderer() {
        directionHint = DEFAULT_DIRECTION_HINT;
    }

    /**
     * Instantiates a new quick node orderer.
     * 
     * @param directionHint
     *            the direction hint
     */
    public QuickNodeOrderer(DirectionHint directionHint) {
        this.directionHint = directionHint;
    }

    /**
     * Procedes to node ordering of <tt>graph</tt>.
     * 
     * @param graph
     *            the graph
     * @throws LayoutException
     *             the layout exception
     * @parameter graph A proper layered graph. When the call returns, <tt>graph</tt> as a new node
     *            order where crossing count is minimized.
     */
    @Override
    public void orderNodes(LayeredGraph graph) throws LayoutException {

        if (graph == null) {
            throw new NullPointerException("layeredGraph");
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
     * Order nodes on layer.
     * 
     * @param currentLayer
     *            the current layer
     * @param fixedLayer
     *            the fixed layer
     */
    private void orderNodesOnLayer(int currentLayer, int fixedLayer) {

        int nodeCount = graph.nbVerticesOnLayer(currentLayer);
        Vertex[] order = graph.verticesOnLayer(currentLayer).toArray(new Vertex[nodeCount]);

        split(currentLayer, fixedLayer, order, 0, nodeCount);

        graph.setLayerOrder(currentLayer, Arrays.asList(order));
    }

    /*
     * Recursive QuickSort implementation where the comparison is made between the number of
     * crossings that occur whether each node is situated left or right of another.
     */
    /**
     * Split.
     * 
     * @param currentLayer
     *            the current layer
     * @param fixedLayer
     *            the fixed layer
     * @param order
     *            the order
     * @param i
     *            the i
     * @param j
     *            the j
     */
    private void split(int currentLayer, int fixedLayer, Vertex[] order, int i, int j) {
        if (j - 1 > i) {
            Vertex[] newOrder = new Vertex[j - i];

            int low = i;
            int pivot = i;
            int high = j;

            for (int k = i + 1; k < j; k++) {
                int c_k_pivot = graph.getNbCrossingsBetweenVertices(currentLayer, fixedLayer,
                        order[k], order[pivot]);
                int c_pivot_k = graph.getNbCrossingsBetweenVertices(currentLayer, fixedLayer,
                        order[pivot], order[k]);

                if (c_k_pivot < c_pivot_k) {
                    newOrder[low - i] = order[k];
                    low++;
                } else {
                    newOrder[high - i - 1] = order[k];
                    high--;
                }
            }
            // low == high + 1.
            newOrder[low - i] = order[pivot];

            // apply the new arrangement in order...
            for (int k = i; k < j; k++) {
                order[k] = newOrder[k - i];
            }

            split(currentLayer, fixedLayer, order, i, low);
            split(currentLayer, fixedLayer, order, high, j);
        }
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
}
