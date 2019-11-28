var webSocket   = null;
    var ws_protocol = null;
    var ws_hostname = null;
    var ws_port     = null;
    var ws_endpoint = null;



var receiverId;
var senderImage;

var isReciverExists = false;

var messageContextPath = location.protocol + '//' + location.host
		+ location.pathname;

var groupId;

var isGroupMessge = false;
var isIndMessage = false;
var flagDisableDiv= false;

var selectedChatImgurl="";

var selectedChatId;
var senderUk;
var receiverUk;
var groupImage;

$(document).ready(function() {

	// establish websocket connection
	connect();
	
	$('#imageSelected').text("");
	$("#userNameProfile").html(userFullName)

	$("#messageContentDiv").hide();

	
	$("#recoedDivShowLi").click(function(ev){
		
		$('#myModal').modal('show');
		
		$("#hidenInputMp3").val("");
		
		$("#voiceSendButton").hide();
		
	});
	
	$("#leftArrow").click(function(ev){
		
		$("#leftnav" ).addClass("leftContactNavDiv");
		
	});
	
	// group creation first view ( pic and name and discreption) 
	   $("#groupSaveNextBtn").click(function(ev){
		   
		/*   $('#new_group_members').modal('show');
		   $('#new_group').modal('hide');
		   var name=$('#groupNameFirstModel').val();
		   $('#groupNameHead').html(name);
		   $('.selct_members_show_div').empty();
		   groupImage =$('#file-1')[0].files[0];*/
		   
		   var messageContextPath = location.protocol + '//'
			+ location.host + location.pathname + '/getMembersToAddNewGroup';
		   
		   $.ajax({
				type    : 'POST',
				url     : messageContextPath,
				success : function(data) {
					
					 $('#chat_list').html("");
					
					if (data.length>0){   
						
						console.log('test');
						for(i=0;i<data.length;i++){
							var newFriendList='<li class="list-group-item contactPool" id="'+data[i].id+
							'" data-attachment="'+data[i].attachment.id+'" onclick="selectMemberToGroup(this)"><div><span class="pull-left"><img src="'
							+urlAttachemnt+'/'+data[i].attachment.id
							+'" alt="" class="img-circle img-sm m-r-10"></span></div><div class="list-group-item-body" style="text-align: left;"><div class="list-group-item-heading userNameDiv">'
							+data[i].firstName+'</div><div class="list-group-item-text">'
							+data[i].userName+'</div></div></li>';
							
							 $('#chat_list').append(newFriendList);
						}
	
						
					}else{
					
						console.log("contact list error in add new group");
				
					}
					
				},
				error : function(xhr, error) {
					console.log(xhr, error);
				}
			});
		   
		   
		   var name=$('#groupNameFirstModel').val();
		   $('#groupNameHead').html(name);
		   $('#new_group').modal('hide');
		   $('.selct_members_show_div').empty();
		   groupImage =$('#file-1')[0].files[0];
		   $('#new_group_members').modal('show');
		   
	   });
	
   $("#new_invite_user").click(function(ev){
		$("#inviteMailId").text("");
		$('#invite_user').modal('show');

        $("#inviteBtn").removeAttr("disabled", "");
		$("#errorMessage").text("");
		$("#inviteMailId").val("");
		
		$('.chat_member_list_invit').hide();
		$('#userNameDivInvite').html("");
		
		$("#inviteImage").attr("src","");
		
		$('#addUserBtnInvit').attr("data-userInvId" , '');
		
	});
   $("#new_group_div").click(function(ev){
	  
	   $('#new_group').modal('show');

	   $('#groupNameFirstModel').val(" ");
	   $('#discrpOfGrp').val(" ");
	   $('#blah').attr('src',"/resources/assets/img/sample_profile_pic.png");	
   });
   
   // friends notification 
   $("#friendRequest").click(function(ev){
	   
	   $("#frq-"+userId).hide();
	   
	   var messageContextPath = location.protocol + '//'
		+ location.host + location.pathname + '/friendsRequest';
		
		$.ajax({
			type : 'GET',
			url : messageContextPath,
			success : function(data) {
				
				$('#user_list_FriendReq').empty();
				
				if (!$.trim(data)){   
					// no data avilable
					
				}else{
				
					console.log(data);
					$.each(data,function(index, friendReqObj) {
						
						var friendName=friendReqObj.reqByUserId.firstName +" "+friendReqObj.reqByUserId.lastName; 
						
						var attachmentId = friendReqObj.reqByUserId.attachment.id;

						var imgurls = urlAttachemnt+ "/"+ attachmentId;
						
						var userLi='<li class="list-group-item contactPool group_list_members frient_rqst_list" id="lifreq-'+friendReqObj.reqByUserId.id+'" > '
							+'<div class="friend_request_profile_pic"> <span class="pull-left"> <img src='+imgurls+' '
							+'alt="" class="img-circle img-sm m-r-10 mCS_img_loaded"> </span> </div> <div class="list-group-item-body name_list_div friend_request_user_name">'
							+' <div class="list-group-item-heading userNameDivEdit" style=" text-align: left;">'+friendName+'</div> '
							+'<div class="list-group-item-text"></div> </div> <div class="accept_btn_div"> '
							+'<button type="button" data-userId="'+friendReqObj.reqByUserId.id+'" class="btn btn-success approve_btn" onclick="approveFriendRequest(this)" >Approve</button> '
							+'<button type="button" data-userId="'+friendReqObj.reqByUserId.id+'" class="btn btn-danger decline_btn" onclick="rejectFriendRequest(this)" >Decline</button>'
							+' </div> <div class="date_time_frnd_rqst"> <p><span class="date">01/14/2019</span></p> </div> </li>';
						
					
						$('#user_list_FriendReq').append(userLi);
						
						$("#friend_request").modal('show');
					
					});
				}
				
			},
			error : function(xhr, error) {
				console.log(xhr, error);
			}
		});
		  
	   $('#friend_request').modal('show');
   });
   
   //Self Service User Delete/De-registration
   $("#userDeActivateLi").click(function(ev){
	 
	   $('#userDeRegister').modal('show');
   });
   
   $("#userLogoutLi").click(function(ev){
		 
	   $('#userLogout').modal('show');
   });
   
   
   //conform
   $("#userDeRegisterBtn").click(function(){
	   
	   var messageContextPath = location.protocol + '//'
		+ location.host + location.pathname + '/deRegisterUser';
		
		$.ajax({
			type : 'GET',
			url : messageContextPath,
			success : function(data) {
				console.log(data);
			
				
			},
			error : function(xhr, error) {
				console.log(xhr, error);
			}
		});
	   
   });
   
   
   
   
   $("#searchPeopleBtn").click(function(){
	   
	   $("#searchInHomeViwUl").empty();
	   
	   var userName=$("#searchPeopleTxtBox").val();
	   
	   var messageContextPath = location.protocol + '//'
		+ location.host + location.pathname + '/searchPeople';
		
		$.ajax({
			type : 'GET',
			url : messageContextPath,
			data :{userName:userName}, 
			success : function(data) {
				console.log(data);
			
				if(data!=null){
					
					if(data.message=="friend"){// already frnd 
						
						var attachmentIdNewUser = data.userObj.attachment.id;

						var imgurlNew = urlAttachemnt + "/" + attachmentIdNewUser;
						var userFullName=data.userObj.firstName +" "+data.userObj.lastName;
						
				
						
						var li= '<li class="list-group-item" ><span class="pull-left">'
							+'<img src="'+imgurlNew+'" alt="" class="img-circle max-w-40 m-r-10 "></span> <div class="list-group-item-body"> '
							+'<div class="list-group-item-heading">'+userFullName +'</div> <div class="list-group-item-text">'+data.userObj.userName+'</div>'
							+' </div> <ul class="card-actions icons right-top"> <li class="dropdown"> <div class="rqst_btn_div"> <span>Friend</span>'
							+'  </div></li> </ul></li>';
					
					$("#searchInHomeViwUl").append(li);
						
						
					}else if(data.message=="requestSend"){//request send 
						
						var attachmentIdNewUser = data.userObj.attachment.id;

						var imgurlNew = urlAttachemnt + "/" + attachmentIdNewUser;
						var userFullName=data.userObj.firstName +" "+data.userObj.lastName;
						
		
					
						var li= '<li class="list-group-item" ><span class="pull-left">'
							+'<img src="'+imgurlNew+'" alt="" class="img-circle max-w-40 m-r-10 "></span> <div class="list-group-item-body"> '
							+'<div class="list-group-item-heading">'+userFullName +'</div> <div class="list-group-item-text">'+data.userObj.userName+'</div>'
							+' </div> <ul class="card-actions icons right-top"> <li class="dropdown"> <div class="rqst_btn_div"> <span>Request Send</span>'
							+'  </div></li> </ul></li>';
					
					
					
					$("#searchInHomeViwUl").prepend(li);
					
						
						
						
					}else if(data.message=="userFound"){ //user exist show user and add button
						
						var attachmentIdNewUser = data.userObj.attachment.id;

						var imgurlNew = urlAttachemnt + "/" + attachmentIdNewUser;
						var userFullName=data.userObj.firstName +" "+data.userObj.lastName;
						var onclickItem="addFriendInSearch(this)";
	            		
						
						var li= '<li class="list-group-item" ><span class="pull-left">'
							+'<img src="'+imgurlNew+'" alt="" class="img-circle max-w-40 m-r-10 "></span> <div class="list-group-item-body"> '
							+'<div class="list-group-item-heading">'+userFullName +'</div> <div class="list-group-item-text">'+data.userObj.userName+'</div>'
							+' </div> <ul class="card-actions icons right-top"> <li class="dropdown"> <div class="rqst_btn_div">'
							+' <input id="addReqBtnInSerch" type="submit" data-userinvid="'+data.userObj.id+'" value="Add Friend" onclick="'+onclickItem+'"   class="addrequest">'
							+'  <span id="addReqSpanInSerch"></span></div></li> </ul></li>';
					
					$("#searchInHomeViwUl").append(li);
			
						
					}else if(data.message=="userNotFound"){ //show message as user not found on db
						alert("no data found");
					}
				}
				
			},
			error : function(xhr, error) {
				console.log(xhr, error);
			}
		});
	   
   });
   
   
   $("#changePassBtn").click(function(){
	   
	   var sucess=true;
	   
	   var currentPass=$("#currentPassword").val();
	   
	   var newPassword=$("#newPassTextBox").val();
	   
	   var conformPassword=$("#confNewPassTextBox").val();
	 
	   var regex = /[\u3000-\u303F]|[\u3040-\u309F]|[\u30A0-\u30FF]|[\uFF00-\uFFEF]|[\u4E00-\u9FAF]|[\u2605-\u2606]|[\u2190-\u2195]|\u203B/g; 

		  if(regex.test(newPassword)){
			   //  console.log("Japanese characters found");
				$('#errorMessageChangePass').html("Password contains alpha numeric or special characters only");
			    sucess = false;
			    
		  }

	   if(newPassword==conformPassword){
		   if(sucess){
			   
			   var messageContextPath = location.protocol + '//'
				+ location.host + location.pathname + '/changePassword';
				
				$.ajax({
					type : 'GET',
					url : messageContextPath,
					data :{newPassword:newPassword,
						currentPass:currentPass}, 
					success : function(data) {
						console.log(data);
					if(data.message=="passwordChanged"){
						
						$('#errorMessageChangePass').css({ "color": "green"});
						$('#errorMessageChangePass').html("Password changed Sucessfully..!!!");
						
						
						setTimeout(function() {
							  $("#change_password").modal('hide');
							  $('#errorMessageChangePass').html("");
					          $("#currentPassword").val("");
							  $("#newPassTextBox").val("");
							  $("#confNewPassTextBox").val("");
						}, 1500);
						
					}else if(data.message=="currentPasswordWorng"){
						$('#errorMessageChangePass').html("current password does not match.");
					}
						
					},
					error : function(xhr, error) {
						console.log(xhr, error);
					}
				});
		   }
		   
			
		   
	   }else{
		   
			$('#errorMessageChangePass').html("Password does not Match..!!!");
		   
	   }
		   
	   
	   
	   
	   
	   
   });
	
	
	var recordOnOrOff = 0;
	$("#recordButton").click(
			function(ev) {

				var recordItemsTag = $(ev.currentTarget).closest(
						'.controlButtons').find('#recordContainer')
						.children('#recordCircle');
				console.log(recordItemsTag);
				recordOnOrOff++;
				if (recordOnOrOff == 1) {
					// $(ev.currentTarget).attr('src','../resources/img/voice/stop.ico');
					$(recordItemsTag).removeClass('startRecord')
							.addClass('stopRecord');
					// $("#recordContainer").removeClass('startContainer').addClass('stopContainer');
					$("#recordText").html("Stop");
					$.stopwatch.startTimer('sw');
					startRecording(recordItemsTag);
					getRecording(recordItemsTag);
				} else {
					startPreloader();
					// alert(totalTime);
					recordVoiceDuration = totalTime;
					recordOnOrOff = 0;
					// $(ev.currentTarget).attr('src','../resources/img/voice/Microphone.png');
					$.stopwatch.resetTimer();
					$(recordItemsTag).removeClass('stopRecord')
							.addClass('startRecord');
					// $("#recordContainer").removeClass('stopContainer').addClass('startContainer');
					$("#recordText").html("Record");
					stopRecording(recordItemsTag);
					getRecording(recordItemsTag);
					$("#audioPreLoader").show();
					$("#audioPreLoaderback").show();
				}

			});
	

});


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



