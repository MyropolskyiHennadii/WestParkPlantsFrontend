package plantsAPI.controllers;

import plantsAPI.markers.Geoposition;
import plantsAPI.markers.Plant;
import plantsAPI.repository.EventRepository;
import plantsAPI.repository.GeopositionRepository;
import plantsAPI.repository.PlantRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//@CrossOrigin(origins = "*")
//after dockerizing frontend:
@CrossOrigin(origins = "http://94.130.181.51:8094")
@RestController
@RequestMapping("apiWestpark/")
public class API_GeopositionController {

    private static final Logger logger = LoggerFactory.getLogger(API_GeopositionController.class);

    @Autowired
    private GeopositionRepository geopositionRepository;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private EventRepository eventRepository;

//session's fields, they fill when service start
    private List<Geoposition> geopositions;//all geoposition
    private Double[] longlatRectangle;//main rectangle for map
    private List<Plant> plants;//different plants from geopositions

    /**
     * set null to above session private variables (we need it after exchange, for example)
     */
    public void refreshPrivateVariables(){
        geopositions = null;
        longlatRectangle = null;
        plants = null;
    }

    @GetMapping("geopositions")
    public List<Geoposition> getNotDeletedGeopositions() {
        if(this.geopositions == null){
            this.geopositions = geopositionRepository.getNotDeletedGeopositionsWithPlantsID();
        }
        return this.geopositions;
    }

    @GetMapping("longlatRectangle")
    public Double[] getLongLatRectangle() {
        if(this.longlatRectangle == null){
            this.longlatRectangle = geopositionRepository.getLongLatRectangle();
        }
        return this.longlatRectangle;
    }

    @GetMapping("plantsList")
    public List<Plant> getDifferentPlants() {
        if(this.plants == null){
            this.plants = this.geopositionRepository.getDifferentPlants();
        }
        return this.plants;
    }

    @GetMapping("flowering")
    public List<String> getFlowering() {
        LocalDate today = LocalDate.now();
        String event = "flowering";
        return this.eventRepository.getEventsByDate(today, event);
    }

    /**
     * return list of relative paths to photos for frontend
     * @param params = id_gbif of plant
     * @return
     */
    @PostMapping(value = "photos", headers = {"Content-type=application/json"})
    public List<String> getPhotoForPlant(@RequestBody String params) {
        List<String> listImagesPath = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(params);
            JSONObject jsonParameter = (JSONObject) jsonObject.get("params");
            String id_gbif = (String) jsonParameter.get("id_gbif");
            listImagesPath = plantRepository.getPictureByPlantsID(id_gbif);
        } catch (ParseException e) {
            logger.error("Can't parse json query {}", e.getMessage());
        }
        return listImagesPath;
    }

}
