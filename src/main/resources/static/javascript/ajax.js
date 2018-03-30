$(document).ready(function() {
	
	
	$("#formSelector").submit(function(e) {
	      e.preventDefault();
	      $.post({
	         url : 'postResult',
	         data : $('form[name=formSelector]').serialize(),
	         success : function(res) {
	        	 $("span.error").remove();
	            if(res){
	            	var dropzone = $('#resultDrop');
	            	$(dropzone).html("");
	            	var length = res.length;
	            	var htmlstring ="";
	            	for(var i=0;i<length; i++) {
	            		htmlstring += "<tr><th scope='row'>" + i + "</th> <td>"+res[i]['value']+"</td> <td>"+res[i]['count']+"</td> <td>"+res[i]['average']+"</td></tr>";
	            	}
	        
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
	            	$('option').remove();
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
	
	initiate();
	
});