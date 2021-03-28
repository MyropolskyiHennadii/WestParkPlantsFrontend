package plantsAPI.integrationTests;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plantsAPI.markers.Geoposition;
import plantsAPI.markers.ImageFileWithMetadata;
import plantsAPI.markers.Plant;
import plantsAPI.markers.PlantsSynonym;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class OperationsWithDatabaseTest {

    private static final Logger logger = LoggerFactory.getLogger(OperationsWithDatabaseTest.class);

    @Test
    @Order(2)
    void getExistingRecordGeoposition() {
        logger.info("Testing getExistingRecord Geoposition");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("geopositions");
        EntityManager em = emf.createEntityManager();
        TypedQuery<Geoposition> q = em.createQuery("SELECT a FROM Geoposition a"
                , Geoposition.class);
        assertThat(q.setMaxResults(1).getResultList().size(), equalTo(1));
        logger.info("Ok - Testing getExistingRecord Geoposition");
    }

    @Test
    @Order(3)
    void getExistingRecordPlant() {
        logger.info("Testing getExistingRecord Plant");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("plants");
        EntityManager em = emf.createEntityManager();
        TypedQuery<Plant> q = em.createQuery("SELECT a FROM Plant a"
                , Plant.class);
        assertThat(q.setMaxResults(1).getResultList().size(), equalTo(1));
        logger.info("Ok - Testing getExistingRecord Plant");
    }

    @Test
    @Order(4)
    void getExistingRecordSynonym() {
        logger.info("Testing getExistingRecord PlantsSynonym");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("plants_synonyms");
        EntityManager em = emf.createEntityManager();
        TypedQuery<PlantsSynonym> q = em.createQuery("SELECT a FROM PlantsSynonym a"
                , PlantsSynonym.class);
        assertThat(q.setMaxResults(1).getResultList().size(), equalTo(1));
        logger.info("Ok - Testing getExistingRecord PlantsSynonym");
    }

    @Test
    @Order(5)
    void getExistingRecordEvent() {
        logger.info("Testing getExistingRecord PlantsEvent");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("plants_events");
        EntityManager em = emf.createEntityManager();
        TypedQuery<PlantsSynonym> q = em.createQuery("SELECT a FROM PlantsSynonym a"
                , PlantsSynonym.class);
        assertThat(q.setMaxResults(1).getResultList().size(), equalTo(1));
        logger.info("Ok - Testing getExistingRecord PlantsEvent");
    }

    @Test
    @Order(6)
    void getExistingRecordPictures() {
        logger.info("Testing getExistingRecord Pictures");
        EntityManagerFactory emfPlant = Persistence.createEntityManagerFactory("pictures");
        EntityManager em = emfPlant.createEntityManager();
        TypedQuery<ImageFileWithMetadata> q = em.createQuery("SELECT a FROM ImageFileWithMetadata a"
                , ImageFileWithMetadata.class);
        assertThat(q.setMaxResults(1).getResultList().size(), equalTo(1));
        logger.info("Ok - Testing getExistingRecord PlantsEvent");
    }
}