import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;


public class AppBoard extends JPanel implements MouseListener{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private App app;
	private AppCircle[][] boardRepresentation;
	
	public AppBoard(App app){
		this.app=app;
		this.boardRepresentation=new AppCircle[App.boardOrder][App.boardOrder];

		for(int x=0;x<App.boardOrder;x++){
			for(int y=0;y<App.boardOrder;y++){
				int dist=App.rockMargin*2+App.rockDiameter, baseDist=dist-App.rockDiameter/2;
				this.boardRepresentation[x][y]=new AppCircle(this, x*dist+baseDist, y*dist+baseDist, App.rockDiameter);
			}
		}

		this.addMouseListener(this);
	}
	
	public boolean setPlayerRock(int x, int y, int code){
		AppCircle c=this.boardRepresentation[x][y];
		c.belongCode=code;
		
		this.validate();
		this.repaint();
		
		return this.verifyRockSequence(c, code);
	}
	
	private boolean verifyRockSequence(AppCircle c, int code){
//		TODO: implementar verificacao
		return false;
	}
	
	public Color getColor(int code){
		switch(code){
			case 1:
				return this.app.color;
			case -1:
				return this.app.opColor;
		}
		
		return null;
	}
	
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d=(Graphics2D)g;	

		int squareSize=App.rockMargin*2+App.rockDiameter,
		size=squareSize*(App.boardOrder+1);

		g2d.setColor(App.lineColor);
		for(int x=1;x<=App.boardOrder;x++){
			int num=x*squareSize;
			Line2D lin = new Line2D.Float(0, num, size, num);
			g2d.draw(lin);
			lin = new Line2D.Float(num, 0, num, size);
			g2d.draw(lin);
			
		}

		for(int x=0;x<App.boardOrder;x++){
			for(int y=0;y<App.boardOrder;y++)
				this.boardRepresentation[x][y].paint(g2d);
		}
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		if(this.app.getStatus()!=AppStatus.PLAYING)
			return;
		
		for(int x=0, posX=e.getX(), posY=e.getY();x<App.boardOrder;x++){
			for(int y=0;y<App.boardOrder;y++){
				AppCircle c=this.boardRepresentation[x][y];
				if(c.gotClicked(posX, posY)){
					this.app.getPlayerMove(x, y);
					return;
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
