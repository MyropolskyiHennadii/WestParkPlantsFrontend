package myropolskyi.plants.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import myropolskyi.plants.model.ImageFileWithMetadata;
import myropolskyi.plants.model.Plant;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlantRepository {

    private static final Logger LOGGER = LogManager.getLogger(PlantRepository.class);
    EntityManagerFactory emfPlantAdmin = Persistence.createEntityManagerFactory("plants_remote_admin");
    EntityManagerFactory emfPlant = Persistence.createEntityManagerFactory("myropolskyi/plants");

    /**
     * all paths to photos for the plant
     *
     * @return
     */
    public List<String> getPictureByPlantsID(String id_gbif) {
        EntityManager em = emfPlant.createEntityManager();
        List<String> imagePath = new ArrayList<>();
        Plant plant = em.find(Plant.class, id_gbif);//find plant by id
        imagePath = new ArrayList<>();
        //set of images:
        Set<ImageFileWithMetadata> images = plant.getImages();
        for (ImageFileWithMetadata im : images) {
            imagePath.add(im.getPath_to_picture());
        }

        return imagePath;
    }

    /**
     * list of updated plants
     *
     * @return
     */
    public List<Plant> getUpdatedPlants() {
        EntityManager em = emfPlant.createEntityManager();
        TypedQuery<Plant> q = em.createQuery("SELECT NEW myropolskyi.plants.model.Plant(a.id_gbif, a.common_names, a.scientific_name_family, a.scientific_name_authorship, a.scientific_name, a.web_reference_wiki, a.kind, a.show_only_flowering, 0, 0) FROM Plant a WHERE a.updated = '" + 1 + "'", Plant.class);
        return q.getResultList();
    }

    /**
     * update plant
     *
     * @param plant
     */
    public void updatePlant(Plant plant) {
        EntityManager em = emfPlantAdmin.createEntityManager();
        Plant plantExist = em.find(Plant.class, plant.getId_gbif());
        EntityTransaction t = em.getTransaction();
        t.begin();
        LOGGER.info("update plant id {}", plant.getId_gbif());
        if (plantExist == null) {
            em.persist(plant);
        } else {
            em.merge(plant);
        }
        t.commit();
    }
}
