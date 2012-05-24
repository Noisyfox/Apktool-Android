package per.pqy.apktool;

import android.os.Environment;

public abstract class GlobalValues {

	static public class GPath {
		static public final String SD_CARD = Environment
				.getExternalStorageDirectory().getAbsolutePath();
		static public final String APKTOOL_EXT = SD_CARD + "/apktool";
		static public final String WORKING_DIR = "/lib";
		static public final String BUSYBOX = "/data/local/tmp/busybox";
		static public final String JRE = WORKING_DIR + "/ejre/bin/java";
		static public final String JAR_APKTOOL = APKTOOL_EXT + "/apktool.jar";
		static public final String JAR_SIGNAPK = APKTOOL_EXT
				+ "/sign/signapk.jar";

	}

	static public class GString {
		static public String LOG_TAG = "apkworkstation";
	}
	
	public static final class GCore {
		//public static final  
	}

}
