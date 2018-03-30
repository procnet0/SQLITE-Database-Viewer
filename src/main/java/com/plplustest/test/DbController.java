package com.plplustest.test;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sqlite.SQLiteDataSource;




public class DbController {
	
	
	SQLiteDataSource dataSource= null;
	 Map<Integer,String> collist = null;
	
	public DbController() {
		SQLiteDataSource datasourc = new SQLiteDataSource();
		datasourc.setUrl("jdbc:sqlite:db/us-census.db");
		this.dataSource = datasourc;
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
		 String sql = "SELECT `" +colname+"` as colname , count(*) AS counter, avg(age) AS average FROM census_learn_sql GROUP BY colname ORDER BY colname DESC ,counter  DESC, average DESC LIMIT 100";
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
	 
	 public  Map<Integer,String> getColList() {

		 System.out.println("GETCOLLIST");
		 ResultSet result = null;
		 String sql = "PRAGMA table_info(census_learn_sql)";
		 Map<Integer,String> array = new HashMap<Integer,String>();
		 try {
			 Connection conn = this.connect();
			 System.out.println("PREPARE STATEMENT");
             PreparedStatement stmt = conn.prepareStatement(sql);

			 System.out.println("PREPARE STATEMENT DONE");
			 result = stmt.executeQuery();

			 System.out.println("EXECUTE STATEMENT");
			 result.next();
			 while(result.next()) {
				 System.out.println(result.getInt(1) + " = " + result.getString(2));
				 array.put(result.getInt(1), result.getString(2));
			 }
			 collist = array;
			 conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
		 
		 return array;
	 }
	 

	 

}
