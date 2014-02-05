package mff.cuni.verlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import mff.cuni.config.ConstantsVerlet;
import mff.cuni.config.VectorDouble2D;

public class MoleculesModel {

	private ArrayList <Molecule> molecules = new ArrayList<Molecule>();

	public MoleculesModel() {
	}

	public MoleculesModel(MoleculesModel model) {

		molecules = new ArrayList<Molecule>();
		for (Molecule m : model.getMolecules()) {
			
			Molecule mNew = new Molecule();
			mNew.m = m.m;

			mNew.oldx = m.x % 1;
			mNew.oldy = m.y % 1;

			mNew.x = -1;
			mNew.y = -1;
						
			mNew.fOldx = m.fx;
			mNew.fOldy = m.fy;

			mNew.fx = -1;
			mNew.fy = -1;
			
			mNew.vOldx = m.vx;
			mNew.vOldy = m.vy;
			
			mNew.isPrecipitated = m.isPrecipitated;
			
						
			this.molecules.add(mNew);
		}
	}
	
	public void generateMolecules(int numOfMolecules) {
		
		molecules = new ArrayList<Molecule>();

		for (int i = 0; i < numOfMolecules; i++) {
			molecules.add(new Molecule());
		}
	}

	public void generateM() {
		
		Random generator = new Random();
		
		for (int i = 0; i < molecules.size(); i++) {
			
			int m = generator.nextInt(10) +1;
			
			Molecule moleculeI = molecules.get(i);
			moleculeI.m = m;
		}
	}

	public void generateOldR() {
		
		Random generator = new Random();
		
		for (int i = 0; i < molecules.size(); i++) {
			
			double rOldx = generator.nextDouble();
			if(rOldx < 0) {rOldx *= -1;}
			double rOldy = generator.nextDouble();
			if(rOldy < 0) {rOldy *= -1;}

			Molecule moleculeI = molecules.get(i);
			moleculeI.oldx = rOldx;
			moleculeI.oldy = rOldy;
		}
	}

	public void generateOldV() {

		if (ConstantsVerlet.numberOfMolecules <= 1) {
			return;
		}
		
		double moleculeVAvarage = 2;
		double moleculeMAvarage = 5;
		
		double totalP = ConstantsVerlet.numberOfMolecules *
				moleculeMAvarage * moleculeVAvarage;
		
		double totalPx = totalP;
		double totalPy = totalP;

		ArrayList<Double> rationsX = new ArrayList<Double>();
		ArrayList<Double> rationsY = new ArrayList<Double>();

		rationsX.add(0.5);
		rationsX.add(1.0);

		rationsY.add(0.5);
		rationsY.add(1.0);
		
		Random generator = new Random();
		for (int i = 0; i < molecules.size() -2; i++) {
			rationsX.add(generator.nextDouble());
			rationsY.add(generator.nextDouble());
		}

		Collections.sort(rationsX);
		Collections.sort(rationsY);
		
		for (int index = 0; index < molecules.size(); index++) {
			
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
			moleculeI.vOldx = PXI / moleculeI.m;
			moleculeI.vOldy = PYI / moleculeI.m;
		}
	}
	
	public void setOldVAs0() {
		
		for (int i = 0; i < molecules.size(); i++) {

			Molecule moleculeI = molecules.get(i);
			moleculeI.vOldx = 0;
			moleculeI.vOldy = 0;
		}
	}
	
	public void recountMPlastic() {

		while ( ! ( recountMtoSmallDistance() &&
				    recountMAllDistances() )
			  ) { }
		countF();
	}

	public void recountMSpringy() {

		recountMSpringy_();
		countF();
	}

			
	private boolean recountMtoSmallDistance() {
		
		double [][] distances;
		if ((distances = countDistances(molecules)) != null) {
		
			int [] indexs;
			if ((indexs  = toSmallDistance(distances)) != null) {
	
				Molecule moleculeI = molecules.get(indexs[0]);
				Molecule moleculeJ = molecules.get(indexs[1]);
				
				joinPlastical(moleculeI, moleculeJ);
				return false;
			}
		}
		
		return true;
	}

	private boolean recountMAllDistances() {
		
		for (int i = 0; i < molecules.size(); i++) {
			for (int j = 0; j < molecules.size(); j++) {

				if (i >= j) {
					continue;
				}

				Molecule mI = molecules.get(i);
				Molecule mJ = molecules.get(j);
				
				boolean IWasBiggerAndIsSmallerX =
						mI.oldx > mJ.oldx && mI.x < mJ.x;
				boolean IWasSmallerAndIsBiggerX =
						mI.oldx < mJ.oldx && mI.x > mJ.x;
				
				boolean IWasBiggerAndIsSmallerY =
						mI.oldy > mJ.oldy && mI.y < mJ.y;
				boolean IWasSmallerAndIsBiggerY =
						mI.oldy < mJ.oldy && mI.y > mJ.y;

				boolean join =
						(IWasBiggerAndIsSmallerX || IWasSmallerAndIsBiggerX) &&
						(IWasBiggerAndIsSmallerY || IWasSmallerAndIsBiggerY);
				
				if (join) {
					joinPlastical(mI, mJ);
					return false;
				}
			}
		}

		return true;
	}

