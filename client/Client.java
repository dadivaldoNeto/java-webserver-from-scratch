package client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	private static String getInput() {
		Scanner in = new Scanner(System.in);
		String userInput = in.nextLine();
		in.close();
		return userInput;
	}

	public static void main(String[] args) {

		try {
			String hostname = null;
			Socket socket = new Socket(hostname, 8080);
			OutputStream sendData = socket.getOutputStream();
			sendData.write(args[0].getBytes());
			System.out.println("Data was sent");
		
			//sendData.write(getInput().getBytes());
		
			socket.close();
		}
		catch(IOException | ArrayIndexOutOfBoundsException e) {
			System.err.println("Error: ".concat(e.getMessage()));
		}
	}
}
