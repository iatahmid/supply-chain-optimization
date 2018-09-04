/**
 * 
 */
package nsga2;

import java.util.ArrayList;
import commandcentre.Reader;
import commandcentre.Support;
import layers.Customer;
import layers.DistributionCentre;
import layers.ManufacturingPlant;
import layers.Shoe;
import layers.Supplier;
import utility.Config;

/**
 * @author Tahmid
 *
 */

/**
 * This will work as the individual in our Genetic Algorithm
 *
 */
public class DeliveryPlan {
	
	// NSGA Variables
	public ArrayList<DeliveryPlan> sDominatedList = new ArrayList<DeliveryPlan>();
	public int nDominatorCount = 0;
	public int rank = 0;
	public double crowdingDistance = 0;
	
	// Decision Variables
	
	// Supplier
	private int[][][] productQuantity_W = new int[Config.M][Config.L][Config.T];
	
	// Manufacturing Plant
	private int[][][][] productTransportQuantity_U = new int[Config.J][Config.M][Config.T][Config.K]; // Pair
	private int[][][] isSelected_Z = new int[Config.M][Config.T][Config.K]; // 1-0
	public int[][][] productionQuantity_Q = new int[Config.M][Config.T][Config.K]; // Pair
	private int[][][] inventoryAtEndOfPeriodPlant_I = new int[Config.M][Config.T][Config.K]; // Pair
	
	private float[] availableCapacityPlant = ManufacturingPlant.availableCapacity_WM; // M
	private float[][][] transportCapacityPlant = ManufacturingPlant.transportCapacity_R; // MTK
	private float[][] availableProductionTimePlant = ManufacturingPlant.availableProductionTimePerPeriod_TT; // MT
	
	// Distribution Center
	private int[] isSelected_Y = new int[Config.J];
	private int[][][] inventoryAtEndOfPeriodDC = new int[Config.J][Config.T][Config.K];
	private int[][][][] transportQuantity_X = new int[Config.I][Config.J][Config.T][Config.K];
	
	private int[][][] occupiedSpaceOfWarehouse = new int[Config.J][Config.T][Config.K];
	private int[] capacityOfWarehouse_WD = DistributionCentre.capacityOfWarehouse_WD; // J
	
	// Customer
	private int[][][] backorderQuantity_S = new int[Config.I][Config.T][Config.K];
	private int[][][] receivedQuantityByCustomer = new int[Config.I][Config.T][Config.K];
	
	public DeliveryPlan() {
		// DC
		for (int j = 0; j < Config.J; j++) {
			for (int t = 0; t < Config.T; t++) {
				for (int k = 0; k < Config.K; k++) {
					occupiedSpaceOfWarehouse[j][t][k] = 0;
				}
			}
		
		}
		
		// Customer
		for (int i = 0; i < Config.I; i++) {
			for (int t = 0; t < Config.T; t++) {
				for (int k = 0; k < Config.K; k++) {
					receivedQuantityByCustomer[i][t][k] = 0;
				}
			}
		}
	}
	
	public DeliveryPlan(DeliveryPlan plan) {
		// Supplier
		this.productQuantity_W = plan.productQuantity_W;
		
		// Manufacturing Plant
		this.productTransportQuantity_U = plan.productTransportQuantity_U; // Pair
		this.isSelected_Z = plan.isSelected_Z; // 1-0
		this.productionQuantity_Q = plan.productionQuantity_Q; // Pair
		this.inventoryAtEndOfPeriodPlant_I = plan.inventoryAtEndOfPeriodPlant_I; // Pair
		
		this.availableCapacityPlant = plan.availableCapacityPlant; // M
		this.transportCapacityPlant = plan.transportCapacityPlant; // MTK
		this.availableProductionTimePlant = plan.availableProductionTimePlant; // MT
		
		// Distribution Center
		this.isSelected_Y = plan.isSelected_Y;
		this.inventoryAtEndOfPeriodDC = plan.inventoryAtEndOfPeriodDC;
		this.transportQuantity_X = plan.transportQuantity_X;
		
		this.occupiedSpaceOfWarehouse = plan.occupiedSpaceOfWarehouse;
		this.capacityOfWarehouse_WD = plan.capacityOfWarehouse_WD ; // J
		
		// Customer
		this.backorderQuantity_S = plan.backorderQuantity_S;
		this.receivedQuantityByCustomer = plan.receivedQuantityByCustomer;
	}
	
	
	
