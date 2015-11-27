package test.client.impls;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import test.client.TankClient;
import test.client.interfaces.DrawThingType;

public class Bullet extends DrawThing{

	//属于的哪个坦克的子弹
	private Tank tank;
	
	public static Map<String, Image> images = new HashMap<String, Image>();
	
	//此处使用静态代码块是为了在类加载时就初始化images，提高读图片的性能
	static {
		
		String dirPath = "images/"; //目录
		String imageName[][] = {
				{"L", "missileL.gif"},
				{"LU", "missileLU.gif"},
				{"U", "missileU.gif"},
				{"RU", "missileRU.gif"},
				{"R", "missileR.gif"},
				{"RD", "missileRD.gif"},
				{"D", "missileD.gif"},
				{"LD", "missileLD.gif"}
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
	
	
	public Bullet(int x, int y, int dir, Tank tank, TankClient tc) {
		super(tc);
		this.setDir(dir);
		this.setTc(tc);
		//每new一个子弹对象就向总的子弹向量中添加
		tc.bullets.add(this);
		
		this.tank = tank;
		
		/**
		 * 设置种类
		 */
		this.setType(DrawThingType.BULLET);
		
		//设置阵营
		this.setCamp(this.tank.getCamp());
		
		//初始化子弹大小
		setWidth(images.get(dirToString(dir)).getWidth(null));
		setHeight(images.get(dirToString(dir)).getHeight(null));
		
		localPoint(x, y);
		
		//初始化速度
		setSpeedX(10);
		setSpeedY(10);
		
		//初始化矩形对象
		setRec();
	}

	//定位子弹位置
	public void localPoint(int x, int y){
		setX(x + tank.getWidth() / 2 - getWidth() / 2);
		setY(y + tank.getHeight() / 2 - getHeight() / 2);
	}
	
//	//画一颗子弹
//	public void drawBullet(Graphics g){
//		if(!isLive()) {
//			tank.getBullets().remove(this);
//			getTc().bullets.remove(this);
//			return;
//		}
//		
//		//画出子弹图片
//		g.drawImage(images.get(dirToString(getDir())), getX(), getY(), null);
//		getSize(images, getDir());
//		
//		move();
//		
////		Color c = g.getColor();
////		g.setColor(Color.WHITE);
////		g.fillOval(x, y, WIDTH, HEIGHT);
////		g.setColor(c);
//		
////		System.out.println("x:" + x + ", y:" + y);
//	}
	
	public void move(){
		super.move();
		//判断子弹是否出界
		if(getX() < 0 || getY() < 0 || getX() > TankClient.GAME_WIDTH || getY() > TankClient.GAME_HEIGHT){
			setLive(false);
		}
	}
	
	@Override
	public Rectangle getRec() {
		setRec();
		return super.getRec();
	}

	public Tank getTank() {
		return tank;
	}

	public void setTank(Tank tank) {
		this.tank = tank;
	}
}
