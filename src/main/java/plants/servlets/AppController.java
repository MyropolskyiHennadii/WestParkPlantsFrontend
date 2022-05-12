package plants.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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