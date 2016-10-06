package org.modelsphere.plugins.layout.nodes.sugiyama;

import org.jgrapht.alg.CycleDetector;
import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.jack.srtool.features.layout.graph.Edge;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;
import org.modelsphere.plugins.layout.CyclicGraphException;

/**
 * Reject any graph that contains a cycle by throwing a StageError during removeCycles stage. Do
 * nothing otherwise.
 * 
 * @author Gabriel
 * 
 */
public class CycleRejecter extends CycleRemover {

    /**
     * Search for a cycle. If one is found, throws a StageError.
     * 
     * @param graph
     *            the graph
     * @throws LayoutException
     *             the layout exception
     */
    @Override
    public void removeCycles(Graph graph) throws LayoutException {

        CycleDetector<Vertex, Edge> cycleDetector = new CycleDetector<Vertex, Edge>(graph);

        if (cycleDetector.detectCycles()) {
            throw new CyclicGraphException(null, "This graph contains a cycle. Process interrupted");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.plugins.layout.nodes.sugiyama.CycleRemover#restoreCycles
     * (org.modelsphere. plugins.layout.nodes.sugiyama.LayeredGraph)
     */
    @Override
    public void restoreCycles(Graph graph) throws LayoutException {
        /*
         * Nothig to do here. There is not cycle to restore since this object doesn't remove them.
         */
    }
}
