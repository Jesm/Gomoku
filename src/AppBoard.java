import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
			for(int y=0;y<App.boardOrder;y++)
				this.boardRepresentation[x][y]=new AppCircle(this, x, y, App.rockDiameter);
		}

		this.addMouseListener(this);
	}
	
	public boolean setPlayerRock(int x, int y, int code){
		AppCircle c=this.boardRepresentation[x][y];
		c.belongCode=code;
		
		this.validate();
		this.repaint();
		
		return this.verifySequences(c, code);
	}
	
	private boolean verifySequences(AppCircle c, int code){
		int sequences[][]={
			{1, 0},
			{0, 1},
			{1, 1},
			{1, -1}
		};
		
		for(int x=0;x<sequences.length;x++){
			int v[]=sequences[x];
			List<AppCircle> circles=this.verifyRockSequence(c, v[0], v[1]);
			if(circles.size()==App.victorySequenceLength)
				return true;
		}
		
		return false;
	}
	
	private List<AppCircle> verifyRockSequence(AppCircle c, int x, int y){
		List<AppCircle> ret=new ArrayList<AppCircle>();
		ret.add(c);
		
		boolean avaiable[]={true, true};
		for(int cont=1;avaiable[0]||avaiable[1];cont++){
			for(int direction=0;direction<2;direction++){
				if(!avaiable[direction])
					continue;
				
				int multi=direction==0?1:-1;
				int pos[]={c.x+multi*x*cont, c.y+multi*y*cont};
				
				boolean stop=false;
				for(int axis=0;axis<2;axis++){
					if(0>pos[axis]||pos[axis]>=App.boardOrder)
						stop=true;
				}				
				if(stop){
					avaiable[direction]=false;
					continue;
				}
				
				AppCircle c1=this.boardRepresentation[pos[0]][pos[1]];
				if(c1.belongCode!=c.belongCode){
					avaiable[direction]=false;
					continue;
				}
				else if(direction==0)
					ret.add(c1);
				else if(direction==1)
					ret.add(0, c1);
			}			
		}
		
		return ret;
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
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
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

		int dist=App.rockMargin*2+App.rockDiameter, baseDist=dist-App.rockDiameter/2;
		for(int x=0;x<App.boardOrder;x++){
			for(int y=0;y<App.boardOrder;y++)
				this.boardRepresentation[x][y].paint(g2d, dist, baseDist);
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
					try {
						this.app.getPlayerMove(x, y);
					} catch (IOException | InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
