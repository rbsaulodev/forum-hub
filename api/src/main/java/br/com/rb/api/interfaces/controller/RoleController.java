package br.com.rb.api.interfaces.controller;

import br.com.rb.api.application.dto.role.CreateRoleDTO;
import br.com.rb.api.application.dto.role.RoleDetailsDTO;
import br.com.rb.api.application.dto.role.UpdateRoleDTO;
import br.com.rb.api.application.service.RoleService;
import br.com.rb.api.domain.model.Role;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity create(CreateRoleDTO dto, UriComponentsBuilder uriBuilder){
        Role newRole = roleService.create(dto);
        RoleDetailsDTO detailsDTO = new RoleDetailsDTO(newRole);

        var uri = uriBuilder.path("/roles/${id}").buildAndExpand(newRole.getId()).toUri();
        return ResponseEntity.created(uri).body(detailsDTO);
    }

    @GetMapping
    public ResponseEntity<List<RoleDetailsDTO>> list() {
        List<RoleDetailsDTO> list = roleService.findAll().stream().map(RoleDetailsDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDetailsDTO> detail(@PathVariable Long id) {
        Role role = roleService.findById(id);
        return ResponseEntity.ok(new RoleDetailsDTO(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDetailsDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateRoleDTO dto) {
        Role updatedRole = roleService.update(id, dto);
        return ResponseEntity.ok(new RoleDetailsDTO(updatedRole));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
