package dataclass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<String> tags = new ArrayList<String>();

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
                ", type=" + tags.toString() +
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public PointOfInterest getPointOfInterest() {
        return pointOfInterest;
    }

    public void setPointOfInterest(PointOfInterest pointOfInterest) {
        this.pointOfInterest = pointOfInterest;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
