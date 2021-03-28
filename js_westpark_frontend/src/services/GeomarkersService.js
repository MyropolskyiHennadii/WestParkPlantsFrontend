//ol
import 'ol/ol.css';
import { fromLonLat } from "ol/proj";
//
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import {
    Circle as CircleStyle,
    Fill,
    Stroke,
    Style
} from 'ol/style';

import SynonymsAndLanguages from '../services/SynonymsAndLanguages';

class GeomarkersService {

    //set colour for feature by frequency
    setStyleForFeature(rateOfPlant, flowering) {
        if (!flowering) {
            let rate = rateOfPlant;
            if (rate > 250) { rate = 250 }
            let green = 0
            let blue = 0
            if (rate <= 125) {
                green = rate
            } else {
                green = 125
            }
            if (rate > 125) {
                blue = 0
            } else {
                blue = rate - 125
            }
            return new Style({
                image: new CircleStyle({
                    radius: 4,
                    // from 50 to 250
                    fill: new Fill({ color: 'rgb(0, ' + (75 + green) + ', ' + (75 + blue) + ')' }),
                    stroke: new Stroke({
                        color: 'white',
                        width: 2,
                    }),
                }),
            })
        } else {
            //#FF9E2C orange - for flowering plants
            return new Style({
                image: new CircleStyle({
                    radius: 4,
                    fill: new Fill({ color: '#FF9E2C' }),
                    stroke: new Stroke({
                        color: 'white',
                        width: 2,
                    }),
                }),
            })
        }
    }

    //we have features, but by selecting new language we have to rename them
    getNameFeatureInLanguage(feature, lang, plants) {
        const plantID = feature.get("gbif");
        console.log("Plant's id = " + plantID)
        const plant = plants
            .find(obj => { return obj.id_gbif === plantID });
        feature.set("name", SynonymsAndLanguages.getPlantsNameInLanguage(plant, lang));
        feature.set("wiki", SynonymsAndLanguages.getWikiPageInLanguage(plant, lang));
    }

    //form actual array of geomarkers. 
    //centerLong: center of map, longitude
    //centerLat: center of map, latitude
    //geopositions: array with plants and geopositions
    //mostCommonPlant: Map with frequency plants (by id_gbif) in geopositions
    //plants: array with complete plants (with all fields)
    //plantsEvents: flowering plants
    //lang: browser's language
    getMarkersArray(centerLong, centerLat, geopositions, plantsFrequency, plants, plantsEvents, lang) {

        //form array of geomarkers
        const geomarkers = [];
        for (let index = 0; index < geopositions.length; index++) {

            const plant = plants
                .find(obj => { return obj.id_gbif === geopositions[index].plant.id_gbif });

            //if flowering
            const flowering = plantsEvents.includes(plant.id_gbif);

            //if it isn't flowering and has plant.show_only_flowering = 1
            if (!flowering && plant.show_only_flowering === 1) {
                continue;
            }

            const geoMarker = new Feature(
                {
                    type: 'geoMarker',
                    geometry: new Point(fromLonLat([geopositions[index].longitude, geopositions[index].latitude])),
                    id: index + "_" + geopositions[index].id,
                    gbif: plant.id_gbif,//id of plant
                    /* name: SynonymsAndLanguages.getPlantsNameInLanguage(plant, lang),
                    wiki: SynonymsAndLanguages.getWikiPageInLanguage(plant, lang), */
                    name: "",
                    wiki: "",
                    desc: plant.scientific_name_authorship + "; " + plant.scientific_name_family + ": common names " + plant.common_names
                }
            );
            //find the plant in mostCommonPlants and get it rate
            const res = plantsFrequency.get(plant.id_gbif);

            geoMarker.setStyle(
                //playing with colour und frequence of plant
                this.setStyleForFeature(res, flowering)
            );
            geomarkers.push(geoMarker);
        }

        return geomarkers;
    }

}

export default new GeomarkersService();