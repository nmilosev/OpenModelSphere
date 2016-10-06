package org.modelsphere.plugins.layout.nodes.kandinsky.struct;

import org.modelsphere.jack.srtool.features.layout.graph.Edge;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;

/**
 * <p>
 * Used to keep a track of loop edges that were removed during preprocessing.
 * 
 * @author Gabriel
 * 
 */
public class LoopEdgeRef {

    /** The edge. */
    private Edge edge;

    /** The vertex. */
    private Vertex vertex;

    /**
     * Instantiates a new loop edge ref.
     * 
     * @param edge
     *            A loop edge.
     * @param vertex
     *            The vertex it was originating from and going to.
     */
    public LoopEdgeRef(Edge edge, Vertex vertex) {
        this.edge = edge;
        this.vertex = vertex;
    }

    /**
     * Gets the edge.
     * 
     * @return The edge reference.
     */
    public Edge getEdge() {
        return edge;
    }

    /**
     * Gets the vertex.
     * 
     * @return The vertex reference.
     */
    public Vertex getVertex() {
        return vertex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof LoopEdgeRef) {
            LoopEdgeRef ref = (LoopEdgeRef) o;
            if (edge == ref.getEdge() && vertex == ref.getVertex()) {
                return true;
            }
        }
        return false;
    }
}
