package commandcentre;

import java.util.Comparator;

import nsga2.DeliveryPlan;

public class Z2FitnessComparator implements Comparator<DeliveryPlan> {

	@Override
	public int compare(DeliveryPlan planA, DeliveryPlan planB) {

		// DESCENDING

		float aVal = planA.getZ2Fitness();
		float bVal = planB.getZ2Fitness();

		if(aVal > bVal) {
			return -1;
		} else if(aVal < bVal) {
			return 1;
		}

		return 0;
	}

}
