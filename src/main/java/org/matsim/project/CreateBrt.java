package org.matsim.project;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.NetworkFactory;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.population.routes.LinkNetworkRouteFactory;
import org.matsim.core.population.routes.RouteUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.pt.transitSchedule.api.*;
import org.matsim.vehicles.MatsimVehicleWriter;
import org.matsim.vehicles.VehicleType;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateBrt {
    private static final LinkNetworkRouteFactory routeFactory=new LinkNetworkRouteFactory();
    private static final NetworkFactory networkFactory = NetworkUtils.createNetwork().getFactory();
    private static TransitScheduleFactory scheduleFactory= ScenarioUtils.createScenario(ConfigUtils.createConfig()).getTransitSchedule().getFactory();
    public static void main(String[] args){
        var root = Paths.get("scenarios/Dhaka_input");
        var network = NetworkUtils.readNetwork(root.resolve("dhaka-network.xml.gz").toString());
        var scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        var vehicleType = scenario.getVehicles().getFactory().createVehicleType(Id.create("brt", VehicleType.class));
        vehicleType.setLength(18);//is it in meter?
        vehicleType.setPcuEquivalents(3);
        vehicleType.setMaximumVelocity(25);
        vehicleType.setNetworkMode(TransportMode.pt);//Would that be PT?
        vehicleType.setDescription("brt");
        vehicleType.getCapacity().setSeats(140);
        vehicleType.getCapacity().setStandingRoom(0);
        scenario.getTransitVehicles().addVehicleType(vehicleType);


        var startNode = network.getFactory().createNode(Id.createNodeId("pt_start"), new Coord(237243.491728,2656248.34032));
        network.addNode(startNode);
        var stop1 = network.getFactory().createNode(Id.createNodeId("pt_1"), new Coord(236390.870231 , 2655944.06888));
        network.addNode(stop1);
        var stop2 = network.getFactory().createNode(Id.createNodeId("pt_2"), new Coord(235369.576522,  2655742.98514 ));
        network.addNode(stop2);
        var stop3 = network.getFactory().createNode(Id.createNodeId("pt_3"),new Coord(234273.052801, 2655627.09741));
        network.addNode(stop3);
        var stop4 = network.getFactory().createNode(Id.createNodeId("pt_4"), new Coord(233718.045022, 2655533.2937));
        network.addNode(stop4);
        var stop5 = network.getFactory().createNode(Id.createNodeId("pt_5"), new Coord(233614.658878 , 2655494.79675));
        network.addNode(stop5);
        var stop6 = network.getFactory().createNode(Id.createNodeId("pt_6"), new Coord(233481.484519 , 2654220.6497));
        network.addNode(stop6);
        var stop7 = network.getFactory().createNode(Id.createNodeId("pt_7"), new Coord(233446.32397 , 2653844.80921));
        network.addNode(stop7);
        var stop8 = network.getFactory().createNode(Id.createNodeId("pt_8"), new Coord(233395.944891, 2653001.08939));
        network.addNode(stop8);
        var stop9 = network.getFactory().createNode(Id.createNodeId("pt_9"), new Coord(233447.346159, 2652270.24679));
        network.addNode(stop9);
        var stop10 = network.getFactory().createNode(Id.createNodeId("pt_10"), new Coord(233473.573034, 2651851.04673));
        network.addNode(stop10);
        var stop11 = network.getFactory().createNode(Id.createNodeId("pt_11"), new Coord(233486.40169 , 2651121.8866));
        network.addNode(stop11);
        var stop12 = network.getFactory().createNode(Id.createNodeId("pt_12"), new Coord(233613.328435 , 2650631.14976));
        network.addNode(stop12);
        var stop13 = network.getFactory().createNode(Id.createNodeId("pt_13"), new Coord(233816.558607, 2650017.64593));
        network.addNode(stop13);
        var stop14 = network.getFactory().createNode(Id.createNodeId("pt_14"), new Coord(234068.133763 , 2649325.82968));
        network.addNode(stop14);
        var stop15 = network.getFactory().createNode(Id.createNodeId("pt_15"), new Coord(234256.230841 , 2648798.69705));
        network.addNode(stop15);
        var stop16 = network.getFactory().createNode(Id.createNodeId("pt_16"), new Coord(234568.562389 , 2647915.69295));
        network.addNode(stop16);
        var stop17 = network.getFactory().createNode(Id.createNodeId("pt_17"), new Coord(234886.607626 , 2647089.45707));
        network.addNode(stop17);
        var stop18 = network.getFactory().createNode(Id.createNodeId("pt_18"), new Coord(235193.312738 , 2646203.59476));
        network.addNode(stop18);
        var stop19 = network.getFactory().createNode(Id.createNodeId("pt_19"), new Coord(235182.761021 , 2645240.85877));
        network.addNode(stop19);
        var stop20 = network.getFactory().createNode(Id.createNodeId("pt_20"), new Coord(235470.734452, 2644794.30517));
        network.addNode(stop20);
        var stop21 = network.getFactory().createNode(Id.createNodeId("pt_21"), new Coord(235356.708115,2643446.02615));
        network.addNode(stop21);
        var stop22 = network.getFactory().createNode(Id.createNodeId("pt_22"), new Coord(235220.334357,2641651.6037));
        network.addNode(stop22);
        var stop23 = network.getFactory().createNode(Id.createNodeId("pt_23"), new Coord( 236324.215494,2639723.45178));
        network.addNode(stop23);
        /////line3 Airport to Jheelmil
        var stop24 = network.getFactory().createNode(Id.createNodeId("pt_24"), new Coord(236989.379324,  2638738.63757));
        network.addNode(stop24);
        var stop25 = network.getFactory().createNode(Id.createNodeId("pt_25"), new Coord(237218.145973 , 2637228.88732));
        network.addNode(stop25);
        var stop26 = network.getFactory().createNode(Id.createNodeId("pt_26"), new Coord(237002.436423, 2636819.13809));
        network.addNode(stop26);
        var stop27 = network.getFactory().createNode(Id.createNodeId("pt_27"), new Coord(236453.195883, 2636444.46739));
        network.addNode(stop27);
        var stop28 = network.getFactory().createNode(Id.createNodeId("pt_28"), new Coord(236004.726236 , 2636370.64849));
        network.addNode(stop28);
        var stop29 = network.getFactory().createNode(Id.createNodeId("pt_29"), new Coord(235734.293306 , 2636359.74763));
        network.addNode(stop29);
        var stop30 = network.getFactory().createNode(Id.createNodeId("pt_30"), new Coord(235630.285389, 2636283.36227));
        network.addNode(stop30);
        var stop31 = network.getFactory().createNode(Id.createNodeId("pt_31"), new Coord(235563.477964 , 2636172.50163));
        network.addNode(stop31);
        var stop32 = network.getFactory().createNode(Id.createNodeId("pt_32"), new Coord(235472.385985 , 2635624.34307));
        network.addNode(stop32);
        var stop33 = network.getFactory().createNode(Id.createNodeId("pt_33"), new Coord(235110.54815 , 2633558.76243));
        network.addNode(stop33);
        var stop34 = network.getFactory().createNode(Id.createNodeId("pt_34"), new Coord(234860.57549,2632123.01651));
        network.addNode(stop34);
        var stop35 = network.getFactory().createNode(Id.createNodeId("pt_35"), new Coord(234944.448575,2631714.86945));
        network.addNode(stop35);
        var stop36 = network.getFactory().createNode(Id.createNodeId("pt_36"), new Coord(235145.559871,2631305.44246));
        network.addNode(stop36);
        var stop37 = network.getFactory().createNode(Id.createNodeId("pt_37"), new Coord(235007.28173,2630455.63216));
        network.addNode(stop37);
        var stop38 = network.getFactory().createNode(Id.createNodeId("pt_38"), new Coord(234890.851601,2629733.07348));
        network.addNode(stop38);
        var stop39 = network.getFactory().createNode(Id.createNodeId("pt_39"), new Coord(235103.76151,2629288.03537));
        network.addNode(stop39);
        var stop40 = network.getFactory().createNode(Id.createNodeId("pt_40"), new Coord(235365.123466,2628785.41875));
        network.addNode(stop40);
        var stop41 = network.getFactory().createNode(Id.createNodeId("pt_41"), new Coord(235401.13333,2628430.00325 ));
        network.addNode(stop41);
        var stop42 = network.getFactory().createNode(Id.createNodeId("pt_42"), new Coord(235489.239756,2628324.83116));
        network.addNode(stop42);
        var stop43 = network.getFactory().createNode(Id.createNodeId("pt_43"), new Coord(235555.518014,2628177.06108));
        network.addNode(stop43);
        var stop44 = network.getFactory().createNode(Id.createNodeId("pt_44"), new Coord(235488.251536,2627583.43543));
        network.addNode(stop44);
        var stop45 = network.getFactory().createNode(Id.createNodeId("pt_45"), new Coord(236011.20154,2627534.51387));
        network.addNode(stop45);
        var stop46 = network.getFactory().createNode(Id.createNodeId("pt_46"), new Coord(235971.536009,2626784.7714));
        network.addNode(stop46);
        var stop47 = network.getFactory().createNode(Id.createNodeId("pt_47"), new Coord(236029.638625,2626365.66174));
        network.addNode(stop47);
        var stop48 = network.getFactory().createNode(Id.createNodeId("pt_48"), new Coord(236025.537575,2625943.28007 ));
        network.addNode(stop48);
        var stop49 = network.getFactory().createNode(Id.createNodeId("pt_49"), new Coord(235802.519838,2625365.69349));
        network.addNode(stop49);
        var stop50 = network.getFactory().createNode(Id.createNodeId("pt_50"), new Coord(235790.646637,2625027.20254));
        network.addNode(stop50);
        var stop51 = network.getFactory().createNode(Id.createNodeId("pt_51"), new Coord(235222.454997,2624712.67627));
        network.addNode(stop51);
        var stop52 = network.getFactory().createNode(Id.createNodeId("pt_52"), new Coord(234625.011406,2623610.88953 ));
        network.addNode(stop52);
        var endNode = network.getFactory().createNode(Id.createNodeId("pt_end"), new Coord(235261.871378,2620842.99837));
        network.addNode(endNode);

        /////Node from opposit direction

        var startNodeR = network.getFactory().createNode(Id.createNodeId("pt_startR"), new Coord(235258.209596,2620841.78486));
        network.addNode(startNodeR);
        var stop1R = network.getFactory().createNode(Id.createNodeId("pt_1R"), new Coord(234604.475789 , 2623616.15833));
        network.addNode(stop1R);
        var stop2R = network.getFactory().createNode(Id.createNodeId("pt_2R"), new Coord(235215.675095, 2624713.80201  ));
        network.addNode(stop2R);
        var stop3R = network.getFactory().createNode(Id.createNodeId("pt_3R"),new Coord( 235785.026494, 2625027.4331));
        network.addNode(stop3R);
        var stop4R = network.getFactory().createNode(Id.createNodeId("pt_4R"), new Coord( 235797.779436, 2625365.73003));
        network.addNode(stop4R);
        var stop5R = network.getFactory().createNode(Id.createNodeId("pt_5R"), new Coord(236022.176538 , 2625943.56619));
        network.addNode(stop5R);
        var stop6R = network.getFactory().createNode(Id.createNodeId("pt_6R"), new Coord(236026.595088, 2626365.25995));
        network.addNode(stop6R);
        var stop7R = network.getFactory().createNode(Id.createNodeId("pt_7R"), new Coord( 235968.307263, 2626784.72592));
        network.addNode(stop7R);
        var stop8R = network.getFactory().createNode(Id.createNodeId("pt_8R"), new Coord(236006.351115,2627531.57781 ));
        network.addNode(stop8R);
        var stop9R = network.getFactory().createNode(Id.createNodeId("pt_9R"), new Coord(235479.467631 , 2627581.25784));
        network.addNode(stop9R);
        var stop10R = network.getFactory().createNode(Id.createNodeId("pt_10R"), new Coord(235552.704444 , 2628176.49877));
        network.addNode(stop10R);
        var stop11R = network.getFactory().createNode(Id.createNodeId("pt_11R"), new Coord( 235487.365459,2628322.89302));
        network.addNode(stop11R);
        var stop12R = network.getFactory().createNode(Id.createNodeId("pt_12R"), new Coord( 235397.018338, 2628428.93989));
        network.addNode(stop12R);
        var stop13R = network.getFactory().createNode(Id.createNodeId("pt_13R"), new Coord( 235353.858096,2628795.45799));
        network.addNode(stop13R);
        var stop14R = network.getFactory().createNode(Id.createNodeId("pt_14R"), new Coord(235097.603333,2629286.76465 ));
        network.addNode(stop14R);
        var stop15R = network.getFactory().createNode(Id.createNodeId("pt_15R"), new Coord( 234873.024551, 2629733.09998));
        network.addNode(stop15R);
        var stop16R = network.getFactory().createNode(Id.createNodeId("pt_16R"), new Coord( 235000.289389, 2630478.58265));
        network.addNode(stop16R);
        var stop17R = network.getFactory().createNode(Id.createNodeId("pt_17R"), new Coord( 235139.045986, 2631305.59773));
        network.addNode(stop17R);
        var stop18R = network.getFactory().createNode(Id.createNodeId("pt_18R"), new Coord(234940.211213, 2631713.99615));
        network.addNode(stop18R);
        var stop19R = network.getFactory().createNode(Id.createNodeId("pt_19R"), new Coord(234843.483763, 2632130.61651));
        network.addNode(stop19R);
        var stop20R = network.getFactory().createNode(Id.createNodeId("pt_20R"), new Coord(235084.376345, 2633567.80849));
        network.addNode(stop20R);
        var stop21R = network.getFactory().createNode(Id.createNodeId("pt_21R"), new Coord(235447.517696, 2635634.09821 ));
        network.addNode(stop21R);
        var stop22R = network.getFactory().createNode(Id.createNodeId("pt_22R"), new Coord(235540.598299, 2636185.38515));
        network.addNode(stop22R);
        var stop23R = network.getFactory().createNode(Id.createNodeId("pt_23R"), new Coord( 235616.930744, 2636295.45204 ));
        network.addNode(stop23R);
        /////line3 Airport to Jheelmil
        var stop24R = network.getFactory().createNode(Id.createNodeId("pt_24R"), new Coord( 235731.230972, 2636372.84282));
        network.addNode(stop24R);
        var stop25R = network.getFactory().createNode(Id.createNodeId("pt_25R"), new Coord( 236006.040119, 2636390.27537));
        network.addNode(stop25R);
        var stop26R = network.getFactory().createNode(Id.createNodeId("pt_26R"), new Coord( 236450.871738, 2636475.90132 ));
        network.addNode(stop26R);
        var stop27R = network.getFactory().createNode(Id.createNodeId("pt_27R"), new Coord(236998.824917, 2636859.8125));
        network.addNode(stop27R);
        var stop28R = network.getFactory().createNode(Id.createNodeId("pt_28R"), new Coord(237200.702404, 2637257.3718));
        network.addNode(stop28R);
        var stop29R = network.getFactory().createNode(Id.createNodeId("pt_29R"), new Coord(236970.646736 , 2638732.82371));
        network.addNode(stop29R);
        var stop30R = network.getFactory().createNode(Id.createNodeId("pt_30R"), new Coord(236279.950563, 2639754.49225));
        network.addNode(stop30R);
        var stop31R = network.getFactory().createNode(Id.createNodeId("pt_31R"), new Coord(235203.094242 , 2641658.61411));
        network.addNode(stop31R);
        var stop32R = network.getFactory().createNode(Id.createNodeId("pt_32R"), new Coord( 235340.677851, 2643447.84044));
        network.addNode(stop32R);
        var stop33R = network.getFactory().createNode(Id.createNodeId("pt_33R"), new Coord( 235454.448912, 2644793.24938));
        network.addNode(stop33R);
        var stop34R = network.getFactory().createNode(Id.createNodeId("pt_34R"), new Coord(235177.085649 ,2645239.78756 ));
        network.addNode(stop34R);
        var stop35R = network.getFactory().createNode(Id.createNodeId("pt_35R"), new Coord( 235188.460991, 2646219.67945));
        network.addNode(stop35R);
        var stop36R = network.getFactory().createNode(Id.createNodeId("pt_36R"), new Coord(234879.171275, 2647102.53407 ));
        network.addNode(stop36R);
        var stop37R = network.getFactory().createNode(Id.createNodeId("pt_37R"), new Coord(234563.761734,2647923.22735 ));
        network.addNode(stop37R);
        var stop38R = network.getFactory().createNode(Id.createNodeId("pt_38R"), new Coord( 234252.268476,2648802.53345 ));
        network.addNode(stop38R);
        var stop39R = network.getFactory().createNode(Id.createNodeId("pt_39R"), new Coord( 234059.747997,2649341.2306 ));
        network.addNode(stop39R);
        var stop40R = network.getFactory().createNode(Id.createNodeId("pt_40R"), new Coord(233811.453681 , 2650027.93597));
        network.addNode(stop40R);
        var stop41R = network.getFactory().createNode(Id.createNodeId("pt_41R"), new Coord( 233606.408243, 2650644.55395));
        network.addNode(stop41R);
        var stop42R = network.getFactory().createNode(Id.createNodeId("pt_42R"), new Coord( 233483.911207, 2651126.51463));
        network.addNode(stop42R);
        var stop43R = network.getFactory().createNode(Id.createNodeId("pt_43R"), new Coord(233470.962472 , 2651859.30609));
        network.addNode(stop43R);
        var stop44R = network.getFactory().createNode(Id.createNodeId("pt_44R"), new Coord( 233445.227282, 2652283.45166));
        network.addNode(stop44R);
        var stop45R = network.getFactory().createNode(Id.createNodeId("pt_45R"), new Coord(233394.383083 ,2653009.10598 ));
        network.addNode(stop45R);
        var stop46R = network.getFactory().createNode(Id.createNodeId("pt_46R"), new Coord(233446.352645 , 2653856.73125));
        network.addNode(stop46R);
        var stop47R = network.getFactory().createNode(Id.createNodeId("pt_47R"), new Coord( 233480.37813, 2654233.18117));
        network.addNode(stop47R);
        var stop48R = network.getFactory().createNode(Id.createNodeId("pt_48R"), new Coord( 233607.087342, 2655523.41335));
        network.addNode(stop48R);
        var stop49R = network.getFactory().createNode(Id.createNodeId("pt_49R"), new Coord( 233712.810644, 2655556.22175));
        network.addNode(stop49R);
        var stop50R = network.getFactory().createNode(Id.createNodeId("pt_50R"), new Coord( 234269.362798, 2655678.85637));
        network.addNode(stop50R);
        var stop51R = network.getFactory().createNode(Id.createNodeId("pt_51R"), new Coord(235370.163958 , 2655828.74313));
        network.addNode(stop51R);
        var stop52R = network.getFactory().createNode(Id.createNodeId("pt_52R"), new Coord(236373.730549 ,2656025.41713 ));
        network.addNode(stop52R);
        var endNodeR = network.getFactory().createNode(Id.createNodeId("pt_endR"), new Coord( 237182.916195,2656319.63439 ));
        network.addNode(endNodeR);

        List<Link> connections = new ArrayList<>();
        connections.add(createLink("pt_1L",startNode , stop1));//2
        connections.add(createLink("pt_2L", stop1, stop2));//3
        connections.add(createLink("pt_3L", stop2, stop3));//4
        connections.add(createLink("pt_4L", stop3, stop4));//5
        connections.add(createLink("pt_5L", stop4, stop5));//6
        connections.add(createLink("pt_6L",stop5,stop6));//7
        connections.add(createLink("pt_7L",stop6,stop7));//8
        connections.add(createLink("pt_8L",stop7,stop8));//9
        connections.add(createLink("pt_9L",stop8,stop9));//10
        connections.add(createLink("pt_10L",stop9,stop10));//11
        connections.add(createLink("pt_11L",stop10,stop11));//12
        connections.add(createLink("pt_12L",stop11,stop12));//13
        connections.add(createLink("pt_13L",stop12,stop13));//14
        connections.add(createLink("pt_14L",stop13,stop14));//15
        connections.add(createLink("pt_15L",stop14,stop15));//16
        connections.add(createLink("pt_16L",stop15,stop16));//17
        connections.add(createLink("pt_17L",stop16,stop17));//18
        connections.add(createLink("pt_18L",stop17,stop18));//19
        connections.add(createLink("pt_19L",stop18,stop19));//20
        connections.add(createLink("pt_20L",stop19,stop20));//21
        connections.add(createLink("pt_21L",stop20,stop21));//22
        connections.add(createLink("pt_22L",stop21,stop22));//23
        connections.add(createLink("pt_23L",stop22,stop23));//24
        connections.add(createLink("pt_24L",stop23,stop24));
        connections.add(createLink("pt_25L",stop24,stop25));//25
        connections.add(createLink("pt_26L",stop25,stop26));//26
        connections.add(createLink("pt_27L",stop26,stop27));
        connections.add(createLink("pt_28L",stop27,stop28));//27
        connections.add(createLink("pt_29L",stop28,stop29));
        connections.add(createLink("pt_30L",stop29,stop30));
        connections.add(createLink("pt_31L",stop30,stop31));
        connections.add(createLink("pt_32L",stop31,stop32));//28
        connections.add(createLink("pt_33L",stop32,stop33));//29
        connections.add(createLink("pt_34L",stop33,stop34));
        connections.add(createLink("pt_35L",stop34,stop35));//30
        connections.add(createLink("pt_36L",stop35, stop36));
        connections.add(createLink("pt_37L",stop36, stop37));
        connections.add(createLink("pt_38L",stop37,stop38));//31
        connections.add(createLink("pt_39L",stop38,stop39));//32
        connections.add(createLink("pt_40L",stop39,stop40));//33
        connections.add(createLink("pt_41L",stop40,stop41));
        connections.add(createLink("pt_42L",stop41,stop42));
        connections.add(createLink("pt_43L",stop42,stop43));//34
        connections.add(createLink("pt_44L",stop43,stop44));
        connections.add(createLink("pt_45L",stop44,stop45));//35
        connections.add(createLink("pt_46L",stop45,stop46));
        connections.add(createLink("pt_47L",stop46,stop47));
        connections.add(createLink("pt_48L",stop47,stop48));//36
        connections.add(createLink("pt_49L",stop48,stop49));
        connections.add(createLink("pt_50L",stop49,stop50));//37
        connections.add(createLink("pt_51L",stop50,stop51));//38
        connections.add(createLink("pt_52L",stop51,stop52));
        connections.add(createLink("pt_53L",stop52,endNode));//39//40


        for(var link:connections){
            network.addLink(link);
        }
        var ids = connections.stream()
                .map(link -> link.getId()).collect(Collectors.toList());
        var networkRoute = RouteUtils.createNetworkRoute( ids);


        var Stop1f = scheduleFactory.createTransitStopFacility(Id.create("Stop_1f", TransitStopFacility.class), stop1.getCoord(), false);
        Stop1f.setLinkId(connections.get(0).getId());
        var Stop2f = scheduleFactory.createTransitStopFacility(Id.create("Stop_2f", TransitStopFacility.class), stop2.getCoord(), false);
        Stop2f.setLinkId(connections.get(1).getId());
        var Stop3f = scheduleFactory.createTransitStopFacility(Id.create("Stop_3f", TransitStopFacility.class), stop3.getCoord(), false);
        Stop3f.setLinkId(connections.get(2).getId());
        var Stop4f = scheduleFactory.createTransitStopFacility(Id.create("Stop_4f", TransitStopFacility.class), stop4.getCoord(), false);
        Stop4f.setLinkId(connections.get(3).getId());
        var Stop5f = scheduleFactory.createTransitStopFacility(Id.create("Stop_5f", TransitStopFacility.class), stop5.getCoord(), false);
        Stop5f.setLinkId(connections.get(4).getId());
        var Stop6f = scheduleFactory.createTransitStopFacility(Id.create("Stop_6f", TransitStopFacility.class), stop6.getCoord(), false);
        Stop6f.setLinkId(connections.get(5).getId());
        var Stop7f = scheduleFactory.createTransitStopFacility(Id.create("Stop_7f", TransitStopFacility.class), stop7.getCoord(), false);
        Stop7f.setLinkId(connections.get(6).getId());
        var Stop8f = scheduleFactory.createTransitStopFacility(Id.create("Stop_8f", TransitStopFacility.class), stop8.getCoord(), false);
        Stop8f.setLinkId(connections.get(7).getId());
        var Stop9f = scheduleFactory.createTransitStopFacility(Id.create("Stop_9f", TransitStopFacility.class), stop9.getCoord(), false);
        Stop9f.setLinkId(connections.get(8).getId());
        var Stop10f = scheduleFactory.createTransitStopFacility(Id.create("Stop_10f", TransitStopFacility.class), stop10.getCoord(), false);
        Stop10f.setLinkId(connections.get(9).getId());
        var Stop11f = scheduleFactory.createTransitStopFacility(Id.create("Stop_11f", TransitStopFacility.class), stop11.getCoord(), false);
        Stop11f.setLinkId(connections.get(10).getId());
        var Stop12f = scheduleFactory.createTransitStopFacility(Id.create("Stop_12f", TransitStopFacility.class), stop12.getCoord(), false);
        Stop12f.setLinkId(connections.get(11).getId());
        var Stop13f = scheduleFactory.createTransitStopFacility(Id.create("Stop_13f", TransitStopFacility.class), stop13.getCoord(), false);
        Stop13f.setLinkId(connections.get(12).getId());
        var Stop14f = scheduleFactory.createTransitStopFacility(Id.create("Stop_14f", TransitStopFacility.class), stop14.getCoord(), false);
        Stop14f.setLinkId(connections.get(13).getId());
        var Stop15f = scheduleFactory.createTransitStopFacility(Id.create("Stop_15f", TransitStopFacility.class), stop15.getCoord(), false);
        Stop15f.setLinkId(connections.get(14).getId());
        var Stop16f = scheduleFactory.createTransitStopFacility(Id.create("Stop_16f", TransitStopFacility.class), stop16.getCoord(), false);
        Stop16f.setLinkId(connections.get(15).getId());
        var Stop17f = scheduleFactory.createTransitStopFacility(Id.create("Stop_17f", TransitStopFacility.class), stop17.getCoord(), false);
        Stop17f.setLinkId(connections.get(16).getId());
        var Stop18f = scheduleFactory.createTransitStopFacility(Id.create("Stop_18f", TransitStopFacility.class), stop18.getCoord(), false);
        Stop18f.setLinkId(connections.get(17).getId());
        var Stop19f = scheduleFactory.createTransitStopFacility(Id.create("Stop_19f", TransitStopFacility.class), stop19.getCoord(), false);
        Stop19f.setLinkId(connections.get(18).getId());
        var Stop20f = scheduleFactory.createTransitStopFacility(Id.create("Stop_20f", TransitStopFacility.class), stop20.getCoord(), false);
        Stop20f.setLinkId(connections.get(19).getId());
        var Stop21f = scheduleFactory.createTransitStopFacility(Id.create("Stop_21f", TransitStopFacility.class), stop21.getCoord(), false);
        Stop21f.setLinkId(connections.get(20).getId());
        var Stop22f = scheduleFactory.createTransitStopFacility(Id.create("Stop_22f", TransitStopFacility.class), stop22.getCoord(), false);
        Stop22f.setLinkId(connections.get(21).getId());
        var Stop23f = scheduleFactory.createTransitStopFacility(Id.create("Stop_23f", TransitStopFacility.class), stop23.getCoord(), false);
        Stop23f.setLinkId(connections.get(22).getId());
        var Stop24f = scheduleFactory.createTransitStopFacility(Id.create("Stop_24f", TransitStopFacility.class), stop25.getCoord(), false);
        Stop24f.setLinkId(connections.get(24).getId());
        var Stop25f = scheduleFactory.createTransitStopFacility(Id.create("Stop_25f", TransitStopFacility.class), stop26.getCoord(), false);
        Stop25f.setLinkId(connections.get(25).getId());
        var Stop26f = scheduleFactory.createTransitStopFacility(Id.create("Stop_26f", TransitStopFacility.class), stop28.getCoord(), false);
        Stop26f.setLinkId(connections.get(27).getId());
        var Stop27f = scheduleFactory.createTransitStopFacility(Id.create("Stop_27f", TransitStopFacility.class), stop32.getCoord(), false);
        Stop27f.setLinkId(connections.get(31).getId());
        var Stop28f = scheduleFactory.createTransitStopFacility(Id.create("Stop_28f", TransitStopFacility.class), stop33.getCoord(), false);
        Stop28f.setLinkId(connections.get(32).getId());
        var Stop29f = scheduleFactory.createTransitStopFacility(Id.create("Stop_29f", TransitStopFacility.class), stop35.getCoord(), false);
        Stop29f.setLinkId(connections.get(34).getId());
        var Stop30f = scheduleFactory.createTransitStopFacility(Id.create("Stop_30f", TransitStopFacility.class), stop38.getCoord(), false);
        Stop30f.setLinkId(connections.get(37).getId());
        var Stop31f = scheduleFactory.createTransitStopFacility(Id.create("Stop_31f", TransitStopFacility.class), stop39.getCoord(), false);
        Stop31f.setLinkId(connections.get(38).getId());
        var Stop32f = scheduleFactory.createTransitStopFacility(Id.create("Stop_32f", TransitStopFacility.class), stop40.getCoord(), false);
        Stop32f.setLinkId(connections.get(39).getId());
        var Stop33f = scheduleFactory.createTransitStopFacility(Id.create("Stop_33f", TransitStopFacility.class), stop43.getCoord(), false);
        Stop33f.setLinkId(connections.get(42).getId());
        var Stop34f = scheduleFactory.createTransitStopFacility(Id.create("Stop_34f", TransitStopFacility.class), stop45.getCoord(), false);
        Stop34f.setLinkId(connections.get(44).getId());
        var Stop35f = scheduleFactory.createTransitStopFacility(Id.create("Stop_35f", TransitStopFacility.class), stop48.getCoord(), false);
        Stop35f.setLinkId(connections.get(47).getId());
        var Stop36f = scheduleFactory.createTransitStopFacility(Id.create("Stop_36f", TransitStopFacility.class), stop50.getCoord(), false);
        Stop36f.setLinkId(connections.get(49).getId());
        var Stop37f = scheduleFactory.createTransitStopFacility(Id.create("Stop_37f", TransitStopFacility.class), stop51.getCoord(), false);
        Stop37f.setLinkId(connections.get(50).getId());
        var Stop38f = scheduleFactory.createTransitStopFacility(Id.create("Stop_38f", TransitStopFacility.class), stop52.getCoord(), false);
        Stop38f.setLinkId(connections.get(51).getId());

        List<Link> connections2 = new ArrayList<>();
        connections2.add(createLink("pt_54Lr", startNodeR, stop1R));//40//39
        connections2.add(createLink("pt_55Lr", stop1R, stop2R));
        connections2.add(createLink("pt_56Lr", stop2R, stop3R));//38
        connections2.add(createLink("pt_57Lr", stop3R, stop4R));
        connections2.add(createLink("pt_58Lr", stop4R, stop5R));//37
        connections2.add(createLink("pt_59Lr",stop5R,stop6R));
        connections2.add(createLink("pt_60Lr",stop6R,stop7R));//36
        connections2.add(createLink("pt_61Lr",stop7R,stop8R));
        connections2.add(createLink("pt_62Lr",stop8R,stop9R));//35
        connections2.add(createLink("pt_63Lr",stop9R,stop10R));//34
        connections2.add(createLink("pt_64Lr",stop10R,stop11R));
        connections2.add(createLink("pt_65Lr",stop11R,stop12R));//33
        connections2.add(createLink("pt_66Lr",stop12R,stop13R));
        connections2.add(createLink("pt_67Lr",stop13R,stop14R));//32
        connections2.add(createLink("pt_68Lr",stop14R,stop15R));
        connections2.add(createLink("pt_69Lr",stop15R,stop16R));//31
        connections2.add(createLink("pt_70Lr",stop16R,stop17R));
        connections2.add(createLink("pt_71Lr",stop17R,stop18R));//30
        connections2.add(createLink("pt_72Lr",stop18R,stop19R));
        connections2.add(createLink("pt_73Lr",stop19R,stop20R));//29
        connections2.add(createLink("pt_74Lr",stop20R,stop21R));
        connections2.add(createLink("pt_75Lr",stop21R,stop22R));//28
        connections2.add(createLink("pt_76Lr",stop22R,stop23R));
        connections2.add(createLink("pt_77Lr",stop23R,stop24R));
        connections2.add(createLink("pt_78Lr",stop24R,stop25R));//27
        connections2.add(createLink("pt_79Lr",stop25R,stop26R));
        connections2.add(createLink("pt_80Lr",stop26R,stop27R));//26
        connections2.add(createLink("pt_81Lr",stop27R,stop28R));//25
        connections2.add(createLink("pt_82Lr",stop28R,stop29R));
        connections2.add(createLink("pt_83Lr",stop29R,stop30R));//24
        connections2.add(createLink("pt_84Lr",stop30R,stop31R));//24
        connections2.add(createLink("pt_85Lr",stop31R,stop32R));//23
        connections2.add(createLink("pt_86Lr",stop32R,stop33R));//22
        connections2.add(createLink("pt_87Lr",stop33R,stop34R));//21
        connections2.add(createLink("pt_88Lr",stop34R,stop35R));//20
        connections2.add(createLink("pt_89Lr",stop35R,stop36R));//19
        connections2.add(createLink("pt_90Lr",stop36R,stop37R));//18
        connections2.add(createLink("pt_91Lr",stop37R,stop38R));//17
        connections2.add(createLink("pt_92Lr",stop38R,stop39R));//16
        connections2.add(createLink("pt_93Lr",stop39R,stop40R));//15
        connections2.add(createLink("pt_94Lr",stop40R,stop41R));//14
        connections2.add(createLink("pt_95Lr",stop41R,stop42R));//13
        connections2.add(createLink("pt_96Lr",stop42R,stop43R));//12
        connections2.add(createLink("pt_97Lr",stop43R,stop44R));//11
        connections2.add(createLink("pt_98Lr",stop44R,stop45R));//10
        connections2.add(createLink("pt_99Lr",stop45R,stop46R));//9
        connections2.add(createLink("pt_100Lr",stop46R,stop47R));//8
        connections2.add(createLink("pt_101Lr",stop47R,stop48R));//7
        connections2.add(createLink("pt_102Lr",stop48R,stop49R));//6
        connections2.add(createLink("pt_103Lr",stop49R,stop50R));//5
        connections2.add(createLink("pt_104Lr",stop50R,stop51R));//4
        connections2.add(createLink("pt_105Lr",stop51R,stop52R));//3
        connections2.add(createLink("pt_106Lr",stop52R,endNodeR));//1//2


        for(var link:connections2){
            network.addLink(link);
        }
        var ids2 = connections2.stream()
                .map(link -> link.getId()).collect(Collectors.toList());
        var networkRoute2 = RouteUtils.createNetworkRoute( ids2);


        var Stop1r = scheduleFactory.createTransitStopFacility(Id.create("Stop_1r", TransitStopFacility.class), stop1R.getCoord(), false);
        Stop1r.setLinkId(connections2.get(0).getId());
        var Stop2r = scheduleFactory.createTransitStopFacility(Id.create("Stop_2r", TransitStopFacility.class), stop3R.getCoord(), false);
        Stop2r.setLinkId(connections2.get(1).getId());
        var Stop3r = scheduleFactory.createTransitStopFacility(Id.create("Stop_3r", TransitStopFacility.class), stop5R.getCoord(), false);
        Stop3r.setLinkId(connections2.get(4).getId());
        var Stop4r = scheduleFactory.createTransitStopFacility(Id.create("Stop_4r", TransitStopFacility.class), stop7R.getCoord(), false);
        Stop4r.setLinkId(connections2.get(6).getId());
        var Stop5r = scheduleFactory.createTransitStopFacility(Id.create("Stop_5r", TransitStopFacility.class), stop9R.getCoord(), false);
        Stop5r.setLinkId(connections2.get(8).getId());
        var Stop6r = scheduleFactory.createTransitStopFacility(Id.create("Stop_6r", TransitStopFacility.class), stop10R.getCoord(), false);
        Stop6r.setLinkId(connections2.get(9).getId());
        var Stop7r = scheduleFactory.createTransitStopFacility(Id.create("Stop_7r", TransitStopFacility.class), stop12R.getCoord(), false);
        Stop7r.setLinkId(connections2.get(11).getId());
        var Stop8r = scheduleFactory.createTransitStopFacility(Id.create("Stop_8r", TransitStopFacility.class), stop14R.getCoord(), false);
        Stop8r.setLinkId(connections2.get(13).getId());
        var Stop9r = scheduleFactory.createTransitStopFacility(Id.create("Stop_9r", TransitStopFacility.class), stop16R.getCoord(), false);
        Stop9r.setLinkId(connections2.get(15).getId());
        var Stop10r = scheduleFactory.createTransitStopFacility(Id.create("Stop_10r", TransitStopFacility.class), stop18R.getCoord(), false);
        Stop10r.setLinkId(connections2.get(17).getId());
        var Stop11r = scheduleFactory.createTransitStopFacility(Id.create("Stop_11r", TransitStopFacility.class), stop20R.getCoord(), false);
        Stop11r.setLinkId(connections2.get(19).getId());
        var Stop12r = scheduleFactory.createTransitStopFacility(Id.create("Stop_12r", TransitStopFacility.class), stop22R.getCoord(), false);
        Stop12r.setLinkId(connections2.get(21).getId());
        var Stop13r = scheduleFactory.createTransitStopFacility(Id.create("Stop_13r", TransitStopFacility.class), stop25R.getCoord(), false);
        Stop13r.setLinkId(connections2.get(24).getId());
        var Stop14r = scheduleFactory.createTransitStopFacility(Id.create("Stop_14r", TransitStopFacility.class), stop27R.getCoord(), false);
        Stop14r.setLinkId(connections2.get(26).getId());
        var Stop15r = scheduleFactory.createTransitStopFacility(Id.create("Stop_15r", TransitStopFacility.class), stop28R.getCoord(), false);
        Stop15r.setLinkId(connections2.get(27).getId());
        var Stop16r = scheduleFactory.createTransitStopFacility(Id.create("Stop_16r", TransitStopFacility.class), stop30R.getCoord(), false);
        Stop16r.setLinkId(connections2.get(29).getId());
        var Stop17r = scheduleFactory.createTransitStopFacility(Id.create("Stop_17r", TransitStopFacility.class), stop31R.getCoord(), false);
        Stop17r.setLinkId(connections2.get(30).getId());
        var Stop18r = scheduleFactory.createTransitStopFacility(Id.create("Stop_18r", TransitStopFacility.class), stop32R.getCoord(), false);
        Stop18r.setLinkId(connections2.get(31).getId());
        var Stop19r = scheduleFactory.createTransitStopFacility(Id.create("Stop_19r", TransitStopFacility.class), stop33R.getCoord(), false);
        Stop19r.setLinkId(connections2.get(32).getId());
        var Stop20r = scheduleFactory.createTransitStopFacility(Id.create("Stop_20r", TransitStopFacility.class), stop34R.getCoord(), false);
        Stop20r.setLinkId(connections2.get(33).getId());
        var Stop21r = scheduleFactory.createTransitStopFacility(Id.create("Stop_21r", TransitStopFacility.class), stop35R.getCoord(), false);
        Stop21r.setLinkId(connections2.get(34).getId());
        var Stop22r = scheduleFactory.createTransitStopFacility(Id.create("Stop_22r", TransitStopFacility.class), stop36R.getCoord(), false);
        Stop22r.setLinkId(connections2.get(35).getId());
        var Stop23r = scheduleFactory.createTransitStopFacility(Id.create("Stop_23r", TransitStopFacility.class), stop37R.getCoord(), false);
        Stop23r.setLinkId(connections2.get(36).getId());
        var Stop24r = scheduleFactory.createTransitStopFacility(Id.create("Stop_24r", TransitStopFacility.class), stop38R.getCoord(), false);
        Stop24r.setLinkId(connections2.get(37).getId());
        var Stop25r = scheduleFactory.createTransitStopFacility(Id.create("Stop_25r", TransitStopFacility.class), stop39R.getCoord(), false);
        Stop25r.setLinkId(connections2.get(38).getId());
        var Stop26r = scheduleFactory.createTransitStopFacility(Id.create("Stop_26r", TransitStopFacility.class), stop40R.getCoord(), false);
        Stop26r.setLinkId(connections2.get(39).getId());
        var Stop27r = scheduleFactory.createTransitStopFacility(Id.create("Stop_27r", TransitStopFacility.class), stop41R.getCoord(), false);
        Stop27r.setLinkId(connections2.get(40).getId());
        var Stop28r = scheduleFactory.createTransitStopFacility(Id.create("Stop_28r", TransitStopFacility.class), stop42R.getCoord(), false);
        Stop28r.setLinkId(connections2.get(41).getId());
        var Stop29r = scheduleFactory.createTransitStopFacility(Id.create("Stop_29r", TransitStopFacility.class), stop43R.getCoord(), false);
        Stop29r.setLinkId(connections2.get(42).getId());
        var Stop30r = scheduleFactory.createTransitStopFacility(Id.create("Stop_30r", TransitStopFacility.class), stop44R.getCoord(), false);
        Stop30r.setLinkId(connections2.get(43).getId());
        var Stop31r = scheduleFactory.createTransitStopFacility(Id.create("Stop_31r", TransitStopFacility.class), stop45R.getCoord(), false);
        Stop31r.setLinkId(connections2.get(44).getId());
        var Stop32r = scheduleFactory.createTransitStopFacility(Id.create("Stop_32r", TransitStopFacility.class), stop46R.getCoord(), false);
        Stop32r.setLinkId(connections2.get(45).getId());
        var Stop33r = scheduleFactory.createTransitStopFacility(Id.create("Stop_33r", TransitStopFacility.class), stop47R.getCoord(), false);
        Stop33r.setLinkId(connections2.get(46).getId());
        var Stop34r = scheduleFactory.createTransitStopFacility(Id.create("Stop_34r", TransitStopFacility.class), stop48R.getCoord(), false);
        Stop34r.setLinkId(connections2.get(47).getId());
        var Stop35r = scheduleFactory.createTransitStopFacility(Id.create("Stop_35r", TransitStopFacility.class), stop49R.getCoord(), false);
        Stop35r.setLinkId(connections2.get(48).getId());
        var Stop36r = scheduleFactory.createTransitStopFacility(Id.create("Stop_36r", TransitStopFacility.class), stop50R.getCoord(), false);
        Stop36r.setLinkId(connections2.get(49).getId());
        var Stop37r = scheduleFactory.createTransitStopFacility(Id.create("Stop_37r", TransitStopFacility.class), stop51R.getCoord(), false);
        Stop37r.setLinkId(connections2.get(50).getId());
        var Stop38r = scheduleFactory.createTransitStopFacility(Id.create("Stop_38r", TransitStopFacility.class), stop52R.getCoord(), false);
        Stop38r.setLinkId(connections2.get(51).getId());

        scenario.getTransitSchedule().addStopFacility(Stop1f);
        scenario.getTransitSchedule().addStopFacility(Stop2f);
        scenario.getTransitSchedule().addStopFacility(Stop3f);
        scenario.getTransitSchedule().addStopFacility(Stop4f);
        scenario.getTransitSchedule().addStopFacility(Stop5f);
        scenario.getTransitSchedule().addStopFacility(Stop6f);
        scenario.getTransitSchedule().addStopFacility(Stop7f);
        scenario.getTransitSchedule().addStopFacility(Stop8f);
        scenario.getTransitSchedule().addStopFacility(Stop9f);
        scenario.getTransitSchedule().addStopFacility(Stop10f);
        scenario.getTransitSchedule().addStopFacility(Stop11f);
        scenario.getTransitSchedule().addStopFacility(Stop12f);
        scenario.getTransitSchedule().addStopFacility(Stop13f);
        scenario.getTransitSchedule().addStopFacility(Stop14f);
        scenario.getTransitSchedule().addStopFacility(Stop15f);
        scenario.getTransitSchedule().addStopFacility(Stop16f);
        scenario.getTransitSchedule().addStopFacility(Stop17f);
        scenario.getTransitSchedule().addStopFacility(Stop18f);
        scenario.getTransitSchedule().addStopFacility(Stop19f);
        scenario.getTransitSchedule().addStopFacility(Stop20f);
        scenario.getTransitSchedule().addStopFacility(Stop21f);
        scenario.getTransitSchedule().addStopFacility(Stop22f);
        scenario.getTransitSchedule().addStopFacility(Stop23f);
        scenario.getTransitSchedule().addStopFacility(Stop24f);
        scenario.getTransitSchedule().addStopFacility(Stop25f);
        scenario.getTransitSchedule().addStopFacility(Stop26f);
        scenario.getTransitSchedule().addStopFacility(Stop27f);
        scenario.getTransitSchedule().addStopFacility(Stop28f);
        scenario.getTransitSchedule().addStopFacility(Stop29f);
        scenario.getTransitSchedule().addStopFacility(Stop30f);
        scenario.getTransitSchedule().addStopFacility(Stop31f);
        scenario.getTransitSchedule().addStopFacility(Stop32f);
        scenario.getTransitSchedule().addStopFacility(Stop33f);
        scenario.getTransitSchedule().addStopFacility(Stop34f);
        scenario.getTransitSchedule().addStopFacility(Stop35f);
        scenario.getTransitSchedule().addStopFacility(Stop36f);
        scenario.getTransitSchedule().addStopFacility(Stop37f);
        scenario.getTransitSchedule().addStopFacility(Stop38f);

        scenario.getTransitSchedule().addStopFacility(Stop1r);
        scenario.getTransitSchedule().addStopFacility(Stop2r);
        scenario.getTransitSchedule().addStopFacility(Stop3r);
        scenario.getTransitSchedule().addStopFacility(Stop4r);
        scenario.getTransitSchedule().addStopFacility(Stop5r);
        scenario.getTransitSchedule().addStopFacility(Stop6r);
        scenario.getTransitSchedule().addStopFacility(Stop7r);
        scenario.getTransitSchedule().addStopFacility(Stop8r);
        scenario.getTransitSchedule().addStopFacility(Stop9r);
        scenario.getTransitSchedule().addStopFacility(Stop10r);
        scenario.getTransitSchedule().addStopFacility(Stop11r);
        scenario.getTransitSchedule().addStopFacility(Stop12r);
        scenario.getTransitSchedule().addStopFacility(Stop13r);
        scenario.getTransitSchedule().addStopFacility(Stop14r);
        scenario.getTransitSchedule().addStopFacility(Stop15r);
        scenario.getTransitSchedule().addStopFacility(Stop16r);
        scenario.getTransitSchedule().addStopFacility(Stop17r);
        scenario.getTransitSchedule().addStopFacility(Stop18r);
        scenario.getTransitSchedule().addStopFacility(Stop19r);
        scenario.getTransitSchedule().addStopFacility(Stop20r);
        scenario.getTransitSchedule().addStopFacility(Stop21r);
        scenario.getTransitSchedule().addStopFacility(Stop22r);
        scenario.getTransitSchedule().addStopFacility(Stop23r);
        scenario.getTransitSchedule().addStopFacility(Stop24r);
        scenario.getTransitSchedule().addStopFacility(Stop25r);
        scenario.getTransitSchedule().addStopFacility(Stop26r);
        scenario.getTransitSchedule().addStopFacility(Stop27r);
        scenario.getTransitSchedule().addStopFacility(Stop28r);
        scenario.getTransitSchedule().addStopFacility(Stop29r);
        scenario.getTransitSchedule().addStopFacility(Stop30r);
        scenario.getTransitSchedule().addStopFacility(Stop31r);
        scenario.getTransitSchedule().addStopFacility(Stop32r);
        scenario.getTransitSchedule().addStopFacility(Stop33r);
        scenario.getTransitSchedule().addStopFacility(Stop34r);
        scenario.getTransitSchedule().addStopFacility(Stop35r);
        scenario.getTransitSchedule().addStopFacility(Stop36r);
        scenario.getTransitSchedule().addStopFacility(Stop37r);
        scenario.getTransitSchedule().addStopFacility(Stop38r);


        var Stop1 = scheduleFactory.createTransitRouteStop(Stop1f, 0, 0);
        var Stop2 = scheduleFactory.createTransitRouteStop(Stop2f, 160, 180);
        var Stop3 = scheduleFactory.createTransitRouteStop(Stop3f, 354, 374);
        var Stop4 = scheduleFactory.createTransitRouteStop(Stop4f, 465, 485);
        var Stop5 = scheduleFactory.createTransitRouteStop(Stop5f, 502, 522);
        var Stop6 = scheduleFactory.createTransitRouteStop(Stop6f, 725, 745);
        var Stop7 = scheduleFactory.createTransitRouteStop(Stop7f, 804, 824);
        var Stop8 = scheduleFactory.createTransitRouteStop(Stop8f, 957, 977);
        var Stop9 = scheduleFactory.createTransitRouteStop(Stop9f, 1091, 1111);
        var Stop10 = scheduleFactory.createTransitRouteStop(Stop10f, 1178, 1198);
        var Stop11 = scheduleFactory.createTransitRouteStop(Stop11f, 1313, 1333);
        var Stop12 = scheduleFactory.createTransitRouteStop(Stop12f, 1411, 1431);
        var Stop13 = scheduleFactory.createTransitRouteStop(Stop13f, 1533, 1553);
        var Stop14 = scheduleFactory.createTransitRouteStop(Stop14f, 1668, 1688);
        var Stop15 = scheduleFactory.createTransitRouteStop(Stop15f, 1778, 1798);
        var Stop16 = scheduleFactory.createTransitRouteStop(Stop16f, 1944, 1964);
        var Stop17 = scheduleFactory.createTransitRouteStop(Stop17f, 2102, 2122);
        var Stop18 = scheduleFactory.createTransitRouteStop(Stop18f, 2269, 2289);
        var Stop19 = scheduleFactory.createTransitRouteStop(Stop19f, 2443, 2463);
        var Stop20 = scheduleFactory.createTransitRouteStop(Stop20f, 2545, 2565);
        var Stop21 = scheduleFactory.createTransitRouteStop(Stop21f, 2777, 2797);
        var Stop22 = scheduleFactory.createTransitRouteStop(Stop22f, 3079, 3099);///Uttara to jhilmil 54 minute
        var Stop23 = scheduleFactory.createTransitRouteStop(Stop23f, 3442, 3462);
        var Stop24 = scheduleFactory.createTransitRouteStop(Stop24f, 3890, 3910);
        var Stop25 = scheduleFactory.createTransitRouteStop(Stop25f, 3980, 4000);
        var Stop26 = scheduleFactory.createTransitRouteStop(Stop26f, 4176, 4196);
        var Stop27 = scheduleFactory.createTransitRouteStop(Stop27f, 4291, 4311);
        var Stop28 = scheduleFactory.createTransitRouteStop(Stop28f, 4728, 4748);
        var Stop29 = scheduleFactory.createTransitRouteStop(Stop29f, 5044, 5064);
        var Stop30 = scheduleFactory.createTransitRouteStop(Stop30f, 5267, 5287);
        var Stop31 = scheduleFactory.createTransitRouteStop(Stop31f, 5484, 5504);
        var Stop32 = scheduleFactory.createTransitRouteStop(Stop32f, 5649, 5669);
        var Stop33 = scheduleFactory.createTransitRouteStop(Stop33f, 5716, 5736);
        var Stop34 = scheduleFactory.createTransitRouteStop(Stop34f, 5830, 5850);
        var Stop35 = scheduleFactory.createTransitRouteStop(Stop35f, 6051, 6071);
        var Stop36 = scheduleFactory.createTransitRouteStop(Stop36f, 6216, 6236);
        var Stop37 = scheduleFactory.createTransitRouteStop(Stop37f, 6386, 6406);
        var Stop38 = scheduleFactory.createTransitRouteStop(Stop38f, 6703, 6723);

        var Stop39 = scheduleFactory.createTransitRouteStop(Stop1r, 0, 0);
        var Stop40 = scheduleFactory.createTransitRouteStop(Stop2r, 296, 317);
        var Stop41 = scheduleFactory.createTransitRouteStop(Stop3r, 467, 487);
        var Stop42 = scheduleFactory.createTransitRouteStop(Stop4r, 631, 651);
        var Stop43 = scheduleFactory.createTransitRouteStop(Stop5r, 853, 873);
        var Stop44 = scheduleFactory.createTransitRouteStop(Stop6r, 967, 987);
        var Stop45 = scheduleFactory.createTransitRouteStop(Stop7r, 1034, 1054);
        var Stop46 = scheduleFactory.createTransitRouteStop(Stop8r, 1200, 1220);
        var Stop47 = scheduleFactory.createTransitRouteStop(Stop9r, 1416, 1436);
        var Stop48 = scheduleFactory.createTransitRouteStop(Stop10r, 1639, 1659);
        var Stop49 = scheduleFactory.createTransitRouteStop(Stop11r, 1955, 1975);
        var Stop50 = scheduleFactory.createTransitRouteStop(Stop12r, 2392, 2412);
        var Stop51 = scheduleFactory.createTransitRouteStop(Stop13r, 2506, 2526);
        var Stop52 = scheduleFactory.createTransitRouteStop(Stop14r, 2703, 2723);
        var Stop53 = scheduleFactory.createTransitRouteStop(Stop15r, 2792, 2812);
        var Stop54 = scheduleFactory.createTransitRouteStop(Stop16r, 3240, 3260);
        var Stop55 = scheduleFactory.createTransitRouteStop(Stop17r, 3604, 3624);
        var Stop56 = scheduleFactory.createTransitRouteStop(Stop18r, 3906, 3926);
        var Stop57 = scheduleFactory.createTransitRouteStop(Stop19r, 4137, 4157);
        var Stop58 = scheduleFactory.createTransitRouteStop(Stop20r, 4240, 4260);
        var Stop59 = scheduleFactory.createTransitRouteStop(Stop21r, 4414, 4434);
        var Stop60 = scheduleFactory.createTransitRouteStop(Stop22r, 4581, 4601);
        var Stop61 = scheduleFactory.createTransitRouteStop(Stop23r, 4739, 4759);
        var Stop62 = scheduleFactory.createTransitRouteStop(Stop24r, 4905, 4925);
        var Stop63 = scheduleFactory.createTransitRouteStop(Stop25r, 5015, 5035);
        var Stop64 = scheduleFactory.createTransitRouteStop(Stop26r, 5149, 5169);
        var Stop65 = scheduleFactory.createTransitRouteStop(Stop27r, 5272, 5292);
        var Stop66 = scheduleFactory.createTransitRouteStop(Stop28r, 5370, 5390);
        var Stop67 = scheduleFactory.createTransitRouteStop(Stop29r, 5504, 5524);
        var Stop68 = scheduleFactory.createTransitRouteStop(Stop30r, 5591, 5611);
        var Stop69 = scheduleFactory.createTransitRouteStop(Stop31r, 5725, 5745);
        var Stop70 = scheduleFactory.createTransitRouteStop(Stop32r, 5879, 5899);
        var Stop71 = scheduleFactory.createTransitRouteStop(Stop33r, 5958, 5978);
        var Stop72 = scheduleFactory.createTransitRouteStop(Stop34r, 6182, 6202);
        var Stop73 = scheduleFactory.createTransitRouteStop(Stop35r, 6219, 6239);
        var Stop74 = scheduleFactory.createTransitRouteStop(Stop36r, 6328, 6348);
        var Stop75 = scheduleFactory.createTransitRouteStop(Stop37r, 6523, 6543);
        var Stop76 = scheduleFactory.createTransitRouteStop(Stop38r, 6703, 6723);

        var route1 = scheduleFactory.createTransitRoute(Id.create("route-1", TransitRoute.class), networkRoute, List.of(Stop1, Stop2,Stop3,Stop4,Stop5,
                Stop6,Stop7,Stop8,Stop9,Stop10,Stop11,Stop12,Stop13,Stop14,Stop15,Stop16,Stop17,Stop18,Stop19,Stop20,Stop21,Stop22,Stop23,Stop24,Stop25,Stop26,Stop27,Stop28,Stop29,Stop30,Stop31,Stop32,Stop33,Stop34,
                Stop35,Stop36,Stop37,Stop38), "pt");

        var route2 = scheduleFactory.createTransitRoute(Id.create("route-2", TransitRoute.class), networkRoute2, List.of(Stop39, Stop40,Stop41, Stop42, Stop43, Stop44, Stop45, Stop46, Stop47, Stop48, Stop49,
                Stop50, Stop51, Stop52, Stop53, Stop54, Stop55, Stop56, Stop57, Stop58, Stop59, Stop60, Stop61, Stop62, Stop63, Stop64, Stop65, Stop66, Stop67, Stop68, Stop69, Stop70, Stop71, Stop72, Stop73, Stop74, Stop75, Stop76),"pt");


        // create departures and vehicles for each departure
        for (int i = 5 * 3600; i < 23 * 3600;) {

            var departure1 = scheduleFactory.createDeparture(Id.create("departure_f_" + i, Departure.class), i);
            var vehicle1 = scenario.getTransitVehicles().getFactory().createVehicle(Id.createVehicleId("brt_1_" + i), vehicleType);
            departure1.setVehicleId(vehicle1.getId());
            scenario.getTransitVehicles().addVehicle(vehicle1);
            route1.addDeparture(departure1);

            var departure2 = scheduleFactory.createDeparture(Id.create("departure_r_" + i, Departure.class), i);
            var vehicle2 = scenario.getTransitVehicles().getFactory().createVehicle(Id.createVehicleId("brt_2_" + i), vehicleType);
            departure2.setVehicleId(vehicle2.getId());
            scenario.getTransitVehicles().addVehicle(vehicle2);
            route2.addDeparture(departure2);




            if(i>=8*3600 && i<=10*3600 || i>=16*3600 && i<=17*3600){
                i+=120;//at peak time 1 buses every 2 minutes
            }else{
                i += 300;//at off-peak 1 buses every 5 minutes
            }

        }

        var line1 = scheduleFactory.createTransitLine(Id.create("brt_1", TransitLine.class));
        line1.addRoute(route1);
        var line2 = scheduleFactory.createTransitLine(Id.create("brt_2", TransitLine.class));

        line2.addRoute(route2);


        scenario.getTransitSchedule().addTransitLine(line1);
        scenario.getTransitSchedule().addTransitLine(line2);


        new NetworkWriter(network).write(root.resolve("network-with-pt.xml.gz").toString());
        new TransitScheduleWriter(scenario.getTransitSchedule()).writeFile(root.resolve("transit-Schedule.xml.gz").toString());
        new MatsimVehicleWriter(scenario.getTransitVehicles()).writeFile(root.resolve("transit-vehicles.xml.gz").toString());
    }

    private static String getString(String f) {
        return "f";
    }

    private static Link createLink(String id, Node from, Node to) {

        var connection  = networkFactory.createLink(Id.createLinkId(id), from, to);
        connection.setAllowedModes(Set.of(TransportMode.pt));
        connection.setFreespeed(100);//how to define free speed and capacity?
        connection.setCapacity(10000);//
        return connection;
    }
}
