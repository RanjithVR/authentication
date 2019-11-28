package jp.profield.chat.service;

import java.util.Optional;

import jp.profield.chat.model.Role;

public interface RoleService {

	Optional<Role> findOneByRoleName(String name);
}
