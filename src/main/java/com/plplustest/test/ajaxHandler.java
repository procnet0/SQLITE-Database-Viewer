package com.plplustest.test;

import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ajaxHandler {
	DbController dbController = new DbController();
	
	
	@PostMapping("getResult")
	public ResponseEntity<ResultSet> getDbResult(HttpServletRequest request , HttpServletResponse response) {
		String colname = request.getParameter("name");
		String orderby =request.getParameter("order");
		if(colname == null || orderby == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		ResultSet result = dbController.selectCol(colname, orderby);
		if(result == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ResultSet>(result, HttpStatus.OK);
	}
	
	@PostMapping("init")
		public ResponseEntity<ResultSet> initFromDb(HttpServletRequest request , HttpServletResponse response) {
		ResultSet result = dbController.getColList();
		if(result == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<ResultSet>(result,HttpStatus.OK);
	}

}
