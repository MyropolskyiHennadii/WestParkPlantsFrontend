package plantsAPI.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController {


    private static Logger logger = LoggerFactory.getLogger(AppController.class);

    @RequestMapping({"/WestPark"})
    public String loadUI() {
        logger.info("loading UI -----");
        return "forward:/index.html";
    }

}