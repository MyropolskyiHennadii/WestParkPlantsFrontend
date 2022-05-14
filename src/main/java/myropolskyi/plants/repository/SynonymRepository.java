package myropolskyi.plants.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import myropolskyi.plants.model.PlantsSynonym;

import javax.persistence.*;
import java.util.List;

public class SynonymRepository {

    private static final Logger LOGGER = LogManager.getLogger(SynonymRepository.class);
    EntityManagerFactory emfSynonymAdmin = Persistence.createEntityManagerFactory("plants_synonyms_remote_admin");
    EntityManagerFactory emfSynonym = Persistence.createEntityManagerFactory("plants_synonyms");

    /**
     * get list updated plant's synonyms
     *
     * @return
     */
    public List<PlantsSynonym> getUpdatedSynonyms() {
        //EntityManagerFactory emfSynonym = Persistence.createEntityManagerFactory("plants_synonyms");
        EntityManager em = emfSynonym.createEntityManager();
        TypedQuery<PlantsSynonym> q = em.createQuery("SELECT NEW myropolskyi.plants.model.PlantsSynonym(a.id, a.lang, a.lang_name, a.web_reference_wiki, a.plant.id_gbif) " +
                "FROM PlantsSynonym a WHERE a.updated = '" + 1 + "'", PlantsSynonym.class);
        return q.getResultList();
    }

    /**
     * update synonym
     *
     * @param synonym
     */
    public void updateSynonym(PlantsSynonym synonym) {
        EntityManager em = emfSynonymAdmin.createEntityManager();
        PlantsSynonym synonymExist = em.find(PlantsSynonym.class, synonym.getId());
        EntityTransaction t = em.getTransaction();
        t.begin();
        LOGGER.info("update synonym id {}", synonym.getId());
        if (synonymExist == null) {
            em.persist(synonym);
        } else {
            em.merge(synonym);
        }
        t.commit();
    }
}
