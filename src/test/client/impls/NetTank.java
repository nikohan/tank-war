package test.client.impls;

import test.client.TankClient;
import test.client.interfaces.CampType;

public class NetTank extends Tank{

	
	
	public NetTank(int x, int y, TankClient tc, int id) {
		super(x, y, tc);
		this.setId(id);
		this.setCamp(CampType.NET);
	}
	

}
