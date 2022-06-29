package org.matsim.Input_prep;

import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.contrib.osm.networkReader.LinkProperties;
import org.matsim.contrib.osm.networkReader.SupersonicOsmNetworkReader;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;

public class Network {
    private static final String inputFile = "D:\\PhD Research\\Dhaka_scenario_MATSIM\\Dhaka\\Network_data_prep\\bangladesh-latest.osm.pbf";
    private static final String outputFile = "D:\\PhD Research\\Dhaka_scenario_MATSIM\\Dhaka\\Dhaka_input\\bangladesh-latest-network.xml.gz";
    private static final CoordinateTransformation coordinateTransformation = TransformationFactory.getCoordinateTransformation(TransformationFactory.WGS84, "EPSG:32646");

    public static void main(String[] args) {

        org.matsim.api.core.v01.network.Network network = (org.matsim.api.core.v01.network.Network) new SupersonicOsmNetworkReader.Builder()
                .setCoordinateTransformation(coordinateTransformation)
                .addOverridingLinkProperties("residential", new LinkProperties(9, 1, 30.0 / 3.6, 1500, false))
                .build()
                .read(inputFile);

        new NetworkWriter(network).write(outputFile);
    }
}

