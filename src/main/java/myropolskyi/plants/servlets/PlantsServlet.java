package myropolskyi.plants.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import myropolskyi.plants.model.Geoposition;
import myropolskyi.plants.model.Plant;
import myropolskyi.plants.repository.EventRepository;
import myropolskyi.plants.repository.GeopositionRepository;
import myropolskyi.plants.utils.CommonConstants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@WebServlet(name = "westparkPlants", urlPatterns = "/apiWestparkPlants")
public class PlantsServlet extends HttpServlet {

    private static final Properties properties = CommonConstants.getInstance().getCommonProperty();

    private static final Logger LOGGER = LogManager.getLogger(PlantsServlet.class);

    //data to return, it fills when servlet init
    //JSONArray contains List<Geoposition> geopositions, Double[] longlatRectangle, List<Plant> plants
    private final JSONObject dataToReturn = new JSONObject();
    private List<Geoposition> geopositions;//all geoposition
    private Double[] longlatRectangle;//main rectangle for map
    private List<Plant> plants;//different plants from geopositions


    @Override
    public void init() {
        LOGGER.info("Servlet's initialisation {} is beginning.", getServletContext().getClass().getName());
        try {
            refreshDataForClient();
            LOGGER.info("Servlet's initialisation {} was done successfully.", getServletContext().getClass().getName());
        } catch (JSONException e) {
            LOGGER.warn("Can't initialize data: {}", e.getMessage());
            LOGGER.error("Servlet's initialisation {} was done with errors.", getServletContext().getClass().getName());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("doPost -> Beginning call.");
        setAccessControlHeaders(response);
        try {
            if (dataToReturn == null) {
                LOGGER.info("doPost -> Empty data for client. Filling data...");
                refreshDataForClient();
            }
            //have to add list of flowering plant, because it is dynamic and depends on today's date
            dataToReturn.put("flowering", getFlowering());
            sendOkToClient(response, dataToReturn);
        } catch (JSONException e) {
            LOGGER.error("doPost -> Impossible JSON-data: {}", e.getMessage());
            sendErrorToClient(response, "Intern error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
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
    public void refreshDataForClient() throws JSONException {

        GeopositionRepository geopositionRepository = new GeopositionRepository();
        geopositions = geopositionRepository.getNotDeletedGeopositionsWithPlantsID();
        LOGGER.debug("refreshDataForClient -> get geopositions size {}.", geopositions.size());
        longlatRectangle = geopositionRepository.getLongLatRectangle();
        LOGGER.debug("refreshDataForClient -> get longlatRectangle size {}.", longlatRectangle.length);
        plants = geopositionRepository.getDifferentPlants();
        LOGGER.debug("refreshDataForClient -> get plants size {}.", plants.size());

        //transform to json plants
        JSONArray jsonPlants = new JSONArray();
        plants.stream().forEach(a -> jsonPlants.put(a.composeJsonObject()));
        dataToReturn.put("plants", jsonPlants);
        LOGGER.debug("refreshConstantDataForClient -> plants were added.");
        //transform to json geopositions
        JSONArray jsonGeopositions = new JSONArray();
        geopositions.stream().forEach(a -> jsonGeopositions.put(a.composeJsonObject()));
        dataToReturn.put("geopositions", jsonGeopositions);
        LOGGER.debug("refreshConstantDataForClient -> geopositions were added.");
        //transform to json longlatRectangle
        JSONArray jsonLonglatRectangle = new JSONArray();
        jsonLonglatRectangle.put(longlatRectangle[0]);
        jsonLonglatRectangle.put(longlatRectangle[1]);
        jsonLonglatRectangle.put(longlatRectangle[2]);
        jsonLonglatRectangle.put(longlatRectangle[3]);
        dataToReturn.put("longlatRectangle", jsonLonglatRectangle);
        LOGGER.debug("refreshConstantDataForClient -> longlatRectangle were added.");

    }

    public JSONArray getFlowering() {
        LocalDate today = LocalDate.now();
        String event = "flowering";
        return new EventRepository().getEventsByDate(today, event);
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
    public static void sendOkToClient(HttpServletResponse response, JSONObject returnData) {
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
}
