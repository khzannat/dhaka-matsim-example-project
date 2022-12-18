/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package org.matsim.project;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.otfvis.OTFVisLiveModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup.SnapshotStyle;
import org.matsim.core.config.groups.QSimConfigGroup.TrafficDynamics;
import org.matsim.core.config.groups.StrategyConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.replanning.strategies.DefaultPlanStrategiesModule;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.collections.CollectionUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author nagel
 *
 */
public class RunMatsim1 {

	public static void main(String[] args) {
		//Mode choice model used estimates from 2019 travel diary survey data
		//Base model


		Config config;
		if (args == null || args.length == 0 || args[0] == null) {
			config = ConfigUtils.loadConfig("./config.xml");
		} else {
			config = ConfigUtils.loadConfig(args);
		}
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);

		config.qsim().setTrafficDynamics(TrafficDynamics.kinematicWaves);
		config.qsim().setSnapshotStyle(SnapshotStyle.kinematicWaves);
		{
			StrategyConfigGroup.StrategySettings stratSets = new StrategyConfigGroup.StrategySettings();
			stratSets.setStrategyName(DefaultPlanStrategiesModule.DefaultStrategy.ChangeSingleTripMode);
			stratSets.setWeight(0.1);
			config.strategy().addStrategySettings(stratSets);
			config.changeMode().setModes(new String[]{"car", "bus", "rickshaw", "hh", "cng", "walk", "bike", "motorbike"});

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
		//car 100km/h, //bike 80km/h //cng60/km/h
		//scoring parameter
		{
			PlanCalcScoreConfigGroup.ModeParams pars = new PlanCalcScoreConfigGroup.ModeParams("bus");
			pars.setConstant(3);
			pars.setMarginalUtilityOfTraveling(-0.802);//in hour
			pars.setMonetaryDistanceRate(-0.00152);//in meter
			config.planCalcScore().addModeParams(pars);

			PlanCalcScoreConfigGroup.ModeParams pars1 = new PlanCalcScoreConfigGroup.ModeParams("rickshaw");
			pars1.setConstant(2);//arc 3 20, arc 4 10
			pars1.setMarginalUtilityOfTraveling(-0.802);
			pars1.setMonetaryDistanceRate(-0.01);
			config.planCalcScore().addModeParams(pars1);

			PlanCalcScoreConfigGroup.ModeParams pars2 = new PlanCalcScoreConfigGroup.ModeParams("hh");
			pars2.setConstant(2);
			pars2.setMarginalUtilityOfTraveling(-0.802);
			pars2.setMonetaryDistanceRate(-0.002);
			config.planCalcScore().addModeParams(pars2);

			PlanCalcScoreConfigGroup.ModeParams pars3 = new PlanCalcScoreConfigGroup.ModeParams("cng");
			pars3.setConstant(-1.5);
			pars3.setMarginalUtilityOfTraveling(-0.802);
			pars3.setMonetaryDistanceRate(-0.006);
			config.planCalcScore().addModeParams(pars3);

			PlanCalcScoreConfigGroup.ModeParams pars4 = new PlanCalcScoreConfigGroup.ModeParams("walk");
			pars4.setConstant(0);
			pars4.setMarginalUtilityOfDistance(-0.00012527);//for arc3 and arc 4 -0.001
			pars4.setMarginalUtilityOfTraveling(-0.802);
			config.planCalcScore().addModeParams(pars4);

			PlanCalcScoreConfigGroup.ModeParams pars5 = new PlanCalcScoreConfigGroup.ModeParams("car");
			pars5.setConstant(0.5);//arc 4
			pars5.setMarginalUtilityOfTraveling(-0.802);
			pars5.setMonetaryDistanceRate(-0.008);
			config.planCalcScore().addModeParams(pars5);

			PlanCalcScoreConfigGroup.ModeParams pars6 = new PlanCalcScoreConfigGroup.ModeParams("bike");
			pars6.setConstant(0);
			pars6.setMarginalUtilityOfTraveling(-0.802);
			pars6.setMarginalUtilityOfDistance(-0.00007604);
			config.planCalcScore().addModeParams(pars6);

			PlanCalcScoreConfigGroup.ModeParams pars7 = new PlanCalcScoreConfigGroup.ModeParams("motorbike");
			pars7.setConstant(-2);
			pars7.setMarginalUtilityOfTraveling(-0.802);
			pars7.setMonetaryDistanceRate(-0.006);
			config.planCalcScore().addModeParams(pars7);
			config.planCalcScore().setMarginalUtilityOfMoney(0.002);//in bdt
			config.plansCalcRoute().setAccessEgressType(PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLink);

		}


		Scenario scenario = ScenarioUtils.loadScenario(config);
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
		Controler controler = new Controler(scenario);

		// possibly modify controler here

		//controler.addOverridingModule( new OTFVisLiveModule() ) ;


		// ---

		controler.run();



	}
}
//INFO MemoryObserver:41 used RAM: 925 MB  free: 742 MB  total: 1668 MB//
