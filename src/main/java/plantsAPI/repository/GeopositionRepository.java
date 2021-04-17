package plantsAPI.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import plantsAPI.markers.Geoposition;
import plantsAPI.markers.Plant;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface GeopositionRepository extends JpaRepository<Geoposition, Long> {

    Logger logger = LoggerFactory.getLogger(GeopositionRepository.class);
    EntityManagerFactory emfGeopositionAdmin = Persistence.createEntityManagerFactory("geopositions_remote_admin");
    EntityManagerFactory emfGeoposition = Persistence.createEntityManagerFactory("geopositions");

    default Double[] getLongLatRectangle() {
        Double[] rectangle = new Double[4];
        //EntityManagerFactory emfGeoposition = Persistence.createEntityManagerFactory("geopositions");
        EntityManager em = emfGeoposition.createEntityManager();

        try {
            EntityTransaction t = em.getTransaction();
            try {
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
            } finally {
                if (t.isActive()) {
                    logger.error("-----------------Something wrong with getting main rectangle");
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }

        return rectangle;
    }

    //list of different and USED in Geopositions plants
    default List<Plant> getDifferentPlants() {
        List<Plant> plants = new ArrayList<>();

        //EntityManagerFactory emfGeoposition = Persistence.createEntityManagerFactory("geopositions");
        EntityManager em = emfGeoposition.createEntityManager();
        TypedQuery<Plant> q = em.createQuery("SELECT DISTINCT a.plant FROM Geoposition a  WHERE a.deleted =" +'0' + " ORDER BY a.plant.scientific_name", Plant.class);

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
    default List<Geoposition> getNotDeletedGeopositionsWithPlantsID() {
        //EntityManagerFactory emfGeoposition = Persistence.createEntityManagerFactory("geopositions");
        EntityManager em = emfGeoposition.createEntityManager();
        List<Geoposition> allGeoposition = new ArrayList<>();
        TypedQuery<Geoposition> q = em.createQuery("SELECT NEW plantsAPI.markers.Geoposition(a.id, a.longitude, a.latitude, a.plant.id_gbif) FROM Geoposition a WHERE a.deleted != '" + 1 + "'", Geoposition.class);
        try {
            EntityTransaction t = em.getTransaction();
            try {
                t.begin();
                allGeoposition = q.getResultList();
                t.commit();
            } finally {
                if (t.isActive()) {
                    logger.error("-----------------Something wrong with getting list of geopositions");
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }
        return allGeoposition;
    }

    /**
     * quicker than findAll: getting all geopositions without details
     *
     * @return
     */
    default List<Geoposition> getDeletedGeopositions() {
        //EntityManagerFactory emfGeoposition = Persistence.createEntityManagerFactory("geopositions");
        EntityManager em = emfGeoposition.createEntityManager();
        List<Geoposition> allGeoposition = new ArrayList<>();
        TypedQuery<Geoposition> q = em.createQuery("SELECT NEW plantsAPI.markers.Geoposition(a.id) FROM Geoposition a WHERE a.deleted = '" + 1 + "'", Geoposition.class);
        try {
            EntityTransaction t = em.getTransaction();
            try {
                t.begin();
                allGeoposition = q.getResultList();
                t.commit();
            } finally {
                if (t.isActive()) {
                    logger.error("-----------------Something wrong with getting list of deleted geopositions");
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }
        return allGeoposition;
    }

    /**
     * quicker than findAll: getting all geopositions without details
     *
     * @return
     */
    default List<Geoposition> getUpdatedGeopositions() {
        //EntityManagerFactory emfGeoposition = Persistence.createEntityManagerFactory("geopositions");
        EntityManager em = emfGeoposition.createEntityManager();
        List<Geoposition> allGeoposition = new ArrayList<>();
        TypedQuery<Geoposition> q = em.createQuery("SELECT NEW plantsAPI.markers.Geoposition(a.id, a.longitude, a.latitude, a.plant.id_gbif) FROM Geoposition a WHERE a.updated = '" + 1 + "'", Geoposition.class);
        try {
            EntityTransaction t = em.getTransaction();
            try {
                t.begin();
                allGeoposition = q.getResultList();
                t.commit();
            } finally {
                if (t.isActive()) {
                    logger.error("-----------------Something wrong with getting list of updated geopositions");
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }
        return allGeoposition;
    }

    /**
     * delete geoposition by id
     *
     * @param id
     */
    default void deleteGeoposition(long id) {
        //EntityManagerFactory emfGeoposition = Persistence.createEntityManagerFactory("geopositions_remote_admin");
        EntityManager em = emfGeopositionAdmin.createEntityManager();
        Geoposition geoExist = em.find(Geoposition.class, id);
        if (geoExist != null) {
            try {
                EntityTransaction t = em.getTransaction();
                try {
                    t.begin();
                    logger.info("Delete geoposition id {}", id);
                    em.remove(geoExist);
                    t.commit();
                } finally {
                    if (t.isActive()) {
                        logger.error("-----------------Something wrong with deleting geoposition's id {}", id);
                        t.rollback();
                    }
                }
            } finally {
                em.close();
            }
        }
    }

    /**
     * update geoposition
     *
     * @param geoposition
     */
    default void updateGeoposition(Geoposition geoposition) {
        //EntityManagerFactory emfGeoposition = Persistence.createEntityManagerFactory("geopositions_remote_admin");
        EntityManager em = emfGeopositionAdmin.createEntityManager();
        try {
            EntityTransaction t = em.getTransaction();
            try {
                t.begin();
                logger.info("update geoposition id {}", geoposition.getId());
                em.merge(geoposition);
                t.commit();
            } finally {
                if (t.isActive()) {
                    logger.error("-----------------Something wrong with updating geoposition's id {}", geoposition.getId());
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }
    }

    /**
     * add new geoposition
     * @param geoposition
     */
    default void addGeoposition(Geoposition geoposition) {
        //EntityManagerFactory emfGeoposition = Persistence.createEntityManagerFactory("geopositions_remote_admin");
        EntityManager em = emfGeopositionAdmin.createEntityManager();
        try {
            EntityTransaction t = em.getTransaction();
            try {
                t.begin();
                logger.info("add geoposition id {}", geoposition.getId() + ". Geoposition = " + geoposition);
                logger.info("persist...");
                em.persist(geoposition);
                t.commit();
            } finally {
                if (t.isActive()) {
                    logger.error("-----------------Something wrong with adding geoposition's id {}", geoposition.getId());
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }
    }
}
