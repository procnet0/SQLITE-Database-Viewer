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
import org.springframework.web.multipart.MultipartResolver;

@RestController
public class ajaxHandler {
	List<DbController> ListdbController = new ArrayList<DbController>();
	MultipartResolver multipartResolver = null;
	
	public ajaxHandler() {
		File dir = new File("db/");
		if(dir.isDirectory()) {
			File[] lfile = dir.listFiles();
			int len = lfile.length;
			for(int i = 0; i < len; i++) {
				if(lfile[i].getName().endsWith(".db")) {
					DbController dbCandidate = new DbController(lfile[i].getName());
					if(dbCandidate != null && dbCandidate.getisValid() == true)
						ListdbController.add(dbCandidate);
				}
			}
		}
	}
	
	public DbController getDbController(String dbName) {
		for(DbController dbControl : ListdbController) {
			if(dbControl.getDataBaseName().equals(dbName))
				return dbControl;
		}
		return null;
	}
	
	@PostMapping("postResult")
	public ResponseEntity<List<ResultUnit>> getDbResult(HttpServletRequest request , HttpServletResponse response) {
		String colname = request.getParameter("selector");
		DbController dbController = getDbController("us-census.db");
		String tablename = "census_learn_sql";
		System.out.println("GETDBRESULT - " + colname);
		if(colname == null || dbController == null) {
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
		if(dbController == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		Map<Integer,String> result = dbController.getTableByName("census_learn_sql").getColumnArray();
		if(result == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
	
}
