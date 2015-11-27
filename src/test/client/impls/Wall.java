package test.client.impls;

import test.client.TankClient;
import test.client.interfaces.DrawThingType;

public class Wall extends DrawThing{

	public Wall(int x, int y, int w, int h, TankClient tc) {
		super(tc);
		setX(x);
		setY(y);
		setWidth(w);
		setHeight(h);
		
		setRec();
		this.setType(DrawThingType.WALL);
	}
	
//	public void drawWall(Graphics g) {
//		Color c = g.getColor();
//		g.setColor(Color.BLACK);
//		g.fillRect(getX(), getY(), getWidth(), getHeight());
//		g.setColor(c);
//	}
	
//	//处理被子弹击中
//	public void hittedByBullets(Vector<Bullet> bullets) {
//		for(int i = 0; i < bullets.size(); i++) {
//			Bullet bullet = bullets.get(i);
//			if(bullet.getRec().intersects(getRec())) {
//				bullet.setLive(false);
//			}
//		}
//	}
//	
//	//处理被坦克撞到
//	public void hittedByTanks(Vector<Tank> tanks) {
//		for(int i = 0; i < tanks.size(); i++) {
//			Tank tank = tanks.get(i);
//			if(tank.getRec().intersects(getRec())) {
//				tank.stay();
//			}
//		}
//	}
}
