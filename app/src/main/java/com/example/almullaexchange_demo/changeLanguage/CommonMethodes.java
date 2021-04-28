package com.example.almullaexchange_demo.changeLanguage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CommonMethodes {

    public static void saveStringPreferences(Context context, String strKey, String strValue) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(strKey, strValue);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveBooleanPreferences(Context context, String strKey, boolean boolValue) {
        try {
            if (context == null) return;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(strKey, boolValue);
            editor.apply();
        } catch (Exception e) {
            e.toString();
        }
    }

    public static Object getPreferences(Context context, String key, int preferenceDataType)
    {
        Object value = null;
        if (context == null) return null;
        try
        {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            switch (preferenceDataType)
            {
                case CommonEnvironmentValues.PREFTYPE_BOOLEAN:
                    value = sharedPreferences.getBoolean(key, false);
                    break;
                case CommonEnvironmentValues.PREFTYPE_INT:
                    value = sharedPreferences.getInt(key, 0);
                    break;
                case CommonEnvironmentValues.PREFTYPE_STRING:
                    value = sharedPreferences.getString(key, "");
                    break;
            }
        }
        catch (Exception e)
        {
            e.toString();
            return null;
        }
        return value;
    }

}
