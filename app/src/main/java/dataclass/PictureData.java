package dataclass;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yhoupert on 08/01/15.
 */
public class PictureData implements Serializable {

    private String pseudo;
    private String email;
    private String filePath;
    private Date date;
    private GeoLocation geoLocation;
    private PointOfInterest pointOfInterest;
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
                " pseudo='" + pseudo + '\'' +
                ", email='" + email + '\'' +
                ", filePath='" + filePath + '\'' +
                ", date=" + date +
                ", geoLocation=" + geoLocation +
                ", pointOfInterest=" + pointOfInterest +
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

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
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

    public PointOfInterest getPointOfInterest() {
        return pointOfInterest;
    }

    public void setPointOfInterest(PointOfInterest pointOfInterest) {
        this.pointOfInterest = pointOfInterest;
    }
}
