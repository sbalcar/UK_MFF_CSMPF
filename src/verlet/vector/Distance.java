package verlet.vector;

public class Distance extends Vector {

	public Distance (double distanceX, double distanceY) {
		super(distanceX, distanceY);
	}
	
	public double getNorm() {

		return Math.pow(x*x + y*y, 0.5);
	}
}
