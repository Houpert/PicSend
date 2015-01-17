package dataclass;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yhoupert on 08/01/15.
 */
public class PictureData {

    private Bitmap picture;
    private String pseudo;
    private String email;
    private String filePath;
    private Date date;
    private GeoLocal geo;
    private Type type;
    private EtatType etat;

    public PictureData() {
    }

    public PictureData(String email, String pseudo) {
        this.email = email;
        this.pseudo = pseudo;
    }

    @Override
    public String toString() {
        return "PictureData{" +
                "picture=" + picture +
                ", pseudo='" + pseudo + '\'' +
                ", email='" + email + '\'' +
                ", date=" + date +
                ", geo=" + geo +
                ", type=" + type +
                ", etat=" + etat +
                '}';
    }

    public void storeImage(Bitmap image, Context context) {
        File pictureFile = getOutputMediaFile(context);
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();

            Log.v("debug", "hasSave");

        } catch (FileNotFoundException e) {
            Log.v("debug", "NotFound");
        } catch (IOException e) {
            Log.v("debug", "NotFound");
        }
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(Context context){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.    + getApplicationContext().getPackageName()
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    /*Getter & Setter*/
    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public GeoLocal getGeo() {
        return geo;
    }

    public void setGeo(GeoLocal geo) {
        this.geo = geo;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public EtatType getEtat() {
        return etat;
    }

    public void setEtat(EtatType etat) {
        this.etat = etat;
    }
}
