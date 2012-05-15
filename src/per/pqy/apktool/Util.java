package per.pqy.apktool;

import android.app.Activity;
import android.content.Context;

public class Util {

	public static boolean showConfirmDialog(Context context, String msg) {
		MessageBox msgBox = new MessageBox((Activity) context);
		return msgBox.showDialog(msg, "") == 1 ? true : false;
	}

}
