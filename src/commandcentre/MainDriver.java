/**
 * 
 */
package commandcentre;

import java.io.IOException;
import java.util.ArrayList;
import nsga2.DeliveryPlan;
import nsga2.NSGA;
import nsga2.Population;
import utility.Config;

/**
 * @author Tahmid
 *
 */
public class MainDriver {

	private static void readFiles() {
		
//		Reader reader = new Reader(2, 2, 3, 3, 3, 3);
		Reader.readCL();
		Reader.readD();
		Reader.readF();
		Reader.readDP();

		Reader.readCA();
		Reader.readHD();
		Reader.readDD();
		Reader.readC();
		
		Reader.readHP();
		Reader.readCS();
		Reader.readPT();
		Reader.readA();
		
		Reader.readR();
		Reader.readWM();
		Reader.readDC();
		Reader.readWL();
		
	}
	
	public static void main(String[] args) throws IOException {
//		debug();
//		readFiles();
		engageNSGA();
	}
	
	private static void engageNSGA() {
		
		NSGA nsga = new NSGA();
		Population pop = new Population(Config.populationPerGeneration);
		pop.init();
		
		System.out.printf("%-5s %-20s %-20s %-20s %-20s", "Gen", "Cost", "Time", "Min Z1", "Min Z2");
		System.out.println();
		for (int i = 0; i < Config.generationCount; i++) {
//			System.out.print("Generation: " + (i+1) + " ");
			pop = nsga.evolvePopulation(pop);
			
//			System.out.println(pop.getFittest());
			pop.getFittest().print(i);
		}
		
		System.out.println(pop.getFittest());
//		pop.getFittest().print();
	}

	private static void debug() {
		
		ArrayList<DeliveryPlan> plans = new ArrayList<DeliveryPlan>();
		for (int i = 0; i < 1; i++) {
			DeliveryPlan plan = new DeliveryPlan();
			plan.initialize();
			
			plans.add(plan);
			System.out.println(plan);
		}
		
//		for (DeliveryPlan deliveryPlan : plans) {
//			System.out.println(deliveryPlan);
//		}
		
//		DeliveryPlan plan1 = new DeliveryPlan();
//		plan1.initialize();
//		System.out.println(plan1);
//		
//		DeliveryPlan plan2 = new DeliveryPlan();
//		plan2.initialize();
//		System.out.println(plan2);
//		
//		NSGA nsga = new NSGA();
//		DeliveryPlan child = nsga.crossover(plan1,  plan2);
//		System.out.println(child);
//		child = nsga.mutate(child);
//		System.out.println(child);
//		System.out.println("Cost: " + samplePlan.getCost());
//		System.out.println("Time: " + samplePlan.getTime());
//		
//		DeliveryPlan duplicatePlan = new DeliveryPlan(samplePlan);
//		System.out.println("Cost: " + duplicatePlan.getCost());
//		System.out.println("Time: " + duplicatePlan.getTime());
//		
//		NSGA nsga = new NSGA();
//		DeliveryPlan plan = nsga.crossover(samplePlan, duplicatePlan);
//		System.out.println("Cost: " + plan.getCost());
//		System.out.println("Time: " + plan.getTime());
//		
//		plan = nsga.mutate(plan);
//		System.out.println("Cost: " + plan.getCost());
//		System.out.println("Time: " + plan.getTime());
//		
//		Population pop = new Population(20);
//		pop.init();
////		Collections.sort(pop.getPlans(), new FitnessValueComparator());
//		
//		ArrayList<ArrayList<DeliveryPlan>> fronts = nsga.nonDominatedSort(pop.getPlans());
//		
//		for (ArrayList<DeliveryPlan> front : fronts) {
//			nsga.sortByCrowdingDistance(front);
//			for (DeliveryPlan deliveryPlan : front) {
//				System.out.println(deliveryPlan.rank + ": " + deliveryPlan.crowdingDistance);
//			}
//		}
		
//		for (DeliveryPlan deliveryPlan : pop.getPlans()) {
//			System.out.println(deliveryPlan.rank);
////			System.out.println(deliveryPlan.crowdingDistance);
//		}
		
		// ****************************
		// * TESTING SHOE SIZE VOLUME
		// ****************************

//		System.out.println(Shoe.getQuantityFromVolume(1, 10.5f));
//		System.out.println(Shoe.getVolumeFromQuantity(1, 3));
		
		// **************************
		// * TESTING SHOE SIZE VOLUME
		// **************************
		
		// **************************
		// * SPLITTING AN INTEGER
		// **************************

//		int[] nums = Support.splitValue(3, 2000);
//		System.out.println(Arrays.toString(nums));
		
		// **************************
		// * SPLITTING AN INTEGER
		// **************************
		
		// **************************
		// * TESTING COST CALCULATION
		// **************************

//		int[][][] pq = new int[Config.M][Config.L][Config.T];
//		Supplier.randomizeQuantity_W(pq);
//		System.out.println(Supplier.getProductCost_CLW(pq));
		
//		Supplier s = new Supplier();
//		s.randomizeQuantity();
//		System.out.println(s.getTransportationCost());
		
//		DistributionCentre dc = new DistributionCentre();
//		dc.randomizeWarehouseSelection();
//		System.out.println(dc.getWarehouseCost());
		
		// **************************
		// * TESTING COST CALCULATION
		// **************************
		
		// **************************
		// * MULTI DIMENSIONAL ARRAY
		// **************************
		
//		int I = 3;
//		int J = 4;
//		int K = 2;
//		int L = 3;
//		
//		int[][][][] a = new int[I][J][K][L];
//
//		for (int i = 0; i < I; i++) {
//			for (int j = 0; j < J; j++) {
//				for (int k = 0; k < K; k++) {
//					for (int l = 0; l < L; l++) {
//						a[i][j][k][l] = i+j+k+l;
//					}
//				}
//			}
//		}
//		
//		for (int i = 0; i < I; i++) {
//			for (int j = 0; j < J; j++) {
//				for (int k = 0; k < K; k++) {
//					for (int l = 0; l < L; l++) {
//						System.out.print("a[" +i+ "][" +j+ "]["+k+"]["+l+"] = " +a[i][j][k][l]+ "\t");
//					}
//					System.out.println();
//				}
//				System.out.println();
//			}
//			System.out.println();
//		}
		
		
		// ****************************
		// * MULTI DIMENSIONAL ARRAY * 
		// ****************************
		
		
		// *****************
		// READING FROM FILE
		// *****************

//		try {
//			File file = new File("test.txt");
//			BufferedReader fileReader = new BufferedReader(new FileReader(file));
//
//			String fileContent;
//			int sum = 0;
//			while ((fileContent = fileReader.readLine()) != null) {
//				if (!fileContent.equals("")) {
//					int i = Integer.parseInt(fileContent);
//					sum += i;
//				}
//			}
//			System.out.println(sum);
//
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NumberFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// *****************
		// READING FROM FILE
		// *****************
	}
}