$( "#footerDiv" ).on( "keyup", ".emoji-wysiwyg-editor", function(event) {
	  if ( event.which == 13 && event.shiftKey) {
		 sendPic("enter");
	  }
});

//add members Togroup

$('#addMenberToGrp').click(function(ev){
	
	var groupId=selectedChatId;
	var messageContextPath = location.protocol + '//'
	+ location.host + location.pathname + '/addMembers';
	
	
	$.ajax({
		type : 'GET',
		url : messageContextPath,
		data :{groupId:groupId}, 
		success : function(data) {
			console.log(data);
		
			
		},
		error : function(xhr, error) {
			console.log(xhr, error);
		}
	});
	
});


$("#addMenberToGrp_edit").click(function(){
	
	var groupId=selectedChatId;
	
	
	  
	   var messageContextPath = location.protocol + '//'
		+ location.host + location.pathname + '/getMembersToAddGroup';

		$.ajax({
			type : 'POST',
			url : messageContextPath,
			data :{emailId:emailId}, 
			success : function(data) {
				console.log(data);
				
				
				
			},
			error : function(xhr, error) {
				console.log(xhr, error);
			}
		});
	
});

// edit Group
$("#groupInfo").click(function(ev){
	
	$('#user_list_group').empty();
	
	$("#addMenberToGrp").hide();
	$("#addMenberToGrp_edit").show();
	
	
	
	var groupId=selectedChatId;
	   var messageContextPath = location.protocol + '//'
		+ location.host + location.pathname + '/loadGroupDeatils';
	
	if (!isIndMessage) {
		$.ajax({
			type : 'GET',
			url : messageContextPath,
			data :{groupId:groupId}, 
			success : function(data) {
				console.log(data);
			if(data.group!=null){
				
				$('#groupNameEdit').val(data.group.groupName);
				$('#groupDescriptionEdit').val(data.group.description);
				
				$("#groupImageEdit").attr("src",urlAttachemnt+ "/"+ data.group.attachment.id);
				
				
				if(data.groupMembersList!=null){
					var userDetailsList=data.groupMembersList;
					
					var currentUserGrp=data.currentUserGrpInfo;
					
					
					
					$.each(userDetailsList,function(index,user ) {
			
						var usersObj=user.user;
						console.log(user);
						
						
						var users='<li class="list-group-item contactPool group_list_members" > <div class="profile_pic">'
							+' <span class="pull-left"><img src='+urlAttachemnt+ "/"+ usersObj.attachment.id+' alt="" class="img-circle img-sm m-r-10 mCS_img_loaded"></span>'
							+' </div> <div class="list-group-item-body name_list_div"> <div class="list-group-item-heading userNameDivEdit" style=" text-align: left;">'+usersObj.firstName+'</div>'
							+' <div class="list-group-item-text"></div> </div> <div class="pos_group"> ';
							
						if(user.groupRole==1){
							users+='<span class="group_owner_color">Group Owner</span>';
						}
						
						users+='</div></li>';
						
							
							$('#user_list_group').append(users);
					});
				}
				
				/*
				var users='<li class="list-group-item contactPool group_list_members" > <div class="profile_pic">'
					+' <span class="pull-left"><img src="" alt="" class="img-circle img-sm m-r-10 mCS_img_loaded"></span>'
					+' </div> <div class="list-group-item-body name_list_div"> <div class="list-group-item-heading userNameDivEdit"></div>'
					+' <div class="list-group-item-text"></div> </div> <div class="pos_group"> <span class="group_owner_color">Group Owner</span> '
					+'</div> <!-- <div class="pos_group"> <div class="dropdown"> <button onclick="myFunction()" class="dropbtn">Group Admin</button> '
					+'<div id="myDropdown" class="dropdown-content"> <a href="#">Grant Admin role</a> <a href="#">Remove from group</a> </div> </div> </div> --> </li>;'
*/			}
				
			},
			error : function(xhr, error) {
				console.log(xhr, error);
			}
		});
	}
	
	
	
});


$(document).ready(function(){
	  $("#inviteMailId").change(function(){
		  $("#errorMessage").text("");
	  });
});


//invite button click
$("#inviteBtn123").click(function(ev){
	$("#errorMessage").text("");
	$('#addFriendStatus').html("");
	$('#errorMessageInInvite').html(" ");
	$('#errorMessageInInvite').text(" ");
	
	
     var emailId=$('#inviteMailId').val();
     if ($('#inviteMailId').val() == "") {
			$('#inviteMailId').css("border-bottom", "1px solid red");
			
			$('#errorMessage').html(" Email id is Required...!!!");
			
			  setTimeout(function() {
		  			$('#errorMessage').html("");
		  			$('#inviteMailId').css("border-bottom", "1px solid white");
		  			
		  		}, 3000);
			  sucess = false;
     }
		
		if(emailId!= ""){
				if(!IsEmail($('#inviteMailId').val())){
		          $('#errorMessage').html(" Email id is Incorrect...!!!");
		          $('#inviteMailId').css("border-bottom", "1px solid red");
		          $('#inviteMailId').css("color","red");
					sucess = false;
					
		          setTimeout(function() {
		  			$('#errorMessage').html("");
		  			$('#inviteMailId').css("color","black");
		  			$('#inviteMailId').css("border-bottom", "1px solid white");
		  			
		  		}, 3000);
		        }
				else{
					
					 if(emailId == userEmailId){
						 $('#errorMessage').html(" You are already an user...!!!");
						
						   success = false;
					 }
					 else{
					   // if login user not equal to
					   $('#inviteMailId').css("color","black");
					   $("#inviteBtn").attr("disabled", "disabled");
					   var messageContextPath = location.protocol + '//'
						+ location.host + location.pathname + '/inviteUser';

						$.ajax({
							type : 'POST',
							url : messageContextPath,
							data :{emailId:emailId}, 
							success : function(data) {
								//console.log(data);
								//24-1-19
								if(data.status=="alredy_send"){
									$("#errorMessage").text("Alredy Invited..!!");
									$("#inviteBtn").removeAttr("disabled", "");
									
								}else if(data.status=="sucess"){
									
									setTimeout(function() {
										$('#errorMessage').text("Invitation send sucessfully!!");
										$("#inviteBtn").removeAttr("disabled", "");
										$('#errorMessageInInvite').html("");
							  		}, 3000);

								     setTimeout(function(){
								    	 $('#invite_user').modal('hide');
								    	 $("#inviteBtn").removeAttr("disabled", "");
								     },5000);
								     
								
								}else if(data.status=="user_found"){
									//$('#errorMessageInInvite').text("User Alredy Exist!!");
									
										$('#userNameDivInvite').html(data.user.firstName +" "+data.user.lastName);
										$("#inviteImage").attr("src",urlAttachemnt+ "/"+ data.user.attachment.id);
										$('#addUserBtnInvit').attr('data-userInvId', data.user.id);
										$('.chat_member_list_invit').show();
										$("#errorMessage").text("");
										$("#inviteBtn").removeAttr("disabled", "");
										$('#addUserBtnInvit').show();
										
										
								}else if(data.status=="alredy_frnd"){
									$('#userNameDivInvite').html(data.user.firstName +" "+data.user.lastName);
									$("#inviteImage").attr("src",urlAttachemnt+ "/"+ data.user.attachment.id);
									$('#addUserBtnInvit').hide();
									$('.chat_member_list_invit').show();
									$("#errorMessage").text("");
									$("#inviteBtn").removeAttr("disabled", "");
								}else if(data.status=="request_send"){
									$('#userNameDivInvite').html(data.user.firstName +" "+data.user.lastName);
									$("#inviteImage").attr("src",urlAttachemnt+ "/"+ data.user.attachment.id);
									$('#addUserBtnInvit').hide();
									$('.chat_member_list_invit').show();
									$('#addFriendStatus').html("Request send.");
									
								}
								
								
							},
							error : function(xhr, error) {
								console.log(xhr, error);
								$("#inviteBtn").removeAttr("disabled", "");
							}
						
						});
						
					 }
				}
	   

		}
			
});



$('#file-input-data').change(function(ev){
	
	var filename = $('input[type=file]').val().split('\\').pop();
	$('#imageSelected').text(filename);
	$("#imageSelectedDiv").show();
	  
	});

$("#voiceToTextStart").click(function(ev){
	
	$('#voiceToTextstop').show();
	$("#voiceToTextStart").hide();
	$('.textarea-control').text("");
	
});

$("#deleteImageSele").click(function(ev){
	$("#imageSelectedDiv").hide();
	$('#imageSelected').text("");
	$('#file-input-data').val('');
	
});

$("#changePasswordLi").click(function(){
	
	$("#change_password").modal('show');
});

$("#editProfileLi").click(function(){
	
	
	   var messageContextPath = location.protocol + '//'
		+ location.host + location.pathname + '/userDetails';

		$.ajax({
			type : 'GET',
			url : messageContextPath,
			success : function(data) {
				
				if(data!=null){
					$('#firstNameEdit').val(data.firstName);
					$('#lastNameEdit').val(data.lastName);
					$('#userNameEdit').val(data.userName);
					$('#emailIdEdit').val(data.email);
					
					if(data.gender=="male"){
						$("#maleRadioBtn_Edit").prop('checked','true');
					}
					if(data.gender=="female"){
						$("#femaleRadioBtn_Edit").prop('checked','true');
					}
					
					$("#editprofilePic").attr("src",urlAttachemnt+ "/"+ data.attachment.id);
				}
			
				
			},
			error : function(xhr, error) {
				console.log(xhr, error);
			}
		});
	
	//$("#userEditProfileDiv").modal('show');
});


function readURLEditProfile(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

       
        reader.onload = function(e) {
            $('#editprofilePic')
                .attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);
    }
}

function readURL111(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function(e) {
            $('#blah')
                .attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);
    }
} 



