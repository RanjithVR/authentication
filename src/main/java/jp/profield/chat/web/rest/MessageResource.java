package jp.profield.chat.web.rest;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import jp.profield.chat.WebscoketBean;
import jp.profield.chat.model.Attachment;
import jp.profield.chat.model.ContactList;
import jp.profield.chat.model.FriendRequest;
import jp.profield.chat.model.Group;
import jp.profield.chat.model.GroupMembers;
import jp.profield.chat.model.GroupMessage;
import jp.profield.chat.model.IndividualChatMessage;
import jp.profield.chat.model.UserManagement;
import jp.profield.chat.models.repository.AttachemntRepository;
import jp.profield.chat.models.repository.GroupMessageRepository;
import jp.profield.chat.models.repository.GroupRepository;
import jp.profield.chat.models.repository.IndividualChatMessageRepository;
import jp.profield.chat.models.repository.UserRepository;
import jp.profield.chat.non_model.DeleteMessage;
import jp.profield.chat.non_model.DeleteMessageDTO;
import jp.profield.chat.non_model.GroupDTO;
import jp.profield.chat.non_model.GroupEditDTO;
import jp.profield.chat.non_model.Message;
import jp.profield.chat.non_model.MessageScoketDTO;
import jp.profield.chat.non_model.Register;
import jp.profield.chat.non_model.Status;
import jp.profield.chat.non_model.UserInviteDTO;
import jp.profield.chat.security.uti.SecurityUtils;
import jp.profield.chat.service.AttachmentService;
import jp.profield.chat.service.ChatService;
import jp.profield.chat.service.ContactListService;
import jp.profield.chat.service.FriendRequestService;
import jp.profield.chat.service.GroupMembersService;
import jp.profield.chat.service.GroupService;
import jp.profield.chat.service.IndividualChatMessageService;
import jp.profield.chat.service.MemberInvitationService;
import jp.profield.chat.web.rest.dto.GroupMessageDTO;
import jp.profield.chat.web.rest.dto.IndividualChatDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;

@Controller
@RequestMapping("/company")
public class MessageResource {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${server.upload.path}")
	private String fileSaveDirectory;

	/*
	 * @Value("${local.upload.path}") private String localfileSaveDirectory;
	 */

	@Autowired
	UserRepository userRepository;

	@Autowired
	IndividualChatMessageRepository chatMessageRepository;

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	GroupMessageRepository groupMessageRepository;

	@Autowired
	private AttachemntRepository attachemntRepository;

	@Autowired
	private ChatService chatService;
	@Autowired
	private MemberInvitationService memberInvitationService;

	@Autowired
	private FriendRequestService friendRequestService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private GroupMembersService groupMembersService;

	@Autowired
	private ContactListService contactListService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private IndividualChatMessageService individualChatMessageService;

	@GetMapping(value = "/message")
	public String getMessage(Model model) {
		return "message";
	}

	@GetMapping(value = "/voice")
	public String getVoice(Model model) {
		return "voice";
	}

	@GetMapping(value = "/chatHome")
	public String getChatScreen(Model model) {

		String userName = SecurityUtils.getCurrentUserLogin();
		Optional<UserManagement> opuser = userRepository
				.findByUserNameIgnoreCase(userName);

		List<UserManagement> allusers = new ArrayList<UserManagement>();

		List<Group> allGroupList = new ArrayList<Group>();
		if (opuser.isPresent()) {

			List<ContactList> contactlist = contactListService
					.findAllActiveContactByUserId(opuser.get().getId(), false);
			List<GroupMembers> userexistGroupList = groupMembersService
					.findAllGroupsByActiveUserId(opuser.get().getId(), false);

			if (userexistGroupList != null) {
				allGroupList = userexistGroupList.stream()
						.map(GroupMembers::getGroup)
						.collect(Collectors.toList());
			}

			if (contactlist != null) {

				allusers = contactlist.stream().map(ContactList::getFriendId)
						.collect(Collectors.toList());
			}

			// allusers.remove(opuser.get());
			model.addAttribute("userId", opuser.get().getId());
			model.addAttribute("receverId", opuser.get().getUkMessageKey());
			model.addAttribute("userFullName", opuser.get().getFirstName());
			model.addAttribute("userEmail", opuser.get().getEmail());
			model.addAttribute("userProfilePic", opuser.get().getAttachment()
					.getId());

			if (opuser.isPresent()) {
				UserManagement user = opuser.get();
				Date date = new Date();
				user.setLoginAt(date);
				user.setIsOnline(true);
				userRepository.save(user);
			}

			model.addAttribute("group", allGroupList);
			model.addAttribute("users", allusers);
		}

		return "chatHome";
	}

