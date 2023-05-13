package com.br.digitalmoneyhouse.core.role.service;

import com.br.digitalmoneyhouse.core.role.model.RoleEntity;

import java.util.List;

public interface RoleService {
    RoleEntity save(RoleEntity role);
    RoleEntity update(RoleEntity role);
    void deleteById(Long id);
    RoleEntity findById(Long id);
    RoleEntity findByName(String name);
    List<RoleEntity> findAll();
}
