package org.modelsphere.sms.features.layout;

import java.awt.Dimension;
import java.util.*;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.srtool.features.layout.GraphFactory;
import org.modelsphere.jack.srtool.features.layout.graph.*;
import org.modelsphere.jack.srtool.features.layout.graph.Edge.OrientationConstraint;
import org.modelsphere.jack.srtool.features.layout.graph.Edge.RelationType;
import org.modelsphere.sms.db.*;

/**
 * A factory for creating SMSGraph objects.
 */
public class SMSGraphFactory extends GraphFactory {

    /** The classifiers map. */
    protected HashMap<DbObject, Node> classifiersMap;

    /**
     * Inits the.
     * 
     * @throws DbException
     *             the db exception
     */
    protected void init() throws DbException {
        classifiersMap = new HashMap<DbObject, Node>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.srtool.features.layout.GraphFactory#createGraph(
     * org.modelsphere.jack .baseDb.db.DbObject, org.modelsphere.jack.graphic.Diagram)
     */
    @Override
    public Graph createGraph(DbObject diagramGo, Diagram diagram, List<DbGraphicalObjectI> scope) throws DbException {

        init();

        Graph graph = new Graph(new GraphEdgeFactory());
        
        DbEnumeration components = diagramGo.getComponents().elements();
        while (components.hasMoreElements()) {
            DbObject component = components.nextElement();

            Node node = addNode(graph, component, scope);
            if (node == null) {
                continue;
            }
            classifiersMap.put(component, node);
        }

        components.close();
        components = diagramGo.getComponents().elements();
        while (components.hasMoreElements()) {
            DbObject component = components.nextElement();
            addEdge(graph, component, scope);
        }
        components.close();

        applyDefaultSize(graph, diagram);

        return graph;
    }

    /**
     * Create and add to the graph an edge object representing go.
     * 
     * @param graph
     *            the graph
     * @param go
     *            The graphical object.
     * @return The added edge or null if go is not a connector object or if it is not supported by
     *         this reader.
     * @throws DbException
     *             the db exception
     */
    protected Edge addEdge(Graph graph, DbObject go, List<DbGraphicalObjectI> scope) throws DbException {
        Edge edge = null;
        DbObject source = null;
        DbObject dest = null;

        if (go instanceof DbSMSAssociationGo) {
            DbSMSAssociationGo associationGo = (DbSMSAssociationGo) go;

            edge = createEdge(associationGo);

            source = associationGo.getFrontEndGo();
            dest = associationGo.getBackEndGo();
        } else if (go instanceof DbSMSInheritanceGo) {
            DbSMSInheritanceGo inheritanceGo = (DbSMSInheritanceGo) go;

            edge = createEdge(inheritanceGo);

            source = inheritanceGo.getBackEndGo();
            dest = inheritanceGo.getFrontEndGo();
        }

        // If go is supported, add the node.
        if (edge != null) {
            Node sourceNode = classifiersMap.get(source);
            Node destNode = classifiersMap.get(dest);
            
            // In some cases (bug ??), we can have a link between 2 graphical
            // objects
            // in a diagram without have a graphical object in diagramGO.
            // The graphics engine auto create the peer component for the
            // missing node.
            // Use the same behaviour here and try adding missing nodes.
            if (sourceNode == null) {
                sourceNode = addNode(graph, source, scope);
            }
            if (destNode == null) {
                destNode = addNode(graph, dest, scope);
            }

            // If we have both nodes set, add the edge
            if (sourceNode != null && destNode != null) {
                graph.addEdge(sourceNode, destNode, edge);
            } else {
                edge = null;
            }
        }
        return edge;
    }

