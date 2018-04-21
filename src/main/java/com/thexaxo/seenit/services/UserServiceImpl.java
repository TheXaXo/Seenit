package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Role;
import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.exceptions.UserNotFoundException;
import com.thexaxo.seenit.models.ChangePasswordBindingModel;
import com.thexaxo.seenit.models.EditUserBindingModel;
import com.thexaxo.seenit.models.RegisterUserBindingModel;
import com.thexaxo.seenit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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
        user.setRegistrationDate(LocalDateTime.now());
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
        User user = this.userRepository.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username not found.");
        }

        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUserById(String id) {
        return this.userRepository.getOne(id);
    }

    @Override
    public void subscribe(Subseenit subseenit, User user) {
        if (!user.isSubscribedTo(subseenit.getName())) {
            user.getSubscribedSubseenits().add(subseenit);
        } else {
            user.getSubscribedSubseenits().remove(subseenit);
        }

        this.userRepository.saveAndFlush(user);
    }

    @Override
    public long getSubmittedPostsPages(User user, int size) {
        return (long) Math.ceil((double) user.getPosts().size() / size);
    }

    @Override
    public long getSubmittedCommentsPages(User user, int size) {
        return (long) Math.ceil((double) user.getComments().size() / size);
    }

    @Override
    public long getSavedPostsPages(User user, int size) {
        return (long) Math.ceil((double) user.getSavedPosts().size() / size);
    }

    @Override
    public long getUpvotedPostsPages(User user, int size) {
        return (long) Math.ceil((double) user.getUpvotedPosts().size() / size);
    }

    @Override
    public long getDownvotedPostsPages(User user, int size) {
        return (long) Math.ceil((double) user.getDownvotedPosts().size() / size);
    }

    @Override
    public long getUpvotedCommentsPages(User user, int size) {
        return (long) Math.ceil((double) user.getUpvotedComments().size() / size);
    }

    @Override
    public long getDownvotedCommentsPages(User user, int size) {
        return (long) Math.ceil((double) user.getDownvotedComments().size() / size);
    }

    @Override
    public boolean changePassword(ChangePasswordBindingModel bindingModel, User loggedUser) {
        if (!this.encoder.matches(bindingModel.getCurrentPassword(), loggedUser.getPassword())) {
            return false;
        }

        loggedUser.setPassword(this.encoder.encode(bindingModel.getNewPassword()));
        return true;
    }

    @Override
    public void edit(boolean editRoles, boolean updateAuthentication, String username, EditUserBindingModel bindingModel) {
        User user = this.getUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        user.setUsername(bindingModel.getUsername());
        user.setEmail(bindingModel.getEmail());

        if (bindingModel.getPassword().length() != 0) {
            user.setPassword(this.encoder.encode(bindingModel.getPassword()));
        }

        if (editRoles) {
            Set<Role> rolesToAdd = new HashSet<>();

            for (String roleName : bindingModel.getRoles()) {
                Role role = this.roleService.findRoleByAuthority(roleName);

                if (role == null) {
                    continue;
                }

                rolesToAdd.add(role);
            }

            user.setAuthorities(rolesToAdd);

            if (updateAuthentication) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), rolesToAdd);

                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }
        }

        this.userRepository.saveAndFlush(user);
    }
}