# Steps to Setup

  Clone the application - git clone https://github.com/Mahi87UK/emp-salary-app.git


# Build and run the app using maven:

  ### mvn package
  ### java -jar target/emp-salary-app.jar
 
  or
  
  ### mvn spring-boot:run
  
  or using Dockerfile
  
  ### docker build -t emp-salary-app .
  ### docker run -d -p 8080:8080 emp-salary-app

  The application will be listening on port 8080 with h2 DB as default.

# Running unit test and IT tests:

  To run test alone,execute mvn test

  To run integration tests alone:

  ### mvn -Dtest=EmpSalaryControllerITTest test

# Explore Rest APIs

openapi: 3.0.1

info:

  title: Employee Salary App
  
  description: Employee Salary Management Service
  
  version: "1.0"
  
servers:

- url: http://localhost:8080

  description: Generated server url
  
paths:

  /users/{id}:
  
    get:
    
      tags:
      
      - emp-salary-controller
      
      operationId: getEmpSalaryInfo
      
      parameters:
      
      - name: id
      
        in: path
        
        required: true
        
        schema:
        
          type: string
          
      responses:
      
        "200":
        
          description: OK
          
          content:
          
            '*/*':
            
              schema:
              
                type: object
                
    put:
    
      tags:
      
      - emp-salary-controller
      
      operationId: updateEmpSalaryInfo
      
      parameters:
      
      - name: id
      
        in: path
        
        required: true
        
        schema:
        
          type: string
          
      requestBody:
      
        content:
        
          application/json:
          
            schema:
            
              $ref: '#/components/schemas/EmpSalary'
              
        required: true
        
      responses:
      
        "200":
        
          description: OK
          
          content:
          
            '*/*':
            
              schema:
              
                $ref: '#/components/schemas/ResponseMessage'
                
    delete:
    
      tags:
      
      - emp-salary-controller
      
      operationId: deleteEmpSalaryInfo
      
      parameters:
      
      - name: id
      
        in: path
        
        required: true
        
        schema:
        
          type: string
          
      responses:
      
        "200":
        
          description: OK
          
          content:
          
            '*/*':
            
              schema:
              
                $ref: '#/components/schemas/ResponseMessage'
                
    patch:
    
      tags:
      
      - emp-salary-controller
      
      operationId: updateEmpSalaryInfo_1
      
      parameters:
      
      - name: id
      
        in: path
        
        required: true
        
        schema:
        
          type: string
          
      requestBody:
      
        content:
        
          application/json:
          
            schema:
            
              $ref: '#/components/schemas/EmpSalary'
              
        required: true
        
      responses:
      
        "200":
        
          description: OK
          
          content:
          
            '*/*':
            
              schema:
              
                $ref: '#/components/schemas/ResponseMessage'
                
  /users:
  
    get:
    
      tags:
      
      - emp-salary-controller
      
      operationId: getAllEmpSalaryInfo
      
      parameters:
      
      - name: recordsCriteria
      
        in: query
        
        required: true
        
        schema:
        
          $ref: '#/components/schemas/RecordsCriteria'
          
      responses:
      
        "200":
        
          description: OK
          
          content:
          
            '*/*':
            
              schema:
              
                $ref: '#/components/schemas/ResponseMessage'
                
    post:
    
      tags:
      
      - emp-salary-controller
      
      operationId: createEmpSalary
      
      requestBody:
      
        content:
        
          application/json:
          
            schema:
            
              $ref: '#/components/schemas/EmpSalary'
              
        required: true
        
      responses:
      
        "200":
        
          description: OK
          
          content:
          
            '*/*':
            
              schema:
              
                $ref: '#/components/schemas/ResponseMessage'
                
  /users/upload:
  
    post:
    
      tags:
      
      - emp-salary-controller
      
      operationId: uploadFile
      
      requestBody:
      
        content:
        
          application/json:
          
            schema:
            
              type: object
              
              properties:
              
                file:
                
                  type: string
                  
                  format: binary
                  
      responses:
      
        "200":
        
          description: OK
          
          content:
          
            '*/*':
            
              schema:
              
                type: object
                
components:

  schemas:
  
    EmpSalary:
    
      required:
      
      - id
      
      - login
      
      - name
      
      - salary
      
      - startDate
      
      type: object
      
      properties:
      
        id:
        
          type: string
          
        login:
        
          type: string
          
        name:
        
          type: string
          
        salary:
        
          minimum: 0
          
          exclusiveMinimum: false
          
          type: number
          
          format: double
          
        startDate:
        
          type: string
          
          format: date
          
    ResponseMessage:
    
      type: object
      
      properties:
      
        results:
        
          type: array
          
          items:
          
            type: object
            
        message:
        
          type: string
          
    FilterCriteria:
    
      required:
      
      - key
      
      - operation
      
      - value
      
      type: object
      
      properties:
      
        key:
        
          type: string
          
          enum:
          
          - id
          
          - name
          
          - login
          
          - salary
          
          - startdate
          
        value:
        
          type: object
          
        operation:
        
          type: string
          
          enum:
          
          - GREATER_THAN
          
          - LESS_THAN
          
          - GREATER_THAN_EQUAL
          
          - LESS_THAN_EQUAL
          
          - NOT_EQUAL
          
          - EQUAL
          
          - MATCH
          
          - MATCH_START
          
          - MATCH_END
          
          - IN
          
          - NOT_IN
          
    RecordsCriteria:
    
      type: object
      
      properties:
      
        minSalary:
        
          minimum: 0
          
          exclusiveMinimum: false
          
          type: number
          
          format: double
          
        maxSalary:
        
          minimum: 0
          
          exclusiveMinimum: false
          
          type: number
          
          format: double
          
        offset:
        
          minimum: 0
          
          type: integer
          
          format: int32
          
        limit:
        
          minimum: 0
          
          type: integer
          
          format: int32
          
        sortCriteria:
        
          $ref: '#/components/schemas/SortCriteria'
          
        filterCriteria:
        
          $ref: '#/components/schemas/FilterCriteria'
          
    SortCriteria:
    
      required:
      
      - sortField
      
      - sortOrder
      
      type: object
      
      properties:
      
        sortField:
        
          type: string
          
          enum:
          
          - id
          
          - name
          
          - login
          
          - salary
          
          - startdate
          
        sortOrder:
        
          type: string
          
          enum:
          
          - asc
          
          - desc


# Testing

Can be tested using swagger ui which can be accessed as /swagger-ui.html 

 ### eg: http://localhost:8080/swagger-ui.html
 
Postman or any rest client can be used as well
