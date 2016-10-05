import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by tomas on 10/4/16.
 * Accept connections from clients.
 */
public class Server {
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	private static PrintStream outputStream = null;
	private static Scanner inputStream = null;

	public static void main(String[] args) {
		try {
			serverSocket = new ServerSocket(2222);
			clientSocket = serverSocket.accept();
			outputStream = new PrintStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		outputStream.println("You are connecting from: " + clientSocket.getInetAddress());
		System.out.println(clientSocket.getInetAddress());
	}
}
