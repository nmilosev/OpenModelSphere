package org.modelsphere.plugins.layout.cluster.rectanglepacker;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

import javax.swing.JComponent;

import org.modelsphere.jack.srtool.features.layout.ClustersLayoutAlgorithm;
import org.modelsphere.jack.srtool.features.layout.LayoutAttribute;
import org.modelsphere.jack.srtool.features.layout.LayoutAttributes;
import org.modelsphere.jack.srtool.features.layout.graph.Graph;
import org.modelsphere.jack.srtool.features.layout.graph.Node;

public class RectanglePackerAlgorithm implements ClustersLayoutAlgorithm {
    private static final int CLUSTERS_GAP = 10;
    private static final int NODES_GAP = 40;

    private Dimension2D dimension;
    private int clustersCount;
    private boolean randomCluster;
    private double wIncrement;
    private double hIncrement;
    private HashMap<Node, Integer> clusters;

    @Override
    public JComponent createOptionComponent() {
        return null;
    }

    @Override
    public String getText() {
        return "Rectangle Packer";
    }

    @Override
    public void layoutClusters(Graph graph, HashMap<Node, Integer> clusters,
            HashMap<LayoutAttribute<?>, Object> attributes) {
        this.dimension = LayoutAttributes.CANVAS_DIMENSION.getValue(attributes);
        this.clusters = clusters;
        this.clustersCount = clusters.size();
        this.randomCluster = LayoutAttributes.RANDOMIZE.getValue(attributes);
        this.wIncrement = LayoutAttributes.CANVAS_HORIZONTAL_RESIZE_INCREMENT.getValue(attributes);
        this.hIncrement = LayoutAttributes.CANVAS_VERTICAL_RESIZE_INCREMENT.getValue(attributes);
        layout();
    }

    private void layout() {
        ArrayList<Integer> randomClusters = new ArrayList<Integer>();
        for (int i = 0; i < clustersCount; i++) {
            randomClusters.add(i + 1);
        }
        if (randomCluster)
            Collections.shuffle(randomClusters);

        int nextGrow = 0;
        if (wIncrement > 0)
            nextGrow = 1;
        else if (hIncrement > 0)
            nextGrow = -1;

        boolean ok = false;
        int iterations = 1;
        while (!(ok = layoutClusters(randomClusters))) {
            // if no other way to adjust the layout to fit, stop
            if (nextGrow == 0) {
                break;
            }
            // grow canvas' size (number of pages)
            if (!ok) {
                if (nextGrow > 0) { // expand width
                    dimension.setSize(dimension.getWidth() + wIncrement, dimension.getHeight());
                    if (hIncrement > 0)
                        nextGrow = -nextGrow;
                } else {
                    dimension.setSize(dimension.getWidth(), dimension.getHeight() + hIncrement);
                    if (wIncrement > 0)
                        nextGrow = -nextGrow;
                }
            }
            iterations++;
        }
    }

    private boolean layoutClusters(List<Integer> clusters) {
        RectanglePacker packer = new RectanglePacker((int) dimension.getWidth() - NODES_GAP * 2,
                (int) dimension.getHeight() - NODES_GAP * 2);
        HashMap<Integer, Point> translations = new HashMap<Integer, Point>();
        for (int i = 1; i < clustersCount + 1; i++) {
            int cluster = clusters.get(i - 1);

            Polygon polygon = getPolygon(cluster);
            Rectangle rect = polygon.getBounds();
            rect.grow(CLUSTERS_GAP, CLUSTERS_GAP);

            Point loc = packer.findCoords(rect.width, rect.height);

            if (loc == null) {
                //              loc.translate(rect.width / 2, rect.height / 2);
                //          } else {
                // if grow allowed, return false to try again with 
                if (hIncrement > 0 || wIncrement > 0) {
                    return false;
                } else {
                    // TODO - not enough spaces available in the packer, should we also try to compress clusters? 
                    loc = new Point(0, 0);
                }
            }
            translations.put(cluster, loc);
        }

        for (int i = 1; i < clustersCount + 1; i++) {
            Point loc = translations.get(i);
            if (loc != null) {
                translateCluster(i, loc.x + NODES_GAP, loc.y + NODES_GAP);
            }
        }
        return true;
    }

    private List<Node> getClusterNodes(int cluster) {
        List<Node> clusterNodes = new ArrayList<Node>();
        for (Node node : clusters.keySet()) {
            Integer c = clusters.get(node);
            if (c == cluster) {
                clusterNodes.add(node);
            }
        }
        return clusterNodes;
    }

    private Polygon getPolygon(int cluster) {
        Polygon polygon = new Polygon();
        polygon.getBounds();
        List<Node> clusterNodes = getClusterNodes(cluster);
        for (Node node : clusterNodes) {
            Rectangle2D rect = node.getBounds();
            if (polygon.contains(rect))
                continue;
            int x = (int) rect.getX();
            int y = (int) rect.getY();
            int w = (int) rect.getWidth();
            int h = (int) rect.getHeight();
            polygon.addPoint(x, y);
            polygon.addPoint(x + w, y);
            polygon.addPoint(x, y + h);
            polygon.addPoint(x + w, y + h);
        }

        return polygon;
    }

    private void translateCluster(int cluster, int dx, int dy) {
        List<Node> clusterNodes = getClusterNodes(cluster);
        for (Node node : clusterNodes) {
            node.translate(dx, dy);
        }
    }

}
