package mff.cuni.verlet;

import mff.cuni.config.ConstantsVerlet;
import mff.cuni.gui.Gui;


public class Verlet {

	public void run() {

		MoleculesModel mm = new MoleculesModel();
		mm.generateMolecules(ConstantsVerlet.numberOfMolecules);
		mm.generateM();
		mm.generateOldR();
		mm.generateOldV();
		
		double totalStartP = mm.countTotalP();
		
		mm.countOldF();
		
		// mm contains: oldR, oldV, oldF
		// joining molecules which are too close
//		mm.recountMOld();

		mm.countR();
		mm.countF();
		mm.countV();
		if (ConstantsVerlet.isPlastical) {
			mm.recountMPlastic();
		} else {
			mm.recountMSpringy();
		}
		// mm contains: oldR, oldV, oldF, R, V, F

		
		System.out.println("Step: " + 0);
		System.out.println("  TotalStartP: " + totalStartP);
//		mm.printModel();

		for (int t = 1; t < ConstantsVerlet.repeat; t++) {

			MoleculesModel mmNew = new MoleculesModel(mm);
			// mmNew contains: oldR (as mm.R), oldV (as mm.V), oldF (as mm.F)
			mmNew.countR();
			mmNew.countF();
			mmNew.countV();
			if (ConstantsVerlet.isPlastical) {
				mmNew.recountMPlastic();
			} else {
				mmNew.recountMSpringy();
			}

			System.out.println("Step: " + t);

			double totalM = mmNew.countTotalM();
			System.out.println("  TotalM: " + totalM);

			double totalE = mmNew.countTotalE();
			System.out.println("  TotalE: " + totalE);

			double totalP = mmNew.countTotalP();
			System.out.println("  TotalP: " + totalP);

//			mmNew.printModel();
			
			Gui.setMolecules(mmNew);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// mmNew contains: oldR, oldV, oldF, R, V, F
			mm = mmNew;
		}
				
	}

}
