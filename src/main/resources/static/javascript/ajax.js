$(document).ready(function() {
	
	function initiate() {
	      $.post({
	         url : 'init',
	         data : { foo : null },
	         success : function(res) {
	            if(res){
	            	console.log(res);
	            	var response = res;
	            	if(response && response['status'] && response['status']['Success']) {
	            		populateDb(response['databaseArrays']);
			    		setDbActive(response['databaseName']);
			    		populateTable(response['tableArrays']);
			    		setTbActive(response['tableName']);
			    		populateColumn(response['columnArrays']);
			    		setColActive(response['columnName']);
			    		populateDatatable(response['values']);
	            	}
	            }else{
	            	console.log("empty response for Init");
	            }
	         }
	      });
	};

	$('#addDatabase').submit(function(e) {
	    e.preventDefault();
	    var formdata = new FormData();
	    formdata.append("file",$('#inputGroupFile02')[0].files[0]);
	    xhr = new XMLHttpRequest();
	    xhr.open( 'POST', 'addFile', true );
	    xhr.onreadystatechange = function () {
	    	 if (this.readyState == 4 && this.status == 200) {
			    	console.log(JSON.parse(this.responseText));
			    	response = JSON.parse(this.responseText);
			    	if(response && response['status'] && response['status']['Success']) {
			    		populateDb(response['databaseArrays']);
			    		setDbActive(response['databaseName']);
			    		populateTable(response['tableArrays']);
			    		setTbActive(response['tableName']);
			    		populateColumn(response['columnArrays']);
			    		setColActive(response['columnName']);
			    		populateDatatable(response['values']);
			    	}
			    	else {
			    		
			    	}
	    	 }
	    };
	    xhr.send( formdata );
		return false;
	});
	$('#dataTable').DataTable();
	initiate();
});

function populateDb(db) {
	var htmlstring2 = "";
	var length = Object.keys(db).length;
	for(var i=0;i<length; i++) {
		var fun =  db[i];
		htmlstring2 += '<a class="dropdown-item" data-type="db" data-name="'+fun+'" onclick="getResult(event); return false;" href="'+ db[i]+'">'+ db[i] +'</a>';
	}
	$('#databaseDropZone').html(htmlstring2);
	$('#databaseDropZone').get(0).style.setProperty("--col-count-1", Math.ceil(length/15));
}

function setDbActive(dbName) {
	$('#databaseMenuButton').text(dbName);
}

function populateTable(tables) {
	var htmlstring2 = "";
	var length = Object.keys(tables).length;
	for(var i=0;i<length; i++) {
		var fun =  tables[i];
		htmlstring2 += '<a class="dropdown-item" data-type="table" data-name="'+fun+'" onclick="getResult(event); return false;" href="'+ tables[i]+'">'+ tables[i] +'</a>';
	}
	$('#tableDropZone').html(htmlstring2);
	$('#tableDropZone').get(0).style.setProperty("--col-count-1", Math.ceil(length/15));
}

function setTbActive(tableName) {
	$('#tableMenuButton').text(tableName);
}

function populateColumn(col) {
	var htmlstring2 = "";
	var length = Object.keys(col).length;
	for(var i=0;i<length; i++) {
		var fun =  col[i];
		htmlstring2 += '<a class="dropdown-item" data-type="col" data-name="'+fun+'" onclick="getResult(event); return false;" href="'+ col[i]+'">'+ col[i] +'</a>';
	}
	$('#columnDropZone').html(htmlstring2);
	$('#columnDropZone').get(0).style.setProperty("--col-count-1", Math.ceil(length/15));
}

function setColActive(colName) {
	$('#columnMenuButton').text(colName);
}

function populateDatatable(vals) {
	var dt = $('#dataTable').DataTable()
	dt.clear();
	var length = vals.length;
	for(var i=0;i<length; i++) {
		var node = dt.row.add([
			vals[i]['value'],
			vals[i]['count'],
			vals[i]['average']
		]).draw().node();
		$(node).find('td').eq(0).addClass('value-unit');
		$(node).find('td').eq(1).addClass('count-unit');
		$(node).find('td').eq(2).addClass('age-unit');
	}
}

function getResult(e) {
	console.log(e.target.dataset.name);
	var datatype = e.target.dataset.type;
	
	switch (datatype) {
		case "col":
			var categorie =  e.target.dataset.name;
			var tableSelected= $('#tableMenuButton').text().trim();
			var databaseSelected =$('#databaseMenuButton').text().trim();
			break;
		case "table":
			var categorie =  "";
			var tableSelected= e.target.dataset.name;
			var databaseSelected =$('#databaseMenuButton').text().trim();
			break;
		case "db":
			var categorie = "";
			var tableSelected= "";
			var databaseSelected =e.target.dataset.name;
			break;
	}
	
    $.post({
        url : 'postResult',
        data : {
        	selector : categorie,
        	table : tableSelected,
        	database : databaseSelected
        },
        success : function(res) {
       	 $("span.error").remove();
          
       	 if(res) {
	           	console.log(res);
            	var response = res;
            	if(response && response['status'] && response['status']['Success']) {
            		populateDb(response['databaseArrays']);
		    		setDbActive(response['databaseName']);
		    		populateTable(response['tableArrays']);
		    		setTbActive(response['tableName']);
		    		populateColumn(response['columnArrays']);
		    		setColActive(response['columnName']);
		    		populateDatatable(response['values']);
            	}
	           	
	           	if(res['remaining'] && res['remaining'] >= 1) {
	           		getResult(res['categorie']);
	           	}
           } else {
 	            $('#colMenu').after('<span class="error text-danger">Incorrect Category</span>');
           }
        }
     });
	

}
