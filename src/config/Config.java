package config;

import java.io.IOException;
import java.util.Properties;
public class Config {
	
	private static Properties p = null;
	
	static{
		p = new Properties();
		try {
			p.load(Config.class.getResourceAsStream(
					              "config.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getConfigByName(String name) {
		return p.getProperty(name);
	}

}