	public void initialize() {
		
		availableProductionTimePlant = Reader.readTT();
		
//		System.out.println("Supplier -> Plant");
		
		// Ensure that all the Suppliers meet the demand (Total Component WL) for each Plant.
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					int componentRequired = (int)ManufacturingPlant.componentRequired_WL[m][t];
					int[] splitSupply = Support.splitValue(Config.L, componentRequired);
					for (int l = 0; l < Config.L; l++) {
						productQuantity_W[m][l][t] = splitSupply[l]; 
					}
				}
			}
		}
		// DONE
		
		// FOR TIME CONSTRAINT CHECK
//		availableProductionTimePlant = Reader.readTT();
		float[][] usedTime = new float[Config.M][Config.T];
		for (int t = 0; t < Config.T; t++) {
			for (int m = 0; m < Config.M; m++) {
				usedTime[m][t] = 0;
			}
		}
		
		float[][] compoenentAvailableInPlant = Reader.readWL();
//		System.out.println("Plant: Making shoes.");
		// Ensure that plants produce shoes according to customer demand
		for (int k = 0; k < Config.K; k++) {
//			int shoeDemand = (int)Customer.getStochasticDemandForShoe_DC(k);
			for (int t = 0; t < Config.T; t++) {
				int timeDemand = (int)Customer.getStochasticDemandForPeriod_DC(t, k);
				int[] productionProposal = Support.splitValue(Config.M, 2*timeDemand);
				
//				System.out.println("TimeDemand: " + timeDemand);
//				for (int p = 0; p < productionProposal.length; p++) {
//					System.out.println(productionProposal[p]);
//				}
				
				// How many shoes with the leather available
//				for (int i = 0; i < productionProposal.length; i++) {
//					productionProposal[i] /= Shoe.size[k];
//				}
				
				int prevRemaining = 0;
				// Calculating production quantity
				for (int m = 0; m < Config.M; m++) {
					
					float currentCapacity = availableCapacityPlant[m];
					productionProposal[m] += prevRemaining;
					// Checking production capacity constraint
					if(productionProposal[m] * Shoe.volume[k] > currentCapacity) {
						prevRemaining = (int) Shoe.getQuantityFromVolume(k, (productionProposal[m] * Shoe.volume[k] - currentCapacity));
						productionProposal[m] = (int) Shoe.getQuantityFromVolume(k, currentCapacity);	
					}
					
					// Checking production time constraint
					float productionTime = ManufacturingPlant.getProdutionTimeFromQuantity(m, k, productionProposal[m]);
//					float setupTime = ManufacturingPlant.getSetupTimeFromQuantity(m, k, productionProposal[m]);
//					float totalTime = setupTime+productionTime;
					usedTime[m][t] += productionTime;
					
//					if(totalTime > availableProductionTimePlant[m][t]) {
////						prevRemaining += ManufacturingPlant.getQuantityFromTotalTime(m, k, totalTime-availableProductionTimePlant[m][t]);
//						int prevProposal = productionProposal[m];
//						
//						totalTime = availableProductionTimePlant[m][t];
//						productionProposal[m] = (int) ManufacturingPlant.getQuantityFromTotalTime(m, k, totalTime);
//						
//						prevRemaining += (prevProposal-productionProposal[m]);
//					}
					
					if(productionProposal[m] > Shoe.getShoeQuantityFromLeatherArea(k, compoenentAvailableInPlant[m][t])) {
						int prevProposal = productionProposal[m];
						productionProposal[m] = (int) Shoe.getShoeQuantityFromLeatherArea(k, compoenentAvailableInPlant[m][t]);
						prevRemaining = prevProposal - productionProposal[m];
					}
					
					// Updating production quantity Q by plant
					if(productionProposal[m] > 0) {
						isSelected_Z[m][t][k] = 1;
						productionQuantity_Q[m][t][k] = productionProposal[m];
						
//						availableProductionTimePlant[m][t] -= totalTime;
						currentCapacity -= productionProposal[m];
						compoenentAvailableInPlant[m][t] -= Shoe.getLeatherAreaFromShoeQuantity(k, productionProposal[m]);
					} else {
						isSelected_Z[m][t][k] = 0;
						productionQuantity_Q[m][t][k] = 0;
					}
				}
			}
		}
		
		// CHECKING PRODUCTION TIME CONSTRAINT
		for (int t = 0; t < Config.T; t++) {
			for (int m = 0; m < Config.M; m++) {
				if(usedTime[m][t] > availableProductionTimePlant[m][t]) {
					float extraTime = usedTime[m][t] - availableProductionTimePlant[m][t];
					float[] deductProposal = Support.splitValue(Config.K, extraTime);
					for (int k = 0; k < Config.K; k++) {
						productionQuantity_Q[m][t][k] -= ManufacturingPlant.getQuantityFromProductionTime(m, k, deductProposal[k]);
					}
				}
			}
		}
		
