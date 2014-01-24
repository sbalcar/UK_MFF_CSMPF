package mff.cuni.monteCarlo;

import java.util.ArrayList;
import java.util.Random;

import mff.cuni.config.ConstantsMonteCarlo;

public class MonteCarlo {

	public void run() {
		
		DiskettesModel dm = new DiskettesModel();
		dm.generateFiles(ConstantsMonteCarlo.numberOfFiles);		

		double probabilityOfChanege = 0;
		Random random = new Random();

		
		for (int i = 0; i < ConstantsMonteCarlo.repeat; i++) {
			
			double fitnessOld = dm.fitness();
			System.out.println("Generation:" + i + "  FreeSpaceRatio:  " + fitnessOld);
			//dm.print();
			
			if (fitnessOld == 0.0) {
				break;
			}
			
			ArrayList <Integer> files = dm.getFiles();
			makeStep(files);
			
			DiskettesModel dmNew = new DiskettesModel(files);

			double fitnessNew = dmNew.fitness();			
			if (fitnessNew <= fitnessOld) {
				
				if (0.95 >= random.nextDouble()) {
					dm = dmNew;
				}

			} else {
				// simulovane zihani
				if ( probabilityOfChanege >= random.nextDouble() ) {
					dm = dmNew;
					probabilityOfChanege = 0;
				} else {
					probabilityOfChanege += ConstantsMonteCarlo.SIMULATED_ANNEALING_DIFF;
				}
				
			}
			
		}
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