//update user profile Deatils
$("#updateProfileBtn").click(function(){
	
	var image = $('#file-editprofile')[0].files[0];
	var message = {};
	var sucess = true;

	
	/*if (image == undefined ){
		$('#errorMessage').html(" Profile picture Required...!!!");
		sucess = false;
	}*/
		
	if ($('#firstNameEdit').val() == "") {
	
			$('#errorMessageEdit').html(" First Name Required...!!!");
			sucess = false;

	} else {
		message.firstName = $('#firstNameEdit').val();
	}

	if ($('#lastNameEdit').val() == "") {

		$('#errorMessageEdit').html(" LastName Required...!!!");
		sucess = false;

	} else {
		message.lastName = $('#lastNameEdit').val();
	}

	if ($("#maleRadioBtn_Edit").prop("checked")) {
		message.gender = "male";
	} else {
		message.gender = "female";
	}
	
	
	
	if (sucess) {

		var uploadAttachment = new FormData();
		uploadAttachment.append("file", image);
		uploadAttachment.append('threadPid', JSON.stringify(message));

		var messageContextPath = location.protocol + '//' + location.host
				+ location.pathname+ '/updateProfile';;
		$.ajax({
			method : 'POST',
			url : messageContextPath,
			data : uploadAttachment,
			cache : false,
			contentType : false,
			processData : false,
			success : function(data) {

				$('#errorMessageEdit').html(data.message);

				setTimeout(function() {
					if(data.message=="Saved"){
						$('#userEditProfileDiv').modal('hide');
					}
					$('#errorMessageEdit').html("");
				}, 1000);

			},
			error : function(xhr, error) {
				console.log(xhr, error);
			}

		});

		$('#errorMessageEdit').html(" ");
	} else {
		setTimeout(function() {
			$('#errorMessageEdit').html("");
		}, 3000);
	}
	
});




//search by contacts name 
function searchContact(ev){
	var searchItem = $('#searchContactList').val().toLowerCase().trim();
	$("#chat_list_searchResult").empty();
	

		if ($('#searchContactList').val() != null && $('#searchContactList').val() != '') {

		$('.userNameDiv').each(function(index) {

			if ($(this).text().toLowerCase().indexOf(searchItem) != -1) {

				var matchedLiData = $(this).closest('li')[0].outerHTML;
				$('#chat_list_searchResult').append(matchedLiData);
			//	$('#chat_list_searchResult').find('li').css("background-color ","green");
				$('#chat_list_search').hide();
				$('#chat_list_searchResult').find('li').css('background-color','rgba(0, 0, 0, 0.05)'); 

			}
		});
	}else{
		$('#chat_list_search').show();
	}
}



$('#voiceToTextstop').click(function(ev){
	
	$('#voiceToTextstop').hide();
	$("#voiceToTextStart").show();

});
// select Member to Group
function selectMemberToGroup(ev){
	var  imageSelec = groupImage;

	var attachmentIdSel=$(ev).attr('data-attachment');
	var userId=$(ev).attr('id');
	
	$(ev).remove();
	var imgurl = "attachments/" +attachmentIdSel;
	
	
	//'+urlAttachemnt+ "/"+ usersObj.attachment.id+'
	
	var selected='<div id='+userId+' class="members_img selectedMenbersAdd"><img  src="'+imgurl+'" class="img-circle img-sm m-r-10 mCS_img_loaded"></div>';
	
	
	$('.selct_members_show_div').append(selected);
}

//save Group
$('#saveGroupBtn').click(function(ev){
	
	var selectedUsers=[];
	$(".selectedMenbersAdd").each(function() {
	    
		var userId=$(this).attr('id');
		selectedUsers.push(userId);
	});
	
    var grpOjb={}
	
	grpOjb.groupName= $('#groupNameHead').html();
    grpOjb.discription=$('#discrpOfGrp').val();
    grpOjb.selectedUsers=selectedUsers;
    
	var uploadAttachment = new FormData();
	uploadAttachment.append("file", groupImage);
	uploadAttachment.append('threadPid', JSON.stringify(grpOjb));
	
	
	var messageContextPath = location.protocol + '//'
	+ location.host + location.pathname + '/saveGroup';

	$.ajax({
		type : 'POST',
		url : messageContextPath,
		data : uploadAttachment,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			console.log(data);
			if(data.message=="Saved"){
				$('#new_group_members').modal('hide');
			}
		
		},
		error : function(xhr, error) {
			console.log(xhr, error);
		}
	});
	
});


