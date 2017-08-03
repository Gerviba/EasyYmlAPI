package ItemStackConfig;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import hu.gerviba.easyymlapi.YamlAPIManager;
import hu.gerviba.easyymlapi.datastructure.ArgumentType;
import hu.gerviba.easyymlapi.identifiers.ConfigValue;

public class ItemStackConfig {
	
	@ConfigValue(defaultValue = "WOOD, 1, 2")
	protected ItemStack item;

	public static List<ItemStackConfig> init() throws Exception {
		return YamlAPIManager.newBuilder(ItemStackConfig.class)
				.setFile("tests", "TeamInfoConfig.yml")
				.addCustomClass(ItemStack.class, ArgumentType.MATERIAL, ArgumentType.INT, ArgumentType.SHORT)
				.load()
				.build();
	}

	public ItemStack getItem() {
		return item;
	}

	
	
}
