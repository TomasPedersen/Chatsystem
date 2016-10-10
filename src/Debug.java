import java.net.Socket;
import java.time.LocalTime;

/**
 * Created by tomas on 10/10/16.
 */
public class Debug {
	public static final int DEBUG_LEVEL = 2;

	public static void debug(String debugMessage) {
		debug(1, debugMessage);
	}

	public static void debug(int debugLevel, String debugMessage) {
		if (DEBUG_LEVEL >= debugLevel) {
			System.out.println(LocalTime.now() + " ***DEBUG_LEVEL:" + debugLevel + "***  " + debugMessage);

		}
	}

	public static void debug(String debugMessage, Socket clientSocket){
		debug(1, debugMessage, clientSocket);
	}
	public static void debug(int debugLevel, String debugMessage, Socket clientSocket){
		debug(clientSocket.getInetAddress()+":"+clientSocket.getPort());
	}
}
