package com.bms.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
	private ConfigUtil() {
		
	}
	// Not necessarily singleton 
	private static ConfigUtil INSTANCE = new ConfigUtil();
	private static Properties configs = null;
	
	public static ConfigUtil getInstance() {
		return INSTANCE;
	}
	
	public synchronized String getConfigValue(String configName) throws IOException {
		if (configs == null) {
			configs = new Properties();
			String configFilepath = "Configs.properties";
			InputStream inputStream = null;
			try{
				inputStream = this.getClass().getClassLoader().getResourceAsStream(configFilepath);
				if (inputStream != null) {
					configs.load(inputStream);
				} else {
					throw new IOException("Configs.properties file not Found!");
				}
			} finally {
				inputStream.close();
			}
		}
		return configs.getProperty(configName).trim();
	}
}
