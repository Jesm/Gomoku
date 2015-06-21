import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map.Entry;


public class ComCommand {
	
	protected Integer code;
	public byte args[];
	
	public static int maxLength=8;
	
	public static HashMap<Integer, String> codes;
	static{
		codes = new HashMap<Integer, String>();
		codes.put(1, "invite");
		codes.put(2, "accepted_invite");
		codes.put(3, "move");
    }
	
	public ComCommand(){
		this.code=0;
		this.args=new byte[0];
	}

	public ComCommand(String name){
		this();
		
		for(Entry<Integer, String> entry : ComCommand.codes.entrySet()){
			if(name.equals(entry.getValue()))
				this.code=entry.getKey();
		}
	}

	public ComCommand(String name, byte arr[]){
		this(name);
		this.args=arr;
	}

	public ComCommand(ByteBuffer buffer){
		this();
		
		byte byteArr[]=buffer.array();
		this.code=(int)byteArr[0];
		
		if(byteArr.length>1){
			this.args=new byte[byteArr.length-1];
			for(int x=1;x<byteArr.length;x++)
				this.args[x-1]=byteArr[x];
		}
	}
	
	public String getName() {
		return ComCommand.codes.get(this.code);
	}
	
	public byte[] getBytes(){
		ByteBuffer ret=ByteBuffer.allocate(ComCommand.maxLength);
		ret.put(ComCommand.reduceToByte(this.code));
		
		for(byte b:this.args)
			ret.put(b);
		
		return ret.array();
	}
	
	public static byte reduceToByte(int num){
		byte arr[]=ByteBuffer.allocate(Integer.BYTES).putInt(num).array();
//		System.out.println(arr[0]);
//		System.out.print(arr[1]);
//		System.out.print(arr[2]);
//		System.out.print(arr[3]);
		return arr[3];
	}
}
