 
//path to cross origin (backend)
const pathToCrossOrigin = 'http://94.130.181.51:8095/apiWestpark/';
//'http://94.130.181.51:8094/apiWestpark/';
//'http://94.130.181.51:8095/apiWestpark/'
//'http://localhost:8080/apiWestpark/';

 
class CommonWestParkConstants {

    getPathToCrossOrigin(params) {
        return pathToCrossOrigin;
    }

}

export default new CommonWestParkConstants();