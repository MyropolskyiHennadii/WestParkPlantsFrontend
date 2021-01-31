package plantsAPI.repository;

import plantsAPI.markers.ImageFileWithMetadata;
import plantsAPI.markers.Plant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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

}
