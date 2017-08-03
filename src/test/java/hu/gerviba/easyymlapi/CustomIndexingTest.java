package hu.gerviba.easyymlapi;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import hu.gerviba.easyymlapi.identifiers.ConfigId;
import hu.gerviba.easyymlapi.identifiers.ConfigValue;
import hu.gerviba.easyymlapi.utils.TestUtil;
import junit.framework.Assert;

public class CustomIndexingTest {

	@BeforeClass
	public static void init() {
		TestUtil.delete(new File("tests", "testCI1.yml"));
	}
	
	private static class TestCI1Class {
		@ConfigValue
		private static int ignoreStatic;
		@ConfigValue(path = "other.path")
		private static int ignoreThisToo;
		@ConfigId
		private String playerName;
		@ConfigValue
		private int level;
		@ConfigValue(path = "random")
		private int otherValue;
		@ConfigValue(path = "random", name = "sub.otherSubValue")
		private int otherSubValue;
		@ConfigValue(path = "random.another", defaultValue = "32")
		private int anotherValue;
	}
	
	@Test
	public void testStringIndexing() throws Exception {
		Map<String, TestCI1Class> map = new ConfigBuilder<TestCI1Class>(TestCI1Class.class)
				.setFile("tests", "testCI1.yml")
				.setMinimumInstances(10)
				.loadSafe()
				.buildMap();
		
		map.forEach((k, v) -> {
			Assert.assertEquals(k, v.playerName);
			Assert.assertEquals(32, v.anotherValue);
		});
		
		Assert.assertEquals("ignoreStatic: 0\n" + 
				"other:\n" + 
				"  path:\n" + 
				"    ignoreThisToo: 0\n" + 
				"'0':\n" + 
				"  level: 0\n" + 
				"'1':\n" + 
				"  level: 0\n" + 
				"'2':\n" + 
				"  level: 0\n" + 
				"'3':\n" + 
				"  level: 0\n" + 
				"'4':\n" + 
				"  level: 0\n" + 
				"'5':\n" + 
				"  level: 0\n" + 
				"'6':\n" + 
				"  level: 0\n" + 
				"'7':\n" + 
				"  level: 0\n" + 
				"'8':\n" + 
				"  level: 0\n" + 
				"'9':\n" + 
				"  level: 0\n" + 
				"random:\n" + 
				"  '0':\n" + 
				"    otherValue: 0\n" + 
				"    sub:\n" + 
				"      otherSubValue: 0\n" + 
				"  '1':\n" + 
				"    otherValue: 0\n" + 
				"    sub:\n" + 
				"      otherSubValue: 0\n" + 
				"  '2':\n" + 
				"    otherValue: 0\n" + 
				"    sub:\n" + 
				"      otherSubValue: 0\n" + 
				"  '3':\n" + 
				"    otherValue: 0\n" + 
				"    sub:\n" + 
				"      otherSubValue: 0\n" + 
				"  '4':\n" + 
				"    otherValue: 0\n" + 
				"    sub:\n" + 
				"      otherSubValue: 0\n" + 
				"  '5':\n" + 
				"    otherValue: 0\n" + 
				"    sub:\n" + 
				"      otherSubValue: 0\n" + 
				"  '6':\n" + 
				"    otherValue: 0\n" + 
				"    sub:\n" + 
				"      otherSubValue: 0\n" + 
				"  '7':\n" + 
				"    otherValue: 0\n" + 
				"    sub:\n" + 
				"      otherSubValue: 0\n" + 
				"  '8':\n" + 
				"    otherValue: 0\n" + 
				"    sub:\n" + 
				"      otherSubValue: 0\n" + 
				"  '9':\n" + 
				"    otherValue: 0\n" + 
				"    sub:\n" + 
				"      otherSubValue: 0\n" + 
				"  another:\n" + 
				"    '0':\n" + 
				"      anotherValue: 32\n" + 
				"    '1':\n" + 
				"      anotherValue: 32\n" + 
				"    '2':\n" + 
				"      anotherValue: 32\n" + 
				"    '3':\n" + 
				"      anotherValue: 32\n" + 
				"    '4':\n" + 
				"      anotherValue: 32\n" + 
				"    '5':\n" + 
				"      anotherValue: 32\n" + 
				"    '6':\n" + 
				"      anotherValue: 32\n" + 
				"    '7':\n" + 
				"      anotherValue: 32\n" + 
				"    '8':\n" + 
				"      anotherValue: 32\n" + 
				"    '9':\n" + 
				"      anotherValue: 32",
				String.join("\n", Files.readAllLines(Paths.get("tests/testCI1.yml"))));
	}
	
}
