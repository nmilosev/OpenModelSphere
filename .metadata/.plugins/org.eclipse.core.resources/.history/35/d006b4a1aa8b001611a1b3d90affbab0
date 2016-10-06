package org.modelsphere.plugins.layout.nodes.kandinsky;

import java.util.*;

import org.modelsphere.jack.srtool.features.layout.graph.Edge;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;
import org.modelsphere.jack.srtool.features.layout.graph.Edge.OrientationConstraint;
import org.modelsphere.plugins.layout.nodes.kandinsky.planarization.UpwardPlaneStringGraph;
import org.modelsphere.plugins.layout.nodes.kandinsky.struct.*;

/**
 * <p>
 * Execute algorithm mixed-upward orthogonalization as orthogonalization step.
 * <p>
 * The input is a mixed upward planar <i>st</i>-graph and a mapping of vertex type.
 * 
 * @author David
 * 
 */
public class Orthogonalization {
    /**
     * The Enum BendType.
     */
    enum BendType {

        /** The concave. */
        concave,

        /** The convex. */
        convex
    }

    /**
     * The Enum KandinskyProperties.
     */
    enum KandinskyProperties {

        /** The bend or end property. */
        bendOrEndProperty, // Definition p65
        /** The non empty face property. */
        nonEmptyFaceProperty
        // Definition p66
    }

    /**
     * The Enum VertexShape.
     */
    enum VertexShape {

        /** The up. */
        up,
        /** The down. */
        down,
        /** The left. */
        left,
        /** The right. */
        right
    }

    /**
     * Mixed upward orthogonalization.
     * 
     * <p>
     * Execute algorithm mixed-upward orthogonalization as orthogonalization step.
     * <p>
     * The input is a mixed upward planar <i>st</i>-graph and a mapping of vertex type.
     * 
     * @param upwardPlaneStringGraph
     *            the upward plane string graph
     * @return absolute Kandinsky shape S
     */
    AbsoluteKandinskyShape mixedUpwardOrthogonalization(
            UpwardPlaneStringGraph upwardPlaneStringGraph) {

        Set<Edge> edges = getAllDirectedEdges(upwardPlaneStringGraph);

        // Remove all directed edges temporarily
        List<DirectedEdgeRef> removedEdges = removeAllDirectedEdges(upwardPlaneStringGraph, edges);

        // QD = Upward-Orthogonalization(G)
        upwardOrthogonalization(upwardPlaneStringGraph);

        // Reinsert temporarily removed edges
        insertAllDirectedEdges(upwardPlaneStringGraph, removedEdges);

        // Construct set of angle constraints AC from QD
        Set<AngleConstraint> angleConstraints = construcAngleConstraints(upwardPlaneStringGraph);

        // Construct set of bend constraints BC from QD
        Set<BendConstraint> bendConstraints = construcBendConstraints(upwardPlaneStringGraph);

        // Q = Constrained-Kandinsky(G,AC,BC)
        constrainedKandinsky(upwardPlaneStringGraph, angleConstraints, bendConstraints);

        // Compute S from Q
        computeSfromQ();
        // TODO:
        AbsoluteKandinskyShape kadinskyShape = null;
        return kadinskyShape;
    }

    /**
     * Gets the all directed edges. In the UpwardPlaneStringGraph, all edges are directed
     * 
     * @param upwardPlaneStringGraph
     *            the upward plane string graph
     * @return the all directed edges
     */
    Set<Edge> getAllDirectedEdges(UpwardPlaneStringGraph upwardPlaneStringGraph) {
        Set<Edge> edges = upwardPlaneStringGraph.edgeSet();
        Set<Edge> directed = new LinkedHashSet<Edge>();
        for (Edge e : edges) {
            if (!e.getOrientationConstraint().equals(OrientationConstraint.UNDIRECTED)) {
                directed.add(e);
            }
        }
        return directed;
    }

