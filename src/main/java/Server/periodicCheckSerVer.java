package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class periodicCheckSerVer extends Thread {
	
	private Socket connectionSocket;
	
	
	public Socket getConnectionSocket() {
		return connectionSocket;
	}
	public void setConnectionSocket(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}
	
	public periodicCheckSerVer(Socket connectionSocket ) {
		
		this.connectionSocket = connectionSocket;
	}
	
	public void run() {
		
		  try {
			  
			  BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			  String clientSentence = inFromClient.readLine();
			  if(clientSentence.equalsIgnoreCase("Hello")) {
					  
			  }
		
		  } catch (IOException e) {
			e.printStackTrace();
		}		
	}
	

}
