package mff.cuni.molecule;

import java.util.ArrayList;

import mff.cuni.config.ConstantsVerlet;
import mff.cuni.config.VectorDouble2D;

public class MoleculesModel {

	private ArrayList <Molecule> molecules = new ArrayList<Molecule>();

	
	public MoleculesModel() {
	}
	
	public MoleculesModel(MoleculesModel model) {
		
		ArrayList<Molecule> moleculesOld = model.getMolecules();
		
		this.molecules = new ArrayList<Molecule>();
		for (Molecule m : moleculesOld) {
			
			Molecule mNew = new Molecule();
			mNew.m = m.m;
			
			mNew.x = -1;
			mNew.y = -1;
			
			mNew.oldx = m.x % 1;
			mNew.oldy = m.y % 1;
			
			mNew.fx = -1;
			mNew.fy = -1;
			
			mNew.vOldx = m.vx;
			mNew.vOldy = m.vy;
			
			mNew.fOldx = m.fx;
			mNew.fOldy = m.fy;
			
			this.molecules.add(mNew);
		}

	}

	public void recountMofMolecules() {
		
		while (! recountMofMolecules3()) {
		}
	}
	
	private boolean recountMofMolecules3() {
		
		ArrayList <Molecule> moleculesNew = this.molecules;
				
			for (int i = 0; i < moleculesNew.size(); i++) {
				for (int j = 0; j < moleculesNew.size(); j++) {

				if (i >= j) {
					continue;
				}


				double [][] distances;
				if ((distances = countDistances(this.molecules)) != null) {
				
					int [] indexs = toSmallDistance(distances);
					
					if (indexs != null) {
						
						int indexI = indexs[0];
						int indexJ = indexs[1];
									
						Molecule moleculeI = moleculesNew.get(indexI);
						Molecule moleculeJ = moleculesNew.get(indexJ);
						
						joinMolecules(moleculeI, moleculeJ);
						return false;
					}
				}
				
				Molecule moleculeNewI = moleculesNew.get(i);
				Molecule moleculeNewJ = moleculesNew.get(j);

				double newIx = moleculeNewI.x;
				double newIy = moleculeNewI.y;
				double oldIx = moleculeNewI.oldx;
				double oldIy = moleculeNewI.oldy;

				double newJx = moleculeNewJ.x;
				double newJy = moleculeNewJ.y;
				double oldJx = moleculeNewJ.oldx;
				double oldJy = moleculeNewJ.oldy;
				
				
				boolean IWasBiggerAndIsSmallerX = oldIx > oldJx && newIx < newJx;
				boolean IWasSmallerAndIsBiggerX = oldIx < oldJx && newIx > newJx;
				
				boolean IWasBiggerAndIsSmallerY = oldIy > oldJy && newIy < newJy;
				boolean IWasSmallerAndIsBiggerY = oldIy < oldJy && newIy > newJy;

				boolean join = (IWasBiggerAndIsSmallerX || IWasSmallerAndIsBiggerX) &&
						(IWasBiggerAndIsSmallerY || IWasSmallerAndIsBiggerY);
				
				if (join) {
					joinMolecules(moleculeNewI, moleculeNewJ);
					return false;
				}
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

	public void countF() {
						
		VectorDouble2D [][] matrix =
				new VectorDouble2D[molecules.size()][molecules.size()];
		
		for (int i = 0; i < molecules.size(); i++) {
			Molecule moI = molecules.get(i);
						
			for (int j = 0; j < molecules.size(); j++) {
				Molecule moJ = molecules.get(j);
				
				if (i == j) {
					VectorDouble2D vectorIJ = new VectorDouble2D();
					vectorIJ.x = 0;
					vectorIJ.y = 0;
					
					matrix[i][j] = vectorIJ;
					break;
				}
				
				double xDiff = moI.x - moJ.x;
				double yDiff = moI.y - moJ.y;
								
				double distance2 = xDiff*xDiff + yDiff*yDiff;
				double f = moI.m*moJ.m / distance2;
				
//				double fx = f * xDiff;
//				double fy = f * yDiff;
			
				double fx = moI.m*moJ.m / xDiff;
				double fy = moI.m*moJ.m / yDiff;

				VectorDouble2D vectorIJ = new VectorDouble2D();
				vectorIJ.x = -fx;
				vectorIJ.y = -fy;

				VectorDouble2D vectorJI = new VectorDouble2D();
				vectorJI.x = fx;
				vectorJI.y = fy;
				
				matrix[i][j] = vectorIJ;
				matrix[j][i] = vectorJI;
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
						
			moleculeI.fx = xDiff;
			moleculeI.fy = yDiff;
		}
		
	}

	public ArrayList<VectorDouble2D> countR() {
		
		ArrayList<VectorDouble2D> a = new ArrayList<VectorDouble2D>();
		
		for (Molecule m : molecules) {
			VectorDouble2D rd = new VectorDouble2D();
			rd.x = m.x;
			rd.y = m.y;
			
			a.add(rd);
		}
		return a;
	}

	boolean IS_PLASTICAL = true;
	private void joinMolecules(Molecule molecule1, Molecule molecule2) {

		// spojeni - plasticka srazka
		if (IS_PLASTICAL) {
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

		} else { // odraz - pruzna srazka
			
			double p1x = molecule1.m * molecule1.x;
			double p1y = molecule1.m * molecule1.y;

			double p2x = molecule2.m * molecule2.x;
			double p2y = molecule2.m * molecule2.y;

			molecule1.vx = p2x / molecule1.m;
			molecule1.vy = p2y / molecule1.m;
			
			molecule2.vx = p1x / molecule2.m;
			molecule2.vy = p1y / molecule2.m;
		}
	}


	public void countRNew(MoleculesModel mmOld) {
		ArrayList<Molecule> moleculesOld = mmOld.getMolecules();

		// spocti rNew
		for (int i = 0; i < moleculesOld.size(); i++) {
			Molecule moleculeOldI = moleculesOld.get(i);
			Molecule moleculeNew = this.molecules.get(i);

			double m = moleculeOldI.m;
			double rx = moleculeOldI.x;
			double ry = moleculeOldI.y;

			double fx = moleculeOldI.fx;
			double fy = moleculeOldI.fy;

			double vx = moleculeOldI.vx;
			double vy = moleculeOldI.vy;

			double h = ConstantsVerlet.h;

			moleculeNew.x = rx + h*vx + h*h/2*fx/m;
			moleculeNew.y = ry + h*vy + h*h/2*fy/m;

		}

	}

	public void countVNew() {
		
		for (int i = 0; i < this.molecules.size(); i++) {
			Molecule moleculeNew = this.molecules.get(i);

			double m = moleculeNew.m;

			double rx = moleculeNew.x;
			double ry = moleculeNew.y;

			double fx = moleculeNew.fOldx;
			double fy = moleculeNew.fOldy;

			double fNewx = moleculeNew.fx;
			double fNewy = moleculeNew.fy;

			double vx = moleculeNew.vOldx;
			double vy = moleculeNew.vOldy;
			
			double h = ConstantsVerlet.h;

			moleculeNew.vx = vx + h*(fx + fNewx)/2*m;
			moleculeNew.vy = vy + h*(fy + fNewy)/2*m;
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
			
			totalE += 1/2 * moleculeI.m * velocity2;
		}
		
		return totalE;
	}

	
	public double countTotalP() {

		double totalP = 0;
		for (int i = 0; i < this.molecules.size(); i++) {
			Molecule moleculeI = this.molecules.get(i);

			double velocity2 = Math.pow(moleculeI.vx, 2) +
					Math.pow(moleculeI.vy, 2);

			totalP += moleculeI.m * Math.pow(velocity2, 0.5);
		}

		return totalP;	
	}


	public void roundOfRNew() {
		for (Molecule mI :this.molecules) {
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
	
	
/*
	private void joinMolecules(Molecule molecule1, Molecule molecule2) {
//		molecule1.m += molecule2.m;
//		molecule1.x = (molecule1.oldx + molecule2.oldx)/2.0;
//		molecule1.y = (molecule1.oldy + molecule2.oldy)/2.0;
		
		double sumM = molecule1.m + molecule2.m;
		double delataX = molecule1.oldx - molecule2.oldx;
		double delataY = molecule1.oldy - molecule2.oldy;
		double delataVX = molecule1.vOldx - molecule2.vOldx;
		double delataVY = molecule1.vOldy - molecule2.vOldy;
		
		molecule1.m = (int)sumM;
		molecule1.x = (1+ delataX / sumM * (double)molecule2.m) * molecule2.oldx;
		molecule1.y = (1+ delataY / sumM * (double)molecule2.m) * molecule2.oldy;
		molecule1.vx = (1+ delataVX / sumM * (double)molecule2.m) * molecule2.vOldx;
		molecule1.vy = (1+ delataVY / sumM * (double)molecule2.m) * molecule2.vOldy;

		molecules.remove(molecule2);
	}
*/

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
				
				Molecule moI = molecules.get(i);
				Molecule moJ = molecules.get(j);

				double distancIJ = distances[i][j];
				if (distancIJ < ConstantsVerlet.minDistace/2) {
//				if (distancIJ < moI.getRadius() + moJ.getRadius()) {

					int[] indexs = new int [2];
					indexs[0] = i;
					indexs[1] = j;

					return indexs;
				}
			}
		}
		return null;
	}

}
