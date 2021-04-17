package plantsAPI.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import plantsAPI.markers.PlantsEvent;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<PlantsEvent, String> {

    Logger logger = LoggerFactory.getLogger(EventRepository.class);
    EntityManagerFactory emfPlantEventAdmin = Persistence.createEntityManagerFactory("plants_events_remote_admin");
    EntityManagerFactory emfPlantEvents = Persistence.createEntityManagerFactory("plants_events");
    /**
     * list with id_gbif of plants with event
     *
     * @param currentDate
     * @param event
     * @return
     */
    default List<String> getEventsByDate(LocalDate currentDate, String event) {
        List<String> listEvents = new ArrayList<>();
        //EntityManagerFactory emfEvent = Persistence.createEntityManagerFactory("plants_events");
        EntityManager em = emfPlantEvents.createEntityManager();
//TODO FUNCTION to MySQL
        TypedQuery<PlantsEvent> q = em.createQuery("SELECT a FROM PlantsEvent a WHERE " +
                "a.event = '" +
                event + "' AND a.deleted = '" + 0 +"'", PlantsEvent.class);
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
                listEvents.add(ev.getPlant().getId_gbif());
            }
        }
        //logger.info("Flowering size: "+listEvents.size());
        return listEvents;
    }

    /**
     * List updated events
     * @return
     */
    default List<PlantsEvent> getUpdatedEvents(){
        List<PlantsEvent> plantsEvents = new ArrayList<>();
        //EntityManagerFactory emfEvents = Persistence.createEntityManagerFactory("plants_events");
        EntityManager em = emfPlantEvents.createEntityManager();

        TypedQuery<PlantsEvent> q = em.createQuery("SELECT a FROM PlantsEvent a WHERE a.updated = '" + 1 + "'", PlantsEvent.class);
        try {
            EntityTransaction t = em.getTransaction();
            try {
                t.begin();
                plantsEvents = q.getResultList();
                t.commit();
            } finally {
                if (t.isActive()) {
                    logger.error("-----------------Something wrong with getting list of updated events!");
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }
        return plantsEvents;
    }

    /**
     * update events
     * @param event
     */
    default void updateEvent(PlantsEvent event) {
        //EntityManagerFactory emfPlant = Persistence.createEntityManagerFactory("plants_events_remote_admin");
        EntityManager em = emfPlantEventAdmin.createEntityManager();
        PlantsEvent eventExist = em.find(PlantsEvent.class, event.getId());
        try {
            EntityTransaction t = em.getTransaction();
            try {
                t.begin();
                logger.info("update event id {}", event.getId());
                if (eventExist == null) {
                    em.persist(event);
                } else {
                    em.merge(event);
                }
                t.commit();
            } finally {
                if (t.isActive()) {
                    logger.error("-----------------Something wrong with updating event id {}", event.getId());
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }
    }
}
