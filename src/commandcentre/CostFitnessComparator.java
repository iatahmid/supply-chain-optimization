package commandcentre;

import java.util.Comparator;

import nsga2.DeliveryPlan;

public class CostFitnessComparator implements Comparator<DeliveryPlan> {

	@Override
	public int compare(DeliveryPlan planA, DeliveryPlan planB) {
		
		// DESCENDING
		
		float aVal = planA.getCostFitness();
		float bVal = planB.getCostFitness();
		
		if(aVal > bVal) {
			return -1;
		} else if(aVal < bVal) {
			return 1;
		}
		
		return 0;
	}

}
