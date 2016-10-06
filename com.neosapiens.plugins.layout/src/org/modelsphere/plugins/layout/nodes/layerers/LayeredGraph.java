package org.modelsphere.plugins.layout.nodes.layerers;

import java.util.*;

import org.modelsphere.jack.srtool.features.layout.graph.*;

/**
 * <p>
 * Represents a directed layered graph.
 * <p>
 * Adds layer-considerations to the com.jgrapht.graph.DefaultDirectedGraph<Vertex, Edge> class.
 * Vertices and edges logic still belongs to the super class.
 * <p>
 * In a layered graph, each vertex is assigned a layer value which is represented by an integer >=
 * 0. Default layer value, when not explicitly specified, is zero.
 * <p>
 * Also, inside a layer, each node is assigned an order (a position in the layer) which also
 * represented as an integer >= 0. When not explicitly specified, each node that is added to a layer
 * gets the next available order value. Inside a layer, all order number must be continuous starting
 * from 0 to <i>the number of nodes on that layer</i> - 1.
 * 
 * @author Gabriel
 */
public class LayeredGraph extends Graph {

    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = -8777488163459684966L;

    /**
     * The (vertex -> layer) map.
     */
    protected Map<Vertex, Integer> layers;

    /**
     * The order inside of each layer. The first level of list represents the layer list, there
     * should be on entry per layer. The second level of list represents the node ordering inside
     * the corresponding layer.
     */
    protected List<List<Vertex>> orders;

    /**
     * The number of layers present in <tt>layers</tt>.
     */
    protected int layerCount;

    /**
     * Instantiates a new layered graph.
     */
    public LayeredGraph() {
        layers = new HashMap<Vertex, Integer>();
        orders = new LinkedList<List<Vertex>>();
        layerCount = 0;
    }

    /**
     * Overrides super class' addVertex method to ensure that every added vertex gets a
     * corresponding layer value. Treatment is done by invoking addVertex(Vertex, int) with zero as
     * the default layer value.
     * 
     * @param vertex
     *            the vertex
     * @return true, if successful
     */
    @Override
    public boolean addVertex(Vertex vertex) {
        return this.addVertex(vertex, 0);
    }

    /**
     * Adds the specified vertex to this graph if not already present and assigns it to the
     * specified layer.
     * 
     * @param vertex
     *            the vertex
     * @param layer
     *            The layer number to assign to the vertex. Must be >= 0.
     * @return true if this graph did not already contain the specified vertex and that the
     *         specified layer is valid. false otherwise.
     */
    public boolean addVertex(Vertex vertex, int layer) {
        if (layer < 0) {
            return false;
        }
        if (!super.addVertex(vertex)) {
            return false;
        }
        layers.put(vertex, layer);
        updateOrdersFirstLevel();
        getVertexOrderList(vertex).add(vertex);
        return true;
    }

    /**
     * Removes the supplied vertex from this graph along with all touching edges.
     * 
     * @param vertex
     *            The vertex to remove from this graph.
     * @return true if the graph contained the specified vertex and that it was removed. false if
     *         the vertex was not part of the graph or if vertex was null.
     * 
     */
    @Override
    public boolean removeVertex(Vertex vertex) {
        if (!super.removeVertex(vertex)) {
            return false;
        }
        getVertexOrderList(vertex).remove(vertex);
        layers.remove(vertex);
        updateOrdersFirstLevel();
        return true;
    }

    /**
     * Assigns a new layer number to the supplied vertex.
     * 
     * @param vertex
     *            The target vertex which already belongs to this graph.
     * @param layer
     *            The new layer number. Must be >= 0.
     * @return true if the operation was successful and 'vertex' has been assigned to 'layer'. false
     *         otherwise.
     */
    public boolean setLayer(Vertex vertex, int layer) {
        if (vertex == null || !containsVertex(vertex) || layer < 0) {
            return false;
        }

        getVertexOrderList(vertex).remove(vertex);
        layers.put(vertex, layer);
        updateOrdersFirstLevel();
        getVertexOrderList(vertex).add(vertex);
        return true;
    }

    /**
     * Gets the layer.
     * 
     * @param vertex
     *            the vertex
     * @return The layer number associated with the supplied vertex. -1 if vertex was null
     */
    public int getLayer(Vertex vertex) {
        if (vertex == null || !layers.containsKey(vertex)) {
            return -1;
        }
        return layers.get(vertex);
    }

