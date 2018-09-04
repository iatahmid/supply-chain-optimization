package commandcentre;

import java.util.Comparator;

import nsga2.DeliveryPlan;

public class TimeFitnessComparator implements Comparator<DeliveryPlan> {

	@Override
	public int compare(DeliveryPlan planA, DeliveryPlan planB) {
		
		// DESCENDING
		
		float aVal = planA.getTimeFitness();
		float bVal = planB.getTimeFitness();
		
		if(aVal > bVal) {
			return -1;
		} else if(aVal < bVal) {
			return 1;
		}
		
		return 0;
	}
	
}
