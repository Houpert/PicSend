package dataclass;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yhoupert on 08/01/15.
 */
public class PictureData implements Serializable {

    private String pseudo;
    private String email;
    private String filePath;
    private Date date;
    private GeoLocal geo;
    private InterestPoint interes;
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
                "  pseudo='" + pseudo + '\'' +
                ", email='" + email + '\'' +
                ", filePath='" + filePath + '\'' +
                ", date=" + date +
                ", geo=" + geo +
                ", interes=" + interes +
                ", type=" + type +
                ", etat=" + etat +
                '}';
    }

    /*Getter & Setter*/
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

    public InterestPoint getInteres() {
        return interes;
    }

    public void setInteres(InterestPoint interes) {
        this.interes = interes;
    }
}
