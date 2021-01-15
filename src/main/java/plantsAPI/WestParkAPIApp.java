package plantsAPI;

import plantsAPI.repository.GeopositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WestParkAPIApp {

    @Autowired
    private GeopositionRepository geopositionRepository;

    public static void main(String[] args) {
        SpringApplication.run(WestParkAPIApp.class, args);
    }

}
