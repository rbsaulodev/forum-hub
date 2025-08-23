package br.com.rb.api.controller;

import br.com.rb.api.application.dto.role.CreateRoleDTO;
import br.com.rb.api.application.dto.role.RoleDetailsDTO;
import br.com.rb.api.application.dto.role.UpdateRoleDTO;
import br.com.rb.api.application.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleDetailsDTO> create(@RequestBody @Valid CreateRoleDTO dto, UriComponentsBuilder uriBuilder){
        RoleDetailsDTO newRole = roleService.create(dto);
        var uri = uriBuilder.path("/roles/{id}").buildAndExpand(newRole.id()).toUri();
        return ResponseEntity.created(uri).body(newRole);
    }

    @GetMapping
    public ResponseEntity<List<RoleDetailsDTO>> list() {
        List<RoleDetailsDTO> list = roleService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDetailsDTO> detail(@PathVariable Long id) {
        RoleDetailsDTO role = roleService.findById(id);
        return ResponseEntity.ok(role);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDetailsDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateRoleDTO dto) {
        RoleDetailsDTO updatedRole = roleService.update(id, dto);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}