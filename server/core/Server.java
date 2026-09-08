package server.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;

public class Server {
	private final ServerSocket socketImpl;

	public Server(int port) throws IOException{
		socketImpl = new ServerSocket(port);
	}

	public void run() {
		System.out.println("Waiting for connection");
		try {
			java.net.Socket socket = socketImpl.accept();
			System.out.println("Request received");
			InputStream inputStream = socket.getInputStream();

			byte[] data = inputStream.readAllBytes();
			
			System.out.print("Client Sent: ");

			for (byte b : data)
				System.out.print((char)b);
			System.out.println();
		} catch (IOException e) {
			System.err.println("Can't accept connection because".concat(e.getMessage()));
		}
	}
}
