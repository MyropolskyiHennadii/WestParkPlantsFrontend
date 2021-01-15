package plantsAPI.markers;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDate;

@Entity
@Table(name = "pictures")
public class ImageFileWithMetadata {

    private static Logger logger = LoggerFactory.getLogger(ImageFileWithMetadata.class);

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

    @Transient
    private Double longitude;
    @Transient
    private Double latitude;

    public ImageFileWithMetadata(File file) {
        this.file = file;
    }

    public ImageFileWithMetadata() {
        this.latitude = -999999.99999;
        this.longitude = -999999.99999;
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
}
