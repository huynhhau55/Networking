package Lib;

import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;

public class ListFile implements  Serializable {
	
	private static final long serialVersionUID = 1L;
	int id;
	File[] FL;
	int portNumber;
	InetAddress serverHost;
	
	public ListFile() {
	
	}
	
	public ListFile(int id, File[] fL) {
		
		this.id = id;
		FL = fL;
	}

	
	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public InetAddress getServerHost() {
		return serverHost;
	}

	public void setServerHost(InetAddress serverHost) {
		this.serverHost = serverHost;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public File[] getFL() {
		return FL;
	}

	public void setFL(File[] fL) {
		FL = fL;
	}

}
