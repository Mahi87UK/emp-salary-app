# Steps to Setup

  Clone the application - git clone https://github.com/Mahi87UK/emp-salary-app.git


# Build and run the app using maven:

  mvn package
  java -jar target/emp-salary-app.jar
 
  or
  
  ### mvn spring-boot:run
  
  or using Dockerfile
  
  ### docker build -t emp-salary-app .
  ### docker run -d -p 8080:8080 emp-salary-app

  The application will be listening on port 8080 with h2 DB as default.

# Running unit test and IT tests:

  To run test alone,execute mvn test

  To run integration tests alone:

  mvn -Dtest=EmpSalaryControllerITTest test

# Explore Rest APIs

The app defines following CRUD APIs.

## POST /users/upload  - to save employees information from csv

with csv file as request param and sample file should be as below with headers

### id,login,name,salary,startDate
### e0001a,  testa,Harry Pottera,10.0,16-Nov-01

## GET /users - Retrieve employees information

with optional request parameters such as 
### minSalary (should be decimal)
### maxSalary(should be decimal)
### offset(starting row/record - should be non negative number)
### limit (max no of records - should be non negativenumber)
### sortCriteria.sortOrder (asc or desc) & sortCriteria.sortField (id or name or login or salary or startDate)
### filterCriteria.key (id or name or login or salary or startDate) & filterCriteria.value (valid value for the field) & filterCriteria.operation (GREATER_THAN or LESS_THAN or GREATER_THAN_EQUAL or LESS_THAN_EQUAL or NOT_EQUAL or EQUAL or MATCH or MATCH_START or MATCH_END)

## POST /users - Create a record
  with request body containing employee information
### eg: {"id": "id2","name": "name2","login": "login2","salary": 1234.00,"startDate": "2001-11-16"}   

## GET /users/{$id} - get specific employee information

## PUT or PATCH /users/{$id} - update specific employee information
  with request body containing employee information to be updated
### eg: {"id": "id2","name": "name2updated","login": "login2","salary": 1234.00,"startDate": "2001-11-16"}  

## DELETE /users/{$id} - delete specific employee information

## Can be tested using postman or any other rest client.