    /**
     * Create and add to the graph a node object representing go.
     * 
     * @param graph
     *            the graph
     * @param go
     *            The graphical object.
     * @return The added node or null if go is not a box object or if it is not supported by this
     *         reader.
     * @throws DbException
     *             the db exception
     */
    protected Node addNode(Graph graph, DbObject go, List<DbGraphicalObjectI> scope) throws DbException {
        if (scope != null && !scope.contains(go)){
            return null;
        }
        
        Node node = null;
        if (go instanceof DbSMSClassifierGo) {
            node = createNode((DbSMSClassifierGo) go);
        } else if (go instanceof DbSMSPackageGo) {
            node = createNode((DbSMSPackageGo) go);
        }

        // If go is supported, add the node.
        if (node != null) {
            graph.addVertex(node);
        }
        return node;
    }

    /**
     * Apply default size.
     * 
     * @param graph
     *            the graph
     * @param diagram
     *            the diagram
     * @throws DbException
     *             the db exception
     */
    @SuppressWarnings("unchecked")
    protected void applyDefaultSize(Graph graph, Diagram diagram) throws DbException {
        // Apply default width and height. Use preferred size of the peer
        // component when auto
        // fit is active since the stored size is unreliable. Graphical objects'
        // size is only
        // set when turning off the auto fit. When turning on the auto fit, the
        // size is not set
        // to the fit size.
        Enumeration<GraphicComponent> gccomponents = diagram.components();
        while (gccomponents.hasMoreElements()) {
            GraphicComponent gc = gccomponents.nextElement();
            int fitmode = gc.getAutoFitMode();
            if (fitmode == GraphicComponent.NO_FIT) {
                continue;
            }
            if (!(gc instanceof ActionInformation)) {
                continue;
            }
            DbObject go = ((ActionInformation) gc).getGraphicalObject();
            Node node = graph.getNodeForGraphicalObject(go);
            if (node == null) {
                continue;
            }
            Dimension preferredSize = gc.getPreferredSize();
            if (preferredSize == null) {
                continue;
            }
            node.setFitMode(fitmode);
            if (fitmode == GraphicComponent.HEIGHT_FIT || fitmode == GraphicComponent.TOTAL_FIT) {
                node.setHeight(preferredSize.height);
            }
            if (fitmode == GraphicComponent.TOTAL_FIT) {
                node.setWidth(preferredSize.width);
            }
        }

    }

    /**
     * Creates a new SMSGraph object.
     * 
     * @param go
     *            the go
     * @return the edge
     * @throws DbException
     *             the db exception
     */
    protected Edge createEdge(DbSMSAssociationGo go) throws DbException {
        Edge edge = new Edge(OrientationConstraint.Free);
        edge.setRelationType(RelationType.Association);
        edge.setRightAngled(go.isRightAngle());
        edge.setGo(go, go.getAssociation());
        return edge;
    }

    /**
     * Creates a new SMSGraph object.
     * 
     * @param go
     *            The inheritance graphical object.
     * @return A new Edge that represents the inheritance relation in which the edge goes out of the
     *         parent into the child.
     * @throws DbException
     *             the db exception
     */
    protected Edge createEdge(DbSMSInheritanceGo go) throws DbException {
        Edge edge = new Edge(OrientationConstraint.Upward);
        edge.setRelationType(RelationType.Generalization);
        edge.setRightAngled(go.isRightAngle());
        edge.setGo(go, go.getInheritance());
        edge.setDiagramReversed(true);
        return edge;
    }

    /**
     * Creates a new SMSGraph object.
     * 
     * @param go
     *            the go
     * @return the node
     * @throws DbException
     *             the db exception
     */
    protected Node createNode(DbSMSClassifierGo go) throws DbException {
        Node node = new Node();
        node.setGO(go, go.getClassifier());
        return node;
    }

    /**
     * Creates a new SMSGraph object.
     * 
     * @param go
     *            the go
     * @return the node
     * @throws DbException
     *             the db exception
     */
    protected Node createNode(DbSMSPackageGo go) throws DbException {
        Node node = new Node();
        node.setGO(go, go.getPackage());
        return node;
    }
}
