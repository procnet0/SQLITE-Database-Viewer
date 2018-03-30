# PlPlusTest


This project is a SQLITE user-database Viewer, with Java in backend and JavaScript in front-end.
In order to work the database need to contain at least one column named 'age' and one other column.
When you select a column name, the application display the Top 100 value in this column
 + the number of user with this value, and the average Age of user with this value.

## Getting Started

To run this locally

 Install Maven.
 Clone this repo  to your file system
 Build with [Maven](https://maven.apache.org/)
 In [STS](https://spring.io/tools)
    On windows go on [your-folder]/src/main/resources/
        * right-clic on sqlite-jdbc-3.21.0.jar
        * ->build-path
        * ->add to build path
 Then Start server as spring boot app.
 You will be able to access the site at http://localhost:8080.

### Running


  Base functionality:
    Select a column to see the associated result appears.

  Todo functionality:

    Upload a conform database to the server.
    Select a database already uploaded on the server.
    Sort result field by Value/ Average age / Count.
    If more then 100 values, Be able to display 100 next values.





## THINGS TO DO:

### -Possibility to upload database to server.
    |-> Possibility to select database to work on.
	-Store list of DBcontroller in ajaxHandler.
	-store linked table list with column list 
	-Check if table contain "age" column other way don't add to valid table.
	-check if db contain at least 1 valid table.
	
### -Better front
    	|-> max size result area + scroll
    	|-> database Stats area (database name
                          + selectable database)


	
	-make Rest call from front with :
		-Database name (check in ajaxHandler)
		-Table name(check in dbcontroller) 
		-Column name (check in dbcontroller)
		-Actual offset if there is.
	
	-send back to front :
		- result list
	 	- max number result
	-
## Built With
  * [Spring](https://spring.io/) - Java Framework
  * [Maven](https://maven.apache.org/) - Dependency Management
  * [JQuery](https://jquery.com/) - JavaScript library
  * [Bootstrap4](https://getbootstrap.com/) - Front-end Component Library

## Authors
  * *Vincent Balart*
