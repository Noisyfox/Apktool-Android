package per.pqy.apktool;

import android.os.Environment;

public abstract class GlobalValues {

	public static final class GPath {
		public static final String SD_CARD = Environment
				.getExternalStorageDirectory().getAbsolutePath();
		public static final String APKTOOL_EXT = SD_CARD + "/apktool";
		public static final String WORKING_DIR = "/lib";
		public static final String BUSYBOX = "/data/local/tmp/busybox";
		public static final String JRE = WORKING_DIR + "/ejre/bin/java";
		public static final String JAR_APKTOOL = APKTOOL_EXT + "/apktool.jar";
		public static final String JAR_SIGNAPK = APKTOOL_EXT
				+ "/sign/signapk.jar";

	}

	public static final class GString {
		public static final String LOG_TAG = "apkworkstation";
	}

	public static final class GMsg {
		public static final int MSG_NULL = 0;
		public static final int MSG_SHOW_LOADING_DIALOG = 1;
		public static final int MSG_HIDE_LOADING_DIALOG = 2;
		public static final int MSG_LOADING_START = 3;
		public static final int MSG_LOADING_FINISH = 4;
		public static final int MSG_LOADING_FAIL = 5;
		public static final int MSG_SHOW_TOAST = 6;
		public static final int MSG_CORE_ENABLE = 7;
		public static final int MSG_CORE_DISABLE = 8;
		public static final String MSG_TYPE = "MSG";
		public static final String MSG_INFO = "MSG_INFO";
	}

	public static final class GCore {
		public static final String MAINAPP_VERSION = "1.0";
		public static final String EXTERNAL_VERSION = "1.0";
	}
	
	public static final class GMark {
		public static final int MARK_FILE_APK_INPUT = 1;
		public static final int MARK_FILE_APK_OUTPUT = 2;
		public static final int MARK_FILE_DIR_INPUT = 3;
		public static final int MARK_FILE_DIR_OUTPUT = 4;
		public static final int MARK_FILE_DIR_PROJECT = 5;
	}

}
