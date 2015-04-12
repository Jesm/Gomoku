
public enum AppStatus {
	INITIALIZED(0), READY(1), PLAYING(2), WAITING(3);
	
	private final int value;
	
	AppStatus(int num){
		this.value=num;
	}
}
