package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import util.*;

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
	// Opret debug objekt.
	static Debug d;


	public static void main(String[] args) {
		int portNumber = 2222;
		int debugLevel = 0;

		switch (args.length){
			case 2:
				debugLevel = Integer.getInteger(args[2]);
			case 1:
				portNumber = Integer.getInteger(args[1]);
			case 0:
				System.out.println("Using portnumber "+portNumber+"  debuglevel 0");
				break;
			default:
				System.out.println("Usage: server portnumber debuglevel");
				System.exit(1);
		}
		// Opret debug objekt.
		Debug d = new Debug(debugLevel);

		new HeartbeatThread(userThreads).start();	// Start heartbeattråd
		try {
			serverSocket = new ServerSocket(2222);    // Opret serverSocket
			d.debug(1,"serverSocket created on " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {    // Main loop. Vent på connections.
			try {
				d.debug("Waiting for connection");
				clientSocket = serverSocket.accept();    // Lyt på serverSocket. Blokerer.
				streamToClient = new PrintStream(clientSocket.getOutputStream());
				d.debug("Got connection from: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

				// Undersøg om der er plads til flere brugere.
				if (userThreads.size() >= MAX_USERS) {
					streamToClient.println("server.Server full");
					d.debug(1, "server.Server full");
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
