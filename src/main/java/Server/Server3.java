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

public class Server3 {
	
	private static Socket Server3_Client;
	private static int  MasterServerPort = 6789;
	private static String filePath = ".\\data\\Server\\Server3\\";
	
	public static void main(String[] args) throws Exception {
		
		sendFile();
		sendFileUDP();
				
	}
	static void sendFile() {
		try {
			
			System.out.println("Server3 running ...");
			Server3_Client = new Socket(InetAddress.getByName("172.16.1.2"),MasterServerPort);
			ListFile ds = new ListFile();
			String dirname = ".\\data\\Server\\Server3\\";
			File F1 = new File(dirname);
			File[] FL = F1.listFiles();
			Arrays.sort(FL);
			ds.setFL(FL);
			ds.setId(3);
			ds.setPortNumber(9878);
			ds.setServerHost(Server3_Client.getLocalAddress());
			ObjectOutputStream os = new ObjectOutputStream(Server3_Client.getOutputStream());
			os.writeObject(ds);
			Server3_Client.close();
			
		}catch (IOException e) {
		
			e.printStackTrace();
		}
	}
	
	static void sendFileUDP() throws Exception {
		
		DatagramSocket serverSocketUDP = new DatagramSocket(9878);
		byte[] receiveData = new byte[1024];
		
		while(true) {
			
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocketUDP.receive(receivePacket);		
			new ServerThreadUDP(serverSocketUDP,filePath,receivePacket).start();		
		}	
	}

}
