package plants.servlets;

import plants.model.Geoposition;
import plants.model.Plant;
import plants.model.PlantsEvent;
import plants.model.PlantsSynonym;
import plants.repository.EventRepository;
import plants.repository.GeopositionRepository;
import plants.repository.PlantRepository;
import plants.repository.SynonymRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.Collectors;

/*@CrossOrigin(origins = "*")
@RestController
@RequestMapping("exchangeWestpark/")*/
//Controller for exchange between local and remote databases
public class ExchangeController {

/*    private static final Logger logger = LoggerFactory.getLogger(ExchangeController.class);

    @Autowired
    private GeopositionRepository geopositionRepository;
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private SynonymRepository synonymRepository;
    @Autowired
    private EventRepository eventRepository;

    //to refresh private variables in API
    @Autowired
    private PlantsServlet plantsServlet;*/

/*    @GetMapping("get_deleted_geopositions")*/
/*    public List<Geoposition> getDeletedGeopositions() {
        List<Geoposition> deleted = geopositionRepository.getDeletedGeopositions();
        logger.info("-------------------------- Get REMOTE deleted geo. Size: " + deleted.size());
        return deleted;
    }*/

    /**
     * receive JSON with id geopositions to delete
     *
     * @param params = JSON obj
     * @return OK = true/false
     */
  /*  @PostMapping(value = "set_deleted_geopositions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})*/
    public boolean setDeletedGeopositions(String params) {
/*        logger.info("-------------------------- Set REMOTE deleted geo. " + params);*/
/*        JSONParser parser = new JSONParser();
        try {
            JSONObject results = (JSONObject) parser.parse(params);
            JSONArray deleted = (JSONArray) results.get("deleted_geopositions");
            for (Object el : deleted) {
                JSONObject elJson = (JSONObject) el;
                long id = (Long) elJson.get("id");
                geopositionRepository.deleteGeoposition(id);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        //refresh list geopositions and so on in main controller
        plantsServlet.refreshPrivateVariables();*/
        return true;
    }

    /**
     * get list updated geopositions
     *
     * @return
     */
/*    @GetMapping("get_updated_geopositions")
    public List<Geoposition> getUpdatedGeopositions() {
        logger.info("-------------------------- Get REMOTE updated geo. ");
        List<Geoposition> updated = geopositionRepository.getUpdatedGeopositions();
        logger.info("-------------------------- Size: " + updated.size());
        return updated;
    }*/

    /**
     * receive JSON with id geopositions to update
     *
     * @param @param params = JSON obj
     * @return OK = true/false
     */
    /*@PostMapping(value = "set_updated_geopositions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})*/
    public boolean setUpdatedGeopositions(String params) {
        if (params != null) {
           /* logger.info("-------------------------- Set REMOTE updated geo. " + params);
            EntityManagerFactory emfGeoposition = Persistence.createEntityManagerFactory("geopositions_remote_admin");
            EntityManager em = emfGeoposition.createEntityManager();
            JSONParser parser = new JSONParser();
            try {
                JSONObject results = (JSONObject) parser.parse(params);
                JSONArray updated = (JSONArray) results.get("updated");
                for (Object el : updated) {
                    JSONObject location = (JSONObject) ((JSONObject) el).get("item");
                    Geoposition geoExist = em.find(Geoposition.class, location.get("id"));
                    if (geoExist != null) {
                        geopositionRepository.updateGeoposition(new Geoposition((Long) location.get("id"), (Double) location.get("longitude"), (Double) location.get("latitude"), (String) location.get("gbif")));
                    } else {
                        geopositionRepository.addGeoposition(new Geoposition((Long) location.get("id"), (Double) location.get("longitude"), (Double) location.get("latitude"), (String) location.get("gbif")));
                    }
                }
                //refresh list geopositions and so on in main controller
                plantsServlet.refreshPrivateVariables();
                return true;
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }*/
        }
/*        logger.info("-------------------------- Set REMOTE updated geo = NULL!!!");*/
        return false;
    }

    /**
     * get list updated plants
     *
     * @return
     */
/*    @GetMapping("get_updated_plants")
    public List<Plant> getUpdatedPlants() {
        logger.info("-------------------------- Get REMOTE updated plants. ");
        List<Plant> updated = plantRepository.getUpdatedPlants();
        logger.info("-------------------------- Size: " + updated.size());
        return updated;
    }*/

