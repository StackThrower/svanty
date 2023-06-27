package com.svanty;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringSecurityTests {

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
    public void AdminMembersNotAuthorizedUser() throws Exception {
        mvc.perform(get("/en/panel/admin/members")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "spring")
    public void AdminMembersAuthorizedUserWithNoRoleAdmin() throws Exception {
        mvc.perform(get("/en/panel/admin/members")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "spring", roles = {"ADMIN", "USER"})
    public void AdminMembersAuthorizedUser() throws Exception {
        mvc.perform(get("/en/panel/admin/members")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(value = "spring")
    public void AdminPanelAuthorizedUserNegative() throws Exception {
        mvc.perform(get("/ru/panel/admin/")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser("spring")
    public void indexAuthorized() throws Exception {
        mvc.perform(get("/ru/")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @Test
    public void givenAuthRequestOnPrivateService_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/ru/")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }


    // AUTH
    @Test
    public void signinUnautorizedUser() throws Exception {
        mvc.perform(get("/en/signin")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @Test
    public void signUpUnautorizedUser() throws Exception {
        mvc.perform(get("/en/signup")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @Test
    public void passwordResetUnautorizedUser() throws Exception {
        mvc.perform(get("/en/password/reset")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @Test
    public void logoutUnautorizedUser() throws Exception {
        mvc.perform(get("/en/logout")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection());
    }
    // AUTH
}
