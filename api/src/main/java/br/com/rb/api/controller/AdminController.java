package br.com.rb.api.controller;

import br.com.rb.api.application.dto.user.AdminCreateUserDTO;
import br.com.rb.api.application.dto.user.UserDetailsDTO;
import br.com.rb.api.application.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDetailsDTO> createUserByAdmin(@RequestBody @Valid AdminCreateUserDTO dto, UriComponentsBuilder uriBuilder) {
        UserDetailsDTO newUserDto = userService.createUserByAdmin(dto);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(newUserDto.id()).toUri();
        return ResponseEntity.created(uri).body(newUserDto);
    }
}