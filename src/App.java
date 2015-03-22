import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JFrame;
public class App extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AppStatus status;
	
	private static int padding=20;
	private static int lineNumber=19;
	private static int rockWidth=20;
	private static int rockMargin=3;
	
	public static void main(String args[]){
		new App();
	}
	
	public App(){
		this.status=AppStatus.STARTED;
		this.createGameEnvironment();
	}
	
	private App createGameMenu(){
		
		return this;
	}
	
	private App createGameEnvironment(){
		this.setTitle("Gomoku");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(null);
		
		
//		this.getContentPane().add(f);
		Dimension screenSize=java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		Dimension appSize=new Dimension(500, 540);		
		this.setBounds((screenSize.width-appSize.width)/2, (screenSize.height-appSize.height)/2, appSize.width, appSize.height);
		
		this.setVisible(true);
		
		return this;
	}
	
	public void paint(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		
		int rhs=(App.rockMargin+App.rockWidth)/2,
		size=rhs*2*(App.lineNumber+1),
		left=App.padding+rhs,
		top=20+App.padding+rhs;
		
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
