
public class Hub {
	
	private static Hub instance;
	
	public static Hub getInstance(){
		if(Hub.instance==null)
			Hub.instance=new Hub();
		return Hub.instance;
	}
	
}
