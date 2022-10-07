package org.matsim.project;

import ch.sbb.matsim.config.SwissRailRaptorConfigGroup;
import ch.sbb.matsim.routing.pt.raptor.SwissRailRaptorModule;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.drt.optimizer.insertion.extensive.ExtensiveInsertionSearchParams;
import org.matsim.contrib.drt.run.*;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.otfvis.OTFVisLiveModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.config.groups.StrategyConfigGroup.StrategySettings;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.replanning.strategies.DefaultPlanStrategiesModule.DefaultSelector;
import org.matsim.core.replanning.strategies.DefaultPlanStrategiesModule.DefaultStrategy;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.collections.CollectionUtils;
import org.matsim.vis.otfvis.OTFVisConfigGroup;

import java.util.HashSet;
import java.util.Set;

public class RunMatsim5 {
//Dhaka scenario with bus and hh as drt mode ans walk and rickshaw as access mode
    public static void main( String[] args ) {

        run( args, true);
    }

    public static void run(String[] args, boolean otfvis){
        Config config;
        if ( args!=null && args.length>=1 ) {
            config = ConfigUtils.loadConfig( args );
        } else {
            config = ConfigUtils.loadConfig( "E:\\Abm_output\\Road_pricing\\Scenario_1\\config.xml" ) ;
            config.controler().setOverwriteFileSetting( OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists );
        }

        Scenario scenario = DrtControlerCreator.createScenarioWithDrtRouteFactory(config);
        ScenarioUtils.loadScenario(scenario);
        config.qsim().setTrafficDynamics(QSimConfigGroup.TrafficDynamics.kinematicWaves);
        config.qsim().setSnapshotStyle(QSimConfigGroup.SnapshotStyle.kinematicWaves);
        config.controler().setLastIteration( 1 );
        config.qsim().setSimStarttimeInterpretation( QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime );
        config.qsim().setInsertingWaitingVehiclesBeforeDrivingVehicles(true);
        config.qsim().setSnapshotStyle(QSimConfigGroup.SnapshotStyle.queue);
        config.plansCalcRoute().setNetworkModes(CollectionUtils.stringToSet( "car,cng,motorbike"));

        {   //teleported mode settings
            PlansCalcRouteConfigGroup.ModeRoutingParams pars1 = new PlansCalcRouteConfigGroup.ModeRoutingParams("rickshaw");
            pars1.setTeleportedModeFreespeedLimit( 10/3.6 );
            pars1.setTeleportedModeFreespeedFactor( 2.00 );
            config.plansCalcRoute().addModeRoutingParams( pars1 );

            PlansCalcRouteConfigGroup.ModeRoutingParams pars4 = new PlansCalcRouteConfigGroup.ModeRoutingParams("walk");
            pars4.setTeleportedModeSpeed( 3/3.6  );
            pars4.setBeelineDistanceFactor(1.4);
            config.plansCalcRoute().addModeRoutingParams( pars4 );

            PlansCalcRouteConfigGroup.ModeRoutingParams pars5 = new PlansCalcRouteConfigGroup.ModeRoutingParams("bike");
            pars5.setTeleportedModeSpeed( 8/3.6  );
            pars5.setBeelineDistanceFactor(1.3);
            config.plansCalcRoute().addModeRoutingParams( pars5 );

            config.plansCalcRoute().setNetworkModes(CollectionUtils.stringToSet( "car,cng,motorbike"));//do I need to include drt mode here?

        }

        @SuppressWarnings("unused")
        DvrpConfigGroup dvrpConfig = ConfigUtils.addOrGetModule( config, DvrpConfigGroup.class );
        MultiModeDrtConfigGroup multiModeDrtCfg = ConfigUtils.addOrGetModule(config, MultiModeDrtConfigGroup.class);
        {
            DrtConfigGroup drtConfig = new DrtConfigGroup();
            drtConfig.setMode("bus");
            drtConfig.setStopDuration(300);
            drtConfig.setMaxWaitTime(900);
            drtConfig.setMaxTravelTimeAlpha(1.3) ;
            drtConfig.setMaxTravelTimeBeta(10. * 60);
            drtConfig.setRejectRequestIfMaxWaitOrTravelTimeViolated(false) ;
            drtConfig.setVehiclesFile("bus.xml");
            drtConfig.setChangeStartLinkToLastLinkInSchedule(true);
            drtConfig.setOperationalScheme(DrtConfigGroup.OperationalScheme.stopbased);
            drtConfig.setTransitStopFile("drtstops.xml");
            drtConfig.addDrtInsertionSearchParams(new ExtensiveInsertionSearchParams());
            multiModeDrtCfg.addParameterSet(drtConfig);
        }
        {
            DrtConfigGroup drtConfig = new DrtConfigGroup();
            drtConfig.setMode("hh");
            drtConfig.setStopDuration(300);
            drtConfig.setMaxWaitTime(900);
            drtConfig.setMaxTravelTimeAlpha(1.3) ;//?
            drtConfig.setMaxTravelTimeBeta(10. * 60);//?
            drtConfig.setRejectRequestIfMaxWaitOrTravelTimeViolated(false) ;
            drtConfig.setVehiclesFile("hh.xml");
            drtConfig.setChangeStartLinkToLastLinkInSchedule(true);
            drtConfig.setOperationalScheme(DrtConfigGroup.OperationalScheme.serviceAreaBased);
            drtConfig.setDrtServiceAreaShapeFile("drt_service_185_utm.shp");
            drtConfig.addDrtInsertionSearchParams(new ExtensiveInsertionSearchParams());
            multiModeDrtCfg.addParameterSet(drtConfig);
        }

        for (DrtConfigGroup drtCfg : multiModeDrtCfg.getModalElements()) {
            DrtConfigs.adjustDrtConfig(drtCfg, config.planCalcScore(), config.plansCalcRoute());
        }
        {
            // clear strategy settings from config file:
            config.strategy().clearStrategySettings();

            // configure mode innovation so that travellers start using drt:
            config.strategy().addStrategySettings( new StrategySettings().setStrategyName( DefaultStrategy.ChangeSingleTripMode ).setWeight( 0.1 ) );
            config.changeMode().setModes(new String[]{"car", "bus", "hh", "cng", "motorbike","walk"});
            // have a "normal" plans choice strategy:
            config.strategy().setFractionOfIterationsToDisableInnovation(0.8);
            config.strategy().addStrategySettings( new StrategySettings().setStrategyName("ReRoute").setWeight( 0.1 ) );//The way I mentioned the reroute weight is it ok?
            config.strategy().addStrategySettings( new StrategySettings().setStrategyName( DefaultSelector.SelectExpBeta ).setWeight( 0.7 ) );

        }
        {
            // add params so that scoring works:
            PlanCalcScoreConfigGroup.ModeParams pars = new PlanCalcScoreConfigGroup.ModeParams("bus");
            pars.setConstant(0);
            pars.setMarginalUtilityOfTraveling(-0.009874);
            pars.setMonetaryDistanceRate(-0.00152);
            config.planCalcScore().addModeParams(pars);


            PlanCalcScoreConfigGroup.ModeParams pars1 = new PlanCalcScoreConfigGroup.ModeParams("rickshaw");
            pars1.setConstant(2.20936);
            pars1.setMarginalUtilityOfTraveling(-0.124778);
            pars1.setMonetaryDistanceRate(-0.01);
            config.planCalcScore().addModeParams(pars1);

            PlanCalcScoreConfigGroup.ModeParams pars2 = new PlanCalcScoreConfigGroup.ModeParams("hh");
            pars2.setConstant(-1.160528);
            pars2.setMarginalUtilityOfTraveling(-0.057504);
            pars2.setMonetaryDistanceRate(-0.002);
            config.planCalcScore().addModeParams(pars2);


            PlanCalcScoreConfigGroup.ModeParams pars3 = new PlanCalcScoreConfigGroup.ModeParams("cng");
            pars3.setConstant(-1.769893);
            pars3.setMarginalUtilityOfTraveling(-0.018020);
            pars3.setMonetaryDistanceRate(-0.006);
            config.planCalcScore().addModeParams(pars3);

            PlanCalcScoreConfigGroup.ModeParams pars4 = new PlanCalcScoreConfigGroup.ModeParams("walk");
            pars4.setConstant(3.37646);
            pars4.setMarginalUtilityOfTraveling(-0.144264);
            pars4.setMonetaryDistanceRate(-0.005);
            config.planCalcScore().addModeParams(pars4);

            PlanCalcScoreConfigGroup.ModeParams pars5 = new PlanCalcScoreConfigGroup.ModeParams("car");
            pars5.setConstant(-2.097424);
            pars5.setMarginalUtilityOfTraveling(-0.020432);
            pars5.setMonetaryDistanceRate(-0.008);
            config.planCalcScore().addModeParams(pars5);

            PlanCalcScoreConfigGroup.ModeParams pars6 = new PlanCalcScoreConfigGroup.ModeParams("bike");
            pars6.setConstant(-1.989341);
            pars6.setMarginalUtilityOfTraveling(-0.066166);
            pars6.setMonetaryDistanceRate(-0.005);
            config.planCalcScore().addModeParams(pars6);

            PlanCalcScoreConfigGroup.ModeParams pars7 = new PlanCalcScoreConfigGroup.ModeParams("motorbike");
            pars7.setConstant(-1.277938);
            pars7.setMarginalUtilityOfTraveling(-0.037805);
            pars7.setMonetaryDistanceRate(-0.006);
            config.planCalcScore().addModeParams(pars7);
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

        SwissRailRaptorConfigGroup.IntermodalAccessEgressParameterSet paramSetRickshaw = new SwissRailRaptorConfigGroup.IntermodalAccessEgressParameterSet();
        paramSetRickshaw.setMode("rickshaw");
        paramSetRickshaw.setInitialSearchRadius(4000);
        paramSetRickshaw.setMaxRadius(6000);
        paramSetRickshaw.setSearchExtensionRadius(0.1);
        srrConfig.addIntermodalAccessEgress(paramSetRickshaw);
        config.addModule(srrConfig);

        for (Link link : scenario.getNetwork().getLinks().values()) {
            if (link.getAllowedModes().contains(TransportMode.car)) {
                // It would be better to set the modes directly in the input network file if not all car roads are open to rickshaw, cng, bus, hh etc.
                Set allowedModes1 = new HashSet(link.getAllowedModes());
                allowedModes1.add("cng");
                allowedModes1.add("motorbike");
                allowedModes1.add("hh");
                link.setAllowedModes(allowedModes1);
            }
            if (link.getFreespeed()>=10){
                Set allowedModes2 = new HashSet(link.getAllowedModes());
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
        controler.addOverridingModule( new DvrpModule() ) ;
        controler.addOverridingModule( new MultiModeDrtModule( ) ) ;


        if (otfvis) {
            OTFVisConfigGroup otfVisConfigGroup = ConfigUtils.addOrGetModule(config, OTFVisConfigGroup.class);
            otfVisConfigGroup.setLinkWidth(5);
            otfVisConfigGroup.setDrawNonMovingItems(true);
            controler.addOverridingModule(new OTFVisLiveModule());
        }

        controler.run() ;
    }

}