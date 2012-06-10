package per.pqy.apktool;

import java.io.DataOutputStream;
import java.io.File;

import per.pqy.apktool.GlobalValues.GPath;
import per.pqy.apktool.GlobalValues.GString;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class SystemManager {

	public static boolean RootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			Log.d(GString.LOG_TAG, command + " : " + process.waitFor());
		} catch (Exception e) {
			Log.d(GString.LOG_TAG, "ROOT REE" + e.getMessage());
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		Log.d(GString.LOG_TAG, "RootSUC ");
		return true;
	}

	public Boolean mSystemOK = false;

	private Context mContext;

	public SystemManager(Context context) {
		mContext = context;
	}

	public void cleanSystem() {
		// 取消挂载java
		RootCommand2(GPath.BUSYBOX + " umount /lib");
		mSystemOK = false;
	}

	/*
	 * 初始化系统
	 */
	public void prepareSystem() throws Exception {
		// 检测外置数据完整性
		if (!(new File(GPath.APKTOOL_EXT).exists())) {
			// Toast.makeText(mContext, getString(R.string.no_apktoolext,
			// mSdcard),
			throw new Exception(mContext.getString(R.string.no_apktoolext,
					GPath.APKTOOL_EXT));
		}
		// 安装内置busybox到/data/local/tmp/
		RootCommand2("dd if=" + GPath.APKTOOL_EXT + "/tools/busybox of="
				+ GPath.BUSYBOX);
		RootCommand2("chmod 755 " + GPath.BUSYBOX);
		// 挂载系统分区为可写
		RootCommand2(GPath.BUSYBOX + " mount -o remount,rw /");
		RootCommand2(GPath.BUSYBOX + " mount -o remount,rw /system");
		// 创建工作目录
		if (!(new File(GPath.WORKING_DIR).exists()))
			RootCommand2(GPath.BUSYBOX + " mkdir " + GPath.WORKING_DIR);
		// 挂载java
		RootCommand2(GPath.BUSYBOX + " umount /lib");
		RootCommand2(GPath.BUSYBOX + " mount -o loop -t ext2 "
				+ GPath.APKTOOL_EXT + "/tools/java.ext2 /lib");
		// 安装aapt
		if (!new File("/system/lib/libaaptcomplete.so").exists()) {
			RootCommand2("ln -s " + GPath.WORKING_DIR
					+ "/libaaptcomplete.so /system/lib/libaaptcomplete.so");
		}
		if (!new File("/system/bin/aapt").exists()) {
			RootCommand2("ln -s " + GPath.WORKING_DIR
					+ "/aapt /system/bin/aapt");
		}
		// 测试
		if (RootCommand2("aapt") == 127 || RootCommand2(GPath.JRE) == 127) {
			throw new Exception(mContext.getString(R.string.sys_init_fail));
		}
		mSystemOK = true;
	}

	public int RootCommand2(String command) {
		Process process = null;
		DataOutputStream os = null;
		int Result = -1;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			Result = process.waitFor();
			Log.d(GString.LOG_TAG, command + " : " + Result);
		} catch (Exception e) {
			Toast.makeText(mContext, mContext.getString(R.string.su_fail),
					Toast.LENGTH_LONG).show();
			Log.d(GString.LOG_TAG, "ROOT REE" + e.getMessage());
			return -1;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		Log.d(GString.LOG_TAG, "RootSUC ");
		return Result;
	}

}
