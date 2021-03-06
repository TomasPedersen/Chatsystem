package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import static server.Server.d;

/**
 * Created by tomas on 10/5/16.
 */
public class UserThread extends Thread{
	public String messageToParse = "NO MESSAGE";
	public String userName;
	public LocalTime lastHeartbeat = null;
	public Socket clientSocket = null;
	public Scanner streamFromClient = null;
	public PrintStream streamToClient = null;

	public ArrayList<UserThread> userThreads = null;

	/**
	 * Ny bruger tilføjes i constructor. Det testes i addUser() om brugernavn er gyldigt. Hvis ikke fjernes tråd fra userThreads.
	 * @param userThreads
	 * @param clientSocket
	 */
	public UserThread(ArrayList<UserThread> userThreads, Socket clientSocket) {
		this.lastHeartbeat = LocalTime.now();
		this.clientSocket = clientSocket;
		this.userThreads = userThreads;
		try {
			this.streamFromClient = new Scanner(clientSocket.getInputStream());
			this.streamToClient = new PrintStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {        // Main loop. Ser kontinuert efter nye beskeder i stream.
			d.debug(2,"Waiting for next line");
			messageToParse = streamFromClient.nextLine();
			d.debug(clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " --> " + messageToParse);

			// Parse for token
			switch (messageToParse.split(" ")[0]) {
				case "QUIT":
					d.debug("case QUIT:");
					// User has quit, remove from list.
					for (UserThread u :
							userThreads) {
						if (u.userName.equals(userName)) userThreads.remove(u);
						break;		// TODO Hack. Undgå ConcurrentModificationException.
					}
					// Send liste til tilbageværende brugere
					sendList();
					// Luk socket
					try {
						clientSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
					// TODO Luk denne tråd.
				case "JOIN":
					d.debug("Case JOIN:");
					userName = messageToParse.split(" ")[1];
					d.debug("JOIN from "+userName);
					if (addUser(userName)) {				// Tjeck brugernavn.
						lastHeartbeat = LocalTime.now();	// Opdater heartbeat. Bliver sat ved connection, men timer ud hvis det tager for lang tid at skrive et navn.
						streamToClient.println("J_OK");		// Send besked til client at join er accepteret.
						sendList();							// Send opdateret liste over aktive brugere til alle clienter.
						d.debug(userName + "@" + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " has joined.");    // server.Server-debug.
					} else {
						streamToClient.println("J_ERR");
						d.debug(userName + " was rejected");
					}
					break;
				case "ALVE":
					d.debug("Heartbeat received from "+clientSocket.getInetAddress());
					this.lastHeartbeat = LocalTime.now();	// Heartbeat modtaget, tid for sidste heartbeat for dette objekt sat til klokken nu.
					break;
				case "DATA":
					d.debug("Case DATA:");
					sendToAll(messageToParse);    // Ved DATA skal beskeden blot sendes videre til alle andre.
					break;
				default:
					d.debug("Unknown token");
			}
		}
	}

	/**
	 * Check if username is already in use. If not add to list and return true.
	 * @param enteringUser
	 * @return true if user accepted.
	 */
	private boolean addUser(String enteringUser) {    //Check brugernavn og tilføj til liste over brugere.
		for (UserThread u : userThreads) {
			if (u.userName.equals(enteringUser)) {
				d.debug("addUser( "+enteringUser+")   u.username: "+u.userName);
				return false;    // Brugernavn eksisterer.
			}
		}
		// Brugernavn findes ikke. Tilføj til liste og returner true.
		userThreads.add(this);	// Tilføj denne tråd til userThreads.
		d.debug(enteringUser + " blev tilføjet til listen over aktive brugere.");
		return true;
	}
	// TODO Skriv removeUser()

	private void sendToAll(String message) {    // Send noget til alle clienter.
		d.debug("sendToAll(" + message + ")");
		for (UserThread u : userThreads) {
			sendMessage(u, message);
		}
	}

	private void sendList() {    // Send liste med alle brugernavne til alle brugere.
		String userList = "LIST";
		for (UserThread u :            // Lav først en tekststreng med alle brugernavne
				userThreads) {
			userList += " "+u.userName;
			d.debug("userList: "+userList);
		}
		// Send derefter til alle brugere.
		sendToAll(userList);
	}

	public void sendMessage(UserThread client, String message) {
		d.debug("sendMessage( "+client+", "+message+" )");
		client.streamToClient.println(message);
	}
}
