package Server;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import Lib.ListFile;

public class Server1 {

	private static Socket Server1_Client;
	private static int  MasterServerPort = 6789;
	private static String filePath = ".\\data\\Server\\Server1\\";
	
	@SuppressWarnings({ "resource", "unused" })
	public static void main(String[] args) throws Exception {
		
		sendFile();
		sendFileUDP();
		
		ServerSocket live = new ServerSocket(1111);
		Socket checkLive = live.accept();
		
				
	}
	static void sendFile() {
		try {
			
			System.out.println("Server1 running ...");
			Server1_Client = new Socket(InetAddress.getByName("172.16.1.2"),MasterServerPort);
			ListFile ds = new ListFile();
			String dirname = ".\\data\\Server\\Server1\\";
			File F1 = new File(dirname);
			File[] FL = F1.listFiles();
			Arrays.sort(FL);
			ds.setFL(FL);
			ds.setId(1);
			ds.setPortNumber(9876);
			ds.setServerHost(Server1_Client.getLocalAddress());
			ObjectOutputStream os = new ObjectOutputStream(Server1_Client.getOutputStream());
			os.writeObject(ds);
			Server1_Client.close();
			
		}catch (IOException e) {
		
			e.printStackTrace();
		}
	}
	static void sendFileUDP() throws Exception {
		
		
			DatagramSocket serverSocketUDP = new DatagramSocket(9876);
			byte[] receiveData = new byte[1024];
			
			while(true) {
				
				receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocketUDP.receive(receivePacket);		
				new ServerThreadUDP(serverSocketUDP,filePath,receivePacket).start();		
			}	
	}
	
}
