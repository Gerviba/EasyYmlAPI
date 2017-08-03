package SimpleConfig;

import java.util.List;

import hu.gerviba.easyymlapi.YamlAPIManager;
import hu.gerviba.easyymlapi.identifiers.ConfigValue;

public class SimpleConfig {

	@ConfigValue
	public boolean enabled;
	
	@ConfigValue(path = "Servers")
	protected int maxUsers;
	
	@ConfigValue(path = "Servers", defaultValue = "Minecraft Server")
	private String serverName;
	
	@ConfigValue(path = "Servers", defaultValue = "25565")
	int port;
	
	public static List<SimpleConfig> init() {
		return YamlAPIManager.newBuilder(SimpleConfig.class)
			.setFile("tests", "SimpleConfig.yml")
			.setHeader("Minecraft Server List")
			.setMinimumInstances(4)
			.loadSafe()
			.build();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getMaxUsers() {
		return maxUsers;
	}

	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
