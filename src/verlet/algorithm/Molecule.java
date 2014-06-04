package verlet.algorithm;

import java.util.ArrayList;

import verlet.vector.Distance;
import verlet.vector.Point;
import verlet.vector.Vector;

public class Molecule {

	private int id;
	private int sequenceNumber;
	private int m;

	private Point point;
	private Vector velocity; 	
	private Model model;

	@SuppressWarnings("unused")
	private Molecule() {}

	public Molecule(Molecule molecule) {

		this.id = molecule.id;
		this.sequenceNumber = molecule.getSequenceNumber();
		this.m = molecule.getM();

		this.point = null;
		this.velocity = null;
	}

	public Molecule(int sequenceNumber) {

		this.id = sequenceNumber;
		this.sequenceNumber = sequenceNumber;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public int getID() {
		return this.id;
	}

	public int getSequenceNumber() {
		return this.sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Point getPoint() {
		return this.point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}

	void setVelocity(Vector veloricity) {
		this.velocity = veloricity;
	}
	public Vector getVelocity() {
		return this.velocity;
	}

	public int getM() {
		return this.m;
	}
	public void setM(int m) {
		this.m = m;
	}

	public Model getModel() {
		return this.model;
	}

	public Molecule getNewMoleculeAtNewPosition() {
		
		double diffT = VerletAlgorithm.DIFF_TIME;
		double x = this.getPoint().getX();
		double y = this.getPoint().getY();

		double vx = this.getVelocity().getX();
		double vy = this.getVelocity().getY();

		double fx = this.getF().getX();
		double fy = this.getF().getY();

		double xNew = x  +  vx*diffT  +  Math.pow(diffT, 2)/2  *  fx/m;
		double yNew = y  +  vy*diffT  +  Math.pow(diffT, 2)/2  *  fy/m;

		Point point = new Point(xNew, yNew);

		Molecule molecule = new Molecule(this);
		molecule.setPoint(point);

		return molecule;
	}

	public Distance getDistance(Molecule molecule) {

		double distanceX =
				Math.abs( getPoint().getX() - molecule.getPoint().getX() );
		double distanceY =
				Math.abs( getPoint().getY() - molecule.getPoint().getY() );
		
		Distance distance =
				new Distance(distanceX, distanceY);
		
		return distance;
	}


	public Vector getF() {

		ArrayList<Molecule> molecules = model.getMolecules();

		Vector vectorF = new Vector(0, 0);

		for (Molecule molecule : molecules) {

			if (molecule == this) {
				continue;
			}

			Vector vectorI = countFofMolule(molecule);

			vectorF.plusToVector(vectorI);
		}

		return vectorF;
	}

	public double getPotential(int startSN, int endSN) {

		if ( startSN < 0  ||
		     endSN >= getModel().getNumOfMolecules() ) {

			throw new IllegalStateException("Parameters are not valid");
		}

		double potential = 0;

		for (int i = startSN; i <= endSN; i++) {

			Molecule moleculeI = model.getMoleculeBySN(i);

			potential += countPotentialofMolule(moleculeI);
		}

		return potential;
	}


	// pocita silu kterou na aktualni molekulu pusobi molekula a parametru
	public Vector countFofMolule(Molecule molecule) {

		if (this.getSequenceNumber() == molecule.getSequenceNumber()) {
			throw new IllegalStateException("Cann't count F between the same molecule");
		}

		int sixnX;
		if ( getPoint().getX() < molecule.getPoint().getX() ) {
			sixnX = 1;
		} else {
			sixnX = -1;
		}

		int sixnY;
		if ( getPoint().getY() < molecule.getPoint().getY() ) {
			sixnY = 1;
		} else {
			sixnY = -1;
		}

		/*
		 *      Distances:
		 * 
		 *          |\                      a^2 + b^2 = c^2
		 *          | \ distScalar          ratioXdivY = x / y
		 *        x |  \ 
		 *          |___\
		 *            y
		 *
		 *          |\
		 *          | \
		 *          |  \                    fx^2 + fy^2 = fScalar^2
		 *          |   \                   fx = ratioXdivY * fy
		 *       fx |    \ fScalar          (ratioXdivY * fy)^2 + fy^2 = fScalar^2    
		 *          |     \                 fy^2 * (ratioXdivY + 1) = fScalar^2
		 *          |force \                fy^2 = fScalar^2 / (ratioXdivY + 1)
		 *          |       \               fy = ( fScalar^2 / (ratioXdivY + 1) )^0.5
		 *          |________\              fx = ratioXdivY * fy
		 *             fy
		 * 
		 */

		double m1 = getM();
		double m2 = molecule.getM();

		Distance distance = getDistance(molecule);
		double distanceScalar = distance.getNorm();

		double ratioXdivY = distance.getX() / distance.getY();

		double fScalar = m1 * m2 / Math.pow(distanceScalar, 2);
		
        double fY = Math.pow( Math.pow(fScalar, 2) / (ratioXdivY + 1), 0.5 );
        double fX = ratioXdivY * fY;


		return new Vector(fX*sixnX, fY*sixnY);
	}

	// pocita potencial mezi aktualni molekulou a molekulou v parametru
	private double countPotentialofMolule(Molecule molecule) {
		
		double m1 = getM();
		double m2 = molecule.getM();

		Distance distance = getDistance(molecule);
		double distanceScalar = distance.getNorm();

		return m1 * m2 / distanceScalar;

	}

	public static Molecule plasticalJoin(Molecule molecule1, Molecule molecule2) {
		System.out.println("Plastical Join");

		// spojeni - plasticka srazka
		double m1 = molecule1.getM();
		double m2 = molecule2.getM();
		double v1X = molecule1.getVelocity().getX();
		double v1Y = molecule1.getVelocity().getY();
		double v2X = molecule2.getVelocity().getX();
		double v2Y = molecule2.getVelocity().getY();


		Vector distance = molecule1.getDistance(molecule2);

		double sumM = m1 + m2;
		double distanceX = distance.getX();
		double distanceY = distance.getY();

		double Xt = (0*m1 + distanceX*m2) / (m1 + m2); 
		double Yt = (0*m1 + distanceY*m2) / (m1 + m2); 

		double centerX = molecule2.getPoint().getX() - Xt;
		double centerY = molecule2.getPoint().getY() - Yt;

		double delataVX = (m1 * v1X +
				m2 * v2X) / sumM;
		double delataVY = (m1 * v1Y +
				m2 * v2Y) / sumM;
		
		Molecule m = new Molecule();
		m.setM((int) sumM);
		m.setPoint( new Point(centerX, centerY) );
		m.setVelocity( new Vector(delataVX, delataVY) );		

		return m;
	}

	public static Molecule[] elasticJoinWith(Molecule molecule1, Molecule molecule2) {
		System.out.println("Elastic Join");

		double m1 = molecule1.getM();
		double m2 = molecule2.getM();

		double r1x = molecule1.getPoint().getX();
		double r1y = molecule1.getPoint().getY();

		double r2x = molecule2.getPoint().getX();
		double r2y = molecule2.getPoint().getY();

		double v1x = molecule1.getVelocity().getX();
		double v1y = molecule1.getVelocity().getY();

		double v2x = molecule2.getVelocity().getX();
		double v2y = molecule2.getVelocity().getY();


		double vTx = (m1*v1x + m2*v2x) / (m1 + m2);
		double vTy = (m1*v1y + m2*v2y) / (m1 + m2);
		
		double v1NewX = 2*vTx - v1x;
		double v1NewY = 2*vTy - v1y;

		double v2NewX = 2*vTx - v2x;
		double v2NewY = 2*vTy - v2y;


		Molecule molNew1 = new Molecule(molecule1);
		molNew1.setPoint( new Point(r1x, r1y) );
		molNew1.setVelocity(new Vector(v1NewX, v1NewY) );

		Molecule molNew2 = new Molecule(molecule2);
		molNew2.setPoint( new Point(r2x, r2y) );
		molNew2.setVelocity(new Vector(v2NewX, v2NewY) );


		Molecule[] moleculesNew = new Molecule[2];
		moleculesNew[0] = molNew1;
		moleculesNew[1] = molNew2;

		return moleculesNew;
	}

	public void countNewVeloricity(Molecule moleculeOld) {

		double diffT = VerletAlgorithm.DIFF_TIME;

		double vX = moleculeOld.getVelocity().getX();
		double vY = moleculeOld.getVelocity().getY();

		double Fx = moleculeOld.getF().getX();
		double Fy = moleculeOld.getF().getY();

		double FNewx = this.getF().getX();
		double FNewy = this.getF().getY();

		double vXNew = vX  +  diffT * (Fx + FNewx) / (2*m); 
		double vYNew = vY  +  diffT * (Fy + FNewy) / (2*m);


		this.velocity = new Vector(vXNew, vYNew);
	}

	public double getEkin() {

		double vX = getVelocity().getX();
		double vY = getVelocity().getY();

		double Ekin = 0.5 * m * (vX*vX + vY*vY);
		
		return Ekin;
	}

	// Hybnost molekuly
	public Vector getP() {

		double pX = this.getM() * getVelocity().getX();
		double pY = this.getM() * getVelocity().getY();

		return new Vector(pX, pY);
	}

}
