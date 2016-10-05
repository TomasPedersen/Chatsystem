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
	private static String nick = null;
	private static Scanner userInput = null;
	private static String message = null;

	public static void main(String[] args) {
		userInput = new Scanner(System.in);

		//Opret forbindelse til server
		try {
			clientSocket = new Socket("localhost", 2222);
			inputStream = new Scanner(clientSocket.getInputStream());
			outputStream = new PrintStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Indtast brugernavn
		System.out.print("Indtast brugernavn: ");
		while( !(userNameOK(nick = userInput.nextLine())) );	// Indtast brugernavn og check om nick overholder kravene.
		System.out.println("Dit brugernavn er "+nick);

		//Join server.
		outputStream.print("JOIN "+nick);

		// Vent på J_OK eller J_ERR
		switch ( message=inputStream.nextLine()){
			case "J_OK":
				System.out.println("Join ok");
				break;
			case "J_ERR":
				System.out.println("Join ikke ok");
				break;
			default:
				System.out.println("Ukendt besked: "+message);
		}
	}
	static boolean userNameOK(String n){
		if(n.length()>12){
			System.out.println("Brugernavn må maksimalt være 12 tegn");
			return false;
		}
		// TODO Check for gyldige tegn, kun bogstaver, tal - og _ er gyldige.
		return true;
	}
}
