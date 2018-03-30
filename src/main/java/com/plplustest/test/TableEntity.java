package com.plplustest.test;

import java.util.Map;

public class TableEntity {
	
	String tableName = null;
	Map<Integer,String> columnArray= null;
	
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
