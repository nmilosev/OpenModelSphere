/**
 * This algorithm is an implementation of Markus Eiglsperger work on :
 * 
 * <pre>
 * Automatic Layout of UML Class Diagrams:
 * A Topology-Shape-Metrics Approach
 * 
 * Dissertation
 * der Fakult�t f�r Informations- und
 * Kognitionswissenschaften
 * der Eberhard-Karls-Universit�t T�ubingen
 * zur Erlangung des Grades eines
 * Doktors der Naturwissenschaften
 * (Dr. rer. nat.)
 * 
 * vorgelegt von
 * Dipl.-Inform. Markus Eiglsperger
 * aus Wangen am Bodensee
 * 
 * T�bingen 2003
 * </pre>
 */
package org.modelsphere.plugins.layout.nodes.kandinsky;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.modelsphere.jack.srtool.features.layout.LayoutException;
import org.modelsphere.jack.srtool.features.layout.NodesLayoutAlgorithm;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;
import org.modelsphere.jack.srtool.features.layout.graph.Node;
import org.modelsphere.plugins.layout.nodes.kandinsky.planarization.Planarization;
import org.modelsphere.plugins.layout.nodes.kandinsky.planarization.UpwardPlaneStringGraph;
import org.modelsphere.plugins.layout.nodes.kandinsky.struct.AbsoluteKandinskyShape;
import org.modelsphere.plugins.layout.nodes.kandinsky.struct.Label;
import org.modelsphere.plugins.layout.nodes.kandinsky.struct.LoopEdgeRef;

/**
 * Definitions :
 * 
 * <pre>
 * We introduce the concept of style, which defines a mapping
 * from a class diagram graph C to a layout graph L which is defined as:
 *  - A mixed graph G = (V,E),
 *  - a subset D subset of E denoting the directed edges,
 *  - a subset H subset of E denoting the edges which are considered to be part of
 *      a hyperedge,
 *  - a set of labels L
 *  - a function T : L --> {source, center, target} denoting the preferred
 *      position of a label along the edge,
 *  - a function s : V union L --> IN^2 denoting the size of the vertices, resp.
 *      labels, in the drawing.
 * </pre>
 * 
 * @author David
 * 
 */
public class KandinskyLayoutAlgorithm implements NodesLayoutAlgorithm {

    /** The preprocessing. */
    private Preprocessing preprocessing;

    /** The planarization. */
    private Planarization planarization;

    /** The orthogonalization. */
    private Orthogonalization orthogonalization;

    /** The compaction. */
    private Compaction compaction;

    /** The postprocessing. */
    private Postprocessing postprocessing;

    /**
     * Instantiates a new kandinsky layout algorithm.
     * 
     * @param preprocessing
     *            the preprocessing
     * @param planarization
     *            the planarization
     * @param orthogonalization
     *            the orthogonalization
     * @param compaction
     *            the compaction
     * @param postprocessing
     *            the postprocessing
     */
    public KandinskyLayoutAlgorithm(Preprocessing preprocessing, Planarization planarization,
            Orthogonalization orthogonalization, Compaction compaction,
            Postprocessing postprocessing) {
        super();
        this.preprocessing = preprocessing;
        this.planarization = planarization;
        this.orthogonalization = orthogonalization;
        this.compaction = compaction;
        this.postprocessing = postprocessing;
    }

    /**
     * <pre>
     * 1. Preprocessing:
     * (a) Remove edges from D until the edges in D union H induce an acyclic subgraph of G.
     * (b) The edges in H are substituted by stars.
     * (c) If the edges in D do not induce a connected subgraph add edges temporarily to D to make
     * this subgraph connected by using a minimum spanning tree algorithm.
     * 2. Execute algorithm mixed-upward-planarization from Chapter 3 as planarization step.
     * 3. Execute algorithm mixed-upward orthogonalization from Chapter 4 as orthogonalization step.
     * 4. Execute algorithm class diagram compaction from Chapter 5 as compaction step.
     * 5. Postprocessing:
     * (a) Remove all dummy vertices from the graph including: crossings, label vertices, hyperedge
     * vertices, and artificial bends.
     * (b) Place edge labels with preferred placement
     * </pre>
     * 
     * @param mixedGraph
     *            the mixed graph
     * @param clusterNodes
     *            the cluster nodes
     * @throws LayoutException
     *             the layout exception
     * @see org.modelsphere.jack.srtool.features.layout.NodesLayoutAlgorithm#layoutClusterNodes(org.modelsphere.jack.srtool.features.layout.graph.OldGraph,java.util.List)
     */
    @Override
    public void layoutClusterNodes(Graph mixedGraph, List<Node> clusterNodes)
            throws LayoutException {
        List<Label> labels = new ArrayList<Label>();

        preprocessing.setGraphToProcess(mixedGraph);
        List<LoopEdgeRef> selfLoopEdges = preprocessing.removeSelfLoopEdges();
        preprocessing.substituteHyperedges();

        UpwardPlaneStringGraph upwardPlaneGraph = planarization.planarizeGraph(mixedGraph);

        AbsoluteKandinskyShape orthogonalizedGraph = orthogonalization.mixedUpwardOrthogonalization(upwardPlaneGraph);

        compaction.compactGraph(orthogonalizedGraph, selfLoopEdges, labels);

        // No need to remove dummy or hyper vertices because only vertices and
        // edges in metadata will be considered.
        postprocessing.placeEdgeLabels(mixedGraph);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.modelsphere.jack.srtool.features.layout.LayoutAlgorithm# createOptionComponent()
     */
    @Override
    public JComponent createOptionComponent() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.srtool.features.layout.LayoutAlgorithm#getText()
     */
    @Override
    public String getText() {
        return "Kandinsky";
    }

}
