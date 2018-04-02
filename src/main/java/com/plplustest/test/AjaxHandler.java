package com.plplustest.test;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AjaxHandler {
	List<DbController> ListdbController = new ArrayList<DbController>();
	
	public AjaxHandler() {
		File dir = new File("db/");
		if(dir.isDirectory()) {
			File[] lfile = dir.listFiles();
			int len = lfile.length;
			for(int i = 0; i < len; i++) {
				if(lfile[i].getName().endsWith(".db")) {
					DbController dbCandidate = new DbController(lfile[i].getName());
					System.out.println(lfile[i].getName());
					if(dbCandidate != null && dbCandidate.getisValid() == true) {
						System.out.println("add "+ dbCandidate.getDataBaseName());
						ListdbController.add(dbCandidate);
					}
				}
			}
		}
	}
	
	public DbController getDbController(String dbName) {
		for(DbController dbControl : ListdbController) {
			if(dbControl.getDataBaseName().equals(dbName)) {
				return dbControl;
			}
		}
		return null;
	}
	
	@PostMapping("postResult")
	public ResponseEntity<List<ResultUnit>> getDbResult(HttpServletRequest request , HttpServletResponse response) {
		String colname = request.getParameter("selector");
		DbController dbController = getDbController("us-census.db");
		String tablename = "census_learn_sql";
		System.out.println("GETDBRESULT - " + colname);
		if(colname == null || dbController == null || tablename == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		List<ResultUnit> result = dbController.selectCol(colname, tablename);
		if(result == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<ResultUnit>>(result, HttpStatus.OK);
	}
	
	@PostMapping("init")
		public ResponseEntity< Map<Integer,String>> initFromDb(HttpServletRequest request , HttpServletResponse response) {
		DbController dbController = getDbController("us-census.db");
		 Map<Integer,String> error = new HashMap<>();
		if(dbController == null) {
			System.out.println(" dbcontroller is  null");
			error.put(1, " dbcontroller is null");
			return new ResponseEntity<>(error,HttpStatus.NO_CONTENT);
		}
		Map<Integer,String> result = dbController.getTableByName("census_learn_sql").getColumnArray();
		if(result == null) {
			System.out.println(" result is null ");
			error.put(1, " result is null");
			return new ResponseEntity<>(error,HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity< Map<Integer,String>>(result,HttpStatus.OK);
	}
	
	@PostMapping("addFile")
	public ResponseEntity<Map<String,String>> addDatabase( @RequestParam("file") MultipartFile file) {

		System.out.println("ADD FILE type "+file.getContentType() +" name ="+ file.getName() +" real name ="+ file.getOriginalFilename() + " size ="+ file.getSize());
		Map<String,String> result = new HashMap<String, String>();
		if(file.isEmpty() == false) {
			try {
				File dir = new File("db");
				if (!dir.exists())
					dir.mkdirs();
				System.out.println(dir.getAbsolutePath());
				System.out.println("filename = "+file.getName());
				File serverFile = new File(dir.getAbsolutePath()+ File.separator + file.getOriginalFilename());
				if(serverFile.exists() == false) {
					Files.copy(file.getInputStream(), serverFile.toPath());
				}
				DbController dbCandidate = new DbController(serverFile.getName());
				if(dbCandidate != null && dbCandidate.getisValid().equals(true)) {
					ListdbController.add(dbCandidate);
					result.put("Success", file.getOriginalFilename());
					return new ResponseEntity<Map<String,String>>(result,HttpStatus.OK);
				}
				serverFile.delete();
				result.put("Error", "Database is not valid");
				return new ResponseEntity<Map<String,String>>(result,HttpStatus.INTERNAL_SERVER_ERROR);
				
			} catch (Exception e) {
				e.printStackTrace();
				result.put("Error", e.getMessage());
				return new ResponseEntity<Map<String,String>>(result,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			result.put("Error", "File is empty");
			return new ResponseEntity<Map<String,String>>(result,HttpStatus.BAD_REQUEST);
		}
	}
	
	public JsonResponseGeneric computeResult(String dbName) {
		JsonResponseGeneric response = new JsonResponseGeneric();
		DbController db = getDbController(dbName);
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
		JsonResponseGeneric response = new JsonResponseGeneric();
		DbController db = getDbController(dbName);
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
		JsonResponseGeneric response = new JsonResponseGeneric();
		DbController db = getDbController(dbName);
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