package dev.bbzblit.m426;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bbzblit.m426.repository.SessionRepository;
import dev.bbzblit.m426.repository.UserRepository;
import dev.bbzblit.m426.service.SessionService;
import dev.bbzblit.m426.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
         "spring.datasource.url=jdbc:mariadb://${MARIADB_SERVER:localhost}:${MARIADB_SERVER_PORT:3306}/m426_test" +
                 "?createDatabaseIfNotExist=true"
})
@AutoConfigureMockMvc
public class ParentTest {

    @Autowired
    public MockMvc mvc;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public SessionRepository sessionRepository;

    @Autowired
    public UserService userService;

    @Autowired
    public SessionService sessionService;

    //DON'T USE THE @Autowired ANNOTATION IT WILL BREAK THE TESTS!
    //THE ObjectMapper SHOULD BE CONFIGURED IN A LOCAL ENVIRONMENT!
    public ObjectMapper localObjectMapper = new ObjectMapper();

    @Autowired
    public ObjectMapper objectMapper;
    @BeforeEach
    @AfterEach
    public void init(){
        this.localObjectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        this.sessionRepository.deleteAll();
        this.userRepository.deleteAll();
    }

}