function getMessageWindow(event,name, receiverUk, reciverId, image) {

	
	$("#leftnav" ).removeClass( "leftContactNavDiv" );
	$('#groupInfoIcon').hide();// hide groupInfoIcon when indv chat screen
	
	selectedChatId=reciverId;
	selectedChatImgurl=image;
	$('.contactPool').removeClass('active');
     $(event).addClass("active");
	//active
	isReciverExists = true;
	senderUk=receiverUk;
	$("#messageContentDiv").show();
	$("#footerDiv").show();
	$('#chat-wrapper').empty();
	
	$('.textarea-control').text("");
	$('#file-input-data').val('');

	isGroupMessge = false;
	isIndMessage = true;
	
	$(".image-upload").show();// file choose only visible for  indv message.

	$("#" + receiverUk).hide();
	senderImage = image;

	receiverId = reciverId;
	var message = "hey  " + name + "  welcome to Chat";

	// $('#currentChatHead').src="../resources/assets/img/profiles/0"+senderImage+".jpg";

	$("#currentChatHead").attr("src",urlAttachemnt+ "/"+ image);
	
	$(".name").text(name);

	$.ajax({
				method : 'GET',
				url : urlMessageAll + "/" + reciverId,
				success : function(data) {
					 //console.log(data);
																						 
					var imageUrl = "../resources/assets/img/profiles/0"
							+ senderImage + ".jpg";
					//var senderImage='<img src="'+ imgurls+ '">';
					
					$.map( data, function( val, key ) {
				
						/*var dateDiv='<div class="chat-row" style="text-align: center;" ><p>'+key+'</p></div>';
						$('#chat-wrapper').append(dateDiv);*/
						
						var date = new Date(key);
						var keyDate = date.getFullYear()+"-"+date.getMonth()+1+"-"+date.getDate();

						var today = new Date();
						var yesterday = today.getDate()-1;

						var yesterdayDate= today.getFullYear()+"-"+today.getMonth()+1+"-"+yesterday;
						var todayDate = today.getFullYear()+"-"+today.getMonth()+1+"-"+today.getDate();

						if(keyDate==todayDate){
						  key = "Today" ;
						}else if(keyDate==yesterdayDate){
							key = "Yesterday" ;
						}else{
							key = key;
						}
						
						var dateDiv='<div class="chat-row" style="text-align: center;" ><p>'+key+'</p></div>';
						$('#chat-wrapper').append(dateDiv);
		
						
						$.each(val,function(index, messageObj) {

									console.log(messageObj);
											var senderId = messageObj["senderId"];
												
												var mesageHrMinuts= messageObj["timeOfMessage"].split(":");
												var timeOfMessage=formatAMPM(mesageHrMinuts[0],mesageHrMinuts[1]);
												
												
											if (userId == senderId) {// our own message adding to div
												//checking for deleted messages
												if(messageObj.isDelete)
												{
													//console.log("deleted msg "+messageObj["messageContent"]);
													var message = '<div class="chat-row" id='
															+ messageObj["id"]
															+ '><img src='
															+ urlAttachemnt+ "/"+ userProfilePic
															+ ' alt=""class="img-circle img-sm pull-right"><div class="deleted_messages"><div class="message"><p class="currentMsgContentPara">'
															+ '</p></div><span></span></div><div class="date">'+timeOfMessage+'</div></div>';
													$('#chat-wrapper').append(message);
													
													$('.currentMsgContentPara').html("<i class='fa fa-trash-o' aria-hidden='true'></i> This message was deleted");
													$('.currentMsgContentPara').removeClass('currentMsgContentPara');
												}
												else
												{
													if (messageObj.attachment) {
														var fileExt=messageObj["attachmentName"].split(".");
														var attachmentId = messageObj.attachemnt;

														var imgurls = urlAttachemnt
																+ "/"
																+ attachmentId;

													
														if (messageObj.attachmetExt == "image/jpeg" || messageObj.attachmetExt == "image/png") {

															var messageDiv = '<div class="chat-row" id='
																+ messageObj["id"]
																+ ' ><img src='
																+  urlAttachemnt+ "/"+ userProfilePic
																+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
																+ '<img src="'
																+ imgurls
																+ '"></div><div><p></p></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																+ messageObj["messageContent"]
																+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																+ attachmentId
																+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
														$('#chat-wrapper').append(
																messageDiv);
														
															
															
															
														
														} else if (messageObj.attachmetExt == "application/pdf") {

															var messageDiv = '<div class="chat-row" id='
																	+ messageObj["id"]
																	+ ' ><img src='
																	+ urlAttachemnt+ "/"+ userProfilePic
																	+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
																	+ '<img src="../resources/assets/static-image/download.jpg"></a></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																	+ messageObj["messageContent"]
																	+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																	+ attachmentId
																	+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
															$('#chat-wrapper')
																	.append(
																			messageDiv);

														}else if (messageObj.attachmetExt == ".mp3") {
														
																var messageDiv = '<div class="chat-row" id='
																	+ messageObj["id"]
															    	+ ' ><img src='
															        + urlAttachemnt+ "/"+ userProfilePic
															        + ' alt=""class="img-circle img-sm pull-right"><div class="bubble">'
																	+ '<div style="text-align: right;"><audio class="audio1" controls=""><source src="'+ imgurls+'" type="audio/mpeg"></audio></div>'
																	+ '<div class="date">'+timeOfMessage+'</div></div></div>';
															$('#chat-wrapper')
																	.append(messageDiv);
														}
														else if (messageObj.attachmetExt == "application/msword" || messageObj.attachmetExt == "application/vnd.openxmlformats-officedocument.wordprocessingml.document") {

															var messageDiv = '<div class="chat-row" id='
																	+ messageObj["id"]
																	+ ' ><img src='
																	+ urlAttachemnt+ "/"+ userProfilePic
																	+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
																	+ '<img src="../resources/assets/static-image/microSoftWord.jpg"></a></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																	+ messageObj["messageContent"]
																	+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																	+ attachmentId
																	+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
															$('#chat-wrapper')
																	.append(
																			messageDiv);

														}
														else if (messageObj.attachmetExt == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" || messageObj.attachmetExt =="application/vnd.ms-excel") {

															var messageDiv = '<div class="chat-row" id='
																	+ messageObj["id"]
																	+ ' ><img src='
																	+ urlAttachemnt+ "/"+ userProfilePic
																	+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
																	+ '<img src="../resources/assets/static-image/xls.jpg"></a></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																	+ messageObj["messageContent"]
																	+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																	+ attachmentId
																	+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
															$('#chat-wrapper')
																	.append(
																			messageDiv);

														}
														/*else if (messageObj.attachmetExt ==  messageObj.attachmetExt == "application/vnd.ms-powerpoint"
															|| messageObj.attachmetExt == "application/vnd.openxmlformats-officedocument.presentationml.presentation" ) {*/
														
														else if(fileExt[1]=="ppt" || fileExt[1]=="pptx"){

															var messageDiv = '<div class="chat-row" id='
																	+ messageObj["id"]
																	+ ' ><img src='
																	+ urlAttachemnt+ "/"+ userProfilePic
																	+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
																	+ '<img src="../resources/assets/static-image/ppt.jpg"></a></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																	+ messageObj["messageContent"]
																	+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																	+ attachmentId
																	+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
															$('#chat-wrapper')
																	.append(
																			messageDiv);

														}
														
														else if (messageObj.attachmetExt == "audio/mp3") {

															var messageDiv = '<div class="chat-row" id='
																	+ messageObj["id"]
																	+ ' ><img src='
																	+ urlAttachemnt+ "/"+ userProfilePic
																	+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
																	+ '<img src="../resources/assets/static-image/mp3.jpg"></a></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																	+ messageObj["messageContent"]
																	+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																	+ attachmentId
																	+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
															$('#chat-wrapper')
																	.append(
																			messageDiv);

														}
														else if (messageObj.attachmetExt == "video/mp4") {

															var messageDiv = '<div class="chat-row" id='
																	+ messageObj["id"]
																	+ ' ><img src='
																	+ urlAttachemnt+ "/"+ userProfilePic
																	+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
																	+ '<img src="../resources/assets/static-image/mp4.jpg"></a></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																	+ messageObj["messageContent"]
																	+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																	+ attachmentId
																	+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
															$('#chat-wrapper')
																	.append(
																			messageDiv);

														}
														else if (messageObj.attachmetExt == "text/plain") {

															var messageDiv = '<div class="chat-row" id='
																	+ messageObj["id"]
																	+ ' ><img src='
																	+ urlAttachemnt+ "/"+ userProfilePic
																	+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
																	+ '<img src="../resources/assets/static-image/txt.jpg"></a></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																	+ messageObj["messageContent"]
																	+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																	+ attachmentId
																	+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
															$('#chat-wrapper')
																	.append(
																			messageDiv);

														}
														 else{
																var messageDiv = '<div class="chat-row" id='+ messageObj["id"]	+ '  ><img src='
																	+ 	urlAttachemnt+ "/"+userProfilePic
																	+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
																	+ '<img src="../resources/assets/static-image/file-icon.jpg"></a></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																	+  messageObj["messageContent"]
																	+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																	+ attachmentId
																	+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
															$('#chat-wrapper')
																	.append(
																			messageDiv);
															}
														

													} else {
														var message = '<div class="chat-row" id='
										
															+ messageObj["id"]
																+ '><img src='
																+ urlAttachemnt+ "/"+ userProfilePic
																+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="message"><p class="currentMsgContentPara">'
																+ '</p></div><span></span></div><div class="date">'+timeOfMessage+'</div></div>';
														$('#chat-wrapper').append(
																message);
														
														$('.currentMsgContentPara').html(messageObj["messageContent"]);
														$('.currentMsgContentPara').removeClass('currentMsgContentPara');
													}
												}
												
/////////////////////////////////////////////
												

											} else {
												

												if(messageObj.isDelete)
												{
													var messageDiv = '<div class="chat-row response" id='
															+ messageObj["id"]
															+ ' ><img src='
															+ urlAttachemnt+ "/"+ image
															+ ' alt=""class="img-circle img-sm pull-left"><div class="deleted_messages_response"><div class="message"><p class="currentMsgContentPara">'
															+ '</p></div><span></span></div><div class="date lft_date">'
															+timeOfMessage+'</div></div>';
													$('#chat-wrapper').append(
															messageDiv);
													$('.currentMsgContentPara').html("<i class='fa fa-trash-o' aria-hidden='true'></i> This message was deleted");

													$('.currentMsgContentPara').removeClass('currentMsgContentPara');
												}
												else
												{
													if (messageObj.attachment) { // if attachemnt is exist
														var fileExt=messageObj["attachmentName"].split(".");
														console.log(fileExt);
														var attachmentId = messageObj.attachemnt;

														var imgurls = urlAttachemnt
																+ "/"
																+ attachmentId;
														if (messageObj.attachmetExt == "image/jpeg" || messageObj.attachmetExt == "image/png") {
															var messageDiv = '<div class="chat-row response" id='
																+ messageObj["id"]
																+ ' ><img src='
																+ urlAttachemnt+ "/"+ image
																+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
																+ '<img src="'
																+ imgurls
																+ '"></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																+ messageObj["messageContent"]
																+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																+ attachmentId
																+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
														$('#chat-wrapper').append(
																messageDiv);
														}else if (messageObj.attachmetExt == ".mp3") {
														
															
															var messageDiv = '<div class="chat-row response" id='
																+ messageObj["id"]
																+ ' ><img src='
															    + urlAttachemnt+ "/"+ image
															    + ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response">'
																+ '<audio class="audio1" controls=""><source src="'+ imgurls+'" type="audio/mpeg"></audio>'
																+ '<div style="text-align: left; font-size: smaller;">'+timeOfMessage+'</div></div></div>';
														$('#chat-wrapper')
																.append(messageDiv);
														
														
														}
														 else if (messageObj.attachmetExt == "application/pdf") {

																var messageDiv = '<div class="chat-row response" id='
																		+ messageObj["id"]
																		+ ' ><img src='
																		+ urlAttachemnt+ "/"+ image
																		+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
																		+ '<img src="../resources/assets/static-image/download.jpg"></a></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																		+ messageObj["messageContent"]
																		+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																		+ attachmentId
																		+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
																$('#chat-wrapper')
																		.append(
																				messageDiv);

															}
														 else if (messageObj.attachmetExt == "application/msword" ||messageObj.attachmetExt == "application/vnd.openxmlformats-officedocument.wordprocessingml.document") {

																var messageDiv = '<div class="chat-row response" id='
																		+ messageObj["id"]
																		+ ' ><img src='
																		+ urlAttachemnt+ "/"+ image
																		+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
																		+ '<img src="../resources/assets/static-image/microSoftWord.jpg"></a></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																		+ messageObj["messageContent"]
																		+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																		+ attachmentId
																		+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
																$('#chat-wrapper')
																		.append(
																				messageDiv);

															}
														 else if (messageObj.attachmetExt == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" || messageObj.attachmetExt =="application/vnd.ms-excel") {

																var messageDiv = '<div class="chat-row response" id='
																		+ messageObj["id"]
																		+ ' ><img src='
																		+ urlAttachemnt+ "/"+ image
																		+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
																		+ '<img src="../resources/assets/static-image/xls.jpg"></a></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																		+ messageObj["messageContent"]
																		+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																		+ attachmentId
																		+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
																$('#chat-wrapper')
																		.append(
																				messageDiv);

															}
														
														
														/* else if (messageObj.attachmetExt ==  messageObj.attachmetExt == "application/vnd.ms-powerpoint"
																|| messageObj.attachmetExt == "application/vnd.openxmlformats-officedocument.presentationml.presentation") {*/
															else if(fileExt[1]=="ppt" || fileExt[1]=="pptx"){
																var messageDiv = '<div class="chat-row response" id='
																		+ messageObj["id"]
																		+ ' ><img src='
																		+ urlAttachemnt+ "/"+ image
																		+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
																		+ '<img src="../resources/assets/static-image/ppt.jpg"></a></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																		+ messageObj["messageContent"]
																		+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																		+ attachmentId
																		+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
																$('#chat-wrapper')
																		.append(
																				messageDiv);

															}
														 else if (messageObj.attachmetExt == "audio/mp3") {

																var messageDiv = '<div class="chat-row response" id='
																		+ messageObj["id"]
																		+ ' ><img src='
																		+ urlAttachemnt+ "/"+ image
																		+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
																		+ '<img src="../resources/assets/static-image/mp3.jpg"></a></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																		+ messageObj["messageContent"]
																		+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																		+ attachmentId
																		+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
																$('#chat-wrapper')
																		.append(
																				messageDiv);

															}
														 else if (messageObj.attachmetExt == "video/mp4") {

																var messageDiv = '<div class="chat-row response" id='
																		+ messageObj["id"]
																		+ ' ><img src='
																		+ urlAttachemnt+ "/"+ image
																		+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
																		+ '<img src="../resources/assets/static-image/mp4.jpg"></a></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																		+ messageObj["messageContent"]
																		+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																		+ attachmentId
																		+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
																$('#chat-wrapper')
																		.append(
																				messageDiv);

															}
														 else if (messageObj.attachmetExt == "text/plain") {

																var messageDiv = '<div class="chat-row response" id='
																		+ messageObj["id"]
																		+ ' ><img src='
																		+ urlAttachemnt+ "/"+ image
																		+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
																		+ '<img src="../resources/assets/static-image/txt.jpg"></a></div><div><p>'+messageObj["attachmentName"]+'</p></div><div class="message"><p>'
																		+ messageObj["messageContent"]
																		+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																		+ attachmentId
																		+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
																$('#chat-wrapper')
																		.append(
																				messageDiv);

															}
														 else{
																var messageDiv = '<div class="chat-row response" id='+ messageObj["id"]	+ '  ><img src='
																	+ 	urlAttachemnt+ "/"+image
																	+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
																	+ '<img src="../resources/assets/static-image/file-icon.jpg"></a></div><div><p>'+messageObj.attachmentName+'</p></div><div class="message"><p>'
																	+  message.messageContent
																	+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
																	+ attachmentId
																	+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
															$('#chat-wrapper')
																	.append(
																			messageDiv);
															}
														
														
												

													} else {
														// if olny text message

														var messageDiv = '<div class="chat-row response" id='
																+ messageObj["id"]
																+ ' ><img src='
																+ urlAttachemnt+ "/"+ image
																+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="message"><p class="currentMsgContentPara">'
																+ '</p></div><span></span></div><div class="date lft_date">'+timeOfMessage+'</div></div>';
														$('#chat-wrapper').append(
																messageDiv);
														
														$('.currentMsgContentPara').html(messageObj["messageContent"]);
														$('.currentMsgContentPara').removeClass('currentMsgContentPara');

													}
												
												}
/////////////////////////////////////////////
												

											}
											
						    });
					 	
						});
					
					setTimeout(function() {
						$('#chat_con_text').mCustomScrollbar("scrollTo",
								"bottom");
					}, 1500);
					
					
					
					
				},
				error : function(xhr, error) {
					console.log(xhr, error);
				}

			});
	
}



function formatAMPM(hours,minutes) {

	  var ampm = hours >= 12 ? 'pm' : 'am';
	  hours = hours % 12;
	  hours = hours ? hours : 12; // the hour '0' should be '12'
	  minutes = minutes < 10 ? '0'+minutes : minutes;
	  var strTime = hours + ':' + minutes + ' ' + ampm;
	  return strTime;
	}

function getPdf(attachementId) {
	//console.log(attachementId);
	window.location.href = urlAttachemnt + "/getpdf/" + attachementId;
}

function downloadFile(attachementId) {
	//console.log(attachementId);
	window.location.href = urlAttachemnt + "/download/" + attachementId;
}

function sendMessage() {

	// if any receiver or group is selected.
	if (isReciverExists) {
		if ($('.textarea-control').text() != "") {

			if (isIndMessage) {

				// ind message sending
				var message = {};
				message.message = $('.textarea-control').text();
				message.receiveUserId = receiverId;
				message.type = "1";

				var messageContextPath = location.protocol + '//'
						+ location.host + location.pathname;
				$.ajax({
					method : 'POST',
					url : messageContextPath,
					contentType : "application/json; charset=utf-8",
					data : JSON.stringify(message),
					cache : false,
					contentType : false,
					processData : false,
					success : function(data) {
						console.log(data);

						createmessageview(data, message.receiveUserId, 1);
						$('.textarea-control').text("");
						// $("#message-" + id).val('');
					},
					error : function(xhr, error) {
						console.log(xhr, error);
					}

				});
			} else {
				// group message sending

				var message = {};

				message.message = $('.textarea-control').val();
				message.receiveUserId = receiverId;
				message.senderId = userId;

				message.groupId = groupId;
				message.senderName = userFullName;

				var messageContextPath = location.protocol + '//'
						+ location.host + location.pathname + '/sample';

				$.ajax({
					method : 'POST',
					url : messageContextPath,
					contentType : "application/json; charset=utf-8",
					data : JSON.stringify(message),
					success : function(data) {

						createmessageviewGrp(data, message.receiveUserId, 1);
						$('.textarea-control').text("");
					},
					error : function(xhr, error) {
						console.log(xhr, error);
					}

				});

			}

		}
	}

}

