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
import java.util.Enumeration; 
import java.util.TooManyListenersException;


public class Com implements SerialPortEventListener  {

	protected App app;
	protected String name;
	protected SerialPort port;
	
	public static int timeout=100;
	public static int baudrate=10;
	
	public Com(String str, App a) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, TooManyListenersException{
		this.app=a;
		this.name=str;
		CommPortIdentifier id=CommPortIdentifier.getPortIdentifier(str);
		
		this.port = (SerialPort) id.open("Com", timeout);	
		this.port.setSerialPortParams(baudrate, this.port.DATABITS_8, this.port.STOPBITS_1, this.port.PARITY_NONE);
		this.port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		
		this.listen();
	}
	
	private void listen() throws TooManyListenersException{
		this.port.notifyOnDataAvailable(true);
		this.port.addEventListener(this);
	}
	
	public void sendCommand(ComCommand cmd) throws IOException, InterruptedException{
		OutputStream saida = this.port.getOutputStream();
		saida.write(cmd.toString().getBytes());
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
	        	
	    		StringBuffer buffer=new StringBuffer();
				InputStream entrada = null;
				try {
					entrada = this.port.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				int data=-2;
				while(data!=-1){
	            	if(data!=-2){
	            		char c=(char)data;
		            	if(c=='\r')
		            		c='\n';
		            	buffer.append(c);
	            	}
	                
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