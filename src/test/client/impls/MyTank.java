package test.client.impls;

import java.awt.event.KeyEvent;

import test.client.TankClient;
import test.client.interfaces.CampType;

public class MyTank extends Tank{
	

	public MyTank(int x, int y, TankClient tc) {
		super(x, y, tc);
		this.setCamp(CampType.MY);
	}

	//响应键盘事件
	public void keyPressed(KeyEvent e){
		
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_UP:
			this.setbU(true);//向上移动
			break;
		case KeyEvent.VK_DOWN:
			this.setbD(true);//向下移动
			break;
		case KeyEvent.VK_LEFT:
			this.setbL(true);//向左移动
			break;
		case KeyEvent.VK_RIGHT:
			this.setbR(true);//向右移动
			break;
		}
		if(key == KeyEvent.VK_CONTROL && getBullets().size() < 3){
			Bullet bullet = shot();//开火
			//添加到子弹向量中
			getBullets().add(bullet);
		}
		locateDirection();
	}
		


	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_UP:
			this.setbU(false);//向上移动
			break;
		case KeyEvent.VK_DOWN:
			this.setbD(false);//向下移动
			break;
		case KeyEvent.VK_LEFT:
			this.setbL(false);//向左移动
			break;
		case KeyEvent.VK_RIGHT:
			this.setbR(false);//向右移动
			break;
		}
		locateDirection();
	}

}
