package hu.gerviba.easyymlapi;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.gerviba.easyymlapi.ConfigBuilder;
import hu.gerviba.easyymlapi.identifiers.ConfigId;
import hu.gerviba.easyymlapi.identifiers.ConfigValue;
import hu.gerviba.easyymlapi.utils.TestUtil;

public class PrimitiveTest {
	
	@BeforeClass
	public static void init() {
		TestUtil.delete(new File("tests", "testP1.yml"));
		TestUtil.delete(new File("tests", "testP2.yml"));
		TestUtil.delete(new File("tests", "testP3.yml"));
		TestUtil.delete(new File("tests", "testP4.yml"));
		TestUtil.delete(new File("tests", "testP5.yml"));
		TestUtil.delete(new File("tests", "testP6.yml"));
		TestUtil.delete(new File("tests", "testP7.yml"));
		TestUtil.delete(new File("tests", "testP8.yml"));
	}
	
	@SuppressWarnings("unused")
	private static class TestP1Class {
		private static int ignoreThisGlobal;
		private int ignoreThis;
		
		@ConfigValue(defaultValue = "1")
		private static int globalInt;
		
		@ConfigValue
		private int testInt;
		@ConfigValue
		private String testString;
	}
	
	@Test
	public void testPrimitive() throws Exception {
		List<TestP1Class> list = new ConfigBuilder<TestP1Class>(TestP1Class.class)
				.setHeader("Header Line 1", "Header Line two")
				.setFile("tests", "testP1.yml")
				.loadSafe()
				.build();
		
		Assert.assertEquals(1, TestP1Class.globalInt);
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(0, list.get(0).testInt);
		Assert.assertEquals("n/a", list.get(0).testString);
		Assert.assertEquals(
				"# Header Line 1\n" +
				"# Header Line two\n" +
				"globalInt: 1\n" +
				"'0':\n" +
				"  testInt: 0\n" +
				"  testString: n/a",
				String.join("\n", Files.readAllLines(Paths.get("tests/testP1.yml"))));
	}
	
	@SuppressWarnings("unused")
	private static class TestP2Class {
		private static int ignoreThisGlobal;
		private int ignoreThis;
		
		@ConfigValue(path = "Global.Test2", name = "GLOBAL_INT", defaultValue = "10")
		private static int globalInt;
		
		@ConfigValue(path = "Test2", name = "TEST_INT", defaultValue = "30")
		private int testInt;
		@ConfigValue(path = "Test2", name = "TEST_STRING", defaultValue = "EZ EGY STRING")
		private String testString;
	}
	
	@Test
	public void testPrimitivePath() throws Exception {
		List<TestP2Class> list = new ConfigBuilder<TestP2Class>(TestP2Class.class)
				.setFile("tests", "testP2.yml")
				.loadSafe()
				.build();
		
		Assert.assertEquals(10, TestP2Class.globalInt);
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(
				"Global:\n" +
				"  Test2:\n" +
				"    GLOBAL_INT: 10\n" +
				"Test2:\n" +
				"  '0':\n" +
				"    TEST_INT: 30\n" +
				"    TEST_STRING: EZ EGY STRING",
				String.join("\n", Files.readAllLines(Paths.get("tests/testP2.yml"))));
	}
	
	@SuppressWarnings("unused")
	private static class TestP3Class {
		private final static int ignoreThisGlobalCafeBabe = 0xCafeBabe;
		private final int ignoreThisFoo = 0xF00;
		
		@ConfigValue(defaultValue = "1")
		private final static int globalInt = 0;
		
		@ConfigValue(defaultValue = "0")
		private final int testInt = -10;
		@ConfigValue(defaultValue = "Foo")
		private final String testString = "Bar";
	}
	
	@Test
	public void testPrimitiveFinal() throws Exception {
		List<TestP3Class> list = new ConfigBuilder<TestP3Class>(TestP3Class.class)
				.setFile("tests", "testP3.yml")
				.load()
				.build();
		
		Assert.assertEquals(0, TestP3Class.globalInt);
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(-10, list.get(0).testInt);
		Assert.assertEquals("Bar", list.get(0).testString);
		Assert.assertEquals(
				"globalInt: 1\n" +
				"'0':\n" +
				"  testInt: 0\n" +
				"  testString: Foo",
				String.join("\n", Files.readAllLines(Paths.get("tests/testP3.yml"))));
	}
	
	private static class TestP4Class {
		@ConfigValue(defaultValue = "1")
		private transient static int globalInt = 0;
		
		@ConfigValue(defaultValue = "0")
		private transient int testInt = -10;
		@ConfigValue(defaultValue = "Foo")
		private transient String testString = "Bar";
	}
	
	@Test
	public void testPrimitiveTransient() throws Exception {
		List<TestP4Class> list = new ConfigBuilder<TestP4Class>(TestP4Class.class)
				.setFile("tests", "testP4.yml")
				.load()
				.build();
		
		Assert.assertEquals(0, TestP3Class.globalInt);
		Assert.assertEquals(1, list.size());
		Assert.assertEquals(0, list.get(0).testInt);
		Assert.assertEquals("Foo", list.get(0).testString);
		Assert.assertEquals(
				"globalInt: 1\n" +
				"'0':\n" +
				"  testInt: 0\n" +
				"  testString: Foo",
				String.join("\n", Files.readAllLines(Paths.get("tests/testP4.yml"))));
	}
	
	private static class TestP5Class {
	
