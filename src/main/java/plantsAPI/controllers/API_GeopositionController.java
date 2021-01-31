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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("apiWestpark/")
public class API_GeopositionController {

    private static Logger logger = LoggerFactory.getLogger(API_GeopositionController.class);

    @Autowired
    private GeopositionRepository geopositionRepository;

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private EventRepository eventRepository;

//sessions fields, they fill when service start
    private List<Geoposition> geopositions;//all geoposition
    private Double[] longlatRectangle;//main rectangle for map
    private List<Plant> plants;//different plants from geopositions

    @Autowired
    @GetMapping("geopositions")
    public List<Geoposition> getGeopositions() {
        //logger.info("-------------------------- start geo. Size: "+commonConstants.getGeopositions().size());
        //return this.geopositionRepository.getAllGeopositionsWithPlantsID();
        if(this.geopositions == null){
            this.geopositions = geopositionRepository.getAllGeopositionsWithPlantsID();
        }
        return this.geopositions;
    }

    @Autowired
    @GetMapping("longlatRectangle")
    public Double[] getLongLatRectangle() {
        //logger.info("-------------------------- start rectangle");
        //return this.geopositionRepository.getLongLatRectangle();
        if(this.longlatRectangle == null){
            this.longlatRectangle = geopositionRepository.getLongLatRectangle();
        }
        return this.longlatRectangle;
    }

    @Autowired
    @GetMapping("plantsList")
    public List<Plant> getDifferentPlants() {
        //logger.info("-------------------------- start plant size:"+commonConstants.getPlants().size());
        //return this.geopositionRepository.getDifferentPlants();
        if(this.plants == null){
            this.plants = geopositionRepository.getDifferentPlants();
        }
        return this.plants;
    }

    @Autowired
    @GetMapping("flowering")
    public List<String> getFlowering() {
        //logger.info("-------------------------- start flowering");
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
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(params);
            JSONObject jsonParameter = (JSONObject) jsonObject.get("params");
            String id_gbif = (String) jsonParameter.get("id_gbif");
            listImagesPath = plantRepository.getPictureByPlantsID(id_gbif);
        } catch (ParseException e) {
            logger.error("Can't parse json query {}", e.getMessage());
        }
        return listImagesPath;
    }

}
