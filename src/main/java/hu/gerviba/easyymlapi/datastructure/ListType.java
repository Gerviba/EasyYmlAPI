package hu.gerviba.easyymlapi.datastructure;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.configuration.file.YamlConfiguration;

public enum ListType {
	BYTE((def) -> def.map(x -> Byte.parseByte(x)).collect(Collectors.toList()),
			(cfg, path) -> cfg.getByteList(path)),
	
	SHORT((def) -> def.map(x -> Short.parseShort(x)).collect(Collectors.toList()),
			(cfg, path) -> cfg.getShortList(path)),
	
	INT((def) -> def.map(x -> Integer.parseInt(x)).collect(Collectors.toList()),
			(cfg, path) -> cfg.getIntegerList(path)),
	
	LONG((def) -> def.map(x -> Long.parseLong(x)).collect(Collectors.toList()),
			(cfg, path) -> cfg.getLongList(path)),
	
	FLOAT((def) -> def.map(x -> Float.parseFloat(x)).collect(Collectors.toList()),
			(cfg, path) -> cfg.getFloatList(path)),
	
	DOUBLE((def) -> def.map(x -> Double.parseDouble(x)).collect(Collectors.toList()),
			(cfg, path) -> cfg.getDoubleList(path)),
	
	CHAR((def) -> def.map(x -> x.length() > 0 ? x.charAt(0) : ' ').collect(Collectors.toList()), //TODO: Test it
			(cfg, path) -> cfg.getCharacterList(path)), 
	
	STRING((def) -> def.collect(Collectors.toList()),
			(cfg, path) -> cfg.getStringList(path)),
	
	CUSTOM((def) -> def.collect(Collectors.toList()), null);

	private final Function<Stream<String>, List<Object>> defaultValueConverter;
	private final BiFunction<YamlConfiguration, String, List<?>> loader;
	
	private ListType(Function<Stream<String>, List<Object>> converter, BiFunction<YamlConfiguration, String, List<?>> loader) {
		this.defaultValueConverter = converter;
		this.loader = loader;
	}

	public final List<Object> convertDefaultValue(String[] defaultValue) {
		return defaultValueConverter.apply(Arrays.stream(defaultValue));
	}

	public final List<?> getLoadedObject(YamlConfiguration cfg, String path) {
		return loader.apply(cfg, path);
	}
	
}