function sendPic(mode) {

	var image = $('#file-input-data')[0].files[0];
	//var text = $('.textarea-control').text().trim();


	/*const fileAsBlob = new Blob([$('#file-input-data')[0].files[0]]);
	console.log(fileAsBlob);
	
	const blobAsFile = new File([fileAsBlob], image.name, {type: image.type, lastModified: image.lastModifiedDate});
	console.log(blobAsFile);*/
	

/*	var fileReader = new FileReader();
	 fileReader.readAsArrayBuffer($('#file-input-data')[0].files[0]);
	 var imageData = fileReader.result;
	console.log(imageData);*/
	
	//var text= $('#textBoxMessage').val();
	if(mode=="enter"){
		text= $('.emoji-wysiwyg-editor').html().trim().replace(/(<br>)*/g,"");
	}else{
		
		text= $('.emoji-wysiwyg-editor').html().trim();
	}

	if (image != undefined || text != "") {

		if (isIndMessage) {
			
			var message = {};
			message.message = text;
			message.receiveUserId = selectedChatId;
			message.type = "1";
			message.status = "message_ind";
		    message.senderUserId=userId;
		    message.senderUK=loginUserUk;
		 
		    webSocket.send(JSON.stringify(message));
		    
			$('.textarea-control').text("");
			$('#file-input-data').val('');
			$("#imageSelectedDiv").hide();
			$('#imageSelected').text("");
			/*
			var message = {};
			message.message = text;
			message.receiveUserId = receiverId;
			message.type = "1";

			var uploadAttachment = new FormData();
			uploadAttachment.append("file", image);
			uploadAttachment.append('threadPid', JSON.stringify(message));
			// uploadAttachment.append('receiverId',receiverId)
			
			var messageContextPath = location.protocol + '//'
			+ location.host + location.pathname + '/upload-attachment';

			$.ajax({
				type : 'POST',
				url : messageContextPath,
				data : uploadAttachment,
				cache : false,
				contentType : false,
				processData : false,
				success : function(data) {
					//console.log(data);
					createmessageview(data, message.receiveUserId, 1);
					$('.textarea-control').text("");

					$('#file-input-data').val('');
					$("#imageSelectedDiv").hide();
					$('#imageSelected').text("");
				},
				error : function(xhr, error) {
					console.log(xhr, error);
				}
			});
		*/}else{

			// group message sending
			
			var message = {};
			message.message = text;
			message.receiveUserId = selectedChatId;
			message.type = "1";
			message.status = "groupMessage";
		    message.senderUserId=userId;
		    message.senderUK=loginUserUk;
		 
		    webSocket.send(JSON.stringify(message));
		    
			$('.textarea-control').text("");
			$('#file-input-data').val('');
			$("#imageSelectedDiv").hide();
			$('#imageSelected').text("");
			
			
			
			/*var message = {};
			var a1=$('.textarea-control').find('input').val();
			
			var a=$('.textarea-control').find('input').text();

			message.message = text;
			
			//message.message = $('.textarea-control').find('input').val();
			
			
			message.receiveUserId = receiverId;
			message.senderId = userId;

			message.groupId = groupId;
			message.senderName = userFullName;
			
			
			var uploadAttachment = new FormData();
			uploadAttachment.append("file", image);
			uploadAttachment.append('threadPid', JSON.stringify(message));

			var messageContextPath = location.protocol + '//'
					+ location.host + location.pathname + '/sample';

			$.ajax({
				type : 'POST',
				url : messageContextPath,
				data : uploadAttachment,
				cache : false,
				contentType : false,
				processData : false,
				success : function(data) {
						//console.log("Saved Grp Reposnse"+data)
					createmessageviewGrp(data, message.receiveUserId, 1);
					$('.textarea-control').text("");
					$('#file-input-data').val('');
					$("#imageSelectedDiv").hide();
					$('#imageSelected').text("");
				},
				error : function(xhr, error) {
					console.log(xhr, error);
				}

			});*/

		}
	}

}
function logout() {

	//scoket push to save user online status And inform Friends List to user logout
	var message_data = {};
	message_data.senderUserId=userId;
	message_data.status = "logout";
	webSocket.send(JSON.stringify(message_data));
	
	window.location.href = '../logout';

}

function voiceSaveMP3() {
	

	if (isIndMessage) {
		
		var message = {};
		message.message = $("#hidenInputMp3").val();
		message.receiveUserId = receiverId;
		message.type = "1";
	
		var messageContextPath1 = location.protocol + '//'
		+ location.host + location.pathname + '/saveMp3';
		
		$.ajax({
			method : 'POST',
			url : messageContextPath1,
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(message),
			success : function(data) {
				createmessageview(data);
				$('#myModal').modal('hide');
				$("#hidenInputMp3").val("");
				
				$('#voiceSendButton').hide();
				
			},
			error : function(xhr, error) {
				onError(xhr, error);
			}
		});
	}else{
		
		var message = {};
		message.message = $("#hidenInputMp3").val();
		//message.receiveUserId = receiverId;
		//message.type = "1";
		message.receiveUserId = receiverId;
		message.senderId = userId;

		message.groupId = groupId;
		message.senderName = userFullName;
	
		var messageContextPath1 = location.protocol + '//'
		+ location.host + location.pathname + '/saveToGroupMp3';
		
		$.ajax({
			method : 'POST',
			url : messageContextPath1,
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(message),
			success : function(data) {
				createmessageviewGrp(data);
				$('#myModal').modal('hide');
				$("#hidenInputMp3").val("");
				
				$('#voiceSendButton').hide();
				
			},
			error : function(xhr, error) {
				onError(xhr, error);
			}
		});
	
	}
	
	
}

function addUserFromInvite(ev){
	var userIdInvite=$(ev).attr('data-userInvId');
	  if(userIdInvite!= ""){
		   
		   // if login user not equal to
		   
		   var messageContextPath = location.protocol + '//'
			+ location.host + location.pathname + '/sendFrndRequest';//inviteUser

			$.ajax({
				type : 'POST',
				url : messageContextPath,
				data :{userIdInvite:userIdInvite}, 
				success : function(data) {
					console.log(data);
					if(data.message=="sucess"){
						$('#addFriendStatus').html("Request Send.");
						$('#addUserBtnInvit').hide();
						
						//$('#invite_user').modal('hide');
						
						setTimeout(function() {
							$('#invite_user').modal('hide');
							$('#errorMessageInInvite').html(" ");
				  		}, 3000);

					}
					
				},
				error : function(xhr, error) {
					console.log(xhr, error);
				}
			});
			
			
	   }
}

function getMessageWindowGroup(event,name, groupUKId, gId,attachmentId) {
	
	$('.contactPool').removeClass('active');
    $(event).addClass("active");
    
	isReciverExists = true;
	selectedChatId=gId;
	//$("#file-input-data").hide();
	$("#footerDiv").show();//display the  footer
	$("#messageContentDiv").show();//display the message content box
	
	$('#groupInfo').show();
	$('#groupInfoIcon').show();
	
	$('#groupInfo').attr('data-groupId', gId);
	
	$('#chat-wrapper').empty();
	//$(".image-upload").hide();
	
	$("#" + gId).hide();

	isGroupMessge = true;
	isIndMessage = false;

	groupId = gId;//groupID
	receiverId = gId;//GroupUKeyIDfor message sending 

	var message = "hey  " + name + "  welcome to Chat";

	// $('#currentChatHead').src="../resources/assets/img/profiles/0"+senderImage+".jpg";
	$("#currentChatHead").attr("src",urlAttachemnt+ "/"+ attachmentId);

	$(".name").text(name);

	$.ajax({
				method : 'GET',
				url : urlMessageGroupAll + "/" + groupId,
				success : function(data) {
					console.log(data);
					var imageUrl = "../resources/assets/img/profiles/group.jpg";

					if (data != "") {
						
						$.map( data, function( val, key ) {
							
						/*	var dateDiv='<div class="chat-row" style="text-align: center;" ><p>'+key+'</p></div>';
							$('#chat-wrapper').append(dateDiv);*/
							
							var date = new Date(key);
							var keyDate = date.getFullYear()+"-"+date.getMonth()+1+"-"+date.getDate();

							var today = new Date();
							var yesterday = today.getDate()-1;

							var yesterdayDate= today.getFullYear()+"-"+today.getMonth()+1+"-"+yesterday;
							var todayDate = today.getFullYear()+"-"+today.getMonth()+1+"-"+today.getDate();

							if(keyDate==todayDate){
							  key = "Today" ;
							}else if(keyDate==yesterdayDate){
								key = "Yesterday" ;
							}else{
								key = key;
							}
							
							var dateDiv='<div class="chat-row" style="text-align: center;" ><p>'+key+'</p></div>';
							$('#chat-wrapper').append(dateDiv);
							
							$.each(val,function(index, messageObj) {
								
								console.log(messageObj);

							//	$.each(messageObj1,function(index, messageObj) {		
								
								var senderId = messageObj["senderId"];
								
								var mesageHrMinuts= messageObj["timeOfMessage"].split(":");
								var timeOfMessage=formatAMPM(mesageHrMinuts[0],mesageHrMinuts[1]);
								
								if(messageObj.messageType==4){
									
									var messageInfo='<div style="text-align:center;margin-bottom: 10px;">' +messageObj["messageContent"]+ '</div>';
									$('#chat-wrapper').append(messageInfo);
								}else{

								if (userId == senderId) {
									
									if (messageObj.isAttachment) { // if attachemnt is exist
										var attachmentId = messageObj.attachemnt.id;

										var imgurl = urlAttachemnt + "/" + attachmentId;
										
										if (messageObj.attachemnt.extension == "image/jpeg" || messageObj.attachemnt.extension == "image/png") {
											var messageDiv = '<div class="chat-row " style="width: 30%;"><img src='
												+ imageUrl
												+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div  class="divImage">'
												+ '<img  src="'
												+ imgurl
												+ '"></div><div class="message"><p>'
												+ messageObj.messageContent
												+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
												+ attachmentId
												+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div></div></div>';
										    $('#chat-wrapper').append(messageDiv);
										}else if (messageObj.attachemnt.extension == ".mp3") {
											
											var messageDiv = '<div class="chat-row " ><img src='
										        + imageUrl
										        + ' alt=""class="img-circle img-sm pull-right"><div class="bubble">'
												+ '<div style="text-align: left;"><audio class="audio1" controls=""><source src="'+ imgurl+'" type="audio/mpeg"></audio></div>'
												+ '<div>'+timeOfMessage+'</div></div></div>';
										$('#chat-wrapper')
												.append(messageDiv);
										}

										

									}else{
										var message = '<div class="chat-row" id='
											+ messageObj["id"]
											+ '><div class="bubble"><div class="message"><p class="currentMsgContentPara">'
											+ '</p></div></div><div class="date">'+timeOfMessage+'</div></div>';
									$('#chat-wrapper').append(
											message);
									
									console.log(messageObj["messageContent"]);
									$('.currentMsgContentPara').html(messageObj["messageContent"]);
									$('.currentMsgContentPara').removeClass('currentMsgContentPara');

									}
									
								
								} else {

									/*var messageDiv = '<div class="chat-row response" id='
											+ messageObj["id"]
											+ '><img src='
											+ imageUrl
											+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="message"> <b>'
											+ messageObj["senderName"]
											+ '</b><p>'
											+ messageObj["messageContent"]
											+ '</p></div><div class="date">'+timeOfMessage+'</div></div></div>';
									$('#chat-wrapper').append(
											messageDiv);*/
									

									
									/*var mesageHrMinuts= message["timeOfMessage"].split(":");
									var timeOfMessage=formatAMPM(mesageHrMinuts[0],mesageHrMinuts[1]);*/
									
									if (messageObj.isAttachment) { // if attachemnt is exist
										var attachmentId = messageObj.attachemnt.id;

										var imgurl = urlAttachemnt + "/" + attachmentId;
										
										if (messageObj.attachemnt.extension == "image/jpeg" || messageObj.attachemnt.extension == "image/png") {
											var messageDiv = '<div class="chat-row response" style="width: 30%;"><img src='
												+ imageUrl
												+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div  class="divImage">'
												+ '<img src="'
												+ imgurl
												+ '"></div><div class="message"><p>'
												+ messageObj.messageContent
												+ '</p></div> <div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
												+ attachmentId
												+ '\')" aria-hidden="true"></i> </div><div class="date">'+timeOfMessage+'</div><div></div>';
										    $('#chat-wrapper').append(messageDiv);
										}else if (messageObj.attachemnt.extension == ".mp3") {
											
											var messageDiv = '<div class="chat-row response" ><img src='
										        + imageUrl
										        + ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response">'
												+ '<div style="text-align: left;"><audio class="audio1" controls=""><source src="'+ imgurl+'" type="audio/mpeg"></audio></div>'
												+ '<div>'+timeOfMessage+'</div></div></div>';
										$('#chat-wrapper')
												.append(messageDiv);
										}

										

									} else{
									
										var messageDiv = '<div class="chat-row response"><div class="bubble_response"><div class="message"><b>'
											+ messageObj.senderName + '</b><p class="currentMsgContentPara">' 
											+ '</p></div></div><div class="date lft_date">'+timeOfMessage+'</div></div>';
									$('#chat-wrapper').append(messageDiv);
									
									$('.currentMsgContentPara').html(messageObj["messageContent"]);
									$('.currentMsgContentPara').removeClass('currentMsgContentPara');
									}
									
							}
						}
								
			//});
								
					});
			
							
						});
						
						
					
						
						setTimeout(function() {
							$('#chat_con_text').mCustomScrollbar("scrollTo",
									"bottom");
						}, 1500);
						
						
						
						
					}

				},
				error : function(xhr, error) {
					console.log(xhr, error);
				}

			});

}

