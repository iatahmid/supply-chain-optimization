/**
 * 
 */
package layers;

import commandcentre.Reader;
import utility.Config;

/**
 * @author Tahmid
 *
 */
public class Customer {
	
	// Reader
//	private Reader reader;
	
	// Costs
	private static float[][][] unitShortageCost_CS = Reader.readCS();
	
	// Decision Variables
//	private int[][][] backorderQuantity_S;
	
	public static float[][][] stochasticDemand_DC = Reader.readDC();
	
//	public Customer() {
////		reader = new Reader(Config.K, Config.T, Config.L, Config.M, Config.J, Config.I);
//		
//		unitShortageCost_CS = Reader.readCS();
//		stochasticDemand_DC = Reader.readDC();
//		
//		backorderQuantity_S = new int[Config.I][Config.T][Config.K];
//		
//	}

	public static float getShortageCost_CS(int[][][] backorderQuantity_S) {
		
		float shortageCost = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int i = 0; i < Config.I; i++) {
					shortageCost += unitShortageCost_CS[i][t][k] * backorderQuantity_S[i][t][k];
				}
			}
		}
		
		return shortageCost;
	}
	
	public static float getShortageCost_sigmaCS(int[][][] backorderQuantity_S) {

		float shortageCost = 0;
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int i = 0; i < Config.I; i++) {
					shortageCost += Math.pow((0.1 * unitShortageCost_CS[i][t][k] * backorderQuantity_S[i][t][k]), 2);
				}
			}
		}

		return shortageCost;
	}
	
	public static float getStochasticDemand_DC() {
		float stochasticDemand = 0;
		
		for (int k = 0; k < Config.K; k++) {
			for (int t = 0; t < Config.T; t++) {
				for (int i = 0; i < Config.I; i++) {
					stochasticDemand += stochasticDemand_DC[i][t][k];
				}
			}
		}
		
		return stochasticDemand;
	}
	
	public static float getStochasticDemandForPeriod_DC(int t, int k) {
		float stochasticDemand = 0;
		
		for (int i = 0; i < Config.I; i++) {
			stochasticDemand += stochasticDemand_DC[i][t][k];
		}
		
		return stochasticDemand;
	}
	
	public static float getStochasticDemandForShoe_DC(int k) {
		float stochasticDemand = 0;
		
		for (int t = 0; t < Config.T; t++) {
			for (int i = 0; i < Config.I; i++) {
				stochasticDemand += stochasticDemand_DC[i][t][k];
			}
		}
		
		return stochasticDemand;
	}
	
	public static float getStochasticDemandForShoe_DC(int i, int t, int k) {
		return stochasticDemand_DC[i][t][k];
	}
	
}
