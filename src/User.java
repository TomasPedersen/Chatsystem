import java.net.InetAddress;
import java.time.LocalTime;

/**
 * Created by tomas on 10/5/16.
 */
public class User {
	public String userName;
	public InetAddress IP_Address;
	public int portNumber;
	public LocalTime lastHeartbeat = null;

	public User(String userName, InetAddress IP_Address, int portNumber) {
		this.userName = userName;
		this.IP_Address = IP_Address;
		this.portNumber = portNumber;
		this.lastHeartbeat = LocalTime.now();
	}
}
