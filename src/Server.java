import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Created by tomas on 10/4/16.
 * Accept connections from clients.
 */
public class Server {
	private static final int MAX_USERS = 10;
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	private static ArrayList<UserThread> userThreads = new ArrayList<>(MAX_USERS);
	private static PrintStream streamToClient = null;

	public static void main(String[] args) {
		new HeartbeatThread(userThreads).start();	// Start heartbeattråd
		try {
			serverSocket = new ServerSocket(2222);    // Opret serverSocket
			System.out.println("serverSocket created on " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {    // Main loop. Vent på connections.
			try {
				Debug.debug("Waiting for connection");
				clientSocket = serverSocket.accept();    // Lyt på serverSocket. Blokerer.
				streamToClient = new PrintStream(clientSocket.getOutputStream());
				Debug.debug("Got connection from: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

				// Undersøg om der er plads til flere brugere.
				if (userThreads.size() >= MAX_USERS) {
					streamToClient.println("Server full");
					Debug.debug(1, "Server full");
					try {
						clientSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {    // Opret og start ny tråd.
					UserThread t = new UserThread(userThreads, clientSocket);
					t.start();	// Start userThread.
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
