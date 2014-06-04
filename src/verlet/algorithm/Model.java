package verlet.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import verlet.vector.Distance;
import verlet.vector.Point;
import verlet.vector.Vector;

public class Model {

	public static enum JoinType {PLASTICAL_JOIN, ELASTIC_JOIN};

	private ArrayList<Molecule> molecules = new ArrayList<Molecule>();
	private JoinType joinType;

	public Model(JoinType type) {
		this.joinType = type;
	}

	public void generateModel(int numOfMolecules) {

		generateMolecules(numOfMolecules);

		generateM();
		generatePoint();

		generateVeloricityKeepTotalP0();
		setVeloricity0();
	}

	private void generateMolecules(int numOfMolecules) {
		for (int i = 0; i < numOfMolecules; i++) {

			Molecule moleculeI = new Molecule(i);
			moleculeI.setModel(this);

			molecules.add(moleculeI);
		}
	}

	private void generateM() {

		Random rand = new Random();
		for (Molecule moleculeI : getMolecules() ) {

			moleculeI.setM( rand.nextInt(9) +1 );
		}
	}

	private void generatePoint() {

		for (Molecule moleculeI : getMolecules() ) {

			// TODO: hack with position
			Point point = new Point();
			point.generatPosition();
			int sequenceNumber = moleculeI.getSequenceNumber();

			if (sequenceNumber == 0) {
				point = new Point(130, -130);
			} if (sequenceNumber == 1) {
				point = new Point(30, 130);
			} if (sequenceNumber == 2) {
				point = new Point(-130, 20);
			}

			moleculeI.setPoint(point);
		}
	}

	private void setVeloricity0() {

		for (Molecule moleculeI : getMolecules() ) {

			moleculeI.setVelocity(new Vector(0, 0));
		}
	}

	private void generateVeloricityKeepTotalP0() {

		int numOfMolecules = getNumOfMolecules();
		
		// if number of molecules is odd number
		if (numOfMolecules % 2 == 1) {
			// skip the last molecule
			numOfMolecules--;
		}


		double vAvarage = 2;
		double mAvarage = 5;

		double totalPx = numOfMolecules * vAvarage * mAvarage;
		double totalPy = totalPx;

		ArrayList<Double> rationsX = new ArrayList<Double>();
		ArrayList<Double> rationsY = new ArrayList<Double>();

		rationsX.add(0.5);
		rationsX.add(1.0);

		rationsY.add(0.5);
		rationsY.add(1.0);

		Random generator = new Random();
		for (int i = 0; i < numOfMolecules -2; i++) {
			rationsX.add(generator.nextDouble());
			rationsY.add(generator.nextDouble());
		}

		Collections.sort(rationsX);
		Collections.sort(rationsY);		

		for (int index = 0; index < numOfMolecules; index++) {
		
			double doubleXImin1 = 0;
			double doubleYImin1 = 0;
			if (index > 0) {
				doubleXImin1 = rationsX.get(index -1);
				doubleYImin1 = rationsY.get(index -1);
			}
			double doubleXI = rationsX.get(index);
			double doubleYI = rationsY.get(index);

			double rationXI = doubleXI - doubleXImin1;
			double rationYI = doubleYI - doubleYImin1;

			double signX = 1;
			if (doubleXI <= 0.5) {
				signX = -1;
			}
			double signY = 1;
			if (doubleYI <= 0.5) {
				signY = -1;
			}

			double PXI = totalPx * signX * rationXI;
			double PYI = totalPy * signY * rationYI;

			Molecule moleculeI = molecules.get(index);

			int mI = moleculeI.getM();
			Vector veloricity = new Vector(PXI/mI, PYI/mI);

			moleculeI.setVelocity(veloricity);
		}
	}


	public JoinType getJoinType() {
		return joinType;
	}

	public ArrayList<Molecule> getMolecules() {
		return this.molecules;
	}
	public void addMolecule(Molecule molecule) {
		this.molecules.add(molecule);
	}
	void removeMolecule(Molecule molecule) {
		this.molecules.remove(molecule);
	}

