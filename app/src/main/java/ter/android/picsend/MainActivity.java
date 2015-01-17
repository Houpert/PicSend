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
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Date;

import dataclass.EtatType;
import dataclass.GeoLocal;
import dataclass.InterestPoint;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(notEmptyData()) {
            photoTake = false;
            setContentView(R.layout.photo_layout);
            initIdPhoto();
        }else {
            Intent settingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(settingsActivity);
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

    private void initIdPhoto(){
        img = (ImageView) findViewById(R.id.imageView);
    }

    /*Toast Message*/
    private void toastMessage(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
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
                Intent settingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(settingsActivity);
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



    public String readData(String key){
        SharedPreferences settings = getSharedPreferences("Data", Context.MODE_PRIVATE);
        if (settings.contains(key))
            return settings.getString(key,"");
        return null;

    }

    public boolean notEmptyData(){
        SharedPreferences settings = getSharedPreferences("Data", Context.MODE_PRIVATE);
        if(settings.contains("pseudo") && settings.contains("email"))
            return true;
        return false;
    }


    public void takePhoto(View view) {
        if(!notEmptyData()){
            toastMessage("Veuillez fournir les informations");
            Intent settingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(settingsActivity);
        }else{
            picData.setPseudo(readData("pseudo"));
            picData.setEmail(readData("email"));

        }

        if(photoTake == false) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }else{
            photoTake = false;
            Intent tagActivity = new Intent(this, TagActivity.class);
            tagActivity.putExtra("picData",picData);
            startActivity(tagActivity);
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
                    img = (ImageView) findViewById(R.id.imageView);


                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);

                    myBitmap.createScaledBitmap(myBitmap, size.x/25, size.y/25, true);
                    img.setImageBitmap(myBitmap);


                    this.img.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            int[] viewCoords = new int[2];
                            img.getLocationOnScreen(viewCoords);

                            int touchX = (int) event.getX();
                            int touchY = (int) event.getY();

                            int imageX = touchX - viewCoords[0]; // viewCoords[0] is the X coordinate
                            int imageY = touchY - viewCoords[1]; // viewCoords[1] is the y coordinate

                            InterestPoint ip = new InterestPoint(imageX,imageY);
                            picData.setInteres(ip);
                            return false;
                        }
                    });




                    if(!notEmptyData()) {
                        Intent settingsActivity = new Intent(this, SettingsActivity.class);
                        startActivity(settingsActivity);
                    }

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                    picData.setGeo(geoLocal);
                    picData.setDate(new Date());

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

