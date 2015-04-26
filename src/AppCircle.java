import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;


public class AppCircle{

	public int belongCode;
	
	private AppBoard board;
	private Ellipse2D.Double ellipse;
	public int x, y, d;
	
	public AppCircle(AppBoard b, int x, int y, int diam){
		this.board=b;
		this.belongCode=0;
		this.x=x;
		this.y=y;
		this.d=diam;
	}

	public boolean gotClicked(int x, int y) {
		return this.belongCode==0&&this.ellipse.contains(x, y);
	}
	
	private Color getColor(){
		switch(this.belongCode){
			case 0:
				return App.rockColor;
			default:
				return this.board.getColor(this.belongCode);
		}
	}

	public void paint(Graphics2D g, int multi, int adder) {
		g.setColor(this.getColor());
		this.ellipse=new Ellipse2D.Double(this.x*multi+adder, this.y*multi+adder, this.d, this.d);
		g.fill(this.ellipse);
		g.draw(this.ellipse);		
	}
}
