import React from 'react';
import ReactDOM from 'react-dom';
import AsideFiltersComponent from './AsideFiltersComponent';
import GeomarkersService from '../services/GeomarkersService';
import MapWrapper from './MapWrapper';

/* helpful class to be parent for the left and right sides*/
class MainPaneComponent extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            geomarkers: [],
            plantsFrequency: new Map(),
            browserLanguage: navigator.language || navigator.userLanguage
        }
    }

    handleChangeMarkers = (geomarkers) => (this.setState({ geomarkers }));

    componentDidMount() {

        const plantsFrequency = this.makePlantsFrequency();
        this.setState({plantsFrequency: plantsFrequency});

        this.setState({geomarkers: GeomarkersService.getMarkersArray((this.props.mainRectangle[0] + this.props.mainRectangle[1]) / 2,
                (this.props.mainRectangle[2] + this.props.mainRectangle[3]) / 2,
                this.props.geopositions, plantsFrequency, this.props.plants, 
                this.props.flowering,//event = flowering
                navigator.language || navigator.userLanguage)});

        //prepare filter's side
        ReactDOM.render(
            <React.StrictMode>
                <AsideFiltersComponent plants={this.props.plants}
                    geopositions={this.props.geopositions}
                    mainRectangle={this.props.mainRectangle}
                    plantsFrequency={plantsFrequency}
                    browserLanguage={this.state.browserLanguage}
                    handleChangeMarkers={this.handleChangeMarkers} 
                    flowering = {this.props.flowering}
                    t={this.props.t}/>
            </React.StrictMode>,
            document.getElementById('filters'))
    }

    makePlantsFrequency() {
        //most common
        const plantsFrequency = new Map();

        this.props.geopositions.forEach(element => {

            const gbif = element.plant.id_gbif;

            //omit not flowering with flag show_only_flowering
            //if flowering
            const flowering = this.props.flowering.includes(gbif);
            const plant = this.props.plants
                .find(obj => {return obj.id_gbif === gbif});
            //if it isn't flowering and has plant.show_only_flowering = 1 => omit this plant
            if (flowering || plant.show_only_flowering === 0) {
                if (plantsFrequency.has(gbif)) {
                    plantsFrequency.set(gbif, 1 + plantsFrequency.get(gbif))
                } else {
                    plantsFrequency.set(gbif, 1)
                }
            } 
        });
        //sorting Map
        plantsFrequency[Symbol.iterator] = function* () {
            yield* [...this.entries()].sort((a, b) => a[1] - b[1]);
        }
        return plantsFrequency;
    }

    render() {
        return <MapWrapper plants={this.props.plants}
            browserLanguage={this.state.browserLanguage}
            geopositions={this.props.geopositions}
            mainRectangle={this.props.mainRectangle}
            geomarkers={this.state.geomarkers} />
    }
}


export default MainPaneComponent