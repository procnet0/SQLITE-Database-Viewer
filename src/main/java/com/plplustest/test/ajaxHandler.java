package com.plplustest.test;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
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
	DbController dbController = new DbController();
	MultipartResolver multipartResolver = null;
	
	@PostMapping("postResult")
	public ResponseEntity<List<ResultUnit>> getDbResult(HttpServletRequest request , HttpServletResponse response) {
		String colname = request.getParameter("selector");
		String tablename = "census_learn_sql";
		if(colname == null) {
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
		 Map<Integer,String> result = dbController.getTableByName("census_learn_sql").getColumnArray();
		if(result == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity< Map<Integer,String>>(result,HttpStatus.OK);
	}
	
	@PostMapping("addFile")
	public ResponseEntity<String> addDatabase( @RequestParam("file") MultipartFile file) {

		System.out.println("ADD FILE type "+file.getContentType() +" name ="+ file.getName() +" real name ="+ file.getOriginalFilename() + " size ="+ file.getSize());
		
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
				return new ResponseEntity<String>("Success",HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<String>("File is empty",HttpStatus.BAD_REQUEST);
		}
	}
	
}
