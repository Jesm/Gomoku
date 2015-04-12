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
		
		int squareSize=App.rockMargin*2+App.rockWidth,
		size=squareSize*(App.boardOrder+1);
		
		for(int x=1;x<=App.boardOrder;x++){
			int num=x*squareSize;
			this.drawLine(g2, 0, num, size, num);
			this.drawLine(g2, num, 0, num, size);
		}
	}
	
	private void drawLine(Graphics2D g, int startX, int startY, int endX, int EndY){
		Line2D lin = new Line2D.Float(startX, startY, endX, EndY);
		g.draw(lin);
	}
}
