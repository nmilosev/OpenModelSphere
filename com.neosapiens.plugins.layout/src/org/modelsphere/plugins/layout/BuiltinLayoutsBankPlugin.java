package org.modelsphere.plugins.layout;

import java.util.ArrayList;
import java.util.List;

import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.features.layout.LayoutAlgorithm;
import org.modelsphere.jack.srtool.features.layout.LayoutPlugin;
import org.modelsphere.plugins.layout.clusters.NullClusterLayoutAlgorithm;
import org.modelsphere.plugins.layout.clusters.arevalo.ArevaloPackingAlgorithm;
import org.modelsphere.plugins.layout.nodes.layerers.CoffmanGrahamLayerer;
import org.modelsphere.plugins.layout.nodes.layerers.SinkConsumerLayerer;
import org.modelsphere.plugins.layout.nodes.sugiyama.*;

/**
 * The Class BuiltinLayoutsBankPlugin.
 */
public class BuiltinLayoutsBankPlugin extends LayoutPlugin implements Plugin2 {

    // Clusters layout algorithms
    /** The null cluster layout algorithm. */
    private NullClusterLayoutAlgorithm nullClusterLayoutAlgorithm;

    /** The arevalo packing algorithm. */
    private ArevaloPackingAlgorithm arevaloPackingAlgorithm;

    // Nodes layout algorithms
    /** The sugiyama tall. */
    private SugiyamaLayoutAlgorithm sugiyamaTall;

    /** The sugiyama with Coffman-Graham layering. */
    private SugiyamaLayoutAlgorithm sugiyamaCoffmanGraham;

    /**
     * Instantiates a new builtin layouts bank plugin.
     */
    public BuiltinLayoutsBankPlugin() {
        nullClusterLayoutAlgorithm = new NullClusterLayoutAlgorithm();
        arevaloPackingAlgorithm = new ArevaloPackingAlgorithm();

        sugiyamaTall = new SugiyamaLayoutAlgorithm(new DefaultCycleRemover(),
                new SinkConsumerLayerer(), new QuickSmartNodeOrderer(),
                new DefaultCoordinateAssigner());
        sugiyamaTall.setText("Sugiyama: Tall");

        sugiyamaCoffmanGraham = new SugiyamaLayoutAlgorithm(new DefaultCycleRemover(),
                new CoffmanGrahamLayerer(), new QuickSmartNodeOrderer(),
                new DefaultCoordinateAssigner());
        sugiyamaCoffmanGraham.setText("Sugiyama with Coffman/Graham layering");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.srtool.features.layout.LayoutPlugin#getLayoutAlgorithms ()
     */
    @Override
    public List<LayoutAlgorithm> getLayoutAlgorithms() {
        List<LayoutAlgorithm> algorithms = new ArrayList<LayoutAlgorithm>();

        algorithms.add(nullClusterLayoutAlgorithm);
        algorithms.add(arevaloPackingAlgorithm);

        algorithms.add(sugiyamaTall);
        algorithms.add(sugiyamaCoffmanGraham);

        return algorithms;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.modelsphere.jack.srtool.features.layout.LayoutPlugin# getDefaultLayoutAlgorithms()
     */
    @Override
    public List<LayoutAlgorithm> getDefaultLayoutAlgorithms() {
        // TODO : Default layout algorithms should not be hard coded like this.
        List<LayoutAlgorithm> defaults = new ArrayList<LayoutAlgorithm>();

        defaults.add(arevaloPackingAlgorithm);
        defaults.add(sugiyamaCoffmanGraham);

        return defaults;
    }

	@Override
	public OptionGroup getOptionGroup() {
		return null;
	}

	@Override
	public PluginAction getPluginAction() {
		return null;
	}
}
