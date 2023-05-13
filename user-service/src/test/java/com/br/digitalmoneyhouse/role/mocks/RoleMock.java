package com.br.digitalmoneyhouse.role.mocks;

import com.br.digitalmoneyhouse.core.role.model.RoleEntity;

import java.util.Arrays;
import java.util.List;

public interface RoleMock {

    static List<RoleEntity> getListOfRoleEntity() {
        return Arrays.asList(getRoleEntityAdmin(), getRoleEntityClient());
    }

    static RoleEntity getRoleEntityAdmin() {
       return new RoleEntity(1L, "ROLE_ADMIN");
    }

    static RoleEntity getRoleEntityClient() {
        return new RoleEntity(2L, "ROLE_CLIENT");
    }

    
}
