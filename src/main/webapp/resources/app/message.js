
function view(id, name, type) {
	// $("#sendview").show();

	$("#message").val("");

	if (type == 1) {

		$("#messageli").empty();

		// var body = '<div class="col-sm-5" id="sendview"> <input type="text"
		// id="message" style="width: 250px;"> <input type="button" id="save"
		// onclick="save()" value="send"></div>';

		if ($("#chat-page-" + id).length == 0) {
			var data = '<div id="chat-page-'
					+ id
					+ '" style="float: left;"> <div class="chat-container" style="margin-left: 40px;">'
					+ ' <div class="chat-header"> <h2>'
					+ name
					+ '</h2> </div>  <ul id="messageArea-'
					+ id
					+ '">  </ul> <div id="messageForm-'
					+ id
					+ '">'
					+ ' <div class="form-group"> <div class="input-group clearfix"> <input type="text" id="message-'
					+ id
					+ '" '
					+ 'placeholder="Type a message..." autocomplete="off"  onkeydown="functionThengaKula(event,'
					+ id
					+ ')" class="form-control textChat" style="style="width: 76%;"/> '
					+ '<button id="btn_sentMessage" onclick="save('
					+ id
					+ ')" type="button" class="primary" style="float: right;width: 23%;">Send</button> '
					+ '<input name="uploadDobFile" id="file-' + id
					+ '" type="file"  value="Browse...">'
					+ '</div> </div> </div> </div> </div>';

			$("#messageDiv").append(data);
		}
	} else {

		$("#messageli").empty();

		// var body = '<div class="col-sm-5" id="sendview"> <input type="text"
		// id="message" style="width: 250px;"> <input type="button" id="save"
		// onclick="save()" value="send"></div>';

		if ($("#chat-page-" + id).length == 0) {
			var data = '<div id="chat-page-'
					+ id
					+ '" style="float: left;"> <div class="chat-container" style="margin-left: 40px;">'
					+ ' <div class="chat-header"> <h2>'
					+ name
					+ '</h2> </div>  <ul id="messageArea-'
					+ id
					+ '">  </ul> <div id="messageForm-'
					+ id
					+ '">'
					+ ' <div class="form-group"> <div class="input-group clearfix"> <input  type="text" id="message-'
					+ id
					+ '" '
					+ 'placeholder="Type a message..."  onkeydown="functionThengaKula(event,'
					+ id
					+ ')" autocomplete="off"  class="form-control textChat" style="style="width: 76%;" /> '
					+ '<button id="btn_sentMessage" onclick="saveGroup('
					+ id
					+ ')" type="button" class="primary" style="float: right;width: 23%;">Send</button> '
					+ '<input type="file" id="input-file-now" class="dropify"accept=".xlsx,.xls,image/*,.doc,.docx,.pdf" />'
					+ '</div> </div> </div> </div> </div>';

			$("#messageDiv").append(data);
		}
	}

}

function functionThengaKula(ev, id) {
	var x = event.which || event.keyCode;

	if (x == 13) {
		saveGroup(id);
		alert("Enter");
	}
}

function save(id) {
	var message = {};

	message.message = $("#message-" + id).val();
	message.receiveUserId = id;

	message.type = "1";

	var messageContextPath = location.protocol + '//' + location.host
			+ location.pathname;
	$.ajax({
		method : 'POST',
		url : messageContextPath,
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(message),
		success : function(data) {
			createmessageview(data, message.receiveUserId, 1);
			$("#message-" + id).val('');
		},
		error : function(xhr, error) {
			console.log(xhr, error);
		}

	});
}

