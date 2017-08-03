package ClassParserConfig;

import java.util.List;

import org.bukkit.util.Vector;

import hu.gerviba.easyymlapi.YamlAPIManager;
import hu.gerviba.easyymlapi.datastructure.ArgumentType;
import hu.gerviba.easyymlapi.identifiers.ConfigValue;

public class ClassParserConfig {

	public static class TestClassToParse {
		
		private String data;
		
		public TestClassToParse(Byte b) {
			this.data = "TestClassToParse(byte: " + b + ")";
		}
		
		public TestClassToParse(int i, float f) {
			this.data = "TestClassToParse(int: " + i + ", float: " + f + ")";
		}
		
		public TestClassToParse(long l, String s1, String s2) {
			this.data = "TestClassToParse(long: " + l + ", String: " + s1 + ", String: " + s2 + ")";
		}
		
		@Override
		public String toString() {
			return this.data;
		}
		
	}
	
	public static class Color {
		
		private short r, g, b;

		public Color(short r, short g, short b) {
			if (!isValid(r) || !isValid(g) || !isValid(b))
				throw new RuntimeException("Invalid RGB value");
			
			this.r = r;
			this.g = g;
			this.b = b;
		}
		
		public int getRGB() {
			return r << 16 | g << 8 | b;
		}
		
		public boolean isValid(short value) {
			return value >= 0 && value <= 255;
		}
		
	}
	
	@ConfigValue(defaultValue = "-5")
	private TestClassToParse type1;
	@ConfigValue(defaultValue = "300, 33.3")
	private TestClassToParse type2;
	@ConfigValue(defaultValue = "42, String with space, Strinct_String")
	private TestClassToParse type3;
	@ConfigValue
	private Vector vector;
	@ConfigValue(defaultValue = "rgb(255, 16, 128)")
	private Color color;
	
	public static List<ClassParserConfig> init() throws Exception {
		return YamlAPIManager.newBuilder(ClassParserConfig.class)
				.setFile("tests", "ClassParserConfig.yml")
				.addCustomClass(TestClassToParse.class, ArgumentType.BYTE_OBJECT)
				.addCustomClass(TestClassToParse.class, ArgumentType.INT, ArgumentType.FLOAT)
				.addCustomClass(TestClassToParse.class, ArgumentType.LONG, ArgumentType.STRING, ArgumentType.STRICT_STRING)
				.addCustomClass(Vector.class, ArgumentType.DOUBLE, ArgumentType.DOUBLE, ArgumentType.DOUBLE)
				.addCustomClass(Color.class, "^rgb\\(\\d{1,3}\\,\\ \\d{1,3}\\,\\ \\d{1,3}\\)$", 
					(value) -> {
						String[] parts = value.replace("rgb(", "").replace(")", "").split("\\,\\ ", 3);
						return new Color(Short.parseShort(parts[0]), 
								Short.parseShort(parts[1]), 
								Short.parseShort(parts[2]));
					})
				.build();
				
	}

	public TestClassToParse getType1() {
		return type1;
	}
	
	public TestClassToParse getType2() {
		return type2;
	}

	public TestClassToParse getType3() {
		return type3;
	}

	public Vector getVector() {
		return vector;
	}

	public Color getColor() {
		return color;
	}

}
