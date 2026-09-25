
import java.io.IOException;
import core.Server;


public class Main {
	public static void main(String[] args) {
		try {
			Server server = new Server(9090);
			server.run();
		}
		catch(IOException e) {
			System.err.println("Internal server error: " + e.getMessage());
		}
	}
}
