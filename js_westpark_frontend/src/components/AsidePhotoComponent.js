import React from 'react';
import Image from 'react-bootstrap/Image';
import { Col, Row } from "react-bootstrap";

const imStyle = {width: 85, height: 85};

/* schows 6 plant's photos*/
class AsidePhotoComponent extends React.Component {

    showPhoto(e) {
        //TODO (modal window/portal with big photo)
        console.log("photo N:" + e["i"]);//it works:)
    }

    render() {
        const feature = this.props.feature;
        const photos = this.props.photos;
        if (photos != null && photos.length > 0) {
            let photos1 = [];
            let photos2 = [];
            for (let i = 0; i < this.props.photos.length; i++) {
                if (i <= 2) {
                    //photos1.push(<Col xs={6} md={4}> <Image src={process.env.PUBLIC_URL + "/PreparedPhotosForPlantsDB/" + this.props.photos[i]} style={{ width: 85, height: 85 }} alt={feature.get("id_gbif")} onClick={() => { this.showPhoto({ i }) }} /></Col>)
                    photos1.push(<Col xs={6} md={4}>
                        <a href={process.env.PUBLIC_URL + "/PreparedPhotosForPlantsDB/" + this.props.photos[i] } target="_blank">
                    <Image 
                        src={process.env.PUBLIC_URL + "/PreparedPhotosForPlantsDB/" + this.props.photos[i] } 
                        style={imStyle}      
                        alt={feature.get("id_gbif")}/>
                        </a></Col>)
        
                } else {
                    if (i <= 5) {
                        photos2.push(<Col xs={6} md={4}> 
                        <a href={process.env.PUBLIC_URL + "/PreparedPhotosForPlantsDB/" + this.props.photos[i] } target="_blank">
                        <Image src={process.env.PUBLIC_URL + "/PreparedPhotosForPlantsDB/" + this.props.photos[i]} 
                        style={imStyle} alt={feature.get("id_gbif")} />
                        </a></Col>)
                    }
                }
            }
            return (
                <div>
                    <Row>
                        {photos1}
                    </Row>
                    <p></p>
                    <Row>
                        {photos2}
                    </Row>
                </div>
            )
        } else {
            return (
                <div> </div>
            )
        }
    }
}

export default AsidePhotoComponent