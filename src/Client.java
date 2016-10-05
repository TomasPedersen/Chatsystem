import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by tomas on 10/4/16.
 * Connect to server.
 */
public class Client {
    private static Socket clientSocket = null;
    private static PrintStream outputStream = null;
    private static Scanner inputStream = null;


	public static void main(String[] args) {
		try {

			clientSocket = new Socket("localhost", 2222);
			inputStream = new Scanner(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(inputStream.nextLine());
		//TODO Send message to server.
	}
}
