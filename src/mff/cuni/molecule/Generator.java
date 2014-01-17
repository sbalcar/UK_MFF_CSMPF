package mff.cuni.molecule;

import java.util.ArrayList;
import java.util.Random;


public class Generator {

	public static MoleculesModel generateMolecules(int count) {
		  
		Random generator = new Random();
		ArrayList <Molecule> molecules = new ArrayList <Molecule>();
		
		for (int i = 0; i < count; i++) {
			double rx = generator.nextDouble();
			if(rx < 0) {rx *= -1;}
			double ry = generator.nextDouble();
			if(ry < 0) {ry *= -1;}
			double vx = generator.nextDouble();
			double vy = generator.nextDouble();
			int m = generator.nextInt(10) +1;
			
			Molecule molecule = new Molecule();
			molecule.m = m;
			molecule.x = rx;
			molecule.y = ry;
			molecule.vx = vx;
			molecule.vy = vy;
						
			molecules.add(molecule);
		}

		MoleculesModel mm = new MoleculesModel();
		mm.setMolecules(molecules);

		return mm;
	}

}