function saveGroup(id) {

	var message = {};

	message.message = $("#message-" + id).val();
	message.receiveUserId = id;
	message.senderUserId = loginUserUk;

	// var messageContextPath = location.protocol + '//' + location.host
	// + location.pathname+'/sample';

	var messageContextPath = location.protocol + '//' + location.host
			+ location.pathname+'attch';

	var attach = new FormData();

	var file = uploadAttachment();

	attach.append("files", file);
	attach.append('message', JSON.stringify(message));

	$.ajax({
		type : "POST",
		//enctype : 'multipart/form-data',
		url : messageContextPath,
		data : attach,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			createmessageview(data, message.receiveUserId, 1);
			$("#message-" + id).val('');
		},
		error : function(xhr, error) {
			$("#divImgeUpload").html("");
			console.log(xhr, error);
		}
	});

	// $.ajax({
	// method : 'POST',
	// url : messageContextPath,
	// contentType : "application/json; charset=utf-8",
	// data : JSON.stringify(message),
	// success : function(data) {
	// createmessageview(data,message.receiveUserId,1);
	// $("#message-"+id).val('');
	// },
	// error : function(xhr, error) {
	//			
	// }
	//		
	//		
	// });

	/*
	 * var groupMessage = {};
	 * 
	 * groupMessage.message = $("#message-"+id).val(); groupMessage.grouoUKid =
	 * id; groupMessage.senderUserId=loginUserUk;
	 * 
	 * var messageContextPath = location.protocol + '//' + location.host +
	 * location.pathname+'/sample';
	 * 
	 * $.ajax({ method : 'POST', url : messageContextPath, contentType :
	 * "application/json; charset=utf-8", data : JSON.stringify(groupMessage),
	 * success : function(data) { createmessageview(data,message.receiveUserId);
	 * $("#message-"+id).val(''); }, error : function(xhr, error) { }
	 * 
	 * 
	 * });
	 */
}

function saveMP3() {
	
	alert("sucess");
	
	var message = {};

	message.message = $("#hidenInputMp3").val();
	
	var messageContextPath = location.protocol + '//' + location.host
	+ location.pathname+'/'+"saveMp3";

	$.ajax({
		method : 'POST',
		url : messageContextPath,
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(message),
		success : function(data) {
			createmessageview(data);
		},
		error : function(xhr, error) {
			onError(xhr, error);
		}
	});
}


function createmessageview(message, id, align) {
	
	if (align == 1) {

		//<img alt="" src="${imgurl}/123">
		
		
		var lastMessage = '<li style="color:blue; list-style: none; text-align: right;margin-right: 10px;"><img alt="" src="'+imageURL+'/123"></li>';
		
		 lastMessage += '<li style="color:blue; list-style: none; text-align: right;margin-right: 10px;">'
				+ message.message + '</li>';
		
		console.log(lastMessage);
		$("#messageArea-" + id).append(lastMessage);
	} else {
		// $("#messageArea-"+id).append("<li>" + message.message + "</li>");

		var lastMessage = '<li style="color:blue; list-style: none; text-align: right;margin-right: 10px;"><img alt="" src="'+imageURL+'/123"></li>';

		
		 lastMessage += '<li style="color:green;list-style: none;">'
				+ message.message + '</li>';
		$("#messageArea-" + id).append(lastMessage);
	}

}

$(document).ready(
		function() {
			// establish websocket connection
			connect();

			$("#sendview").hide();

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

			$("input").change(function(ev) {
				$("input").css("background-color", "yellow");
			});
			/*
			 * // Get the input field var input =
			 * document.getElementById("validationServer01"); // Execute a
			 * function when the user releases a key on the keyboard
			 * input.addEventListener("keyup", function(event) { // Cancel the
			 * default action, if needed event.preventDefault(); // Number 13 is
			 * the "Enter" key on the keyboard if (event.keyCode === 13) { //
			 * Trigger the button element with a click
			 * document.getElementById("btn_sentMessage").click(); } });
			 */

		});

function uploadAttachment() {
	$("#error_attchmnt").text("");
	var image = $('#input-file-now')[0].files[0];
	if (image == null) {
		$("#error_attchmnt").text("please select image");
		return;
	}
	var ext = $('#input-file-now').val().split('.').pop().toLowerCase();
	var allowedFiles = [ "doc", "docx", "pdf", "xls", "xlsx", "pdf", "gif",
			"png", "jpg", "jpeg", "tiff" ];
	if ($.inArray(ext, allowedFiles) == -1) {
		$("#error_attchmnt").text(
				"Please upload files having extensions: "
						+ allowedFiles.join(', ') + " only.");
		return;
	}
	return image;
}

// connect stompClient web socket

var stompClient = null;
function connect() {

	if (stompClient != null) {
		stompClient.disconnect();
	}
	var socket = new SockJS(contextPath + '/tracking');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		stompClient
				.subscribe('/message/send/individual/' + loginUserUk,
						function(response) {
							var messageBody = JSON.parse(response.body);
							messageBody.currentUser = false;
							createmessageview(messageBody,
									messageBody.senderUserId, 2);
						});

		stompClient.subscribe('/message/send/individual/' + loginUserUkGrp,
				function(response) {
					var messageBody = JSON.parse(response.body);
					messageBody.currentUser = false;
					if (loginUserUk != messageBody.senderUserId) {
						createmessageview(messageBody,
								messageBody.receiveUserId, 2);
					}

				});
	});

}