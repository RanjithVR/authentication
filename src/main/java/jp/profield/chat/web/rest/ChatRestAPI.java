package jp.profield.chat.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import jp.profield.chat.api.dto.FriendRequestDTO;
import jp.profield.chat.model.FriendRequest;
import jp.profield.chat.model.UserManagement;
import jp.profield.chat.models.repository.UserRepository;
import jp.profield.chat.non_model.Status;
import jp.profield.chat.non_model.UserInviteDTO;
import jp.profield.chat.security.uti.SecurityUtils;
import jp.profield.chat.service.ChatService;
import jp.profield.chat.web.rest.dto.IndividualChatDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ChatRestAPI {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ChatService  chatService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/api/test/users")
	public List<UserManagement> getUsers() {
		
		List<UserManagement>users = userRepository.findAll();
		return users;
	}
	
	
    @RequestMapping(value = "/api/inviteUser", method = RequestMethod.POST) 
    public ResponseEntity<UserInviteDTO> inviteUser(@RequestParam("userId") String userId, @RequestParam("emailId") String emailId
    							, HttpServletResponse response) {
    	
    	UserInviteDTO userInviteDTO = new UserInviteDTO();
    	Optional<UserManagement> currentUser = userRepository.findById(userId);
    	
    	if(currentUser.isPresent()){
    		userInviteDTO= chatService.inviteNewUserByMail(emailId, currentUser.get());
    	}
    	
    	return new ResponseEntity<>(userInviteDTO, HttpStatus.OK);
    	
    }
    
    @GetMapping(value = "/api/changePassword", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> changePassword(@RequestParam String newPassword,@RequestParam String currentPass,
			@RequestParam String userId){
		Status status= new Status();
		
		
		Optional<UserManagement> currentUser = userRepository.findOneByid(userId);
		if(currentUser.isPresent()){
			if(newPassword!=null && newPassword!=""){
				//check current password
				if(passwordEncoder.matches(newPassword, currentUser.get().getPassword())){
					UserManagement userObj=currentUser.get();
					userObj.setPassword(passwordEncoder.encode(newPassword));
					userRepository.save(userObj);
					status.setMessage("passwordChanged");
				}else{
					status.setMessage("currentPasswordWorng.");
				}
				
			}
		}else{
			status.setMessage("userNotFound");
		}
		return new ResponseEntity<>(status, HttpStatus.OK);
    }
    
	@GetMapping("/api/getAllFirendRequest")
	public List<FriendRequestDTO> getAllFriendsRequestByUserId(
			@RequestParam String userId) {

		List<FriendRequestDTO> friendReqList = new ArrayList<FriendRequestDTO>();

		Optional<UserManagement> currentUser = userRepository.findById(userId);
		if (currentUser.isPresent()) {

			List<FriendRequest> friendRequestList = chatService
					.findAllPendingRequestByUserId(userId, false);
			if (friendRequestList != null) {
			
				friendReqList=friendRequestList.stream().map(FriendRequestDTO::new).collect(Collectors.toList());
				
			}
		}

		return friendReqList;
	}

	@GetMapping(value = "/api/approvefriendReq", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> approveFriendRequest(
			@RequestParam String friendId, @RequestParam String userId) {
		Status status = new Status();

		Optional<UserManagement> currentUser = userRepository.findById(userId);

		if (currentUser.isPresent()) {

			status = chatService.approveFriendRequestByUserId(friendId,
					currentUser.get());
		}

		return new ResponseEntity<>(status, HttpStatus.OK);
	}

	
	@GetMapping(value = "/api/rejectFriendReq", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> rejectFriendRequest(
			@RequestParam String friendId, @RequestParam String userId) {
		Status status = new Status();

		Optional<UserManagement> currentUser = userRepository.findById(userId);

		if (currentUser.isPresent()) {

			status = chatService.rejectFriendRequestByUserId(friendId,
					currentUser.get());
		}

		return new ResponseEntity<>(status, HttpStatus.OK);
	}
	
	@PostMapping(value="/api/deRegisterUser", produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<Status> deregisterUser(@RequestParam String userId){
		
		Status status = new Status();
		Optional<UserManagement> currentUser = userRepository.findById(userId);
		
		if (currentUser.isPresent()) {
			status = chatService.userSelfDeRegistration(currentUser.get());
			
		}
		return new ResponseEntity<>(status,HttpStatus.OK);
				
		
	}

}