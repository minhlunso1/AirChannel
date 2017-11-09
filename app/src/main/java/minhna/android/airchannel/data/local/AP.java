package minhna.android.airchannel.data.local;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Minh on 11/9/2017.
 */

@Singleton
public class AP {
    private SharedPreferences mAppPreferences;
    private SharedPreferences.Editor mEditor;

    @Inject
    public AP(SharedPreferences mSharedPreferences) {
        this.mAppPreferences = mSharedPreferences;
    }

    public void clearPrefs() {
        mAppPreferences.edit().clear().apply();
    }

    public void saveData(String key, boolean value) {
        mEditor = mAppPreferences.edit();
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    public boolean getData(String key) {
        return mAppPreferences.getBoolean(key, false);
    }

    public boolean getBooleanData(String key, boolean def) {
        return mAppPreferences.getBoolean(key, def);
    }

    public void saveData(String key, String value) {
        mEditor = mAppPreferences.edit();
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public String getStringData(String key) {
        return mAppPreferences.getString(key, null);
    }
    public String getStringDataWithDefault(String key) {
        return mAppPreferences.getString(key, "");
    }

    public String getStringDataWithDefault(String key, String defaultString) {
        return mAppPreferences.getString(key, defaultString);
    }

    public void saveData(String key, long value) {
        mEditor = mAppPreferences.edit();
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public void saveData(String key, int value) {
        mEditor = mAppPreferences.edit();
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public long getLongData(String key) {
        return mAppPreferences.getLong(key, -1);
    }

    public long getLongDataWithDefault(String key) {
        return mAppPreferences.getLong(key, 0);
    }

    public int getIntData(String key) {
        return mAppPreferences.getInt(key, -1);
    }

    public int getIntDataWithDefaultValue(String key, int defaultValue) {
        return mAppPreferences.getInt(key, defaultValue);
    }


    public void saveData(String key, float value) {
        mEditor = mAppPreferences.edit();
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    public float getFloatData(String key) {
        try {
            return mAppPreferences.getFloat(key, 0);
        } catch (Exception e) {
            return 0.0f;
        }
    }
}
