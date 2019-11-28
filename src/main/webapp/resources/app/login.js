

$(document).ready(function() {
	
	$("#savePassword").click(function(ev){
		
		var sucess=true;
		var tocken=$("#tockenFrgtPassword").val();
		var newPass=$("#newPassTextBox").val();
		var conformPass=$("#confNewPassTextBox").val();
		
		var emailId=$("#emailFrgtPass").val();
		
		if (tocken == "") {
			
			$('#errorMessageFrgtPass').html(" Tocken Required...!!!");
			sucess = false;

	     }
		  
		  var regex = /[\u3000-\u303F]|[\u3040-\u309F]|[\u30A0-\u30FF]|[\uFF00-\uFFEF]|[\u4E00-\u9FAF]|[\u2605-\u2606]|[\u2190-\u2195]|\u203B/g; 

		  if(regex.test(newPass)){
			   //  console.log("Japanese characters found");
				$('#errorMessageFrgtPass').html("Password contains alpha numeric or special characters only");
			    sucess = false;
			    
		  }

		if (newPass != conformPass) {

			$('#errorMessageFrgtPass').html("Password doesn't match...!!!");
			sucess = false;

		} 
		var messageContextPath = location.protocol + '//'
		+ location.host + '/savePassword';

				$.ajax({
					method : 'POST',
					url : messageContextPath,
					data : {
						tocken : tocken,
						password : newPass,
						emailId :emailId
					},
				
					success : function(data) {
				
						console.log(data);
				
						if (data.message == "sucess") {
				
							$("#forgot_password").modal('hide');
				
						}else if(data.message== "invalid-tocken"){
							// display error message
							$("#errorMessageFrgtPass").html("Token Mismatch...!!!")
						}
				
					},
					error : function(xhr, error) {
						console.log(xhr, error);
					}
				
				});
		
		
		
	});

	//forgotPassword id link
	$("#nextBtnFrgtPassword").click(function(ev){
		
		
		var emailId=$("#emailFrgtPass").val();
		var userName=$("#userNameFrgtPass").val();
						if (IsEmail(emailId)) {

							var messageContextPath = location.protocol + '//'
									+ location.host + '/forgotPassword';
						
							$.ajax({
								method : 'POST',
								url : messageContextPath,
								data : {
									emailId : emailId,
									userName : userName
								},

								success : function(data) {

									console.log(data);

									if (data.message == "send") {

										$('#forgotpassDiv').hide();
										$('#forgotPassTokenDiv').show();
										$('#savePassword').show();
										$('#nextBtnFrgtPassword').hide();

									}else{
										// display error message
									}

								},
								error : function(xhr, error) {
									console.log(xhr, error);
								}

							});

						}else{
							// emailId  not Valid
			
		                }
		
		
		
		
	});


});


function IsEmail(email) {
	  var regex = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	  if(!regex.test(email)) {
	    return false;
	  }else{
	    return true;
	  }
}

