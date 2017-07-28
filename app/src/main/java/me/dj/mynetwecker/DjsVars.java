package me.dj.mynetwecker;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class DjsVars{

    private static SharedPreferences mySavedSettings;

    public static Vibrator myVibr;
    public static final long[] vibrPattern = {0, 1500, 250};
    public static final String LogTAG = "DjsLog";
    public static MediaPlayer const_mp;


    public static String const_PassWord;
    public static String const_HostAddress;
    public static String const_AuthKey;
    public static String const_WakePhrase;


    private DjsVars(){}


    public static void setPref(MainActivity myAct) {
        mySavedSettings = myAct.getPreferences(Context.MODE_PRIVATE);
        return;
    }

    public static void getSavedVals(){

        const_PassWord = mySavedSettings.getString("Password", "1234");
        if(const_PassWord.isEmpty()){
            const_PassWord = "1234";
        }
        const_HostAddress = mySavedSettings.getString("HostAddress", null);
        const_AuthKey = mySavedSettings.getString("AuthKey", null);
        const_WakePhrase = mySavedSettings.getString("WakePhrase", "wakeup");

        return;
    }

    public static void updateSavedVals(){
        mySavedSettings.edit().putString("Password", const_PassWord).commit();
        mySavedSettings.edit().putString("HostAddress", const_HostAddress).commit();
        mySavedSettings.edit().putString("AuthKey", const_AuthKey).commit();
        mySavedSettings.edit().putString("WakePhrase", const_WakePhrase).commit();

        return;
    }

    public static void setConst_PassWord(String p_PassWord) {
        DjsVars.const_PassWord = p_PassWord;
        return;
    }

    public static void setConst_HostAddress(String p_HostAddress) {
        DjsVars.const_HostAddress = p_HostAddress;
        return;
    }

    public static void setConst_AuthKey(String p_AuthKey) {
        DjsVars.const_AuthKey = p_AuthKey;
        return;
    }

    public static void setConst_WakePhrase(String p_WakePhrase) {
        DjsVars.const_WakePhrase = p_WakePhrase;
        return;
    }
}