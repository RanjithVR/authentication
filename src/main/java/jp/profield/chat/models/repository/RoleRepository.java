package jp.profield.chat.models.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import jp.profield.chat.model.Role;

public interface RoleRepository extends MongoRepository<Role, String>{

	Optional<Role> findOneByRoleName(String name);
}
