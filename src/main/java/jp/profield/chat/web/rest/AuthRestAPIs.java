package jp.profield.chat.web.rest;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import jp.profield.chat.model.Role;
import jp.profield.chat.model.UserManagement;
import jp.profield.chat.non_model.JwtResponse;
import jp.profield.chat.non_model.LoginForm;
import jp.profield.chat.non_model.SignUpForm;
import jp.profield.chat.non_model.Status;
import jp.profield.chat.security.jwt.JwtProvider;
import jp.profield.chat.security.uti.UserPrinciple;
import jp.profield.chat.service.AttachmentService;
import jp.profield.chat.service.ChatService;
import jp.profield.chat.service.MailSendService;
import jp.profield.chat.service.RoleService;
import jp.profield.chat.service.UserManagementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {
	
	@Value("${server.upload.path}")
	private String fileSaveDirectory;
	
	/*@Value("${local.upload.path}")
	private String localfileSaveDirectory;*/

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserManagementService userManagementService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;
    
    @Autowired
	private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AttachmentService attachmentService;
    
	@Autowired
	private MailSendService mailSendService;
    
	@Autowired
	private ChatService chatService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

    	JwtResponse jwtResponse= new JwtResponse();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        //jwt = jwtProvider.generateJwtToken(authentication);
        jwtResponse.setAccessToken(jwtProvider.generateJwtToken(authentication)); 
        UserPrinciple user=(UserPrinciple) authentication.getPrincipal();
        jwtResponse.setUserId(user.getId());
        jwtResponse.setStatus("Scucess");
        
        return ResponseEntity.ok(jwtResponse);
    }
    
    

    public class Message {

        private String from;
        private String text;
		public String getFrom() {
			return from;
		}
		public void setFrom(String from) {
			this.from = from;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}

        // adding getters and setters here
    }
    
   
    @PostMapping("/signup")
	public Status  registerUser(@Valid @RequestBody SignUpForm signUpRequest) {

    	Status status= new Status();
		Optional<UserManagement> user = userManagementService
				.findByUserNameIgnoreCase(signUpRequest.getUsername());

		if (user.isPresent()) {
			 status.setMessage("Username is already taken!");
			 return status;
		} else {

			Optional<UserManagement> userExist = userManagementService
					.findByEmail(signUpRequest.getEmail());

			if (userExist.isPresent()) {

				status.setMessage("Email Id is already taken!");
				 return status;
			} else {
				UserManagement userObj = new UserManagement();

				userObj.setFirstName(signUpRequest.getFirstName());
				userObj.setLastName(signUpRequest.getLastName());
				userObj.setEmail(signUpRequest.getEmail());
				userObj.setUserName(signUpRequest.getUsername());
				userObj.setPassword(passwordEncoder.encode(signUpRequest
						.getPassword()));

				Random rand = new Random();
				int n = rand.nextInt(500000) + 1;
				long number = n;

				userObj.setUkMessageKey(number);

				Optional<Role> role = roleService
						.findOneByRoleName(signUpRequest.getRole());
				userObj.setRole(role.get());
				Date date = new Date();
				userObj.setCreatedOn(date);

				userObj.setActivated(true);
				userObj.setIsDeleted(false);

				userManagementService.saveUser(userObj);
				status.setUserId(userObj.getId());
				status.setMessage("saved");
				
				mailSendService.sendWelcomeMailToNewUser(signUpRequest.getEmail(), signUpRequest.getUsername(),
						signUpRequest
						.getPassword(), signUpRequest.getFirstName()+" "+signUpRequest.getLastName());
				
				return status;
				
				

			}

		}

	}
    
    //forgot password  sending tocken to mail
    @PostMapping(value = "/forgotPassword")
	public Status forgotPassword(@RequestParam("emailId") String emailId,@RequestParam("userName") String userName){
		Status status= new Status();
		if(emailId!=null && userName!=null){
			 status= chatService.forgotPassword(emailId, userName);
		}
		
		return status;
		
	}
    
    //save password after tocken received , process with token and password
	@PostMapping(value = "/savePassword")
	public Status savePassword(@RequestParam("tocken") long tocken,@RequestParam("password")  String password,@RequestParam("emailId") String emailId){
		Status status= new Status();
		if(emailId!=null){
		
			 status= chatService.savePasswordFromForgotPassword(tocken, password, emailId);
		}
		return status;
	}

	
    
    @RequestMapping(value = "/uploadProfilePic", method = RequestMethod.POST) 
    public Status uploadFileHandler(@RequestParam("userId") String userId, MultipartHttpServletRequest request
      , HttpServletResponse response) {

		MultipartFile file = request.getFile("file");
		Status status = new Status();

		Optional<UserManagement> user = userManagementService
				.findOneByid(userId);

		if (user.isPresent()) {
			if (!file.isEmpty()) {

				UserManagement userObj = user.get();
				try {
					byte[] bytes = file.getBytes();

					userObj.setProfileImage(bytes);

					Random rnd = new Random();
					int randmNumber = 100000 + rnd.nextInt(900000);
					
					String fileLocation = getFileSaveDirectory("profilePic")
							+ "/" + randmNumber + file.getOriginalFilename();
					writeToDisk(file.getBytes(), fileLocation, "profilePic");

					userObj.setProfileImageUrl(fileLocation);
					userManagementService.saveUser(userObj);
					status.setUserId(userObj.getId());
					status.setMessage("Saved");
					return status;
				} catch (Exception e) {
					status.setMessage("Error");
					return null;
				}
			}
		}

		return status;
	} 
	private void writeToDisk(byte[] fileBytes, String fileLocation, String folderName) {
		try {
			File fileDir = new File(getFileSaveDirectory(folderName));
			if (!fileDir.exists()) {
				boolean retval = fileDir.mkdirs();
				if (retval) {
					System.out.println("Directory : " + fileDir
							+ " created successfully");
				} else {
					System.out.println("Directory : " + fileDir + " creation failed");
				}
			}
			FileCopyUtils.copy(fileBytes, new File(fileLocation));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

    
	private String getFileSaveDirectory(String folderName) {
		return fileSaveDirectory + folderName;
	}

}