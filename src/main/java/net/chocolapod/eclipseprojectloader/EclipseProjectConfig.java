package net.chocolapod.eclipseprojectloader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EclipseProjectConfig {
	private static final String		DEFAULT_PROPERTIES = ".settings/eclipseProjectConfig.properties";

	private final Properties	properties;

	private EclipseProjectConfig() {
		properties = new Properties();
	}
	
	private void load(InputStream in) throws IOException {
		properties.load(in);
	}

	/**
	 *	指定のプロパティの設定値を返す
	 */
	public String get(ConfigKey key) {
		String	value = properties.getProperty(key.getKey());
		
		//	未設定なら、初期値を返す
		if (value == null) {
			value = key.getDefaultValue();
		}

		return value;
	}

	public static EclipseProjectConfig load() {
		EclipseProjectConfig	config = new EclipseProjectConfig();
		String					fileName = DEFAULT_PROPERTIES;
		InputStream				in = null;
		
		try {
//			in = EclipseProjectClassLoader.class.getResourceAsStream(fileName);
			in = new FileInputStream(fileName);
			if (in != null) {
				config.load(in);
			}
		} catch (Exception e) {
			//	無視する
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return config;
	}

}
