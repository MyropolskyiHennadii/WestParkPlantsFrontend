import React from 'react';
import RemoteDataService from '../services/RemoteDataService';
import MainPaneComponent from './MainPaneComponent';

class RemoteDataComponent extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            geopositions: [], //all points with plants
            mainRectangle: [],//0: minLongitude; 1: maxLongitude; 2: minLatitude; 3: maxLatitude
            plants: [],//all plants, used in geopositions
            flowering: [],//event = flowering
            isLoadingRectangle: true,
            isLoadingPlants: true,
            isLoadingFlowering: true,
            isLoadingData: true
        }
    }

    componentDidMount() {

        //get plants with coordinates
        RemoteDataService.getGeopositions(null).then(
            (response) => {
                this.setState({ geopositions: response.data });
                this.setState({ isLoadingData: false });
                console.log("Geopositions loaded.")
            }
        );

        //get main rectangle
        RemoteDataService.getLongLatRectangle(null).then(
            (response) => {
                this.setState({ mainRectangle: response.data });
                this.setState({ isLoadingRectangle: false });
                console.log("Rectangle loaded")
            }
        );

        //get plants
        RemoteDataService.getAllPlants(null).then(
            (response) => {
                this.setState({ plants: response.data });
                this.setState({ isLoadingPlants: false });
                console.log("All plants loaded")
            }
        );
        
        //plant's event = flowering
        RemoteDataService.getEventFlowering(null).then(
            (response) => {
                this.setState({ flowering: response.data });
                this.setState({ isLoadingFlowering: false });
                console.log("Flowering loaded")
            }
        );

    }

    render() {
        if (this.state.isLoadingRectangle || this.state.isLoadingData 
            || this.state.isLoadingPlants || this.state.isLoadingFlowering) {
            return (
                <div>
                    <h1> Plant's List is loading</h1>
                </div>)
        } else {
            return (<MainPaneComponent
                mainRectangle={this.state.mainRectangle}
                geopositions={this.state.geopositions}
                plants={this.state.plants} 
                flowering={this.state.flowering}
                t={this.props.t}/>)
        }
    }
}

export default RemoteDataComponent