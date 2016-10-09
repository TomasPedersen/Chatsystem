import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by tomas on 10/4/16.
 * Connect to server.
 */
public class Client implements Runnable{
	public static final boolean DEBUG = true;
    private static Socket clientSocket = null;
    private static PrintStream outputStream = null;
    private static Scanner inputStream = null;
	private static String nick = null;
	private static Scanner userInput = null;
	private static String message = null;
	private static boolean JOIN_OK = false;

	public static void main(String[] args) {
		userInput = new Scanner(System.in);

		//Opret forbindelse til server
		try {
			clientSocket = new Socket("192.168.1.101", 2222);
//			clientSocket = new Socket("localhost", 2222);
			inputStream = new Scanner(clientSocket.getInputStream());
			outputStream = new PrintStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Indtast brugernavn
		while (!JOIN_OK) {
			System.out.print("Indtast brugernavn: ");
			while( !(userNameOK(nick = userInput.nextLine())) );	// Indtast brugernavn og check om nick overholder kravene.

			//Join server.
			outputStream.println("JOIN "+nick);
			System.out.println("JOIN "+nick);

			// Vent på J_OK eller J_ERR
			// Fortsæt til main loop ved J_OK, ellers gentag indtast brugernavn.
			message = inputStream.nextLine();
			System.out.println(message);
			switch ( message ){
				case "J_OK":
					System.out.println("Join ok");
					JOIN_OK = true;
					break;
				case "J_ERR":
					System.out.println("Brugernavn afvist af server");
					break;
				default:
					System.out.println("Ukendt besked: "+message);
			}
		}

		// Server har accepteret brugernavn.
		// Start tråd til at læse fra server.
		new Thread(new Client()).start();

		// Main loop. Læser fra tastatur og sender til server.
		debug("Main loop starter.");
		do {
			if(userInput.hasNext()) {
				System.out.println("hasNext()");
				message = userInput.nextLine();    // Tjeck om brugeren har skrevet noget og send det til server. Blokerer.
				if(message.equals("QUIT")) {
					outputStream.println("QUIT");
					break;
				}
				outputStream.println("DATA " + nick + ": " + message);
				debug("Sent to server: DATA " + nick + ": " + message);
			}


		} while (!message.equals("QUIT"));

		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tråd der læser fra server og skriver til konsol.
	 */
	public void run(){
		while(true){
			System.out.println(inputStream.nextLine());
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
	public static void debug(String debugMessage){
		if(DEBUG) System.out.println("**DEBUG**  "+ debugMessage);
	}
}
