package server.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.MessageFormat;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

public class Server {
	private final ServerSocketChannel socketImpl;
	private final Selector selector;
	private static final int MAX_BUFFER = 2048;
	private ByteBuffer buffer;


	public Server(int port) throws IOException {
		socketImpl = ServerSocketChannel.open();
		selector = Selector.open();

		socketImpl.bind(new InetSocketAddress(port));
		socketImpl.configureBlocking(false);
		socketImpl.register(selector, SelectionKey.OP_ACCEPT);
		buffer = ByteBuffer.allocate(MAX_BUFFER);
	}


	/***
	 * 
	 * Read all datas from client socket
	 * 
	 ****/
	private void read(SocketChannel sock) throws IOException {
		sock.read(buffer);
		while (buffer.hasRemaining()) {
			System.out.print(buffer.getChar());
		}
		System.out.println();
		buffer.clear();
	}

	/***
	 * It will accept connection, and resgister the socket into the Selector object
	 ***/
	private void accept(ServerSocketChannel ch) throws IOException {
		SocketChannel socket = ch.accept();
		
		socket.configureBlocking(false);
		socket.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		System.out.println(MessageFormat.format("IP: {0} CONNECTED", socket.getLocalAddress() ));
	
		read(socket);
		socket.close();
	}

	public void run() {
		try {
			
			while (true) {
				System.out.println("[Waiting for a request...]");
				if (selector.select(10000L) == 0)
					continue;
				Set<SelectionKey> keys = selector.selectedKeys();
				for (var key : keys) {
					if (key.isAcceptable() ) {
						if (key.channel() instanceof ServerSocketChannel ch)
							accept(ch);
					}
					if (key.isReadable()) {
						System.out.println("Can read input! kkk"); 
					}
				}
				selector.selectedKeys().clear();
			}

		} catch (IOException e) {
			System.err.println("Error: ".concat(e.getMessage()));
		}
	}
}
