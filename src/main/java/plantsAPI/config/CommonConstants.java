package plantsAPI.config;

//now is useless class
public class CommonConstants {

    private static CommonConstants instance;

    //private String preparedPhotosDirectory = "D:\\Intellij Projects\\WestParkPlants\\js_westpark_frontend\\public\\PreparedPhotosForPlantsDB";

    private CommonConstants() {
    }

    public static synchronized CommonConstants getInstance() {
        if (instance == null) {
            instance = new CommonConstants();
        }
        return instance;
    }

/*    public String getPreparedPhotosDirectory() {
        return preparedPhotosDirectory;
    }

    public void setPreparedPhotosDirectory(String preparedPhotosDirectory) {
        this.preparedPhotosDirectory = preparedPhotosDirectory;
    }*/

}
