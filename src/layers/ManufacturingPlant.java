/**
 * 
 */
package layers;

import java.util.Random;

import commandcentre.Reader;
import utility.Config;

/**
 * @author Tahmid
 *
 */
public class ManufacturingPlant {

	// Reader
//	private Reader reader;
	
	// Costs
	private static float[][][][] unitTransportCost_DP = Reader.readDP();
	private static int[][][] setupCost_CA = Reader.readCA();
	private static float[][][] unitProductionCost_C = Reader.readC();
	private static float[][][] unitHoldingCost_HP = Reader.readHP();
	
	// Decision Variables
//	private int[][][][] productTransportQuantity_U;
//	private int[][][] isSelected_Z;
//	private int[][][] productionQuantity_Q;
//	private int[][][] inventoryAtEndOfPeriodPlant_I;
	
	// times
	private static float[][] unitProductionTime_PT = Reader.readPT();
	private static float[][] unitSetupTime_A = Reader.readA();
	public static float[][] availableProductionTimePerPeriod_TT = Reader.readTT();

	// constraints
	public static float[] availableCapacity_WM = Reader.readWM();
	public static float[][][] transportCapacity_R = Reader.readR();
	public static float[][] componentRequired_WL = Reader.readWL();
	// cost constraint
	// time constraint
	
//	public ManufacturingPlant() {
////		reader = new Reader(Config.K, Config.T, Config.L, Config.M, Config.J, Config.I);
//		
//		unitTransportCost_DP = Reader.readDP();
//		setupCost_CA = Reader.readCA();
//		unitProductionCost_C = Reader.readC();
//		unitHoldingCost_HP = Reader.readHP();
//		
//		unitProductionTime_PT = Reader.readPT();
//		unitSetupTime_A = Reader.readA();
//		availableProductionTimePerPeriod_TT = Reader.readTT();
//		
//		availableCapacity_WM = Reader.readWM();
//		transportCapacity_R = Reader.readR();
//		
//		productTransportQuantity_U = new int[Config.J][Config.M][Config.T][Config.K];
//		isSelected_Z = new int[Config.M][Config.T][Config.K];
//		productionQuantity_Q = new int[Config.M][Config.T][Config.K];
//		inventoryAtEndOfPeriodPlant_I = new int[Config.M][Config.T][Config.K];
//	}

	// ***********************************************************************
	// COST
	// ***********************************************************************
	
