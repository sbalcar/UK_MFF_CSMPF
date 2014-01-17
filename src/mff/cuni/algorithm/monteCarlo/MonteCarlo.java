package mff.cuni.algorithm.monteCarlo;

import mff.cuni.config.ConstantsMonteCarlo;
import mff.cuni.algorithm.monteCarlo.Generator;
import mff.cuni.diskette.DiskettesModel;

public class MonteCarlo {

	public void run() {
		
		DiskettesModel dm = Generator.generateFiles(
				ConstantsMonteCarlo.numberOfFiles);		
		
		for (int i = 0; i < ConstantsMonteCarlo.repeat; i++) {
			double fitnessOld = dm.fitness();
			System.out.println("Generation:" + i + "  Fitness:" + fitnessOld);
			
			DiskettesModel dmNew = dm.clone();
			dmNew.makeStep();

			double fitnessNew = dmNew.fitness();
			if (fitnessNew < fitnessOld) {
				dm = dmNew;
			}
		}

	}
		
}
