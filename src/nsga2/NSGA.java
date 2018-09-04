/**
 * 
 */
package nsga2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import commandcentre.CostFitnessComparator;
import commandcentre.CrowdingDistanceComparator;
import commandcentre.TimeFitnessComparator;
import commandcentre.Z1FitnessComparator;
import commandcentre.Z2FitnessComparator;
import utility.Config;

/**
 * @author Tahmid
 *
 */
public class NSGA {
	
	Random random = new Random();
	
	// Genetic Operator
	private float mutationRate = Config.mutationRate;
	private float crossoverRate = Config.crossoverRate;
//	private int tournamentSize = 5;
	
	//
	public ArrayList<ArrayList<DeliveryPlan>> nonDominatedSort(ArrayList<DeliveryPlan> plans) {
		
		ArrayList<ArrayList<DeliveryPlan>> dominationFronts = new ArrayList<ArrayList<DeliveryPlan>>();
		
//		System.out.print(" NSGAPopSize: " + plans.size());
		
//		int count = 0;
		for (int p = 0; p < plans.size(); p++) {
			for (int q = 0; q < plans.size(); q++) {
				if (p == q) {
					continue;
				}

				if (plans.get(p).dominates(plans.get(q))) {
					plans.get(p).addToDominatedList(plans.get(q));
					plans.get(q).setRank(0);
				} else if (plans.get(q).dominates(plans.get(p))) {
					plans.get(p).incrementDominatorCount();
				}
			}
			
			if(plans.get(p).getDominatorCount() == 0) {
				if(dominationFronts.isEmpty()) {
					dominationFronts.add(new ArrayList<DeliveryPlan>());
				}
				
				dominationFronts.get(0).add(plans.get(p));
				plans.get(p).setRank(0);
				
//				count++;
			}
		}
		
		int i = 1;
		while(dominationFronts.size() == i) {
			ArrayList<DeliveryPlan> nextFrontSet = new ArrayList<DeliveryPlan>();
			for (DeliveryPlan planP : dominationFronts.get(i-1)) {
				for (DeliveryPlan planQ : planP.getDominatedList()) {
					planQ.decrementDominatorCount();
					if(planQ.getDominatorCount() == 0) {
						planQ.setRank(i);
						nextFrontSet.add(planQ);
						
//						count++;
					}
				}
			}
			
			i++;
			if(!nextFrontSet.isEmpty()) {
				dominationFronts.add(nextFrontSet);
			}
		}
		
//		System.out.print(" Inner Count: " + count);
		
		return dominationFronts;
	}
	
	public ArrayList<DeliveryPlan> sortByCrowdingDistance(ArrayList<DeliveryPlan> plans) {
		
//		System.out.print(" CDPopSize: " + plans.size());
		
		for (DeliveryPlan plan : plans) {
			plan.crowdingDistance = 0;
		}
		
		int last = plans.size() - 1;
		
		// OBJECTIVE FUNCTION - Z1
		Collections.sort(plans, new Z1FitnessComparator());
		
		plans.get(0).crowdingDistance = Double.POSITIVE_INFINITY;
		plans.get(last).crowdingDistance = Math.max(plans.get(last).crowdingDistance, Double.MAX_VALUE);
		
		if(plans.get(0).getZ1Fitness() != plans.get(last).getZ1Fitness()) {
			double range = plans.get(last).getZ1Fitness() - plans.get(0).getZ1Fitness();
			for (int i = 1; i < last; i++) {
				plans.get(i).crowdingDistance += (plans.get(i+1).getZ1Fitness() - plans.get(i-1).getZ1Fitness()) / range;
			}
		}
		
		// OBJECTIVE FUNCTION - Z2
		Collections.sort(plans, new Z2FitnessComparator());

		plans.get(0).crowdingDistance = Double.POSITIVE_INFINITY;
		plans.get(last).crowdingDistance = Math.max(plans.get(last).crowdingDistance, Double.MAX_VALUE);

		if(plans.get(0).getZ2Fitness() != plans.get(last).getZ2Fitness()) {
			double range = plans.get(last).getZ2Fitness() - plans.get(0).getZ2Fitness();
			for (int i = 1; i < last; i++) {
				plans.get(i).crowdingDistance += (plans.get(i+1).getZ2Fitness() - plans.get(i-1).getZ2Fitness()) / range;
			}
		}
		
		// OBJECTIVE FUNCTION - COST
//		Collections.sort(plans, new CostFitnessComparator());
//		
//		plans.get(0).crowdingDistance = Double.POSITIVE_INFINITY;
//		plans.get(last).crowdingDistance = Math.max(plans.get(last).crowdingDistance, Double.MAX_VALUE);
//		
//		if(plans.get(0).getCostFitness() != plans.get(last).getCostFitness()) {
//			double range = plans.get(last).getCostFitness() - plans.get(0).getCostFitness();
//			for (int i = 1; i < last; i++) {
//				plans.get(i).crowdingDistance += (plans.get(i+1).getCostFitness() - plans.get(i-1).getCostFitness()) / range;
//			}
//		}
		
		// OBJECTIVE FUNCTION - TIME
//		Collections.sort(plans, new TimeFitnessComparator());
//
//		plans.get(0).crowdingDistance = Double.POSITIVE_INFINITY;
//		plans.get(last).crowdingDistance = Math.max(plans.get(last).crowdingDistance, Double.MAX_VALUE);
//
//		if(plans.get(0).getTimeFitness() != plans.get(last).getTimeFitness()) {
//			double range = plans.get(last).getTimeFitness() - plans.get(0).getTimeFitness();
//			for (int i = 1; i < last; i++) {
//				plans.get(i).crowdingDistance += (plans.get(i+1).getTimeFitness() - plans.get(i-1).getTimeFitness()) / range;
//			}
//		}
		
		// ULTIMATE SORT
		Collections.sort(plans, new CrowdingDistanceComparator());
		
		return plans;
	}
	
