<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime"%>
 <%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <title>Chat Register</title>

    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,600,700|Poppins:300,400,500,600" rel="stylesheet">

    <spring:url value="/resources/assets/css/vendor.bundle.css" var="vendorBundleCss"></spring:url>
    <link rel="stylesheet" href="${vendorBundleCss}">

    <spring:url value="/resources/assets/css/app.bundle.css" var="appBundleCss"></spring:url>
    <link rel="stylesheet" href="${appBundleCss}">

    <spring:url value="/resources/assets/css/theme-a.css" var="themeACss"></spring:url>
    <link rel="stylesheet" href="${themeACss}">
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    
    <spring:url value="/resources/assets/css/component.css" var="componentCss"></spring:url>
    <link rel="stylesheet" href="${componentCss}">

    <script>(function(e,t,n){var r=e.querySelectorAll("html")[0];r.className=r.className.replace(/(^|\s)no-js(\s|$)/,"$1js$2")})(document,window,0);</script>
    <style>
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

.profile_pic_upload_main_div img {
    max-width: 180px;
    max-height: 180px;
}
</style>
</head>

<body id="auth_wrapper">
    <div id="login_wrapper">
        <div id="login_content">
            <div style="text-align: center; font-size: larger; margin-bottom: 25px;">
            </div>
            <h1 class="login-title">
                Sign up for Profield Chat
            </h1>
            <div class="login-body">
                <div class="profile_pic_upload">
                    <div class="profile_pic_upload_main_div">
                        <div class="box">
                            <input type="file" onchange="readURL(this);" value="Select Profile Picture" name="file-1[]" id="file-1" class="inputfile inputfile-1" data-multiple-caption="{count} files selected" multiple />
                            <label for="file-1"><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAOUSURBVGhD7ZhLSFVRFIY1NSUqkyiMalhBYQMHjZxkNnFQhNCDCkICkWgQEUFBo+g1KIWeQtDAgb2ohKCaRNCgsGEQmQ572QOpVLLSvrX3f7GL3jwvvV64H/ysfdbe6797ec4953oK8uTJMz2Mjo7OQhUhNEelMwM2VDYyMnIJDTEOBTWvUK2ssgsbadO+IkH9AFoju+zAPurYxIjfUnSweEoolG1yYFyK8UTX9b+qZF0PMRHwaiZM9Dlp0hYzw6JCzJrQS8YzFvY3hO6gKm19DOYL0TW3MkegEftupd8oyO/x07kFjXxE89WG+04801zOwd4b1YZr5L/PAOYH0bAOZxTsq0VtuEsrIyxsJRQT56FOnw0G640P6IdSiYP3RbWRuREW/SKUaZmduSo/kxnWPEcH0AoOS1RqnzGX3AbUij65xQmAV6BGjCVaZo2s19Q4mOtGm7XUQdrO5FJi2v2f4wXkTyD7Q8UCj8kbMVhoT90aYj3q9tl0yD8glMvLNnmU+IL4x+YNxt/QLbTJfSgwrkVftCQS1AdrZDIwekgoMh/GO9FnP5MZ1jxBy1WzFg1oKjTUxm8EEztD7kwwPuazwWC93QTc05nYoHRoqE2kEfedIO5QKhTU9aKF8rDLMzTUxWsEgy7VljOe9HLKBLXnzYdYrVQokmjkoNUSjygVCeqH0WJ5vVY6MNTEbmSV1RK7lIoMHnvldU6pwMRuBGajIox++8Po4HHZ9kHcp1RgYjVCcZ/qKn0mHvjdNT/iFqUCE7eRQdWV+0w88OswP+IupQITqxGRepL3+8PosJnT5kU8pFRgYjeCwUbV3vCZ6ODl/tMj3lQqMEk0krr/1ysVCep7CSXEUvTdZ4NDTexGvhLcr1rGj302PNRulUeTUqGI3YiByRmrJy5Db5UODDVt+nz7X+W9z4aDukQasWdInXkwXo0Cv+di7RWCPYvsNdR1l4wAtfEbEf2YVcungnEL+umnxsPcG7RN6+011Ck3ERG8EmvEzOwd03bZ2dlZhBrRBXQPtaOTyP7DLNYaewcQ+UykwCO5RlJg+gitk+2EsMze3jejd74qHlPSSArMe5BdYvtRA9qNDqPbKNE3KvhNXSPTSVojHPQpn3Ow9+NqwzXSrnwuUqM2XCMrUeQ3GdmCPXeqhTFI2jumnLnE2Ot9gvsFPg4m7OWa3RqvMrZftTNK7KsDnUXul3eePHny5MkyBQV/AeCRbg3kR4fDAAAAAElFTkSuQmCC"></label>
                        </div>


           
               <img id="blah"  src="<spring:url value="/resources/assets/img/sample_profile_pic.png"/>" alt="your image" />
                    </div>
                </div>
                <div class="form-signin">



					<div class="sign_text_main_div">
                        <div class="form-group label-floating text_box_div">
                            <input name="firstname" id="firstName" type="text" class="form-control" placeholder="first name" autofocus="true" />
                        </div>
                        <div class="form-group label-floating text_box_div">
                            <input name="lastName" id="lastName" type="text" class="form-control" placeholder="last name" autofocus="true" />
                        </div>
                        
                        <div class="form-group label-floating text_box_div">
							<div class="gender_div">
								<div class="gender_div_male"><input type="radio" name="gender" id="maleRadioBtn" value="male" checked="checked"> <span>Male</span></div>
								<div class="gender_div_female"><input type="radio" name="gender"  id="femaleRadioBtn" value="female"> <span>Female</span></div>
                        </div>
                        <div class="form-group label-floating text_box_div">
                            <input name="username" id="userName" type="text" class="form-control" placeholder="user name" autofocus="true" />
                        </div>
                        <div class="form-group label-floating text_box_div">
                            <input name="emailid" id="emailId" type="text" class="form-control" placeholder="email" autofocus="true" />
                        </div>
                        <div class="form-group label-floating text_box_div">
							<input name="password" id="password" type="password" class="form-control" placeholder="password" />
                        </div>
                        <div class="form-group label-floating text_box_div">
							<input name="password" id="password2" type="password" class="form-control" placeholder="repeat password" />
                        </div>
					</div>
					<div class="sign_text_main_div_btm">
						<span id="errorMessage" style="color:red;"></span>
						<button class="btn btn-info btn-block m-t-40" id="register" style="width: 100%;" onclick="reg()">Create New User</button>
						<div class="login-footer p-15">
							<p>
								Already have an account? <a href="login" data-toggle="login"> Sign in</a>
							</p>
						</div>
                    </div>

                </div>
            </div>

        </div>
    </div>
    <spring:url value="/resources/app/register.js" var="registerjs"></spring:url>
    <script type="text/javascript" src="${registerjs}"></script>


    <!--    <script src="js/vendor.bundle.js"></script>
    <script src="js/app.bundle.js"></script> -->

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <!-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> -->

    <spring:url value="/resources/assets/input_file/js/custom-file-input.js" var="CustomFileInput"></spring:url>
    <script type="text/javascript" src="${CustomFileInput}"></script>
    <script>
        function readURL(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();

                reader.onload = function(e) {
                    $('#blah')
                        .attr('src', e.target.result);
                };

                reader.readAsDataURL(input.files[0]);
            }
        }
    </script>
</body>

</html>