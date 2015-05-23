import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;



public class ComEscuta implements Runnable, SerialPortEventListener {

	
	public String Dadoslidos;

	public int nodeBytes;

	private int baudrate;

	private int timeout;

	private CommPortIdentifier cp;

	private SerialPort portaCom;

	private OutputStream saida;

	private InputStream entrada;

	private Thread threadLeitura;

	private boolean IDPortaOK;

	private boolean PortaOK;

	private boolean Leitura;

	private boolean Escrita;

	private String porta;

	protected String peso;

	
	public ComEscuta(String p, int b,  int t){
		
		this.porta = p;
		
		this.baudrate = b;
		
		this.timeout = t;
	}

	public void setPeso(String peso){
		
		this.peso = peso;
	
	}
	
	public String getPeso(){
		
		return peso;
		
	}
	
	public void HabilitarEscrita(){
		
		Escrita = true;
		
		Leitura = false;
	}
	
	public void HabilitarLeitura(){
	
		Escrita = false;
		
		Leitura = true;
	}
	
	public void ObterIdDaPorta(){
		try{
			cp = CommPortIdentifier.getPortIdentifier(porta);
			
			if(cp == null){
				System.out.println("Erro na porta");
				IDPortaOK = false;
				System.exit(1);
			}
		
			IDPortaOK = true;
		
		}catch(Exception e){
			System.out.println("Erro obtendo ID da porta: " + e);
			IDPortaOK = false;
			System.exit(1);
		}
	}
	
	
	public void AbrirPorta(){
		
		try {
			
			portaCom = (SerialPort)cp.open("ComEscuta", timeout);
			PortaOK = true;
			
			portaCom.setSerialPortParams(baudrate, portaCom.DATABITS_8, portaCom.STOPBITS_1, portaCom.PARITY_NONE);
			portaCom.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			
		} catch (Exception e) {
			
			PortaOK = false;
			System.out.println("Erro abrindo comunicação: " + e);
			System.exit(1);
			
		}
		
	}
	
	public void LerDados(){
		
		if (Escrita == false){
			try {
				
				entrada = portaCom.getInputStream();
				
			} catch (Exception e) {
				
				System.out.println("Erro de stream" + e);
				System.exit(1);
			}
			
			try {
				
				portaCom.addEventListener(this);
				
			} catch (Exception e) {
				
				System.out.println("Erro listener" + e);
				System.exit(1);
			}
			
			portaCom.notifyOnDataAvailable(true);
			
			try {
				
				threadLeitura = new Thread(this);
				threadLeitura.start();
				run();
				
			} catch (Exception e) {
				
				System.out.println("Erro de thread " + e);
			}
		}
	}
	
	public void EnviarUmaString(String msg){
		if (Escrita = true){
			try {
				
				saida = portaCom.getOutputStream();
				System.out.println("Fluxo ok");
				
			} catch (Exception e) {
				
				System.out.println("Erro.status " + e);
			}
			
			try {
				
				System.out.println("Enviando Bits para a porta" + porta);
				System.out.println("Enviando: " + msg);
				saida.write(msg.getBytes());
				Thread.sleep(100);
				saida.flush();
				
			} catch (Exception e) {
				
					System.out.println("Houve um erro no envio");
					System.out.println("Status " + e);
					System.exit(1);
			}
		}
		else{
			
			System.exit(1);
		}
	}
	
	
	
	
	
	public void serialEvent(SerialPortEvent ev) {
		
		StringBuffer bufferLeitura = new StringBuffer();

		int novoDado = 0;
    
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

                //Novo algoritmo de leitura.

                while(novoDado != -1){

                    try{

                        novoDado = entrada.read();

                        if(novoDado == -1){

                            break;

                        }

                        if('\r' == (char)novoDado){

                            bufferLeitura.append('\n');

                        }else{

                            bufferLeitura.append((char)novoDado);

                        }

                    }catch(IOException ioe){

                        System.out.println("Erro de leitura serial: " + ioe);

                    }

                }

                setPeso(new String(bufferLeitura));

                System.out.println(getPeso());

            break;

        }
		
	}


	public void run() {
		
		try {
			
			Thread.sleep(5);
			
		} catch (InterruptedException e) {
			
			System.out.println("Erro de tread " + e);
		}
		
	}
	
	public void FecharCom(){

        try {

            portaCom.close();

        } catch (Exception e) {

            System.out.println("Erro fechando porta: " + e);

            System.exit(0);

        }

	}



	public String obterPorta(){

		return porta;
    
	}



	public int obterBaudrate(){

		return baudrate;

	}	
	
}