import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;


public class AppBoard extends JPanel{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void paint(Graphics g){
		super.paint(g);		
		Graphics2D g2=(Graphics2D)g;
		
		int rhs=App.rockMargin+App.rockWidth/2,
		size=rhs*2*(App.lineNumber+1),
		left=App.padding+rhs,
		top=App.padding+rhs;
		
		for(int x=0;x<App.lineNumber;x++){
			int num=(x+1)*rhs*2;
			this.drawLine(g2, left, num+top, left+size, num+top);
			this.drawLine(g2, num+left, top, num+left, top+size);
		}
	}
	
	private void drawLine(Graphics2D g, int startX, int startY, int endX, int EndY){
		Line2D lin = new Line2D.Float(startX, startY, endX, EndY);
		g.draw(lin);
	}
}
