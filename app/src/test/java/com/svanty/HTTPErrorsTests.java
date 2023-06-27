package com.svanty;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HTTPErrorsTests {

    @Autowired
    WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void notFoundPageTestWithNoCredentials() throws Exception {
        mvc.perform(get("/en/WERfsdfowersdfasdfwexwe")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(value = "spring", roles = {"ADMIN", "USER"})
    public void notFoundPageTestWithCredentials() throws Exception {
        mvc.perform(get("/en/WERfsdfowersdfasdfwexwedsdsdsdssdsdds")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isNotFound());
    }



}
