import React from 'react';
import ReactDOM from 'react-dom';
import i18n from "i18next";
import GeomarkersService from '../services/GeomarkersService';
import RemotePhotosService from '../services/RemotePhotosService';
import AsideInfoComponent from './AsideInfoComponent';
import AsidePhotoComponent from './AsidePhotoComponent';
import SynonymsAndLanguages from '../services/SynonymsAndLanguages';

/* filters aside*/
class AsideFiltersComponent extends React.Component {

    //something like callback to the parent (MainPane)
    handleChangeGeomarkers = (e) => this.props.handleChangeMarkers(e);

    //change array by filter manipulations
    refreshGeomarkers(coordinatesWithFilter) {
        const geomarkers = GeomarkersService.getMarkersArray(
            this.props.centerLong,
            this.props.centerLat,
            coordinatesWithFilter, this.props.plantsFrequency, this.props.plants,
            this.props.flowering,
            i18n.language);
        this.handleChangeGeomarkers(geomarkers);
        return geomarkers;
    }


    //handle selection in list (filter only for one species)
    handleChangeSelectFromListSpecies() {
        const selectFilter = document.getElementById("selectPlantID");
        const locale = i18n.language;

        document.getElementById("id_common_trees").checked = false;
        //document.getElementById("id_rare_trees").checked = false;
        document.getElementById("selectFamilyID").selectedIndex = 0;

        if (selectFilter.selectedIndex === 0) {//without filter
            this.handleChangeGeomarkers(GeomarkersService.getMarkersArray(
                this.props.centerLong,
                this.props.centerLat,
                this.props.geopositions, this.props.plantsFrequency, this.props.plants,
                this.props.flowering,
                locale))
            //clear info about plant
            ReactDOM.render(
                <React.StrictMode>
                    <AsideInfoComponent feature={null}
                    />
                </React.StrictMode>,
                document.getElementById('infoPlant'))
            //and clear photo
            ReactDOM.render(
                <React.StrictMode>
                    <AsidePhotoComponent feature={null} photos={null}
                    />
                </React.StrictMode>,
                document.getElementById('plantsPhoto')
            );
        } else {//filter by id
            const geopositions = this.props.geopositions;
            const coordinatesWithFilter = geopositions
                .filter(x => x.plant.id_gbif === selectFilter.options[selectFilter.selectedIndex].id);
            const features = this.refreshGeomarkers(coordinatesWithFilter);
            //immideately render info about plant (simple the first element in filter-array)
            let currentFeature = features[0];
            //info
            GeomarkersService.getNameFeatureInLanguage(currentFeature, i18n.language, this.props.plants);
            ReactDOM.render(
                <React.StrictMode>
                    <AsideInfoComponent feature={currentFeature}
                    />
                </React.StrictMode>,
                document.getElementById('infoPlant')
            );
            //and photo
            RemotePhotosService.renderPhoto(currentFeature);
        }
    }

    //handle check-box "Without common species"
    handleChangeWithoutCommon() {
        const geopositions = this.props.geopositions;
        let isChecked = document.getElementById("id_common_trees").checked;
        //document.getElementById("id_rare_trees").checked = false;
        document.getElementById("selectPlantID").selectedIndex = 0;
        document.getElementById("selectFamilyID").selectedIndex = 0;
        //clear info about plant
        ReactDOM.render(
            <React.StrictMode>
                <AsideInfoComponent feature={null}
                />
            </React.StrictMode>,
            document.getElementById('infoPlant'))

        if (isChecked) {
            //filter
            const coordinatesWithFilter = geopositions.filter(
                function (e) { return this.indexOf(e.plant.id_gbif) < 0; },
                this.props.mostCommon
            );
            this.refreshGeomarkers(coordinatesWithFilter);

        } else {
            this.refreshGeomarkers(geopositions);
        }
        //and clear photo
        ReactDOM.render(
            <React.StrictMode>
                <AsidePhotoComponent feature={null} photos={null}
                />
            </React.StrictMode>,
            document.getElementById('plantsPhoto')
        );
    }

