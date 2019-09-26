package Server;
import java.io.*;
import Lib.ListFile;
import java.net.*;
import java.util.ArrayList;

public class ServerThread extends Thread {
	
	private Socket socket;
	private String fileName = ".\\data\\Server\\MasterServer\\";
	public ServerThread(Socket socket) {
		
		this.socket = socket;
	}
	
	public void run() {
		
		try {
			InputStream input = socket.getInputStream();
			ObjectInputStream oInput = new ObjectInputStream(input);
			ListFile listFile = new ListFile();
			try {
				
				listFile = (ListFile)oInput.readObject();
				
				if(listFile.getId() > 0) {
					String filePath = fileName  + listFile.getId() + ".txt";
					ObjectOutputStream oOutput = new ObjectOutputStream(new FileOutputStream(filePath));
					oOutput.writeObject(listFile);
					oOutput.close();
					
				}else if(listFile.getId() == 0) {
					
					ObjectOutputStream oOut = new ObjectOutputStream(socket.getOutputStream());
					ArrayList<ListFile> listFiles = new ArrayList<ListFile>();
					File f = new File(fileName);
					File[] LF = f.listFiles();
					ObjectInputStream oIn = null;
					for (int i = 0; i < LF.length; i++) {
						
						oIn = new ObjectInputStream(new FileInputStream(fileName + LF[i].getName()));	
						ListFile tmp = (ListFile)oIn.readObject();
						listFiles.add(tmp);
						
					}
					if (listFiles.size() > 0) {
						
						oOut.writeObject(listFiles);
						
					}
					else {
						
						oOut.writeBoolean(true);
					}
					oOut.close();
					
					
				}	
				
				
				
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}