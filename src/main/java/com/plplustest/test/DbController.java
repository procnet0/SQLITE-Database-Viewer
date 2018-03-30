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
	 Map<Integer,String> collist = null;
	 Map<Integer,String> TablesList = null;
	 String dataBaseName = null;
	


	public DbController() {
		SQLiteDataSource datasourc = new SQLiteDataSource();
		datasourc.setUrl("jdbc:sqlite:db/us-census.db");
		this.dataSource = datasourc;
		setDataBaseName();
		setCollist();
		setTablesList();
	}
	
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
	 
	 public List<ResultUnit> selectCol(String colname) {
		 ResultSet result = null;
		 System.out.println("SELECTCOL");
		 if(collist.containsValue(colname) == false )
			 return null;;
		 List<ResultUnit> results = new ArrayList<ResultUnit>();
		 String sql = "SELECT `" +colname+"` as colname , count(*) AS counter, avg(age) AS average FROM census_learn_sql GROUP BY colname ORDER BY colname*1 ASC , colname DESC LIMIT 100";
		 try {
				 Connection conn = this.connect();
				 System.out.println("PREPARE STATEMENT");
	             PreparedStatement stmt = conn.prepareStatement(sql);
				 System.out.println("EXECUTE STATEMENT");
				 result = stmt.executeQuery();
				 while(result.next()) {
					 ResultUnit unit = new ResultUnit();
					 System.out.println(result.getString(1) + " = " + result.getInt(2) + " / " + result.getFloat(3));
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
	 
	 public void setCollist() {

		 System.out.println("SETCOLLIST");
		 ResultSet result = null;
		 String sql = "PRAGMA table_info(census_learn_sql)";
		 Map<Integer,String> array = new HashMap<Integer,String>();
		 try {
			 Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
			 result = stmt.executeQuery();
			 result.next();
			 while(result.next()) {
				 System.out.println(result.getInt(1) + " = " + result.getString(2) + "is numeric" + result.getString(2));
				 array.put(result.getInt(1), result.getString(2));
			 }
			 conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
		 
		 this.collist = array;
	 }

	 public  Map<Integer,String> getColList() {
		return this.collist;
	 }

	 
	public Map<Integer, String> getTablesList() {
		return TablesList;
	}


	public void setTablesList() {
		ResultSet result = null;
		Map<Integer,String> array = new HashMap<Integer,String>();
		try {
			System.out.println("SetTableList");
			 String sql = "SELECT * FROM sqlite_master WHERE type='table'";
			 Connection conn = this.connect();
			 Statement stmt = conn.createStatement();
			 result =  stmt.executeQuery(sql);
			 while(result.next()) {
				 System.out.println(result.getInt(1) + " = " + result.getString(2) );
				 array.put(result.getInt(1), result.getString(2));
			 }
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
			
		this.TablesList = array ;
	}

	 

}
