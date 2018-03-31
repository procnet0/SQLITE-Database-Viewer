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

import org.sqlite.SQLiteDataSource;




public class DbController {
	
	
	SQLiteDataSource dataSource= null;
	 List<TableEntity> TablesList = null;
	 String dataBaseName = null;
	


	public DbController() {
		SQLiteDataSource datasourc = new SQLiteDataSource();
		datasourc.setUrl("jdbc:sqlite:db/us-census.db");
		this.dataSource = datasourc;
		setDataBaseName();
		setTablesList();
		
		for(TableEntity table :TablesList) {
			setCollist(table);
		}
	}
	
	// Construct a DbController with the associated database name. 
	
	public DbController(String databaseName) {
		SQLiteDataSource datasourc = new SQLiteDataSource();
		datasourc.setUrl("jdbc:sqlite:db/"+databaseName+".db");
		this.dataSource = datasourc;
		setDataBaseName();
	}

	public String getDataBaseName() {
		return dataBaseName;
	}

	public void setDataBaseName() {
		if(this.dataSource != null)
			this.dataBaseName = this.dataSource.getDatabaseName();
		System.out.println(dataBaseName);
	}
	 public Connection connect() throws SQLException {

		 System.out.println("Connect");
		Connection conn = dataSource.getConnection();
		 System.out.println("Connect SUCCEED");
		return conn;
	 }
	 
	 public List<ResultUnit> selectCol(String colname, String tableName) {
		 ResultSet result = null;
		 System.out.println("SELECTCOL");
		 for(TableEntity table : TablesList) {
				if(table.getTableName().equals(tableName)) {
					if(table.getColumnArray().containsValue(colname)) {
						 List<ResultUnit> results = new ArrayList<ResultUnit>();
						 String sql = "SELECT `" +colname+"` as colname , count(*) AS counter, avg(age) AS average FROM census_learn_sql GROUP BY colname ORDER BY colname*1 , colname DESC LIMIT 100";
						 try {
								 Connection conn = this.connect();
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
	 
	 public void setCollist(TableEntity table) {

		 System.out.println("SETCOLLIST");
		 ResultSet result = null;
		 String sql = "PRAGMA table_info("+table.getTableName()+")";
		 Map<Integer,String> array = new HashMap<Integer,String>();
		 try {
			 Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
			 result = stmt.executeQuery();
			 Integer i = 0;
			 
			 boolean ageValid = false;
			 while(result.next()) {
				 System.out.println(i + " = *" + result.getString(2) +"* type = "+result.getString(3)  );
				 if(result.getString(2).equals("age") == false) {
					array.put(i, result.getString(2));
				 	i++;
				 }
				 else if(result.getString(3).equals("int") == true) {
					 ageValid = true;
				 }
			 }
			 System.out.println("age is valid = "+ageValid);
			 conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
		 
		 table.setColumnArray(array);
	 }

	 public  Map<Integer,String> getColList(TableEntity table) {
		return table.getColumnArray();
	 }

	 
	public List<TableEntity> getTablesList() {
		return TablesList;
	}


	public void setTablesList() {
		ResultSet result = null;
		List<TableEntity> list = new ArrayList<TableEntity>();
		try {
			System.out.println("SetTableList");
			 String sql = "SELECT * FROM sqlite_master WHERE type='table'";
			 Connection conn = this.connect();
			 Statement stmt = conn.createStatement();
			 result =  stmt.executeQuery(sql);
			 while(result.next()) {
				 TableEntity tableActual = new TableEntity();
				 System.out.println(result.getInt(1) + " = " + result.getString(2) );
				 tableActual.setTableName(result.getString(2));
				 list.add(tableActual);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		this.TablesList = list ;
	}

	public TableEntity getTableByName(String tableName) {
		for(TableEntity table : TablesList) {
			if(table.getTableName().equals(tableName)) {
				return table;
			}
		}
		return null;
	}
	 

}