	public static float getTransportCost_DpU(int[][][][] productTransportQuantity_U) {
		
		float transportCost = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					for (int j = 0; j < Config.J; j++) {
						transportCost += unitTransportCost_DP[j][m][t][k] * productTransportQuantity_U[j][m][t][k];
					}
				}
			}
		}
		
		return transportCost;
	}
	
	public static float getTransportCost_sigmaDpU(int[][][][] productTransportQuantity_U) {

		float transportCost = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					for (int j = 0; j < Config.J; j++) {
						transportCost += Math.pow(0.1 * unitTransportCost_DP[j][m][t][k] * productTransportQuantity_U[j][m][t][k], 2);
					}
				}
			}
		}

		return transportCost;
	}
	
	public static int getSetupCost_CaZ(int[][][] isSelected_Z) {
		
		int setupCost = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					setupCost += setupCost_CA[m][t][k] * isSelected_Z[m][t][k];
				}
			}
		}
		
		return setupCost;
	}
	
	public static int getSetupCost_sigmaCaZ(int[][][] isSelected_Z) {

		int setupCost = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					setupCost += Math.pow(0.1 * setupCost_CA[m][t][k] * isSelected_Z[m][t][k], 2);
				}
			}
		}

		return setupCost;
	}
	
	public static float getProductionCost_CQ(int[][][] productionQuantity_Q) {
		
		float productionCost = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					productionCost += unitProductionCost_C[m][t][k] * productionQuantity_Q[m][t][k];
				}
			}
		}
		
		return productionCost;
	}
	
	public static float getProductionCost_sigmaCQ(int[][][] productionQuantity_Q) {

		float productionCost = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					productionCost += Math.pow((0.1 * unitProductionCost_C[m][t][k] * productionQuantity_Q[m][t][k]), 2);
				}
			}
		}

		return productionCost;
	}
	
	public static float getHoldingCost_HpI(int[][][] inventoryAtEndOfPeriod_I) {

		float holdingCost = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					holdingCost += unitHoldingCost_HP[m][t][k] * inventoryAtEndOfPeriod_I[m][t][k];
				}
			}
		}

		return holdingCost;
	}
	
	public static float getHoldingCost_sigmaHpI(int[][][] inventoryAtEndOfPeriod_I) {

		float holdingCost = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					holdingCost += Math.pow((0.1 * unitHoldingCost_HP[m][t][k] * inventoryAtEndOfPeriod_I[m][t][k]), 2);
				}
			}
		}

		return holdingCost;
	}
	
	// ***********************************************************************
	// TIME
	// ***********************************************************************
	
	public static float getProdutionTimeOnePeriod_PTQ(int[][][] productionQuantity_Q, int period) {
		float productionTime = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int m = 0; m < Config.M; m++) {
				productionTime += unitProductionTime_PT[m][k] * productionQuantity_Q[m][period][k];
			}
		}
		
		return productionTime;
	}
	
	public static float getProdutionTimeFromQuantity(int m, int k, int quantity) {
		return unitProductionTime_PT[m][k] * quantity;		
	}
	
	public static float getQuantityFromProductionTime(int m, int k, float time) {
		return time / unitProductionTime_PT[m][k];		
	}
	
	public static float getProdutionTimeOneDay_PT(int[][][] productionQuantity_Q) {
		float productionTime = 0;
		for (int t = 0; t < Config.T; t++) {
			productionTime += getProdutionTimeOnePeriod_PTQ(productionQuantity_Q, t);
		}
		
		return productionTime;
	}

	// --------------------------------------------------------------------------
	
	public static float getSetupTimeOnePeriod_AZ(int[][][] isSelected_Z, int period) {
		float setupTime = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int m = 0; m < Config.M; m++) {
				setupTime += unitSetupTime_A[m][k] * isSelected_Z[m][period][k];
			}
		}
		
		return setupTime;
	}
	
	public static float getSetupTimeFromQuantity(int m, int k, int quantity) {
		return unitSetupTime_A[m][k] * quantity;
	}
	
	public static float getQuantityFromSetupTime(int m, int k, float time) {
		return time / unitSetupTime_A[m][k];
	}
	
	public static float getSetupTimeOneDay_AZ(int[][][] isSelected_Z) {
		float setupTime = 0;
		for (int t = 0; t < Config.T; t++) {
			setupTime += getSetupTimeOnePeriod_AZ(isSelected_Z, t);
		}
		
		return setupTime;
	}
	
	public static float getQuantityFromTotalTime(int m, int k, float time) {
		return time / (unitSetupTime_A[m][k] + unitProductionTime_PT[m][k]);
	}
	
	// ---------------------------------------------------------------------------------
	
	public static float getAvailableProductionTime_TT() {
		float availableProductionTime = 0;
		for (int t = 0; t < Config.T; t++) {
			for (int m = 0; m < Config.M; m++) {
				availableProductionTime += availableProductionTimePerPeriod_TT[m][t];
			}
		}
		
		return availableProductionTime;
	}
	
	// ---------------------------------------------------------------------------------
	
	public static float getAvailableCapacity_WM() {
		float capacity = 0;
		for (int m = 0; m < Config.M; m++) {
			capacity += availableCapacity_WM[m];
		}
		
		return capacity;
	}
	
	// ---------------------------------------------------------------------------------
	
//	public static float getTransportCapacity_R() {
//		float transportCapacity = 0;
//		for (int k = 0; k < Config.K; k++) {
//			for (int m = 0; m < Config.M; m++) {
//				transportCapacity += transportCapacity_R[m][k];
//			}
//		}
//		
//		return transportCapacity;
//	}

	// ---------------------------------------------------------------------------------
	
	// ***********************************************************************
	// DECISION VARIABLES
	// ***********************************************************************	
	
	public static void randomizeTransportQuantity_U(int[][][][] productTransportQuantity_U) {

		Random rand = new Random();
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					for (int j = 0; j < Config.J; j++) {
						productTransportQuantity_U[j][m][t][k] = rand.nextInt();
					}
				}
			}
		}

	}
	
	public static void randomizePlantSelection_Z(int[][][] isSelected_Z) {
		Random rand = new Random();
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int m = 0; m < Config.M; m++) {
					isSelected_Z[m][t][k] = rand.nextInt(2);
				}
			}
		}
	}
	
	
}
