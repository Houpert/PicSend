package dataclass;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by yhoupert on 08/01/15.
 */
public class PictureData {

    private Bitmap picture;
    private String pseudo;
    private String email;
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

    public EtatType getEtat() {
        return etat;
    }

    public void setEtat(EtatType etat) {
        this.etat = etat;
    }
}
