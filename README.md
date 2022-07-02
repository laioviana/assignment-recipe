# Recipe Assigment

# Running the project

Before running the project make sure the MySQL database is running.
To start the DB execute the `docker compose up` command.

To run the project execute the `mvn spring-boot:run` command.
To run the tests execute the  `mvn test` command.


You can also access the swagger ui in [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)

# API Documentation

## GET /recipe

*List all recipes*

### Parameters

|Name|Type|Description|Default|
|---|---|---|---|
|page|integer|1-based index of the results page|0|
|size|integer|Number of search results per page|10|

### Responses

|Status|Meaning|Description|
|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|recipes not found|

## POST /recipe

*Creates a new recipe*

### Body Parameters

|Name|Type|Description|
|---|---|---|
|title|String|recipe title|
|vegetarian|Boolean|if the recipe is vegetarian|
|servings|Integer|quantity of servings|
|instructions|String|recipe instructions|
|ingredients|List String>|ingredients of the recipe|

### Responses

|Status|Meaning|Description|
|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|wrong input parameters|

## PUT /recipe/{recipeId}

*Update recipe*

### Path/Body Parameters

|Name|Type|Description|Required|
|---|---|---|---|
|recipeId|Long|id for the recipe that will be updated|yes
|title|String|recipe title|no
|vegetarian|Boolean|if the recipe is vegetarian|no
|servings|Integer|quantity of servings|no
|instructions|String|recipe instructions|no
|ingredients|List String>|ingredients of the recipe|no

### Responses

|Status|Meaning|Description|
|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|wrong input parameters|

## DELETE /recipe/{recipeId}

* Delete recipe*

### Path Parameters

|Name|Type|Description|
|---|---|---|
|recipeId|Long|id for the recipe that will be deleted|


### Responses

|Status|Meaning|Description|
|---|---|---|
|402|[No Content](https://tools.ietf.org/html/rfc7231#section-6.5.1)|Recipe deleted|

## GET /recipe/{recipeId}

*Get recipe by ID*

### Path Parameters

|Name|Type|Description|
|---|---|---|
|recipeId|Long|id for the recipe|


### Responses

|Status|Meaning|Description|
|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|recipe not found|

## GET /recipe/filter

*List all recipes by filter*

### Request Parameters

|Name|Type|Description|Default|
|---|---|---|---|
|title|String|recipe title|
|vegetarian|Boolean|if the recipe is vegetarian|
|servings|Integer|quantity of servings|
|instructions|String|recipe instructions|
|ingredient|String|ingredient of the recipe|
|includeIngredient|Boolean|if the filter should include the ingredient|true
|page|integer|1-based index of the results page|0|
|size|integer|Number of search results per page|10|

### Responses

|Status|Meaning|Description|
|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|successful operation|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|wrong input parameters|