//		for (int t = 0; t < Config.T; t++) {
//			for (int k = 0; k < Config.K; k++) {
//				int q = 0;
//				for (int m = 0; m < Config.M; m++) {
//					q += productionQuantity_Q[m][t][k];
//				}
//				System.out.println(q);
//				System.out.println((int)Customer.getStochasticDemandForPeriod_DC(t, k));
//			}
//		}
		
//		System.out.println("Plant -> DC");
		// Ensure that the shoes produced in the plants reach the Distribution Centers
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					int transportableQuantity = productionQuantity_Q[m][t][k];
					if(t > 0) {
						transportableQuantity += inventoryAtEndOfPeriodPlant_I[m][t-1][k];
					}
					int[] transportProposal = Support.splitValue(Config.J, transportableQuantity);
					
//					System.out.println("Transportable Quantity: " + transportableQuantity + " k: " + k + " m: " + m + " t: " + t);
					
					int prevRemaining = 0;
					int currentProductQuantity = productionQuantity_Q[m][t][k];
					for (int j = 0; j < Config.J; j++) {
						transportProposal[j] += prevRemaining;
						
						// Checking Transport Capacity Constraint
						if(transportProposal[j] > transportCapacityPlant[m][t][k]) {
							prevRemaining = (int) (transportProposal[j] - transportCapacityPlant[m][t][k]);
							transportProposal[j] = (int) transportCapacityPlant[m][t][k];
						}
						
						// Checking Warehouse Capacity Constraint
						if(Shoe.getVolumeFromQuantity(k, transportProposal[j]) + occupiedSpaceOfWarehouse[j][t][k] > capacityOfWarehouse_WD[j]) {
							int prevProposal = transportProposal[j];
							transportProposal[j] = (int) Shoe.getQuantityFromVolume(k, capacityOfWarehouse_WD[j] - occupiedSpaceOfWarehouse[j][t][k]);
							prevRemaining += prevProposal - transportProposal[j];
						}
						
						// Check if we have actually got the Products to transport
						if(transportProposal[j] > 0 && transportProposal[j] <= currentProductQuantity) {
							// Quantity transported from Plant to DC
							productTransportQuantity_U[j][m][t][k] = transportProposal[j];
							
							// Transported from plant. So decreasing.
							currentProductQuantity -= transportProposal[j];
							
							// Warehouse gets more shoes.
							occupiedSpaceOfWarehouse[j][t][k] += Shoe.getVolumeFromQuantity(k, transportProposal[j]);
						} else {
							productTransportQuantity_U[j][m][t][k] = 0;
						}
					}
					// After transporting to DC. Whatever remains.
					inventoryAtEndOfPeriodPlant_I[m][t][k] = currentProductQuantity;
				}
			}
		}
		
		// Find out which Distribution Centers are being used
		for (int j = 0; j < Config.J; j++) {
			int used = 0;
			for (int t = 0; t < Config.T; t++) {
				for (int k = 0; k < Config.K; k++) {
					used += occupiedSpaceOfWarehouse[j][t][k];
				}
			}
			if(used > 0) {
				isSelected_Y[j] = 1;
			} else {
				isSelected_Y[j] = 0;
			}
		}
		
//		System.out.println("DC -> Customer");
		
		// DC is supplying to Customer
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int j = 0; j < Config.J; j++) {
					int availableShoe = (int) Shoe.getQuantityFromVolume(k, occupiedSpaceOfWarehouse[j][t][k]);
					if(t > 0) {
						availableShoe += inventoryAtEndOfPeriodDC[j][t][k];
					}
					int[] transportProposal = Support.splitValue(Config.I, availableShoe);
					
					int prevRemaining = 0;
					for (int i = 0; i < Config.I; i++) {
						transportProposal[i] += prevRemaining;
						
						if(transportProposal[i] > Shoe.getQuantityFromVolume(k, occupiedSpaceOfWarehouse[j][t][k])) {
							prevRemaining = (int) (transportProposal[i] - Shoe.getQuantityFromVolume(k, occupiedSpaceOfWarehouse[j][t][k]));
							transportProposal[i] = (int) Shoe.getQuantityFromVolume(k, occupiedSpaceOfWarehouse[j][t][k]);
						}
						
						transportQuantity_X[i][j][t][k] = transportProposal[i];
						receivedQuantityByCustomer[i][t][k] += transportProposal[i];
						
						occupiedSpaceOfWarehouse[j][t][k] -= Shoe.getVolumeFromQuantity(k, transportProposal[i]);
					}
					inventoryAtEndOfPeriodDC[j][t][k] = (int) Shoe.getQuantityFromVolume(k, occupiedSpaceOfWarehouse[j][t][k]);
				}
			}
		}
		
		// Back order Quantity
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int i = 0; i < Config.I; i++) {
					if(receivedQuantityByCustomer[i][t][k] < Customer.stochasticDemand_DC[i][t][k]) {
						backorderQuantity_S[i][t][k] = (int) (Customer.stochasticDemand_DC[i][t][k] - receivedQuantityByCustomer[i][t][k]); 
					}
				}
			}
		}
		
