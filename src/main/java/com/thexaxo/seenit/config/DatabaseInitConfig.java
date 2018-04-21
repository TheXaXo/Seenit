package com.thexaxo.seenit.config;

import com.thexaxo.seenit.entities.Role;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.repositories.RoleRepository;
import com.thexaxo.seenit.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Configuration
public class DatabaseInitConfig {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private BCryptPasswordEncoder encoder;

    public DatabaseInitConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Bean
    public CommandLineRunner populateDatabaseData() {
        return args -> {
            List<Role> roles = roleRepository.findAll();

            if (roles.isEmpty()) {
                Role user = new Role();
                user.setAuthority("ROLE_USER");
                roleRepository.saveAndFlush(user);

                Role moderator = new Role();
                moderator.setAuthority("ROLE_MODERATOR");
                roleRepository.saveAndFlush(moderator);

                Role admin = new Role();
                admin.setAuthority("ROLE_ADMIN");
                roleRepository.saveAndFlush(admin);

                Role god = new Role();
                god.setAuthority("ROLE_GOD");
                roleRepository.saveAndFlush(god);
            }

            List<User> users = userRepository.findAll();

            if (users.isEmpty()) {
                User god = new User();
                Role godRole = this.roleRepository.findRoleByAuthority("ROLE_GOD");

                god.setUsername("god");
                god.setPassword(encoder.encode("ebasiDulgataITrudnaParolaBace"));
                god.setEmail("god@seenit.com");
                god.setRegistrationDate(LocalDateTime.now());
                god.setAccountNonExpired(true);
                god.setAccountNonLocked(true);
                god.setCredentialsNonExpired(true);
                god.setEnabled(true);
                god.setAuthorities(new HashSet<>(Collections.singletonList(godRole)));

                this.userRepository.saveAndFlush(god);
            }
        };
    }
}