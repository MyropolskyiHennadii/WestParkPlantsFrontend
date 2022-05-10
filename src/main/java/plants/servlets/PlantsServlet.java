package plants.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import plants.model.Geoposition;
import plants.model.Plant;
import plants.repository.EventRepository;
import plants.repository.GeopositionRepository;
import plants.repository.PlantRepository;
import plants.utils.CommonConstants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//@CrossOrigin(origins = "*")
//after dockerizing frontend:
/*@CrossOrigin(origins = "http://94.130.181.51:8094")
@RestController
@RequestMapping("apiWestpark/")*/
public class PlantsServlet extends HttpServlet {

    private static final Properties properties = CommonConstants.getInstance().getCommonProperty();

    private static final Logger LOGGER = LogManager.getLogger(PlantsServlet.class);
    private GeopositionRepository geopositionRepository;
    private PlantRepository plantRepository;
    private EventRepository eventRepository;

    //session's fields, they fill when service start
    private List<Geoposition> geopositions;//all geoposition
    private Double[] longlatRectangle;//main rectangle for map
    private List<Plant> plants;//different plants from geopositions


    @Override
    public void init() {
        LOGGER.info("Servlet's initialisation {}", getServletContext().getClass().getName());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        setAccessControlHeaders(response);

        LOGGER.debug("doPost");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        sendErrorToClient(response, "Method not allowed", 405);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        setAccessControlHeaders(response);
    }

    /**
     * set null to above session private variables (we need it after exchange, for example)
     */
    public void refreshPrivateVariables() {
        geopositions = null;
        longlatRectangle = null;
        plants = null;
    }

    /*    @GetMapping("geopositions")*/
    public List<Geoposition> getNotDeletedGeopositions() {
        if (this.geopositions == null) {
            this.geopositions = geopositionRepository.getNotDeletedGeopositionsWithPlantsID();
        }
        return this.geopositions;
    }

    /*    @GetMapping("longlatRectangle")*/
    public Double[] getLongLatRectangle() {
        if (this.longlatRectangle == null) {
            this.longlatRectangle = geopositionRepository.getLongLatRectangle();
        }
        return this.longlatRectangle;
    }

    /*    @GetMapping("plantsList")*/
    public List<Plant> getDifferentPlants() {
        if (this.plants == null) {
            this.plants = this.geopositionRepository.getDifferentPlants();
        }
        return this.plants;
    }

    /*    @GetMapping("flowering")*/
    public List<String> getFlowering() {
        LocalDate today = LocalDate.now();
        String event = "flowering";
        return this.eventRepository.getEventsByDate(today, event);
    }

    /**
     * return list of relative paths to photos for frontend
     *
     * @param params = id_gbif of plant
     * @return
     */
    /*    @PostMapping(value = "photos", headers = {"Content-type=application/json"})*/
    public List<String> getPhotoForPlant(@RequestBody String params) {
        List<String> listImagesPath = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(params);
            JSONObject jsonParameter = (JSONObject) jsonObject.get("params");
            String id_gbif = (String) jsonParameter.get("id_gbif");
            listImagesPath = plantRepository.getPictureByPlantsID(id_gbif);
        } catch (ParseException e) {
            LOGGER.error("Can't parse json query {}", e.getMessage());
        }
        return listImagesPath;
    }

    /**
     * sends error to client and logs it
     *
     * @param response
     * @param message
     */
    public static void sendErrorToClient(HttpServletResponse response, String message, int returnCode) {
        LOGGER.error(message);
        response.setStatus(returnCode);
        LOGGER.info("Status {} was sent to client.", returnCode);
    }

    /**
     * sends return data and OK to client
     *
     * @param response
     * @param returnData
     */
    public static void sendOkToClient(HttpServletResponse response, JSONArray returnData) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print(returnData.toString());
            out.flush();
        } catch (IOException e) {
            LOGGER.error("Can not write to response: {}", e.getMessage());
        }
        response.setStatus(HttpServletResponse.SC_OK);
        LOGGER.info("Status OK was sent to client");
    }

    /**
     * sets headers for servlet's response
     *
     * @param resp
     */
    public static void setAccessControlHeaders(HttpServletResponse resp) {

        resp.setHeader("Access-Control-Allow-Origin", properties.getProperty("Access.Control.Allow.Origin"));
        resp.setHeader("Access-Control-Allow-Methods", properties.getProperty("Access.Control.Allow.Methods"));
        resp.setHeader("Access-Control-Allow-Headers", properties.getProperty("Access.Control.Allow.Headers"));
        /*resp.setHeader("Access-Control-Allow-Credentials", "true");*/
        /*resp.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");*/
        /*resp.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");*/
    }


    /**
     * gets data from request
     *
     * @param req
     * @return
     */
    private String getRequestData(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        req.setCharacterEncoding("UTF-8");
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }
}
