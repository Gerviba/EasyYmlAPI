package hu.gerviba.easyymlapi.utils;

import java.io.File;

public final class TestUtil {

	public static void delete(File file) {
		if (file.exists())
			file.delete();
	}
	
}
