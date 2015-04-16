import java.awt.Color;
import java.awt.geom.Ellipse2D;


public class AppCircle extends Ellipse2D.Double{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int belongsToPlayer;
	
	public AppCircle(int x, int y, int diam){
		super(x, y, diam, diam);
		this.belongsToPlayer=0;
	}
	
	public void setColor(Color c){
		System.out.println("Teste");
	}
}
