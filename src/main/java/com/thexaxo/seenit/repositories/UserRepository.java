package com.thexaxo.seenit.repositories;

import com.thexaxo.seenit.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findUserByUsername(String username);

    User findUserByEmail(String username);
}