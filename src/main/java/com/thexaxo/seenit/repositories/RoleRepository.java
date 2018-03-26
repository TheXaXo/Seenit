package com.thexaxo.seenit.repositories;

import com.thexaxo.seenit.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByAuthority(String authority);
}