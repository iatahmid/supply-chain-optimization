/**
 * 
 */
package layers;

import java.util.Random;

import javax.xml.crypto.dsig.spec.TransformParameterSpec;

import commandcentre.Reader;
import utility.Config;

/**
 * @author Tahmid
 *
 */
public class DistributionCentre {
	
	// Reader
//	private Reader reader;
	
	// Costs
	private static int[] warehouseCost_F = Reader.readF();;
	private static float[][][] unitInventoryCost_HD = Reader.readHD();
	private static float[][][][] unitTransportationCost_DD = Reader.readDD();
	
	// Decision Variables
//	private int[] isSelected_Y;
	
//	private int[][][] inventoryAtEndOfPeriodDC;
//	private int[][][][] transportQuantity_X;
	
	// Constraints
	public static  int[] capacityOfWarehouse_WD = Reader.readWD();
	
//	public DistributionCentre() {
//		// Initialize Reader
////		reader = new Reader(Config.K, Config.T, Config.L, Config.M, Config.J, Config.I);
//		
//		// Reading the given costs
//		warehouseCost_F = Reader.readF();
//		unitInventoryCost_HD = Reader.readHD();
//		unitTransportationCost_DD = Reader.readDD();
//		
//		// Preparing the decision variables
//		isSelected_Y = new int[Config.J];
//		
//		inventoryAtEndOfPeriodDC = new int[Config.J][Config.T][Config.K];
//		transportQuantity_X = new int[Config.I][Config.J][Config.T][Config.K];
//	}
	
	// F * Y
	public static int getWarehouseCost_FY(int[] isSelected_Y) {
		
		int warehouseCost = 0;
		for (int j = 0; j < Config.J; j++) {
			warehouseCost += warehouseCost_F[j] * isSelected_Y[j];
		}
		
		return warehouseCost;
	}
	
	public static float getInventoryCost_HdI(int[][][] inventoryAtEndOfPeriod) {
		
		float inventoryCost = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int j = 0; j < Config.J; j++) {
					inventoryCost += unitInventoryCost_HD[j][t][k] * inventoryAtEndOfPeriod[j][t][k];
				}
			}
		}
		
		return inventoryCost;
	}
	
	public static float getInventoryCost_sigmaHdI(int[][][] inventoryAtEndOfPeriod) {

		float inventoryCost = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int j = 0; j < Config.J; j++) {
					inventoryCost += Math.pow(0.1 * unitInventoryCost_HD[j][t][k] * inventoryAtEndOfPeriod[j][t][k], 2);
				}
			}
		}

		return inventoryCost;
	}
	
	public static float getTransportCost_DdX(int[][][][] transportQuantity_X) {
		
		float transportCost = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int j = 0; j < Config.J; j++) {
					for (int i = 0; i < Config.I; i++) {
						transportCost += unitTransportationCost_DD[i][j][t][k] * transportQuantity_X[i][j][t][k];
					}
				}
			}
		}
		return transportCost;
	}
	
	public static float getTransportCost_sigmaDdX(int[][][][] transportQuantity_X) {

		float transportCost = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int j = 0; j < Config.J; j++) {
					for (int i = 0; i < Config.I; i++) {
						transportCost += Math.pow(0.1 * unitTransportationCost_DD[i][j][t][k] * transportQuantity_X[i][j][t][k], 2);
					}
				}
			}
		}
		return transportCost;
	}
	
	// random Y
	public static void randomizeWarehouseSelection_Y(int[] isSelected_Y) {
		Random rand = new Random();
		for (int j = 0; j < Config.J; j++) {
			isSelected_Y[j] = rand.nextInt(2);
		}
	}
	
	// sigmaF * Y
	public static int getWarehouseCost_sigmaFY(int[] isSelected_Y) {

		int warehouseCost = 0;
		for (int j = 0; j < Config.J; j++) {
			warehouseCost += (0.1 * warehouseCost_F[j] * isSelected_Y[j]) 
							* (0.1 * warehouseCost_F[j] * isSelected_Y[j]);
		}

		return warehouseCost;
	}
}
