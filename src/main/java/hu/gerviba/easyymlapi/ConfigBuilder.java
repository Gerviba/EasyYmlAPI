package hu.gerviba.easyymlapi;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import hu.gerviba.easyymlapi.datastructure.ArgumentType;
import hu.gerviba.easyymlapi.datastructure.ProcessorMap;
import hu.gerviba.easyymlapi.identifiers.ConfigId;
import hu.gerviba.easyymlapi.identifiers.ConfigListValue;
import hu.gerviba.easyymlapi.identifiers.ConfigValue;

public final class ConfigBuilder<T> {
	
	static {
		if (Bukkit.getServer() != null)
			System.out.println("[EasyYamlAPI] Ready to use! (by Gerviba)"); 
	}
	
	private YamlConfiguration cfg = null;
	private File file = null;
	private String header = null;
	private Class<T> clazz;
	
	private int minInstanceCount = 1;
	private String[] defaultKeys = {"default"};
	private String globalDefaultPath = "";
	private String instanceDefaultPath = "";
	private boolean intTypeIndexing = true;
	private HashMap<String, Object> customDefaults = new HashMap<>();
	private HashMap<String, Object> customSets = new HashMap<>();
	private HashMap<String, Function<String, Object>> customFieldProcessor = new HashMap<>();
	private ProcessorMap customClasses = new ProcessorMap();
	
	ConfigBuilder(Class<T> clazz) {
		this.clazz = clazz;
		
		validateClass(clazz);
	}

	public ConfigBuilder<T> setHeader(String... header) {
		this.header = String.join("\n", header);
		return this;
	}

	public ConfigBuilder<T> setFile(String file) {
		this.file = new File(file);
		return this;
	}

	public ConfigBuilder<T> setFile(String path, String file) {
		this.file = new File(path, file);
		return this;
	}

	public ConfigBuilder<T> setFile(File file) {
		this.file = file;
		return this;
	}
	
	public ConfigBuilder<T> setMinimumInstances(int instanceCount) {
		this.minInstanceCount = instanceCount;
		return this;
	}
	
	public ConfigBuilder<T> setDefaultKeys(String... keys) {
		this.defaultKeys = keys;
		return this;
	}
	
	public ConfigBuilder<T> setGlobalDefaultPath(String globalDefault) {
		this.globalDefaultPath = globalDefault + ".";
		return this;
	}

	public ConfigBuilder<T> setInstanceDefaultPath(String instanceDefault) {
		this.instanceDefaultPath = instanceDefault + ".";
		return this;
	}
	
	public ConfigBuilder<T> addCustomClass(Class<?> customClass, ArgumentType... arguments) {
		customClasses.put(customClass, arguments);
		return this;
	}
	
	public ConfigBuilder<T> addCustomClass(Class<?> customClass, String format, Function<String, Object> function) {
		customClasses.put(customClass, format, function);
		return this;
	}
	
	public ConfigBuilder<T> addCustomFieldProcesor(String fieldName, Function<String, Object> function) {
		customFieldProcessor.put(fieldName, function);
		return this;
	}
	
	public ConfigBuilder<T> setInstance(T instance) {
		// TODO
		return this;
	}
	
	public ConfigBuilder<T> setInstances(List<T> instances) {
		for (T instance : instances)
			setInstance(instance);
		return this;
	}
	
	public ConfigBuilder<T> setInstance(int id, T instance) {
		// TODO
		return this;
	}
	
	public ConfigBuilder<T> setInstances(int startingWithId, List<T> instances) {
		for (T instance : instances)
			setInstance(startingWithId++, instance);
		return this;
	}
	
	public ConfigBuilder<T> setInstance(String id, T instance) {
		// TODO
		return this;
	}
	
	public ConfigBuilder<T> setInstance(UUID id, T instance) {
		// TODO
		return this;
	}
	
	public ConfigBuilder<T> setIntegerTypeIndexing(boolean intIndexing) {
		this.intTypeIndexing = intIndexing;
		return this;
	}
	
