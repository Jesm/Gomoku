import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;


public class AppBoard extends JPanel{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Graphics2D g2;

	public void paint(Graphics g){
		super.paint(g);		
		this.g2=(Graphics2D)g;
		
		int squareSize=App.rockMargin*2+App.rockDiameter,
		size=squareSize*(App.boardOrder+1);

		this.g2.setColor(App.lineColor);
		for(int x=1;x<=App.boardOrder;x++){
			int num=x*squareSize;
			this.drawLine(0, num, size, num);
			this.drawLine(num, 0, num, size);
			
		}

		this.g2.setColor(App.rockColor);
		for(int x=0;x<App.boardOrder;x++)
			for(int y=0;y<App.boardOrder;y++)
				this.drawCircle(x, y);
		
	}
	
	private void drawLine(int startX, int startY, int endX, int EndY){
		Line2D lin = new Line2D.Float(startX, startY, endX, EndY);
		this.g2.draw(lin);
	}
	
	private void drawCircle(int x, int y){
		int dist=App.rockMargin*2+App.rockDiameter, baseDist=dist-App.rockDiameter/2;
		Ellipse2D.Double c=new AppCircle(x*dist+baseDist, y*dist+baseDist, App.rockDiameter);
		this.g2.draw(c);
		this.g2.fill(c);
	}
}
