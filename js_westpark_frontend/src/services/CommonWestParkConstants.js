 
//path to cross origin (Java API)
const pathToCrossOrigin = 'http://94.130.181.51:8095/apiWestpark/';
//const pathToCrossOrigin = 'http://localhost:8080/apiWestpark/';

 
class CommonWestParkConstants {

    getPathToCrossOrigin(params) {
        return pathToCrossOrigin;
    }

}

export default new CommonWestParkConstants();