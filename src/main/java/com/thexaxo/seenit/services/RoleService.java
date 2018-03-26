package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();

    Role findRoleByAuthority(String authority);

    void create(Role role);
}