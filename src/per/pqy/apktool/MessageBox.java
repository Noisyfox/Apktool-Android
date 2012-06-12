/**
 * FileName:     MessageBox.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-11 上午9:04:19
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-11      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package per.pqy.apktool;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

/**
 * @ClassName: MessageBox
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-11 上午9:04:19
 * 
 */
public class MessageBox extends BlockedDialog {

	public MessageBox(Activity context) {
		super(context);
	}

	@Override
	public void onCreate() {
		setContentView(R.layout.messagebox);
		this.setCancelable(false);
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

	public int showDialog(String Msg, String Title) {
		TextView TvErrorInfo = (TextView) findViewById(R.id.textViewInfo);
		TvErrorInfo.setText(Msg);
		TextView TvTitle = (TextView) findViewById(R.id.textViewTitle);
		TvTitle.setText(Title);

		return super.showDialog();
	}

}
