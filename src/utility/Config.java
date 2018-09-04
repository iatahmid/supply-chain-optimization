/**
 * 
 */
package utility;

/**
 * @author Tahmid
 *
 */
public class Config {
	
	// Production
	public static int G = 1; // ComponentType
	public static int K = 2; // ProductType
	public static int T = 2; // TimeShift
	
	// Layers
	public static int L = 3; // SupplierCount
	public static int M = 3; // ManufacturingPlantCount
	public static int J = 3; // DistributionCentreCount
	public static int I = 3; // CustomerCount
	
	// Genetic Operators
	public static float mutationRate = 0.15f;
	public static float crossoverRate = 0.5f;
	public static int generationCount = 5;
	public static int populationPerGeneration = 200;
	
}
