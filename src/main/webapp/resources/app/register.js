
$(document).ready(function() {



});

function reg(event) {
	var sucess = true;
	var image = $('#file-1')[0].files[0];
	var message = {};
	
	
	/*if (image == undefined ){
		$('#errorMessage').html(" Profile picture Required...!!!");
		sucess = false;
	}*/
		
	if ($('#firstName').val() == "") {
	
			$('#errorMessage').html(" First Name Required...!!!");
			sucess = false;

	} else {
		message.firstName = $('#firstName').val();
	}

	if ($('#lastName').val() == "") {

		$('#errorMessage').html(" LastName Required...!!!");
		sucess = false;

	} else {
		message.lastName = $('#lastName').val();
	}

	if ($("#maleRadioBtn").prop("checked")) {
		message.gender = "male";
	} else {
		message.gender = "female";
	}

	if ($('#userName').val() == "") {

		$('#errorMessage').html(" UserName Required...!!!");
		sucess = false;

	} else {
		message.userName = $('#userName').val();
	}

	if ($('#emailId').val() == "") {

		$('#errorMessage').html(" Email id is Required...!!!");
		sucess = false;

	}
	else{
		
		if(IsEmail($('#emailId').val())){
			
			message.emailId = $('#emailId').val();
			
		}
		else{
			$('#errorMessage').html(" Email id is Incorrect...!!!");
			sucess = false;
		}
		
	}

	if ($('#password').val() == "") {

		$('#errorMessage').html("Password Required...!!!");
		sucess = false;

	} else {
		message.password = $('#password').val();
	}

	if ($('#password1').val() == "") {

		$('#errorMessage').html(" Repeat password Required...!!!");
		sucess = false;

	}
	
  var password = $("#password").val();
	  
	  var regex = /[\u3000-\u303F]|[\u3040-\u309F]|[\u30A0-\u30FF]|[\uFF00-\uFFEF]|[\u4E00-\u9FAF]|[\u2605-\u2606]|[\u2190-\u2195]|\u203B/g; 

	  if(regex.test(password)){
		   //  console.log("Japanese characters found");
			$('#errorMessage').html("Password contains alpha numeric or special characters only");
		    sucess = false;
		    
	  }

	var password = $("#password").val();
	var confirmPassword = $("#password2").val();

	if (password != confirmPassword) {

		$('#errorMessage').html("Password doesn't match...!!!");
		sucess = false;

	} else {
		message.password = $('#password').val();
	}
	
	
	

	if (sucess) {
		//23-01-19
		$("#register").attr("disabled", "disabled");

		var uploadAttachment = new FormData();
		uploadAttachment.append("file", image);
		uploadAttachment.append('threadPid', JSON.stringify(message));

		var messageContextPath = location.protocol + '//' + location.host
				+ location.pathname;
		$.ajax({
			method : 'POST',
			url : messageContextPath,
			data : uploadAttachment,
			cache : false,
			contentType : false,
			processData : false,
			success : function(data) {

				$('#errorMessage').html(data.message);

				setTimeout(function() {
					window.location.href = 'login';
					$('#errorMessage').html("");
				}, 1000);

			},
			error : function(xhr, error) {
				console.log(xhr, error);
				$("#register").attr("disabled", "");

			}

		});

		$('#errorMessage').html(" ");
	} else {
		setTimeout(function() {
			$('#errorMessage').html("");
		}, 3000);
	}
}


function IsEmail(email) {
	
	  var regex = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	  if(!regex.test(email)) {
		  
	    return false;
	  }
	  else
	  {
	    return true;
	  }
}
	

