
class SynonymsAndLanguages {

    //get plant's name in browser's (user's) language
    getPlantsNameInLanguage(plant, lang) {
        if((plant === undefined) || (plant === null)){
            return "undefined";
        }
        if (lang === 'en') {
            return plant.scientific_name;
        }
        const varNames = plant.synonyms;
        if (varNames === undefined || varNames.length === 0) {
            return plant.scientific_name;
        } else {
            const name = varNames.find(x => x.lang === lang)
            if (name === undefined) {
                return plant.scientific_name;
            } else {
                return name.lang_name;
            }
        }
    }

    //get wiki-page in browser's (user's) language
    getWikiPageInLanguage(plant, lang) {
        if (lang === 'en') {
            return plant.web_reference_wiki;
        }
        const varNames = plant.synonyms
        if (varNames === undefined || varNames.length === 0) {
            return plant.web_reference_wiki;
        } else {
            const name = varNames.find(x => x.lang === lang)
            if (name === undefined) {
                return plant.web_reference_wiki;
            } else {
                return name.web_reference_wiki;
            }
        }
    }
}

export default new SynonymsAndLanguages();