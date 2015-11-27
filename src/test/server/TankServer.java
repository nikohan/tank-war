package test.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TankServer {

	public static final int TCP_PORT = 9999;
	
	public static final int UDP_PORT = 6666;
	
	private static int id = 100;
	
	public static List<ClientInfo> clients = new ArrayList<ClientInfo>();
	
	public void start() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(TCP_PORT);
			System.out.println("server started...");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//start UDPThread
		new Thread(new UDPThread()).start();
		
		while(true) {
			Socket client = null;
			try {
//				TimeUnit.MILLISECONDS.sleep(1000);
				//获得client的Socket
				client = ss.accept();
				System.out.println("client : " + client.getInetAddress() + ":" +client.getPort());
				//获得client的地址
				String address = client.getInetAddress().getHostAddress();
				
				//获得转发UDP数据报的协议端口
				DataInputStream dis = new DataInputStream(client.getInputStream());
				int udpPort = dis.readInt();
				
				//将id发送给客户端
				DataOutputStream dos = new DataOutputStream(client.getOutputStream());
				dos.writeInt(id++);
				
				ClientInfo ci = new ClientInfo(address, udpPort);
				System.out.println(ci.address + ":" + ci.udpPort);
				//加入到clients中
				clients.add(ci);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(client != null) {
					try {
						client.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {

		new TankServer().start();
	}

	private class ClientInfo {
		String address;
		int udpPort;
		
		public ClientInfo(String address, int udpPort) {
			this.udpPort = udpPort;
			this.address = address;
		}
	}
	
	private class UDPThread implements Runnable {

		public void run() {
			//接收client的数据缓冲区
			byte[] buf = new byte[1024];
			
			DatagramSocket ds = null;
			
			try {
				//在UDP_PORT接收数据
				ds = new DatagramSocket(UDP_PORT);

				System.out.println("UDPThread started at port : " + UDP_PORT);
				
				while(true) {
					
	//				TimeUnit.MILLISECONDS.sleep(10);
					DatagramPacket dp = new DatagramPacket(buf, buf.length);
					//接收数据报
					ds.receive(dp);
//					System.out.println("a packet received from client : " + 
//								dp.getAddress().getHostAddress());
					
//					String sendClient = dp.getAddress().getHostAddress();
					
					//转发packet
					for(ClientInfo c : clients) {
//						//不转发给发送数据报的客户端
//						if(sendClient.equals(c.address)) {
//							continue;
//						}
						dp.setSocketAddress(new InetSocketAddress(c.address, c.udpPort));
						ds.send(dp);
					}
					
				}//while
			
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//run
		
	}
}
