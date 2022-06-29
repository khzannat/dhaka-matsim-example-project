package org.matsim.Input_prep;

import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.contrib.osm.networkReader.LinkProperties;
import org.matsim.contrib.osm.networkReader.OsmTags;
import org.matsim.contrib.osm.networkReader.SupersonicOsmNetworkReader;
import org.matsim.core.network.algorithms.NetworkCleaner;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.utils.gis.shp2matsim.ShpGeometryUtils;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CropNetworkWithShapefile {
    private static Path input = Paths.get("D:\\PhD Research\\Dhaka_scenario_MATSIM\\Dhaka\\Network_data_prep\\bangladesh-latest.osm.pbf");
    private static Path filterShape = Paths.get("D:\\PhD Research\\Dhaka_scenario_MATSIM\\Input_data_preparation\\Final Data\\Location\\Shapefile\\DCC_thana_UTM.shp");
    public static void main (String[] args) throws MalformedURLException{
        new CropNetworkWithShapefile().create();
    }

    private void create() throws MalformedURLException{
        CoordinateTransformation transformation = TransformationFactory.getCoordinateTransformation(TransformationFactory.WGS84, "EPSG:32646");
        List<PreparedGeometry> filterGeometries = ShpGeometryUtils.loadPreparedGeometries(filterShape.toUri().toURL());
        SupersonicOsmNetworkReader reader = new SupersonicOsmNetworkReader.Builder()
                .setCoordinateTransformation(transformation)
                .setIncludeLinkAtCoordWithHierarchy((coord, hierarchyLevel) -> {

                    if (hierarchyLevel <= LinkProperties.LEVEL_PRIMARY) return true;
                    return ShpGeometryUtils.isCoordInPreparedGeometries(coord, filterGeometries);
                })
                .setAfterLinkCreated((link, osmTags, direction) -> {

                    if (osmTags.containsKey(OsmTags.CYCLEWAY)) {
                        Set<String> modes = new HashSet<>(link.getAllowedModes());
                        modes.add(TransportMode.bike);
                        link.setAllowedModes(modes);
                    }
                })
                .build();

        Network network = reader.read(input.toString());
        new NetworkCleaner().run(network);
        new NetworkWriter(network).write("D:\\PhD Research\\Dhaka_scenario_MATSIM\\Dhaka\\Network_data_prep\\dhaka-network.xml.gz");
    }

}
