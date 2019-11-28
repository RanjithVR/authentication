package jp.profield.chat.service.impl;

import java.util.Optional;

import jp.profield.chat.model.Role;
import jp.profield.chat.models.repository.RoleRepository;
import jp.profield.chat.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class RoleServiceImpl implements RoleService{

	@Autowired
	RoleRepository roleRepository;
	
	@Override
	public Optional<Role> findOneByRoleName(String name) {
	
		return roleRepository.findOneByRoleName(name);
	}

}
