import React from 'react';

const featureStyle = { marginLeft: '0em', backgroundColor: '#EEE8AA'};

/* return info about the plants after mouse-click at the right side*/
class AsideInfoComponent extends React.Component {

    render() {
        if (this.props.feature != null) {
            const feature = this.props.feature;
            return (
                <div style={featureStyle}>
                    <b>ID: </b> <span>{feature.get('id')}</span> <b> gbif: </b><span>{feature.get('gbif')}</span>
                    <div>
                        <b>name: </b><span>{feature.get('name')}</span>
                    </div>
                    <b>description: </b> <span>{feature.get('desc')}</span>
                    <div>
                        <a href={feature.get('wiki')} target="_blank"><b>Plant's wiki-page: {feature.get('name')}</b></a>
                    </div>
                </div>

            )
        } else {
            return (
                <div> </div>
            )
        }
    }
}

export default AsideInfoComponent