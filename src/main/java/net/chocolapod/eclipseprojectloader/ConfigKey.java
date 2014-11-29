package net.chocolapod.eclipseprojectloader;

public enum ConfigKey {
	ResourcesDir("resources.dir", "src/main/resources/"),
	TargetDir("target.dir", "target/classes/")
	;

	private final String	key;
	private final String	defaultValue;
	
	private ConfigKey(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	public String getKey() {
		return key;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

}
