package hu.gerviba.easyymlapi.datastructure;

import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

public enum ArgumentType {
	BYTE("-?\\d{1,3}", Byte.TYPE, (part) -> Byte.parseByte(part)),
	BYTE_OBJECT("-?\\d{1,3}", Byte.class, (part) -> Byte.parseByte(part)),
	SHORT("-?\\d{1,5}", Short.TYPE, (part) -> Short.parseShort(part)),
	SHORT_OBJECT("-?\\d{1,5}", Short.class, (part) -> Short.parseShort(part)),
	INT("-?\\d{1,10}", Integer.TYPE, (part) -> Integer.parseInt(part)),
	INT_OBJECT("-?\\d{1,10}", Integer.class, (part) -> Integer.parseInt(part)),
	LONG("-?\\d{1,19}", Long.TYPE, (part) -> Long.parseLong(part)),
	LONG_OBJECT("-?\\d{1,19}", Long.class, (part) -> Long.parseLong(part)),
	FLOAT("-?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?", Float.TYPE, (part) -> Float.parseFloat(part)),
	FLOAT_OBJECT("-?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?", Float.class, (part) -> Float.parseFloat(part)),
	DOUBLE("-?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?", Double.TYPE, (part) -> Double.parseDouble(part)),
	DOUBLE_OBJECT("-?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?", Double.class, (part) -> Double.parseDouble(part)),
	BOOLEAN("((1)|(0)|(true)|(false))", Boolean.TYPE, (part) -> part.equalsIgnoreCase("true") || part.equals("1")),
	BOOLEAN_OBJECT("((1)|(0)|(true)|(false))", Boolean.class, (part) -> part.equalsIgnoreCase("true") || part.equals("1")),
	CHAR(".", Character.TYPE, (part) -> part.charAt(0)),
	CHAR_OBJECT(".", Character.class, (part) -> part.charAt(0)),
	STRICT_STRING("[a-z0-9_]+", String.class, (part) -> part),
	STRING(".+", String.class, (part) -> part),
	HEX_INT("((\\0\\x)|(\\#))?[0-9a-f]{1,8}", Integer.TYPE, (part) -> Integer.parseInt(part.replaceAll("0[xX]", "").replace("#", ""), 16)),
	MATERIAL("[a-z0-9_]+", Material.class, (part) -> Material.getMaterial(part)),
	WORLD("[a-z0-9_]+", World.class, (part) -> Bukkit.getWorld(part)),
	;
	
	private final String regExPart;
	private final Class<?> clazz;
	private final Function<String, Object> parser;
	
	private ArgumentType(String regExPart, Class<?> clazz, Function<String, Object> parser) {
		this.regExPart = regExPart;
		this.clazz = clazz;
		this.parser = parser;
	}

	public String getRegExPart() {
		return regExPart;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public Function<String, Object> getParser() {
		return parser;
	}
}
