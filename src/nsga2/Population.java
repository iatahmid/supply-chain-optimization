/**
 * 
 */
package nsga2;

import java.util.ArrayList;

/**
 * @author Tahmid
 *
 */
public class Population {
	
	// holds our delivery plans
	public ArrayList<DeliveryPlan> plans = new ArrayList<DeliveryPlan>();
	
	// generates a population
	public Population(int popSize) {
		for (int i = 0; i < popSize; i++) {
			DeliveryPlan plan = new DeliveryPlan();
			addPlan(plan);
		}
	}

	public Population() {
		// Open to possibilities.
	}

	public void init() {
		for (DeliveryPlan deliveryPlan : plans) {
			deliveryPlan.initialize();
		}
	}
	
	public void addPlan(DeliveryPlan plan) {
		plans.add(plan);
	}
	
	public DeliveryPlan getPlan(int index) {
		return plans.get(index);
	}
	
	// it may change.
	public DeliveryPlan getFittest() {

		DeliveryPlan fittest = plans.get(0);
		for (int i = 0; i < populationSize(); i++) {
			if(getPlan(i).isBetterThan(fittest)) {
				fittest = getPlan(i); 
			}
		}
		
		return fittest;
	}
	
	// returns the population size.
	public int populationSize() {
		return plans.size();
	}
	
	public ArrayList<DeliveryPlan> getPlans(){
		return plans;
	}
	
}
