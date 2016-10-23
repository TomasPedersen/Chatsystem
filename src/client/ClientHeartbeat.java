package client;

import java.io.PrintStream;

import static client.Client.d;

/**
 * Created by tomas on 10/11/16.
 */
public class ClientHeartbeat implements Runnable {
	private final int HEARTBEAT_TIME_MILLIS = 59000;
	PrintStream streamToServer = null;

	public ClientHeartbeat(PrintStream streamToServer){
		this.streamToServer = streamToServer;
	}

	@Override
	public void run() {
		while(true) {
			streamToServer.println("ALVE");            // Send heartbeat
			d.debug(2, "Heartbeat sent.");
			try {
				Thread.sleep(HEARTBEAT_TIME_MILLIS);    // Vent på at tiden går.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
