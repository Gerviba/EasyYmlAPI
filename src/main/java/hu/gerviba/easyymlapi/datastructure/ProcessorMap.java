package hu.gerviba.easyymlapi.datastructure;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ProcessorMap {
	
	private static class ProcessorObject {
		
		private final Class<?> clazz;
		private final ArgumentType[] params;
		private final BiFunction<String, ProcessorObject, Object> func;
		
		public ProcessorObject(Class<?> clazz, ArgumentType[] params, BiFunction<String, ProcessorObject, Object> func) {
			this.clazz = clazz;
			this.params = params;
			this.func = func;
		}
		
		public Object process(String value) {
			return func.apply(value, this);
		}

		public Class<?> getClazz() {
			return clazz;
		}

		public Class<?>[] getParameters() {
			return Arrays.stream(params).map(p -> p.getClazz()).toArray(size -> new Class<?>[size]);
		}
		
		public Object[] getValues(final String[] parts) {
			Object[] result = new Object[params.length];
			for (int i = 0; i < result.length; ++i)
				result[i] = params[i].getParser().apply(parts[i]);
			return result;
		}
		
	}
	
	private static final BiFunction<String, ProcessorObject, Object> DEFAULT_FUNCTION = (value, procObj) -> {
		try {
			Constructor<?> constructor = procObj.getClazz().getDeclaredConstructor(procObj.getParameters());
			constructor.setAccessible(true);
			return constructor.newInstance(procObj.getValues(value.split("\\,\\ ", procObj.getParameters().length)));
		} catch (NoSuchMethodException 
				| SecurityException 
				| InstantiationException 
				| IllegalAccessException 
				| IllegalArgumentException 
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	};
	
	private HashMap<String, HashMap<String, ProcessorObject>> map = new HashMap<>();
	
	public void put(Class<?> clazz, ArgumentType[] params) {
		if (!map.containsKey(clazz.getName()))
			map.put(clazz.getName(), new HashMap<>());
		
		map.get(clazz.getName()).put(generateRegExFormat(params), new ProcessorObject(clazz, params, DEFAULT_FUNCTION));
	}
	
	public void put(Class<?> clazz, String format, Function<String, Object> func) {
		if (!map.containsKey(clazz.getName()))
			map.put(clazz.getName(), new HashMap<>());
		
		map.get(clazz.getName()).put(format, new ProcessorObject(clazz, null, (value, b) -> func.apply(value)));
	}
	
	public boolean containsKey(Class<?> clazz) {
		return map.containsKey(clazz.getName());
	}
	
	public boolean containsKey(Class<?> clazz, final String value) {
		if (!map.containsKey(clazz.getName()))
			return false;
		
		return map.get(clazz.getName()).entrySet().stream()
				.filter(x -> value.matches(x.getKey()))
				.findAny()
				.isPresent();
	}
	
	public Object get(Class<?> clazz, final String value) {
		if (!map.containsKey(clazz.getName()))
			throw new RuntimeException("No matching pattern found for this value");
		
		return map.get(clazz.getName()).entrySet().stream()
				.filter(x -> value.matches(x.getKey()))
				.findAny()
				.orElseThrow(() -> new RuntimeException("No matching pattern found for this value"))
				.getValue()
				.process(value);
	}
	
	public static String generateRegExFormat(ArgumentType... types) {
		return "^(?i)"+String.join("\\,\\ ", Arrays.stream(types)
				.map(x -> x.getRegExPart())
				.toArray(size -> new String[size])) + "$";
	}
	
	
}
