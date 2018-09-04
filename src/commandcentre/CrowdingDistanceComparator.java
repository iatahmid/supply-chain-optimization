package commandcentre;

import java.util.Comparator;

import nsga2.DeliveryPlan;

public class CrowdingDistanceComparator implements Comparator<DeliveryPlan> {

	@Override
	public int compare(DeliveryPlan planA, DeliveryPlan planB) {
		
		if(planA.crowdingDistance > planB.crowdingDistance) {
			return -1;
		} else if(planA.crowdingDistance < planB.crowdingDistance) {
			return 1;
		}
	
		return 0;
	}

}