    /**
     * receive JSON with plants to update
     *
     * @param @param params = JSON obj
     * @return OK = true/false
     */
   /* @PostMapping(value = "set_updated_plants", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})*/
    public boolean setUpdatedPlants(String params) {
  /*      if (params != null) {
            logger.info("-------------------------- Set LOCAL updated plants. " + params);
            JSONParser parser = new JSONParser();
            try {
                JSONObject results = (JSONObject) parser.parse(params);
                JSONArray updated = (JSONArray) results.get("updated");
                for (Object el : updated) {
                    JSONObject location = (JSONObject) ((JSONObject) el).get("item");
                    plantRepository.updatePlant(new Plant(
                            (String) location.get("id_gbif"),
                            (String) location.get("common_names"),
                            (String) location.get("scientific_name_family"),
                            (String) location.get("scientific_name_authorship"),
                            (String) location.get("scientific_name"),
                            (String) location.get("web_reference_wiki"),
                            (String) location.get("kind"),
                            Integer.parseInt("" + location.get("show_only_flowering")),
                            0, 0));
                }
                //refresh list geopositions and so on in main controller
                plantsServlet.refreshPrivateVariables();
                return true;
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }
        logger.info("-------------------------- Set LOCAL updated plants = NULL!!!");*/
        return false;
    }

    /**
     * get list updated synonyms
     *
     * @return
     */
  /*  @GetMapping("get_updated_synonyms")
    public List<JSONObject> getUpdatedSynonyms() {
        logger.info("-------------------------- Get LOCAL updated synonyms.");
        List<PlantsSynonym> updated = synonymRepository.getUpdatedSynonyms();
        //because of plant we need to transform:
        List<JSONObject> updatedJson = updated.stream()
                .map(a -> {
                    JSONObject synonymJson = new JSONObject();
                    synonymJson.put("id", a.getId());
                    synonymJson.put("id_gbif", a.getPlant().getId_gbif());
                    synonymJson.put("lang", a.getLang());
                    synonymJson.put("lang_name", a.getLang_name());
                    synonymJson.put("web_reference_wiki", a.getWeb_reference_wiki());
                    return synonymJson;
                })
                .collect(Collectors.toList());
        logger.info("-------------------------- Size: " + updated.size());
        return updatedJson;
    }*/

    /**
     * receive JSON with synonyms to update
     *
     * @param @param params = JSON obj
     * @return OK = true/false
     */
/*    @PostMapping(value = "set_updated_synonyms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public boolean setUpdatedSynonyms(@RequestBody String params) {
        if (params != null) {
            logger.info("-------------------------- Set REMOTE updated synonyms." + params);
            JSONParser parser = new JSONParser();
            try {
                JSONObject results = (JSONObject) parser.parse(params);
                JSONArray updated = (JSONArray) results.get("updated");
                for (Object el : updated) {
                    JSONObject location = (JSONObject) ((JSONObject) el).get("item");
                    synonymRepository.updateSynonym(new PlantsSynonym(
                            Long.parseLong("" + location.get("id")),
                            (String) location.get("lang"),
                            (String) location.get("lang_name"),
                            (String) location.get("web_reference_wiki"),
                            (String) location.get("id_gbif")));
                }
                //refresh list geopositions and so on in main controller
                plantsServlet.refreshPrivateVariables();
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        logger.info("-------------------------- Set REMOTE updated synonyms = NULL!!!");
        return false;
    }*/

    /**
     * get list updated events
     *
     * @return
     */
/*
    @GetMapping("get_updated_events")
    public List<JSONObject> getUpdatedEvents() {
        logger.info("-------------------------- Get LOCAL updated events. ");
        List<PlantsEvent> updated = eventRepository.getUpdatedEvents();
        //because of plant we need to transform:
        List<JSONObject> updatedJson = updated.stream()
                .map(a -> {
                    JSONObject eventsJson = new JSONObject();
                    eventsJson.put("id", a.getId());
                    eventsJson.put("id_gbif", a.getPlant().getId_gbif());
                    eventsJson.put("date_from", a.getDate_from());
                    eventsJson.put("month_from", a.getMonth_from());
                    eventsJson.put("date_to", a.getDate_to());
                    eventsJson.put("month_to", a.getMonth_to());
                    eventsJson.put("event", a.getEvent());
                    return eventsJson;
                })
                .collect(Collectors.toList());
        logger.info("-------------------------- Size: " + updated.size());
        return updatedJson;
    }
*/

    /**
     * receive JSON with events to update
     *
     * @param @param params = JSON obj
     * @return OK = true/false
     */
/*    @PostMapping(value = "set_updated_events", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public boolean setUpdatedEvents(@RequestBody String params) {
        if (params != null) {
            logger.info("-------------------------- Set REMOTE updated events. " + params);
            JSONParser parser = new JSONParser();
            try {
                JSONObject results = (JSONObject) parser.parse(params);
                JSONArray updated = (JSONArray) results.get("updated");
                for (Object el : updated) {
                    JSONObject location = (JSONObject) ((JSONObject) el).get("item");
                    PlantsEvent event = new PlantsEvent(
                            Integer.parseInt("" + location.get("id")),
                            (String) location.get("event"),
                            Integer.parseInt("" + location.get("date_from")),
                            Integer.parseInt("" + location.get("month_from")),
                            Integer.parseInt("" + location.get("date_to")),
                            Integer.parseInt("" + location.get("month_to")),
                            (String) location.get("id_gbif"));
                    eventRepository.updateEvent(event);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        logger.info("-------------------------- Set REMOTE updated events = NULL!!!");
        return false;
    }*/
}
