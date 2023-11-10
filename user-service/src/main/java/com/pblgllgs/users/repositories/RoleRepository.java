package com.pblgllgs.users.repositories;

import com.pblgllgs.users.model.entities.RoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);
}
