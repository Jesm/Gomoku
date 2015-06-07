import java.util.HashMap;
import java.util.Map.Entry;


public class ComCommand {
	
	protected Integer code;
	public String args[];
	
	public static HashMap<Integer, String> codes;
	static{
		codes = new HashMap<Integer, String>();
		codes.put(1, "invite");
		codes.put(2, "accepted_invite");
		codes.put(3, "move");
    }
	
	public ComCommand(){
		this.code=0;
		this.args=new String[0];
	}

	public ComCommand(String name){
		this();
		
		for(Entry<Integer, String> entry : ComCommand.codes.entrySet()){
			if(name.equals(entry.getValue()))
				this.code=entry.getKey();
		}
	}

	public ComCommand(String name, String arr[]){
		this(name);
		this.args=arr;
	}

	public ComCommand(StringBuffer b){
		this();
		
		String[] arr=b.toString().split(" ");
		this.code=Integer.parseInt(arr[0]);
		
		if(arr.length>1){
			this.args=new String[arr.length-1];
			for(int x=1;x<arr.length;x++)
				this.args[x-1]=arr[x];
		}
	}
	
	public String getName() {
		return ComCommand.codes.get(this.code);
	}
	
	public String toString(){
		StringBuilder b=new StringBuilder();
		b.append(this.code);
		
		for(String str:this.args)
			b.append(" "+str);
		
		return b.toString();
	}
}