		@ConfigValue
		private static byte globalByte;
		@ConfigValue
		private static short globalShort;
		@ConfigValue
		private static int globalInt;
		@ConfigValue
		private static long globalLong;
		@ConfigValue
		private static float globalFloat;
		@ConfigValue
		private static double globalDouble;
		@ConfigValue(defaultValue = "true")
		private static boolean globalBoolean;
		@ConfigValue
		private static char globalChar;
		@ConfigValue
		private static String globalString;
		
		@ConfigValue
		private byte testByte;
		@ConfigValue
		private short testShort;
		@ConfigValue
		private int testInt;
		@ConfigValue(defaultValue = "17179869184")
		private long testLong;
		@ConfigValue
		private float testFloat;
		@ConfigValue(defaultValue = "0.6")
		private double testDouble;
		@ConfigValue
		private boolean testBoolean;
		@ConfigValue(defaultValue = "T")
		private char testChar;
		@ConfigValue
		private String testString;
	}
	
	@Test
	public void testAllPrimitive() throws Exception {
		List<TestP5Class> list = new ConfigBuilder<TestP5Class>(TestP5Class.class)
				.setHeader("Header Only 1 Line")
				.setFile("tests", "testP5.yml")
				.loadSafe()
				.build();
		
		Assert.assertEquals(true, TestP5Class.globalBoolean);
		Assert.assertEquals(Double.doubleToLongBits(0.6D), Double.doubleToLongBits(list.get(0).testDouble));
		Assert.assertEquals(17179869184L, list.get(0).testLong);
		Assert.assertEquals('a', TestP5Class.globalChar);
		Assert.assertEquals('T', list.get(0).testChar);
		Assert.assertEquals(
				"# Header Only 1 Line\n" +
				"globalByte: 0\n" +
				"globalShort: 0\n" +
				"globalInt: 0\n" +
				"globalLong: 0\n" +
				"globalFloat: 0.0\n" +
				"globalDouble: 0.0\n" +
				"globalBoolean: true\n" +
				"globalChar: a\n" +
				"globalString: n/a\n" +
				"'0':\n" +
				"  testByte: 0\n" +
				"  testShort: 0\n" +
				"  testInt: 0\n" +
				"  testLong: 17179869184\n" +
				"  testFloat: 0.0\n" +
				"  testDouble: 0.6\n" +
				"  testBoolean: false\n" +
				"  testChar: T\n" +
				"  testString: n/a",
				String.join("\n", Files.readAllLines(Paths.get("tests/testP5.yml"))));
	}
	
	private static class TestP678Class {
		@ConfigValue
		private static int globalInt = 0;
		
		@ConfigId
		private int uid;
		@ConfigValue(defaultValue = "0")
		private int testInt;
		@ConfigValue(defaultValue = "Bar")
		private String testString;
	}
	
	@Test
	public void testMoreInstances() throws Exception {
		List<TestP678Class> list = new ConfigBuilder<TestP678Class>(TestP678Class.class)
				.setFile("tests", "testP6.yml")
				.setHeader("Header 1", "Header 2", "Header 3")
				.setMinimumInstances(3)
				.load()
				.build();
		
		Assert.assertEquals(3, list.size());
		Assert.assertEquals(
				"# Header 1\n" +
				"# Header 2\n" +
				"# Header 3\n" +
				"globalInt: 0\n" +
				"'0':\n" +
				"  testInt: 0\n" +
				"  testString: Bar\n" +
				"'1':\n" +
				"  testInt: 0\n" +
				"  testString: Bar\n" +
				"'2':\n" +
				"  testInt: 0\n" +
				"  testString: Bar",
				String.join("\n", Files.readAllLines(Paths.get("tests/testP6.yml"))));
	}
	
	@Test
	public void testDefaultPath() throws Exception {
		List<TestP678Class> list = new ConfigBuilder<TestP678Class>(TestP678Class.class)
				.setFile(new File("tests/testP7.yml"))
				.setMinimumInstances(3)
				.setGlobalDefaultPath("GlobalDefault")
				.setInstanceDefaultPath("InstanceDefault")
				.load()
				.build();
		
		Assert.assertEquals(3, list.size());
		Assert.assertEquals(
				"GlobalDefault:\n" +
				"  globalInt: 0\n" +
				"InstanceDefault:\n" +
				"  '0':\n" +
				"    testInt: 0\n" +
				"    testString: Bar\n" +
				"  '1':\n" +
				"    testInt: 0\n" +
				"    testString: Bar\n" +
				"  '2':\n" +
				"    testInt: 0\n" +
				"    testString: Bar",
				String.join("\n", Files.readAllLines(Paths.get("tests/testP7.yml"))));
	}
	
	
	@Test
	public void testConfigId() throws Exception {
		List<TestP678Class> list = new ConfigBuilder<TestP678Class>(TestP678Class.class)
				.setFile(new File("tests/testP8.yml"))
				.setMinimumInstances(3)
				.setGlobalDefaultPath("GlobalDefault")
				.setInstanceDefaultPath("InstanceDefault")
				.load()
				.build();
		
		for (int i = 0; i < 3; ++i)
			Assert.assertEquals(i, list.get(i).uid);
	}
	
}

//				.<Location>registerInterpreter("%f, %f, %f, %f, %f", (path, value) -> {
//					String[] p = value.split(", ");
//					return new Location(Double.parseDouble(p[0]), Double.parseDouble(p[1]), Double.parseDouble(p[2]));
//				})