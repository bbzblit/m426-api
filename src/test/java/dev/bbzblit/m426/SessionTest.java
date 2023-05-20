package dev.bbzblit.m426;


import dev.bbzblit.m426.entity.Session;
import dev.bbzblit.m426.entity.User;
import dev.bbzblit.m426.entity.dto.LoginModel;
import dev.bbzblit.m426.helper.RandomString;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SessionTest extends ParentTest {

    private User testUser;

    @BeforeEach
    public void userTestInit() {

        User user = new User();
        user.setPassword("Password.123");
        user.setEmail("testing@bbzbl-it.dev");
        user.setFirstname("Max");
        user.setLastname("Mustermann");
        user.setUsername("TestUser");

        this.testUser = this.userService.registerUser(user);
    }

    @Test
    public void testLogin() throws Exception {
        String json = this.mvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                this.objectMapper.writeValueAsString(
                                        new LoginModel("TestUser", "Password.123")
                                )))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Session session = this.objectMapper.readValue(json, Session.class);
        assertEquals(this.sessionService.getSessionByToken(session.getToken()).getUser(), this.testUser);
    }

    @Test
    public void testLoginWithWrongPassword() throws Exception{
        String json = this.mvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                this.objectMapper.writeValueAsString(
                                        new LoginModel("TestUser", "WrongPassword")
                                )))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();


        assertTrue(json.contains("Wrong Password or Username"),
                "Expected to contain error message " +
                        "'Wrong Password or Username: " + json);
    }

    @Test
    public void testLogout() throws Exception{

        Session session = new Session();
        session.setUser(this.testUser);
        session.setToken(new RandomString().nextString());
        session.setExpirationDate(LocalDateTime.now().plusHours(2));

        session = this.sessionRepository.save(session);


        this.mvc.perform(post("/api/v1/logout")
                        .cookie(new Cookie("session", session.getToken())))
                .andExpect(status().isOk());

        assertTrue(sessionRepository.findById(session.getToken()).isEmpty(),
                "Session didn't get deleted from repository");
    }

    @Test
    public void testInvalidLogout() throws Exception{
        String json = this.mvc.perform(post("/api/v1/logout")
                        .cookie(new Cookie("session", "ThisIsAnInvalidSession")))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();

        assertTrue(json.contains("Your currently not logged in or your session is expired"),
                "Expected to contain error message " +
                        "'Your currently not logged in or your session is expired' but got: " + json);
    }

    @Test
    public void testInvalidLogoutWithoutCookie() throws Exception{
        String json = this.mvc.perform(post("/api/v1/logout"))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();

        assertTrue(json.contains("You have to be logged in to connect to this endpoint"),
                "Expected to contain error message " +
                        "'You have to be logged in to connect to this endpoint' but got: " + json);
    }

}
