$(document).ready(function() {
	
	
	$("#sender").click(function(e) {
	      e.preventDefault();
	      
	      $.post({
	         url : 'getResult',
	         data : $('form[name=formSelector]').serialize(),
	         success : function(res) {
	        	 $("span.error").remove();
	            if(res){
	            	console.log(res);
	             
	            }else{
	  	            $('select[name=selector]').after('<span class="error text-danger">Incorrect Category</span>');
	            }
	         }
	      })
	      return false;
	});
	
	
	function initiate() {
	
		 
	      $.post({
	         url : 'init',
	         data : { foo : null },
	         success : function(res) {
	            if(res){
	            	console.log(res);
	            	$("#selector");
	        		
	            }else{
	            	$("#selector");
	            }
	         }
	      })
		
	}
	
	initiate();
	
});