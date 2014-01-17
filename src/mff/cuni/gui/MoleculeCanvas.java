package mff.cuni.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JComponent;

import mff.cuni.config.ConstantsVerlet;
import mff.cuni.config.VectorDouble2D;
import mff.cuni.molecule.Molecule;
import mff.cuni.molecule.MoleculesModel;

public class MoleculeCanvas extends JComponent {
	
	private static final long serialVersionUID = 1L;
	
	private MoleculesModel moleculesModel;
	
	public MoleculeCanvas(MoleculesModel moleculesModel_) {
		moleculesModel = moleculesModel_;
	}
//  private static Color m_tRed = new Color(255, 0, 0, 150);

//  private static Color m_tGreen = new Color(0, 255, 0, 150);

//  private static Color m_tBlue = new Color(0, 0, 255, 150);

//  private static Font monoFont = new Font("Monospaced", Font.BOLD
//      | Font.ITALIC, 36);

//  private static Font sanSerifFont = new Font("SanSerif", Font.PLAIN, 12);

//  private static Font serifFont = new Font("Serif", Font.BOLD, 24);

//  private static ImageIcon java2sLogo = new ImageIcon("java2s.gif");

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    moleculesModel.countF();
    
    for (int i = 0; i < moleculesModel.getMolecules().size(); i++) {
    	Molecule m = moleculesModel.getMolecules().get(i);
    	
	    // yellow circle
	    g.setColor(Color.blue);
	    int centerX = (int) (ConstantsVerlet.widthOfCanvas * m.x);
	    int centerY = (int) (ConstantsVerlet.heightOfCanvas * m.y);
//	    int radius = Constants.sizeOfMolecule * m.m/2;
	    int radius = (int) (ConstantsVerlet.widthOfCanvas * m.getRadius());
	    
	    g.fillOval(centerX - radius/2,
	    		centerY - radius/2,
	    		radius, radius);
	    
	    g.setColor(Color.black);
	    double fx = m.fx;
	    double fy = m.fy;
	    
	    int lineX = (int) (fx / 10);
	    int lineY = (int) (fy / 10);
//	    g.drawLine(centerX, centerY,
//	    		centerX + 2*lineX, centerY + 2*lineY);
	    
	    g.setColor(Color.red);
	    g.drawString("M:" + m.m, centerX -5, centerY +5);

    }
	    
    // magenta circle
//    g.setColor(Color.magenta);
//    g.fillOval(160, 160, 240, 240);

    // paint the icon below blue sqaure
//    int w = java2sLogo.getIconWidth();
//    int h = java2sLogo.getIconHeight();
//    java2sLogo.paintIcon(this, g, 280 - (w / 2), 120 - (h / 2));

    // paint the icon below red sqaure
//    java2sLogo.paintIcon(this, g, 120 - (w / 2), 280 - (h / 2));

    // transparent red square
//    g.setColor(m_tRed);
//    g.fillRect(60, 220, 120, 120);

    // transparent green circle
//    g.setColor(m_tGreen);
//    g.fillOval(140, 140, 120, 120);

    // transparent blue square
//    g.setColor(m_tBlue);
//    g.fillRect(220, 60, 120, 120);

//    g.setColor(Color.black);

//    g.setFont(monoFont);
//    FontMetrics fm = g.getFontMetrics();
//    w = fm.stringWidth("Java Source");
//    h = fm.getAscent();
//    g.drawString("Java Source", 120 - (w / 2), 120 + (h / 4));

//    g.setFont(sanSerifFont);
//    fm = g.getFontMetrics();
//    w = fm.stringWidth("and");
//    h = fm.getAscent();
//    g.drawString("and", 200 - (w / 2), 200 + (h / 4));

//    g.setFont(serifFont);
//    fm = g.getFontMetrics();
//    w = fm.stringWidth("Support.");
//    h = fm.getAscent();
//    g.drawString("Support.", 280 - (w / 2), 280 + (h / 4));
  }

  public Dimension getPreferredSize() {
    return new Dimension(ConstantsVerlet.heightOfCanvas, ConstantsVerlet.widthOfCanvas);
  }

  public Dimension getMinimumSize() {
    return getPreferredSize();
  }
}