# csiro-doi-service
The DOI Service (DOISERV) is a web application that provides an interface to the DOI Minting Service hosted by DataCite. The application provides a REST-API that allows its callers to create and manage Digital Object Identifiers (DOIs).

# Technology Stack
Java Amazon JDK 11.X
Tomcat 9.X
Gradle 6.X
Spring Core 5.X
Spring Web 5.X
Spring Security 5.X
Spring -data-rest-core 5.X
Spring -boot-starter-data-mongodb
Oracle 


# Application Architecture




# API Specification

| Resource | GET | POST | PUT | DELETE |
|-----------|---------|----------|--------|---------|
|/api/v1/dois/	| Return a list of DOIs |	Create a new DOI | Error | Error |
|/api/v1/dois/{doi}	| Return a DOI if exists	|Error	|If DOI exists, update it else ERROR	|If Draft DOI exists, delete it else ERROR |
|/api/v1/dois/{doi}/activate | Error	|Error	|If DOI exists, activate it else ERROR	|Error |
|/api/v1/dois/{doi}/deactivate | Error	|Error	|If DOI exists, deactivate it else ERROR	|Error |

### Get an existing DOI
Endpoint : GET /api/v1/dois/{doi}
Parameters
The parameters defined below are a part of the doiDTO object used by the current DOI client

Parameter	Description	Parameter Type	Data Type	Mandatory
doi	
The doi of the collection or publication. For example 

path	String	Yes
Example : GET /api/v1/dois?doi=10.80413/test_doi_5ee6b8a5e65fb

Response: DOI Metadata XML file


### Get a List of DOIs
Endpoint : GET /api/v1/dois/
Response: TBD


### Create a new DOI
Create a Findable DOI To create a DOI in Findable state with a URL and metadata you need to include all of the required DOI metadata fields (DOI, creators, title, publisher, publicationYear, resourceTypeGeneral)

Endpoint : POST /api/v1/dois/
Parameters
Parameter	Description	Parameter Type	Data Type	Mandatory
url	
The url for the DOI

path	String	Yes
doiDTO	The doi DTO which contains the metadata associated with the DOI	body	DoiDTO	Yes
DoiDTO fields
Field	Description	Data Type	Mandatory
identifier	
A persistent identifier that uniquely dentifies a resource.

This will be used to set the suffix of the DOI.

String	Yes
url	Landing page URL	String	Yes
title	Title of the Digital Object	String	Yes
creators	
The main researchers involved working on the data, or the authors of the publication in priority order. May be a
corporate/institutional or personal name.	List<String>	Yes
publisher	CSIRO	String	Yes
publicationYear	Publication Year	String	Yes
resourceType	Type of resource such as datasets and collections associated workflows, software, models, grey literature	String	Yes
resourceTypeDescription	Resource Type description	String	No
subjects	FOR codes / Keywords	List<String>	No
geoLocations	GeoLocations	List<GeoLocation>	No
Response
201 : Created



### Update a DOI
Endpoint : PUT /api/v1/dois/{doi}
Parameters
Parameter	Description	Parameter Type	Data Type	Mandatory
doi	
The doi of the collection or publication. For example 

path	String	Yes
doiDTO	The doi DTO to be updated	body	DoiDTO	No
Response
200 OK



### Deactivate a DOI
Endpoint : PUT /api/v1/dois/{doi}/deactivate
Parameters
Parameter	Description	Parameter Type	Data Type	Mandatory
doi	
The doi of the collection or publication. For example 

path	String	Yes
Response
200 OK



### Activate a DOI
Endpoint : PUT /api/v1/dois/{doi}/activate
Parameters
Parameter	Description	Parameter Type	Data Type	Mandatory
doi	
The doi of the collection or publication. For example 

path	String	Yes
Response
200 OK



### Delete a DOI
Endpoint : DELETE/api/v1/dois/{doi}
Parameters
The parameters defined below are a part of the doiDTO object used by the current DOI client

Parameter	Description	Parameter Type	Data Type	Mandatory
doi	
The doi of the collection or publication. For example 

path	String	Yes
Response
No response from the service



# Versioning
To keep the service backward compatible, it is recommended to implement versioning.

Initial version will be v1.



# HTTP response status codes
The server should always return the right HTTP status code to the client. (Recommended)

Standard HTTP status codes
200 – OK – Everything is working
201 – OK – New resource has been created
204 – OK – The resource was successfully deleted

304 – Not Modified – The client can use cached data

400 – Bad Request – The request was invalid or cannot be served. The exact error should be explained in the error payload. E.g. „The JSON is not valid“
401 – Unauthorized – The request requires a user authentication
403 – Forbidden – The server understood the request but is refusing it or the access is not allowed.
404 – Not found – There is no resource behind the URI.
422 – Unprocessable Entity – Should be used if the server cannot process the entity, e.g. if an image cannot be formatted or mandatory fields are missing in the payload.

500 – Internal Server Error – API developers should avoid this error. If an error occurs in the global catch blog, the stracktrace should be logged and not returned as in the response.

Use error payloads
All exceptions should be mapped in an error payload. Here is an example how a JSON payload should look like.



# Security
Authentication between the clients will be provided using HTTP Basic


# API Documentation(Swagger)
All the RESTful API must follow Swagger Specification for API documentation: http://swagger.io/specification
# Reference
DataCite Documentation https://support.datacite.org/docs/getting-started

Swagger Specification http://swagger.io/specification

Swagger UI https://swagger.io/swagger-ui/
