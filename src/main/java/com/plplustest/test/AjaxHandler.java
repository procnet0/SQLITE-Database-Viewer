package com.plplustest.test;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AjaxHandler {
	private List<DbController> ListdbController = new ArrayList<DbController>();
	private List<String> dbArray = new ArrayList<String>(); 
	private static final Logger LOGGER = LoggerFactory.getLogger(AjaxHandler.class);
	
	public AjaxHandler() {
		LOGGER.info("AJAXHANDLER CONSTRUCTOR START");
		File dir = new File("db/");
		if(dir.isDirectory()) {
			File[] lfile = dir.listFiles();
			int len = lfile.length;
			for(int i = 0; i < len; i++) {
				if(lfile[i].getName().endsWith(".db")) {
					DbController dbCandidate = new DbController(lfile[i].getName());
					if(dbCandidate != null && dbCandidate.getisValid() == true) {
						LOGGER.info("AJAXHANDLER CONSTRUCTOR - add " +  dbCandidate.getDataBaseName());
						ListdbController.add(dbCandidate);
						dbArray.add(dbCandidate.getDataBaseName());
					}
				}
			}
		}
		LOGGER.info("AJAXHANDLER CONSTRUCTOR END");
	}
	
	public DbController getDbController(String dbName) {
		LOGGER.info("getDbController");
		for(DbController dbControl : ListdbController) {
			if(dbControl.getDataBaseName().equals(dbName)) {
				return dbControl;
			}
		}
		return null;
	}
	
	@PostMapping("postResult")
	public ResponseEntity<JsonResponseGeneric> getDbResult(HttpServletRequest request , HttpServletResponse response) {
		String colname = request.getParameter("selector");
		String dbName = request.getParameter("database");
		String tablename =  request.getParameter("table");
		LOGGER.info("GETDBRESULT - database - table - column - " +  dbName + " - " + tablename + " - " + colname);
		if(colname == null && dbName == null && tablename == null ) {
			return new ResponseEntity<JsonResponseGeneric>(HttpStatus.BAD_REQUEST);
		}
		if(!(dbName == null || dbName.isEmpty()) && !(tablename == null || tablename.isEmpty()) && !(colname == null || colname.isEmpty()) ) {
			return new ResponseEntity<JsonResponseGeneric>(computeResult(dbName,tablename, colname), HttpStatus.OK);
		}
		else if(!(dbName == null || dbName.isEmpty()) && !(tablename == null || tablename.isEmpty()) ) {
			return new ResponseEntity<JsonResponseGeneric>(computeResult(dbName,tablename), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<JsonResponseGeneric>(computeResult(dbName), HttpStatus.OK);
		}

	}
	
	@PostMapping("init")
		public ResponseEntity<JsonResponseGeneric> initFromDb(HttpServletRequest request , HttpServletResponse response) {

		LOGGER.info("INITFROMDB");
		String dbname = "";
		if(!dbArray.isEmpty())
			dbname = dbArray.get(0);
		return new ResponseEntity<JsonResponseGeneric>(computeResult(dbname),HttpStatus.OK);
	}
	
	@PostMapping("addFile")
	public ResponseEntity<JsonResponseGeneric> addDatabase( @RequestParam("file") MultipartFile file) {

		LOGGER.info("ADD FILE type "+file.getContentType() +" name ="+ file.getName() +" real name ="+ file.getOriginalFilename() + " size ="+ file.getSize());
		JsonResponseGeneric result = new JsonResponseGeneric();
		Map<String,String> status = new HashMap<String,String>();
		if(file.isEmpty() == false) {
			try {
				File dir = new File("db");
				if (!dir.exists())
					dir.mkdirs();
				File serverFile = new File(dir.getAbsolutePath()+ File.separator + file.getOriginalFilename());
				if(serverFile.exists() == false) {
					Files.copy(file.getInputStream(), serverFile.toPath());
				}
				DbController dbCandidate = new DbController(serverFile.getName());
				if(dbCandidate != null && dbCandidate.getisValid().equals(true)) {
					ListdbController.add(dbCandidate);
					dbArray.add(dbCandidate.getDataBaseName());
					return new ResponseEntity<JsonResponseGeneric>(computeResult(dbCandidate.getDataBaseName()),HttpStatus.OK);
				}
				serverFile.delete();
				status.put("Error", "Database is not valid");
				result.setStatus(status);
				return new ResponseEntity<JsonResponseGeneric>(result,HttpStatus.INTERNAL_SERVER_ERROR);
				
			} catch (Exception e) {
				e.printStackTrace();
				status.put("Error", e.getMessage());
				result.setStatus(status);
				return new ResponseEntity<JsonResponseGeneric>(result,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			status.put("Error", "File is empty");
			result.setStatus(status);
			return new ResponseEntity<JsonResponseGeneric>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	public JsonResponseGeneric computeResult(String dbName) {
		LOGGER.info("COMPUTERESULT - 1 ARG - " + dbName);
		JsonResponseGeneric response = new JsonResponseGeneric();
		DbController db = getDbController(dbName);
		response.setDatabaseArrays(dbArray);
		Map<String, String> status = new HashMap<String, String>();
		if(db != null) {
			response.setDatabaseName(dbName);
			List<TableEntity> tablelist = db.getTablesList();
			if(!tablelist.isEmpty()) {
				Map<Integer,String> tableArray = new HashMap<Integer,String>();
				Integer i = 0;
				for(TableEntity table: tablelist ) {
					tableArray.put(i, table.getTableName());
					i++;
				}
				response.setTableArrays(tableArray);
				response.setTableName(tablelist.get(0).getTableName());
				response.setColumnArrays(tablelist.get(0).getColumnArray());
				if(response.getColumnArrays() != null) {
					response.setColumnName(response.getColumnArrays().get(0));
					response.setMaxResult(db.getMaxResult(response.getColumnName(), response.getTableName()));
					response.setValues(db.selectCol(response.getColumnName(), response.getTableName()));
					response.setOffset(0);
					status.put("Success", "DataTable updated");
				}else {
					status.put("Error", " Columns not found/invalid");
				}
			}else {
				status.put("Error", " Tables not found/invalid");
			}
		}
		else {
			status.put("Error", "Database not found/invalid");
		}
		response.setStatus(status);
		return response;
	}
	
	public JsonResponseGeneric computeResult(String dbName, String tbName) {
		LOGGER.info("COMPUTERESULT - 2 ARG - " + dbName + " - "+ tbName );
		JsonResponseGeneric response = new JsonResponseGeneric();
		DbController db = getDbController(dbName);
		response.setDatabaseArrays(dbArray);
		Map<String, String> status = new HashMap<String, String>();
		if(db != null) {
			response.setDatabaseName(dbName);
			List<TableEntity> tablelist = db.getTablesList();
			TableEntity tb = db.getTableByName(tbName);
			if(!tablelist.isEmpty() && tb != null) {
				Map<Integer,String> tableArray = new HashMap<Integer,String>();
				Integer i = 0;
				for(TableEntity table: tablelist ) {
					tableArray.put(i, table.getTableName());
					i++;
				}
				response.setTableArrays(tableArray);
				response.setTableName(tbName);
				response.setColumnArrays(tb.getColumnArray());
				if(response.getColumnArrays() != null) {
					response.setColumnName(response.getColumnArrays().get(0));
					response.setMaxResult(db.getMaxResult(response.getColumnName(), response.getTableName()));
					response.setValues(db.selectCol(response.getColumnName(), response.getTableName()));
					response.setOffset(0);
					status.put("Success", "DataTable updated");
				}else {
					status.put("Error", " Columns not found/invalid");
				}
			}else {
				status.put("Error", " Tables not found/invalid");
			}
		}
		else {
			status.put("Error", "Database not found/invalid");
		}
		response.setStatus(status);
		return response;
	}
	
	public JsonResponseGeneric computeResult(String dbName, String tbName, String colName) {

		LOGGER.info("COMPUTERESULT - 3 ARG - " + dbName + " - "+ tbName + " - " + colName );
		JsonResponseGeneric response = new JsonResponseGeneric();
		DbController db = getDbController(dbName);
		response.setDatabaseArrays(dbArray);
		Map<String, String> status = new HashMap<String, String>();
		if(db != null) {
			response.setDatabaseName(dbName);
			List<TableEntity> tablelist = db.getTablesList();
			TableEntity tb = db.getTableByName(tbName);
			if(!tablelist.isEmpty() && tb != null) {
				Map<Integer,String> tableArray = new HashMap<Integer,String>();
				Integer i = 0;
				for(TableEntity table: tablelist ) {
					tableArray.put(i, table.getTableName());
					i++;
				}
				response.setTableArrays(tableArray);
				response.setTableName(tbName);
				response.setColumnArrays(tb.getColumnArray());
				if(response.getColumnArrays() != null && response.getColumnArrays().containsValue(colName)) {
					response.setColumnName(colName);
					response.setMaxResult(db.getMaxResult(response.getColumnName(), response.getTableName()));
					response.setValues(db.selectCol(response.getColumnName(), response.getTableName()));
					response.setOffset(0);
					status.put("Success", "DataTable updated");
				}else {
					status.put("Error", " Columns not found/invalid");
				}
			}else {
				status.put("Error", " Tables not found/invalid");
			}
		}
		else {
			status.put("Error", "Database not found/invalid");
		}
		response.setStatus(status);
		return response;
	}
	
	
}
