

<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html>
 
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="description" content="">
<meta name="keywords" content="">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Chat Application</title>

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

<spring:url value="/resources/assets/css/theme-a.css" var="themeACss"></spring:url>
<link rel="stylesheet" href="${themeACss}">

<spring:url value="/WEB-INF/jsp/register.jsp" var="register"></spring:url>
<link rel="text/javascript" href="${register}">
</head>

<body id="auth_wrapper">
    <div id="login_wrapper">

        <div id="login_content">
            <div style="text-align: center; font-size: larger; margin-bottom: 25px;">
              
				
            </div>
            <h1 class="login-title">
                Sign In to your account
            </h1>
            <div class="login-body">
               <form method="POST" action="${contextPath}/login" class="form-signin">
        <h2 class="form-heading" style="text-align: center;" >Sign In</h2>

   
        
         <div class="form-group label-floating is-empty ">
           <span>${msg}</span>
                       <!--  <label class="control-label">Email</label> -->
                        <!-- <input type="email" class="form-control"> -->
                        <input name="username" type="text" class="form-control" placeholder="User Name"
                   autofocus="true"/>
                        
                    </div>
                    <div class="form-group label-floating is-empty">
                      <!--   <label class="control-label">Password</label> -->
                        <!-- <input type="password" class="form-control"> -->
                         <input name="password" type="password" class="form-control" placeholder="Password" />
                    </div>
                <!--     <a href="javascript:void(0)" class="forgot-pass pull-right">Forgot Password?</a> -->
                    <div class="checkbox inline-block">
        
                
                <p class="forgot_password"><a href="#" id="forgotPassword"  data-toggle="modal" data-target="#forgot_password">Forgot Password</i>
											</a></p>
                    
                       <!--  <label>
                            <input type="checkbox" class="checkbox-inline" value="">
                            Remember Me
                        </label> -->
                    </div>
               <!--      <a href="index.html" class="btn btn-info btn-block m-t-40">Sign In</a> -->
               <button class="btn btn-info btn-block m-t-40" type="submit">Sign In</button>
               
               <div>
                	 <p>
                   Don't have an account?  <a href="${pageContext.request.contextPath}/register" data-toggle="login">  Sign up</a>
                </p>
               </div>

    </form>
            </div>
           <!--  <div class="login-footer p-15">
                <p>
                    Don't have an account? <a href="javascript:void(0)" data-toggle="register">Create an account</a>
                </p>
            </div> -->
            <%-- <div id="register_wrapper">
                <ul class="card-actions icons right-top">
                    <li>
                        <a href="javascript:void(0)" data-toggle="register">
                            <i class="zmdi zmdi-close"></i>
                        </a>
                    </li>
                </ul>
                <div class="logo">
                   <!-- Create an account -->
                </div>
                <h1 class="login-title">
                    Create an account
                </h1>
                <div class="login-body">
                    <form class="clear-both">
                        <h2 class="text-center p-t-20">
                            Or sign up below
                        </h2>
                        <div class="form-group label-floating is-empty">
                            <label class="control-label">Name</label>
                            <input type="text" class="form-control">
                        </div>
                        <div class="form-group label-floating is-empty">
                            <label class="control-label">Email</label>
                            <input type="email" class="form-control">
                        </div>
                        <div class="form-group label-floating is-empty">
                            <label class="control-label">Password</label>
                            <input type="password" class="form-control">
                        </div>
                        <div class="form-group label-floating is-empty">
                            <label class="control-label">Confirm Password</label>
                            <input type="password" class="form-control">
                        </div>
                        <div class="checkbox inline-block">
                            <label>
                                <input type="checkbox" class="checkbox-inline" value="">
                                I agree to the <a href="javascript:void(0)">terms of services</a>
                            </label>
                        </div>
                        <a href="javascript:void(0)" class="btn btn-info btn-block m-t-40">Create my account</a>
                    </form>
                </div>
                <div class="login-footer p-15">
                    <p>
                        Already have an account? <a href="javascript:void(0)" data-toggle="register">Sign In</a>
                    </p>
                </div>
            </div> --%>
        </div>
    </div>
    
 <!--    forgotpassword Div -->
    <div id="forgot_password" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
	 
			


<div class="modal-body invite_user_div">
					<div class="for_got_pass_div" >
						
						
						<div class="invite_email">
							<div class="form-group label-floating is-empty " id="forgotpassDiv">
							<h2>Forgot your Password?</h2>
							<p>Enter your User Name and email below to receive password reset notification</p>
								<span></span>
								
								<input name="username" type="text" class="form-control" placeholder="User Name"  id="userNameFrgtPass"  autofocus="true">
								<input name="email" type="text" class="form-control" placeholder="Email" id="emailFrgtPass" autofocus="true">
							</div>
						</div>
					</div>
					<div class="for_got_pass_div_reset " id="forgotPassTokenDiv" style=" display: none;">
						<h2>Reset Password</h2>
						
						<div class="invite_email">
							<div class="form-group label-floating is-empty ">
								<span></span>
							
								<input name="code" type="text" class="form-control" placeholder="Enter Code" id="tockenFrgtPassword" autofocus="true">
                                <input name="new_pass" type="password" class="form-control" placeholder="New Password" id="newPassTextBox"  autofocus="true">
								<input name="re_pass" type="password" class="form-control" placeholder="Confirm Password" id="confNewPassTextBox"   autofocus="true">
							</div>
						</div>
					</div>
				</div>
				<div><span id="errorMessageFrgtPass" style="color:red;"></span></div>
				<div class="modal-footer">
					<button class="btn btn-info invite_user_btn" id="nextBtnFrgtPassword" type="submit">Next</button>
					<button class="btn btn-info invite_user_btn" id="savePassword" type="submit" style=" display: none;">Save</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
   
       <spring:url value="/resources/app/login.js" var="loginJs"></spring:url>
	<script type="text/javascript" src="${loginJs}"></script>
  <script src="js/vendor.bundle.js"></script>
  <script src="js/app.bundle.js"></script> 
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
 <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> 
</body>
</html>
