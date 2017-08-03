package hu.gerviba.easyymlapi;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import hu.gerviba.easyymlapi.datastructure.ArgumentType;
import hu.gerviba.easyymlapi.datastructure.CustomListProcessor;
import hu.gerviba.easyymlapi.datastructure.ListType;
import hu.gerviba.easyymlapi.datastructure.ProcessorMap;
import hu.gerviba.easyymlapi.identifiers.ConfigListValue;
import hu.gerviba.easyymlapi.identifiers.ConfigValue;
import hu.gerviba.easyymlapi.utils.TestLocation;
import hu.gerviba.easyymlapi.utils.TestUtil;

public class ClassTest {

	@BeforeClass
	public static void init() {
		TestUtil.delete(new File("tests", "testC1.yml"));
		TestUtil.delete(new File("tests", "testC2.yml"));
		TestUtil.delete(new File("tests", "testC3.yml"));
	}

	private class CustomProcessor implements CustomListProcessor<String> {
		@Override
		public List<String> process(List<String> raw) {
			System.out.println("Custom processor were used");
			return raw.stream().map(line -> line.toLowerCase()).collect(Collectors.toList());
		}
		
	}
	
	private static class TestC1Class {
		@ConfigListValue(type = ListType.STRING, processor = CustomProcessor.class, defaultValue = {"ABC", "DEF", "GHI"})
		private static List<String> globalStringList;

		@ConfigListValue(type = ListType.INT, processor = CustomProcessor.class, defaultValue = {"12", "54", "18"})
		private static List<Integer> globalIntList;

		@ConfigListValue(type = ListType.STRING, processor = CustomProcessor.class, defaultValue = {"test line 1", "test-line2", "line3"})
		private List<String> testList;
		
		@ConfigListValue(type = ListType.CUSTOM, processor = CustomProcessor.class, defaultValue = {"TEST line 1", "teSt-line2", "liNE3"})
		private List<String> testLowercase;
		
		@ConfigListValue(type = ListType.CHAR, processor = CustomProcessor.class, defaultValue = {"a", "b", "c", "d", "e", "f"})
		private List<String> testCharlist;
		
	}

	@Ignore
	@Test
	public void testList() throws Exception {
		List<TestC1Class> list = new ConfigBuilder<TestC1Class>(TestC1Class.class)
				.setFile("tests", "testC1.yml")
				.loadSafe()
				.build();
		
		//FIXME: Static CustomProcessor lists not supported?
		//System.out.println("GL: " + TestC1Class.globalStringList);
		
		Assert.assertEquals(
				"globalStringList:\n" + 
				"- ABC\n" + 
				"- DEF\n" + 
				"- GHI\n" + 
				"globalIntList:\n" + 
				"- 12\n" + 
				"- 54\n" + 
				"- 18\n" + 
				"'0':\n" + 
				"  testList:\n" + 
				"  - test line 1\n" + 
				"  - test-line2\n" + 
				"  - line3\n" + 
				"  testLowercase:\n" + 
				"  - TEST line 1\n" + 
				"  - teSt-line2\n" + 
				"  - liNE3\n" + 
				"  testCharlist:\n" + 
				"  - a\n" + 
				"  - b\n" + 
				"  - c\n" + 
				"  - d\n" + 
				"  - e\n" + 
				"  - f\n",
				String.join("\n", Files.readAllLines(Paths.get("tests/testC1.yml"))));
	}
	
	private static class TestC2Class {
		@ConfigValue(defaultValue = "10.0, 10.5, 10.0, 90.0, 10.0")
		private static TestLocation globalLocation;

		@ConfigValue(defaultValue = "10.0, 11.5, 10.0, 90.0, 10.0")
		private TestLocation testLocation;
	}

	@Test
	public void testTestLocation() throws Exception {
		List<TestC2Class> list = new ConfigBuilder<TestC2Class>(TestC2Class.class)
				.setFile("tests", "testC2.yml")
				.addCustomClass(TestLocation.class, 
						ArgumentType.DOUBLE, ArgumentType.DOUBLE, ArgumentType.DOUBLE, ArgumentType.FLOAT, ArgumentType.FLOAT)
				.loadSafe()
				.build();
		
		Assert.assertEquals(Double.doubleToLongBits(11.5), Double.doubleToLongBits(list.get(0).testLocation.getY()));
		Assert.assertEquals(
				"globalLocation: 10.0, 10.5, 10.0, 90.0, 10.0\n" +
				"'0':\n" +
				"  testLocation: 10.0, 11.5, 10.0, 90.0, 10.0",
				String.join("\n", Files.readAllLines(Paths.get("tests/testC2.yml"))));
	}
	
	private static class TestC3Class {
		@ConfigValue(defaultValue = "world, 10.0, 10.5, 10.0, 90.0, 10.0")
		private static TestLocation globalLocation;

		@ConfigValue(defaultValue = "asd_world, 10.0, 11.5, 10.0, 90.0, 10.0")
		private TestLocation testLocation;
	}

	@Test
	public void testLocation() throws Exception {
		List<TestC3Class> list = new ConfigBuilder<TestC3Class>(TestC3Class.class)
				.setFile("tests", "testC3.yml")
				.addCustomClass(TestLocation.class, ProcessorMap.generateRegExFormat(
					ArgumentType.STRICT_STRING, ArgumentType.DOUBLE, ArgumentType.DOUBLE, ArgumentType.DOUBLE, 
					ArgumentType.FLOAT, ArgumentType.FLOAT), 
					(value) -> {
						String[] p = value.split("\\,\\ ");
						return new TestLocation(p[0], Double.parseDouble(p[1]), Double.parseDouble(p[2]), Double.parseDouble(p[3]), 
								Float.parseFloat(p[4]), Float.parseFloat(p[5]));
					})
				.loadSafe()
				.build();
		
		Assert.assertEquals(Double.doubleToLongBits(11.5), Double.doubleToLongBits(list.get(0).testLocation.getY()));
		Assert.assertEquals("TestLocation [world=world, x=10.0, y=10.5, z=10.0, yaw=90.0, pitch=10.0]", TestC3Class.globalLocation.toString());
		Assert.assertEquals("TestLocation [world=asd_world, x=10.0, y=11.5, z=10.0, yaw=90.0, pitch=10.0]", list.get(0).testLocation.toString());
		Assert.assertEquals(
				"globalLocation: world, 10.0, 10.5, 10.0, 90.0, 10.0\n" +
				"'0':\n" +
				"  testLocation: asd_world, 10.0, 11.5, 10.0, 90.0, 10.0",
				String.join("\n", Files.readAllLines(Paths.get("tests/testC3.yml"))));
	}
	
}
