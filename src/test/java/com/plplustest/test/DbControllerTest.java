package com.plplustest.test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.sqlite.SQLiteDataSource;

public class DbControllerTest {
	
	 @InjectMocks private DbController dbController;
	 @Mock private SQLiteDataSource mockDataSource;
	 @Mock private List<TableEntity> mockTablesList;
	 @Mock private String mockdataBaseName;
	 @Mock private Boolean mockisValid;
	
	@Test
	public void TestConnect() throws Exception {
		
	}
}
