package com.thexaxo.seenit;

import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.EditUserBindingModel;
import com.thexaxo.seenit.models.RegisterUserBindingModel;
import com.thexaxo.seenit.repositories.UserRepository;
import com.thexaxo.seenit.services.RoleService;
import com.thexaxo.seenit.services.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTests {
    private static final String PASSWORD_HASH = "myCustomHash";
    private static final String USER_PASSWORD_NEW = "newPassword";
    private static final String USER_EMAIL_OLD = "asd@asd.asd";
    private static final String USER_EMAIL_NEW = "asd@asd.asd2";
    private static final String USER_USERNAME = "asd12";
    private static final String USER_PASSWORD = "asd12";

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setUp() {
        when(this.passwordEncoder.encode(anyString()))
                .thenReturn(PASSWORD_HASH);
    }

    @Test
    public void testRegister_withUsernameAndPassword_passwordShouldBeEncoded() {
        when(this.userRepository.saveAndFlush(any()))
                .thenAnswer(i -> i.getArgument(0));

        RegisterUserBindingModel bindingModel = new RegisterUserBindingModel();
        bindingModel.setUsername(USER_USERNAME);
        bindingModel.setPassword(USER_PASSWORD);

        User result = this.userService.register(bindingModel);

        assertEquals(
                "Password was not or wrongly encoded!",
                PASSWORD_HASH,
                result.getPassword()
        );
    }

    @Test
    public void testEdit_emailShouldBeChanged() {
        when(this.userRepository.saveAndFlush(any()))
                .thenAnswer(i -> i.getArgument(0));

        User userMock = new User();
        userMock.setEmail(USER_EMAIL_OLD);

        when(this.userService.getUserByUsername(anyString()))
                .thenReturn(userMock);

        EditUserBindingModel bindingModel = new EditUserBindingModel();
        bindingModel.setEmail(USER_EMAIL_NEW);
        bindingModel.setPassword("");

        User result = this.userService.edit(false, false, "", bindingModel);

        assertEquals(
                "Email was not changed!",
                USER_EMAIL_NEW,
                result.getEmail()
        );
    }

    @Test
    public void testEdit_passwordShouldBeChanged() {
        when(this.userRepository.saveAndFlush(any()))
                .thenAnswer(i -> i.getArgument(0));

        User userMock = new User();
        userMock.setPassword(USER_PASSWORD);

        when(this.userService.getUserByUsername(anyString()))
                .thenReturn(userMock);

        EditUserBindingModel bindingModel = new EditUserBindingModel();
        bindingModel.setPassword(USER_PASSWORD_NEW);

        User result = this.userService.edit(false, false, "", bindingModel);

        assertEquals(
                "Password was not changed!",
                PASSWORD_HASH,
                result.getPassword()
        );
    }
}