	public Molecule getMoleculeBySN(int SequenceNumber) {

		for (int i = 0;  i < molecules.size(); i++) {

			Molecule moleculeI = molecules.get(i);
			if (moleculeI.getSequenceNumber() == SequenceNumber) {
				return moleculeI;
			}
		}

		return null;
	}

	public int getNumOfMolecules() {
		return this.molecules.size();
	}

	public Distance getDistancesBySN(int sequenceNumber1,
			int sequenceNumber2) {

		Molecule molecule1 = getMoleculeBySN(sequenceNumber1);
		Molecule molecule2 = getMoleculeBySN(sequenceNumber2);

		return molecule1.getDistance(molecule2);		
	}

	public Model getModelWithNewPosition() {

		Model modelNew = new Model( getJoinType() );

		for ( Molecule moleculeI : getMolecules() ) {

			Molecule moleculeNewI =
					moleculeI.getNewMoleculeAtNewPosition();
			moleculeNewI.setModel(modelNew);

			modelNew.addMolecule(moleculeNewI);
		}

		return modelNew;
	}

	public void countNewVeloricity(Model modelOld) {

		ArrayList<Molecule> moleculesNew = getMolecules();
		ArrayList<Molecule> moleculesOld = modelOld.getMolecules();

		for (int i = 0; i < getMolecules().size(); i++) {

			Molecule moleculeNew = moleculesNew.get(i);
			Molecule moleculeOld = moleculesOld.get(i);

			moleculeNew.countNewVeloricity(moleculeOld);
		}
	}

	public void repareCollision(Model modelOld) {

		int numOfMolecules = modelOld.getMolecules().size();

		for (int sNI = 0; sNI < numOfMolecules; sNI++) {
			for (int sNJ = sNI+1; sNJ < numOfMolecules; sNJ++) {

				Molecule molOldI = modelOld.getMoleculeBySN(sNI);
				Molecule molOldJ = modelOld.getMoleculeBySN(sNJ);

				Molecule molNewI = getMoleculeBySN(sNI);
				Molecule molNewJ = getMoleculeBySN(sNJ);

				if ( willBeOverleap(molOldI, molOldJ, molNewI, molNewJ) ) {
					
					//molNewI.joinWith(molNewJ);
					join(molOldI, molOldJ, molNewI, molNewJ);
					return;
				}

			}
		}

	}

	private void join(Molecule molOld1, Molecule molOld2,
			Molecule molNew1, Molecule molNew2) {

		Model model = molOld1.getModel();

		if ( model.getJoinType() == JoinType.PLASTICAL_JOIN ) {

			joinPlastical(molOld1, molOld2, molNew1, molNew2);

		} else 	if ( model.getJoinType() == JoinType.ELASTIC_JOIN ) {

			joinElastic(molOld1, molOld2, molNew1, molNew2);
			
		} else {
			throw new IllegalStateException("This type of Join is not implemented");
		}

	}

	private void joinPlastical(Molecule molOld1, Molecule molOld2,
			Molecule molNew1, Molecule molNew2) {

		Molecule molOld = Molecule.plasticalJoin(molOld1, molOld2);

		molNew1.setM( molOld.getM() );
		molNew1.setPoint( molOld.getPoint() );
		molNew1.setVelocity( molOld.getVelocity() );

		Model modelNew = molNew1.getModel();
		int numOfMolecules = modelNew.getNumOfMolecules();
		
		// renumbering
		Molecule moleculeLast = modelNew.getMoleculeBySN( numOfMolecules -1 );
		moleculeLast.setSequenceNumber( molNew2.getSequenceNumber() );

		modelNew.removeMolecule(molNew2);
	}

	private void joinElastic(Molecule molOld1, Molecule molOld2,
			Molecule molNew1, Molecule molNew2) {

		Molecule[] moleculesNew = Molecule.elasticJoinWith(molOld1, molOld2);

		Model modelNew = molNew1.getModel();
		modelNew.removeMolecule(molNew1);
		modelNew.removeMolecule(molNew2);

		Molecule result1 = moleculesNew[0];
		result1.setModel(modelNew);

		Molecule result2 = moleculesNew[1];
		result2.setModel(modelNew);

		modelNew.addMolecule(result1);
		modelNew.addMolecule(result2);
	}
	
