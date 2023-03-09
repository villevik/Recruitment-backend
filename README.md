
# Recruitment application - backend

This is a java spring application used as a backend for the recruitment application

## How to run application on localhost
* Clone repository
* Connect to your database in application.properties file
* Add the URL of your frontend to the list of allowed origins in WebSecurityConfig.java
* Run Main.java

## Deployment
This app is deployed to Azure
* Create app service on Azure
* Update the value of publish-profile in the workflows file with your own publisher-profile key

### Additional information
* Make sure that you run java 16 both on localhost and Azure's app service
