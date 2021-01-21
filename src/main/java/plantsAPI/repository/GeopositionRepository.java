package plantsAPI.repository;

import plantsAPI.markers.Geoposition;
import plantsAPI.markers.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public interface GeopositionRepository extends JpaRepository<Geoposition, Long> {

    default Double[] getLongLatRectangle(){
        Double[] rectangle = new Double[4];
        EntityManagerFactory emfGeoposition = Persistence.createEntityManagerFactory("geopositions");
        EntityManager em = emfGeoposition.createEntityManager();

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

        return rectangle;
    }

    //list of different and USED in Geopositions plants
    default List<Plant> getDifferentPlants(){
        EntityManagerFactory emfGeoposition = Persistence.createEntityManagerFactory("geopositions");
        EntityManager em = emfGeoposition.createEntityManager();
        TypedQuery<Plant> q = em.createQuery("SELECT DISTINCT a.plant FROM Geoposition a ORDER BY a.plant.scientific_name", Plant.class);
        List<Plant> plants = q.getResultList();
        //service element of the list: for filter in React
        plants.add(0, new Plant("All (without filter)",  "All (without filter)"));
        return plants;
    }

    /**
     * quicker than findAll: getting all geopositions without details
     * @return
     */
    default List<Geoposition> getAllGeopositionsWithPlantsID(){
        EntityManagerFactory emfGeoposition = Persistence.createEntityManagerFactory("geopositions");
        EntityManager em = emfGeoposition.createEntityManager();
        TypedQuery<Geoposition> q = em.createQuery("SELECT NEW plantsAPI.markers.Geoposition(a.id, a.longitude, a.latitude, a.plant.id_gbif) FROM Geoposition a", Geoposition.class);
        return q.getResultList();
    }
}
