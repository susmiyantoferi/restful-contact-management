package projectjava.belajar.resfullapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import projectjava.belajar.resfullapi.entity.User;
import projectjava.belajar.resfullapi.model.LoginUserRequest;
import projectjava.belajar.resfullapi.model.TokenResponse;
import projectjava.belajar.resfullapi.model.WebResponse;
import projectjava.belajar.resfullapi.repository.UserRepository;
import projectjava.belajar.resfullapi.security.BCrypt;
import projectjava.belajar.resfullapi.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;


@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testFilledUserNotFound() throws Exception {

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("test");
        request.setPassword("test");


        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });

    }

    @Test
    void testFilledWrongPassword() throws Exception {

        User user = new User();
        user.setName("test");
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("test");
        request.setPassword("test");


        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertNotNull(response.getErrors());
        });

    }


    @Test
    void testLoginSuccess() throws Exception {

        User user = new User();
        user.setName("test");
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("test");
        request.setPassword("rahasia");


        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

            assertNull(response.getErrors());
            assertNotNull(response.getData().getToken());
            assertNotNull(response.getData().getExpiredAt());

            User testDb = userRepository.findById("test").orElse(null);
            assertNotNull(testDb);
            assertEquals(testDb.getToken(), response.getData().getToken());
            assertEquals(testDb.getTokenExpiredAt(), response.getData().getExpiredAt());
        });

    }
}