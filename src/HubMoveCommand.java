
public class HubMoveCommand extends HubCommand {

	public int x, y;
	
	public HubMoveCommand(String str, int x, int y) {
		super(str);
		this.x=x;
		this.y=y;
	}

}
