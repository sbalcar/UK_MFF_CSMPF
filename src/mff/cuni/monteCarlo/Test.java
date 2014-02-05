package mff.cuni.monteCarlo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import mff.cuni.config.ConstantsMonteCarlo;

public class Test {

	public static void main(String [ ] args)
	{
		System.out.println("MC test");
		
		Test testMC = new Test();
		testMC.run();		
	}
	
	public void run() {

		int repeatsM1 = 20;
		String fileNameM1 = "OutputTest1MC.txt";
		ConstantsMonteCarlo.numberOfFiles = 250;
		ConstantsMonteCarlo.maxSizeOfFiles = 10;

		runTest(repeatsM1, fileNameM1);

		int repeatsM2 = 20;
		String fileNameM2 = "OutputTest2MC.txt";
		ConstantsMonteCarlo.numberOfFiles = 250;
		ConstantsMonteCarlo.maxSizeOfFiles = 5;

		runTest(repeatsM2, fileNameM2);

		int repeatsM3 = 20;
		String fileNameM3 = "OutputTest3MC.txt";
		ConstantsMonteCarlo.numberOfFiles = 250;
		ConstantsMonteCarlo.maxSizeOfFiles = 35;

		runTest(repeatsM3, fileNameM3);

	}
	
	public void runTest(int repeats, String fileName) {
		
		MonteCarlo m1 = new MonteCarlo();
		
		ArrayList<Integer> lastGenerationNumsM1 = new ArrayList<Integer>();
		ArrayList<Double> fitnessResultsM1 = new ArrayList<Double>();

		for (int i = 0; i < repeats; i++) {
			m1.run();
			
			lastGenerationNumsM1.add(m1.lastGenerationNum);
			fitnessResultsM1.add(m1.fitnessResult);
		}

        try {
        	
          File file = new File(fileName);
          BufferedWriter output = new BufferedWriter(new FileWriter(file));
          
          output.write("ExperimentNums : LastGenerationNums : FitnessResults : NumOfDiskettes\n");
          
          for (int i = 0; i < lastGenerationNumsM1.size(); i++) {

        	  int lastGenerationNumM1I = lastGenerationNumsM1.get(i);
        	  double fitnessResultM1I = fitnessResultsM1.get(i);
        	  int numOfDiskettes = m1.dm.getNumOfDiskettes();

        	  output.write("" + (i+1) + " : " +
        	          lastGenerationNumM1I + " : " +
        			  fitnessResultM1I + " : " +
        			  numOfDiskettes + "\n");
          }
          
          output.close();
        } catch ( IOException e ) {
           e.printStackTrace();
        }
		
	}
}
