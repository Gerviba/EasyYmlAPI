package CustomConfig;

import hu.gerviba.easyymlapi.ConfigBuilder;
import hu.gerviba.easyymlapi.YamlAPIManager;
import hu.gerviba.easyymlapi.identifiers.ConfigValue;

public class CustomConfig {
	
	@ConfigValue
	public boolean enabled;
	
	@ConfigValue(path = "Servers")
	protected int maxUsers;
	
	@ConfigValue(path = "Servers", defaultValue = "Minecraft Server")
	private String serverName;
	
	@ConfigValue(path = "Servers", defaultValue = "25565")
	int port;
	
	public static ConfigBuilder<CustomConfig> init() {
		return YamlAPIManager.newBuilder(CustomConfig.class)
			.setFile("tests", "SimpleConfig.yml")
			.setHeader("Minecraft Server List")
			.setMinimumInstances(4)
			.addCustom("Servers.2.UniqueInfo", "This is a unique info")
			.loadSafe();
	}
	
}
