# Employee Salary Management Webservice

A demo web service for Employee Salary Management

## Usage

Build
```
./mvnw clean install
```

Run (using Maven plugin)
```
./mvnw spring-boot:run
```

Run (as a packaged application)
```
java -jar target/salary-management-service-1.0.0.jar
```

Test
```
./mvnw test
```

## Design considerations 

Package-by-feature project structure is used here
* Instead of package-by-layer
* Ease of navigation 
* Scalability -- when the project eventually scales up, this project structure makes it much easier to refactor / break this application into separate microservices 

Separate DTO classes for different purposes
* User request-DTO, response-DTO, CSV-DTO, entity-DTO, each have their own class
* Instead of a single one-size-fits-all "UserDto" class
* This allows for cleaner code and finer-grained control over field validations for each purpose, as the annotations and validation logic are not mixed together in a single class   

Spring JPA vs JdbcTemplate
* For simple queries, JPA is quick and convenient
* For more complex queries, JdbcTemplate may be more appropriate, and allows for finer optimisation
  * e.g. We can use a single multi-row INSERT statement with JdbcTemplate, whereas JPA "simulates" this via batch inserts (multiple single-row INSERT statements)
  * Even though JPA's batch insert can be optimised, a single multi-row insert statement via JdbcTemplate is still [faster](https://stackoverflow.com/a/1793209)
* Here, we used a combination of the two to reap the benefits of both
* When using JdbcTemplate, make use of named queries and prepared statements to prevent SQL injection

BigDecimal is used here for monetary values
* Instead of `float` or `double` 
* `float` and `double` have precision issues, and hence are unsuitable for monetary usages

Lombok, OpenCsv
* Well-established libraries are used to reduce boilerplate code and avoid reinventing the wheel

## Future Improvements

* If the upload filesize is large and processing is expected to take a long time, the processing can be made asynchronous
  * controller immediately returns HTTP 200 "upload received", and calls worker node/process to process the file asynchronously
  * provide another API endpoint to check on upload status
* It may be important to keep an audit trail of changes made to employees' salaries
  * separate table can be created to keep a "ledger" of the changes -- each change is always appended as a row to this Ledger table (instead of update) 
* Database versioning libraries (Flyway, Liquibase) can be used to increase maintainability of database schema