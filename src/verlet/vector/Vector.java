package verlet.vector;

public class Vector {

	protected double x = 0;
	protected double y = 0;

	public Vector(double vXNew, double vYNew) {

		this.x = vXNew;
		this.y = vYNew;
	}

	public void plusToVector(Vector vector) {

		x += vector.getX();
		y += vector.getY();
	}

	public Vector plusVector(Vector vector) {

		double resultX = x + vector.getX();
		double resultY = y + vector.getY();

		return new Vector(resultX, resultY);
	}

	public Vector minusVector(Vector vector) {

		double resultX = x - vector.getX();
		double resultY = y - vector.getY();

		return new Vector(resultX, resultY);
	}

	public Vector multiplyVector(Vector vector) {

		double resultX = x * vector.getX();
		double resultY = y * vector.getY();

		return new Vector(resultX, resultY);
	}

	public Vector absolutValue() {

		double resultX = x;
		double resultY = y;

		if (resultX < 0) {
			resultX *= (-1);
		}
		if (resultY < 0) {
			resultY *= (-1);
		}

		return new Vector(resultX, resultY);
	}
	
	public double getX() {

		return x;
	}
	public double getY() {

		return y;
	}

	public void print(String description) {

		System.out.println(description + " X: " + x);
		System.out.println(description + " Y: " + y);
	}
}
