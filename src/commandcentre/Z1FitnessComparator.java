package commandcentre;

import java.util.Comparator;

import nsga2.DeliveryPlan;

public class Z1FitnessComparator implements Comparator<DeliveryPlan> {

	@Override
	public int compare(DeliveryPlan planA, DeliveryPlan planB) {

		// DESCENDING

		float aVal = planA.getZ1Fitness();
		float bVal = planB.getZ1Fitness();

		if(aVal > bVal) {
			return -1;
		} else if(aVal < bVal) {
			return 1;
		}

		return 0;
	}

}
