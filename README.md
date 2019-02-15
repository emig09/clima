# clima
- API fails when the metric to get Celsius temperature is added to the URL. That is the reason numbers for temperature are so high.
- At the time of search by name, services are returning just one. Even if I search city names that can be duplicated.
- When app starts, first city displayed is got by lat-long.
- When user adds a city, name and id are stored and then, when returning to the detail screen, that id is used to get detail of weather
for the corresponding city.
- Note ui design is just simple. I preferred to complete all the challenge faster over introduce a better design.
- Note location permission logic is on MainActivity. I considered better to left that there over create an utility class.