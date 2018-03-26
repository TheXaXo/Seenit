package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Role;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.RegisterUserBindingModel;
import com.thexaxo.seenit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserRepository userRepository;
    private RoleService roleService;
    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, RoleService roleService, BCryptPasswordEncoder encoder) {
        this.userRepository = repository;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @Override
    public void register(RegisterUserBindingModel bindingModel) {
        User user = new User();

        user.setUsername(bindingModel.getUsername());
        user.setPassword(encoder.encode(bindingModel.getPassword()));
        user.setEmail(bindingModel.getEmail());
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        Role userRole = this.roleService.findRoleByAuthority("ROLE_USER");

        if (userRole == null) {
            userRole = new Role();
            userRole.setAuthority("ROLE_USER");
            userRole.setUsers(new HashSet<>());

            this.roleService.create(userRole);
        }

        user.setAuthorities(new HashSet<>(Collections.singletonList(userRole)));
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public boolean userWithUsernameExists(String username) throws UsernameNotFoundException {
        return this.userRepository.findUserByUsername(username) != null;
    }

    @Override
    public boolean userWithEmailExists(String email) {
        return this.userRepository.findUserByEmail(email) != null;
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findUserByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUserById(long id) {
        return this.userRepository.getOne(id);
    }
}