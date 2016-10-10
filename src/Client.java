import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by tomas on 10/4/16.
 * Connect to server.
 */
public class Client implements Runnable{
    private static Socket clientSocket = null;
    private static PrintStream outputStream = null;
    private static Scanner inputStream = null;
	private static String nick = null;
	private static Scanner userScanner = null;
	private static String userMessage = null;
	private static boolean JOIN_OK = false;

	public static void main(String[] args) {
		userScanner = new Scanner(System.in);

		//Opret forbindelse til server
		try {
			clientSocket = new Socket("192.168.1.101", 2222);
			//clientSocket = new Socket("localhost", 2222);
			//clientSocket = new Socket("patina.dyndns.dk", 2222);
			inputStream = new Scanner(clientSocket.getInputStream());
			outputStream = new PrintStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Indtast brugernavn
		while (!JOIN_OK) {
			System.out.print("Indtast brugernavn: ");
			while( !(userNameOK(nick = userScanner.nextLine())) );	// Indtast brugernavn og check om nick overholder kravene.

			//Join server.
			outputStream.println("JOIN "+nick);
			Debug.debug(2,"Sent to server:  JOIN "+nick);

			// Vent på J_OK eller J_ERR
			// Fortsæt til main loop ved J_OK, ellers gentag indtast brugernavn.
			userMessage = inputStream.nextLine();
			switch (userMessage){
				case "J_OK":
					System.out.println("Join accepteret af server");
					JOIN_OK = true;
					break;
				case "J_ERR":
					System.out.println("Brugernavn afvist af server");
					break;
				default:
					System.out.println("Ukendt besked: "+ userMessage);
			}
		}

		// Server har accepteret brugernavn.
		// Start tråd til at læse fra server.
		new Thread(new Client()).start();

		// Main loop. Læser fra tastatur og sender til server.
		Debug.debug(2,"Main loop starter.");
		while(true) {
			userMessage = userScanner.nextLine();    // Tjeck om brugeren har skrevet noget. Blokerer.
			if (userMessage.equals("/quit")) {    // Brugeren har sagt /quit. Send QUIT til server. Luk socket og luk programmet.
				Debug.debug("Bruger sagde /quit");
				outputStream.println("QUIT");
				try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);    // Afslut clienten.
			}

			// Normal besked. Send teksten til server med DATA + nick foran.
			outputStream.println("DATA " + nick + " " + userMessage);
			Debug.debug(1, "Sent to server: DATA " + nick + " " + userMessage);
		}
	}

	/**
	 * Tråd der læser fra server og skriver til konsol.
	 */
	public void run(){
		String serverMessage = "";
		while(true){	// Parse tokens from server.
			Debug.debug(2, "Waiting for serverMessage.");
			serverMessage = inputStream.nextLine();
			//Debug.debug(2,"Server said: "+serverMessage);
			switch(serverMessage.split(" ")[0]){
				case "DATA":
					String userName = serverMessage.split(" ")[1];
					System.out.println("<"+userName+"> "+serverMessage.substring( 6+userName.length() ));	// DATA plus et mellemrum = 5 tegn.
					break;
				case "LIST":
					System.out.println(serverMessage);
			}
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
