
import java.io.IOException;
import core.Server;


public class Main {
	public static void main(String[] args) {
		try {
			Server server = new Server(8080);
			server.run();
		}
		catch(IOException e) {
			System.out.println("Internal server error");
		}
	}
}
