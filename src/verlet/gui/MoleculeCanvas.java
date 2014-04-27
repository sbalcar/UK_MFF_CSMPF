package verlet.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import verlet.algorithm.Model;
import verlet.algorithm.Molecule;
import verlet.vector.Point;
import verlet.vector.Vector;


public class MoleculeCanvas extends JComponent {

	private static final long serialVersionUID = 1L;

	private Model moleculesModel;

	public static int widthOfCanvas = 400;
	public static int heightOfCanvas = 500;

	private int radius = 20;

	public MoleculeCanvas(Model moleculesModel) {
		this.moleculesModel = moleculesModel;
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

    for (int i = 0; i < moleculesModel.getMolecules().size(); i++) {
    	Molecule m = moleculesModel.getMolecules().get(i);
    	Point point = m.getPoint();

	    // yellow circle
	    g.setColor(Color.blue);
	    
	    int radius = (int) (this.radius);

	    Double doubX = point.getX();
	    Double doubY = point.getY();

	    int centerX = this.getWidth()/2 + doubX.intValue();
	    int centerY = this.getHeight()/2 - doubY.intValue();

	    g.fillOval(centerX - radius/2,
	    		centerY - radius/2,
	    		radius, radius);

	    g.setColor(Color.black);
	    Vector vectorF = m.getF();
	    double fx = vectorF.getX();
	    double fy = vectorF.getY();

	    int lineX = (int) (1000*fx);
	    int lineY = (int) (1000*fy);
	    g.drawLine(centerX, centerY,
	    		centerX + 2*lineX, centerY - 2*lineY);

	    g.setColor(Color.red);
	    g.drawString("M:" + m.getM(), centerX -5, centerY +5);

	    g.drawString("ROH", 25, 25);
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
    return new Dimension(MoleculeCanvas.heightOfCanvas,
    		MoleculeCanvas.widthOfCanvas);
  }

  public Dimension getMinimumSize() {
    return getPreferredSize();
  }
}