    /**
     * Removes the all directed edges. In the UpwardPlaneStringGraph, all edges are directed
     * 
     * @param upwardPlaneStringGraph
     *            the upward plane string graph
     * @param edges
     *            the edges
     * @return the list
     */
    List<DirectedEdgeRef> removeAllDirectedEdges(UpwardPlaneStringGraph upwardPlaneStringGraph,
            Set<Edge> edges) {
        List<DirectedEdgeRef> removedEdges = new ArrayList<DirectedEdgeRef>();

        for (Edge edge : edges) {
            removedEdges.add(new DirectedEdgeRef(edge, upwardPlaneStringGraph.getEdgeSource(edge),
                    upwardPlaneStringGraph.getEdgeTarget(edge)));
            upwardPlaneStringGraph.removeEdge(edge);
        }
        return removedEdges;
    }

    /**
     * Insert all directed edges. In the UpwardPlaneStringGraph, all edges are directed
     * 
     * @param upwardPlaneStringGraph
     *            the upward plane string graph
     * @param edges
     *            the edges
     * @return the graph
     */
    UpwardPlaneStringGraph insertAllDirectedEdges(UpwardPlaneStringGraph upwardPlaneStringGraph,
            List<DirectedEdgeRef> edges) {
        for (DirectedEdgeRef edgeRef : edges) {
            upwardPlaneStringGraph.addEdge(edgeRef.getSource(), edgeRef.getTarget(),
                    edgeRef.getEdge());
        }
        return upwardPlaneStringGraph;
    }

    // TODO NOT IMPLEMENTED
    /**
     * Construc angle constraints.
     * 
     * @param upwardPlaneStringGraph
     *            the upward plane string graph
     * @return the set
     */
    Set<AngleConstraint> construcAngleConstraints(UpwardPlaneStringGraph upwardPlaneStringGraph) {
        return null;
    }

    // TODO NOT IMPLEMENTED
    /**
     * Construc bend constraints.
     * 
     * @param upwardPlaneStringGraph
     *            the upward plane string graph
     * @return the set
     */
    Set<BendConstraint> construcBendConstraints(UpwardPlaneStringGraph upwardPlaneStringGraph) {
        return null;
    }

    // TODO NOT IMPLEMENTED
    /**
     * Compute sfrom q.
     */
    void computeSfromQ() {

    }

    /**
     * We assume that the inEdges(v) and outEdges(v) are ordered from left to right according to the
     * mixed upward embedding.
     * 
     * Algorithm7: Upward-Orthogonalization.Weassumethatthe edgesin in(v)and
     * out(v)areorderedfromlefttorightaccording tothemixedupwardembedding. Input:
     * Upwardplanarization G =(V,E,F), Mapping type : V â†’{vertex , crossing, hypervertex} Output:
     * Absolutequasi-orthogonalshape S page 104 (dans le PDF)
     * 
     * @param upwardPlaneStringGraph
     *            the upward plane string graph
     * @return Absolute quasi-orthogonal shape S
     */
    Graph upwardOrthogonalization(UpwardPlaneStringGraph upwardPlaneStringGraph) {

        // tail-shape
        tailShape(upwardPlaneStringGraph);
        // head-shape
        headShape(upwardPlaneStringGraph);
        // Assignshapestoreverseedges
        assignShapeToReversedEdges(upwardPlaneStringGraph);
        // Assignangles
        assignAngles(upwardPlaneStringGraph);

        // TODO: NOT IMPLEMENTED
        return null;
    }

    /**
     * Tail shape.
     * 
     * @param upwardPlaneStringGraph
     *            the upward plane string graph
     */
    private void tailShape(UpwardPlaneStringGraph upwardPlaneStringGraph) {
        // TODO: Not implemented

        // foreach v of V {
        for (Vertex vertex : upwardPlaneStringGraph.vertexSet()) {
            /*
             * l = out(v), m = median(l), Ss(lm)=â€? â†‘ â€? if type(v)= crossing then if m =0 then
             * Ss(l1)=â€? â†’â†‘ â€? else Ss(l0)=â€? â†?â†‘ â€? else for 0 â‰¤ i<m { Ss(li)=â€? â†‘â†?â†‘ â€? } for m<i< |l| {
             * Ss(li)=â€? â†‘â†’â†‘ â€? }
             */
        }
    }

