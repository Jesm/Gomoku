
public enum AppStatus {
	STARTED(0), READY(1), WAITING(2);
	
	private final int value;
	
	AppStatus(int num){
		this.value=num;
	}
}
