package com.plplustest.test;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.sqlite.SQLiteDataSource;




public class DbController {
	
	private SQLiteDataSource dataSource= null;
	private List<TableEntity> TablesList = null;
	private String dataBaseName = null;
	private Boolean isValid = false;

	// Get if the DbController was created with a valid database.
	// Must contain at least 1 table with at least 2 column( "age" + another)
	// Must be a SQLITE database.
	// For further extensibility ,  have to check for  the database type 
	// and get the good drive before creating datasource. 
	// Additionally sql request should be adaptative.
	public Boolean getisValid() {
		return isValid;
	}

	// Test Constructor not used.
	public DbController() {
		SQLiteDataSource datasourc = new SQLiteDataSource();
		datasourc.setUrl("jdbc:sqlite:db/us-census.db");
		setDataSource(datasourc);
		setDataBaseName("us-census.db");
		setTablesList();
		checkValidity(getDataSource(), getTablesList());
		System.out.println(isValid);
	}
	
	// Construct a DbController with the associated database name. 
	public DbController(String databaseNam) {
		SQLiteDataSource datasourc = new SQLiteDataSource();
		System.out.println("new dBcontroller for "+ databaseNam);
		datasourc.setUrl("jdbc:sqlite:db/"+databaseNam);
		setDataSource(datasourc);
		setDataBaseName(databaseNam);
		setTablesList();
		checkValidity(getDataSource(), getTablesList());
	}
	
	
	// Set validity of this DbController
	public void checkValidity(DataSource ds, List<TableEntity> tl) {
		if( ds != null && tl != null && tl.isEmpty() != true) {
			this.isValid = true;
		}
		else {
			this.isValid = false;
		}
	}
	
	// Get name of the database file associated with this DbController
	public String getDataBaseName() {
		return dataBaseName;
	}


	// Set name of the database file associated with this DbController
	public void setDataBaseName(String dbname) {
		if(this.dataSource != null)
			this.dataBaseName = dbname;
	}
	
	
	// Retrieve a connection to the MySQLI datasource
	public Connection connect(DataSource dataSrc) throws SQLException {

		 System.out.println("Connect");
		Connection conn = dataSrc.getConnection();
		 System.out.println("Connect SUCCEED");
		return conn;
	 }
	 
	// Get info About the Column selected in a specific valid Table of this Database.
	 public List<ResultUnit> selectCol(String colname, String tableName) {
		 ResultSet result = null;
		 System.out.println("SELECTCOL");
		 for(TableEntity table : TablesList) {
				if(table.getTableName().equals(tableName)) {
					if(table.getColumnArray().containsValue(colname)) {
						 List<ResultUnit> results = new ArrayList<ResultUnit>();
						 String sql = "SELECT `" +colname+"` as colname , count(*) AS counter, avg(age) AS average FROM census_learn_sql GROUP BY colname ORDER BY colname*1 , colname DESC LIMIT 100";
						 try {
								 Connection conn = this.connect(getDataSource());
								 System.out.println("PREPARE STATEMENT");
					             PreparedStatement stmt = conn.prepareStatement(sql);
								 System.out.println("EXECUTE STATEMENT");
								 result = stmt.executeQuery();
								 while(result.next()) {
									 ResultUnit unit = new ResultUnit();
									// System.out.println(result.getString(1) + " = " + result.getInt(2) + " / " + result.getFloat(3));
									 unit.setValue(result.getString(1));
									 unit.setCount(result.getInt(2));
									 unit.setAverage(result.getFloat(3));
									 results.add(unit);
								 }
								 conn.close();
					        } catch (SQLException e) {
					            System.out.println(e.getMessage());
					        }catch (NullPointerException e) {
					            System.out.println(e.getMessage());
					        }
						 return results;
					}
				}
			}
		 
		return null;
		
	 }
	 
	 // Retrieve the list of Column present in the Table passed in parameter  ( "age" is ommited )
	 // Check if age column is present if not the Table wont be Valid
	 public void setCollist(TableEntity table) {

		 System.out.println("SETCOLLIST");
		 ResultSet result = null;
		 String sql = "PRAGMA table_info("+table.getTableName()+")";
		 Map<Integer,String> array = new HashMap<Integer,String>();
		 try {
			 Connection conn = this.connect(getDataSource());
             PreparedStatement stmt = conn.prepareStatement(sql);
			 result = stmt.executeQuery();
			 Integer i = 0;
			 
			 while(result.next()) {
				 System.out.println(i + " = *" + result.getString(2) +"* type = "+result.getString(3)  );
				 if(result.getString(2).equals("age") == false) {
					array.put(i, result.getString(2));
				 	i++;
				 }
				 else if(result.getString(3).equals("int") == true) {
					 table.setAgevalid(true);
				 }
			 }
			 conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
		 if(table.getAgevalid().equals(true))
			 table.setColumnArray(array);
	 }

	 // Get the Array of the Column present in the Table ( "age" is ommited )
	 public  Map<Integer,String> getColList(TableEntity table) {
		return table.getColumnArray();
	 }

	 // Get List of the valid Table's present in this database.
	public List<TableEntity> getTablesList() {
		return TablesList;
	}


	// Set the List of the Valid Table's present in this Database + Start column indexing for validity check
	public void setTablesList() {
		ResultSet result = null;
		List<TableEntity> list = new ArrayList<TableEntity>();
		try {
			System.out.println("SetTableList");
			 String sql = "SELECT * FROM sqlite_master WHERE type='table'";
			 Connection conn = this.connect(getDataSource());
			 Statement stmt = conn.createStatement();
			 result =  stmt.executeQuery(sql);
			 while(result.next()) {
				 TableEntity tableActual = new TableEntity();
				// System.out.println(result.getInt(1) + " = " + result.getString(2) );
				 tableActual.setTableName(result.getString(2));
				 setCollist(tableActual);
				 if(tableActual.getAgevalid() == true && tableActual.getColumnArray() != null && tableActual.getColumnArray().isEmpty() == false)
					 list.add(tableActual);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		this.TablesList = list ;
	}

	// Get the TableEntity  associated with the table name passed in parameter.
	public TableEntity getTableByName(String tableName) {
		for(TableEntity table : TablesList) {
			if(table.getTableName().equals(tableName)) {
				return table;
			}
		}
		return null;
	}
	
	public SQLiteDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(SQLiteDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setTablesList(List<TableEntity> tablesList) {
		TablesList = tablesList;
	}

}
