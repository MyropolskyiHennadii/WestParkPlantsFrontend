package plants.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import plants.model.PlantsSynonym;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public interface SynonymRepository extends JpaRepository<PlantsSynonym, String> {

    Logger logger = LoggerFactory.getLogger(SynonymRepository.class);
    EntityManagerFactory emfSynonymAdmin = Persistence.createEntityManagerFactory("plants_synonyms_remote_admin");
    EntityManagerFactory emfSynonym = Persistence.createEntityManagerFactory("plants_synonyms");

    /**
     * get list updated plant's synonyms
     * @return
     */
    default List<PlantsSynonym> getUpdatedSynonyms(){
        List<PlantsSynonym> plantsSynonyms = new ArrayList<>();
        //EntityManagerFactory emfSynonym = Persistence.createEntityManagerFactory("plants_synonyms");
        EntityManager em = emfSynonym.createEntityManager();
        TypedQuery<PlantsSynonym> q = em.createQuery("SELECT NEW plantsAPI.markers.PlantsSynonym(a.id, a.lang, a.lang_name, a.web_reference_wiki, a.plant.id_gbif) " +
                "FROM PlantsSynonym a WHERE a.updated = '" + 1 + "'", PlantsSynonym.class);

        try {
            EntityTransaction t = em.getTransaction();
            try {
                t.begin();
                plantsSynonyms = q.getResultList();
                t.commit();
            } finally {
                if (t.isActive()) {
                    logger.error("-----------------Something wrong with getting list of updated synonyms!");
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }
        return plantsSynonyms;
    }

    /**
     * update synonym
     * @param synonym
     */
    default void updateSynonym(PlantsSynonym synonym) {
        //EntityManagerFactory emfPlant = Persistence.createEntityManagerFactory("plants_synonyms_remote_admin");
        EntityManager em = emfSynonymAdmin.createEntityManager();
        PlantsSynonym synonymExist = em.find(PlantsSynonym.class, synonym.getId());
        try {
            EntityTransaction t = em.getTransaction();
            try {
                t.begin();
                logger.info("update synonym id {}", synonym.getId());
                if (synonymExist == null) {
                    em.persist(synonym);
                } else {
                    em.merge(synonym);
                }
                t.commit();
            } finally {
                if (t.isActive()) {
                    logger.error("-----------------Something wrong with updating synonym id {}", synonym.getId());
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }
    }
}
