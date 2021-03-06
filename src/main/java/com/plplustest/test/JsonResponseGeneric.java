package com.plplustest.test;

import java.util.List;
import java.util.Map;

public class JsonResponseGeneric {
	
	private String databaseName;
	private String tableName;
	private String columnName;
	private List<String> databaseArrays;
	private Map<Integer,String> tableArrays;
	private Map<Integer,String> columnArrays;
	private List<ResultUnit> values;
	private Integer maxResult = 0;
	private Integer offset;
	private Map<String,String> status;
	
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Map<Integer, String> getTableArrays() {
		return tableArrays;
	}
	public void setTableArrays(Map<Integer, String> tableArrays) {
		this.tableArrays = tableArrays;
	}
	public Map<Integer, String> getColumnArrays() {
		return columnArrays;
	}
	public void setColumnArrays(Map<Integer, String> columnArrays) {
		this.columnArrays = columnArrays;
	}
	public List<ResultUnit> getValues() {
		return values;
	}
	public void setValues(List<ResultUnit> values) {
		this.values = values;
	}
	public Integer getMaxResult() {
		return maxResult;
	}
	public void setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Map<String, String> getStatus() {
		return status;
	}
	public void setStatus(Map<String, String> status) {
		this.status = status;
	}
	public List<String> getDatabaseArrays() {
		return databaseArrays;
	}
	public void setDatabaseArrays(List<String> dbArray) {
		this.databaseArrays = dbArray;
	}

}