	private boolean recountMSpringy_() {
		
		double [][] distances = countDistances(molecules);

		int WAS_PRECIPITATED = 1;
		int IS_PRECIPITATED = 2;
		
		for (int i = 0; i < molecules.size(); i++) {
			int abc = 0;
			for (int j = 0; j < molecules.size(); j++) {

				if (i >= j) {
					continue;
				}

				Molecule mI = molecules.get(i);
				Molecule mJ = molecules.get(j);

				if (distances[i][j] < ConstantsVerlet.minDistace) {
	
					if (mI.isPrecipitated == WAS_PRECIPITATED)
						continue;
					if (mJ.isPrecipitated == WAS_PRECIPITATED)
						continue;

					joinSpringy(mI, mJ);

					mI.isPrecipitated = WAS_PRECIPITATED;
					mJ.isPrecipitated = WAS_PRECIPITATED;

				} else {
					
//					abc++;
//					if (abc == molecules.size()) {
//						mI.isPrecipitated = -1;
//					}
				}
				
			}
		}
		
		for (int i = 0; i < molecules.size(); i++) {
			
			Molecule mI = molecules.get(i);
			
			int distanceCounter = 0;
			for (int j = 0; j < molecules.size(); j++) {

				if (i == j) {
					continue;
				}

				if (distances[i][j] >= ConstantsVerlet.minDistace) {
					distanceCounter++;
				}
		   }
			
			if (distanceCounter == molecules.size() -1) {
				mI.isPrecipitated = -1;
			}
			
		}

		return true;
	}

	public void setMolecules(ArrayList <Molecule> molecules_) {
		this.molecules = molecules_;
	}
	
	public ArrayList <Molecule> getMolecules() {
		return molecules;
	}

	public void countOldF() {
		countParamF(true);
	}

	public void countF() {
		countParamF(false);
	}

	public void countParamF(boolean recountOld) {

		VectorDouble2D [][] matrix =
				new VectorDouble2D[molecules.size()][molecules.size()];

		for (int i = 0; i < molecules.size(); i++) {						
			for (int j = 0; j < molecules.size(); j++) {
				
				Molecule moI = molecules.get(i);
				Molecule moJ = molecules.get(j);

				if (i == j) {
					matrix[i][j] = new VectorDouble2D();
					matrix[i][j].x = 0;
					matrix[i][j].y = 0;
					break;
				}

				double distX = 0;
				double distY = 0;
				if (recountOld) {
					distX = (moI.oldx - moJ.oldx);
					distY = (moI.oldy - moJ.oldy);
				} else {
					distX = (moI.x - moJ.x);
					distY = (moI.y - moJ.y);
				}

				double dist = Math.pow(distX*distX + distY*distY, 0.5);
                double f = moI.m*moJ.m / dist;

				double fx = f * distX;
				double fy = f * distY;

				matrix[i][j] = new VectorDouble2D();
				matrix[i][j].x = -fx;
				matrix[i][j].y = -fy;

				matrix[j][i] = new VectorDouble2D();
				matrix[j][i].x = fx;
				matrix[j][i].y = fy;
			}
		}

		for (int i = 0; i < molecules.size(); i++) {
			Molecule moleculeI = molecules.get(i);

			double xDiff = 0;
			double yDiff = 0;

			for (int j = 0; j < molecules.size(); j++) {
				
				VectorDouble2D diff = matrix[i][j];
				xDiff += diff.x;
				yDiff += diff.y;
			}

			if (recountOld) {
				moleculeI.fOldx = xDiff;
				moleculeI.fOldy = yDiff;
			} else {
				moleculeI.fx = xDiff;
				moleculeI.fy = yDiff;
			}

		}
	}


	private void joinPlastical(Molecule molecule1, Molecule molecule2) {

		// spojeni - plasticka srazka
		double sumM = molecule1.m + molecule2.m;
		double X = (molecule1.x + molecule2.x) /2;
		double Y = (molecule1.y + molecule2.y) /2;
		double delataVX = (molecule1.m * molecule1.vx +
				molecule2.m * molecule2.vx) / sumM;
		double delataVY = (molecule1.m * molecule1.vy +
				molecule2.m * molecule2.vy) / sumM;

		molecule1.m = (int)sumM;
		molecule1.x = X;
		molecule1.y = Y;
		molecule1.vx = delataVX;
		molecule1.vy = delataVY;
		
		molecules.remove(molecule2);
	}

	private void joinSpringy(Molecule molecule1, Molecule molecule2) {

		double p1x = molecule1.m * molecule1.vx;
		double p1y = molecule1.m * molecule1.vy;

		double p2x = molecule2.m * molecule2.vx;
		double p2y = molecule2.m * molecule2.vy;

		molecule1.vx = p2x / molecule1.m;
		molecule1.vy = p2y / molecule1.m;
		
		molecule2.vx = p1x / molecule2.m;
		molecule2.vy = p1y / molecule2.m;
	}
		

