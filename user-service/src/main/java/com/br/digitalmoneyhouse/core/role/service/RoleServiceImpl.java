package com.br.digitalmoneyhouse.core.role.service;

import com.br.digitalmoneyhouse.core.exceptions.NotFoundException;
import com.br.digitalmoneyhouse.core.exceptions.NotUniqueException;
import com.br.digitalmoneyhouse.core.role.model.RoleEntity;
import com.br.digitalmoneyhouse.core.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The type Role service.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    /**
     * If the role name is unique, save the role.
     *
     * @param role The role object that is being saved.
     * @return A Role object
     */
    public RoleEntity save(RoleEntity role) {
        checkUniqueRole(role.getName());
        role.setId(null);
        log.info("Saving role [" + role.getName() + "].");
        return roleRepository.save(role);
    }

    /**
     * If the role id is null or the role doesn't exist, throw an exception. Otherwise, check if the role name is unique
     * and save the role
     *
     * @param role The role object that is being updated.
     * @return The role object is being returned.
     */
    @Override
    public RoleEntity update(RoleEntity role) {
        if(role.getId() == null || !roleRepository.existsById(role.getId())){
            throw new NotFoundException("Role id not provided or not found.");
        }
        checkUniqueRole(role.getName());
        log.info("Updating role of id [" + role.getId() + "].");
        return roleRepository.save(role);
    }

    /**
     * If the role exists, delete it.
     *
     * @param id The id of the role to be deleted.
     */
    @Override
    public void deleteById(Long id) {
        findById(id);
        log.info("Deleting role of id [" + id + "].");
        roleRepository.deleteById(id);
    }

    /**
     * If the role exists, return it, otherwise throw an exception.
     *
     * @param id The id of the role to be found.
     * @return A Role object.
     */
    @Override
    public RoleEntity findById(Long id) {
        Optional<RoleEntity> role = roleRepository.findById(id);
        log.info("Finding role of id [" + id + "].");
        return role.orElseThrow(() -> {
            log.error("Role not found.");
            throw new NotFoundException("Role not found.");
        });
    }

    /**
     * Find a role by name, or throw a NotFoundException if it doesn't exist.
     *
     * @param name The name of the role.
     * @return A Role object.
     */
    @Override
    public RoleEntity findByName(String name) {
        Optional<RoleEntity> role = roleRepository.findByName(name);
        log.info("Finding role [" + name + "].");
        return role.orElseThrow(() -> {
            log.error("Role not found.");
            throw new NotFoundException("Role not found.");
        });
    }

    /**
     * > Find all roles in the database and return them in a list. If no roles are found, throw a NotFoundException
     *
     * @return A list of all the roles in the database.
     */
    @Override
    public List<RoleEntity> findAll() {
        List<RoleEntity> roleList = roleRepository.findAll();
        if(roleList.isEmpty()){
            log.error("Role not found.");
            throw new NotFoundException("No Role found");
        }
        log.info("Finding list of roles.");
        return roleList;
    }

    /**
     * If a role with the same name already exists, throw a NotUniqueException
     *
     * @param roleName The name of the role to be created.
     */
    private void checkUniqueRole(String roleName){
        Optional<RoleEntity> exintingRole = roleRepository.findByName(roleName);
        if(exintingRole.isPresent()){
            log.error("Role [" + roleName + "] already exists.");
            throw new NotUniqueException("Role name already exists.");
        }
    }

}
