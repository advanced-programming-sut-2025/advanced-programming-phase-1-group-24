package org.example.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.example.Controller.ProfileMenuController;
import org.example.Controller.LoginMenuController;
import org.example.Model.App;
import org.example.Model.Result;
import org.example.Model.User;
import org.example.Model.UserDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileMenuControllerTest {

    ProfileMenuController controller;
    App mockApp;
    User mockUser;

    @BeforeEach
    void setUp() {
        controller = new ProfileMenuController();
        mockApp = mock(App.class);
        mockUser = mock(User.class);

        // Mock App.getInstance()
        try (MockedStatic<App> mockedApp = mockStatic(App.class)) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
        }
    }

    @Test
    void changeUsername_nullUser() {
        when(mockApp.getLoggedInUser()).thenReturn(null);
        try (MockedStatic<App> mockedApp = mockStatic(App.class)) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            Result result = controller.changeUsername("newUser");
            assertFalse(result.isSuccessful());
            assertEquals("please login first!", result.message());
        }
    }

    @Test
    void changeUsername_sameAsCurrent() {
        when(mockApp.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getUsername()).thenReturn("current");

        try (MockedStatic<App> mockedApp = mockStatic(App.class)) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            Result result = controller.changeUsername("current");
            assertFalse(result.isSuccessful());
            assertEquals("new username is the same as current one!", result.message());
        }
    }

    @Test
    void changeUsername_invalidFormat() {
        when(mockApp.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getUsername()).thenReturn("current");

        try (MockedStatic<App> mockedApp = mockStatic(App.class);
             MockedStatic<LoginMenuController> mockedLogin = mockStatic(LoginMenuController.class)) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            mockedLogin.when(() -> LoginMenuController.isValidUsername("bad!name")).thenReturn(false);

            Result result = controller.changeUsername("bad!name");
            assertFalse(result.isSuccessful());
            assertEquals("username format is invalid!", result.message());
        }
    }

    @Test
    void changeUsername_alreadyTaken() {
        when(mockApp.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getUsername()).thenReturn("current");
        when(mockApp.getUserByUsername("taken")).thenReturn(mock(User.class));

        try (MockedStatic<App> mockedApp = mockStatic(App.class);
             MockedStatic<LoginMenuController> mockedLogin = mockStatic(LoginMenuController.class)) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            mockedLogin.when(() -> LoginMenuController.isValidUsername("taken")).thenReturn(true);

            Result result = controller.changeUsername("taken");
            assertFalse(result.isSuccessful());
            assertEquals("this username is already taken!", result.message());
        }
    }

    @Test
    void changeUsername_successful() {
        when(mockApp.getLoggedInUser()).thenReturn(mockUser);
        when(mockUser.getUsername()).thenReturn("old");
        when(mockApp.getUserByUsername("new")).thenReturn(null);
        when(mockApp.getUsers()).thenReturn(new ArrayList<>());

        try (MockedStatic<App> mockedApp = mockStatic(App.class);
             MockedStatic<LoginMenuController> mockedLogin = mockStatic(LoginMenuController.class);
             MockedStatic<UserDatabase> mockedDB = mockStatic(UserDatabase.class)) {

            mockedApp.when(App::getInstance).thenReturn(mockApp);
            mockedLogin.when(() -> LoginMenuController.isValidUsername("new")).thenReturn(true);

            Result result = controller.changeUsername("new");
            assertTrue(result.isSuccessful());
            assertEquals("username changed successfully!", result.message());
            verify(mockUser).setUsername("new");
          //  mockedDB.verify(() -> UserDatabase.saveUsers(isA(ArrayList.class)));
        }
    }
    @Test
    void changeEmail_shouldFail_whenUserIsNull() {
        try (MockedStatic<App> mockedApp = mockStatic(App.class)) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            when(mockApp.getLoggedInUser()).thenReturn(null);

            Result result = controller.changeEmail("new@example.com");

            assertFalse(result.isSuccessful());
            assertEquals("please login first!", result.message());
        }
    }

    @Test
    void changeEmail_shouldFail_whenEmailIsSameAsCurrent() {
        try (MockedStatic<App> mockedApp = mockStatic(App.class)) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            when(mockApp.getLoggedInUser()).thenReturn(mockUser);
            when(mockUser.getEmail()).thenReturn("same@example.com");

            Result result = controller.changeEmail("same@example.com");

            assertFalse(result.isSuccessful());
            assertEquals("new email is the same as current one!", result.message());
        }
    }

    @Test
    void changeEmail_shouldFail_whenEmailFormatInvalid() {
        try (
                MockedStatic<App> mockedApp = mockStatic(App.class);
                MockedStatic<LoginMenuController> mockedLogin = mockStatic(LoginMenuController.class)
        ) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            when(mockApp.getLoggedInUser()).thenReturn(mockUser);
            when(mockUser.getEmail()).thenReturn("old@example.com");

            mockedLogin.when(() -> LoginMenuController.isValidEmail("bad-email")).thenReturn(false);

            Result result = controller.changeEmail("bad-email");

            assertFalse(result.isSuccessful());
            assertEquals("email format is invalid!", result.message());
        }
    }

    @Test
    void changeEmail_shouldSucceed_whenValidAndDifferent() {
        try (
                MockedStatic<App> mockedApp = mockStatic(App.class);
                MockedStatic<LoginMenuController> mockedLogin = mockStatic(LoginMenuController.class);
                MockedStatic<UserDatabase> mockedDB = mockStatic(UserDatabase.class)
        ) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            when(mockApp.getLoggedInUser()).thenReturn(mockUser);
            when(mockUser.getEmail()).thenReturn("old@example.com");
            when(mockApp.getUsers()).thenReturn(new ArrayList<>());

            mockedLogin.when(() -> LoginMenuController.isValidEmail("new@example.com")).thenReturn(true);

            Result result = controller.changeEmail("new@example.com");

            assertTrue(result.isSuccessful());
            assertEquals("email changed successfully!", result.message());
            verify(mockUser).setEmail("new@example.com");
          // mockedDB.verify(() -> UserDatabase.saveUsers(any()));
        }
    }


    @Test
    void changePassword_shouldFail_whenUserIsNull() {
        try (MockedStatic<App> mockedApp = mockStatic(App.class)) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            when(mockApp.getLoggedInUser()).thenReturn(null);

            Result result = controller.changePassword("NewPass123!", "OldPass123!");

            assertFalse(result.isSuccessful());
            assertEquals("please login first!", result.message());
        }
    }

    @Test
    void changePassword_shouldFail_whenOldPasswordIncorrect() {
        try (
                MockedStatic<App> mockedApp = mockStatic(App.class);
                MockedStatic<LoginMenuController> mockedLogin = mockStatic(LoginMenuController.class)
        ) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            when(mockApp.getLoggedInUser()).thenReturn(mockUser);

            mockedLogin.when(() -> LoginMenuController.hashSHA256("wrongOld")).thenReturn("hashedWrong");
            when(mockUser.getPassword()).thenReturn("hashedCorrect");

            Result result = controller.changePassword("NewPass123!", "wrongOld");

            assertFalse(result.isSuccessful());
            assertEquals("current password is incorrect!", result.message());
        }
    }

    @Test
    void changePassword_shouldFail_whenOldAndNewPasswordAreSame() {
        try (
                MockedStatic<App> mockedApp = mockStatic(App.class);
                MockedStatic<LoginMenuController> mockedLogin = mockStatic(LoginMenuController.class)
        ) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            when(mockApp.getLoggedInUser()).thenReturn(mockUser);

            mockedLogin.when(() -> LoginMenuController.hashSHA256("SamePass123!")).thenReturn("hashedSame");
            when(mockUser.getPassword()).thenReturn("hashedSame");

            Result result = controller.changePassword("SamePass123!", "SamePass123!");

            assertFalse(result.isSuccessful());
            assertEquals("new password is the same as current one!", result.message());
        }
    }

    @Test
    void changePassword_shouldFail_whenPasswordIsWeak() {
        try (
                MockedStatic<App> mockedApp = mockStatic(App.class);
                MockedStatic<LoginMenuController> mockedLogin = mockStatic(LoginMenuController.class)
        ) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            when(mockApp.getLoggedInUser()).thenReturn(mockUser);

            mockedLogin.when(() -> LoginMenuController.hashSHA256("OldPass123")).thenReturn("hashedOld");
            when(mockUser.getPassword()).thenReturn("hashedOld");
            mockedLogin.when(() -> LoginMenuController.isStrongPassword("weakpass")).thenReturn(false);

            Result result = controller.changePassword("weakpass", "OldPass123");

            assertFalse(result.isSuccessful());
            assertEquals("password is weak! it must contain lowercase, uppercase, digit, and special character, and be at least 8 chars", result.message());
        }
    }

    @Test
    void changePassword_shouldSucceed_whenAllValidationsPass() {
        try (
                MockedStatic<App> mockedApp = mockStatic(App.class);
                MockedStatic<LoginMenuController> mockedLogin = mockStatic(LoginMenuController.class);
                MockedStatic<UserDatabase> mockedDB = mockStatic(UserDatabase.class)
        ) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            when(mockApp.getLoggedInUser()).thenReturn(mockUser);
            when(mockApp.getUsers()).thenReturn(new ArrayList<>());

            // Hash comparison for old password
            mockedLogin.when(() -> LoginMenuController.hashSHA256("OldPass123!")).thenReturn("hashedOld");
            when(mockUser.getPassword()).thenReturn("hashedOld");

            // New password is different and strong
            mockedLogin.when(() -> LoginMenuController.isStrongPassword("NewPass123!")).thenReturn(true);
            mockedLogin.when(() -> LoginMenuController.hashSHA256("NewPass123!")).thenReturn("hashedNew");

            Result result = controller.changePassword("NewPass123!", "OldPass123!");

            assertTrue(result.isSuccessful());
            assertEquals("password changed successfully!", result.message());
            verify(mockUser).setPassword("hashedNew");
           // mockedDB.verify(() -> UserDatabase.saveUsers(any()));
        }
    }
    @Test
    void changeNickname_shouldFail_whenUserIsNull() {
        try (MockedStatic<App> mockedApp = mockStatic(App.class)) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            when(mockApp.getLoggedInUser()).thenReturn(null);

            Result result = controller.changeNickname("NewNick");

            assertFalse(result.isSuccessful());
            assertEquals("please login first!", result.message());
        }
    }

    @Test
    void changeNickname_shouldFail_whenNicknameIsSame() {
        try (MockedStatic<App> mockedApp = mockStatic(App.class)) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            when(mockApp.getLoggedInUser()).thenReturn(mockUser);
            when(mockUser.getNickname()).thenReturn("SameNick");

            Result result = controller.changeNickname("SameNick");

            assertFalse(result.isSuccessful());
            assertEquals("new nickname is the same as current one!", result.message());
        }
    }

    @Test
    void changeNickname_shouldSucceed_whenNicknameIsDifferent() {
        try (
                MockedStatic<App> mockedApp = mockStatic(App.class);
                MockedStatic<UserDatabase> mockedDB = mockStatic(UserDatabase.class)
        ) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            when(mockApp.getLoggedInUser()).thenReturn(mockUser);
            when(mockUser.getNickname()).thenReturn("OldNick");
            when(mockApp.getUsers()).thenReturn(new ArrayList<>());

            Result result = controller.changeNickname("NewNick");

            assertTrue(result.isSuccessful());
            assertEquals("nickname changed successfully!", result.message());
            verify(mockUser).setNickname("NewNick");
           // mockedDB.verify(() -> UserDatabase.saveUsers(any()));
        }
    }

    @Test
    void showUserInfo_shouldFail_whenUserIsNull() {
        try (MockedStatic<App> mockedApp = mockStatic(App.class)) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            when(mockApp.getLoggedInUser()).thenReturn(null);

            Result result = controller.showUserInfo();

            assertFalse(result.isSuccessful());
            assertEquals("please login first!", result.message());
        }
    }

    @Test
    void showUserInfo_shouldSucceed_whenUserIsLoggedIn() {
        try (MockedStatic<App> mockedApp = mockStatic(App.class)) {
            mockedApp.when(App::getInstance).thenReturn(mockApp);
            when(mockApp.getLoggedInUser()).thenReturn(mockUser);

            // Mocking user info
            when(mockUser.getUsername()).thenReturn("testUser");
            when(mockUser.getEmail()).thenReturn("test@example.com");
            when(mockUser.getNickname()).thenReturn("Tester");
            when(mockUser.getMaxMoneyInGames()).thenReturn(10000);
            when(mockUser.getPlayedGames()).thenReturn(5);

            Result result = controller.showUserInfo();

            assertTrue(result.isSuccessful());
            String expected = """
                    username: testUser
                    email: test@example.com
                    nickname: Tester
                    Max money in a game: 10000
                    played games: 5
                    """;
            assertEquals(expected, result.message());
        }
    }
}

    // Repeat similarly for changeEmail, changePassword, changeNickname, showUserInfo...


