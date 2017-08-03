package hu.gerviba.easyymlapi;

import java.util.List;

import org.junit.Test;

import hu.gerviba.easyymlapi.identifiers.ConfigId;
import hu.gerviba.easyymlapi.identifiers.ConfigValue;

public class YamlAPIManagerTest {

	private static class TestAPIClass {
		@ConfigId
		private int version;
		@ConfigValue(defaultValue = "FirstRelease")
		private String name;
		
		@Override
		public String toString() {
			return version +" -> " + name;
		}
	}
	
	@Test
	public void testNewBuilder() throws Exception {
		List<TestAPIClass> list = YamlAPIManager.newBuilder(TestAPIClass.class)
				.setFile("tests", "testAPI.yml")
				.setHeader("TestAPI Versions and Names:")
				.build();
		
		if (System.getProperty("debug", "false").equalsIgnoreCase("true"))
			list.forEach(System.out::println);
	}
	
}
