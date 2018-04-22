package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.ChangePasswordBindingModel;
import com.thexaxo.seenit.models.EditUserBindingModel;
import com.thexaxo.seenit.models.RegisterUserBindingModel;

import java.util.List;

public interface UserService {
    User register(RegisterUserBindingModel bindingModel);

    boolean userWithUsernameExists(String username);

    boolean userWithEmailExists(String email);

    User getUserByUsername(String username);

    List<User> getAllUsers();

    User getUserById(String id);

    void subscribe(Subseenit subseenit, User user);

    long getSubmittedPostsPages(User user, int size);

    long getSubmittedCommentsPages(User user, int size);

    long getSavedPostsPages(User user, int size);

    long getUpvotedPostsPages(User user, int size);

    long getDownvotedPostsPages(User user, int size);

    long getUpvotedCommentsPages(User user, int size);

    long getDownvotedCommentsPages(User user, int size);

    boolean changePassword(ChangePasswordBindingModel bindingModel, User loggedUser);

    User edit(boolean editRoles, boolean updateAuthentication, String username, EditUserBindingModel bindingModel);
}