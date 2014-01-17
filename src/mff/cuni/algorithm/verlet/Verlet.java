package mff.cuni.algorithm.verlet;

import mff.cuni.config.ConstantsVerlet;
import mff.cuni.config.VectorDouble2D;
import mff.cuni.gui.Gui;
import mff.cuni.molecule.Generator;
import mff.cuni.molecule.Molecule;
import mff.cuni.molecule.MoleculesModel;

import java.util.ArrayList;

public class Verlet {

	public void run() {

		MoleculesModel mm = Generator.generateMolecules(
				ConstantsVerlet.numberOfMolecules);		
		for (int t = 1; t < ConstantsVerlet.repeat; t++) {
			System.out.println("Step: " + t);
			
			double totalM = mm.countTotalM();
			System.out.println("TotalM: " + totalM);
			
			double totalE = mm.countTotalE();
			System.out.println("TotalE: " + totalE);

			double totalP = mm.countTotalP();
			System.out.println("TotalP: " + totalP);
			
			Gui.setMolecules(mm);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mm.countF();

			MoleculesModel mmNew = new MoleculesModel(mm);
			mmNew.countRNew(mm);
			mmNew.recountMofMolecules();
			mmNew.roundOfRNew();
			mmNew.countVNew();

			mm = mmNew;

		}
				
	}

}
