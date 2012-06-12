package per.pqy.apktool;

import java.io.File;
import java.util.List;

import per.pqy.apktool.GlobalValues.GMark;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

//负责处理apkproject并与UI进程交互
public class ApkOperator {

	private SystemManager SM;
	private Context mContext;
	private Handler mHandler;

	public ApkOperator(Context context, SystemManager sm, Handler UI) {
		mContext = context;
		SM = sm;
		mHandler = UI;
	}

	public final void buildApk(ApkProject apk) {

	}

	public final ApkProject createApkProject(String projectName,
			String projectpath) {
		if(projectName.replace(" ", "").isEmpty()){
			Toast.makeText(
					mContext,
					mContext.getString(R.string.project_create_emptyprojectname),
					Toast.LENGTH_LONG).show();
			return null;
		}
		ApkProject APK = new ApkProject(mContext);
		APK.project = new File(projectpath);
		if (APK.project.exists()) {
			if (APK.project.isDirectory()) {
				if (!Util.showConfirmDialog(mContext, mContext.getString(
						R.string.project_create_dir_exist,
						APK.project.getAbsoluteFile()))) {
					return null;
				}
			} else {
				Toast.makeText(
						mContext,
						mContext.getString(R.string.dir_is_file,
								APK.project.getAbsoluteFile()),
						Toast.LENGTH_LONG).show();
				return null;
			}
		} else {
			// 创建工程文件夹
			if (!APK.project.mkdirs()) {
				Toast.makeText(
						mContext,
						mContext.getString(R.string.mkdir_fail,
								APK.project.getAbsolutePath()),
						Toast.LENGTH_LONG).show();
				return null;
			}
		}
		// 创建project.xml

		return APK;
	}

	public final void decodeApk(ApkProject apk) {
		File inapk = apk.getFile(GMark.MARK_FILE_APK_INPUT);
		File outdir = apk.getFile(GMark.MARK_FILE_DIR_OUTPUT);
		if (inapk == null) {
			return;
		} else if (!inapk.isFile()) {
			return;
		}
		if (outdir == null) {
			return;
		} else if (!outdir.isDirectory()) {
			return;
		}
	}

	public final void installFramework(List<ApkProject.Framework> framework) {
		for (ApkProject.Framework _res : framework) {
			_res.getFile();
		}
	}

	public final void installFramework(String frameworkRes, String tag) {

	}

	public final void openProject(ApkProject apk) {

	}

	public final void signApk(ApkProject apk) {

	}

}
