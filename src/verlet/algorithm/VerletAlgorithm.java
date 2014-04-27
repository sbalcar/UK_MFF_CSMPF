package verlet.algorithm;


import verlet.algorithm.Model.JoinType;
import verlet.gui.Gui;
import verlet.vector.Vector;


public class VerletAlgorithm {

	public static int NUMBER_OF_MOLECULES = 2;
	public static int NUMBER_OF_GENERATIONS = 20000;
	public static double DIFF_TIME = 10;

	private int t = 0;

	public void run() {

		System.out.println("Generation: " + t);

		Model model = new Model(JoinType.ELASTIC_JOIN);		
		model.generateModel(NUMBER_OF_MOLECULES);
		Gui.setMolecules(model);
		model.print();

		Vector EtotFirst = model.getEtot();

		for (t = 1; t <= NUMBER_OF_GENERATIONS; t++) {

			System.out.println("Generation: " + t);

			Model modelNew = model.getModelWithNewPosition();
			modelNew.countNewVeloricity(model);

			modelNew.repareCollision(model);
			modelNew.print();
			Gui.setMolecules(modelNew);

			Vector EtotNew = modelNew.getEtot();
			Vector error = countError(EtotFirst, EtotNew);
			error.print("Error % : ");

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			model = modelNew;
		}
	}


	private Vector countError(Vector EtotFirst, Vector EtotNew) {
		
		Vector diff = EtotFirst.minusVector(EtotNew).absolutValue();

		double ExFirst = EtotFirst.getX();
		double EyFirst = EtotFirst.getY();

		double Ediffx = diff.getX();
		double Ediffy = diff.getY();

		double ExPercent = Ediffx / ExFirst * 100;
		double EyPercent = Ediffy /EyFirst  * 100;

		return new Vector(ExPercent, EyPercent);
	}
}
