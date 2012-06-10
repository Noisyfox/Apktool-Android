package per.pqy.apktool;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MessageBox extends Dialog {
	int dialogResult;
	Handler mHandler;

	public MessageBox(Activity context) {
		super(context);
		dialogResult = 0;
		setOwnerActivity(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		onCreate();
	}

	public void endDialog(int result) {
		dismiss();
		setDialogResult(result);
		Message m = mHandler.obtainMessage();
		mHandler.sendMessage(m);
	}

	public int getDialogResult() {
		return dialogResult;
	}

	public void onCreate() {
		setContentView(R.layout.messagebox);
		findViewById(R.id.btnCancel).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View paramView) {
						endDialog(0);
					}
				});
		findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				endDialog(1);
			}
		});
	}

	public void setDialogResult(int dialogResult) {
		this.dialogResult = dialogResult;
	}

	public int showDialog(String Msg, String Title) {
		TextView TvErrorInfo = (TextView) findViewById(R.id.textViewInfo);
		TvErrorInfo.setText(Msg);
		TextView TvTitle = (TextView) findViewById(R.id.textViewTitle);
		TvTitle.setText(Title);

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