//		for (int k = 0; k < Config.K; k++) {
//			for (int t = 0; t < Config.T; t++) {
//				int receivedQuantity = 0;
//				for (int i = 0; i < Config.I; i++) {
//					receivedQuantity += receivedQuantityByCustomer[i][t][k];
//				}
//				
//				int demand = 0;
//				for (int i = 0; i < Config.I; i++) {
//					demand += Customer.stochasticDemand_DC[i][t][k];
//				}
//				
//				System.out.println("Received: " + receivedQuantity);
//				System.out.println("Demand: " + demand);
//			}
//		}
	}
	
	// 
	public float getCost() {
		
		float cost = 0;
		cost += Supplier.getProductCost_CLW(productQuantity_W);
		cost += Supplier.getTransportationCost_DW(productQuantity_W);
		
		cost += ManufacturingPlant.getSetupCost_CaZ(isSelected_Z);
		cost += ManufacturingPlant.getProductionCost_CQ(productionQuantity_Q);
		cost += ManufacturingPlant.getTransportCost_DpU(productTransportQuantity_U);
		cost += ManufacturingPlant.getHoldingCost_HpI(inventoryAtEndOfPeriodPlant_I);
		
		cost += DistributionCentre.getWarehouseCost_FY(isSelected_Y);
		cost += DistributionCentre.getInventoryCost_HdI(inventoryAtEndOfPeriodDC);
		cost += DistributionCentre.getTransportCost_DdX(transportQuantity_X);
		
		cost += Customer.getShortageCost_CS(backorderQuantity_S);
		
		return cost;
	}
	
	public float getMinZ1() {
		float cost = 0;
		cost += DistributionCentre.getWarehouseCost_FY(isSelected_Y);
		cost += ManufacturingPlant.getTransportCost_DpU(productTransportQuantity_U);
		cost += ManufacturingPlant.getSetupCost_CaZ(isSelected_Z);
		cost += DistributionCentre.getInventoryCost_HdI(inventoryAtEndOfPeriodDC);
		
		cost += DistributionCentre.getTransportCost_DdX(transportQuantity_X);
		cost += ManufacturingPlant.getProductionCost_CQ(productionQuantity_Q);
		cost += ManufacturingPlant.getHoldingCost_HpI(inventoryAtEndOfPeriodPlant_I);
		cost += Customer.getShortageCost_CS(backorderQuantity_S);
		
		return cost;
	}
	
	public float getMinZ2() {
		float cost = 0;
		cost += DistributionCentre.getWarehouseCost_sigmaFY(isSelected_Y);
		cost += ManufacturingPlant.getTransportCost_sigmaDpU(productTransportQuantity_U);
		cost += ManufacturingPlant.getSetupCost_sigmaCaZ(isSelected_Z);
		cost += DistributionCentre.getInventoryCost_sigmaHdI(inventoryAtEndOfPeriodDC);
		
		cost += DistributionCentre.getTransportCost_sigmaDdX(transportQuantity_X);
		cost += ManufacturingPlant.getProductionCost_sigmaCQ(productionQuantity_Q);
		cost += ManufacturingPlant.getHoldingCost_sigmaHpI(inventoryAtEndOfPeriodPlant_I);
		cost += Customer.getShortageCost_sigmaCS(backorderQuantity_S);
		
		return cost;
	}
	
	public float getZ1Fitness() {
		return (float) (1.0 / getMinZ1());
	}
	
	public float getZ2Fitness() {
		return (float) (1.0 / getMinZ2());
	}
	
	public float getCostFitness() {
		return 1 / getCost();
	}
	
	public float getTime() {
		
		float time = 0;
		time += ManufacturingPlant.getProdutionTimeOneDay_PT(productionQuantity_Q);
		time += ManufacturingPlant.getSetupTimeOneDay_AZ(isSelected_Z);
		
		return time;
	}
	
	public float getTimeFitness() {
		return 1 / getTime();
	}

