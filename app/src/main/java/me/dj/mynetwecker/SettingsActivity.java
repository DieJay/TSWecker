package me.dj.mynetwecker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private EditText edtTxtSettingsPw;
    private EditText edtTxtSettingsHost;
    private EditText edtTxtSettingsAuth;
    private EditText edtTxtSettingsPoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        edtTxtSettingsPw    = (EditText) findViewById(R.id.edtTxtSettingsPw);
        edtTxtSettingsHost  = (EditText) findViewById(R.id.edtTxtSettingsHost);
        edtTxtSettingsAuth  = (EditText) findViewById(R.id.edtTxtSettingsAuth);
        edtTxtSettingsPoke  = (EditText) findViewById(R.id.edtTxtSettingsPoke);

        if(DjsVars.const_HostAddress != null){
            edtTxtSettingsHost.setText(DjsVars.const_HostAddress);
        }
        if(DjsVars.const_AuthKey != null){
            edtTxtSettingsAuth.setText(DjsVars.const_AuthKey);
        }
        edtTxtSettingsPoke.setText(DjsVars.const_WakePhrase);
    }

    public void updateSettings(View view){

        if(!edtTxtSettingsPw.getText().toString().isEmpty()){
            DjsVars.setConst_PassWord(edtTxtSettingsPw.getText().toString());
        } else{
            DjsVars.setConst_PassWord(DjsVars.const_PassWord);
        }
        DjsVars.setConst_HostAddress(edtTxtSettingsHost.getText().toString());
        DjsVars.setConst_AuthKey(edtTxtSettingsAuth.getText().toString());
        DjsVars.setConst_WakePhrase(edtTxtSettingsPoke.getText().toString());

        DjsVars.updateSavedVals();

        Toast.makeText(getBaseContext(), getString(R.string.strSettingsSaved), Toast.LENGTH_SHORT).show();

    }
}
