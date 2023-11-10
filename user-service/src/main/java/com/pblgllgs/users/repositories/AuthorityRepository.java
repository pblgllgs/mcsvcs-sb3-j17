package com.pblgllgs.users.repositories;

import com.pblgllgs.users.model.entities.AuthorityEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<AuthorityEntity,Long> {

    AuthorityEntity findByName(String name);
}
