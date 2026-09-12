package server.core;

import java.nio.charset.StandardCharsets;
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
//	private ByteBuffer buffer;

	private String httpResponse = """
        HTTP/1.1 200 OK\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 13\r
        Connection: keep-alive\r
        \r
        <p1>Hello, World!</p1>""";

	public Server(int port) throws IOException {
		socketImpl = ServerSocketChannel.open();
		selector = Selector.open();

		socketImpl.bind(new InetSocketAddress(port));
		socketImpl.configureBlocking(false);
		socketImpl.register(selector, SelectionKey.OP_ACCEPT);
//		buffer = ByteBuffer.allocate(MAX_BUFFER);
	}

	/***
	 * 
	 * Read all datas from client socket
	 * 
	 ***/
	private void read(SocketChannel sock) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER);
		System.out.println("Client sent: \n");

		while (true) {
			int read_bytes = sock.read(buffer);
			if (read_bytes <= 0) {
				if (read_bytes == -1) {
					sock.close();
					return ;
				}
				break ;
			}
			buffer.flip();
			while (buffer.hasRemaining()) {
				System.out.print((char)buffer.get());
			}
			System.out.println();
			buffer.clear();
		}
		buffer.clear();
		buffer.put(httpResponse.getBytes(StandardCharsets.UTF_8));
		buffer.flip();
		sock.write(buffer);
	}

	/***
	 * It will accept the connection, then register the client socket into the Selector object
	 ***/
	private void accept(ServerSocketChannel ch) throws IOException {
		SocketChannel socket = ch.accept();
		
		socket.configureBlocking(false);
		socket.register(selector, SelectionKey.OP_READ);
		System.out.println(MessageFormat.format("IP: {0} CONNECTED", socket.getLocalAddress() ));
	}

	public void run() {
		try {
			
			while (true) {
				System.out.println("[Waiting for a request...]");
				if (selector.select(10000L) == 0)
					continue;
				Set<SelectionKey> keys = selector.selectedKeys();
				for (var key : keys) {
					try {
						if (key.isAcceptable() ) {
							if (key.channel() instanceof ServerSocketChannel ch)
								accept(ch);
						}
						if (key.isReadable()) {
							if (key.channel() instanceof SocketChannel sock)
								read(sock);
							else
								System.out.println("Can read input! kkk"); 
						}
					}catch (IOException e) {
						key.channel().close();
						System.err.println("Damn!! Error: ".concat(e.getMessage()));					
					}
				}
				selector.selectedKeys().clear();
			}

		} catch (IOException e) {
			System.err.println("Error: ".concat(e.getMessage()));
		}
	}
}
