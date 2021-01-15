import axios from 'axios';
import React from 'react';
import ReactDOM from 'react-dom';
import CommonWestParkConstants from './CommonWestParkConstants';

import AsidePhotoComponent from '../components/AsidePhotoComponent';


//all photos of plant
class RemotePhotosService {

    async getPhotos(gbif) {
        const photos_URL = CommonWestParkConstants.getPathToCrossOrigin() + 'photos';
        const response = await axios.post(photos_URL, { params: { id_gbif: gbif } });
        return response;
    }

    renderPhoto(feature) {
        this.getPhotos(feature.get('gbif')).then(
            (response) => {

                let images = []
                for (let index = 0; index < response.data.length; index++) {
                    images.push(response.data[index]);
                }
                console.log("Photos loaded: " + feature.get('gbif'));
                ReactDOM.render(
                    <React.StrictMode>
                        <AsidePhotoComponent feature={feature} photos={images} />
                    </React.StrictMode>,
                    document.getElementById('plantsPhoto')
                );
            }
        );
    }
}

export default new RemotePhotosService();