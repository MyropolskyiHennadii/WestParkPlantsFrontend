package plants.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import plants.model.Geoposition;
import plants.model.Plant;
import plants.servlets.PlantsServlet;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class GeopositionRepository {

    private static final Logger LOGGER = LogManager.getLogger(GeopositionRepository.class);
    EntityManagerFactory emfGeopositionAdmin = Persistence.createEntityManagerFactory("geopositions_remote_admin");
    EntityManagerFactory emfGeoposition = Persistence.createEntityManagerFactory("geopositions");

    public Double[] getLongLatRectangle() {
        Double[] rectangle = new Double[4];
        EntityManager em = emfGeoposition.createEntityManager();
        EntityTransaction t = em.getTransaction();

        t.begin();
        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery<Number> cq = qb.createQuery(Number.class);
        Root<Geoposition> root = cq.from(Geoposition.class);
        cq.select(qb.min(root.get("longitude")));//min longitude
        rectangle[0] = (Double) em.createQuery(cq).getSingleResult();
        cq.select(qb.max(root.get("longitude")));//max longitude
        rectangle[1] = (Double) em.createQuery(cq).getSingleResult();
        cq.select(qb.min(root.get("latitude")));//min latitude
        rectangle[2] = (Double) em.createQuery(cq).getSingleResult();
        cq.select(qb.max(root.get("latitude")));//max latitude
        rectangle[3] = (Double) em.createQuery(cq).getSingleResult();
        t.commit();

        return rectangle;
    }

    //list of different and USED in Geopositions plants
    public List<Plant> getDifferentPlants() {
        List<Plant> plants = new ArrayList<>();
        EntityManager em = emfGeoposition.createEntityManager();
        TypedQuery<Plant> q = em.createQuery("SELECT DISTINCT a.plant FROM Geoposition a  WHERE a.deleted =" + '0' + " ORDER BY a.plant.scientific_name", Plant.class);

        plants = q.getResultList();
        //service element of the list: for filter in React
        plants.add(0, new Plant("--All (without filter)", "--All (without filter)"));
        return plants;
    }

    /**
     * quicker than findAll: getting all geopositions without details
     *
     * @return
     */
    public List<Geoposition> getNotDeletedGeopositionsWithPlantsID() {

        EntityManager em = emfGeoposition.createEntityManager();
        List<Geoposition> allGeoposition = new ArrayList<>();
        TypedQuery<Geoposition> q = em.createQuery("SELECT NEW plants.model.Geoposition(a.id, a.longitude, a.latitude, a.plant.id_gbif) FROM Geoposition a WHERE a.deleted != '" + 1 + "'", Geoposition.class);
        EntityTransaction t = em.getTransaction();
        t.begin();
        allGeoposition = q.getResultList();
        t.commit();
        return allGeoposition;
    }

    /**
     * quicker than findAll: getting all geopositions without details
     *
     * @return
     */
    public List<Geoposition> getDeletedGeopositions() {

        EntityManager em = emfGeoposition.createEntityManager();
        List<Geoposition> allGeoposition = new ArrayList<>();
        TypedQuery<Geoposition> q = em.createQuery("SELECT NEW plants.model.Geoposition(a.id) FROM Geoposition a WHERE a.deleted = '" + 1 + "'", Geoposition.class);

        EntityTransaction t = em.getTransaction();
        t.begin();
        allGeoposition = q.getResultList();
        t.commit();
        return allGeoposition;
    }

    /**
     * quicker than findAll: getting all geopositions without details
     *
     * @return
     */
    public List<Geoposition> getUpdatedGeopositions() {

        EntityManager em = emfGeoposition.createEntityManager();
        List<Geoposition> allGeoposition = new ArrayList<>();
        TypedQuery<Geoposition> q = em.createQuery("SELECT NEW plants.model.Geoposition(a.id, a.longitude, a.latitude, a.plant.id_gbif) FROM Geoposition a WHERE a.updated = '" + 1 + "'", Geoposition.class);

        EntityTransaction t = em.getTransaction();
        t.begin();
        allGeoposition = q.getResultList();
        t.commit();

        return allGeoposition;
    }

    /**
     * delete geoposition by id
     *
     * @param id
     */
    public void deleteGeoposition(long id) {

        EntityManager em = emfGeopositionAdmin.createEntityManager();
        Geoposition geoExist = em.find(Geoposition.class, id);
        if (geoExist != null) {
            EntityTransaction t = em.getTransaction();
            t.begin();
            LOGGER.info("Delete geoposition id {}", id);
            em.remove(geoExist);
            t.commit();
        }
    }

    /**
     * update geoposition
     *
     * @param geoposition
     */
    public void updateGeoposition(Geoposition geoposition) {

        EntityManager em = emfGeopositionAdmin.createEntityManager();

        EntityTransaction t = em.getTransaction();

        t.begin();
        LOGGER.info("update geoposition id {}", geoposition.getId());
        em.merge(geoposition);
        t.commit();
    }

    /**
     * add new geoposition
     *
     * @param geoposition
     */
    public void addGeoposition(Geoposition geoposition) {

        EntityManager em = emfGeopositionAdmin.createEntityManager();
        EntityTransaction t = em.getTransaction();

        t.begin();
        LOGGER.info("add geoposition id {}", geoposition.getId() + ". Geoposition = " + geoposition);
        LOGGER.info("persist...");
        em.persist(geoposition);
        t.commit();

    }
}
