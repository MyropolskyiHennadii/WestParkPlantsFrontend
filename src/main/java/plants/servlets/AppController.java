package plants.servlets;


/*@Controller*/
public class AppController {


/*    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @RequestMapping({"/WestPark"})*/
    public String loadUI() {
/*        logger.info("loading UI -----");*/
        //return "forward:/index.html";
        //after pulling JS-frontend to docker-container:
        return "redirect://94.130.181.51:8094";
    }

}