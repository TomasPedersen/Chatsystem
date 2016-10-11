import java.io.PrintStream;

/**
 * Created by tomas on 10/11/16.
 */
public class ClientHeartbeat implements Runnable {
	public static final int HEARTBEAT_TIMEOUT_MILLIS = 59000;
	PrintStream streamToServer = null;

	public ClientHeartbeat(PrintStream streamToServer){
		this.streamToServer = streamToServer;
	}
	@Override
	public void run() {
		while(true) {
			streamToServer.println("ALVE");            // Send heartbeat
			Debug.debug(2, "Heartbeat sent.");
			try {
				Thread.sleep(HEARTBEAT_TIMEOUT_MILLIS);    // Vent på timeout.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