	public ConfigBuilder<T> addCustomDefault(String path, Object value) {
		if (this.cfg == null)
			customDefaults.put(path, value);
		else
			cfg.addDefault(path, value);
		return this;
	}
	
	public ConfigBuilder<T> addCustom(String path, Object value) {
		if (this.cfg == null)
			customSets.put(path, value);
		else
			cfg.set(path, value);
		return this;
	}

	public ConfigBuilder<T> loadSafe() {
		try {
			checkFile();
			cfg = YamlConfiguration.loadConfiguration(file);
			addDefaults();
			cfg.addDefaults(customDefaults);
			for (Entry<String, Object> entry : customSets.entrySet())
				cfg.set(entry.getKey(), entry.getValue());
			
			save();
			loadStaticValues();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public ConfigBuilder<T> load() throws IOException, SecurityException, NoSuchFieldException, 
			IllegalArgumentException, IllegalAccessException {
		checkFile();
		cfg = YamlConfiguration.loadConfiguration(file);
		addDefaults();
		cfg.addDefaults(customDefaults);
		for (Entry<String, Object> entry : customSets.entrySet())
			cfg.set(entry.getKey(), entry.getValue());
		
		save();
		loadStaticValues();

		return this;
	}

	private void checkFile() {
		if (file == null)
			this.file = new File("plugins", clazz.getSimpleName());
	}
	
	public void save() throws IOException {
		if (cfg == null)
			loadSafe();
		cfg.options().header(header).copyHeader(true);
		cfg.options().copyDefaults(true);
		cfg.save(file);
	}
	
	public void saveSafe() {
		try {
			if (cfg == null)
				loadSafe();
			cfg.options().header(header).copyHeader(true);
			cfg.options().copyDefaults(true);
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadStaticValues() throws SecurityException, NoSuchFieldException, IllegalArgumentException, 
			IllegalAccessException {
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(ConfigValue.class) && Modifier.isStatic(field.getModifiers())) {
				setFieldAccessable(field);
				ConfigValue cv = field.getDeclaredAnnotation(ConfigValue.class);
				setFieldValue(field, null, makeFullPath(field, cv));
			}
		}
	}

	private void addDefaults() throws SecurityException {
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(ConfigValue.class)) {
				ConfigValue cv = field.getDeclaredAnnotation(ConfigValue.class);

				if (Modifier.isStatic(field.getModifiers())) {
					cfg.addDefault(makeFullPath(field, cv), calcDefaultValue(field, cv));
				} else if (intTypeIndexing) {
					for (int index = 0; index < this.minInstanceCount; ++index)
						cfg.addDefault(makeFullPath(index, field, cv), calcDefaultValue(field, cv));
				} else {
					for (String index : defaultKeys)
						cfg.addDefault(makeFullPath(index, field, cv), calcDefaultValue(field, cv));
				}
			} else if (field.isAnnotationPresent(ConfigListValue.class)) {
				ConfigListValue clv = field.getDeclaredAnnotation(ConfigListValue.class);

				if (Modifier.isStatic(field.getModifiers())) {
					cfg.addDefault(makeFullPath(field, clv), calcDefaultValue(clv));
				} else if (intTypeIndexing) {
					for (int index = 0; index < this.minInstanceCount; ++index)
						cfg.addDefault(makeFullPath(index, field, clv), calcDefaultValue(clv));
				} else {
					for (String index : defaultKeys)
						cfg.addDefault(makeFullPath(index, field, clv), calcDefaultValue(clv));
				}
			}
		}
	}

