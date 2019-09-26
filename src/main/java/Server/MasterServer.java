package Server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class MasterServer {
	

@SuppressWarnings("resource")
public static void main(String[] args) throws Exception {
		
		InetAddress ip = InetAddress.getByName("172.16.1.2");
		try{
			ServerSocket welcomeSocket = new ServerSocket(6789,10,ip);
			while (true) {
        	
				Socket socket = welcomeSocket.accept();
				new ServerThread(socket).start();
			}
		}catch(IOException ex) {
			
			ex.printStackTrace();
		}


	}

}
