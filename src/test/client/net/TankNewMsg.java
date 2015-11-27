package test.client.net;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import test.client.TankClient;
import test.client.impls.MyTank;
import test.client.impls.NetTank;
import test.client.interfaces.Msg;

public class TankNewMsg implements Msg{

	public static final int msgType = Msg.TANK_NEW_MSG;
	
	private MyTank myTank;
	
	private TankClient tc;
	
	public TankNewMsg(TankClient tc) {
		this.tc = tc;
	}
	
	public TankNewMsg(MyTank myTank) {
		this.myTank = myTank;
	}

	public void send(DatagramSocket ds, String address, int port) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		try {
			dos.writeInt(msgType);
			dos.writeInt(myTank.getId());
			dos.writeInt(myTank.getX());
			dos.writeInt(myTank.getY());
			dos.writeInt(myTank.getDir());
			
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

	public void parse(DataInputStream dis) {

		try {
			int id = dis.readInt();
			//如果接收到自己的坦克信息，就返回
			if(id == tc.getMyTank().getId()) {
				return;
			}
			
			int x = dis.readInt();
			int y = dis.readInt();
			int dir = dis.readInt();
			
//			System.out.println("id:" + id + " x:" + x + " y:" + y + " dir:" + dir);
			
			boolean exist = false;
			
			//查询是否已经存在这个坦克
			for(int i = 0; i < tc.tanks.size(); i++) {
				int tankId = tc.tanks.get(i).getId();
				//若存在就退出循环，并把exist赋值为true
				if(tankId == id) {
					exist = true;
					break;
				}
			}
			
			//如果不存在这个坦克，则新加入一个伙伴坦克
			if(!exist) {
				//向前面的client发送自己的主战tank数据
				TankNewMsg myMsg = new TankNewMsg(tc.getMyTank());
				tc.getNc().send(myMsg);
				
				//初始化一个伙伴坦克
				NetTank netTank = new NetTank(x, y, tc, id);
				netTank.setDir(dir);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//parse
	
}
