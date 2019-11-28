package jp.profield.chat.web.rest;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import jp.profield.chat.WebscoketBean;
import jp.profield.chat.model.Attachment;
import jp.profield.chat.model.ContactList;
import jp.profield.chat.model.IndividualChatMessage;
import jp.profield.chat.model.MemberInvitation;
import jp.profield.chat.model.UserManagement;
import jp.profield.chat.models.repository.AttachemntRepository;
import jp.profield.chat.models.repository.IndividualChatMessageRepository;
import jp.profield.chat.models.repository.RoleRepository;
import jp.profield.chat.models.repository.UserRepository;
import jp.profield.chat.non_model.MessageScoketDTO;
import jp.profield.chat.non_model.Register;
import jp.profield.chat.non_model.Status;
import jp.profield.chat.service.ContactListService;
import jp.profield.chat.service.MailSendService;
import jp.profield.chat.service.MemberInvitationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;

/**
 * Web controller for managing Login.
 * 
 * @version 1.0
 */
@Controller
public class LoginResource {
	
	private final Logger log=LoggerFactory.getLogger(this.getClass());

	@Value("${server.upload.path}")
	private String fileSaveDirectory;
	
	@Inject
	private UserRepository userRepository;

	@Inject
	private RoleRepository roleRepository;

	@Inject
	private PasswordEncoder passwordEncoder;
	
	@Inject
	private AttachemntRepository attachemntRepository;
	
	@Autowired
	private MailSendService  mailSendService;
	
	@Autowired
	private MemberInvitationService memberInvitationService;
	
	@Autowired
	private ContactListService contactListService;
	
	@Inject
	private ServletContext servletContext;
	
	@Autowired
	private IndividualChatMessageRepository indMessageRepository;
	
	

	
	
	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public String login() {
		
		return "login";
	}
	
