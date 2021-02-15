package plantsAPI.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import plantsAPI.markers.Geoposition;
import plantsAPI.repository.GeopositionRepository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("exchangeWestpark/")
//Controller for exchange between local and remote databases
public class ExchangeController {

    private static Logger logger = LoggerFactory.getLogger(ExchangeController.class);

    @Autowired
    private GeopositionRepository geopositionRepository;

    @Autowired
    private API_GeopositionController api_geopositionController;

    @GetMapping("get_deleted_geopositions")
    public List<Geoposition> getDeletedGeopositions() {
        List<Geoposition> deleted = geopositionRepository.getDeletedGeopositions();
        logger.info("-------------------------- Send REMOTE deleted geo. Size: "+ deleted.size());
        return deleted;
    }

    /**
     * receive JSON with id geopositions to delete
     * @param params = JSON obj
     * @return OK = true/false
     */
    @PostMapping(value = "set_deleted_geopositions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public boolean setDeletedGeopositions(@RequestBody String params) {
        logger.info("-------------------------- Receive REMOTE deleted geo. "+params);
        JSONParser parser = new JSONParser();
        try {
            JSONObject results = (JSONObject) parser.parse(params);
            JSONArray deleted = (JSONArray)results.get("deleted_geopositions");
            for(Object el: deleted){
                JSONObject elJson = (JSONObject)el;
                long id = (Long) elJson.get("id");
                geopositionRepository.deleteGeoposition(id);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        //refresh list geopositions and so on in main controller
        api_geopositionController.refreshPrivateVariables();
        return true;
    }

    /**
     * get list updated geopositions
     * @return
     */
    @GetMapping("get_updated_geopositions")
    public List<Geoposition> getUpdatedGeopositions() {
        logger.info("-------------------------- Send REMOTE deleted geo. ");
        List<Geoposition> updated = geopositionRepository.getUpdatedGeopositions();
        logger.info("-------------------------- Size: " + updated.size());
        return updated;
    }

    /**
     * receive JSON with id geopositions to update
     * @param @param params = JSON obj
     * @return OK = true/false
     */
    @PostMapping(value = "set_updated_geopositions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public boolean setUpdatedGeopositions(@RequestBody String params) {
        logger.info("-------------------------- Receive REMOTE updated geo. "+params);
        JSONParser parser = new JSONParser();
        try {
            JSONObject results = (JSONObject) parser.parse(params);
            JSONArray updated = (JSONArray)results.get("updated_geopositions");
            for(Object el: updated){
                JSONObject location = (JSONObject) ((JSONObject)el).get("location");
                geopositionRepository.updateGeoposition(new Geoposition((Long)location.get("id"), (Double)location.get("longitude"), (Double)location.get("latitude"), (String)location.get("gbif")));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        //refresh list geopositions and so on in main controller
        api_geopositionController.refreshPrivateVariables();
        return true;
    }

}
