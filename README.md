# Plants of Westpark (Munich)

That's frontend of the Westpark's (Munich) map with recognized plants, their descriptions (wiki), photos and geolocations.

One part has been written in Java (SpringBoot), one part in JS.
Java's part is API for plant's database with their geolocations, descriptions. photos ans so on.
JS part is properly frontend (it was built with React, files were built are in /resources/static directory).

Language of the frontend is determined by browser's default language (de, ru, uk or en).
Map: OpenStreetMap.
Markers: Openlayer library for JS.

Database with plant's geolocations, descriptions and so on lie under MySQL and is managed by backend PhotosPlantsGeolocations, https://github.com/MyropolskyiHennadii/PhotosPlantsGeolocations.

I don't know, how long I will pay for VPS, but now you can see the working application here:
http://94.130.181.51:8095/WestPark

