import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;


public class Com {
	
	protected String[] portas;
	protected Enumeration listaDePortas;
	ArrayList portasCom;
	
	public Com(){
		
		listaDePortas = CommPortIdentifier.getPortIdentifiers();
	
	}
	
	
	public ArrayList ObterPortas(){
		
		portasCom = Collections.list(listaDePortas);
		return portasCom;
	}
	
	
	public String[] ListarPortas(){
		
		int i = 0;
		
		portas = new String[10];
		
		while (listaDePortas.hasMoreElements()){
			CommPortIdentifier ips = (CommPortIdentifier)listaDePortas.nextElement();
			portas[i] = ips.getName();
			i++;
		}
		
		return portas;
	}
	
	public boolean PortaExiste (String COMp){
		
		String temp;
		boolean e = false;
		
		while(listaDePortas.hasMoreElements()){
			CommPortIdentifier ips = (CommPortIdentifier)listaDePortas.nextElement();
			temp = ips.getName();
			
			if (temp.equals(COMp)== true){
				e = true;
			}
		
		}
		
		return e;
	}
	
	
	
	
}