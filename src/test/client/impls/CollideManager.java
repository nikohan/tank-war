package test.client.impls;

import test.client.interfaces.Collide;
import test.client.interfaces.DrawThingType;

public class CollideManager implements Collide{

	public void collide(DrawThing self, DrawThing other) {
		
		if(self.getRec().intersects(other.getRec())) {
			
			if(DrawThingType.TANK == self.getType() && DrawThingType.TANK == other.getType()) {
				
				collideTankByTank((Tank)self, (Tank)other);
				
			} else if(DrawThingType.TANK == self.getType() && DrawThingType.BULLET == other.getType() 
						&& self.getCamp() != other.getCamp()) {
				
				collideTankByBullets((Tank)self, (Bullet)other );
				
			} else if(DrawThingType.TANK == self.getType() && DrawThingType.WALL == other.getType()) {
				
				collideTankByWall((Tank)self);
				
			} else if(DrawThingType.BULLET == self.getType() && DrawThingType.WALL == other.getType()) {
				
				collideBulletByWall((Bullet)self);
				
			}
		}
	}
	
	/**
	 * 坦克与坦克碰撞
	 * @param s
	 * @param o
	 */
	public void collideTankByTank(Tank s, Tank o) {
		s.stay();
		o.stay();
	}
	
	/**
	 * 坦克与子弹碰撞
	 * @param s
	 * @param o
	 */
	public void collideTankByBullets(Tank s, Bullet o) {
		if(s.getBloodLife() > 20) {
			//生命值减去20
			s.setBloodLife(s.getBloodLife() - 20);;
		}else {
			//坦克存活标记记为false
			s.setLive(false);
			//爆炸
			s.getTc().explodes.add(new Explode(s.getX(), s.getY(), s.getTc()));
		}
		
		//子弹存活标记记为false
		o.setLive(false);
	}
	
	/**
	 * 坦克与墙碰撞
	 * @param s
	 */
	public void collideTankByWall(Tank s) {
		s.stay();
	}

	/**
	 * 子弹与墙碰撞
	 * @param s
	 */
	public void collideBulletByWall(Bullet s) {
		s.setLive(false);
	}
	
}
