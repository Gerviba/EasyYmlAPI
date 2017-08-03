package SimpleMixedConfig;

import java.util.List;

import hu.gerviba.easyymlapi.YamlAPIManager;
import hu.gerviba.easyymlapi.identifiers.ConfigValue;

public class SimpleMixedConfig {

	@ConfigValue(defaultValue = "Icecream Store")
	private static String storeName;
	
	@ConfigValue(path = "Icecream", name = "count", defaultValue = "42")
	private static int icecreamCount;
	@ConfigValue(path = "Icecream", name = "price", defaultValue = "1.30")
	public static double icecreamPrice;
	@ConfigValue(path = "Icecream", name = "cold", defaultValue = "true")
	private static boolean icecreamCold;
	
	@ConfigValue(path = "Statistics")
	private static byte averageCustomerCount;
	@ConfigValue(path = "Statistics", defaultValue = "Steve")
	private static String lastCustomerName;
	
	@ConfigValue(defaultValue = "Steve")
	private String name;
	@ConfigValue(defaultValue = "16")
	private int age;
	@ConfigValue(defaultValue = "2.60")
	private double bill;
	@ConfigValue(defaultValue = "HAPPY")
	private CustomerFeeling feeling;
	
	public static enum CustomerFeeling {
		SATISFIED, HAPPY, ANGRY;
	}

	public static List<SimpleMixedConfig> init() {
		return YamlAPIManager.newBuilder(SimpleMixedConfig.class)
			.setFile("tests", "SimpleMixedConfig.yml")
			.setHeader("Store Info", "with Multiline header")
			.setMinimumInstances(2)
			.setInstanceDefaultPath("Customers")
			.loadSafe()
			.build();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CustomerFeeling getFeeling() {
		return feeling;
	}

	public void setFeeling(CustomerFeeling feeling) {
		this.feeling = feeling;
	}
	
	
}