    /**
     * Vertices on layer.
     * 
     * @param layer
     *            The specific layer number for which we want to get the vertex set. Must be in the
     *            range 0..(<tt>layerCount</tt> - 1).
     * @return a set of all the nodes that have been assigned to 'layer' -OR- an empty set if the
     *         graph is empty, or if is out of bounds.
     */
    public Set<Vertex> verticesOnLayer(int layer) {
        Set<Vertex> set = new HashSet<Vertex>();
        if (layer < 0 || layer >= layerCount) {
            return set;
        }
        set.addAll(orders.get(layer));
        return set;
    }

    /**
     * Finds the neighbors of a specified vertex that are found on a specified layer.
     * 
     * @param v
     *            A vertex for which the set of neighbor on a particular layer is needed.
     * @param layer
     *            The layer on which we are looking for neighbors.
     * @return The of vertices that are neighbors to and that are found on layer . Does not take
     *         edge orientation into account.
     */
    public Set<Vertex> neighborsOnLayer(Vertex v, int layer) {

        Collection<Vertex> neighbors = neighborsOf(v);
        Set<Vertex> neighborsOnLayer = new HashSet<Vertex>();
        for (Vertex vertex : neighbors) {
            if (getLayer(vertex) == layer) {
                neighborsOnLayer.add(vertex);
            }
        }
        return neighborsOnLayer;
    }

    /**
     * Gets the nb layers.
     * 
     * @return The number of layers present in the graph. Can be 0 if this graph is emtpy.
     */
    public int getNbLayers() {
        return layerCount;
    }

    /**
     * Gets the top layer.
     * 
     * @return The highest layer value found in this layered graph or -1 if this graph is empty.
     */
    public int getTopLayer() {
        return layerCount - 1;
    }

    /**
     * Flip the order of the layers. Meaning that vertices on layer 0 now gets on layer
     * <tt>topLayer</tt> and vice versa and that vertices on layer 1 gets on layer <tt>topLayer</tt>
     * - 1 and so on...
     */
    public void flipLayers() {
        Map<Vertex, Integer> layersUpdate = new HashMap<Vertex, Integer>();
        List<List<Vertex>> ordersUpdate = new ArrayList<List<Vertex>>(layerCount);

        for (Map.Entry<Vertex, Integer> entry : layers.entrySet()) {
            layersUpdate.put(entry.getKey(), layerCount - 1 - entry.getValue());
        }
        for (int i = 0; i < layerCount; i++) {
            ordersUpdate.add(i, orders.get(layerCount - 1 - i));
        }

        layers = layersUpdate;
        orders = ordersUpdate;
    }

    /**
     * Gets the order.
     * 
     * @param v
     *            the v
     * @return The order of vertex - 1)) inside its layer -OR- -1 if is not part of this graph.
     */
    public int getOrder(Vertex v) {
        if (v == null) {
            throw new NullPointerException("v");
        }
        if (!containsVertex(v)) {
            return -1;
        }

        return getVertexOrderList(v).indexOf(v);
    }

    /**
     * Set the order of a vertex on its layer.
     * 
     * @param v
     *            The target vertex.
     * @param order
     *            The position at which the vertex should appear in its layer. Must be between 0 and
     *            <i>nb vertices on the same layer</i> - 1.
     * @return True if success -OR- false if is not a part of this graph, or if is not in the valid
     *         range.
     */
    public boolean setOrder(Vertex v, int order) {
        if (v == null) {
            throw new NullPointerException("v");
        }
        if (!containsVertex(v) || order < 0 || order >= nbVerticesOnLayer(layers.get(v))) {
            return false;
        }

        getVertexOrderList(v).remove(v);
        getVertexOrderList(v).add(order, v);
        return true;
    }

    /**
     * Gets the layer order.
     * 
     * @param layer
     *            The layer number for which we want to get the vertex order list. Must be between 0
     *            and <i>nb layers</i> - 1.
     * @return A list that represents the vertex order on the specified layer.
     */
    public List<Vertex> getLayerOrder(int layer) {
        if (layer < 0 || layer >= layerCount) {
            return new ArrayList<Vertex>();
        } else {
            return new ArrayList<Vertex>(orders.get(layer));
        }
    }

    /**
     * Nb vertices on layer.
     * 
     * @param layer
     *            A layer number.
     * @return The number of vertices that are on the specified layer.
     */
    public int nbVerticesOnLayer(int layer) {
        return orders.get(layer).size();
    }

