package me.dj.mynetwecker;


import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static me.dj.mynetwecker.DjsVars.keepRunning;
import static me.dj.mynetwecker.DjsVars.g_myAct;


public class WakeUpHandler extends Thread{

    private boolean isFirstMsg = true;

    private Context g_myContext;
    private TextView g_InvokerText;

    public int oldVol;
    public AudioManager myAudio;

    public WakeUpHandler(Context p_myContext, TextView p_InvokerText){
        g_myContext = p_myContext;
        g_InvokerText = p_InvokerText;
    }

    public void closeConnection(){
        keepRunning = false;
        this.interrupt();
        return;
    }

    public void run(){

        keepRunning = true;

        if(!isOnline()){
            keepRunning = false;
        }

        try {
            String inCum = null;//( ͡° ͜ʖ ͡°)
            Socket mySocket = null;
            BufferedReader in = null;
            PrintWriter out = null;
            if(keepRunning){
                mySocket = new Socket(DjsVars.const_HostAddress, 25639);
                in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                out = new PrintWriter(mySocket.getOutputStream(), true);
            }
            while (keepRunning) {

                inCum = in.readLine();
                if(inCum == null){break;}
                Log.d(DjsVars.LogTAG, inCum);
                if(isFirstMsg){
                    isFirstMsg = false;
                    out.println("auth apikey=" + DjsVars.const_AuthKey);
                    Log.i(DjsVars.LogTAG, "Authkey was send!");
                    inCum = in.readLine();
                    if(inCum.startsWith("error id=1538")){
                        Log.e(DjsVars.LogTAG, "Wrong Auth Key detected!");
                        DjsVars.spawnToast(g_myAct.getString(R.string.strInvalidAuth));
                        return;
                    }
                    out.println("clientnotifyregister schandlerid=0 event=notifyclientpoke");
                    Log.d(DjsVars.LogTAG, "Registered!");
                }else{
                    final String[] myInString = inCum.split(" ");
                    if(myInString.length >= 5) {
                        if(myInString[5].toLowerCase().equals("msg=" + DjsVars.const_WakePhrase)){

                            g_myAct.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    g_InvokerText.setText(myInString[3].substring("invokername=".length()));
                                    g_InvokerText.setVisibility(View.VISIBLE);
                                    spawnNotification(myInString[3].substring("invokername=".length()));

                                    PowerManager pm = (PowerManager) g_myAct.getApplicationContext().getSystemService(Context.POWER_SERVICE);
                                    PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
                                    wakeLock.acquire();
                                }
                            });



                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                            DjsVars.const_mp = MediaPlayer.create(g_myContext.getApplicationContext(), notification);
                            DjsVars.const_mp.setLooping(true);;
                            myAudio = (AudioManager) g_myAct.getSystemService(Context.AUDIO_SERVICE);
                            oldVol = myAudio.getStreamVolume(AudioManager.STREAM_MUSIC);
                            myAudio.setStreamVolume(AudioManager.STREAM_MUSIC, myAudio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                            DjsVars.const_mp.start();
                            DjsVars.myVibr = (Vibrator) g_myAct.getSystemService(Context.VIBRATOR_SERVICE);
                            DjsVars.myVibr.vibrate(DjsVars.vibrPattern, 0);
                            break;
                        }
                    }
                }
            }
            keepRunning = false;
            this.interrupt();
        }catch (IOException e){
            Log.e(DjsVars.LogTAG, "An error occured!");
            if(DjsVars.const_mp != null) {
                DjsVars.const_mp.stop();
                myAudio.setStreamVolume(AudioManager.STREAM_MUSIC, oldVol, 0);
            }
            e.printStackTrace();
        }
    }


    public boolean isOnline(){
        //setPrgBarVisible(true);
        Socket myConnectionTest = null;
        try{
            myConnectionTest = new Socket(DjsVars.const_HostAddress, 25639);
        }catch(IOException e){
            //setPrgBarVisible(false);
            DjsVars.isOnline = false;
            return false;
        }finally {
            try{
                if(myConnectionTest != null){
                    myConnectionTest.close();
                }
            }catch(IOException e){}
        }
        //setPrgBarVisible(false);
        DjsVars.isOnline = true;
        return true;
    }

    /*private void setPrgBarVisible(boolean p_val){

        final int intVal;

        if (p_val){
            intVal = View.VISIBLE;
        }else{
            intVal = View.INVISIBLE;
        }

        g_myAct.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DjsVars.prgBar.setVisibility(intVal);
            }
        });
    }*/

    public void spawnNotification(String p_invoker){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(g_myContext);
        int color = g_myContext.getResources().getColor(R.color.colorMyRed);
        Intent intent = new Intent(g_myContext, NoteActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(g_myContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(g_myAct.getString(R.string.strWakeTitle))
                .setContentText(p_invoker + " poked you!")
                .setColor(color);

        builder.addAction(R.drawable.ic_info_black_24dp, g_myAct.getString(R.string.strMute), pIntent);

        Notification notification = builder.build();
        NotificationManagerCompat.from(g_myContext).notify(0, notification);

    }
}
