/**
 * FileName:     BlockedDialog.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-11 上午8:52:56
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-11      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package per.pqy.apktool;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;

/**
 * @ClassName:     BlockedDialog
 * @Description:   TODO
 * @author:        Noisyfox
 * @date:          2012-6-11 上午8:52:56
 *
 */
public abstract class BlockedDialog extends Dialog {
	int dialogResult;
	Handler mHandler;

	public BlockedDialog(Activity context) {
		super(context);
		dialogResult = 0;
		setOwnerActivity(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		onCreate();
	}
	
	public abstract void onCreate();
	
	public final void endDialog(int result) {
		dismiss();
		setDialogResult(result);
		Message m = mHandler.obtainMessage();
		mHandler.sendMessage(m);
	}

	public final void setDialogResult(int dialogResult) {
		this.dialogResult = dialogResult;
	}
	
	public final int getDialogResult() {
		return dialogResult;
	}
	
	public final int showDialog() {
		
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message mesg) {
				throw new RuntimeException();
			}
		};
		super.show();
		try {
			Looper.getMainLooper();
			Looper.loop();
		} catch (RuntimeException e2) {
		}
		return dialogResult;
	}
}