    /**
     * Sets a whole layer order at once.
     * 
     * @param layer
     *            The taget layer number.
     * @param order
     *            The order to which each vertex should appear. Must contain one, and only one,
     *            reference to each of <tt>verticesOnLayer(layer)</tt>.
     * @return true if the operation is successful and vertices now occupies their new position -OR-
     *         false if the operation could not by completed and the graph is unchanged.
     */
    public boolean setLayerOrder(int layer, List<Vertex> order) {
        if (order == null) {
            throw new NullPointerException("order");
        }
        if (layer < 0 || layer >= layerCount || order.size() != nbVerticesOnLayer(layer)) {
            return false;
        }
        Set<Vertex> layerVertexSet = verticesOnLayer(layer);
        for (Vertex v : order) {
            if (!layerVertexSet.contains(v)) {
                return false;
            } else {
                layerVertexSet.remove(v);
            }
        }
        if (!layerVertexSet.isEmpty()) {
            return false;
        }

        // flush old data.
        orders.remove(layer);
        // insert a copy of 'order' to make sure no external code has a
        // reference to our private
        // data.
        orders.add(layer, new ArrayList<Vertex>(order));

        return true;
    }

    /**
     * Gets the all layer order.
     * 
     * @return the all layer order
     */
    public List<List<Vertex>> getAllLayerOrder() {
        List<List<Vertex>> ordersCopy = new LinkedList<List<Vertex>>();

        for (List<Vertex> layerOrder : orders) {
            ordersCopy.add(new ArrayList<Vertex>(layerOrder));
        }

        return ordersCopy;
    }

    /**
     * Sets the all layer order.
     * 
     * @param orders
     *            the orders
     * @return true, if successful
     */
    public boolean setAllLayerOrder(List<List<Vertex>> orders) {
        if (orders == null) {
            throw new NullPointerException("orders");
        }
        if (orders.size() != layerCount) {
            return false;
        }

        List<List<Vertex>> backup = getAllLayerOrder();

        boolean success = true;
        for (int i = 0; i < orders.size() && success; i++) {
            success = setLayerOrder(i, orders.get(i));
        }

        if (success) {
            return true;
        } else {
            setAllLayerOrder(backup);
            return false;
        }
    }

    /**
     * <p>
     * Note: This method will give meaningful results only if used on a <i>properly</i> layered
     * graph.
     * 
     * @return The number of edges crossing in the graph. The real treatment is done through the
     *         method that gets called once for each contiguous layer pair in the graph.
     */
    public int getNbCrossings() {
        int crossCount = 0;

        for (int i = 0; i < layerCount - 1; i++) {
            crossCount += getNbCrossingsBetween(i, i + 1);
        }

        return crossCount;
    }

    /**
     * <p>
     * Compute the number of edge crossing that occurs between two layers.
     * <p>
     * Note: This method will give meaningful results only if used on a <i>proper</i> graph.
     * 
     * @param layer1
     *            A layer value. Must be in the range 0..<tt>layerCount</tt> - 1.
     * @param layer2
     *            Another layer contiguous to <tt>layer1</tt>. Must be one of <tt>layer1</tt> - 1 or
     *            <tt>layer1</tt> + 1 and still fit in the range 0..<tt>layerCount</tt> - 1.
     * @return The number of crossing edges detected between layer1 and layer2.
     */
    public int getNbCrossingsBetween(int layer1, int layer2) {
        // Assertions.
        // Are layers in range?
        if (layer1 < 0 || layer1 >= layerCount || layer2 < 0 || layer2 >= layerCount) {
            return -1;
        }
        // Are both layers the same?
        if (layer1 == layer2) {
            return -1;
        }
        // Are layers contiguous?
        if (Math.abs(layer2 - layer1) > 1) {
            return -1;
        }

        int crossCount = 0;

        // for each edge pair...
        List<Edge> edges = findEdgesBetweenTwoLayers(layer1, layer2);
        for (int i = 0; i < edges.size() - 1; i++) {
            for (int j = i + 1; j < edges.size(); j++) {
                // Compare the order difference on both layers,
                // if the signs are different, both edges cross...
                Edge e1 = edges.get(i);
                Edge e2 = edges.get(j);

                // first we need to know which vertex is which...
                Vertex v1onLayer1 = null;
                Vertex v1onLayer2 = null;
                if (getLayer(getEdgeSource(e1)) == layer1) {
                    v1onLayer1 = getEdgeSource(e1);
                    v1onLayer2 = getEdgeTarget(e1);
                } else {
                    v1onLayer1 = getEdgeTarget(e1);
                    v1onLayer2 = getEdgeSource(e1);
                }

                Vertex v2onLayer1 = null;
                Vertex v2onLayer2 = null;
                if (getLayer(getEdgeSource(e2)) == layer1) {
                    v2onLayer1 = getEdgeSource(e2);
                    v2onLayer2 = getEdgeTarget(e2);
                } else {
                    v2onLayer1 = getEdgeTarget(e2);
                    v2onLayer2 = getEdgeSource(e2);
                }

                // then we compute the difference in the order
                // of the end-points of each edge on one layer.
                float diffOnLayer1 = getOrder(v2onLayer1) - getOrder(v1onLayer1);
                float diffOnLayer2 = getOrder(v2onLayer2) - getOrder(v1onLayer2);

                // If both differences are of different signs, it means the
                // edges cross.
                if ((diffOnLayer1 * diffOnLayer2) < 0) {
                    crossCount++;
                }
            }
        }
        return crossCount;
    }

