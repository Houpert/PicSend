package ter.android.picsend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Date;

import dataclass.GeoLocation;
import dataclass.PointOfInterest;
import dataclass.PictureData;
import mailSender.Mail;

public class MainActivity extends ActionBarActivity {

    private Mail m;
    private boolean photoTaken = false;

    /*Const*/
    static final int REQUEST_IMAGE_CAPTURE = 1;

    /*Debug Variable*/
    private static final String TAG = "MainActivity";

    /*Other Variable*/
    private PictureData picData = new PictureData();
    private GeoLocation geoLocation = new GeoLocation();
    private LocationListener locationListener;
    private LocationManager locationManager;
    private ImageView img;
    private ImageView interact;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!settingsFilled()) {
            Intent settingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(settingsActivity);
        } else {
            if (photoTaken) {
                Intent tagActivity = new Intent(this, TagActivity.class);
                startActivity(tagActivity);
            } else {
                setContentView(R.layout.photo_layout);
                initPhoto();
                takePhoto(null);
            }
        }

        /*GeoLocation*/
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                geoLocation.setLatAndLong(location.getLatitude(), location.getLongitude());
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

    private void initPhoto(){
        img = (ImageView) findViewById(R.id.imageView);
        interact = (ImageView) findViewById(R.id.interact);
        button = (Button) findViewById(R.id.button_photo);
    }

    /*Toast Message*/
    private void toastMessage(String msg, int time) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, time);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
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

    public boolean settingsFilled(){
        SharedPreferences settings = getSharedPreferences("Data", Context.MODE_PRIVATE);
        if(settings.contains("pseudo") && settings.contains("email"))
            return true;
        return false;
    }


    public void takePhoto(View view) {
        if(!settingsFilled()){
            Intent settingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(settingsActivity);
        }else{
            picData.setPseudo(readData("pseudo"));
            picData.setEmail(readData("email"));

        }

        if(photoTaken == false) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                button.setText(R.string.next);
            }

        }else{
            photoTaken = false;
            Intent tagActivity = new Intent(this, TagActivity.class);
            tagActivity.putExtra("picData", picData);
            startActivity(tagActivity);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(this, imageUri.toString(), Toast.LENGTH_LONG).show();
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

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
                    img.setImageBitmap(myBitmap);

                    toastMessage("Add a point of interest on the picture",  Toast.LENGTH_LONG);
                    this.img.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            int[] viewCoords = new int[2];
                            img.getLocationOnScreen(viewCoords);

                            int touchX = (int) event.getX();
                            int touchY = (int) event.getY();

                            int imageX = touchX - viewCoords[0]; // viewCoords[0] is the X coordinate
                            int imageY = touchY - viewCoords[1]; // viewCoords[1] is the y coordinate

                            interact.setX(touchX);
                            interact.setY(touchY);
                            interact.setVisibility(View.VISIBLE);


                            Drawable drawable = img.getDrawable();
                            Rect imageBounds = drawable.getBounds();

                            //Taille de l'image de base
                            int intrinsicHeight = drawable.getIntrinsicHeight();
                            int intrinsicWidth = drawable.getIntrinsicWidth();

                            //Taille de l'image resize
                            int scaledHeight = img.getHeight();
                            int scaledWidth = img.getWidth();

                            //Ratio de l'image
                            float heightRatio = intrinsicHeight / scaledHeight;
                            float widthRatio = intrinsicWidth / scaledWidth;

                            //distance ecran clique user
                            float scaledImageOffsetX = touchX - imageBounds.left;
                            float scaledImageOffsetY = touchY - imageBounds.top;

                            //coord de l'image
                            int originalImageOffsetX = (int)(Math.round(scaledImageOffsetX * widthRatio));
                            int originalImageOffsetY = (int)(Math.round(scaledImageOffsetY * heightRatio));

                            PointOfInterest ip = new PointOfInterest(originalImageOffsetX,originalImageOffsetY);
                            picData.setPointOfInterest(ip);

                            toastMessage("Point of interest added", Toast.LENGTH_SHORT);
                            return false;
                        }
                    });




                    if(!settingsFilled()) {
                        Intent settingsActivity = new Intent(this, SettingsActivity.class);
                        startActivity(settingsActivity);
                    }

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                    picData.setGeoLocation(geoLocation);
                    picData.setDate(new Date());

                    photoTaken = true;
                }catch (Exception e){
                    toastMessage("Not enough memory",  Toast.LENGTH_SHORT);
                }
            }
        }else{
            toastMessage("Error during photo taking",  Toast.LENGTH_SHORT);
        }
    }
}

