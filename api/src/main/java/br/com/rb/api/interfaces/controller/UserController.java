package br.com.rb.api.interfaces.controller;

import br.com.rb.api.application.dto.user.AdminCreateUserDTO;
import br.com.rb.api.application.dto.user.CreateUserDTO;
import br.com.rb.api.application.dto.user.UserDetailsDTO;
import br.com.rb.api.application.service.UserService;
import br.com.rb.api.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/register")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDetailsDTO> registerStudent(@RequestBody @Valid CreateUserDTO dto, UriComponentsBuilder uriBuilder) {
        User newUser = userService.registerStudent(dto);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserDetailsDTO(newUser));
    }


    @PostMapping("/admin/users")
    public ResponseEntity<UserDetailsDTO> createUserByAdmin(@RequestBody @Valid AdminCreateUserDTO dto, UriComponentsBuilder uriBuilder) {
        User newUser = userService.createUserByAdmin(dto);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserDetailsDTO(newUser));
    }
}
