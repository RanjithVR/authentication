<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime"%>
 <%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
 

<html>

<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="description" content="">
<meta name="keywords" content="">
<title>Chat Application</title>

 <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css" integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/" crossorigin="anonymous">

<link
	href="https://fonts.googleapis.com/css?family=Open+Sans:400,600,700|Poppins:300,400,500,600"
	rel="stylesheet">
<script type="text/javascript"
	src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
	
<spring:url value="/resources/assets/css/vendor.bundle.css"
	var="vendorBundleCss"></spring:url>
<link rel="stylesheet" href="${vendorBundleCss}">

<spring:url value="/resources/assets/css/app.bundle.css"
	var="appBundleCss"></spring:url>
<link rel="stylesheet" href="${appBundleCss}">

<spring:url value="/resources/assets/css/theme-a.css" var="themeacss"></spring:url>
<link rel="stylesheet" href="${themeacss}">

<spring:url value="/resources/assets/css/style.css" var="styleCss"></spring:url>
<link rel="stylesheet" href="${styleCss}">

<spring:url value="/resources/assets/css/preloader.css" var="preloaderCss"></spring:url>
<link rel="stylesheet" href="${preloaderCss}">

<!-- Begin emoji-picker Stylesheets -->

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<spring:url value="/resources/assets/emoji/css/emoji.css" var="emojiCss"></spring:url>
<link href="${emojiCss}" rel="stylesheet">
<!-- End emoji-picker Stylesheets -->

<spring:url value="/company/getMessageAll" var="urlMessageAll"></spring:url>

<spring:url value="/company/getGroupMessageAll" var="urlMessageGroupAll"></spring:url>

<spring:url value="/company/profile" var="urlprfAttachemnt" />

<spring:url value="/company/attachments" var="urlAttachemnt" />

