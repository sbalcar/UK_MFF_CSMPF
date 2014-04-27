package verlet.vector;

import java.util.Random;

public class Point extends Vector {

	public Point() {
		super(0, 0);
		generatPosition();
	}

	public Point(double x, double y) {
		super(x, y);
	}

	public void generatPosition() {

		Random rn = new Random();
		
		int size = 300;
		
		this.x = rn.nextInt(size) -size/2;
		this.y = rn.nextInt(size) -size/2;
	}
	
}
