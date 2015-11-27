package test.client.interfaces;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface Msg {

	/**
	 * 新加入的tank消息
	 */
	public static int TANK_NEW_MSG = 0;
	
	/**
	 * tank移动的消息
	 */
	public static int TANK_MOVE_MSG = 1;
	
	public void parse(DataInputStream dis);
	
	public void send(DatagramSocket ds, String address, int port);
}
