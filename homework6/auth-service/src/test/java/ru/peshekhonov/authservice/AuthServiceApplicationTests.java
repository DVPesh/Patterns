package ru.peshekhonov.authservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.peshekhonov.authservice.utils.JwtTokenUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void securityTokenTest() throws Exception {
        String jsonRequest = """
                {
                    "username": "Dima Petrov",
                    "password": "100"
                }
                """;
        MvcResult result = mockMvc.perform(
                        post("/auth")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String token = result.getResponse().getContentAsString();
        token = token.split("\",\"", 2)[0].replace("{\"token\":\"", "");

        Assertions.assertThat(jwtTokenUtil.getUsernameFromToken(token))
                .isEqualTo("Dima Petrov");

        Assertions.assertThat(jwtTokenUtil.getRoles(token))
                .hasSize(2)
                .contains("ROLE_USER", "ROLE_ADMIN");

        jsonRequest = """
                {
                    "username": "Dima Petrov",
                    "password": "200"
                }
                """;
        mockMvc.perform(
                        post("/auth")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
