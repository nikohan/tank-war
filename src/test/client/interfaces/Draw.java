package test.client.interfaces;

import java.awt.Graphics;

import test.client.impls.DrawThing;

/**
 * 画家能够画出的物体
 * @author zh
 *
 */
public interface Draw {

	/**
	 * 画出物体
	 * @param g 画笔
	 * @param drawThing 物体对象
	 */
	public void draw(Graphics g, DrawThing drawThing);

}
