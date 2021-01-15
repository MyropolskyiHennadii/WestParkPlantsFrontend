import axios from 'axios';
import CommonWestParkConstants from './CommonWestParkConstants';

class RemoteDataService {

    //main map's rectangle (min, max long and lat)
    getLongLatRectangle(params) {
        const rectangle_URL = CommonWestParkConstants.getPathToCrossOrigin(params) + 'longlatRectangle';
        return axios.get(rectangle_URL);
    }

    //plant's events(now only flowering)
    getEventFlowering(params) {
        const flowering_URL = CommonWestParkConstants.getPathToCrossOrigin(params) + 'flowering';
        return axios.get(flowering_URL);
    }

    //get all geolocations
    getGeopositions(params) {
        const geopositions_URL = CommonWestParkConstants.getPathToCrossOrigin(params) + 'geopositions';
        return axios.get(geopositions_URL);
    }

    //all plants (different)
    getAllPlants(params) {
        const plants_URL = CommonWestParkConstants.getPathToCrossOrigin(params) + 'plantsList';
        return axios.get(plants_URL);
    }

}

export default new RemoteDataService();