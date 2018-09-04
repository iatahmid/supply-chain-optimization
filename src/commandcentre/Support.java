package commandcentre;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Support {
	
	// Splits an integer into multiple integers 
	// such that all of them sums up to the given integer
	public static int[] splitValue(int splitCount, int value) {
		
		Random random = new Random();
		int[] nums = new int[splitCount];
		for (int i = 0; i < nums.length; i++) {
			nums[i] = (int) Math.ceil(random.nextFloat() * value);
//			if(nums[i] == 0) {
//				nums[i] = (int) Math.ceil(random.nextFloat() * value);
//			}
			value -= nums[i];
		}
		if(value < 0) {
//			Arrays.sort(nums);
//			nums[splitCount-1] += value;
			value *= (-1);
			int[] tmp = splitValue(splitCount, value);
			for (int i = 0; i < tmp.length; i++) {
				nums[i] -= tmp[i];
			}
		} else if(value > 0) {
//			Arrays.sort(nums);
//			nums[0] += value;
			int[] tmp = splitValue(splitCount, value);
			for (int i = 0; i < tmp.length; i++) {
				nums[i] += tmp[i];
			}
		}
//		Collections.shuffle(Arrays.asList(nums));
		
		return nums;
	}
	
	public static float[] splitValue(int splitCount, float value) {

		Random random = new Random();
		float[] nums = new float[splitCount];
		for (int i = 0; i < nums.length; i++) {
			nums[i] = random.nextFloat() * value;
//			if(nums[i] == 0) {
//				nums[i] = random.nextFloat() * value;
//			}
			value -= nums[i];
		}
		if(value < 0) {
			//			Arrays.sort(nums);
			//			nums[splitCount-1] += value;
			value *= (-1);
			float[] tmp = splitValue(splitCount, value);
			for (int i = 0; i < tmp.length; i++) {
				nums[i] -= tmp[i];
			}
		} else if(value > 0) {
			//			Arrays.sort(nums);
			//			nums[0] += value;
			float[] tmp = splitValue(splitCount, value);
			for (int i = 0; i < tmp.length; i++) {
				nums[i] += tmp[i];
			}
		}
		//		Collections.shuffle(Arrays.asList(nums));

		return nums;
	}
	
	private static void prepareInputFile(String fileName, String abbreviation, int subScriptLen1) {

		File file;
		PrintWriter fileWriter = null;
		try {
			file = new File(fileName);
			fileWriter = new PrintWriter(new FileWriter(file));

			for (int m = 1; m <= subScriptLen1; m++) {
				fileWriter.println(abbreviation + "_" + m);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fileWriter.close();
		}
	}

	private static void prepareInputFile(String fileName, String abbreviation, int subScriptLen1, int subScriptLen2) {

		File file;
		PrintWriter fileWriter = null;
		try {
			file = new File(fileName);
			fileWriter = new PrintWriter(new FileWriter(file));

			for (int k = 1; k <= subScriptLen2; k++) {
				for (int m = 1; m <= subScriptLen1; m++) {
					fileWriter.println(abbreviation + "_" + m + "" + k + "");
				}
				fileWriter.println();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fileWriter.close();
		}
	}

	private static void prepareInputFile(String fileName, String abbreviation, String superScript, int superScriptLen,
			int subScriptLen) {

		File file;
		PrintWriter fileWriter = null;
		try {
			file = new File(fileName);
			fileWriter = new PrintWriter(new FileWriter(file));

			for (int i = 1; i <= superScriptLen; i++) {
				fileWriter.println(superScript + "=" + i);
				for (int m = 1; m <= subScriptLen; m++) {
					fileWriter.println(abbreviation + "_" + m);
				}
				fileWriter.println();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fileWriter.close();
		}
	}

	private static void prepareInputFile(String fileName, String abbreviation, String superScript, int superScriptLen,
			int subScriptLen1, int subScriptLen2) {

		File file;
		PrintWriter fileWriter = null;
		try {
			file = new File(fileName);
			fileWriter = new PrintWriter(new FileWriter(file));

			for (int i = 1; i <= superScriptLen; i++) {
				fileWriter.println(superScript + "=" + i);
				for (int k = 1; k <= subScriptLen2; k++) {
					for (int m = 1; m <= subScriptLen1; m++) {
						fileWriter.println(abbreviation + "_" + m + "" + k + "");
					}
					fileWriter.println();
				}
				fileWriter.println();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fileWriter.close();
		}
	}

	private static void prepareInputFile(String fileName, String abbreviation, String superScript, int superScriptLen,
			int subScriptLen1, int subScriptLen2, int subScriptLen3) {

		File file = new File(fileName);

		try {
			PrintWriter fileWriter = new PrintWriter(new FileWriter(file));

			for (int i = 1; i <= superScriptLen; i++) {
				fileWriter.println(superScript + "=" + i);
				for (int j = 1; j <= subScriptLen3; j++) {
					for (int k = 1; k <= subScriptLen2; k++) {
						for (int m = 1; m <= subScriptLen1; m++) {
							fileWriter.println(abbreviation + "_" + m + "" + k + "" + j);
						}
						fileWriter.println();
					}
					fileWriter.println();
				}
				fileWriter.println();
			}

			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void prepareAllFiles() {
		prepareInputFile("productCost_CL.txt", "CL", "g", 1, 3, 3, 2);
		prepareInputFile("transportationCost_D.txt", "d", "g", 1, 3, 3, 2);
		prepareInputFile("transportationCost_DP.txt", "DP", "k", 2, 3, 3, 2);
		prepareInputFile("setupCost_CA.txt", "CA", "k", 2, 3, 2);

		prepareInputFile("inventoryCost_HD.txt", "HD", "k", 2, 3, 2);
		prepareInputFile("transportationCost_DD.txt", "DD", "k", 2, 3, 3, 2);
		prepareInputFile("productionCost_C.txt", "C", "k", 2, 3, 2);
		prepareInputFile("inventoryCost_HP.txt", "HP", "k", 2, 3, 2);

		prepareInputFile("shortageCost_CS.txt", "CS", "k", 2, 3, 2);
		prepareInputFile("productionTime_PT.txt", "PT", "k", 2, 3);
		prepareInputFile("setupTime_A.txt", "A", "k", 2, 3);
		prepareInputFile("availableProductionTime_TT.txt", "TT", 3, 2);

		prepareInputFile("capacityOfManufacturingPlant_WM.txt", "WM", 3);
		prepareInputFile("transportCapacity_R.txt", "R", "k", 2, 3);
		prepareInputFile("stochasticDemand_DC.txt", "DC", "k", 2, 3, 2);
		prepareInputFile("componentNeeds_WL.txt", "WL", 3, 2);

		prepareInputFile("capacityOfWarehose_WD.txt", "WD", 3);
		prepareInputFile("volumeOfUnitProduct_V.txt", "V", 2);
		prepareInputFile("setupCostOfWarehouse_F.txt", "F", 3);
	}
}
