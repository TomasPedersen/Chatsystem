package client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import util.*;

/**
 * Created by tomas on 10/4/16.
 * Connect to server.
 */
public class Client implements Runnable{
    private Socket clientSocket = null;
    private PrintStream outputStream = null;
    private Scanner inputStream = null;
	private String nick = null;
	private Scanner userScanner = null;
	private String userMessage = null;
	private String serverMessage = null;
	static Debug d;		// Initialiser Debug object. Skal importeres static i andre klasser.

	public void client(String[] args) {
		// Default værdier hvis intet angivet i args.
		String hostName = "localhost";
		int portNumber = 2222;
		int debugLevel = 0;

		switch (args.length){
			case 3:
				debugLevel = Integer.parseInt(args[2]);
			case 2:
				portNumber = Integer.parseInt(args[1]);
			case 1:
				hostName = args[0];
			case 0:
				System.out.println("Using "+hostName+":"+portNumber+"  debuglevel "+debugLevel);
				break;
			default:
				System.out.println("Usage: client hostname portnumber debuglevel");
		}
		// Create debug object
		d = new Debug(debugLevel);

		//Opret forbindelse til server
		try {
			clientSocket = new Socket(hostName, portNumber);
			inputStream = new Scanner(clientSocket.getInputStream());
			outputStream = new PrintStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// TODO while(!join());	// Vent på at server accepterer join.

		// server.Server har accepteret brugernavn.
		// Start tråd til at læse fra server.
		new Thread(new Client()).start();
		// Start ClientHeartbeat tråd.
		new Thread(new ClientHeartbeat(outputStream)).start();


		// Main loop. Læser fra tastatur og sender til server.
		d.debug(2,"Main loop starter.");
		while(true) {
			userMessage = userScanner.nextLine();    // Tjeck om brugeren har skrevet noget. Blokerer.
			if (userMessage.equals("/quit")) {    // Brugeren har sagt /quit. Send QUIT til server. Luk socket og luk programmet.
				d.debug("Bruger sagde /quit");
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
			d.debug(1, "Sent to server: DATA " + nick + " " + userMessage);
		}
	}

	/**
	 * Tråd der læser fra server og skriver til konsol.
	 */
	public void run(){
		String serverMessage = "";
		while(true){	// Parse tokens from server.
			d.debug(2, "Waiting for serverMessage.");
			try {
				serverMessage = inputStream.nextLine();
			} catch (NoSuchElementException e) {
				d.debug(1,"No line found. Probably means server closed socket. Exiting.");
				System.exit(0);
			}
			d.debug(2,"Server said: "+serverMessage);
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
