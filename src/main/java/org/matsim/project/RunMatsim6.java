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

public class RunMatsim6 {
    public static void main(String[] args) {
        //employed_subgroup

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
            config.transit().setVehiclesFile("transit-vehicles.xml.gz");
            config.transit().setTransitScheduleFile("transit-Schedule.xml.gz");
            config.transit().setUseTransit(true);
            config.transit().setTransitModes(Set.of("pt"));
            //config.transitRouter().setMaxBeelineWalkConnectionDistance(0.4);//maximum beeline distance between stops that agents could transfer to by walking
            config.transitRouter().setSearchRadius(400);//the radius in which stop locations are searched, given a start or target coordinate
            config.transitRouter().setExtensionRadius(0);//step size to increase searchRadius if no stops are found
            config.changeMode().setModes(new String[]{"car", "bus", "rickshaw", "hh", "cng", "bike", "walk","motorbike","pt"});
            config.transit().setBoardingAcceptance(BoardingAcceptance.checkStopOnly);
            config.planCalcScore().setBrainExpBeta(1);
            config.planCalcScore().setLearningRate(1);
            config.strategy().setMaxAgentPlanMemorySize(5);
            config.strategy().setFractionOfIterationsToDisableInnovation(0.8);


        }
        {
            config.strategy().addStrategySettings( new StrategyConfigGroup.StrategySettings().setStrategyName( DefaultPlanStrategiesModule.DefaultStrategy.ChangeSingleTripMode ).setSubpopulation( "employed").setWeight(0.1 ) );
            config.strategy().addStrategySettings( new StrategyConfigGroup.StrategySettings().setStrategyName( DefaultPlanStrategiesModule.DefaultSelector.SelectExpBeta ).setSubpopulation("employed" ).setWeight(0.8 ) );
            config.strategy().addStrategySettings( new StrategyConfigGroup.StrategySettings().setStrategyName( DefaultPlanStrategiesModule.DefaultStrategy.ReRoute ).setSubpopulation( "employed").setWeight(0.1 ) );


        }
        {
            config.strategy().addStrategySettings( new StrategyConfigGroup.StrategySettings().setStrategyName( DefaultPlanStrategiesModule.DefaultStrategy.ChangeSingleTripMode ).setSubpopulation( "unemployed").setWeight(0.1 ) );
            config.strategy().addStrategySettings( new StrategyConfigGroup.StrategySettings().setStrategyName( DefaultPlanStrategiesModule.DefaultSelector.SelectExpBeta ).setSubpopulation("unemployed" ).setWeight(0.8 ) );
            config.strategy().addStrategySettings( new StrategyConfigGroup.StrategySettings().setStrategyName( DefaultPlanStrategiesModule.DefaultStrategy.ReRoute ).setSubpopulation( "unemployed").setWeight(0.1 ) );

        }
        {
            config.strategy().addStrategySettings( new StrategyConfigGroup.StrategySettings().setStrategyName( DefaultPlanStrategiesModule.DefaultStrategy.ChangeSingleTripMode ).setSubpopulation( "default").setWeight(0.1 ) );
            config.strategy().addStrategySettings( new StrategyConfigGroup.StrategySettings().setStrategyName( DefaultPlanStrategiesModule.DefaultSelector.SelectExpBeta ).setSubpopulation("default" ).setWeight(0.8 ) );
            config.strategy().addStrategySettings( new StrategyConfigGroup.StrategySettings().setStrategyName( DefaultPlanStrategiesModule.DefaultStrategy.ReRoute ).setSubpopulation( "default").setWeight(0.1 ) );

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



        PlanCalcScoreConfigGroup.ScoringParameterSet params1 = config.planCalcScore().getOrCreateScoringParameters( "employed" );

        {

            PlanCalcScoreConfigGroup.ActivityParams actParams1 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams1.setActivityType( "home" );
            actParams1.setTypicalDuration(12);
            actParams1.setMinimalDuration(8);
            params1.addActivityParams( actParams1 );

            PlanCalcScoreConfigGroup.ActivityParams actParams2 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams2.setActivityType( "work" );
            actParams2.setTypicalDuration(8);
            actParams2.setMinimalDuration(1);
            actParams2.setOpeningTime(7*3600);
            actParams2.setClosingTime(22*3600);
            params1.addActivityParams( actParams2 );

            PlanCalcScoreConfigGroup.ActivityParams actParams3 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams3.setActivityType( "education" );
            actParams3.setTypicalDuration(5);
            actParams3.setOpeningTime(8*3600);
            actParams3.setClosingTime(20*3600);
            params1.addActivityParams( actParams3 );

            PlanCalcScoreConfigGroup.ActivityParams actParams4 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams4.setActivityType( "shopping" );
            actParams4.setTypicalDuration(1);
            actParams4.setOpeningTime(8*3600);
            actParams4.setClosingTime(22*3600);
            params1.addActivityParams( actParams4 );

            PlanCalcScoreConfigGroup.ActivityParams actParams5 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams5.setActivityType( "personal" );
            actParams5.setTypicalDuration(1);
            actParams5.setOpeningTime(0*3600);
            actParams5.setClosingTime(24*3600);
            params1.addActivityParams( actParams5 );

            PlanCalcScoreConfigGroup.ActivityParams actParams6 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams6.setActivityType( "leisure" );
            actParams6.setTypicalDuration(1);
            actParams6.setOpeningTime(7*3600);
            actParams6.setClosingTime(23*3600);
            params1.addActivityParams( actParams6 );

            PlanCalcScoreConfigGroup.ActivityParams actParams7 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams7.setActivityType( "other" );
            actParams7.setTypicalDuration(1);
            actParams7.setOpeningTime(0*3600);
            actParams7.setClosingTime(24*3600);
            params1.addActivityParams( actParams7 );
            params1.setPerforming_utils_hr(6);
            params1.setMarginalUtlOfWaiting_utils_hr(0.0);
            params1.setLateArrival_utils_hr(-18);
            params1.setMarginalUtlOfWaitingPt_utils_hr(0.0);
            params1.setMarginalUtilityOfMoney(0.002);


        }

        {
            PlanCalcScoreConfigGroup.ModeParams pars = new PlanCalcScoreConfigGroup.ModeParams("bus");
            pars.setConstant(3);
            pars.setMarginalUtilityOfTraveling(-0.802-0.102640);
            pars.setMonetaryDistanceRate(-0.00152);
            params1.addModeParams(pars);

            PlanCalcScoreConfigGroup.ModeParams pars1 = new PlanCalcScoreConfigGroup.ModeParams("rickshaw");
            pars1.setConstant(2);//arc 3 20, arc 4 10
            pars1.setMarginalUtilityOfTraveling(-0.802-0.102640);
            pars1.setMonetaryDistanceRate(-0.01);
            params1.addModeParams(pars1);

            PlanCalcScoreConfigGroup.ModeParams pars2 = new PlanCalcScoreConfigGroup.ModeParams("hh");
            pars2.setConstant(2);
            pars2.setMarginalUtilityOfTraveling(-0.802-0.102640);
            pars2.setMonetaryDistanceRate(-0.002);
            params1.addModeParams(pars2);

            PlanCalcScoreConfigGroup.ModeParams pars3 = new PlanCalcScoreConfigGroup.ModeParams("cng");
            pars3.setConstant(-1.5);
            pars3.setMarginalUtilityOfTraveling(-0.802-0.102640);
            pars3.setMonetaryDistanceRate(-0.006);
            params1.addModeParams(pars3);

            PlanCalcScoreConfigGroup.ModeParams pars4 = new PlanCalcScoreConfigGroup.ModeParams("walk");
            pars4.setConstant(0);
            pars4.setMarginalUtilityOfDistance(-0.00012527);//for arc3 and arc 4 -0.001
            pars4.setMarginalUtilityOfTraveling(-0.802-0.102640);
            params1.addModeParams(pars4);

            PlanCalcScoreConfigGroup.ModeParams pars5 = new PlanCalcScoreConfigGroup.ModeParams("car");
            pars5.setConstant(0.5);//arc 4
            pars5.setMarginalUtilityOfTraveling(-0.802-0.102640);
            pars5.setMonetaryDistanceRate(-0.008);
            params1.addModeParams(pars5);

            PlanCalcScoreConfigGroup.ModeParams pars6 = new PlanCalcScoreConfigGroup.ModeParams("bike");
            pars6.setConstant(0);
            pars6.setMarginalUtilityOfTraveling(-0.802-0.102640);
            pars6.setMarginalUtilityOfDistance(-0.00007604);
            params1.addModeParams(pars6);

            PlanCalcScoreConfigGroup.ModeParams pars7 = new PlanCalcScoreConfigGroup.ModeParams("motorbike");
            pars7.setConstant(-2);
            pars7.setMarginalUtilityOfTraveling(-0.802-0.102640);
            pars7.setMonetaryDistanceRate(-0.006);
            params1.addModeParams(pars7);


            PlanCalcScoreConfigGroup.ModeParams pars8 = new PlanCalcScoreConfigGroup.ModeParams( "pt" );
            pars8.setConstant(3);
            pars8.setMarginalUtilityOfTraveling(-0.802-0.102640);
            pars8.setMonetaryDistanceRate(-0.00152);
            params1.addModeParams( pars8 );

        }

        PlanCalcScoreConfigGroup.ScoringParameterSet params2 = config.planCalcScore().getOrCreateScoringParameters( "unemployed" );

        {

            PlanCalcScoreConfigGroup.ActivityParams actParams1 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams1.setActivityType( "home" );
            actParams1.setTypicalDuration(12);
            actParams1.setMinimalDuration(8);
            params2.addActivityParams( actParams1 );

            PlanCalcScoreConfigGroup.ActivityParams actParams2 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams2.setActivityType( "work" );
            actParams2.setTypicalDuration(8);
            actParams2.setMinimalDuration(1);
            actParams2.setOpeningTime(7*3600);
            actParams2.setClosingTime(22*3600);
            params2.addActivityParams( actParams2 );

            PlanCalcScoreConfigGroup.ActivityParams actParams3 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams3.setActivityType( "education" );
            actParams3.setTypicalDuration(5);
            actParams3.setOpeningTime(8*3600);
            actParams3.setClosingTime(20*3600);
            params2.addActivityParams( actParams3 );

            PlanCalcScoreConfigGroup.ActivityParams actParams4 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams4.setActivityType( "shopping" );
            actParams4.setTypicalDuration(1);
            actParams4.setOpeningTime(8*3600);
            actParams4.setClosingTime(22*3600);
            params2.addActivityParams( actParams4 );

            PlanCalcScoreConfigGroup.ActivityParams actParams5 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams5.setActivityType( "personal" );
            actParams5.setTypicalDuration(1);
            actParams5.setOpeningTime(0);
            actParams5.setClosingTime(24*3600);
            params2.addActivityParams( actParams5 );

            PlanCalcScoreConfigGroup.ActivityParams actParams6 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams6.setActivityType( "leisure" );
            actParams6.setTypicalDuration(1);
            actParams6.setOpeningTime(7*3600);
            actParams6.setClosingTime(23*3600);
            params2.addActivityParams( actParams6 );

            PlanCalcScoreConfigGroup.ActivityParams actParams7 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams7.setActivityType( "other" );
            actParams7.setTypicalDuration(1);
            actParams7.setOpeningTime(0);
            actParams7.setClosingTime(24*3600);
            params2.addActivityParams( actParams7 );
            params2.setLateArrival_utils_hr(-18);
            params2.setEarlyDeparture_utils_hr(0);
            params2.setPerforming_utils_hr(6);
            params2.setMarginalUtlOfWaiting_utils_hr(0);
            params2.setMarginalUtlOfWaitingPt_utils_hr(0.0);
            params2.setMarginalUtilityOfMoney(0.002);



        }
        {
            PlanCalcScoreConfigGroup.ModeParams pars = new PlanCalcScoreConfigGroup.ModeParams("bus");
            pars.setConstant(3);
            pars.setMarginalUtilityOfTraveling(-0.802);
            pars.setMonetaryDistanceRate(-0.00152);
            params2.addModeParams(pars);

            PlanCalcScoreConfigGroup.ModeParams pars1 = new PlanCalcScoreConfigGroup.ModeParams("rickshaw");
            pars1.setConstant(2);
            pars1.setMarginalUtilityOfTraveling(-0.802);
            pars1.setMonetaryDistanceRate(-0.01);
            params2.addModeParams(pars1);

            PlanCalcScoreConfigGroup.ModeParams pars2 = new PlanCalcScoreConfigGroup.ModeParams("hh");
            pars2.setConstant(2);
            pars2.setMarginalUtilityOfTraveling(-0.802);
            pars2.setMonetaryDistanceRate(-0.002);
            params2.addModeParams(pars2);

            PlanCalcScoreConfigGroup.ModeParams pars3 = new PlanCalcScoreConfigGroup.ModeParams("cng");
            pars3.setConstant(-1.5);
            pars3.setMarginalUtilityOfTraveling(-0.802);
            pars3.setMonetaryDistanceRate(-0.006);
            params2.addModeParams(pars3);

            PlanCalcScoreConfigGroup.ModeParams pars4 = new PlanCalcScoreConfigGroup.ModeParams("walk");
            pars4.setConstant(0);
            pars4.setMarginalUtilityOfDistance(-0.00012527);
            pars4.setMarginalUtilityOfTraveling(-0.802);
            params2.addModeParams(pars4);

            PlanCalcScoreConfigGroup.ModeParams pars5 = new PlanCalcScoreConfigGroup.ModeParams("car");
            pars5.setConstant(0.5);
            pars5.setMarginalUtilityOfTraveling(-0.802);
            pars5.setMonetaryDistanceRate(-0.008);
            params2.addModeParams(pars5);

            PlanCalcScoreConfigGroup.ModeParams pars6 = new PlanCalcScoreConfigGroup.ModeParams("bike");
            pars6.setConstant(0);
            pars6.setMarginalUtilityOfTraveling(-0.802);
            pars6.setMarginalUtilityOfDistance(-0.00007604);
            params2.addModeParams(pars6);

            PlanCalcScoreConfigGroup.ModeParams pars7 = new PlanCalcScoreConfigGroup.ModeParams("motorbike");
            pars7.setConstant(-2);
            pars7.setMarginalUtilityOfTraveling(-0.802);
            pars7.setMonetaryDistanceRate(-0.006);
            params2.addModeParams(pars7);


            PlanCalcScoreConfigGroup.ModeParams pars8 = new PlanCalcScoreConfigGroup.ModeParams( "pt" );
            pars8.setConstant(3);
            pars8.setMarginalUtilityOfTraveling(-0.802);
            pars8.setMonetaryDistanceRate(-0.00152);
            params2.addModeParams( pars8 );


        }

        PlanCalcScoreConfigGroup.ScoringParameterSet params3 = config.planCalcScore().getOrCreateScoringParameters( "default" );

        {


            PlanCalcScoreConfigGroup.ActivityParams actParams1 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams1.setActivityType( "home" );
            actParams1.setTypicalDuration(12);
            actParams1.setMinimalDuration(8);
            params3.addActivityParams( actParams1 );

            PlanCalcScoreConfigGroup.ActivityParams actParams2 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams2.setActivityType( "work" );
            actParams2.setTypicalDuration(8);
            actParams2.setMinimalDuration(1);
            actParams2.setOpeningTime(7*3600);
            actParams2.setClosingTime(22*3600);
            params3.addActivityParams( actParams2 );

            PlanCalcScoreConfigGroup.ActivityParams actParams3 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams3.setActivityType( "education" );
            actParams3.setTypicalDuration(5);
            actParams3.setOpeningTime(8*3600);
            actParams3.setClosingTime(20*3600);
            params3.addActivityParams( actParams3 );

            PlanCalcScoreConfigGroup.ActivityParams actParams4 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams4.setActivityType( "shopping" );
            actParams4.setTypicalDuration(1);
            actParams4.setOpeningTime(8*3600);
            actParams4.setClosingTime(22*3600);
            params3.addActivityParams( actParams4 );

            PlanCalcScoreConfigGroup.ActivityParams actParams5 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams5.setActivityType( "personal" );
            actParams5.setTypicalDuration(1);
            actParams5.setOpeningTime(0);
            actParams5.setClosingTime(24*3600);
            params3.addActivityParams( actParams5 );

            PlanCalcScoreConfigGroup.ActivityParams actParams6 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams6.setActivityType( "leisure" );
            actParams6.setTypicalDuration(1);
            actParams6.setOpeningTime(7*3600);
            actParams6.setClosingTime(23*3600);
            params3.addActivityParams( actParams6 );

            PlanCalcScoreConfigGroup.ActivityParams actParams7 = new PlanCalcScoreConfigGroup.ActivityParams();
            actParams7.setActivityType( "other" );
            actParams7.setTypicalDuration(1);
            actParams7.setOpeningTime(0);
            actParams7.setClosingTime(24*3600);
            params3.addActivityParams( actParams7 );
            params3.setLateArrival_utils_hr(-18);
            params3.setEarlyDeparture_utils_hr(0);
            params3.setPerforming_utils_hr(6);
            params3.setMarginalUtlOfWaitingPt_utils_hr(0.0);
            params3.setMarginalUtlOfWaiting_utils_hr(0);
            params3.setMarginalUtilityOfMoney(0.002);



        }
        {
            PlanCalcScoreConfigGroup.ModeParams pars = new PlanCalcScoreConfigGroup.ModeParams("bus");
            pars.setConstant(3);
            pars.setMarginalUtilityOfTraveling(-0.802);
            pars.setMonetaryDistanceRate(-0.00152);
            params3.addModeParams(pars);

            PlanCalcScoreConfigGroup.ModeParams pars1 = new PlanCalcScoreConfigGroup.ModeParams("rickshaw");
            pars1.setConstant(2);
            pars1.setMarginalUtilityOfTraveling(-0.802);
            pars1.setMonetaryDistanceRate(-0.01);
            params3.addModeParams(pars1);

            PlanCalcScoreConfigGroup.ModeParams pars2 = new PlanCalcScoreConfigGroup.ModeParams("hh");
            pars2.setConstant(2);
            pars2.setMarginalUtilityOfTraveling(-0.802);
            pars2.setMonetaryDistanceRate(-0.002);
            params3.addModeParams(pars2);

            PlanCalcScoreConfigGroup.ModeParams pars3 = new PlanCalcScoreConfigGroup.ModeParams("cng");
            pars3.setConstant(-1.5);
            pars3.setMarginalUtilityOfTraveling(-0.802);
            pars3.setMonetaryDistanceRate(-0.006);
            params3.addModeParams(pars3);

            PlanCalcScoreConfigGroup.ModeParams pars4 = new PlanCalcScoreConfigGroup.ModeParams("walk");
            pars4.setConstant(0);
            pars4.setMarginalUtilityOfDistance(-0.00012527);
            pars4.setMarginalUtilityOfTraveling(-0.802);
            params3.addModeParams(pars4);

            PlanCalcScoreConfigGroup.ModeParams pars5 = new PlanCalcScoreConfigGroup.ModeParams("car");
            pars5.setConstant(0.5);//arc 4
            pars5.setMarginalUtilityOfTraveling(-0.802);
            pars5.setMonetaryDistanceRate(-0.008);
            params3.addModeParams(pars5);

            PlanCalcScoreConfigGroup.ModeParams pars6 = new PlanCalcScoreConfigGroup.ModeParams("bike");
            pars6.setConstant(0);
            pars6.setMarginalUtilityOfTraveling(-0.802);
            pars6.setMarginalUtilityOfDistance(-0.00007604);
            params3.addModeParams(pars6);

            PlanCalcScoreConfigGroup.ModeParams pars7 = new PlanCalcScoreConfigGroup.ModeParams("motorbike");
            pars7.setConstant(-2);
            pars7.setMarginalUtilityOfTraveling(-0.802);
            pars7.setMonetaryDistanceRate(-0.006);
            params3.addModeParams(pars7);


            PlanCalcScoreConfigGroup.ModeParams pars8 = new PlanCalcScoreConfigGroup.ModeParams( "pt" );
            pars8.setConstant(3);
            pars8.setMarginalUtilityOfTraveling(-0.802);
            pars8.setMonetaryDistanceRate(-0.00152);
            params3.addModeParams( pars8 );


        }

        config.plansCalcRoute().setAccessEgressType(PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLink);

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
            if (link.getFreespeed()<10){
                Set allowedModes3 = new HashSet(link.getAllowedModes());
                allowedModes3.add("rickshaw");
                link.setAllowedModes( allowedModes3 );
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