import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class Hub {
	
	private static Hub instance;
	
	public static Hub getInstance(){
		if(Hub.instance==null)
			Hub.instance=new Hub();
		return Hub.instance;
	}
	
	
	private Map<String, App> ports; 
	
	public Hub(){
		this.ports=new HashMap<String, App>();
	}
	
	public void registerPort(String port, App app){
		this.ports.put(port, app);
	}
	
	public void sendCommand(App caller, HubCommand cmd){
		Collection<App> arr=this.ports.values();
		for(App app:arr){
			if(app!=caller)
				app.receiveCommand(cmd);
		}
	}
	
}
