package org.matsim.project;

import ch.sbb.matsim.config.SwissRailRaptorConfigGroup;
import ch.sbb.matsim.routing.pt.raptor.SwissRailRaptorModule;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.config.groups.StrategyConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.replanning.strategies.DefaultPlanStrategiesModule;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.collections.CollectionUtils;
import org.matsim.pt.config.TransitConfigGroup.BoardingAcceptance;

import java.util.HashSet;
import java.util.Set;

public class RunMatsim5 {
    public static void main(String[] args) {
        //Time and cost sensitivity at the base scenario with BRT and multimodla accessibility

        Config config;
        if (args == null || args.length == 0 || args[0] == null) {
            config = ConfigUtils.loadConfig("./config.xml");
        } else {
            config = ConfigUtils.loadConfig(args);
        }
        config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
        config.qsim().setTrafficDynamics(QSimConfigGroup.TrafficDynamics.kinematicWaves);
        config.qsim().setSnapshotStyle(QSimConfigGroup.SnapshotStyle.kinematicWaves);
        {
            StrategyConfigGroup.StrategySettings stratSets = new StrategyConfigGroup.StrategySettings();
            stratSets.setStrategyName(DefaultPlanStrategiesModule.DefaultStrategy.ChangeSingleTripMode);
            stratSets.setWeight(0.1);
            config.strategy().addStrategySettings(stratSets);


            config.transit().setVehiclesFile("transit-vehicles.xml.gz");
            config.transit().setTransitScheduleFile("transit-Schedule.xml.gz");
            config.transit().setUseTransit(true);
            config.transit().setTransitModes(Set.of("pt"));
            config.transitRouter().setMaxBeelineWalkConnectionDistance(0.4);//maximum beeline distance between stops that agents could transfer to by walking
            config.transitRouter().setSearchRadius(400);//the radius in which stop locations are searched, given a start or target coordinate
            config.transitRouter().setExtensionRadius(0);//step size to increase searchRadius if no stops are found
            config.changeMode().setModes(new String[]{"car", "bus", "rickshaw", "hh", "cng", "bike", "walk","motorbike","pt"});
            config.transit().setBoardingAcceptance(BoardingAcceptance.checkStopOnly);
        }
        //routing parameter
        {
            PlansCalcRouteConfigGroup.ModeRoutingParams pars = new PlansCalcRouteConfigGroup.ModeRoutingParams("bus");
            pars.setTeleportedModeFreespeedLimit( 16/3.6 );
            pars.setTeleportedModeFreespeedFactor( 2.50 );
            config.plansCalcRoute().addModeRoutingParams( pars );


            PlansCalcRouteConfigGroup.ModeRoutingParams pars1 = new PlansCalcRouteConfigGroup.ModeRoutingParams("rickshaw");
            pars1.setTeleportedModeSpeed( 10/3.6 );
            pars1.setBeelineDistanceFactor( 2.00 );
            config.plansCalcRoute().addModeRoutingParams( pars1 );

            PlansCalcRouteConfigGroup.ModeRoutingParams pars2 = new PlansCalcRouteConfigGroup.ModeRoutingParams("hh");
            pars2.setTeleportedModeFreespeedLimit( 13/3.6 );
            pars2.setTeleportedModeFreespeedFactor( 3.00 );
            config.plansCalcRoute().addModeRoutingParams( pars2 );

            PlansCalcRouteConfigGroup.ModeRoutingParams pars4 = new PlansCalcRouteConfigGroup.ModeRoutingParams("walk");
            pars4.setTeleportedModeSpeed( 3/3.6  );
            pars4.setBeelineDistanceFactor(1.4);
            config.plansCalcRoute().addModeRoutingParams( pars4 );

            PlansCalcRouteConfigGroup.ModeRoutingParams pars5 = new PlansCalcRouteConfigGroup.ModeRoutingParams("bike");
            pars5.setTeleportedModeSpeed( 8/3.6  );
            pars5.setBeelineDistanceFactor(1.3);
            config.plansCalcRoute().addModeRoutingParams( pars5 );


            config.plansCalcRoute().setNetworkModes(CollectionUtils.stringToSet( "car,cng,motorbike"));


        }
        //scoring parameter
        {
            PlanCalcScoreConfigGroup.ModeParams pars = new PlanCalcScoreConfigGroup.ModeParams("bus");
            pars.setConstant(3);
            pars.setMarginalUtilityOfTraveling(-0.0134);
            pars.setMonetaryDistanceRate(-0.00152);
            config.planCalcScore().addModeParams(pars);

            PlanCalcScoreConfigGroup.ModeParams pars1 = new PlanCalcScoreConfigGroup.ModeParams("rickshaw");
            pars1.setConstant(2);//arc 3 20, arc 4 10
            pars1.setMarginalUtilityOfTraveling(-0.0134);
            pars1.setMonetaryDistanceRate(-0.01);
            config.planCalcScore().addModeParams(pars1);

            PlanCalcScoreConfigGroup.ModeParams pars2 = new PlanCalcScoreConfigGroup.ModeParams("hh");
            pars2.setConstant(2);
            pars2.setMarginalUtilityOfTraveling(-0.0134);
            pars2.setMonetaryDistanceRate(-0.002);
            config.planCalcScore().addModeParams(pars2);

            PlanCalcScoreConfigGroup.ModeParams pars3 = new PlanCalcScoreConfigGroup.ModeParams("cng");
            pars3.setConstant(-1.5);
            pars3.setMarginalUtilityOfTraveling(-0.0134);
            pars3.setMonetaryDistanceRate(-0.006);
            config.planCalcScore().addModeParams(pars3);

            PlanCalcScoreConfigGroup.ModeParams pars4 = new PlanCalcScoreConfigGroup.ModeParams("walk");
            pars4.setConstant(0);
            pars4.setMarginalUtilityOfDistance(-0.000125);//for arc3 and arc 4 -0.001
            pars4.setMarginalUtilityOfTraveling(-0.0134);
            config.planCalcScore().addModeParams(pars4);

            PlanCalcScoreConfigGroup.ModeParams pars5 = new PlanCalcScoreConfigGroup.ModeParams("car");
            pars5.setConstant(0.5);
            pars5.setMarginalUtilityOfTraveling(-0.0134);
            pars5.setMonetaryDistanceRate(-0.008);
            config.planCalcScore().addModeParams(pars5);

            PlanCalcScoreConfigGroup.ModeParams pars6 = new PlanCalcScoreConfigGroup.ModeParams("bike");
            pars6.setConstant(0);
            pars6.setMarginalUtilityOfTraveling(-0.0134);
            pars6.setMarginalUtilityOfDistance(-0.0000759);
            config.planCalcScore().addModeParams(pars6);

            PlanCalcScoreConfigGroup.ModeParams pars7 = new PlanCalcScoreConfigGroup.ModeParams("motorbike");
            pars7.setConstant(-2);
            pars7.setMarginalUtilityOfTraveling(-0.0134);
            pars7.setMonetaryDistanceRate(-0.006);
            config.planCalcScore().addModeParams(pars7);


            PlanCalcScoreConfigGroup.ModeParams pars8 = new PlanCalcScoreConfigGroup.ModeParams( "pt" );
            pars8.setConstant(3);
            pars8.setMarginalUtilityOfTraveling(-0.0134);
            //pars8.setMonetaryDistanceRate(-0.00152);
            pars8.setDailyMonetaryConstant(-30);
            config.planCalcScore().addModeParams( pars8 );
            config.planCalcScore().setMarginalUtilityOfMoney(0.002);
            config.plansCalcRoute().setAccessEgressType(PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLink);
        }


        SwissRailRaptorConfigGroup srrConfig = new SwissRailRaptorConfigGroup();
        srrConfig.setUseIntermodalAccessEgress(true);
        srrConfig.setIntermodalAccessEgressModeSelection(SwissRailRaptorConfigGroup.IntermodalAccessEgressModeSelection.RandomSelectOneModePerRoutingRequestAndDirection);

        SwissRailRaptorConfigGroup.IntermodalAccessEgressParameterSet paramSetWalk = new SwissRailRaptorConfigGroup.IntermodalAccessEgressParameterSet();
        paramSetWalk.setMode(TransportMode.walk);
        paramSetWalk.setInitialSearchRadius(400);
        paramSetWalk.setMaxRadius(1000);
        paramSetWalk.setSearchExtensionRadius(0.1);
        srrConfig.addIntermodalAccessEgress(paramSetWalk);

        SwissRailRaptorConfigGroup.IntermodalAccessEgressParameterSet paramSetbike = new SwissRailRaptorConfigGroup.IntermodalAccessEgressParameterSet();
        paramSetbike.setMode(TransportMode.bike);
        paramSetbike.setInitialSearchRadius(1000);
        paramSetbike.setMaxRadius(2000);
        paramSetbike.setSearchExtensionRadius(0.1);
        srrConfig.addIntermodalAccessEgress(paramSetbike);

        SwissRailRaptorConfigGroup.IntermodalAccessEgressParameterSet paramSetRickshaw = new SwissRailRaptorConfigGroup.IntermodalAccessEgressParameterSet();
        paramSetRickshaw.setMode("rickshaw");
        paramSetRickshaw.setInitialSearchRadius(4000);
        paramSetRickshaw.setMaxRadius(6000);
        paramSetRickshaw.setSearchExtensionRadius(0.1);
        srrConfig.addIntermodalAccessEgress(paramSetRickshaw);
        config.addModule(srrConfig);

        Scenario scenario = ScenarioUtils.loadScenario(config) ;
        for (Link link : scenario.getNetwork().getLinks().values()) {
            if (link.getAllowedModes().contains(TransportMode.car)) {
                // It would be better to set the modes directly in the input network file if not all car roads are open to rickshaw, cng, bus, hh etc.
                Set allowedModes1 = new HashSet(link.getAllowedModes());
                allowedModes1.add("cng");
                allowedModes1.add("motorbike");
                link.setAllowedModes(allowedModes1);
            }
            if (link.getFreespeed()>=10){
                Set allowedModes2 = new HashSet(link.getAllowedModes());
                allowedModes2.add("hh");
                allowedModes2.add("bus");
                link.setAllowedModes( allowedModes2 );
            }



            if(link.getLength()<=0)
                link.setLength(10);



        }



        Controler controler = new Controler( scenario ) ;


        controler.addOverridingModule(new SwissRailRaptorModule());
        // ---

        controler.run();
    }
}