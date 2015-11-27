package test.client.impls;

import java.awt.Color;
import java.awt.Graphics;

import test.client.TankClient;
import test.client.interfaces.CampType;
import test.client.interfaces.Draw;
import test.client.interfaces.DrawThingType;
import test.client.interfaces.TankDirection;

public class Drawer implements Draw{

	private DrawThing drawThing;
	
	public void draw(Graphics g, DrawThing drawThing) {
		this.drawThing = drawThing;
		switch(drawThing.getType()) {
		case DrawThingType.TANK:
			draw(g, (Tank)drawThing);
			break;
		case DrawThingType.BULLET:
			draw(g, (Bullet)drawThing);
			break;
		case DrawThingType.EXPLODE:
			draw(g, (Explode)drawThing);
			break;
		case DrawThingType.WALL:
			draw(g, (Wall)drawThing);
			break;
		}
	}
	
	/**
	 * 画出坦克
	 * @param g 画笔
	 * @param tank 坦克对象
	 */
	public void draw(Graphics g, Tank tank) {
		// TODO Auto-generated method stub
		//如果死了就不画了
		if(!tank.isLive()) {
			tank.getTc().drawThings.remove(tank);
			//从总的坦克向量中去除此对象
			tank.getTc().tanks.remove(tank);
			return;
		}
		
		//画出坦克图片
		g.drawImage(Tank.images.get(tank.dirToString(tank.getpDir())), tank.getX(), tank.getY(), null);
		tank.getSize(Tank.images, tank.getpDir());
		
//		drawTank(g, tank);
		
		//画出坦克id
		if(tank.getCamp() == CampType.MY) {
			g.drawString("" + ((MyTank)tank).getId(), tank.getX(), tank.getY() + 10);
		}else if(tank.getCamp() == CampType.NET) {
			g.drawString("" + ((NetTank)tank).getId(), tank.getX(), tank.getY() + 10);
		}
		
		
		//画出生命条
		tank.drawBlood(g);
		
		//在坦克移动之前记录位置
		tank.setOldX(tank.getX());
		tank.setOldY(tank.getY());
		//移动坦克
		tank.move();
	}
	
	//画一辆无图片坦克
	public void drawTank(Graphics g, Tank tank){
		
		Color c = g.getColor();
		
		//区别敌我颜色
		if(tank.getType() == CampType.MY) {
			g.setColor(Color.RED);
		}else {
			g.setColor(Color.blue);
		}
		
		g.fillOval(tank.getX(), tank.getY(), tank.getWidth(), tank.getHeight());
		g.setColor(c);
		
		//画出炮筒
		switch(tank.getpDir()){
		case TankDirection.L:
			g.drawLine(tank.getX() + tank.getWidth() / 2, tank.getY() + tank.getHeight() / 2, tank.getX(), tank.getY() + tank.getHeight() / 2);
			break;
		case TankDirection.LU:
			g.drawLine(tank.getX() + tank.getWidth() / 2, tank.getY() + tank.getHeight() / 2, tank.getX(), tank.getY());
			break;
		case TankDirection.U:
			g.drawLine(tank.getX() + tank.getWidth() / 2, tank.getY() + tank.getHeight() / 2, tank.getX() + tank.getWidth() / 2, tank.getY());
			break;
		case TankDirection.RU:
			g.drawLine(tank.getX() + tank.getWidth() / 2, tank.getY() + tank.getHeight() / 2, tank.getX() + tank.getWidth(), tank.getY());
			break;
		case TankDirection.R:
			g.drawLine(tank.getX() + tank.getWidth() / 2, tank.getY() + tank.getHeight() / 2, tank.getX() + tank.getWidth(), tank.getY() + tank.getHeight() / 2);
			break;
		case TankDirection.RD:
			g.drawLine(tank.getX() + tank.getWidth() / 2, tank.getY() + tank.getHeight() / 2, tank.getX() + tank.getWidth(), tank.getY() + tank.getHeight());
			break;
		case TankDirection.D:
			g.drawLine(tank.getX() + tank.getWidth() / 2, tank.getY() + tank.getHeight() / 2, tank.getX() + tank.getWidth() / 2, tank.getY() + tank.getHeight());
			break;
		case TankDirection.LD:
			g.drawLine(tank.getX() + tank.getWidth() / 2, tank.getY() + tank.getHeight() / 2, tank.getX(), tank.getY() + tank.getHeight());
			break;
		case TankDirection.STOP:
			break;
		}
		
	}

	/**
	 * 画出子弹
	 * @param g 画笔
	 * @param bullet 子弹对象
	 */
	public void draw(Graphics g, Bullet bullet) {
		// TODO Auto-generated method stub
		if(!bullet.isLive()) {
			bullet.getTc().drawThings.remove(bullet);
			bullet.getTank().getBullets().remove(bullet);
			bullet.getTc().bullets.remove(bullet);
			return;
		}
		
		//画出子弹图片
		g.drawImage(Bullet.images.get(bullet.dirToString(bullet.getDir())), bullet.getX(), bullet.getY(), null);
		bullet.getSize(Bullet.images, bullet.getDir());
		
		bullet.move();
	}

	/**
	 * 画出爆炸
	 * @param g 画笔
	 * @param explode 爆炸对象
	 */
	public void draw(Graphics g, Explode explode) {
		// TODO Auto-generated method stub
		if(!explode.isLive()) {
			explode.getTc().drawThings.remove(explode);
			explode.getTc().explodes.remove(explode);
			return;
		}
		
		if(explode.getCount() == Explode.images.size()) {
			explode.setLive(false);
			return;
		}
		g.drawImage(Explode.images.get(explode.getCount()), explode.getX(), explode.getY(), null);
		
		//count++
		explode.countNum();
	}

	/**
	 * 画出墙
	 * @param g 画笔
	 * @param wall 墙对象
	 */
	public void draw(Graphics g, Wall wall) {
		// TODO Auto-generated method stub
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillRect(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
		g.setColor(c);
	}

	public DrawThing getDrawThing() {
		return drawThing;
	}

	public void setDrawThing(DrawThing drawThing) {
		this.drawThing = drawThing;
	}

}