//	public float getFitness() {
//		return 1 / (0.6f*getCost() + 0.4f*getTime());
//	}
	
	public boolean isBetterThan(DeliveryPlan plan) {
		if(this.rank < plan.rank) {
			return true;
		} else if(this.rank > plan.rank) {
			return false;
		} else if(this.crowdingDistance > plan.crowdingDistance) {
			return true;
		} else {
			return false;
		}
	}
	
	//
	public boolean dominates(DeliveryPlan plan) {
		if(this.getZ1Fitness() > plan.getZ1Fitness()) {
			return true;
		} else if(this.getZ2Fitness() > plan.getZ2Fitness()) {
			return true;
		} 

		return false;
	}
	
	// 
	public void addToDominatedList(DeliveryPlan plan) {
		sDominatedList.add(plan);
	}
	
	public ArrayList<DeliveryPlan> getDominatedList(){
		return sDominatedList;
	}
	
	public void incrementDominatorCount() {
		nDominatorCount++;
	}
	
	public void decrementDominatorCount() {
		nDominatorCount--;
	}
	
	public int getDominatorCount() {
		return nDominatorCount;
	}
	
	public void setRank(int r) {
		rank = r;
	}
	
	public int getRank() {
		return rank;
	}
	
	public void normalize() {
		
//		for (int m = 0; m < Config.M; m++) {
//			float usedSpace = 0;
////			float usedTime = 0;
//			int totalProduced = 0;
//			for (int t = 0; t < Config.T; t++) {
//				for (int k = 0; k < Config.K; k++) {
//					totalProduced += productionQuantity_Q[m][t][k];
//					usedSpace += totalProduced * Shoe.volume[k];
////					usedTime += ManufacturingPlant.getProdutionTimeFromQuantity(m, k, totalProduced);
////					usedTime += ManufacturingPlant.getSetupTimeFromQuantity(m, k, totalProduced);
//				}
//			}
//			
//			float extraSpace = usedSpace - availableCapacityPlant[m];
//			if(extraSpace > 0) {
//				extraSpace /= (Config.T * Config.K);
//				
//				for (int t = 0; t < Config.T; t++) {
//					float usedTime = 0;
//					for (int k = 0; k < Config.K; k++) {
//						int extraShoe = (int) Shoe.getQuantityFromVolume(k, extraSpace);
//						productionQuantity_Q[m][t][k] -= extraShoe;
//						usedTime += ManufacturingPlant.getProdutionTimeFromQuantity(m, k, productionQuantity_Q[m][t][k]);
//						usedTime += ManufacturingPlant.getSetupTimeFromQuantity(m, k, productionQuantity_Q[m][t][k]);
//					
//						System.out.println(productionQuantity_Q[m][t][k]);
//					}
//					
//					if(usedTime > availableProductionTimePlant[m][t]) {
//						float extraTime = (usedTime - availableProductionTimePlant[m][t]) / Config.K;
//						for (int k = 0; k < Config.K; k++) {
//							int extraShoe = (int) ManufacturingPlant.getQuantityFromTotalTime(m, k, extraTime);
//							productionQuantity_Q[m][t][k] -= extraShoe;
//						
//							System.out.println(productionQuantity_Q[m][t][k]);
//						}
//					}
//				}
//			}
//		}
//		
//		for (int k = 0; k < Config.K; k++) {
//			for (int t = 0; t < Config.T; t++) {
//				for (int m = 0; m < Config.M; m++) {
//					if(productionQuantity_Q[m][t][k] > 0) {
//						isSelected_Z[m][t][k] = 1;
//					} else {
//						productionQuantity_Q[m][t][k] = 0;
//						isSelected_Z[m][t][k] = 0;
//					}
//				}
//			}
//		}
		
		// Reinitialize
		// FOR TIME CONSTRAINT CHECK
//		availableProductionTimePlant = Reader.readTT();
		float[][] usedTime = new float[Config.M][Config.T];
		for (int t = 0; t < Config.T; t++) {
			for (int m = 0; m < Config.M; m++) {
				usedTime[m][t] = 0;
			}
		}
		
		// DC
		for (int j = 0; j < Config.J; j++) {
			for (int t = 0; t < Config.T; t++) {
				for (int k = 0; k < Config.K; k++) {
					occupiedSpaceOfWarehouse[j][t][k] = 0;
				}
			}

		}

		// Customer
		for (int i = 0; i < Config.I; i++) {
			for (int t = 0; t < Config.T; t++) {
				for (int k = 0; k < Config.K; k++) {
					receivedQuantityByCustomer[i][t][k] = 0;
				}
			}
		}
		
		float[][] compoenentAvailableInPlant = Reader.readWL();
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				int[] productionProposal = new int[Config.M];
				for (int m = 0; m < Config.M; m++) {
					productionProposal[m] = productionQuantity_Q[m][t][k];
				}
				
				int prevRemaining = 0;
				// Calculating production quantity
				for (int m = 0; m < Config.M; m++) {
					
					float currentCapacity = availableCapacityPlant[m];
					productionProposal[m] += prevRemaining;
					// Checking production capacity constraint
					if(productionProposal[m] * Shoe.volume[k] > currentCapacity) {
						prevRemaining = (int) Shoe.getQuantityFromVolume(k, (productionProposal[m] * Shoe.volume[k] - currentCapacity));
						productionProposal[m] = (int) Shoe.getQuantityFromVolume(k, currentCapacity);	
					}
					
					// Checking production time constraint
					float productionTime = ManufacturingPlant.getProdutionTimeFromQuantity(m, k, productionProposal[m]);
//					float setupTime = ManufacturingPlant.getSetupTimeFromQuantity(m, k, productionProposal[m]);
//					float totalTime = setupTime+productionTime;
//					if(totalTime > availableProductionTimePlant[m][t]) {
////						prevRemaining += ManufacturingPlant.getQuantityFromTotalTime(m, k, totalTime-availableProductionTimePlant[m][t]);
//						int prevProposal = productionProposal[m];
//						
//						totalTime = availableProductionTimePlant[m][t];
//						productionProposal[m] = (int) ManufacturingPlant.getQuantityFromTotalTime(m, k, totalTime);
//						
//						prevRemaining += (prevProposal-productionProposal[m]);
//					}
					usedTime[m][t] += productionTime;
					
					if(productionProposal[m] > Shoe.getShoeQuantityFromLeatherArea(k, compoenentAvailableInPlant[m][t])) {
						int prevProposal = productionProposal[m];
						productionProposal[m] = (int) Shoe.getShoeQuantityFromLeatherArea(k, compoenentAvailableInPlant[m][t]);
						prevRemaining = prevProposal - productionProposal[m];
					}
					
					// Updating production quantity Q by plant
					if(productionProposal[m] > 0) {
						isSelected_Z[m][t][k] = 1;
						productionQuantity_Q[m][t][k] = productionProposal[m];
						
//						availableProductionTimePlant[m][t] -= totalTime;
						currentCapacity -= productionProposal[m];
						compoenentAvailableInPlant[m][t] -= Shoe.getLeatherAreaFromShoeQuantity(k, productionProposal[m]);
					} else {
						isSelected_Z[m][t][k] = 0;
						productionQuantity_Q[m][t][k] = 0;
					}
				}
			}
		}
		
		for (int t = 0; t < Config.T; t++) {
			for (int m = 0; m < Config.M; m++) {
				if(usedTime[m][t] > availableProductionTimePlant[m][t]) {
					float extraTime = usedTime[m][t] - availableProductionTimePlant[m][t];
					float[] deductProposal = Support.splitValue(Config.K, extraTime);
					for (int k = 0; k < Config.K; k++) {
						productionQuantity_Q[m][t][k] -= ManufacturingPlant.getQuantityFromProductionTime(m, k, deductProposal[k]);
					}
				}
			}
		}
		
		// ***************************************************************************
		// AS PREVIOUS
		// ***************************************************************************
		// Ensure that the shoes produced in the plants reach the Distribution Centers
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					int transportableQuantity = productionQuantity_Q[m][t][k];
					if(t > 0) {
						transportableQuantity += inventoryAtEndOfPeriodPlant_I[m][t-1][k];
					}
					int[] transportProposal = Support.splitValue(Config.J, transportableQuantity);
					
