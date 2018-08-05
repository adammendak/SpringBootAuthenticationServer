package com.adammendak.authentication.repository;

import com.adammendak.authentication.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
