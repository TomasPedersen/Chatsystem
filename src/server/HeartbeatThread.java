package server;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;

import static server.Server.d;


/**
 * Created by tomas on 10/10/16.
 */
public class HeartbeatThread extends Thread {
	ArrayList<UserThread> userThreads = null;
	private static final long HEARTBEATE_TIMEOUT_SECONDS = 60;

	// Constructor
	public HeartbeatThread(ArrayList<UserThread> userThreads){
		this.userThreads = userThreads;
	}

	@Override
	public void run() {
		while(true){
<<<<<<< Updated upstream:src/server/HeartbeatThread.java
			d.debug(3, "Heartbeat check");
=======
			util.Debug.debug(3, "Heartbeat check");
>>>>>>> Stashed changes:src/HeartbeatThread.java
			for (UserThread u :
					userThreads) {
				// Check for hver userThread om forskellen mellem lastHeartbeat og now() er mere end 59 sekunder.
				// Hvis den er, så luk socket og slet tråd fra userThreads.
				if(u.lastHeartbeat.plusSeconds(HEARTBEATE_TIMEOUT_SECONDS).isBefore(LocalTime.now())) {
					try {
						u.clientSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					userThreads.remove(u);
					d.debug(2, "Thread was removed: "+u);
					break;	// TODO Dette er et hack. Uden break fås ConcurrentModificationException efter sletning af sidste element.
				}
			}
			try {
				Thread.sleep(1000);	// Kør testen en gang hvert sekund.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
