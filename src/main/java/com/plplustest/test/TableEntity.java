package com.plplustest.test;

import java.util.Map;


// Represent a Table inside a database . 
// This element is always associated with a DbController.
public class TableEntity {
	
	String tableName = null;
	Map<Integer,String> columnArray= null;
	Boolean agevalid = false;
	
	public Boolean getAgevalid() {
		return agevalid;
	}
	public void setAgevalid(Boolean agevalid) {
		this.agevalid = agevalid;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Map<Integer, String> getColumnArray() {
		return columnArray;
	}
	public void setColumnArray(Map<Integer, String> columnArray) {
		this.columnArray = columnArray;
	}

}
