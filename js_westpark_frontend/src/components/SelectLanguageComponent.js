import React from 'react';
import i18n from "i18next";

/*select language*/
class SelectLanguageComponent extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            langs: []
        }
    }

    //something like callback to the parent (MainPane)
    handleChangeLanguage = (e) => this.props.handleChangeLanguage(e);

    //
    componentDidMount() {
        let options = [];
        let nameDefault = "";
        if(i18n.language === 'de'){
            nameDefault = "Deutsch"
        } else if(i18n.language === 'ru'){
            nameDefault = "Русский"
        } else if(i18n.language === 'uk'){
            nameDefault = "Українська"
        } else {
            nameDefault = "English"
        }
        options.push(<option value={i18n.language} id={i18n.language} key={i18n.language}>{nameDefault}</option>);
        if (i18n.language !== 'en') {
            options.push(<option value="en" id="en" key="en">English</option>);
        }
        if (i18n.language !== 'de') {
            options.push(<option value="de" id="de" key="de">Deutsch</option>);
        }
        if (i18n.language !== 'ru') {
            options.push(<option value="ru" id="ru" key="ru">Русский</option>);
        }
        if (i18n.language !== 'uk') {
            options.push(<option value="uk" id="uk" key="uk">Українська</option>);
        }
        this.setState({ langs: options });
    }

    //handle selection in list (filter only for one species)
    handleChangeSelectFromList() {
        const selectFilter = document.getElementById("selectLanguageID");
        const langID = selectFilter.selectedIndex;
        this.handleChangeLanguage(this.state.langs[langID].key);
    }

    render() {
        return (
            <div>
                <form>
                    <div>
                        <select id="selectLanguageID" onChange={this.handleChangeSelectFromList.bind(this)}>
                            {this.state.langs}
                        </select>
                    </div>
                </form>
            </div>
        )
    }
}

export default SelectLanguageComponent