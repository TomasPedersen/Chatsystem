package cli;

import client.*;

/**
 * Created by tomas on 10/27/16.
 */
public class RunClient {
	public static void main(String[] args) {
		Client c = new Client();
		c.client("192.168.1.3".split(" "));
	}
}
