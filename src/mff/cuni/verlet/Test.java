package mff.cuni.verlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import mff.cuni.config.ConstantsVerlet;
import mff.cuni.verlet.Test;

public class Test {

	public static void main(String [ ] args)
	{
		System.out.println("Verlet test");
		ConstantsVerlet.debugMode = true;
		
		Test testVerlet = new Test();
		testVerlet.run();		
	}
	
	public void run() {

		int repeatsV1 = 5;
		String fileNameV1 = "OutputTest1VerletPlastical.txt";
		ConstantsVerlet.numberOfMolecules = 12;
		ConstantsVerlet.isPlastical = true;

		runTest(repeatsV1, fileNameV1);
		
		
		int repeatsV2 = 5;
		String fileNameV2 = "OutputTest2VerletNotPlastical.txt";
		ConstantsVerlet.numberOfMolecules = 12;
		ConstantsVerlet.isPlastical = false;

		runTest(repeatsV2, fileNameV2);
	}

	public void runTest(int repeats, String fileName) {
            
		for (int i = 0; i < repeats; i++) {
			
			Verlet vI = new Verlet();
			vI.run();
			
			ArrayList<Double> energyI = vI.energy;
			String fileNamePlusNum = fileName + i;
			
			saveEnergy(energyI, fileNamePlusNum);
		}
	}

	public void saveEnergy(ArrayList<Double> energy, String fileName) {
		
        try {
        	
            File file = new File(fileName);
            BufferedWriter output = new BufferedWriter(new FileWriter(file));

            output.write("GenerationNums : Energy\n");
            
			for (int i = 0; i < energy.size(); i++) {
				
				Double energyI = energy.get(i);
								
				output.write("" + (i+1) + " : " + energyI + "\n");
			}
          
            output.close();
        } catch ( IOException e ) {
           e.printStackTrace();
        }
	}

}
