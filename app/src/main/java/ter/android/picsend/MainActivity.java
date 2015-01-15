package ter.android.picsend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Date;

import dataclass.EtatType;
import dataclass.GeoLocal;
import dataclass.PictureData;
import dataclass.Type;

public class MainActivity extends ActionBarActivity {

    /*Const*/
    static final int REQUEST_IMAGE_CAPTURE = 1;

    /*Debug Variable*/
    private static final String TAG = "MyActivity";

    /*Other Variable*/
    private PictureData picData = new PictureData();
    private GeoLocal geoLocal = new GeoLocal();
    private LocationListener locationListener;
    private LocationManager locationManager;
    private ImageView img;

    /*Setting View variable*/
    private EditText pseudo_et;
    private EditText email_et;

    /*AfterPhoto variable*/
    private RadioGroup rg_type;
    private RadioGroup rg_etat;
    private RadioButton rb_etat_1;
    private RadioButton rb_etat_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_layout);
        img = (ImageView) findViewById(R.id.imageView);

        /*GeoLocation*/
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                geoLocal.setLatAndLong(location.getLatitude(), location.getLongitude());
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,0,locationListener);
    }

    /*Init Block By Layout*/
    private void initIdSetting() {
        pseudo_et = (EditText) findViewById(R.id.setting_et_pseudo);
        email_et = (EditText) findViewById(R.id.setting_et_email);

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


    /*Toast Message*/
    private void toastMessage(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /*Setting Block*/
    public void valideSetting(View view) {
        if(pseudo_et.getText().toString().matches("")|| email_et.getText().toString().matches(""))
            toastMessage("Un champ a été oubliée");
        else {
            picData.setPseudo(pseudo_et.getText().toString());
            picData.setEmail(email_et.getText().toString());
            setContentView(R.layout.photo_layout);

        }
    }

    /*AfterPhoto Block*/
    public void valideData(View view) {
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

        picData.setDate(new Date());

        if(picData.getPseudo() == null || picData.getEmail() == null) {
            setContentView(R.layout.setting_layout);
            initIdSetting();
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        picData.setGeo(geoLocal);
        sendEmail();
        Log.v(TAG, picData.toString());

    }

    /*Menu Block*/
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
            case R.id.menu_settings:
                setContentView(R.layout.setting_layout);
                initIdSetting();
                return true;
            case R.id.menu_afterphoto:
                setContentView(R.layout.afterphoto_layout);
                initIdAfterPhoto();
                return true;
            default:
                return false;
        }
    }


    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void sendEmail(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"yoann.houpert@yahoo.fr"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "PicSend Project");
        emailIntent.putExtra(Intent.EXTRA_TEXT   , picData.toString());


        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(this, imageUri.toString(), Toast.LENGTH_LONG).show();
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);

            picData.setPicture(imageBitmap);

            Log.v(TAG, picData.toString());

        }
    }
}

