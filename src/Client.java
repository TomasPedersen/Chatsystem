import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by tomas on 10/4/16.
 */
public class Client {
    static Socket clientSocket = null;
    static PrintStream outputStream = null;
    static Scanner inputStream = null;


    public static void main(String[] args) {
        try {
            clientSocket = new Socket("localhost", 2222);
            inputStream = new Scanner(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(inputStream.nextLine());
    }
}
