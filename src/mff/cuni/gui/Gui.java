package mff.cuni.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import mff.cuni.config.ConstantsVerlet;
import mff.cuni.monteCarlo.MonteCarlo;
import mff.cuni.verlet.MoleculesModel;
import mff.cuni.verlet.Verlet;

public class Gui {

	private static JFrame mainFrame = null;
	private static Container pane = null;
	private static JLabel nameOfAlg = null;
	private static MoleculeCanvas moleculeCanvas = null;
	
	public static void start() {
		
		MoleculesModel model = new MoleculesModel();

		JMenuItem menuItemVerlet = new JMenuItem("Verlet - Dinamika N gravitacnich castic");
        menuItemVerlet.addActionListener(new VerletAL());

		JMenuItem menuItemMC = new JMenuItem("Monte Carlo - Diskety");
        menuItemMC.addActionListener(new MonteCarloAL());

		JMenu menu = new JMenu("Algoritmy");
		menu.add(menuItemVerlet);
		menu.add(menuItemMC);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);
				
		nameOfAlg = new JLabel("Nazev algoritmu");
		nameOfAlg.setBorder(BorderFactory.createEmptyBorder(
                0, (ConstantsVerlet.widthOfCanvas/2 -90), 15, 0));
		nameOfAlg.setFont(new Font("Serif", Font.PLAIN, 24));
		
		
		moleculeCanvas = new MoleculeCanvas(model);		
		moleculeCanvas.setBorder(BorderFactory.createLineBorder(Color.black));
		moleculeCanvas.setSize(ConstantsVerlet.widthOfCanvas,
	    		ConstantsVerlet.heightOfCanvas);
		

	    mainFrame = new JFrame("Pocitacove simulace");
	    mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    mainFrame.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        } );
	    
	    mainFrame.setSize(ConstantsVerlet.widthOfCanvas,
	    		ConstantsVerlet.heightOfCanvas);
	    mainFrame.setJMenuBar(menuBar);
	    pane = mainFrame.getContentPane();
        ((JComponent) pane).setBorder(BorderFactory.createEmptyBorder(40,40,40,40));
	    
	    pane.add(nameOfAlg, BorderLayout.NORTH);
	    pane.add(moleculeCanvas, BorderLayout.CENTER);
		
	    mainFrame.pack();
	    mainFrame.setVisible(true);
	}

	public static void setStartPage() {
		  
		nameOfAlg.setText("Pocitacove simulace");
		
	    pane.repaint();
		mainFrame.pack();
	    mainFrame.repaint();

	}

	
	public static void setMolecules(MoleculesModel moleculeModel) {
	  
		pane = mainFrame.getContentPane();
	    pane.remove(moleculeCanvas);
		moleculeCanvas = new MoleculeCanvas(moleculeModel);		
		moleculeCanvas.setBorder(BorderFactory.createLineBorder(Color.black));
		
		pane = mainFrame.getContentPane();
	    pane.add(moleculeCanvas, BorderLayout.CENTER);
		
	    pane.repaint();
		mainFrame.pack();
	    mainFrame.repaint();

	}
	
}



class VerletAL implements ActionListener {
	  public void actionPerformed(ActionEvent e) {
		  		  
		  Thread vt = new VerletThread();
		  vt.start();

	  }
}


class VerletThread extends Thread {

    public void run() {
    	
		Verlet v = new Verlet();
		v.run();

    }
}


class MonteCarloAL implements ActionListener {
	  public void actionPerformed(ActionEvent e) {
		  		  
		  Thread mct = new MonteCarloThread();
		  mct.start();

	  }
}


class MonteCarloThread extends Thread {

    public void run() {
    	
		MonteCarlo mc = new MonteCarlo();
		mc.run();

    }
}

