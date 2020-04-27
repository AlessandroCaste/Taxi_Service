# Taxi Service A

This service provides the first API interface for the 'Get My Taxi' challenge.  
Service A provides the following endpoints:

```
POST /maps/
```
Posts a json map of a city, used to compute routes for taxies.

```
POST /maps/{city}/taxi_positions/{taxiId}
```

Posts the position of a taxi with id taxiId in the map with id mapId.

```
POST {city}/user_requests/
```
Posts a request to compute routes from a starting point to a destination. This instruction is designed to contact service B.  

## Further info

+ Service A builds up a h2 db that stores map information; besides didactical reasons, it provides a persistent repository across runs.  
+ Inputs are validated: maps, taxis and coordinates are checked before submission.  
+ Posting an entity with an already used id erases the previous submission.  
+ Connection to service B happens through a WebClient blocking call. Due to the nature of the application this should suffice, but further updates may implement load balancing/meshing.
