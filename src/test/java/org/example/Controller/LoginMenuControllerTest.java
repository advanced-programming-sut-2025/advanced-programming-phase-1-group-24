package org.example.Controller;

import org.example.Model.App;
import org.example.Model.Menus.Menu;
import org.example.Model.Result;
import org.example.Model.User;
import org.example.Model.UserDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginMenuControllerTest {
    private LoginMenuController controller;
    private App app;

    @BeforeEach
    void setUp() {
        controller = new LoginMenuController();
        app = App.getInstance();
        app.getUsers().clear();
        app.setSecurityQuestions(List.of("Your favorite color?", "Your first pet's name?"));
        app.setLoggedInUser(null);
    }

    private Matcher mockMatcher(String username, String password, String confirm, String nickname, String email, String gender) {
        Matcher matcher = mock(Matcher.class);
        when(matcher.group("username")).thenReturn(username);
        when(matcher.group("password")).thenReturn(password);
        when(matcher.group("confirm")).thenReturn(confirm);
        when(matcher.group("nickname")).thenReturn(nickname);
        when(matcher.group("email")).thenReturn(email);
        when(matcher.group("gender")).thenReturn(gender);
        return matcher;
    }

    @Test
    void register_shouldFail_whenUsernameTaken() {
        User existing = new User("user1", "hashed", "nick", "test@mail.com", false);
        app.getUsers().add(existing);

        Matcher matcher = mockMatcher("user1", "Password1!", "Password1!", "nick", "new@mail.com", "male");
        Result result = controller.register(matcher);

        assertFalse(result.isSuccessful());
        assertEquals("username is already taken! try adding numbers or -", result.message());
    }

    @Test
    void register_shouldFail_whenUsernameInvalid() {
        Matcher matcher = mockMatcher("inv@lid", "Password1!", "Password1!", "nick", "mail@mail.com", "female");
        Result result = controller.register(matcher);

        assertFalse(result.isSuccessful());
        assertEquals("username format is invalid!", result.message());
    }

    @Test
    void register_shouldFail_whenEmailInvalid() {
        Matcher matcher = mockMatcher("user2", "Password1!", "Password1!", "nick", "bad..email", "female");
        Result result = controller.register(matcher);

        assertFalse(result.isSuccessful());
        assertEquals("email format is invalid!", result.message());
    }

    @Test
    void register_shouldFail_whenPasswordsDoNotMatch() {
        Matcher matcher = mockMatcher("user3", "Password1!", "WrongPass", "nick", "test@mail.com", "male");
        Result result = controller.register(matcher);

        assertFalse(result.isSuccessful());
        assertEquals("password and confirmation do not match!", result.message());
    }

    @Test
    void register_shouldFail_whenPasswordIsWeak() {
        Matcher matcher = mockMatcher("user4", "weakpass", "weakpass", "nick", "test@mail.com", "female");
        Result result = controller.register(matcher);

        assertFalse(result.isSuccessful());
        assertTrue(result.message().startsWith("password is weak!"));
    }

    @Test
    void register_shouldSucceed_withRandomPassword() {
        Matcher matcher = mockMatcher("user5", "random", "", "nick", "test@mail.com", "male");

        try (MockedStatic<UserDatabase> mockedDB = mockStatic(UserDatabase.class)) {
            Result result = controller.register(matcher);

            assertTrue(result.isSuccessful());
            assertTrue(result.message().startsWith("choose a security question:"));
            assertNotNull(app.getLoggedInUser());
            //mockedDB.verify(() -> UserDatabase.saveUsers(any(ArrayList.class)));
        }
    }

    @Test
    void register_shouldSucceed_withValidInfo() {
        Matcher matcher = mockMatcher("user6", "Strong1!@", "Strong1!@", "nick", "test@mail.com", "female");

        try (MockedStatic<UserDatabase> mockedDB = mockStatic(UserDatabase.class)) {
            Result result = controller.register(matcher);

            assertTrue(result.isSuccessful());
            assertTrue(result.message().contains("choose a security question:"));
            assertNotNull(app.getLoggedInUser());
            //mockedDB.verify(() -> UserDatabase.saveUsers(any(ArrayList.class)));
        }
    }


    @Test
    void pickQuestion_shouldFail_whenNotLoggedIn() {
        app.setLoggedInUser(null);
        Matcher matcher = mock(Matcher.class);
        Result result = controller.pickQuestion(matcher);

        assertFalse(result.isSuccessful());
        assertEquals("no pending user registration found!", result.message());
    }

    @Test
    void pickQuestion_shouldFail_whenInvalidIndex() {
        app.setLoggedInUser(new User("u", "p", "n", "e", true));
        Matcher matcher = mock(Matcher.class);
        when(matcher.group("questionNumber")).thenReturn("10");
        Result result = controller.pickQuestion(matcher);

        assertFalse(result.isSuccessful());
        assertEquals("invalid question number!", result.message());
    }

    @Test
    void pickQuestion_shouldFail_whenAnswerMismatch() {
        app.setLoggedInUser(new User("u", "p", "n", "e", true));
        Matcher matcher = mock(Matcher.class);
        when(matcher.group("questionNumber")).thenReturn("1");
        when(matcher.group("answer")).thenReturn("abc");
        when(matcher.group("answerConfirm")).thenReturn("xyz");
        Result result = controller.pickQuestion(matcher);

        assertFalse(result.isSuccessful());
        assertEquals("answer and confirmation do not match!", result.message());
    }

    @Test
    void pickQuestion_shouldSucceed_withValidInput() {
        User tempUser = new User("u", "p", "n", "e", true);
        app.setLoggedInUser(tempUser);
        Matcher matcher = mock(Matcher.class);
        when(matcher.group("questionNumber")).thenReturn("1");
        when(matcher.group("answer")).thenReturn("blue");
        when(matcher.group("answerConfirm")).thenReturn("blue");

        try (MockedStatic<UserDatabase> mockedDB = mockStatic(UserDatabase.class)) {
            Result result = controller.pickQuestion(matcher);

            assertTrue(result.isSuccessful());
            assertEquals("user registered successfully. you are now in login menu!", result.message());
           // mockedDB.verify(() -> UserDatabase.saveUsers(any(ArrayList.class)));
        }

        assertNull(app.getLoggedInUser());
    }

    private Matcher mockQuestionMatcher(String number, String answer, String confirm) {
        Matcher matcher = mock(Matcher.class);
        when(matcher.group("questionNumber")).thenReturn(number);
        when(matcher.group("answer")).thenReturn(answer);
        when(matcher.group("answerConfirm")).thenReturn(confirm);
        return matcher;
    }

    @Test
    void testForgetPassword_userNotFound() {
        Matcher matcher = mock(Matcher.class);
        when(matcher.group("username")).thenReturn("nonexistent");
        Scanner scanner = new Scanner("");

        Result result = controller.forgetPassword(matcher, scanner);

        assertFalse(result.isSuccessful());
        assertEquals("no user with this username exists!", result.message());
    }

    @Test
    void testForgetPassword_invalidAnswerFormat() {
        User user = new User("john", "pass", "John", "john@example.com", true);
        user.setSecurityQuestion("color?");
        user.setSecurityAnswer("blue");
        app.getUsers().add(user);

        Matcher matcher = mock(Matcher.class);
        when(matcher.group("username")).thenReturn("john");

        String input = "wrong format";
        Scanner scanner = new Scanner(input + "\n");

        Result result = controller.forgetPassword(matcher, scanner);

        assertFalse(result.isSuccessful());
        assertEquals("invalid format! expected: answer -a <answer>", result.message());
    }

    @Test
    void testForgetPassword_wrongAnswer() {
        User user = new User("john", "pass", "John", "john@example.com", true);
        user.setSecurityQuestion("color?");
        user.setSecurityAnswer("blue");
        app.getUsers().add(user);

        Matcher matcher = mock(Matcher.class);
        when(matcher.group("username")).thenReturn("john");

        String input = "answer -a green";
        Scanner scanner = new Scanner(input + "\n");

        Result result = controller.forgetPassword(matcher, scanner);

        assertFalse(result.isSuccessful());
        assertEquals("incorrect answer! returning to main menu...", result.message());
    }

    @Test
    void testForgetPassword_customPasswordFlow() {
        User user = new User("john", "pass", "John", "john@example.com", true);
        user.setSecurityQuestion("color?");
        user.setSecurityAnswer("blue");
        app.getUsers().add(user);

        Matcher matcher = mock(Matcher.class);
        when(matcher.group("username")).thenReturn("john");

        String input = "answer -a blue\nyes\nweak\nStrong1!";
        Scanner scanner = new Scanner(input + "\n");

        Result result = controller.forgetPassword(matcher, scanner);

        assertTrue(result.isSuccessful());
        assertEquals("password changed successfully! you can now log in.", result.message());
        assertTrue(LoginMenuController.isStrongPassword("Strong1!"));
    }

    @Test
    void testForgetPassword_randomPasswordFlow() {
        User user = new User("john", "pass", "John", "john@example.com", true);
        user.setSecurityQuestion("color?");
        user.setSecurityAnswer("blue");
        app.getUsers().add(user);

        Matcher matcher = mock(Matcher.class);
        when(matcher.group("username")).thenReturn("john");

        String input = "answer -a blue\nno";
        Scanner scanner = new Scanner(input + "\n");

        Result result = controller.forgetPassword(matcher, scanner);

        assertTrue(result.isSuccessful());
        assertEquals("password changed successfully! you can now log in.", result.message());
    }


}