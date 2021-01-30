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
    handleChangeSelectFromList() {
        const selectFilter = document.getElementById("selectPlantID");
        const locale = i18n.language;

        document.getElementById("id_common_trees").checked = false;
        document.getElementById("id_rare_trees").checked = false;

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
            ReactDOM.render(
                <React.StrictMode>
                    <AsideInfoComponent feature={features[0]}
                    />
                </React.StrictMode>,
                document.getElementById('infoPlant')
            );
            //and photo
            RemotePhotosService.renderPhoto(features[0]);
        }
    }

    //handle check-box "Without common species"
    handleChangeWithoutCommon() {
        const geopositions = this.props.geopositions;
        let isChecked = document.getElementById("id_common_trees").checked;
        document.getElementById("id_rare_trees").checked = false;
        document.getElementById("selectPlantID").selectedIndex = 0;
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
            document.getElementById('plantsPhoto')
        );
    }

    render() {
        const lang = i18n.language;//language
        //options for filter and sorting array of plants:
        const plants = this.props.plants;// all different plants
        let options = [];
        for (let i = 0; i < plants.length; i++) {

            //if flowering
            const flowering = this.props.flowering.includes(plants[i].id_gbif);
            //if it isn't flowering and has plant.show_only_flowering = 1 => omit this plant
            if (!flowering && plants[i].show_only_flowering === 1) {
                continue;
            }
            const nameForUser = SynonymsAndLanguages.getPlantsNameInLanguage(plants[i], lang);
            options.push(<option value={nameForUser} id={plants[i].id_gbif} key={plants[i].id_gbif}>{nameForUser}</option>);
        }
        options.sort(function (a, b) {
            var nameA = a.props.value.toUpperCase();
            var nameB = b.props.value.toUpperCase();
            if (nameA < nameB) {
                return -1;
            }
            if (nameA > nameB) {
                return 1;
            }
            return 0;})  

        return (
            <div>
                <h2>{this.props.t('Filters')}</h2>
                <form>
                    <div>
                        <input type="checkbox" id="id_common_trees" name="filter_common_trees" value="common_trees" onChange={this.handleChangeWithoutCommon.bind(this)}></input>
                        <p>{this.props.t('FiltersWithoutCommon')}</p>
                    </div>
                    <div>
                        <input type="checkbox" id="id_rare_trees" name="filter_rare_trees" value="rare_trees" onChange={this.handleChangeRare.bind(this)}></input>
                        <p>{this.props.t('FiltersOnlyRarest')}</p>
                    </div>
                    <div>
                        <p>{this.props.t('FiltersOnlyOne')}</p>
                        <select id="selectPlantID" onChange={this.handleChangeSelectFromList.bind(this)}>
                            {options}
                        </select>
                    </div>
                </form>
            </div>
        )
    }
}

export default AsideFiltersComponent