package server.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

public class Server {
	private final ServerSocketChannel socketImpl;
	private final Selector selector;

	public Server(int port) throws IOException {
		socketImpl = ServerSocketChannel.open();
		selector = Selector.open();

		InetSocketAddress addr = new InetSocketAddress(port);
		socketImpl.bind(addr);
		socketImpl.configureBlocking(false);

		socketImpl.register(selector, SelectionKey.OP_ACCEPT);
	}

	private void epoll_ctl(SelectableChannel channel, int ops) {
		try {
			channel.configureBlocking(false);
			channel.register(selector, ops);
		}
		catch(IOException e) {
			System.err.println("Error: ".concat(e.getMessage()));
		}
	}

	private void accept() throws IOException {
		SocketChannel socket = socketImpl.accept();
		if (socket == null)
			return ;
		System.out.println("Request received");
		epoll_ctl(socket, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		System.out.print("Client Connected: ");
	}

	public void run() {
		try {
			while (true) {

				System.out.println("[Waiting for a request...]");
				int can_connect = selector.select(800000L);
				if (can_connect == 0)
					continue;
				
				Set<SelectionKey> keys = selector.selectedKeys();
				
				for (var key : keys) {
					SelectableChannel sock = key.channel();
					if (key.isAcceptable())
						accept();
					if (key.isReadable()) {
						System.out.println("Can write! kkk"); 
					}
				}

				selector.selectedKeys().clear();
			}

		} catch (IOException e) {
			System.err.println("Error: ".concat(e.getMessage()));
		}
	}
}