    /**
     * Head shape.
     * 
     * @param upwardPlaneStringGraph
     *            the upward plane string graph
     */
    private void headShape(UpwardPlaneStringGraph upwardPlaneStringGraph) {
        // TODO: Not implemented
        // foreach v of V
        for (Vertex vertex : upwardPlaneStringGraph.vertexSet()) {
            /*
             * l = in(v), m = median(l) if type(v)= crossing then if m =0 then Ss(l1)+=â€? â†? â€? else
             * Ss(l0)+=â€? â†’ â€? else if type(v)= hypervertex then for 0 â‰¤ i<m do Ss(li)+=â€? â†’ â€? for m<i<
             * |l| do Ss(li)+=â€? â†? â€? else for 0 â‰¤ i<m do Ss(li)+=â€? â†’â†‘ â€? for m<i< |l| do Ss(li)+=â€? â†?â†‘
             * â€?
             */
        }

    }

    /**
     * Assign shape to reversed edges.
     * 
     * @param upwardPlaneStringGraph
     *            the upward plane string graph
     */
    private void assignShapeToReversedEdges(UpwardPlaneStringGraph upwardPlaneStringGraph) {
        // TODO: Not implemented
        // foreach e of E
        Set<Edge> edges = upwardPlaneStringGraph.edgeSet();
        for (Edge edge : edges) {
            /*
             * //E=e voir 104 if Ss(e)= E Ss(e)= Ss(Â¯e)
             */
        }

    }

    /**
     * Assign angles.
     * 
     * @param upwardPlaneStringGraph
     *            the upward plane string graph
     */
    private void assignAngles(UpwardPlaneStringGraph upwardPlaneStringGraph) {
        // TODO: Not implemented
        // foreach v of V
        for (Vertex vertex : upwardPlaneStringGraph.vertexSet()) {
            /*
             * for 0 â‰¤ i< |out(v)|âˆ’ 1 { Sa(out(v)i)=0 } if Î´âˆ’(v)=0 Sa(out(v)|out(v)|âˆ’1)=4) else
             * Sa(out(v)|out(v)âˆ’1|)=2 for 1 â‰¤ i< |in(v)|âˆ’ 1 { Sa(in(v)i)=0 } if Î´+(v)=0
             * Sa(in(v)0)=4) else Sa(in(v)0)=2
             */
        }
    }

    /**
     * Constrained kandinsky.
     * 
     * @param upwardPlaneStringGraph
     *            the upward plane string graph
     * @param angleConstraints
     *            the angle constraints
     * @param bendConstraints
     *            the bend constraints
     */
    void constrainedKandinsky(UpwardPlaneStringGraph upwardPlaneStringGraph,
            Set<AngleConstraint> angleConstraints, Set<BendConstraint> bendConstraints) {

    }

    /*
     * Minimal Cost (Preposition Euler Algorithm pafe 60-61) Best Algorithm is Arc Partition Minimum
     * Cost Flow Network page 67-70 (IS NP-HARD proof p72)
     */
    /**
     * Bend minimization.
     */
    private void bendMinimization() {
        // GOTO Successive Shortest Path Algorithm
    }

    /*
     * Page 78-80
     */
    /**
     * Transformation algorithm.
     */
    private void transformationAlgorithm() {

    }

    /*
     * After this assignment we perform bend stretching transformations to re- duce the number of
     * bends. A bend stretching transformation removes su- perï¬‚uous bends from an edge by assigning
     * a new shape with less bends to it without changing the ï¬?rst and last direction in the shape.
     * Table 4.1 shows all bend-stretching transformations. (Page 93)
     */
    /**
     * Bend stretching.
     */
    public void bendStretching() {

    }

}
