package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Role;
import com.thexaxo.seenit.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepository repository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Role> getAllRoles() {
        return this.repository.findAll();
    }

    @Override
    public Role findRoleByAuthority(String authority) {
        return this.repository.findRoleByAuthority(authority);
    }

    @Override
    public void create(Role role) {
        this.repository.saveAndFlush(role);
    }
}