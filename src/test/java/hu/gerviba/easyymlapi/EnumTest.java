package hu.gerviba.easyymlapi;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.bukkit.Material;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.gerviba.easyymlapi.ConfigBuilder;
import hu.gerviba.easyymlapi.identifiers.ConfigValue;
import hu.gerviba.easyymlapi.utils.TestUtil;

public class EnumTest {
	
	@BeforeClass
	public static void init() {
		TestUtil.delete(new File("tests", "testE1.yml"));
		TestUtil.delete(new File("tests", "testE2.yml"));
	}
	
	@SuppressWarnings("unused")
	private static class TestE1Class {
		
		private static int ignoreThisGlobal;
		private int ignoreThis;
		
		@ConfigValue
		private static Enum1 globalEnum;
		
		@ConfigValue(defaultValue = "VALUE2")
		private Enum1 testEnum;
		
	}
	
	private static enum Enum1 {
		VALUE1,
		VALUE2,
		VALUE3;
	}
	
	@Test
	public void testEnum() throws Exception {
		new ConfigBuilder<TestE1Class>(TestE1Class.class)
				.setFile("tests", "testE1.yml")
				.loadSafe()
				.build();
		
		Assert.assertEquals(Enum1.VALUE1, TestE1Class.globalEnum);
		Assert.assertEquals(
				"globalEnum: VALUE1\n" +
				"'0':\n" +
				"  testEnum: VALUE2",
				String.join("\n", Files.readAllLines(Paths.get("tests/testE1.yml"))));
	}
	
	private static class TestE2Class {
		
		@ConfigValue
		private static Material globalMaterial;
		
		@ConfigValue(defaultValue = "BOOKSHELF")
		private Material bookshelf;
		
	}
	
	@Test
	public void testMaterial() throws Exception {
		new ConfigBuilder<TestE2Class>(TestE2Class.class)
				.setFile("tests", "testE2.yml")
				.loadSafe()
				.build();
		
		Assert.assertEquals(Material.AIR, TestE2Class.globalMaterial);
		Assert.assertEquals(
				"globalMaterial: AIR\n" +
				"'0':\n" +
				"  bookshelf: BOOKSHELF",
				String.join("\n", Files.readAllLines(Paths.get("tests/testE2.yml"))));
	}
	
}
