package TeamInfoConfig;

import java.util.List;

import hu.gerviba.easyymlapi.YamlAPIManager;
import hu.gerviba.easyymlapi.datastructure.ArgumentType;
import hu.gerviba.easyymlapi.identifiers.ConfigId;
import hu.gerviba.easyymlapi.identifiers.ConfigValue;

public class TeamInfoConfig {

	@ConfigId
	public int teamId;
	
	@ConfigValue(defaultValue = "Team Name")
	protected String name;
	
	@ConfigValue(defaultValue = "0xFF0000")
	private int color;
	
	public static List<TeamInfoConfig> init() throws Exception {
		return YamlAPIManager.newBuilder(TeamInfoConfig.class)
			.setFile("tests", "TeamInfoConfig.yml")
			.setMinimumInstances(4)
			.addCustomFieldProcesor("color", (value) -> ArgumentType.HEX_INT.getParser().apply(value))
			.load()
			.build();
	}

	public int getId() {
		return teamId;
	}

	public int getColor() {
		return color;
	}
	
}
