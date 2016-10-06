/**
 * 
 */
package org.modelsphere.plugins.layout.nodes.kandinsky.planarization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.modelsphere.jack.srtool.features.layout.graph.Edge;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;
import org.modelsphere.plugins.layout.nodes.layerers.LayeredGraph;

/**
 * Adds the following restrictions : edges's source will always be in a lower or equal layer than
 * his target.
 * 
 * @author David
 * 
 */
public class UpwardPlaneGraph extends LayeredGraph {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5834059523200800788L;

    /**
     * Create a default edge with Association as RelationType and Free as OrientationConstraint.
     * 
     * @param sourceVertex
     *            the source vertex
     * @param targetVertex
     *            the target vertex
     * @return the edge
     * @see org.jgrapht.graph.AbstractBaseGraph#addEdge(java.lang.Object, java.lang.Object)
     */
    @Override
    public Edge addEdge(Vertex sourceVertex, Vertex targetVertex) {

        if (getLayer(sourceVertex) >= getLayer(targetVertex)) {
            throw new IllegalArgumentException(
                    "Source vertex must always be on a lower layer than target vertex. Source is on layer "
                            + getLayer(sourceVertex) + " and target is on layer "
                            + getLayer(targetVertex));
        }
        return super.addEdge(sourceVertex, targetVertex);
    }

    /**
     * This edge will be added only if his source vertex layer is lower than is his target layer.
     * 
     * @param sourceVertex
     *            the source vertex
     * @param targetVertex
     *            the target vertex
     * @param edge
     *            the edge
     * @return true, if successful
     * @see org.jgrapht.graph.AbstractBaseGraph#addEdge(java.lang.Object, java.lang.Object,
     *      java.lang.Object)
     */
    @Override
    public boolean addEdge(Vertex sourceVertex, Vertex targetVertex, Edge edge) {

        if (getLayer(sourceVertex) >= getLayer(targetVertex)) {
            throw new IllegalArgumentException(
                    "Source vertex must always be on a lower layer than target vertex. Source is on layer "
                            + getLayer(sourceVertex) + " and target is on layer "
                            + getLayer(targetVertex));
        }
        return super.addEdge(sourceVertex, targetVertex, edge);
    }

    /**
     * Sets the layer.
     * 
     * @param vertex
     *            the vertex
     * @param layer
     *            the layer
     * @return true, if successful
     * @see org.modelsphere.plugins.layout.nodes.layerers.LayeredGraph#setLayer(org.modelsphere.jack.srtool.features.layout.graph.Vertex,
     *      int)
     */
    @Override
    public boolean setLayer(Vertex vertex, int layer) {

        for (Edge edge : incomingEdgesOf(vertex)) {
            if (getLayer(getEdgeSource(edge)) >= layer) {
                throw new IllegalArgumentException(
                        "This vertex have incoming edges that prevent it to move from layer "
                                + getLayer(getEdgeSource(edge)) + "to layer " + layer);
            }
        }

        for (Edge edge : outgoingEdgesOf(vertex)) {
            if (layer >= getLayer(getEdgeTarget(edge))) {
                throw new IllegalArgumentException(
                        "This vertex have ougoing edges that prevent it to move from layer "
                                + getLayer(getEdgeTarget(edge)) + " to layer " + layer);
            }
        }
        return super.setLayer(vertex, layer);
    }

    /**
     * Insert new layer above.
     * 
     * @param layer
     *            the layer
     */
    public void insertNewLayerAbove(int layer) {

        for (Entry<Vertex, Integer> entry : layers.entrySet()) {
            if (entry.getValue() > layer) {
                layers.put(entry.getKey(), entry.getValue() + 1);
            }
        }
        orders.add(layer + 1, new ArrayList<Vertex>());

        layerCount++;
    }

    /**
     * Suppose that vertices source and target form an edge, then this method will return all edges
     * crossing it.
     * 
     * @param source
     *            the source
     * @param target
     *            the target
     * @return the crossing edges of
     */
    public List<Edge> getCrossingEdgesOf(Vertex source, Vertex target) {
        List<Edge> crossedEdges = new ArrayList<Edge>();

        int sourceLayer = getLayer(source);
        int targetLayer = getLayer(target);

        List<Edge> edgesBetweenLayer = getEdgesBetweenLayer(sourceLayer, targetLayer);

        int sourceOrder = getOrder(source);
        int targetOrder = getOrder(target);

        for (Edge edge : edgesBetweenLayer) {
            int edgeSourceOrder = getOrder(getEdgeSource(edge));
            int edgeTargetOrder = getOrder(getEdgeTarget(edge));

            if (sourceOrder < edgeSourceOrder) {
                if (targetOrder > edgeTargetOrder) {
                    crossedEdges.add(edge);
                }
            } else if (sourceOrder > edgeSourceOrder) {
                if (targetOrder < edgeTargetOrder) {
                    crossedEdges.add(edge);
                }
            }
        }
        return crossedEdges;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.plugins.layout.nodes.layerers.LayeredGraph#getEdgesBetweenLayer(int,
     * int)
     */
    public List<Edge> getEdgesBetweenLayer(int sourceLayer, int targetLayer) {
        List<Edge> edges = new ArrayList<Edge>();

        for (Edge edge : edgeSet()) {
            int edgeSourceLayer = getLayer(getEdgeSource(edge));
            int edgeTargetLayer = getLayer(getEdgeTarget(edge));

            if (edgeSourceLayer > sourceLayer) {
                if (edgeSourceLayer < targetLayer) {
                    edges.add(edge);
                }
            } else if (edgeSourceLayer < sourceLayer) {
                if (edgeTargetLayer > sourceLayer) {
                    edges.add(edge);
                }
            } else {
                edges.add(edge);
            }
        }
        return edges;
    }
}
