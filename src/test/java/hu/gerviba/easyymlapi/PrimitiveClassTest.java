package hu.gerviba.easyymlapi;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.gerviba.easyymlapi.ConfigBuilder;
import hu.gerviba.easyymlapi.identifiers.ConfigValue;
import hu.gerviba.easyymlapi.utils.TestUtil;

public class PrimitiveClassTest {

	@BeforeClass
	public static void init() {
		TestUtil.delete(new File("tests", "testPC.yml"));
	}
	
	private static class TestPC1Class {
		
		@ConfigValue
		private static Byte globalByte;
		@ConfigValue(defaultValue = "30000")
		private static Short globalShort;
		@ConfigValue
		private static Integer globalInt;
		@ConfigValue
		private static Long globalLong;
		@ConfigValue
		private static Float globalFloat;
		@ConfigValue
		private static Double globalDouble;
		@ConfigValue
		private static Character globalChar;
		@ConfigValue(defaultValue = "true")
		private static Boolean globalBoolean;
		
		@ConfigValue
		private Byte testByte;
		@ConfigValue
		private Short testShort;
		@ConfigValue
		private Integer testInt;
		@ConfigValue(defaultValue = "17179869184")
		private Long testLong;
		@ConfigValue
		private Float testFloat;
		@ConfigValue(defaultValue = "0.6")
		private Double testDouble;
		@ConfigValue(defaultValue = "G")
		private Character testChar;
		@ConfigValue
		private Boolean testBoolean;
	}
	
	@Test
	public void testAllPrimitive() throws Exception {
		List<TestPC1Class> list = new ConfigBuilder<TestPC1Class>(TestPC1Class.class)
				.setFile("tests/testPC.yml")
				.loadSafe()
				.build();
		
		Assert.assertEquals(new Short((short) 30000), TestPC1Class.globalShort);
		Assert.assertEquals(Double.doubleToLongBits(0.6D), Double.doubleToLongBits(list.get(0).testDouble));
		Assert.assertEquals(new Long(17179869184L), list.get(0).testLong);
		Assert.assertEquals(new Character('G'), list.get(0).testChar);
		Assert.assertEquals(
				"globalByte: 0\n" +
				"globalShort: 30000\n" +
				"globalInt: 0\n" +
				"globalLong: 0\n" +
				"globalFloat: 0.0\n" +
				"globalDouble: 0.0\n" +
				"globalChar: a\n" +
				"globalBoolean: true\n" +
				"'0':\n" +
				"  testByte: 0\n" +
				"  testShort: 0\n" +
				"  testInt: 0\n" +
				"  testLong: 17179869184\n" +
				"  testFloat: 0.0\n" +
				"  testDouble: 0.6\n" +
				"  testChar: G\n" +
				"  testBoolean: false",
				String.join("\n", Files.readAllLines(Paths.get("tests/testPC.yml"))));
	}
	
}