//					System.out.println("Transportable Quantity: " + transportableQuantity + " k: " + k + " m: " + m + " t: " + t);
					
					int prevRemaining = 0;
					int currentProductQuantity = productionQuantity_Q[m][t][k];
					for (int j = 0; j < Config.J; j++) {
						transportProposal[j] += prevRemaining;
						
						// Checking Transport Capacity Constraint
						if(transportProposal[j] > transportCapacityPlant[m][t][k]) {
							prevRemaining = (int) (transportProposal[j] - transportCapacityPlant[m][t][k]);
							transportProposal[j] = (int) transportCapacityPlant[m][t][k];
						}
						
						// Checking Warehouse Capacity Constraint
						if(Shoe.getVolumeFromQuantity(k, transportProposal[j]) + occupiedSpaceOfWarehouse[j][t][k] > capacityOfWarehouse_WD[j]) {
							int prevProposal = transportProposal[j];
							transportProposal[j] = (int) Shoe.getQuantityFromVolume(k, capacityOfWarehouse_WD[j] - occupiedSpaceOfWarehouse[j][t][k]);
							prevRemaining += prevProposal - transportProposal[j];
						}
						
						// Check if we have actually got the Products to transport
						if(transportProposal[j] > 0 && transportProposal[j] <= currentProductQuantity) {
							// Quantity transported from Plant to DC
							productTransportQuantity_U[j][m][t][k] = transportProposal[j];
							
							// Transported from plant. So decreasing.
							currentProductQuantity -= transportProposal[j];
							
							// Warehouse gets more shoes.
							occupiedSpaceOfWarehouse[j][t][k] += Shoe.getVolumeFromQuantity(k, transportProposal[j]);
						} else {
							productTransportQuantity_U[j][m][t][k] = 0;
						}
					}
					// After transporting to DC. Whatever remains.
					inventoryAtEndOfPeriodPlant_I[m][t][k] = currentProductQuantity;
				}
			}
		}
		
		// Find out which Distribution Centers are being used
		for (int j = 0; j < Config.J; j++) {
			int used = 0;
			for (int t = 0; t < Config.T; t++) {
				for (int k = 0; k < Config.K; k++) {
					used += occupiedSpaceOfWarehouse[j][t][k];
				}
			}
			if(used > 0) {
				isSelected_Y[j] = 1;
			} else {
				isSelected_Y[j] = 0;
			}
		}
		
