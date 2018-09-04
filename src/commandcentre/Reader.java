/**
 * 
 */
package commandcentre;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import utility.Config;

/**
 * @author Tahmid
 *
 */
public class Reader {

	private static int K = Config.K;;
	private static int T = Config.T;

	private static int L = Config.L;
	private static int M = Config.M;
	private static int J = Config.J;
	private static int I = Config.I;

//	public Reader(int k, int t, int l, int m, int j, int i) {
//		K = k;
//		T = t;
//
//		L = l;
//		M = m;
//		J = j;
//		I = i;
//	}

	@SuppressWarnings("resource")
	public static int[][][] readCL() {

		int CL[][][] = new int[M][L][T];

		try {
			File file = new File("productCost_CL.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int t = 0; t < T; t++) {
				for (int l = 0; l < L; l++) {
					for (int m = 0; m < M; m++) {
						if((fileContent = fileReader.readLine()) != null) {
							CL[m][l][t] = Integer.parseInt(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("tk")).trim());
						}
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return CL;
	}

	@SuppressWarnings("resource")
	public static float[][][] readD() {

		float D[][][] = new float[M][L][T];

		try {
			File file = new File("transportationCost_D.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int t = 0; t < T; t++) {
				for (int l = 0; l < L; l++) {
					for (int m = 0; m < M; m++) {
						if((fileContent = fileReader.readLine()) != null) {
							D[m][l][t] = Float.parseFloat(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("tk")).trim());
						}
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return D;
	}

	public static int[] readF() {

		int F[] = new int[J];

		try {
			File file = new File("setupCostOfWarehouse_F.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int j = 0; j < J; j++) {
				if((fileContent = fileReader.readLine()) != null) {
					F[j] = Integer.parseInt(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("tk")).trim());
				}
			}

			// System.out.println("Finished reading F.");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return F;
	}
	
	public static float[][][][] readDP() {

		float DP[][][][] = new float[J][M][T][K];

		try {
			File file = new File("transportationCost_DP.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int k = 0; k < K; k++) {
				for (int t = 0; t < T; t++) {
					for (int m = 0; m < M; m++) {
						for (int j = 0; j < J; j++) {
							if((fileContent = fileReader.readLine()) != null) {
								DP[j][m][t][k] = Float.parseFloat(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("tk")).trim());
							}
						}
					}
				}				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return DP;
	}
	
	public static int[][][] readCA() {

		int CA[][][] = new int[M][T][K];

		try {
			File file = new File("setupCost_CA.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int k = 0; k < K; k++) {
				for (int t = 0; t < T; t++) {
					for (int m = 0; m < M; m++) {
						if((fileContent = fileReader.readLine()) != null) {
							CA[m][t][k] = Integer.parseInt(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("tk")).trim());
						}						
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return CA;
	}
	
	public static float[][][] readHD() {

		float HD[][][] = new float[J][T][K];

		try {
			File file = new File("inventoryCost_HD.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int k = 0; k < K; k++) {
				for (int t = 0; t < T; t++) {
					for (int j = 0; j < J; j++) {
						if((fileContent = fileReader.readLine()) != null) {
							HD[j][t][k] = Float.parseFloat(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("tk")).trim());
						}						
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return HD;
	}

	public static float[][][][] readDD() {

		float DD[][][][] = new float[I][J][T][K];

		try {
			File file = new File("transportationCost_DD.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int k = 0; k < K; k++) {
				for (int t = 0; t < T; t++) {
					for (int j = 0; j < J; j++) {
						for (int i = 0; i < I; i++) {
							if((fileContent = fileReader.readLine()) != null) {
								DD[i][j][t][k] = Float.parseFloat(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("tk")).trim());
							}
						}
					}
				}				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return DD;
	}
	
	public static float[][][] readC() {

		float C[][][] = new float[M][T][K];

		try {
			File file = new File("productionCost_C.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int k = 0; k < K; k++) {
				for (int t = 0; t < T; t++) {
					for (int m = 0; m < M; m++) {
						if((fileContent = fileReader.readLine()) != null) {
							C[m][t][k] = Float.parseFloat(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("tk")).trim());
						}						
					}
				}
			}
			
			// System.out.println("Finished reading C. ");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return C;
	}

	public static float[][][] readHP() {

		float HP[][][] = new float[M][T][K];

		try {
			File file = new File("inventoryCost_HP.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int k = 0; k < K; k++) {
				for (int t = 0; t < T; t++) {
					for (int m = 0; m < M; m++) {
						if((fileContent = fileReader.readLine()) != null) {
							HP[m][t][k] = Float.parseFloat(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("tk")).trim());
						}						
					}
				}
			}
			
			// System.out.println("Finished reading HP.");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return HP;
	}
	
	public static float[][][] readCS() {

		float CS[][][] = new float[I][T][K];

		try {
			File file = new File("shortageCost_CS.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int k = 0; k < K; k++) {
				for (int t = 0; t < T; t++) {
					for (int i = 0; i < I; i++) {
						if((fileContent = fileReader.readLine()) != null) {
							CS[i][t][k] = Float.parseFloat(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("tk")).trim());
						}						
					}
				}
			}
			
			// System.out.println("Finished reading CS.");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return CS;
	}

	public static float[][] readPT() {

		float PT[][] = new float[M][K];

		try {
			File file = new File("productionTime_PT.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int k = 0; k < K; k++) {
				for (int m = 0; m < M; m++) {
					if((fileContent = fileReader.readLine()) != null) {
						PT[m][k] = Float.parseFloat(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("min")).trim());
					}
				}
			}
			
			// System.out.println("Finished reading PT.");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return PT;
	}
	
	public static float[][] readA() {

		float A[][] = new float[M][K];

		try {
			File file = new File("setupTime_A.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int k = 0; k < K; k++) {
				for (int m = 0; m < M; m++) {
					if((fileContent = fileReader.readLine()) != null) {
						A[m][k] = Float.parseFloat(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("min")).trim());
					}
				}
			}
			
			// System.out.println("Finished reading A.");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return A;
	}

	public static float[][] readTT() {

		float TT[][] = new float[M][T];

		try {
			File file = new File("availableProductionTime_TT.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int t = 0; t < T; t++) {
				for (int m = 0; m < M; m++) {
					if((fileContent = fileReader.readLine()) != null) {
						TT[m][t] = Float.parseFloat(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("min")).trim());
					}
				}
			}
			
			// System.out.println("Finished reading TT.");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return TT;
	}

	
	public static float[][][] readR() {

		float R[][][] = new float[M][T][K];

		try {
			File file = new File("transportCapacity_R.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int t = 0; t < Config.T; t++) {
				for (int k = 0; k < K; k++) {
					for (int m = 0; m < M; m++) {
						if((fileContent = fileReader.readLine()) != null) {
							R[m][t][k] = Float.parseFloat(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("pairs")).trim());
						}
					}
				}				
			}
			
			// System.out.println("Finished reading R.");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return R;
	}
	
	public static float[] readWM() {

		float WM[] = new float[M];

		try {
			File file = new File("capacityOfManufacturingPlant_WM.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int m = 0; m < M; m++) {
				if((fileContent = fileReader.readLine()) != null) {
					WM[m] = Integer.parseInt(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("feet")).trim());
				}
			}

			// System.out.println("Finished reading WM.");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return WM;
	}

	public static float[][][] readDC() {

		float DC[][][] = new float[I][T][K];

		try {
			File file = new File("stochasticDemand_DC.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int k = 0; k < K; k++) {
				for (int t = 0; t < T; t++) {
					for (int i = 0; i < I; i++) {
						if((fileContent = fileReader.readLine()) != null) {
							DC[i][t][k] = Float.parseFloat(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("pairs")).trim());
						}						
					}
				}
			}
			
			// System.out.println("Finished reading DC.");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return DC;
	}

	public static float[][] readWL() {

		float WL[][] = new float[M][T];

		try {
			File file = new File("componentNeeds_WL.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int t = 0; t < T; t++) {
				for (int m = 0; m < M; m++) {
					if((fileContent = fileReader.readLine()) != null) {
						WL[m][t] = Float.parseFloat(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("sq")).trim());
					}
				}
			}
			
			// System.out.println("Finished reading WL.");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return WL;
	}
	
	public static int[] readWD() {

		int WD[] = new int[J];

		try {
			File file = new File("capacityOfWarehose_WD.txt");
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(file));
			String fileContent;

			for (int j = 0; j < J; j++) {
				if((fileContent = fileReader.readLine()) != null) {
					WD[j] = Integer.parseInt(fileContent.substring(fileContent.indexOf("=")+1, fileContent.indexOf("feet")).trim());
				}
			}
			
			// System.out.println("Finished reading WD.");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return WD;
	}
}