    /**
     * <p>
     * Compute the response to the following question: How many crossings are found between edges of
     * <tt>v1</tt> and <tt>v2</tt> that link them to vertices in <tt>fixedLayer</tt> if <tt>v1</tt>
     * comes before (has a smaller order value) than <tt>v2</tt> in <tt>currentLayer</tt>.
     * <p>
     * The method consider the actual order of <tt>fixedLayer</tt> completely ignoring actual order
     * for <tt>fixedLayer</tt>. It is irrelevant to the question at hand anyway.
     * 
     * @param currentLayer
     *            The current layer that contains <tt>v1</tt> and <tt>v2</tt>. Must be a valid layer
     *            number, in the range 0.. <tt>layerCount</tt> - 1.
     * @param fixedLayer
     *            The other layer that serves to determine which edges to consider during crossing
     *            count. Must be one of <tt>currentLayer</tt> - 1 or <tt>currentLayer</tt> + 1 and
     *            still fit in the range 0..<tt>layerCount</tt> - 1
     * @param v1
     *            The first vertex for edge selection. It is assumed to come before <tt>v2</tt> in
     *            <tt>currentLayer</tt> when computing the number of crossings. Must be part of
     *            <tt>currentLayer</tt>.
     * @param v2
     *            The second vertex for edge selection. It is assumed to come after <tt>v1</tt> in
     *            <tt>currentLayer</tt> when computing the number of crossings. Must be part of
     *            <tt>currentLayer</tt>.
     * @return The number of crossings of edges adjacent to between comes before -OR- -1 if one the
     *         arguments does not respect the preconditions.
     */
    public int getNbCrossingsBetweenVertices(int currentLayer, int fixedLayer, Vertex v1, Vertex v2) {

        if (v1 == null) {
            throw new NullPointerException("v1");
        }
        if (v2 == null) {
            throw new NullPointerException("v2");
        }
        // Are v1 and v2 the same?
        if (v1.equals(v2)) {
            return -1;
        }
        // Is the currentLayer in range?
        if (currentLayer < 0 || currentLayer >= layerCount) {
            return -1;
        }
        // Is the fixedLayer in range?
        if (fixedLayer < 0 || fixedLayer >= layerCount || Math.abs(fixedLayer - currentLayer) != 1) {
            return -1;
        }
        // Are v1 and v2 part of currentLayer?
        if (getLayer(v1) != currentLayer || getLayer(v2) != currentLayer) {
            return -1;
        }

        int crossingCount = 0;

        Set<Vertex> v1Neighbors = neighborsOnLayer(v1, fixedLayer);
        Set<Vertex> v2Neighbors = neighborsOnLayer(v2, fixedLayer);

        if (!v1Neighbors.isEmpty() && !v2Neighbors.isEmpty()) {
            for (Vertex v1Neighbor : v1Neighbors) {
                for (Vertex v2Neighbor : v2Neighbors) {
                    if (!v1Neighbor.equals(v2Neighbor)) {
                        if (getOrder(v2Neighbor) < getOrder(v1Neighbor)) {
                            crossingCount++;
                        }
                    }
                }
            }
        }
        return crossingCount;
    }

    /**
     * Checks if is proper.
     * 
     * @return whether this graph is proper or not.
     */
    public boolean isProper() {
        return findImproperEdges().size() == 0;
    }

    /**
     * Insert necessary dummy-vertex so that the resulting layered graph is <i>proper</i>. Meaning
     * that for every edge, the source and target vertices are on contiguous layers.
     */
    public void makeProper() {
        Set<Edge> problems = findImproperEdges();
        for (Edge e : problems) {
            fixEdge(e);
        }
    }

