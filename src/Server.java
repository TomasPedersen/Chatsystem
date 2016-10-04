import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tomas on 10/4/16.
 */
public class Server {
    static ServerSocket serverSocket = null;
    static Socket clientSocket = null;
    static PrintStream outputStream = null;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(2222);
            clientSocket = serverSocket.accept();
            outputStream = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputStream.println("You are connecting from: " + clientSocket.getInetAddress());
        System.out.println(clientSocket.getInetAddress());
    }
}
