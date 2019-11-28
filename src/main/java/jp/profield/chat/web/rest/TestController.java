package jp.profield.chat.web.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import jp.profield.chat.model.Group;
import jp.profield.chat.model.Role;
import jp.profield.chat.model.UserManagement;
import jp.profield.chat.models.repository.GroupRepository;
import jp.profield.chat.models.repository.RoleRepository;
import jp.profield.chat.models.repository.UserRepository;
import jp.profield.chat.web.rest.dto.UserDTO;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@Inject
	private UserRepository userRepository;

	@Inject
	private RoleRepository roleRepository;

	@Inject
	private PasswordEncoder passwordEncoder;
	
	@Inject
	private GroupRepository groupRepository;

	@RequestMapping("/sample/getallTes")
	public List<UserDTO> getall() {
		return userRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
	}

	@RequestMapping("/usersave")
	public String save() {
		UserManagement user = new UserManagement();
		//user.setCompany("company");

		user.setEmail("jois@gmail.com");
		user.setActivated(true);
		//user.setFullName("Jois Joy");
		//user.setMobile("880022558866");
		// encoading password
		user.setPassword(passwordEncoder.encode("1234"));
		user.setUkMessageKey(1232222588789L);
		//user.setPid("abbcdqqedrrcc");
		user.setRole(roleRepository.findOneByRoleName("USER").get());
		user.setUserName("jois@gmail.com");

		userRepository.save(user); 
		return "Saved To Chat application..........";

	}

	@RequestMapping("/role")
	public String saveRole() {
		
		Optional<Role> roleExist=roleRepository.findOneByRoleName("USER");
		if(roleExist.isPresent()){
			return "USER Role Alerdy Exist...!!!!";
		}else{
			Role role = new Role();
			role.setRoleName("USER");
			role.setDescription("description");
			roleRepository.save(role);
			return "Saved To Chat application..........";
		}
		
		

	}
	
	@RequestMapping("/saveGroup")
	public String saveGroup() {
	
		Group groupName= groupRepository.findByGroupName("Profield Group");
		Group group= new Group();
		if(groupName == null){
			group.setGroupName("Profield Group");
			group.setGroupUniqeId(12345678998877L);
			groupRepository.save(group);
			return "Created "+group.getGroupName()+"To Chat application...!!!!";
		}
	
		return null;

	}
	
	
	@RequestMapping("/saveGroupMembers")
	public String saveGroupMembers() {
	
		Group groupName= groupRepository.findByGroupName("Indocosmo Group");
		
		
		return null;

	}


}