	/*
	 * @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
	 * public String testMethod() throws URISyntaxException {
	 * List<UserManagement>userListbyName=userRepository.findUserByName("田");
	 * return fileSaveDirectory;
	 * 
	 * }
	 */
	@PostMapping(value = "/chatHome", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> createMessage(
			@RequestBody Message messageDTO, MultipartHttpServletRequest request)
			throws URISyntaxException {

		Optional<UserManagement> senderUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		// save message
		Optional<UserManagement> opuser = userRepository
				.findByUkMessageKey(messageDTO.getReceiveUserId());
		// messageDTO.setSenderUserId(senderUser.get().getId());
		IndividualChatMessage message = new IndividualChatMessage();
		if (opuser.isPresent()) {
			// pushing using websocket
			messageDTO.setSenderUserId(senderUser.get().getUkMessageKey());
			/*
			 * simpMessagingTemplate.convertAndSend("/message/send/individual/"
			 * + opuser.get().getUkMessageKey(), messageDTO);
			 */
			message.setMessageContent(messageDTO.getMessage());
			message.setReceiverId(opuser.get().getId());
			message.setSenderId(senderUser.get().getId());
			message.setRead(true);
			Date date = new Date();
			// message.setCreatedDate(date.toLocaleString());
			chatMessageRepository.save(message);

		}
		return new ResponseEntity<>(messageDTO, HttpStatus.OK);

	}

	// group message saving

	@PostMapping(value = "/chatHome/sample", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<GroupMessageDTO>> cSample(
			MultipartHttpServletRequest request) throws URISyntaxException {

		// System.out.println("Methodd ++++++++++++++++++++ "+
		// messageDTO.getReceiveUserId());
		String threadPid = request.getParameterValues("threadPid")[0];

		Gson gson = new Gson();
		Message messageDTO = gson.fromJson(threadPid, Message.class);
		MultipartFile file = request.getFile("file");

		GroupMessage grpMessage = new GroupMessage();
		List<GroupMessage> messageList = new ArrayList<GroupMessage>();
		List<GroupMessageDTO> result = new ArrayList<GroupMessageDTO>();

		Optional<UserManagement> senderUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		if (file != null) {

			try {
				Random rnd = new Random();
				int randmNumber = 100000 + rnd.nextInt(900000);
				String path = request.getServletContext().getRealPath("/");

				/*
				 * if(Debug.STATUS){ String fileLocation =
				 * getFileSaveDirectory("image") + "/" + randmNumber +
				 * file.getOriginalFilename();
				 * 
				 * }else{ // fileLocation = getFileSaveDirectory("attachmets") +
				 * "/" + randmNumber+ file.getOriginalFilename(); }
				 */
				String fileLocation = getFileSaveDirectory("image") + "/"
						+ randmNumber + file.getOriginalFilename();

				System.out.println("File location ++:" + fileLocation);
				writeToDisk(file.getBytes(), fileLocation, "image");
				Attachment attachmentObj = new Attachment();
				// String cwd = System.getProperty("user.dir");
				// System.out.println("Current working directory : " + cwd);

				attachmentObj.setLocation(fileLocation);
				attachmentObj.setActivated(true);
				attachmentObj.setAttachmentName(file.getOriginalFilename());
				attachmentObj.setExtension(file.getContentType());
				// System.out.println("Ext==="+file.getContentType());
				attachmentObj.setUploadedDate(LocalDateTime.now());
				attachmentObj = attachemntRepository.save(attachmentObj);
				grpMessage.setAttachemnt(attachmentObj);
				grpMessage.setIsAttachment(true);

			} catch (IOException ex) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

		}

		messageDTO.setSenderName(senderUser.get().getFirstName());
		grpMessage.setIsDelete(false);
		grpMessage.setGroupId(messageDTO.getGroupId());
		grpMessage.setMessageContent(messageDTO.getMessage());
		grpMessage.setIsRead(true);
		grpMessage.setSenderId(messageDTO.getSenderId());
		grpMessage.setSenderName(messageDTO.getSenderName());
		LocalDateTime now = LocalDateTime.now();
		grpMessage.setCreatedDate(now);

		groupMessageRepository.save(grpMessage);
		messageList.add(grpMessage);
		result = messageList.stream().map(GroupMessageDTO::new)
				.collect(Collectors.toList());

		// simpMessagingTemplate.convertAndSend("/message/send/group/" +
		// messageDTO.getReceiveUserId(), messageDTO);
		// simpMessagingTemplate.convertAndSend("/message/send/group/" +
		// messageDTO.getReceiveUserId(), result);

		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@PostMapping(value = "/chatHome/inviteUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserInviteDTO> inviteNewUser(
			@RequestParam String emailId) throws URISyntaxException {

		UserInviteDTO userInviteDTO = new UserInviteDTO();
		
		Optional<UserManagement> senderUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		if (emailId != null && emailId != "") {
			userInviteDTO = chatService.inviteNewUserByMail(emailId,
					senderUser.get());

		}
		return new ResponseEntity<>(userInviteDTO, HttpStatus.OK);

	}

	@GetMapping(value = "/chatHome/loadGroupDeatils", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GroupEditDTO> loadGroupDeatils(
			@RequestParam String groupId) throws URISyntaxException {

		GroupEditDTO groupDetails = new GroupEditDTO();
		Optional<UserManagement> senderUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		if (groupId != null && groupId != "") {
			Group group = groupService.findByGroupIdAndActive(groupId, false);
			if (group != null) {

				List<GroupMembers> groupMember = groupMembersService
						.getAllActiveMemberOfGrpByGroupId(group.getId());
				groupDetails.setGroupMembersList(groupMember);

				GroupMembers grpmember = groupMembersService
						.getCurrentUserGrpInfoByUserIdAndGrpId(groupId,
								senderUser.get().getId());
				if (grpmember != null) {
					groupDetails.setCurrentUserGrpInfo(grpmember);
				}

			}
			groupDetails.setGroup(group);
		}
		return new ResponseEntity<>(groupDetails, HttpStatus.OK);

	}

	@PostMapping(value = "/chatHome/getMembersToAddGroup", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserManagement> getMembersToAddGroup(
			@RequestParam String groupId) {

		Optional<UserManagement> currentUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		if (currentUser.isPresent()) {

			List<ContactList> contactList = contactListService
					.findAllContactListByUserId(currentUser.get().getId());
			List<GroupMembers> groupMembers = groupMembersService
					.getAllActiveMemberOfGrpByGroupId(groupId);

			System.out.println("The  Members are ..!!!");
		}

		return null;

	}

	@PostMapping(value = "/chatHome/sendFrndRequest", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> sendFrndRequest(
			@RequestParam String userIdInvite) throws URISyntaxException {

		Status status = new Status();
		Optional<UserManagement> currentUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		Optional<UserManagement> inviteeUser = userRepository
				.findById(userIdInvite);
		if (inviteeUser.isPresent()) {

			status = chatService.sendFriendRequest(inviteeUser.get().getId(),
					currentUser.get());

		}
		return new ResponseEntity<>(status, HttpStatus.OK);

	}

	// change the code
	@SuppressWarnings("null")
	@PostMapping(value = "/chatHome/upload-attachment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<IndividualChatDTO>> uploadEditFile(
			MultipartHttpServletRequest request) {

		String threadPid = request.getParameterValues("threadPid")[0];
		Gson gson = new Gson();
		Message message = gson.fromJson(threadPid, Message.class);

		Optional<UserManagement> senderUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		Optional<UserManagement> opuser = userRepository
				.findByUkMessageKey(message.getReceiveUserId());
		IndividualChatMessage messageObj = new IndividualChatMessage();
		List<IndividualChatMessage> messageList = new ArrayList<IndividualChatMessage>();
		List<IndividualChatDTO> result = new ArrayList<IndividualChatDTO>();

		MultipartFile file = request.getFile("file");

		/*
		 * if (file == null ) {//&& file.isEmpty() //return new
		 * ResponseEntity<>(HttpStatus.NO_CONTENT); } else
		 */

		if (file != null) {

			try {

				Random rnd = new Random();
				int randmNumber = 100000 + rnd.nextInt(900000);

				String path = request.getServletContext().getRealPath("/");
				// String ppppp=getFileSaveDirectory(fileSaveDirectory);
				// System.out.println(path);

				/*
				 * if(Debug.STATUS){
				 * 
				 * String fileLocation = getFileSaveDirectory("attachmets") +
				 * "/" + randmNumber + file.getOriginalFilename();
				 * 
				 * }else{ // fileLocation = getFileSaveDirectory("attachmets") +
				 * "/" + randmNumber+ file.getOriginalFilename(); }
				 */
				String fileLocation = getFileSaveDirectory("image") + "/"
						+ randmNumber + file.getOriginalFilename();

				System.out.println("File location ++:" + fileLocation);

				writeToDisk(file.getBytes(), fileLocation, "image");
				Attachment attachmentObj = new Attachment();

				String cwd = System.getProperty("user.dir");
				System.out.println("Current working directory : " + cwd);

				attachmentObj.setLocation(fileLocation);
				attachmentObj.setActivated(true);
				attachmentObj.setAttachmentName(file.getOriginalFilename());
				attachmentObj.setExtension(file.getContentType());
				System.out.println("File exte ++:" + file.getContentType());
				attachmentObj.setUploadedDate(LocalDateTime.now());

				attachmentObj = attachemntRepository.save(attachmentObj);
				messageObj.setAttachemnt(attachmentObj);
				messageObj.setIsAttachment(true);

			} catch (IOException ex) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

		}

		if (opuser.isPresent()) {

			messageObj.setSenderId(senderUser.get().getId());

			messageObj.setMessageContent(message.getMessage());
			messageObj.setReceiverId(opuser.get().getId());
			messageObj.setSenderId(senderUser.get().getId());
			messageObj.setRead(true);

			LocalDateTime now = LocalDateTime.now();
			messageObj.setCreatedDate(now);

			messageObj = chatMessageRepository.save(messageObj);
			messageObj.setSenderUserUkId(senderUser.get().getUkMessageKey());
			messageList.add(messageObj);
			result = messageList.stream().map(IndividualChatDTO::new)
					.collect(Collectors.toList());

			/*
			 * simpMessagingTemplate.convertAndSend("/message/send/individual/"
			 * + opuser.get().getUkMessageKey(), messageObj);
			 */

			/*
			 * simpMessagingTemplate.convertAndSend("/message/send/individual/"
			 * + opuser.get().getUkMessageKey(), result);
			 */

		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	private void writeToDisk(byte[] fileBytes, String fileLocation,
			String folderName) {
		try {
			File fileDir = new File(getFileSaveDirectory(folderName));
			if (!fileDir.exists()) {
				// fileDir.mkdirs();

				boolean retval = fileDir.mkdirs();

				// evaluate the result
				if (retval) {
					System.out.println("Directory : " + fileDir
							+ " created successfully");
				} else {
					System.out.println("Directory : " + fileDir
							+ " creation failed");
				}
			}
			FileCopyUtils.copy(fileBytes, new File(fileLocation));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private String getFileSaveDirectory(String folderName) {
		// return FILE_SAVE_DIRECTORY +
		// SecurityUtils.getCurrentUsersCompanyId();
		return fileSaveDirectory + folderName;
	}

	@GetMapping(value = "/getMessageAll/{receiverId}")
	public ResponseEntity<Map<LocalDate, List<IndividualChatDTO>>> getMessage(
			@PathVariable String receiverId) throws URISyntaxException {

		Optional<UserManagement> senderUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		Optional<UserManagement> opuser = userRepository
				.findOneByid(receiverId);
		if (opuser.isPresent()) {

			List<IndividualChatMessage> messageList = chatMessageRepository
					.findAllByReceiverIdAndSenderId(receiverId, senderUser
							.get().getId());

			List<IndividualChatDTO> result = messageList.stream()
					.map(IndividualChatDTO::new).collect(Collectors.toList());
			Map<LocalDate, List<IndividualChatDTO>> mapValues = result
					.parallelStream().collect(
							Collectors.groupingBy(pl -> pl.getDate()));

			// sort by keys, a,b,c..., and return a new LinkedHashMap
			// toMap() will returns HashMap by default, we need LinkedHashMap to
			// keep the order.
			Map<LocalDate, List<IndividualChatDTO>> mapObj = mapValues
					.entrySet()
					.stream()
					.sorted(Map.Entry.comparingByKey())
					.collect(
							Collectors.toMap(Map.Entry::getKey,
									Map.Entry::getValue,
									(oldValue, newValue) -> oldValue,
									LinkedHashMap::new));

			// Not Recommend, but it works.
			// Alternative way to sort a Map by keys, and put it into the
			// "result" map
			Map<LocalDate, List<IndividualChatDTO>> result2 = new LinkedHashMap<>();
			mapObj.entrySet().stream().sorted(Map.Entry.comparingByKey())
					.forEachOrdered(x -> result2.put(x.getKey(), x.getValue()));

			// List<Integer> resultzz = new ArrayList(mapValues.keySet());
			// Map<LocalDate, List<IndividualChatDTO>> inv = invert(mapValues);
			// messageList.addAll(chatMessageRepository.findAllByReceiverIdAndSenderId(receiverId,
			// senderUser.get().getId()));
			// messageList.stream().filter(mess->mess.getCreatedDate())
			// List<IndividualChatMessage> reverseView =
			// Lists.reverse(messageList);

			return new ResponseEntity<>(result2, HttpStatus.OK);
		}

		return null;

	}

	@GetMapping(value = "/getGroupMessageAll/{groupId}")
	public ResponseEntity<Map<LocalDate, List<GroupMessageDTO>>> getAllGroupMessageByGrpId(
			@PathVariable String groupId) throws URISyntaxException {

		Optional<Group> group = groupRepository.findById(groupId);

		if (group.isPresent()) {

			List<GroupMessage> groupMessageList = groupMessageRepository
					.findAllBygroupId(groupId);

			List<GroupMessageDTO> result = groupMessageList.stream()
					.map(GroupMessageDTO::new).collect(Collectors.toList());

			Map<LocalDate, List<GroupMessageDTO>> mapValues = result
					.parallelStream().collect(
							Collectors.groupingBy(pl -> pl.getDate()));

			Map<LocalDate, List<GroupMessageDTO>> mapObj = mapValues
					.entrySet()
					.stream()
					.sorted(Map.Entry.comparingByKey())
					.collect(
							Collectors.toMap(Map.Entry::getKey,
									Map.Entry::getValue,
									(oldValue, newValue) -> oldValue,
									LinkedHashMap::new));

			Map<LocalDate, List<GroupMessageDTO>> result2 = new LinkedHashMap<>();
			mapObj.entrySet().stream().sorted(Map.Entry.comparingByKey())
					.forEachOrdered(x -> result2.put(x.getKey(), x.getValue()));

			return new ResponseEntity<>(result2, HttpStatus.OK);
		}

		return null;

	}
	

 @PostMapping(value = "/chatHome/getMembersToAddNewGroup", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserManagement>> getMembersToAddNewGroup() {
		
		List<UserManagement> allusers = new ArrayList<UserManagement>();

		Optional<UserManagement> currentUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());
		if(currentUser.isPresent()){
			List<ContactList> contactlist = contactListService
					.findAllActiveContactByUserId(currentUser.get().getId(), false);
			if(contactlist!=null) {
				allusers = contactlist.stream().map(ContactList::getFriendId)
						.collect(Collectors.toList());
			}
		
		}

		return new ResponseEntity<>(allusers, HttpStatus.OK);
	}

	@GetMapping(value = "/attachments/{pid}")
	public ResponseEntity<byte[]> getAttachment(@PathVariable String pid) {
		log.debug("Request to get profileId {}", pid);
		System.out.println(pid);

		Optional<Attachment> attch = attachemntRepository.findById(pid);
		if (attch.isPresent()) {
			File physicalFile = getPhysicalFileByFile(attch.get().getLocation());
			if (physicalFile.exists()) {
				try {
					byte[] fileContent = Files.readAllBytes(physicalFile
							.toPath());

					log.debug("Response @@@@", pid);
					log.debug("byte Response #####", fileContent);

					return new ResponseEntity<>(fileContent, HttpStatus.OK);
					// return new
					// ResponseEntity<>(Files.toByteArray(physicalFile),
					// HttpStatus.OK);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		byte[] newByte = new byte[0];

		return new ResponseEntity<>(newByte, HttpStatus.NO_CONTENT);
	}

	/*
	 * @GetMapping(value = "/profile/{url}") public ResponseEntity<byte[]>
	 * getAttachmentProfile(@PathVariable String url) { //
	 * log.debug("rest web request to get a Coatingtype by pid {}", pid);
	 * 
	 * File physicalFile = getPhysicalFileByFile(url); if
	 * (physicalFile.exists()) { try { return new
	 * ResponseEntity<>(Files.toByteArray(physicalFile), HttpStatus.OK); } catch
	 * (IOException e) { e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * //byte[] newByte= new byte[0];
	 * 
	 * return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	 * 
	 * }
	 */

	public File getPhysicalFileByFile(String path) {
		File physicalFile = new File(path);
		return physicalFile;
	}

	@GetMapping(value = "/attachments/getpdf/{attachmentPid}")
	public ResponseEntity<byte[]> getPDF(@PathVariable String attachmentPid)
			throws IOException {
		// log.debug("Request to upload a file : {}", attachmentPid);
		// Optional<Attachment> opAttachment =
		// attachmentService.findOneByPidAndActivated(attachmentPid, true);
		Optional<Attachment> opAttachment = attachemntRepository
				.findById(attachmentPid);
		if (opAttachment.isPresent()) {

			File physicalFile = getPhysicalFileByFile(opAttachment.get()
					.getLocation());
			System.out.println(physicalFile);
			if (physicalFile.exists()) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType
						.parseMediaType("application/pdf"));
				// String filename = "output.pdf";
				// headers.setContentDispositionFormData(filename, filename);
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

				byte[] fileContent = Files.readAllBytes(physicalFile.toPath());

				ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
						fileContent, headers, HttpStatus.OK);
				return response;
			}
		}
		return null;
	}

	@GetMapping("/attachments/download/{attachmentPid}")
	public StreamingResponseBody getSteamingFile(HttpServletResponse response,
			@PathVariable String attachmentPid) throws IOException {
		log.debug("Request to download a file : {}", attachmentPid);
		Optional<Attachment> opAttachment = attachemntRepository
				.findById(attachmentPid);
		if (opAttachment.isPresent()) {

			File physicalFile = getPhysicalFileByFile(opAttachment.get()
					.getLocation());
			if (physicalFile.exists()) {
				response.setContentType(opAttachment.get().getMimeType());
				response.addHeader("Content-Disposition",
						"attachment; filename="
								+ opAttachment.get().getAttachmentName());

				response.setContentLength((int) physicalFile.length());
				InputStream inputStream = new FileInputStream(physicalFile);
				return outputStream -> {
					int nRead;
					byte[] data = new byte[1024];
					while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
						outputStream.write(data, 0, nRead);
					}
					inputStream.close();
				};
			}
		}
		return null;
	}

	@PostMapping(value = "/chatHome/saveMp3", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<IndividualChatDTO>> saveMp3(
			@RequestBody Message messageDTO) throws URISyntaxException,
			IOException {

		// System.out.println(messageDTO.getMessage());
		Optional<UserManagement> senderUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		Optional<UserManagement> opuser = userRepository
				.findByUkMessageKey(messageDTO.getReceiveUserId());

		IndividualChatMessage messageObj = new IndividualChatMessage();
		List<IndividualChatMessage> messageList = new ArrayList<IndividualChatMessage>();
		List<IndividualChatDTO> result = new ArrayList<IndividualChatDTO>();

		if (opuser.isPresent()) {

			String extension = ".mp3";
			byte[] decodedBytes;
			String filedata = messageDTO.getMessage();
			String split[] = filedata.split("data:audio/mp3;base64,");
			String data = split[1];
			decodedBytes = Base64.getDecoder().decode(data);
			Random rnd = new Random();
			int randmNumber = 100000 + rnd.nextInt(900000);
			String fileName = "voiceRecord" + randmNumber;

			String fileLocation = getFileSaveDirectory("voice") + "/"
					+ fileName + extension;
			writeToDisk(decodedBytes, fileLocation, "voice");
			Attachment attachmentObj = new Attachment();

			attachmentObj.setLocation(fileLocation);
			attachmentObj.setActivated(true);
			attachmentObj.setAttachmentName("Voice File");
			attachmentObj.setExtension(extension);
			attachmentObj.setUploadedDate(LocalDateTime.now());

			attachmentObj = attachemntRepository.save(attachmentObj);

			messageObj.setAttachemnt(attachmentObj);
			messageObj.setIsAttachment(true);
			messageObj.setSenderId(senderUser.get().getId());
			messageObj.setMessageContent("");
			messageObj.setReceiverId(opuser.get().getId());
			messageObj.setSenderId(senderUser.get().getId());
			messageObj.setRead(true);
			messageObj.setCreatedDate(LocalDateTime.now());
			messageObj = chatMessageRepository.save(messageObj);

			messageObj.setSenderUserUkId(senderUser.get().getUkMessageKey());
			messageList.add(messageObj);
			result = messageList.stream().map(IndividualChatDTO::new)
					.collect(Collectors.toList());

			/*
			 * simpMessagingTemplate.convertAndSend("/message/send/individual/"
			 * + opuser.get().getUkMessageKey(), result);
			 */

		}

		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@GetMapping(value = "/chatHome/deleteMessage", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DeleteMessage> deleteMessage(
			@RequestParam(value = "messageIds[]") String[] messageIds) {

		DeleteMessage status = new DeleteMessage();

		Optional<UserManagement> senderUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());
		if (senderUser.isPresent()) {

			List<IndividualChatMessage> messageList = individualChatMessageService
					.findAllMessageByMessageIds(messageIds);

			if (messageList != null) {
				for (IndividualChatMessage messageObj : messageList) {

					messageObj.setIsDelete(true);
					individualChatMessageService.saveMessage(messageObj);
				}

				status.setStatus("sucess");
				status.setMessageid(messageIds);
				
				//check the friend socket
				
				WebSocketSession friendSockt = (WebSocketSession) WebscoketBean
						.getmLiveSession().get(messageList.get(0).getReceiverId());
				if (friendSockt != null) {
					
					DeleteMessageDTO deleteMessageObj = new DeleteMessageDTO();
					deleteMessageObj.setStatus("delete_Ind_Message");
					deleteMessageObj.setReceiverId(messageList.get(0).getReceiverId());
					deleteMessageObj.setMessageId(messageIds);
					deleteMessageObj.setSenderId(senderUser.get().getId());
					
					Gson gsonObj = new Gson();
					gsonObj.toJson(deleteMessageObj);
					try {
						friendSockt.sendMessage(new TextMessage(gsonObj
								.toJson(deleteMessageObj)));
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
				
				
				
			} else {
				status.setStatus("failed");
			}
		}

		return new ResponseEntity<>(status, HttpStatus.OK);

	}

	@GetMapping(value = "/chatHome/addMembers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> contactList(@RequestParam String groupId)
			throws URISyntaxException {

		Optional<UserManagement> senderUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		List<GroupMembers> membersList = groupMembersService
				.getAllGrpMembersIdByGroupId(groupId);
		List<ContactList> contactList = contactListService
				.findAllActiveContactByUserId(senderUser.get().getId(), false);
		System.out.println("Haiii");

		return new ResponseEntity<>(membersList.get(0).getId(), HttpStatus.OK);

	}

	@GetMapping(value = "/chatHome/deRegisterUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> userSelfDeRegister() {

		Status status = new Status();
		Optional<UserManagement> currentUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		if (currentUser.isPresent()) {
			status = chatService.userSelfDeRegistration(currentUser.get());

		}

		return new ResponseEntity<>(status, HttpStatus.OK);

	}

	@GetMapping(value = "/chatHome/searchPeople", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> searchPeople(@RequestParam String userName) {

		Status status = new Status();

		Optional<UserManagement> currentUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		if (currentUser.isPresent()) {

			Optional<UserManagement> searchUserObj = userRepository.findByUserNameIgnoreCase(userName);
			if(searchUserObj.isPresent()){
				
				if(!searchUserObj.get().getIsDeleted()){
					
					status.setMessage("userFound");
					status.setUserObj(searchUserObj.get());
					
					
					
					ContactList contactlist = contactListService
							.findContactListExsistByUserIdAndFrndId(currentUser.get().getId(), 
									searchUserObj.get().getId());
					if(contactlist!=null){
						if(!contactlist.getIsDeleted()){
							status.setMessage("friend");
						}
						
					}
					
	
					Optional<FriendRequest> requestExist = friendRequestService
							.findByInviteeIdAndUserId( searchUserObj.get().getId(),currentUser.get().getId(),false);
					if(requestExist.isPresent()){
						if(!requestExist.get().isActedOrNot()){
							status.setMessage("requestSend");
						}
						
					}
					
					
					
					
				}
	 }else{
			status.setMessage("userNotFound");
	}
			

		}

		return new ResponseEntity<>(status, HttpStatus.OK);

	}

	@GetMapping(value = "/chatHome/friendsRequest", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FriendRequest>> findFriendRequest() {

		Optional<UserManagement> currentUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		List<FriendRequest> friendRequestist = new ArrayList<FriendRequest>();

		if (currentUser.isPresent()) {
			friendRequestist = friendRequestService
					.getAllPendingFriendRequestByUserId(currentUser.get()
							.getId(), false);
		}

		return new ResponseEntity<>(friendRequestist, HttpStatus.OK);

	}

	// TODO
	@GetMapping(value = "/chatHome/changePassword", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> changePassword(
			@RequestParam String newPassword, @RequestParam String currentPass) {
		Status status = new Status();

		Optional<UserManagement> currentUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());
		if (currentUser.isPresent()) {
			if (newPassword != null && newPassword != "") {
				// check current password
				if (passwordEncoder.matches(newPassword, currentUser.get()
						.getPassword())) {
					UserManagement userObj = currentUser.get();
					userObj.setPassword(passwordEncoder.encode(newPassword));
					userRepository.save(userObj);
					status.setMessage("passwordChanged");
				} else {
					status.setMessage("currentPasswordWorng.");
				}

			}
		} else {
			status.setMessage("userNotFound");
		}

		return new ResponseEntity<>(status, HttpStatus.OK);

	}

	// Accept friend Request

	@GetMapping(value = "/chatHome/acceptFriendRequest", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> acceptFriendRequest(
			@RequestParam String friendId) {
		Status status = new Status();
		Optional<UserManagement> currentUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());
		if (currentUser.isPresent()) {

			status = chatService.approveFriendRequestByUserId(friendId,
					currentUser.get());

		}
		return new ResponseEntity<>(status, HttpStatus.OK);

	}

	// reject friend Request

	@GetMapping(value = "/chatHome/rejectFriendRequest", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> rejectFriendRequest(
			@RequestParam String friendId) {
		Status status = new Status();
		Optional<UserManagement> currentUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());
		if (currentUser.isPresent()) {

			status = chatService.rejectFriendRequestByUserId(friendId,
					currentUser.get());

		}
		return new ResponseEntity<>(status, HttpStatus.OK);

	}

	@GetMapping(value = "/chatHome/userDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserManagement> getUserDetailsForEditProfile() {

		UserManagement userobj = new UserManagement();
		Optional<UserManagement> currentUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());
		if (currentUser.isPresent()) {

			userobj.setId(currentUser.get().getId());
			userobj.setFirstName(currentUser.get().getFirstName());
			userobj.setLastName(currentUser.get().getLastName());
			userobj.setAttachment(currentUser.get().getAttachment());
			userobj.setUserName(currentUser.get().getUserName());
			userobj.setEmail(currentUser.get().getEmail());
			userobj.setGender(currentUser.get().getGender());
		}

		return new ResponseEntity<>(userobj, HttpStatus.OK);

	}

	@PostMapping(value = "/chatHome/saveToGroupMp3", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<GroupMessageDTO>> saveToGroupMp3(
			@RequestBody Message messageDTO) throws URISyntaxException,
			IOException {

		Optional<UserManagement> senderUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		List<GroupMessage> messageList = new ArrayList<GroupMessage>();
		List<GroupMessageDTO> result = new ArrayList<GroupMessageDTO>();

		GroupMessage grpMessage = new GroupMessage();

		String extension = ".mp3";
		byte[] decodedBytes;
		String filedata = messageDTO.getMessage();// base_64 value
		String split[] = filedata.split("data:audio/mp3;base64,");
		String data = split[1];
		decodedBytes = Base64.getDecoder().decode(data);
		Random rnd = new Random();
		int randmNumber = 100000 + rnd.nextInt(900000);
		String fileName = "voiceRecord" + randmNumber;

		String fileLocation = getFileSaveDirectory("voice") + "/" + fileName
				+ extension;
		writeToDisk(decodedBytes, fileLocation, "voice");
		Attachment attachmentObj = new Attachment();

		attachmentObj.setLocation(fileLocation);
		attachmentObj.setActivated(true);
		attachmentObj.setAttachmentName("Voice File");
		attachmentObj.setExtension(extension);
		attachmentObj.setUploadedDate(LocalDateTime.now());
		attachmentObj = attachemntRepository.save(attachmentObj);

		grpMessage.setAttachemnt(attachmentObj);
		grpMessage.setIsAttachment(true);

		grpMessage.setIsDelete(false);
		grpMessage.setGroupId(messageDTO.getGroupId());
		grpMessage.setMessageContent(messageDTO.getMessage());
		grpMessage.setIsRead(true);
		grpMessage.setSenderId(messageDTO.getSenderId());
		grpMessage.setSenderName(messageDTO.getSenderName());
		LocalDateTime now = LocalDateTime.now();
		grpMessage.setCreatedDate(now);

		groupMessageRepository.save(grpMessage);

		messageList.add(grpMessage);
		result = messageList.stream().map(GroupMessageDTO::new)
				.collect(Collectors.toList());

		// simpMessagingTemplate.convertAndSend("/message/send/group/" +
		// messageDTO.getReceiveUserId(), messageDTO);
		/*
		 * simpMessagingTemplate.convertAndSend("/message/send/group/" +
		 * messageDTO.getReceiveUserId(), result);
		 */
		return new ResponseEntity<>(result, HttpStatus.OK);

	}

	@PostMapping(value = "/chatHome/updateProfile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> register(MultipartHttpServletRequest request)
			throws URISyntaxException {
		Status status = new Status();
		String threadPid = request.getParameterValues("threadPid")[0];
		Gson gson = new Gson();
		Register registerDTO = gson.fromJson(threadPid, Register.class);

		MultipartFile file = request.getFile("file");

		Optional<UserManagement> currentUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		if (currentUser.isPresent()) {
			UserManagement user = currentUser.get();

			if (file != null) {

				try {
					byte[] bytes = file.getBytes();
					user.setProfileImage(bytes);
					Random rnd = new Random();
					int randmNumber = 100000 + rnd.nextInt(900000);
					String fileLocation = getFileSaveDirectory("profilePic")
							+ "/" + randmNumber + file.getOriginalFilename();
					writeToDisk(file.getBytes(), fileLocation, "profilePic");
					String tumbImage = getFileSaveDirectory("profilePic")
							+ "/thumb-" + randmNumber
							+ file.getOriginalFilename();
					saveScaledImage(fileLocation, tumbImage);

					// user.setProfileImageUrl(fileLocation);
					Optional<Attachment> attachmentExist = attachmentService
							.findOneByid(user.getAttachment().getId());
					if (attachmentExist.isPresent()) {

						Attachment attachment = attachmentExist.get();

						attachment.setActivated(true);
						attachment
								.setAttachmentName("profilePic" + randmNumber);
						// attachment.setExtension(extension);
						attachment.setUploadedDate(LocalDateTime.now());
						attachment.setLocation(fileLocation);
						attachment.setThumbfile(tumbImage);
						attachemntRepository.save(attachment);

						user.setAttachment(attachment);
					}

				} catch (Exception e) {
					status.setMessage("Error");
					return null;
				}
			}

			user.setFirstName(registerDTO.getFirstName());
			user.setLastName(registerDTO.getLastName());
			user.setGender(registerDTO.getGender());
			userRepository.save(user);
			status.setMessage("Saved");
		}

		return new ResponseEntity<>(status, HttpStatus.OK);

	}

	@SuppressWarnings("null")
	@PostMapping(value = "/chatHome/saveGroup", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> saveGroup(MultipartHttpServletRequest request) {
		Status status = new Status();
		String threadPid = request.getParameterValues("threadPid")[0];
		Gson gson = new Gson();
		GroupDTO groupDto = gson.fromJson(threadPid, GroupDTO.class);

		Optional<UserManagement> currentUser = userRepository
				.findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());

		if (currentUser.isPresent()) {

			Group group = new Group();
			group.setGroupName(groupDto.getGroupName());
			group.setDescription(groupDto.getDiscription());
			group.setCreatedBy(currentUser.get().getId());

			group.setCreatedOn(new Date());
			group.setIsDeleted(false);
			Random rand = new Random();
			int n = rand.nextInt(5000000) + 1;
			long number = n;
			group.setGroupUniqeId(number);

			MultipartFile file = request.getFile("file");

		

				if (file == null) {
					ServletContext servletContext = request.getSession().getServletContext();
					File groupImage = new File(servletContext.getRealPath("/resources/assets/img/profiles/group.jpg"));
					String fileName="group.jpg";
					try {
						//byte[] bytes = file.getBytes();
						byte[] bytes = Files.readAllBytes(groupImage.toPath());
						group.setProfileImage(bytes);

						Random rnd = new Random();
						int randmNumber = 100000 + rnd.nextInt(900000);

						String fileLocation = getFileSaveDirectory("profilePic")
								+ "/" + randmNumber + fileName;
						writeToDisk(bytes, fileLocation, "profilePic");

						String tumbImage = getFileSaveDirectory("profilePic")
								+ "/thumb-" + randmNumber
								+ fileName;

						saveScaledImage(fileLocation, tumbImage);

						// user.setProfileImageUrl(fileLocation);
						Attachment attachment = new Attachment();
						attachment.setActivated(true);
						attachment.setAttachmentName("GrpprofilePic" + randmNumber);
						// attachment.setExtension(extension);
						attachment.setUploadedDate(LocalDateTime.now());

						attachment.setLocation(fileLocation);
						attachment.setThumbfile(tumbImage);

						attachemntRepository.save(attachment);

						group.setAttachment(attachment);

					} catch (Exception e) {
						status.setMessage("Error");
						return null;
					}
				}
				else {
					try {
						byte[] bytes = file.getBytes();

						group.setProfileImage(bytes);

						Random rnd = new Random();
						int randmNumber = 100000 + rnd.nextInt(900000);

						String fileLocation = getFileSaveDirectory("profilePic")
								+ "/" + randmNumber + file.getOriginalFilename();
						writeToDisk(file.getBytes(), fileLocation, "profilePic");

						String tumbImage = getFileSaveDirectory("profilePic")
								+ "/thumb-" + randmNumber
								+ file.getOriginalFilename();

						saveScaledImage(fileLocation, tumbImage);

						// user.setProfileImageUrl(fileLocation);
						Attachment attachment = new Attachment();
						attachment.setActivated(true);
						attachment.setAttachmentName("GrpprofilePic" + randmNumber);
						// attachment.setExtension(extension);
						attachment.setUploadedDate(LocalDateTime.now());

						attachment.setLocation(fileLocation);
						attachment.setThumbfile(tumbImage);

						attachemntRepository.save(attachment);

						group.setAttachment(attachment);

					} catch (Exception e) {
						status.setMessage("Error");
						return null;
					}
					
				}
			/*if (!file.isEmpty()) {

				try {
					byte[] bytes = file.getBytes();

					group.setProfileImage(bytes);

					Random rnd = new Random();
					int randmNumber = 100000 + rnd.nextInt(900000);

					String fileLocation = getFileSaveDirectory("profilePic")
							+ "/" + randmNumber + file.getOriginalFilename();
					writeToDisk(file.getBytes(), fileLocation, "profilePic");

					String tumbImage = getFileSaveDirectory("profilePic")
							+ "/thumb-" + randmNumber
							+ file.getOriginalFilename();

					saveScaledImage(fileLocation, tumbImage);

					// user.setProfileImageUrl(fileLocation);
					Attachment attachment = new Attachment();
					attachment.setActivated(true);
					attachment.setAttachmentName("GrpprofilePic" + randmNumber);
					// attachment.setExtension(extension);
					attachment.setUploadedDate(LocalDateTime.now());

					attachment.setLocation(fileLocation);
					attachment.setThumbfile(tumbImage);

					attachemntRepository.save(attachment);

					group.setAttachment(attachment);

				} catch (Exception e) {
					status.setMessage("Error");
					return null;
				}
			}*/

			groupService.saveGroup(group);

			// save current user as group owner
			GroupMembers grpOwner = new GroupMembers();

			grpOwner.setGroup(group);
			grpOwner.setUser(currentUser.get());
			grpOwner.setIsDeleted(false);
			grpOwner.setGroupRole(1);// group owner
			grpOwner.setMember(true);
			grpOwner.setCreatedOn(new Date());
			grpOwner.setCreatedBy(currentUser.get().getId());
			groupMembersService.saveGroupMemberToGroup(grpOwner);

			// save group selected member to the Group Member list
			String[] userDeatilsArray = groupDto.getselectedUsers();

			for (String userId : userDeatilsArray) {

				Optional<UserManagement> user = userRepository.findById(userId);
				if (user.isPresent()) {
					GroupMembers grpMember = new GroupMembers();

					grpMember.setGroup(group);
					grpMember.setUser(user.get());
					grpMember.setIsDeleted(false);
					grpMember.setGroupRole(3);// group member
					grpMember.setMember(true);
					grpMember.setCreatedOn(new Date());
					grpMember.setCreatedBy(currentUser.get().getId());

					groupMembersService.saveGroupMemberToGroup(grpMember);
					status.setMessage("Saved");
					// add a messageto group that inform user added by owner
					// name
					GroupMessage grpMessage = new GroupMessage();
					grpMessage.setIsDelete(false);

					String messageData = user.get().getFirstName()
							+ "  Added by  " + currentUser.get().getFirstName();

					grpMessage.setMessageContent(messageData);
					grpMessage.setIsRead(true);
					grpMessage.setMessageType(4);// user added info
					grpMessage.setGroupId(group.getId());
					grpMessage.setSenderId(currentUser.get().getId());
					grpMessage.setSenderName(currentUser.get().getFirstName());
					LocalDateTime now = LocalDateTime.now();
					grpMessage.setCreatedDate(now);
					groupMessageRepository.save(grpMessage);

					// notify the group member With new group deatils.
					WebSocketSession senderSocket = (WebSocketSession) WebscoketBean
							.getmLiveSession().get(user.get().getId());
					if (senderSocket != null) {
						MessageScoketDTO messageDTOObj = new MessageScoketDTO();
						messageDTOObj.setStatus("newGroup");
						messageDTOObj.setGroup(group);
						messageDTOObj.setSenderUserId(currentUser.get().getId());
						
						Gson gsonObj = new Gson();
						gsonObj.toJson(messageDTOObj);
						try {
							senderSocket.sendMessage(new TextMessage(gsonObj
									.toJson(messageDTOObj)));
						} catch (IOException e) {

							e.printStackTrace();
						}
					}
				}
			}
			
			
			// notify the Group created person With new group deatils.
			WebSocketSession senderSocket = (WebSocketSession) WebscoketBean
					.getmLiveSession().get(currentUser.get().getId());
			if (senderSocket != null) {
				MessageScoketDTO messageDTOObj = new MessageScoketDTO();
				messageDTOObj.setStatus("newGroup");
				messageDTOObj.setGroup(group);
				messageDTOObj.setSenderUserId(currentUser.get().getId());
				
				Gson gsonObj = new Gson();
				gsonObj.toJson(messageDTOObj);
				try {
					senderSocket.sendMessage(new TextMessage(gsonObj
							.toJson(messageDTOObj)));
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			
		}

		return new ResponseEntity<>(status, HttpStatus.OK);
	}

	// thumbnail image creation
	private void saveScaledImage(String filePath, String outputFile) {
		try {

			BufferedImage sourceImage = ImageIO.read(new File(filePath));
			int width = sourceImage.getWidth();
			int height = sourceImage.getHeight();

			if (width > height) {
				float extraSize = height - 100;
				float percentHight = (extraSize / height) * 100;
				float percentWidth = width - ((width / 100) * percentHight);
				BufferedImage img = new BufferedImage((int) percentWidth, 100,
						BufferedImage.TYPE_INT_RGB);
				Image scaledImage = sourceImage.getScaledInstance(
						(int) percentWidth, 100, Image.SCALE_SMOOTH);
				img.createGraphics().drawImage(scaledImage, 0, 0, null);
				BufferedImage img2 = new BufferedImage(100, 100,
						BufferedImage.TYPE_INT_RGB);
				img2 = img.getSubimage((int) ((percentWidth - 100) / 2), 0,
						100, 100);

				ImageIO.write(img2, "jpg", new File(outputFile));
			} else {
				float extraSize = width - 100;
				float percentWidth = (extraSize / width) * 100;
				float percentHight = height - ((height / 100) * percentWidth);
				BufferedImage img = new BufferedImage(100, (int) percentHight,
						BufferedImage.TYPE_INT_RGB);
				Image scaledImage = sourceImage.getScaledInstance(100,
						(int) percentHight, Image.SCALE_SMOOTH);
				img.createGraphics().drawImage(scaledImage, 0, 0, null);
				BufferedImage img2 = new BufferedImage(100, 100,
						BufferedImage.TYPE_INT_RGB);
				img2 = img.getSubimage(0, (int) ((percentHight - 100) / 2),
						100, 100);

				ImageIO.write(img2, "jpg", new File(outputFile));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * 
	 * private final Logger log=LoggerFactory.getLogger(this.getClass());
	 * 
	 * @Value("${server.upload.path}") private String fileSaveDirectory;
	 * 
	 * @Value("${local.upload.path}") private String localfileSaveDirectory;
	 * 
	 * @Autowired UserRepository userRepository;
	 * 
	 * @Autowired IndividualChatMessageRepository chatMessageRepository;
	 * 
	 * @Autowired GroupRepository groupRepository;
	 * 
	 * //@Autowired //private SimpMessagingTemplate simpMessagingTemplate;
	 * 
	 * @Autowired GroupMessageRepository groupMessageRepository;
	 * 
	 * @Autowired private AttachemntRepository attachemntRepository;
	 * 
	 * @Autowired private ChatService chatService;
	 * 
	 * @Autowired private MemberInvitationService memberInvitationService;
	 * 
	 * @Autowired private FriendRequestService friendRequestService;
	 * 
	 * @Autowired private GroupService groupService;
	 * 
	 * @Autowired private GroupMembersService groupMembersService;
	 * 
	 * @Autowired private ContactListService contactListService;
	 * 
	 * @Autowired private PasswordEncoder passwordEncoder;
	 * 
	 * 
	 * @GetMapping(value = "/message") public String getMessage(Model model) {
	 * return "message"; }
	 * 
	 * 
	 * @GetMapping(value = "/voice") public String getVoice(Model model) {
	 * return "voice"; }
	 * 
	 * 
	 * 
	 * @GetMapping(value = "/chatHome") public String getChatScreen(Model model)
	 * { List<UserManagement> allusers = userRepository.findAll(); List<Group>
	 * allGroup = groupRepository.findAll(); String userName =
	 * SecurityUtils.getCurrentUserLogin();
	 * 
	 * Optional<UserManagement> opuser =
	 * userRepository.findByUserNameIgnoreCase(userName);
	 * 
	 * if (opuser.isPresent()) { allusers.remove(opuser.get());
	 * model.addAttribute("userId", opuser.get().getId());
	 * model.addAttribute("receverId", opuser.get().getUkMessageKey());
	 * model.addAttribute("userFullName", opuser.get().getFirstName());
	 * model.addAttribute("userProfilePic",
	 * opuser.get().getAttachment().getId());
	 * 
	 * if(opuser.isPresent()){ UserManagement user=opuser.get(); Date date = new
	 * Date(); user.setLoginAt(date); user.setIsOnline(true);
	 * userRepository.save(user); } }
	 * 
	 * model.addAttribute("group", allGroup); model.addAttribute("users",
	 * allusers); return "chatHome"; }
	 * 
	 * @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
	 * public String testMethod() throws URISyntaxException {
	 * List<UserManagement>userListbyName=userRepository.findUserByName("田");
	 * return fileSaveDirectory;
	 * 
	 * }
	 * 
	 * @PostMapping(value = "/chatHome", produces =
	 * MediaType.APPLICATION_JSON_VALUE) public ResponseEntity<Message>
	 * createMessage(@RequestBody Message messageDTO,
	 * MultipartHttpServletRequest request) throws URISyntaxException {
	 * 
	 * Optional<UserManagement> senderUser = userRepository
	 * .findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());
	 * 
	 * // save message Optional<UserManagement> opuser =
	 * userRepository.findByUkMessageKey(messageDTO.getReceiveUserId()); //
	 * messageDTO.setSenderUserId(senderUser.get().getId());
	 * IndividualChatMessage message = new IndividualChatMessage(); if
	 * (opuser.isPresent()) { // pushing using websocket
	 * messageDTO.setSenderUserId(senderUser.get().getUkMessageKey());
	 * simpMessagingTemplate.convertAndSend("/message/send/individual/" +
	 * opuser.get().getUkMessageKey(), messageDTO);
	 * message.setMessageContent(messageDTO.getMessage());
	 * message.setReceiverId(opuser.get().getId());
	 * message.setSenderId(senderUser.get().getId()); message.setRead(true);
	 * Date date = new Date(); //message.setCreatedDate(date.toLocaleString());
	 * chatMessageRepository.save(message);
	 * 
	 * } return new ResponseEntity<>(messageDTO, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * //group message saving
	 * 
	 * @PostMapping(value = "/chatHome/sample", produces =
	 * MediaType.APPLICATION_JSON_VALUE) public
	 * ResponseEntity<List<GroupMessageDTO>> cSample(MultipartHttpServletRequest
	 * request) throws URISyntaxException {
	 * 
	 * // System.out.println("Methodd ++++++++++++++++++++ "+ //
	 * messageDTO.getReceiveUserId()); String threadPid =
	 * request.getParameterValues("threadPid")[0];
	 * 
	 * Gson gson = new Gson(); Message messageDTO = gson.fromJson(threadPid,
	 * Message.class); MultipartFile file = request.getFile("file");
	 * 
	 * GroupMessage grpMessage = new GroupMessage(); List<GroupMessage>
	 * messageList= new ArrayList<GroupMessage>(); List<GroupMessageDTO>
	 * result=new ArrayList<GroupMessageDTO>();
	 * 
	 * Optional<UserManagement> senderUser = userRepository
	 * .findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());
	 * 
	 * if (file != null ) {
	 * 
	 * try { Random rnd = new Random(); int randmNumber = 100000 +
	 * rnd.nextInt(900000); String path =
	 * request.getServletContext().getRealPath("/");
	 * 
	 * if(Debug.STATUS){ String fileLocation = getFileSaveDirectory("image") +
	 * "/" + randmNumber + file.getOriginalFilename();
	 * 
	 * }else{ // fileLocation = getFileSaveDirectory("attachmets") + "/" +
	 * randmNumber+ file.getOriginalFilename(); } String fileLocation =
	 * getFileSaveDirectory("image") + "/" + randmNumber +
	 * file.getOriginalFilename();
	 * 
	 * System.out.println("File location ++:"+fileLocation);
	 * writeToDisk(file.getBytes(), fileLocation, "image"); Attachment
	 * attachmentObj = new Attachment(); // String cwd =
	 * System.getProperty("user.dir"); //
	 * System.out.println("Current working directory : " + cwd);
	 * 
	 * attachmentObj.setLocation(fileLocation);
	 * attachmentObj.setActivated(true);
	 * attachmentObj.setAttachmentName(file.getOriginalFilename());
	 * attachmentObj.setExtension(file.getContentType()); //
	 * System.out.println("Ext==="+file.getContentType());
	 * attachmentObj.setUploadedDate(LocalDateTime.now()); attachmentObj =
	 * attachemntRepository.save(attachmentObj);
	 * grpMessage.setAttachemnt(attachmentObj);
	 * grpMessage.setIsAttachment(true);
	 * 
	 * } catch (IOException ex) { return new
	 * ResponseEntity<>(HttpStatus.NO_CONTENT); }
	 * 
	 * }
	 * 
	 * messageDTO.setSenderName(senderUser.get().getFirstName());
	 * grpMessage.setIsDelete(false);
	 * grpMessage.setGroupId(messageDTO.getGroupId());
	 * grpMessage.setMessageContent(messageDTO.getMessage());
	 * grpMessage.setIsRead(true);
	 * grpMessage.setSenderId(messageDTO.getSenderId());
	 * grpMessage.setSenderName(messageDTO.getSenderName()); LocalDateTime now =
	 * LocalDateTime.now(); grpMessage.setCreatedDate(now);
	 * 
	 * groupMessageRepository.save(grpMessage); messageList.add(grpMessage);
	 * result
	 * =messageList.stream().map(GroupMessageDTO::new).collect(Collectors.toList
	 * ());
	 * 
	 * //simpMessagingTemplate.convertAndSend("/message/send/group/" +
	 * messageDTO.getReceiveUserId(), messageDTO);
	 * //simpMessagingTemplate.convertAndSend("/message/send/group/" +
	 * messageDTO.getReceiveUserId(), result);
	 * 
	 * return new ResponseEntity<>(result, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * //change the code
	 * 
	 * @SuppressWarnings("null")
	 * 
	 * @PostMapping(value = "/chatHome/upload-attachment", produces =
	 * MediaType.APPLICATION_JSON_VALUE) public
	 * ResponseEntity<List<IndividualChatDTO>>
	 * uploadEditFile(MultipartHttpServletRequest request) {
	 * 
	 * String threadPid = request.getParameterValues("threadPid")[0]; Gson gson
	 * = new Gson(); Message message = gson.fromJson(threadPid, Message.class);
	 * 
	 * Optional<UserManagement> senderUser = userRepository
	 * .findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());
	 * 
	 * Optional<UserManagement> opuser =
	 * userRepository.findByUkMessageKey(message.getReceiveUserId());
	 * IndividualChatMessage messageObj = new IndividualChatMessage();
	 * List<IndividualChatMessage> messageList= new
	 * ArrayList<IndividualChatMessage>(); List<IndividualChatDTO> result=new
	 * ArrayList<IndividualChatDTO>();
	 * 
	 * MultipartFile file = request.getFile("file");
	 * 
	 * if (file == null ) {//&& file.isEmpty() //return new
	 * ResponseEntity<>(HttpStatus.NO_CONTENT); } else
	 * 
	 * if (file != null ) {
	 * 
	 * try {
	 * 
	 * Random rnd = new Random(); int randmNumber = 100000 +
	 * rnd.nextInt(900000);
	 * 
	 * String path = request.getServletContext().getRealPath("/"); // String
	 * ppppp=getFileSaveDirectory(fileSaveDirectory); //
	 * System.out.println(path);
	 * 
	 * 
	 * if(Debug.STATUS){
	 * 
	 * String fileLocation = getFileSaveDirectory("attachmets") + "/" +
	 * randmNumber + file.getOriginalFilename();
	 * 
	 * }else{ // fileLocation = getFileSaveDirectory("attachmets") + "/" +
	 * randmNumber+ file.getOriginalFilename(); } String fileLocation =
	 * getFileSaveDirectory("image") + "/" + randmNumber +
	 * file.getOriginalFilename();
	 * 
	 * System.out.println("File location ++:"+fileLocation);
	 * 
	 * writeToDisk(file.getBytes(), fileLocation, "image"); Attachment
	 * attachmentObj = new Attachment();
	 * 
	 * String cwd = System.getProperty("user.dir");
	 * System.out.println("Current working directory : " + cwd);
	 * 
	 * attachmentObj.setLocation(fileLocation);
	 * attachmentObj.setActivated(true);
	 * attachmentObj.setAttachmentName(file.getOriginalFilename());
	 * attachmentObj.setExtension(file.getContentType());
	 * System.out.println("File exte ++:"+file.getContentType());
	 * attachmentObj.setUploadedDate(LocalDateTime.now());
	 * 
	 * attachmentObj = attachemntRepository.save(attachmentObj);
	 * messageObj.setAttachemnt(attachmentObj);
	 * messageObj.setIsAttachment(true);
	 * 
	 * } catch (IOException ex) { return new
	 * ResponseEntity<>(HttpStatus.NO_CONTENT); }
	 * 
	 * }
	 * 
	 * if (opuser.isPresent()) {
	 * 
	 * messageObj.setSenderId(senderUser.get().getId());
	 * 
	 * messageObj.setMessageContent(message.getMessage());
	 * messageObj.setReceiverId(opuser.get().getId());
	 * messageObj.setSenderId(senderUser.get().getId());
	 * messageObj.setRead(true);
	 * 
	 * LocalDateTime now = LocalDateTime.now(); messageObj.setCreatedDate(now);
	 * 
	 * messageObj = chatMessageRepository.save(messageObj);
	 * messageObj.setSenderUserUkId(senderUser.get().getUkMessageKey());
	 * messageList.add(messageObj);
	 * result=messageList.stream().map(IndividualChatDTO
	 * ::new).collect(Collectors.toList());
	 * 
	 * simpMessagingTemplate.convertAndSend("/message/send/individual/" +
	 * opuser.get().getUkMessageKey(), messageObj);
	 * 
	 * simpMessagingTemplate.convertAndSend("/message/send/individual/" +
	 * opuser.get().getUkMessageKey(), result);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * return new ResponseEntity<>(result, HttpStatus.OK); }
	 * 
	 * private void writeToDisk(byte[] fileBytes, String fileLocation, String
	 * folderName) { try { File fileDir = new
	 * File(getFileSaveDirectory(folderName)); if (!fileDir.exists()) {
	 * //fileDir.mkdirs();
	 * 
	 * boolean retval = fileDir.mkdirs();
	 * 
	 * // evaluate the result if (retval) { System.out.println("Directory : " +
	 * fileDir + " created successfully"); } else {
	 * System.out.println("Directory : " + fileDir + " creation failed"); } }
	 * FileCopyUtils.copy(fileBytes, new File(fileLocation)); } catch
	 * (IOException ioe) { ioe.printStackTrace(); } }
	 * 
	 * private String getFileSaveDirectory(String folderName) { // return
	 * FILE_SAVE_DIRECTORY + // SecurityUtils.getCurrentUsersCompanyId(); return
	 * fileSaveDirectory + folderName; }
	 * 
	 * @GetMapping(value = "/getMessageAll/{receiverId}") public
	 * ResponseEntity<Map<LocalDate, List<IndividualChatDTO>>>
	 * getMessage(@PathVariable String receiverId) throws URISyntaxException {
	 * 
	 * Optional<UserManagement> senderUser = userRepository
	 * .findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());
	 * 
	 * Optional<UserManagement> opuser = userRepository.findOneByid(receiverId);
	 * if (opuser.isPresent()) {
	 * 
	 * List<IndividualChatMessage> messageList =
	 * chatMessageRepository.findAllByReceiverIdAndSenderId(receiverId,
	 * senderUser.get().getId());
	 * 
	 * List<IndividualChatDTO>
	 * result=messageList.stream().map(IndividualChatDTO::
	 * new).collect(Collectors.toList()); Map<LocalDate,
	 * List<IndividualChatDTO>> mapValues =
	 * result.parallelStream().collect(Collectors.groupingBy(pl ->
	 * pl.getDate()));
	 * 
	 * 
	 * // sort by keys, a,b,c..., and return a new LinkedHashMap // toMap() will
	 * returns HashMap by default, we need LinkedHashMap to keep the order.
	 * Map<LocalDate, List<IndividualChatDTO>> mapObj =
	 * mapValues.entrySet().stream() .sorted(Map.Entry.comparingByKey())
	 * .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
	 * (oldValue, newValue) -> oldValue, LinkedHashMap::new));
	 * 
	 * 
	 * // Not Recommend, but it works. //Alternative way to sort a Map by keys,
	 * and put it into the "result" map Map<LocalDate, List<IndividualChatDTO>>
	 * result2 = new LinkedHashMap<>(); mapObj.entrySet().stream()
	 * .sorted(Map.Entry.comparingByKey()) .forEachOrdered(x ->
	 * result2.put(x.getKey(), x.getValue()));
	 * 
	 * // List<Integer> resultzz = new ArrayList(mapValues.keySet()); //
	 * Map<LocalDate, List<IndividualChatDTO>> inv = invert(mapValues); //
	 * messageList
	 * .addAll(chatMessageRepository.findAllByReceiverIdAndSenderId(receiverId,
	 * // senderUser.get().getId())); //
	 * messageList.stream().filter(mess->mess.getCreatedDate())
	 * //List<IndividualChatMessage> reverseView = Lists.reverse(messageList);
	 * 
	 * return new ResponseEntity<>(result2, HttpStatus.OK); }
	 * 
	 * return null;
	 * 
	 * }
	 * 
	 * @GetMapping(value = "/getGroupMessageAll/{groupId}") public
	 * ResponseEntity<Map<LocalDate, List<GroupMessageDTO>>>
	 * getAllGroupMessageByGrpId(@PathVariable String groupId) throws
	 * URISyntaxException {
	 * 
	 * Optional<Group> group = groupRepository.findById(groupId);
	 * 
	 * if (group.isPresent()) {
	 * 
	 * List<GroupMessage> groupMessageList =
	 * groupMessageRepository.findAllBygroupId(groupId);
	 * 
	 * List<GroupMessageDTO>
	 * result=groupMessageList.stream().map(GroupMessageDTO
	 * ::new).collect(Collectors.toList());
	 * 
	 * Map<LocalDate, List<GroupMessageDTO>> mapValues =
	 * result.parallelStream().collect(Collectors.groupingBy(pl ->
	 * pl.getDate()));
	 * 
	 * 
	 * Map<LocalDate, List<GroupMessageDTO>> mapObj =
	 * mapValues.entrySet().stream() .sorted(Map.Entry.comparingByKey())
	 * .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
	 * (oldValue, newValue) -> oldValue, LinkedHashMap::new));
	 * 
	 * Map<LocalDate, List<GroupMessageDTO>> result2 = new LinkedHashMap<>();
	 * mapObj.entrySet().stream() .sorted(Map.Entry.comparingByKey())
	 * .forEachOrdered(x -> result2.put(x.getKey(), x.getValue()));
	 * 
	 * return new ResponseEntity<>(result2, HttpStatus.OK); }
	 * 
	 * return null;
	 * 
	 * }
	 * 
	 * @GetMapping(value = "/attachments/{pid}") public ResponseEntity<byte[]>
	 * getAttachment(@PathVariable String pid) { //
	 * log.debug("rest web request to get a Coatingtype by pid {}", pid);
	 * Optional<Attachment> attch = attachemntRepository.findById(pid); if
	 * (attch.isPresent()) { File physicalFile =
	 * getPhysicalFileByFile(attch.get().getLocation()); if
	 * (physicalFile.exists()) { try { return new
	 * ResponseEntity<>(Files.toByteArray(physicalFile), HttpStatus.OK); } catch
	 * (IOException e) { e.printStackTrace(); } } }
	 * 
	 * byte[] newByte= new byte[0];
	 * 
	 * return new ResponseEntity<>(newByte, HttpStatus.NO_CONTENT); }
	 * 
	 * @GetMapping(value = "/profile/{url}") public ResponseEntity<byte[]>
	 * getAttachmentProfile(@PathVariable String url) { //
	 * log.debug("rest web request to get a Coatingtype by pid {}", pid);
	 * 
	 * File physicalFile = getPhysicalFileByFile(url); if
	 * (physicalFile.exists()) { try { return new
	 * ResponseEntity<>(Files.toByteArray(physicalFile), HttpStatus.OK); } catch
	 * (IOException e) { e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * //byte[] newByte= new byte[0];
	 * 
	 * return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	 * 
	 * }
	 * 
	 * 
	 * public File getPhysicalFileByFile(String path) { File physicalFile = new
	 * File(path); return physicalFile; }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @GetMapping(value = "/attachments/getpdf/{attachmentPid}") public
	 * ResponseEntity<byte[]> getPDF(@PathVariable String attachmentPid) throws
	 * IOException { //log.debug("Request to upload a file : {}",
	 * attachmentPid); //Optional<Attachment> opAttachment =
	 * attachmentService.findOneByPidAndActivated(attachmentPid, true);
	 * Optional<Attachment> opAttachment =
	 * attachemntRepository.findById(attachmentPid);
	 * if(opAttachment.isPresent()){
	 * 
	 * 
	 * File physicalFile =
	 * getPhysicalFileByFile(opAttachment.get().getLocation());
	 * System.out.println(physicalFile); if (physicalFile.exists()) {
	 * HttpHeaders headers = new HttpHeaders();
	 * headers.setContentType(MediaType.parseMediaType("application/pdf")); //
	 * String filename = "output.pdf"; //
	 * headers.setContentDispositionFormData(filename, filename);
	 * headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	 * ResponseEntity<byte[]> response = new
	 * ResponseEntity<byte[]>(Files.toByteArray(physicalFile), headers,
	 * HttpStatus.OK); return response; } } return null; }
	 * 
	 * 
	 * @GetMapping("/attachments/download/{attachmentPid}") public
	 * StreamingResponseBody getSteamingFile(HttpServletResponse response,
	 * @PathVariable String attachmentPid) throws IOException {
	 * log.debug("Request to download a file : {}", attachmentPid);
	 * Optional<Attachment> opAttachment =
	 * attachemntRepository.findById(attachmentPid);
	 * if(opAttachment.isPresent()){
	 * 
	 * 
	 * File physicalFile =
	 * getPhysicalFileByFile(opAttachment.get().getLocation()); if
	 * (physicalFile.exists()) {
	 * response.setContentType(opAttachment.get().getMimeType());
	 * response.addHeader("Content-Disposition", "attachment; filename=" +
	 * opAttachment.get().getAttachmentName());
	 * 
	 * response.setContentLength((int) physicalFile.length()); InputStream
	 * inputStream = new FileInputStream(physicalFile); return outputStream -> {
	 * int nRead; byte[] data = new byte[1024]; while ((nRead =
	 * inputStream.read(data, 0, data.length)) != -1) { outputStream.write(data,
	 * 0, nRead); } inputStream.close(); }; } } return null; }
	 * 
	 * 
	 * 
	 * @PostMapping(value = "/chatHome/saveMp3", produces =
	 * MediaType.APPLICATION_JSON_VALUE) public
	 * ResponseEntity<List<IndividualChatDTO>> saveMp3(@RequestBody Message
	 * messageDTO) throws URISyntaxException, IOException {
	 * 
	 * //System.out.println(messageDTO.getMessage()); Optional<UserManagement>
	 * senderUser = userRepository
	 * .findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());
	 * 
	 * Optional<UserManagement> opuser =
	 * userRepository.findByUkMessageKey(messageDTO.getReceiveUserId());
	 * 
	 * IndividualChatMessage messageObj = new IndividualChatMessage();
	 * List<IndividualChatMessage> messageList= new
	 * ArrayList<IndividualChatMessage>(); List<IndividualChatDTO> result=new
	 * ArrayList<IndividualChatDTO>();
	 * 
	 * if(opuser.isPresent()){
	 * 
	 * String extension=".mp3"; byte[] decodedBytes ; String filedata =
	 * messageDTO.getMessage(); String split[]=
	 * filedata.split("data:audio/mp3;base64,"); String data=split[1];
	 * decodedBytes =Base64.getDecoder().decode(data); Random rnd = new
	 * Random(); int randmNumber = 100000 + rnd.nextInt(900000); String fileName
	 * = "voiceRecord"+randmNumber;
	 * 
	 * String fileLocation = getFileSaveDirectory("voice") +
	 * "/"+fileName+extension; writeToDisk(decodedBytes, fileLocation, "voice");
	 * Attachment attachmentObj = new Attachment();
	 * 
	 * attachmentObj.setLocation(fileLocation);
	 * attachmentObj.setActivated(true);
	 * attachmentObj.setAttachmentName("Voice File");
	 * attachmentObj.setExtension(extension);
	 * attachmentObj.setUploadedDate(LocalDateTime.now());
	 * 
	 * attachmentObj = attachemntRepository.save(attachmentObj);
	 * 
	 * messageObj.setAttachemnt(attachmentObj);
	 * messageObj.setIsAttachment(true);
	 * messageObj.setSenderId(senderUser.get().getId());
	 * messageObj.setMessageContent("");
	 * messageObj.setReceiverId(opuser.get().getId());
	 * messageObj.setSenderId(senderUser.get().getId());
	 * messageObj.setRead(true); messageObj.setCreatedDate(LocalDateTime.now());
	 * messageObj = chatMessageRepository.save(messageObj);
	 * 
	 * messageObj.setSenderUserUkId(senderUser.get().getUkMessageKey());
	 * messageList.add(messageObj);
	 * result=messageList.stream().map(IndividualChatDTO
	 * ::new).collect(Collectors.toList());
	 * 
	 * simpMessagingTemplate.convertAndSend("/message/send/individual/" +
	 * opuser.get().getUkMessageKey(), result);
	 * 
	 * 
	 * }
	 * 
	 * return new ResponseEntity<>(result, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * @PostMapping(value = "/chatHome/saveToGroupMp3", produces =
	 * MediaType.APPLICATION_JSON_VALUE) public
	 * ResponseEntity<List<GroupMessageDTO>> saveToGroupMp3(
	 * 
	 * @RequestBody Message messageDTO) throws URISyntaxException, IOException {
	 * 
	 * Optional<UserManagement> senderUser = userRepository
	 * .findByUserNameIgnoreCase(SecurityUtils.getCurrentUserLogin());
	 * 
	 * List<GroupMessage> messageList = new ArrayList<GroupMessage>();
	 * List<GroupMessageDTO> result = new ArrayList<GroupMessageDTO>();
	 * 
	 * GroupMessage grpMessage = new GroupMessage();
	 * 
	 * String extension = ".mp3"; byte[] decodedBytes; String filedata =
	 * messageDTO.getMessage();// base_64 value String split[] =
	 * filedata.split("data:audio/mp3;base64,"); String data = split[1];
	 * decodedBytes = Base64.getDecoder().decode(data); Random rnd = new
	 * Random(); int randmNumber = 100000 + rnd.nextInt(900000); String fileName
	 * = "voiceRecord" + randmNumber;
	 * 
	 * String fileLocation = getFileSaveDirectory("voice") + "/" + fileName +
	 * extension; writeToDisk(decodedBytes, fileLocation, "voice"); Attachment
	 * attachmentObj = new Attachment();
	 * 
	 * attachmentObj.setLocation(fileLocation);
	 * attachmentObj.setActivated(true);
	 * attachmentObj.setAttachmentName("Voice File");
	 * attachmentObj.setExtension(extension);
	 * attachmentObj.setUploadedDate(LocalDateTime.now()); attachmentObj =
	 * attachemntRepository.save(attachmentObj);
	 * 
	 * grpMessage.setAttachemnt(attachmentObj);
	 * grpMessage.setIsAttachment(true);
	 * 
	 * grpMessage.setIsDelete(false);
	 * grpMessage.setGroupId(messageDTO.getGroupId());
	 * grpMessage.setMessageContent(messageDTO.getMessage());
	 * grpMessage.setIsRead(true);
	 * grpMessage.setSenderId(messageDTO.getSenderId());
	 * grpMessage.setSenderName(messageDTO.getSenderName()); LocalDateTime now =
	 * LocalDateTime.now(); grpMessage.setCreatedDate(now);
	 * 
	 * groupMessageRepository.save(grpMessage);
	 * 
	 * messageList.add(grpMessage); result =
	 * messageList.stream().map(GroupMessageDTO::new)
	 * .collect(Collectors.toList());
	 * 
	 * // simpMessagingTemplate.convertAndSend("/message/send/group/" + //
	 * messageDTO.getReceiveUserId(), messageDTO);
	 * simpMessagingTemplate.convertAndSend("/message/send/group/" +
	 * messageDTO.getReceiveUserId(), result);
	 * 
	 * return new ResponseEntity<>(result, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * //thumbnail image creation private void saveScaledImage(String
	 * filePath,String outputFile){ try {
	 * 
	 * BufferedImage sourceImage = ImageIO.read(new File(filePath)); int width =
	 * sourceImage.getWidth(); int height = sourceImage.getHeight();
	 * 
	 * if(width>height){ float extraSize= height-100; float percentHight =
	 * (extraSize/height)*100; float percentWidth = width -
	 * ((width/100)*percentHight); BufferedImage img = new
	 * BufferedImage((int)percentWidth, 100, BufferedImage.TYPE_INT_RGB); Image
	 * scaledImage = sourceImage.getScaledInstance((int)percentWidth, 100,
	 * Image.SCALE_SMOOTH); img.createGraphics().drawImage(scaledImage, 0, 0,
	 * null); BufferedImage img2 = new BufferedImage(100, 100
	 * ,BufferedImage.TYPE_INT_RGB); img2 =
	 * img.getSubimage((int)((percentWidth-100)/2), 0, 100, 100);
	 * 
	 * ImageIO.write(img2, "jpg", new File(outputFile)); }else{ float extraSize=
	 * width-100; float percentWidth = (extraSize/width)*100; float percentHight
	 * = height - ((height/100)*percentWidth); BufferedImage img = new
	 * BufferedImage(100, (int)percentHight, BufferedImage.TYPE_INT_RGB); Image
	 * scaledImage = sourceImage.getScaledInstance(100,(int)percentHight,
	 * Image.SCALE_SMOOTH); img.createGraphics().drawImage(scaledImage, 0, 0,
	 * null); BufferedImage img2 = new BufferedImage(100, 100
	 * ,BufferedImage.TYPE_INT_RGB); img2 = img.getSubimage(0,
	 * (int)((percentHight-100)/2), 100, 100);
	 * 
	 * ImageIO.write(img2, "jpg", new File(outputFile)); }
	 * 
	 * } catch (IOException e) { e.printStackTrace(); }
	 * 
	 * }
	 */
	
	
}