	private Object calcDefaultValue(Field field, ConfigValue cv) {
		if (customFieldProcessor.containsKey(field.getName()))
			return cv.defaultValue().length() == 0 ? "Please set the defaultValue propery" : cv.defaultValue();
		
		switch (field.getType().getName()) {
			case "byte":
			case "java.lang.Byte":
				return Byte.parseByte(cv.defaultValue().length() == 0 ? "0" : cv.defaultValue());
			case "short":
			case "java.lang.Short":
				return Short.parseShort(cv.defaultValue().length() == 0 ? "0" : cv.defaultValue());
			case "int":
			case "java.lang.Integer":
				return Integer.parseInt(cv.defaultValue().length() == 0 ? "0" : cv.defaultValue());
			case "long":
			case "java.lang.Long":
				return Long.parseLong(cv.defaultValue().length() == 0 ? "0" : cv.defaultValue());
			case "float":
			case "java.lang.Float":
				return Float.parseFloat(cv.defaultValue().length() == 0 ? "0" : cv.defaultValue());
			case "double":
			case "java.lang.Double":
				return Double.parseDouble(cv.defaultValue().length() == 0 ? "0" : cv.defaultValue());
			case "boolean":
			case "java.lang.Boolean":
				return cv.defaultValue().equalsIgnoreCase("true") || cv.defaultValue().equals("1");
			case "char":
			case "java.lang.Character":
				return cv.defaultValue().length() == 0 ? "a" : cv.defaultValue().charAt(0);
			case "java.lang.String":
				return cv.defaultValue().length() == 0 ? "n/a" : cv.defaultValue();
			case "org.bukkit.util.Vector":
				return cv.defaultValue().length() == 0 ? "0, 0, 0" : cv.defaultValue();
			case "org.bukkit.Location":
				return cv.defaultValue().length() == 0 
						? Bukkit.getWorlds().get(0).getName() + ", 0.5, 10.0, 0.5, 0.0, 0.0" 
						: cv.defaultValue();
			case "java.util.List":
				throw new RuntimeException(
						"Use @ConfigListValue annotation instead of @ConfigValue for types java.util.List");
		}
		
		if (field.getType().isEnum()) {
			if (field.getType().getEnumConstants().length == 0)
				throw new RuntimeException("Enum '" + field.getType().getName() + "' is empty!");
			return cv.defaultValue().length() == 0 ? field.getType().getEnumConstants()[0].toString() : cv.defaultValue();
		} else if (!customClasses.containsKey(field.getType())) {
			throw new RuntimeException("Invalid type: " + field.getType().getName());
		}
		
		return cv.defaultValue();
	}
	
	private List<Object> calcDefaultValue(ConfigListValue cv) {
		return cv.type().convertDefaultValue(cv.defaultValue());
	}

