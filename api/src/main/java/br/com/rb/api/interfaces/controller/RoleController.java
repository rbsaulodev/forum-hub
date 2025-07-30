package br.com.rb.api.interfaces.controller;

import br.com.rb.api.application.dto.role.CreateRoleDTO;
import br.com.rb.api.application.dto.role.RoleDetailsDTO;
import br.com.rb.api.application.service.RoleService;
import br.com.rb.api.domain.model.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
}
