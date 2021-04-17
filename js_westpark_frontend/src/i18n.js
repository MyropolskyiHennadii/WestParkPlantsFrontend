import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

import Backend from 'i18next-http-backend';
import LanguageDetector from 'i18next-browser-languagedetector';
// don't want to use this?
// have a look at the Quick start guide 
// for passing in lng and translations on init

i18n
    // load translation using http -> see /public/locales (i.e. https://github.com/i18next/react-i18next/tree/master/example/react/public/locales)
    // learn more: https://github.com/i18next/i18next-http-backend
    .use(Backend)
    // detect user language
    // learn more: https://github.com/i18next/i18next-browser-languageDetector
    .use(LanguageDetector)
    // pass the i18n instance to react-i18next.
    .use(initReactI18next)
    // init i18next
    // for all options read: https://www.i18next.com/overview/configuration-options
    .init({

        /*     interpolation: {
              escapeValue: false, // not needed for react as it escapes by default
            }, */
        fallbackLng: 'en',
        debug: true,

        //we init with resources   
        resources: {

            ru: {
                translations: {
                    title: "Растения Вестпарка (Мюнхен)",
                    Summary: "Растения Вестпарка (Мюнхен). Сводка",
                    MappedSpecies: "Число обозначенных на карте видов: ",
                    MostCommon: "Часто встречающиеся виды (светло-зеленый маркер): ",
                    MostRare: "Редко встречающиеся виды (темно-зеленый маркер): ",
                    Flowering: "Сейчас цветут (красный/желтый маркер): ",
                    Filters: "Фильтры: ",
                    FiltersWithoutCommon: "Показать без наиболее часто встречающихся",
                    FiltersOnlyRarest: "Показать наиболее редкие",
                    FiltersOnlyOne: "Показать только один вид из списка",
                    FiltersByFamily: "Показать только одно семейство из списка",
                    Footer: "Со всеми вопросами обращайтесь к Миропольскому Геннадию miropolskij@gmail.com"
                }
            },

            
            uk: {
                translations: {
                    title: "Рослини Вестпарку (Мюнхен)",
                    Summary: "Рослини Вестпарку (Мюнхен). Зведення",
                    MappedSpecies: "Кількість позначених на мапі видів: ",
                    MostCommon: "Найчастіші види (світло-зелений маркер): ",
                    MostRare: "Рідкісні види (темно-зелений маркер): ",
                    Flowering: "Зараз квітнуть (червоний/жовтий маркер): ",
                    Filters: "Фільтри: ",
                    FiltersWithoutCommon: "Показати без найбільш вживаних",
                    FiltersOnlyRarest: "Показати найбільш рідкісні",
                    FiltersOnlyOne: "Показати тільки один вид зі списку",
                    FiltersByFamily: "Показати тільки одну родину зі списку",
                    Footer: "З усіма питаннями звертайтесь до Миропольського Геннадія miropolskij@gmail.com"
                }
            },

            de: {
                translations: {
                    title: "Die Pflanzen des Westpark (München)",
                    Summary: "Die Pflanzen des Westpark (München). Übersicht",
                    MappedSpecies: "Zahl den auf der Karte bezeichnete Arten: ",
                    MostCommon: "Übliche Arten (grüner marker): ",
                    MostRare: "Seltene Arten (dunkel-grüner marker): ",
                    Flowering: "In Blüte stehen (roter/gelber marker): ",
                    Filters: "Filter: ",
                    FiltersWithoutCommon: "Zeigen ohne übliche",
                    FiltersOnlyRarest: "Zeigen nur seltene",
                    FiltersOnlyOne: "Zeigen nur eine Art von der Liste",
                    FiltersByFamily: "Zeigen nur eine Familie von der Liste",
                    Footer: "Wenden Sie mit allen Fragen an zu Myropolskyi Hennadii miropolskij@gmail.com"
                }
            },

            en: {
                translations: {
                    title: "Westpark's Plants (Munich)",
                    Summary: "Westpark's Plants (Munich). Summary",
                    MappedSpecies: "Number of mapped species: ",
                    MostCommon: "Most common species (green marker): ",
                    MostRare: "Most rare species (dark-green marker): ",
                    Flowering: "Flowering now (red/yellow marker): ",
                    Filters: "Filters: ",
                    FiltersWithoutCommon: "Show without most common",
                    FiltersOnlyRarest: "Show only rarest",
                    FiltersOnlyOne: "Select only one species from all the list",
                    FiltersByFamily: "Select only one family from list",
                    Footer: "With all questions mail to Myropolskyi Hennadii miropolskij@gmail.com"
                }
            }

        },

        ns: ["translations"],
        defaultNS: ["translations"],

        react: {
            useSuspense: false,
            wait: true
        }
    });


export default i18n;