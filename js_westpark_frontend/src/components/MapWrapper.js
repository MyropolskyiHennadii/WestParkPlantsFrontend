import React from 'react';
import ReactDOM from 'react-dom';

//ol
import { Map, View } from 'ol';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import 'ol/ol.css';
import { fromLonLat } from "ol/proj";
import VectorSource from 'ol/source/Vector';
import { Vector as VectorLayer } from 'ol/layer';

//right side of the page
import AsideInfoComponent from './AsideInfoComponent';
import AsidePhotoComponent from './AsidePhotoComponent';
import RemotePhotosService from '../services/RemotePhotosService';

/* just the map */
class MapWrapper extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            vectorLayer: new VectorLayer()
        }
    }

    componentDidMount() {

        this.doMapWithMarkers()

    }

    //doing map and markers (layer)
    doMapWithMarkers() {

        let map = new Map({
            target: 'map',
            layers: [
                new TileLayer({
                    source: new OSM()
                }),
                //markers
                this.state.vectorLayer
            ],
            view: new View({
                center: fromLonLat([(this.props.mainRectangle[0] + this.props.mainRectangle[1]) / 2,
                (this.props.mainRectangle[2] + this.props.mainRectangle[3]) / 2]),
                zoom: 16
            })
        });

        //by click on point - show details of plant
        map.on('click', function (evt) {
            const feature = map.forEachFeatureAtPixel(evt.pixel, function (feature) {
                return feature;
            });
            if (feature) {
                //info
                ReactDOM.render(
                    <React.StrictMode>
                        <AsideInfoComponent feature={feature} />
                    </React.StrictMode>,
                    document.getElementById('infoPlant')
                );
                //photos
                RemotePhotosService.renderPhoto(feature);
            } else {
                //info
                ReactDOM.render(
                    <React.StrictMode>
                        <AsideInfoComponent feature={null} />
                    </React.StrictMode>,
                    document.getElementById('infoPlant')
                );
                //photo
                ReactDOM.render(
                    <React.StrictMode>
                        <AsidePhotoComponent feature={null} photos={null}/>
                    </React.StrictMode>,
                    document.getElementById('plantsPhoto')
                );
            }
        });
    }

    render() {
        //have to do this here because of filter's change
        this.state.vectorLayer.setSource(new VectorSource({
            features: this.props.geomarkers
        }));
        const styleMap = {
            width: '90%',
            height: 550,
            border: "double",
            backgroundColor: '#cccccc',
            marginLeft: 'auto',
            marginRight: 'auto'
        }

        return (
            <div id='map' style={styleMap}>
            </div>
        )
    }
}

export default MapWrapper