	// 
	public Population evolvePopulation(Population pop) {
		
		Population nextGeneration = new Population();
//		System.out.print(" Pop Size: " + pop.populationSize());
		
		// Prepare the offsprings
		for (int i = 0; i < pop.populationSize(); i++) {
			
			// Choosing parents on Binary Tournament Selection
			DeliveryPlan parent1 = binaryTournamentSelection(pop);
			DeliveryPlan parent2 = binaryTournamentSelection(pop);
			
			// Crossover
			DeliveryPlan child = crossover(parent1, parent2);
			
			// Mutation
			child = mutate(child);
			
			// Popularize the next generation
			nextGeneration.addPlan(child);
			
//			child.print(0);
		}
		
		// Include the previous generation
		for (int i = 0; i < pop.populationSize(); i++) {
			nextGeneration.addPlan(pop.getPlan(i));
			
//			pop.getPlan(i).print(0);
		}
		
//		System.out.print(" NextGen Size: " + nextGeneration.populationSize());
		// Non dominated sorting
		for (int i = 0; i < nextGeneration.populationSize(); i++) {
			nextGeneration.getPlan(i).rank = 0;
			nextGeneration.getPlan(i).crowdingDistance = 0;
			nextGeneration.getPlan(i).nDominatorCount = 0;
			nextGeneration.getPlan(i).sDominatedList = new ArrayList<DeliveryPlan>();
		}
		
		ArrayList<ArrayList<DeliveryPlan>> dominationFronts = nonDominatedSort(nextGeneration.getPlans());
		
//		int count = 0;
//		for (ArrayList<DeliveryPlan> arrayList : dominationFronts) {
////			for (DeliveryPlan deliveryPlan : arrayList) {
////				count++;
////			}
//			count += arrayList.size();
//		}
//		System.out.print(" FrontCount: " + count);
		
		Population newPop = new Population();
		int remainingPopulation = pop.populationSize();
		boolean finished = false;
		for (ArrayList<DeliveryPlan> front : dominationFronts) {
			
//			if(front.size() <= remainingPopulation) {
//				for (DeliveryPlan plan : front) {
//					newPop.addPlan(plan);
//					remainingPopulation--;
//				}
//			} else if(front.size() > remainingPopulation) {
//				sortByCrowdingDistance(front);
//				for (int i = 0; i < remainingPopulation; i++) {
//					newPop.addPlan(front.get(i));
//					remainingPopulation--;
//				}
//			} else if(remainingPopulation == 0) {
//				break;
//			}
			
			
			ArrayList<DeliveryPlan> sortedFront = sortByCrowdingDistance(front);
			for (DeliveryPlan deliveryPlan : sortedFront) {
				if(remainingPopulation <= 0) {
					finished = true;
					break;
				}
				newPop.addPlan(deliveryPlan);
				remainingPopulation--;
			}
			if(finished) {
				break;
			}
		}
		
//		System.out.println(" NewPop Size: " + newPop.populationSize());
		// Return the top N plans
		return newPop;
	}

	// 
	public DeliveryPlan crossover(DeliveryPlan parent1, DeliveryPlan parent2) {
		
		DeliveryPlan child = new DeliveryPlan(parent1);
		
		if(random.nextFloat() < crossoverRate) {
			for (int k = 0; k < Config.K; k++) {
				for (int t = 0; t < Config.T; t++) {
					for (int m = 0; m < Config.M; m++) {
						
						int temp = parent1.productionQuantity_Q[m][t][k];
						child.productionQuantity_Q[m][t][k] = parent2.productionQuantity_Q[m][t][k];
						parent2.productionQuantity_Q[m][t][k] = temp;	

					}
				}
			}
			child.normalize();	
		}
		
		return child;
	}
	
	// 
	public DeliveryPlan mutate(DeliveryPlan plan) {
		
		int prev = plan.productionQuantity_Q[1][1][1];
//		int prev = plan.productionQuantity_Q[0][0][0];
		
		if(random.nextFloat() < mutationRate) {
			for (int k = 0; k < Config.K; k++) {
				for (int t = 0; t < Config.T; t++) {
					for (int m = 0; m < Config.M; m++) {
						int temp = plan.productionQuantity_Q[m][t][k]; 
						plan.productionQuantity_Q[m][t][k] = prev;
						prev = temp;						
					}
				}
			}
			plan.normalize();
		}
		
		return plan;
	}
	
	// 
	public DeliveryPlan tournamentSelection(Population pop, int tournamentSize) {
		// Create a tournament population
        Population tournament = new Population(tournamentSize);
        
        // For each place in the tournament get a random candidate plan and
        // add it
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.populationSize());
            tournament.addPlan(pop.getPlan(randomId));;
        }
     
        // Get the fittest plan
        DeliveryPlan best = tournament.getPlan(0);
        for (int i = 0; i < tournamentSize; i++) {
			if(tournament.getPlan(i).isBetterThan(best)) {
				best = tournament.getPlan(i);
			}
		}
        
        return best;
	}
	
	// 
	public DeliveryPlan binaryTournamentSelection(Population pop) {
		
		int randomIdx = (int)(Math.random() * pop.populationSize());
		DeliveryPlan plan1 = pop.getPlan(randomIdx);
		
//		System.out.println(randomIdx);
		
		randomIdx = (int)(Math.random() * pop.populationSize());
		DeliveryPlan plan2 = pop.getPlan(randomIdx);
		
//		System.out.println(randomIdx);
		
		if(plan1.isBetterThan(plan2)) {
			return plan1;
		} else {
			return plan2;
		}
	}
}
