package com.br.digitalmoneyhouse.role.controller;


import com.br.digitalmoneyhouse.core.exceptions.NotFoundException;
import com.br.digitalmoneyhouse.core.role.model.RoleEntity;
import com.br.digitalmoneyhouse.core.role.service.RoleService;
import com.br.digitalmoneyhouse.role.mocks.RoleMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = {"ADMIN"})
public class RoleControllerTest {

    private final String URL = "/api/v1/role";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @Test
    public void whenCallFindAllShouldReturnOkWithBody() throws Exception {
        List<RoleEntity> roleList = RoleMock.getListOfRoleEntity();

        when(roleService.findAll()).thenReturn(roleList);

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser()
    public void whenCallFindAllShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/role"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenFindByIdWithValidIdShouldReturnOkWithBody() throws Exception {
         RoleEntity role = RoleMock.getRoleEntityAdmin();

         when(roleService.findById(anyLong())).thenReturn(role);

         mockMvc.perform(get(URL + "/" + 1L))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.name", is(role.getName())));
    }

    @Test
    public void whenFindByIdWithInvalidIdShouldReturnNotFoundException() throws Exception {
        when(roleService.findById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get(URL + "/" + 99L))
                .andExpect(status().isNotFound());
    }
}
