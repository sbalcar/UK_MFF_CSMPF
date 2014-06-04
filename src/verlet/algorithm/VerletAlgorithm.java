package verlet.algorithm;


import verlet.algorithm.Model.JoinType;
import verlet.gui.Gui;


public class VerletAlgorithm {

	public static int NUMBER_OF_MOLECULES = 2;
	public static int NUMBER_OF_GENERATIONS = 20000;
	public static double DIFF_TIME = 10;
	
	private int t = 0;
	private JoinType joinType = JoinType.ELASTIC_JOIN;

	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}

	public void run() {

		System.out.println("Generation: " + t);

		Model model = new Model(joinType);		
		model.generateModel(NUMBER_OF_MOLECULES);
		Gui.setMolecules(model);
		model.print();

		double EtotFirst = model.getEtot();

		for (t = 1; t <= NUMBER_OF_GENERATIONS; t++) {

			System.out.println("Generation: " + t);

			Model modelNew = model.getModelWithNewPosition();
			modelNew.countNewVeloricity(model);

			modelNew.repareCollision(model);
			modelNew.print();
			Gui.setMolecules(modelNew);

			double EtotNew = modelNew.getEtot();
			double error = countError(EtotFirst, EtotNew);
			System.out.println("Error % : " + error);

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			model = modelNew;
		}
	}


	private double countError(double EtotFirst, double EtotNew) {
		
		double diff = EtotFirst - EtotNew;

		double EPercent = diff / EtotFirst * 100;

		return EPercent;
	}
}
