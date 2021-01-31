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

    /**
     * list with id_gbif of plants with event
     *
     * @param currentDate
     * @param event
     * @return
     */
    default List<String> getEventsByDate(LocalDate currentDate, String event) {
        List<String> listEvents = new ArrayList<>();
        EntityManagerFactory emfEvent = Persistence.createEntityManagerFactory("plants_events");
        EntityManager em = emfEvent.createEntityManager();
        List<PlantsEvent> events = new ArrayList<>();
//TODO FUNCTION to MySQL
        TypedQuery<PlantsEvent> q = em.createQuery("SELECT a FROM PlantsEvent a WHERE " +
                "a.event = '" +
                event + "'", PlantsEvent.class);
 /*       try {
            EntityTransaction t = em.getTransaction();
            try {
                t.begin();*/
                events = q.getResultList();
/*                t.commit();
            } finally {
                if (t.isActive()) {
                    logger.error("-----------------Something wrong with list events");
                    t.rollback();
                }
            }
        } finally {
            em.close();
        }*/
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
            if (currentDate.isBefore(to) && currentDate.isAfter(from)) {
                listEvents.add(ev.getPlant().getId_gbif());
            }
        }

        return listEvents;
    }
}