    //handle check.box families
    handleChangeSelectFamily() {
        const locale = i18n.language;
        const selectFilter = document.getElementById("selectFamilyID");

        document.getElementById("id_common_trees").checked = false;
        document.getElementById("selectPlantID").selectedIndex = 0;
        //clear info about plant
        ReactDOM.render(
            <React.StrictMode>
                <AsideInfoComponent feature={null}
                />
            </React.StrictMode>,
            document.getElementById('infoPlant'));
        //and clear photo
        ReactDOM.render(
            <React.StrictMode>
                <AsidePhotoComponent feature={null} photos={null}
                />
            </React.StrictMode>,
            document.getElementById('plantsPhoto')
        );
        if (selectFilter.selectedIndex === 0) {//without filter
            this.handleChangeGeomarkers(GeomarkersService.getMarkersArray(
                this.props.centerLong,
                this.props.centerLat,
                this.props.geopositions, this.props.plantsFrequency, this.props.plants,
                this.props.flowering,
                locale))
        } else {//filter by family
            const geopositions = this.props.geopositions;
            const nameFamily = selectFilter.options[selectFilter.selectedIndex].text;
            const arrayPlantFamily = [];
            const plants = this.props.plants;
            for (let i = 0; i < plants.length; i++) {  
                if(plants[i].scientific_name_family == nameFamily){
                    arrayPlantFamily.push(plants[i].id_gbif);
                }
            }
            const coordinatesWithFilter = geopositions
                .filter(x => arrayPlantFamily.includes(x.plant.id_gbif));
            this.handleChangeGeomarkers(GeomarkersService.getMarkersArray(
                this.props.centerLong,
                this.props.centerLat,
                coordinatesWithFilter, this.props.plantsFrequency, this.props.plants,
                this.props.flowering,
                locale));
        }
    }

    //stop this:
    /*     
        //handle check-box only rare trees
        handleChangeRare() {
            const geopositions = this.props.geopositions;
            let isChecked = document.getElementById("id_rare_trees").checked;
            document.getElementById("id_common_trees").checked = false;
            document.getElementById("selectPlantID").selectedIndex = 0;
            //clear info about plant
            ReactDOM.render(
                <React.StrictMode>
                    <AsideInfoComponent feature={null}
                    />
                </React.StrictMode>,
                document.getElementById('infoPlant'));
    
            if (isChecked) {
                //filter
                const coordinatesWithFilter = geopositions.filter(
                    function (e) { return this.indexOf(e.plant.id_gbif) >= 0; },
                    this.props.mostRare
                );
                this.refreshGeomarkers(coordinatesWithFilter);
            } else {
                this.refreshGeomarkers(geopositions);
            }
            //and clear photo
            ReactDOM.render(
                <React.StrictMode>
                    <AsidePhotoComponent feature={null} photos={null}
                    />
                </React.StrictMode>,
                document.getElementById('plantsPhoto');
            );
        } */

    render() {
        const lang = i18n.language;//language
        //options for filter and sorting array of plants:
        const plants = this.props.plants;// all different plants
        let optionsSpecies = [];//select species
        let setFamilies = new Set//select family
        setFamilies.add("--All (without filter)")

        for (let i = 0; i < plants.length; i++) {

            //if flowering
            const flowering = this.props.flowering.includes(plants[i].id_gbif);
            //if it isn't flowering and has plant.show_only_flowering = 1 => omit this plant
            if (!flowering && plants[i].show_only_flowering === 1) {
                continue;
            }
            const nameForUser = SynonymsAndLanguages.getPlantsNameInLanguage(plants[i], lang);
            optionsSpecies.push(<option value={nameForUser} id={plants[i].id_gbif} key={plants[i].id_gbif}>{nameForUser}</option>);

            if(plants[i].scientific_name_family !== null){
                setFamilies.add(plants[i].scientific_name_family);
            }
        }

        optionsSpecies.sort(function (a, b) {
            var nameA = a.props.value.toUpperCase();
            var nameB = b.props.value.toUpperCase();
            if (nameA < nameB) {
                return -1;
            }
            if (nameA > nameB) {
                return 1;
            }
            return 0;
        })

        //family options:
        let optionsFamilies = [];
        for (let item of setFamilies) {
            optionsFamilies.push(<option value={item} id={item} key={item}>{item}</option>);
        }

        return (
            <div>
                <h2>{this.props.t('Filters')}</h2>
                <form>
                    <div>
                        <input type="checkbox" id="id_common_trees" name="filter_common_trees" value="common_trees" onChange={this.handleChangeWithoutCommon.bind(this)}></input>
                        <p>{this.props.t('FiltersWithoutCommon')}</p>
                    </div>
                    <div>
                        <p>{this.props.t('FiltersByFamily')}</p>
                        <select id="selectFamilyID" onChange={this.handleChangeSelectFamily.bind(this)}>
                            {optionsFamilies}
                        </select>

                    </div>
                    {/* 
                    //stop this
                    <div>
                        <input type="checkbox" id="id_rare_trees" name="filter_rare_trees" value="rare_trees" onChange={this.handleChangeRare.bind(this)}></input>
                        <p>{this.props.t('FiltersOnlyRarest')}</p>
                    </div> */}
                    <div>
                        <p>{this.props.t('FiltersOnlyOne')}</p>
                        <select id="selectPlantID" onChange={this.handleChangeSelectFromListSpecies.bind(this)}>
                            {optionsSpecies}
                        </select>
                    </div>
                </form>
            </div>
        )
    }
}

export default AsideFiltersComponent