//		System.out.println("DC -> Customer");
		
		// DC is supplying to Customer
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int j = 0; j < Config.J; j++) {
					int availableShoe = (int) Shoe.getQuantityFromVolume(k, occupiedSpaceOfWarehouse[j][t][k]);
					if(t > 0) {
						availableShoe += inventoryAtEndOfPeriodDC[j][t-1][k];
					}
					int[] transportProposal = Support.splitValue(Config.I, availableShoe);
					
					int prevRemaining = 0;
					for (int i = 0; i < Config.I; i++) {
						transportProposal[i] += prevRemaining;
						
						if(transportProposal[i] > Shoe.getQuantityFromVolume(k, occupiedSpaceOfWarehouse[j][t][k])) {
							prevRemaining = (int) (transportProposal[i] - Shoe.getQuantityFromVolume(k, occupiedSpaceOfWarehouse[j][t][k]));
							transportProposal[i] = (int) Shoe.getQuantityFromVolume(k, occupiedSpaceOfWarehouse[j][t][k]);
						}
						
						transportQuantity_X[i][j][t][k] = transportProposal[i];
						receivedQuantityByCustomer[i][t][k] += transportProposal[i];
						
						occupiedSpaceOfWarehouse[j][t][k] -= Shoe.getVolumeFromQuantity(k, transportProposal[i]);
					}
					inventoryAtEndOfPeriodDC[j][t][k] = (int) Shoe.getQuantityFromVolume(k, occupiedSpaceOfWarehouse[j][t][k]);
				}
			}
		}
		
		// Back order Quantity
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int i = 0; i < Config.I; i++) {
					if(receivedQuantityByCustomer[i][t][k] < Customer.stochasticDemand_DC[i][t][k]) {
						backorderQuantity_S[i][t][k] = (int) (Customer.stochasticDemand_DC[i][t][k] - receivedQuantityByCustomer[i][t][k]); 
					}
				}
			}
		}
	}
	
	public void print(int i) {
		
		System.out.printf("%-5d %-20f %-20f %-20f %-20f", i, getCost(), getTime(), getMinZ1(), getMinZ2());
		System.out.println();
//		System.out.println("Cost: " + getCost() + " Time: " + getTime() + " Min Z1: " + getMinZ1() + " Min Z2: " + getMinZ2());
		
	}
	
	public String toString() {
		
//		private float[][] availableProductionTimePlant = ManufacturingPlant.availableProductionTimePerPeriod_TT; // MT
//		
//		private int[][][] occupiedSpaceOfWarehouse = new int[Config.J][Config.T][Config.K];
//		private int[] capacityOfWarehouse_WD = DistributionCentre.capacityOfWarehouse_WD; // J
//		
//		private int[][][] receivedQuantityByCustomer = new int[Config.I][Config.T][Config.K];
		
		String msg = "";
		
		// W
		for (int t = 0; t < Config.T; t++) {
			for (int l = 0; l < Config.L; l++) {
				for (int m = 0; m < Config.M; m++) {
					msg += "W_" + (m+1) + "" + (l+1) + "" + (t+1) + " = " + productQuantity_W[m][l][t] + "\n";
				}
			}
		}
		
		msg += "\n";
		// U
		for (int k = 0; k < Config.K; k++) {
			msg += "k: " + (k+1) + "\n";
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					for (int j = 0; j < Config.J; j++) {
						msg += "U_" + (j+1) + "" + (m+1) + "" + (t+1) + " = " + productTransportQuantity_U[j][m][t][k] + "\n";
					}
				}
			}
		}
		
		msg += "\n";
		// Z
		for (int k = 0; k < Config.K; k++) {
			msg += "k: " + (k+1) + "\n";
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					msg += "Z_" + (m+1) + "" + (t+1) + " = " + isSelected_Z[m][t][k] + "\n";
				}
			}
		}
		
		msg += "\n";
		// Q
		for (int k = 0; k < Config.K; k++) {
			msg += "k: " + (k+1) + "\n";
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					msg += "Q_" + (m+1) + "" + (t+1) + " = " + productionQuantity_Q[m][t][k] + "\n";
				}
			}
		}
		
		msg += "\n";
		// I
		for (int k = 0; k < Config.K; k++) {
			msg += "k: " + (k+1) + "\n";
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					msg += "Im_" + (m+1) + "" + (t+1) + " = " + inventoryAtEndOfPeriodPlant_I[m][t][k] + "\n";
				}
			}
		}
		
		msg += "\n";
		// Y
		for (int j = 0; j < Config.J; j++) {
			msg += "Y_" + (j+1) + " = " + isSelected_Y[j] + "\n";

		}
		
		msg += "\n";
		// Ij
		for (int k = 0; k < Config.K; k++) {
			msg += "k: " + (k+1) + "\n";
			for (int t = 0; t < Config.T; t++) {
				for (int j = 0; j < Config.J; j++) {
					msg += "Ij_" + (j+1) + "" + (t+1) + " = " + inventoryAtEndOfPeriodDC[j][t][k] + "\n";
				}
			}
		}
		
		msg += "\n";
		// X
		for (int k = 0; k < Config.K; k++) {
			msg += "k: " + (k+1) + "\n";
			for (int t = 0; t < Config.T; t++) {
				for (int j = 0; j < Config.J; j++) {
					for (int i = 0; i < Config.I; i++) {
						msg += "X_" + (i+1) + "" + (j+1) + "" + (t+1) + " = " + transportQuantity_X[i][j][t][k] + "\n";
					}
				}
			}
		}
		
		msg += "\n";
		// S
		for (int k = 0; k < Config.K; k++) {
			msg += "k: " + (k+1) + "\n";
			for (int t = 0; t < Config.T; t++) {
				for (int i = 0; i < Config.J; i++) {
					msg += "S_" + (i+1) + "" + (t+1) + " = " + backorderQuantity_S[i][t][k] + "\n";
				}
			}
		}
		
//		msg += "\n";
//		// WM
//		for (int m = 0; m < Config.M; m++) {
//			msg += "WM_" + (m+1) + " = " + availableCapacityPlant[m] + "\n";
//		}
//		
//		msg += "\n";
//		// R
//		for (int k = 0; k < Config.K; k++) {
//			msg += "k: " + (k+1) + "\n";
//			for (int t = 0; t < Config.T; t++) {
//				for (int m = 0; m < Config.M; m++) {
//					msg += "R_" + (m+1) + "" + (t+1) + " = " + transportCapacityPlant[m][t][k] + "\n";
//				}
//			}
//		}
//		
//		msg += "\n";
//		for (int t = 0; t < Config.T; t++) {
//			for (int m = 0; m < Config.M; m++) {
//				msg += "TT_" + (m+1) + "" + (t+1) + " = " + availableProductionTimePlant[m][t] + "\n";
//			}
//		}
		
		msg += "\n";
		msg += "Cost: " + getCost() + "\n";
		msg += "Time: " + getTime() + "\n";
		
		return msg;
	}
}
