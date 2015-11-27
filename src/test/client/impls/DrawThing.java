package test.client.impls;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Map;

import test.client.TankClient;
import test.client.interfaces.TankDirection;

public abstract class DrawThing{

	private int x;
	private int y;
	
	//大管家类声明
	private TankClient tc;
	
	private int speedX = 0;
	private int speedY = 0;
	
	//宽度
	private int width;
	//高度
	private int height;
	
	/**
	 * 类型
	 */
	private int type;
	
	/**
	 * 阵营
	 */
	private int camp;
	
	//是否活着
	private boolean isLive = true;
	
	//画笔
	private Graphics g = null;
	
	//方向
	private int dir = TankDirection.STOP;
	
	private Rectangle rec;//矩形对象
	
	public DrawThing(TankClient tc) {
		this.tc = tc;
		rec = new Rectangle(x, y, width, height);
		this.tc.drawThings.add(this);
	}
	
	/**
	 * 将图片大小传给对象的大小
	 * @param dir 方向
	 */
	public void getSize(Map<String, Image> images, int dir) {
		Image image = images.get(dirToString(dir));
		this.setWidth(image.getWidth(null));
		this.setHeight(image.getHeight(null));
	}
	
	//移动
	public void move(){
		switch(dir){
		case TankDirection.L:
			x -= speedX;//向左移动
			break;
		case TankDirection.LU:
			x -= speedX;//向左移动
			y -= speedY;//向上移动
			break;
		case TankDirection.U:
			y -= speedY;//向上移动
			break;
		case TankDirection.RU:
			x += speedX;//向右移动
			y -= speedY;//向上移动
			break;
		case TankDirection.R:
			x += speedX;//向右移动
			break;
		case TankDirection.RD:
			x += speedX;//向右移动
			y += speedY;//向下移动
			break;
		case TankDirection.D:
			y += speedY;//向下移动
			break;
		case TankDirection.LD:
			x -= speedX;//向左移动
			y += speedY;//向下移动
			break;
		case TankDirection.STOP:
			break;
		}
	}
	
	/**
	 * 将方向转换为字符串
	 * @return
	 */
	public String dirToString(int dir) {
		StringBuilder str = new StringBuilder();
		switch(dir){
		case TankDirection.L:
			str.append("L");
			break;
		case TankDirection.LU:
			str.append("LU");
			break;
		case TankDirection.U:
			str.append("U");
			break;
		case TankDirection.RU:
			str.append("RU");
			break;
		case TankDirection.R:
			str.append("R");
			break;
		case TankDirection.RD:
			str.append("RD");
			break;
		case TankDirection.D:
			str.append("D");
			break;
		case TankDirection.LD:
			str.append("LD");
			break;
		case TankDirection.STOP:
			break;
		}
		return str.toString();
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public Graphics getG() {
		return g;
	}

	public void setG(Graphics g) {
		this.g = g;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public TankClient getTc() {
		return tc;
	}

	public void setTc(TankClient tc) {
		this.tc = tc;
	}
	
	public Rectangle getRec() {
		return rec;
	}

	public void setRec() {
		this.rec.setBounds(x, y, width, height);
	}

	public int getCamp() {
		return camp;
	}

	public void setCamp(int camp) {
		this.camp = camp;
	}
	
}
