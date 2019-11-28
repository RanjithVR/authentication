<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime"%>


<html>
<head>

<spring:url value="/resources/assets/image/images.png" var="micro"></spring:url>

<spring:url value="/resources/assets/css/main.css" var="mainCss"></spring:url>

<spring:url value="/company/image" var="imgurl"></spring:url>

<link rel="stylesheet" href="${mainCss }">

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Message</title>
</head>
<body>



	<noscript>
		<h2>Sorry! Your browser doesn't support Javascript</h2>
	</noscript>
	<div id="username-page123" style="border: 2px solid red"></div>
	<div id="username-page">

		<div class="username-page-container">
			<!--  <input type="button"  value="Upload..........!!!!" onclick="saveMP3Data()"  /> -->
			<c:forEach items="${users}" var="user">
				<br>
				<input type="button" id="user"
					onclick="view('${user.id}','${user.userName}','1')"
					value="${user.userName}">

			</c:forEach>

			<c:forEach items="${group}" var="group">
				<br>
				<input type="button" id="group"
					onclick="view('${group.ukMessageKey}','${group.groupName}','2')"
					value="${group.groupName}">

			</c:forEach>

		</div>
	</div>

	<!-- <div id="chat-page" style="float: left;">
		<div class="chat-container">
			<div class="chat-header">
				<h2>Spring WebSocket Chat Demo</h2>
			</div>

			<ul id="messageArea">

			</ul>
			<div id="messageForm">
				<div class="form-group">
					<div class="input-group clearfix">
						<input type="text" id="message" placeholder="Type a message..."
							autocomplete="off" class="form-control" />
						<button type="submit" class="primary">Send</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="chat-page" style="float: right;">
		<div class="chat-container">
			<div class="chat-header">
				<h2>Spring WebSocket Chat Demo</h2>
			</div>

			<ul id="messageArea">

			</ul>
			<div id="messageForm">
				<div class="form-group">
					<div class="input-group clearfix">
						<input type="text" id="message" placeholder="Type a message..."
							autocomplete="off" class="form-control" />
						<button type="submit" class="primary">Send</button>
					</div>
				</div>
			</div>
		</div>
	</div> -->








	<br>
	<br>
	<!-- <div id="sendview">
		<input type="text" id="message" style="width: 250px;"> <input
			type="button" id="save" onclick="save()" value="send">
	</div> -->

	<div id="messageDiv"></div>
	<!-- <ul id="messageli" style="border:1px solid black;width:25%;"></ul> -->


	<form id="uploadVoiceAjax" method="POST" enctype="multipart/form-data">

		<input name="convertSoundFile" id="hidenInputMp3" type="hidden"
			style="position: relative; left: 5px; width: 73%;" />

		<div id="popupBckDiv"></div>
		<div id="manageSoundUploadPopupPnl">
			<div id="lightbox-panel" style="z-index: 7;">
				<p align="right" style="padding: 0; margin: 0;">
					<a id="close-panel" href="#"> <img
						onclick="$('#manageSoundUploadPopupPnl').hide();$('#popupBckDiv').hide(); return false;"
						th:src="@{/resources/img/close_button.png}" />
					</a>
				</p>
				<h2 style="font-size: 20px;">Upload Voice</h2>

				<fieldset class="textbox">
					<table id="voiceUploadTable" style="margin-top: 20px;">
						<tbody>
							<tr>
								<td style="width: 150px; text-align: center;">Title</td>
								<td><input id="voiceTitleTextBox" type="text"
									name="txtaddDriverName" maxlength="40" /></td>

							</tr>
							<tr>
								<td
									style="width: 150px; text-align: center; padding: 10px 15px;">
									<input id="recordVoice" class="uploadFile" type="radio"
									checked="true" name="fileUploadType"
									style="margin-right: 5px; left: 19px; position: relative;" />
									<span style="position: relative; left: 19px;">Record</span>
								</td>
								<td style="padding: 10px 15px;"><input class="uploadFile"
									id="fileChooseVoice" type="radio" name="fileUploadType"
									style="margin-right: 5px;" /> <span style="">File
										Choose</span></td>

							</tr>
							<tr id="fileChooseItem">
								<td colspan="2" style="padding: 20px; text-align: center;">
									<input name="uploadDobFile" id="uploadDOBFile" type="file"
									accept="audio/*"
									onchange="startPreloader(); previewFile(this); $('#hidenInputMp3').val('');"
									value="Browse..."
									style="position: relative; left: 5px; width: 73%;" /> <audio
										id="fileChooseAudioCntrl" controls="controls">

									</audio>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="controlButtons" style="width: 100%; float: left;">
						<div id="controlButtons">
							<div class="recordButton">
								<img src="${micro}" id="recordButton" class="recordOff" />
							</div>
							<span id="recordHelp">Allow your microphone.</span>

							<div id="recordContainer" class="startContainer">
								<div id="recordTime">
									<span id="sw_m">00</span>:<span id="sw_s">00</span>:<span
										id="sw_ms">00</span>
								</div>
								<div id="recordCircle" class="startRecord">
									<div id="recordText">Record</div>
								</div>
							</div>
							<div id="recdListInManageVoice"
								class="newsfeedContainer recordingslist"></div>

						</div>


					</div>

					<div align="center" style="padding: 0 20px;">
						<input type="hidden" id="hdnDriverId" value="0" /> <input
							type="button" value="Upload" onclick="saveMp3()"
							id="btnUploadVoice" class="btn_m"
							style="position: relative; left: 138px; top: 41px;" />
					</div>
					<label id="errMsgMangeSound"
						style="display: none; color: red; position: relative; top: -60px; font-size: 12px;">Supported
						formats: MP3,WAV,M4A,OGG</label> <label id="errorMessgaeInMangeSound"
						style="color: red; position: relative; top: -35px; display: none">File
						Name Alrady Exist,Please Rename title</label>
				</fieldset>

				<div id="audioPreLoaderback"></div>
				<div id="audioPreLoader"></div>
			</div>
		</div>
		<input type="button" id="saveMp3" onclick="saveMP3()" value="Download">
	</form>
	<%-- <img alt="" src="${imgurl}/123"> --%>


	<script type="text/javascript">
		var imageURL = "${imgurl}";
	</script>
</body>


<!-- SockJs -->
<spring:url value="/resources/assets/js/websocket/sockjs-0.3.4.js"
	var="sockjs"></spring:url>
<script type="text/javascript" src="${sockjs}"></script>

<!-- StompJS -->
<spring:url value="/resources/assets/js/websocket/stomp.js" var="stomp"></spring:url>
<script type="text/javascript" src="${stomp}"></script>



<!-- //change the js from local -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<spring:url value="/resources/assets/js/voicePannel/recorderControl.js"
	var="recorderControl"></spring:url>
<script type="text/javascript" src="${recorderControl}"></script>

<spring:url value="/resources/assets/js/voicePannel/jquery.stopwatch.js"
	var="stopwatch"></spring:url>
<script type="text/javascript" src="${stopwatch}"></script>

<spring:url value="/resources/assets/js/voicePannel/recorder.js"
	var="recorder"></spring:url>
<script type="text/javascript" src="${recorder}"></script>

<spring:url value="/resources/app/message.js" var="messagejs"></spring:url>
<script type="text/javascript" src="${messagejs}"></script>
<script>
	var contextPath = "${pageContext.request.contextPath}";
	var loginUserUk = "${receverId}";
	var loginUserUkGrp = "12345689";
</script>



</html>