package mff.cuni.monteCarlo;

import java.util.ArrayList;
import java.util.Random;

import mff.cuni.config.ConstantsMonteCarlo;

public class MonteCarlo {

	public DiskettesModel dm = null;
	public double fitnessResult = -1;
	public int lastGenerationNum = -1;
	
	public void run() {

		dm = new DiskettesModel();
		dm.generateFiles(ConstantsMonteCarlo.numberOfFiles);		

		Random random = new Random();

		int numGenI = 0;
		double fitnessOld = -1;
		double probabilityOfChanege = 0;
		
		for (numGenI = 0; numGenI < ConstantsMonteCarlo.repeat; numGenI++) {

			fitnessOld = dm.fitness();
			System.out.println("Generation:" + numGenI + "  FreeSpaceRatio:  " + fitnessOld);
			//dm.print();

			if (fitnessOld == 0.0) {
				this.fitnessResult = fitnessOld;
				this.lastGenerationNum = numGenI;
				break;
			}

			ArrayList <Integer> files = dm.getFiles();
			makeStep(files);

			DiskettesModel dmNew = new DiskettesModel(files);

			double fitnessNew = dmNew.fitness();			
			if (fitnessNew <= fitnessOld) {

				if (ConstantsMonteCarlo.ACCEPTANCE_PROBABILITY >=
						random.nextDouble()) {
					dm = dmNew;
				}

			} else {
				// simulovane zihani
				if ( probabilityOfChanege >= random.nextDouble() ) {
					dm = dmNew;
					probabilityOfChanege = 0;
				} else {
					probabilityOfChanege +=
							ConstantsMonteCarlo.SIMULATED_ANNEALING_DIFF;
				}

			}

		}

		this.fitnessResult = fitnessOld;
		this.lastGenerationNum = numGenI;
	}
		
	private void makeStep(ArrayList <Integer> files) {
		
		ArrayList <Integer> filesChanged = files;
		
		Random generator = new Random();
		int index1 = generator.nextInt(filesChanged.size());
		int index2 = generator.nextInt(filesChanged.size());
		
		int value1 = filesChanged.get(index1);
		int value2 = filesChanged.get(index2);
		
		filesChanged.set(index1, value2);
		filesChanged.set(index2, value1);
		
	}

}
