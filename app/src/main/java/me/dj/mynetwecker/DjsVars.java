package me.dj.mynetwecker;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.net.MalformedURLException;
import java.net.URL;

public class DjsVars{

    public static final int const_Version = 99;


    private static SharedPreferences mySavedSettings;

    public static Vibrator myVibr;
    public static final long[] vibrPattern = {0, 1500, 250};
    public static final String LogTAG = "DjsLog";

    public static URL updateDir;
    public static MediaPlayer const_mp;

    public static ToggleButton tglBtnOnOff;
    public static WakeUpHandler myWakeUp;
    public static TextView txtVwInvoker;
    public static Button btnSettings;
    public static EditText edtTxtPw;
    public static ProgressBar prgBar;
    public static boolean keepRunning = true;
    public static boolean isOnline = false;

    public static String const_PassWord;
    public static String const_HostAddress;
    public static String const_AuthKey;
    public static String const_WakePhrase;





    private DjsVars(){}

    public static void setUrl(){
        try {
            updateDir = new URL("http://macoga.de/apk/TSWecker/");
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
    }

    public static void muteAlarm(){
        tglBtnOnOff.setChecked(false);
        const_mp.stop();
        if(myVibr != null){
            myVibr.cancel();
        }
        if(const_mp != null) {
            const_mp.stop();
        }
        if(myWakeUp != null){
            if(myWakeUp.myAudio != null){
                myWakeUp.myAudio.setStreamVolume(AudioManager.STREAM_MUSIC, myWakeUp.oldVol, 0);
            }
        }
        if(txtVwInvoker != null){
            txtVwInvoker.setText("");
        }
        if(btnSettings != null){
            btnSettings.setEnabled(true);
        }
        if(edtTxtPw != null){
            edtTxtPw.setEnabled(true);
        }
    }

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