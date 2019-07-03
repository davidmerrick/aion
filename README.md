[![CircleCI](https://circleci.com/gh/davidmerrick/aion/tree/master.svg?style=svg)](https://circleci.com/gh/davidmerrick/aion/tree/master)

[Aion](https://en.wikipedia.org/wiki/Aion_(deity)) is a Hellenistic deity associated with time.

Also an iCal proxy and filtering service.

[Swagger docs](https://petstore.swagger.io/?url=https://raw.githubusercontent.com/davidmerrick/aion/master/openapi.yaml)

# The problem

Ever subscribe to an iCal calendar (Meetup, Facebook, etc) that has some events that aren't in your area? Or just a subset that you aren't interested in?

Aion allows you to proxy iCal calendars and filter out the events you aren't interested in. It also allows filtering by location.

Less noise, more signal. Making the world a better place™.

![](img/gavin.png)

# Functionality

v1 is just going to support filtering by event title and location. More fields will be added later.

# CircleCI

This project has a relatively more complex CircleCI config, as it needs to run 
a containerized dynamo local for testing.

CircleCI has a local tool to help validate your config here: https://circleci.com/docs/2.0/local-cli/

# Stack
- AWS Lambda
- [Spark](http://sparkjava.com/) web framework
- Kotlin
- Google Geocoder API
- [Serverless Framework](https://serverless.com/) for deployment

# Reference

- Google Maps Geocoding API: https://developers.google.com/maps/documentation/geocoding/intro
- Google Maps Geocoding SDK: https://github.com/googlemaps/google-maps-services-java

# Tools
- [Swagger Editor](https://editor.swagger.io/)