	private boolean willBeOverleap(Molecule molOld1, Molecule molOld2,
			Molecule molNew1, Molecule molNew2) {

		Point pointOld1 = molOld1.getPoint();
		Point pointOld2 = molOld2.getPoint();

		Point pointNew1 = molNew1.getPoint();
		Point pointNew2 = molNew2.getPoint();

		int signOldX = 0;
		int signOldY = 0;

		if ( pointOld1.getX() < pointOld2.getX() ) {
			signOldX = -1;
		} else if ( pointOld1.getX() > pointOld2.getX() ) {
			signOldX = 1;
		}
		if ( pointOld1.getY() < pointOld2.getY() ) {
			signOldY = -1;
		} else if ( pointOld1.getY() > pointOld2.getY() ) {
			signOldY = 1;
		}

		int signNewX = 0;
		int signNewY = 0;

		if ( pointNew1.getX() < pointNew2.getX() ) {
			signNewX = -1;
		} else if ( pointNew1.getX() > pointNew2.getX() ) {
			signNewX = 1;
		}
		if ( pointNew1.getY() < pointNew2.getY() ) {
			signNewY = -1;
		} else if ( pointNew1.getY() > pointNew2.getY() ) {
			signNewY = 1;
		}


		if (signOldX == signNewX*(-1)) {
			if (signOldY == signNewY*(-1)) {
				return true;
			}
		}

		if (signOldX == 0 && signNewX == 0) {
			if (signOldY == signNewY*(-1)) {
				return true;
			}
		}

		if (signOldX == 0 && signNewX == 0) {
			if (signOldY == 0 && signNewY == 0) {
				return true;
			}
		}

		return false;
	}

	public double getEkin() {

		double energy = 0;

		for (Molecule moleculeI : getMolecules() ) {

			energy += moleculeI.getEkin();
		}

		return energy;
	}

	public double getEpot() {

		/*
		 * How to count potential Energy:
		 * 
		 * We have count Energy which is necessary to move
		 * sequentially all (n-1) molecules to infinite distance.
		 * 
		 * Depends on order of moving?
		 *  
		 *  Removing first:       Removing second:    Removing third 
		 *         x                  o                  o
		 *           | |                |  '               |   '
		 *           | |                |  '               |   '
		 *         o |                x    '             o |
		 *           |   '              |  '               | |
		 *           |   '              |  '               | |
		 *         o                  o    '             x
		 * 
		 * Doesn't depend on order, but is necessary to count with
		 * absolute values of energy.
		 * 
		 */
		double potential = 0;

		for (int i = 0; i < getNumOfMolecules(); i++ ) {
			for (int j = i+1; j < getNumOfMolecules(); j++ ) {

				Molecule moleculeI = getMoleculeBySN(i);
				Molecule moleculeJ = getMoleculeBySN(j);

				Distance vectorDistance = moleculeI.getDistance(moleculeJ);
				double distance = (int) vectorDistance.getNorm();

				potential += moleculeI.getM() * moleculeJ.getM() / distance;
			}
		}

		return potential * (-1);
	}

	public double getEtot() {
		
		double vectorEkin = getEkin();
		double vectorEpot = getEpot();

		return vectorEpot + vectorEkin;
	}

	public Vector getP() {

		Vector vector = new Vector(0, 0);

		for (Molecule moleculeI : getMolecules()) {

			vector.plusToVector(moleculeI.getP());
		}

		return vector;
	}

	public void print() {

		System.out.println("Model Info:");

//		this.getMolecule(0).getDistance(this.getMolecule(1)).print("Distance");
//		this.getMolecule(0).getF().print("F0");
//		this.getMolecule(1).getF().print("F1");

		double Ekin = getEkin();
		System.out.println("Ekin: " + Ekin);

		double Epot = getEpot();
		System.out.println("Epot: " + Epot);

		double Etot = getEtot();
		System.out.println("Etot: " + Etot);

		Vector vectorP = getP();
		vectorP.print("P");

	}

}