	public List<T> build() {
		if (cfg == null)
			loadSafe();
		if (!intTypeIndexing)
			throw new RuntimeException("Method build() is not applicable for this config file. Use buildMap() instead.");
		
		try {
			List<T> result = new ArrayList<>();
			
			for (int i = 0; true; ++i) {
				int fields = 0;
				for (Field field : clazz.getDeclaredFields()) {
					if (field.isAnnotationPresent(ConfigValue.class) && !Modifier.isStatic(field.getModifiers())) {
						ConfigValue cv = field.getDeclaredAnnotation(ConfigValue.class);
						++fields;
						if (!cfg.contains(makeFullPath(i, field, cv)))
							return result;
					}
				}
				
				if (fields == 0)
					return result;
				
				T instance = newInstance();
				for (Field field : clazz.getDeclaredFields()) {
					if (!Modifier.isStatic(field.getModifiers())) {
						setFieldAccessable(field);
						
						if (field.isAnnotationPresent(ConfigValue.class)) {
							ConfigValue cv = field.getDeclaredAnnotation(ConfigValue.class);
							setFieldValue(field, instance, makeFullPath(i, field, cv));
						} else if (field.isAnnotationPresent(ConfigId.class)) {
							field.set(instance, i);
						}
					}
				}
				result.add(instance);
			}
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}
	
	public Map<String, T> buildMap() {
		return this.<String>buildMap((key) -> key);
	}
	
	public Map<UUID, T> buildUUIDMap() {
		return this.<UUID>buildMap((key) -> UUID.fromString(key));
	}
	
	public <K> Map<K, T> buildMap(Function<String, K> keyConverter) {
		if (cfg == null)
			loadSafe();
		boolean needForSave = false;
		
		try {
			Map<K, T> result = new HashMap<>();
			
			for (String key : getRawKeys()) {
				for (Field field : clazz.getDeclaredFields()) {
					if (field.isAnnotationPresent(ConfigValue.class) && !Modifier.isStatic(field.getModifiers())) {
						ConfigValue cv = field.getDeclaredAnnotation(ConfigValue.class);
						if (!cfg.contains(makeFullPath(key, field, cv))) {
							cfg.set(makeFullPath(key, field, cv), calcDefaultValue(field, cv));
							needForSave = true;
							continue;
						}
					}
				}
				
				T instance = newInstance();
				for (Field field : clazz.getDeclaredFields()) {
					if (!Modifier.isStatic(field.getModifiers())) {
						setFieldAccessable(field);
						
						if (field.isAnnotationPresent(ConfigValue.class)) {
							ConfigValue cv = field.getDeclaredAnnotation(ConfigValue.class);
							setFieldValue(field, instance, makeFullPath(key, field, cv));
						} else if (field.isAnnotationPresent(ConfigId.class)) {
							field.set(instance, key);
						}
					}
				}
				result.put(keyConverter.apply(key), instance);
			}
			
			return result;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		} finally {
			if (needForSave)
				saveSafe();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setFieldValue(Field field, T instance, String path) throws IllegalArgumentException, 
	IllegalAccessException {
		if (customFieldProcessor.containsKey(field.getName())) {
			field.set(instance, customFieldProcessor.get(field.getName()).apply(cfg.getString(path)));
		} else if (customClasses.containsKey(field.getType(), cfg.getString(path))) {
			field.set(instance, customClasses.get(field.getType(), cfg.getString(path)));
		} else if (field.getType().isEnum()) {
			field.set(instance, Enum.valueOf((Class<? extends Enum>) field.getType(), cfg.getString(path)));
		} else if (field.getType() == Character.class || field.getType() == Character.TYPE) {
			field.set(instance, cfg.getString(path).charAt(0));
		} else {
			field.set(instance, cfg.get(path));
		}
	}
	
	private String maskPath(String path, boolean global) {
		return path.length() == 0 ? (global ? globalDefaultPath : instanceDefaultPath) : path + ".";
	}
	
	private String maskName(String name, String fileldName) {
		return name.length() == 0 ? fileldName : name;
	}

	private String makeFullPath(String key, Field field, ConfigValue cv) {
		return maskPath(cv.path(), false) + key + "." + maskName(cv.name(), field.getName());
	}

	private String makeFullPath(int key, Field field, ConfigValue cv) {
		return maskPath(cv.path(), false) + key + "." + maskName(cv.name(), field.getName());
	}
	
	private String makeFullPath(Field field, ConfigValue cv) {
		return maskPath(cv.path(), true) + maskName(cv.name(), field.getName());
	}
	
	private String makeFullPath(String key, Field field, ConfigListValue clv) {
		return maskPath(clv.path(), false) + key + "." + maskName(clv.name(), field.getName());
	}

	private String makeFullPath(int key, Field field, ConfigListValue clv) {
		return maskPath(clv.path(), false) + key + "." + maskName(clv.name(), field.getName());
	}
	
	private String makeFullPath(Field field, ConfigListValue clv) {
		return maskPath(clv.path(), true) + maskName(clv.name(), field.getName());
	}
	
	public Set<String> getRawKeys() {
		HashSet<String> keyAndName = new HashSet<>();
		for (String key : cfg.getKeys(true).stream()
				.filter(x -> cfg.getConfigurationSection(x) == null)
				.collect(Collectors.toSet())) {
			for (String path : getAllPaths(true).stream()
					.sorted((a, b) -> b.length() - a.length())
					.collect(Collectors.toList())) {
				if (key.startsWith(path)) {
					keyAndName.add(key.substring(path.length() + Math.min(path.length(), 1)));
					break;
				}
			}
		}
		
		Set<String> result = new HashSet<>();
		for (String key : keyAndName) {
			for (String name : getAllNames(true).stream()
					.sorted((a, b) -> b.length() - a.length())
					.collect(Collectors.toList())) {
				if (key.endsWith(name)) {
					result.add(key.substring(0, key.length() - name.length() - Math.min(name.length(), 1)));
					break;
				}
			}
		}
		return result;
	}
	
	private Set<String> getAllNames(boolean instance) {
		Set<String> result = new HashSet<>();
		for (Field field : clazz.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers()) ^ instance) {
				if (field.isAnnotationPresent(ConfigValue.class)) {
					result.add(field.getDeclaredAnnotation(ConfigValue.class).name().length() == 0 
							? field.getName() 
							: field.getDeclaredAnnotation(ConfigValue.class).name());
				} else if (field.isAnnotationPresent(ConfigListValue.class)) {
					result.add(field.getDeclaredAnnotation(ConfigListValue.class).name().length() == 0 
							? field.getName()
							: field.getDeclaredAnnotation(ConfigListValue.class).name());
				}
			}
		}
		return result;
	}
	
	private Set<String> getAllPaths(boolean instance) {
		Set<String> result = new HashSet<>();
		for (Field field : clazz.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers()) ^ instance) {
				if (field.isAnnotationPresent(ConfigValue.class)) {
					result.add(field.getDeclaredAnnotation(ConfigValue.class).path());
				} else if (field.isAnnotationPresent(ConfigListValue.class)) {
					result.add(field.getDeclaredAnnotation(ConfigListValue.class).path());
				}
			}
		}
		return result;
	}

	private void setFieldAccessable(Field field) throws NoSuchFieldException, SecurityException, 
			IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);
		Field modField = Field.class.getDeclaredField("modifiers");
		modField.setAccessible(true);
		modField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
	}

	private T newInstance() throws NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Constructor<T> constructor;
		constructor = clazz.getDeclaredConstructor();
		constructor.setAccessible(true);
		return constructor.newInstance();
	}
	
	public YamlConfiguration getRawConfiguration() {
		return this.cfg;
	}
	
	public Object getCustomValue(String path) {
		return this.cfg.get(path);
	}
	
	private void validateClass(Class<T> clazz) throws SecurityException, RuntimeException {
		int idCount = 0, annotatedCount = 0;
		HashSet<String> names = new HashSet<>();
		
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(ConfigId.class)) {
				checkIfStatic(field);
				checkIfIncompatible(field);
				checkIfWrongTypes(field);
				++idCount;
			} else if (field.isAnnotationPresent(ConfigValue.class)) {
				++annotatedCount;
				names.add(field.getDeclaredAnnotation(ConfigValue.class).name().length() == 0 
						? field.getName() : field.getDeclaredAnnotation(ConfigValue.class).name());
			} else if (field.isAnnotationPresent(ConfigListValue.class)) {
				++annotatedCount;
				names.add(field.getDeclaredAnnotation(ConfigListValue.class).name().length() == 0 
						? field.getName() : field.getDeclaredAnnotation(ConfigListValue.class).name());
			}
		}
		if (idCount > 1)
			throw new RuntimeException("There must be maximum one gield with @ConfigId annotation.");
		if (names.size() != annotatedCount)
			throw new RuntimeException("Duplicated names were found");
	}

	private void checkIfWrongTypes(Field field) throws RuntimeException {
		if (!field.getType().getName().equals("int") 
				&& !field.getType().getName().equals("java.lang.Integer")
				&& !field.getType().getName().equals("java.lang.String")
				&& !field.getType().getName().equals("java.util.UUID"))
			throw new RuntimeException(
					"Invalid @ConfigId data type. Only int, Integer, String and UUID are supported.");
	}

	private void checkIfIncompatible(Field field) throws RuntimeException {
		if (field.isAnnotationPresent(ConfigValue.class) || field.isAnnotationPresent(ConfigListValue.class))
			throw new RuntimeException(
					"Annotation @ConfigId is incompatible with @ConfigValue and @ConfigListValue.");
	}

	private void checkIfStatic(Field field) throws RuntimeException {
		if (Modifier.isStatic(field.getModifiers()))
			throw new RuntimeException("Invalid use of @ConfigId. The annotated field mustn't be static.");
	}

}
