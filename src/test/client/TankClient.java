package test.client;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import test.client.impls.Bullet;
import test.client.impls.CollideManager;
import test.client.impls.DrawThing;
import test.client.impls.Drawer;
import test.client.impls.EnemyTank;
import test.client.impls.Explode;
import test.client.impls.MyTank;
import test.client.impls.Tank;
import test.client.impls.Wall;
import test.client.interfaces.CampType;
import test.client.interfaces.DrawThingType;
import test.client.net.NetClient;
import test.client.utils.PropertyMgr;

/**
 * 坦克主窗口
 * @author zh
 *
 */
public class TankClient extends Frame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4710940107472837222L;
	
	//窗口起始坐标
	public static final int X = 200;
	public static final int Y = 100;
	//窗口大小
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	//框架起始坐标
	public static final int GAME_X = 0;
	public static final int GAME_Y = 0;
	//游戏框架宽度
	public static final int GAME_WIDTH = 500;
	//游戏框架高度
	public static final int GAME_HEIGHT = 400;
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
	static TankClient tc;
	
	//线程对象
	Thread thread;
	private PaintThread paintThread;
	
	public Wall wall = null;
	
	//我的坦克
	private MyTank myTank = null;
	
	//我的生命
	static int lifeNum = PropertyMgr.getPropertyInt("lifeNum");
	
	//总的子弹向量
	public Vector<Bullet> bullets = new Vector<Bullet>();
	
	//总的坦克向量
	public Vector<Tank> tanks = new Vector<Tank>();
	
	//爆炸向量
	public Vector<Explode> explodes = new Vector<Explode>();
	
	//可以被画家画出的物体
	public List<DrawThing> drawThings = new ArrayList<DrawThing>();
	
	//定义一个缓冲图片
	Image offScreenImage = null;
	
	//画家
	Drawer drawer;
	
	//碰撞管理者
	CollideManager collideManager;
	
	//连接服务器对象
	NetClient nc;
	
	
	
	public TankClient() {
		//初始化我的坦克
		myTank = new MyTank(50, 50, this);
		
//		//初始化敌人的坦克
//		for(int i = 1; i <= 3; i++) {
//			new EnemyTank(300, i * 80, this);
//		}
		
		//初始化画家
		drawer = new Drawer();
		
		//初始化碰撞管理员
		collideManager = new CollideManager();
		
//		//初始化墙
//		wall = new Wall(200, 0, 50, 200, this);
	}
	
	//双缓冲解决闪烁移动
	//此方法在paint方法调用前调用
	public void update(Graphics g) {
		//判断是否为空
		if(offScreenImage == null){
			//创建于框架一样大小的图片
			offScreenImage = this.createImage(WIDTH, HEIGHT);
		}
		//得到缓冲图片的画笔
		Graphics offGraphics = offScreenImage.getGraphics();
		//解决移动产生一条线问题,用缓冲画笔再次画出一个框架大小的矩形
		Color c = offGraphics.getColor();
		offGraphics.setColor(Color.GREEN);
		offGraphics.fillRect(0, 0, WIDTH, HEIGHT);
		offGraphics.setColor(c);
		//将要画的内容缓冲在此画笔上
		paint(offGraphics);
		//一次性画在屏幕上
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.GREEN);
		g.fill3DRect(GAME_X, GAME_Y, GAME_WIDTH, GAME_HEIGHT, true);
		g.setColor(c);
		
		g.setColor(Color.RED);
		g.drawString("Ctrl键发射子弹", GAME_X + 100, GAME_HEIGHT + 50);
		g.drawString("存活坦克:" + tanks.size(), 600, 50);
		g.drawString("存活子弹:" + bullets.size(), 600, 70);
		g.drawString("爆炸数:" + explodes.size(), 600, 90);
		g.drawString("myTank:old(" + myTank.getOldX() + " , " + myTank.getOldY() + ")", 600, 110);
		g.drawString("current:(" + myTank.getX() + " , " + myTank.getY() + ")", 650, 130);
		g.drawString("我的生命:" + lifeNum , 600, 150);
		g.drawString("drawThings:" + drawThings.size() , 600, 170);
		g.setColor(c);
		
		//画出所有物体
		for(int i = 0; i < drawThings.size(); i++) {
			DrawThing drawThingSelf = drawThings.get(i);
			drawer.draw(g, drawThingSelf);
			
			//碰撞检测
			for(int j = 0; j < drawThings.size(); j++) {
				DrawThing drawThingOther = drawThings.get(j);
				if(drawThingOther != drawThingSelf) {
					collideManager.collide(drawThingSelf, drawThingOther);
				}
				
			}
			
			//敌人坦克随机移动
			if(drawThingSelf.getType() == DrawThingType.TANK && drawThingSelf.getCamp() == CampType.ENEMY) {
				((EnemyTank)drawThingSelf).randomMoveAndShot();
			}
		}
		
