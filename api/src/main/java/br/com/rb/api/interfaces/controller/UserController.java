package br.com.rb.api.interfaces.controller;

import br.com.rb.api.application.dto.user.AdminCreateUserDTO;
import br.com.rb.api.application.dto.user.CreateUserDTO;
import br.com.rb.api.application.dto.user.UserDetailsDTO;
import br.com.rb.api.application.dto.user.UpdateUserDTO;
import br.com.rb.api.application.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDetailsDTO> register(@RequestBody @Valid CreateUserDTO dto, UriComponentsBuilder uriBuilder) {
        UserDetailsDTO newUser = userService.registerStudent(dto);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(newUser.id()).toUri();
        return ResponseEntity.created(uri).body(newUser);
    }

    @PostMapping("/admin")
    public ResponseEntity<UserDetailsDTO> createUserByAdmin(@RequestBody @Valid AdminCreateUserDTO dto, UriComponentsBuilder uriBuilder) {
        UserDetailsDTO newUser = userService.createUserByAdmin(dto);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(newUser.id()).toUri();
        return ResponseEntity.created(uri).body(newUser);
    }

    @GetMapping
    public ResponseEntity<Page<UserDetailsDTO>> list(@PageableDefault(size = 10, sort = {"name"}) Pageable pageable) {
        Page<UserDetailsDTO> page = userService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> detail(@PathVariable Long id) {
        UserDetailsDTO user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateUserDTO dto) {
        UserDetailsDTO updatedUser = userService.update(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}