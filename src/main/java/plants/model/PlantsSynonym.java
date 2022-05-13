package plants.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "plants_synonyms")
public class PlantsSynonym {
    private static final Logger LOGGER = LogManager.getLogger(PlantsSynonym.class);

    @Id
    //remark because of exchange with local database, where synonym can appear
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_gbif")
    @JsonBackReference//important to prevent infinite loop of references
    private Plant plant;//foreign key in database

    @Column
    private String lang;
    @Column
    private String lang_name;
    @Column
    private String web_reference_wiki;
    @Column
    private int updated;//1 = was updated, 0 = wasn't
    @Column
    private int deleted;//1 = was marked as deleted, 0 = wasn't

    public PlantsSynonym(Plant plant, String lang, String lang_name, String web_reference_wiki) {
        this.plant = plant;
        this.lang = lang;
        this.lang_name = lang_name;
        this.web_reference_wiki = web_reference_wiki;
        this.updated = 0;
        this.deleted = 0;
    }

    public PlantsSynonym(Plant plant, String lang, String lang_name, String web_reference_wiki, long id) {
        this.plant = plant;
        this.lang = lang;
        this.lang_name = lang_name;
        this.web_reference_wiki = web_reference_wiki;
        this.id = id;
        this.updated = 0;
        this.deleted = 0;
    }

    public PlantsSynonym(Plant plant, String lang, String lang_name, String web_reference_wiki, int updated, int deleted) {
        this.plant = plant;
        this.lang = lang;
        this.lang_name = lang_name;
        this.web_reference_wiki = web_reference_wiki;
        this.updated = updated;
        this.deleted = deleted;
    }

    //for exchange between databases
    public PlantsSynonym(long id, String lang, String lang_name, String web_reference_wiki, String gbif) {
        this.id = id;
        this.plant = new Plant(gbif);
        this.lang = lang;
        this.lang_name = lang_name;
        this.web_reference_wiki = web_reference_wiki;
        this.updated = 0;
        this.deleted = 0;
    }


    public PlantsSynonym() {
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLang_name() {
        return lang_name;
    }

    public void setLang_name(String lang_name) {
        this.lang_name = lang_name;
    }

    public String getWeb_reference_wiki() {
        return web_reference_wiki;
    }

    public void setWeb_reference_wiki(String web_reference_wiki) {
        this.web_reference_wiki = web_reference_wiki;
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
        return "PlantsSynonym{" +
                "id=" + id +
                ", plant=" + plant.getId_gbif() +
                ", lang='" + lang + '\'' +
                ", lang_name='" + lang_name + '\'' +
                ", web_reference_wiki='" + web_reference_wiki + '\'' +
                ", updated=" + updated +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public int hashCode() {
        return lang_name.length();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantsSynonym plantsSynonym = (PlantsSynonym) o;
        return getPlant().getId_gbif().equals(plantsSynonym.getPlant().getId_gbif())
                && getLang().equals(((PlantsSynonym) o).getLang());
    }

    /**
     * composes json-representation for author-exemplar
     */
    public JSONObject composeJsonObject() {
        JSONObject jsonSynonym = new JSONObject();
        jsonSynonym.put("id", id);
        jsonSynonym.put("lang", lang);
        jsonSynonym.put("lang_name", lang_name);
        jsonSynonym.put("web_reference_wiki", web_reference_wiki);
        jsonSynonym.put("updated", updated);
        jsonSynonym.put("deleted", deleted);
        return jsonSynonym;
    }
}
