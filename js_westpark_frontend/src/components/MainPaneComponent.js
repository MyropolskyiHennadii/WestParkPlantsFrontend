import React from 'react';
import ReactDOM from 'react-dom';
import i18n from "i18next";
import SynonymsAndLanguages from '../services/SynonymsAndLanguages';
import AsideFiltersComponent from './AsideFiltersComponent';
import AsideInfoComponent from './AsideInfoComponent';
import AsidePhotoComponent from './AsidePhotoComponent';
import GeomarkersService from '../services/GeomarkersService';
import MapWrapper from './MapWrapper';
import SelectLanguageComponent from './SelectLanguageComponent';
import Summary from './SummaryComponent';

/* helpful class to be parent for the left and right sides*/
class MainPaneComponent extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            geomarkers: [],
            language: i18n.language
        }
    }

    //change markers
    handleChangeMarkers = (geomarkers) => (this.setState({ geomarkers }));

    //change language
    handleChangeLanguage = (language) => (
        i18n.changeLanguage(language),

        //clear info about plant
        ReactDOM.render(
            <React.StrictMode>
                <AsideInfoComponent feature={null}
                />
            </React.StrictMode>,
            document.getElementById('infoPlant')),

        //and clear photo
        ReactDOM.render(
            <React.StrictMode>
                <AsidePhotoComponent feature={null} photos={null}
                />
            </React.StrictMode>,
            document.getElementById('plantsPhoto')
        ),
        this.rerenderSummaryAndFilter(),

        this.setState({ language })
    );


    //by didmount or changing language: make service map with frequncy data
    //0: mostCommon;
    //1: mostRare;
    //2: mostCommonSpecies;
    //3: mostRareSpecies;
    //4: nowFlowering;
    //5: plantsFrequency;
    //6: setFamilies
    refreshFrequencyData() {

        const plantsFrequency = this.makePlantsFrequency();

        const frequencyData = new Map();

        let mostRareSpecies = "";
        let mostRare = [];
        let mostCommonSpecies = "";
        let mostCommon = [];

        //most rare
        const kMax = 12;
        let i = 0;
        for (let [key, value] of plantsFrequency) {

            const plant = this.props.plants.find(x => x.id_gbif === key);
            mostRare.push(key);
            mostRareSpecies += SynonymsAndLanguages.getPlantsNameInLanguage(plant, i18n.language) + " (" + value
                + ((i === kMax - 1) ? ")." : "); ");
            i++;
            if (i >= kMax) {
                break;
            }
        }

        //most common
        const reversePlantsFrequency = plantsFrequency;
        //sorting the Map in reverse oder
        reversePlantsFrequency[Symbol.iterator] = function* () {
            yield* [...this.entries()].sort((a, b) => b[1] - a[1]);
        }
        let j = 0;
        for (let [key, value] of reversePlantsFrequency) {
            mostCommon.push(key);
            mostCommonSpecies += SynonymsAndLanguages.getPlantsNameInLanguage(this.props.plants.find(x => x.id_gbif === key), i18n.language) + " (" + value
                + ((j === kMax - 1) ? ")." : "); ");
            j++;
            if (j >= kMax) {
                break;
            }
        }

        //flowering:
        let iMax = 20;
        if (this.props.flowering.length < iMax) {
            iMax = this.props.flowering.length;
        }
        let nowFlowering = "";
        for (let index = 0; index < iMax; index++) {

            //if there are "flowering", which are absent in geopositions
            let curPlant = this.props.plants.find(x => x.id_gbif === this.props.flowering[index]);
            if((curPlant=== undefined)
            || (curPlant === null)){
                console.log("Omit flowering plant with gbif = " + this.props.flowering[index]);
                iMax++;
                continue;
            }

            nowFlowering += SynonymsAndLanguages.getPlantsNameInLanguage(this.props.plants.find(x => x.id_gbif === this.props.flowering[index]), i18n.language)
                + ((index === (iMax - 1)) ? "." : "; ");
        }

        frequencyData.set("mostCommon", mostCommon);
        frequencyData.set("mostRare", mostRare);
        frequencyData.set("mostCommonSpecies", mostCommonSpecies);
        frequencyData.set("mostRareSpecies", mostRareSpecies);
        frequencyData.set("nowFlowering", nowFlowering);
        frequencyData.set("plantsFrequency", plantsFrequency);

        return frequencyData;
    }


    //rerenderAfterLanguageChanging
    rerenderSummaryAndFilter() {
        const frequencyData = this.refreshFrequencyData();
        this.renderSummary(frequencyData);
        this.renderAsideFilter(frequencyData);
    }

    //render Filter's side
    renderAsideFilter(frequencyData) {
        ReactDOM.render(
            <React.StrictMode>
                <AsideFiltersComponent plants={this.props.plants}
                    geopositions={this.props.geopositions}
                    mainRectangle={this.props.mainRectangle}
                    mostCommon={frequencyData.get('mostCommon')}
                    mostRare={frequencyData.get('mostRare')}
                    plantsFrequency={frequencyData.get('plantsFrequency')}
                    handleChangeMarkers={this.handleChangeMarkers}
                    flowering={this.props.flowering}
                    t={this.props.t} />
            </React.StrictMode>,
            document.getElementById('filters'))
    }

    //render Summary
    renderSummary(frequencyData) {
        // summary's side
        ReactDOM.render(
            <React.StrictMode>
                <Summary plants={this.props.plants}
                    mostRareSpecies={frequencyData.get('mostRareSpecies')}
                    mostCommonSpecies={frequencyData.get('mostCommonSpecies')}
                    nowFlowering={frequencyData.get('nowFlowering')}
                    plantsFrequency={frequencyData.get('plantsFrequency')}
                    t={this.props.t} />
            </React.StrictMode>,
            document.getElementById('summary'));
    }

    componentDidMount() {

        const frequencyData = this.refreshFrequencyData();

        const plantsFrequency = frequencyData.get('plantsFrequency');

        this.setState({
            geomarkers: GeomarkersService.getMarkersArray((this.props.mainRectangle[0] + this.props.mainRectangle[1]) / 2,
                (this.props.mainRectangle[2] + this.props.mainRectangle[3]) / 2,
                this.props.geopositions, plantsFrequency, this.props.plants,
                this.props.flowering,//event = flowering
                i18n.language)
        });

        //language
        ReactDOM.render(
            <React.StrictMode>
                <SelectLanguageComponent
                    handleChangeLanguage={this.handleChangeLanguage} />
            </React.StrictMode>,
            document.getElementById('select_language'));

        this.renderSummary(frequencyData);
        this.renderAsideFilter(frequencyData);

    }


    //form frequncy og plants on the map
    makePlantsFrequency() {

        const plantsFrequency = new Map();
        this.props.geopositions.forEach(element => {

            const gbif = element.plant.id_gbif;

            //omit not flowering with flag show_only_flowering
            //if flowering
            const flowering = this.props.flowering.includes(gbif);
            const plant = this.props.plants
                .find(obj => { return obj.id_gbif === gbif });
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
        return (<MapWrapper plants={this.props.plants}
            geopositions={this.props.geopositions}
            mainRectangle={this.props.mainRectangle}
            geomarkers={this.state.geomarkers} />)
    }
}


export default MainPaneComponent