WeatherApp is an app consisting of three microsevices that connect between themselves via kafka brocker and REST API
The app simulates the work of a weather controll station the three microservices are responsible for:
1st microservice: producing data
2nd microservice: managing data, connecting to database, storing data 
3rd microservice: displaying data and all user related contacts
How to start app:
1. Start kafka brocker on default kafka port
2. Start MySQL database , create a connection named "weatherreports" with login creditentials for admin 
    Login:root
    Password:root
3. Start the microservice responsible for managing data (needs to be first in order to create kafka topic)
4. Start the other microsevices
This is a part of WeatherApp responsible for managing data:
ENDPOINTS:
GET "/reports/{date}" -returns a list of weatherreports for the date given in parameter:  parameter {date} contains date that user wants data about
GET "/next"-returns a list of weatherreports chronologicaly later compared to currently displayed data
GET "/previous"-returns a list of weatherreports chronologicaly earlier compared to currently displayed data
