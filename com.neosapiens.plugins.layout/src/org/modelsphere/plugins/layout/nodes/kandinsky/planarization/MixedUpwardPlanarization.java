package org.modelsphere.plugins.layout.nodes.kandinsky.planarization;

import java.util.ArrayList;
import java.util.List;

import org.modelsphere.jack.srtool.features.layout.graph.Edge;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;
import org.modelsphere.jack.srtool.features.layout.graph.Vertex;

/**
 * <p>
 * Output of Planarization step and input of Orthogonalization step.
 * <p>
 * Represents a Plane <i>st</i>-graph (string graph).
 * 
 * @author Gabriel
 * 
 */
public class MixedUpwardPlanarization extends Graph {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7977311642269618460L;

    /**
     * The Enum DartDirection.
     * 
     * @see Dart
     * @author Gabriel
     */
    public enum DartDirection {

        /** The SAME. */
        SAME,
        /** The OPPOSITE. */
        OPPOSITE
    }

    /**
     * <p>
     * Represents a Dart in a plane graph.
     * <p>
     * For each edge inserted in this graph, two darts are created, one pointing the same way as the
     * edge and one pointing the opposite way.
     * <p>
     * Those darts are used to describe a {@link Face}.
     * <p>
     * Reference: Section 2.1.4
     * 
     * @author Gabriel
     * 
     */
    public class Dart {

        /** The edge. */
        private Edge edge;

        /** The direction. */
        private DartDirection direction;

        /**
         * Creates a new instance of <tt>Dart</tt>.
         * 
         * @param edge
         *            The edge it is referring to.
         * @param direction
         *            Whether the <tt>Dart</tt> is in the same direction as the edge or in the
         *            opposite.
         */
        public Dart(Edge edge, DartDirection direction) {
            if (edge == null) {
                throw new NullPointerException("edge");
            }
            if (direction == null) {
                throw new NullPointerException("direction");
            }
            this.edge = edge;
            this.direction = direction;
        }

        /**
         * Gets the source.
         * 
         * @return this 's source.
         */
        public Vertex getSource() {
            if (direction == DartDirection.SAME) {
                return getEdgeSource(edge);
            } else {
                return getEdgeTarget(edge);
            }
        }

        /**
         * Gets the target.
         * 
         * @return this 's target.
         */
        public Vertex getTarget() {
            if (direction == DartDirection.SAME) {
                return getEdgeTarget(edge);
            } else {
                return getEdgeSource(edge);
            }
        }

        /**
         * Gets the edge.
         * 
         * @return this 's corresponding edge.
         */
        public Edge getEdge() {
            return edge;
        }

        /**
         * Gets the direction.
         * 
         * @return this .
         */
        public DartDirection getDirection() {
            return direction;
        }
    }

    /**
     * <p>
     * Each face in this graph is represented by the cyclic list of dart encountered when walking
     * around the face in clockwise order.
     * <p>
     * By definition, the first face is the outer one.
     * <p>
     * Reference: Section 2.1.4
     * 
     * @author Gabriel
     */
    public class Face {
        /**
         * The list of darts encountered when walking around the face in clockwise order.
         */
        private Dart[] darts;

        /**
         * Creates a new instance of <tt>Face</tt>.
         * 
         * @param darts
         *            Specifies the darts defining the face.
         */
        public Face(Dart[] darts) {
            if (darts == null) {
                throw new NullPointerException("darts");
            }
            if (darts.length == 0) {
                throw new RuntimeException("The dart array must not be empty.");
            }
            this.darts = darts;
        }

        /**
         * Gets the darts.
         * 
         * @return the darts
         */
        public Dart[] getDarts() {
            return darts;
        }

        /**
         * Gets the dart.
         * 
         * @param i
         *            the i
         * @return the dart
         */
        public Dart getDart(int i) {
            return darts[i];
        }

        /**
         * Gets the dart count.
         * 
         * @return the dart count
         */
        public int getDartCount() {
            return darts.length;
        }
    }

    /**
     * <p>
     * Darts in this graph.
     */
    private List<Dart> darts;

    /**
     * <p>
     * Faces in this graph.
     * <p>
     * By definition, the first face should by the outer face.
     */
    private List<Face> faces;

    /**
     * Instantiates a new mixed upward planarization.
     */
    public MixedUpwardPlanarization() {
        darts = new ArrayList<Dart>();
        faces = new ArrayList<Face>();
    }

    /*
     * TODO Coder les algos UEI et DEI (ici ou dans l'objet Planarization.
     */

    /**
     * <p>
     * Delegate the actual edge creation/insertion to the super class. If the new edge is
     * successfully created, the two corresponding darts are created and added to the list.
     * 
     * @param source
     *            the source
     * @param target
     *            the target
     * @return the edge
     */
    @Override
    public Edge addEdge(Vertex source, Vertex target) {
        Edge edge = super.addEdge(source, target);
        if (edge != null) {
            darts.add(new Dart(edge, DartDirection.SAME));
            darts.add(new Dart(edge, DartDirection.OPPOSITE));
        }
        return edge;
    }

    /**
     * <p>
     * Delegates the actual edge insertion to the super class. If the new edge is successfully
     * inserted, the two corresponding darts are created and added to the list.
     * 
     * @param source
     *            the source
     * @param target
     *            the target
     * @param edge
     *            the edge
     * @return true, if successful
     */
    @Override
    public boolean addEdge(Vertex source, Vertex target, Edge edge) {
        if (super.addEdge(source, target, edge)) {
            darts.add(new Dart(edge, DartDirection.SAME));
            darts.add(new Dart(edge, DartDirection.OPPOSITE));
            return true;
        }
        return false;
    }

    /**
     * Delegates the actual edge removal to the super class. If the edge is successfully removed,
     * the corresponding darts are removed from the list.
     * 
     * @param edge
     *            the edge
     * @return true, if successful
     */
    @Override
    public boolean removeEdge(Edge edge) {
        if (super.removeEdge(edge)) {
            removeDarts(edge);
            return true;
        }
        return false;
    }

    /**
     * Delegates the actual edge removal to the super class. It the edge is successfully removed,
     * the corresponding darts are removed from the list.
     * 
     * @param source
     *            the source
     * @param target
     *            the target
     * @return the edge
     */
    @Override
    public Edge removeEdge(Vertex source, Vertex target) {
        Edge edge = super.removeEdge(source, target);
        if (edge != null) {
            removeDarts(edge);
        }
        return edge;
    }

    /**
     * Remove this <tt>darts</tt> list entry that correspond to the specified edge.
     * 
     * @param edge
     *            An edge (that, typically, is not part of this graph anymore).
     */
    private void removeDarts(Edge edge) {
        List<Dart> newList = new ArrayList<Dart>();

        for (Dart dart : darts) {
            if (!dart.getEdge().equals(edge)) {
                newList.add(dart);
            }
        }
        darts = newList;
    }

    /**
     * Gets the darts.
     * 
     * @return a copy if this graph's dart list. The list is not backed by the graph.
     */
    public List<Dart> getDarts() {
        return new ArrayList<Dart>(darts);
    }

    /**
     * Gets the faces.
     * 
     * @return a copy of this graph's face list. The list is not backed by the graph.
     */
    public List<Face> getFaces() {
        return new ArrayList<Face>(faces);
    }
}
