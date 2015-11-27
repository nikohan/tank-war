package test.client.net;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import test.client.TankClient;
import test.client.interfaces.Msg;
import test.server.TankServer;

public class NetClient {
	
	private static int UDP_START_PORT = 5555;
	
	//UDP port
	private int udpPort;
	
	//TCP port
	public static final int TCP_PORT = 9999;
	
	//address
	public static final String ADDRESS = "127.0.0.1";
	
	private DatagramSocket ds = null;
	
	
	//大管家对象
	TankClient tc;
	
	public NetClient(TankClient tc) {
		udpPort = UDP_START_PORT++;
		this.tc = tc;
		
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * connect to server
	 */
	public void connect() {
		Socket server = null;
		try {
			server = new Socket(ADDRESS, TCP_PORT);
			System.out.println("link success");
			DataOutputStream dos = new DataOutputStream(server.getOutputStream());
			//传UDP端口给server
			dos.writeInt(udpPort);
			
			//接收server发来的id
			DataInputStream dis = new DataInputStream(server.getInputStream());
			int id = dis.readInt();
			tc.getMyTank().setId(id);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(server != null) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//发送一个坦克信息给server
		Msg msg = new TankNewMsg(tc.getMyTank());
		send(msg);
		
		//start receive thread
		new Thread(new receiveThread()).start();
	}
	
	//client接收线程
	private class receiveThread implements Runnable{

		//初始化缓冲区
		byte[] buf = new byte[1024];
		
		public void run() {

			
			while(true) {
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				ByteArrayInputStream bais = null;
				DataInputStream dis = null;
				try {
					//接收数据报
					ds.receive(dp);
					
					bais = new ByteArrayInputStream(buf, 0, dp.getLength());
					dis = new DataInputStream(bais);
					
					//解析接收的数据报
					parse(dis);
					
					System.out.println("a packet received from server");
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if(dis != null) {
						try {
							dis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}//while
			
		}//run
		
	} 
	
	//解析数据报中的tank数据
	public void parse(DataInputStream dis){
		int msgType = -1;
		Msg msg = null;
		try {
			msgType = dis.readInt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//根据消息类型确定解析的是哪个消息类
		switch(msgType) {
		case Msg.TANK_NEW_MSG:
			msg = new TankNewMsg(tc);
			msg.parse(dis);
			break;
		case Msg.TANK_MOVE_MSG:
			msg = new TankMoveMsg(tc);
			msg.parse(dis);
			break;
		}
	
	}
	
	//发送tank数据
	public void send(Msg msg) {
		msg.send(ds, ADDRESS, TankServer.UDP_PORT);
	}
}
