import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
	private static String userName = null;
	private static ArrayList<User> activeClients = new ArrayList<>();

	public static void main(String[] args) {
		try {
			serverSocket = new ServerSocket(2222);	// Opret serverSocket
			System.out.println("serverSocket created");
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(true) {	//TODO Create thread. serverSocket.accept() blokerer.
			try {
				System.out.println("Waiting for connection");
				clientSocket = serverSocket.accept();	// Lyt på serverSocket. Blokerer.
				inputStream = new Scanner(clientSocket.getInputStream());
				outputStream = new PrintStream(clientSocket.getOutputStream());
				System.out.println("Got connection from: " + clientSocket.getInetAddress()+":"+clientSocket.getPort());
				} catch (IOException e) {
				e.printStackTrace();
			}
			while (true) {
				message = inputStream.nextLine();
				System.out.println(clientSocket.getInetAddress() + " --> " + message);

				// Parse token
				System.out.println(message.split(" ")[0]);
				switch(message.split(" ")[0]){
					case "QUIT":break;
					case "JOIN":
						userName = message.split(" ")[1];
						System.out.println(userName+ " wants to JOIN");
						if(addUser(new User(userName, clientSocket.getInetAddress(), clientSocket.getLocalPort()))) {
							outputStream.println("J_OK");	// Send besked til client at join er accepteret.
							sendList();						// Send opdateret liste over aktive brugere til alle clienter.
							System.out.println(userName + " accepted");	// Server-debug.
						} else{
							outputStream.println("J_ERR");
							System.out.println(userName+" rejected");
						}
						break;
					case "ALVE":break; //TODO Vedligehold liste over heartbeats.
					case "DATA":
						sendToAll("<"+userName+">"+message.substring( message.indexOf(":")+1 ));	// Besked starter efter det første kolon.
						break;
					default:
						System.out.println("Unknown token");
				}
			}
		}
	}

	/**
	 * Check if username is already in use. If not add to list and return true.
	 * @param enteringUser
	 * @return true if user accepted.
	 */
	static boolean addUser(User enteringUser){	//TODO Check brugernavn og tilføj til liste over brugere.
		for(User u:activeClients) {
			if (u.userName.equals(enteringUser.userName)) return false;	// Brugernavn eksisterer.
		}
		// Brugernavn findes ikke. Tilføj til list og returner true.
		activeClients.add(enteringUser);
		return true;
	}

	static void sendToAll(String message){	//TODO Send noget til alle clienter.
		System.out.println(message);
	}
	static void sendList(){
		//TODO Send userList til alle brugere.
	}
}
