import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by tomas on 10/5/16.
 */
public class UserThread extends Thread{
	public static final boolean DEBUG = true;
	public String messageToParse = "NO MESSAGE";
	public String userName;
	public LocalTime lastHeartbeat = null;
	public Socket clientSocket = null;
	public Scanner streamFromClient = null;
	public PrintStream streamToClient = null;
	public int userIndex;		// Index i userThreads for denne tråd.

	public ArrayList<UserThread> userThreads = null;

	/**
	 * Ny bruger tilføjes i constructor. Det testes i addUser() om brugernavn er gyldigt. Hvis ikke fjernes tråd fra userThreads.
	 * @param userThreads
	 * @param clientSocket
	 */
	public UserThread(ArrayList<UserThread> userThreads, Socket clientSocket) {
		this.userIndex = userThreads.size()-1;	// Den nye bruger er den sidste i ArrayList<UserThreads>. Så befinder sig på index size()-1.
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
			messageToParse = streamFromClient.nextLine();
			debug(clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " --> " + messageToParse);	// Skriver besked til konsol.

			// Parse for token
			System.out.println(messageToParse.split(" ")[0]);
			switch (messageToParse.split(" ")[0]) {
				case "QUIT":
					debug("case QUIT:");
					break;        //TODO håndtér QUIT for client.
				case "JOIN":
					userName = messageToParse.split(" ")[1];
					if(DEBUG)System.out.println(userName + " sent JOIN");
					if (addUser(userName)) {
						streamToClient.println("J_OK");    // Send besked til client at join er accepteret.
						sendList();                        // Send opdateret liste over aktive brugere til alle clienter.
						debug(userName + "@" + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " has joined.");    // Server-debug.
					} else {
						streamToClient.println("J_ERR");
						System.out.println(userName + " rejected");
						// Brugernavn afvist, fjern denne tråd fra userThreads.
						userThreads.remove(this.userIndex);
						// TODO Dræb tråden.
					}
					break;
				case "ALVE":
					break; //TODO Vedligehold liste over heartbeats.
				case "DATA":
					sendToAll("<" + userName + ">" + messageToParse.substring(messageToParse.indexOf(":") + 1));    // Besked starter efter det første kolon.
					break;
				default:
					debug("Unknown token");
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
			if (u.userName.equals(enteringUser)) return false;    // Brugernavn eksisterer.
		}

		// Brugernavn findes ikke. Tilføj til liste og returner true.
		debug(enteringUser + " blev tilføjet til listen over aktive brugere.");
		return true;
	}

	private void sendToAll(String message) {    // Send noget til alle clienter.
		debug("sendToAll(" + message + ")");
		for (UserThread u : userThreads) {
			sendMessage(u, message);
		}
	}

	private void sendList() {    //TODO Send liste med alle brugernavne til alle brugere.
		String userList = "LIST ";
		for (UserThread u :            // Lav først en tekststreng med alle brugernavne
				userThreads) {
			userList.concat(u.userName);
			debug("sendList, u.userName: " + u.userName);
		}
		// Send derefter til alle brugere.
		sendToAll(userList);

	}

	public static void debug(String debugMessage) {
		if (DEBUG) System.out.println(LocalTime.now() + " " + debugMessage);
	}

	public static void sendMessage(UserThread client, String message) {
		client.streamToClient.println(message);
	}
}
