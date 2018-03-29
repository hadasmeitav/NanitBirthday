package com.nanit.birthday.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.nanit.birthday.R;

/**
 * Created by hadas on 3/28/18.
 */

public class Preferences {

    private SharedPreferences sharedPrefs;

    public enum Key {
        Name, Date
    }

    public Preferences(Context context) {
        sharedPrefs = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    public void setValue(Key key, String value) {
        sharedPrefs.edit().putString(key.name(), value).commit();
    }

    public String getValue(Key key) {
        return sharedPrefs.getString(key.name(), null);
    }

}
