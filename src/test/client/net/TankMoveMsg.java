package test.client.net;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import test.client.TankClient;
import test.client.impls.Tank;
import test.client.interfaces.Msg;

public class TankMoveMsg implements Msg{

	public static final int msgType = Msg.TANK_MOVE_MSG;
	
	private int id;
	private int dir;
	private int x;
	private int y;
	
	private TankClient tc;
	
	public TankMoveMsg(TankClient tc) {
		this.tc = tc;
	}
	
	public TankMoveMsg(int id, int dir, int x, int y) {
		this.id = id;
		this.dir = dir;
		this.x = x;
		this.y = y;
	}

	public void send(DatagramSocket ds, String address, int port) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
			dos.writeInt(dir);
			dos.writeInt(x);
			dos.writeInt(y);
			
			//转换为字节数组
			byte[] buf = baos.toByteArray();
			
			DatagramPacket dp = new DatagramPacket(buf, buf.length, 
					new InetSocketAddress(address, port));
			ds.send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	//解析流的内容
	public void parse(DataInputStream dis) {

		try {
			int id = dis.readInt();
			//如果接收到自己的坦克信息，就返回
			if(id == tc.getMyTank().getId()) {
				return;
			}
			
			int dir = dis.readInt();
			int x = dis.readInt();
			int y = dis.readInt();
			
//			System.out.println("change dirction:" + dir);
			
			for(Tank tank : tc.tanks) {
				if(id == tank.getId()) {
					tank.setDir(dir);
					tank.setX(x);
					tank.setY(y);
					break;
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//parse
	
}
