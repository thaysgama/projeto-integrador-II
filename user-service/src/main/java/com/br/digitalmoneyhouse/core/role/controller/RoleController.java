package com.br.digitalmoneyhouse.core.role.controller;

import com.br.digitalmoneyhouse.core.role.model.RoleEntity;
import com.br.digitalmoneyhouse.core.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Role controller.
 */
@RestController
@RequestMapping("/api/v1/role")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * Find all response entity.
     *
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity<List<RoleEntity>> findAll(){
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }

    /**
     * Find by id response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleEntity> findById(@PathVariable Long id){
        return new ResponseEntity<>(roleService.findById(id), HttpStatus.OK);
    }

    /**
     * Save response entity.
     *
     * @param role the role
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<RoleEntity> save(@RequestBody RoleEntity role){
        return new ResponseEntity<>(roleService.save(role), HttpStatus.CREATED);
    }

    /**
     * Update response entity.
     *
     * @param role the role
     * @return the response entity
     */
    @PutMapping
    public ResponseEntity<RoleEntity> update(@RequestBody RoleEntity role){
        return new ResponseEntity<>(roleService.update(role), HttpStatus.CREATED);
    }

    /**
     * Delete by id response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RoleEntity> deleteById(@PathVariable Long id){
        roleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
