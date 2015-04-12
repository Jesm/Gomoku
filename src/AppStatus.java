
public enum AppStatus {
	INITIALIZED(0), READY(1), INVITED(2), PLAYING(3), WAITING(4);
	
	private final int value;
	
	AppStatus(int num){
		this.value=num;
	}
}
