package plantsAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import plantsAPI.markers.PlantsEvent;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<PlantsEvent, String> {

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
//TODO FUNCTION to MySQL
        TypedQuery<PlantsEvent> q = em.createQuery("SELECT a FROM PlantsEvent a WHERE " +
                "a.event = '" +
                event + "'", PlantsEvent.class);
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
            if (currentDate.isBefore(to) && currentDate.isAfter(from)) {
                listEvents.add(ev.getPlant().getId_gbif());
            }
        }
        return listEvents;
    }
}
