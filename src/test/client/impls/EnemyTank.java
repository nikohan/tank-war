package test.client.impls;

import test.client.TankClient;
import test.client.interfaces.CampType;

public class EnemyTank extends Tank{

	public EnemyTank(int x, int y, TankClient tc) {
		super(x, y, tc);
		this.setCamp(CampType.ENEMY);
	}
	
	//敌人坦克随机移动和开火
	public void randomMoveAndShot() {
		//敌人坦克随机改变方向
		if(Math.random() > 0.95){
			int rDir = (int) (Math.random() * 8);
			this.setDir(rDir);
		}
		
		//敌人坦克随机开火
		if(Math.random() > 0.95 && getBullets().size() < 3){
			Bullet bullet = this.shot();
			this.getBullets().add(bullet);
		}
	}
}
