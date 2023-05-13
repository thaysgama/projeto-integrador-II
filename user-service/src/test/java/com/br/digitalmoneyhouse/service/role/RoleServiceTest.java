package com.br.digitalmoneyhouse.service.role;

import com.br.digitalmoneyhouse.core.role.model.RoleEntity;
import com.br.digitalmoneyhouse.core.role.repository.RoleRepository;
import com.br.digitalmoneyhouse.core.role.service.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RoleServiceTest {

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Test
    public void givenValidId_whenFindById_thenReturnUserRole() {
        // Arrange
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("USER");
        when(roleRepository.findById(any())).thenReturn(Optional.of(roleEntity));

        // Act, Assert
        assertEquals(
                roleService.findById(1L).getName(),
                "USER"
        );
    }
}
