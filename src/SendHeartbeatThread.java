import java.io.PrintStream;
import java.time.LocalTime;

/**
 * Created by tomas on 10/10/16.
 */
public class SendHeartbeatThread extends Thread {
	LocalTime lastHeartbeat = null;
	PrintStream outPutStream = null;
	private static final long HEARTBEAT_TIMEOUT_MILLIS = 59000;

	public SendHeartbeatThread(PrintStream outPutStream){
		this.lastHeartbeat = LocalTime.now();
		this.outPutStream = outPutStream;
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(HEARTBEAT_TIMEOUT_MILLIS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			outPutStream.println("ALVE");	// Send heartbeat.
		}
	}
}