    /**
     * Find improper edges.
     * 
     * @return The set of edges in this graph that span across multiple layers.
     */
    private Set<Edge> findImproperEdges() {
        Set<Edge> improperEdges = new HashSet<Edge>();

        for (Vertex source : vertexSet()) {
            int sourceLayer = getLayer(source);
            for (Edge e : outgoingEdgesOf(source)) {
                Vertex target = getEdgeTarget(e);
                int targetLayer = getLayer(target);
                int diff = Math.abs(sourceLayer - targetLayer);
                if (diff > 1) {
                    improperEdges.add(e);
                }
            }
        }
        return improperEdges;
    }

    /**
     * Inserts the necessary vertices between <tt>e.source</tt> and <tt>e.target</tt> so that
     * <tt>e</tt> is replaced by a new path of edges connecting the source and the target and
     * passing by newly created vertices one layer at a time.
     * 
     * @param e
     *            A problematic edge in this graph.
     */
    private void fixEdge(Edge e) {
        Vertex source = getEdgeSource(e);
        int sourceLayer = getLayer(source);
        Vertex target = getEdgeTarget(e);
        int targetLayer = getLayer(target);

        int diff = Math.abs(sourceLayer - targetLayer);
        int neededNullNodes = diff - 1;
        if (neededNullNodes > 0) {
            int direction = -1;
            if (sourceLayer < targetLayer) {
                direction = 1;
            }
            Vertex previous = source;
            for (int i = 0; i < neededNullNodes; i++) {
                Vertex v = new DummyVertex(e);
                addVertex(v, sourceLayer + (i + 1) * direction);
                addEdge(previous, v);
                previous = v;
            }
            addEdge(previous, target);
            removeEdge(source, target);
        }
    }

    /**
     * <p>
     * Make sure that the first-level list of <tt>orders</tt> features as many second-level lists as
     * there are layers in the graph.
     * <p>
     * The method calls <tt>updateNbLayers</tt> to make sure the layer count is up to date.
     */
    private void updateOrdersFirstLevel() {

        updateNbLayers();

        // If there are no layers in the graph (hence the graph is empty)
        if (layerCount == 0) {
            // sets an empty list.
            orders = new ArrayList<List<Vertex>>();
        } else {
            // If the graph is not empty
            List<List<Vertex>> newList = new ArrayList<List<Vertex>>(layerCount);
            for (int i = 0; i < layerCount; i++) {
                // If there was already an order
                if (i < orders.size()) {
                    newList.add(i, orders.get(i));
                } else {
                    newList.add(i, new ArrayList<Vertex>());
                }
            }
            orders = newList;
        }
    }

    /**
     * Update <code>topLayer</code>'s content so that it reflects the highest layer value found in
     * <code>layers</code>.
     * 
     * @return 's new value.
     */
    private int updateNbLayers() {
        int topLayer = -1;
        for (Integer value : layers.values()) {
            if (value > topLayer) {
                topLayer = value;
            }
        }
        layerCount = topLayer + 1;
        return layerCount;
    }

    /**
     * Gets the vertex order list.
     * 
     * @param v
     *            the v
     * @return The second-level list, in that corresponds to the layer in which is.
     */
    private List<Vertex> getVertexOrderList(Vertex v) {
        return orders.get(layers.get(v));
    }

    /**
     * Find edges between two layers.
     * 
     * @param layer1
     *            the layer1
     * @param layer2
     *            the layer2
     * @return the list
     */
    private List<Edge> findEdgesBetweenTwoLayers(int layer1, int layer2) {
        List<Edge> result = new ArrayList<Edge>();

        List<Vertex> list1 = orders.get(layer1);
        List<Vertex> list2 = orders.get(layer2);

        for (Vertex v : list1) {
            for (Edge e : outgoingEdgesOf(v)) {
                if (list2.contains(getEdgeTarget(e))) {
                    result.add(e);
                }
            }
            for (Edge e : incomingEdgesOf(v)) {
                if (list2.contains(getEdgeSource(e))) {
                    result.add(e);
                }
            }
        }
        return result;
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

    /**
     * Gets the edges between layer.
     * 
     * @param sourceLayer
     *            the source layer
     * @param targetLayer
     *            the target layer
     * @return the edges between layer
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

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.srtool.features.layout.graph.Graph#clone()
     */
    @Override
    public LayeredGraph clone() {
        LayeredGraph graph = new LayeredGraph();

        for (int layer = 0; layer < layerCount; layer++) {
            List<Vertex> vertices = orders.get(layer);
            for (Vertex v : vertices) {
                graph.addVertex(v, layer);
            }
        }

        for (Edge e : edgeSet()) {
            graph.addEdge(getEdgeSource(e), getEdgeTarget(e), e);
        }

        return graph;
    }
}