//		//画出爆炸
//		for(int i = 0; i < explodes.size(); i++) {
//			Explode explode = explodes.get(i);
//			if(explode != null) {
//				drawer.draw(g, explode);
//			}
//		}
//		//画出所有坦克
//		drawAllTanks(g);
//		
//		//画出所有子弹
//		drawAllBullets(g);
//		
//		//画出墙
//		drawer.draw(g, wall);
//		wall.hittedByBullets(bullets);
//		wall.hittedByTanks(tanks);
	}
	
//	//在战场上画出所有坦克
//	public void drawAllTanks(Graphics g){
//		if(tanks != null) {
//			//遍历总的坦克向量
//			for(int i = 0; i < tanks.size(); i++) {
//				Tank tank = tanks.get(i);
//				
//				if(tank != null){
//					//画一辆坦克
//					drawer.draw(g, tank);
//					//坦克碰撞
//					tank.hittedByTanks(tanks);
//					//坦克与子弹碰撞
//					tank.hittedByBullets(bullets);
//					
//					//敌人坦克随机移动
//					if(tank.getCamp() == CampType.ENEMY) {
//						((EnemyTank)tank).randomMoveAndShot();
//					}
//				}
//			}
//		}
//	
//		
//	}
//	
//	//在战场上画出所有子弹
//	public void drawAllBullets(Graphics g) {
//		if(bullets != null){
//			//画出子弹
//			for(int i = 0; i < bullets.size(); i++){
//				Bullet bullet = bullets.get(i);
//				if(bullet != null){
//					drawer.draw(g, bullet);
//				}
//			}
//		}
//	}
	
	
	//启动框架
	public void lauchFrame() {
		this.setLocation(X, Y);
		this.setSize(WIDTH, HEIGHT);
		this.setTitle("TankWar");
		//添加关闭框架响应事件，采用匿名内部类
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//退出程序
				System.exit(0);
			}
		});
		this.setResizable(false);//设置窗口不可调大小
		this.setBackground(Color.GREEN);
		setVisible(true);
		
		//注册监听
		this.addKeyListener(new KeyMonitor());
		
		paintThread = new PaintThread();
		//启动线程
		thread = new Thread(paintThread);
		thread.start();
		
		//new一个连接服务器对象
		nc = new NetClient(this);
		nc.connect();
	}
	
	//关闭框架
	public void closeFrame() {
		this.setVisible(false);
	}
	
	//重启框架
	public void restartFrame() {
		closeFrame();
//		thread.stop();
		paintThread.isLive = false;
		tc = new TankClient();
		tc.lauchFrame();
	}

	public static void main(String[] args) {
		tc = new TankClient();
		tc.lauchFrame();
	}
	


	/**
	 * 内部类：过一段时间重画一次
	 * @author 赵涵管理员
	 *
	 */
	private class PaintThread implements Runnable {
		//是否停止线程
		boolean isLive = true;

		public void run() {
			while(isLive) {

				//判断是否可以重生
				if(lifeNum > 0 && !tanks.contains(myTank)) {
					myTank.relife();
					lifeNum--;
				}
				
				//重画战场
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 键盘监听内部类
	 * @author 赵涵管理员
	 *
	 */
	private class KeyMonitor extends KeyAdapter{

		public void keyPressed(KeyEvent e) {
			//按键移动
			if(myTank.isLive()) myTank.keyPressed(e);
		}

		public void keyReleased(KeyEvent e) {
			if(myTank.isLive()) myTank.keyReleased(e);
			
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_F2) {
				tc.restartFrame();
			}
		}
		
	}

	public MyTank getMyTank() {
		return myTank;
	}

	public NetClient getNc() {
		return nc;
	}

	public void setNc(NetClient nc) {
		this.nc = nc;
	}

}
