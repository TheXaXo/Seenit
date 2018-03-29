package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.RegisterUserBindingModel;

import java.util.List;

public interface UserService {
    void register(RegisterUserBindingModel bindingModel);

    boolean userWithUsernameExists(String username);

    boolean userWithEmailExists(String email);

    User getUserByUsername(String username);

    List<User> getAllUsers();

    User getUserById(String id);

    void subscribe(Subseenit subseenit, User user);
}