<style>
 .startRecord{background-color:#F23030;color:#ffffff;display:none;}
.stopRecord{background-color:#ffffff;color:#000000;display:none;} 

.recordStop{color: #ff0000; padding: 2px 2px; border: 1px solid; border-radius: 50%;}
.date{
text-align: right; font-size: smaller;
}
  img{
  max-width:180px;
}
.inputfile {
    width: 0.1px;
    height: 0.1px;
    opacity: 1;
    overflow: hidden;
    position: absolute;
    z-index: -1;
}
</style>
    <spring:url value="/resources/assets/css/component.css" var="componentCss"></spring:url>
    <link rel="stylesheet" href="${componentCss}">

    <script>(function(e,t,n){var r=e.querySelectorAll("html")[0];r.className=r.className.replace(/(^|\s)no-js(\s|$)/,"$1js$2")})(document,window,0);</script>

</head>

<body>
	<div id="app_wrapper" class="app-chat">
		<section id="content_outer_wrapper">
			<div id="content_wrapper">
				<div id="content" class="container">
					<div id="content_type" class="boxed-leftnav card drawer">
						<div id="leftnav">
							<div class="card">
								<header class="card-heading">
									<ul class="card-actions icons left-top">
										<a href="" role="button" data-card-off-canvas="is-active">
											<i class="zmdi zmdi-menu"></i>
										</a>
									</ul>

									<ul class="card-actions icons right-top">

										<li class="dropdown " data-toggle="tooltip"
											data-placement="left" title="Friend requests"><a
											href="#" id="friendRequest" data-toggle="modal"
											class="friend_request_div tool"> 
											<i class="fa fa-users"><sup><i class="fal fa-comment-alt blink_me" id='frq-${userId}' style="color:green;display:none"></i></sup> </i>
											
										</a></li>
										<li class="dropdown" data-toggle="tooltip"
											data-placement="bottom" title="Invite friends"><a
											href="#" id="new_invite_user" data-toggle="modal"> <i
												class="fa fa-user-plus"></i>
										</a></li>
										<li class="dropdown" data-toggle="tooltip"
											data-placement="right" title="New group"><a href="#"
											id="new_group_div" data-toggle="modal"> <i
												class="zmdi zmdi-accounts-add"></i>
										</a></li>
									</ul>
								</header>
								<div class="card-body chat_tottal_scroll">
								<div class="list-group scrollbar">
									<ul class="chatbox_add">
										<li class="list-group-item " data-contacts="show"><span
											class="pull-left"><button
													class="btn btn-green btn-fab btn-fab-sm m-r-10">
													<i class="zmdi zmdi-plus"></i>
												</button></span>
											<div class="list-group-item-body">
												<div class="list-group-item-heading m-t-5">Add People</div>
											</div></li>
										<li>
											<div class="card-contacts-search">
												<div class="form-group is-empty">
													<input type="text"
														placeholder="Search by Name"
														class="form-control" id="searchContactList" onkeyup="searchContact()" autocomplete="off">
												</div>
											</div>
										</li>
										</ul>
										<ul id="chat_list_searchResult">
										</ul>
										<ul class="chat_list" id="chat_list_search">
										<c:set var="count" value="0" scope="page" />
										<c:forEach items="${users}" var="user" varStatus="loop">
											<c:set var="count" value="${count + 1}" scope="page" />
											<li class="list-group-item contactPool" id='li-${user.ukMessageKey}'
												onclick="getMessageWindow(this,'${user.firstName}','${user.ukMessageKey}','${user.id}','${user.attachment.id}')">
												<div>
											
													<span class="pull-left"><img
														src="${urlAttachemnt}/${user.attachment.id}"
														alt="" class="img-circle img-sm m-r-10"></span> <i
														class="fa fa-commenting-o blink_me" id='${user.ukMessageKey}'
														style="float: right; display: none;color: blue;"></i>
												</div>
												<div class="list-group-item-body">
												<div>	
													<div class="list-group-item-heading userNameDiv">${user.firstName}</div>
													
													<c:if test = "${!user.isOnline}"> <sapn><i class="fa fa-circle online" id='online-${user.id}' style="float: right;color:green;display:none;"></i></sapn> </c:if>
													<c:if test = "${user.isOnline}"> <sapn><i class="fa fa-circle online" id='online-${user.id}' style="float: right;color:green;"></i></sapn> </c:if>
											    	 
												</div>
												<div class="list-group-item-text">${user.userName}</div>

												</div>
											</li>
										</c:forEach>

										<c:forEach items="${group}" var="group">

											<li class="list-group-item contactPool" id='li-${group.id}'
												onclick="getMessageWindowGroup(this,'${group.groupName}','${group.groupUniqeId}','${group.id}','${group.attachment.id}')">
												<div>
													<span class="pull-left"><img
														src="${urlAttachemnt}/${group.attachment.id}"
														alt="" class="img-circle img-sm m-r-10"></span>
														<i class="fa fa-commenting-o blink_me" id='${group.id}'
														style="float: right; display: none;color: blue;"></i>
												</div>
												<div class="list-group-item-body">
													<div class="list-group-item-heading">${group.groupName}</div>


												</div>
											</li>



										</c:forEach>

									</ul>
									</div>
								</div>
							</div>
						</div>
						<div class="content-body">
							<div class="card">
								<header class="card-heading">

									<ul class="active-contact">
										<li class="hidden-sm hidden-md hidden-lg"><a
											href="javascript:void(0)" role="button"
											class="pull-left m-r-40 show-contacts" data-contacts="show">
												<i class="zmdi zmdi-arrow-left" id="leftArrow"></i>
										</a></li>
										<li><span class="avatar"><img src="" alt=""
												class="img-circle img-sm pull-left m-r-10"
												id="currentChatHead"></span></li>
										<li><span class="name"></span></li>
									</ul>

									<ul class="card-actions icons right-top topInfoUl" style=" width: 125px;">
										<!--  <li>
                                            <a href="javascript:void(0)">
                                                <i class="zmdi zmdi-videocam"></i>
                                            </a>
                                        </li> -->
										<li class="expand" style="float: left;"><a href="javascript:void(0)"
											data-toggle-expand="content-expanded"
											data-key="contentExpand" data-expand-location="chat"> <i
												class="zmdi zmdi-fullscreen"></i>
										</a></li>
										
									 <!-- <li class="dropdown" id="groupInfo" style="display:none;">
									 <a href="javascript:void(0)" data-toggle="modal" data-target="#new_group_info"> <i class="zmdi zmdi-info zmdi-hc-fw"></i>
										</a> <ul class="dropdown-menu btn-primary dropdown-menu-right">
                                                <li>
                                                    <a href="javascript:void(0)">Option One</a>
                                                </li>
                                                <li>
                                                    <a href="javascript:void(0)">Option Two</a>
                                                </li>
                                                <li>
                                                    <a href="javascript:void(0)">Option Three</a>
                                                </li>
                                            </ul></li> -->
                                            
                                      <li class="dropdown dropdown-notification nav-item show" id="groupInfoIcon" style="display: none;">

	<a class="nav-link nav-link-label" href="#" data-toggle="dropdown" id="rightMenuIcon" aria-expanded="true">
        <i class="zmdi zmdi-menu"></i>
	</a>
	
	<a href="#"  data-toggle="modal" data-target="#messageDelete" id="deleteIcon"  style="display:none"> 
		<i id="deleteButton" class="fa fa-trash"></i>
	</a>

	<a href="#"   id="selectAllIcon"  style="display:none" onclick="selectAll()" data-toggle="tooltip" data-placement="bottom" title="Select all"> 
		<i class="fa fa-check" aria-hidden="true"></i>
	</a>
	
	
	
                                <!--       <li class="dropdown dropdown-notification nav-item show" id="groupInfoIcon" style="display: none;">
	<a class="nav-link nav-link-label" href="#" data-toggle="dropdown" aria-expanded="true">
        <i class="zmdi zmdi-menu"></i>
	</a>
	
	<a href="#"  data-toggle="modal" data-target="#messageDelete" id="deleteLink"  style="display:none"> 
		<i id="deleteButton" class="fa fa-trash"></i>
	</a> -->
	
	
    <ul class="dropdown-menu dropdown-menu-media dropdown-menu-right show">
        
		<li class="dropdown" id="groupInfo" style="display:none;">
									 <a href="javascript:void(0)" data-toggle="modal" data-target="#new_group_info"> <i class="zmdi zmdi-info zmdi-hc-fw"></i>
										</a></li>
        <li class="scrollable-container media-list ps-container ps-theme-dark" data-ps-id="2d0e477b-a9c8-6768-671c-3421a3d59960">
	<!-- 	 	<a href="#" id="groupInfo">Group Info</a>  -->
			
			<a href="#" id="deleteGrp"  data-toggle="modal" data-target="#DeRegister">Delete Group </a>
			
        </li>
    </ul>
</li>  
                                            
									</ul>
								</header>
								<div class="card-body mw-lightGray scrollbar" id="chat_con_text">
									<section id="chat-wrapper">
										
										
										<!-- <div class="chat-row first">
											<img src="img/profiles/18.jpg" alt=""
												class="img-circle img-sm pull-right">
											<div class="bubble">
												<div class="message">
													<p>Hey, I'm back in town...you wanna meet-up for
														coffee?</p>
												</div>
												<div class="date"></div>
											</div>
										</div>
										<div class="chat-row response">
											<img src="img/profiles/07.jpg" alt=""
												class="img-circle img-sm pull-left">
											<div class="bubble_response">
												<div class="message">
													<p>Yeah that sounds great! Have a new project to run by
														you.</p>
												</div>
												<div class="date"></div>
											</div>
										</div>
										<div class="chat-row">
											<img src="img/profiles/18.jpg" alt=""
												class="img-circle img-sm pull-right">
											<div class="bubble">
												<div class="message">
													<p>Awesome, let's meet at the coffee shop on South
														Brodway. What time works best for you?</p>
												</div>
												<div class="date"></div>
											</div>
										</div>
										<div class="chat-row response">
											<img src="img/profiles/07.jpg" alt=""
												class="img-circle img-sm pull-left">
											<div class="bubble_response">
												<div class="message">
													<p>Have a meeting today, but I'll be free around 4pm.</p>
												</div>
												<div class="date"></div>
											</div>
										</div>
										<div class="chat-row">
											<img src="img/profiles/18.jpg" alt=""
												class="img-circle img-sm pull-right">
											<div class="bubble">
												<div class="message">
													<p>Perfect, see ya then.</p>
												</div>
												<div class="date"></div>
											</div>
										</div>
										<div class="chat-row response last">
											<img src="img/profiles/07.jpg" alt=""
												class="img-circle img-sm pull-left">
											<div class="chat-bubble">
												<div class="loading">
													<div class="dot one"></div>
													<div class="dot two"></div>
													<div class="dot three"></div>
												</div>
											</div>
										</div> -->
									</section>
								</div>
								<div class="card-footer p-10" id="footerDiv" style="display:none">

									<div class="image_alt_name" id="imageSelectedDiv" style="display: none;">
										<span id="imageSelected" ></span> <span
											class="image_alt_name_close" id="deleteImageSele" style="color:red;">x</span>
											<span class="arw_btm_img">&nbsp;</span>
									</div>
									<ul class="card-actions icons left-bottom">
										
										

									
									<li>	<div class="image-upload"> <label for="file-input-data"> <i class="zmdi zmdi-attachment-alt"></i> </label>

  
 <input id="file-input-data" type="file" accept=".xlsx,.xls,image/*,.doc, .docx,.ppt, .pptx,.txt,.pdf,.mp2,.mp3,.mp4,.mov,.eps,.ai,.indd" style="display:none;"/></div></li>
 
 

										<li class="record_list" id="recoedDivShowLi">
											<div class="recording_div" style="display:none;">
												<div class="recording_div_con"></div>
											</div>
											<a href="javascript:void(0)"> <i
												class="zmdi zmdi-mic zmdi-hc-fw"></i></a>
										</li>
										<!-- <li>
                                            <a href="javascript:void(0)">
                                                <i class="zmdi zmdi-mood"></i>
                                            </a>
                                        </li> -->
									</ul>
									<div class="form-group m-10 p-0 p-l-75 is-empty">
										<div class="input-group">
											<label class="sr-only">Leave a comment...</label>
											<div class="type_text_box_div">
												<!-- <div class="lead emoji-picker-container">
												  <input type="text" class="form-control form-rounded input-lightGray" placeholder="Leave a comment.." data-emojiable="true">
												</div> -->
												<div class="lead emoji-picker-container">
												<!-- 	<textarea id="textBoxMessage123" style="resize:vertical;"
														class="form-control form-rounded input-lightGray textarea-control"
														rows="3" placeholder="Leave a comment.."
														data-emojiable="true" data-emoji-input="unicode"
														onkeyup="sendMessage()"></textarea> -->
														
															<input id="textBoxMessage" style="resize:vertical;"
														class="form-control form-rounded input-lightGray textarea-control"
														placeholder="Leave a comment.."
														data-emojiable="true" data-emoji-input="unicode"
														onkeyup="sendMessage()"/>
												</div>
												<!-- <input type="text" class="form-control form-rounded input-lightGray" placeholder="Leave a comment.."> -->
												<!-- <div class="smily_icon"><i class="zmdi zmdi-mood"></i></div> -->
												<div class="record_icon">
													<i class="zmdi zmdi-mic zmdi-hc-fw" id="voiceToTextStart" onclick="startRecordingVoiceToText()"></i>
													<i class="zmdi zmdi-mic zmdi-hc-fw recordStop" id="voiceToTextstop" style="display:none;" onclick="stopRecordingVoiceToText()"></i>
												</div>
											</div>
											<span class="input-group-btn send_btn"> <!-- <button type="button"
													class="btn btn-blue btn-fab animate-fab btn-fab-sm">
													<i class="zmdi zmdi-mail-send" id="sendMessage"
														onclick="sendMessage()"></i>
												</button> -->

					
												<button type="button"
													class="btn btn-blue btn-fab  btn-fab-sm">
													<i class="zmdi zmdi-mail-send" id="sendPic"
														onclick="sendPic()"></i>
												</button>



											</span>
											<!-- onkeyup="sendMessage()" -->





										</div>
									</div>



								</div>
							</div>
							
							
							
							<!--Chat Contact List -->
							<div class="card card-contacts hide" id="chat_contact_wrapper">
								<header class="card-heading">
									<ul
										class="card-actions icons left-top hidden-sm hidden-md hidden-lg">
										<li><a href="javascript:void(0)" role="button"
											data-off-canvas-mobile="is-active"> <i
												class="zmdi zmdi-menu"></i>
										</a></li>
									</ul>
									<h2 class="card-title p-l-50">New friend request</h2>
									<ul class="card-actions icons right-top">
										<li><a href="javascript:void(0)" data-contacts="hide">
												<i class="zmdi zmdi-close"></i>
										</a></li>
									</ul>
								</header>
								<div class="card-body p-0">
									<div class="card-contacts-search">
										<div class="form-group is-empty">
											<a href="javascript:void(0)" class="close-search"
												data-card-search="close" title=""> <i
												class="zmdi zmdi-close"></i></a> <input type="text" 
												placeholder="Enter user Name"
												class="form-control add_people_search_txt" id="searchPeopleTxtBox" autocomplete="off"> 
												<input type="submit" value="serach" id="searchPeopleBtn" class="add_people_search_btn">
												
												 <a href="javascript:void(0)" class="clear-search"
												data-card-search="clear" title=""><i
												class="zmdi zmdi-close-circle"></i></a>
										</div>
									</div>
									<ul class="list-group " id="searchInHomeViwUl">
									 	

									</ul>
								</div>
								<!-- <div class="card-footer border-top">
									<ul class="card-actions icons right">
										<li data-toggle="tooltip" data-placement="left"
											data-original-title="Add New Contact">
											<button class="btn btn-primary btn-fab" data-toggle="modal"
												data-target="#newContactModal">
												<i class="zmdi zmdi-account-add"></i>
											</button>
										</li>
									</ul>
								</div> -->
							</div> 
							<!--Group Create List -->
							<!-- 	<div class="card card-contacts hide" id="group_create_wrapper">
								<header class="card-heading">
									<ul
										class="card-actions icons left-top hidden-sm hidden-md hidden-lg">
										<li><a href="javascript:void(0)" role="button"
											data-off-canvas-mobile="is-active"> <i
												class="zmdi zmdi-menu"></i>
										</a></li>
									</ul>
									<h2 class="card-title p-l-50">New Group</h2>
									<ul class="card-actions icons right-top">
										<li><a href="javascript:void(0)" group-contact="hide">
												<i class="zmdi zmdi-close"></i>
										</a></li>
									</ul>
								</header>
								<div class="card-body p-0">
									<div class="card-contacts-search">
										<div class="form-group is-empty">
											<a href="javascript:void(0)" class="close-search"
												data-card-search="close" title=""> <i
												class="zmdi zmdi-close"></i></a> <input type="text"
												placeholder="Enter name, email, or phone"
												class="form-control" autocomplete="off"> <a
												href="javascript:void(0)" class="clear-search"
												data-card-search="clear" title=""><i
												class="zmdi zmdi-close-circle"></i></a>
										</div>
									</div>
									<ul class="list-group ">
										<li class="list-group-item" data-contacts="hide"><span
											class="pull-left"><img src="img/profiles/09.jpg"
												alt="" class="img-circle max-w-40 m-r-10 "></span>
											<div class="list-group-item-body">
												<div class="list-group-item-heading">Gabriel Saunders</div>
												<div class="list-group-item-text">gabriel@chatapplication@pro</div>
											</div>
											<ul class="card-actions icons right-top">
												<li class="dropdown"><a href="javascript:void(0)"
													data-toggle="dropdown" aria-expanded="false"> <i
														class="zmdi
															zmdi-more-vert"></i>
												</a>
													<ul
														class="dropdown-menu btn-primary
															dropdown-menu-right">
														<li><a role="button" data-toggle="modal"
															href="javascript:void(0)" data-target="#contactEditUser"
															data-name="Gabriel Saunders"
															data-email="gabriel@chatapplication.pro"
															data-phone="+1-202-555-0102"
															data-img="img/profiles/09.jpg" data-star="false"><i
																class="zmdi
																	zmdi-edit"></i> Edit</a></li>
														<li><a href="javascript:void(0)"><i
																class="zmdi zmdi-archive"></i> Archive</a></li>
														<li><a href="javascript:void(0)"><i
																class="zmdi zmdi-delete"></i> Delete</a></li>
													</ul></li>
											</ul></li>

										<li class="list-group-item" data-contacts="hide"><span
											class="pull-left"><img src="img/profiles/06.jpg"
												alt="" class="img-circle max-w-40 m-r-10 "></span>
											<div class="list-group-item-body">
												<div class="list-group-item-heading">Shawna Cohen</div>
												<div class="list-group-item-text">shawna@chatapplication@pro</div>
											</div>
											<ul class="card-actions icons right-top">
												<li class="dropdown"><a href="javascript:void(0)"
													data-toggle="dropdown" aria-expanded="false"> <i
														class="zmdi
																zmdi-more-vert"></i>
												</a>
													<ul
														class="dropdown-menu btn-primary
																dropdown-menu-right">
														<li><a role="button" data-toggle="modal"
															href="javascript:void(0)" data-target="#contactEditUser"
															data-name="Shawna Cohen"
															data-email="shawna@chatapplication.pro"
															data-phone="+1-202-555-0107"
															data-img="img/profiles/06.jpg"> <i
																class="zmdi zmdi-edit"></i> Edit
														</a></li>
														<li><a href="javascript:void(0)"><i
																class="zmdi zmdi-archive"></i> Archive</a></li>
														<li><a href="javascript:void(0)"><i
																class="zmdi zmdi-delete"></i> Delete</a></li>
													</ul></li>
											</ul></li>

										<li class="list-group-item" data-contacts="hide"><span
											class="pull-left"><img src="img/profiles/15.jpg"
												alt="" class="img-circle max-w-40 m-r-10 "></span>
											<div class="list-group-item-body">
												<div class="list-group-item-heading">Jason Kendall</div>
												<div class="list-group-item-text">jason@chatapplication.pro</div>
											</div>
											<ul class="card-actions icons right-top">
												<li class="dropdown"><a href="javascript:void(0)"
													data-toggle="dropdown" aria-expanded="false"> <i
														class="zmdi
																	zmdi-more-vert"></i>
												</a>
													<ul
														class="dropdown-menu btn-primary
																	dropdown-menu-right">
														<li><a role="button" data-toggle="modal"
															href="javascript:void(0)" data-target="#contactEditUser"
															data-name="Jason Kendall"
															data-email="jason@chatapplication.pro"
															data-phone="+1-202-555-0154"
															data-img="img/profiles/15.jpg"> <i
																class="zmdi
																			zmdi-edit"></i> Edit
														</a></li>
														<li><a href="javascript:void(0)"><i
																class="zmdi zmdi-archive"></i> Archive</a></li>
														<li><a href="javascript:void(0)"><i
																class="zmdi zmdi-delete"></i> Delete</a></li>
													</ul></li>
											</ul></li>

										<li class="list-group-item" data-contacts="hide"><span
											class="pull-left"><img src="img/profiles/17.jpg"
												alt="" class="img-circle max-w-40 m-r-10 "></span>
											<div class="list-group-item-body">
												<div class="list-group-item-heading">Thomas Banks</div>
												<div class="list-group-item-text">thomas@chatapplication.pro</div>
											</div>
											<ul class="card-actions icons right-top">
												<li class="dropdown"><a href="javascript:void(0)"
													data-toggle="dropdown" aria-expanded="false"> <i
														class="zmdi
																		zmdi-more-vert"></i>
												</a>
													<ul
														class="dropdown-menu btn-primary
																		dropdown-menu-right">
														<li><a role="button" data-toggle="modal"
															href="javascript:void(0)" data-target="#contactEditUser"
															data-name="Thomas	Banks"
															data-email="thomas@chatapplication.pro"
															data-phone="+1-202-555-0143"
															data-img="img/profiles/17.jpg"> <i
																class="zmdi
																				zmdi-edit"></i> Edit
														</a></li>
														<li><a href="javascript:void(0)"><i
																class="zmdi zmdi-archive"></i> Archive</a></li>
														<li><a href="javascript:void(0)"><i
																class="zmdi zmdi-delete"></i> Delete</a></li>
													</ul></li>
											</ul></li>

										<li class="list-group-item" data-contacts="hide"><span
											class="pull-left"><img src="img/profiles/11.jpg"
												alt="" class="img-circle max-w-40 m-r-10 "></span>
											<div class="list-group-item-body">
												<div class="list-group-item-heading">Rebecca Harrisr</div>
												<div class="list-group-item-text">rebecca@chatapplication.pro</div>
											</div>
											<ul class="card-actions icons right-top">
												<li class="dropdown"><a href="javascript:void(0)"
													data-toggle="dropdown" aria-expanded="false"> <i
														class="zmdi
																			zmdi-more-vert"></i>
												</a>
													<ul
														class="dropdown-menu btn-primary
																			dropdown-menu-right">
														<li><a role="button" data-toggle="modal"
															href="javascript:void(0)" data-target="#contactEditUser"
															data-name="Rebecca Harris"
															data-email="rebecca@chatapplication.pro"
															data-phone="+1-202-555-0189"
															data-img="img/profiles/11.jpg"> <i
																class="zmdi
																					zmdi-edit"></i> Edit
														</a></li>
														<li><a href="javascript:void(0)"><i
																class="zmdi zmdi-archive"></i> Archive</a></li>
														<li><a href="javascript:void(0)"><i
																class="zmdi zmdi-delete"></i> Delete</a></li>
													</ul></li>
											</ul></li>

									</ul>
								</div>
								<div class="card-footer border-top">
									<ul class="card-actions icons right">
										<li data-toggle="tooltip" data-placement="left"
											data-original-title="Add New Contact">
											<button class="btn btn-primary btn-fab" data-toggle="modal"
												data-target="#newContactModal">
												<i class="zmdi zmdi-account-add"></i>
											</button>
										</li>
									</ul>
								</div>
							</div> -->
						</div>
						<!--Chat Profile Menu -->
						<div class="card card-off-canvas">
							<header class="card-heading alt-heading">
								<div class="profile">
									<img src="img/profiles/18.jpg" alt=""
										class="img-circle card-img">
								</div>
								<a href="javascript:void(0)" class="info"><span
									id="userNameProfile"></span></a>
							</header>
							<div class="card-body p-0">
								<nav class="submenu">
									<ul>
									   <li id="editProfileLi"><a href="javascript:void(0)"  data-modal-id="popup1"><i
												class="fas fa-user-edit" ></i><span class="p-l-20">Edit Profile</span></a></li>
												
										<li id="changePasswordLi"><a href="javascript:void(0)"><i
												class="fas fa-key" ></i><span class="p-l-20">Change Password</span></a></li>
												
										<li id="userDeActivateLi"><a href="javascript:void(0)"><i
												class="fas fa-user-slash"></i><span
												class="p-l-20" >Account Deactivation</span></a></li>
										<li id="userLogoutLi" ><a href="javascript:void(0)"><i
												class="fas fa-sign-out-alt"></i><span class="p-l-20" id="logOut"
												>Log out </span></a></li>
										<!-- <li class="divider">

                                        </li>
                                        <li><a href="javascript:void(0)"><i class="zmdi zmdi-settings"></i><span class="p-l-20">Settings</span></a></li>
                                        <li><a href="javascript:void(0)"><i class="zmdi zmdi-help"></i><span class="p-l-20">Help</span></a></li>
                                        <li><a href="javascript:void(0)"><i class="zmdi  zmdi-comment-text"></i><span class="p-l-20">Feedback</span></a></li> -->
									</ul>
									<nav></nav>
								</nav>
							</div>
						</div>
					</div>
				</div>
				<section id="chat_compose_wrapper">
					<div class="tippy-top">
						<div class="recipient">Allison Grayce</div>
						<ul class="card-actions icons  right-top">
							<li><a href="javascript:void(0)"> <i
									class="zmdi zmdi-videocam"></i>
							</a></li>
							<li class="dropdown"><a href="javascript:void(0)"
								data-toggle="dropdown" aria-expanded="false"> <i
									class="zmdi zmdi-more-vert"></i>
							</a>
								<ul class="dropdown-menu btn-primary dropdown-menu-right">
									<li><a href="javascript:void(0)">Option One</a></li>
									<li><a href="javascript:void(0)">Option Two</a></li>
									<li><a href="javascript:void(0)">Option Three</a></li>
								</ul></li>
							<li><a href="javascript:void(0)" data-chat="close"> <i
									class="zmdi zmdi-close"></i>
							</a></li>
						</ul>
					</div>
					<div class='chat-wrapper scrollbar'>
						<div class='chat-message scrollbar'>
							<div class='chat-message chat-message-recipient'>
								<img class='chat-image chat-image-default'
									src='img/profiles/05.jpg' />
								<div class='chat-message-wrapper'>
									<div class='chat-message-content'>
										<p>Hey Mike, we have funding for our new project!</p>
									</div>
									<div class='chat-details'>
										<span class='today small'></span>
									</div>
								</div>
							</div>
							<div class='chat-message chat-message-sender'>
								<img class='chat-image chat-image-default'
									src='img/profiles/02.jpg' />
								<div class='chat-message-wrapper '>
									<div class='chat-message-content'>
										<p>Awesome! Photo booth banh mi pitchfork kickstarter
											whatever, prism godard ethical 90's cray selvage.</p>
									</div>
									<div class='chat-details'>
										<span class='today small'></span>
									</div>
								</div>
							</div>
							<div class='chat-message chat-message-recipient'>
								<img class='chat-image chat-image-default'
									src='img/profiles/05.jpg' />
								<div class='chat-message-wrapper'>
									<div class='chat-message-content'>
										<p>Artisan glossier vaporware meditation paleo humblebrag
											forage small batch.</p>
									</div>
									<div class='chat-details'>
										<span class='today small'></span>
									</div>
								</div>
							</div>
							<div class='chat-message chat-message-sender'>
								<img class='chat-image chat-image-default'
									src='img/profiles/02.jpg' />
								<div class='chat-message-wrapper'>
									<div class='chat-message-content'>
										<p>Bushwick letterpress vegan craft beer dreamcatcher
											kickstarter.</p>
									</div>
									<div class='chat-details'>
										<span class='today small'></span>
									</div>
								</div>
							</div>
						</div>
					</div>
					<footer id="compose-footer">
						<form class="form-horizontal compose-form">
							<ul class="card-actions icons left-bottom">
								<li><a href="javascript:void(0)"> <i
										class="zmdi zmdi-attachment-alt"></i>
								</a></li>
								<li><a href="javascript:void(0)"> <i
										class="zmdi zmdi-mood"></i>
								</a></li>
							</ul>
							<div class="form-group m-10 p-l-75 is-empty">
								<div class="input-group">
									<label class="sr-only">Leave a comment...</label> <input
										type="text" class="form-control form-rounded input-lightGray"
										placeholder="Leave a comment.."> <span
										class="input-group-btn">
										<button type="button" class="btn btn-blue btn-fab  btn-fab-sm">
											<i class="zmdi zmdi-mail-send"></i>
										</button>
									</span>
								</div>
							</div>
						</form>
					</footer>
				</section>
			</div>
		</section>
		<aside id="app_sidebar-right">
			<div class="sidebar-inner sidebar-overlay">
				<div class="tabpanel">
					<ul class="nav nav-tabs nav-justified">
						<li class="active" role="presentation"><a
							href="#sidebar_chat" data-toggle="tab" aria-expanded="true">Chat</a></li>
						<li role="presentation"><a href="#sidebar_activity"
							data-toggle="tab">Activity</a></li>
					</ul>
					<div class="tab-content">
						<div class="tab-pane fade active in" id="sidebar_chat">
							<form class="m-l-15 m-r-15 m-t-30">
								<div class="input-group search-target">
									<span class="input-group-addon"><i
										class="zmdi zmdi-search"></i></span>
									<div class="form-group is-empty">
										<input type="text" value="" placeholder="Filter contacts..."
											class="form-control" data-search-trigger="open">
									</div>
								</div>
							</form>
							<ul class="description">
								<li class="title">Online</li>
							</ul>
							<ul class="list-group p-0">
								<li class="list-group-item" data-chat="open"
									data-chat-name="John Smith"><span class="pull-left"><img
										src="img/profiles/07.jpg" alt=""
										class="img-circle max-w-40 m-r-10 "></span> <i
									class="badge mini success status"></i>
									<div class="list-group-item-body">
										<div class="list-group-item-heading">John Smith</div>
										<div class="list-group-item-text">New York, NY</div>
									</div></li>
								<li class="list-group-item" data-chat="open"
									data-chat-name="Allison Grayce"><span class="pull-left"><img
										src="img/profiles/05.jpg" alt=""
										class="img-circle max-w-40 m-r-10 "></span> <i
									class="badge mini success status"></i>
									<div class="list-group-item-body">
										<div class="list-group-item-heading">Allison Grayce</div>
										<div class="list-group-item-text">Seattle, WA</div>
									</div></li>
								<li class="list-group-item" data-chat="open"
									data-chat-name="Ashley Ford"><span class="pull-left"><img
										src="img/profiles/18.jpg" alt=""
										class="img-circle max-w-40 m-r-10 "></span> <i
									class="badge mini success status"></i>
									<div class="list-group-item-body">
										<div class="list-group-item-heading">Ashley Ford</div>
										<div class="list-group-item-text">Denver, CO</div>
									</div></li>
								<li class="list-group-item" data-chat="open"
									data-chat-name="Johanna Kollmann"><span class="pull-left"><img
										src="img/profiles/11.jpg" alt=""
										class="img-circle max-w-40 m-r-10 "></span> <i
									class="badge mini success status"></i>
									<div class="list-group-item-body">
										<div class="list-group-item-heading">Johanna Kollmann</div>
										<div class="list-group-item-text">Palo Alto, Ca</div>
									</div></li>
							</ul>
							<ul class="description">
								<li class="title">Busy</li>
							</ul>
							<ul class="list-group p-0">
								<li class="list-group-item" data-chat="open"
									data-chat-name="Mike Jones"><span class="pull-left"><img
										src="img/profiles/03.jpg" alt=""
										class="img-circle max-w-40 m-r-10 "></span> <i
									class="badge mini warning status"></i>
									<div class="list-group-item-body">
										<div class="list-group-item-heading">Mike Jones</div>
										<div class="list-group-item-text">San Francisco, CA</div>
									</div></li>
								<li class="list-group-item" data-chat="open"
									data-chat-name="Nikki Clark"><span class="pull-left"><img
										src="img/profiles/06.jpg" alt=""
										class="img-circle max-w-40 m-r-10 "></span> <i
									class="badge mini warning status"></i>
									<div class="list-group-item-body">
										<div class="list-group-item-heading">Nikki Clark</div>
										<div class="list-group-item-text">Sarasota, FL</div>
									</div></li>
								<li class="list-group-item" data-chat="open"
									data-chat-name="Jason Kendall"><span class="pull-left"><img
										src="img/profiles/15.jpg" alt=""
										class="img-circle max-w-40 m-r-10 "></span> <i
									class="badge mini warning status"></i>
									<div class="list-group-item-body">
										<div class="list-group-item-heading">Jason Kendall</div>
										<div class="list-group-item-text">New York, NY</div>
									</div></li>
							</ul>
							<ul class="description">
								<li class="title">Offline</li>
							</ul>
							<ul class="list-group p-0">
								<li class="list-group-item" data-chat="open"
									data-chat-name="Josh Hemsley"><span class="pull-left"><img
										src="img/profiles/16.jpg" alt=""
										class="img-circle max-w-40 m-r-10 "></span> <i
									class="badge mini danger status"></i>
									<div class="list-group-item-body">
										<div class="list-group-item-heading">Josh Hemsley</div>
										<div class="list-group-item-text">Salem, MA</div>
									</div></li>
								<li class="list-group-item" data-chat="open"
									data-chat-name="James Hart"><span class="pull-left"><img
										src="img/profiles/09.jpg" alt=""
										class="img-circle max-w-40 m-r-10 "></span> <i
									class="badge mini danger status"></i>
									<div class="list-group-item-body">
										<div class="list-group-item-heading">James Hart</div>
										<div class="list-group-item-text">Salem, MA</div>
									</div></li>
							</ul>
							<button class="btn btn-primary btn-fab btn-fab-sm animate-fab"
								data-chat="open" id="chat_fab_new">
								<i class="zmdi zmdi-plus"></i>
							</button>
						</div>
						<div class="tab-pane fade" id="sidebar_activity">
							<div class="sidebar-timeline">
								<div class="time-item">
									<div class="item-info">
										<small class="text-muted">15 minutes ago</small>
										<p>
											<a href="#" class="accent">Mike Jones</a> fixed z-index
											conflict sidebar.scss
										</p>
									</div>
								</div>
								<div class="time-item">
									<div class="item-info">
										<small class="text-muted">30 minutes ago</small>
										<p>
											<a href="javascript:void(0)" class="accent">Hazel Dean</a>
											left a comment on product page designs.
										</p>
										<p>
											<em>"Yuccie shoreditch trust fund, artisan tumblr
												sustainable cronut unicorn blog seitan. "</em>
										</p>
									</div>
								</div>
								<div class="time-item">
									<div class="item-info">
										<small class="text-muted">45 minutes ago</small>
										<p>
											<a href="javascript:void(0)" class="accent">Molly</a>
											requested time off for training.
										</p>
										<p>
											<em>"Snackwave church-key cardigan you probably haven't
												heard of them, asymmetrical microdosing cronut "</em>
										</p>
									</div>
								</div>
								<div class="time-item">
									<div class="item-info">
										<small class="text-muted">3 hours ago</small>
										<p>
											<a href="javascript:void(0)" class="accent">Frederick Roy</a>
											commented your post.
										</p>
										<p>
											<em>"Skateboard dreamcatcher la croix, edison bulb
												sustainable sriracha vexillologist kombucha master cleanse."</em>
										</p>
									</div>
								</div>
								<div class="time-item">
									<div class="item-info">
										<small class="text-muted">1 hour ago</small>
										<p>
											<a href="javascript:void(0)" class="accent">Holly Cobb</a>
											Uploaded 6 new photos.
										</p>
									</div>
								</div>
								<div class="time-item">
									<div class="item-info">
										<small class="text-muted">5 hours ago</small>
										<p>
											<a href="javascript:void(0)" class="accent">Neal Stephens</a>
											setup a meeting with<a href="#" class="text-success">
												Jason Kendall</a>.
										</p>
										<p>
											<em>"Authentic aesthetic tattooed, PBR&B squid tote bag
												schlitz vaporware glossier yr man braid direct trade disrupt
												poke. "</em>
										</p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</aside>
	</div>
	<!-- MODALS =============================================-->
	<div class="modal fade" id="contactEditUser" tabindex="-1"
		role="dialog" aria-labelledby="contactLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<ul class="card-actions icons right-top">
						<li><a href="javascript:void(0)" class="text-white"
							data-dismiss="modal" aria-label="Close"> <i
								class="zmdi zmdi-close"></i>
						</a></li>
					</ul>
					<h4 class="modal-title">Edit Contact</h4>
					<div class="user-avatar-wrapper">
						<figure>
							<div class="icon-upload">
								<label for="file-input"> <span class="edit-avatar">
										<img src="" alt="" class="avatar img-circle animated zoomIn" />
								</span>
								</label>
							</div>
							<figcaption>
								<h5 class="name animated fadeInUp"></h5>
								<ul class="card-actions icons">
									<li><a href="javascript:void(0)"><i
											class="zmdi star zmdi-star-outline"></i></a></li>
									<li class="dropdown"><a href="javascript:void(0)"
										data-toggle="dropdown"> <i class="zmdi zmdi-more-vert"></i>
									</a>
										<ul class="dropdown-menu p-15">
											<li>
												<div class="checkbox">
													<label class="checkbox-inline action"> <input
														type="checkbox" id="" value=""><span
														class="checkbox-material"></span> Archive
													</label>
												</div>
											</li>
											<li>
												<div class="checkbox">
													<label class="checkbox-inline action"> <input
														type="checkbox" checked="" id="" value=""><span
														class="checkbox-material"></span> Delete
													</label>
												</div>
											</li>
											<li>
												<div class="checkbox">
													<label class="checkbox-inline action"> <input
														type="checkbox" checked="" id="" value=""><span
														class="checkbox-material"></span> Block
													</label>
												</div>
											</li>
										</ul></li>
								</ul>
							</figcaption>
						</figure>
					</div>
				</div>
				<div class="modal-body">
					<div class="form-group label-floating">
						<div class="input-group">
							<span class="input-group-addon"><i
								class="zmdi zmdi-account"></i></span> <label class="control-label">Full
								Name</label> <input type="text" class="form-control" id="edit_name"
								value="name">
						</div>
					</div>
					<div class="form-group label-floating">
						<div class="input-group">
							<span class="input-group-addon"><i class="zmdi zmdi-email"></i></span>
							<label class="control-label">Email Address</label> <input
								type="email" class="form-control" id="edit_email"
								value="email@chatapplication.pro">
						</div>
					</div>
					<div class="form-group label-floating">
						<div class="input-group">
							<span class="input-group-addon"><i class="zmdi zmdi-phone"></i></span>
							<label class="control-label">Phone Number</label> <input
								type="text" class="form-control" id="edit_phone" value="phone">
						</div>
					</div>
					<div class="form-group label-floating is-empty">
						<div class="input-group">
							<span class="input-group-addon"><i class="zmdi zmdi-pin"></i></span>
							<label class="control-label">Address</label> <input type="text"
								class="form-control" id="edit_address">
						</div>
					</div>
					<div class="form-group label-floating is-empty">
						<div class="input-group">
							<span class="input-group-addon"><i
								class="mdi mdi-note-text"></i></span> <label for="textArea"
								class="control-label">Notes</label>
							<textarea class="form-control" rows="3" id="edit_notes"></textarea>
						</div>
					</div>
				</div>
				
				<div class="sign_text_main_div_btm">
						<span id="errorMessage" style="color:red;"></span>
                <div class="modal-footer">
                    <button class="btn btn-info invite_user_btn" style=" float: inherit;" type="button" id="inviteBtn" >Invite</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal"  style=" float: right;">Close</button>
                </div>
            </div>
				
			</div>
		</div>
	</div>
	<div class="modal fade" id="newContactModal" tabindex="-1"
		role="dialog" aria-labelledby="newContactModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<ul class="card-actions icons right-top">
						<li><a href="javascript:void(0)" class="text-white"
							data-dismiss="modal" aria-label="Close"> <i
								class="zmdi zmdi-close"></i>
						</a></li>
					</ul>
					<h4 class="modal-title">New Contact</h4>
					<div class="user-avatar-wrapper">
						<figure>
							<div class="icon-upload">
								<label for="file-input"> <span class="edit-avatar">
										<div class="no-avatar app_primary_lighten_bg animated zoomIn"></div>
								</span>
								</label>
							</div>
							<figcaption>
								<h5 class="name"></h5>
								<ul class="card-actions icons">
									<li><a href="javascript:void(0)"><i
											class="zmdi star zmdi-star-outline"></i></a></li>
									<li class="dropdown"><a href="javascript:void(0)"
										data-toggle="dropdown"> <i class="zmdi zmdi-more-vert"></i>
									</a>
										<ul class="dropdown-menu p-15">
											<li>
												<div class="checkbox">
													<label class="checkbox-inline action"> <input
														type="checkbox" id="" value=""><span
														class="checkbox-material"></span> Archive
													</label>
												</div>
											</li>
											<li>
												<div class="checkbox">
													<label class="checkbox-inline action"> <input
														type="checkbox" id="" value=""><span
														class="checkbox-material"></span> Delete
													</label>
												</div>
											</li>
											<li>
												<div class="checkbox">
													<label class="checkbox-inline action"> <input
														type="checkbox" id="" value=""><span
														class="checkbox-material"></span> Block
													</label>
												</div>
											</li>
										</ul></li>
								</ul>
							</figcaption>
						</figure>
					</div>
				</div>
				<div class="modal-body">
					<div class="form-group label-floating">
						<div class="input-group">
							<span class="input-group-addon"><i
								class="zmdi zmdi-account"></i></span> <label class="control-label">Full
								Name</label> <input type="text" class="form-control" id="add_name"
								value="">
						</div>
					</div>
					<div class="form-group label-floating">
						<div class="input-group">
							<span class="input-group-addon"><i class="zmdi zmdi-email"></i></span>
							<label class="control-label">Email Address</label> <input
								type="email" class="form-control" id="add_email" value="">
						</div>
					</div>
					<div class="form-group label-floating">
						<div class="input-group">
							<span class="input-group-addon"><i class="zmdi zmdi-phone"></i></span>
							<label class="control-label">Phone Number</label> <input
								type="text" class="form-control" id="add_phone" value="">
						</div>
					</div>
					<div class="form-group label-floating is-empty">
						<div class="input-group">
							<span class="input-group-addon"><i class="zmdi zmdi-pin"></i></span>
							<label class="control-label">Address</label> <input type="text"
								class="form-control" id="add_address">
						</div>
					</div>
					<div class="form-group label-floating is-empty">
						<div class="input-group">
							<span class="input-group-addon"><i
								class="mdi mdi-note-text"></i></span> <label for="textArea"
								class="control-label">Notes</label>
							<textarea class="form-control" rows="3" id="textArea"
								id="add_notes"></textarea>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button class="btn btn-default btn-flat pull-left">Delete</button>
					<button class="btn btn-primary btn-flat" data-dismiss="modal"
						aria-label="Close">Cancel</button>
					<button class="btn btn-primary">Save</button>
				</div>
			</div>
		</div>
	</div>
	
	
	<div class="bg_transparent_div" style="display:none;"> </div>
	
	<div id="divPreloader" style="display: none;">
			<div id="divPreloaderStatus"></div>
	</div>
		
	<!-- END MODALS =============================================-->
	
	<!-- Modal -->
  <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-sm">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        <!--   <h4 class="modal-title">Modal Header</h4> -->
        </div>
       
        <div class="modal-footer">
        
        	<div class="controlButtons" style="width: 100%; float: left;">
						<div id="controlButtons" style="text-align:center;">
						
		<input name="convertSoundFile" id="hidenInputMp3" type="hidden"
			style="position: relative; left: 5px; width: 73%;" />
			
			
							<div class="recordButton">
								<img  style="cursor: pointer;" src="../resources/assets/img/Microphone.png" id="recordButton" class="recordOff" />
							</div>
						<!-- 	<span id="recordHelp">Allow your microphone.</span> -->
							<div id="recordContainer" class="startContainer">
								<div id="recordTime">
									<span id="sw_m">00</span>:<span id="sw_s">00</span>:<span
										id="sw_ms">00</span>
										<div id="recordText">Record</div>
								</div>
								<div id="recordCircle" class="startRecord">
									
								</div>
							</div>
							<div id="recdListInManageVoice"
								class="newsfeedContainer recordingslist"></div>
						</div>
					</div>
					
        
          <button type="button" class="btn" onclick="voiceSaveMP3()" id="voiceSendButton" style="display:none;">SEND</button>
          
           <!-- data-dismiss="modal" -->
        </div>
      </div>
      
    </div>
  </div>
<div id="new_group" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">

            <div class="modal-body invite_user_div">
                <div class="profile_pic_upload">
                    <div class="profile_pic_upload_main_div group_profile">
                        <div class="box">
                            <input type="file" onchange="readURL111(this);" value="Select Profile Picture" name="file-1[]" id="file-grp" class="inputfile inputfile-1" data-multiple-caption="{count} files selected" multiple />
                            <label for="file-1"><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAOUSURBVGhD7ZhLSFVRFIY1NSUqkyiMalhBYQMHjZxkNnFQhNCDCkICkWgQEUFBo+g1KIWeQtDAgb2ohKCaRNCgsGEQmQ572QOpVLLSvrX3f7GL3jwvvV64H/ysfdbe6797ec4953oK8uTJMz2Mjo7OQhUhNEelMwM2VDYyMnIJDTEOBTWvUK2ssgsbadO+IkH9AFoju+zAPurYxIjfUnSweEoolG1yYFyK8UTX9b+qZF0PMRHwaiZM9Dlp0hYzw6JCzJrQS8YzFvY3hO6gKm19DOYL0TW3MkegEftupd8oyO/x07kFjXxE89WG+04801zOwd4b1YZr5L/PAOYH0bAOZxTsq0VtuEsrIyxsJRQT56FOnw0G640P6IdSiYP3RbWRuREW/SKUaZmduSo/kxnWPEcH0AoOS1RqnzGX3AbUij65xQmAV6BGjCVaZo2s19Q4mOtGm7XUQdrO5FJi2v2f4wXkTyD7Q8UCj8kbMVhoT90aYj3q9tl0yD8glMvLNnmU+IL4x+YNxt/QLbTJfSgwrkVftCQS1AdrZDIwekgoMh/GO9FnP5MZ1jxBy1WzFg1oKjTUxm8EEztD7kwwPuazwWC93QTc05nYoHRoqE2kEfedIO5QKhTU9aKF8rDLMzTUxWsEgy7VljOe9HLKBLXnzYdYrVQokmjkoNUSjygVCeqH0WJ5vVY6MNTEbmSV1RK7lIoMHnvldU6pwMRuBGajIox++8Po4HHZ9kHcp1RgYjVCcZ/qKn0mHvjdNT/iFqUCE7eRQdWV+0w88OswP+IupQITqxGRepL3+8PosJnT5kU8pFRgYjeCwUbV3vCZ6ODl/tMj3lQqMEk0krr/1ysVCep7CSXEUvTdZ4NDTexGvhLcr1rGj302PNRulUeTUqGI3YiByRmrJy5Db5UODDVt+nz7X+W9z4aDukQasWdInXkwXo0Cv+di7RWCPYvsNdR1l4wAtfEbEf2YVcungnEL+umnxsPcG7RN6+011Ck3ERG8EmvEzOwd03bZ2dlZhBrRBXQPtaOTyP7DLNYaewcQ+UykwCO5RlJg+gitk+2EsMze3jejd74qHlPSSArMe5BdYvtRA9qNDqPbKNE3KvhNXSPTSVojHPQpn3Ow9+NqwzXSrnwuUqM2XCMrUeQ3GdmCPXeqhTFI2jumnLnE2Ot9gvsFPg4m7OWa3RqvMrZftTNK7KsDnUXul3eePHny5MkyBQV/AeCRbg3kR4fDAAAAAElFTkSuQmCC"></label>
                        </div>



                        <img id="blah" class="group_img" src="<spring:url value="/resources/assets/img/sample_profile_pic.png"/>" alt="your image" />
                    </div>
                </div>
                <h2 class="new_group_class">New Group</h2>
                <div class="group_name_div">
                    <input name="group_name" type="text" class="form-control group_name_text_box" placeholder="Enter group name" id="groupNameFirstModel" autofocus="true">
                    <input name="group_name" type="text" class="form-control group_name_dsc_text_box" placeholder="Description" id="discrpOfGrp" autofocus="true">
                </div>
            </div>
            <div class="modal-footer">
            <button class="btn btn-info invite_user_btn" type="submit" data-toggle="modal" id="groupSaveNextBtn" style="float: inherit;" >Next</button>
           </div>
        </div>

    </div>
</div>
<div id="new_group_info" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">

            <div class="modal-body invite_user_div">
                <div class="profile_pic_upload">
                    <div class="profile_pic_upload_main_div group_profile">
                        <div class="box">
                            <input type="file" onchange="readURL111(this);" value="Select Profile Picture" name="file-1[]" id="file-1" class="inputfile inputfile-1" data-multiple-caption="{count} files selected" multiple />
                            <label for="file-1"><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAOUSURBVGhD7ZhLSFVRFIY1NSUqkyiMalhBYQMHjZxkNnFQhNCDCkICkWgQEUFBo+g1KIWeQtDAgb2ohKCaRNCgsGEQmQ572QOpVLLSvrX3f7GL3jwvvV64H/ysfdbe6797ec4953oK8uTJMz2Mjo7OQhUhNEelMwM2VDYyMnIJDTEOBTWvUK2ssgsbadO+IkH9AFoju+zAPurYxIjfUnSweEoolG1yYFyK8UTX9b+qZF0PMRHwaiZM9Dlp0hYzw6JCzJrQS8YzFvY3hO6gKm19DOYL0TW3MkegEftupd8oyO/x07kFjXxE89WG+04801zOwd4b1YZr5L/PAOYH0bAOZxTsq0VtuEsrIyxsJRQT56FOnw0G640P6IdSiYP3RbWRuREW/SKUaZmduSo/kxnWPEcH0AoOS1RqnzGX3AbUij65xQmAV6BGjCVaZo2s19Q4mOtGm7XUQdrO5FJi2v2f4wXkTyD7Q8UCj8kbMVhoT90aYj3q9tl0yD8glMvLNnmU+IL4x+YNxt/QLbTJfSgwrkVftCQS1AdrZDIwekgoMh/GO9FnP5MZ1jxBy1WzFg1oKjTUxm8EEztD7kwwPuazwWC93QTc05nYoHRoqE2kEfedIO5QKhTU9aKF8rDLMzTUxWsEgy7VljOe9HLKBLXnzYdYrVQokmjkoNUSjygVCeqH0WJ5vVY6MNTEbmSV1RK7lIoMHnvldU6pwMRuBGajIox++8Po4HHZ9kHcp1RgYjVCcZ/qKn0mHvjdNT/iFqUCE7eRQdWV+0w88OswP+IupQITqxGRepL3+8PosJnT5kU8pFRgYjeCwUbV3vCZ6ODl/tMj3lQqMEk0krr/1ysVCep7CSXEUvTdZ4NDTexGvhLcr1rGj302PNRulUeTUqGI3YiByRmrJy5Db5UODDVt+nz7X+W9z4aDukQasWdInXkwXo0Cv+di7RWCPYvsNdR1l4wAtfEbEf2YVcungnEL+umnxsPcG7RN6+011Ck3ERG8EmvEzOwd03bZ2dlZhBrRBXQPtaOTyP7DLNYaewcQ+UykwCO5RlJg+gitk+2EsMze3jejd74qHlPSSArMe5BdYvtRA9qNDqPbKNE3KvhNXSPTSVojHPQpn3Ow9+NqwzXSrnwuUqM2XCMrUeQ3GdmCPXeqhTFI2jumnLnE2Ot9gvsFPg4m7OWa3RqvMrZftTNK7KsDnUXul3eePHny5MkyBQV/AeCRbg3kR4fDAAAAAElFTkSuQmCC"></label>
                        </div>
                        <img id="groupImageEdit" class="group_img" src="<spring:url value="/resources/assets/img/sample_profile_pic.png"/>" alt="your image" />
                    </div>
                </div>
                <div class="group_name_edit">
					<input type="text" value="Group Name"  id="groupNameEdit" class="group_name_edit">
				</div>
                <div class="group_description_edit">
					<input type="text" value="edit Group Description" id="groupDescriptionEdit" class="group_disc_edit">
				</div>
				<div class="add_group_list">
				<a href="#" data-toggle="modal" id="addMenberToGrp" data-target="#new_group_members">+Add members</a>
			<!-- 	<a class="js-open-modal btn" href="#" data-modal-id="popup1"> Pop Up One</a>  -->
				<a href="#" data-toggle="modal" id="addMenberToGrp_edit" data-target="#new_group_members_Edit" style="display:none;">+Add members</a>
				
				</div>
                <div class="chat_member_list">
					<ul class="chat_list" id="user_list_group">

						<!-- ### grp menmberListing	<li class="list-group-item contactPool group_list_members" >
								<div class="profile_pic">
									<span class="pull-left"><img src="" alt="" class="img-circle img-sm m-r-10 mCS_img_loaded"></span> 
								</div>
								<div class="list-group-item-body name_list_div">
									<div class="list-group-item-heading userNameDivEdit">Ranjith VR</div>
									<div class="list-group-item-text">ranju@gmail.com</div>

								</div>
								<div class="pos_group">
									<span class="group_owner_color">Group Owner</span>
								</div>
								
									<div class="pos_group">
									<div class="dropdown">
									  <button onclick="myFunction()" class="dropbtn">Group Admin</button>
									  <div id="myDropdown" class="dropdown-content">
										<a href="#">Grant Admin role</a>
										<a href="#">Remove from group</a>
									  </div>
									</div>
								</div>
							</li> -->
							
						

						</ul>
				</div>
            </div>
            <div class="modal-footer">
           <!--      <button class="btn btn-info invite_user_btn" type="submit" data-toggle="modal" style="float: inherit;" data-target="#new_group_members">Next</button>
        -->     </div>
        </div>

    </div>
</div>

<div id="popup1" class="modal-box">
  <header> <a href="#" class="js-modal-close close">×</a>
    <h3><center>Edit profile</center></h3>
  </header>
  <div class="modal-body">
  
      <div class="login-body">
                <div class="profile_pic_upload">
                    <div class="profile_pic_upload_main_div">
                        <div class="box">
                            <input type="file" onchange="readURLEditProfile(this);" value="Select Profile Picture" name="file-11[]" id="file-editprofile" class="inputfile inputfile-1" data-multiple-caption="{count} files selected" multiple />
                            <label for="file-editprofile"><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAOUSURBVGhD7ZhLSFVRFIY1NSUqkyiMalhBYQMHjZxkNnFQhNCDCkICkWgQEUFBo+g1KIWeQtDAgb2ohKCaRNCgsGEQmQ572QOpVLLSvrX3f7GL3jwvvV64H/ysfdbe6797ec4953oK8uTJMz2Mjo7OQhUhNEelMwM2VDYyMnIJDTEOBTWvUK2ssgsbadO+IkH9AFoju+zAPurYxIjfUnSweEoolG1yYFyK8UTX9b+qZF0PMRHwaiZM9Dlp0hYzw6JCzJrQS8YzFvY3hO6gKm19DOYL0TW3MkegEftupd8oyO/x07kFjXxE89WG+04801zOwd4b1YZr5L/PAOYH0bAOZxTsq0VtuEsrIyxsJRQT56FOnw0G640P6IdSiYP3RbWRuREW/SKUaZmduSo/kxnWPEcH0AoOS1RqnzGX3AbUij65xQmAV6BGjCVaZo2s19Q4mOtGm7XUQdrO5FJi2v2f4wXkTyD7Q8UCj8kbMVhoT90aYj3q9tl0yD8glMvLNnmU+IL4x+YNxt/QLbTJfSgwrkVftCQS1AdrZDIwekgoMh/GO9FnP5MZ1jxBy1WzFg1oKjTUxm8EEztD7kwwPuazwWC93QTc05nYoHRoqE2kEfedIO5QKhTU9aKF8rDLMzTUxWsEgy7VljOe9HLKBLXnzYdYrVQokmjkoNUSjygVCeqH0WJ5vVY6MNTEbmSV1RK7lIoMHnvldU6pwMRuBGajIox++8Po4HHZ9kHcp1RgYjVCcZ/qKn0mHvjdNT/iFqUCE7eRQdWV+0w88OswP+IupQITqxGRepL3+8PosJnT5kU8pFRgYjeCwUbV3vCZ6ODl/tMj3lQqMEk0krr/1ysVCep7CSXEUvTdZ4NDTexGvhLcr1rGj302PNRulUeTUqGI3YiByRmrJy5Db5UODDVt+nz7X+W9z4aDukQasWdInXkwXo0Cv+di7RWCPYvsNdR1l4wAtfEbEf2YVcungnEL+umnxsPcG7RN6+011Ck3ERG8EmvEzOwd03bZ2dlZhBrRBXQPtaOTyP7DLNYaewcQ+UykwCO5RlJg+gitk+2EsMze3jejd74qHlPSSArMe5BdYvtRA9qNDqPbKNE3KvhNXSPTSVojHPQpn3Ow9+NqwzXSrnwuUqM2XCMrUeQ3GdmCPXeqhTFI2jumnLnE2Ot9gvsFPg4m7OWa3RqvMrZftTNK7KsDnUXul3eePHny5MkyBQV/AeCRbg3kR4fDAAAAAElFTkSuQmCC"></label>
                        </div>


           
               <img id="editprofilePic"  src="<spring:url value="/resources/assets/img/sample_profile_pic.png"/>" alt="your image" />
                    </div>
                </div>
                <div class="form-signin">

					<div class="sign_text_main_div">
                        <div class="form-group label-floating text_box_div">
                            <input name="firstname" id="firstNameEdit" type="text" class="form-control" placeholder="first name" autofocus="true" />
                        </div>
                        <div class="form-group label-floating text_box_div">
                            <input name="lastName" id="lastNameEdit" type="text" class="form-control" placeholder="last name" autofocus="true" />
                        </div>
                        
                        <div class="form-group label-floating text_box_div">
							<div class="gender_div">
								<!-- <div class="gender_div_male_edit"><input type="radio" name="gender" id="maleRadioBtn_Edit" value="male"> <span>Male</span></div>
								<div class="gender_div_male_edit"><input type="radio" name="gender"  id="female_gender_div" value="female"> <span>Female</span></div> -->
                        	
                        		<div class="gender_div_male_edit"><input type="radio" name="gender" id="maleRadioBtn_Edit" value="male" > <span>Male</span></div>
								<div class="gender_div_male_edit" id="female_gender_div"><input type="radio" name="gender"  id="femaleRadioBtn_Edit" value="female"> <span>Female</span></div>
                        	    
                        	</div>
	                        <div class="form-group label-floating text_box_div">
	                            <input name="username" id="userNameEdit" type="text" class="form-control"  disabled />
	                        </div>
	                        <div class="form-group label-floating text_box_div">
	                            <input name="emailid" id="emailIdEdit" type="text" class="form-control"  disabled />
	                        </div>
                        
						</div>
					<div class="sign_text_main_div_btm">
						<span id="errorMessageEdit" style="color:red;"></span>
						<button class="btnUpdate" id="updateProfileBtn" style="width: 100%;" >update profile</button>
						
                    </div>

                </div>
            </div>

        </div>
  
    </div>
  <!-- <footer> <a href="#" class="btn btn-small js-modal-close">Close</a> </footer> -->
</div>

<!--   edit group deatils  -->
<div id="new_group_info_edit" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">

            <div class="modal-body invite_user_div">
                <div class="profile_pic_upload">
                    <div class="profile_pic_upload_main_div group_profile">
                        <div class="box">
                            <input type="file" onchange="readURL111(this);" value="Select Profile Picture" name="file-1[]" id="file-1" class="inputfile inputfile-1" data-multiple-caption="{count} files selected" multiple />
                            <label for="file-1"><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAOUSURBVGhD7ZhLSFVRFIY1NSUqkyiMalhBYQMHjZxkNnFQhNCDCkICkWgQEUFBo+g1KIWeQtDAgb2ohKCaRNCgsGEQmQ572QOpVLLSvrX3f7GL3jwvvV64H/ysfdbe6797ec4953oK8uTJMz2Mjo7OQhUhNEelMwM2VDYyMnIJDTEOBTWvUK2ssgsbadO+IkH9AFoju+zAPurYxIjfUnSweEoolG1yYFyK8UTX9b+qZF0PMRHwaiZM9Dlp0hYzw6JCzJrQS8YzFvY3hO6gKm19DOYL0TW3MkegEftupd8oyO/x07kFjXxE89WG+04801zOwd4b1YZr5L/PAOYH0bAOZxTsq0VtuEsrIyxsJRQT56FOnw0G640P6IdSiYP3RbWRuREW/SKUaZmduSo/kxnWPEcH0AoOS1RqnzGX3AbUij65xQmAV6BGjCVaZo2s19Q4mOtGm7XUQdrO5FJi2v2f4wXkTyD7Q8UCj8kbMVhoT90aYj3q9tl0yD8glMvLNnmU+IL4x+YNxt/QLbTJfSgwrkVftCQS1AdrZDIwekgoMh/GO9FnP5MZ1jxBy1WzFg1oKjTUxm8EEztD7kwwPuazwWC93QTc05nYoHRoqE2kEfedIO5QKhTU9aKF8rDLMzTUxWsEgy7VljOe9HLKBLXnzYdYrVQokmjkoNUSjygVCeqH0WJ5vVY6MNTEbmSV1RK7lIoMHnvldU6pwMRuBGajIox++8Po4HHZ9kHcp1RgYjVCcZ/qKn0mHvjdNT/iFqUCE7eRQdWV+0w88OswP+IupQITqxGRepL3+8PosJnT5kU8pFRgYjeCwUbV3vCZ6ODl/tMj3lQqMEk0krr/1ysVCep7CSXEUvTdZ4NDTexGvhLcr1rGj302PNRulUeTUqGI3YiByRmrJy5Db5UODDVt+nz7X+W9z4aDukQasWdInXkwXo0Cv+di7RWCPYvsNdR1l4wAtfEbEf2YVcungnEL+umnxsPcG7RN6+011Ck3ERG8EmvEzOwd03bZ2dlZhBrRBXQPtaOTyP7DLNYaewcQ+UykwCO5RlJg+gitk+2EsMze3jejd74qHlPSSArMe5BdYvtRA9qNDqPbKNE3KvhNXSPTSVojHPQpn3Ow9+NqwzXSrnwuUqM2XCMrUeQ3GdmCPXeqhTFI2jumnLnE2Ot9gvsFPg4m7OWa3RqvMrZftTNK7KsDnUXul3eePHny5MkyBQV/AeCRbg3kR4fDAAAAAElFTkSuQmCC"></label>
                        </div>
                        <img id="groupImageEdit" class="group_img" src="<spring:url value="/resources/assets/img/sample_profile_pic.png"/>" alt="your image" />
                    </div>
                </div>
                <div class="group_name_edit">
					<input type="text" value="Group Name"  id="groupNameEdit1" class="group_name_edit">
				</div>
                <div class="group_description_edit">
					<input type="text" value="edit Group Description" id="groupDescriptionEdit" class="group_disc_edit">
				</div>
				<div class="add_group_list">
				
				</div>
                <div class="chat_member_list">
					<ul class="chat_list" id="user_list_group">

						<!-- ### grp menmberListing	<li class="list-group-item contactPool group_list_members" >
								<div class="profile_pic">
									<span class="pull-left"><img src="" alt="" class="img-circle img-sm m-r-10 mCS_img_loaded"></span> 
								</div>
								<div class="list-group-item-body name_list_div">
									<div class="list-group-item-heading userNameDivEdit">Ranjith VR</div>
									<div class="list-group-item-text">ranju@gmail.com</div>

								</div>
								<div class="pos_group">
									<span class="group_owner_color">Group Owner</span>
								</div>
								
									<div class="pos_group">
									<div class="dropdown">
									  <button onclick="myFunction()" class="dropbtn">Group Admin</button>
									  <div id="myDropdown" class="dropdown-content">
										<a href="#">Grant Admin role</a>
										<a href="#">Remove from group</a>
									  </div>
									</div>
								</div>
							</li> -->
							
						

						</ul>
				</div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-info invite_user_btn" type="submit" data-toggle="modal" style="float: inherit;" data-target="#new_group_members">update</button>
            </div>
        </div>

    </div>
</div>


<!-- Modal -->
    <div id="invite_user" class="modal fade" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">

                <div class="modal-body invite_user_div">
                    <h2>invite new user</h2>
                    <p>Enter email of a new user for sending invitation to join the chat</p>
                    <div class="invite_email">
                        <div class="form-group label-floating is-empty ">
                            <span></span>
                            <!--  <label class="control-label">Email</label> -->
                            <!-- <input type="email" class="form-control"> -->
                            <input name="username" style="text-align:center;" id="inviteMailId"  type="email" class="form-control" placeholder="email" onkeyup="clearErrorMessage()" autofocus="true">
                        </div>
                        <div><lable id="errorMessageInInvite"  style="color:blue;" ></lable></div>
                        
                        	<div class="chat_member_list_invit" style="display:none;">

							<ul class="chat_list" id="chat_list_search">
								<li class="list-group-item contactPool group_list_members"><div>

										<span class="pull-left"><img id="inviteImage" alt=""
											class="img-circle img-sm m-r-10 mCS_img_loaded"> </span>
									</div>
									<div class="list-group-item-body">
										<div><div id="userNameDivInvite" style=" text-align: left; width:60%;"></div>
										<span id="addFriendStatus" ></span></div>
										 <button class="btn btn-info" id="addUserBtnInvit" onclick="addUserFromInvite(this)"
									type="button" style="width: 19%; margin-left: 82%; margin-top: -8%; height: 29px; padding-top: 5px;" id="addUser" >Add</button>
									
									</div>
							</li>
							</ul>
						</div>
                <div class="modal-footer">
                    <button class="btn btn-info invite_user_btn" style=" float: inherit;" type="button" id="inviteBtn123" >Invite</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal"  style=" float: right;">Close</button>
                </div>
            </div>

        </div>
    </div>
    </div>
    </div>
<div id="new_group_members" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">

            <div class="modal-body invite_user_div">
               <div class="group_name_hedding">
				<h3 id="groupNameHead"></h3>
			   </div>
			   <div class="form-group is-empty group_add_member_list">
					<input type="text" placeholder="Search for contacts here" class="form-control" id="searchContactListGrp" onkeyup="searchContact()" autocomplete="off">
				</div>
				<div class="selct_members_show_div">
				</div>
				<div class="chat_member_list">
					<ul class="chat_list" id="chat_list">

							<c:set var="count" value="0" scope="page" />
							<c:forEach items="${users}" var="user" varStatus="loop">
								<c:set var="count" value="${count + 1}" scope="page" />

								<li class="list-group-item contactPool" id='${user.id}'  data-attachment="${user.attachment.id}"	onclick="selectMemberToGroup(this)">
									<div>
										<span class="pull-left"><img
											src="${urlAttachemnt}/${user.attachment.id}" alt=""
											class="img-circle img-sm m-r-10"></span>

									</div>
									<div class="list-group-item-body" style="text-align: left;">
										<div class="list-group-item-heading userNameDiv">${user.firstName}</div>
										<div class="list-group-item-text">${user.userName}</div>

									</div>
								</li>
							</c:forEach>
						</ul>
				</div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-info invite_user_btn" id="saveGroupBtn" style="float: inherit;" type="submit">Save</button>
            </div>
        </div>

    </div>
</div>


<!-- 	add members in edit gRoup div -->

<div id="new_group_members_Edit" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">

            <div class="modal-body invite_user_div">
               <div class="group_name_hedding">
				<h3 id="groupNameHead"></h3>
			   </div>
			   <div class="form-group is-empty group_add_member_list">
					<input type="text" placeholder="Search for contacts here" class="form-control" id="searchContactListGrp" onkeyup="searchContact()" autocomplete="off">
				</div>
			<!-- 	<div class="selct_members_show_div">
				</div> -->
				<div class="chat_member_list">
					<ul class="chat_list" id="chat_list">

						<%-- 	<c:set var="count" value="0" scope="page" />
							<c:forEach items="${users}" var="user" varStatus="loop">
								<c:set var="count" value="${count + 1}" scope="page" />

								<li class="list-group-item contactPool" id='${user.id}'  data-attachment="${user.attachment.id}"	onclick="selectMemberToGroup(this)">
									<div>
										<span class="pull-left"><img
											src="${urlAttachemnt}/${user.attachment.id}" alt=""
											class="img-circle img-sm m-r-10"></span>

									</div>
									<div class="list-group-item-body" style="text-align: left;">
										<div class="list-group-item-heading userNameDiv">${user.firstName}</div>
										<div class="list-group-item-text">${user.userName}</div>

									</div>
								</li>
							</c:forEach> --%>
						</ul>
				</div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-info invite_user_btn" id="addMembersGrpBtn" style="float: inherit;" type="submit">Add Members</button>
            </div>
        </div>

    </div>
</div>


<!-- message delete modal -->
<div id="messageDelete" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
 
		<div class="modal-body invite_user_div">
			<h2 style="font-size:16px;">Are you sure to delete the selected messages ?</h2>
		
		</div>
		<div class="modal-footer">
			<button class="btn btn-info invite_user_btn" style="float: right;" id="confirmMessageDelete" onclick="deleteMessages()">Yes</button>
			<button type="button" class="btn btn-default" data-dismiss="modal" style="float: right;">No</button>
		</div>
    </div>

  </div>
</div>
<!-- group info modal -->
<div id="DeRegister" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
 
		<div class="modal-body invite_user_div">
			<h2 style="font-size:16px;">Are you sure to delete the group ?</h2>
		
		</div>
		<div class="modal-footer">
			<button class="btn btn-info invite_user_btn" type="submit" style="float: right;">Yes</button>
			<button type="button" class="btn btn-default" data-dismiss="modal" style="float: right;">No</button>
		</div>
    </div>

  </div>
</div>

<!-- user de register -->

<div id="userDeRegister" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
 
		<div class="modal-body invite_user_div">
			<h2 style="font-size:16px;">Are you sure, you still want to deactivate your account?</h2>
		
		</div>
		<div class="modal-footer">
		
		    <button type="button" class="btn btn-default" data-dismiss="modal" style="float: right;">No</button>
		 	<button class="btn btn-info invite_user_btn" type="submit" id="userDeRegisterBtn" style="float: right;">Yes</button>
			
		</div>
    </div>

  </div>
</div>

<!-- user logout -->

<div id="userLogout" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
 
		<div class="modal-body invite_user_div">
			<h2 style="font-size:16px;">Are you sure, you still want to logout your account?</h2>
		
		</div>
		<div class="modal-footer">
		
		    <button type="button" class="btn btn-default" data-dismiss="modal" style="float: right;">No</button>
		 	<button class="btn btn-info invite_user_btn" type="submit" onclick="logout()" id="userLogoutBtn" style="float: right;">Yes</button>
			
		</div>
    </div>

  </div>
</div>

<!-- user edit profile -->
	<%-- <div id="userEditProfileDiv" class="modal fade" role="dialog">
		<div class="modal-dialog">

			<!-- Modal content-->
			<div class="modal-content">

				<div class="modal-body invite_user_div">
				
				    <div id="login_wrapper">

        <div id="login_content">
            <div style="text-align: center; font-size: larger; margin-bottom: 25px;">


            </div>
            <h1 class="login-title">
                Edit profile
            </h1>
            <div class="login-body">
                <div class="profile_pic_upload">
                    <div class="profile_pic_upload_main_div">
                        <div class="box">
                            <input type="file" onchange="readURL(this);" value="Select Profile Picture" name="file-1[]" id="file-1" class="inputfile inputfile-1" data-multiple-caption="{count} files selected" multiple />
                            <label for="file-1"><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAOUSURBVGhD7ZhLSFVRFIY1NSUqkyiMalhBYQMHjZxkNnFQhNCDCkICkWgQEUFBo+g1KIWeQtDAgb2ohKCaRNCgsGEQmQ572QOpVLLSvrX3f7GL3jwvvV64H/ysfdbe6797ec4953oK8uTJMz2Mjo7OQhUhNEelMwM2VDYyMnIJDTEOBTWvUK2ssgsbadO+IkH9AFoju+zAPurYxIjfUnSweEoolG1yYFyK8UTX9b+qZF0PMRHwaiZM9Dlp0hYzw6JCzJrQS8YzFvY3hO6gKm19DOYL0TW3MkegEftupd8oyO/x07kFjXxE89WG+04801zOwd4b1YZr5L/PAOYH0bAOZxTsq0VtuEsrIyxsJRQT56FOnw0G640P6IdSiYP3RbWRuREW/SKUaZmduSo/kxnWPEcH0AoOS1RqnzGX3AbUij65xQmAV6BGjCVaZo2s19Q4mOtGm7XUQdrO5FJi2v2f4wXkTyD7Q8UCj8kbMVhoT90aYj3q9tl0yD8glMvLNnmU+IL4x+YNxt/QLbTJfSgwrkVftCQS1AdrZDIwekgoMh/GO9FnP5MZ1jxBy1WzFg1oKjTUxm8EEztD7kwwPuazwWC93QTc05nYoHRoqE2kEfedIO5QKhTU9aKF8rDLMzTUxWsEgy7VljOe9HLKBLXnzYdYrVQokmjkoNUSjygVCeqH0WJ5vVY6MNTEbmSV1RK7lIoMHnvldU6pwMRuBGajIox++8Po4HHZ9kHcp1RgYjVCcZ/qKn0mHvjdNT/iFqUCE7eRQdWV+0w88OswP+IupQITqxGRepL3+8PosJnT5kU8pFRgYjeCwUbV3vCZ6ODl/tMj3lQqMEk0krr/1ysVCep7CSXEUvTdZ4NDTexGvhLcr1rGj302PNRulUeTUqGI3YiByRmrJy5Db5UODDVt+nz7X+W9z4aDukQasWdInXkwXo0Cv+di7RWCPYvsNdR1l4wAtfEbEf2YVcungnEL+umnxsPcG7RN6+011Ck3ERG8EmvEzOwd03bZ2dlZhBrRBXQPtaOTyP7DLNYaewcQ+UykwCO5RlJg+gitk+2EsMze3jejd74qHlPSSArMe5BdYvtRA9qNDqPbKNE3KvhNXSPTSVojHPQpn3Ow9+NqwzXSrnwuUqM2XCMrUeQ3GdmCPXeqhTFI2jumnLnE2Ot9gvsFPg4m7OWa3RqvMrZftTNK7KsDnUXul3eePHny5MkyBQV/AeCRbg3kR4fDAAAAAElFTkSuQmCC"></label>
                        </div>


           
               <img id="editprofilePic"  src="<spring:url value="/resources/assets/img/sample_profile_pic.png"/>" alt="your image" />
                    </div>
                </div>
                <div class="form-signin">

					<div class="sign_text_main_div">
                        <div class="form-group label-floating text_box_div">
                            <input name="firstname" id="firstNameEdit" type="text" class="form-control" placeholder="first name" autofocus="true" />
                        </div>
                        <div class="form-group label-floating text_box_div">
                            <input name="lastName" id="lastNameEdit" type="text" class="form-control" placeholder="last name" autofocus="true" />
                        </div>
                        
                        <div class="form-group label-floating text_box_div">
							<div class="gender_div">
								<div class="gender_div_male_edit"><input type="radio" name="gender" id="maleRadioBtn_Edit" value="male"> <span>Male</span></div>
								<div class="gender_div_male_edit"><input type="radio" name="gender"  id="femaleRadioBtn_Edit" value="female"> <span>Female</span></div>
                        </div>
                        <div class="form-group label-floating text_box_div">
                            <input name="username" id="userNameEdit" type="text" class="form-control"  disabled />
                        </div>
                        <div class="form-group label-floating text_box_div">
                            <input name="emailid" id="emailIdEdit" type="text" class="form-control"  disabled />
                        </div>
                        
					</div>
					<div class="sign_text_main_div_btm">
						<span id="errorMessageEdit" style="color:red;"></span>
						<button class="btn " id="updateProfileBtn" style="width: 100%;" >update profile</button>
						
                    </div>

                </div>
            </div>

        </div>
    </div>
				</div>



<div class="modal-footer">
		
				
		</div>
			</div>

		</div>
	</div> --%>

	<!--   change password Div -->
    <div id="change_password" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
	 
           <div class="modal-body invite_user_div">
					
					<div class="for_got_pass_div_reset " id="changePassword" >
						<h2>Change Password</h2>
						
						<div class="changePassword">
							<div class="form-group label-floating is-empty ">
								<span></span>
							
								<input name="code" type="password" class="form-control" placeholder="Current Password" id="currentPassword" autofocus="true">
                                <input name="new_pass" type="password" class="form-control" placeholder="New Password" id="newPassTextBox"  autofocus="true">
								<input name="re_pass" type="password" class="form-control" placeholder="Confirm Password" id="confNewPassTextBox"   autofocus="true">
							   <div><span id="errorMessageChangePass" style="color:red;text-align: center; width: 100%;"></span></div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="modal-footer">
					
					<button class="btn btn-info invite_user_btn" id="changePassBtn" type="submit" style="float: inherit;">Save</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>

 <!-- friendRequest -->
		<div id="friend_request" class="modal fade" role="dialog">
    <div class="modal-dialog frient_request_mdl_content_dialog">

        <!-- Modal content-->
        <div class="modal-content frient_request_mdl_content">

            <div class="modal-body  frient_request_mdl_content">
                <div class="friend_request_headding">
					<h3>Manage friend request</h3>
				</div>
				
                <div class="chat_member_list">
					<ul class="chat_list" id="user_list_FriendReq">
					
						<!-- <li class="list-group-item contactPool group_list_members frient_rqst_list"> 
							<div class="friend_request_profile_pic"> 
								<span class="pull-left">
									<img src="/company/attachments/5c3c2b5e23410e0dc42fa21c" alt="" class="img-circle img-sm m-r-10 mCS_img_loaded">
								</span> 
							</div> 
							<div class="list-group-item-body name_list_div friend_request_user_name"> 
								<div class="list-group-item-heading userNameDivEdit" style=" text-align: left;">Deepak Kurian</div> 
								<div class="list-group-item-text"></div> 
							</div> 
							<div class="accept_btn_div"> 
                             <button type="button" class="btn btn-success approve_btn">Approve</button>    <button type="button" class="btn btn-danger decline_btn">Decline</button>
							</div>
							
							<div class="date_time_frnd_rqst">
                            <p><span class="time">05:13 PM</span><span class="date">01/14/2019</span></p>
  						  </div>
							
						</li> -->
					</ul>
				</div>
            </div>
            
        </div>

    </div>
</div>

<!-- message delete modal -->
<div id="messageDelete" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
 
		<div class="modal-body invite_user_div">
			<h2 style="font-size:16px;">Are you sure to delete the selected messages ?</h2>
		
		</div>
		<div class="modal-footer">
			<button class="btn btn-info invite_user_btn" style="float: right;" id="confirmMessageDelete" onclick="deleteMessages()">Yes</button>
			<button type="button" class="btn btn-default" data-dismiss="modal" style="float: right;" onclick="refreshSelection()">No</button>
		</div>

    </div>

  </div>
</div>




	<spring:url value="/resources/assets/js/vendor.bundle.js"
		var="vendorBundleJs"></spring:url>
	<script src="${vendorBundleJs}"></script>

 	<spring:url value="/resources/assets/js/app.bundle.js"
		var="appBundleJs"></spring:url>
	<script src="${appBundleJs}"></script> 


	<!-- Begin emoji-picker JavaScript -->
	<spring:url value="/resources/assets/emoji/js/config.js" var="configJs"></spring:url>
	<script src="${configJs}"></script>


	<spring:url value="/resources/assets/emoji/js/util.js" var="utilJs"></spring:url>
	<script src="${utilJs}"></script>

	<spring:url value="/resources/assets/emoji/js/jquery.emojiarea.js"
		var="jqueryEmojiareaJs"></spring:url>
	<script src="${jqueryEmojiareaJs}"></script>

	<spring:url value="/resources/assets/emoji/js/emoji-picker.js"
		var="emojiPickerJs"></spring:url>

	<script src="${emojiPickerJs}"></script>
	<!-- End emoji-picker JavaScript -->
	<spring:url value="/resources/app/chatHome.js" var="chatHomejs"></spring:url>
	<script type="text/javascript" src="${chatHomejs}"></script>
	<script>
		$(function() {
			// Initializes and creates emoji set from sprite sheet
			window.emojiPicker = new EmojiPicker({
				emojiable_selector : '[data-emojiable=true]',
				assetsPath : '../resources/assets/emoji/img',
				popupButtonClasses : 'fa fa-smile-o'
			});
			// Finds all elements with `emojiable_selector` and converts them to rich emoji input fields
			// You may want to delay this step if you have dynamically created input fields that appear later in the loading process
			// It can be called as many times as necessary; previously converted input fields will not be converted again
			window.emojiPicker.discover();
		});
	</script>
	<script>
		// Google Analytics
		(function(i, s, o, g, r, a, m) {
			i['GoogleAnalyticsObject'] = r;
			i[r] = i[r] || function() {
				(i[r].q = i[r].q || []).push(arguments)
			}, i[r].l = 1 * new Date();
			a = s.createElement(o), m = s.getElementsByTagName(o)[0];
			a.async = 1;
			a.src = g;
			m.parentNode.insertBefore(a, m)
		})(window, document, 'script',
				'//www.google-analytics.com/analytics.js', 'ga');

		ga('create', 'UA-49610253-3', 'auto');
		ga('send', 'pageview');
	</script>
</body>
<!-- SockJs -->
<spring:url value="/resources/assets/js/websocket/sockjs-0.3.4.js"
	var="sockjs"></spring:url>
<script type="text/javascript" src="${sockjs}"></script>

<!-- StompJS -->
<spring:url value="/resources/assets/js/websocket/stomp.js" var="stomp"></spring:url>
<script type="text/javascript" src="${stomp}"></script>

<spring:url value="/resources/assets/js/moment.js" var="momentsJs"></spring:url>
<script type="text/javascript" src="${momentsJs}"></script>




<spring:url value="/resources/assets/js/voicePannel/recorderControl.js"
	var="recorderControl"></spring:url>
<script type="text/javascript" src="${recorderControl}"></script>

<spring:url value="/resources/assets/js/voicePannel/jquery.stopwatch.js"
	var="stopwatch"></spring:url>
<script type="text/javascript" src="${stopwatch}"></script>

<spring:url value="/resources/assets/js/voicePannel/recorder.js"
	var="recorder"></spring:url>
<script type="text/javascript" src="${recorder}"></script>


<spring:url value="/resources/assets/js/VoiceToText/app.js"
	var="appjs"></spring:url>
<script type="text/javascript" src="${appjs}"></script>

<spring:url value="/resources/assets/js/preloader.js"
	var="preloaderjs"></spring:url>
<script type="text/javascript" src="${preloaderjs}"></script>

<script>
	var contextPath = "${pageContext.request.contextPath}";
	var urlMessageAll="${urlMessageAll}";
	
	var urlMessageGroupAll="${urlMessageGroupAll}"
	var loginUserUk = "${receverId}";
	var loginUserUkGrp = "12345678998877";
	var userId="${userId}";
	var userFullName="${userFullName}";
	var urlAttachemnt="${urlAttachemnt}";
	var userProfilePic="${userProfilePic}";
	var userEmailId="${userEmail}";
	
</script>
<!-- <script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.min.js"></script> -->
    <spring:url value="/resources/assets/input_file/js/custom-file-input.js" var="CustomFileInput"></spring:url>
    <script type="text/javascript" src="${CustomFileInput}"></script>
    <script>
  
        
   /*      function readURLEditProfile(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();

                alert("Haiii..!!");
                reader.onload = function(e) {
                    $('#editprofilePic')
                        .attr('src', e.target.result);
                };

                reader.readAsDataURL(input.files[0]);
            }
        } */
    </script>
    
    <script>
/* When the user clicks on the button, 
toggle between hiding and showing the dropdown content */
function myFunction() {
  document.getElementById("myDropdown").classList.toggle("show");
}

// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {
    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
}
</script>


  <%-- <spring:url value="/resources/assets/input_file/js/custom-file-input.js" var="CustomFileInput"></spring:url>
    <script type="text/javascript" src="${CustomFileInput}"></script>
    <script>
        function readURLEditProfile(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();

                alert("Haiii..!!");
                reader.onload = function(e) {
                    $('#editprofilePic')
                        .attr('src', e.target.result);
                };

                reader.readAsDataURL(input.files[0]);
            }
        }
        
        
       
    </script>  --%>

<script>
$(function(){

var appendthis =  ("<div class='modal-overlay js-modal-close'></div>");

	$('a[data-modal-id]').click(function(e) {
		e.preventDefault();
    $("body").append(appendthis);
    $(".modal-overlay").fadeTo(500, 0.7);
    //$(".js-modalbox").fadeIn(500);
		var modalBox = $(this).attr('data-modal-id');
		$('#'+modalBox).fadeIn($(this).data());
	});  
  
  
$(".js-modal-close, .modal-overlay").click(function() {
    $(".modal-box, .modal-overlay").fadeOut(500, function() {
        $(".modal-overlay").remove();
    });
 
});
 
$(window).resize(function() {
    $(".modal-box").css({
        top: ($(window).height() - $(".modal-box").outerHeight()) / 2,
        left: ($(window).width() - $(".modal-box").outerWidth()) / 2
    });
});
 
$(window).resize();
 
});
</script>
</html>