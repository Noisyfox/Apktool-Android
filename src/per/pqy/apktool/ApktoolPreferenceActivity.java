/**
 * FileName:     ApktoolPreferenceActivity.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-10 下午11:01:55
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-10      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package per.pqy.apktool;

import java.io.File;

import per.pqy.apktool.GlobalValues.GPath;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * @ClassName: ApktoolPreferenceActivity
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-10 下午11:01:55
 * 
 */
public class ApktoolPreferenceActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {
	private static final String PROJECT_FOLDER = "pref_key_project_folder";
	private static final String SYSTEM_SEPARATOR = File.separator;

	private EditTextPreference mEditTextPreferenceProjectFolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		mEditTextPreferenceProjectFolder = (EditTextPreference) findPreference(PROJECT_FOLDER);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Setup the initial values
		SharedPreferences sharedPreferences = getPreferenceScreen()
				.getSharedPreferences();

		mEditTextPreferenceProjectFolder.setSummary(this.getString(
				R.string.pref_project_folder_summary, sharedPreferences
						.getString(PROJECT_FOLDER, GPath.DEFAULT_PROJECT)));

		// Set up a listener whenever a key changes
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (PROJECT_FOLDER.equals(key)) {
			mEditTextPreferenceProjectFolder.setSummary(this.getString(
					R.string.pref_project_folder_summary, sharedPreferences
							.getString(PROJECT_FOLDER, GPath.DEFAULT_PROJECT)));
		}

	}

	public static String getProjectFolder(Context context) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		String projectFolder = settings.getString(PROJECT_FOLDER, context
				.getString(R.string.default_project_folder,
						GPath.DEFAULT_PROJECT));

		if (TextUtils.isEmpty(projectFolder)) { // setting primary folder =
												// empty("")
			projectFolder = GPath.DEFAULT_PROJECT;
		}

		// it's remove the end char of the project folder setting when it with
		// the '/' at the end.
		int length = projectFolder.length();
		if (length > 1
				&& SYSTEM_SEPARATOR.equals(projectFolder.substring(length - 1))) { // length
																					// =
																					// 1,
																					// ROOT_PATH
			return projectFolder.substring(0, length - 1);
		} else {
			return projectFolder;
		}
	}

}
