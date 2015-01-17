package ter.android.picsend;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Date;

import dataclass.EtatType;
import dataclass.PictureData;
import dataclass.Type;
import mailSender.MailFeedTask;

/**
 * Created by yhoupert on 17/01/15.
 */
public class TagActivity extends Activity {

    private PictureData picData = new PictureData();

    /*AfterPhoto variable*/
    private RadioGroup rg_type;
    private RadioGroup rg_etat;
    private RadioButton rb_etat_1;
    private RadioButton rb_etat_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_layout);
        picData = (PictureData) getIntent().getSerializableExtra("picData");
        Log.v("DEBUG",picData.toString());

        initIdAfterPhoto();



    }


    private void initIdAfterPhoto() {
        rg_type = (RadioGroup) findViewById(R.id.type);
        rg_etat = (RadioGroup) findViewById(R.id.etat);
        rb_etat_1 = (RadioButton) findViewById(R.id.rb_etat_1);
        rb_etat_2 = (RadioButton) findViewById(R.id.rb_etat_2);

        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.rb_animal) {
                    rb_etat_1.setText(R.string.invertebre);
                    rb_etat_2.setText(R.string.vertebre);
                }else {
                    rb_etat_1.setText(R.string.more50);
                    rb_etat_2.setText(R.string.less50);
                }
            }

        });

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
                startActivity(mainActivity);
                return true;
            case R.id.menu_settings:
                Intent settingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivity);
                return true;
            default:
                return false;
        }

    }

    /*Toast Message*/
    private void toastMessage(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void sendEmail(){
        MailFeedTask mft = new MailFeedTask(this,picData);
        mft.execute();

        toastMessage("Email en cours d'envoie");

        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

    public void validData(View view) {
        int selected_Type = rg_type.getCheckedRadioButtonId();
        int selected_etat = rg_etat.getCheckedRadioButtonId();

        if(R.id.rb_animal == selected_Type) {
            picData.setType(Type.Animal);
            if(R.id.rb_etat_1 == selected_etat)
                picData.setEtat(EtatType.Invertebre);
            else
                picData.setEtat(EtatType.Vertebre);
        }else {
            picData.setType(Type.Plante);
            if(R.id.rb_etat_1 == selected_etat)
                picData.setEtat(EtatType.More50);
            else
                picData.setEtat(EtatType.Less50);
        }

        sendEmail();
        Log.v("DEBUG", picData.toString());

    }
}
