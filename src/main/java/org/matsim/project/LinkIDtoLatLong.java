package org.matsim.project;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.network.NetworkUtils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.HashMap;

public class LinkIDtoLatLong {

    public static void main(String[] args){
        String path = "E:\\Abm_output\\Synthetic_data\\Ground_truth\\Ground_truth_spreadsheet.csv";
        var root = Paths.get("D:\\PhD Research\\dhaka-matsim-example-project\\scenarios\\Input");
        var network = NetworkUtils.readNetwork(root.resolve("dhaka-network.xml.gz").toString());

        String line = "";
        String cvsSplitBy = ",";
        HashMap<String, Double[]> linkCoords = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            // Read the first line, which contains the headers
            //line = br.readLine();

            while ((line = br.readLine()) != null) {
                // Use comma as separator
                String s = (String)line;
                String[] link = s.split(cvsSplitBy);

                // Get the link ID
                String linkId = link[3];
                //linkId= linkId.strip();
                Link link1 = network.getLinks().get(linkId);
                System.out.println(link1);

                // Get the latitude and longitude from the network file
                if (link1 != null) {
                    Coord coord = link1.getFromNode().getCoord();
                    Double[] coords = new Double[]{coord.getX(), coord.getY()};
                    System.out.println(coords);

                    // Save the lat-long in the HashMap
                    linkCoords.put(linkId, coords);
                } else {
                    System.out.println("Link not found: " + linkId);
                }

            }

            // Write the link ID and lat-long to the same CSV file
            writeToCSV(linkCoords);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeToCSV(HashMap<String, Double[]> linkCoords) {
        try (FileWriter writer = new FileWriter("linkids_latlong.csv")) {
            writer.append("linkId,latitude,longitude");
            writer.append('\n');

            for (String linkId : linkCoords.keySet()) {
                Double[] coords = linkCoords.get(linkId);
                writer.append(linkId + "," + coords[0] + "," + coords[1]);
                writer.append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
