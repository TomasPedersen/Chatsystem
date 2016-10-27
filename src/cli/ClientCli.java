package cli;

import java.util.Scanner;
import client.*;

/**
 * Created by tomas on 10/27/16.
 */
public class ClientCli {
	private boolean JOIN_OK = false;

	public boolean join(){
// Indtast brugernavn
	Scanner userScanner = new Scanner(System.in);
		while(!JOIN_OK) {

		System.out.print("Indtast brugernavn: ");
		while (!(userNameOK(nick = userScanner.nextLine())))
			;    // Indtast brugernavn og check om nick overholder kravene.

		//Join server.
		outputStream.println("JOIN " + nick);
		d.debug(2, "Sent to server:  JOIN " + nick);

		// Vent på J_OK eller J_ERR
		// Fortsæt til main loop ved J_OK, ellers gentag indtast brugernavn.
		serverMessage = inputStream.nextLine();
		switch (serverMessage) {
			case "J_OK":
				System.out.println("Join accepteret af server");
				JOIN_OK = true;
				break;
			case "J_ERR":
				System.out.println("Brugernavn afvist af server");
				break;
			default:
				System.out.println("Server sendte ukendt besked: " + serverMessage);
		}
	}
		}
}