package plantsAPI.repository;

import plantsAPI.markers.Geoposition;
import plantsAPI.markers.ImageFileWithMetadata;
import plantsAPI.markers.Plant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public interface PlantRepository extends JpaRepository<Plant, String> {

    Logger logger = LoggerFactory.getLogger(PlantRepository.class);

    /**
     * all paths to photos for the plant
     *
     * @return
     */
    default List<String> getPictureByPlantsID(String id_gbif) {
        EntityManagerFactory emfPlant = Persistence.createEntityManagerFactory("plants");
        EntityManager em = emfPlant.createEntityManager();

        List<String> imagePath = new ArrayList<>();
        try {
            EntityTransaction t = em.getTransaction();
            try {
                t.begin();
                Plant plant = em.find(Plant.class, id_gbif);//find plant by id
                imagePath = new ArrayList<>();
                //set of images:
                Set<ImageFileWithMetadata> images = plant.getImages();
                for (ImageFileWithMetadata im : images) {
                    imagePath.add(im.getPath_to_picture());
                }
                t.commit();
            } finally {
                if (t.isActive()) {
                    logger.error("-----------------Something wrong with getting pictures path");
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }
        return imagePath;
    }

    /**
     * list of updated plants
     * @return
     */
    default List<Plant> getUpdatedPlants(){
        List<Plant> updated = new ArrayList<>();
        EntityManagerFactory emfPlant = Persistence.createEntityManagerFactory("plants");
        EntityManager em = emfPlant.createEntityManager();
        TypedQuery<Plant> q = em.createQuery("SELECT NEW plantsAPI.markers.Plant(a.id_gbif, a.common_names, a.scientific_name_family, a.scientific_name_authorship, a.scientific_name, a.web_reference_wiki, a.kind, a.show_only_flowering, 0, 0) FROM Plant a WHERE a.updated = '" + 1 + "'", Plant.class);
        try {
            EntityTransaction t = em.getTransaction();
            try {
                t.begin();
                updated = q.getResultList();
                t.commit();
            } finally {
                if (t.isActive()) {
                    logger.error("-----------------Something wrong with getting updated plants");
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }
        return updated;
    }

    /**
     * update plant
     * @param plant
     */
    default void updatePlant(Plant plant) {
        EntityManagerFactory emfPlant = Persistence.createEntityManagerFactory("plants_remote_admin");
        EntityManager em = emfPlant.createEntityManager();
        Plant plantExist = em.find(Plant.class, plant.getId_gbif());
        try {
            EntityTransaction t = em.getTransaction();
            try {
                t.begin();
                logger.info("update plant id {}", plant.getId_gbif());
                if (plantExist == null) {
                    em.persist(plant);
                } else {
/*                    geoExist.setLatitude(geoposition.getLatitude());
                    geoExist.setLongitude(geoposition.getLongitude());
                    geoExist.setPlant(geoposition.getPlant());
                    geoExist.setDeleted(0);
                    geoExist.setUpdated(0);*/
                    em.merge(plant);
                }
                t.commit();
            } finally {
                if (t.isActive()) {
                    logger.error("-----------------Something wrong with updating plant's id {}", plant.getId_gbif());
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }
    }
}
