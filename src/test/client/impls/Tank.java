package test.client.impls;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;

import test.client.TankClient;
import test.client.interfaces.CampType;
import test.client.interfaces.DrawThingType;
import test.client.interfaces.Msg;
import test.client.interfaces.TankDirection;
import test.client.net.TankMoveMsg;

public abstract class Tank extends DrawThing{
	
	private int id;
	
	private int oldX;	//前一单元时间x坐标
	private int oldY;	//前一单元时间y坐标
	
	//判断坦克的方向
	private boolean bL = false, bU = false, bR = false, bD = false;

	//炮筒方向
	private int pDir = TankDirection.U;
	
	private Blood blood;
	
	//子弹集合
	private Vector<Bullet> bullets = new Vector<Bullet>();
	
	public static Map<String, Image> images = new HashMap<String, Image>();
	
	//此处使用静态代码块是为了在类加载时就初始化images，提高读图片的性能
	static {
		String dirPath = "images/"; //目录
		String imageName[][] = {
				{"L", "tankL.gif"},
				{"LU", "tankLU.gif"},
				{"U", "tankU.gif"},
				{"RU", "tankRU.gif"},
				{"R", "tankR.gif"},
				{"RD", "tankRD.gif"},
				{"D", "tankD.gif"},
				{"LD", "tankLD.gif"}
		};
		
		try {
			for(int i = 0; i < imageName.length; i++) {
				images.put(imageName[i][0], ImageIO.read(new File(dirPath + imageName[i][1])));
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public Tank(int x, int y, TankClient tc) {
		super(tc);
		this.setX(x);
		this.setY(y);
		this.setTc(tc);
		
		//每new一个坦克对象则加入到总的坦克向量中去
		tc.tanks.add(this);
		
		oldX = x;
		oldY = y;
		
		blood = new Blood();
		
		//初始化坦克的大小
		this.setWidth(images.get("L").getWidth(null));
		this.setHeight(images.get("L").getHeight(null));
		
		this.setType(DrawThingType.TANK);
		
		//初始化速度
		setSpeedX(5);
		setSpeedY(5);
		
		//初始化矩形对象
		setRec();
		
	}
	
//	//画一辆坦克
//	public void drawTank(Graphics g){
//		
//		//如果死了就不画了
//		if(!this.isLive()) {
//			//从总的坦克向量中去除此对象
//			this.getTc().tanks.remove(this);
//			return;
//		}
////		this.g = g;
////		Color c = this.g.getColor();
////		
////		//区别敌我颜色
////		if(type == TankType.MY) {
////			this.g.setColor(Color.RED);
////		}else {
////			this.g.setColor(Color.blue);
////		}
////		
////		this.g.fillOval(x, y, WIDTH, HEIGHT);
////		this.g.setColor(c);
////		
////		//画出炮筒
////		switch(pDir){
////		case TankDirection.L:
////			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x, y + HEIGHT / 2);
////			break;
////		case TankDirection.LU:
////			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x, y);
////			break;
////		case TankDirection.U:
////			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH / 2, y);
////			break;
////		case TankDirection.RU:
////			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH, y);
////			break;
////		case TankDirection.R:
////			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH, y + HEIGHT / 2);
////			break;
////		case TankDirection.RD:
////			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH, y + HEIGHT);
////			break;
////		case TankDirection.D:
////			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH / 2, y + HEIGHT);
////			break;
////		case TankDirection.LD:
////			g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x, y + HEIGHT);
////			break;
////		case TankDirection.STOP:
////			break;
////		}
//		
//		//画出坦克图片
//		g.drawImage(images.get(dirToString(pDir)), this.getX(), this.getY(), null);
//		getSize(images, pDir);
//		
//		//画出生命条
//		blood.drawBlood(g);
//		
//		//在坦克移动之前记录位置
//		oldX = this.getX();
//		oldY = this.getY();
//		//移动坦克
//		move();	
//	}
	

	
	//坦克移动
	public void move(){
		super.move();
		//确定炮筒的方向
		if(this.getDir() != TankDirection.STOP){
			this.setpDir(this.getDir());
		}
		
		//判断是否出界，如果出界就会停止
		if(getX() < TankClient.GAME_X) setX(TankClient.GAME_X);
		if(getY() < TankClient.GAME_Y + 25) setY(TankClient.GAME_Y + 25);
		if(getX() + getWidth() > TankClient.GAME_WIDTH) setX(TankClient.GAME_WIDTH - getWidth());
		if(getY() + getHeight() > TankClient.GAME_HEIGHT) setY(TankClient.GAME_HEIGHT - getHeight());
	}
	
	//确定坦克方向
	public void locateDirection(){
		int oldDir = this.getDir();
		
		if(bL && !bU && !bR && !bD) this.setDir(TankDirection.L);
		else if(bL && bU && !bR && !bD) this.setDir(TankDirection.LU);
		else if(!bL && bU && !bR && !bD) this.setDir(TankDirection.U);
		else if(!bL && bU && bR && !bD) this.setDir(TankDirection.RU);
		else if(!bL && !bU && bR && !bD) this.setDir(TankDirection.R);
		else if(!bL && !bU && bR && bD) this.setDir(TankDirection.RD);
		else if(!bL && !bU && !bR && bD) this.setDir(TankDirection.D);
		else if(bL && !bU && !bR && bD) this.setDir(TankDirection.LD);
		else if(!bL && !bU && !bR && !bD) this.setDir(TankDirection.STOP);
		
		//当tank改变方向的时候，发送一个TankMoveMsg消息给server
		if(oldDir != this.getDir()) {
			Msg msg = new TankMoveMsg(this.id, this.getDir(), this.getX(), this.getY());
			this.getTc().getNc().send(msg);
		}
		
	}
	
	//开火
	public Bullet shot() {
		Bullet bullet = new Bullet(this.getX(), this.getY(), pDir, this, this.getTc());
		return bullet;
	}
	

//	//处理坦克与子弹碰撞
//	public void hittedByBullets(Vector<Bullet> bullets) {
//		for(int i = 0; i < bullets.size(); i++) {
//			Bullet bullet = bullets.get(i);
//			//如果是同一阵营时，返回
//			if(bullet.getCamp() == this.getCamp()) return;
//			
//			//判断子弹的矩形模型是否与坦克的矩形模型碰撞
//			if(bullet.getRec().intersects(this.getRec())) {
//				if(blood.life > 20) {
//					//生命值减去10
//					blood.life -= 20;
//				}else {
//					//坦克存活标记记为false
//					setLive(false);
//					//爆炸
//					getTc().explodes.add(new Explode(getX(), getY(), getTc()));
//				}
//				
//				//子弹存活标记记为false
//				bullet.setLive(false);
//			}
//		}
//	}
	
//	public void hittedByTanks(Vector<Tank> tanks) {
//		for(int i = 0; i < tanks.size(); i++) {
//			Tank tank = tanks.get(i);
//			if(this != tank && tank.getRec().intersects(this.getRec())) {
//				stay();
//			}
//		}
//	}
	
	//回到老位置
	public void stay() {
		this.setX(this.getOldX());
		this.setY(this.getOldY());
		setDir(TankDirection.STOP);
	}
	
	//重生
	public void relife() {
		setLive(true);
		blood.life = 100;
		setX(50);
		setY(50);
		getTc().tanks.add(this);
		getTc().drawThings.add(this);
	}
	
	private class Blood {
		
		//生命值
		int life = 100;
		
		public void drawBlood(Graphics g) {
			Color c = g.getColor();
//			g.setColor(Color.BLUE);
			g.draw3DRect(getX(), getY() + getHeight(), getWidth(), getHeight() / 5, true);
			if(getCamp() == CampType.MY) {
				g.setColor(Color.BLUE);
			}else if(getCamp() == CampType.ENEMY) {
				g.setColor(Color.RED);
			}else if(getCamp() == CampType.NET) {
				g.setColor(Color.ORANGE);
			}
			
			int remainder =  getWidth() * life / 100;
			g.fillRect(getX(), getY() + getHeight(), remainder, getHeight() / 5);
			g.setColor(c);
		}
	}
	
	public int getOldX() {
		return oldX;
	}

	public void setOldX(int oldX) {
		this.oldX = oldX;
	}

	public int getOldY() {
		return oldY;
	}

	public void setOldY(int oldY) {
		this.oldY = oldY;
	}

	public Blood getBlood() {
		return blood;
	}
	
	public int getBloodLife() {
		return blood.life;
	}
	
	public void setBloodLife(int life) {
		blood.life = life;
	}
	
	public void drawBlood(Graphics g) {
		blood.drawBlood(g);
	}

	public void setBlood(Blood blood) {
		this.blood = blood;
	}

	public int getpDir() {
		return pDir;
	}

	public void setpDir(int pDir) {
		this.pDir = pDir;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isbL() {
		return bL;
	}

	public void setbL(boolean bL) {
		this.bL = bL;
	}

	public boolean isbU() {
		return bU;
	}

	public void setbU(boolean bU) {
		this.bU = bU;
	}

	public boolean isbR() {
		return bR;
	}

	public void setbR(boolean bR) {
		this.bR = bR;
	}

	public boolean isbD() {
		return bD;
	}

	public void setbD(boolean bD) {
		this.bD = bD;
	}

	@Override
	public Rectangle getRec() {
		setRec();
		return super.getRec();
	}

	public Vector<Bullet> getBullets() {
		return bullets;
	}

	public void setBullets(Vector<Bullet> bullets) {
		this.bullets = bullets;
	}
	
}
