import React from 'react';
import { useTranslation } from 'react-i18next';

export default function Summary(props) {
    const [t, i18n] = useTranslation();
    return (
        <div>
            <h3 style={{ marginLeft: 2 + 'em' }}>{t("Summary")}</h3>
            <p style={{ marginLeft: 2 + 'em' }}>{t('MappedSpecies')} {props.numberOfSpecies}. {t('MostCommon')} {props.mostCommonSpecies} {t('MostRare')} {props.mostRareSpecies} </p>
            <p style={{ marginLeft: 2 + 'em' }}>{t('Flowering')} {props.flowering}</p>
        </div>
    );
 } 
