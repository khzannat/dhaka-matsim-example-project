package org.matsim.Input_prep;

import org.locationtech.jts.geom.Geometry;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.gis.ShapeFileReader;

import java.io.*;

public class ExtractLinkByShape {
    private static final String shapeFile = "E:\\Abm_output\\Road_pricing\\Mapping\\TAZ_121.shp";
    private static final String networkPath = "E:\\Abm_output\\Base_model\\dhaka-network.xml.gz";

    public static void main(String []data) throws IOException  {
        //return Stream.of(data)
        //.map(this::escapeSpecialCharacters)
        //       .collect(Collectors.joining(","));

        var features = ShapeFileReader.getAllFeatures(shapeFile);
        var network = NetworkUtils.readNetwork(networkPath);

        var geometry = features.stream()
                .map(feature -> (Geometry)feature.getDefaultGeometry())
                .findFirst()
                .orElseThrow();
        var filteredNetwork = network.getLinks().values().stream()
                .filter(link -> {
                    var coord = link.getCoord();
                    return geometry.covers(MGC.coord2Point(coord));

                })
                .collect(NetworkUtils.getCollector());
        saveRecord(filteredNetwork);


    }
    public static void saveRecord(Network filteredNetwork){
        File csvOutputFile= new File("E:\\link_121.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)){
            filteredNetwork.getLinks().values()
                    //.map(this::ExtractLinkByShape)
                    .forEach(pw::println);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    public String escapeSpecialCharacters(String data){
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")||data.contains("[]")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

}

