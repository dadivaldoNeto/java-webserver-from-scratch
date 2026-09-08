package server.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;

public class Server {
	private final ServerSocket socketImpl;

	public Server(int port) throws IOException {
		socketImpl = new ServerSocket(port);
	}

	private void accept(int i) throws IOException {
		java.net.Socket socket = socketImpl.accept();
		System.out.println("Request received | Client #" + (i + 1));
		InputStream inputStream = socket.getInputStream();

		byte[] data = inputStream.readAllBytes();

		System.out.print("Client Sent: ");

		for (byte b : data)
			System.out.print((char) b);
		System.out.println();
	}

	public void run() {
		System.out.println("Waiting for connection");
		try {
			int i = 0;
			while (i < 2) {
				accept(i);
				++i;
			}
		} catch (IOException e) {
			System.err.println("Can't accept connection because".concat(e.getMessage()));
		}
	}
}
