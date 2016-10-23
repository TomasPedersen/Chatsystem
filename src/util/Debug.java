package util;

import java.net.Socket;
import java.time.LocalTime;

/**
 * Created by tomas on 10/10/16.
 */
public class Debug {
	public static int DEBUG_LEVEL;	// 0, 1, 2 eller 3 er brugbare værdier.

	public Debug(){DEBUG_LEVEL = 0; }
	public Debug(int debugLevel){DEBUG_LEVEL = debugLevel; }

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
