package org.modelsphere.plugins.layout.nodes.sugiyama;

import org.jgrapht.VertexFactory;
import org.modelsphere.jack.srtool.features.layout.graph.DummyVertex;
import org.modelsphere.jack.srtool.features.layout.graph.Edge;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;

/**
 * A factory for creating DummyVertex objects.
 */
public class DummyVertexFactory implements VertexFactory<Vertex> {

    /** The replaced edges. */
    private Edge[] replacedEdges;

    /**
     * Instantiates a new dummy vertex factory.
     */
    public DummyVertexFactory() {
        this(new Edge[0]);
    }

    /**
     * Instantiates a new dummy vertex factory.
     * 
     * @param replacedEdge
     *            the replaced edge
     */
    public DummyVertexFactory(Edge replacedEdge) {
        this(new Edge[] { replacedEdge });
    }

    /**
     * Instantiates a new dummy vertex factory.
     * 
     * @param replacedEdges
     *            the replaced edges
     */
    public DummyVertexFactory(Edge[] replacedEdges) {
        this.replacedEdges = replacedEdges;
    }

    /**
     * Generates a DummyVertex.
     * 
     * @return A node whose GO, SO, and name attributes are set to null and whose bounding is set to
     *         (0, 0, -1, -1) with a weight of 0.
     */
    @Override
    public Vertex createVertex() {
        Vertex v = new DummyVertex(replacedEdges);
        return v;
    }
}
