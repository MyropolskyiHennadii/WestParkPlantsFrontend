package myropolskyi.plants.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDate;

@Entity
@Table(name = "pictures")
public class ImageFileWithMetadata {

    private static final Logger LOGGER = LogManager.getLogger(ImageFileWithMetadata.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Transient
    private File file;
    @Column
    private String organ;
    @Column
    private LocalDate photos_date;
    @Column
    private String path_to_picture;//path to final directory (final pictures directory)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_gbif")
    @JsonBackReference//important to prevent infinite loop of references
    private Plant plant;//foreign key in database
/*    @Column
    private int updated;//1 = was updated, 0 = wasn't
    @Column
    private int deleted;//1 = was marked as deleted, 0 = wasn't*/

    @Transient
    private Double longitude;
    @Transient
    private Double latitude;

    public ImageFileWithMetadata(File file) {
        this.file = file;
/*        this.updated = 0;
        this.deleted = 0;*/
    }

    public ImageFileWithMetadata() {
        this.latitude = -999999.99999;
        this.longitude = -999999.99999;
/*        this.updated = 0;
        this.deleted = 0;*/
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public LocalDate getPhotos_date() {
        return photos_date;
    }

    public void setPhotos_date(LocalDate photos_date) {
        this.photos_date = photos_date;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getOrgan() {
        return organ;
    }

    public void setOrgan(String organ) {
        this.organ = organ;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath_to_picture() {
        return path_to_picture;
    }

    public void setPath_to_picture(String path_to_picture) {
        this.path_to_picture = path_to_picture;
    }

/*    public int getUpdated() {
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
    }*/

    @Override
    public String toString() {
        return "Image {" + (file == null ? "No File!!!" : file.getName()) +
                ", date = " + photos_date +
                ", longitude = " + longitude +
                ", latitude = " + latitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (getPath_to_picture() == null || getPath_to_picture().isEmpty()) return false;
        ImageFileWithMetadata that = (ImageFileWithMetadata) o;
        if (getFile() != null && !getFile().getAbsolutePath().isEmpty()) {
            if (that.getFile() != null) {
                return (getFile().getAbsolutePath().equals(that.getFile().getAbsolutePath()));
            } else {
                return false;
            }
        }
        return getPath_to_picture().equals(((ImageFileWithMetadata) o).getPath_to_picture());
    }

    @Override
    public int hashCode() {
        return getLongitude().intValue();
    }

    /**
     * composes json-representation for author-exemplar
     */
    public JSONObject composeJsonObject() {
        JSONObject jsonImage = new JSONObject();
        jsonImage.put("id", id);
        jsonImage.put("organ", organ);
        jsonImage.put("photos_date", photos_date.toString());
        jsonImage.put("path_to_picture", path_to_picture);
        return jsonImage;
    }
}
