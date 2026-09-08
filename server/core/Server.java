package server.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

public class Server {
	private final ServerSocketChannel socketImpl;
	private  final Selector selector;

	public Server(int port) throws IOException {
		socketImpl = ServerSocketChannel.open();
		selector = Selector.open();
		
		InetSocketAddress addr = new InetSocketAddress(port);
		socketImpl.bind(addr);		
		socketImpl.configureBlocking(false);

		socketImpl.register(selector, SelectionKey.OP_ACCEPT);
	}

	private void accept() throws IOException {
		SocketChannel socket = socketImpl.accept();
		System.out.println("Request received");
		InputStream inputStream = socket.socket().getInputStream();

		byte[] data = inputStream.readAllBytes();

		System.out.print("Client Sent: ");

		for (byte b : data)
			System.out.print((char) b);
		System.out.println();
	}

	public void run() {
		try {
			while (true) {
				System.out.println("[Waiting for a request...]");
				int can_connect = selector.select();
				if (can_connect == 0)
					continue ;
				Set<SelectionKey> keys = selector.selectedKeys();
				for (var key : keys) {
					SelectableChannel sock = key.channel();
					if (key.isAcceptable()) {
						accept();
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Can't accept connection because".concat(e.getMessage()));
		}
	}
}
