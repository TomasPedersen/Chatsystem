import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
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
						if(addUser(userName)) {
							outputStream.println("J_OK");
							System.out.println(userName + " accepted");
						}
						break;
					case "ALVE":break;
					case "DATA":
						sendToAll("<"+userName+"> "+message.substring(message.indexOf(":")));	// Besked starter efter det første kolon.
						break;
					default:
						System.out.println("Unknown token");
				}
			}
		}
	}
	static boolean addUser(String userName){	//TODO Check brugernavn og tilføj til liste over brugere.
		return true;
	}
	static void sendToAll(String message){
		System.out.println(message);
	}
}
