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
public class Supplier {
	
//	private Reader reader;
	
	// Relevant Costs
	private static int[][][] unitProductCost_CL = Reader.readCL();
	private static float[][][] unitTransportationCost_D = Reader.readD();
	
	// Decision Variables
//	private int[][][] productQuantity_W;
	
//	public Supplier() {
////		reader = new Reader(Config.K, Config.T, Config.L, Config.M, Config.J, Config.I);
//		
//		unitProductCost_CL = Reader.readCL();
//		unitTransportationCost_D = Reader.readD();
//		
//		productQuantity_W = new int[Config.M][Config.L][Config.T];
//	}
	
	// CL * W
	public static int getProductCost_CLW(int[][][] productQuantity_W) {
		
		int productCost = 0;
		for (int t = 0; t < Config.T; t++) {
			for (int l = 0; l < Config.L; l++) {
				for (int m = 0; m < Config.M; m++) {
					productCost += unitProductCost_CL[m][l][t] * productQuantity_W[m][l][t];
				}
			}
		}
		
		return productCost;
	}
	
	// D * W
	public static float getTransportationCost_DW(int[][][] productQuantity_W) {

		float transportationCost = 0;
		for (int t = 0; t < Config.T; t++) {
			for (int l = 0; l < Config.L; l++) {
				for (int m = 0; m < Config.M; m++) {
					transportationCost += unitTransportationCost_D[m][l][t] * productQuantity_W[m][l][t];
				}
			}
		}

		return transportationCost;
	}
	
	// W
	public static void randomizeQuantity_W(int[][][] productQuantity_W) {
		
		Random rand = new Random();
		for (int t = 0; t < Config.T; t++) {
			for (int l = 0; l < Config.L; l++) {
				for (int m = 0; m < Config.M; m++) {
					productQuantity_W[m][l][t] = rand.nextInt(5);
				}
			}
		}
	}
}
