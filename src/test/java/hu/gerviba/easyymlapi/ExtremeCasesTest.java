package hu.gerviba.easyymlapi;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.gerviba.easyymlapi.identifiers.ConfigId;
import hu.gerviba.easyymlapi.identifiers.ConfigValue;
import hu.gerviba.easyymlapi.utils.TestUtil;

public class ExtremeCasesTest {

	@BeforeClass
	public static void init() {
		TestUtil.delete(new File("tests", "testEC1.yml"));
	}
	
	private static class TestEC1Class {

	}
	
	@Test
	public void testEmpty() throws Exception {
		new ConfigBuilder<TestEC1Class>(TestEC1Class.class)
				.setFile("tests", "testEC1.yml")
				.loadSafe()
				.build()
				;
		
		Assert.assertEquals("",
				String.join("\n", Files.readAllLines(Paths.get("tests/testEC1.yml"))));
	}
	
	private static class TestEC2Class {
		@ConfigId
		public int id1;
		@ConfigId
		public int id2;	
	}
	
	@Test(expected = RuntimeException.class)
	public void testMultipleIds() throws Exception {
		new ConfigBuilder<TestEC2Class>(TestEC2Class.class);
		
	}
	
	private static class TestEC3Class {
		@ConfigId
		public static int id;
	}
	
	@Test(expected = RuntimeException.class)
	public void testStaticId() throws Exception {
		new ConfigBuilder<TestEC3Class>(TestEC3Class.class);
		
	}
	
	private static class TestEC4Class {
		@ConfigValue
		public static int id;
		@ConfigValue(name = "id")
		public static int uid;
	}
	
	@Test(expected = RuntimeException.class)
	public void testDuplicatedNames() throws Exception {
		new ConfigBuilder<TestEC4Class>(TestEC4Class.class);
		
	}
	
	private static class TestEC5Class {
		@ConfigId
		@ConfigValue
		public static List<String> uid;
	}
	
	@Test(expected = RuntimeException.class)
	public void testDuplicatedAnnotations() throws Exception {
		new ConfigBuilder<TestEC5Class>(TestEC5Class.class);
		
	}
	
}
