package Client;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import Lib.FileInfo;
import Lib.ListFile;

public class Client2 {
	
	private static Socket Client2;
	private static int  MasterServerPort = 6789;
	
	public static void main(String[] args) {
		
		do {
			
			sendFile();
			
		} while (true);
		

	}
	@SuppressWarnings("unchecked")
	static void sendFile() {
		
		try {
			
			System.out.println("Client2 running ...");
			Client2 = new Socket(InetAddress.getByName("172.16.1.2"),MasterServerPort);
			ListFile ds = new ListFile();
			ds.setId(0);
			ds.setPortNumber(Client2.getLocalPort());
			ds.setServerHost(Client2.getLocalAddress());
			ObjectOutputStream os = new ObjectOutputStream(Client2.getOutputStream());
			os.writeObject(ds);
						
			ObjectInputStream oi = new ObjectInputStream(Client2.getInputStream());
			ArrayList<ListFile> listFiles = new ArrayList<ListFile>();
			listFiles = (ArrayList<ListFile>)oi.readObject();
			File[] fl ;
			int stt = 0;
			String format = "%-10s%-40s%s%n";
			System.out.printf(format, "STT", "File Name", "Size");
			for (int i = 0; i < listFiles.size(); i++) {
				
				fl = listFiles.get(i).getFL();
				for (int j = 0; j < fl.length; j++) {
					
					System.out.printf(format, ++stt, fl[j].getName(), fl[j].length()+" bytes");
					
				}
			}
			System.out.print("Input file dowload: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			int chose = Integer.parseInt(br.readLine());
			stt = 0;
			for (int i = 0; i < listFiles.size(); i++) {
				
				fl = listFiles.get(i).getFL();
				for (int j = 0; j < fl.length; j++) {
					
					if(++stt == chose) {
						
						DatagramSocket clientSocket =new DatagramSocket();
						InetAddress add = listFiles.get(i).getServerHost();
						int portServer = listFiles.get(i).getPortNumber();
						byte[] sendData = new byte[1024];
						sendData = fl[j].getName().getBytes();
						DatagramPacket dp = new DatagramPacket(sendData, sendData.length,add,portServer);
						clientSocket.send(dp);
						reciveFile(portServer,clientSocket,add);						
						clientSocket.close();
						break;
						
					}
					
					
				}
			}
				
			Client2.close();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static void reciveFile(int portNumber,DatagramSocket socket,InetAddress add) throws ClassNotFoundException, IOException {
		
		int PIECES_OF_FILE_SIZE = 1024 * 30;
		byte[] receiveData = new byte[PIECES_OF_FILE_SIZE];
	    DatagramPacket receivePacket;
	    receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData());
        ObjectInputStream ois;
		try {
			
			ois = new ObjectInputStream(bais);	
			FileInfo fileInfo = (FileInfo) ois.readObject();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("D:\\Client2\\" + fileInfo.getFilename()));
			System.out.println("Receiving file...");
			
			for (int i = 0; i < (fileInfo.getPiecesOfFile() - 1); i++) {
				 
				 receivePacket = new DatagramPacket(receiveData, receiveData.length,receivePacket.getAddress(),receivePacket.getPort());
				 socket.receive(receivePacket);
				 bos.write(receiveData, 0, PIECES_OF_FILE_SIZE);
				 
			 }
			 receivePacket = new DatagramPacket(receiveData, receiveData.length,receivePacket.getAddress(),receivePacket.getPort());
			 socket.receive(receivePacket);
		     bos.write(receiveData, 0, fileInfo.getLastByteLength());
		     bos.flush();
		     bos.close();
		     System.out.println("Done!");  

	
		} catch (IOException e) {
			
			e.printStackTrace();
		}


	}
	
	
	
}
