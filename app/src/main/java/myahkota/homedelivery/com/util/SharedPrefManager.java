package myahkota.homedelivery.com.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import myahkota.homedelivery.com.App;


public class SharedPrefManager {

    private static SharedPrefManager instance;
    private SharedPreferences sharedPreferences;

    private SharedPrefManager(Context _context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
    }

    public static SharedPrefManager getInstance() {
        if (instance == null) {
            instance = new SharedPrefManager(App.getAppContext());
        }
        return instance;
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void saveString(String _key, String _value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(_key, _value);
        editor.apply();
    }

    public void saveInt(String _key, int _value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(_key, _value);
        editor.apply();
    }

    public void saveLong(String _key, long _value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(_key, _value);
        editor.apply();
    }


    public void saveBoolean(String _key, boolean _value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(_key, _value);
        editor.apply();
    }

    public String retrieveString(String _s) {
        return sharedPreferences.getString(_s, "");
    }

    public boolean retrieveBoolean(String _s) {
        return sharedPreferences.getBoolean(_s, false);
    }

    public int retrieveInt(String _s) {
        return sharedPreferences.getInt(_s, -1);
    }

    public long retrieveLong(String _s) {
        return sharedPreferences.getLong(_s, -1);
    }


    public void saveCity(String _value) {
        saveString("CITY_ID", _value);
    }

    public String retrieveCity() {
        return retrieveString("CITY_ID");
    }




}