	@RequestMapping(value = {"/register" }, method = RequestMethod.GET)
	public String register() {
		return "register";
	}
	
	
	@PostMapping(value = "/forgotPassword", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> forgotPassword(@RequestParam String emailId,@RequestParam String userName){
		Status status= new Status();
		if(emailId!=null && userName!=null){
			// status= chatService.forgotPassword(emailId, userName);
		}
		
		return new ResponseEntity<>(status, HttpStatus.OK);
		
	}
	
	@PostMapping(value = "/savePassword", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> savePassword(@RequestParam long tocken,@RequestParam String password,@RequestParam String emailId){
		Status status= new Status();
		if(emailId!=null){
			 //status= chatService.savePasswordFromForgotPassword(tocken, password, emailId);
		}
		
		return new ResponseEntity<>(status, HttpStatus.OK);
		
	}
	
	@PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> register(
              MultipartHttpServletRequest request)
			throws URISyntaxException {
		String threadPid = request.getParameterValues("threadPid")[0];
		Gson gson = new Gson();
		Register registerDTO = gson.fromJson(threadPid, Register.class);

		Status status = new Status();
		MultipartFile file = request.getFile("file");
		Optional<UserManagement> userExist = userRepository
				.findByUserNameIgnoreCase(registerDTO.getUserName());

		if (!userExist.isPresent()) {

			Optional<UserManagement> userEmailExist = userRepository
					.findByEmail(registerDTO.getEmailId());

			if (!userEmailExist.isPresent()) {
				UserManagement user = new UserManagement();
				user.setEmail(registerDTO.getEmailId());
				user.setActivated(true);
				user.setFirstName(registerDTO.getFirstName());
				user.setLastName(registerDTO.getLastName());
				user.setUserName(registerDTO.getUserName());
				user.setGender(registerDTO.getGender());
				user.setIsOnline(false);
				// encoading password
				user.setPassword(passwordEncoder.encode(registerDTO
						.getPassword()));

				Random rand = new Random();
				int n = rand.nextInt(500000) + 1;
				long number = n;

				user.setUkMessageKey(number);
				user.setRole(roleRepository.findOneByRoleName("USER").get());

				if (file == null) {
					ServletContext servletContext = request.getSession()
							.getServletContext();
					File rootDir;
					String fileName = "";
					if (user.getGender().equals("male")) {
						rootDir = new File(
								servletContext
										.getRealPath("/resources/assets/img/boy.png"));
						fileName = "boy.png";
					} else {
						rootDir = new File(
								servletContext
										.getRealPath("/resources/assets/img/girl.png"));
						fileName = "girl.png";
					}

					try {
						byte[] bytes = Files.readAllBytes(rootDir.toPath());
						user.setProfileImage(bytes);
						Random rnd = new Random();
						int randmNumber = 100000 + rnd.nextInt(900000);
						String fileLocation = getFileSaveDirectory("profilePic")
								+ "/" + randmNumber + fileName;
						writeToDisk(bytes, fileLocation, "profilePic");
						String tumbImage = getFileSaveDirectory("profilePic")
								+ "/thumb-" + randmNumber + fileName;
						saveScaledImage(fileLocation, tumbImage);

						// user.setProfileImageUrl(fileLocation);
						Attachment attachment = new Attachment();
						attachment.setActivated(true);
						attachment
								.setAttachmentName("profilePic" + randmNumber);
						// attachment.setExtension(extension);
						attachment.setUploadedDate(LocalDateTime.now());
						attachment.setLocation(fileLocation);
						attachment.setThumbfile(tumbImage);
						attachemntRepository.save(attachment);

						user.setAttachment(attachment);
						status.setMessage("Saved");

					} catch (IOException e1) {

						e1.printStackTrace();
					}

				} else {
					try {
						byte[] bytes = file.getBytes();
						user.setProfileImage(bytes);
						Random rnd = new Random();
						int randmNumber = 100000 + rnd.nextInt(900000);
						String fileLocation = getFileSaveDirectory("profilePic")
								+ "/"
								+ randmNumber
								+ file.getOriginalFilename();
						writeToDisk(bytes, fileLocation, "profilePic");
						String tumbImage = getFileSaveDirectory("profilePic")
								+ "/thumb-" + randmNumber
								+ file.getOriginalFilename();
						saveScaledImage(fileLocation, tumbImage);

						// user.setProfileImageUrl(fileLocation);
						Attachment attachment = new Attachment();
						attachment.setActivated(true);
						attachment.setAttachmentName("profilePic" + randmNumber);
						// attachment.setExtension(extension);
						attachment.setUploadedDate(LocalDateTime.now());
						attachment.setLocation(fileLocation);
						attachment.setThumbfile(tumbImage);
						attachemntRepository.save(attachment);

						user.setAttachment(attachment);
						status.setMessage("Saved");

					} catch (Exception e) {
						status.setMessage("Error");
						return null;
					}
				}

				userRepository.save(user);
				status.setMessage("New user Registered..!!!");
				
			

				// chek and add friends to new User ContactList
				List<MemberInvitation> inviteeList = memberInvitationService
						.findAllinvitationByNewUserEmailId(registerDTO
								.getEmailId());
				for (MemberInvitation invitaion : inviteeList) {

					Optional<UserManagement> inviterUser = userRepository
							.findById(invitaion.getUser().getId());
					if (inviterUser.isPresent()) {
						// save to current user Contact list
						ContactList contactList = new ContactList();
						contactList.setFriendId(inviterUser.get());
						contactList.setUserId(user);
						Date date = new Date();
						contactList.setCreatedOn(date);
						contactListService.saveContactList(contactList);

						// save to inviter Contact list
						ContactList contactListFrnd = new ContactList();

						contactListFrnd.setFriendId(user);
						contactListFrnd.setUserId(inviterUser.get());
						contactListFrnd.setCreatedOn(date);
						contactListService.saveContactList(contactListFrnd);
						
						
						IndividualChatMessage messageObj= new IndividualChatMessage();
						
						messageObj.setIsAttachment(false);
						messageObj.setMessageContent(user.getFirstName() +"  joined chat...!!!");
						
						messageObj.setReceiverId(inviterUser.get().getId());// receiverId 
						messageObj.setSenderId(user.getId());
						messageObj.setRead(true);
						
						LocalDateTime now = LocalDateTime.now();  
						messageObj.setCreatedDate(now);
						//drop a mesage to IndividualChatMessage by the  new Reg User name joined The chat
						indMessageRepository.save(messageObj);
						
				      //
					
						//send a live notification to invited user.
						  WebSocketSession senderSocket=(WebSocketSession)WebscoketBean.getmLiveSession().get(inviterUser.get().getId());
	   	    			   if(senderSocket!=null){
	   	    				   
	   	    				MessageScoketDTO messageDTOObj= new MessageScoketDTO();
	   	    				messageDTOObj.setStatus("newUser");
	   	    				messageDTOObj.setUserManagement(user);
	   	    				
	   	    				Gson gsonObj = new Gson();
	   		    		    gsonObj.toJson(messageDTOObj);
	   	    				 try {
								senderSocket.sendMessage(new TextMessage(gsonObj .toJson(messageDTOObj)));
							} catch (IOException e) {
								
								e.printStackTrace();
							}
	   	    				   
	   	    			   }
						
					}
				}
				
				log.info("new user Registered with email= "+registerDTO.getEmailId());

				// send welcome mail to user
				mailSendService.sendWelcomeMailToNewUser(
						registerDTO.getEmailId(), registerDTO.getUserName(),
						registerDTO.getPassword(), registerDTO.getFirstName()
								+ " " + registerDTO.getLastName());
				

			} else {
				status.setMessage("emailId is already taken!");
				return null;
			}

		} else {

			status.setMessage("Username is already taken!");
		}
		return new ResponseEntity<>(status, HttpStatus.OK);

	}
	
	
	/*public void createThumbnail(File sourceImage, int width,int height,String outputFile) {
		String destImageName =outputFile ;

		File destImage = new File(destImageName);
		if (!thumbNailFile.exists()) {
		BufferedImage img = ImageIO.read(sourceImage);
		BufferedImage thumbImg = Scalr.resize(img, Method.QUALITY, Mode.AUTOMATIC, width, height,Scalr.OP_ANTIALIAS);
		ImageIO.write(thumbImg, fileFormat, destImage);
		}
		}*/
	
	
	//thumbnail image  creation 
	private  void saveScaledImage(String filePath,String outputFile){
	    try {

	        BufferedImage sourceImage = ImageIO.read(new File(filePath));
	        int width = sourceImage.getWidth();
	        int height = sourceImage.getHeight();

	        if(width>height){
	            float extraSize=    height-100;
	            float percentHight = (extraSize/height)*100;
	            float percentWidth = width - ((width/100)*percentHight);
	            BufferedImage img = new BufferedImage((int)percentWidth, 100, BufferedImage.TYPE_INT_RGB);
	            Image scaledImage = sourceImage.getScaledInstance((int)percentWidth, 100, Image.SCALE_SMOOTH);
	            img.createGraphics().drawImage(scaledImage, 0, 0, null);
	            BufferedImage img2 = new BufferedImage(100, 100 ,BufferedImage.TYPE_INT_RGB);
	            img2 = img.getSubimage((int)((percentWidth-100)/2), 0, 100, 100);

	            ImageIO.write(img2, "jpg", new File(outputFile));    
	        }else{
	            float extraSize=    width-100;
	            float percentWidth = (extraSize/width)*100;
	            float  percentHight = height - ((height/100)*percentWidth);
	            BufferedImage img = new BufferedImage(100, (int)percentHight, BufferedImage.TYPE_INT_RGB);
	            Image scaledImage = sourceImage.getScaledInstance(100,(int)percentHight, Image.SCALE_SMOOTH);
	            img.createGraphics().drawImage(scaledImage, 0, 0, null);
	            BufferedImage img2 = new BufferedImage(100, 100 ,BufferedImage.TYPE_INT_RGB);
	            img2 = img.getSubimage(0, (int)((percentHight-100)/2), 100, 100);

	            ImageIO.write(img2, "jpg", new File(outputFile));
	        }

	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }

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

	

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public ModelAndView welcome() {
		ModelAndView model = new ModelAndView();
		model.setViewName("welcome");
		return model;
	}

	@RequestMapping(value = "/aaa", method = RequestMethod.GET)
	public ModelAndView aaa() {
		ModelAndView model = new ModelAndView();
		model.setViewName("redirect:swagger-ui.html");
		return model;
	}
	
	
	


	
	
	
	/*

	@Value("${server.upload.path}")
	private String fileSaveDirectory;
	
	@Inject
	private UserRepository userRepository;

	@Inject
	private RoleRepository roleRepository;

	@Inject
	private PasswordEncoder passwordEncoder;
	
	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
	@RequestMapping(value = {"/register" }, method = RequestMethod.GET)
	public String register() {
		return "register";
	}
	
	@PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> register(
              MultipartHttpServletRequest request)
		throws URISyntaxException {

	String threadPid = request.getParameterValues("threadPid")[0];
	Gson gson = new Gson();
	Register registerDTO = gson.fromJson(threadPid, Register.class);

	Status status = new Status();

	MultipartFile file = request.getFile("file");
	Optional<UserManagement> userExist = userRepository
			.findByUserNameIgnoreCase(registerDTO.getUserName());

	
	if (!userExist.isPresent()) {
		
		Optional<UserManagement> userEmailExist = userRepository
				.findByEmail(registerDTO.getEmailId());
		
		if (!userEmailExist.isPresent()) {
			UserManagement user = new UserManagement();
			user.setEmail(registerDTO.getEmailId());
			user.setActivated(true);
			user.setFirstName(registerDTO.getFirstName());
			user.setLastName(registerDTO.getLastName());
			user.setUserName(registerDTO.getUserName());
			user.setGender(registerDTO.getGender());
			// encoading password
			user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

			Random rand = new Random();
			int n = rand.nextInt(500000) + 1;
			long number = n;

			user.setUkMessageKey(number);
			user.setRole(roleRepository.findOneByRoleName("USER").get());

			if (!file.isEmpty()) {

				try {
					byte[] bytes = file.getBytes();

					user.setProfileImage(bytes);

					Random rnd = new Random();
					int randmNumber = 100000 + rnd.nextInt(900000);

					String fileLocation = getFileSaveDirectory("profilePic")
							+ "/" + randmNumber + file.getOriginalFilename();
					writeToDisk(file.getBytes(), fileLocation, "profilePic");

					user.setProfileImageUrl(fileLocation);

					status.setMessage("Saved");

				} catch (Exception e) {
					status.setMessage("Error");
					return null;
				}
			}

			userRepository.save(user);
			status.setMessage("New user Registered..!!!");
			
		}else{
			status.setMessage("emailId is already taken!");
			return null;
		}

	}else{
		
		status.setMessage("Username is already taken!");
	}
	return new ResponseEntity<>(status, HttpStatus.OK);

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

	

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public ModelAndView welcome() {
		ModelAndView model = new ModelAndView();
		model.setViewName("welcome");
		return model;
	}

	@RequestMapping(value = "/aaa", method = RequestMethod.GET)
	public ModelAndView aaa() {
		ModelAndView model = new ModelAndView();
		model.setViewName("redirect:swagger-ui.html");
		return model;
	}
	
	
	

*/}
