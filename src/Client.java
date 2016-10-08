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
	private static boolean JOIN_OK = false;

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
		while (!JOIN_OK) {
			System.out.print("Indtast brugernavn: ");
			//while( !(userNameOK(nick = userInput.nextLine())) );	// Indtast brugernavn og check om nick overholder kravene.
			nick = "Test";
			System.out.println("Dit brugernavn er "+nick);

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
					System.out.println("Join ikke ok");
					break;
				default:
					System.out.println("Ukendt besked: "+message);
			}
		}

		// Main loop
		// Server har accepteret brugernavn.
		do {
			if(userInput.hasNext()) message = userInput.nextLine();	// Tjeck om brugeren har skrevet noget og send det til server. Blokerer ikke.
			outputStream.println("DATA "+nick+": "+message);
			System.out.println("DATA "+nick+": "+message);

			if(inputStream.hasNext()) System.out.println(inputStream.nextLine());	// Tjeck om der er besked fra server. Skriv det ud. Blokerer ikke.
		} while (!message.equals("QUIT"));
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
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
