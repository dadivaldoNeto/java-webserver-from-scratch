package client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
	
	public static void main(String[] args) {

		try {
			String hostname = null;
			Socket socket = new Socket(hostname, 8080);
			OutputStream sendData = socket.getOutputStream();
			sendData.write(args[0].getBytes());
			System.out.println("Data was sent");
			socket.close();
		}
		catch(IOException | ArrayIndexOutOfBoundsException e) {
			System.err.println("Error: ".concat(e.getMessage()));
		}
	}
}
