package ter.android.picsend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
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
import mailSender.Mail;
import mailSender.MailFeedTask;

public class MainActivity extends ActionBarActivity {

    private Mail m;
    private boolean photoTake;

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
        if(notEmptyData()) {
            photoTake = false;
            setContentView(R.layout.photo_layout);
            initIdPhoto();
        }else {
            setContentView(R.layout.setting_layout);
            initIdSetting();
            readData();
        }


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

    private void initIdPhoto(){
        img = (ImageView) findViewById(R.id.imageView);
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
            initIdPhoto();

            saveData("pseudo",pseudo_et.getText().toString());
            saveData("email",email_et.getText().toString());
        }
    }

    /*Persistence Data*/
    public void saveData(String key, String value){
        SharedPreferences settings = getSharedPreferences("Data", Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = settings.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public void readData(){
        SharedPreferences settings = getSharedPreferences("Data", Context.MODE_PRIVATE);
        if (settings.contains("pseudo")){
            String pseudo = settings.getString("pseudo","");
            pseudo_et.setText(pseudo);
            picData.setPseudo(pseudo);
        }
        if (settings.contains("email")){
            String email = settings.getString("email","");
            email_et.setText(email);
            picData.setEmail(email);
        }

    }

    public boolean notEmptyData(){
        SharedPreferences settings = getSharedPreferences("Data", Context.MODE_PRIVATE);
        if(settings.contains("pseudo") && settings.contains("email"))
            return true;
        return false;
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

        if(!notEmptyData()) {
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
                readData();
                return true;
            case R.id.menu_photo:
                setContentView(R.layout.photo_layout);
                initIdPhoto();
                photoTake = false;
                return true;
            default:
                return false;
        }
    }

    public void sendEmail(){
        MailFeedTask mft = new MailFeedTask(this,picData);
        mft.execute();

        toastMessage("Email en cours d'envoie");

        setContentView(R.layout.photo_layout);
        initIdPhoto();
    }

    public void takePhoto(View view) {
        if(photoTake == false) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }else{
            setContentView(R.layout.tag_layout);
            initIdAfterPhoto();

            picData.storeImage(picData.getPicture(), getApplicationContext());
            photoTake = false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(this, imageUri.toString(), Toast.LENGTH_LONG).show();
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);*/

            String fileSrc = null;
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC");
            if(cursor != null && cursor.moveToLast()){
               try {
                   Uri fileURI = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                   fileSrc = fileURI.toString();
                   picData.setFilePath(fileSrc);
                   cursor.close();

                   Bitmap myBitmap = BitmapFactory.decodeFile(fileSrc);
                   picData.setPicture(myBitmap);
                   ImageView myImage = (ImageView) findViewById(R.id.imageView);


                   Display display = getWindowManager().getDefaultDisplay();
                   Point size = new Point();
                   display.getSize(size);

                   myBitmap.createScaledBitmap(myBitmap, size.x/25, size.y/25, true);
                   myImage.setImageBitmap(myBitmap);

                   photoTake = true;
               }catch (Exception e){
                   toastMessage("Not enought memory");
               }
            }
        }else{
            toastMessage("Error during take the photo");
        }
    }
}

