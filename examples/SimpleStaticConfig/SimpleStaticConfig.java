package SimpleStaticConfig;

import hu.gerviba.easyymlapi.YamlAPIManager;
import hu.gerviba.easyymlapi.identifiers.ConfigValue;

public class SimpleStaticConfig {

	@ConfigValue
	public static String CONFIG_STR1;
	
	@ConfigValue(defaultValue = "String 2")
	public static String CONFIG_STR2;
	
	@ConfigValue(name = "CONFIG_3", defaultValue = "String 3")
	public static String CONFIG_STRN;
	
	public static void init() {
		YamlAPIManager.newBuilder(SimpleStaticConfig.class)
			.setFile("tests", "SimpleStaticConfig.yml")
			.setHeader("We only have static content here")
			.setGlobalDefaultPath("Global")
			.loadSafe();
	}
	
}
