package per.pqy.apktool;

import android.content.Context;

import java.io.DataOutputStream;
import java.io.File;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import per.pqy.apktool.GlobalValues.GPath;

public class SystemManager {

	public Boolean mSystemOK = false;
	private Context mContext;
	private Process mProcess = null;
	private DataOutputStream mOs = null;

	public SystemManager(Context context) {
		mContext = context;
	}

	/*
	 * 初始化系统
	 */
	public boolean prepareSystem() {
		//获取su权限
		try {
			mProcess = Runtime.getRuntime().exec("su");
			mOs = new DataOutputStream(mProcess.getOutputStream());
		} catch (Exception e) {
			Toast.makeText(mContext, mContext.getString(R.string.su_fail),
					Toast.LENGTH_LONG).show();
			return false;
		}
		// 检测外置数据完整性
		if (!(new File(GPath.APKTOOL_EXT).exists())) {
			// Toast.makeText(mContext, getString(R.string.no_apktoolext,
			// mSdcard),
			Toast.makeText(mContext,
					mContext.getString(R.string.no_apktoolext, GPath.APKTOOL_EXT),
					Toast.LENGTH_LONG).show();
			return false;
		}
		// 安装内置busybox到/data/local/tmp/
		RootCommand2("dd if=" + GPath.APKTOOL_EXT + "/tools/busybox of=" + GPath.BUSYBOX);
		RootCommand2("chmod 755 " + GPath.BUSYBOX);
		// 挂载系统分区为可写
		RootCommand2(GPath.BUSYBOX + " mount -o remount,rw /");
		RootCommand2(GPath.BUSYBOX + " mount -o remount,rw /system");
		// 创建工作目录
		if (!(new File(GPath.WORKING_DIR).exists()))
			RootCommand2(GPath.BUSYBOX + " mkdir " + GPath.WORKING_DIR);
		// 挂载java
		RootCommand2(GPath.BUSYBOX + " umount /lib");
		RootCommand2(GPath.BUSYBOX + " mount -o loop -t ext2 " + GPath.APKTOOL_EXT
				+ "/tools/java.ext2 /lib");
		// 安装aapt
		if (!new File("/system/lib/libaaptcomplete.so").exists()) {
			RootCommand2("ln -s " + GPath.WORKING_DIR
					+ "/libaaptcomplete.so /system/lib/libaaptcomplete.so");
		}
		if (!new File("/system/bin/aapt").exists()) {
			RootCommand2("ln -s " + GPath.WORKING_DIR + "/aapt /system/bin/aapt");
		}
		// 测试
		if (RootCommand2("aapt") == 127 || RootCommand2(GPath.JRE) == 127) {
			Toast.makeText(mContext,
					mContext.getString(R.string.sys_init_fail),
					Toast.LENGTH_LONG).show();
			return false;
		}
		mSystemOK = true;
		return true;
	}

	public void cleanSystem() {
		// 取消挂载java
		RootCommand2(GPath.BUSYBOX + " umount /lib");
		mSystemOK = false;
		try {
			if (mOs != null) {
				mOs.close();
			}
			mProcess.destroy();
		} catch (Exception e) {
		} finally {
			mOs = null;
			mProcess = null;
		}
	}

	public static boolean RootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			Log.d("hrh", command + " : " + process.waitFor());
		} catch (Exception e) {
			Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
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
		Log.d("*** DEBUG ***", "RootSUC ");
		return true;
	}

	public int RootCommand2(String command) {
		if (mProcess == null || mOs == null)
			return -1;
		int Result = -1;
		try {
			mOs.writeBytes(command + "\n");
			mOs.writeBytes("exit\n");
			mOs.flush();
			Result = mProcess.waitFor();
			Log.d("hrh", command + " : " + Result);
		} catch (Exception e) {
			Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
			return -1;
		}
		Log.d("*** DEBUG ***", "RootSUC ");
		return Result;
	}

}
