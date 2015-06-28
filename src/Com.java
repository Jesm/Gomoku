import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Enumeration; 
import java.util.TooManyListenersException;


public class Com implements SerialPortEventListener  {

	protected App app;
	protected String name;
	protected SerialPort port;
	
	public static int timeout=100;
	public static int baudrate=9600;
	
//	Documentação:
//	http://docs.oracle.com/cd/E17802_01/products/products/javacomm/reference/api/javax/comm/package-summary.html
	
	public Com(String str, App a) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, TooManyListenersException{
		this.app=a;
		this.name=str;
		CommPortIdentifier id=CommPortIdentifier.getPortIdentifier(str);
		
		this.port = (SerialPort) id.open("Gomoku", timeout);	
//		this.port.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		this.port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		
		this.listen();
	}
	
	private void listen() throws TooManyListenersException{
		this.port.notifyOnDataAvailable(true);
		this.port.addEventListener(this);
	}
	
	public void sendCommand(ComCommand cmd) throws IOException, InterruptedException{
		OutputStream saida = this.port.getOutputStream();
		saida.write(cmd.getBytes());
		Thread.sleep(100);
		saida.flush();
	}

	public void close() {
		this.port.close();
	}
	

	@Override
	public void serialEvent(SerialPortEvent ev) {	
	    switch (ev.getEventType()) {	
	        case SerialPortEvent.BI:	
	        case SerialPortEvent.OE:	
	        case SerialPortEvent.FE:	
	        case SerialPortEvent.PE:	
	        case SerialPortEvent.CD:	
	        case SerialPortEvent.CTS:	
	        case SerialPortEvent.DSR:	
	        case SerialPortEvent.RI:	
	        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:	
	        break;	
	        case SerialPortEvent.DATA_AVAILABLE:
	        	
	        	ByteBuffer buffer=ByteBuffer.allocate(ComCommand.maxLength);
				InputStream entrada = null;
				try {
					entrada = this.port.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				int data=-2;
				while(data!=-1){
	            	if(data!=-2)
		            	buffer.put((byte)data);
	                
	            	try {
						data = entrada.read();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
				
				ComCommand cmd=new ComCommand(buffer);
				this.app.receiveCommand(cmd);
	        break;
	
	    }
		
	}


		
	

	public static String[] ListarPortas(){		
		
		int i = 0;		
		String portas[] = new String[10];
		Enumeration<CommPortIdentifier> listaDePortas = CommPortIdentifier.getPortIdentifiers();
		
		while (listaDePortas.hasMoreElements()){
			CommPortIdentifier ips = listaDePortas.nextElement();
			portas[i] = ips.getName();
			i++;
		}
		
		return portas;
	}
	
	public static boolean PortaExiste (String COMp){
		
		boolean e = false;
		String ports[]=Com.ListarPortas();
		
		for(String temp:ports){
			
			if (COMp.equals(temp)== true){
				e = true;
			}
		
		}
		
		return e;
	}
	
}