package Server;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import Lib.ListFile;

public class Server2 {
	
	private static Socket Server2_Client;
	private static int  MasterServerPort = 6789;
	private static String filePath = ".\\data\\Server\\Server2\\";
	
	public static void main(String[] args) throws Exception {
		
		sendFile();
		sendFileUDP();
				
	}
	static void sendFile() {
		try {
			
			System.out.println("Server2 running ...");
			Server2_Client = new Socket(InetAddress.getByName("172.16.1.2"),MasterServerPort);
			ListFile ds = new ListFile();
			String dirname = ".\\data\\Server\\Server2\\";
			File F1 = new File(dirname);
			File[] FL = F1.listFiles();
			Arrays.sort(FL);
			ds.setFL(FL);
			ds.setId(2);
			ds.setPortNumber(9877);
			ds.setServerHost(Server2_Client.getLocalAddress());
			ObjectOutputStream os = new ObjectOutputStream(Server2_Client.getOutputStream());
			os.writeObject(ds);
			Server2_Client.close();
			
		}catch (IOException e) {
		
			e.printStackTrace();
		}
	}
	
	static void sendFileUDP() throws Exception {
		
		DatagramSocket serverSocketUDP = new DatagramSocket(9877);
		byte[] receiveData = new byte[1024];
		
		while(true) {
			
			receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocketUDP.receive(receivePacket);		
			new ServerThreadUDP(serverSocketUDP,filePath,receivePacket).start();		
		}
	
	
	
}

}
