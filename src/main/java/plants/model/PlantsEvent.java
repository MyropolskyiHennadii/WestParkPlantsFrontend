package plants.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "plants_events")
public class PlantsEvent {

    private static Logger logger = LoggerFactory.getLogger(PlantsEvent.class);

    @Id
    //remark because of exchange with local database, where event can appear
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String event;
    @Column
    private int date_from;
    @Column
    private int month_from;
    @Column
    private int date_to;
    @Column
    private int month_to;
    @Column
    private int updated;//1 = was updated, 0 = wasn't
    @Column
    private int deleted;//1 = was marked as deleted, 0 = wasn't

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_gbif")
    @JsonBackReference//important to prevent infinite loop of references
    private Plant plant;//foreign key in database

    public PlantsEvent() {
    }

    public PlantsEvent(String event, int date_from, int month_from, int date_to, int month_to, Plant plant) {
        this.event = event;
        this.date_from = date_from;
        this.month_from = month_from;
        this.date_to = date_to;
        this.month_to = month_to;
        this.plant = plant;
        this.updated = 0;
        this.deleted = 0;
    }

    public PlantsEvent(String event, int date_from, int month_from, int date_to, int month_to, int updated, int deleted, Plant plant) {
        this.event = event;
        this.date_from = date_from;
        this.month_from = month_from;
        this.date_to = date_to;
        this.month_to = month_to;
        this.updated = updated;
        this.deleted = deleted;
        this.plant = plant;
    }

    //for exchange between local and remote database
    public PlantsEvent(int id, String event, int date_from, int month_from, int date_to, int month_to, String id_gbif) {
        this.id = id;
        this.event = event;
        this.date_from = date_from;
        this.month_from = month_from;
        this.date_to = date_to;
        this.month_to = month_to;
        this.plant = new Plant(id_gbif);
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getDate_from() {
        return date_from;
    }

    public void setDate_from(int date_from) {
        this.date_from = date_from;
    }

    public int getMonth_from() {
        return month_from;
    }

    public void setMonth_from(int month_from) {
        this.month_from = month_from;
    }

    public int getDate_to() {
        return date_to;
    }

    public void setDate_to(int date_to) {
        this.date_to = date_to;
    }

    public int getMonth_to() {
        return month_to;
    }

    public void setMonth_to(int month_to) {
        this.month_to = month_to;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantsEvent that = (PlantsEvent) o;
        return getEvent().equals(that.getEvent()) &&
                getPlant().equals(that.getPlant());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlant());
    }

    @Override
    public String toString() {
        return "PlantsEvent{" +
                "event='" + event + '\'' +
                ", id=" + id +
                ", date_from=" + date_from +
                ", month_from=" + month_from +
                ", date_to=" + date_to +
                ", month_to=" + month_to +
                ", plant=" + plant +
                '}';
    }
}