function scrollToBottom(id){
	   var div = document.getElementById(id);
	   div.scrollTop = div.scrollHeight - div.clientHeight;
	}

function createmessageview(message, id, align) {
	//var imageUrl = "../resources/assets/img/profiles/0" + senderImage + ".jpg";

	if (align == 2) {// response chat upending
		var mesageHrMinuts= message["timeOfMessage"].split(":");
		var timeOfMessage=formatAMPM(mesageHrMinuts[0],mesageHrMinuts[1]);
		console.log("####"+message);
		
		if (message.attachment) { // if attachemnt is exist
			var attachmentId = message.attachemnt;
			var fileExt=messageObj.attachmentName.split(".");
			var imgurl = urlAttachemnt + "/" + attachmentId;
			
			if (message.attachmetExt == "image/jpeg" || message.attachmetExt == "image/png") {
				var messageDiv = '<div class="chat-row response" style="width: 30%;"><img src='
					+ 	urlAttachemnt+ "/"+selectedChatImgurl
					+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div  class="divImage">'
					+ '<img src="'
					+ imgurl
					+ '"></div><div><p>'+messageObj.attachmentName+'</p></div><div class="message"><p>'
					+ message.messageContent
					+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
					+ attachmentId
					+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div></div></div>';
			    $('#chat-wrapper').append(messageDiv);
			}else if (message.attachmetExt == ".mp3") {
				
				var messageDiv = '<div class="chat-row response" ><img src='
			        + 	urlAttachemnt+ "/"+selectedChatImgurl
			        + ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response">'
					+ '<div style="text-align: left;"><audio class="audio1" controls=""><source src="'+ imgurl+'" type="audio/mpeg"></audio></div>'
					+ '<div>'+timeOfMessage+'</div></div></div>';
			$('#chat-wrapper')
					.append(messageDiv);
			} else if (message.attachmetExt == "application/pdf") {

				var messageDiv = '<div class="chat-row response"  ><img src='
						+ 	urlAttachemnt+ "/"+selectedChatImgurl
						+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
						+ '<img src="../resources/assets/static-image/download.jpg"></a></div><div><p>'+messageObj.attachmentName+'</p></div><div class="message"><p>'
						+  message.messageContent
						+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
						+ attachmentId
						+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
				$('#chat-wrapper')
						.append(
								messageDiv);

			}
			else if (message.attachmetExt == "application/msword" || messageObj.attachmetExt == "application/vnd.openxmlformats-officedocument.wordprocessingml.document") {

				var messageDiv = '<div class="chat-row response"  ><img src='
						+ 	urlAttachemnt+ "/"+selectedChatImgurl
						+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
						+ '<img src="../resources/assets/static-image/microSoftWord.jpg"></a></div><div><p>'+messageObj.attachmentName+'</p></div><div class="message"><p>'
						+  message.messageContent
						+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
						+ attachmentId
						+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
				$('#chat-wrapper')
						.append(
								messageDiv);

			}
			else if (message.attachmetExt == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" || message.attachmetExt =="application/vnd.ms-excel") {

				var messageDiv = '<div class="chat-row response"  ><img src='
						+ 	urlAttachemnt+ "/"+selectedChatImgurl
						+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
						+ '<img src="../resources/assets/static-image/xls.jpg"></a></div><div><p>'+messageObj.attachmentName+'</p></div><div class="message"><p>'
						+  message.messageContent
						+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
						+ attachmentId
						+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
				$('#chat-wrapper')
						.append(
								messageDiv);

			}
		/*	else if (message.attachmetExt ==  message.attachmetExt == "application/vnd.ms-powerpoint"
				|| message.attachmetExt == "application/vnd.openxmlformats-officedocument.presentationml.presentation") {*/
			else if(fileExt[1]=="ppt" || fileExt[1]=="pptx"){
				var messageDiv = '<div class="chat-row response"  ><img src='
						+ 	urlAttachemnt+ "/"+selectedChatImgurl
						+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
						+ '<img src="../resources/assets/static-image/ppt.jpg"></a></div><div><p>'+messageObj.attachmentName+'</p></div><div class="message"><p>'
						+  message.messageContent
						+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
						+ attachmentId
						+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
				$('#chat-wrapper')
						.append(
								messageDiv);

			}
			else if (message.attachmetExt == "audio/mp3") {

				var messageDiv = '<div class="chat-row response"  ><img src='
						+ 	urlAttachemnt+ "/"+selectedChatImgurl
						+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
						+ '<img src="../resources/assets/static-image/mp3.jpg"></a></div><div><p>'+messageObj.attachmentName+'</p></div><div class="message"><p>'
						+  message.messageContent
						+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
						+ attachmentId
						+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
				$('#chat-wrapper')
						.append(
								messageDiv);

			}
			else if (message.attachmetExt == "video/mp4") {

				var messageDiv = '<div class="chat-row response"  ><img src='
						+ 	urlAttachemnt+ "/"+selectedChatImgurl
						+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
						+ '<img src="../resources/assets/static-image/mp4.jpg"></a></div><div><p>'+messageObj.attachmentName+'</p></div><div class="message"><p>'
						+  message.messageContent
						+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
						+ attachmentId
						+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
				$('#chat-wrapper')
						.append(
								messageDiv);

			}
			else if (message.attachmetExt == "text/plain") {

				var messageDiv = '<div class="chat-row response"  ><img src='
						+ 	urlAttachemnt+ "/"+selectedChatImgurl
						+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
						+ '<img src="../resources/assets/static-image/txt.jpg"></a></div><div><p>'+messageObj.attachmentName+'</p></div><div class="message"><p>'
						+  message.messageContent
						+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
						+ attachmentId
						+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
				$('#chat-wrapper')
						.append(
								messageDiv);

			}else{
				var messageDiv = '<div class="chat-row response"  ><img src='
					+ 	urlAttachemnt+ "/"+selectedChatImgurl
					+ ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="divImage">'
					+ '<img src="../resources/assets/static-image/file-icon.jpg"></a></div><div><p>'+messageObj.attachmentName+'</p></div><div class="message"><p>'
					+  message.messageContent
					+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
					+ attachmentId
					+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
			$('#chat-wrapper')
					.append(
							messageDiv);
			}

			

		} else {
			// if olny text message

			var messageDiv = '<div class="chat-row response" id=' + message.id + '><img src='
				+ 	urlAttachemnt+ "/"+selectedChatImgurl + ' alt=""class="img-circle img-sm pull-left"><div class="bubble_response"><div class="message"><p class="currentMsgContentPara">'
				+ '</p></div></div><div class="date lft_date">'+timeOfMessage+'</div></div>';
				$('#chat-wrapper').append(messageDiv);

				$('.currentMsgContentPara').html(message.messageContent);
				$('.currentMsgContentPara').removeClass('currentMsgContentPara');

		}

	} else {
	//	var message=message[0];
		var mesageHrMinuts= message["timeOfMessage"].split(":");
		var timeOfMessage=formatAMPM(mesageHrMinuts[0],mesageHrMinuts[1]);

		if (message.attachment) {
			var fileExt=message.attachmentName.split(".");
			var attachmentId = message.attachemnt;

			var imgurl = urlAttachemnt + "/" + attachmentId;
			
			if (message.attachmetExt == "image/jpeg"|| message.attachmetExt == "image/png") {
				var messageDiv = '<div class="chat-row" style="width: 30%;"><img src='
					+ urlAttachemnt+ "/"+ userProfilePic
					+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div  class="divImage">'
					+ '<img src="'
					+ imgurl
					+ '"></div><div><p>'+message.attachmentName+'</p></div><div class="message"><p>'
					+ message.messageContent
					+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
					+ attachmentId
					+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div></div></div>';
			$('#chat-wrapper').append(messageDiv);
			}else if (message.attachmetExt == ".mp3") {
			
				
				var messageDiv = '<div class="chat-row"><img src='
			        + urlAttachemnt+ "/"+ userProfilePic
			        + ' alt=""class="img-circle img-sm pull-right"><div class="bubble">'
					+ '<div style="text-align: left;"><audio class="audio1" controls=""><source src="'+ imgurl+'" type="audio/mpeg"></audio></div>'
					+ '<div class="date">'+timeOfMessage+'</div></div></div>';
			$('#chat-wrapper')
					.append(messageDiv);
			
			}
			else if (message.attachmetExt == "application/pdf") {

				var messageDiv = '<div class="chat-row" ><img src='
						+ urlAttachemnt+ "/"+ userProfilePic
						+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
						+ '<img src="../resources/assets/static-image/download.jpg"></a></div><div><p>'+message.attachmentName+'</p></div><div class="message"><p>'
						+  message.messageContent
						+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
						+ attachmentId
						+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
				$('#chat-wrapper')
						.append(messageDiv);

			}
			else if (message.attachmetExt == "application/msword" || message.attachmetExt == "application/vnd.openxmlformats-officedocument.wordprocessingml.document") {

				var messageDiv = '<div class="chat-row" ><img src='
						+ urlAttachemnt+ "/"+ userProfilePic
						+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
						+ '<img src="../resources/assets/static-image/microSoftWord.jpg"></a></div><div><p>'+message.attachmentName+'</p></div><div class="message"><p>'
						+  message.messageContent
						+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
						+ attachmentId
						+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
				$('#chat-wrapper')
						.append(messageDiv);

			}
			else if (message.attachmetExt == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" || message.attachmetExt =="application/vnd.ms-excel") {

				var messageDiv = '<div class="chat-row" ><img src='
						+ urlAttachemnt+ "/"+ userProfilePic
						+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
						+ '<img src="../resources/assets/static-image/xls.jpg"></a></div><div><p>'+message.attachmentName+'</p></div><div class="message"><p>'
						+  message.messageContent
						+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
						+ attachmentId
						+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
				$('#chat-wrapper')
						.append(messageDiv);

			}
		/*	else if (message.attachmetExt ==  message.attachmetExt == "application/vnd.ms-powerpoint"
				|| message.attachmetExt == "application/vnd.openxmlformats-officedocument.presentationml.presentation") {*/
			else if(fileExt[1]=="ppt" || fileExt[1]=="pptx"){
				var messageDiv = '<div class="chat-row" ><img src='
						+ urlAttachemnt+ "/"+ userProfilePic
						+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
						+ '<img src="../resources/assets/static-image/ppt.jpg"></a></div><div><p>'+message.attachmentName+'</p></div><div class="message"><p>'
						+  message.messageContent
						+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
						+ attachmentId
						+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
				$('#chat-wrapper')
						.append(messageDiv);

			}
			else if (message.attachmetExt == "audio/mp3") {

				var messageDiv = '<div class="chat-row" ><img src='
						+ urlAttachemnt+ "/"+ userProfilePic
						+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
						+ '<img src="../resources/assets/static-image/mp3.jpg"></a></div><div><p>'+message.attachmentName+'</p></div><div class="message"><p>'
						+  message.messageContent
						+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
						+ attachmentId
						+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
				$('#chat-wrapper')
						.append(messageDiv);

			}
			else if (message.attachmetExt == "video/mp4") {

				var messageDiv = '<div class="chat-row" ><img src='
						+ urlAttachemnt+ "/"+ userProfilePic
						+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
						+ '<img src="../resources/assets/static-image/mp4.jpg"></a></div><div><p>'+message.attachmentName+'</p></div><div class="message"><p>'
						+  message.messageContent
						+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
						+ attachmentId
						+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
				$('#chat-wrapper')
						.append(messageDiv);

			}
			else if (message.attachmetExt == "text/plain") {

				var messageDiv = '<div class="chat-row" ><img src='
						+ urlAttachemnt+ "/"+ userProfilePic
						+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
						+ '<img src="../resources/assets/static-image/txt.jpg"></a></div><div><p>'+message.attachmentName+'</p></div><div class="message"><p>'
						+  message.messageContent
						+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
						+ attachmentId
						+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
				$('#chat-wrapper')
						.append(messageDiv);

			}else{
				var messageDiv = '<div class="chat-row" id=' + message.id + ' ><img src='
					+ urlAttachemnt+ "/"+ userProfilePic
					+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="divImage">'
					+ '<img src="../resources/assets/static-image/file-icon.jpg"></a></div><div><p>'+message.attachmentName+'</p></div><div class="message"><p>'
					+  message.messageContent
					+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
					+ attachmentId
					+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div><span></span></div></div>';
			$('#chat-wrapper')
					.append(messageDiv);
				
			}

			

		} else {

			var messageDiv = '<div class="chat-row" id=' + message.id + '><img src='
					+ urlAttachemnt+ "/"+ userProfilePic
					+ ' alt=""class="img-circle img-sm pull-right"><div class="bubble"><div class="message"><p class="currentMsgContentPara">'
					+ '</p></div></div><div class="date">'+timeOfMessage+'</div></div>';
			$('#chat-wrapper').append(messageDiv);
			
			$('.currentMsgContentPara').html(message.messageContent);
			$('.currentMsgContentPara').removeClass('currentMsgContentPara');

		}

	}
	
	//$('#chat_con_text').mCustomScrollbar("scrollTo","bottom");
	setTimeout(function() {
		$('#chat_con_text').mCustomScrollbar("scrollTo",
				"bottom");
	}, 100);

}

