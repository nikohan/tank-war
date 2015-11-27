package test.client.impls;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import test.client.TankClient;
import test.client.interfaces.DrawThingType;

public class Explode extends DrawThing{

	
//	public int [] len = {5,7,13,17,25,35,20,10,5};
	
	//images属于explode类，不属于单个对象
	public static List<Image> images = new ArrayList<Image>();
	
	//计数，属于单个对象
	private int count = 0;
	
	//此处使用静态代码块是为了在类加载时就初始化images，提高读图片的性能
	static {
		String dirPath = "images/"; //目录
		String imageName[] = {
				"0.gif",
				"1.gif",
				"2.gif",
				"3.gif",
				"4.gif",
				"5.gif",
				"6.gif",
				"7.gif",
				"8.gif",
				"9.gif",
				"10.gif"
		};
		
		try {
			for(int i = 0; i < imageName.length; i++) {
				images.add(ImageIO.read(new File(dirPath + imageName[i])));
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public Explode(int x, int y, TankClient tc) {
		super(tc);
		this.setX(x);
		this.setY(y);
		this.setTc(tc);
		this.setType(DrawThingType.EXPLODE);
	}
	
//	public void drawExplode(Graphics g) {
//		
//		if(!isLive()) {
//			getTc().explodes.remove(this);
//			return;
//		}
//		
//		if(count == images.size()) {
//			setLive(false);
//			return;
//		}
//		g.drawImage(images.get(count), getX(), getY(), null);
//		count++;
//		
////		Color c = g.getColor();
////		g.setColor(Color.RED);
////		g.fillOval(x, y, len[count], len[count]);
//	
////		g.setColor(c);
//	}
	
	//计数器
	public void countNum() {
		count++;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
