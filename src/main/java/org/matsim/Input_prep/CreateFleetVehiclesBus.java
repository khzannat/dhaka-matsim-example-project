package org.matsim.Input_prep;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.DvrpVehicleSpecification;
import org.matsim.contrib.dvrp.fleet.FleetWriter;
import org.matsim.contrib.dvrp.fleet.ImmutableDvrpVehicleSpecification;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

public class CreateFleetVehiclesBus {
    private static final int numberOfVehiclesBus = 1500;
    private static final int seatsPerVehicleBus = 30; //this is important for DRT, value is not used by taxi
    private static final double operationStartTime = 5 * 60 * 60;
    private static final double operationEndTime = 24 * 60 * 60;
    private static final Random random = MatsimRandom.getRandom();
    private static final int numberOfVehicleHh = 750;
    private static final int seatPervehicleHh = 8;

    private static final Path networkFile = Paths.get("D:\\PhD Research\\Dhaka_scenario_MATSIM\\Dhaka\\scenarios\\Dhaka_input\\dhaka-network.xml.gz");
    private static final Path outputFile = Paths.get("fleetVehicles.xml");


    public static void main(String[] args) {

        new CreateFleetVehiclesBus().run();
    }

    private void run() {

        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());

        new MatsimNetworkReader(scenario.getNetwork()).readFile(networkFile.toString());
        final int[] i = {0};

        Stream<DvrpVehicleSpecification> vehicleSpecificationStream = scenario.getNetwork().getLinks().entrySet().stream()
                .filter(entry -> entry.getValue().getAllowedModes().contains(TransportMode.car))
                .filter(entry->entry.getValue().getFreespeed()>=10)// drt can only start on links with Transport mode 'car'
                .sorted((e1, e2) -> (random.nextInt(2) - 1)) // shuffle links
                .limit(numberOfVehiclesBus) // select the first *numberOfVehicles* links
                .map(entry -> ImmutableDvrpVehicleSpecification.newBuilder()
                        .id(Id.create("bus_" + i[0]++, DvrpVehicle.class))
                        .startLinkId(entry.getKey())
                        .capacity(seatsPerVehicleBus)
                        .serviceBeginTime(operationStartTime)
                        .serviceEndTime(operationEndTime)
                        .build());
        Stream<DvrpVehicleSpecification> vehicleSpecificationStream2 = scenario.getNetwork().getLinks().entrySet().stream()
                .filter(entry -> entry.getValue().getAllowedModes().contains(TransportMode.car)) // drt can only start on links with Transport mode 'car'
                .sorted((e1, e2) -> (random.nextInt(2) - 1)) // shuffle links
                .limit(numberOfVehicleHh) // select the first *numberOfVehicles* links
                .map(entry -> ImmutableDvrpVehicleSpecification.newBuilder()
                        .id(Id.create("hh_" + i[0]++, DvrpVehicle.class))
                        .startLinkId(entry.getKey())
                        .capacity(seatPervehicleHh)
                        .serviceBeginTime(operationStartTime)
                        .serviceEndTime(operationEndTime)
                        .build());
        var vehicle =Stream.concat(vehicleSpecificationStream,vehicleSpecificationStream2);
        new FleetWriter(vehicle).write(outputFile.toString());


    }
}

