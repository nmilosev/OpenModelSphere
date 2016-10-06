package org.modelsphere.plugins.layout.nodes.kandinsky.struct;

import org.modelsphere.jack.srtool.features.layout.graph.Edge;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;

/**
 * Used to keep track of directed edges that were removed duing preprocessing.
 * 
 * @author Gabriel
 * 
 */
public class DirectedEdgeRef {

    /** The edge. */
    private Edge edge;

    /** The source. */
    private Vertex source;

    /** The target. */
    private Vertex target;

    /**
     * Instantiates a new directed edge ref.
     * 
     * @param edge
     *            A directed edge.
     * @param source
     *            The vertex it was originating from.
     * @param target
     *            The vertex it was targeting to.
     */
    public DirectedEdgeRef(Edge edge, Vertex source, Vertex target) {
        this.edge = edge;
        this.source = source;
        this.target = target;
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
     * Gets the source.
     * 
     * @return The source reference.
     */
    public Vertex getSource() {
        return source;
    }

    /**
     * Gets the target.
     * 
     * @return The target reference.
     */
    public Vertex getTarget() {
        return target;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof DirectedEdgeRef) {
            DirectedEdgeRef ref = (DirectedEdgeRef) o;
            if (edge == ref.getEdge() && source == ref.getSource() && target == ref.getTarget()) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return ("" + edge.toString() + "|" + source.toString() + "|" + target.toString()).hashCode();
    }
}
