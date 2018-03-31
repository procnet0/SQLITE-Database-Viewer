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
	            	var dropzone = $('#resultDrop');
	            	$(dropzone).html("");
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
	            	$('#dataTable').DataTable().
	            	dropzone.html(htmlstring);
	            	
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
	            	var htmlstring = "";
	            	$('#selector option').remove();
	            	var length = Object.keys(res).length + 1;
	            	for(var i=1;i<length; i++) {
	            		htmlstring += '<option value="'+ res[i] +'" >' + res[i] + '</option>';
	            	}
	            	$('#selector').append(htmlstring);
	            }else{
	            	$("#selector");
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
	    xhr.onreadystatechange = function ( response ) {console.log(response);};
	    xhr.send( formdata );
/*
	     $.ajax({
	    	 type: "POST",
	         url : "addFile",
	         enctype: 'multipart/form-data',
	         data: formdata,
	         contentType: false,
	         cache: false,
	         processData: false,
	         success : function(res) {
	        	 $("span.error").remove();
	            if(res){
	            	console.log(res);
	            }else{
	            	console.log(res);
	  	            $('#dbsender').after('<span class="error text-danger">Error</span>');
	            }
	         }
	      });*/
	    
		return false;
	});
	
	initiate();
	$('#dataTable').DataTable();
});