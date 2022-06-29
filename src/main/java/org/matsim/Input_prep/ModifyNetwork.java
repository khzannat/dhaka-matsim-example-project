package org.matsim.Input_prep;

import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;

import java.util.HashSet;
import java.util.Set;

public class ModifyNetwork {
    public static void main(String[] args) {
        Network network = NetworkUtils.createNetwork();
        new MatsimNetworkReader(network).readFile("scenarios/Dhaka_input1/Input_with_walk_bike_moto_5pc/dhaka-network.xml.gz");

        for (Link link : network.getLinks().values()) {
            if (link.getAllowedModes().contains(TransportMode.car)) {
                // It would be better to set the modes directly in the input network file if not all car roads are open to rickshaw, cng, bus, hh etc.
                Set allowedModes1 = new HashSet(link.getAllowedModes());
                allowedModes1.add("cng");
                allowedModes1.add("motorbike");

                link.setAllowedModes(allowedModes1);
            }
            if (link.getFreespeed() >= 10) {
                Set allowedModes2 = new HashSet(link.getAllowedModes());
                allowedModes2.add("hh");
                allowedModes2.add("bus");
                link.setAllowedModes(allowedModes2);
            }
            if (link.getFreespeed() < 10) {
                Set allowedModes3 = new HashSet(link.getAllowedModes());
                allowedModes3.add("rickshaw");
                link.setAllowedModes(allowedModes3);
            }
        }
        new NetworkWriter(network).write("scenarios/Dhaka_input1/Input_with_walk_bike_moto_5pc/modified-network.xml.gz");


    }
}

