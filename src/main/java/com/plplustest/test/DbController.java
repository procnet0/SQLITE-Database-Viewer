package com.plplustest.test;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;


@Component
public class DbController {
	 public Connection connect() throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String dbfile = "jdbc:sqlite:E:\\Bitnami\\wampstack-7.1.13-1\\apache2\\htdocs\\PlPlusTest\\onepageapp\\src\\main\\resources\\us-census.db";
		Connection conn = null;
		conn = DriverManager.getConnection(dbfile);
		return conn;
	 }
	 
	 public ResultSet selectCol(String colname, String orderby) {
		 ResultSet result = null;
		 String ordertype = "average";
		 if (orderby == "count") {
			 ordertype = "average";
		 }
		 
		 String sql = "SELECT ? as colname , count(*) AS counter, avg(age) AS average FROM census_learn_sql GROUP BY colname ORDER BY ? DESC LIMIT 100";
		 try {
				 Connection conn = this.connect();
	             PreparedStatement stmt = conn.prepareStatement(sql);
				 stmt.setString(1, colname);
				 stmt.setString(2, ordertype);
				 result = stmt.executeQuery();
				 conn.close();
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	            e.printStackTrace();
	            System.out.println(e.getSQLState());
	        }catch (NullPointerException e) {
	            System.out.println(e.getMessage());
	        }
		 return result;
	 }
	 
	 public ResultSet getColList() {
		 
		 ResultSet result = null;
		 String sql = "PRAGMA table_info(census_learn_sql)";
		 try {
			 Connection conn = this.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
			 result = stmt.executeQuery();
			 conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println(e.getSQLState());
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
		 return result;
	 }
	 

	 

}
