package per.pqy.apktool;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
	Button btn1, btn2, btn3, btn4;
	EditText et1, et2;
	String str1, str2;
	TextView tv;
	String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if (!(new File(SDCARD + "/apktool").exists())) {
			Toast.makeText(this,
					"检测到您手机内存卡缺少相应插件，" + SDCARD + "/apktool文件夹不存在！软件无法工作！",
					Toast.LENGTH_LONG).show();
		}

		SystemManager.RootCommand("mount -o remount,rw yaffs2 /");
		SystemManager.RootCommand("mount -o remount,rw yaffs2 /system");

		File file1 = new File("/system/xbin/busybox"), file2 = new File(
				"/system/bin/busybox"), file3 = new File("/system/sbin/busybox");
		if ((!file1.exists()) && (!file2.exists()) && (!file3.exists())) {
			SystemManager.RootCommand("dd if=" + SDCARD
					+ "/apktool/busybox of=/system/bin/busybox");
			SystemManager.RootCommand("chmod 755 /system/bin/busybox");
		}
		if (!(new File("/lib").exists()))
			SystemManager.RootCommand("busybox mkdir /lib");
		if (!(new File("/tmp").exists()))
			SystemManager.RootCommand("busybox mkdir /tmp");
		SystemManager.RootCommand("busybox mount -t ext2 " + SDCARD
				+ "/apktool/ext2 /lib");
		File file4 = new File("/system/lib/libaaptcomplete.so");
		if (!file4.exists()) {
			SystemManager
					.RootCommand("ln -s /lib/libaaptcomplete.so /system/lib/libaaptcomplete.so");
		}
		File file5 = new File("/system/bin/aapt");
		if (!file5.exists()) {
			SystemManager.RootCommand("ln -s /lib/aapt /system/bin/aapt");
		}

		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);

		et1 = (EditText) findViewById(R.id.et1);
		et2 = (EditText) findViewById(R.id.et2);

		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub

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

				Process process = null;
				DataOutputStream os = null;

				try {

					String cmd = "sh " + SDCARD + "/apktool/apktool.sh d -f "
							+ str1 + " " + str2;
					process = Runtime.getRuntime().exec("su");
					os = new DataOutputStream(process.getOutputStream());
					os.writeBytes(cmd + "\n");
					os.writeBytes("exit\n");
					os.flush();

					process.waitFor();

					Toast.makeText(ApktoolActivity.this, "反编译完成！",
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
				;

			}

		});

		btn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub

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
				// TODO Auto-generated method stub
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

					String cmd = "sh " + SDCARD + "/apktool/signapk.sh " + str1
							+ " " + str2;

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

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.item1:
			// 跳转Activity
			Intent intent = new Intent();
			intent.setClass(ApktoolActivity.this, About.class);
			startActivity(intent);
			return true;

		}

		return false;
	}
}
