package com.plplustest.test;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ajaxHandler {
	DbController dbController = new DbController();
	
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

}
