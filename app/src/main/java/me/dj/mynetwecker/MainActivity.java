package me.dj.mynetwecker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import static me.dj.mynetwecker.DjsVars.LogTAG;

/*GitHub release*/

public class MainActivity extends AppCompatActivity {

    private Context g_myContext = this;
    private Activity g_myAct = this;
    private EditText edtTxtPw;
    private Button btnSettings;

    private ToggleButton tglBtnOnOff;

    private WakeUpHandler myWakeUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DjsVars.setPref(this);
        DjsVars.getSavedVals();//Fetch Saved Values from Android Storage

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        tglBtnOnOff = (ToggleButton) findViewById(R.id.tglBtnOnOff);
        edtTxtPw = (EditText) findViewById(R.id.edtTxtPw);
        btnSettings = (Button) findViewById(R.id.btnSettings);
        final TextView txtVwInvoker = (TextView) findViewById(R.id.txtVwInvoker);

        txtVwInvoker.setVisibility(View.INVISIBLE);

        tglBtnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(tglBtnOnOff.isChecked()){
                    if(edtTxtPw.getText().toString().equals("")){
                        tglBtnOnOff.setChecked(false);
                        if(DjsVars.myVibr != null){
                            DjsVars.myVibr.cancel();
                        }
                        Toast.makeText(getBaseContext(), getString(R.string.strEnterPin), Toast.LENGTH_SHORT).show();
                        if(DjsVars.const_mp != null) {
                            DjsVars.const_mp.stop();
                        }
                        if(myWakeUp != null){
                            if(myWakeUp.myAudio != null){
                                myWakeUp.myAudio.setStreamVolume(AudioManager.STREAM_MUSIC, myWakeUp.oldVol, 0);
                            }
                        }


                    }else{
                        if(edtTxtPw.getText().toString().equals(DjsVars.const_PassWord)){

                            myWakeUp = new WakeUpHandler(g_myContext, g_myAct, txtVwInvoker);

                            if(DjsVars.const_HostAddress != null) {
                                if(DjsVars.const_AuthKey != null) {
                                    if(!DjsVars.const_AuthKey.isEmpty()){

                                        if(myWakeUp.isOnline()){
                                            myWakeUp.start();
                                            edtTxtPw.setEnabled(false);
                                            btnSettings.setEnabled(false);
                                            Toast.makeText(getBaseContext(), getString(R.string.strTurnedOn), Toast.LENGTH_SHORT).show();
                                        }else{
                                            btnSettings.setEnabled(true);
                                            edtTxtPw.setEnabled(true);
                                            tglBtnOnOff.setChecked(false);
                                            Toast.makeText(getBaseContext(), getString(R.string.strNoConnect), Toast.LENGTH_SHORT).show();
                                        }

                                    }else{
                                        noAuthKey();
                                    }

                                }else{
                                    noAuthKey();
                                }
                            }else{
                                if(DjsVars.const_mp != null) {
                                    DjsVars.const_mp.stop();
                                    myWakeUp.myAudio.setStreamVolume(AudioManager.STREAM_MUSIC, myWakeUp.oldVol, 0);
                                }
                                edtTxtPw.setEnabled(true);
                                btnSettings.setEnabled(true);
                                tglBtnOnOff.setChecked(false);
                                Toast.makeText(getBaseContext(), getString(R.string.strNoIP), Toast.LENGTH_SHORT).show();
                            }


                        }else{
                            Toast.makeText(getBaseContext(), getString(R.string.strInvalidPW), Toast.LENGTH_SHORT).show();

                            if(DjsVars.const_mp != null) {
                                DjsVars.const_mp.stop();
                                myWakeUp.myAudio.setStreamVolume(AudioManager.STREAM_MUSIC, myWakeUp.oldVol, 0);
                            }
                            edtTxtPw.setEnabled(true);
                            btnSettings.setEnabled(true);
                            tglBtnOnOff.setChecked(false);
                        }
                    }
                }else{
                   Toast.makeText(getBaseContext(), getString(R.string.strTurnedOff), Toast.LENGTH_SHORT).show();
                   if(DjsVars.myVibr != null){
                       DjsVars.myVibr.cancel();
                   }
                   edtTxtPw.setEnabled(true);
                   btnSettings.setEnabled(true);
                   txtVwInvoker.setVisibility(View.INVISIBLE);
                   myWakeUp.closeConnection();
                   if(DjsVars.const_mp != null) {
                       DjsVars.const_mp.stop();
                   }
                   if(myWakeUp.myAudio != null){
                       myWakeUp.myAudio.setStreamVolume(AudioManager.STREAM_MUSIC, myWakeUp.oldVol, 0);
                   }
                   Log.d(LogTAG, "Alarm is now turned off!");
                }
            }
        });

        tglBtnOnOff.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                DjsVars.const_mp = MediaPlayer.create(g_myContext.getApplicationContext(), notification);
                DjsVars.const_mp.start();
                return false;
            }
        });

    }

    public void openSettings(View view){

        if(!edtTxtPw.getText().toString().equals("")){
            if(edtTxtPw.getText().toString().equals(DjsVars.const_PassWord)){
                Intent openSettingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(openSettingsIntent);
            }else{
                Toast.makeText(getBaseContext(), getString(R.string.strInvalidPW), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getBaseContext(), getString(R.string.strEnterPin), Toast.LENGTH_SHORT).show();
        }

    }

    private void noAuthKey(){
        if(DjsVars.const_mp != null) {
            DjsVars.const_mp.stop();
            myWakeUp.myAudio.setStreamVolume(AudioManager.STREAM_MUSIC, myWakeUp.oldVol, 0);
        }
        edtTxtPw.setEnabled(true);
        btnSettings.setEnabled(true);
        tglBtnOnOff.setChecked(false);
        Toast.makeText(getBaseContext(), getString(R.string.strNoAuthKey), Toast.LENGTH_SHORT).show();
    }

}
