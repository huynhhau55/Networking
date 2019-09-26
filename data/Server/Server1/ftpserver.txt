package FTP_SerVer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server_1 {

	public static void main(String[] args) {
		
		DatagramSocket socket = null;
		DatagramPacket inPacket = null;
		DatagramPacket outPacket = null;
		byte[] inBuf, outBuf;
		@SuppressWarnings("unused")
		String msg;
		final int port = 5000;
		
		try {
			
			socket = new DatagramSocket(port);
			while (true) {
				
				System.out.println("\nRunning...\n");
				inBuf = new byte[100];
				inPacket = new DatagramPacket(inBuf, inBuf.length);
				socket.receive(inPacket);
				
				int sourcePort = inPacket.getPort();
				InetAddress sourceAddress = inPacket.getAddress();
				msg = new String(inPacket.getData(),0,inPacket.getLength());
				System.out.println("Client: " + sourceAddress + ":" + sourcePort);
				
				String dirname = ".\\src\\FTP_SerVer\\ListFile.txt";
				File f1 = new File(dirname);
				File fl[] = f1.listFiles();
				
				StringBuilder sb = new StringBuilder("\n");
				int c = 0 ;
				
				for (int i = 0; i < fl.length; i++) {
					
					if(fl[i].canRead()) {
						c++;
						
					}
					sb.append(c+"files found. \n\n");
				
					for (int j = 0; j < fl.length; j++) {
					
						sb.append(fl[j].getName()+" "+fl[j].length() + "Bytes\n");
					
					}
					sb.append("\nEnter file name for dowload: ");
					outBuf = (sb.toString()).getBytes();
					outPacket = new DatagramPacket(outBuf, 0, outBuf.length, sourceAddress, sourcePort);
					socket.send(outPacket);
				
					inBuf = new byte[100];
					inPacket = new DatagramPacket(inBuf, inBuf.length);
					socket.receive(inPacket);
					String fileName = new String(inPacket.getData(), 0, inPacket.getLength());
					System.out.println("Request File: "+fileName);
				
					boolean flis = false;
					int index = -1;
					sb = new StringBuilder("");
					for (int k = 0; k < fl.length; k++) {
					
						if(fl[k].getName().toString().equalsIgnoreCase(fileName)) {
						
							index = i;
							flis = true;
						}
					}
					if(!flis) {
						System.out.println("Error");
						sb.append("Error");
						outBuf = (sb.toString()).getBytes();
						outPacket = new DatagramPacket(outBuf, 0, outBuf.length, sourceAddress, sourcePort);
						socket.send(outPacket);					
						
					}
					else {
						
						try {
							
							File ff = new File(fl[index].getAbsolutePath());
							FileReader fr = new FileReader(ff);
							BufferedReader brf = new BufferedReader(fr);
							String s = null;
							sb = new StringBuilder();
							
							while((s = brf.readLine()) != null) {
								
								sb.append(s);							
							}
							
							if(brf.readLine() == null) {
								
								brf.close();
								System.out.println("File Read Successful. Closing Socket. ");
							}
							outBuf = new byte[100000];
							outBuf = (sb.toString()).getBytes();
							outPacket = new DatagramPacket(outBuf, 0,outBuf.length,sourceAddress,sourcePort);
							socket.send(outPacket);
								
						}catch(IOException ioe) {
							
							System.out.println(ioe);
						}				
					}									
				}				
			}						
		} catch (Exception e) {
			System.out.println("Error\n");
		}
	}
}
