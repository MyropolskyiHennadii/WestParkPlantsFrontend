package plants.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CommonConstants {

    private static volatile CommonConstants instance;
    private static boolean flag = true;
    private final Properties commonProperty = new Properties();//main properties

    private static final Logger LOGGER = LogManager.getLogger(CommonConstants.class);

    public Properties getCommonProperty() {
        return commonProperty;
    }

    private void loadPropertiesFile(String filePath) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream input = loader.getResourceAsStream(filePath)) {
            if (input != null) {
                commonProperty.load(input);
            } else {
                LOGGER.error("Null instead input stream!");
                throw new FileNotFoundException("property file 'config.properties' isn't found in the classpath");
            }
        } catch (IOException e) {
            LOGGER.error("Can't load property from path {}: {}", filePath, e.getMessage());
        }
    }

    private CommonConstants() {
        // to prevent instantiating by Reflection call
        if (flag) {
            loadPropertiesFile("application.properties");
            flag = false;
        } else {
            throw new IllegalStateException("CommonConstants already initialized.");
        }
    }

    /**
     * Public accessor.
     *
     * @return an instance of the class.
     */
    public static CommonConstants getInstance() {
        // local variable increases performance by 25 percent
        // Joshua Bloch "Effective Java, Second Edition", p. 283-284

        //var result = instance;
        //in Java 8
        CommonConstants result = instance;

        // Check if singleton instance is initialized.
        // If it is initialized then we can return the instance.
        if (result == null) {
            // It is not initialized but we cannot be sure because some other thread might have
            // initialized it in the meanwhile.
            // So to make sure we need to lock on an object to get mutual exclusion.
            synchronized (CommonConstants.class) {
                // Again assign the instance to local variable to check if it was initialized by some
                // other thread while current thread was blocked to enter the locked zone.
                // If it was initialized then we can return the previously created instance
                // just like the previous null check.
                result = instance;
                if (result == null) {
                    // The instance is still not initialized so we can safely
                    // (no other thread can enter this zone)
                    // create an instance and make it our singleton instance.
                    instance = result = new CommonConstants();
                }
            }
        }
        return result;
    }
}
