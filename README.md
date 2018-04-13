# SQLITE-database-Viewer


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
    Select a databse/table/column to see the associated result appears.

  Additionnal functionality:
  	Add a sqlite database to the server. Database's table must have one "age" column plus one other column at least. 


## THINGS TO DO:

	-make Rest call from front with :
		-Actual offset if there is.
		
	Deploy to Heroku.
	
	Implements Unit test.
		
## Built With
  * [Spring](https://spring.io/) - Java Framework
  * [Maven](https://maven.apache.org/) - Dependency Management
  * [JQuery](https://jquery.com/) - JavaScript library
  * [Bootstrap4](https://getbootstrap.com/) - Front-end Component Library

## Authors
  * *Vincent Balart*
