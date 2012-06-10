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

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @ClassName: ApktoolPreferenceActivity
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-10 下午11:01:55
 * 
 */
public class ApktoolPreferenceActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub

	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
