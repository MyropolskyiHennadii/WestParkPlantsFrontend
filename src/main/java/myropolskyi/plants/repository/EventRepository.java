package myropolskyi.plants.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import myropolskyi.plants.model.PlantsEvent;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class EventRepository {

    private static final Logger LOGGER = LogManager.getLogger(EventRepository.class);
    EntityManagerFactory emfPlantEventAdmin = Persistence.createEntityManagerFactory("plants_events_remote_admin");
    EntityManagerFactory emfPlantEvents = Persistence.createEntityManagerFactory("plants_events");

    /**
     * list with id_gbif of plants with event
     *
     * @param currentDate
     * @param event
     * @return
     */
    public JSONArray getEventsByDate(LocalDate currentDate, String event) {

        JSONArray floweringId = new JSONArray();
        EntityManager em = emfPlantEvents.createEntityManager();
//TODO FUNCTION to MySQL
        TypedQuery<PlantsEvent> q = em.createQuery("SELECT a FROM PlantsEvent a WHERE " +
                "a.event = '" +
                event + "' AND a.deleted = '" + 0 + "'", PlantsEvent.class);
        List<PlantsEvent> events = q.getResultList();
        for (PlantsEvent ev : events) {
            LocalDate from = LocalDate.of(currentDate.getYear(), ev.getMonth_from(), ev.getDate_from());
            LocalDate to = LocalDate.of(currentDate.getYear(), ev.getMonth_to(), ev.getDate_to());
            if (ev.getMonth_to() < ev.getMonth_from()) {
                if (currentDate.getMonthValue() >= ev.getMonth_from()) {
                    to = to.plusYears(1);
                } else {
                    from = from.minusYears(1);
                }
            }
            if ((currentDate.isBefore(to) || currentDate.equals(to)) && (currentDate.isAfter(from) || currentDate.equals(from))) {
                floweringId.put(ev.getPlant().getId_gbif());
            }
        }
        return floweringId;
    }

    /**
     * List updated events
     *
     * @return
     */
    public List<PlantsEvent> getUpdatedEvents() {
        List<PlantsEvent> plantsEvents = new ArrayList<>();
        EntityManager em = emfPlantEvents.createEntityManager();
        TypedQuery<PlantsEvent> q = em.createQuery("SELECT a FROM PlantsEvent a WHERE a.updated = '" + 1 + "'", PlantsEvent.class);
        return q.getResultList();
    }

    /**
     * update events
     *
     * @param event
     */
    public void updateEvent(PlantsEvent event) {
        EntityManager em = emfPlantEventAdmin.createEntityManager();
        PlantsEvent eventExist = em.find(PlantsEvent.class, event.getId());

        EntityTransaction t = em.getTransaction();

        t.begin();
        LOGGER.info("update event id {}", event.getId());
        if (eventExist == null) {
            em.persist(event);
        } else {
            em.merge(event);
        }
        t.commit();

    }
}
