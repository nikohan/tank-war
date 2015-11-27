package test.client.interfaces;

import test.client.impls.DrawThing;


/**
 * 碰撞
 * @author zh
 *
 */
public interface Collide {

	/**
	 * 碰撞管理
	 */
	public void collide(DrawThing self, DrawThing other);
}
