package edu.kit.ifv.mobitopp.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.controler.Controler;

import edu.kit.ifv.mobitopp.matsim.ExternalDemandCreator;
import edu.kit.ifv.mobitopp.matsim.MatsimPersonCreator;
import edu.kit.ifv.mobitopp.matsim.MatsimPlanCreator;

public class Matsim {

	private final SimulationContext context;
	private final Scenario scenario;
	private final Network network;
	private final Population population;

	public Matsim(SimulationContext context, Scenario scenario) {
		this.context = context;
		this.scenario = scenario;
		this.network = scenario.getNetwork();
		this.population = scenario.getPopulation();
	}

	public void createPersons() {
		System.out.println("Create demand for matsim");
		createInternalDemand();
		createExternalDemand();
	}

	private void createInternalDemand() {
		System.out.println("Create internal demand for matsim");
		List<Person> persons = persons();
		System.out.println(persons.size() + " persons simulated in mobiTopp");
		MatsimPersonCreator creator = new MatsimPersonCreator(population);
		List<org.matsim.api.core.v01.population.Person> matsim = creator.createPersons(persons);
		System.out.println(matsim.size() + " matsim persons created.");
	}

	private void createExternalDemand() {
		ExternalDemandCreator demandCreator = new ExternalDemandCreator(context);
		demandCreator.addDemandTo(population);
	}

	private List<Person> persons() {
		List<Person> persons = new ArrayList<Person>();
		Collection<Integer> ids = context.personLoader().getPersonOids();
		for (Integer id : ids) {
			Person p = context.personLoader().getPersonByOid(id);
			persons.add(p);
		}
		return persons;
	}

	public void createPlans() {
		System.out.println("Create plans for matsim persons");
		List<Person> persons = persons();
		MatsimPlanCreator creator = new MatsimPlanCreator(population, network);
		creator.createPlansForPersons(persons);
	}

	public void simulate() {
		new Controler(scenario).run();
	}

}
