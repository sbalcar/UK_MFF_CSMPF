package mff.cuni.verlet;



public class Molecule {
	public int m;
	public double density;
	
	public double x;
	public double y;
	
	public double oldx;
	public double oldy;
	
	public double vx;
	public double vy;

	public double vOldx;
	public double vOldy;

	public double fx;
	public double fy;
	
	public double fOldx;
	public double fOldy;

	public int isPrecipitated;

	public Molecule() {
		this.m = 0;
		this.density = 8000.0;

		this.oldx = -1;
		this.oldy = -1;

		this.x = -1;
		this.y = -1;

		this.vOldx = 0;
		this.vOldy = 0;

		this.vx = 0;
		this.vy = 0;

		this.isPrecipitated = -1;
	}

	public double getRadius() {
		double volume = m / density;
		
		double radius3 = volume / (4/3) / 3.14;
		double radius = Math.pow(radius3, 1.0/3);
		
		return radius;
	}
	
	public double getVscalar() {

		double v2 = vx*vx + vy*vy;
		return Math.pow(v2, 0.5);
	}
	
	public void printMolecule() {

		System.out.println("    m:     " + m);

		System.out.println("    oldx:  " + oldx);
		System.out.println("    oldy:  " + oldy);
		
		System.out.println("    fOldx: " + fOldx);
		System.out.println("    fOldy: " + fOldy);
		
		System.out.println("    vOldx: " + vOldx);
		System.out.println("    vOldy: " + vOldy);
		
		System.out.println("    vx:    " + vx);
		System.out.println("    vy:    " + vy);
	}

}



