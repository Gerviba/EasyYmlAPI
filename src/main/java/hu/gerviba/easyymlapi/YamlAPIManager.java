package hu.gerviba.easyymlapi;

import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class YamlAPIManager extends JavaPlugin {

	public static final String LOCATION_FORMAT = "^(?i)[a-z0-9_]+\\,\\ "
			+ "-?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\,\\ "
			+ "-?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\,\\ "
			+ "-?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?(\\,\\ "
			+ "-?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?\\,\\ "
			+ "-?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?)?$";
	public static final Function<String, Object> LOCATION_FUNC = (value) -> {
		String[] p = value.split("\\,\\ ", 6);
		return p.length == 6 
				? new Location(Bukkit.getWorld(p[0]), 
					Double.parseDouble(p[1]), Double.parseDouble(p[2]), Double.parseDouble(p[3]), 
					Float.parseFloat(p[4]), Float.parseFloat(p[5]))
				: new Location(Bukkit.getWorld(p[0]), 
					Double.parseDouble(p[1]), Double.parseDouble(p[2]), Double.parseDouble(p[3]));
	};
	
	private static YamlAPIManager instance = null;
	
	public static String getVersion() {
		return instance.getDescription().getVersion();
	}
	
	public static <T> ConfigBuilder<T> newBuilder(Class<T> clazz) {
		return new ConfigBuilder<>(clazz);
	}
	
	public YamlAPIManager() {
		instance = this;
	}
	
	@Override
	public void onEnable() {
		System.out.println("[EasyYmlAPI] Thank you for using EasyYmlAPI v" + getDescription().getVersion());
	}
	
}
