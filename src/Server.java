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
	private static String message = "NO MESSAGE";

	public static void main(String[] args) {
		try {
			serverSocket = new ServerSocket(2222);	// Opret serverSocket
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(true) {
			try {
				clientSocket = serverSocket.accept();	// Lyt på serverSocket.
				inputStream = new Scanner(clientSocket.getInputStream());
				System.out.println("Got connection from: " + clientSocket.getInetAddress()+":"+clientSocket.getPort());
				outputStream = new PrintStream(clientSocket.getOutputStream());
				outputStream.println("You are connecting from: " + clientSocket.getInetAddress()+":"+clientSocket.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (true) {
				message = inputStream.nextLine();
				System.out.println(clientSocket.getInetAddress() + " --> " + message);

				if (message.equals("QUIT")) {
					try {
						clientSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}
}
