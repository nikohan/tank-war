package test.client.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import test.client.TankClient;

public class PropertyMgr {
	
	private static Properties props = new Properties();
	
	static {
		InputStream is = TankClient.class.getClassLoader().getResourceAsStream("config/tank.properties");
		try {
			props.load(is);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private PropertyMgr() {}
	
	/**
	 * 根据key得到配置文件中的String类型的value
	 * @param key
	 * @return
	 */
	public static String getPropertyString(String key) {
		return props.getProperty(key);
	}
	
	/**
	 * 根据key得到配置文件中的int类型的value
	 * @param key
	 * @return
	 */
	public static int getPropertyInt(String key) {
		return Integer.valueOf(props.getProperty(key));
	}
}
