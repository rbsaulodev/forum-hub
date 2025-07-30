package br.com.rb.api.interfaces.controller;

import br.com.rb.api.application.dto.user.UpdateUserDTO;
import br.com.rb.api.application.dto.user.UserDetailsDTO;
import br.com.rb.api.application.service.UserService;
import br.com.rb.api.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDetailsDTO>> list(@PageableDefault(size = 10, sort = {"name"}) Pageable pageable) {
        Page<UserDetailsDTO> page = userService.findAll(pageable).map(UserDetailsDTO::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> detail(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(new UserDetailsDTO(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateUserDTO dto) {
        User updatedUser = userService.update(id, dto);
        return ResponseEntity.ok(new UserDetailsDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}