package me.dj.mynetwecker;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Dj on 28.07.2017.
 * 22:49
 */

public class Updater {

    Context g_myContext;

    public Updater(Context p_myContext){
        DjsVars.setUrl();
        g_myContext = p_myContext;
    }

    public void updateCheck() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(DjsVars.updateDir.openStream()));
        if(Integer.parseInt(in.readLine()) > DjsVars.const_Version){
            Log.i(DjsVars.LogTAG, "Update available!");
        }
    }
}
