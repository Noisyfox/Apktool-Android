package per.pqy.apktool;

import java.io.DataOutputStream;
import java.io.File;

import per.pqy.apktool.GlobalValues.GMsg;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ApktoolActivity extends Activity {
	/** Called when the activity is first created. */
	Button btn1, btn2, btn3, btn_in, btn_out;
	EditText et1, et2;
	String str1, str2;
	TextView tv;
	String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
	Context mContext;
	SystemManager SM;
	ApkOperator Apktool;
	ProgressDialog mProgressDialog = null;
	ApkProject ap = new ApkProject(this);

	private static int RESULTCODE_FILESELECT_IN = 1;
	private static int RESULTCODE_FILESELECT_OUT = 2;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.getData().getInt(GMsg.MSG_TYPE, GMsg.MSG_NULL)) {
			case GMsg.MSG_SHOW_LOADING_DIALOG:
				loadingDialog(msg.getData().getString(GMsg.MSG_INFO), true);
				break;
			case GMsg.MSG_HIDE_LOADING_DIALOG:
				loadingDialog(false);
				break;
			case GMsg.MSG_LOADING_START:// 开始初始化
				loadingDialog(true);
				break;
			case GMsg.MSG_LOADING_FINISH:// 初始化成功
				loadingDialog(false);
				break;
			case GMsg.MSG_LOADING_FAIL:// 初始化失败
				loadingDialog(false);
				break;
			case GMsg.MSG_SHOW_TOAST:
				Toast.makeText(mContext,
						msg.getData().getString(GMsg.MSG_INFO),
						Toast.LENGTH_LONG).show();
				break;
			case GMsg.MSG_NULL:
			default:
				break;
			}
		}
	};

	Handler mApkOperatorHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.getData().getInt(GMsg.MSG_TYPE, GMsg.MSG_NULL)) {
			case GMsg.MSG_SHOW_LOADING_DIALOG:
				loadingDialog(msg.getData().getString(GMsg.MSG_INFO), true);
				break;
			case GMsg.MSG_HIDE_LOADING_DIALOG:
				loadingDialog(false);
				break;
			case GMsg.MSG_LOADING_START:// 开始初始化
				loadingDialog(true);
				break;
			case GMsg.MSG_LOADING_FINISH:// 初始化成功
				loadingDialog(false);
				break;
			case GMsg.MSG_LOADING_FAIL:// 初始化失败
				loadingDialog(false);
				break;
			case GMsg.MSG_SHOW_TOAST:
				Toast.makeText(mContext,
						msg.getData().getString(GMsg.MSG_INFO),
						Toast.LENGTH_LONG).show();
				break;
			case GMsg.MSG_NULL:
			default:
				break;
			}
		}
	};

	private void doInit() {
		Thread initThread = new Thread() {
			public void err_Msg(String s) {
				Util.sendMessage(mHandler, GMsg.MSG_SHOW_TOAST, s);
			}

			public void init_Fail() {
				Util.sendMessage(mHandler, GMsg.MSG_LOADING_FAIL);
			}

			private void init_Start() {
				Util.sendMessage(mHandler, GMsg.MSG_LOADING_START);
			}

			public void init_Success() {
				Util.sendMessage(mHandler, GMsg.MSG_LOADING_FINISH);
			}

			public void run() {
				init_Start();
				try {
					SM.prepareSystem();
					ap.saveProject();
				} catch (Exception e) {
					err_Msg(e.getMessage());
					init_Fail();
				}
				init_Success();
			}
		};
		initThread.start();
	}

	private void loadingDialog(boolean show) {
		if (show) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
			mProgressDialog = ProgressDialog.show(mContext, "",
					mContext.getString(R.string.loading), true);
		} else {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
		}
	}

	private void loadingDialog(String msg, boolean show) {
		if (show) {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
			mProgressDialog = ProgressDialog.show(mContext, "", msg, true);
		} else {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULTCODE_FILESELECT_IN) {
			Bundle bundle = null;
			if (data != null && (bundle = data.getExtras()) != null) {
				et1.setText(bundle.getString("file"));
			}
		} else if (requestCode == RESULTCODE_FILESELECT_OUT) {
			Bundle bundle = null;
			if (data != null && (bundle = data.getExtras()) != null) {
				et2.setText(bundle.getString("file"));
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContext = this;
		SM = new SystemManager(mContext);
		Apktool = new ApkOperator(mContext, SM, mApkOperatorHandler);
		// 初始化
		doInit();

		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		btn_in = (Button) findViewById(R.id.selectinput);
		btn_out = (Button) findViewById(R.id.selectoutput);

		et1 = (EditText) findViewById(R.id.et1);
		et2 = (EditText) findViewById(R.id.et2);

		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				str1 = et1.getText().toString();
				str2 = et2.getText().toString();
				if (!(new File(str1).exists())) {
					Toast.makeText(ApktoolActivity.this,
							"文件" + str1 + "不存在！请检查", Toast.LENGTH_LONG).show();
					return;
				}
				if ((new File(str1).isDirectory())) {
					Toast.makeText(ApktoolActivity.this, str1 + "是一个文件夹！请检查",
							Toast.LENGTH_LONG).show();
					return;
				}
				if ((new File(str2).isFile())) {
					Toast.makeText(ApktoolActivity.this, str1 + "是一个文件！请检查",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (new File(str2).isDirectory()) {

					Toast.makeText(ApktoolActivity.this,
							"文件夹" + str2 + "已存在！请另填。", Toast.LENGTH_LONG)
							.show();
					return;
				} else if (new File(str2).isFile()) {
					Toast.makeText(ApktoolActivity.this,
							str2 + "指向的是一个已存在的文件，请填写文件夹！", Toast.LENGTH_LONG)
							.show();
					return;
				}

				String cmd = "sh " + SDCARD + "/apktool/apktool.sh d -f "
						+ str1 + " " + str2;
				SystemManager.RootCommand(cmd);
				Toast.makeText(ApktoolActivity.this, "反编译完成！",
						Toast.LENGTH_LONG).show();

			}

		});

		btn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				str1 = et1.getText().toString();
				str2 = et2.getText().toString();
				if (new File(str1).exists()) {
					Toast.makeText(ApktoolActivity.this,
							"文件（夹）" + str1 + "已存在！请另取文件名。", Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (!(new File(str2).exists())) {
					Toast.makeText(ApktoolActivity.this,
							"文件夹" + str2 + "不存在！请检查。", Toast.LENGTH_LONG)
							.show();
					return;
				}
				if ((new File(str2).isFile())) {
					Toast.makeText(ApktoolActivity.this, str1 + "是一个文件！请检查",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (!(new File(str2 + "/AndroidManifest.xml").exists())) {
					Toast.makeText(
							ApktoolActivity.this,
							"文件夹" + str2
									+ "缺失AndroidManifest.xml文件，可能不包含源码，请检查。",
							Toast.LENGTH_LONG).show();
					return;
				}
				Process process = null;
				DataOutputStream os = null;
				try {

					String cmd = "sh  " + SDCARD + "/apktool/apktool.sh b  "
							+ str2 + " " + str1;

					process = Runtime.getRuntime().exec("su");
					os = new DataOutputStream(process.getOutputStream());
					os.writeBytes(cmd + "\n");
					os.writeBytes("exit\n");
					os.flush();
					process.waitFor();
					Toast.makeText(ApktoolActivity.this, "重编译完成！",
							Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());

				} finally {
					try {
						if (os != null) {
							os.close();
						}
						process.destroy();
					} catch (Exception e) {
					}
				}

			}
		}

		);

		btn3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				str1 = et1.getText().toString();
				str2 = et2.getText().toString();
				if (!(new File(str1).exists())) {
					Toast.makeText(ApktoolActivity.this, str1 + "不存在！请检查",
							Toast.LENGTH_LONG).show();
					return;
				}
				if ((new File(str1).isDirectory())) {
					Toast.makeText(ApktoolActivity.this, str1 + "是一个文件夹！请检查",
							Toast.LENGTH_LONG).show();
					return;
				}
				if ((new File(str2).exists())) {
					Toast.makeText(ApktoolActivity.this, str2 + "已经存在！请另取文件名",
							Toast.LENGTH_LONG).show();
					return;
				}

				Process process = null;
				DataOutputStream os = null;
				try {

					String cmd = "sh " + SDCARD + "/apktool/sign/signapk.sh "
							+ str1 + " " + str2;

					process = Runtime.getRuntime().exec("su");
					os = new DataOutputStream(process.getOutputStream());
					os.writeBytes(cmd + "\n");
					os.writeBytes("exit\n");
					os.flush();
					process.waitFor();
					Toast.makeText(ApktoolActivity.this, "签名完成！",
							Toast.LENGTH_LONG).show();

				} catch (Exception e) {
					Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());

				} finally {
					try {
						if (os != null) {
							os.close();
						}
						process.destroy();
					} catch (Exception e) {
					}
				}

			}
		}

		);

		btn_in.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ApktoolActivity.this,
						MyFileManager.class);
				startActivityForResult(intent, RESULTCODE_FILESELECT_IN);
			}
		});

		btn_out.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ApktoolActivity.this,
						MyFileManager.class);
				startActivityForResult(intent, RESULTCODE_FILESELECT_OUT);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		SM.cleanSystem();
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {

		case R.id.item1:
			// 跳转Activity
			intent = new Intent();
			intent.setClass(ApktoolActivity.this, About.class);
			startActivity(intent);
			return true;

		case R.id.preference:
			// 跳转Activity
			intent = new Intent();
			intent.setClass(ApktoolActivity.this,
					ApktoolPreferenceActivity.class);
			startActivity(intent);
			return true;

		}

		return false;
	}

}
