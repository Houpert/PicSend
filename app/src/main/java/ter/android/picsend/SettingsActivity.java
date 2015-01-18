package ter.android.picsend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import dataclass.PictureData;

/**
 * Created by yhoupert on 17/01/15.
 */

public class SettingsActivity extends Activity {


    /*Setting View variable*/
    private EditText pseudo_et;
    private EditText email_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        initIdSetting();


        if(notEmptyData("pseudo"))
            pseudo_et.setText(readData("pseudo"));
        if(notEmptyData("email"))
            email_et.setText(readData("email"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_exit:
                System.exit(RESULT_OK);
                return true;
            case R.id.menu_photo:
                Intent mainActivity = new Intent(this, MainActivity.class);
                mainActivity.putExtra("pseudo",pseudo_et.getText());
                mainActivity.putExtra("email",email_et.getText());
                startActivity(mainActivity);
                return true;
            default:
                return false;
        }

    }


    /*Init Block By Layout*/
    private void initIdSetting() {
        pseudo_et = (EditText) findViewById(R.id.setting_et_pseudo);
        email_et = (EditText) findViewById(R.id.setting_et_email);
    }

    /*Persistence Data*/
    public void saveData(String key, String value){
        SharedPreferences settings = getSharedPreferences("Data", Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = settings.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public String readData(String key){
        SharedPreferences settings = getSharedPreferences("Data", Context.MODE_PRIVATE);
        if (settings.contains(key))
            return settings.getString(key,"");
        return null;

    }

    public boolean notEmptyData(String key){
        SharedPreferences settings = getSharedPreferences("Data", Context.MODE_PRIVATE);
        if(settings.contains(key))
            return true;
        return false;
    }

    /*Toast Message*/
    private void toastMessage(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void validSetting(View view) {
        if(pseudo_et.getText().toString().matches("")|| email_et.getText().toString().matches(""))
            toastMessage("All fields are mandatory");
        else {
            saveData("pseudo",pseudo_et.getText().toString());
            saveData("email",email_et.getText().toString());

            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
        }
    }
}

