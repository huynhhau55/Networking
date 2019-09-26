package Server;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import Lib.FileInfo;

public class ServerThreadUDP extends Thread {
	
	private static final int PIECES_OF_FILE_SIZE = 1024 * 30;
	private DatagramSocket serverSocket;
	private byte[] sendData;
	private String filePath ;
	private DatagramPacket sendPacket ;
	private DatagramPacket receivePacket;
	
	
	public ServerThreadUDP(DatagramSocket serverSocket, String filePath,DatagramPacket receivePacket) {
		
		this.serverSocket = serverSocket;
		this.filePath = filePath;
		this.receivePacket = receivePacket;
		
	}
	
	public void run() {
		try {
			
			String fileSend = new String(receivePacket.getData());
			File f1 = new File(filePath);
			File[] fl = f1.listFiles();
			
			for (int i = 0; i < fl.length; i++) {
			
				String a = fl[i].getName().toString();
				String b = fileSend.toString();
				String c = a.trim();
				String d = b.trim();
				System.out.println(c);
				System.out.println(d);
				if(c.equalsIgnoreCase(d)) {
					
					InputStream input = new FileInputStream(fl[i].getAbsolutePath());
					BufferedInputStream bfi = new BufferedInputStream(input);
					sendData = new byte[PIECES_OF_FILE_SIZE];
					
					long fileLength = fl[i].length();
					int piecesOfFile = (int)(fileLength / PIECES_OF_FILE_SIZE);
					int lastByteLength = (int)(fileLength % PIECES_OF_FILE_SIZE);
					
					if(lastByteLength > 0) {
						
						piecesOfFile++;
					}
					
					byte[][] fileBytess = new byte[piecesOfFile][PIECES_OF_FILE_SIZE];
			        int count = 0;
			        while (bfi.read(sendData, 0, PIECES_OF_FILE_SIZE) > 0) {
			            fileBytess[count++] = sendData;
			            sendData = new byte[PIECES_OF_FILE_SIZE];
			        }
			        
			        FileInfo fileInfo = new FileInfo();
			        fileInfo.setFilename(fl[i].getName());
			        fileInfo.setFileSize(fl[i].length());
			        fileInfo.setPiecesOfFile(piecesOfFile);
			        fileInfo.setLastByteLength(lastByteLength);
			 
			        
			        ByteArrayOutputStream baos = new ByteArrayOutputStream();
			        ObjectOutputStream oos = new ObjectOutputStream(baos);
			        oos.writeObject(fileInfo);
			        sendPacket = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, receivePacket.getAddress(), receivePacket.getPort());
			        serverSocket.send(sendPacket);
			        
			        System.out.println("Sending file...");
			        
			        for (int j = 0; j < (count - 1); j++) {
			            sendPacket = new DatagramPacket(fileBytess[j], PIECES_OF_FILE_SIZE,receivePacket.getAddress(), receivePacket.getPort());
			            serverSocket.send(sendPacket);
			            waitServer(40);
			        }

			        sendPacket = new DatagramPacket(fileBytess[count - 1], PIECES_OF_FILE_SIZE,receivePacket.getAddress(),receivePacket.getPort());
			        serverSocket.send(sendPacket);
			        waitServer(40);			      
			        bfi.close();
			        System.out.println("Sent");
					break;
				}
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void waitServer(long millisecond) {
	    try {
	        Thread.sleep(millisecond);
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
	
}
