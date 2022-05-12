package plants.model;

import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "geopositions")
public class Geoposition {

    @Id
    //remark because of exchange with local database, where geoposition can appear
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private double longitude;
    @Column
    private double latitude;
    @Column
    private int updated;//1 = was updated, 0 = wasn't
    @Column
    private int deleted;//1 = was marked as deleted, 0 = wasn't

    @OneToOne(fetch = FetchType.EAGER)//???
    @JoinColumn(name = "id_gbif")
    private Plant plant;//foreign key in database

    public Geoposition(double longitude, double latitude, Plant plant) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.plant = plant;
        this.updated = 0;
        this.deleted = 0;
    }

    public Geoposition(long id, double longitude, double latitude, Plant plant) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.plant = plant;
        this.updated = 0;
        this.deleted = 0;
    }

    //For quick fetch from react
    public Geoposition(long id, double longitude, double latitude, String id_gbif) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.plant = new Plant(id_gbif);
        this.updated = 0;
        this.deleted = 0;
    }

    public Geoposition(double longitude, double latitude, int updated, int deleted, Plant plant) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.updated = updated;
        this.deleted = deleted;
        this.plant = plant;
    }

    public Geoposition(long id) {
        this.id = id;
    }

    public Geoposition() {
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Geoposition{ id " + id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", plantID='" + plant.getId_gbif() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (getPlant() == null) return false;
        Geoposition that = (Geoposition) o;
        return Double.compare(that.getLongitude(), getLongitude()) == 0 &&
                Double.compare(that.getLatitude(), getLatitude()) == 0 &&
                getPlant().getId_gbif().equals(that.getPlant().getId_gbif());
    }

    @Override
    public int hashCode() {
        return (int) longitude;
    }

    /**
     * composes json-representation for author-exemplar
     */
    public JSONObject composeJsonObject() {
        JSONObject jsonGeopositon = new JSONObject();
        jsonGeopositon.put("id", id);
        jsonGeopositon.put("longitude", longitude);
        jsonGeopositon.put("latitude", latitude);
        jsonGeopositon.put("updated", updated);
        jsonGeopositon.put("deleted", deleted);
        jsonGeopositon.put("plant", plant.composeJsonObject());
        return jsonGeopositon;
    }
}
