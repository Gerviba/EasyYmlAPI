package hu.gerviba.easyymlapi;

import java.io.File;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.junit.BeforeClass;
import org.junit.Test;

import ClassParserConfig.ClassParserConfig;
import CustomConfig.CustomConfig;
import ItemStackConfig.ItemStackConfig;
import SimpleConfig.SimpleConfig;
import SimpleMixedConfig.SimpleMixedConfig;
import SimpleMixedConfig.SimpleMixedConfig.CustomerFeeling;
import SimpleStaticConfig.SimpleStaticConfig;
import TeamInfoConfig.TeamInfoConfig;
import hu.gerviba.easyymlapi.utils.TestUtil;
import junit.framework.Assert;

public class ExampleTest {

	@BeforeClass
	public static void init() {
		TestUtil.delete(new File("tests", "ClassParserConfig.yml"));
		TestUtil.delete(new File("tests", "CustomConfig.yml"));
		TestUtil.delete(new File("tests", "ItemStackConfig.yml"));
		TestUtil.delete(new File("tests", "SimpleConfig.yml"));
		TestUtil.delete(new File("tests", "SimpleMixedConfig.yml"));
		TestUtil.delete(new File("tests", "SimpleStaticConfig.yml"));
		TestUtil.delete(new File("tests", "TeamInfoConfig.yml"));
	}
	
	@Test
	public void testClassParser() throws Exception {
		List<ClassParserConfig> list = ClassParserConfig.init();
		Assert.assertEquals("TestClassToParse(byte: -5)", list.get(0).getType1().toString());
		Assert.assertEquals("TestClassToParse(int: 300, float: 33.3)", list.get(0).getType2().toString());
		Assert.assertEquals("TestClassToParse(long: 42, String: String with space, String: Strinct_String)", 
				list.get(0).getType3().toString());
		Assert.assertEquals(new Vector(0.0, 0.0, 0.0), list.get(0).getVector());
		Assert.assertEquals(16715904, list.get(0).getColor().getRGB());
	}
	
	@Test
	public void testCustomConfig() throws Exception {
		Assert.assertEquals("This is a unique info", CustomConfig.init().getCustomValue("Servers.2.UniqueInfo"));
	}
	
	@Test
	public void testItemStackConfig() throws Exception {
		List<ItemStackConfig> list = ItemStackConfig.init();
		Assert.assertEquals(Material.WOOD, list.get(0).getItem().getType());
		Assert.assertEquals(1, list.get(0).getItem().getAmount());
		Assert.assertEquals(2, list.get(0).getItem().getDurability());
	}
	
	@Test
	public void testSimpleConfig() throws Exception {
		List<SimpleConfig> list = SimpleConfig.init();
		Assert.assertEquals(4, list.size());
		Assert.assertEquals("Minecraft Server", list.get(3).getServerName());
		Assert.assertEquals(false, list.get(3).isEnabled());
		Assert.assertEquals(25565, list.get(3).getPort());
		Assert.assertEquals(0, list.get(3).getMaxUsers());
	}
	
	@Test
	public void testSimpleMixedConfig() throws Exception {
		List<SimpleMixedConfig> list = SimpleMixedConfig.init();
		Assert.assertEquals(2, list.size());
		Assert.assertEquals("Steve", list.get(0).getName());
		Assert.assertEquals(Double.doubleToLongBits(1.3), Double.doubleToLongBits(SimpleMixedConfig.icecreamPrice));
		Assert.assertEquals(CustomerFeeling.HAPPY, list.get(1).getFeeling());
	}
	
	@Test
	public void testSimpleStaticConfig() throws Exception {
		SimpleStaticConfig.init();
		Assert.assertEquals("n/a", SimpleStaticConfig.CONFIG_STR1);
		Assert.assertEquals("String 2", SimpleStaticConfig.CONFIG_STR2);
		Assert.assertEquals("String 3", SimpleStaticConfig.CONFIG_STRN);
	}
	
	@Test
	public void testTeamInfoConfig() throws Exception {
		List<TeamInfoConfig> list = TeamInfoConfig.init();
		Assert.assertEquals(4, list.size());
		Assert.assertEquals(0, list.get(0).getId());
		Assert.assertEquals(1, list.get(1).getId());
		Assert.assertEquals(2, list.get(2).getId());
		Assert.assertEquals(3, list.get(3).getId());
		Assert.assertEquals(16711680, list.get(2).getColor());
	}
}