function createmessageviewGrp(message, id, align) {
	//var imageUrl = "../resources/assets/img/profiles/group.jpg";

	
	if (align == 2) {
	
		var mesageHrMinuts= message["timeOfMessage"].split(":");
		var timeOfMessage=formatAMPM(mesageHrMinuts[0],mesageHrMinuts[1]);
		
		if (message.isAttachment) { // if attachemnt is exist
			var attachmentId = message.attachemnt.id;

			var imgurl = urlAttachemnt + "/" + attachmentId;
			
			if (message.attachemnt.extension == "image/jpeg" || message.attachemnt.extension == "image/png") {
				var messageDiv = '<div class="chat-row response" style="width: 30%;"><div class="bubble_response"><div  class="divImage">'
					+ '<img src="'
					+ imgurl
					+ '"></div><div class="message"><p>'
					+ message.messageContent
					+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
					+ attachmentId
					+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div></div></div>';
			    $('#chat-wrapper').append(messageDiv);
			}else if (message.attachemnt.extension == ".mp3") {
				
				var messageDiv = '<div class="chat-row response" ><div class="bubble_response">'
					+ '<div style="text-align: left;"><audio class="audio1" controls=""><source src="'+ imgurl+'" type="audio/mpeg"></audio></div>'
					+ '<div>'+timeOfMessage+'</div></div></div>';
			$('#chat-wrapper').append(messageDiv);
			
		
			}

			

		} else{
		
			var messageDiv = '<div class="chat-row response id=' + message.id + '"><div class="bubble_response"><div class="message"><b>'
				+ message.senderName + '</b><p class="currentMsgContentPara" >' 
				+ '</p></div></div><div class="date lft_date">'+timeOfMessage+'</div></div>';
		$('#chat-wrapper').append(messageDiv);
	
		$('.currentMsgContentPara').text(message.messageContent);
		$('.currentMsgContentPara').removeClass('currentMsgContentPara');
		
		}
		
		
	} else {
		
	//	var message=message[0];
		var mesageHrMinuts= message["timeOfMessage"].split(":");
		var timeOfMessage=formatAMPM(mesageHrMinuts[0],mesageHrMinuts[1]);
		
		
		if (message.isAttachment) {

			var attachmentId = message.attachemnt.id;

			var imgurl = urlAttachemnt + "/" + attachmentId;
			
			if (message.attachemnt.extension == "image/jpeg"|| message.attachemnt.extension == "image/png") {
				var messageDiv = '<div class="chat-row" style="width: 30%;"><div class="bubble"><div  class="divImage">'
					+ '<img src="'
					+ imgurl
					+ '"></div><div class="message"><p>'
					+ message.messageContent
					+ '</p></div><div class="dwnload_btn_div"><i class="fa fa-download" onClick="downloadFile(\''
					+ attachmentId
					+ '\')" aria-hidden="true"></i></div><div class="date">'+timeOfMessage+'</div></div></div>';
			$('#chat-wrapper').append(messageDiv);
			}else if (message.attachemnt.extension == ".mp3") {
			
				
				var messageDiv = '<div class="chat-row"><div class="bubble">'
					+ '<div style="text-align: left;"><audio class="audio1" controls=""><source src="'+ imgurl+'" type="audio/mpeg"></audio></div>'
					+ '<div class="date">'+timeOfMessage+'</div></div></div>';
			$('#chat-wrapper')
					.append(messageDiv);
			
			}

			

		} else {
//<img src='+ imageUrl+ ' alt=""class="img-circle img-sm pull-right">
			
		var messageDiv = '<div class="chat-row" id=' + message.id + '><div class="bubble"><div class="message"><p class="currentMsgContentPara" >'
				+ '</p></div></div><div class="date">'+timeOfMessage+'</div></div>';
		$('#chat-wrapper').append(messageDiv);
		
		$('.currentMsgContentPara').html(message.messageContent);
	
		$('.currentMsgContentPara').removeClass('currentMsgContentPara');
		}
	}
	
	setTimeout(function() {
		$('#chat_con_text').mCustomScrollbar("scrollTo",
				"bottom");
	}, 100);

}



var stompClient = null;
function connect() {
	
	//var webscoketPath = 'ws://'+ location.host + '/socket';
	//var webscoketPath = 'ws://'+ location.host + '/'  + location.pathname.split('/')[1] + '/socket';
	
    var webscoketPath ;
	
	if(location.host.split(':')[0]=="localhost"){
		webscoketPath = 'ws://'+ location.host + '/socket';
	}else{
		 webscoketPath = 'ws://'+ location.host + '/'  + location.pathname.split('/')[1] + '/socket';
	}
	openWSConnection(webscoketPath)	
	
	
	
	/*
	
	if (stompClient != null) {
		stompClient.disconnect();
	}
	//alert(contextPath);
	var socket = new SockJS(contextPath + '/tracking');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		stompClient.subscribe('/message/send/individual/' + loginUserUk,
				function(response) {
					var messageBody1 = JSON.parse(response.body);
					messageBody1.currentUser = false;
					
					 var messageBody=messageBody1[0];
					if (receiverId == messageBody.senderUserUkId) {
						createmessageview(messageBody,
								messageBody.senderUserUkId, 2);
						$("#" + messageBody.senderUserUkId).hide();

					} else {
					//when a new message recived push the contact to top of the contact pool display ballon icon with animation
						$("#" + messageBody.senderUserUkId).show();
					    $("#li-"+ messageBody.senderUserUkId).parent().
					             prepend($("#li-"+ messageBody.senderUserUkId));
					}

				});

		stompClient.subscribe('/message/send/group/' + loginUserUkGrp,
				function(response) {
					var messageBody1 = JSON.parse(response.body);
					messageBody1.currentUser = false;

					 var messageBody=messageBody1[0];
					if (userId != messageBody.senderId) {
						console.log("Grp message  recived");

						createmessageviewGrp(messageBody,
								messageBody.receiveUserId, 2);
						
						if(selectedChatId!=messageBody.groupId){
							$("#" + messageBody.groupId).show();
						    $("#li-"+ messageBody.groupId).parent().
						             prepend($("#li-"+ messageBody.groupId));
						}
					}

				});
	});*/
}

// approve friend request
function approveFriendRequest(ev){
	
	//alert($(ev).attr('data-userid'));
	
	var friendId=$(ev).attr('data-userid');
	
	
	  var messageContextPath = location.protocol + '//'
		+ location.host + location.pathname + '/acceptFriendRequest';
		
		$.ajax({
			type : 'GET',
			url : messageContextPath,
			data :{friendId:friendId}, 
			success : function(data) {
				console.log(data);
				if(data.message=="approve"){
					$('#lifreq-'+friendId).hide();
				}
			
				
			},
			error : function(xhr, error) {
				console.log(xhr, error);
			}
		});
	
	
}


//reject friend request
function rejectFriendRequest(ev){

	var friendId=$(ev).attr('data-userid');
	
	  var messageContextPath = location.protocol + '//'
		+ location.host + location.pathname + '/rejectFriendRequest';
		
		$.ajax({
			type : 'GET',
			url : messageContextPath,
			data :{friendId:friendId}, 
			success : function(data) {
				console.log(data);
				if(data.message=="rejected"){
					$('#lifreq-'+friendId).hide();
				}
			
				
			},
			error : function(xhr, error) {
				console.log(xhr, error);
			}
		});
	
	
}

