package FTP_Client;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client_1 {

	public static void main(String[] args) {
		
		DatagramSocket socket = null;
		DatagramPacket inPacket = null;
		DatagramPacket outPacket = null;
		byte[] inBuf, outBuf;
		final int port = 5000;
		String msg = null;
		@SuppressWarnings("resource")
		Scanner src = new Scanner(System.in);
		
		try {
			
			InetAddress address = InetAddress.getByName("127.0.0.1");
			socket = new DatagramSocket();
			
			msg = "";
			outBuf = msg.getBytes();
			outPacket = new DatagramPacket(outBuf, outBuf.length);
			socket.send(outPacket);
			
			inBuf = new byte[65535];
			inPacket = new DatagramPacket(inBuf, inBuf.length);
			socket.receive(inPacket);
			
			String data = new String(inPacket.getData(),0,inPacket.getLength());
			//Print file list
			System.out.println(data);
			
			//Send file name
			String fileName = src.nextLine();
			outBuf = fileName.getBytes();
			outPacket = new DatagramPacket(outBuf, 0, outBuf.length, address,port);
			socket.send(outPacket);
			
			//Receive file
			inBuf = new byte[100000];
			inPacket = new DatagramPacket(inBuf, inBuf.length);
			socket.receive(inPacket);
			
			data = new String(inPacket.getData(),0,inPacket.getLength());
			if(data.endsWith("Error")) {
				
				System.out.println("File doesn't exist\n");
				socket.close();
				
			}
			else{
				
				try {
					
					BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
					pw.write(data);
					pw.close();
					
					System.out.println("File Write Successful. Closing Socket ");
					socket.close();
					
				}
				catch(IOException ioe) {
					
					System.out.println("File Error\n");
					socket.close();
				}
			}
			
			
			
		}catch(Exception e) {
			System.out.println("\nNetwork error. Please try again. \n");
			
		}

	}

}
