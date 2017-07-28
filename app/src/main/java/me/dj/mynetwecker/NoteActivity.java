package me.dj.mynetwecker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DjsVars.muteAlarm();
        this.finish();

    }
}
