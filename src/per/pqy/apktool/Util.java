package per.pqy.apktool;

import per.pqy.apktool.GlobalValues.GMsg;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class Util {

	public static boolean showConfirmDialog(Context context, String msg) {
		MessageBox msgBox = new MessageBox((Activity) context);
		return msgBox.showDialog(msg, "") == 1 ? true : false;
	}

	public static void sendMessage(Handler handler, int type, String smsg) {
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putInt(GMsg.MSG_TYPE, type);
		b.putString(GMsg.MSG_INFO, smsg);
		msg.setData(b);
		handler.sendMessage(msg);
	}

	public static void sendMessage(Handler handler, int type) {
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putInt(GMsg.MSG_TYPE, type);
		msg.setData(b);
		handler.sendMessage(msg);
	}

}
