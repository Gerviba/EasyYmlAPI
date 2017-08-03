package hu.gerviba.easyymlapi;

import java.io.File;

import org.junit.BeforeClass;

import hu.gerviba.easyymlapi.utils.TestUtil;

public class MapOutputTest {
	
	@BeforeClass
	public static void init() {
		TestUtil.delete(new File("tests", "testMO1.yml"));
	}
	
}
