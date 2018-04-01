$(document).ready(function() {
	
	
	$("#formSelector").submit(function(e) {
	      e.preventDefault();
	      $.post({
	         url : 'postResult',
	         data : $('form[name=formSelector]').serialize(),
	         success : function(res) {
	        	 $("span.error").remove();
	            if(res){
	            	console.log(res);
	            	var dt = $('#dataTable').DataTable()
	            	dt.clear();
	            	var length = res.length;
	            	var htmlstring ="";
	            	for(var i=0;i<length; i++) {
	            		var node = dt.row.add([
	            			res[i]['value'],
	            			res[i]['count'],
	            			res[i]['average']
	            		]).draw().node();
	            		$(node).find('td').eq(0).addClass('value-unit');
	            		$(node).find('td').eq(1).addClass('count-unit');
	            		$(node).find('td').eq(2).addClass('age-unit');
	            	}
	            	
	            }else{
	  	            $('select[name=selector]').after('<span class="error text-danger">Incorrect Category</span>');
	            }
	         }
	      });
	      return false;
	});
	

	
	function initiate() {
	
		 
	      $.post({
	         url : 'init',
	         data : { foo : null },
	         success : function(res) {
	            if(res){
	            	console.log(res);
	            	
	            	var htmlstring2 = "";
	            	var length = Object.keys(res).length;
	            	for(var i=0;i<length; i++) {
	            		var fun =  res[i];
	            		htmlstring2 += '<a class="dropdown-item" data-name="'+fun+'" onclick="getResult(event); return false;" href="'+ res[i]+'">'+ res[i] +'</a>';
	            	}
	            	$('#columnDropZone').html(htmlstring2);
	            	$('#columnDropZone').get(0).style.setProperty("--col-count-1", Math.ceil(length/15));
	            	$('#columnMenuButton').text(res[0]);
	            }else{
	            	console.log("empty response for Init");
	            }
	         }
	      });
	};


		
	$('#addDatabase').submit(function(e) {
	    e.preventDefault();
	    var formdata = new FormData();
	    formdata.append("file",$('#inputGroupFile02')[0].files[0])
	    console.log(formdata);
	   
	    
	    xhr = new XMLHttpRequest();

	    xhr.open( 'POST', 'addFile', true );
	    xhr.onreadystatechange = function ( response ) {
	    	console.log(response);
	    	if(response && response['Success']) {
	    	var active =$('#databaseMenuButton');
	    	$(active).text(response['Success'])
	    	$(active).attr('data-name', response['Success'])
	    	}
	    	else {
	    		
	    	}
	    };
	    xhr.send( formdata );
	    
		return false;
	});

	$('#dataTable').DataTable();
	initiate();
});

function getResult(e) {
	console.log(e.target.dataset.name);
	var categorie = e.target.dataset.name;
	console.log(categorie);
	var tableSelected= $('#tableMenuButton').text().trim();
	console.log(tableSelected);
	var databaseSelected =$('#databaseMenuButton').text().trim();
	console.log(databaseSelected);
	
    $.post({
        url : 'postResult',
        data : {
        	selector : categorie,
        	table : tableSelected,
        	database : databaseSelected
        },
        success : function(res) {
       	 $("span.error").remove();
          
       	 if(res){
	           	console.log(res);
	           	$('#columnMenuButton').text(categorie);
	           	var dt = $('#dataTable').DataTable()
	           //	if(res['offset'] == 0)
	           		dt.clear();
	           	var length = res.length;
	           	for(var i=0;i<length; i++) {
	           		var node = dt.row.add([
	           			res[i]['value'],
	           			res[i]['count'],
	           			res[i]['average']
	           		]).draw().node();
	           		$(node).find('td').eq(0).addClass('value-unit');
	           		$(node).find('td').eq(1).addClass('count-unit');
	           		$(node).find('td').eq(2).addClass('age-unit');
	           	}
	           	
	           	if(res['remaining'] && res['remaining'] >= 1) {
	           		getResult(res['categorie']);
	           	}
           }else{
 	            $('#colMenu').after('<span class="error text-danger">Incorrect Category</span>');
           }
       	 
       	 
        }
     });
	

}