function openWSConnection(webscoketPath) {
	
    var webSocketURL = null;
    webSocketURL = webscoketPath;
    console.log("openWSConnection::Connecting to: " + webSocketURL);
	
	
    try {
        webSocket = new WebSocket(webSocketURL);
        webSocket.onopen = function(openEvent) {
        	
        	   var msg =userId;
        	   var message_data = {};
        	    message_data.message = msg;
        	    message_data.status = "connect";
        	
        	webSocket.send(JSON.stringify(message_data));
        	
            console.log("WebSocket OPEN: " + JSON.stringify(openEvent, null, 4));
            
        };
        webSocket.onclose = function (closeEvent) {
        	
        	
        	var message_data = {};
        	message_data.senderUserId=userId;
        	message_data.status = "logout";
        	webSocket.send(JSON.stringify(message_data));
        	
            console.log("WebSocket CLOSE: " + JSON.stringify(closeEvent, null, 4));
           
        };
        webSocket.onerror = function (errorEvent) {
            console.log("WebSocket ERROR: " + JSON.stringify(errorEvent, null, 4));
        };
        webSocket.onmessage = function (messageEvent) {
            var wsMsg = messageEvent.data;
           
            var obj = JSON.parse(wsMsg);
            console.log(obj);
            if(obj.status=="chat"){
            	console.log("chat come :"+obj.individualChatDTO);
            	if (receiverId == obj.individualChatDTO.senderId) {
            		
            		//alert(obj.individualChatDTO.messageContent);
            		createmessageview(obj.individualChatDTO,
            				0, 2);
					$("#" + obj.individualChatDTO.senderUserUkId).hide();
            		
            	}else{
            		
            		$("#" + obj.individualChatDTO.senderUserUkId).show();
            		
    			    $("#li-"+  obj.individualChatDTO.senderUserUkId).
    			         parent().prepend($("#li-"+ obj.individualChatDTO.senderUserUkId));
            	}
            	
            	
            }else  if(obj.status=="chat-res"){
            	
            	console.log("chat Resp"+obj.individualChatDTO);
            	
            	createmessageview(obj.individualChatDTO,
        				0, 1);
            }else  if(obj.status=="groupchat"){
            	
            	console.log(obj);
            	
            	if (receiverId == obj.groupMessageDTO.groupId) {
            		
            		createmessageviewGrp(obj.groupMessageDTO,0, 2);
            		$("#" + obj.groupMessageDTO.groupId).hide();
            	}else{

            		$("#" + obj.groupMessageDTO.groupId).show();
    			    $("#li-"+ obj.groupMessageDTO.groupId).
    			         parent().prepend($("#li-"+ obj.groupMessageDTO.groupId));
            	}
            	
            	
            }else if(obj.status=="groupchat-response"){
            	if (receiverId == obj.groupMessageDTO.groupId) {
            		createmessageviewGrp(obj.groupMessageDTO,0, 1);
            	}
            	
            }else  if(obj.status=="onlineStatus"){
            	console.log(obj);
            	
            	if(obj.onlineStatus){
            		// friend user is  online
            		$("#online-"+ obj.senderUserId).show();
            	}else{
            		//friend user goes to offline
            		 $("#online-"+ obj.senderUserId).hide();
            	}
            	
            }else if(obj.status=="newUser"){
            	
            	console.log(obj);
            	
            	if(obj.userManagement!=null){
            	
            		var attachmentIdNewUser = obj.userManagement.attachment.id;

					var imgurlNew = urlAttachemnt + "/" + attachmentIdNewUser;
					
					var onclickItem="getMessageWindow(this,'"+obj.userManagement.firstName+"','"+obj.userManagement.ukMessageKey+"','"+obj.userManagement.id+"', '"+obj.userManagement.attachment.id+"')";
            		
            	var newUserLi ='<li class="list-group-item contactPool"   id="li-'+obj.userManagement.ukMessageKey+'"'
            	+' onclick="'+onclickItem+'">'
            	+' <div> <span class="pull-left"><img src="'+imgurlNew+'" alt="" '
            	+'class="img-circle img-sm m-r-10"></span> <i class="fa fa-commenting-o blink_me" id='+obj.userManagement.ukMessageKey+' style="float: right; display: none;color: blue;"></i>'
            	+' </div> <div class="list-group-item-body"> <div> <div class="list-group-item-heading userNameDiv">'+obj.userManagement.firstName+'</div> <c:if test = '+obj.userManagement.isOnline+'> '
            	+'<sapn><i class="fa fa-circle online" id="online-'+obj.userManagement.id+'" style="float: right;color:green;display:none;"></i></sapn> </c:if> <c:if test = '+obj.userManagement.isOnline+'> '
            	+'<sapn><i class="fa fa-circle online" id="online-'+obj.userManagement.id+'" style="float: right;color:green;"></i></sapn> </c:if> </div> <div class="list-group-item-text">'+obj.userManagement.userName+'</div> </div> </li>';
          
            	
            	$("#chat_list_search").prepend(newUserLi);
            	//$("#" + obj.userManagement.ukMessageKey).show();
            	
            /*    $("#li-"+  obj.individualChatDTO.senderUserUkId).
		         parent().prepend($("#li-"+ obj.individualChatDTO.senderUserUkId));*/
            	
            	}
            }else if(obj.status=="newGroup"){
            	console.log("New Group Info...!!!");
            	console.log(obj);
            	
            	var attachmentIdNewGroup = obj.group.attachment.id;
            	
            	var imgurlGroup = urlAttachemnt + "/" + attachmentIdNewGroup;
            	
            	var onlickMethod="getMessageWindowGroup(this,'"+obj.group.groupName+"','"+obj.group.groupUniqeId+"','"+obj.group.id+"','"+obj.group.attachment.id+"')";
            	
            	
            	var newGrpLi='<li class="list-group-item contactPool" "li-'+obj.group.id+'"'
            	+' onclick="'+onlickMethod+'">'
            	+' <div> <span class="pull-left"><img src="'+imgurlGroup+'"alt="" class="img-circle img-sm m-r-10"></span>'
            	+' <i class="fa fa-commenting-o blink_me" id='+obj.group.id+' style="float: right; display: none;color: blue;"></i> </div> <div class="list-group-item-body">'
            	+' <div class="list-group-item-heading">'+obj.group.groupName+'</div> </div> </li>';
            	
            	$("#chat_list_search").prepend(newGrpLi);
            	$("#" +obj.group.id).show();
            	
            }else if(obj.status=="delete_Ind_Message"){
            	console.log("Deleted Message..!!!");
            	console.log(obj);
            	
            	var messageIds=obj.messageId;

				for(i=0;i<messageIds.length;i++)
				{
				

					//removing bubble class to avoid selection again
					$("#"+messageIds[i]).find("div.bubble").addClass("deleted_messages"); 

					$("#"+messageIds[i]).find("div.bubble").removeClass("bubble"); 

					$("#"+messageIds[i]).find("p").html("<i class='fa fa-trash-o' aria-hidden='true'></i> This message was deleted");
					
				}
            	
            }else if(obj.status=="new_frnd_Req"){
              	console.log(obj);
              	$("#frq-"+obj.receiverId).show();
            	
            }
            else if(obj.status == "leftgroup"){
            	
            	console.log("left group message "+obj);
            	
            	  // check current  chat with same group
            	// push message obj.message

        		$("#" + obj.groupId).show();
			    $("#li-"+ obj.groupId). parent().prepend($("#li-"+ obj.groupId));
			    
			  
            }
            
           //
            
           /* if (receiverId == obj.senderId) {
				//createmessageview(messageBody,messageBody.senderUserUkId, 2);
            	 alert(obj.message);
				
				$("#" + obj.senderUK).hide();

			} else {
			//when a new message recived push the contact to top of the contact pool display ballon icon with animation
				$("#" + obj.senderUK).show();
			    $("#li-"+ obj.senderUK).parent().prepend($("#li-"+obj.senderUK));
			}
            */
            
            
            
            
        /*	if (receiverId == messageBody.senderUserUkId) {
				createmessageview(messageBody,
						messageBody.senderUserUkId, 2);
				$("#" + messageBody.senderUserUkId).hide();

			} else {
			//when a new message recived push the contact to top of the contact pool display ballon icon with animation
				$("#" + messageBody.senderUserUkId).show();
			    $("#li-"+ messageBody.senderUserUkId).parent().
			             prepend($("#li-"+ messageBody.senderUserUkId));
			}*/
            
            
            
          /*  var ws_activeuser = document.getElementById("active_user");
          
            if (wsMsg.indexOf("error") > 0) {
                document.getElementById("incomingMsgOutput").value += "error: " + wsMsg.error + "\r\n";
            } else {
                
              //  ws_activeuser.options.length=0
                try{
                	
                	if(wsMsg.includes("live-user=")){
                		var user_data = wsMsg.split("=");
                		console.log("LIVE-USERS "+user_data[1]);
                		var json_out = JSON.parse(user_data[1]);
                		var loop_count = 0;
                    	for(i in json_out){
                    		console.log("Data "+json_out[i].userName);
                    		ws_activeuser.options[loop_count]=new Option(json_out[i].userName, json_out[i].userId, true, false)
                    		loop_count++;
                    	}
                	}
                	else if(wsMsg.includes("user-message=")){
                		var user_data = wsMsg.split("=");
                		document.getElementById("username").value=user_data[1];
                	}
                	else{
                		document.getElementById("incomingMsgOutput").value += "message: " + wsMsg + "\r\n";
                	}
                	
                }
                catch(e){
                	console.log(e);
                }
            //
        }*/
    }
        } catch (exception) {
        console.error(exception);
    }
}

function onSendClick() {
    if (webSocket.readyState != WebSocket.OPEN) {
        console.error("webSocket is not open: " + webSocket.readyState);
        return;
    }
  // var ws_activeuser = document.getElementById("active_user").value;
    var msg =text= $('.emoji-wysiwyg-editor').html().trim();
   
   /* var message_data = {};
    message_data.message = msg;
    message_data.status = "message_ind";
    message_data.senderUK=loginUserUk;
    message_data.senderId=userId;
    message_data.receiverId=selectedChatId;*/
    
	var message = {};
	message.message = msg;
	message.receiveUserId = selectedChatId;
	message.type = "1";
	message.status = "message_ind";
    message.senderUserId=userId;
    message.senderUK=loginUserUk;
    
    
   // message_data.receverId = ws_activeuser;
    webSocket.send(JSON.stringify(message));
    
}

//adding onclick functionality to dynamicaly created bubble class divisions
$("#chat-wrapper").on('click','.bubble', function () {
	
	//finding parent with class "chat-row"
	if($(this).closest(".chat-row").hasClass("deleteMessages"))
	{
		$(this).closest(".chat-row").removeClass("deleteMessages"); 
		console.log("has class");
	}
	else
	{
		$(this).closest(".chat-row").addClass("deleteMessages");
	}
	
	if(($('.deleteMessages').length)>0)
	{
		//enabling delete icon
		$('#rightMenuIcon').css("display","none");
		$('#deleteIcon').css("display","inline-block");
		$('#selectAllIcon').css("display","inline-block");
		
	}
	else
	{
		$('#deleteIcon').css("display","none");
		$('#selectAllIcon').css("display","none");
		$('#rightMenuIcon').css("display","inline-block");
		
	}
	
});

function refreshSelection()
{
	//triggers when user click no option in delete confirm
	//code to refresh selection here
	$( ".deleteMessages" ).each(function( index ) {
		 //removing selection class
		 $(this).closest(".chat-row").removeClass("deleteMessages");
	});

	//disabling delete icon
	if(($('.deleteMessages').length)==0)
	{
		$('#deleteIcon').css("display","none");
		$('#selectAllIcon').css("display","none");
		$('#rightMenuIcon').css("display","inline-block");
		
	}
}
function deleteMessages()
{
	//pushing selected ids to array
	var messageIds=[];
	$( ".deleteMessages" ).each(function( index ) {
		  console.log( index + ": " + $( this ).attr('id') );
		  messageIds.push($( this ).attr('id'));
	});
	
	//ajax to delete 
	
	  var messageContextPath = location.protocol + '//'
		+ location.host + location.pathname + '/deleteMessage';

		$.ajax({
			type : 'GET',
			url : messageContextPath,
			data :{messageIds:messageIds}, 
			success : function(data) {
				
				if(data.status="success"){
					messageIds=data.messageid;

					for(i=0;i<messageIds.length;i++)
					{
						//removing delete selection class "deleteMessages"
						$("#"+messageIds[i]).removeClass("deleteMessages"); 

						//removing bubble class to avoid selection again
						$("#"+messageIds[i]).find("div.bubble").addClass("deleted_messages"); 

						$("#"+messageIds[i]).find("div.bubble").removeClass("bubble"); 

						$("#"+messageIds[i]).find("p").html("<i class='fa fa-trash-o' aria-hidden='true'></i> This message was deleted");
						
					}
					
					
				}
				
				
			},
			error : function(xhr, error) {
				console.log(xhr, error);
			}
		});
	
	
	//refresh the messages
	//hide modal after succssfull deletion
	$("#messageDelete").modal('hide');
}
//selecting all messages
var clickCount=1;
function selectAll()
{
	
		//adding deleteMessage class 
		if(clickCount==1)
		{
			$(".bubble").closest(".chat-row").each(function( index ) {

				$(this).addClass("deleteMessages"); 
			});

			clickCount=0;
		}
		else
		{
			$(".bubble").closest(".chat-row").each(function( index ) {

				$(this).removeClass("deleteMessages");
			});
			$('#deleteIcon').css("display","none");
			$('#selectAllIcon').css("display","none");
			$('#rightMenuIcon').css("display","inline-block");
			clickCount=1;
		}
	
}


function addFriendInSearch(ev){
	
	var userIdInvite=$(ev).attr('data-userInvId');
	  if(userIdInvite!= ""){
		   var messageContextPath = location.protocol + '//'
			+ location.host + location.pathname + '/sendFrndRequest';//inviteUser

			$.ajax({
				type : 'POST',
				url : messageContextPath,
				data :{userIdInvite:userIdInvite}, 
				success : function(data) {
					console.log(data);
					if(data.message=="sucess"){
						$('#addReqSpanInSerch').html("Request Send.");
						$('#addReqBtnInSerch').hide();
						
						

					}
					
				},
				error : function(xhr, error) {
					console.log(xhr, error);
				}
			});
			
			
	   }

	
}