	public void countR() {

		// spocti rNew
		for (int i = 0; i < this.molecules.size(); i++) {
			Molecule moleculeI = this.molecules.get(i);

			double m = moleculeI.m;
			double rOldx = moleculeI.oldx;
			double rOldy = moleculeI.oldy;

			double fOldX = moleculeI.fOldx;
			double fOldY = moleculeI.fOldy;
			
			double vOldX = moleculeI.vOldx;
			double vOldY = moleculeI.vOldy;

			double h = ConstantsVerlet.h;


			double x = rOldx + h*vOldX + h*h/2*fOldX/m;
			double y = rOldy + h*vOldY + h*h/2*fOldY/m;
			
			moleculeI.x = (x +1) % 1;
			moleculeI.y = (y +1) % 1;

		}

	}

	public void countV() {
		
		for (int i = 0; i < this.molecules.size(); i++) {
			Molecule moleculeI = this.molecules.get(i);

			double m = moleculeI.m;

			double fOldX = moleculeI.fOldx;
			double fOldY = moleculeI.fOldy;

			double fX = moleculeI.fx;
			double fY = moleculeI.fy;

			double vOldX = moleculeI.vOldx;
			double vOldY = moleculeI.vOldy;

			double h = ConstantsVerlet.h;

			moleculeI.vx = vOldX + h*(fOldX + fX)/(2*m);
			moleculeI.vy = vOldY + h*(fOldY + fY)/(2*m);
		}
	}
	
	public double countTotalM() {
		
		double totalM = 0;
		for (int i = 0; i < this.molecules.size(); i++) {
			Molecule moleculeNew = this.molecules.get(i);

			totalM += moleculeNew.m;
		}
		
		return totalM;
	}

	public double countTotalE() {
		
		double totalE = 0;
		for (int i = 0; i < this.molecules.size(); i++) {
			Molecule moleculeI = this.molecules.get(i);

			double velocity2 = Math.pow(moleculeI.vx, 2) +
					Math.pow(moleculeI.vy, 2);

			double eI = 0.5 * (double)moleculeI.m * velocity2;
			totalE += eI;
		}

		return totalE;
	}


	public double countTotalP() {

		double totalPx = 0;
		double totalPy = 0;
		
		for (int i = 0; i < this.molecules.size(); i++) {
			Molecule moleculeI = this.molecules.get(i);

			totalPx += moleculeI.m * moleculeI.vx;
			totalPy += moleculeI.m * moleculeI.vy;			
		}
		
		double p2 = Math.pow(totalPx, 2) + Math.pow(totalPy, 2);
	
		return Math.pow(p2, 0.5);
	}


	public void roundOfRNew() {
		for (Molecule mI : this.molecules) {
			mI.x = mI.x % 1;
			mI.y = mI.y % 1;
			
			if (mI.x < 0) {
				mI.x += 1;
			}
			
			if (mI.y < 0) {
				mI.y += 1;
			}

		}
	}

	private double [][] countDistances(ArrayList<Molecule> molecules) {
		
		double [][] matrix = new double[molecules.size()][molecules.size()];
		
		for (int i = 0; i < molecules.size(); i++) {
			Molecule moI = molecules.get(i);
						
			for (int j = 0; j < molecules.size(); j++) {
				Molecule moJ = molecules.get(j);
				
				if (i == j) {
					VectorDouble2D vectorIJ = new VectorDouble2D();
					vectorIJ.x = 0;
					vectorIJ.y = 0;
					break;
				}
				
				double xDiff = moI.oldx - moJ.oldx;
				double yDiff = moI.oldy - moJ.oldy;
								
				double distance = Math.sqrt(xDiff*xDiff + yDiff*yDiff);
				matrix[i][j] = distance;
				matrix[j][i] = distance;
			}			
		}
		return matrix;
	}

	private int [] toSmallDistance(double [][] distances) {
		
		
		for (int i = 0; i < molecules.size(); i++) {
	        for (int j = 0; j < molecules.size(); j++) {

				if (i == j) {
					break;
				}

				//Molecule moI = molecules.get(i);
				//Molecule moJ = molecules.get(j);
				
				double distancIJ = distances[i][j];
				if (distancIJ < ConstantsVerlet.minDistace/2) {
				//if (distancIJ < moI.getRadius()/4 + moJ.getRadius()/4) {

					int[] indexs = new int [2];
					indexs[0] = i;
					indexs[1] = j;

					return indexs;
				}
			}
		}
		return null;
	}
	
	
	public void printModel() {
		System.out.println("-------------------");

		for (int i = 0; i < molecules.size(); i++) {

			Molecule molI = molecules.get(i);
			
			System.out.println("    MoleculeNum: " + i);
			molI.printMolecule();
		}
	}
}
