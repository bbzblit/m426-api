package dev.bbzblit.m426;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bbzblit.m426.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserTest extends ParentTest {

    private User testUser;


    @BeforeEach
    public void userInit() {
        User user = new User();
        user.setPassword("Password.123");
        user.setEmail("testing@bbzbl-it.dev");
        user.setFirstname("Max");
        user.setLastname("Mustermann");
        user.setUsername("TestUser");

        this.testUser = user;
    }

    @Test
    public void testRegistration() throws Exception {

        System.out.println(this.testUser.getPassword() + " ><<<<<<<<<<<<<<<<<<<");
        String json = this.mvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.localObjectMapper.writeValueAsString(this.testUser)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        User resultUser = this.objectMapper.readValue(json, User.class);

        assertEquals(resultUser.getUsername(), this.testUser.getUsername(), "Username should be equals");
        assertNull(resultUser.getPassword(), "Password should not be present in response");
        assertNull(resultUser.getSaltValue(), "Salt value should not be present in response");
        assertFalse(resultUser.isEmployee(), "Should not be able to set Employee flag on registration");
    }

    @Test
    public void testDublicatedRegistration() throws Exception {

        this.userService.registerUser(this.testUser);


        String json = this.mvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(this.testUser)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertTrue(json.contains("There is already a User with the username TestUser"),
                "Message should contain phrase 'There is already a User with the username' but got: " + json);
    }


}
