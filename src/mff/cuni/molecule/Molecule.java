package mff.cuni.molecule;



public class Molecule {
	public int m;
	public double density;
	
	public double x;
	public double y;
	
	public double oldx;
	public double oldy;
	
	public double vx;
	public double vy;
	
	public double fx;
	public double fy;

	public double vOldx;
	public double vOldy;

	public double fOldx;
	public double fOldy;


	public Molecule() {
		this.m = -1;
		this.density = 8000.0;
		
		this.x = -1;
		this.y = -1;

		this.oldx = -1;
		this.oldy = -1;
		
		this.vx = 0;
		this.vy = 0;

	}
	
	public Molecule(double x_, double y_, double vx_, double vy_, int m_) {
		this.m = m_;
		this.density = 8000.0;

		this.x = x_;
		this.y = y_;
		
		this.vx = vx_;
		this.vy = vy_;
	}

	public double getRadius() {
		double volume = m / density;
		
		double radius3 = volume / (4/3) / 3.14;
		double radius = Math.pow(radius3, 1.0/3);
		
		return radius;
	}
	
	public double getVscalar() {
		
		double v2 = vx * vx + vy*vy;
		return Math.pow(v2, 0.5);
	}

}



